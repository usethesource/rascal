module lang::rascalcore::compile::muRascal2Java::JGenie

import lang::rascal::\syntax::Rascal;

import IO;
import List;
import Location;
import Map;
import Node;
import Set;
import String;

import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::compile::muRascal2Java::CodeGen;
import lang::rascalcore::compile::util::Names;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;

import lang::rascalcore::compile::muRascal2Java::Conversions;

extend lang::rascalcore::check::CheckerCommon;

alias JCode = str;

data JGenie
    = jgenie(
        str () getModuleName,
        loc () getModuleLoc,
        void (MuFunction) setFunction,
        MuFunction () getFunction,
        str () getFunctionName,
        bool (MuExp) isDefinedInCurrentFunction,
        AType (loc src) getType,
        str(loc def) getImportedModuleName,
        str (list[loc] srcs) getAccessor,
        Define (loc src) getDefine,
        list[MuExp] (loc src) getExternalVars,
        void(str name) setKwpDefaults,
        str() getKwpDefaults,
        bool(AType) hasCommonKeywordFields,
        str(AType atype) shareType,
        str(value con) shareConstant,
        str(Symbol, map[Symbol,Production]) shareATypeConstant,
        str () getConstants,
        bool (str con) isWildCard,
        void(set[MuExp] evars) addExternalVars,
        bool (MuExp exp) isExternalVar,
        void(set[MuExp] lvars) addLocalRefs,
        bool(MuExp lvar) isLocalRef,
        bool(MuExp var) isRef,
        str(str prefix) newTmp,
        void(str) addImportedLibrary,
        list[str] () getImportedLibraries,
        bool (tuple[str name, AType funType, str scope, list[loc] ofunctions, list[loc] oconstructors] overloads) usesLocalFunctions
      )
    ;
    
