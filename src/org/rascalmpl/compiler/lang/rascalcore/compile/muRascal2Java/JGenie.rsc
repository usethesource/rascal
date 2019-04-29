module lang::rascalcore::compile::muRascal2Java::JGenie

import lang::rascal::\syntax::Rascal;

import List;
import Set;
import IO;
import String;
import Map;
import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::compile::muRascal2Java::CodeGen;
import lang::rascalcore::compile::util::Names;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;

import lang::rascalcore::compile::muRascal2Java::Conversions;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::BasicRascalConfig;
extend analysis::typepal::TypePal;

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
        str (loc src) getAccessor,
        str (loc src) getAccessorInResolver,
        Define (loc src) getDefine,
        list[MuExp] (loc src) getExternalVars,
        void(str name) setKwpDefaults,
        str() getKwpDefaults,
        str(AType atype) shareType,
        str(value con) shareConstant,
        str(AType, map[AType,set[AType]]) shareATypeConstant,
        str () getConstants,
        bool (str con) isWildCard,
        void(list[MuExp] evars) addExternalVars,
        bool (MuExp exp) isExternalVar,
        str(str prefix) newTmp,
        void(str) addImportedLibrary,
        list[str] () getImportedLibraries,
        void (tuple[str name, AType funType, str scope, list[loc] ofunctions, list[loc] oconstructors] overloads) addResolver,
        bool (tuple[str name, AType funType, str scope, list[loc] ofunctions, list[loc] oconstructors] overloads) isResolved,
        bool (tuple[str name, AType funType, str scope, list[loc] ofunctions, list[loc] oconstructors] overloads) usesLocalFunctions
      )
    ;
    
JGenie makeJGenie(str moduleName, map[str,TModel] tmodels, map[str,loc] moduleLocs, map[str, MuFunction] muFunctions){
    str kwpDefaults = "$kwpDefaults";
    map[value,str] constants = ();
    map[str,value] constant2value = ();
    map[AType,str] types = ();
    int nconstants = -1;
    int ntypes = -1;
    
    map[AType,str] atype_constants = ();
    map[AType,map[AType,set[AType]]] atype_definitions = ();
    map[str,AType] atype_constant2atype = ();
    int ntconstants = -1;
    
    set[MuExp] externalVars = {};
    int ntmps = -1;
    set[str] importedLibraries = {};
    
    map[str, AType] declaredVars = ();
    TModel currentModule = tmodels[moduleName];
    loc currentModuleScope = moduleLocs[moduleName];
    set[tuple[str name, AType funType, str scope, list[loc] ofunctions, list[loc] oconstructors]] resolvers = {};
    str functionName = "$UNKNOWN";
    MuFunction function;
    
    map[loc,list[MuExp]] fun2externals = (fun.src : fun.externalVars  | fun <- range(muFunctions));
    
    iprintln(fun2externals);
   
    str _getModuleName()
        = currentModule.modelName;
        
    loc _getModuleLoc()
        = currentModuleScope;
        
    void _setFunction(MuFunction fun){
        function = fun;
        qname = replaceAll(fun.qname, "::", "_");
        idx = findFirst(qname, "$CLOSURE");
        if(idx >= 0){
            functionName = qname[idx ..];
            idx = findFirst(functionName, "_");
            functionName = functionName[ .. idx];
        } else { 
            idx = findFirst(qname, "$");    // preserve $ as first characters (as oppsoed to separator with module name 
            functionName = idx > 0 ? qname[idx+1 .. ] : qname;
        }
    }
    
    MuFunction _getFunction()
        = function;
    
    str _getFunctionName()
        = functionName;
    
    AType _getType(loc src)
        = currentModule.facts[src];
        
    str getImportedModuleName(loc def){
        for(mname <- moduleLocs){
            if(containedIn(def, moduleLocs[mname])){
                return module2field(mname);
            }
        }
        throw "getImportedModuleName<def>";
    }
        
    str _getAccessor(loc src){
        for(mname <- tmodels){
            if(tmodels[mname].definitions[src]?){
                def = tmodels[mname].definitions[src];
                if(defType(AType tp) := def.defInfo){
                    descriptor = atype2idpart(tp);
                    baseName = getJavaName(def.id);
                    if(containedIn(def.defined, currentModuleScope)){
                        return startsWith(baseName, "$CLOSURE") ? baseName : "$me.<baseName>_<descriptor>";
                    } else {
                        return startsWith(baseName, "$CLOSURE") ? baseName : "<getImportedModuleName(def.defined)>.<baseName>";
                    }
                 }
             }
        }
        //for(str fname <- muFunctions){
        //    if(muFunctions[fname].src == src){
        //        return fname;
        //    }
        //}
        throw "No accessor found for <src>";
    }
    
    str _getAccessorInResolver(loc src){
        for(mname <- tmodels){
            if(tmodels[mname].definitions[src]?){
                def = tmodels[mname].definitions[src];
                if(defType(AType tp) := def.defInfo){
                    baseName = "<getJavaName(def.id)>_<def.defined.begin.line>_<def.defined.end.line>";
                    if(containedIn(def.defined, currentModuleScope)){
                        return baseName;
                    } else {
                        return "<getImportedModuleName(src)>.<baseName>";
                    }
                 }
             }
        }
        throw "getAccessorInResolver <src>";
    }
    
    Define _getDefine(loc src){
        for(mname <- tmodels){
                if(tmodels[mname].definitions[src]?){
                    return tmodels[mname].definitions[src];
                }
        }
        throw "getDefine <src>";
    }
    
    list[MuExp] _getExternalVars(loc src){
        return containedIn(src, currentModuleScope) ? fun2externals[src] : [];
    
        //extVarDefs = {};
        //for(mname <- tmodels){
        //    tm = tmodels[mname];
        //    useDef = tm.useDef;
        //    definitions = tm.definitions;
        //    for(<u, d> <- useDef, containedIn(u, src), definitions[d]?, Define def := definitions[d], def.idRole == variableId(), !containedIn(def.scope, src)){
        //        extVarDefs += <def.id, d>;
        //    }
        //}
        //return sort(toList(extVarDefs));
    }
    
    bool _isDefinedInCurrentFunction(MuExp var){
        definitions = currentModule.definitions;
        for(d <- definitions, def := definitions[d], var.name == def.id, containedIn(def.scope, function.src), def.idRole in variableRoles){
            return true;
        }
        return false;
    }
    
    void _setKwpDefaults(str name){
        kwpDefaults = name;
    }
    
    str _getKwpDefaults() = kwpDefaults;
    
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
    
    str _shareATypeConstant(AType t, map[AType,set[AType]] definitions){
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
               'private static final <value2outertype(v)> <constants[v]> = <value2IValue(v)>;
               '<}>
               '<for(t <- types){>
               'private static final Type <types[t]> = <atype2typestore(t)>;
               '<}>
               '<for(t <- atype_constants){>
               'private static final IConstructor <atype_constants[t]> = <atype2IValue(areified(t), atype_definitions[t])>;
               '<}>";
    }
    
    bool _isWildCard(str con){
        if(constant2value[con]?){
            return constant2value[con] == "_";
        }
        return false;
    }
    
    void _addExternalVars(list[MuExp] vars){
        externalVars += toSet(vars);
    }
    
    bool _isExternalVar(MuExp var) = var in externalVars;
    
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
    
    void _addResolver(tuple[str name, AType funType, str scope, list[loc] ofunctions, list[loc] oconstructors] overloads){
        resolvers += overloads;
    }
    bool _isResolved(tuple[str name, AType funType, str scope, list[loc] ofunctions, list[loc] oconstructors] overloads){
        return overloads in resolvers;
    }
    
    bool _usesLocalFunctions(tuple[str name, AType funType, str scope, list[loc] ofunctions, list[loc] oconstructors] overloads){
        return    any(of <- overloads.ofunctions, containedIn(currentModule.definitions[of].defined, currentModuleScope))
               || any(oc <- overloads.oconstructors, containedIn(currentModule.definitions[oc].defined, currentModuleScope));
    }
    
    return jgenie(
                _getModuleName,
                _getModuleLoc,
                _setFunction,
                _getFunction,
                _getFunctionName,
                _isDefinedInCurrentFunction,
                _getType,
                _getAccessor,
                _getAccessorInResolver,
                _getDefine,
                _getExternalVars,
                _setKwpDefaults,
                _getKwpDefaults,
                _shareType,
                _shareConstant,
                _shareATypeConstant,
                _getConstants,
                _isWildCard,
                _addExternalVars,
                _isExternalVar,
                _newTmp,
                _addImportedLibrary,
                _getImportedLibraries,
                _addResolver,
                _isResolved,
                _usesLocalFunctions
            );
}

