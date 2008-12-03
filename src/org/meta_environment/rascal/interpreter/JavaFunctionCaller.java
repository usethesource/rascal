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

import net.java.dev.hickory.testing.Compilation;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IDouble;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.ISourceRange;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.BoolType;
import org.eclipse.imp.pdb.facts.type.DoubleType;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.IntegerType;
import org.eclipse.imp.pdb.facts.type.ListType;
import org.eclipse.imp.pdb.facts.type.MapType;
import org.eclipse.imp.pdb.facts.type.NamedTreeType;
import org.eclipse.imp.pdb.facts.type.NamedType;
import org.eclipse.imp.pdb.facts.type.RelationType;
import org.eclipse.imp.pdb.facts.type.SetType;
import org.eclipse.imp.pdb.facts.type.SourceLocationType;
import org.eclipse.imp.pdb.facts.type.SourceRangeType;
import org.eclipse.imp.pdb.facts.type.StringType;
import org.eclipse.imp.pdb.facts.type.TreeNodeType;
import org.eclipse.imp.pdb.facts.type.TreeType;
import org.eclipse.imp.pdb.facts.type.TupleType;
import org.eclipse.imp.pdb.facts.type.ValueType;
import org.eclipse.imp.pdb.facts.type.VoidType;
import org.meta_environment.rascal.ast.Formal;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Parameters;
import org.meta_environment.rascal.ast.Type;

public class JavaFunctionCaller {
	private static final String METHOD_NAME = "call";
	private final Writer out;
	private final Map<FunctionDeclaration,Class<?>> cache = new WeakHashMap<FunctionDeclaration, Class<?>>();
	private final TypeEvaluator typeEvaluator = new TypeEvaluator();
	private final JavaTypes javaTypes = new JavaTypes();
	private final JavaClasses javaClasses = new JavaClasses();

	public JavaFunctionCaller(Writer outputWriter) {
		this.out = outputWriter;
	}

	public void compileJavaMethod(FunctionDeclaration declaration) {
		try {
			getJavaClass(declaration);
		} catch (ClassNotFoundException e) {
			throw new RascalBug("unexpected error in Java compilation", e);
		}
	}
	
	public IValue callJavaMethod(FunctionDeclaration declaration, IValue[] actuals) {
		try {
			Class<?> clazz = getJavaClass(declaration);
			Parameters parameters = declaration.getSignature().getParameters();
			Class<?>[] javaTypes = getJavaTypes(parameters);
			if (javaTypes.length > 0) { // non-void
			  Method method = clazz.getDeclaredMethod(METHOD_NAME, javaTypes);
			  return (IValue) method.invoke(actuals);
			}
			else {
				Method method = clazz.getDeclaredMethod(METHOD_NAME);
				return (IValue) method.invoke(null);
			}
		} catch (SecurityException e) {
			throw new RascalBug("Unexpected security exception", e);
		} catch (NoSuchMethodException e) {
			throw new RascalBug("Method that was just generated could not be found", e);
		} catch (ClassNotFoundException e) {
			throw new RascalBug("Class that was just generated could not be found", e);
		} catch (IllegalArgumentException e) {
			throw new RascalBug("An illegal argument was generated for a generated method", e);
		} catch (IllegalAccessException e) {
			throw new RascalBug("Unexpected illegal access exception", e);
		} catch (InvocationTargetException e) {
			throw new RascalBug("Method that was just generated in a generated class could not be called on that class", e);
		}
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
		String name = declaration.getSignature().getName().toString();
		String fullClassName = "org.meta_environment.rascal.java." + name;
		String params = getJavaFormals(declaration.getSignature()
				.getParameters());
		Compilation compilation = new Compilation();

		compilation.addSource(fullClassName).addLine(
				"package org.meta_environment.rascal.java;").
				addLine("import org.eclipse.imp.pdb.facts.type.*;").
				addLine("import org.eclipse.imp.pdb.facts.*;").
				addLine("import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;").
				addLine("import org.eclipse.imp.pdb.facts.io.*;").
				addLine("import org.eclipse.imp.pdb.facts.visitors.*;").
				addLine("public class " + name + "{").
				addLine("  private static final IValueFactory values = ValueFactory.getInstance();").
				addLine("  private static final TypeFactory types = TypeFactory.getInstance();").
				addLine("  public static IValue " + METHOD_NAME + "(" + params + ") {").
				addLine(declaration.getBody().toString()).
				addLine("  }").
				addLine("}");

		compilation.doCompile(out);

		if (compilation.getDiagnostics().size() != 0) {
			for (Diagnostic<? extends JavaFileObject> d : compilation.getDiagnostics()) {
				System.err.println(d.getMessage(null) + ":" + d.getLineNumber());
			}
			throw new RascalTypeError("Compilation of Java method failed.");
		}

		return compilation.getOutputClass(fullClassName);
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

		public Class<?> visitBool(BoolType boolType) {
			return IBool.class;
		}

		public Class<?> visitDouble(DoubleType type) {
			return IDouble.class;
		}

		public Class<?> visitInteger(IntegerType type) {
			return IInteger.class;
		}

		public Class<?> visitList(ListType type) {
			return IList.class;
		}

		public Class<?> visitMap(MapType type) {
			return IMap.class;
		}

		public Class<?> visitNamed(NamedType type) {
			return IValue.class;
		}

		public Class<?> visitNamedTree(NamedTreeType type) {
			return INode.class;
		}

		public Class<?> visitRelationType(RelationType type) {
			return IRelation.class;
		}

		public Class<?> visitSet(SetType type) {
			return ISet.class;
		}

		public Class<?> visitSourceLocation(SourceLocationType type) {
			return ISourceLocation.class;
		}

		public Class<?> visitSourceRange(SourceRangeType type) {
			return ISourceRange.class;
		}

		public Class<?> visitString(StringType type) {
			return IString.class;
		}

		public Class<?> visitTree(TreeType type) {
			return ITree.class;
		}

		public Class<?> visitTreeNode(TreeNodeType type) {
			return INode.class;
		}

		public Class<?> visitTuple(TupleType type) {
			return ITuple.class;
		}

		public Class<?> visitValue(ValueType type) {
			return IValue.class;
		}

		public Class<?> visitVoid(VoidType type) {
			return null;
		}
	}
	
	private static class JavaTypes implements ITypeVisitor<String> {
		public String visitBool(BoolType boolType) {
			return "IBool";
		}

		public String visitDouble(DoubleType type) {
			return "IDouble";
		}

		public String visitInteger(IntegerType type) {
			return "IInteger";
		}

		public String visitList(ListType type) {
			return "IList";
		}

		public String visitMap(MapType type) {
			return "IMap";
		}

		public String visitNamed(NamedType type) {
			return "IValue";
		}

		public String visitNamedTree(NamedTreeType type) {
			return "INode";
		}

		public String visitRelationType(RelationType type) {
			return "IRelation";
		}

		public String visitSet(SetType type) {
			return "ISet";
		}

		public String visitSourceLocation(SourceLocationType type) {
			return "ISourceLocation";
		}

		public String visitSourceRange(SourceRangeType type) {
			return "ISourceRange";
		}

		public String visitString(StringType type) {
			return "IString";
		}

		public String visitTree(TreeType type) {
			return "ITree";
		}

		public String visitTreeNode(TreeNodeType type) {
			return "INode";
		}

		public String visitTuple(TupleType type) {
			return "ITuple";
		}

		public String visitValue(ValueType type) {
			return "IValue";
		}

		public String visitVoid(VoidType type) {
			return null;
		}
	}
}
