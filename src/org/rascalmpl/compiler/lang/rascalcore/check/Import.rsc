@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
@bootstrapParser
module lang::rascalcore::check::Import

/*
    Check imports, and read/write TPL files.
*/

extend lang::rascalcore::check::CheckerCommon;

import lang::rascalcore::check::RascalConfig;
import lang::rascalcore::check::ADTandGrammar;
import lang::rascalcore::compile::CompileTimeError;
import lang::rascalcore::check::ModuleLocations;

import DateTime;
import IO;
import List;
import ListRelation;
import Location;
import Map;
import Set;
import Relation;
import String;

import analysis::graphs::Graph;
import util::Reflective;
import util::Benchmark;
import lang::rascalcore::compile::util::Names; // TODO: refactor, this is an undesired dependency on compile

ModuleStatus reportSelfImport(rel[loc, PathRole, loc] paths, ModuleStatus ms){
    for(<from, importPath(), from> <- paths){
       // mname = getRascalModuleName(from, ms.pathConfig);
        ms.messages[from] ? {} += {error("Self import not allowed", from)};
        ms.status[from] ? {} += {check_error()};
    }
    return ms;
}

ModuleStatus reportCycles(rel[MODID, PathRole, MODID]paths, rel[MODID,MODID] extendPlus, ModuleStatus ms){
    extendCycle = { m | <m, m> <- extendPlus };
    if(size(extendCycle) > 0){
        for(mid <- extendCycle){
            mname = getRascalModuleName(mid, ms.pathConfig);
            causes = [ info("Part of extend cycle <getRascalModuleName(emid, ms.pathConfig)>", emid) | emid <- extendCycle ];
            ms.messages[mid] ? {} += {error("Extend cycle not allowed", mid, causes=causes)};
            ms.status[mid] ? {} += {check_error()};
        }
    }       
    pathsPlus = {<from, to> | <from, _, to> <- paths}+;

    cyclicMixed = { mid1, mid2 
                  | <mid1, mid2> <- pathsPlus, mid1 != mid2,
                    (  <mid1, importPath(), mid2> in paths && <mid2, extendPath(), mid1> in paths
                    || <mid1, extendPath(), mid2> in paths && <mid2, importPath(), mid1> in paths
                    )
                  };

    for(mid <- cyclicMixed){
        set[loc] cycle = { mid2 |  <mid1, mid2> <- pathsPlus, mid1 == mid, mid2 in cyclicMixed } +
                         { mid1 |  <mid1, mid2> <- pathsPlus, mid2 == mid , mid1 in cyclicMixed };
        if(size(cycle) > 0){
            causes = [ info("Part of mixed import/extend cycle <getRascalModuleName(eloc, ms.pathConfig)>", eloc) | eloc <- cycle ];

            ms.messages[mid] ? {} += { error("Mixed import/extend cycle not allowed", mid, causes=causes) };
            ms.status[mid] ? {} += {check_error()};
        }
    }
    return ms;
}

// Complete a ModuleStatus 
//- by adding transitive edges for extend paths
//- by checking circular dependencies
// TODO: reuse enhancePathRelation from RascalConfig here
ModuleStatus completeModuleStatus(ModuleStatus ms){
    pcfg = ms.pathConfig;
    // extra safeguard against physical locations (due to erroneous packager)
    paths = visit(ms.paths){
        case loc l => moduleName2moduleId(getRascalModuleName(l,pcfg)) when !isModuleId(l)
    }

    ms = reportSelfImport(paths, ms);
    
    imports = {<from, to> | <MODID from, importPath(), MODID to> <- paths};
    extendPlus = {<from, to> | <MODID from, extendPath(), MODID to> <- paths}+;
    paths += { <from, extendPath(), to> | <MODID from, MODID to> <- extendPlus };

    ms = reportCycles(paths, extendPlus, ms);

    paths += { *{<c, importPath(), a> | a <- extendPlus[b], c != a} 
             | < MODID c, MODID b> <- imports 
             };

    ms.paths = paths;                                   
    return ms;
}