// ---- casting ---------------------------------------------------------------

str castArg(AType t, str x) = startsWith(x, "((<atype2javatype(t)>)(") ? x : "(<atype2javatype(t)>)(<x>)";
str cast(AType t, str x) = "(<castArg(t,x)>)";
//
//// ---- atype 2 java type -----------------------------------------------------
//
//str atype2javatype(abool())                 = "IBool";
//str atype2javatype(aint())                  = "IInteger";
//str atype2javatype(areal())                 = "IReal";
//str atype2javatype(arat())                  = "IRational";
//str atype2javatype(anum())                  = "INumber";
//str atype2javatype(astr())                  = "IString";
//str atype2javatype(aloc())                  = "ISourceLocation";
//str atype2javatype(adatetime())             = "IDateTime";
//str atype2javatype(alist(AType t))          = "IList";
//str atype2javatype(aset(AType t))           = "ISet";
//str atype2javatype(arel(AType ts))          = "ISet";
//str atype2javatype(alrel(AType ts))         = "IList";
//str atype2javatype(atuple(AType ts))        = "ITuple";
//str atype2javatype(amap(AType d, AType r))  = "IMap";
//
//
//
//str atype2javatype(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
//                                            = "FunctionInstance0\<<atype2javatype(ret)>\>"
//                                              when isEmpty(formals);
//str atype2javatype(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
//                                            = "FunctionInstance<size(formals)>\<<atype2javatype(ret)>, <intercalate(", ", [atype2javatype(f) | f <- formals])>\>"
//                                              when !isEmpty(formals);
// 
//str atype2javatype(anode(list[AType fieldType] fields)) 
//                                            = "INode";
//str atype2javatype(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) 
//                                            = "IConstructor";
//
//str atype2javatype(t: acons(AType adt, list[AType fieldType] fields, lrel[AType fieldType, Expression defaultExp] kwFields))
//                                            = "IConstructor";
//                 
//str atype2javatype(aparameter(str pname, AType bound)) 
//                                            = atype2javatype(bound);
//str atype2javatype(areified(AType atype))   = "Type";
//
//str atype2javatype(avalue())                = "IValue";
//
//default str atype2javatype(AType t) { throw "atype2javatype: cannot handle <t>"; }
//
//// ---- Convert AType to a descriptor that can be used in a Java identifier
//
//str atype2idpart(avoid())                 = "void";
//str atype2idpart(abool())                 = "bool";
//str atype2idpart(aint())                  = "int";
//str atype2idpart(areal())                 = "real";
//str atype2idpart(arat())                  = "rat";
//str atype2idpart(anum())                  = "num";
//str atype2idpart(astr())                  = "str";
//str atype2idpart(aloc())                  = "loc";
//str atype2idpart(adatetime())             = "datetime";
//str atype2idpart(alist(AType t))          = "list_<atype2idpart(t)>";
//str atype2idpart(aset(AType t))           = "set_<atype2idpart(t)>";
//str atype2idpart(arel(AType ts))          = "rel_<atype2idpart(ts)>";
//str atype2idpart(alrel(AType ts))         = "listrel_<atype2idpart(ts)>";
//str atype2idpart(atuple(AType ts))        = "tuple_<atype2idpart(ts)>";
//str atype2idpart(amap(AType d, AType r))  = "map_<atype2idpart(d)>_<atype2idpart(r)>";
//
//str atype2idpart(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
//                                              = "<atype2idpart(ret)>_<intercalate("_", [atype2idpart(f) | f <- formals])>";
//
//str atype2idpart(anode(list[AType fieldType] fields))
//                                              = "node";
//
//
//str atype2idpart(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) 
//                                              = getJavaName(adtName);
//                                              
//str atype2idpart(t:acons(AType adt, list[AType fieldType] fields, lrel[AType fieldType, Expression defaultExp] kwFields))
//                                              = "$<getJavaName(adt.adtName)><t.label? ? "_" + getJavaName(t.label) : "">_<intercalate("_", [atype2idpart(f) | f <- fields])>";
//
//str atype2idpart(overloadedAType(rel[loc, IdRole, AType] overloads)){
//    resType = avoid();
//    formalsType = avoid();
//    for(<def, idrole, tp> <- overloads){
//        resType = alub(resType, getResult(tp));
//        formalsType = alub(formalsType, atypeList(getFormals(tp)));
//    }
//    ftype = atypeList(atypes) := formalsType ? afunc(resType, formalsType.atypes, []) : afunc(resType, [formalsType], []);
//    return atype2idpart(ftype);
//}
//
//str atype2idpart(atypeList(list[AType] ts)) = intercalate("_", [atype2idpart(t) | t <- ts]);
//
//str atype2idpart(aparameter(str pname, AType bound)) = "P<avalue() := bound ? "" : atype2idpart(bound)>"; 
//str atype2idpart(areified(AType atype))   = "reified_<atype2idpart(atype)>";
//str atype2idpart(avalue())                = "value";
//
//default str atype2idpart(AType t) { throw "atype2idpart: cannot handle <t>"; }
//
//// ---- convert an AType to an IValue (i.e., reify the AType)
//
//str lab(AType t) = t.label? ? value2IValue(t.label) : "";
//str lab2(AType t) = t.label? ? ", <value2IValue(t.label)>" : "";
//
//str atype2IValue(at:avoid(), _)              = "avoid(<lab(at)>)";
//str atype2IValue(at:abool(), _)              = "abool(<lab(at)>)";
//str atype2IValue(at:aint(), _)               = "aint(<lab(at)>)";
//str atype2IValue(at:areal(), _)              = "areal(<lab(at)>)";
//str atype2IValue(at:arat(), _)               = "arat(<lab(at)>)";
//str atype2IValue(at:anum(), _)               = "anum(<lab(at)>)";
//str atype2IValue(at:astr(), _)               = "astr(<lab(at)>)";
//str atype2IValue(at:aloc(), _)               = "aloc(<lab(at)>)";
//str atype2IValue(at:adatetime(), _)          = "adatetime(<lab(at)>)";
//
//str atype2IValue(at:alist(AType t), map[AType, set[AType]] defs)          = "alist(<atype2IValue(t, defs)><lab2(at)>)";
//str atype2IValue(at:abag(AType t), map[AType, set[AType]] defs)           = "abag(<atype2IValue(t, defs)><lab2(at)>)";
//str atype2IValue(at:aset(AType t), map[AType, set[AType]] defs)           = "aset(<atype2IValue(t, defs)><lab2(at)>)";
//str atype2IValue(at:arel(AType ts), map[AType, set[AType]] defs)          = "arel(<atype2IValue(ts, defs)><lab2(at)>)";
//str atype2IValue(at:alrel(AType ts), map[AType, set[AType]] defs)         = "alrel(<atype2IValue(ts, defs)><lab2(at)>)";
//
//str atype2IValue(at:atuple(AType ts), map[AType, set[AType]] defs)        = "atuple(<atype2IValue(ts, defs)><lab2(at)>)";
//str atype2IValue(at:amap(AType d, AType r), map[AType, set[AType]] defs)  = "amap(<atype2IValue(d, defs)>,<atype2IValue(r, defs)><lab2(at)>)"; // TODO: complete from here
//
//str atype2IValue(at:afunc(AType ret, list[AType] formals, list[Keyword] kwFormals), map[AType, set[AType]] defs)
//                                              = "<atype2IValue(ret)>_<intercalate("_", [atype2IValue(f) | f <- formals])>";
//str atype2IValue(at:anode(list[AType fieldType] fields), map[AType, set[AType]] defs) = "anode(<lab(at)>)";
//str atype2IValue(at:aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole), map[AType, set[AType]] defs) =
//            "aadt(<value2IValue(adtName)>, <atype2IValue(parameters,defs)>, <getName(syntaxRole)>)";
//str atype2IValue(at:acons(AType adt, list[AType fieldType] fields, lrel[AType fieldType, Expression defaultExp] kwFields), map[AType, set[AType]] defs)
//                                              = "acons(<atype2IValue(adt, defs)>, <atype2IValue(fields, defs)>, <atype2IValue(kwFields,defs)><lab2(at)>)";
//str atype2IValue(overloadedAType(rel[loc, IdRole, AType] overloads)){
//    resType = avoid();
//    formalsType = avoid();
//    for(<def, idrole, tp> <- overloads){
//        resType = alub(resType, getResult(tp));
//        formalsType = alub(formalsType, atypeList(getFormals(tp)));
//    }
//    ftype = atypeList(atypes) := formalsType ? afunc(resType, formalsType.atypes, []) : afunc(resType, [formalsType], []);
//    return atype2IValue(ftype);
//}
//
//str atype2IValue(at:aparameter(str pname, AType bound), map[AType, set[AType]] defs) = "aparameter(<atype2IValue(bound)>)"; 
//str atype2IValue(at:aprod(AProduction production), map[AType, set[AType]] defs) = "aprod(<tree2IValue(production, defs)>";
//str atype2IValue(at:areified(AType atype), map[AType, set[AType]] definitions) = "reifiedAType(<atype2IValue(atype, definitions)>, <defs(definitions)>)";
//str atype2IValue(at:avalue(), _)                = "avalue(<lab(at)>)";
////default str atype2IValue(AType t, map[AType, set[AType]] defs) { throw "atype2IValue: cannot handle <t>"; }
//
//str atype2IValue(list[AType] ts, map[AType, set[AType]] defs) = "$VF.list(<intercalate(", ", [atype2IValue(t,defs) | t <- ts])>)";
//str atype2IValue(lrel[AType fieldType,Expression defaultExp] ts, map[AType, set[AType]] defs) = "$VF.list(<intercalate(", ", [atype2IValue(t.fieldType,defs) | t <- ts])>)";
//
//str atype2IValue(set[AType] ts, map[AType, set[AType]] defs) = "$VF.set(<intercalate(", ", [atype2IValue(t,defs) | t <- ts])>)";
//
//str defs(map[AType, set[AType]] defs) {
//    return "buildMap(<intercalate(", ", ["<atype2IValue(k,defs)>, $VF.set(<intercalate(", ", [ atype2IValue(elem,defs) | elem <- defs[k] ])>)" | k <- defs ])>)";
//}
//
//// ---- Associativity ---------------------------------------------------------
//
//str tree2IValue(\left(), map[AType, set[AType]] defs) = "left()";
//str tree2IValue(\right(), map[AType, set[AType]] defs) = "right()";
//str tree2IValue(\assoc(), map[AType, set[AType]] defs) = "assoc()";
//str tree2IValue(\non-assoc(), map[AType, set[AType]] defs) = "non_assoc()";
//
//// ---- Attr ------------------------------------------------------------------
//
//str tree2IValue(\tag(value v),  map[AType, set[AType]] defs) = "tag(<value2IValue(v)>)";
//str tree2IValue(\assoc(Associativity \assoc),  map[AType, set[AType]] defs) = "assoc(<tree2IValue(\assoc, defs)>)";
//str tree2IValue(\bracket(),  map[AType, set[AType]] defs) = "bracket())";
//
//// ---- Tree ------------------------------------------------------------------
//
//str tree2IValue(tr:appl(AProduction aprod, list[Tree] args), map[AType, set[AType]] defs)
//    = tr.src? ? "appl(<tree2IValue(aprod, defs)>, <tree2IValue(args, defs)>, <value2IValue(tr.src)>)"
//              : "appl(<tree2IValue(aprod, defs)>, <tree2IValue(args, defs)>)";
//
//str tree2IValue(cycle(AType asymbol, int cycleLength), map[AType, set[AType]] defs)
//    = "cycle(<atype2IValue(asymbol, defs)>, <value2IValue(cycleLength)>)";
//
//str tree2IValue(amb(set[Tree] alternatives), map[AType, set[AType]] defs)
//    = "amb(<tree2IValue(alternatives)>)";
// 
//str tree2IValue(char(int character), map[AType, set[AType]] defs)
//    = "tchar(<value2IValue(character)>)";
//    
//// ---- SyntaxRole ------------------------------------------------------------
//
//str tree2IValue(SyntaxRole sr, map[AType, set[AType]] defs) = "<sr>";
//   
//// ---- AProduction -----------------------------------------------------------
//
//str tree2IValue(\choice(AType def, set[AProduction] alternatives), map[AType, set[AType]] defs)
//    = "choice(<atype2IValue(def, defs)>, <tree2IValue(alternatives, defs)>)";
//
//str tree2IValue(tr:prod(AType def, list[AType] asymbols), map[AType, set[AType]] defs){
//    base = "prod(<atype2IValue(def, defs)>, <atype2IValue(asymbols, defs)>";
//    kwds = tr.attributes? ? ", <tree2IValue(tr.attributes, defs)>" : "";
//    if(tr.src?) kwds += ", <value2IValue(tr.src)>";
//    return base + kwds + ")";
//}
//
//str tree2IValue(regular(AType def), map[AType, set[AType]] defs)
//    = "regular(<atype2IValue(def, defs)>)";
//
//str tree2IValue(error(AProduction prod, int dot), map[AType, set[AType]] defs)
//    = "error(<tree2IValue(prod, defs)>, <value2IValue(dot)>)";
//
//str tree2IValue(skipped(), map[AType, set[AType]] defs)
//    = "skipped()";
//    
//str tree2IValue(\priority(AType def, list[AProduction] choices), map[AType, set[AType]] defs)
//    = "priority(<atype2IValue(def, defs)>, <tree2IValue(choices, defs)>)";
//    
//str tree2IValue(\associativity(AType def, Associativity \assoc, set[AProduction] alternatives), map[AType, set[AType]] defs)
//    = "associativity(<atype2IValue(def, defs)>, <tree2IValue(\assoc, defs)>, <tree2IValue(alternatives, defs)>)";
//
//str tree2IValue(\others(AType def) , map[AType, set[AType]] defs)
//    = "others(<atype2IValue(def, defs)>)";
//
//str tree2IValue(\reference(AType def, str cons), map[AType, set[AType]] defs)
//    = "reference(<atype2IValue(def, defs)>, <value2IValue(cons)>)";
//    
//// ---- ACharRange ------------------------------------------------------------
//str tree2IValue(range(int begin, int end), map[AType, set[AType]] defs)
//    = "range(<value2IValue(begin)>, <value2IValue(end)>)";
//    
//// ---- AType extensions for parse trees --------------------------------------
//str atype2IValue(AType::lit(str string), map[AType, set[AType]] defs)
//    = "lit(<value2IValue(string)>)";
//
//str atype2IValue(AType::cilit(str string), map[AType, set[AType]] defs)
//    = "cilit(<value2IValue(string)>)";
//
//str atype2IValue(AType::\char-class(list[ACharRange] ranges), map[AType, set[AType]] defs)
//    = "char_class(<tree2IValue(ranges)>)";    
// 
//
//str atype2IValue(AType::\empty(), map[AType, set[AType]] defs)
//    = "empty()";     
//
//str atype2IValue(AType::\opt(AType symbol), map[AType, set[AType]] defs)
//    = "opt(<atype2IValue(symbol, defs)>)";     
//
//str atype2IValue(AType::\iter(AType symbol), map[AType, set[AType]] defs)
//    = "iter(<atype2IValue(symbol, defs)>)";     
//
//str atype2IValue(AType::\iter-star(AType symbol), map[AType, set[AType]] defs)
//    = "iter_star(<atype2IValue(symbol, defs)>)";   
//
//str atype2IValue(AType::\iter-seps(AType symbol, list[AType] separators), map[AType, set[AType]] defs)
//    = "iter_seps(<atype2IValue(symbol, defs)>, <atype2IValue(separators, defs)>)";     
// 
//str atype2IValue(AType::\iter-star-seps(AType symbol, list[AType] separators), map[AType, set[AType]] defs)
//    = "iter_star_seps(<atype2IValue(symbol, defs)>, <atype2IValue(separators, defs)>)";   
//    
//str atype2IValue(AType::\alt(set[AType] alternatives) , map[AType, set[AType]] defs)
//    = "alt(<atype2IValue(alternatives, defs)>)";     
//
//str atype2IValue(AType::\seq(list[AType] symbols) , map[AType, set[AType]] defs)
//    = "seq(<atype2IValue(symbols, defs)>)";     
// 
//str atype2IValue(AType::\start(AType symbol), map[AType, set[AType]] defs)
//    = "start(<atype2IValue(symbol, defs)>)";   
//
//str atype2IValue(AType::\conditional(AType symbol, set[ACondition] conditions), map[AType, set[AType]] defs)
//    = "conditional(<atype2IValue(symbol, defs)>, <tree2IValue(conditions, defs)>)";   
//    
//// ---- ACondition ------------------------------------------------------------
//
//str tree2IValue(\follow(AType atype), map[AType, set[AType]] defs)
//    = "follow(<atype2IValue(atype, defs)>)";   
//
//str tree2IValue(\not-follow(AType atype), map[AType, set[AType]] defs)
//    = "not_follow(<atype2IValue(atype, defs)>)";
//    
//str tree2IValue(\precede(AType atype), map[AType, set[AType]] defs)
//    = "precede(<atype2IValue(atype, defs)>)";  
//
//str tree2IValue(\not-precede(AType atype), map[AType, set[AType]] defs)
//    = "not_precede(<atype2IValue(atype, defs)>)"; 
//    
//str tree2IValue(\delete(AType atype), map[AType, set[AType]] defs)
//    = "delete(<atype2IValue(atype, defs)>)"; 
//    
//str tree2IValue(\at-column(int column), map[AType, set[AType]] defs)
//    = "at_column(<value2IValue(column)>)";  
//    
//str tree2IValue(\begin-of-line(), map[AType, set[AType]] defs)
//    = "begin_of_line()";
//    
//str tree2IValue(\end-of-line(), map[AType, set[AType]] defs)
//    = "end_of_line()"; 
//    
//str tree2IValue(\excepte(str label), map[AType, set[AType]] defs)
//    = "except(<value2IValue(label)>)";              
//                  
////---- list/set wrappers for some parse tree constructs
//
//str tree2IValue(list[Tree] trees, map[AType, set[AType]] defs)
//    = "$VF.list(<intercalate(", ", [ tree2IValue(tr, defs) | tr <- trees ])>)";
//    
//str tree2IValue(set[Tree] trees, map[AType, set[AType]] defs)
//    = "$VF.set(<intercalate(", ", [ tree2IValue(tr, defs) | tr <- trees ])>)";
//    
//str tree2IValue(set[AProduction] prods, map[AType, set[AType]] defs)
//    = "$VF.set(<intercalate(", ", [ tree2IValue(pr, defs) | pr <- prods ])>)";    
//    
//str tree2IValue(set[Attr] attrs, map[AType, set[AType]] defs)
//    = "$VF.set(<intercalate(", ", [ tree2IValue(a, defs) | a <- attrs ])>)";   
//    
//str tree2IValue(set[ACondition] conds, map[AType, set[AType]] defs)
//    = "$VF.set(<intercalate(", ", [ tree2IValue(c, defs) | c <- conds ])>)"; 
//    
//str tree2IValue(list[ACharRange] ranges, map[AType, set[AType]] defs)
//    = "$VF.list(<intercalate(", ", [ tree2IValue(r, defs) | r <- ranges ])>)"; 
//
//// ---- Get the outermost type of an AType (used for names of primitives)
//
//str getOuter(avoid())                 = "avoid";
//str getOuter(abool())                 = "abool";
//str getOuter(aint())                  = "aint";
//str getOuter(areal())                 = "areal";
//str getOuter(arat())                  = "arat";
//str getOuter(anum())                  = "anum";
//str getOuter(astr())                  = "astr";
//str getOuter(aloc())                  = "aloc";
//str getOuter(adatetime())             = "adatetime";
//str getOuter(alist(AType t))          = "alist";
//str getOuter(aset(AType t))           = "aset";
//str getOuter(arel(AType ts))          = "aset";
//str getOuter(alrel(AType ts))         = "alist";
//str getOuter(atuple(AType ts))        = "atuple";
//str getOuter(amap(AType d, AType r))  = "amap";
//str getOuter(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
//                                      = "afunc";
//str getOuter(anode(list[AType fieldType] fields)) 
//                                      = "anode";
//str getOuter(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) = "aadt";
//str getOuter(t:acons(AType adt, list[AType fieldType] fields, lrel[AType fieldType, Expression defaultExp] kwFields))
//                                      = "acons";
//str getOuter(aparameter(str pname, AType bound)) 
//                                      = getOuter(bound);
//str getOuter(avalue())                = "avalue";
//default str getOuter(AType t)         = "avalue";
//
//// ---- Convert an AType to a Java method that tests for that AType.
//
//str atype2istype(avoid())                 = "isBottom";
//str atype2istype(abool())                 = "isBool";
//str atype2istype(aint())                  = "isInteger";
//str atype2istype(areal())                 = "isReal";
//str atype2istype(arat())                  = "isRational";
//str atype2istype(anum())                  = "isNumber";
//str atype2istype(astr())                  = "isString";
//str atype2istype(aloc())                  = "isSourceLocation";
//str atype2istype(adatetime())             = "isDateTime";
//str atype2istype(alist(AType t))          = "isList";
//str atype2istype(aset(AType t))           = "isSet";
//str atype2istype(arel(AType ts))          = "isRelation";
//str atype2istype(alrel(AType ts))         = "isListRelation";
//str atype2istype(atuple(AType ts))        = "isTuple";
//str atype2istype(amap(AType d, AType r))  = "isMap";
//
//str atype2istype(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
//                                          = "isExternalType";
//str atype2istype(anode(list[AType fieldType] fields)) 
//                                          = "isNode";
//str atype2istype(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) 
//                                          = "isAbstractData";
//str atype2istype(t: acons(AType adt, list[AType fieldType] fields, lrel[AType fieldType, Expression defaultExp] kwFields))
//                                          = "isConstructor";
//str atype2istype(overloadedAType(rel[loc, IdRole, AType] overloads))
//                                          = "isOverloaded";
//str atype2istype(aparameter(str pname, AType bound)) = atype2istype(bound);
//str atype2istype(areified(AType atype))   = "isReified";    // TODO
//str atype2istype(avalue())                = "isTop";
//default str atype2istype(AType t)         { throw "atype2istype: cannot handle <t>"; }
//
//// ----
//
//// TODO cover all cases
//
//str escapeForJ(str s){
//   n = size(s);
//   i = 0;
//   res = "";
//   while(i < n){
//    c = s[i];
//    switch(c){
//        case "\b": res += "\\b";
//        case "\t": res += "\\t";
//        case "\n": res += "\\f";
//        case "\r": res += "\\r";
//        case "\'": res += "\\\'";
//        case "\"": res += "\\\"";
//        case "\\": if(i+1 < n){ 
//                        c1 = s[i+1];
//                        i += 1;
//                        if(c1 in {"b", "t","n","r","\'", "\"", "\\"}){
//                            res += "<c><c1>";
//                        } else {
//                            res += c1;
//                        }
//                    } else {
//                        res += c;
//                    }
//        default: res +=  c;
//     }
//     i += 1;
//   }
//   return res;
//}
//    
//// ---- Convert a Rascal value to the equivalent IValue
//
//str value2IValue(bool b) = "$VF.bool(<b>)";
//str value2IValue(int n) = "$VF.integer(<n>)";
//str value2IValue(real r) = "$VF.real(<r>)";
//str value2IValue(rat rt) = "$VF.rational(\"<rt>\")";
//str value2IValue(str s) = "$VF.string(\"<escapeForJ(s)>\")";
//
//str value2IValue(loc l) {
//    base = "$VF.sourceLocation(\"<l.scheme>\", "
//           + "\"<l.authority>\", "
//           //+ (l.host? ? ("\"<l.host>\", ") : "\"\", ")
//           //+ (l.port? ? ("\"<l.port>\", ") : "\"\", ")
//           + "\"<l.path>\""
//           //+ "\"<l.query>\", "
//           //+ "\"<l.fragment>\""
//           + ")";
//     return l.offset? ? "$VF.sourceLocation(<base>, <l.offset>, <l.length>, <l.begin.line>, <l.end.line>, <l.begin.column>, <l.end.column>)"
//                      : base;
//        
//}
//
//str value2IValue(datetime dt) {
//    if(dt.isDateTime)
//        return "$VF.datetime(<dt.year>, <dt.month>, <dt.day>, <dt.hour>, <dt.minute>, <dt.second>, <dt.millisecond>, <dt.timezoneOffsetHours>, <dt.timezoneOffsetMinutes>)";
//    if(dt.isDate)
//        return "$VF.date(<dt.year>, <dt.month>, <dt.day>)";
//    return "$VF.time(<dt.hour>, <dt.minute>, <dt.second>, <dt.millisecond>)";
//}
//
//str value2IValue(list[&T] lst) = "$VF.list(<intercalate(", ", [value2IValue(elem) | elem <- lst ])>)";
//str value2IValue(set[&T] st) = "$VF.set(<intercalate(", ", [value2IValue(elem) | elem <- st ])>)";
//
//str value2IValue(rel[&A] r) = "$VF.set(<intercalate(", ", [value2IValue(elem) | elem <- r ])>)";
//str value2IValue(rel[&A,&B] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>)" | tup <- r ])>)";
//str value2IValue(rel[&A,&B,&C] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>)" | tup <- r ])>)";
//str value2IValue(rel[&A,&B,&C,&D] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>)" | tup <- r ])>)";
//str value2IValue(rel[&A,&B,&C,&D,&E] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>)" | tup <- r ])>)";
//str value2IValue(rel[&A,&B,&C,&D,&E,&F] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>)" | tup <- r ])>)";
//str value2IValue(rel[&A,&B,&C,&D,&E,&F,&G] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>)" | tup <- r ])>)";
//str value2IValue(rel[&A,&B,&C,&D,&E,&F,&G,&H] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>)" | tup <- r ])>)";
//str value2IValue(rel[&A,&B,&C,&D,&E,&F,&G,&H] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>, <value2IValue(tup[8])>)" | tup <- r ])>)";
//str value2IValue(rel[&A,&B,&C,&D,&E,&F,&G,&H,&I] r) = "$VF.set(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>, <value2IValue(tup[8])>, <value2IValue(tup[9])>)" | tup <- r ])>)";
//
//str value2IValue(lrel[&A] r) = "$VF.list(<intercalate(", ", [value2IValue(elem) | elem <- r ])>)";
//str value2IValue(lrel[&A,&B] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>)" | tup <- r ])>)";
//str value2IValue(lrel[&A,&B,&C] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>)" | tup <- r ])>)";
//str value2IValue(lrel[&A,&B,&C,&D] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>)" | tup <- r ])>)";
//str value2IValue(lrel[&A,&B,&C,&D,&E] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>)" | tup <- r ])>)";
//str value2IValue(lrel[&A,&B,&C,&D,&E,&F] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>)" | tup <- r ])>)";
//str value2IValue(lrel[&A,&B,&C,&D,&E,&F,&G] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>)" | tup <- r ])>)";
//str value2IValue(lrel[&A,&B,&C,&D,&E,&F,&G,&H] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>)" | tup <- r ])>)";
//str value2IValue(lrel[&A,&B,&C,&D,&E,&F,&G,&H,&I] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>, <value2IValue(tup[8])>)" | tup <- r ])>)";
//str value2IValue(lrel[&A,&B,&C,&D,&E,&F,&G,&H,&I,&J] r) = "$VF.list(<intercalate(", ", ["$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>, <value2IValue(tup[8])>, <value2IValue(tup[9])>)" | tup <- r ])>)";
//
//str value2IValue(tuple[&A] tup) = "$VF.tuple(<value2IValue(tup[0])>)";
//str value2IValue(tuple[&A,&B] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>)";
//str value2IValue(tuple[&A,&B,&C] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>)";
//str value2IValue(tuple[&A,&B,&C,&D] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>)";
//str value2IValue(tuple[&A,&B,&C,&D,&E] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>)";
//str value2IValue(tuple[&A,&B,&C,&D,&E,&F] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>)";
//str value2IValue(tuple[&A,&B,&C,&D,&E,&F,&G] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>)";
//str value2IValue(tuple[&A,&B,&C,&D,&E,&F,&G,&H] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>)";
//str value2IValue(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>, <value2IValue(tup[8])>)";
//str value2IValue(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I,&J] tup) = "$VF.tuple(<value2IValue(tup[0])>, <value2IValue(tup[1])>, <value2IValue(tup[2])>, <value2IValue(tup[3])>, <value2IValue(tup[4])>, <value2IValue(tup[5])>, <value2IValue(tup[6])>, <value2IValue(tup[7])>, <value2IValue(tup[8])>, <value2IValue(tup[9])>)";
//
//str value2IValue(map[&K,&V] mp) = "buildMap(<intercalate(", ", ["<value2IValue(k)>, <value2IValue(mp[k])>" | k <- mp ])>)";
//
//
//str value2IValue(node nd) = "$VF.node(<getName(nd)>, <intercalate(", ", [ value2IValue(child) | child <- getChildren(nd) ])>)";
//str value2IValue(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) = adtName;
//
//str value2IValue(acons(AType adt,
//                list[AType fieldType] fields,
//                lrel[AType fieldType, Expression defaultExp] kwFields))
//                 = "IConstructor";
//
//str value2IValue(t:avoid()) { throw "value2IValue: cannot handle <t>"; }
//str value2IValue(t:areified(AType atype)) { throw "value2IValue: cannot handle <t>"; }
//default str value2IValue(AType t) { throw "value2IValue: cannot handle <t>"; }
//
//// ---- value2OuterType
//
//str value2outertype(int n) = "IInteger";
//str value2outertype(bool b) = "IBool";
//str value2outertype(real r) = "IReal";
//str value2outertype(rat rt) = "IRational";
//str value2outertype(str s) = "IString";
//
//str value2outertype(node nd) = "INode";
//str value2outertype(loc l) = "ISourceLocation";
//str value2outertype(datetime dt) = "IDateTime";
//str value2outertype(list[&T] lst) = "IList";
//str value2outertype(set[&T] st) = "ISet";
//str value2outertype(map[&K,&V] st) = "IMap";
//str value2outertype(atuple(AType ts)) = "ITuple";
//str value2outertype(tuple[&A] tup) = "ITuple";
//str value2outertype(tuple[&A,&B] tup) = "ITuple";
//str value2outertype(tuple[&A,&B,&C] tup) = "ITuple";
//str value2outertype(tuple[&A,&B,&C,&D] tup) = "ITuple";
//str value2outertype(tuple[&A,&B,&C,&D,&E] tup) = "ITuple";
//str value2outertype(tuple[&A,&B,&C,&D,&E,&F] tup) = "ITuple";
//str value2outertype(tuple[&A,&B,&C,&D,&E,&F,&G] tup) = "ITuple";
//str value2outertype(tuple[&A,&B,&C,&D,&E,&F,&G,&H] tup) = "ITuple";
//str value2outertype(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I] tup) = "ITuple";
//str value2outertype(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I,&J] tup) = "ITuple";
//
//str value2outertype(amap(AType d, AType r)) = "IMap";
//str value2outertype(arel(AType ts)) = "IRelation";
//str value2outertype(alrel(AType ts)) = "IListRelation";
//
//str value2outertype(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) = adtName;
//
//str value2outertype(acons(AType adt,
//                list[AType fieldType] fields,
//                lrel[AType fieldType, Expression defaultExp] kwFields))
//                 = "IConstructor";
//str value2outertype(areified(AType atype)) = "IConstructor";
//default str value2outertype(AType t) = "IValue";
//
//// ----
//
//str atype2typestore(aint()) = "$TF.integerType()";
//str atype2typestore(abool()) = "$TF.boolType()";
//str atype2typestore(areal()) = "$TF.realType()";
//str atype2typestore(arat()) = "$TF.rationalType()";
//str atype2typestore(astr()) = "$TF.stringType()";
//str atype2typestore(anum()) = "$TF.numberType()";
//str atype2typestore(anode(list[AType fieldType] fields)) = "$TF.nodeType()";
//str atype2typestore(avoid()) = "$TF.voidType()";
//str atype2typestore(avalue()) = "$TF.valueType()";
//str atype2typestore(aloc()) = "$TF.sourceLocationType()";
//str atype2typestore(adatetime()) = "$TF.dateTimeType()";
//str atype2typestore(alist(AType t)) = "$TF.listType(<atype2typestore(t)>)";
//str atype2typestore(aset(AType t)) = "$TF.setType(<atype2typestore(t)>)";
//str atype2typestore(atuple(AType ts)) = "$TF.tupleType(<atype2typestore(ts)>)";
//str atype2typestore(amap(AType d, AType r)) = "$TF.mapType(<atype2typestore(d)>,<atype2typestore(r)>)";
//str atype2typestore(arel(AType t)) = "$TF.relationType(<atype2typestore(t)>)";
//str atype2typestore(alrel(AType t)) = "$TF.listRelationType(<atype2typestore(t)>)";
//str atype2typestore(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) = getADTName(adtName);
//str atype2typestore(acons(AType adt,
//                list[AType fieldType] fields,
//                lrel[AType fieldType, Expression defaultExp] kwFields))
//                 = "IConstructor";
//
//str atype2typestore(atypeList(list[AType] atypes)) = intercalate(", ", [atype2typestore(t) | t <- atypes]);
//str atype2typestore(areified(AType atype)) = "AType";
//default str atype2typestore(AType t) = "$TF.valueType()";