JGenie makeJGenie(MuModule m, 
                  map[str,TModel] tmodels, 
                  map[str,loc] moduleLocs, 
                  map[str, MuFunction] muFunctions){
                  
    map[str,loc] allModuleLocs = moduleLocs;
    map[loc,str] allLocs2Module = invertUnique(moduleLocs);
    MuModule currentModule = m;
    str moduleName = m.name;
    map[AType, map[str,AType]] commonKeywordFieldsNameAndType = m.commonKeywordFields;
    str kwpDefaults = "$kwpDefaults";
    map[value,str] constants = ();
    map[str,value] constant2value = ();
    map[AType,str] types = ();
    int nconstants = -1;
    int ntypes = -1;
    
    map[ParseTree::Symbol,str] atype_constants = ();
    map[ParseTree::Symbol,map[ParseTree::Symbol,ParseTree::Production]] atype_definitions = ();
    map[str,ParseTree::Symbol] atype_constant2atype = ();
    int ntconstants = -1;
    
    set[MuExp] externalVars = {};
    set[MuExp] localRefs = {};
    int ntmps = -1;
    set[str] importedLibraries = {};
    
    TModel currentTModel = tmodels[moduleName];
    loc currentModuleScope = moduleLocs[moduleName];
    //set[tuple[str name, AType funType, str scope, list[loc] ofunctions, list[loc] oconstructors]] resolvers = {};
    map[str resolverName, list[MuExp] externalVars] resolver2externalVars = ();
    str functionName = "$UNKNOWN";
    MuFunction function;
    
    map[loc,set[MuExp]] fun2externals = (fun.src : fun.externalVars  | fun <- range(muFunctions));
    map[loc,MuFunction] muFunctionsByLoc = (f.src : f | fname <- muFunctions, f := muFunctions[fname]);
    rel[str,AType,str] mergedOverloads = {};
    
    extendScopes = { m2loc | <module_scope, extendPath(), m2loc> <- tmodels[moduleName].paths };
    importScopes = { m2loc | <module_scope, importPath(), m2loc> <- tmodels[moduleName].paths};
    importScopes += { m2loc | imp <- importScopes, <imp, extendPath(), m2loc> <- tmodels[moduleName].paths};
    importAndExtendScopes = importScopes + extendScopes;
    
   
    allPaths = { *(tmodels[mname].paths) | mname <- tmodels};
    sortedImportAndExtendScopes =
        sort(importAndExtendScopes,
            bool(loc a, loc b) { return <a, extendPath(), b> in allPaths; });
   
    extends = {<a, b> | <a, extendPath(), b> <- allPaths, a in importAndExtendScopes, b in importAndExtendScopes}+;
   
    loc findDefiningModuleForDef(loc def){
        for(ms <- sortedImportAndExtendScopes){
            if(isContainedIn(def, ms)){
               return ms;
            }
        }
        throw "findDefiningModuleForDef: <def>";
    }
    
    loc findLargestExtendingModule(loc ms){
        if(<a, ms> <- extends){
            return findLargestExtendingModule(a);
        }
        return ms;
    }
    
    loc findImportForDef(loc def){
        base = findDefiningModuleForDef(def);
        return findLargestExtendingModule(base);
    }
    
    str _getModuleName()
        = currentTModel.modelName;
        
    loc _getModuleLoc()
        = currentModuleScope;
        
    private bool isIgnored(MuFunction muFun){
        return !isEmpty(domain(muFun.tags) & {"ignore", "Ignore", "ignoreInterpreter", "IgnoreInterpreter"});
    }
        
    void _setFunction(MuFunction fun){
        function = fun;
        functionName = fun.uniqueName; //isOuterScopeName(fun.scopeIn) ? fun.uniqueName : "<fun.scopeIn>_<fun.uniqueName>";
    }
    
    MuFunction _getFunction()
        = function;
    
    str _getFunctionName()
        = functionName;
    
    AType _getType(loc src)
        = currentTModel.facts[src];
        
    str _getImportedModuleName(loc def){
        return module2field(allLocs2Module[findImportForDef(def)]);
    }
        
    str _getAccessor(list[loc] srcs){
        // Single src
        if(size(srcs) == 1){
            src = srcs[0];
            if(currentTModel.definitions[src]?){
                def = currentTModel.definitions[src];
                if(defType(AType tp) := def.defInfo){
                        baseName = getJavaName(def.id);
                        if(isContainedIn(def.defined, currentModuleScope)){
                            if(def.scope != currentModuleScope){    // inner function
                                fun = muFunctionsByLoc[def.defined];
                                return "<fun.scopeIn>_<baseName>";
                            }
                            return baseName;
                        } else {
                            return isClosureName(baseName) ? baseName : "<_getImportedModuleName(def.defined)>.<baseName>";
                        }
                     }
            }
            for(ms <- sortedImportAndExtendScopes, mname := allLocs2Module[ms]){
                if(tmodels[mname].definitions[src]?){
                    def = tmodels[mname].definitions[src];
                    if(defType(AType tp) := def.defInfo){
                        descriptor = atype2idpart(tp);
                        baseName = getJavaName(def.id);
                      
                        if(isContainedIn(def.defined, currentModuleScope)){
                            return baseName;
                        } else {
                            return isClosureName(baseName) ? baseName : "<_getImportedModuleName(def.defined)>.<baseName>";
                        }
                     }
                 }
            }
            throw "No accessor found for <src>";
        }
        // Multiple srcs
        name = "???";
        for(mname <- tmodels){
            if(tmodels[mname].definitions[srcs[0]]?){
                def = tmodels[mname].definitions[srcs[0]];
                name = def.id;
                break;
            }
        }
       
        if(isSyntheticFunctionName(name)){
            return name;
        }
        
        scopeIn = definedInInnerScope(srcs);
        
        if(scopeIn != ""){
            return "<scopeIn>_<name>";
        }
        if(any(d <- srcs, isContainedIn(d, currentModuleScope))){
            for(d <- srcs){
                def = currentTModel.definitions[d];
                if(isConstructorType(def.defInfo.atype)){
                    return name;
                }
            }
            return "$me.<name>";
        }
        
       for(ms <- sortedImportAndExtendScopes){
        if(any(d <- srcs, isContainedIn(d, ms))){
            return "<module2field(allLocs2Module[ms])>.<name>";
        }
       }
       throw "_getAccessor: <name>, <srcrs>";
    }
    
    str definedInInnerScope(list[loc] srcs){
        scopeIn = "";
        for(d <- srcs){
            if(isContainedIn(d, currentModuleScope)){
                if(muFunctionsByLoc[d]?){
                    fun = muFunctionsByLoc[d];
                    iprintln(fun.scopeIn);
                    if(fun.scopeIn == "") return "";
                    scopeIn = fun.scopeIn;
                }
            } else {
                return "";
            }
        }
        return scopeIn;
    }
    
    Define _getDefine(loc src){
        for(mname <- tmodels){
                if(tmodels[mname].definitions[src]?){
                    res = tmodels[mname].definitions[src];
                    return res;
                }
        }
        throw "getDefine <src>";
    }
    
    list[MuExp] _getExternalVars(loc src){
        if(fun2externals[src]?){
            evars = isContainedIn(src, currentModuleScope) ? fun2externals[src] : {};
            return [var | var <- evars, var.pos >= 0 ];
        }
        return [];
    }
    
    bool _isDefinedInCurrentFunction(MuExp var){
        definitions = currentTModel.definitions;
        for(d <- definitions, def := definitions[d], var.name == def.id, isContainedIn(def.scope, function.src), def.idRole in variableRoles){
            return true;
        }
        return false;
    }
    
    void _setKwpDefaults(str name){
        kwpDefaults = name;
    }
    
    str _getKwpDefaults() = kwpDefaults;
    
    bool _hasCommonKeywordFields(AType ctype){
        switch(ctype){
            case aadt(_,_,_):
                return commonKeywordFieldsNameAndType[ctype]?;
            case acons(AType adt, _, _): 
                return commonKeywordFieldsNameAndType[adt]?;
            default: 
                return false;
        }
    }
    
    str _shareType(AType atype){
        if(types[atype]?) return types[atype];
        ntypes += 1;
        c = "$T<ntypes>";
        types[atype] = c;
        return c;
    }
    
    str _shareConstant(value v){
        if(constants[v]?) return constants[v];
        nconstants += 1;
        c = "$C<nconstants>";
        constants[v] = c;
        constant2value[c] = v;
        return c;
    }
    
    str _shareATypeConstant(Symbol t, map[Symbol, Production] definitions){
        if(atype_constants[t]?) return atype_constants[t];
        ntconstants += 1;
        c = "$R<ntconstants>";
        atype_constants[t] = c;
        atype_definitions[t] = definitions;
        atype_constant2atype[c] = t;
        return c;
    }
    
    str _getConstants(){
        return "<for(v <- constants){>
               'private final <value2outertype(v)> <constants[v]> = <value2IValue(v)>;<}>
               '<for(t <- types){>private final io.usethesource.vallang.type.Type <types[t]> = <atype2vtype(t)>;<}>
               '<for(t <- atype_constants){>
               'private final IConstructor <atype_constants[t]> = $RVF.reifiedType(<value2IValue(t)>, <value2IValue(atype_definitions[t])>);
               '<}>";
    }
    
    bool _isWildCard(str con){
        if(constant2value[con]?){
            return constant2value[con] == "_";
        }
        return false;
    }
    
    void _addExternalVars(set[MuExp] vars){
        externalVars += vars;
    }
    
    bool _isExternalVar(MuExp var) = var in externalVars && var.pos != -1;
    
    void _addLocalRefs(set[MuExp] vars){
        localRefs += vars;
    }
    
    bool _isLocalRef(MuExp var) = var in localRefs;
    
    bool _isRef(MuExp var) = _isExternalVar(var) || _isLocalRef(var);
    
    str _newTmp(str prefix){
        ntmps += 1;
        return "$<prefix><ntmps>";
    }
    
    void _addImportedLibrary(str lib){
        importedLibraries += lib;
    }
    
    list[str] _getImportedLibraries(){
        return toList(importedLibraries);
    }
    
    void _addMergedOverloads(rel[str,AType,str] merged){
        mergedOverloads += merged;
    }
    
    bool _usesLocalFunctions(tuple[str name, AType funType, str scope, list[loc] ofunctions, list[loc] oconstructors] overloads){
        return    any(of <- overloads.ofunctions, isContainedIn(currentTModel.definitions[of].defined, currentModuleScope))
               || any(oc <- overloads.oconstructors, isContainedIn(currentTModel.definitions[oc].defined, currentModuleScope));
    }
    
    tuple[str name, list[str] argTypes] getElements(str signature){
        if(/<name:[a-z A-Z 0-9 _]+>(<args:.*>)/ := signature){
           return <name, [tps[0] | str arg <- split(",", args), tps := split(" ", arg)]>;
        }
    }
    
    return jgenie(
                _getModuleName,
                _getModuleLoc,
                _setFunction,
                _getFunction,
                _getFunctionName,
                _isDefinedInCurrentFunction,
                _getType,
                _getImportedModuleName,
                _getAccessor,
                _getDefine,
                _getExternalVars,
                _setKwpDefaults,
                _getKwpDefaults,
                _hasCommonKeywordFields,
                _shareType,
                _shareConstant,
                _shareATypeConstant,
                _getConstants,
                _isWildCard,
                _addExternalVars,
                _isExternalVar,
                _addLocalRefs,
                _isLocalRef,
                _isRef,
                _newTmp,
                _addImportedLibrary,
                _getImportedLibraries,
                _usesLocalFunctions
            );
}

// ---- casting ---------------------------------------------------------------

str castArg(AType t, str x) = startsWith(x, "((<atype2javatype(t)>)(") ? x : "((<atype2javatype(t)>)(<x>))";
str cast(AType t, str x) = "(<castArg(t,x)>)";