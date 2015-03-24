package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IRational;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Opcode;


public class RVMRun implements IRVM {

	// ----------------------------
	// Exit stack, cf, sp, realized :)
	// public Object[] stack;
	// public Frame cf; // current frame
	// public int sp;
	// TODO : ccf, cccf and activeCoroutines needed to allow exception handling in coroutines. :(

	public IValueFactory vf;

	public static IBool Rascal_TRUE;
	public static IBool Rascal_FALSE;
	public static IInteger Rascal_ZERO;
	public static IInteger Rascal_ONE;

	private final TypeFactory tf;

	protected final IString NONE;
	protected final IString YIELD;
	protected final IString FAILRETURN;
	protected final IString PANIC;

	private boolean debug = true;

	protected ArrayList<Function> functionStore;
	protected Map<String, Integer> functionMap;

	// Function overloading
	private final Map<String, Integer> resolver;
	protected ArrayList<OverloadedFunction> overloadedStore;

	private TypeStore typeStore = new TypeStore();
	private final Types types;

	protected ArrayList<Type> constructorStore;
	private Map<String, Integer> constructorMap;
	public ArrayList<Frame> stacktrace = new ArrayList<Frame>();

	private final Map<IValue, IValue> moduleVariables;
	PrintWriter stdout;
	PrintWriter stderr;

	private final Map<Class<?>, Object> instanceCache;
	private final Map<String, Class<?>> classCache;

	// Management of active coroutines
	protected Stack<Coroutine> activeCoroutines = new Stack<>();
	protected Frame ccf = null; // The start frame of the current active
								// coroutine
	// (coroutine's main function)
	protected Frame cccf = null; // The candidate coroutine's start frame; used
									// by theguard semantics
	IEvaluatorContext ctx;

	private List<ClassLoader> classLoaders;

	public RascalExecutionContext rex;
	private boolean trackCalls;
	private boolean finalized;
	protected ILocationCollector locationCollector;

	public IEvaluatorContext getEvaluatorContext() {
		return rex.getEvaluatorContext();
	}

	// An exhausted coroutine instance
	public static Coroutine exhausted = new Coroutine(null) {

		@Override
		public void next(Frame previousCallFrame) {
			throw new RuntimeException("Internal error: an attempt to activate an exhausted coroutine instance.");
		}

		@Override
		public void suspend(Frame current) {
			throw new RuntimeException("Internal error: an attempt to suspend an exhausted coroutine instance.");
		}

		@Override
		public boolean isInitialized() {
			return true;
		}

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public Coroutine copy() {
			throw new RuntimeException("Internal error: an attempt to copy an exhausted coroutine instance.");
		}
	};

	@Override
	public RascalExecutionContext getRex() {
		return rex;
	}

	public RVMRun(RascalExecutionContext rex) {
		super();

		this.vf = rex.getValueFactory();
		tf = TypeFactory.getInstance();
		typeStore = rex.getTypeStore();
		this.instanceCache = new HashMap<Class<?>, Object>();
		this.classCache = new HashMap<String, Class<?>>();

		this.rex = rex;
		rex.setRVM(this);
		this.classLoaders = rex.getClassLoaders();
		this.stdout = rex.getStdOut();
		this.stderr = rex.getStdErr();
		this.debug = rex.getDebug();
		this.trackCalls = rex.getTrackCalls();
		this.finalized = false;

		this.types = new Types(this.vf);

		Rascal_TRUE = vf.bool(true);
		Rascal_FALSE = vf.bool(false);
		Rascal_ZERO = vf.integer(0);
		Rascal_ONE = vf.integer(1);

		// Return types used in code generator
		NONE = vf.string("$nothing$");
		YIELD = vf.string("$yield0$");
		FAILRETURN = vf.string("$failreturn$");
		PANIC = vf.string("$panic$");

		functionStore = new ArrayList<Function>();
		constructorStore = new ArrayList<Type>();

		functionMap = new HashMap<String, Integer>();
		constructorMap = new HashMap<String, Integer>();

		resolver = new HashMap<String, Integer>();
		overloadedStore = new ArrayList<OverloadedFunction>();

		moduleVariables = new HashMap<IValue, IValue>();

		MuPrimitive.init(vf);
		RascalPrimitive.init(this, rex);
		Opcode.init(stdout, rex.getProfile());

		this.locationCollector = NullLocationCollector.getInstance();

	}

	public Type symbolToType(IConstructor symbol) {
		return types.symbolToType(symbol, typeStore);
	}

	/**
	 * Narrow an Object as occurring on the RVM runtime stack to an IValue that can be returned. Note that various non-IValues can occur: - Coroutine - Reference - FunctionInstance -
	 * Object[] (is converted to an IList)
	 * 
	 * @param result
	 *            to be returned
	 * @return converted result or an exception
	 */
	private IValue narrow(Object result) {
		if (result instanceof Boolean) {
			return vf.bool((Boolean) result);
		}
		if (result instanceof Integer) {
			return vf.integer((Integer) result);
		}
		if (result instanceof IValue) {
			return (IValue) result;
		}
		if (result instanceof Thrown) {
			((Thrown) result).printStackTrace(stdout);
			return vf.string(((Thrown) result).toString());
		}
		if (result instanceof Object[]) {
			IListWriter w = vf.listWriter();
			Object[] lst = (Object[]) result;
			for (int i = 0; i < lst.length; i++) {
				w.append(narrow(lst[i]));
			}
			return w.done();
		}
		throw new RuntimeException("PANIC: Cannot convert object back to IValue: " + result);
	}

	/**
	 * Represent any object that can occur on the RVM stack stack as string
	 * 
	 * @param some
	 *            stack object
	 * @return its string representation
	 */
	private String asString(Object o) {
		if (o == null)
			return "null";
		if (o instanceof Boolean)
			return ((Boolean) o).toString() + " [Java]";
		if (o instanceof Integer)
			return ((Integer) o).toString() + " [Java]";
		if (o instanceof IValue)
			return ((IValue) o).toString() + " [IValue]";
		if (o instanceof Type)
			return ((Type) o).toString() + " [Type]";
		if (o instanceof Object[]) {
			StringBuilder w = new StringBuilder();
			Object[] lst = (Object[]) o;
			w.append("[");
			for (int i = 0; i < lst.length; i++) {
				w.append(asString(lst[i]));
				if (i < lst.length - 1)
					w.append(", ");
			}
			w.append("]");
			return w.toString() + " [Object[]]";
		}
		if (o instanceof Coroutine) {
			return "Coroutine[" + ((Coroutine) o).frame.function.getName() + "]";
		}
		if (o instanceof Function) {
			return "Function[" + ((Function) o).getName() + "]";
		}
		if (o instanceof FunctionInstance) {
			return "Function[" + ((FunctionInstance) o).function.getName() + "]";
		}
		if (o instanceof OverloadedFunctionInstance) {
			OverloadedFunctionInstance of = (OverloadedFunctionInstance) o;
			String alts = "";
			for (Integer fun : of.functions) {
				alts = alts + functionStore.get(fun).getName() + "; ";
			}
			return "OverloadedFunction[ alts: " + alts + "]";
		}
		if (o instanceof Reference) {
			Reference ref = (Reference) o;
			return "Reference[" + ref.stack + ", " + ref.pos + "]";
		}
		if (o instanceof IListWriter) {
			return "ListWriter[" + ((IListWriter) o).toString() + "]";
		}
		if (o instanceof ISetWriter) {
			return "SetWriter[" + ((ISetWriter) o).toString() + "]";
		}
		if (o instanceof IMapWriter) {
			return "MapWriter[" + ((IMapWriter) o).toString() + "]";
		}
		if (o instanceof Matcher) {
			return "Matcher[" + ((Matcher) o).pattern() + "]";
		}
		if (o instanceof Thrown) {
			return "THROWN[ " + asString(((Thrown) o).value) + " ]";
		}

		if (o instanceof StringBuilder) {
			return "StringBuilder[" + ((StringBuilder) o).toString() + "]";
		}
		if (o instanceof HashSet) {
			return "HashSet[" + ((HashSet) o).toString() + "]";
		}
		if (o instanceof HashMap) {
			return "HashMap[" + ((HashMap) o).toString() + "]";
		}
		if (o instanceof Map.Entry) {
			return "Map.Entry[" + ((Map.Entry) o).toString() + "]";
		}
		throw new RuntimeException("PANIC: asString cannot convert: " + o);
	}

