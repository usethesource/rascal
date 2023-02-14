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
        lrel[AType atype, Expression defaultExp] (MuFunction fun) collectKwpFormals,
        lrel[str name, AType atype, MuExp defaultExp] (MuFunction fun) collectKwpDefaults,
        list[str] (MuFunction fun) collectRedeclaredKwps,
        list[str] (MuFunction fun) collectDeclaredKwps,
        void(str name) setKwpDefaultsName,
        str() getKwpDefaultsName,
        bool(AType) hasCommonKeywordFields,
        str(AType atype) shareType,
        str(value con) shareConstant,
        str(Symbol, map[Symbol,Production]) shareReifiedConstant,
        tuple[str,str,list[value]] () getConstants,
        bool (str con) isWildCard,
        void(set[MuExp] evars) addExternalRefs,
        void(set[MuExp] lvars) addLocalRefs,
        bool(MuExp var) isRef,
        bool (MuExp var)varHasLocalScope,
        bool (MuExp var)varHasGlobalScope,
        str(str prefix) newTmp,
        void(str) addImportedLibrary,
        list[str] () getImportedLibraries,
        bool (tuple[str name, AType funType, str scope, list[loc] ofunctions, list[loc] oconstructors] overloads) usesLocalFunctions
        //void (bool on) generatingTests
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
    map[value,int] constant2idx = ();
    map[int,value] idx2constant = ();
    list[value] constantlist = [];
    map[AType,str] type2id = ();
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
    set[loc] extending = currentTModel.paths[currentModuleScope, extendPath()];
    rel[loc, loc] importedByExtend = {<i, e> | e <- extending, i <- currentTModel.paths[e, importPath()]};
    str functionName = "$UNKNOWN";
    MuFunction function = muFunction("", "", avalue(), [], [], [], "", false, true, false, {}, {}, {}, currentModuleScope, [], (), muBlock([]));               
    
    
    map[loc,set[MuExp]] fun2externals = (fun.src : fun.externalRefs | fun <- range(muFunctions), fun.scopeIn != "");
    map[loc,MuFunction] muFunctionsByLoc = (f.src : f | fname <- muFunctions, f := muFunctions[fname]);
    
    extendScopes = { m2loc | <_, extendPath(), m2loc> <- tmodels[moduleName].paths };
    importScopes = { m2loc | <_, importPath(), m2loc> <- tmodels[moduleName].paths};
    importScopes += { m2loc | imp <- importScopes, <imp, extendPath(), m2loc> <- tmodels[moduleName].paths};
    importAndExtendScopes = importScopes + extendScopes;
   
    allPaths = { *(tmodels[mname].paths) | mname <- tmodels};
    sortedImportAndExtendScopes =
        sort(importAndExtendScopes,
            bool(loc a, loc b) { return <a, extendPath(), b> in allPaths; });
   
    extends = {<a, b> | <a, extendPath(), b> <- allPaths, a in importAndExtendScopes, b in importAndExtendScopes}+;
    
    JGenie thisJGenie;
    //bool generatingTests = true;
   
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
        return base;
        //return findLargestExtendingModule(base);
    }
    
    str _getModuleName()
        = currentTModel.modelName;
        
    loc _getModuleLoc()
        = currentModuleScope;
        
    //private bool isIgnored(MuFunction muFun){
    //    return !isEmpty(domain(muFun.tags) & {"ignore", "Ignore", "ignoreInterpreter", "IgnoreInterpreter"});
    //}
        
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
    
    str _getImportViaExtend(loc def, str mname){
        ibe = importedByExtend[def];
        if(isEmpty(ibe)){
            return "";
        }
        edef = getFirstFrom(ibe);
        emod = allLocs2Module[edef];
        if(emod == mname) return "";
        res = "<module2field(emod)>.";
        //println("_getImportViaExtend: <def> ==\> <res>");
        return res;
    }
    
    bool b = false;
    @memo
    str _getATypeAccessor(AType t){
        t1 = unsetR(t, "alabel");
        tlabel = t.alabel? ? getUnqualifiedName(t.alabel) : "";
        
        found_defs = {};
        for(Define def <- range(currentTModel.definitions), def.idRole in (dataOrSyntaxRoles + constructorId())){
            //!isConstructorType(t1) || def.defInfo.atype.alabel == t.alabel
            if((isConstructorType(t1) && t1 := def.defInfo.atype && def.defInfo.atype.alabel == tlabel) ||
               (aadt(adtName,ps1,_) := t1 && aadt(adtName,ps2,_) := def.defInfo.atype && asubtype(ps1, ps2))){
                found_defs += def;
            }
        }
        for(Define def <- found_defs){
            for(ms <- allLocs2Module, isContainedIn(def.scope, ms)){
                defMod = allLocs2Module[ms];
                res = defMod == moduleName ? "" : "<_getImportViaExtend(ms, defMod)><module2field(defMod)>.";
                if(b)println("getTypeAccessor(<t>) =\> <res>)");
                return res;
            }
        }
        if(b)println("getTypeAccessor(<t>) =\> \"\"");
        return ""; //throw "No accessor found for <t>";
    }
    
    @memo
    str _getAccessor(list[loc] srcs){
        // Single src
        if(size(srcs) == 1){
            src = srcs[0];
            if(currentTModel.definitions[src]?){
                def = currentTModel.definitions[src];
                if(defType(AType _) := def.defInfo){
                    baseName = getJavaName(def.id);
                    
                    if(isContainedIn(def.defined, currentModuleScope)){
                        if(def.scope != currentModuleScope){    // inner function
                            fun = muFunctionsByLoc[def.defined];
                            return isEmpty(fun.scopeIn) ? "<isClosureName(baseName) ? "" : ""/*"$me."*/><baseName>" : "<fun.scopeIn>_<baseName>";
                        }
                        return baseName;
                    } else {
                        return def.scope in importScopes ? "<_getImportedModuleName(def.defined)>.<baseName>"
                                                         : "<isClosureName(baseName) ? "" : "$me."><baseName>";
                    }
                 }
            }
            for(ms <- sortedImportAndExtendScopes, mname := allLocs2Module[ms]){
                if(tmodels[mname].definitions[src]?){
                    def = tmodels[mname].definitions[src];
                    if(defType(AType _) := def.defInfo){
                        baseName = getJavaName(def.id);
                        if(isClosureName(baseName)){
                            return baseName;
                        }
                        if(isContainedIn(def.defined, currentModuleScope)){
                            return baseName;
                        } else {
                            return def.scope in importScopes ? "<_getImportedModuleName(def.defined)>.<baseName>"
                                                             : "$me.<baseName>";
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
        jname = getJavaName(name);
        
        if(scopeIn != ""){
            return "<scopeIn>_<jname>";
        }
        
        if(any(d <- srcs, isContainedIn(d, currentModuleScope))){
            for(d <- srcs){
                def = currentTModel.definitions[d];
                if(isConstructorType(def.defInfo.atype)){
                    return jname;
                }
            }
            return "$me.<jname>";
        }
        //
        //return "$me.<name>";
        
       for(ms <- sortedImportAndExtendScopes){
            if(any(d <- srcs, isContainedIn(d, ms))){
                return "<ms in importScopes ? module2field(allLocs2Module[ms]) : "$me">.<jname>";
            }
       }
       
       return "$me.<jname>";
      
       //throw "_getAccessor: <name>, <srcs>";
    }
    
    str definedInInnerScope(list[loc] srcs){
        scopeIn = "";
        for(d <- srcs){
            if(isContainedIn(d, currentModuleScope)){
                if(muFunctionsByLoc[d]?){
                    fun = muFunctionsByLoc[d];
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
            fun = muFunctionsByLoc[src];
            evars = isContainedIn(src, currentModuleScope) ? fun2externals[src] : {};
            return sort([var | var <- evars, var.pos >= 0, var notin fun.formals ]);
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
    
    lrel[AType atype, Expression defaultExp] _collectKwpFormals(MuFunction fun){
        scopes = currentTModel.scopes;
        kwFormals = fun.ftype.kwFormals;
        outer = scopes[fun.src];
        while (muFunctionsByLoc[outer]?){
            fun1 = muFunctionsByLoc[outer];
            kwFormals += fun1.ftype.kwFormals;
            outer = scopes[fun1.src];
        }
        return kwFormals;
    }
    
    lrel[str name, AType atype, MuExp defaultExp] _collectKwpDefaults(MuFunction fun){
        if(isSyntheticFunctionName(fun.name)) return []; // closures, function compositions ...
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
        if(isSyntheticFunctionName(fun.name)) return []; // closures, function compositions ...
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
        if(isSyntheticFunctionName(fun.name)) return []; // closures, function compositions ...
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
    
    AType setScopeInfoType = afunc(
          avoid(),
          [
            aloc(alabel="scope"),
            aadt(
              "ScopeRole",
              [],
              dataSyntax()),
            avalue()
          ],
          []);
    
    str _shareType(AType atype){
        //println("%%%%% shareType: <atype>");
        //atype = unsetR(atype, "alabel");
        couter = "";
        if(type2id[atype]?){
            return type2id[atype];
        } else if(aadt(str adtName, list[AType] _, SyntaxRole _) := atype){
            res = "<_getATypeAccessor(atype)>ADT_<adtName>";
            return res;
        } else {
            ntypes += 1;
            couter = "$T<ntypes>";
            type2id[atype] = couter;
        }
        visit(atype){
            case AType t: {
                if(aadt(str _, list[AType] _, SyntaxRole _) := t){
                        ;
                } else if(acons(AType _, list[AType] _, list[Keyword] _) := t){
                        ;
                } else if(atypeList(_) := t){
                        ;
                } else if(!type2id[t]?){
                    ntypes += 1;
                    c = "$T<ntypes>";
                type2id[t] = c;
                }
            }
        }
        return couter;
    }
    
    str _shareConstant(value v) {
        c = -1;
        
        v = unsetR(v, "src");
        
        if (constant2idx[v]?) {
            idx = constant2idx[v];
          return "((<value2outertype(v)>)$constants.get(<idx>)<inlineComment(v)>)";
        }
        else {
          nconstants += 1;
          c = nconstants; //"$C<nconstants>";
          constant2idx[v] =  c; //"((<value2outertype(v)>)$constants[<c>])";
          constantlist += v;
          idx2constant[c] = v;
        }
        
        if (Symbol _ := v || Production _ := v || Tree _ := v || map[&X,&Y] _ := v) {
            visit (v) {
              case value e: {
                e = unsetR(e, "src");
                if (!constant2idx[e]?, Symbol _ := e || Production _ := e || Tree _ := e) {
                  nconstants += 1;
                  d = nconstants; //"$C<nconstants>";
                  constant2idx[e] = d; //"((<value2outertype(v)>)$constants[<d>])";
                  constantlist += e;
                  idx2constant[d] = e;
                }
              }
            }
        }
        
        return "((<value2outertype(v)>)$constants.get(<c>)<inlineComment(v)>)";
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
    
    tuple[str,str, list[value]] _getConstants() {
        str tdecls = "";
        str tinits = "";
        done = {};
        // Generate type constants in the right declaration order, such that
        // they are always declared before they are used in the list of type fields
        for(t <- type2id){
            bottom-up visit(t) {
              case AType s:
                    if (s notin done, s in type2id) {
                      tdecls += "final io.usethesource.vallang.type.Type <type2id[s]>;\t/*<s>*/\n";
                      tinits += "<type2id[s]> = <atype2vtype(s, thisJGenie)>;\n";
                      done += {s};
                    }
            }
        }
        rdecls = "";
        rinits = "";
        for(t <- reified_constants){
               rdecls += "final IConstructor <reified_constants[t]>;\t/*<t>*/\n";
               rinits += "<reified_constants[t]> = $RVF.reifiedType(<value2IValue(t, constant2idx)>, <value2IValue(reified_definitions[t], constant2idx)>);\n";
        }       
        return <"<tdecls><rdecls>",
                "<tinits><rinits>",
                constantlist
               >;
    }
    
    bool _isWildCard(str con){
        if(/.*get\(<idx:[0-9]+>\)/ := con){
            i = toInt(idx);
            if(idx2constant[i]?){
                return idx2constant[i] == "_";
            }
            return false;
        } else {
            return false;
        }
    }
    
    void _addExternalRefs(set[MuExp] vars){
        externalRefs += vars;
    }
        
    void _addLocalRefs(set[MuExp] vars){
        localRefs += vars;
    }
   
   bool varIn(MuExp var, set[MuExp] refs)
        = !isEmpty(refs) 
           && any(er <- refs, er.name == var.name, er.pos == var.pos, er.fuid == var.fuid, var.pos != -1);
    
    bool _isRef(MuExp var) { 
        var1 = unsetRec(var, "alabel"); 
        return  varIn(var1, function.externalRefs) || varIn(var1, localRefs);
    }
    
    bool _varHasLocalScope(MuExp var) = var.pos >= 0 && var.fuid == function.uniqueName;
    bool _varHasGlobalScope(MuExp var) = var.pos < 0;
    
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
    
    //tuple[str name, list[str] argTypes] getElements(str signature){
    //    if(/<name:[a-z A-Z 0-9 _]+>(<args:.*>)/ := signature){
    //       return <name, [tps[0] | str arg <- split(",", args), tps := split(" ", arg)]>;
    //    }
    //}
    
    //void _generatingTests(bool status){
    //    generatingTests = status;
    //}
    
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
                _addLocalRefs,
                _isRef,
                _varHasLocalScope,
                _varHasGlobalScope,
                _newTmp,
                _addImportedLibrary,
                _getImportedLibraries,
                _usesLocalFunctions
               // _generatingTests
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
