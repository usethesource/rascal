package org.meta_environment.rascal.interpreter;

import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import net.java.dev.hickory.testing.Compilation;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IDouble;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.ISourceRange;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.meta_environment.rascal.ast.Formal;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Parameters;
import org.meta_environment.rascal.ast.Signature;
import org.meta_environment.rascal.ast.Tag;
import org.meta_environment.rascal.ast.Tags;
import org.meta_environment.rascal.ast.Type;
import org.meta_environment.rascal.interpreter.exceptions.RascalBug;
import org.meta_environment.rascal.interpreter.exceptions.RascalException;
import org.meta_environment.rascal.interpreter.exceptions.RascalTypeError;

public class JavaFunctionCaller {
	private static final String JAVA_IMPORTS_TAG = "javaImports";
	private static final String UNWANTED_MESSAGE_PREFIX = "org/meta_environment/rascal/java/";
	private static final String UNWANTED_MESSAGE_POSTFIX = "\\.java:";
	private static final String METHOD_NAME = "call";
	private static final String VALUE_FACTORY = "org.eclipse.imp.pdb.facts.impl.reference.ValueFactory";
	
	private final Writer out;
	private final static Map<FunctionDeclaration,Class<?>> cache = new WeakHashMap<FunctionDeclaration, Class<?>>();
	private final TypeEvaluator typeEvaluator;
	private final JavaTypes javaTypes = new JavaTypes();
	private final JavaClasses javaClasses = new JavaClasses();
	
	public JavaFunctionCaller(Writer outputWriter, TypeEvaluator te) {
		this.out = outputWriter;
		this.typeEvaluator = te;
		
		if (ToolProvider.getSystemJavaCompiler() == null) {
			throw new RascalBug("Could not find an installed System Java Compiler, please provide a Java Runtime that includes the Java Development Tools (JDK 1.6 or higher).");
		}
		
		if (ToolProvider.getSystemToolClassLoader() == null) {
			throw new RascalBug("Could not find an System Tool Class Loader, please provide a Java Runtime that includes the Java Development Tools (JDK 1.6 or higher).");
		}
	}

	public void compileJavaMethod(FunctionDeclaration declaration) {
		try {
			getJavaClass(declaration);
		} catch (ClassNotFoundException e) {
			throw new RascalBug("unexpected error in Java compilation", e);
		}
	}
	
	public IValue callJavaMethod(FunctionDeclaration declaration, IValue[] actuals) {
		Parameters parameters = declaration.getSignature().getParameters();
		Class<?>[] javaTypes = getJavaTypes(parameters);
		
		try {
			Class<?> clazz = getJavaClass(declaration);
		
			if (javaTypes.length > 0) { // non-void
			  Method method = clazz.getDeclaredMethod(METHOD_NAME, javaTypes);
			  return (IValue) method.invoke(null, (Object[]) actuals);
			}
			else {
				Method method = clazz.getDeclaredMethod(METHOD_NAME);
				return (IValue) method.invoke(null);
			}
		} catch (SecurityException e) {
			throw new RascalBug("Unexpected security exception", e);
		} catch (NoSuchMethodException e) {
			throw new RascalBug("Method that was just generated could not be found:\n" +
					"Formal parameters: " + parameters +"\n" +
					"Java Types: " + toString(javaTypes), e);
		} catch (ClassNotFoundException e) {
			throw new RascalBug("Class that was just generated could not be found", e);
		} catch (IllegalArgumentException e) {
			throw new RascalBug("An illegal argument was generated for a generated method", e);
		} catch (IllegalAccessException e) {
			throw new RascalBug("Unexpected illegal access exception", e);
		} catch (InvocationTargetException e) {
			Throwable targetException = e.getTargetException();
			
			if (targetException instanceof RascalException) {
				throw (RascalException) targetException;
			}
			else {
				throw new RascalException(ValueFactory.getInstance(), targetException.getMessage());
			}
		}
	}
	
	private String toString(Class<?>[] javaTypes) {
		StringBuilder str = new StringBuilder();
		for (Class<?> clazz : javaTypes) {
			str.append(clazz.getCanonicalName() + " ");
		}
		return str.toString();
	}

	private Class<?> getJavaClass(FunctionDeclaration declaration) throws ClassNotFoundException {
		Class<?> clazz = cache.get(declaration);
		
		if (clazz == null) {
			clazz = buildJavaClass(declaration);
			cache.put(declaration, clazz);
		}
		
		return clazz;
	}

	private Class<?> buildJavaClass(FunctionDeclaration declaration) throws ClassNotFoundException {
		Signature signature = declaration.getSignature();
		String imports = getImports(declaration);
		String name = signature.getName().toString();
		String fullClassName = "org.meta_environment.rascal.java." + name;
		String params = getJavaFormals(signature
				.getParameters());
		String result = signature.getType().accept(typeEvaluator).isVoidType() ? "void" : "IValue";
		Compilation compilation = new Compilation();

		compilation.addSource(fullClassName).addLine(
				"package org.meta_environment.rascal.java;").
				addLine("import org.meta_environment.rascal.interpreter.exceptions.RascalException;").
				addLine("import org.meta_environment.rascal.interpreter.SubList;").
				addLine("import org.eclipse.imp.pdb.facts.type.*;").
				addLine("import org.eclipse.imp.pdb.facts.*;").
				addLine("import org.eclipse.imp.pdb.facts.impl.*;").
				addLine("import org.eclipse.imp.pdb.facts.impl.reference.*;").
				addLine("import " + VALUE_FACTORY + ";").
				addLine("import org.eclipse.imp.pdb.facts.io.*;").
				addLine("import org.eclipse.imp.pdb.facts.visitors.*;").
				addLine("import java.util.Random;").
				addLine(imports).
				addLine("public class " + name + "{").
				addLine("  private static final IValueFactory values = ValueFactory.getInstance();").
				addLine("  private static final TypeFactory types = TypeFactory.getInstance();").
				addLine("  private static final Random random = new Random();").
				addLine("  public static " + result + " " + METHOD_NAME + "(" + params + ") {").
				addLine(declaration.getBody().toString()).
				addLine("  }").
				addLine("}");

		System.err.println("Classpath for compilation: " + System.getProperty("java.class.path"));
		compilation.doCompile(out);

		if (compilation.getDiagnostics().size() != 0) {
			StringBuilder messages = new StringBuilder();
			for (Diagnostic<? extends JavaFileObject> d : compilation.getDiagnostics()) {
				String message = d.getMessage(null);
				message = message.replaceAll(UNWANTED_MESSAGE_PREFIX, "").replaceAll(UNWANTED_MESSAGE_POSTFIX, ",");
				messages.append(message + "\n");
			}
			throw new RascalTypeError("Compilation of Java method failed due to the following error(s): \n" + messages.toString());
		}

		return compilation.getOutputClass(fullClassName);
	}

