package org.rascalmpl.interpreter.env;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;

public class IsomorphicTypes {
	
	private static Map<Type, Type> isomorphicTypes = new HashMap<Type, Type>();
	private static Map<Type, Map<Type, Type>> functors = new HashMap<Type, Map<Type,Type>>();
		
	public static void declareAsIsomorphic(Type type1, Type type2) {
		if(isomorphicTypes.containsKey(type2))
			return ;
		else {
			isomorphicTypes.put(type2, type1);
		}
			
	}
	
	public static boolean isIsomorphic(Type type1, Type type2) {
		return (isomorphicTypes.containsKey(type2) && isomorphicTypes.get(type2).equals(type1));
	}
	
	public static Type getIsomorphicType(Type functor) {
		return (isomorphicTypes.containsKey(functor)) ? isomorphicTypes.get(functor) : null;
	}
	
	public static void storeParameterization(Type functor, Map<Type,Type> types) {
		if(functors.containsKey(functor)) return ;
		functors.put(functor, types);
	}
	
	public static Map<Type, Type> getParameterization(Type functor) {
		return functors.get(functor);
	}
	
	// collects all the directly or indirectly used recursive types
	public static void collectAllRecursiveTypes(Type type, Set<Type> types, IEvaluator<Result<IValue>> eval) {
		if( types.contains(type) || !(type.isAbstractDataType() 
										|| type.isNodeType() 
										|| type.isListType() || type.isListRelationType() 
										|| type.isSetType() || type.isRelationType()
										|| type.isMapType()) ) return ;
		types.add(type);
		if(type.isAbstractDataType())
			for(Type alt : eval.getCurrentEnvt().lookupAlternatives(type))
				for(int i = 0; i < alt.getFieldTypes().getArity(); i++)
					collectAllRecursiveTypes(alt.getFieldTypes().getFieldType(i), types, eval);
		if(type.isListType() || type.isListRelationType() || type.isSetType() || type.isRelationType())
			collectAllRecursiveTypes(type.getElementType(), types, eval);
		if(type.isMapType()) {
			collectAllRecursiveTypes(type.getKeyType(), types, eval);
			collectAllRecursiveTypes(type.getValueType(), types, eval);
		}
	}
	
	// collects all the directly or indirectly used types
	public static void collectAllTypes(Type type, Set<Type> types, IEvaluator<Result<IValue>> __eval) {
		if(types.contains(type)) return ;
		types.add(type);
		if(type.isAbstractDataType())
			for(Type alt : __eval.getCurrentEnvt().lookupAlternatives(type))
				for(int i = 0; i < alt.getFieldTypes().getArity(); i++)
					collectAllTypes(alt.getFieldTypes().getFieldType(i), types, __eval);
		if(type.isListType() || type.isListRelationType() || type.isSetType() || type.isRelationType())
			collectAllTypes(type.getElementType(), types, __eval);
		if(type.isMapType()) {
			collectAllTypes(type.getKeyType(), types, __eval);
			collectAllTypes(type.getValueType(), types, __eval);
		}
		if(type.isTupleType())
			for(int i = 0; i < type.getArity(); i++)
				collectAllTypes(type.getFieldType(i), types, __eval);
	}


}
