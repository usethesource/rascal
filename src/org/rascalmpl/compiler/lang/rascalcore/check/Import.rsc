@bootstrapParser
module lang::rascalcore::check::Import

/*
    Check imports, and read/write TPL files.
*/

extend lang::rascalcore::check::CheckerCommon;

import lang::rascalcore::check::RascalConfig;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::check::ADTandGrammar;
import lang::rascalcore::compile::CompileTimeError;

import DateTime;
import IO;
import List;
import ListRelation;
import Location;
import Map;
import Set;
import Relation;
import String;
import ValueIO;

import analysis::graphs::Graph;
import util::Reflective;
import util::Benchmark;
import lang::rascalcore::compile::util::Names; // TODO: refactor, this is an undesired dependency on compile

private str getRascalModuleName(loc mloc, map[loc,str] moduleStrs, PathConfig pcfg){
    if(moduleStrs[mloc]? ){
        return moduleStrs[mloc];
    }
    return getRascalModuleName(mloc, pcfg);
}

// Complete a ModuleStatus by adding a contains relation that adds transitive edges for extend
ModuleStatus completeModuleStatus(ModuleStatus ms){
    pcfg = ms.pathConfig;
    moduleStrs = invertUnique(ms.moduleLocs);
    paths = ms.paths + { <ms.moduleLocs[a], r, ms.moduleLocs[b]> | <str a, PathRole r, str b> <- ms.strPaths, ms.moduleLocs[a]?, ms.moduleLocs[b]? };
    extendPlus = {<from, to> | <from, extendPath(), to> <- paths}+;

    paths += { <from, extendPath(), to> | <from, to> <- extendPlus };

    pathsPlus = {<from, to> | <from, _, to> <- paths}+;

    cyclicMixed = {mloc1, mloc2 | <mloc1, mloc2> <- pathsPlus, mloc1 != mloc2,
                             <mloc1, importPath(), mloc2> in paths && <mloc2, extendPath(), mloc1> in paths
                             || <mloc1, extendPath(), mloc2> in paths && <mloc2, importPath(), mloc1> in paths };

    for(mloc <- cyclicMixed){
        mname = getRascalModuleName(mloc, moduleStrs, pcfg);
        set[str] cycle = { getRascalModuleName(mloc2, moduleStrs, pcfg) |  <mloc1, mloc2> <- pathsPlus, mloc1 == mloc, mloc2 in cyclicMixed } +
                         { getRascalModuleName(mloc1, moduleStrs, pcfg) |  <mloc1, mloc2> <- pathsPlus, mloc2 == mloc , mloc1 in cyclicMixed };
        if(size(cycle) > 1){
            ms.messages[mname] = (ms.messages[mname] ? {}) + error("Mixed import/extend cycle not allowed: {<intercalate(", ", toList(cycle))>}", mloc);
        }
    }
     paths += { *{<c, importPath(), a>| a <- extendPlus[b]} | < c, importPath(), b> <- paths };
    //paths += { <c, importPath(), a> | < c, importPath(), b> <- paths,  <b , extendPath(), a> <- paths};

    ms.paths = paths;

   strPaths = {};
    for(<loc from, PathRole r, loc to> <- paths){
        try {
            mfrom = getRascalModuleName(from, moduleStrs, pcfg);
            mto = getRascalModuleName(to, moduleStrs, pcfg);
            strPaths += <mfrom, r, mto >;
        } catch _: ;/* ignore non-existing module */
    }
    ms.strPaths = strPaths;
    return ms;
}

ModuleStatus getImportAndExtendGraph(set[str] qualifiedModuleNames, RascalCompilerConfig ccfg){
    return completeModuleStatus((newModuleStatus(ccfg) | getImportAndExtendGraph(qualifiedModuleName, it) | qualifiedModuleName <- qualifiedModuleNames));
}

ModuleStatus getImportAndExtendGraph(set[str] qualifiedModuleNames, ModuleStatus ms){
    return completeModuleStatus((ms | getImportAndExtendGraph(qualifiedModuleName, it) | qualifiedModuleName <- qualifiedModuleNames));
}

ModuleStatus getImportAndExtendGraph(str qualifiedModuleName, RascalCompilerConfig ccfg){
    return completeModuleStatus(getImportAndExtendGraph(qualifiedModuleName, newModuleStatus(ccfg)));
}