	public String getFunctionName(int n) {
		for (String fname : functionMap.keySet()) {
			if (functionMap.get(fname) == n) {
				return fname;
			}
		}
		throw new RuntimeException("PANIC: undefined function index " + n);
	}

	public String getConstructorName(int n) {
		for (String cname : constructorMap.keySet()) {
			if (constructorMap.get(cname) == n) {
				return cname;
			}
		}
		throw new RuntimeException("PANIC: undefined constructor index " + n);
	}

	public String getOverloadedFunctionName(int n) {
		for (String ofname : resolver.keySet()) {
			if (resolver.get(ofname) == n) {
				return ofname;
			}
		}
		throw new RuntimeException("PANIC: undefined overloaded function index " + n);
	}

	public IValue executeFunction(String uid_func, IValue[] args) {
		// Frame oldCF = cf;
		// cf.sp = sp;

		int oldPostOp = postOp;
		ArrayList<Frame> oldstacktrace = stacktrace;
		Thrown oldthrown = thrown;
		int oldarity = arity;

		Function func = functionStore.get(functionMap.get(uid_func));
		Frame root = new Frame(func.scopeId, null, func.maxstack, func);
		// cf = root;

		// Pass the program arguments to main
		for (int i = 0; i < args.length; i++) {
			root.stack[i] = args[i];
		}
		Object o = dynRun(func.funId, root);

		// cf = oldCF;
		// stack = cf.stack;
		// sp = cf.sp;

		postOp = oldPostOp;
		stacktrace = oldstacktrace;
		thrown = oldthrown;
		arity = oldarity;

		if (o instanceof Thrown) {
			throw (Thrown) o;
		}
		return narrow(o);
	}

	public IValue executeFunction(FunctionInstance func, IValue[] args) {
		// Frame oldCF = cf;
		// cf.sp = sp;

		int oldPostOp = postOp;
		ArrayList<Frame> oldstacktrace = stacktrace;
		Thrown oldthrown = thrown;
		int oldarity = arity;

		Frame root = new Frame(func.function.scopeId, null, func.env, func.function.maxstack, func.function);
		// cf = root;

		// stack = cf.stack;
		// sp = func.function.nlocals;
		root.sp = func.function.nlocals;

		// Pass the program arguments to main
		for (int i = 0; i < args.length; i++) {
			root.stack[i] = args[i];
		}

		Object o = dynRun(func.function.funId, root);

		// cf = oldCF;
		// stack = cf.stack;
		// sp = cf.sp;

		postOp = oldPostOp;
		stacktrace = oldstacktrace;
		thrown = oldthrown;
		arity = oldarity;

		if (o instanceof Thrown) {
			throw (Thrown) o;
		}
		return narrow(o);
	}

	private String trace = "";

	public String getTrace() {
		return trace;
	}

	public void appendToTrace(String trace) {
		this.trace = this.trace + trace + "\n";
	}

	/*
	 * The following instance variables are only used by executeProgram
	 */
	public Frame root; // Root frame of a program
	int postOp;
	Thrown thrown;
	int arity;

	Object globalReturnValue = null;

	public Class<?> getJavaClass(String className) {
		Class<?> clazz = classCache.get(className);
		if (clazz != null) {
			return clazz;
		}
		try {
			clazz = this.getClass().getClassLoader().loadClass(className);
		} catch (ClassNotFoundException e1) {
			// If the class is not found, try other class loaders
			for (ClassLoader loader : this.classLoaders) {
				try {
					clazz = loader.loadClass(className);
					break;
				} catch (ClassNotFoundException e2) {
					;
				}
			}
		}
		if (clazz == null) {
			throw new CompilerError("Class " + className + " not found");
		}
		classCache.put(className, clazz);
		return clazz;
	}

