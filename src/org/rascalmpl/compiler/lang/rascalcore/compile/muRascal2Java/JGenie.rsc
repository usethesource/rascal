@bootstrapParser
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
        list[Keyword] (MuFunction fun) collectKwpFormals,
        lrel[str name, AType atype, MuExp defaultExp] (MuFunction fun) collectKwpDefaults,
        list[str] (MuFunction fun) collectRedeclaredKwps,
        list[str] (MuFunction fun) collectDeclaredKwps,
        bool(AType) hasCommonKeywordFields,
        str(AType atype) shareType,
        str(AType atype) accessType,
        bool(AType atype) isSharedType,
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
    
    for(mname <- tmodels){
        checkAllTypesAvailable(tmodels[mname]);
    }
    
    TModel currentTModel = tmodels[moduleName];
    checkAllTypesAvailable(currentTModel);  // TODO: remove
    loc currentModuleScope = moduleLocs[moduleName];
    set[loc] extending = currentTModel.paths[currentModuleScope, extendPath()];
    rel[loc, loc] importedByExtend = {<i, e> | e <- extending, i <- currentTModel.paths[e, importPath()]};
    str functionName = "$UNKNOWN";
    MuFunction function = muFunction("", "*unknown", avalue(), [], [], [], "", false, true, false, {}, {}, {}, currentModuleScope, [], (), muBlock([]));               
    
    
    map[loc,set[MuExp]] fun2externals = (fun.src : fun.externalRefs | fun <- range(muFunctions), fun.scopeIn != "");
    map[loc,MuFunction] muFunctionsByLoc = (f.src : f | fname <- muFunctions, f := muFunctions[fname]);
    
    allPaths = { *(tmodels[mname].paths) | mname <- tmodels};
    
    allExtendScopes = { b | <a, extendPath(), b> <- allPaths };
    
    extendScopesCurrentModule = { mscope | <currentModuleScope, extendPath(), mscope> <- allPaths };
    importScopesCurrentModule = { mscope | <currentModuleScope, importPath(), mscope> <- allPaths} ;
    flattenedImportScopes = importScopesCurrentModule + { mscope | imp <- importScopesCurrentModule, <imp, extendPath(), mscope> <- allPaths };
    importAndExtendScopes = flattenedImportScopes + extendScopesCurrentModule;
   
    extends = {<a, b> | <a, extendPath(), b> <- allPaths, a in importAndExtendScopes, b in importAndExtendScopes}+;
    
    sortedImportAndExtendScopes =
        sort(importAndExtendScopes,
            bool(loc a, loc b) { return <a, b> in extends /*|| <a, importPath(), b> in allPaths*/; });
    
    JGenie thisJGenie;
   
    loc findDefiningModuleForDef(loc def){
        for(ms <- sortedImportAndExtendScopes){
            if(isContainedIn(def, ms)){
               return ms;
            }
        }
        throw "findDefiningModuleForDef: <def>";
    }
    
    loc findImportForDef(loc def){
        base = findDefiningModuleForDef(def);
        return base;
    }
    
    str _getModuleName(){
        return currentTModel.modelName;
     }   
    loc _getModuleLoc()
        = currentModuleScope;
        
    //private bool isIgnored(MuFunction muFun){
    //    return !isEmpty(domain(muFun.tags) & {"ignore", "Ignore", "ignoreInterpreter", "IgnoreInterpreter"});
    //}
        
    void _setFunction(MuFunction fun){
        function = fun;
        functionName = fun.uniqueName;
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
        tlabel = t.alabel? ? asUnqualifiedName(t.alabel) : "";
        
        // Instantiated adt will allways come from current module
        if(aadt(adtName,params,_) := t, !isEmpty(params)){
            if(any(p <- params, !isTypeParameter(p))){
                return "";
            }
        }
            
        found_defs = {};
        for(Define def <- range(currentTModel.definitions), def.idRole in (dataOrSyntaxRoles + constructorId())){
            // TODO: the following conditions were originally combined with an ||, but the compiler does not like that
            if(isConstructorAType(t1) && t1 := def.defInfo.atype && def.defInfo.atype.alabel == tlabel){
                //if(tlabel == "conditional"){
                //    println("conditional");
                //}
                found_defs += def;
            } else if(aadt(adtName,ps1,_) := t1 && aadt(adtName,ps2,_) := def.defInfo.atype && asubtypeList(ps1, ps2)){
                found_defs += def;
            }
        }
        for(ms <- sortedImportAndExtendScopes){
            for(Define def <- found_defs){
                //println("def: <def>");
                if(isContainedIn(def.scope, ms)){
                    defMod = allLocs2Module[ms];
                    res = defMod == moduleName ? "" : "<_getImportViaExtend(ms, defMod)><module2field(defMod)>.";
                    if(b)println("getTypeAccessor(<t>) =\> <res>)");
                    return res;
                }
            }
        }
        //for(Define def <- found_defs){
        //  println("def: <def>");
        //  for(ms <- sortedImportAndExtendScopes, isContainedIn(def.scope, ms)){
        //    //for(ms <- allLocs2Module, ms in importAndExtendScopes, isContainedIn(def.scope, ms)){
        //        defMod = allLocs2Module[ms];
        //        res = defMod == moduleName ? "" : "<_getImportViaExtend(ms, defMod)><module2field(defMod)>.";
        //        if(b)println("getTypeAccessor(<t>) =\> <res>)");
        //        return res;
        //    }
        //}
        //println("getTypeAccessor(<t>) =\> \"\"");
        return ""; //throw "No accessor found for <t>";
    }
    
    bool importFromSameModule(list[loc] srcs){
        fst = srcs[0].top;
        return all(src <- srcs, fst == src.top) && any(imp <- importScopesCurrentModule, isContainedIn(srcs[0], imp));
    }
    
    @memo
    str _getAccessor(list[loc] srcs){
        assert !isEmpty(srcs);
        // Single src
        if(size(srcs) == 1 || importFromSameModule(srcs)){
            src = srcs[0];
            if(currentTModel.definitions[src]?){
                def = currentTModel.definitions[src];
                if(defType(AType _) := def.defInfo){
                    baseName = asJavaName(def.id);
                    
                    if(isSyntheticFunctionName(def.id)){
                        return baseName;
                    } else if(isContainedIn(def.defined, currentModuleScope)){
                        if(def.scope != currentModuleScope){    // inner function
                            fun = muFunctionsByLoc[def.defined];
                            return isEmpty(fun.scopeIn) ? baseName : "<fun.scopeIn>_<baseName>";
                        }
                        return baseName;
                    } else {
                        res = def.scope in extendScopesCurrentModule ? "$me.<baseName>"
                                                        : "<_getImportedModuleName(def.defined)>.<baseName>";
                        return res;
                    }
                 }
            }
            for(ms <- sortedImportAndExtendScopes, mname := allLocs2Module[ms]){
                if(tmodels[mname].definitions[src]?){
                    def = tmodels[mname].definitions[src];
                    if(defType(AType _) := def.defInfo){
                        baseName = asJavaName(def.id);
                        
                        if(isSyntheticFunctionName(baseName)){
                            return baseName;
                        }
                        if(isContainedIn(def.defined, currentModuleScope)){
                            return baseName;
                        } else {
                            return def.scope in extendScopesCurrentModule ? "$me.<baseName>"
                                                             : "<_getImportedModuleName(def.defined)>.<baseName>";
                        }
                     }
                 }
            }
            throw "No accessor found for <src>";
        }
        // Multiple srcs
    
        unknown = "???_getAccessor_name_not_found_???<srcs>"; 
        name = unknown;                                      // Find of definition
        Define fun_def;
        for(mname <- tmodels){
            if(tmodels[mname].definitions[srcs[0]]?){
                fun_def = tmodels[mname].definitions[srcs[0]];
                name = fun_def.id;
                break;
            }
        }
       
        if(isSyntheticFunctionName(name)){
            return name;
        }
        
        scopeIn = definedInInnerScope(srcs);
        jname = asJavaName(name);
        
        if(scopeIn != ""){
            return "<scopeIn>_<jname>";
        }
        
        if(any(d <- srcs, isContainedIn(d, currentModuleScope))){
            return "$me.<jname>";
        }
        alternative_defined_in_extended_module = any(d <- srcs, ms <- extendScopesCurrentModule, isContainedIn(d, ms));
        if(alternative_defined_in_extended_module){
            return "$me.<jname>";
        }
        
       return jname;
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
    
    list[Keyword] _collectKwpFormals(MuFunction fun){
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
    
    void shareSubtypes(AType atype){
        atype1 = atype; // ensure this is a read only variable; TODO: compiler should do this.
        bottom-up visit(atype){
            case AType t: if(t != atype1){
                if(AType a: aadt(str adtName, list[AType] parameters, SyntaxRole _) := t){
                    acc = _getATypeAccessor(t);
                    res = prefixADT(getUniqueADTName(a)); 
                    type2id[unset(t, "alabel")] = res; //"<acc><res>";
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
    }
    
    str _shareType(AType atype){
        couter = "";
        if(type2id[atype]?){
            return type2id[atype];
        } else if(a: aadt(str adtName, list[AType] parameters, SyntaxRole _) := atype){
            res = prefixADT(getUniqueADTName(a));  
            couter = res;
            type2id[unset(atype, "alabel")] = res;                  
        } else if(c:acons(AType _, list[AType] _, list[Keyword] _)  := atype){
            couter = atype2idpart(c);
            atype = atype[alabel=asUnqualifiedName(atype.alabel)];
            type2id[atype] = couter;
        } else if(overloadedAType(rel[loc def, IdRole role, AType atype] overloads) := atype){
            ovl_lub = (avoid() | alub(it, t) | t <- overloads.atype);
            if(!type2id[ovl_lub]?) {
                ntypes += 1;
                couter = "$T<ntypes>";
                type2id[ovl_lub] = couter;
            }
            shareSubtypes(ovl_lub);
            
            ut = unset(atype, "alabel");
            if(type2id[ut]?) return type2id[ut];
            ntypes += 1;
            couter = "$T<ntypes>";
            type2id[ut] = couter;
        } else {
            ut = unset(atype, "alabel");
            if(type2id[ut]?) return type2id[ut];
            ntypes += 1;
            couter = "$T<ntypes>";
            type2id[ut] = couter;
        }
        shareSubtypes(atype);
       
        return couter;
    }
    
    bool _isSharedType(AType atype){
        return type2id[atype]?;
    }
    
    str _shareConstant(value v) {
        c = -1;
        
        //v = unsetR(v, "src");
        
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
                //e = unsetR(e, "src");
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
        str consinits = "";
        str adtinits_instantiate = "";
        str adtinits = "";
        str adtinits_param = "";
        str kwpTypeDecls = "";
        done = {};
        declared_ADT_names = {};
        parameterized_ADTs = { a | a:aadt(str adtName, list[AType] parameters, SyntaxRole _) <- getADTs(), !isEmpty(parameters), all(p <- parameters, isTypeParameter(p)) };
        instantiated_Cons = { c | /c:acons(AType adtType, list[AType] fields, list[Keyword] kwpFields) := currentTModel.facts + currentTModel.specializedFacts, !isEmpty(adtType.parameters), all(p <- adtType.parameters, !isTypeParameter(p)) };
        
        for(c <- instantiated_Cons){
            _shareType(c);
        }
        // Generate type constants in the right declaration order, such that
        // they are always declared before they are used in the list of type fields
        
        //println("<moduleName>: domain(type2id): <size(domain(type2id))>"); iprintln(domain(type2id),lineLimit=10000);
        //println("type2id:");
        //for(k <- type2id) println("<k>: <type2id[k]>");
        //println("-----------------------------------------");
        
        bool simpler(AType a, AType b) {
            if(/a := b && a != b) return true;
            if(isAtomicAType(a) && !isAtomicAType(b)) return true;
            return false;
        }
        
        sorted_types = sort(domain(type2id), simpler);
        
        //println("sorted_types:"); for(t <- sorted_types) println(t);
        
        for(t <- sorted_types){
            bottom-up visit(t) {
              case AType s:
                    if (s notin done, s in type2id) {
                      if(a:aadt(str adtName, list[AType] parameters, SyntaxRole sr) := s){
                            aname = getUniqueADTName(a);
                            if(aname notin declared_ADT_names){
                                declared_ADT_names += {aname};
                                tdecls += "public final io.usethesource.vallang.type.Type <type2id[s]>;\t/*<s>*/\n";
                                adtdef = "";
                                if(isEmpty(parameters)){
                                     switch(sr){
                                          case dataSyntax():        adtdef = "$TF.abstractDataType($TS, \"<adtName>\")";
                                          case contextFreeSyntax(): adtdef = "new NonTerminalType($RVF.constructor(RascalValueFactory.Symbol_Sort, $VF.string(\"<adtName>\")))";
                                          case lexicalSyntax():     adtdef = "new NonTerminalType($RVF.constructor(RascalValueFactory.Symbol_Lex, $VF.string(\"<adtName>\")))";
                                          case layoutSyntax():      adtdef = "new NonTerminalType($RVF.constructor(RascalValueFactory.Symbol_Layouts, $VF.string(\"<adtName>\")))";
                                          case keywordSyntax():     adtdef = "new NonTerminalType($RVF.constructor(RascalValueFactory.Symbol_Keywords, $VF.string(\"<adtName>\")))";
                                     };
                                     adtinits += "<type2id[s]> = <adtdef>;\n";
                                } else {
                                    if(s in parameterized_ADTs || all(p <- parameters, !isTypeParameter(p))){
                                        params = intercalate(", ", [ type2id[unset(par, "alabel")] | par <- parameters]);
                                        paramsV = "$VF.list(<intercalate(", ", [ atype2IValue(par, ()) | par <- parameters])>)";
                                        switch(sr){
                                             case dataSyntax():        adtdef = "$TF.abstractDataType($TS, \"<adtName>\", <params>)";
                                             case contextFreeSyntax(): adtdef = "new NonTerminalType($RVF.constructor(RascalValueFactory.Symbol_ParameterizedSort, $VF.string(\"<adtName>\"), <paramsV>))";
                                             case lexicalSyntax():     adtdef = "new NonTerminalType($RVF.constructor(RascalValueFactory.Symbol_ParameterizedLex, $VF.string(\"<adtName>\"), <paramsV>))";
                                         }
                                        adtinits_param += "<type2id[s]> = <adtdef>;\n";
                                    } else {
                                        bindings = [];
                                        padt = a;
                                        for(pa <- parameterized_ADTs){
                                            if(pa.adtName == adtName && size(parameters) == size(pa.parameters)) {                                               
                                                for(int i <- index(parameters)){
                                                    if(!type2id[parameters[i]]?){
                                                        println("<parameters[i]> not found");
                                                    }
                                                    bindings += "<type2id[pa.parameters[i]]>, <type2id[parameters[i]]>";
                                                }
                                                padt = pa;
                                                break;
                                            }
                                        }
                                        adtinits_instantiate += "<prefixADT(aname)> = <_getATypeAccessor(padt)><asADTName(adtName)>_<size(parameters)>.instantiate(java.util.Map.of(<intercalate(", ", bindings)>));\n";
                                    }
                                }
                            }
                        } else if(c:acons(AType adtType, list[AType] fields, list[Keyword] kwpFields) := s){
                            isLocal = isEmpty(_getATypeAccessor(s));
                            if(isLocal){
                                cname = atype2idpart(c);                                 
                                hasFieldNames = all(fld <- fields, !isEmpty(fld.alabel));
                                fieldDecls = hasFieldNames ? [ "<atype2vtype(fld,thisJGenie)>, \"<fld.alabel>\"" | fld <- fields ] : [ "<atype2vtype(fld, thisJGenie)>" | fld <- fields ];
                                adt_name = prefixADT(getUniqueADTName(adtType));
                               
                                tdecls += "public final io.usethesource.vallang.type.Type <type2id[s]>;\t/*<s>*/\n";
                                consinits += "<cname> = $TF.constructor($TS, <adt_name>, \"<asUnqualifiedName(c.alabel)>\"<isEmpty(fieldDecls) ? "" : ", <intercalate(", ", fieldDecls)>">);\n";
                                for(kwpField <- kwpFields){
                                    kwpTypeDecls += "$TS.declareKeywordParameter(<cname>,\"<kwpField.fieldType.alabel>\", <_getATypeAccessor(kwpField.fieldType)><atype2vtype(kwpField.fieldType, thisJGenie)>);\n";
                                }
                            }
                        } else {
                            tdecls += "public final io.usethesource.vallang.type.Type <type2id[s]>;\t/*<s>*/\n";
                            tinits += "<type2id[s]> = <atype2vtype(s, thisJGenie)>;\n";
                        }
                        done += {s};
                    }
            }
        }
        
        tinits = adtinits + tinits + adtinits_param + adtinits_instantiate + consinits;
        rdecls = "";
        rinits = "";
        for(t <- reified_constants){
               rdecls += "public final IConstructor <reified_constants[t]>;\t/*<t>*/\n";
               rinits += "<reified_constants[t]> = $RVF.reifiedType(<value2IValue(t, constant2idx)>, <value2IValue(reified_definitions[t], constant2idx)>);\n";
        }       
        return <"<tdecls><rdecls>",
                "<tinits><rinits><kwpTypeDecls>",
                constantlist
               >;
    }
    
    str _accessType(AType tp){
        return "<_getATypeAccessor(tp)><_shareType(tp)>";
    }
    
    bool _isWildCard(str con){
        if(/.*get\(<idx:[0-9]+>\)/ := con){
            i = toInt(idx);
            if(idx2constant[i]?){
                return idx2constant[i] == "_";
            }
        }   
        return false;
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
        return /*var1.fuid != function.uniqueName && */(varIn(var1, function.externalRefs) || varIn(var1, localRefs));
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
                _hasCommonKeywordFields,
                _shareType,
                _accessType,
                _isSharedType,
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
            );
    
     //thisJGenie.shareType(anode([]));   // Add types that can be implicitly defined by lubbing overloaded functions
     //thisJGenie.shareType(anum());
     //thisJGenie.shareType(avalue());
     return thisJGenie;
}

// ---- casting ---------------------------------------------------------------

str castArg(AType t, str x) = isValueAType(t) ? x : (startsWith(x, "((<atype2javatype(t)>)(") ? x : "((<atype2javatype(t)>)(<x>))");
str cast(AType t, str x) = "(<castArg(t,x)>)";

// --- removing src annos

public &T  unsetR(&T x, str keywordParameter) = visit(x) { 
  case node n => unset(n, keywordParameter)
};