ModuleStatus getImportAndExtendGraph(str qualifiedModuleName, ModuleStatus ms){
//println("getImportAndExtendGraph: <qualifiedModuleName>");
    pcfg = ms.pathConfig;
    qualifiedModuleName = unescape(qualifiedModuleName);

    if(!ms.status[qualifiedModuleName]?){
        ms.status[qualifiedModuleName] = {};
    }

    if(module_dependencies_extracted() in ms.status[qualifiedModuleName]){
        return ms;
    }
    ms.status[qualifiedModuleName] += module_dependencies_extracted();

    <found, tm, ms> = getTModelForModule(qualifiedModuleName, ms);
    if(found){
        ms.paths = tm.paths;
        allImportsAndExtendsValid = true;
        rel[str, PathRole] localImportsAndExtends = {};

        if(!ms.moduleLastModified[qualifiedModuleName]?){
            ms.moduleLastModified[qualifiedModuleName] = getLastModified(qualifiedModuleName, ms.moduleLastModified, pcfg);
        }

        if(tm.store[key_bom]? && rel[str,datetime,PathRole] bom := tm.store[key_bom]){
           for(<str m, datetime timestampInBom, PathRole pathRole> <- bom){
               if(!ms.status[m]?){
                    ms.status[m] = {};
               }
               if(m != qualifiedModuleName){
                    localImportsAndExtends += <m, pathRole>;
               }
               if(isModuleModified(m, timestampInBom, pcfg)){
                    allImportsAndExtendsValid = false;
                    ms.status[m] += rsc_changed();
                    ms.status[m] -= {tpl_uptodate(), checked()};
                    ms.status[qualifiedModuleName] -= tpl_saved();
                    if(ms.compilerConfig.verbose){
                        println("--- using <getLastModified(m,ms.moduleLastModified,pcfg)> (most recent) version of <m>,
                                '    older <timestampInBom> version was used in previous check of <qualifiedModuleName>");
                    }
               }
           }

        } else {
            throw "No bill-of-materials found for <qualifiedModuleName>";
        }
        if(!allImportsAndExtendsValid){ // Check that the source code of qualifiedModuleName is available
            mloc = |unknown:///|(0,0,<0,0>,<0,0>);
            try {
                try {
                    mloc = getRascalModuleLocation(qualifiedModuleName, pcfg);
                } catch e: {
                    err = error("Cannot get location for <qualifiedModuleName>: <e>", mloc);
                    ms.messages[qualifiedModuleName] = { err };
                    tm = tmodel(modelName=qualifiedModuleName, messages=[ err ]);
                    ms = addTModel(qualifiedModuleName, tm, ms);
                    ms.status[qualifiedModuleName] += { rsc_not_found() };
                    return ms;
                }
                if(mloc.extension != "rsc" || isModuleLocationInLibs(qualifiedModuleName, mloc, pcfg)) throw "No src or library module 1"; //There is only a tpl file available
            } catch value _:{
                <incompatible, ms> = isCompatibleBinaryLibrary(tm, ms);
                if(!isEmpty(incompatible)){
                    txt = "Recompilation or reconfiguration needed: binary module `<qualifiedModuleName>` uses incompatible module(s) <intercalateAnd(incompatible)>";
                    msg = error(txt, mloc);
                    tm.messages += [msg];
                    ms.messages[qualifiedModuleName] ? {} += { msg };
                    throw rascalBinaryNeedsRecompilation(qualifiedModuleName, msg);
                } else {
                    allImportsAndExtendsValid = true;
                    if(ms.compilerConfig.verbose){
                        println("--- reusing tmodel of <qualifiedModuleName> (source not accessible)");
                    }
                }
            }
        }
        if(allImportsAndExtendsValid){
            ms.status[qualifiedModuleName] += {tpl_uptodate(), checked()}; //TODO: maybe check existence of generated java files
            ms.moduleLocs += tm.moduleLocs;
            ms.paths += tm.paths;
            ms.strPaths += {<qualifiedModuleName, pathRole, imp> | <str imp, PathRole pathRole> <- localImportsAndExtends };
            ms.status[qualifiedModuleName] += module_dependencies_extracted();
            ms.messages[qualifiedModuleName] ? {} += toSet(tm.messages);
            for(<imp, pr> <- localImportsAndExtends, isEmpty({module_dependencies_extracted()} & ms.status[imp])  ){
                ms.status[imp] -= tpl_saved();
                ms = getImportAndExtendGraph(imp, ms);
            }
            return ms;
         }
    }

    if(rsc_not_found() in ms.status[qualifiedModuleName]){
        return ms;
    }

    <success, pt, ms> = getModuleParseTree(qualifiedModuleName, ms);
    if(success){
        <ms, imports_and_extends> = getModulePathsAsStr(pt, ms);

        for(<_, kind, imp> <- imports_and_extends, rsc_not_found() notin ms.status[imp]){
            ms.strPaths += {<qualifiedModuleName, kind, imp>};
            ms = getImportAndExtendGraph(imp, ms);
        }
    } else {
         ms.status[qualifiedModuleName] += rsc_not_found();
    }

    return ms;
}

ModuleStatus getInlineImportAndExtendGraph(Tree pt, RascalCompilerConfig ccfg){
    ms = newModuleStatus(ccfg);
    visit(pt){
        case  m: (Module) `<Header header> <Body _>`: {
            qualifiedModuleName = prettyPrintName(header.name);
            ms.moduleLocs[qualifiedModuleName] = getLoc(m);
            <ms, imports_and_extends> = getModulePathsAsStr(m, ms);
        }
    }
    return completeModuleStatus(ms);
}

// Is binary library module compatible with its dependencies (originating from imports and extends)?
tuple[list[str], ModuleStatus] isCompatibleBinaryLibrary(TModel lib, ModuleStatus ms){
    libName = lib.modelName;
    set[loc] libLogical = domain(lib.logical2physical);
    set[loc] libDefines = { l | l <- libLogical, getModuleFromLogical(l) == libName };
    set[loc] libDependsOn = libLogical - libDefines;
    set[str] libDependsOnModules = { getModuleFromLogical(l) | l <- libDependsOn };
    set[loc] dependentsProvide = {};
    for(m <- libDependsOnModules){
       <found, tm, ms> = getTModelForModule(m, ms);
       if(found){
           dependentsProvide += domain(tm.logical2physical);
       }
    }
    unsatisfied = libDependsOn - dependentsProvide;
    if(isEmpty(unsatisfied)){
        println("isCompatibleBinaryLibrary <libName>: satisfied");
        return <[], ms>;
    } else {
        println("isCompatibleBinaryLibrary, <libName> unsatisfied: <unsatisfied>");
        incompatibleModules = { split("/", u.path)[1] | u <- unsatisfied };
        return <toList(incompatibleModules), ms>;
    }
}

// Example: |rascal+function:///util/Math/round$d80e373d64c01979| ==> util::Math
// Example: |rascal+module:///lang/rascal/syntax/Rascal| -> lang::rascal::syntax::Rascal
str getModuleFromLogical(loc l){
    i = findLast(l.path[1..], "/");
    res = (l.scheme == "rascal+module" || i < 0) ? l.path[1..] : l.path[1..i+1];
    res = replaceAll(res, "/", "::");
    //println("getModuleFromLogical: <l> -\> <res>");
    return res;
}

// // Is what library module lib provides compatible with all uses in the modules libUsers?
// tuple[list[str], ModuleStatus] isCompatibleBinaryLibrary(TModel lib, set[str] libUsers, ModuleStatus ms){

//     libName = lib.modelName;
//     set[loc] libProvides = domain(lib.logical2physical);
//     set[str] libProvidesModules = { getModuleFromLogical(l) | l <- libProvides };
//     set[loc] usersRequire = {};
//     for(m <- libUsers){
//        <found, tm, ms> = getTModelForModule(m, ms);
//        if(found){
//            usersRequire += domain(tm.logical2physical);
//        }
//     }
//     usersRequireFromLib = { l | l <- usersRequire, getModuleFromLogical(l) in libProvidesModules };

//     if(usersRequireFromLib <= libProvides){
//         //println("isCompatibleBinaryLibrary <libName>: satisfied");
//         return <[], ms>;
//     } else {
//         println("isCompatibleBinaryLibrary, <libName> unsatisfied: <usersRequireFromLib - libProvides>");
//         unsatisfied = usersRequireFromLib - libProvides;
//         incompatibleModules = { split("/", u.path)[1] | u <- unsatisfied };
//         return <toList(incompatibleModules), ms>;
//     }
// }

tuple[bool, ModuleStatus] importsAndExtendsAreBinaryCompatible(TModel tm, set[str] importsAndExtends, ModuleStatus ms){
    moduleName = tm.modelName;
    physical2logical = invertUnique(tm.logical2physical);

    modRequires = { lg | l <- range(tm.useDef),
                        physical2logical[l]?, lg := physical2logical[l],
                        moduleName !:= getModuleFromLogical(lg) };
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

tuple[ModuleStatus, rel[str, PathRole, str]] getModulePathsAsStr(Module m, ModuleStatus ms){
    moduleName = unescape("<m.header.name>");
    imports_and_extends = {};
    for(imod <- m.header.imports, imod has \module){
        iname = unescape("<imod.\module.name>");
        imports_and_extends += <moduleName, imod is \default ? importPath() : extendPath(), iname>;
        ms.status[iname] = ms.status[iname] ? {};
        try {
            mloc = getRascalModuleLocation(iname, ms.pathConfig);
         } catch str msg: {
            err = error("Cannot get location for <iname>: <msg>", imod@\loc);
            ms.messages[moduleName] ? {} += { err };
            ms.status[iname] += { rsc_not_found() };
         }
    }
    ms.strPaths += imports_and_extends;
    return <ms, imports_and_extends>;
}

// ---- Save modules ----------------------------------------------------------

map[str, loc] getModuleScopes(TModel tm)
    = (id: defined | <loc _, str id, str _orgId, moduleId(),  loc defined, DefInfo _> <- tm.defines);

loc getModuleScope(str qualifiedModuleName, map[str, loc] moduleScopes, PathConfig pcfg){
    if(moduleScopes[qualifiedModuleName]?){
        return moduleScopes[qualifiedModuleName];
    }
    for(l <- range(moduleScopes)){
        if(getRascalModuleName(l, pcfg) == qualifiedModuleName){
            return l;
        }
    }
    throw "No module scope found for <qualifiedModuleName>";
}

tuple[map[str,TModel], ModuleStatus] prepareForCompilation(set[str] component, map[str,set[str]] m_imports, map[str,set[str]] m_extends, ModuleStatus ms, map[str,loc] moduleScopes, TModel tm){
   //map[str,TModel] tmodels = (); //ms.tmodels;
    pcfg = ms.pathConfig;

    dependencies_ok = true;
    for(m <- component, rsc_not_found() notin ms.status[m], MStatus::ignored() notin ms.status[m]){
        if(parse_error() in ms.status[m]){
            return <(m : tmodel(modelName=m,messages=toList(ms.messages[m]))) ,ms>;
        }
        for(imp <- m_imports[m] + m_extends[m], rsc_not_found() notin ms.status[imp], MStatus::ignored() notin ms.status[m]){
            imp_status = ms.status[imp];
            if(parse_error() in imp_status || checked() notin imp_status){
                dependencies_ok = false;
                cause = (rsc_not_found() in imp_status) ? "module not found" : "due to syntax error";
                ms.messages[m] = (ms.messages[imp] ? {}) + error("<imp in m_imports[imp] ? "Imported" : "Extended"> module <imp> could not be checked (<cause>)", moduleScopes[m]);
            }
        }
        if(!dependencies_ok){
            return <(m: tmodel(modelName=m,messages=toList(ms.messages[m]))), ms>;
        }
    }
    transient_tms = (m : tm | m <- component);
    org_tm = tm;
    for(m <- component, rsc_not_found() notin ms.status[m], MStatus::ignored() notin ms.status[m]){
        tm = org_tm;
        tm.modelName = m;
        mScope = getModuleScope(m, moduleScopes, pcfg);
        tm.moduleLocs = (m : mScope);

        tm.definitions = ( def.defined : def | Define def <- tm.defines);

        transient_tms[m] = tm;
        <tm, ms> = addGrammar(m, m_imports[m], m_extends[m], transient_tms, ms);
        ms.messages[m] = toSet(tm.messages);
        transient_tms[m] = tm;
    }
    return <transient_tms, ms>;
}

ModuleStatus doSaveModule(set[str] component, map[str,set[str]] m_imports, map[str,set[str]] m_extends, ModuleStatus ms, map[str,loc] moduleScopes, map[str, TModel] transient_tms, RascalCompilerConfig compilerConfig){
    map[str,datetime] moduleLastModified = ms.moduleLastModified;
    pcfg = ms.pathConfig;

    if(any(c <- component, !isEmpty({parse_error(), rsc_not_found(), MStatus::ignored()} & ms.status[c]))){
        return ms;
    }

    //println("doSaveModule: <qualifiedModuleName>, <imports>, <extends>, <moduleScopes>");
    component_scopes = { getModuleScope(qualifiedModuleName, moduleScopes, pcfg) | qualifiedModuleName <- component };
    set[loc] filteredModuleScopes = {};
    loc2moduleName = invertUnique(ms.moduleLocs);

    bool isContainedInComponentScopes(loc inner){
        return any(cs <- component_scopes, isContainedIn(inner, cs));
    };

    bool isContainedInFilteredModuleScopes(loc inner){
        return any(cs <- filteredModuleScopes, isContainedIn(inner, cs));
    };

    for(qualifiedModuleName <- component){
        start_save = cpuTime();
        tm = transient_tms[qualifiedModuleName];
        mscope = getModuleScope(qualifiedModuleName, moduleScopes, pcfg);
        <found, tplLoc> = getTPLWriteLoc(qualifiedModuleName, pcfg);

        imports = m_imports[qualifiedModuleName];
        extends = m_extends[qualifiedModuleName];

        bom = makeBom(qualifiedModuleName, ms);

        extendedModuleScopes = {getModuleScope(m, moduleScopes, pcfg) | str m <- extends, checked() in ms.status[m]};
        extendedModuleScopes += {*tm.paths[ems,importPath()] | ems <- extendedModuleScopes}; // add imports of extended modules
        filteredModuleScopes = {getModuleScope(m, moduleScopes, pcfg) | str m <- (qualifiedModuleName + imports), checked() in ms.status[m]} + extendedModuleScopes;

        TModel m1 = tmodel();
        m1.rascalTplVersion = compilerConfig.rascalTplVersion;
        m1.modelName = qualifiedModuleName;
        m1.moduleLocs = (qualifiedModuleName : mscope);

        m1.facts = (key : tm.facts[key] | key <- tm.facts, isContainedInComponentScopes(key));

        m1.specializedFacts = (key : tm.specializedFacts[key] | key <- tm.specializedFacts, isContainedInComponentScopes(key), any(fms <- filteredModuleScopes, isContainedIn(key, fms)));
        m1.facts += m1.specializedFacts;

        m1.messages = sort( { msg | msg <- tm.messages, msg.at.path == mscope.path}, bool(Message a, Message b){ return a.at.begin.line < b.at.begin.line; });
        ms.messages[qualifiedModuleName] = toSet(m1.messages);

        filteredModuleScopePaths = {ml.path |loc  ml <- filteredModuleScopes};

        m1.scopes
                = ( inner : tm.scopes[inner]
                  | loc inner <- tm.scopes,
                    inner.path in filteredModuleScopePaths,
                    isContainedInComponentScopes(inner)
                  );

        m1.store
                = (key_bom : bom);
        m1.store[key_grammar]
                = tm.store[key_grammar] ? grammar({}, ());

        m1.store[key_ADTs]
                = tm.store[key_ADTs] ? {};
        m1.store[key_common_keyword_fields]
                = tm.store[key_common_keyword_fields] ? [];

        m1.paths = { tup | tuple[loc from, PathRole pathRole, loc to] tup <- tm.paths, tup.from == mscope || tup.from in filteredModuleScopes /*|| tup.from in filteredModuleScopePaths*/ };

        keepRoles = variableRoles + keepInTModelRoles;
        m1.useDef = { <u, d>
                    | <u, d> <- tm.useDef,
                          isContainedIn(u, mscope)
                       || (tm.definitions[d]? && tm.definitions[d].idRole in keepRoles)
                    };

        // Filter model for current module and replace functions in defType by their defined type

        defs = for(tup:<loc _scope, str _id, str _orgId, IdRole idRole, loc defined, DefInfo _defInfo> <- tm.defines){
                    if( ( idRole in variableRoles ?  isContainedInComponentScopes(defined)
                                                  : (  idRole in keepInTModelRoles
                                                    && ( isContainedInComponentScopes(defined)
                                                       || isContainedInFilteredModuleScopes(defined)
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
        log2phys = tm.logical2physical;
        m1.logical2physical = tm.logical2physical;
        m1.usesPhysicalLocs = true;
        ms.status[qualifiedModuleName] -= {tpl_saved()};
        ms = addTModel(qualifiedModuleName, m1, ms);
    }
    return ms;
}