	public Object getJavaClassInstance(Class<?> clazz) {
		Object instance = instanceCache.get(clazz);
		if (instance != null) {
			return instance;
		}
		// Class<?> clazz = null;
		// try {
		// clazz = this.getClass().getClassLoader().loadClass(className);
		// } catch(ClassNotFoundException e1) {
		// // If the class is not found, try other class loaders
		// for(ClassLoader loader : this.classLoaders) {
		// //for(ClassLoader loader : ctx.getEvaluator().getClassLoaders()) {
		// try {
		// clazz = loader.loadClass(className);
		// break;
		// } catch(ClassNotFoundException e2) {
		// ;
		// }
		// }
		// }
		try {
			Constructor<?> constructor = clazz.getConstructor(IValueFactory.class);
			instance = constructor.newInstance(vf);
			instanceCache.put(clazz, instance);
			return instance;
		} catch (IllegalArgumentException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (InstantiationException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (SecurityException e) {
			throw new ImplementationError(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new ImplementationError(e.getMessage(), e);
		}
	}

	int callJavaMethod(String methodName, String className, Type parameterTypes, Type keywordTypes, int reflect, Object[] stack, int sp) throws Throw {
		Class<?> clazz = null;
		try {
			// try {
			// clazz = this.getClass().getClassLoader().loadClass(className);
			// } catch(ClassNotFoundException e1) {
			// // If the class is not found, try other class loaders
			// for(ClassLoader loader : this.classLoaders) {
			// //for(ClassLoader loader : ctx.getEvaluator().getClassLoaders()) {
			// try {
			// clazz = loader.loadClass(className);
			// break;
			// } catch(ClassNotFoundException e2) {
			// ;
			// }
			// }
			// }
			//
			// if(clazz == null) {
			// throw new CompilerError("Class " + className + " not found, while trying to call method" + methodName);
			// }

			// Constructor<?> cons;
			// cons = clazz.getConstructor(IValueFactory.class);
			// Object instance = cons.newInstance(vf);
			clazz = getJavaClass(className);
			Object instance = getJavaClassInstance(clazz);

			Method m = clazz.getMethod(methodName, makeJavaTypes(methodName, className, parameterTypes, keywordTypes, reflect));
			int arity = parameterTypes.getArity();
			int kwArity = keywordTypes.getArity();
			int kwMaps = kwArity > 0 ? 2 : 0;
			Object[] parameters = new Object[arity + kwArity + reflect];
			int i = 0;
			while (i < arity) {
				parameters[i] = stack[sp - arity - kwMaps + i];
				i++;
			}
			if (kwArity > 0) {
				@SuppressWarnings("unchecked")
				Map<String, IValue> kwMap = (Map<String, IValue>) stack[sp - 2];
				@SuppressWarnings("unchecked")
				Map<String, Map.Entry<Type, IValue>> kwDefaultMap = (Map<String, Map.Entry<Type, IValue>>) stack[sp - 1];

				while (i < arity + kwArity) {
					String key = keywordTypes.getFieldName(i - arity);
					IValue val = kwMap.get(key);
					if (val == null) {
						val = kwDefaultMap.get(key).getValue();
					}
					parameters[i] = val;
					i++;
				}
			}

			if (reflect == 1) {
				parameters[arity + kwArity] = converted.contains(className + "." + methodName) ? this.rex : this.getEvaluatorContext(); // TODO: remove CTX
			}
			stack[sp - arity - kwMaps] = m.invoke(instance, parameters);
			return sp - arity - kwMaps + 1;
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof Throw) {
				throw (Throw) e.getTargetException();
			}
			if (e.getTargetException() instanceof Thrown) {
				throw (Thrown) e.getTargetException();
			}
			e.printStackTrace();
		}
		return sp;
	}

	HashSet<String> converted = new HashSet<String>(Arrays.asList("org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ParsingTools.parseFragment",
			"org.rascalmpl.library.experiments.Compiler.CoverageCompiled.startCoverage", "org.rascalmpl.library.experiments.Compiler.CoverageCompiled.stopCoverage",
			"org.rascalmpl.library.experiments.Compiler.CoverageCompiled.getCoverage", "org.rascalmpl.library.experiments.Compiler.ProfileCompiled.startProfile",
			"org.rascalmpl.library.experiments.Compiler.ProfileCompiled.stopProfile", "org.rascalmpl.library.experiments.Compiler.ProfileCompiled.getProfile",
			"org.rascalmpl.library.experiments.Compiler.ProfileCompiled.reportProfile", "org.rascalmpl.library.lang.csv.IOCompiled.readCSV",
			"org.rascalmpl.library.lang.csv.IOCompiled.getCSVType", "org.rascalmpl.library.lang.csv.IOCompiled.writeCSV", "org.rascalmpl.library.lang.json.IOCompiled.fromJSON",
			"org.rascalmpl.library.PreludeCompiled.exists", "org.rascalmpl.library.PreludeCompiled.lastModified", "org.rascalmpl.library.PreludeCompiled.implode",
			"org.rascalmpl.library.PreludeCompiled.isDirectory", "org.rascalmpl.library.PreludeCompiled.isFile", "org.rascalmpl.library.PreludeCompiled.remove",
			"org.rascalmpl.library.PreludeCompiled.mkDirectory", "org.rascalmpl.library.PreludeCompiled.listEntries", "org.rascalmpl.library.PreludeCompiled.parse",
			"org.rascalmpl.library.PreludeCompiled.readFile", "org.rascalmpl.library.PreludeCompiled.readFileEnc", "org.rascalmpl.library.PreludeCompiled.md5HashFile",
			"org.rascalmpl.library.PreludeCompiled.writeFile", "org.rascalmpl.library.PreludeCompiled.writeFileEnc", "org.rascalmpl.library.PreludeCompiled.writeBytes",
			"org.rascalmpl.library.PreludeCompiled.appendToFile", "org.rascalmpl.library.PreludeCompiled.appendToFileEnc", "org.rascalmpl.library.PreludeCompiled.readFileLines",
			"org.rascalmpl.library.PreludeCompiled.readFileLinesEnc", "org.rascalmpl.library.PreludeCompiled.readFileBytes", "org.rascalmpl.library.PreludeCompiled.getFileLength",
			"org.rascalmpl.library.PreludeCompiled.readBinaryValueFile", "org.rascalmpl.library.PreludeCompiled.readTextValueFile",
			"org.rascalmpl.library.PreludeCompiled.readTextValueString", "org.rascalmpl.library.PreludeCompiled.writeBinaryValueFile",
			"org.rascalmpl.library.PreludeCompiled.writeTextValueFile", "org.rascalmpl.library.util.MonitorCompiled.startJob", "org.rascalmpl.library.util.MonitorCompiled.event",
			"org.rascalmpl.library.util.MonitorCompiled.endJob", "org.rascalmpl.library.util.MonitorCompiled.todo", "org.rascalmpl.library.util.ReflectiveCompiled.getModuleLocation",
			"org.rascalmpl.library.util.ReflectiveCompiled.getSearchPathLocation"

	/*
	 * TODO: cobra::util::outputlogger::startLog cobra::util::outputlogger::getLog cobra::quickcheck::_quickcheck cobra::quickcheck::arbitrary
	 * 
	 * experiments::Compiler::RVM::Interpreter::ParsingTools::parseFragment experiments::Compiler::RVM::Run::executeProgram
	 * 
	 * experiments::resource::Resource::registerResource experiments::resource::Resource::getTypedResource experiments::resource::Resource::generateTypedInterfaceInternal
	 * 
	 * experiments::vis2::vega::Vega::color
	 * 
	 * lang::aterm::IO::readTextATermFile lang::aterm::IO::writeTextATermFile
	 * 
	 * lang::html::IO::readHTMLFile
	 * 
	 * lang::java::m3::AST::setEnvironmentOptions lang::java::m3::AST::createAstFromFile lang::java::m3::AST::createAstFromString lang::java::m3::Core::createM3FromFile
	 * lang::java::m3::Core::createM3FromFile lang::java::m3::Core::createM3FromJarClass
	 * 
	 * lang::jvm::run::RunClassFile::runClassFile lang::jvm::transform::SerializeClass::serialize
	 * 
	 * lang::rsf::IO::readRSF lang::rsf::IO::getRSFTypes lang::rsf::IO::readRSFRelation
	 * 
	 * lang::yaml::Model::loadYAML lang::yaml::Model::dumpYAML
	 * 
	 * resource::jdbc::JDBC::registerJDBCClass util::tasks::Manager util::Eval util::Monitor util::Reflective
	 * 
	 * util::Webserver
	 * 
	 * vis::Figure::color
	 * 
	 * Traversal::getTraversalContext
	 * 
	 * tutor::HTMLGenerator
	 * 
	 * **eclipse** util::Editors util::FastPrint util::HtmlDisplay util::IDE util::ResourceMarkers vis::Render vis::RenderSWT
	 */
	));

	Class<?>[] makeJavaTypes(String methodName, String className, Type parameterTypes, Type keywordTypes, int reflect) {
		JavaClasses javaClasses = new JavaClasses();
		int arity = parameterTypes.getArity();
		int kwArity = keywordTypes.getArity();
		Class<?>[] jtypes = new Class<?>[arity + kwArity + reflect];

		int i = 0;
		while (i < parameterTypes.getArity()) {
			jtypes[i] = parameterTypes.getFieldType(i).accept(javaClasses);
			i++;
		}

		while (i < arity + kwArity) {
			jtypes[i] = keywordTypes.getFieldType(i - arity).accept(javaClasses);
			i++;
		}

		if (reflect == 1) {
			jtypes[arity + kwArity] = converted.contains(className + "." + methodName) ? RascalExecutionContext.class : IEvaluatorContext.class; // TODO: remove CTX
		}
		return jtypes;
	}

	Class<?>[] makeJavaTypes(Type parameterTypes, int reflect) {
		JavaClasses javaClasses = new JavaClasses();
		int arity = parameterTypes.getArity() + reflect;
		Class<?>[] jtypes = new Class<?>[arity];

		for (int i = 0; i < parameterTypes.getArity(); i++) {
			jtypes[i] = parameterTypes.getFieldType(i).accept(javaClasses);
		}
		if (reflect == 1) {
			try {
				jtypes[arity - 1] = this.getClass().getClassLoader().loadClass("org.rascalmpl.interpreter.IEvaluatorContext");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return jtypes;
	}

	private static class JavaClasses implements ITypeVisitor<Class<?>, RuntimeException> {

		@Override
		public Class<?> visitBool(org.eclipse.imp.pdb.facts.type.Type boolType) {
			return IBool.class;
		}

		@Override
		public Class<?> visitReal(org.eclipse.imp.pdb.facts.type.Type type) {
			return IReal.class;
		}

		@Override
		public Class<?> visitInteger(org.eclipse.imp.pdb.facts.type.Type type) {
			return IInteger.class;
		}

		@Override
		public Class<?> visitRational(org.eclipse.imp.pdb.facts.type.Type type) {
			return IRational.class;
		}

		@Override
		public Class<?> visitNumber(org.eclipse.imp.pdb.facts.type.Type type) {
			return INumber.class;
		}

		@Override
		public Class<?> visitList(org.eclipse.imp.pdb.facts.type.Type type) {
			return IList.class;
		}

		@Override
		public Class<?> visitMap(org.eclipse.imp.pdb.facts.type.Type type) {
			return IMap.class;
		}

		@Override
		public Class<?> visitAlias(org.eclipse.imp.pdb.facts.type.Type type) {
			return type.getAliased().accept(this);
		}

		@Override
		public Class<?> visitAbstractData(org.eclipse.imp.pdb.facts.type.Type type) {
			return IConstructor.class;
		}

		@Override
		public Class<?> visitSet(org.eclipse.imp.pdb.facts.type.Type type) {
			return ISet.class;
		}

		@Override
		public Class<?> visitSourceLocation(org.eclipse.imp.pdb.facts.type.Type type) {
			return ISourceLocation.class;
		}

		@Override
		public Class<?> visitString(org.eclipse.imp.pdb.facts.type.Type type) {
			return IString.class;
		}

		@Override
		public Class<?> visitNode(org.eclipse.imp.pdb.facts.type.Type type) {
			return INode.class;
		}

		@Override
		public Class<?> visitConstructor(org.eclipse.imp.pdb.facts.type.Type type) {
			return IConstructor.class;
		}

		@Override
		public Class<?> visitTuple(org.eclipse.imp.pdb.facts.type.Type type) {
			return ITuple.class;
		}

		@Override
		public Class<?> visitValue(org.eclipse.imp.pdb.facts.type.Type type) {
			return IValue.class;
		}

		@Override
		public Class<?> visitVoid(org.eclipse.imp.pdb.facts.type.Type type) {
			return null;
		}

		@Override
		public Class<?> visitParameter(org.eclipse.imp.pdb.facts.type.Type parameterType) {
			return parameterType.getBound().accept(this);
		}

		@Override
		public Class<?> visitExternal(org.eclipse.imp.pdb.facts.type.Type externalType) {
			return IValue.class;
		}

		@Override
		public Class<?> visitDateTime(Type type) {
			return IDateTime.class;
		}
	}

	public void inject(ArrayList<Function> functionStore2, ArrayList<OverloadedFunction> overloadedStore2, ArrayList<Type> constructorStore2, TypeStore typeStore2,
			Map<String, Integer> functionMap2) {
		// TODO check if we can generate code for them.
		this.functionStore = functionStore2;
		this.overloadedStore = overloadedStore2;
		this.constructorStore = constructorStore2;
		this.typeStore = typeStore2;
		this.functionMap = functionMap2;
	}

	// final public void insnPOP() {
	// sp--;
	// }

	// public int insnPOP(int sp) {
	// return --sp;
	// }
	//
	// public void insnLOADLOC0() {
	// stack[sp++] = stack[0];
	// }
	//
	// public void insnLOADLOC1() {
	// stack[sp++] = stack[1];
	// }
	//
	// public void insnLOADLOC2() {
	// stack[sp++] = stack[2];
	// }
	//
	// public void insnLOADLOC3() {
	// stack[sp++] = stack[3];
	// }
	//
	// public void insnLOADLOC4() {
	// stack[sp++] = stack[4];
	// }
	//
	// public void insnLOADLOC5() {
	// stack[sp++] = stack[5];
	// }
	//
	// public void insnLOADLOC6() {
	// stack[sp++] = stack[6];
	// }
	//
	// public void insnLOADLOC7() {
	// stack[sp++] = stack[7];
	// }
	//
	// public void insnLOADLOC8() {
	// stack[sp++] = stack[8];
	// }
	//
	// public void insnLOADLOC9() {
	// stack[sp++] = stack[9];
	// }
	//
	// public void insnLOADLOC(int i) {
	// stack[sp++] = stack[i];
	// }

	public int insnLOADBOOLTRUE(Object[] stack, int sp) {
		stack[sp++] = Rascal_TRUE;
		return sp;
	}

	public int insnLOADBOOLFALSE(Object[] stack, int sp) {
		stack[sp++] = Rascal_FALSE;
		return sp;
	}

	public int insnLOADINT(Object[] stack, int sp, int i) {
		stack[sp++] = i;
		return sp;
	}

	public int insnLOADCON(Object[] stack, int sp, Frame cf, int arg1) {
		stack[sp++] = cf.function.constantStore[arg1];
		return sp++;
	}

	public int insnLOADLOCREF(Object[] lstack, int lsp, int args1) {
		lstack[lsp++] = new Reference(lstack, args1);
		return lsp;
	}

	public int insnLOADTYPE(Object[] stack, int sp, Frame cf, int arg1) {
		stack[sp++] = cf.function.typeConstantStore[arg1];
		return sp;
	}

	public int insnLOADLOCDEREF(Object[] stack, int sp, int loc) {
		Reference ref = (Reference) stack[loc];
		stack[sp++] = ref.stack[ref.pos];
		return sp;
	}

	public int insnUNWRAPTHROWNLOC(Object[] stack, int sp, int target) {
		stack[target] = ((Thrown) stack[--sp]).value;
		return sp;
	}

	public void insnSTORELOCDEREF(Object[] stack, int sp, int loc) {
		Reference ref = (Reference) stack[loc];
		ref.stack[ref.pos] = stack[sp - 1];
	}

	public int insnLOADFUN(Object[] stack, int sp, int fun) {
		stack[sp++] = new FunctionInstance(functionStore.get(fun), root, this);
		return sp;
	}

	public int insnLOAD_NESTED_FUN(Object[] stack, int sp, Frame cf, int fun, int scopeIn) {
		stack[sp++] = FunctionInstance.computeFunctionInstance(functionStore.get(fun), cf, scopeIn, this);
		return sp;
	}

	public int insnLOADOFUN(Object[] stack, int sp, Frame cf, int ofun) {
		OverloadedFunction of = overloadedStore.get(ofun);
		stack[sp++] = of.scopeIn == -1 ? new OverloadedFunctionInstance(of.functions, of.constructors, root, functionStore, constructorStore, this) : OverloadedFunctionInstance
				.computeOverloadedFunctionInstance(of.functions, of.constructors, cf, of.scopeIn, functionStore, constructorStore, this);
		return sp;
	}

	public int insnLOADCONSTR(Object[] stack, int sp, int construct) {
		Type constructor = constructorStore.get(construct);
		stack[sp++] = constructor;
		return sp;
	}

	public int insnLOADVAR(Object[] stack, int sp, Frame cf, int scopeid, int pos, boolean maxArg2) {
		postOp = 0;
		Object rval;

		if (maxArg2) {
			rval = moduleVariables.get(cf.function.constantStore[scopeid]);
			if (rval == null) {
				postOp = Opcode.POSTOP_CHECKUNDEF;
				return sp; // TODO break INSTRUCTION;
			}
			stack[sp++] = rval;
			return sp;
		}

		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == scopeid) {
				rval = fr.stack[pos];
				if (rval == null) {
					postOp = Opcode.POSTOP_CHECKUNDEF;
					return sp; // TODO break INSTRUCTION;
				}
				stack[sp++] = rval;
				return sp;
			}
		}
		throw new RuntimeException("insnLOADVAR cannot find matching scope: " + scopeid);
	}

	public int insnLOADVARREF(Object[] stack, int sp, Frame cf, int scopeid, int pos, boolean maxarg2) {
		Object rval;
		if (maxarg2) {
			rval = moduleVariables.get(cf.function.constantStore[scopeid]);
			stack[sp++] = rval;
			return sp;
		}

		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == scopeid) {
				rval = new Reference(fr.stack, pos);
				stack[sp++] = rval;
				return sp;
			}
		}
		throw new RuntimeException("LOADVAR or LOADVARREF cannot find matching scope: " + scopeid);
	}

