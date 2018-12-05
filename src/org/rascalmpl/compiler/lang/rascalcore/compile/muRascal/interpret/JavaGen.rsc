module lang::rascalcore::compile::muRascal::interpret::JavaGen

import lang::rascalcore::check::AType;

alias JCode = str;

data JGenie
    = jgenie(
        void(str name) setKwpDefaults,
        str() getKwpDefaults,
        str(value con) shareConstant,
        str () getConstants
      )
    ;
    
JGenie makeJGenie(){
    str kwpDefaults = "$kwpDefaults";
    map[value,str] constants = ();
    int nconstants = -1;
    
    void _setKwpDefaults(str name){
        kwpDefaults = name;
    }
    
    str _getKwpDefaults() = kwpDefaults;
    
    str _shareConstant(value v){
        if(constants[v]?) return constants[v];
        nconstants += 1;
        c = "$C<nconstants>";
        constants[v] = c;
        return c;
    }
    str _getConstants(){
        return "<for(v <- constants){>
               'static final <value2outertype(v)> <constants[v]> = <value2java(v)>;
               '<}>";
    }
    
    return jgenie(
                _setKwpDefaults,
                _getKwpDefaults,
                _shareConstant,
                _getConstants
            );
}

// ---- atype 2 java type -----------------------------------------------------

str atype2java(aint()) = "IInteger";
str atype2java(abool()) = "IBool";
str atype2java(areal()) = "IReal";
str atype2java(arat()) = "IRational";
str atype2java(astr()) = "IString";
str atype2java(anum()) = "INumber";
str atype2java(anode(list[AType fieldType] fields)) = "INode";
str atype2java(avoid()) = "void";
str atype2java(avalue()) = "IValue";
str atype2java(aloc()) = "ISourceLocation";
str atype2java(adatetime()) = "IDateTime";
str atype2java(alist(AType t)) = "IList";
str atype2java(aset(AType t)) = "ISet";
str atype2java(atuple(AType ts)) = "ITuple";
str atype2java(amap(AType d, AType r)) = "IMap";
str atype2java(arel(AType ts)) = "IRelation";
str atype2java(alrel(AType ts)) = "IListRelation";

str atype2java(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) = adtName;

str atype2java(t: acons(AType adt, /*str consName,*/ 
                list[AType fieldType] fields,
                lrel[AType fieldType, Expression defaultExp] kwFields))
                 = "IConstructor";

default str atype2java(AType t) = "IValue";


// ----

str value2java(int n) = "$VF.integer(<n>)";
str value2java(bool b) = "$VF.bool(<b>)";
str value2java(real r) = "$VF.real(<r>)";
str value2java(rat rt) = "$VF.rational(<rt>)";
str value2java(str s) = "$VF.string(\"<s>\")";   // TODO escaping

str value2java(anode(list[AType fieldType] fields)) = "INode";
str value2java(avoid()) = "void";
str value2java(avalue()) = "IValue";
str value2java(aloc()) = "ISourceLocation";
str value2java(adatetime()) = "IDateTime";
str value2java(alist(AType t)) = "IList";
str value2java(aset(AType t)) = "ISet";
str value2java(atuple(AType ts)) = "ITuple";
str value2java(amap(AType d, AType r)) = "IMap";
str value2java(arel(AType ts)) = "IRelation";
str value2java(alrel(AType ts)) = "IListRelation";

str value2java(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)) = adtName;

str value2java(acons(AType adt,
                list[AType fieldType] fields,
                lrel[AType fieldType, Expression defaultExp] kwFields))
                 = "IConstructor";

default str value2java(AType t) = "IValue";

// ---- value2OuterType

str value2outertype(int n) = "IInteger";
str value2outertype(bool b) = "IBool";
str value2outertype(real r) = "IReal";
str value2outertype(rat rt) = "IRational";
str value2outertype(str s) = "IString";

str value2outertype(anode(list[AType fieldType] fields)) = "INode";
str value2outertype(avoid()) = "void";
str value2outertype(avalue()) = "IValue";
str value2outertype(aloc()) = "ISourceLocation";
str value2outertype(adatetime()) = "IDateTime";
str value2outertype(alist(AType t)) = "IList";
str value2outertype(aset(AType t)) = "ISet";
str value2outertype(atuple(AType ts)) = "ITuple";
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
str atype2typestore(arel(AType ts)) = "$TF.relationType(<atype2typestore(t)>)";
str atype2typestore(alrel(AType ts)) = "$TF.listRelationType(<atype2typestore(t)>)";
str atype2typestore(acons(AType adt,
                list[AType fieldType] fields,
                lrel[AType fieldType, Expression defaultExp] kwFields))
                 = "IConstructor";

str atype2typestore(atypeList(list[AType] atypes)) = intercalate(", ", [atype2typestore(t) | t <- atypes]);
default str atype2typestore(AType t) = "$TF.valueType()";