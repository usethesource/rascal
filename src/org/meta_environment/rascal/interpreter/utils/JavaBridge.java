package org.meta_environment.rascal.interpreter.utils;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.meta_environment.rascal.ast.Formal;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Parameters;
import org.meta_environment.rascal.ast.Tag;
import org.meta_environment.rascal.ast.Tags;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.TypeEvaluator;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.staticErrors.MissingTagError;
import org.meta_environment.rascal.interpreter.staticErrors.NonAbstractJavaFunctionError;
import org.meta_environment.rascal.interpreter.staticErrors.NonStaticJavaMethodError;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredJavaMethodError;


public class JavaBridge {
//	private static final String JAVA_IMPORTS_TAG = "javaImports";
	private static final String JAVA_CLASS_TAG = "javaClass";
	
//	private static final String UNWANTED_MESSAGE_PREFIX = "org/meta_environment/rascal/java/";
//	private static final String UNWANTED_MESSAGE_POSTFIX = "\\.java:";
	private static final String METHOD_NAME = "call";
	
//	private final Writer out;
	private final List<ClassLoader> loaders;
	
	private final static Map<FunctionDeclaration,Class<?>> cache = new WeakHashMap<FunctionDeclaration, Class<?>>();
	private final static TypeEvaluator TE = TypeEvaluator.getInstance();
//	private final static JavaTypes javaTypes = new JavaTypes();
	private final static JavaClasses javaClasses = new JavaClasses();
	
	private final IValueFactory vf;
	

	public JavaBridge(OutputStream outputStream, List<ClassLoader> classLoaders, IValueFactory valueFactory) {
//		this.out = new PrintWriter(outputStream);
		this.loaders = classLoaders;
		this.vf = valueFactory;
		
//		Commented out while we wait for a 1.6 JVM on MacOSX 
//		if (ToolProvider.getSystemJavaCompiler() == null) {
//			throw new ImplementationError("Could not find an installed System Java Compiler, please provide a Java Runtime that includes the Java Development Tools (JDK 1.6 or higher).");
//		}
//		
//		if (ToolProvider.getSystemToolClassLoader() == null) {
//			throw new ImplementationError("Could not find an System Tool Class Loader, please provide a Java Runtime that includes the Java Development Tools (JDK 1.6 or higher).");
//		}
	}

	public Method compileJavaMethod(FunctionDeclaration declaration, Environment env) {
		try {
			return getJavaMethod(declaration, env);
		} catch (ClassNotFoundException e) {
			throw new ImplementationError("Error during Java compilation", e.getCause());
		}
	}
	
	private Method getJavaMethod(FunctionDeclaration declaration, Environment env) throws ClassNotFoundException {
		Class<?> clazz = cache.get(declaration);
		
		if (clazz == null) {
			clazz = buildJavaClass(declaration);
			cache.put(declaration, clazz);
		}
		
		Parameters parameters = declaration.getSignature().getParameters();
		Class<?>[] javaTypes = getJavaTypes(parameters, env, false);
		
		try {
			if (javaTypes.length > 0) { // non-void
			  return clazz.getDeclaredMethod(METHOD_NAME, javaTypes);
			}
			
			return clazz.getDeclaredMethod(METHOD_NAME);
		} catch (SecurityException e) {
			throw new ImplementationError("Error during compilation of java function: " + declaration, e.getCause());
		} catch (NoSuchMethodException e) {
			throw new ImplementationError("Unexpected error during compilation of java function: " + declaration,  e.getCause());
		}
	}

	private Class<?> buildJavaClass(FunctionDeclaration declaration) throws ClassNotFoundException {
		// TODO uncomment when we have a 1.6 or 1.7 JVM on MacOS
		throw new ImplementationError("Embedded Java is not supported while Java 1.6 is not available on Mac");
//		Signature signature = declaration.getSignature();
//		String imports = getImports(declaration);
//		String name = signature.getName().toString();
//		String fullClassName = "org.meta_environment.rascal.java." + name;
//		String params = getJavaFormals(signature
//				.getParameters());
//		String result = TE.eval(signature.getType()).isVoidType() ? "void" : "IValue";
//		Compilation compilation = new Compilation();
//
//		compilation.addSource(fullClassName).addLine(
//				"package org.meta_environment.rascal.java;").
//				addLine("import org.meta_environment.rascal.interpreter.RuntimeExceptionFactory;").
//				addLine("import org.eclipse.imp.pdb.facts.type.*;").
//				addLine("import org.eclipse.imp.pdb.facts.*;").
//				addLine("import org.meta_environment.ValueFactoryFactory;").
//				addLine("import org.eclipse.imp.pdb.facts.io.*;").
//				addLine("import org.eclipse.imp.pdb.facts.visitors.*;").
//				addLine("import java.util.Random;").
//				addLine(imports).
//				addLine("public class " + name + "{").
//				addLine("  private static final IValueFactory values = ValueFactoryFactory.getValueFactory();").
//				addLine("  private static final TypeFactory types = TypeFactory.getInstance();").
//				addLine("  private static final Random random = new Random();").
//				addLine("  public static " + result + " " + METHOD_NAME + "(" + params + ") {").
//				addLine(declaration.getBody().toString()).
//				addLine("  }").
//				addLine("}");
//
//	  
//		compilation.doCompile(out);
//		
//		if (compilation.getDiagnostics().size() != 0) {
//			StringBuilder messages = new StringBuilder();
//			for (Diagnostic<? extends JavaFileObject> d : compilation.getDiagnostics()) {
//				String message = d.getMessage(null);
//				message = message.replaceAll(UNWANTED_MESSAGE_PREFIX, "").replaceAll(UNWANTED_MESSAGE_POSTFIX, ",");
//				messages.append(message + "\n");
//			}
//			throw new JavaCompilationError(messages.toString(), declaration);
//		}
//
//		return compilation.getOutputClass(fullClassName);
	}
//
//	private String getImports(FunctionDeclaration declaration) {
//		Tags tags = declaration.getTags();
//		
//		if (tags.hasTags()) {
//			for (Tag tag : tags.getTags()) {
//				if (tag.getName().toString().equals(JAVA_IMPORTS_TAG)) {
//					String contents = tag.getContents().toString();
//					
//					if (contents.length() > 2 && contents.startsWith("{")) {
//						contents = contents.substring(1, contents.length() - 1);
//					}
//					return contents;
//				}
//			}
//		}
//		
//		return "";
//	}
	