ModuleStatus getImportAndExtendGraph(set[MODID] moduleIds, RascalCompilerConfig ccfg){
    return completeModuleStatus((newModuleStatus(ccfg) | getImportAndExtendGraph(moduleId, it) | moduleId <- moduleIds));
}

ModuleStatus getImportAndExtendGraph(set[MODID] moduleIds, ModuleStatus ms){
    return completeModuleStatus((ms | getImportAndExtendGraph(moduleId, it) | moduleId <- moduleIds));
}

ModuleStatus getImportAndExtendGraph(MODID moduleId, RascalCompilerConfig ccfg){
    return completeModuleStatus(getImportAndExtendGraph(moduleId, newModuleStatus(ccfg)));
}

ModuleStatus getImportAndExtendGraph(MODID moduleId, ModuleStatus ms){
    assert isModuleId(moduleId): "getImportAndExtendGraph: <moduleId>";
    pcfg = ms.pathConfig;
    qualifiedModuleName = moduleId2moduleName(moduleId);

    if(!ms.status[moduleId]?){
        ms.status[moduleId] = {};
    }

    if(module_dependencies_extracted() in ms.status[moduleId]){
        return ms;
    }
    ms.status[moduleId] += module_dependencies_extracted();

    <found, tm, ms> = getTModelForModule(moduleId, ms);
    if(found){
        allImportsAndExtendsValid = true;
        rel[loc, PathRole] localImportsAndExtends = {};

        if(!ms.moduleLastModified[moduleId]?){
            ms.moduleLastModified[moduleId] = getLastModified(moduleId, ms);
        }

        if(tm.store[key_bom]? && rel[str,datetime,PathRole] bom := tm.store[key_bom]){
           for(<str m, datetime timestampInBom, PathRole pathRole> <- bom){
               mid = moduleName2moduleId(m);
               if(!ms.status[mid]?){
                    ms.status[mid] = {};
               }
               if(mid != moduleId){
                    localImportsAndExtends += <mid, pathRole>;
               }
                    
               dependencyChanged = (mid != moduleId) && !isEmpty(ms.changedModules & range(ms.paths[mid]));
               //if(dependencyChanged) println("processing BOM of <qualifiedModuleName> and consider <m>, dependencyChanged: <dependencyChanged>");
               <mchanged, ms> = isModuleModified(mid, timestampInBom, pathRole, ms);
               if(dependencyChanged || mchanged){
                    allImportsAndExtendsValid = false;
                    ms.status[mid] += rsc_changed();
                    ms.status[mid] -= {tpl_uptodate(), checked()};
                    ms.status[moduleId] -= tpl_saved();
                    ms.messages[moduleId] = {};
                    if(ms.compilerConfig.verbose){
                        println("--- using <getLastModified(mid, ms)> (most recent) version of <m>,
                                '    older <timestampInBom> version was used in previous check of <qualifiedModuleName>");
                    }
               }
           }

        } else {
            throw "No bill-of-materials found for <moduleId>";
        }
        if(!allImportsAndExtendsValid){ // Check that the source code of qualifiedModuleName is available
            mloc = |unknown:///|(0,0,<0,0>,<0,0>);
            try {
                try {
                    mloc = getRascalModuleLocation(moduleId, ms);
                } catch Message err: {
                    ms.messages[moduleId] = { err };
                    tm = tmodel(modelName=moduleId2moduleName(moduleId), messages=[ err ]);
                    ms = addTModel(moduleId, tm, ms);
                    ms.status[moduleId] += { rsc_not_found() };
                    return ms;
                }
                if(!isRascalLogicalLoc(mloc) && (mloc.extension != "rsc" || isModuleLocationInLibs(mloc, pcfg))) throw "No src or library module 1"; //There is only a tpl file available
            } catch value _:{
                <incompatible, ms> = isCompatibleBinaryLibrary(tm, ms);
                incompatibleNames = [ getModuleNameFromAnyLogical(imod) | MODID imod <- incompatible ];
                if(!isEmpty(incompatible)){
                    causes = [ info("Module <getModuleNameFromAnyLogical(incomp)> is incompatible with <qualifiedModuleName>",  incomp) | incomp <- incompatible ];
                    txt = "Review of dependencies, reconfiguration or recompilation needed: binary module `<qualifiedModuleName>` depends (indirectly) on incompatible module(s) <intercalateAnd(incompatibleNames)>";
                    msg = error(txt, mloc, causes=causes);
                    
                    tm.messages += [msg];
                    ms.messages[moduleId] ? {} += { msg };

                    txt2 = "Review of dependencies, reconfiguration or recompilation needed: imported/extended binary module `<qualifiedModuleName>` depends (indirectly) on incompatible module(s)";

                    usingModules = {user | <user, moduleId> <- ms.paths<0,2>*};
                    for(user <- usingModules){
                            mloc = getRascalModuleLocation(user, ms);
                            if(!isModuleLocationInLibs(mloc, pcfg)){
                                msg2 = error(txt2, mloc, causes=causes);
                                ms.messages[user] ? {} += { msg2 };
                            }
                    }
                } else {
                    allImportsAndExtendsValid = true;
                    if(ms.compilerConfig.verbose){
                        println("--- reusing tmodel of <qualifiedModuleName> (source not accessible)");
                    }
                }
            }
        }
        if(allImportsAndExtendsValid){
            ms.status[moduleId] += {tpl_uptodate(), checked(), tpl_saved()}; //TODO: maybe check existence of generated java files
            ms.moduleLocs += (moduleName2moduleId(mname) : tm.moduleLocs[mname] | mname <- tm.moduleLocs); // TODO: or not?
            ms.paths += tm.paths;
            ms.status[moduleId] += module_dependencies_extracted();
            ms.messages[moduleId] ? {} += toSet(tm.messages);
            for(<imp, _> <- localImportsAndExtends, isEmpty({module_dependencies_extracted()} & ms.status[imp])  ){
                ms.status[imp] -= tpl_saved();
                ms = getImportAndExtendGraph(imp, ms);
            }
            return completeModuleStatus(ms);
         }
    }

    if(rsc_not_found() in ms.status[moduleId]){
        return ms;
    }

    <success, pt, ms> = getModuleParseTree(moduleId, ms);
    if(success){
        <ms, imports_and_extends> = getModulePaths(pt, ms);

        for(<_, _, imp> <- imports_and_extends, rsc_not_found() notin ms.status[imp]){
            ms = getImportAndExtendGraph(imp, ms);
        }
    } else {
         ms.status[moduleId] += rsc_not_found();
    }

    return completeModuleStatus(ms);
}

ModuleStatus getInlineImportAndExtendGraph(Tree pt, RascalCompilerConfig ccfg){
    ms = newModuleStatus(ccfg);
    visit(pt){
        case  Module m: {
            header = m.header;
            body = m.body;
            qualifiedModuleName = prettyPrintName(header.name);
            ms.moduleLocs[moduleName2moduleId(qualifiedModuleName)] = getLoc(m);
            <ms, imports_and_extends> = getModulePaths(m, ms);
        }
    }
    return completeModuleStatus(ms);
}

// Is binary library module compatible with its dependencies (originating from imports and extends)?
tuple[list[MODID], ModuleStatus] isCompatibleBinaryLibrary(TModel lib, ModuleStatus ms){
    libName = lib.modelName;
    set[loc] libLogical = domain(lib.logical2physical);
    set[loc] libDefines = { l | l <- libLogical, getModuleNameFromAnyLogical(l) == libName };
    set[loc] libDependsOn = libLogical - libDefines;
    set[str] libDependsOnModules = { getModuleNameFromAnyLogical(l) | l <- libDependsOn };
    set[loc] dependentsProvide = {};
    for(m <- libDependsOnModules){
       <found, tm, ms> = getTModelForModule(m, ms);
       if(found){
           dependentsProvide += domain(tm.logical2physical);
       }
    }

    unsatisfied = libDependsOn - dependentsProvide;
    
    if(isEmpty(unsatisfied)){
        //println("isCompatibleBinaryLibrary <libName>: satisfied");
        return <[], ms>;
    } else {
        // println("BOM of <lib.modelName>:"); iprintln(lib.store[key_bom]);
        // println("libDependsOn: <size(libDependsOn)>
        //         'dependentsProvide: <size(dependentsProvide)>
        //         'unstatisfied: <size(unsatisfied)>
        //         ");
        // println("isCompatibleBinaryLibrary, <libName> unsatisfied (<unsatisfied>)");
        
        //incompatibleModuleNames = { moduleId2moduleName(u) | u <- unsatisfied };
        return <sort(unsatisfied), ms>;
    }
}

// Example: |rascal+function:///util/Math/round$d80e373d64c01979| ==> util::Math
// Example: |rascal+module:///lang/rascal/syntax/Rascal| -> lang::rascal::syntax::Rascal
str getModuleNameFromAnyLogical(loc l){
    i = findLast(l.path[1..], "/");
    res = (l.scheme == "rascal+module" || i < 0) ? l.path[1..] : l.path[1..i+1];
    res = replaceAll(res, "/", "::");
    //println("getModuleNameFromAnyLogical: <l> -\> <res>");
    return res;
}


tuple[bool, ModuleStatus] importsAndExtendsAreBinaryCompatible(TModel tm, set[MODID] importsAndExtends, ModuleStatus ms){
    moduleName = tm.modelName;
    physical2logical = invertUnique(tm.logical2physical);

    modRequires = { lg | l <- range(tm.useDef),
                        physical2logical[l]?, lg := physical2logical[l],
                        moduleName !:= getModuleNameFromAnyLogical(lg) };
    provided = {};
    if(!isEmpty(modRequires)){
        for(m <- importsAndExtends){
            <found, tm, ms> = getTModelForModule(m, ms);
            if(found){
                provided += domain(tm.logical2physical);
            }
        }
    }

    //println("<moduleName> requires <modRequires>");

    if(isEmpty(modRequires - provided)){
        //println("importsAndExtendsAreBinaryCompatible <moduleName>: satisfied");
        return <true, ms>;
    } else {
        //println("importsAndExtendsAreBinaryCompatible, <moduleName> unsatisfied: <modRequires - provided>");
        return <false, ms>;
    }
}

tuple[ModuleStatus, rel[loc, PathRole, loc]] getModulePaths(Module m, ModuleStatus ms){
    moduleName = unescape("<m.header.name>");
    MODID moduleId = moduleName2moduleId(moduleName);
    imports_and_extends = {};
    for(imod <- m.header.imports, imod has \module){
        iname = unescape("<imod.\module.name>");
        inameId = moduleName2moduleId(iname);
        imports_and_extends += <moduleId, imod is \default ? importPath() : extendPath(), inameId>;
        ms.status[inameId] = ms.status[inameId] ? {};
        // try {
        //    // mloc = getRascalModuleLocation(iname, ms);
        //     ms.paths += {<moduleId, imod is \default ? importPath() : extendPath(), inameId>};
        //  } catch Message err: {
        //     err.at = imod@\loc;
        //     ms.messages[moduleId] ? {} += { err };
        //     ms.status[inameId] += { rsc_not_found() };
        //  }
    }
    ms.paths += imports_and_extends;
    return <ms, imports_and_extends>;
}

// ---- Save modules ----------------------------------------------------------

map[str, loc] getModuleScopes(TModel tm)
    = (id: defined | <loc _, str id, str _orgId, moduleId(),  loc defined, DefInfo _> <- tm.defines);

// loc getModuleScope(str qualifiedModuleName, map[str, loc] moduleScopes, PathConfig pcfg){
//     if(moduleScopes[qualifiedModuleName]?){
//         return moduleScopes[qualifiedModuleName];
//     }
//     for(l <- range(moduleScopes)){
//         if(getRascalModuleName(l, pcfg) == qualifiedModuleName){
//             return l;
//         }
//     }
//     throw "No module scope found for <qualifiedModuleName>";
// }

tuple[map[MODID,TModel], ModuleStatus] prepareForCompilation(set[MODID] component, map[MODID,set[MODID]] m_imports, map[MODID,set[MODID]] m_extends, ModuleStatus ms, TModel tm){
   //map[str,TModel] tmodels = (); //ms.tmodels;
    pcfg = ms.pathConfig;

    dependencies_ok = true;
    for(m <- component, rsc_not_found() notin ms.status[m], MStatus::ignored() notin ms.status[m]){
        if(parse_error() in ms.status[m]){
            return <(m : tmodel(modelName=moduleId2moduleName(m),messages=toList(ms.messages[m]))) ,ms>;
        }
        for(imp <- m_imports[m] + m_extends[m], rsc_not_found() notin ms.status[imp], MStatus::ignored() notin ms.status[m]){
            imp_status = ms.status[imp];
            if(parse_error() in imp_status || checked() notin imp_status){
                dependencies_ok = false;
                cause = (rsc_not_found() in imp_status) ? "module not found" : "due to syntax error";
                ms.messages[m] = (ms.messages[imp] ? {}) + error("<imp in m_imports[imp] ? "Imported" : "Extended"> module <imp> could not be checked (<cause>)", m);
            }
        }
        if(!dependencies_ok){
            return <(m: tmodel(modelName=moduleId2moduleName(m),messages=toList(ms.messages[m]))), ms>;
        }
    }
    transient_tms = (m : tm | m <- component);
    org_tm = tm;
    for(MODID m <- component, rsc_not_found() notin ms.status[m], MStatus::ignored() notin ms.status[m]){
        tm = org_tm;
        mname = moduleId2moduleName(m);
        tm.modelName = mname;

        tm.definitions = ( def.defined : def | Define def <- tm.defines);

        transient_tms[m] = tm;
        <tm, ms> = addGrammar(m, m_imports[m], m_extends[m], transient_tms, ms);
        ms.messages[m] = toSet(tm.messages);
        transient_tms[m] = tm;
    }
    return <transient_tms, ms>;
}

ModuleStatus doSaveModule(set[MODID] component, map[MODID,set[MODID]] m_imports, map[MODID,set[MODID]] m_extends, ModuleStatus ms, map[MODID, TModel] transient_tms, RascalCompilerConfig compilerConfig){
    pcfg = ms.pathConfig;

    component = { m | m <- component, MStatus::ignored() notin ms.status[m] };
    if(isEmpty(component)) return ms;

    //println("doSaveModule: <component>, <m_imports>, <m_extends>, <moduleScopes>");
    component_scopes = component; //{ getModuleScope(mid, moduleScopes, pcfg) | MODID mid <- component };
    set[MODID] filteredModuleScopes = {};
    loc2moduleName = invertUnique(ms.moduleLocs);

    bool isContainedInComponentScopes(loc inner, map[loc,loc] m){
        return any(cs <- component_scopes, isContainedIn(inner, cs, m));
    };

    bool isContainedInFilteredModuleScopes(loc inner, map[loc,loc] m){
        return any(cs <- filteredModuleScopes, isContainedIn(inner, cs, m));
    };

    for(currentModule <- component){
        start_save = cpuTime();
        tm = transient_tms[currentModule];
        <found, tplLoc> = getTPLWriteLoc(currentModule, pcfg);

        imports = m_imports[currentModule];
        extends = m_extends[currentModule];

        bom = makeBom(currentModule, ms);

        extendedModuleScopes = {m | MODID m <- extends, checked() in ms.status[m]};
        extendedModuleScopes += {*tm.paths[ems,importPath()] | MODID ems <- extendedModuleScopes}; // add imports of extended modules
        filteredModuleScopes = {m | MODID m <- (currentModule + imports), checked() in ms.status[m]} + extendedModuleScopes;

        TModel m1 = tmodel();
        m1.version = getCurrentTplVersion();
        m1.rascalTplVersion = compilerConfig.rascalTplVersion;
        m1.modelName = moduleId2moduleName(currentModule);
        m1.moduleLocs = (m1.modelName  : currentModule);

        m1.facts = (key : tm.facts[key] | key <- tm.facts, isContainedInFilteredModuleScopes(key, tm.logical2physical));

        m1.specializedFacts = (key : tm.specializedFacts[key] | key <- tm.specializedFacts, isContainedInComponentScopes(key, tm.logical2physical), any(fms <- filteredModuleScopes, isContainedIn(key, fms)));
        m1.facts += m1.specializedFacts;

        // m1.messages = [ msg | msg <- tm.messages, msg.at.path == currentModule.path || msg is error ];
        m1.messages = tm.messages;
        ms.messages[currentModule] = toSet(m1.messages);

        filteredModuleScopePaths = {ml.path |loc  ml <- filteredModuleScopes};
        m1.scopes = tm.scopes;
        // m1.scopes
        //         = ( inner : tm.scopes[inner]
        //           | loc inner <- tm.scopes,
        //             inner.path in filteredModuleScopePaths,
        //             (tm.scopes[inner] == |global-scope:///| || isContainedInComponentScopes(inner, tm.logical2physical))
        //           );

        m1.store
                = (key_bom : bom);
        m1.store[key_grammar]
                = tm.store[key_grammar] ? grammar({}, ());

        m1.store[key_ADTs]
                = tm.store[key_ADTs] ? {};
        m1.store[key_common_keyword_fields]
                = tm.store[key_common_keyword_fields] ? [];

        m1.paths = { tup | tuple[MODID from, PathRole pathRole, MODID to] tup <- tm.paths, tup.from == currentModule || tup.from in filteredModuleScopes /*|| tup.from in filteredModuleScopePaths*/ };

        keepRoles = variableRoles + keepInTModelRoles;
        m1.useDef = { <u, d>
                    | <u, d> <- tm.useDef,
                          isContainedIn(u, currentModule, tm.logical2physical)
                       || (tm.definitions[d]? && tm.definitions[d].idRole in keepRoles)
                    };

        // Filter model for current module and replace functions in defType by their defined type

        defs = for(tup:<loc _scope, str _id, str _orgId, IdRole idRole, loc defined, DefInfo _defInfo> <- tm.defines){
                    if( ( idRole in variableRoles ? (  isContainedInComponentScopes(defined, tm.logical2physical)
                                                    )
                                                  : (  idRole in keepInTModelRoles
                                                    && ( isContainedInComponentScopes(defined, tm.logical2physical)
                                                       || isContainedInFilteredModuleScopes(defined, tm.logical2physical)
                                                       )
                                                    )
                        )
                      ){
                            append tup;
                    }
                };

        m1.defines = toSet(defs);

        m1.definitions = ( def.defined : def | Define def <- m1.defines);  // TODO this is derived info, can we derive it later?
        // Remove default expressions and fragments
        m1 = visit(m1) {
                    case kwField(AType atype, str fieldName, str definingModule, Expression _defaultExp) => kwField(atype, fieldName, definingModule)
                    case loc l : if(!isEmpty(l.fragment)) insert l[fragment=""];
                 };
        m1.logical2physical = tm.logical2physical;
        ms.status[currentModule] -= {tpl_saved()};
        ms = addTModel(currentModule, m1, ms);
    // println("TModel for <currentModule>:"); iprintln(m1);
    }
    return ms;
}