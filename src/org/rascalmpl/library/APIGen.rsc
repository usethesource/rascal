
@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Atze van der Ploeg - ploeg@cwi.nl - CWI}

module APIGen

/* translates a list of reified types to a java factory class */

import Type;
import List;
import String;
import IO;
  

public str apiGen(str apiName,list[type[value]] ts) {
  map[str,str] emp = ();
  return apiGen(apiName,ts,emp);
}

public str apiGen(str apiName,list[type[value]] ts, map[str,str] externalTypes) {
  str declareType(type[value] t) {
    if(type(\adt(name,ps),defs) := t, choice(_,cs) := (defs[t.symbol])) {
      return   "public static final Type <name> = tf.abstractDataType(typestore, \"<name>\"<typeParamsVarArgs(ps)>);
              '<for(c <- cs) {>
              '  <declareConstructor(c,name)><}>
              ";
    } 
    
    if (type(\alias(name, ps, ap),defs) := t) {
      // s = typeList2FactoryVarArgs(ps); 
      res =  "public static final Type <name> = tf.aliasType(typestore, \"<name>\",<type2FactoryCall(ap)>);";
      return res;
    }
    return ""; 
  }
  
  
  public str declareConstructor(Production::cons(label(str cname, Symbol _), list[Symbol] args, list[Symbol] kwTypes, /*map[str, value(map[str,value])] kwDefaults,*/ set[Attr] _), str typeName) 
    = "public static final Type <typeName>_<cname> 
      '  = tf.constructor(typestore,<typeName>,\"<cname>\"<typeNameTuples2FactoryCallArgs(args)>);";
  
  public str type2FactoryCall(Symbol t){
    switch(t){
      case \value() : return "tf.valueType()";
      case \void() : return "tf.voidType()";
      case \int() :  return "tf.integerType()"; 
      case \rat() : return "tf.rationalType()";
      case \num() : return "tf.numberType()";
      case \real() : return "tf.realType()";
      case \bool() : return "tf.boolType()";
      case \str() :  return "tf.stringType()"; 
      case \loc() : return "tf.sourceLocationType()";
      case \datetime() : return "tf.dateTimeType()";
      case \node() : return "tf.nodeType()";
      case \cons(\adt(name,_),_,_) : return resolveType (name);
      case \set(ti) :  return "tf.setType(<type2FactoryCall(ti)>)";  
      case \list(ti) :  return "tf.listType(<type2FactoryCall(ti)>)";
      case \map(label(l1,ti),label(l2, ti2)) : return "tf.mapType(<type2FactoryCall(ti)>,\"<l1>\", <type2FactoryCall(ti2)>, \"<l2>\")";
      case \map(ti,ti2) : return "tf.mapType(<type2FactoryCall(ti)>,<type2FactoryCall(ti2)>)";
      case \tuple(tis) : return "tf.tupleType(<typeList2FactoryVarArgs(tis)>)";
      case \rel(tis) : return "tf.relType(<typeList2FactoryVarArgs(tis)>)";
      case \func(returnType, args): 
        return "rtf.functionType(<type2FactoryCall(returnType)>,
               '                 tf.tupleType(<typeList2FactoryVarArgsFirstPos(args)>)";
      case \adt(name, _) : return resolveType(name);
      case \alias(name,_,_) : return resolveType(name);
      case \parameter(name, t2) : return "tf.parameterType(\"<name>\",<type2FactoryCall(t2)>)";
      case \reified(t2) : return "rtf.reifiedType(<type2FactoryCall(t2)>)";
      default: 
        throw "Do not now how to construct <t>";  
    }
  }
  
  str typeParamsVarArgs(list[Symbol] p:[_*,label(_,_),_*]) {
     return toExtraArgs([ type2FactoryCall(t) | label(_,t) <- p]);
  }
  
  default str typeParamsVarArgs(list[Symbol] p) {
     return toExtraArgs([ type2FactoryCall(t0) | t <- p, Symbol t0 := t[0]]);
  }
  
  str typeAndLabelsList2FactoryVarArgs(list[Symbol] typesAndLabels){
    return toExtraArgs([type2FactoryCall(typ),"\"<l>\"" | label(l,typ) <- typesAndLabels ]);
  }
  
  str typeList2FactoryVarArgs(list[Symbol] tss){
    if (tss == []) { return "";}
    else { return toExtraArgs([ type2FactoryCall(t) | t <- tss]); }
  }
  
  str typeList2FactoryVarArgsFirstPos(list[Symbol] tss){
    return intercalate(",",[ type2FactoryCall(t) | t <- tss]);
  }
  
  str toExtraArgs(list[str] strs) =
    ("" | "<it>,<s>" | s <- strs);
  
  
  str typeNameTuples2FactoryCallArgs(list[Symbol] args) {
    return toExtraArgs([type2FactoryCall(t),"\"" + n + "\"" | label(n,t) <- args]);
  } 
  
  str resolveType(str s){
    if(externalTypes[s] ? ){
      return "<externalTypes[s]>.<s>";
    } else {
      return s;
    }
  }
  
  
  str declareGetters(Symbol t, set[Production] cs){
    if(adt(name, ps) := t){
      return   "<for(c <- cs) {><declareConstructorGetters(c,name)><}>";
    } 
    // throw "Cannot declare getters for type <t>";
    return ""; 
  }
  
  
  
  str declareConstructorGetters(Production::cons(label(str cname,_), list[Symbol] args, list[Symbol] kwTypes, /*map[str, value(map[str,value])] kwDefaults,*/ set[Attr] _), str typeName){
     if(size(args) == 0) 
       return "";
     return   "<for(i <- [0..size(args)]) {>public static <typeToSimpleJavaType(args[i])> <typeName>_<cname>_<args[i].name>(IConstructor c){
              '  return <javaResult(args[i],"c.get(<i>)")>;
              '} 
              '<}>";
  }
  
  str typeToSimpleJavaType(Symbol t){
    switch(t){
      case \int() : return "int";
      case \real() : return "double";
      case \num() : return "double";
      case \bool() : return "boolean";
      case \str() :  return "String";
      case \label(_, x) : return typeToSimpleJavaType(x);
      default : return typeToJavaType(t);
    }
  }
  
  str javaResult(Symbol t, str access){
    switch(t){
      case \int() : return "((IInteger)<access>).intValue()";
      case \real() : return "((IReal)<access>).doubleValue()";
      case \num() : return "<access> instanceof IInteger ? (double)((IInteger)<access>).intValue() : ((IReal)<access>).doubleValue()";
      case \bool() : return "((IBool)<access>).getValue()";
      case \str() :  return "((IString)<access>).getValue()";
      case \label(_,x) : return javaResult(x, access);
      default : return "(<typeToJavaType(t)>)<access>";
    }
  }
  
  str typeToJavaType(Symbol t){
    str result ;
    
    switch(t){
      case \adt(_,_) : result =  "IConstructor";
      case \cons(_,_,_) : result =  "IConstructor";
      case \int() : result =  "IInteger";
      case \real() : result =  "IReal";
      case \num() : result =  "INumber";
      case \bool() : result =  "IBool";
      case \list(_) : result =  "IList";
      case \map(_,_) : result =  "IMap";
      case \rel(_) : result =  "IRelation";
      case \set(_) : result =  "ISet";
      case \loc() : result =  "ISourceLocation";
      case \str() :  result =  "IString";
      case \datetime() : result =  "IDateTime";
      case \tuple(_) : result =   "ITuple";     
      case \func(returnType, args): result = "ICallableValue";
      case \alias(_,_,a) : result = typeToJavaType(a);
      default : result = "IValue";
    }
    
    return result;
  }
  
  allTypes = ts;
  return   "// This code was generated by Rascal API gen
           'import org.rascalmpl.interpreter.types.RascalTypeFactory;
           'import org.eclipse.imp.pdb.facts.type.Type;
           'import org.eclipse.imp.pdb.facts.type.TypeFactory;
           'import org.eclipse.imp.pdb.facts.type.TypeStore;
           'import org.eclipse.imp.pdb.facts.*;
           '
           'public class <apiName> {
           '  public static TypeStore typestore = new TypeStore(
           '    org.rascalmpl.values.errors.Factory.getStore(), 
           '    org.rascalmpl.values.locations.Factory.getStore());
           '
           '   private static TypeFactory tf = TypeFactory.getInstance();
           '  private static RascalTypeFactory rtf = RascalTypeFactory.getInstance();
           '
           '  <for(type[value] t <- allTypes) { >
           '  <declareType(t)>
           '  <}>
           '  <for(type[value] t <- allTypes, t.symbol in t.definitions, choice(_,cs) := t.definitions[t.symbol]) {> 
           '  <declareGetters(t.symbol,cs)> <}>
           '  private static final class InstanceHolder {
           '    public final static <apiName> factory = new <apiName>();
           '  }
           '    
           '  public static <apiName> getInstance() {
           '    return InstanceHolder.factory;
           '  }
           '  
           '  
           '  public static TypeStore getStore() {
           '    return typestore;
           '  }
           '}";

}
    