	private String getClassName(FunctionDeclaration declaration) {
		Tags tags = declaration.getTags();
		
		if (tags.hasTags()) {
			for (Tag tag : tags.getTags()) {
				if (tag.getName().toString().equals(JAVA_CLASS_TAG)) {
					String contents = tag.getContents().toString();
					
					if (contents.length() > 2 && contents.startsWith("{")) {
						contents = contents.substring(1, contents.length() - 1);
					}
					return contents;
				}
			}
		}
		
		return "";
	}
	
//	private String getJavaFormals(Parameters parameters) {
//		StringBuffer buf = new StringBuffer();
//		List<Formal> formals = parameters.getFormals().getFormals();
//		Iterator<Formal> iter = formals.iterator();
//
//		while (iter.hasNext()) {
//			Formal f = iter.next();
//			String javaType = toJavaType(f.getType());
//			
//			if (javaType != null) { // not void
//			  buf.append(javaType + " " + f.getName());
//
//			  if (iter.hasNext()) {
//				  buf.append(", ");
//			  }
//			}
//		}
//
//		return buf.toString();
//	}
	
//	private org.eclipse.imp.pdb.facts.type.Type toValueType(Type type) {
//		return TE.eval(type);
//	}
	
//	private String toJavaType(Type type) {
//		return toValueType(type).accept(javaTypes);
//	}
	
//	private static class JavaTypes implements ITypeVisitor<String> {
//		public String visitBool(org.eclipse.imp.pdb.facts.type.Type boolType) {
//			return "IBool";
//		}
//
//		public String visitReal(org.eclipse.imp.pdb.facts.type.Type type) {
//			return "IReal";
//		}
//
//		public String visitInteger(org.eclipse.imp.pdb.facts.type.Type type) {
//			return "IInteger";
//		}
//
//		public String visitList(org.eclipse.imp.pdb.facts.type.Type type) {
//			return "IList";
//		}
//
//		public String visitMap(org.eclipse.imp.pdb.facts.type.Type type) {
//			return "IMap";
//		}
//
//		public String visitAlias(org.eclipse.imp.pdb.facts.type.Type type) {
//			return "IValue";
//		}
//
//		public String visitAbstractData(org.eclipse.imp.pdb.facts.type.Type type) {
//			return "IConstructor";
//		}
//
//		public String visitRelationType(org.eclipse.imp.pdb.facts.type.Type type) {
//			return "IRelation";
//		}
//
//		public String visitSet(org.eclipse.imp.pdb.facts.type.Type type) {
//			return "ISet";
//		}
//
//		public String visitSourceLocation(org.eclipse.imp.pdb.facts.type.Type type) {
//			return "ISourceLocation";
//		}
//		
//		public String visitString(org.eclipse.imp.pdb.facts.type.Type type) {
//			return "IString";
//		}
//
//		public String visitNode(org.eclipse.imp.pdb.facts.type.Type type) {
//			return "INode";
//		}
//
//		public String visitConstructor(org.eclipse.imp.pdb.facts.type.Type type) {
//			return "IConstructor";
//		}
//
//		public String visitTuple(org.eclipse.imp.pdb.facts.type.Type type) {
//			return "ITuple";
//		}
//
//		public String visitValue(org.eclipse.imp.pdb.facts.type.Type type) {
//			return "IValue";
//		}
//
//		public String visitVoid(org.eclipse.imp.pdb.facts.type.Type type) {
//			return null;
//		}
//
//		public String visitParameter(org.eclipse.imp.pdb.facts.type.Type parameterType) {
//			return parameterType.getBound().accept(this);
//		}
//
//		public String visitExternal(
//				org.eclipse.imp.pdb.facts.type.Type externalType) {
//			return "IValue";
//		}
//	}
	