	public int insnLOADVARDEREF(Object[] stack, int sp, Frame cf, int scopeid, int pos) {
		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == scopeid) {
				Reference ref = (Reference) fr.stack[pos];
				stack[sp++] = ref.stack[ref.pos];
				return sp;
			}
		}
		throw new RuntimeException("LOADVARDEREF cannot find matching scope: " + scopeid);
	}

	public void insnSTOREVAR(Object[] stack, int sp, Frame cf, int scopeid, int pos, boolean maxarg2) {
		if (maxarg2) {
			IValue mvar = cf.function.constantStore[scopeid];
			moduleVariables.put(mvar, (IValue) stack[sp - 1]);
			return;
		}
		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == scopeid) {
				// TODO: We need to re-consider how to guarantee
				// safe use of both Java objects and IValues
				fr.stack[pos] = stack[sp - 1];
				return;
			}
		}
		throw new RuntimeException("STOREVAR cannot find matching scope: " + scopeid);
	}

	public int insnUNWRAPTHROWNVAR(Object[] stack, int sp, Frame cf, int scopeid, int pos, boolean maxarg2) {
		if (maxarg2) {
			IValue mvar = cf.function.constantStore[scopeid];
			moduleVariables.put(mvar, (IValue) stack[sp - 1]);
			return sp;
		}
		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == scopeid) {
				// TODO: We need to re-consider how to guarantee safe use of
				// both Java objects and IValues
				fr.stack[pos] = ((Thrown) stack[--sp]).value;
				return sp;
			}
		}
		throw new RuntimeException("UNWRAPTHROWNVAR cannot find matching scope: " + scopeid);
	}

	public void insnSTOREVARDEREF(Object[] stack, int sp, Frame cf, int scopeid, int pos) {
		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == scopeid) {
				Reference ref = (Reference) fr.stack[pos];
				ref.stack[ref.pos] = stack[sp - 1];
				return;
			}
		}
		throw new RuntimeException("STOREVARDEREF cannot find matching scope: " + scopeid);
	}

	@SuppressWarnings("unchecked")
	public int insnCALLCONSTR(Object[] stack, int sp, int constrctr, int arity) {
		Type constructor = constructorStore.get(constrctr);

		// arity = CodeBlock.fetchArg2(instruction);
		// cf.src = (ISourceLocation) cf.function.constantStore[instructions[pc++]];

		IValue[] args = new IValue[constructor.getArity()];

		java.util.Map<String, IValue> kwargs;
		Type type = (Type) stack[--sp];
		if (type.getArity() > 0) {
			// Constructors with keyword parameters
			kwargs = (java.util.Map<String, IValue>) stack[--sp];
		} else {
			kwargs = new HashMap<String, IValue>();
		}

		for (int i = 0; i < constructor.getArity(); i++) {
			args[constructor.getArity() - 1 - i] = (IValue) stack[--sp];
		}
		stack[sp++] = vf.constructor(constructor, args, kwargs);

		return sp;
	}

	public int insnCALLJAVA(Object[] stack, int sp, Frame cf, int m, int c, int p, int k, int r) {

		// int arity;
		// postOp = 0;
		// String methodName = ((IString) cf.function.constantStore[m]).getValue();
		// String className = ((IString) cf.function.constantStore[c]).getValue();
		// Type parameterTypes = cf.function.typeConstantStore[p];
		// int reflect = r;
		// arity = parameterTypes.getArity();
		// try {
		// sp = callJavaMethod(methodName, className, parameterTypes, reflect, stack, sp);
		// } catch (Throw e) {
		// thrown = Thrown.getInstance(e.getException(), e.getLocation(), new ArrayList<Frame>());
		// postOp = Opcode.POSTOP_HANDLEEXCEPTION;
		// return sp; // TODO break INSTRUCTION;
		// }
		// return sp;

		String methodName = ((IString) cf.function.constantStore[m]).getValue();
		String className = ((IString) cf.function.constantStore[c]).getValue();
		Type parameterTypes = cf.function.typeConstantStore[p];
		Type keywordTypes = cf.function.typeConstantStore[k];
		int reflect = r;
		arity = parameterTypes.getArity();
		try {
			sp = callJavaMethod(methodName, className, parameterTypes, keywordTypes, reflect, stack, sp);
		} catch (Throw e) {
			stacktrace.add(cf);
			thrown = Thrown.getInstance(e.getException(), e.getLocation(), cf);
			// postOp = Opcode.POSTOP_HANDLEEXCEPTION; break INSTRUCTION;
		} catch (Thrown e) {
			stacktrace.add(cf);
			thrown = e;
			// postOp = Opcode.POSTOP_HANDLEEXCEPTION; break INSTRUCTION;
		} catch (Exception e) {
			e.printStackTrace(stderr);
			throw new CompilerError("Exception in CALLJAVA: " + className + "." + methodName + "; message: " + e.getMessage() + e.getCause(), cf);
		}

		return sp;

	}

	public int insnAPPLY(Object[] lstack, int lsp, int function, int arity) {
		FunctionInstance fun_instance;
		Function fun = functionStore.get(function);
		assert arity <= fun.nformals;
		assert fun.scopeIn == -1;
		fun_instance = FunctionInstance.applyPartial(fun, root, this, arity, lstack, lsp);
		lsp = lsp - arity;
		lstack[lsp++] = fun_instance;
		return lsp;
	}

	public int insnAPPLYDYN(Object[] stack, int sp, int arity) {
		FunctionInstance fun_instance;
		Object src = stack[--sp];
		if (src instanceof FunctionInstance) {
			fun_instance = (FunctionInstance) src;
			assert arity + fun_instance.next <= fun_instance.function.nformals;
			fun_instance = fun_instance.applyPartial(arity, stack, sp);
		} else {
			throw new RuntimeException("Unexpected argument type for APPLYDYN: " + asString(src));
		}
		sp = sp - arity;
		stack[sp++] = fun_instance;
		return sp;
	}

	public int insnSUBSCRIPTARRAY(Object[] stack, int sp) {
		sp--;
		stack[sp - 1] = ((Object[]) stack[sp - 1])[((Integer) stack[sp])];
		return sp;
	}

	public int insnSUBSCRIPTLIST(Object[] stack, int sp) {
		sp--;
		stack[sp - 1] = ((IList) stack[sp - 1]).get((Integer) stack[sp]);
		return sp;
	}

	public int insnLESSINT(Object[] stack, int sp) {
		sp--;
		stack[sp - 1] = ((Integer) stack[sp - 1]) < ((Integer) stack[sp]) ? Rascal_TRUE : Rascal_FALSE;
		return sp;
	}

	public int insnGREATEREQUALINT(Object[] stack, int sp) {
		sp--;
		stack[sp - 1] = ((Integer) stack[sp - 1]) >= ((Integer) stack[sp]) ? Rascal_TRUE : Rascal_FALSE;
		return sp;
	}

	public int insnADDINT(Object[] stack, int sp) {
		sp--;
		stack[sp - 1] = ((Integer) stack[sp - 1]) + ((Integer) stack[sp]);
		return sp;
	}

	public int insnSUBTRACTINT(Object[] stack, int sp) {
		sp--;
		stack[sp - 1] = ((Integer) stack[sp - 1]) - ((Integer) stack[sp]);
		return sp;
	}

	public int insnANDBOOL(Object[] stack, int sp) {
		sp--;
		stack[sp - 1] = ((IBool) stack[sp - 1]).and((IBool) stack[sp]);
		return sp;
	}

	public void insnTYPEOF(Object[] stack, int sp) {
		if (stack[sp - 1] instanceof HashSet<?>) { // For the benefit of set
													// matching
			@SuppressWarnings("unchecked")
			HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
			if (mset.isEmpty()) {
				stack[sp - 1] = tf.setType(tf.voidType());
			} else {
				IValue v = mset.iterator().next();
				stack[sp - 1] = tf.setType(v.getType());
			}
		} else {
			stack[sp - 1] = ((IValue) stack[sp - 1]).getType();
		}
	}

	public int insnSUBTYPE(Object[] stock, int sop) {
		sop--;
		stock[sop - 1] = vf.bool(((Type) stock[sop - 1]).isSubtypeOf((Type) stock[sop]));
		return sop;
	}

	public int insnCHECKARGTYPEANDCOPY(Object[] lstack, int lsp, Frame cof, int loc, int type, int toLoc) {
		Type argType = ((IValue) lstack[loc]).getType();
		Type paramType = cof.function.typeConstantStore[type];

		if (argType.isSubtypeOf(paramType)) {
			lstack[toLoc] = lstack[loc];
			lstack[lsp++] = vf.bool(true);
		} else {
			lstack[lsp++] = vf.bool(false);
		}
		return lsp;
	}

	public void insnLABEL() {
		throw new RuntimeException("label instruction at runtime");
	}

	public void insnHALT(Object[] stack, int sp) {
		if (debug) {
			stdout.println("Program halted:");
			for (int i = 0; i < sp; i++) {
				stdout.println(i + ": " + stack[i]);
			}
		}
		return; // TODO stack[sp - 1];
	}

	public int insnPRINTLN(Object[] stack, int sp, int arity) {
		StringBuilder w = new StringBuilder();
		for (int i = arity - 1; i >= 0; i--) {
			String str = (stack[sp - 1 - i] instanceof IString) ? ((IString) stack[sp - 1 - i]).toString() : asString(stack[sp - 1 - i]);
			w.append(str).append(" ");
		}
		stdout.println(w.toString());
		sp = sp - arity + 1;
		return sp;
	}

	public void insnTHROW(Object[] stack, int sp, Frame cf) {
		Object obj = stack[--sp];
		thrown = null;
		if (obj instanceof IValue) {
			stacktrace = new ArrayList<Frame>();
			stacktrace.add(cf);
			// thrown = Thrown.getInstance((IValue) obj, null, stacktrace);
		} else {
			// Then, an object of type 'Thrown' is on top of the stack
			thrown = (Thrown) obj;
		}
		postOp = Opcode.POSTOP_HANDLEEXCEPTION;
		// TODO break INSTRUCTION;
	}

	public int insnLOADLOCKWP(Object[] stack, int sp, Frame cf, int constant) {
		IString name = (IString) cf.function.codeblock.getConstantValue(constant);
		@SuppressWarnings("unchecked")
		Map<String, Map.Entry<Type, IValue>> defaults = (Map<String, Map.Entry<Type, IValue>>) stack[cf.function.nformals];
		Map.Entry<Type, IValue> defaultValue = defaults.get(name.getValue());
		for (Frame f = cf; f != null; f = f.previousCallFrame) {
			IMap kargs = (IMap) f.stack[f.function.nformals - 1];
			if (kargs.containsKey(name)) {
				IValue val = kargs.get(name);
				if (val.getType().isSubtypeOf(defaultValue.getKey())) {
					stack[sp++] = val;
					return sp;
				}
			}
		}
		stack[sp++] = defaultValue.getValue();
		return sp;
	}

	public void insnLOADVARKWP() {
		return;
	}

	public void insnSTORELOCKWP(Object[] stack, int sp, Frame cf, int constant) {
		IValue val = (IValue) stack[sp - 1];
		IString name = (IString) cf.function.codeblock.getConstantValue(constant);
		IMap kargs = (IMap) stack[cf.function.nformals - 1];
		stack[cf.function.nformals - 1] = kargs.put(name, val);
	}

	public void insnSTOREVARKWP() {
		return;
	}

	public int insnLOADCONT(Object[] stack, int sp, Frame cf, int scopeid) {
		assert stack[0] instanceof Coroutine;
		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == scopeid) {
				// TODO: unsafe in general case (the coroutine object should be
				// copied)
				stack[sp++] = fr.stack[0];
				return sp;
			}
		}
		throw new RuntimeException("LOADCONT cannot find matching scope: " + scopeid);
	}

	// / JVM Helper methods
	public Object dynRun(String fname, IValue[] args) {
		int n = functionMap.get(fname);

		Function func = functionStore.get(n);

		Frame root = new Frame(func.scopeId, null, func.maxstack, func);
		// cf = root;

		// stack = cf.stack;

		root.stack[0] = vf.list(args); // pass the program argument to
		root.stack[1] = vf.mapWriter().done();

		// sp = func.nlocals;
		root.sp = func.nlocals;

		Object result = dynRun(n, root);
		return result;
	}

	public Object dynRun(int n, Frame cf) {
		System.out.println("Unimplemented Base called !");
		return PANIC;
	}

	public Object return0Helper(Object[] st0ck, int spp, Frame cof) {

		Object rval = null;

		boolean returns = cof.isCoroutine;
		if (returns) {
			rval = Rascal_TRUE;
		}

		if (cof == ccf) {
			activeCoroutines.pop();
			ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
		}

		if (returns) {
			cof.previousCallFrame.stack[cof.previousCallFrame.sp++] = rval;
		}

		return rval;
	}

	public Object return1Helper(Object[] lstack, int sop, Frame cof) {
		Object rval = null;
		if (cof.isCoroutine) {
			rval = Rascal_TRUE;
			int[] refs = cof.function.refs;
			if (arity != refs.length) {
				throw new RuntimeException("Coroutine " + cof.function.name + ": arity of return (" + arity + ") unequal to number of reference parameters (" + refs.length + ")");
			}
			for (int i = 0; i < arity; i++) {
				Reference ref = (Reference) lstack[refs[arity - 1 - i]];
				ref.stack[ref.pos] = lstack[--sop];
			}
		} else {
			rval = lstack[sop - 1];
		}
		if (cof.previousCallFrame != null) {
			cof.previousCallFrame.stack[cof.previousCallFrame.sp++] = rval;
		}
		return rval;
	}

	public int jvmCREATE(Object[] stock, int lsp, Frame lcf, int fun, int arity) {
		cccf = lcf.getCoroutineFrame(functionStore.get(fun), root, arity, lsp);
		cccf.previousCallFrame = lcf;

		// lcf.sp = modified by getCoroutineFrame.
		dynRun(fun, cccf); // Run untill guard, leaves coroutine instance in stack.
		return lcf.sp;
	}

	public int jvmCREATEDYN(Object[] lstack, int lsp, Frame lcf, int arity) {
		FunctionInstance fun_instance;

		Object src = lstack[--lsp];

		if (!(src instanceof FunctionInstance)) {
			throw new RuntimeException("Unexpected argument type for CREATEDYN: " + src.getClass() + ", " + src);
		}

		// In case of partial parameter binding
		fun_instance = (FunctionInstance) src;
		cccf = lcf.getCoroutineFrame(fun_instance, arity, lsp);
		cccf.previousCallFrame = lcf;

		// lcf.sp = modified by getCoroutineFrame.
		dynRun(fun_instance.function.funId, cccf);
		return lcf.sp;
	}

	public int typeSwitchHelper(Object[] lstack, int lsp) { // stackpointer calc is done in the inline part.
		IValue val = (IValue) lstack[lsp];
		Type t = null;
		if (val instanceof IConstructor) {
			t = ((IConstructor) val).getConstructorType();
		} else {
			t = val.getType();
		}
		return ToplevelType.getToplevelTypeAsInt(t);
	}

	public int switchHelper(Object[] stack, int spp, boolean useConcreteFingerprint) {
		IValue val = (IValue) stack[spp];
		IInteger fp = vf.integer(ToplevelType.getFingerprint(val, useConcreteFingerprint));
		int toReturn = fp.intValue();
		return toReturn;
	}

	public boolean guardHelper(Object[] stack, int sp) {
		Object rval = stack[sp - 1];
		boolean precondition;
		if (rval instanceof IBool) {
			precondition = ((IBool) rval).getValue();
		} else {
			throw new RuntimeException("Guard's expression has to be boolean!");
		}
		return precondition;
	}

	public void yield1Helper(Frame lcf, Object[] lstack, int lsp, int arity2, int ep) {
		// Stores a Rascal_TRUE value into the stack of the NEXT? caller.
		// The inline yield1 does the return

		// /**/if (lsp != sp)
		// throw new RuntimeException();

		Coroutine coroutine = activeCoroutines.pop();
		ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;

		coroutine.start.previousCallFrame.stack[coroutine.start.previousCallFrame.sp++] = Rascal_TRUE;

		int[] refs = lcf.function.refs;

		for (int i = 0; i < arity2; i++) {
			Reference ref = (Reference) lstack[refs[arity2 - 1 - i]];
			ref.stack[ref.pos] = lstack[--lsp];
		}

		lcf.hotEntryPoint = ep;
		lcf.sp = lsp;

		coroutine.frame = lcf;
		coroutine.suspended = true;

		// /**/cf = lcf.previousCallFrame;
		// /**/sp = lcf.previousCallFrame.sp;
		// /**/ stack = cf.stack;
	}

	public void yield0Helper(Frame lcf, Object[] lstack, int lsp, int ep) {
		// Stores a Rascal_TRUE value into the stack of the NEXT? caller.
		// The inline yield0 does the return
		// /**/if (lsp != sp)
		// throw new RuntimeException();

		Coroutine coroutine = activeCoroutines.pop();
		ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;

		coroutine.start.previousCallFrame.stack[coroutine.start.previousCallFrame.sp++] = Rascal_TRUE;

		lcf.hotEntryPoint = ep;
		lcf.sp = lsp;

		coroutine.frame = lcf;
		coroutine.suspended = true;

		// cf = lcf.previousCallFrame;
		// sp = lcf.previousCallFrame.sp;
		// stack = cf.stack;
	}

	public Object callHelper(Object[] lstack, int lsp, Frame lcf, int funid, int arity, int ep) {
		Frame tmp;
		Function fun;
		Object rval;

		if (lcf.hotEntryPoint != ep) {
			fun = functionStore.get(funid);
			// In case of partial parameter binding
			if (arity < fun.nformals) {
				FunctionInstance fun_instance = FunctionInstance.applyPartial(fun, root, this, arity, lstack, lsp);
				lsp = lsp - arity;
				lstack[lsp++] = fun_instance;
				lcf.sp = lsp;
				return NONE;
			}
			tmp = lcf.getFrame(fun, root, arity, lsp);
			lcf.nextFrame = tmp;
		} else {
			tmp = lcf.nextFrame;
			fun = tmp.function;
		}
		tmp.previousCallFrame = lcf;

		// cf = tmp;
		// sp = tmp.sp;

		rval = dynRun(fun.funId, tmp); // In a full inline version we can call the
										// function directly (name is known).

		if (rval.equals(YIELD)) {
			// drop my stack
			lcf.hotEntryPoint = ep;
			// lcf.sp = sp;

			// sp = cf.previousCallFrame.sp;
			// cf = cf.previousCallFrame;
			// stack = cf.stack;
			return YIELD; // Will cause the inline call to return YIELD
		} else {
			lcf.hotEntryPoint = 0;
			lcf.nextFrame = null; // Allow GC to clean
			return NONE; // Inline call will continue execution
		}
	}

	public int jvmNEXT0(Object[] lstack, int spp, Frame lcf) {

		// if (spp != sp) {
		// System.err.println("FIXIT SP adjusted (entry next) " + spp + " should be " + sp);
		// spp = sp;
		// }

		Coroutine coroutine = (Coroutine) lstack[--spp];
		// Merged the hasNext and next semantics
		if (!coroutine.hasNext()) {
			lstack[spp++] = Rascal_FALSE;
			return spp;
		}
		// put the coroutine onto the stack of active coroutines
		activeCoroutines.push(coroutine);
		ccf = coroutine.start;
		coroutine.next(lcf);

		// Push something on the stack of the prev yielding function
		coroutine.frame.stack[coroutine.frame.sp++] = null;

		lcf.sp = spp;

		coroutine.frame.previousCallFrame = lcf;

		// sp = coroutine.entryFrame.sp;
		// cf = coroutine.entryFrame;
		// stack = cf.stack;

		dynRun(coroutine.entryFrame.function.funId, coroutine.entryFrame);

		// if ( lcf.sp != sp ) {
		// System.err.println("FIXIT SP adjusted (return next) " + spp + " != " + sp);
		// spp = sp;
		// }
		return lcf.sp;
	}

	public Object exhaustHelper(Object[] lstack, int sop, Frame cof) {
		if (cof == ccf) {
			activeCoroutines.pop();
			ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
		}

		// /**/cf = cof.previousCallFrame;
		if (cof.previousCallFrame == null) {
			return Rascal_FALSE;
		}
		cof.previousCallFrame.stack[cof.previousCallFrame.sp++] = Rascal_FALSE; // 'Exhaust' has to always return FALSE,

		// /**/ stack = cof.previousCallFrame.stack;
		// /**/sp = cof.previousCallFrame.sp;

		return NONE;// i.e., signal a failure;
	}

	// jvmOCALL has an issue
	// There are 3 possible ways to reset the stack pointer sp
	// 1: Done by nextFrame
	// 2: Not done by nextFrame (there is no frame)
	// 3: todo after the constructor call.
	// Problem there was 1 frame and the function failed.
	public int jvmOCALL(Object[] lstack, int sop, Frame lcf, int ofun, int arity) {
		boolean stackPointerAdjusted = false;

		// if (sp != sop)
		// throw new RuntimeException();

		lcf.sp = sop;

		OverloadedFunctionInstanceCall ofun_call = null;
		OverloadedFunction of = overloadedStore.get(ofun);

		ofun_call = of.scopeIn == -1 ? new OverloadedFunctionInstanceCall(lcf, of.functions, of.constructors, root, null, arity) : OverloadedFunctionInstanceCall
				.computeOverloadedFunctionInstanceCall(lcf, of.functions, of.constructors, of.scopeIn, null, arity);

		Frame frame = ofun_call.nextFrame(functionStore);

		while (frame != null) {
			stackPointerAdjusted = true; // See text

			// cf = frame;
			// stack = cf.stack;
			// sp = frame.sp;

			Object rsult = dynRun(frame.function.funId, frame);
			if (rsult.equals(NONE)) {
				return lcf.sp; // Alternative matched.
			}
			frame = ofun_call.nextFrame(functionStore);
		}
		Type constructor = ofun_call.nextConstructor(constructorStore);
		if (stackPointerAdjusted == false) {
			sop = sop - arity;
		}
		lstack[sop++] = vf.constructor(constructor, ofun_call.getConstructorArguments(constructor.getArity()));
		lcf.sp = sop;
		return sop;
	}

	public int jvmOCALLDYN(Object[] lstack, int sop, Frame lcf, int typesel, int arity) {
		Object funcObject = lstack[--sop];
		OverloadedFunctionInstanceCall ofunCall = null;
		lcf.sp = sop;

		// Get function types to perform a type-based dynamic
		// resolution
		Type types = lcf.function.codeblock.getConstantType(typesel);
		// Objects of two types may appear on the stack:
		// 1. FunctionInstance due to closures whom will have no overloading
		if (funcObject instanceof FunctionInstance) {
			FunctionInstance fun_instance = (FunctionInstance) funcObject;
			Frame frame = lcf.getFrame(fun_instance.function, fun_instance.env, arity, sop);
			frame.previousCallFrame = lcf;
			// stack = cf.stack;
			// sp = cf.sp;
			dynRun(frame.function.funId, frame);
			return lcf.sp;
		}
		// 2. OverloadedFunctionInstance due to named Rascal
		// functions
		OverloadedFunctionInstance of_instance = (OverloadedFunctionInstance) funcObject;
		ofunCall = new OverloadedFunctionInstanceCall(lcf, of_instance.functions, of_instance.constructors, of_instance.env, types, arity);

		boolean stackPointerAdjusted = false;
		Frame frame = ofunCall.nextFrame(functionStore);
		while (frame != null) {
			stackPointerAdjusted = true; // See text at OCALL
			// cf = frame;
			// stack = cf.stack;
			// sp = frame.sp;
			Object rsult = dynRun(frame.function.funId, frame);
			if (rsult.equals(NONE)) {
				return lcf.sp; // Alternative matched.
			}
			frame = ofunCall.nextFrame(functionStore);
		}
		Type constructor = ofunCall.nextConstructor(constructorStore);
		if (stackPointerAdjusted == false) {
			sop = sop - arity;
		}
		lstack[sop++] = vf.constructor(constructor, ofunCall.getConstructorArguments(constructor.getArity()));
		lcf.sp = sop;
		return sop;
	}

	public Object calldynHelper(Object[] lstack, int lsp, final Frame lcf, int arity, int ep) {
		// In case of CALLDYN, the stack top value of type 'Type'
		// leads to a constructor call
		// This instruction is a monstrosity it should be split in three.
		// if (lsp != sp)
		// throw new RuntimeException();

		Frame tmp;
		Object rval;

		if (lcf.hotEntryPoint != ep) {
			if (lstack[lsp - 1] instanceof Type) {
				Type constr = (Type) lstack[--lsp];
				arity = constr.getArity();
				IValue[] args = new IValue[arity];
				for (int i = arity - 1; i >= 0; i--) {
					args[i] = (IValue) lstack[lsp - arity + i];
				}
				lsp = lsp - arity;
				lstack[lsp++] = vf.constructor(constr, args);
				lcf.sp = lsp;
				return NONE; // DO not return continue execution
			}

			if (lstack[lsp - 1] instanceof FunctionInstance) {
				FunctionInstance fun_instance = (FunctionInstance) lstack[--lsp];
				// In case of partial parameter binding
				if (fun_instance.next + arity < fun_instance.function.nformals) {
					fun_instance = fun_instance.applyPartial(arity, lstack, lsp);
					lsp = lsp - arity;
					lstack[lsp++] = fun_instance;
					lcf.sp = lsp;
					return NONE;
				}
				tmp = lcf.getFrame(fun_instance.function, fun_instance.env, fun_instance.args, arity, lsp);
				lcf.nextFrame = tmp;
			} else {
				throw new RuntimeException("Unexpected argument type for CALLDYN: " + asString(lstack[lsp - 1]));
			}
		} else {
			tmp = lcf.nextFrame;
		}

		tmp.previousCallFrame = lcf;

		// this.cf = tmp;
		// this.stack = cf.stack;
		// this.sp = cf.sp;

		rval = dynRun(tmp.function.funId, tmp); // In a inline version we can call the
												// function directly.
		if (rval.equals(YIELD)) {
			// Save reentry point
			lcf.hotEntryPoint = ep;
			// lcf.sp = lsp;

			// drop my stack, and return
			// cf = cf.previousCallFrame;
			// sp = cf.sp;
			// stack = cf.stack;
			return YIELD; // Will cause the inline call to return YIELD
		} else {
			lcf.hotEntryPoint = 0;
			lcf.nextFrame = null; // Allow GC to clean
			return NONE; // Inline call will continue execution
		}
	}

	public Thrown thrownHelper(Frame cf, Object[] stack, int sp) {
		Object obj = stack[sp];
		Thrown thrown = null;
		if (obj instanceof IValue) {
			thrown = Thrown.getInstance((IValue) obj, null, cf);
		} else {
			thrown = (Thrown) obj;
		}
		return thrown ;
	}

	// public void failReturnHelper() {
	// // repair stack after failreturn;
	// // Small helper can be inlined ?
	// // The inline part returns the failure.
	// cf = cf.previousCallFrame;
	// stack = cf.stack;
	// sp = cf.sp;
	// }

	// Next methods are forced by the interface implementation
	// temporarily needed to facilitate 3 RVM implementations.

	@Override
	public void declare(Function f) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void declareConstructor(String name, IConstructor symbol) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addResolver(IMap resolver) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void fillOverloadedStore(IList overloadedStore) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PrintWriter getStdErr() {
		return rex.getStdErr();
	}

	@Override
	public IValueFactory getValueFactory() {
		return vf;
	}

	@Override
	public void setLocationCollector(ILocationCollector collector) {
		locationCollector = collector;
	}

	@Override
	public void resetLocationCollector() {
		locationCollector = NullLocationCollector.getInstance();
	}

	@Override
	public IValue executeProgram(String string, String uid_module_init, IValue[] arguments) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PrintWriter getStdOut() {
		return rex.getStdOut();
	}

	public void validateInstructionAdressingLimits() {
		int nfs = functionStore.size();
		System.out.println("size functionStore: " + nfs);
		if (nfs >= CodeBlock.maxArg) {
			throw new CompilerError("functionStore size " + nfs + "exceeds limit " + CodeBlock.maxArg);
		}
		int ncs = constructorStore.size();
		System.out.println("size constructorStore: " + ncs);
		if (ncs >= CodeBlock.maxArg) {
			throw new CompilerError("constructorStore size " + ncs + "exceeds limit " + CodeBlock.maxArg);
		}
		int nov = overloadedStore.size();
		System.out.println("size overloadedStore: " + nov);
		if (nov >= CodeBlock.maxArg) {
			throw new CompilerError("constructorStore size " + nov + "exceeds limit " + CodeBlock.maxArg);
		}
	}

	public static void debugPOP(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADLOC0(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADLOC1(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADLOC2(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADLOC3(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADLOC4(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADLOC5(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADLOC6(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADLOC7(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADLOC8(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADLOC9(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADLOC(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugRESETLOCS(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADBOOL(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADINT(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADCON(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADLOCREF(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugCALLMUPRIM(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugJMP(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugJMPTRUE(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugJMPFALSE(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugTYPESWITCH(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugJMPINDEXED(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugSWITCH(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADTYPE(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADLOCDEREF(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugSTORELOC(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugUNWRAPTHROWNLOC(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugSTORELOCDEREF(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADFUN(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOAD_NESTED_FUN(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADOFUN(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADCONSTR(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADVAR(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADVARREF(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADVARDEREF(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugSTOREVAR(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugUNWRAPTHROWNVAR(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugSTOREVARDEREF(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugCALLCONSTR(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugCALLDYN(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugCALL(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugOCALLDYN(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugOCALL(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugCHECKARGTYPEANDCOPY(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugFAILRETURN(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugFILTERRETURN(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugRETURN0(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugRETURN1(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugCALLJAVA(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugCREATE(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugCREATEDYN(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugGUARD(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugAPPLY(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugAPPLYDYN(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugNEXT0(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugNEXT1(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugYIELD0(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugYIELD1(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugEXHAUST(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugCALLPRIM(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugSUBSCRIPTARRAY(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugSUBSCRIPTLIST(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLESSINT(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugGREATEREQUALINT(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugADDINT(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugSUBTRACTINT(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugANDBOOL(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugTYPEOF(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugSUBTYPE(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLABEL(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugHALT(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugPRINTLN(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugTHROW(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADLOCKWP(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADVARKWP(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugSTORELOCKWP(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugSTOREVARKWP(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugLOADCONT(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugRESET(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}

	public static void debugSHIFT(Frame lcf, int lsp) {
		if (lcf == null)
			throw new RuntimeException();
	}
}
