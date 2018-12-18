module lang::rascalcore::compile::muRascal2Java::JGenie

import lang::rascal::\syntax::Rascal;

import List;
import Set;
import IO;
import String;
import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::compile::muRascal2Java::CodeGen;

//extend lang::rascalcore::check::AType;
//extend lang::rascalcore::check::ATypeUtils;
extend lang::rascalcore::check::TypePalConfig;

alias JCode = str;

data JGenie
    = jgenie(
        str () getModuleName,
        AType (loc src) getType,
        str (loc src) getAccessor,
        str (loc src) getAccessorInResolver,
        Define (loc src) getDefine,
        lrel[str,loc] (loc src) getExternalVars,
        void(str name) setKwpDefaults,
        str() getKwpDefaults,
        str(AType atype) shareType,
        str(value con) shareConstant,
        str () getConstants,
        void(set[str] vars) setRefVars,
        bool (str name) isRefVar,
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
    
JGenie makeJGenie(str moduleName, map[str,TModel] tmodels, map[str,loc] moduleLocs){
    str kwpDefaults = "$kwpDefaults";
    map[value,str] constants = ();
    map[AType,str] types = ();
    int nconstants = -1;
    int ntypes = -1;
    set[str] refVars = {};
    set[MuExp] externalVars = {};
    int ntmps = -1;
    set[str] importedLibraries = {};
    
    map[str, AType] declaredVars = ();
    TModel currentModule = tmodels[moduleName];
    loc currentModuleScope = moduleLocs[moduleName];
    set[tuple[str name, AType funType, str scope, list[loc] ofunctions, list[loc] oconstructors]] resolvers = {};
   
    str _getModuleName()
        = currentModule.modelName;
    
    AType _getType(loc src)
        = currentModule.facts[src];
        
    str _getAccessor(loc src){
        for(mname <- tmodels){
            if(tmodels[mname].definitions[src]?){
                def = tmodels[mname].definitions[src];
                if(defType(AType tp) := def.defInfo){
                    baseName = "<def.id>_<def.defined.begin.line>_<def.defined.end.line>";
                    descriptor = atype2descriptor(tp);
                    if(mname == moduleName){
                        return "$me.<def.id>_<descriptor>";
                    } else {
                        return "<replaceAll(mname, "::", "_")>.<baseName>_<descriptor>";
                    }
                 }
             }
        }
    }
    
    str _getAccessorInResolver(loc src){
        for(mname <- tmodels){
            if(tmodels[mname].definitions[src]?){
                def = tmodels[mname].definitions[src];
                if(defType(AType tp) := def.defInfo){
                    baseName = "<def.id>_<def.defined.begin.line>_<def.defined.end.line>";
                    if(mname == moduleName){
                        return baseName; //"$me.<def.id>";
                    } else {
                        return "<replaceAll(mname, "::", "_")>.<baseName>";
                    }
                 }
             }
        }
    }
    
    Define _getDefine(loc src){
        for(mname <- tmodels){
                if(tmodels[mname].definitions[src]?){
                    return tmodels[mname].definitions[src];
                }
        }
    }
    
    lrel[str, loc] _getExternalVars(loc src){
        extVarDefs = {};
        for(mname <- tmodels){
            tm = tmodels[mname];
            useDef = tm.useDef;
            println("useDef:"); iprintln(useDef);
            definitions = tm.definitions;
            for(<u, d> <- useDef, def := definitions[d], def.idRole == variableId(), !containedIn(def.scope, src)){
                extVarDefs += <def.id, d>;
            }
        }
        return sort(toList(extVarDefs));
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
        return c;
    }
    
    str _getConstants(){
        return "<for(v <- constants){>
               'private static final <value2outertype(v)> <constants[v]> = <value2java(v)>;
               '<}>
               '<for(t <- types){>
               'private static final Type <types[t]> = <atype2typestore(t)>;
               '<}>";
    }
    
    void _setRefVars(set[str] vars){
        refVars = vars;
    }
    
    bool _isRefVar(str var) = var in refVars;
    
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
                _getType,
                _getAccessor,
                _getAccessorInResolver,
                _getDefine,
                _getExternalVars,
                _setKwpDefaults,
                _getKwpDefaults,
                _shareType,
                _shareConstant,
                _getConstants,
                _setRefVars,
                _isRefVar,
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

// ---- atype 2 java type -----------------------------------------------------

str atype2java(aint())                  = "IInteger";
str atype2java(abool())                 = "IBool";
str atype2java(areal())                 = "IReal";
str atype2java(arat())                  = "IRational";
str atype2java(astr())                  = "IString";
str atype2java(anum())                  = "INumber";
str atype2java(anode(list[AType fieldType] fields)) = "INode";
str atype2java(avoid())                 = "void";
str atype2java(avalue())                = "IValue";
str atype2java(aloc())                  = "ISourceLocation";
str atype2java(adatetime())             = "IDateTime";
str atype2java(alist(AType t))          = "IList";
str atype2java(aset(AType t))           = "ISet";
str atype2java(atuple(AType ts))        = "ITuple";
str atype2java(amap(AType d, AType r))  = "IMap";
str atype2java(arel(AType ts))          = "IRelation";
str atype2java(alrel(AType ts))         = "IListRelation";

str atype2java(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
                                        = "FunctionInstance0\<<atype2java(ret)>\>"
                                        when isEmpty(formals);
str atype2java(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
                                        = "FunctionInstance<size(formals)>\<<atype2java(ret)>, <intercalate(", ", [atype2java(f) | f <- formals])>\>"
                                        when !isEmpty(formals);
 

str atype2java(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) = "IConstructor";

str atype2java(t: acons(AType adt, /*str consName,*/ 
                list[AType fieldType] fields,
                lrel[AType fieldType, Expression defaultExp] kwFields))
                 = "IConstructor";

default str atype2java(AType t) = "IValue";

// ----
str atype2descriptor(aint())                  = "int";
str atype2descriptor(abool())                 = "bool";
str atype2descriptor(areal())                 = "real";
str atype2descriptor(arat())                  = "rat";
str atype2descriptor(astr())                  = "str";
str atype2descriptor(anum())                  = "num";
str atype2descriptor(anode(list[AType fieldType] fields)) = "node";
str atype2descriptor(avoid())                 = "void";
str atype2descriptor(avalue())                = "value";
str atype2descriptor(aloc())                  = "loc";
str atype2descriptor(adatetime())             = "datetime";
str atype2descriptor(alist(AType t))          = "list";
str atype2descriptor(aset(AType t))           = "set";
str atype2descriptor(atuple(AType ts))        = "tuple";
str atype2descriptor(amap(AType d, AType r))  = "map";
str atype2descriptor(arel(AType ts))          = "rel";
str atype2descriptor(alrel(AType ts))         = "listrel";
str atype2descriptor(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
                                              = "<atype2descriptor(ret)>_<intercalate("_", [atype2descriptor(f) | f <- formals])>";
str atype2descriptor(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) = adtName;
str atype2descriptor(t:acons(AType adt, list[AType fieldType] fields, lrel[AType fieldType, Expression defaultExp] kwFields))
                                              = "<adt.adtName><t.label? ? "_" + t.label : "">_<intercalate("_", [atype2descriptor(f) | f <- fields])>";
str atype2descriptor(overloadedAType(rel[loc, IdRole, AType] overloads)){
    resType = avoid();
    formalsType = avoid();
    for(<def, idrole, tp> <- overloads){
        resType = alub(resType, getResult(tp));
        formalsType = alub(formalsType, atypeList(getFormals(tp)));
    }
    ftype = afunc(resType, formalsType.atypes, []);
    return atype2descriptor(ftype);
}
default str atype2descriptor(AType t) = "value";

// ----

str atype2istype(aint())                  = "isInteger";
str atype2istype(abool())                 = "isBool";
str atype2istype(areal())                 = "isReal";
str atype2istype(arat())                  = "isRational";
str atype2istype(astr())                  = "isString";
str atype2istype(anum())                  = "isNumber";
str atype2istype(anode(list[AType fieldType] fields)) = "isNode";
str atype2istype(avoid())                 = "isBottom";
str atype2istype(avalue())                = "isTop";
str atype2istype(aloc())                  = "isSourceLocation";
str atype2istype(adatetime())             = "isDateTime";
str atype2istype(alist(AType t))          = "isList";
str atype2istype(aset(AType t))           = "isSet";
str atype2istype(atuple(AType ts))        = "isTuple";
str atype2istype(amap(AType d, AType r))  = "isMap";
str atype2istype(arel(AType ts))          = "isRelation";
str atype2istype(alrel(AType ts))         = "isListRelation";
str atype2istype(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
                                        = "isExternalType";
str atype2istype(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) = "isAbstractData";
str atype2istype(t: acons(AType adt, /*str consName,*/ 
                list[AType fieldType] fields,
                lrel[AType fieldType, Expression defaultExp] kwFields))
                 = "isConstructor";
str atype2istype(overloadedAType(rel[loc, IdRole, AType] overloads))
    = "isOverloaded";

default str atype2istype(AType t) = "isTop";



// ----

str value2java(int n) = "$VF.integer(<n>)";
str value2java(bool b) = "$VF.bool(<b>)";
str value2java(real r) = "$VF.real(<r>)";
str value2java(rat rt) = "$VF.rational(<rt>)";
str value2java(str s) = "$VF.string(\"<s>\")";   // TODO escaping

str value2java(anode(list[AType fieldType] fields)) = "INode";
str value2java(aloc()) = "ISourceLocation";
str value2java(datetime dt) = "$VF.date(<dt.year>, <dt.month>, <dt.day>)" when !(dt has minute);
str value2java(datetime dt) = "$VF.dateTime(<dt.year>, <dt.month>, <dt.day>, <dt.hour>, <dt.second>. <dt.millisecond>)" when dt has minute;

str value2java(list[&T] lst) = "$VF.list(<intercalate(", ", [value2java(elem) | elem <- lst ])>)";
str value2java(set[&T] st) ="$VF.set(<intercalate(", ", [value2java(elem) | elem <- st ])>)";
str value2java(tuple[&A] tup) = "$VF.tuple(<tup[0]>)";
str value2java(tuple[&A,&B] tup) = "$VF.tuple(<tup[0]>, <tup[1]>)";

str value2java(tuple[&A,&B,&C] tup) = "$VF.tuple(<tup[0]>, <tup[1]>, <tup[2]>)";
str value2java(tuple[&A,&B,&C,&D] tup) = "$VF.tuple(<tup[0]>, <tup[1]>, <tup[2]>, <tup[3]>)";
str value2java(tuple[&A,&B,&C,&D,&E] tup) = "$VF.tuple(<tup[0]>, <tup[1]>, <tup[2]>, <tup[3]>, <tup[4]>)";
str value2java(tuple[&A,&B,&C,&D,&E,&F] tup) = "$VF.tuple(<tup[0]>, <tup[1]>, <tup[2]>, <tup[3]>, <tup[4]>, <tup[5]>)";
str value2java(tuple[&A,&B,&C,&D,&E,&F,&G] tup) = "$VF.tuple(<tup[0]>, <tup[1]>, <tup[2]>, <tup[3]>, <tup[4]>, <tup[5]>, <tup[6]>)";
str value2java(tuple[&A,&B,&C,&D,&E,&F,&G,&H] tup) = "$VF.tuple(<tup[0]>, <tup[1]>, <tup[2]>, <tup[3]>, <tup[4]>, <tup[5]>, <tup[6]>, <tup[7]>)";
str value2java(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I] tup) = "$VF.tuple(<tup[0]>, <tup[1]>, <tup[2]>, <tup[3]>, <tup[4]>, <tup[5]>, <tup[6]>, <tup[7]>, <tup[8]>)";
str value2java(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I,&J] tup) = "$VF.tuple(<tup[0]>, <tup[1]>, <tup[2]>, <tup[3]>, <tup[4]>, <tup[5]>, <tup[6]>, <tup[7]>, <tup[8]>, <tup[9]>)";
str value2java(map[&K,&V] mp) = "$VF.map(<intercalate(", ", ["<value2java(k)>, <value2java(mp[k])>" | k <- mp ])>)";
str value2java(arel(AType ts)) = "IRelation";
str value2java(alrel(AType ts)) = "IListRelation";

str value2java(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) = adtName;

str value2java(acons(AType adt,
                list[AType fieldType] fields,
                lrel[AType fieldType, Expression defaultExp] kwFields))
                 = "IConstructor";

default str value2java(avoid) = "IVoid";
default str value2java(AType t) = "IValue";

// ---- value2OuterType

str value2outertype(int n) = "IInteger";
str value2outertype(bool b) = "IBool";
str value2outertype(real r) = "IReal";
str value2outertype(rat rt) = "IRational";
str value2outertype(str s) = "IString";

str value2outertype(node nd) = "INode";
str value2outertype(loc l) = "ISourceLocation";
str value2outertype(datetime dt) = "IDateTime";
str value2outertype(list[&T] lst) = "IList";
str value2outertype(set[&T] st) = "ISet";
str value2outertype(atuple(AType ts)) = "ITuple";
str value2outertype(tuple[&A] tup) = "ITuple";
str value2outertype(tuple[&A,&B] tup) = "ITuple";
str value2outertype(tuple[&A,&B,&C] tup) = "ITuple";
str value2outertype(tuple[&A,&B,&C,&D] tup) = "ITuple";
str value2outertype(tuple[&A,&B,&C,&D,&E] tup) = "ITuple";
str value2outertype(tuple[&A,&B,&C,&D,&E,&F] tup) = "ITuple";
str value2outertype(tuple[&A,&B,&C,&D,&E,&F,&G] tup) = "ITuple";
str value2outertype(tuple[&A,&B,&C,&D,&E,&F,&G,&H] tup) = "ITuple";
str value2outertype(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I] tup) = "ITuple";
str value2outertype(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I,&J] tup) = "ITuple";

str value2outertype(amap(AType d, AType r)) = "IMap";
str value2outertype(arel(AType ts)) = "IRelation";
str value2outertype(alrel(AType ts)) = "IListRelation";

str value2outertype(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) = adtName;

str value2outertype(acons(AType adt,
                list[AType fieldType] fields,
                lrel[AType fieldType, Expression defaultExp] kwFields))
                 = "IConstructor";

default str value2outertype(AType t) = "IValue";

// ----

str atype2typestore(aint()) = "$TF.integerType()";
str atype2typestore(abool()) = "$TF.boolType()";
str atype2typestore(areal()) = "$TF.realType()";
str atype2typestore(arat()) = "$TF.rationalType()";
str atype2typestore(astr()) = "$TF.stringType()";
str atype2typestore(anum()) = "$TF.numberType()";
str atype2typestore(anode(list[AType fieldType] fields)) = "$TF.nodeType()";
str atype2typestore(avoid()) = "$TF.voidType()";
str atype2typestore(avalue()) = "$TF.valueType()";
str atype2typestore(aloc()) = "$TF.sourceLocationType()";
str atype2typestore(adatetime()) = "$TF.dateTimeType()";
str atype2typestore(alist(AType t)) = "$TF.listType(<atype2typestore(t)>)";
str atype2typestore(aset(AType t)) = "$TF.setType(<atype2typestore(t)>)";
str atype2typestore(atuple(AType ts)) = "$TF.tupleType(<atype2typestore(ts)>)";
str atype2typestore(amap(AType d, AType r)) = "$TF.mapType(<atype2typestore(d)>,<atype2typestore(r)>)";
str atype2typestore(arel(AType t)) = "$TF.relationType(<atype2typestore(t)>)";
str atype2typestore(alrel(AType t)) = "$TF.listRelationType(<atype2typestore(t)>)";
str atype2typestore(acons(AType adt,
                list[AType fieldType] fields,
                lrel[AType fieldType, Expression defaultExp] kwFields))
                 = "IConstructor";

str atype2typestore(atypeList(list[AType] atypes)) = intercalate(", ", [atype2typestore(t) | t <- atypes]);
default str atype2typestore(AType t) = "$TF.valueType()";