	private Class<?>[] getJavaTypes(Parameters parameters, Environment env, boolean hasReflectiveAccess) {
		List<Formal> formals = parameters.getFormals().getFormals();
		int arity = formals.size();
		Class<?>[] classes = new Class<?>[arity + (hasReflectiveAccess ? 1 : 0)];
		for (int i = 0; i < arity;) {
			Class<?> clazz = toJavaClass(formals.get(i), env);
			
			if (clazz != null) {
			  classes[i++] = clazz;
			}
		}
		
		if (hasReflectiveAccess) {
			classes[arity] = IEvaluatorContext.class;
		}
		
		return classes;
	}
	
	private Class<?> toJavaClass(Formal formal, Environment env) {
		return toJavaClass(toValueType(formal, env));
	}

	private Class<?> toJavaClass(org.eclipse.imp.pdb.facts.type.Type type) {
		return type.accept(javaClasses);
	}
	
	private org.eclipse.imp.pdb.facts.type.Type toValueType(Formal formal, Environment env) {
		return TE.eval(formal, env);
	}
	
	private static class JavaClasses implements ITypeVisitor<Class<?>> {

		public Class<?> visitBool(org.eclipse.imp.pdb.facts.type.Type boolType) {
			return IBool.class;
		}

		public Class<?> visitReal(org.eclipse.imp.pdb.facts.type.Type type) {
			return IReal.class;
		}

		public Class<?> visitInteger(org.eclipse.imp.pdb.facts.type.Type type) {
			return IInteger.class;
		}

		public Class<?> visitList(org.eclipse.imp.pdb.facts.type.Type type) {
			return IList.class;
		}

		public Class<?> visitMap(org.eclipse.imp.pdb.facts.type.Type type) {
			return IMap.class;
		}

		public Class<?> visitAlias(org.eclipse.imp.pdb.facts.type.Type type) {
			return type.getAliased().accept(this);
		}

		public Class<?> visitAbstractData(org.eclipse.imp.pdb.facts.type.Type type) {
			return IConstructor.class;
		}

		public Class<?> visitRelationType(org.eclipse.imp.pdb.facts.type.Type type) {
			return IRelation.class;
		}

		public Class<?> visitSet(org.eclipse.imp.pdb.facts.type.Type type) {
			return ISet.class;
		}

		public Class<?> visitSourceLocation(org.eclipse.imp.pdb.facts.type.Type type) {
			return ISourceLocation.class;
		}

		public Class<?> visitString(org.eclipse.imp.pdb.facts.type.Type type) {
			return IString.class;
		}

		public Class<?> visitNode(org.eclipse.imp.pdb.facts.type.Type type) {
			return INode.class;
		}

		public Class<?> visitConstructor(org.eclipse.imp.pdb.facts.type.Type type) {
			return IConstructor.class;
		}

		public Class<?> visitTuple(org.eclipse.imp.pdb.facts.type.Type type) {
			return ITuple.class;
		}

		public Class<?> visitValue(org.eclipse.imp.pdb.facts.type.Type type) {
			return IValue.class;
		}

		public Class<?> visitVoid(org.eclipse.imp.pdb.facts.type.Type type) {
			return null;
		}

		public Class<?> visitParameter(org.eclipse.imp.pdb.facts.type.Type parameterType) {
			return parameterType.getBound().accept(this);
		}

		public Class<?> visitExternal(
				org.eclipse.imp.pdb.facts.type.Type externalType) {
			return IValue.class;
		}
	}

	public Method lookupJavaMethod(Evaluator eval, FunctionDeclaration func, Environment env, boolean hasReflectiveAccess) {
		if (!func.isAbstract()) {
			throw new NonAbstractJavaFunctionError(func);
		}
		
		String className = getClassName(func);
		String name = func.getSignature().getName().toString();
		
		if (className.length() == 0) {
			throw new MissingTagError(JAVA_CLASS_TAG, func);
		}
		
		for (ClassLoader loader : loaders) {
			try {
				Class<?> clazz = loader.loadClass(className);
				Parameters parameters = func.getSignature().getParameters();
				Class<?>[] javaTypes = getJavaTypes(parameters, env, hasReflectiveAccess);

				try {
					Method m;
					

					if (javaTypes.length > 0) { // non-void
						m = clazz.getDeclaredMethod(name, javaTypes);
					}
					else {
						m = clazz.getDeclaredMethod(name);
					}

					if ((m.getModifiers() & Modifier.STATIC) == 0) {
						throw new NonStaticJavaMethodError(func);
					}

					return m;
				} catch (SecurityException e) {
					throw RuntimeExceptionFactory.permissionDenied(vf.string(e.getMessage()), eval.getCurrentAST(), eval.getStackTrace());
				} catch (NoSuchMethodException e) {
					throw new UndeclaredJavaMethodError(className + "." + name, func);
				}
			} catch (ClassNotFoundException e) {
				continue;
			}
		}
		
		throw new UndeclaredJavaMethodError(className + "." + name, func);
	}
}
