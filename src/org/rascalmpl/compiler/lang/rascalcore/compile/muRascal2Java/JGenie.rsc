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
        str (AType t) getATypeAccessor,
        str (list[loc] srcs) getAccessor,
        Define (loc src) getDefine,
        list[MuExp] (loc src) getExternalRefs,
        lrel[str name, AType atype] (MuFunction fun) collectKwpFormals,
        lrel[str name, AType atype, MuExp defaultExp] (MuFunction fun) collectKwpDefaults,
        list[str] (MuFunction fun) collectRedeclaredKwps,
        list[str] (MuFunction fun) collectDeclaredKwps,
        void(str name) setKwpDefaultsName,
        str() getKwpDefaultsName,
        bool(AType) hasCommonKeywordFields,
        str(AType atype) shareType,
        str(value con) shareConstant,
        str(Symbol, map[Symbol,Production]) shareReifiedConstant,
        tuple[str,str] () getConstants,
        bool (str con) isWildCard,
        void(set[MuExp] evars) addExternalRefs,
        bool (MuExp exp) isExternalRef,
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
    
    map[ParseTree::Symbol,str] reified_constants = ();
    map[ParseTree::Symbol,map[ParseTree::Symbol,ParseTree::Production]] reified_definitions = ();
    int nreified = -1;
    
    set[MuExp] externalRefs = {};
    set[MuExp] localRefs = {};
    int ntmps = -1;
    set[str] importedLibraries = {};
    
    TModel currentTModel = tmodels[moduleName];
    loc currentModuleScope = moduleLocs[moduleName];
    str functionName = "$UNKNOWN";
    MuFunction function;
    
    map[loc,set[MuExp]] fun2externals = (fun.src : fun.externalRefs  | fun <- range(muFunctions));
    map[loc,MuFunction] muFunctionsByLoc = (f.src : f | fname <- muFunctions, f := muFunctions[fname]);
    
    extendScopes = { m2loc | <module_scope, extendPath(), m2loc> <- tmodels[moduleName].paths };
    importScopes = { m2loc | <module_scope, importPath(), m2loc> <- tmodels[moduleName].paths};
    importScopes += { m2loc | imp <- importScopes, <imp, extendPath(), m2loc> <- tmodels[moduleName].paths};
    importAndExtendScopes = importScopes + extendScopes;
   
    allPaths = { *(tmodels[mname].paths) | mname <- tmodels};
    sortedImportAndExtendScopes =
        sort(importAndExtendScopes,
            bool(loc a, loc b) { return <a, extendPath(), b> in allPaths; });
   
    extends = {<a, b> | <a, extendPath(), b> <- allPaths, a in importAndExtendScopes, b in importAndExtendScopes}+;
    
    JGenie thisJGenie;
   
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
    
    str _getATypeAccessor(AType t){   
        t = unsetR(t, "label");
        for(def <- range(currentTModel.definitions), def.idRole in (dataOrSyntaxRoles + constructorId()), t := def.defInfo.atype){
            for(ms <- allLocs2Module, isContainedIn(def.scope, ms)){
                defMod = allLocs2Module[ms];
                return defMod == moduleName ? "" : "<module2field(defMod)>.";
            }
        }
        return ""; //throw "No accessor found for <t>";
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
    
    list[MuExp] _getExternalRefs(loc src){
        if(fun2externals[src]?){
            evars = isContainedIn(src, currentModuleScope) ? fun2externals[src] : {};
            return sort([var | var <- evars, var.pos >= 0 ]);
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
    
    lrel[str name, AType atype] _collectKwpFormals(MuFunction fun){
        scopes = currentTModel.scopes;
        kwpFormals = fun.kwpDefaults<0,1>;
        outer = scopes[fun.src];
        while (muFunctionsByLoc[outer]?){
            fun1 = muFunctionsByLoc[outer];
            kwpFormals += fun1.kwpDefaults<0,1>;
            outer = scopes[fun1.src];
        }
        return kwpFormals;
    }
    
    lrel[str name, AType atype, MuExp defaultExp] _collectKwpDefaults(MuFunction fun){
        scopes = currentTModel.scopes;
        defaults = fun.kwpDefaults;
        funKwps =  fun.kwpDefaults<0>;
        outer = scopes[fun.src];
        while (muFunctionsByLoc[outer]?){
            fun1 = muFunctionsByLoc[outer];
            defaults = [<name, atype, defaultExp> | <str name, AType atype, MuExp defaultExp> <- fun1.kwpDefaults, name notin funKwps] + defaults; // remove outer definitions
            outer = scopes[fun1.src];
        }
        return defaults;
    }
    
    list[str] _collectRedeclaredKwps(MuFunction fun){
        scopes = currentTModel.scopes;
        funKwps = fun.kwpDefaults<0>;
        redeclared = [];
        outer = scopes[fun.src];
        while (muFunctionsByLoc[outer]?){
            fun1 = muFunctionsByLoc[outer];
            outerKwps = fun1.kwpDefaults<0>;
            redeclared += funKwps & outerKwps;
            outer = scopes[fun1.src];
        }
        return redeclared;
    }
    
     list[str] _collectDeclaredKwps(MuFunction fun){
        scopes = currentTModel.scopes;
        funKwps = fun.kwpDefaults<0>;
        declared = funKwps;
        outer = scopes[fun.src];
        while (muFunctionsByLoc[outer]?){
            fun1 = muFunctionsByLoc[outer];
            outerKwps = fun1.kwpDefaults<0>;
            declared += outerKwps;
            outer = scopes[fun1.src];
        }
        return declared;
    }
    
    void _setKwpDefaultsName(str name){
        kwpDefaults = name;
    }
    
    str _getKwpDefaultsName() = kwpDefaults;
    
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
        //println("%%%%% shareType: <atype>");
        //atype = unsetR(atype, "label");
        couter = "";
        if(types[atype]?){
            return types[atype];
        } else if(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole) := atype){
            return "<_getATypeAccessor(atype)>ADT_<adtName>";
        } else {
            ntypes += 1;
            couter = "$T<ntypes>";
            types[atype] = couter;
        }
        visit(atype){
            case AType t: {
                if(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole) := t){
                        ;
                } else if(acons(AType adt, list[AType] fields, list[Keyword] kwFields) := t){
                        ;
                } else if(atypeList(_) := t){
                        ;
                } else if(!types[t]?){
                    ntypes += 1;
                    c = "$T<ntypes>";
                types[t] = c;
                }
            }
        }
        return couter;
    }
    
    str _shareConstant(value v) {
        c = "?";
        
        v = unsetR(v, "src");
        
        if (constants[v]?) {
          return constants[v];
        }
        else {
          nconstants += 1;
          c = "$C<nconstants>";
          constants[v] = c;
          constant2value[c] = v;
        }
        
        if (Symbol _ := v || Production _ := v || Tree _ := v || map[&X,&Y] _ := v) {
            visit (v) {
              case value e: {
                e = unsetR(e, "src");
                if (!constants[e]?, Symbol _ := e || Production _ := e || Tree _ := e) {
                  nconstants += 1;
                  d = "$C<nconstants>";
                  constants[e] = d;
                  constant2value[d] = e;
                }
              }
            }
        }
        
        return c;
    }
    
    str _shareReifiedConstant(Symbol t, map[Symbol, Production] definitions){
        if(reified_constants[t]?) return reified_constants[t];
        nreified += 1;
        c = "$R<nreified>";
        reified_constants[t] = c;
        _shareConstant(t);
        _shareConstant(definitions);
        reified_definitions[t] = definitions;
        return c;
    }
    
    tuple[str,str] _getConstants() {
        str cdecls = "";
        str cinits = "";
        done = {};
        
        // Generate constants in the right declaration order, such that
        // they are always declared before they are used in the list of constant fields
        for(c <- constants){
            if(c == ""){
                if (c notin done) {
                  cdecls += "<value2outertype(c)> <constants[c]>;\n";
                  cinits += "<constants[c]> = <value2IValue(c, constants)>;\n";
                  done += {c};
                }
            } else {
                bottom-up visit(c) {
                  case s:
                        if (s notin done, s in constants) {
                          cdecls += "<value2outertype(s)> <constants[s]>;\n";
                          cinits += "<constants[s]> = <value2IValue(s, constants)>;\n";
                          done += {s};
                        }
                    }
            }
        }
        
        str tdecls = "";
        str tinits = "";
        done = {};
        // Generate type constants in the right declaration order, such that
        // they are always declared before they are used in the list of type fields
        for(t <- types){
            if(t == ""){
                if (c notin done) {
                  tdecls += "io.usethesource.vallang.type.Type <types[t]>;\n";
                  tinits += "<types[t]> = <atype2vtype(t, thisJGenie)>;\n";
                  done += {t};
                }
            } else {
                bottom-up visit(t) {
                  case s:
                        if (s notin done, s in types) {
                          tdecls += "io.usethesource.vallang.type.Type <types[s]>;\n";
                          tinits += "<types[s]> = <atype2vtype(s, thisJGenie)>;\n";
                          done += {s};
                        }
                    }
            }
        }
        rdecls = "";
        rinits = "";
        for(t <- reified_constants){
               rdecls += "IConstructor <reified_constants[t]>;\n";
               rinits += "<reified_constants[t]> = $RVF.reifiedType(<value2IValue(t, constants)>, <value2IValue(reified_definitions[t], constants)>);\n";
        }       
        return <"<cdecls><tdecls><rdecls>",
                "<cinits><tinits><rinits>"
               >;
    }
    
    bool _isWildCard(str con){
        if(constant2value[con]?){
            return constant2value[con] == "_";
        }
        return false;
    }
    
    void _addExternalRefs(set[MuExp] vars){
        externalRefs += vars;
    }
    
    bool _isExternalRef(MuExp var) = var in externalRefs && var.pos != -1;
    
    void _addLocalRefs(set[MuExp] vars){
        localRefs += vars;
    }
    
    bool _isLocalRef(MuExp var) = var in localRefs;
    
    bool _isRef(MuExp var) = _isExternalRef(var) || _isLocalRef(var);
    
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
    
    bool _usesLocalFunctions(tuple[str name, AType funType, str scope, list[loc] ofunctions, list[loc] oconstructors] overloads){
        return    any(of <- overloads.ofunctions, isContainedIn(currentTModel.definitions[of].defined, currentModuleScope))
               || any(oc <- overloads.oconstructors, isContainedIn(currentTModel.definitions[oc].defined, currentModuleScope));
    }
    
    tuple[str name, list[str] argTypes] getElements(str signature){
        if(/<name:[a-z A-Z 0-9 _]+>(<args:.*>)/ := signature){
           return <name, [tps[0] | str arg <- split(",", args), tps := split(" ", arg)]>;
        }
    }
    
    thisJGenie = 
           jgenie(
                _getModuleName,
                _getModuleLoc,
                _setFunction,
                _getFunction,
                _getFunctionName,
                _isDefinedInCurrentFunction,
                _getType,
                _getImportedModuleName,
                _getATypeAccessor,
                _getAccessor,
                _getDefine,
                _getExternalRefs,
                _collectKwpFormals,
                _collectKwpDefaults,
                _collectRedeclaredKwps,
                _collectDeclaredKwps,
                _setKwpDefaultsName,
                _getKwpDefaultsName,
                _hasCommonKeywordFields,
                _shareType,
                _shareConstant,
                _shareReifiedConstant,
                _getConstants,
                _isWildCard,
                _addExternalRefs,
                _isExternalRef,
                _addLocalRefs,
                _isLocalRef,
                _isRef,
                _newTmp,
                _addImportedLibrary,
                _getImportedLibraries,
                _usesLocalFunctions
            );
     return thisJGenie;
}

// ---- casting ---------------------------------------------------------------

str castArg(AType t, str x) = isValueType(t) ? x : (startsWith(x, "((<atype2javatype(t)>)(") ? x : "((<atype2javatype(t)>)(<x>))");
str cast(AType t, str x) = "(<castArg(t,x)>)";

// --- removing src annos

public &T  unsetR(&T x, str keywordParameter) = visit(x) { 
  case node n => unset(n, keywordParameter)
};
