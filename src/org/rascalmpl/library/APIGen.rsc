
@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Atze van der Ploeg - ploeg@cwi.nl - CWI}

module APIGen

/* translates a list of reified types to a java factory class */

import List;
import String;
import IO;
	

public str apiGen(str apiName,list[type[value]] ts) {
	map[str,str] emp = ();
	return apiGen(apiName,ts,emp);
}

public str apiGen(str apiName,list[type[value]] ts, map[str,str] externalTypes) {

	str declareType(type[value] t){
		if("adt"(name,cs,ps) := t){
			return 	"public static final Type <name> = tf.abstractDataType(typestore, \"<name>\"<typeParamsVarArgs(ps)>);
					'<for(c <- cs) {>
					'	<declareConstructor(c,name)><}>
					";
		} 
		if ("alias"(name, ap, ps) := t) {
		println("Gothere");
			list[type[value]] pss = [];
			println("Gothere");
			if(ps != []) {
				pss  = [ p | <p,_> <- ps];
			}
			println("Gothere2 <pss>");
			s = typeList2FactoryVarArgs(pss); 
			println("Gothere3");
			res =  "public static final Type <name> = 
				tf.aliasType(typestore, \"<name>\",<type2FactoryCall(ap)><s>)";
			println(res);
			return res;
		}
		throw "Cannot declare type <t>"; 
	}
	
	
	public str declareConstructor(value t,str typeName){
		switch(t){
			case constructor(str cname,list[tuple[type[value],str]] args) : {
				return "public static final Type <typeName>_<cname> = tf.constructor(typestore,<typeName>,\"<cname>\"<typeNameTuples2FactoryCallArgs(args)>);";
			}
		}
	}
	
	public str type2FactoryCall(type[value] t){
		switch(t){
			case value() : return "tf.valueType()";
			case void() : return "tf.voidType()";
			case int() :  return "tf.integerType()"; 
			case num() : return "tf.numberType()";
			case real() : return "tf.realType()";
			case bool() : return "tf.boolType()";
			case str() :  return "tf.stringType()"; 
			case loc() : return "tf.sourceLocationType()";
			case datetime() : return "tf.dateTimeType()";
			case node() : return "tf.nodeType()";
			case constructor(name,_) : return resolveType (name);
			case set(ti) :  return "tf.setType(<type2FactoryCall(ti)>)";	
			case list(ti) :  return "tf.listType(<type2FactoryCall(ti)>)";
			case map(ti,ti2) : return "tf.mapType(<type2FactoryCall(ti)>,<type2FactoryCall(ti2)>)";
			case tuple(list[type[value]] tis) : 
				return "tf.tupleType(<typeList2FactoryVarArgs(tis)>)";
			case tuple(list[tuple[type[value] \type, str label]] tis):
				return "tf.tupleType(<typeAndLabelsList2FactoryVarArgs(tis)>)";
			case rel(list[tuple[type[value] \type, str label]] tis) :
				 return "tf.relType(<typeAndLabelsList2FactoryVarArgs(tis)>)";
			case rel(list[type[value]] tis) : 
				return "tf.relType(<typeList2FactoryVarArgs(tis)>)";
			case fun(returnType, args): 
				return "rtf.functionType(<type2FactoryCall(returnType)>,
						tf.tupleType(<typeList2FactoryVarArgsFirstPos(args)>)";
		}
		// for some reason this does not work in switch (bug)
		     if("adt"(name,_,_) := t) {
		     	  return resolveType (name); 
		 } else if("alias"(name,p,q) := t) {  
		 	return resolveType (name);
		 } else if("parameter"(name,t2) := t) {  
			return "tf.parameterType(\"<name>\",<type2FactoryCall(t2)>)"; 
		} else if("reified"(type[value] t2) := t) {
			return "rtf.reifiedType(<type2FactoryCall(t2)>)";
		}
		 throw "Do not now how to construct <t>";
	}
	
	str typeParamsVarArgs(list[tuple[type[value],type[value]]] p){
		 return toExtraArgs([ type2FactoryCall(t[0]) | t <- p]);
	}
	
	str typeAndLabelsList2FactoryVarArgs(list[tuple[type[value] \type, str label]] typesAndLabels){
		return toExtraArgs([type2FactoryCall(arg.\type),"\"<arg.label>\"" | arg <- typesAndLabels ]);
	}
	
	str typeList2FactoryVarArgs(list[type[value]] tss){
		println("HALLOO!");
		if(tss == []) { return "";}
		else { return toExtraArgs([ type2FactoryCall(t) | t <- tss]); }
	}
	
	str typeList2FactoryVarArgsFirstPos(list[type[value]] tss){
		return intercalate(",",[ type2FactoryCall(t) | t <- tss]);
	}
	
	str toExtraArgs(list[str] strs) =
		("" | "<it>,<s>" | s <- strs);
	
	
	str typeNameTuples2FactoryCallArgs(list[tuple[type[value],str]] args){
		return toExtraArgs([type2FactoryCall(t),"\"" + n + "\"" | <t,n> <- args]);
	} 
	
	str resolveType(str s){
		if(externalTypes[s] ? ){
			return "<externalTypes[s]>.<s>";
		} else {
			return s;
		}
	}
	
	
	str declareGetters(type[value] t){
		if("adt"(name,cs,ps) := t){
			return 	"<for(c <- cs) {><declareConstructorGetters(c,name)><}>";
		} 
		throw "Cannot declare getters for type <t>"; 
	}
	
	
	
	str declareConstructorGetters(value t,str typeName){
		switch(t){
			case constructor(str cname,list[tuple[type[value],str]] args) : {
				if(size(args) == 0) return "";
				return 	"<for(i <- [0..size(args)-1]) {>public static <typeToSimpleJavaType(args[i][0])> <typeName>_<cname>_<args[i][1]>(IConstructor c){
						'	return <javaResult(args[i][0],"c.get(<i>)")>;
						'}
						'<}>";
			}
		}
	}
	
	str typeToSimpleJavaType(type[value] t){
		switch(t){
			case int() : return "int";
			case real() : return "double";
			case num() : return "double";
			case bool() : return "boolean";
			case str() :  return "String";
			default : return typeToJavaType(t);
		}
	}
	
	str javaResult(type[value] t,str access){
		switch(t){
			case int() : return "((IInteger)<access>).intValue()";
			case real() : return "((IReal)<access>).doubleValue()";
			case num() : return "<access> instanceof IInteger ? (double)((IInteger)<access>).intValue() : ((IReal)<access>).doubleValue()";
			case bool() : return "((IBool)<access>).getValue()";
			case str() :  return "((IString)<access>).getValue()";
			default : return "(<typeToJavaType(t)>)<access>";
		}
	}
	
	str typeToJavaType(type[value] t){
		switch(t){
			case int() : return "IInteger";
			case real() : return "IReal";
			case num() : return "INumber";
			case bool() : return "IBool";
			case list(_) : return "IList";
			case map(_,_) : return "IMap";
			case rel(_) : return "IRelation";
			case set(_) : return "ISet";
			case loc() : return "ISourceLocation";
			case str() :  return "IString";
			case datetime() : return "IDateTime";
			case tuple(_) : return  "ITuple";		 
			case fun(returnType, args): return "Object"; // TODO: fixme with actual type
			default : return "IValue";
		}
	}
	
	allTypes = { x| t <- ts , x:/"adt"(n,c,p) <- t } + toSet(ts);
	return 	"// This code was generated by Rascal API gen
			'import org.rascalmpl.interpreter.types.RascalTypeFactory;
			'import org.eclipse.imp.pdb.facts.type.Type;
			'import org.eclipse.imp.pdb.facts.type.TypeFactory;
			'import org.eclipse.imp.pdb.facts.type.TypeStore;
			'import org.eclipse.imp.pdb.facts.*;
			'
			'public class <apiName> {
			'	public static TypeStore typestore = new TypeStore(
			'		org.rascalmpl.values.errors.Factory.getStore(), 
			'		org.rascalmpl.values.locations.Factory.getStore());
			'
			' 	private static TypeFactory tf = TypeFactory.getInstance();
			'	private static RascalTypeFactory rtf = RascalTypeFactory.getInstance();
			'
			'	<for(type[value] t <- allTypes) { >
			'	<declareType(t)>
			'	<}>
			'	<for(type[value] t <- allTypes) { > <declareGetters(t)> <}>
			'	private static final class InstanceHolder {
			'		public final static <apiName> factory = new <apiName>();
			'	}
			'	  
			'	public static <apiName> getInstance() {
			'		return InstanceHolder.factory;
			'	}
			'	
			'	
			'	public static TypeStore getStore() {
			'		return typestore;
			'	}
			'}";

}
		
