package org.rascalmpl.interpreter.env;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;
/*
 * Note that the data and methods assume that the types are the adt and functor declarations, 
 * i.e., uninstantiated types
 */
public class IsomorphicTypes {
	
	private static Map<Type, Type> isomorphicTypes = new HashMap<Type, Type>();
	
	// Map that specifies the parameterizaion of algebraic data types with type paramaters
	private static Map<Type, Map<Type, Type>> functors = new HashMap<Type, Map<Type,Type>>();
	
	private static Set<Type> builtInBasicTypes = new HashSet<Type>();
	
	private static TypeFactory TF = TypeFactory.getInstance();
	
	static {	
		builtInBasicTypes.add(TF.stringType());
		builtInBasicTypes.add(TF.boolType());
		builtInBasicTypes.add(TF.integerType());
		builtInBasicTypes.add(TF.realType());
		builtInBasicTypes.add(TF.rationalType());
		builtInBasicTypes.add(TF.numberType());
		builtInBasicTypes.add(TF.dateTimeType());
		builtInBasicTypes.add(TF.sourceLocationType());
	}
		
	public static void declareAsIsomorphic(Type adt, Type functor) {
		if(isomorphicTypes.containsKey(functor)) return ;
		else isomorphicTypes.put(functor, adt); // one adt can possibly have multiple functors
	}
	
	public static boolean isBasicType(Type type) {
		System.out.println(type);
		return builtInBasicTypes.contains(type);
	}
	
	public static boolean isAdt(Type type) {
		return type.isAbstractDataType() || type.isNodeType() || type.isListType() || type.isSetType() || type.isTupleType();
	}
	
	public static boolean isFunctor(Type adt) {
		return isomorphicTypes.containsKey(adt) 
				|| (adt.isListType() && adt.getElementType().isTupleType() && adt.getElementType().getArity() == 2)
				|| (adt.isSetType() && adt.getElementType().isTupleType() && adt.getElementType().getArity() == 2);
 	}
	
	public static boolean isIsomorphic(Type adt, Type functor) {
		return (isomorphicTypes.containsKey(functor) && isomorphicTypes.get(functor).equals(adt))
				|| (isBuiltInIsomorphic(adt, functor));
	}
	
	private static boolean isBuiltInIsomorphic(Type adt, Type functor) {
		return (adt.isSetType() && functor.isSetType() && functor.getElementType().isTupleType() && functor.getElementType().getArity() == 2)
				|| (adt.isListType() && functor.isListType() && functor.getElementType().isTupleType() && functor.getElementType().getArity() == 2);
	}
	
	public static Type getIsomorphicType(Type functor) {
		if(isomorphicTypes.containsKey(functor)) return isomorphicTypes.get(functor); 
		if(functor.isListType() && functor.getElementType().isTupleType() && functor.getElementType().getArity() == 2)
			return TypeFactory.getInstance().listType(TypeFactory.getInstance().parameterType("T"));
		if(functor.isSetType() && functor.getElementType().isTupleType() && functor.getElementType().getArity() == 2)
			return TypeFactory.getInstance().listType(TypeFactory.getInstance().parameterType("T"));
		return null;
	}
	
	public static Map<Type, Type> getParameterizationOfTypes(Type functor) {
		return functors.get(functor);
	}
	
	public static Map<Type, Type> getReverseParameterizationOfTypes(Type functor) {
		Map<Type, Type> bindings = functors.get(functor);
		Map<Type, Type> reverse = new HashMap<Type, Type>();
		for(Type key : bindings.keySet())
			reverse.put(bindings.get(key), key);
		return reverse;
	}
	
	public static void storeParameterizationOfTypes(Type functor, Map<Type,Type> parameterizationOfTypes) {
		functors.put(functor, parameterizationOfTypes);
	}
		
	// Collects all the directly or indirectly used (recursive) algebraic data types
	public static void collectAllRecursiveAdtTypes(Type type, Set<Type> types, IEvaluator<Result<IValue>> eval) {
		if( types.contains(type) || !(type.isAbstractDataType() 
										|| type.isNodeType() 
										|| type.isListType() 
										|| type.isSetType()
										|| type.isMapType()) ) return ;
		types.add(type);
		if(type.isAbstractDataType())
			for(Type alt : eval.getCurrentEnvt().lookupAlternatives(type))
				for(int i = 0; i < alt.getFieldTypes().getArity(); i++)
					collectAllRecursiveAdtTypes(alt.getFieldTypes().getFieldType(i), types, eval);
		if(type.isListType() || type.isSetType())
			collectAllRecursiveAdtTypes(type.getElementType(), types, eval);
		if(type.isMapType()) {
			collectAllRecursiveAdtTypes(type.getKeyType(), types, eval);
			collectAllRecursiveAdtTypes(type.getValueType(), types, eval);
		}
	}
	
	// Collects all the directly or indirectly used types
	public static void collectAllTypes(Type type, Set<Type> types, IEvaluator<Result<IValue>> __eval) {
		if(types.contains(type)) return ;
		types.add(type);
		if(type.isAbstractDataType()) {
			Type adt = __eval.getCurrentEnvt().lookupAbstractDataType(type.getName()); // handles type constructors
			Map<Type, Type> bindings = new HashMap<Type, Type>();
			for(int i = 0; i < adt.getTypeParameters().getArity(); i++)
				bindings.put(adt.getTypeParameters().getFieldType(i), type.getTypeParameters().getFieldType(i));
			for(Type alt : __eval.getCurrentEnvt().lookupAlternatives(adt))
				for(int i = 0; i < alt.getFieldTypes().getArity(); i++)
					collectAllTypes(alt.getFieldTypes().getFieldType(i).instantiate(bindings), types, __eval);
		}
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
