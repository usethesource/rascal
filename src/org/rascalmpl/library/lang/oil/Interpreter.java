package org.rascalmpl.library.lang.oil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

public class Interpreter {
	private final IValueFactory VF;
	private final ClassLoader classLoader = Interpreter.class.getClassLoader();
	private HashMap<String, Class<?>> classStore = new HashMap<>();
	private HashMap<IConstructor, Class<?>> expTypes = new HashMap<>();
	private HashMap<String, Object> letStore = new HashMap<>();
	
	public Interpreter(IValueFactory VF) {
		this.VF = VF;
	}
	
	public IString interpret(IList exps) {
		Object result = null;
		for (IValue c : exps) {
			result = internalInterpret(null, (IConstructor) c);
		}
		return VF.string(result.toString());
	}
	
	public IString interpret(IConstructor exp) {
		return VF.string(internalInterpret(null, exp).toString());
	}
	
	private Object internalInterpret(Object object, IConstructor exp) {
		Class<?> clazz = null;
		String className = "";
		if (exp.has("class")) {
			className = ((IString) exp.get("class")).getValue();
			
			clazz = classStore.get(className);
			
			if (clazz == null) {
				try {
					clazz = classLoader.loadClass(className);
					classStore.put(className, clazz);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException("Class " + className + " not found");
				}
			}
		}
		
		List<Object> arguments = new ArrayList<Object>();
		List<Class<?>> argumentTypes = new ArrayList<Class<?>>();
		
		if (exp.has("arguments")) {
			for (IValue argumentType: (IList) exp.get("arguments")) {
	          arguments.add(internalInterpret(null, (IConstructor) argumentType));
	          argumentTypes.add(expTypes.get((IConstructor) argumentType));
	        }
		}
		
		switch (exp.getName()) {
			case "let": {
				letStore.put(((IString) exp.get("key")).getValue(), internalInterpret(null, (IConstructor) exp.get("val")));
				return null;
			}
			case "access": {
				String fieldName = ((IString) exp.get("field")).getValue();
				try {
					Field field = clazz.getField(fieldName);
	                object = field.get(object);
	                
	                expTypes.put(exp, field.getType());
	                
	                return object;
				} catch (NoSuchFieldException e) {
					throw new RuntimeException("Field " + fieldName + " of class " + className + " not found");
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Illegal argument of " + fieldName + " of class " + className);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Illegal access on " + fieldName + " of class " + className);
				}
			}
			case "atom": {
				IValue val = exp.get(0);
		        if (val.getType().isInteger()) {
		          expTypes.put(exp, int.class);
		          return ((IInteger) val).intValue();
		        } else if (val.getType().isBool()) {
		        	expTypes.put(exp, boolean.class);
		        	return ((IBool) val).getValue();
		        }
		        expTypes.put(exp, String.class);
		        return ((IString) val).getValue();
			}
			case "use": {
				Object o = internalInterpret(letStore.get(((IString) exp.get("key")).getValue()), (IConstructor) exp.get("val"));
				expTypes.put(exp, expTypes.get((IConstructor) exp.get("val")));
				return o;
			}
			case "new": {
				try {
					Constructor<?>[] constructors = clazz.getConstructors();
					Constructor<?> constructor = null;
					for(Constructor<?> c : constructors) {
						if (isRequiredMethod(argumentTypes, c.getParameterTypes())) {
							constructor = c;
							break;
						}
					}
	                object = constructor.newInstance(arguments.toArray(new Object[arguments.size()]));	                
	                expTypes.put(exp, object.getClass());
	                return object;
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Illegal access on constructor of class" + className);
				} catch (InvocationTargetException e) {
					throw new RuntimeException("InvocationTargetException on constructor of class" + className);
				} catch (InstantiationException e) {
					throw new RuntimeException("InstantiationException on constructor of class" + className);
				}
			}
			case "call": {
				String methodName = ((IString) exp.get("method")).getValue();
				try {
					Method[] methods = clazz.getMethods();
					Method method = null;
					for(Method m : methods) {
						if (isRequiredMethod(argumentTypes, m.getParameterTypes()) && m.getName().equals(methodName)) {
							method = m;
							break;
						}
					}
					
					object = method.invoke(object, arguments.toArray(new Object[arguments.size()]));
					
	                for (IValue extraCalls : (IList) exp.get("calls")) {
	                	internalInterpret(object, (IConstructor) extraCalls);
	                }
	                
	                expTypes.put(exp, method.getReturnType());
	                
	                return object;
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Illegal access on method " + methodName + " of class" + className);
				} catch (InvocationTargetException e) {
					throw new RuntimeException("InvocationTargetException on method " + methodName + " of class" + className);
				}
			}
			default: {
				throw new RuntimeException("Not supported");
			}
		}
	}

	private boolean isRequiredMethod(List<Class<?>> argumentTypes, Class<?>[] paramTypes) {
		boolean matches = false;
		if (paramTypes.length == argumentTypes.size()) {
			for (int i = 0; i < paramTypes.length; i++) {
				if (!paramTypes[i].isAssignableFrom(argumentTypes.get(i))) {
					return false;
				}
			}
			matches = true;
		}
		return matches;
	}
}