	private String getImports(FunctionDeclaration declaration) {
		Tags tags = declaration.getTags();
		
		if (tags.hasAnnotations()) {
			for (Tag tag : tags.getAnnotations()) {
				if (tag.getName().toString().equals(JAVA_IMPORTS_TAG)) {
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

	private String getJavaFormals(Parameters parameters) {
		StringBuffer buf = new StringBuffer();
		List<Formal> formals = parameters.getFormals().getFormals();
		Iterator<Formal> iter = formals.iterator();

		while (iter.hasNext()) {
			Formal f = iter.next();
			String javaType = toJavaType(f.getType());
			
			if (javaType != null) { // not void
			  buf.append(javaType + " " + f.getName());

			  if (iter.hasNext()) {
				  buf.append(", ");
			  }
			}
		}

		return buf.toString();
	}
	
	private Class<?>[] getJavaTypes(Parameters parameters) {
		List<Formal> formals = parameters.getFormals().getFormals();
		Class<?>[] classes = new Class<?>[formals.size()];
		for (int i = 0; i < classes.length;) {
			Class<?> clazz = toJavaClass(formals.get(i));
			
			if (clazz != null) {
			  classes[i++] = clazz;
			}
		}
		
		return classes;
	}

	private org.eclipse.imp.pdb.facts.type.Type toValueType(Formal formal) {
		return formal.accept(typeEvaluator);
	}
	
	private org.eclipse.imp.pdb.facts.type.Type toValueType(Type type) {
		return type.accept(typeEvaluator);
	}
	
	private Class<?> toJavaClass(Formal formal) {
		return toJavaClass(toValueType(formal));
	}

	private Class<?> toJavaClass(org.eclipse.imp.pdb.facts.type.Type type) {
		return type.accept(javaClasses);
	}

	private String toJavaType(Type type) {
		return toValueType(type).accept(javaTypes);
	}
	
	private static class JavaClasses implements ITypeVisitor<Class<?>> {

		public Class<?> visitBool(org.eclipse.imp.pdb.facts.type.Type boolType) {
			return IBool.class;
		}

		public Class<?> visitDouble(org.eclipse.imp.pdb.facts.type.Type type) {
			return IDouble.class;
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
			return IValue.class;
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

		public Class<?> visitSourceRange(org.eclipse.imp.pdb.facts.type.Type type) {
			return ISourceRange.class;
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
	}
	
	private static class JavaTypes implements ITypeVisitor<String> {
		public String visitBool(org.eclipse.imp.pdb.facts.type.Type boolType) {
			return "IBool";
		}

		public String visitDouble(org.eclipse.imp.pdb.facts.type.Type type) {
			return "IDouble";
		}

		public String visitInteger(org.eclipse.imp.pdb.facts.type.Type type) {
			return "IInteger";
		}

		public String visitList(org.eclipse.imp.pdb.facts.type.Type type) {
			return "IList";
		}

		public String visitMap(org.eclipse.imp.pdb.facts.type.Type type) {
			return "IMap";
		}

		public String visitAlias(org.eclipse.imp.pdb.facts.type.Type type) {
			return "IValue";
		}

		public String visitAbstractData(org.eclipse.imp.pdb.facts.type.Type type) {
			return "IConstructor";
		}

		public String visitRelationType(org.eclipse.imp.pdb.facts.type.Type type) {
			return "IRelation";
		}

		public String visitSet(org.eclipse.imp.pdb.facts.type.Type type) {
			return "ISet";
		}

		public String visitSourceLocation(org.eclipse.imp.pdb.facts.type.Type type) {
			return "ISourceLocation";
		}

		public String visitSourceRange(org.eclipse.imp.pdb.facts.type.Type type) {
			return "ISourceRange";
		}

		public String visitString(org.eclipse.imp.pdb.facts.type.Type type) {
			return "IString";
		}

		public String visitNode(org.eclipse.imp.pdb.facts.type.Type type) {
			return "INode";
		}

		public String visitConstructor(org.eclipse.imp.pdb.facts.type.Type type) {
			return "IConstructor";
		}

		public String visitTuple(org.eclipse.imp.pdb.facts.type.Type type) {
			return "ITuple";
		}

		public String visitValue(org.eclipse.imp.pdb.facts.type.Type type) {
			return "IValue";
		}

		public String visitVoid(org.eclipse.imp.pdb.facts.type.Type type) {
			return null;
		}

		public String visitParameter(org.eclipse.imp.pdb.facts.type.Type parameterType) {
			return parameterType.getBound().accept(this);
		}
	}
}
