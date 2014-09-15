package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Opcode;

public class RVMRun implements IRVM {

	public int sp;
	public Object[] stack;
	public Frame cf; // current frame
	public IValueFactory vf;

	public static IBool Rascal_TRUE;
	public static IBool Rascal_FALSE;

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

	// Management of active coroutines
	protected Stack<Coroutine> activeCoroutines = new Stack<>();
	protected Frame ccf = null; // The start frame of the current active
								// coroutine
	// (coroutine's main function)
	protected Frame cccf = null; // The candidate coroutine's start frame; used
									// by the
	// guard semantics
	IEvaluatorContext ctx;

	private List<ClassLoader> classLoaders;

	public RascalExecutionContext rex;

	IEvaluatorContext getEvaluatorContext() {
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

	public RVMRun(RascalExecutionContext rascalExecutionContext) {
		super();

		rex = rascalExecutionContext;
		this.vf = rex.getValueFactory();

		this.classLoaders = rex.getClassLoaders();
		this.stdout = rex.getStdOut();
		this.stderr = rex.getStdErr();
		this.debug = rex.getDebug();

		this.types = new Types(this.vf);

		Rascal_TRUE = vf.bool(true);
		Rascal_FALSE = vf.bool(false);

		// Return types used in code generator
		NONE = vf.string("$nothing$");
		YIELD = vf.string("$yield0$");
		FAILRETURN = vf.string("$failreturn$");
		PANIC = vf.string("$panic$");

		tf = TypeFactory.getInstance();

		moduleVariables = new HashMap<IValue, IValue>();

		functionStore = new ArrayList<Function>();
		constructorStore = new ArrayList<Type>();

		functionMap = new HashMap<String, Integer>();
		constructorMap = new HashMap<String, Integer>();

		resolver = new HashMap<String, Integer>();
		overloadedStore = new ArrayList<OverloadedFunction>();

		MuPrimitive.init(vf, stdout, rex.getProfile());
		RascalPrimitive.init(this, rex);
		Opcode.init(stdout, rex.getProfile());
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
		Frame oldCF = cf;
		cf.sp = sp;

		int oldPostOp = postOp;
		ArrayList<Frame> oldstacktrace = stacktrace;
		Thrown oldthrown = thrown;
		int oldarity = arity;

		Function func = functionStore.get(functionMap.get(uid_func));
		Frame root = new Frame(func.scopeId, null, func.maxstack, func);
		cf = root;

		// Pass the program arguments to main
		for (int i = 0; i < args.length; i++) {
			cf.stack[i] = args[i];
		}
		Object o = dynRun(func.funId, cf);

		cf = oldCF;
		stack = cf.stack;
		sp = cf.sp;

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
		Frame oldCF = cf;
		cf.sp = sp;

		int oldPostOp = postOp;
		ArrayList<Frame> oldstacktrace = stacktrace;
		Thrown oldthrown = thrown;
		int oldarity = arity;

		Frame root = new Frame(func.function.scopeId, null, func.env, func.function.maxstack, func.function);
		cf = root;

		stack = cf.stack;
		sp = func.function.nlocals;
		cf.sp = sp;

		// Pass the program arguments to main
		for (int i = 0; i < args.length; i++) {
			stack[i] = args[i];
		}

		Object o = dynRun(func.function.funId, cf);

		cf = oldCF;
		stack = cf.stack;
		sp = cf.sp;

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

	int callJavaMethod(String methodName, String className, Type parameterTypes, int reflect, Object[] stack, int sp) throws Throw {
		Class<?> clazz = null;
		try {
			try {
				clazz = this.getClass().getClassLoader().loadClass(className);
			} catch (ClassNotFoundException e1) {
				// If the class is not found, try other class loaders
				for (ClassLoader loader : this.classLoaders) {
					// for(ClassLoader loader : ctx.getEvaluator().getClassLoaders()) {
					try {
						clazz = loader.loadClass(className);
						break;
					} catch (ClassNotFoundException e2) {
						;
					}
				}
			}

			if (clazz == null) {
				throw new CompilerError("Class not found: " + className);
			}

			Constructor<?> cons;
			cons = clazz.getConstructor(IValueFactory.class);
			Object instance = cons.newInstance(vf);
			Method m = clazz.getMethod(methodName, makeJavaTypes(parameterTypes, reflect));
			int nformals = parameterTypes.getArity();
			Object[] parameters = new Object[nformals + reflect];
			for (int i = 0; i < nformals; i++) {
				parameters[i] = stack[sp - nformals + i];
			}
			if (reflect == 1) {
				parameters[nformals] = this.getEvaluatorContext();
			}
			stack[sp - nformals] = m.invoke(instance, parameters);
			return sp - nformals + 1;
		}
		// catch (ClassNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
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
			e.printStackTrace();
		}
		return sp;
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

	final public void insnPOP() {
		sp--;
	}

	public int insnPOP(int sp) {
		return --sp;
	}

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

	// public void insnLOADBOOL(int i) {
	// stack[sp++] = i == 1 ? Rascal_TRUE : Rascal_FALSE;
	// }

	// public void insnLOADBOOLTRUE() {
	// stack[sp++] = Rascal_TRUE;
	// }

	public int insnLOADBOOLTRUE(Object[] stack, int sp) {
		stack[sp++] = Rascal_TRUE;
		return sp;
	}

	// public void insnLOADBOOLFALSE() {
	// stack[sp++] = Rascal_FALSE;
	// }

	public int insnLOADBOOLFALSE(Object[] stack, int sp) {
		stack[sp++] = Rascal_FALSE;
		return sp;
	}

	// public void insnLOADINT(int i) {
	// stack[sp++] = i;
	// }

	public int insnLOADINT(Object[] stack, int sp, int i) {
		stack[sp++] = i;
		return sp;
	}

	// public void insnLOADCON(int arg1) {
	// stack[sp++] = cf.function.constantStore[arg1];
	// }

	public int insnLOADCON(Object[] stack, int sp, Frame cf, int arg1) {
		stack[sp++] = cf.function.constantStore[arg1];
		return sp++;
	}

	// public void insnLOADLOCREF(int i) {
	// stack[sp++] = new Reference(stack, i);
	// }
	public int insnLOADLOCREF(Object[] stack, int sp, int args1) {
		stack[sp++] = new Reference(stack, args1);
		return sp;
	}

	// In this partial implementation fully inlined.
	// public void insnCALLMUPRIM(int arg1, int arg2) {
	// sp = MuPrimitive.values[arg1].execute(stack, sp, arg2);
	// }

	// public void insnLOADTYPE(int i) {
	// stack[sp++] = cf.function.typeConstantStore[i];
	// }
	public int insnLOADTYPE(Object[] stack, int sp, Frame cf, int arg1) {
		stack[sp++] = cf.function.typeConstantStore[arg1];
		return sp;
	}

	// public void insnLOADLOCDEREF(int loc) {
	// Reference ref = (Reference) stack[loc];
	// stack[sp++] = ref.stack[ref.pos];
	// }

	public int insnLOADLOCDEREF(Object[] stack, int sp, int loc) {
		Reference ref = (Reference) stack[loc];
		stack[sp++] = ref.stack[ref.pos];
		return sp;
	}

	// public void insnSTORELOC(int target) {
	// stack[target] = stack[sp - 1];
	// }
	//
	// public void insnSTORELOC(Object[] stack,int sp, int target) {
	// stack[target] = stack[sp - 1];
	// }

	public int insnUNWRAPTHROWNLOC(Object[] stack, int sp, int target) {
		stack[target] = ((Thrown) stack[--sp]).value;
		return sp;
	}

	// public void insnSTORELOCDEREF(int loc) {
	// Reference ref = (Reference) stack[loc];
	// ref.stack[ref.pos] = stack[sp - 1];
	// }
	public void insnSTORELOCDEREF(Object[] stack, int sp, int loc) {
		Reference ref = (Reference) stack[loc];
		ref.stack[ref.pos] = stack[sp - 1];
	}

	// public void insnLOADFUN(int fun) {
	// stack[sp++] = new FunctionInstance(functionStore.get(fun), root, this);
	// }
	public int insnLOADFUN(Object[] stack, int sp, int fun) {
		stack[sp++] = new FunctionInstance(functionStore.get(fun), root, this);
		return sp;
	}

	// public void insnLOAD_NESTED_FUN(int fun, int scopeIn) {
	// stack[sp++] = FunctionInstance.computeFunctionInstance(functionStore.get(fun), cf, scopeIn, this);
	// }
	public int insnLOAD_NESTED_FUN(Object[] stack, int sp, Frame cf, int fun, int scopeIn) {
		stack[sp++] = FunctionInstance.computeFunctionInstance(functionStore.get(fun), cf, scopeIn, this);
		return sp;
	}

	// public void insnLOADOFUN(int ofun) {
	// OverloadedFunction of = overloadedStore.get(ofun);
	// stack[sp++] = of.scopeIn == -1 ? new OverloadedFunctionInstance(of.functions, of.constructors, root, functionStore, constructorStore, this) : OverloadedFunctionInstance
	// .computeOverloadedFunctionInstance(of.functions, of.constructors, cf, of.scopeIn, functionStore, constructorStore, this);
	// }
	public int insnLOADOFUN(Object[] stack, int sp, Frame cf, int ofun) {
		OverloadedFunction of = overloadedStore.get(ofun);
		stack[sp++] = of.scopeIn == -1 ? new OverloadedFunctionInstance(of.functions, of.constructors, root, functionStore, constructorStore, this) : OverloadedFunctionInstance
				.computeOverloadedFunctionInstance(of.functions, of.constructors, cf, of.scopeIn, functionStore, constructorStore, this);
		return sp;
	}

	// public void insnLOADCONSTR(int construct) {
	// Type constructor = constructorStore.get(construct);
	// stack[sp++] = constructor;
	// }
	public int insnLOADCONSTR(Object[] stack, int sp, int construct) {
		Type constructor = constructorStore.get(construct);
		stack[sp++] = constructor;
		return sp;
	}

	// public void insnLOADVAR(int scopeid, int pos, boolean maxArg2) {
	// postOp = 0;
	// Object rval;
	//
	// if (maxArg2) {
	// rval = moduleVariables.get(cf.function.constantStore[scopeid]);
	// if (rval == null) {
	// postOp = Opcode.POSTOP_CHECKUNDEF;
	// return; // TODO break INSTRUCTION;
	// }
	// stack[sp++] = rval;
	// return;
	// }
	//
	// for (Frame fr = cf; fr != null; fr = fr.previousScope) {
	// if (fr.scopeId == scopeid) {
	// rval = fr.stack[pos];
	// if (rval == null) {
	// postOp = Opcode.POSTOP_CHECKUNDEF;
	// return; // TODO break INSTRUCTION;
	// }
	// stack[sp++] = rval;
	// return;
	// }
	// }
	// throw new RuntimeException("LOADVAR cannot find matching scope: " + scopeid);
	// }
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
		throw new RuntimeException("LOADVAR cannot find matching scope: " + scopeid);
	}

	// public void insnLOADVARREF(int scopeid, int pos, boolean maxarg2) {
	// Object rval;
	// if (maxarg2) {
	// rval = moduleVariables.get(cf.function.constantStore[scopeid]);
	// stack[sp++] = rval;
	// return;
	// }
	//
	// for (Frame fr = cf; fr != null; fr = fr.previousScope) {
	// if (fr.scopeId == scopeid) {
	// rval = new Reference(fr.stack, pos);
	// stack[sp++] = rval;
	// return;
	// }
	// }
	// throw new RuntimeException("LOADVAR or LOADVARREF cannot find matching scope: " + scopeid);
	// }

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

	// public void insnLOADVARDEREF(int scopeid, int pos) {
	// for (Frame fr = cf; fr != null; fr = fr.previousScope) {
	// if (fr.scopeId == scopeid) {
	// Reference ref = (Reference) fr.stack[pos];
	// stack[sp++] = ref.stack[ref.pos];
	// return;
	// }
	// }
	// throw new RuntimeException("LOADVARDEREF cannot find matching scope: " + scopeid);
	// }
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

	// public void insnSTOREVAR(int scopeid, int pos, boolean maxarg2) {
	// if (maxarg2) {
	// IValue mvar = cf.function.constantStore[scopeid];
	// moduleVariables.put(mvar, (IValue) stack[sp - 1]);
	// return;
	// }
	// for (Frame fr = cf; fr != null; fr = fr.previousScope) {
	// if (fr.scopeId == scopeid) {
	// // TODO: We need to re-consider how to guarantee
	// // safe use of both Java objects and IValues
	// fr.stack[pos] = stack[sp - 1];
	// return;
	// }
	// }
	// throw new RuntimeException("STOREVAR cannot find matching scope: " + scopeid);
	// }
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

	// public void insnUNWRAPTHROWNVAR(int scopeid, int pos, boolean maxarg2) {
	// if (maxarg2) {
	// IValue mvar = cf.function.constantStore[scopeid];
	// moduleVariables.put(mvar, (IValue) stack[sp - 1]);
	// return;
	// }
	// for (Frame fr = cf; fr != null; fr = fr.previousScope) {
	// if (fr.scopeId == scopeid) {
	// // TODO: We need to re-consider how to guarantee safe use of
	// // both Java objects and IValues
	// fr.stack[pos] = ((Thrown) stack[--sp]).value;
	// return;
	// }
	// }
	// throw new RuntimeException("UNWRAPTHROWNVAR cannot find matching scope: " + scopeid);
	// }
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

	// public void insnSTOREVARDEREF(int scopeid, int pos) {
	// for (Frame fr = cf; fr != null; fr = fr.previousScope) {
	// if (fr.scopeId == scopeid) {
	// Reference ref = (Reference) fr.stack[pos];
	// ref.stack[ref.pos] = stack[sp - 1];
	// return;
	// }
	// }
	// throw new RuntimeException("STOREVARDEREF cannot find matching scope: " + scopeid);
	// }
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

	// public void insnCALLCONSTR(int constrctr, int arity) {
	// Type constructor = constructorStore.get(constrctr);
	//
	// IValue[] args = null;
	// if (arity == constructor.getArity()) {
	// args = new IValue[arity];
	// } else {
	// // Constructors with keyword parameters
	// Type type = (Type) stack[--sp];
	// IMap kwargs = (IMap) stack[--sp];
	// Object[] types = new Object[2 * constructor.getArity() + 2 * kwargs.size()];
	// int j = 0;
	// for (int i = 0; i < constructor.getArity(); i++) {
	// types[j++] = constructor.getFieldType(i);
	// types[j++] = constructor.getFieldName(i);
	// }
	// args = new IValue[constructor.getArity() + kwargs.size()];
	// for (int i = 0; i < type.getArity(); i++) {
	// types[j++] = type.getFieldType(i);
	// types[j++] = type.getFieldName(i);
	// args[constructor.getArity() + i] = kwargs.get(vf.string(type.getFieldName(i)));
	// }
	// constructor = tf.constructorFromTuple(typeStore, constructor.getAbstractDataType(), constructor.getName(), tf.tupleType(types), constructor.getArity());
	// }
	// for (int i = 0; i < constructor.getPositionalArity(); i++) {
	// args[constructor.getPositionalArity() - 1 - i] = (IValue) stack[--sp];
	// }
	// stack[sp++] = vf.constructor(constructor, args);
	// }
	public int insnCALLCONSTR(Object[] stack, int sp,int constrctr, int arity) {
		Type constructor = constructorStore.get(constrctr);

		IValue[] args = null;
		if (arity == constructor.getArity()) {
			args = new IValue[arity];
		} else {
			// Constructors with keyword parameters
			Type type = (Type) stack[--sp];
			IMap kwargs = (IMap) stack[--sp];
			Object[] types = new Object[2 * constructor.getArity() + 2 * kwargs.size()];
			int j = 0;
			for (int i = 0; i < constructor.getArity(); i++) {
				types[j++] = constructor.getFieldType(i);
				types[j++] = constructor.getFieldName(i);
			}
			args = new IValue[constructor.getArity() + kwargs.size()];
			for (int i = 0; i < type.getArity(); i++) {
				types[j++] = type.getFieldType(i);
				types[j++] = type.getFieldName(i);
				args[constructor.getArity() + i] = kwargs.get(vf.string(type.getFieldName(i)));
			}
			constructor = tf.constructorFromTuple(typeStore, constructor.getAbstractDataType(), constructor.getName(), tf.tupleType(types), constructor.getArity());
		}
		for (int i = 0; i < constructor.getPositionalArity(); i++) {
			args[constructor.getPositionalArity() - 1 - i] = (IValue) stack[--sp];
		}
		stack[sp++] = vf.constructor(constructor, args);
		return sp;
	}

//	public void insnCALLJAVA(int m, int c, int p, int r) {
//		int arity;
//		postOp = 0;
//		String methodName = ((IString) cf.function.constantStore[m]).getValue();
//		String className = ((IString) cf.function.constantStore[c]).getValue();
//		Type parameterTypes = cf.function.typeConstantStore[p];
//		int reflect = r;
//		arity = parameterTypes.getArity();
//		try {
//			sp = callJavaMethod(methodName, className, parameterTypes, reflect, stack, sp);
//		} catch (Throw e) {
//			thrown = Thrown.getInstance(e.getException(), e.getLocation(), new ArrayList<Frame>());
//			postOp = Opcode.POSTOP_HANDLEEXCEPTION;
//			return; // TODO break INSTRUCTION;
//		}
//		return;
//	}
	public int insnCALLJAVA(Object[] stack, int sp, Frame cf, int m, int c, int p, int r) {
		
		int arity;
		postOp = 0;
		String methodName = ((IString) cf.function.constantStore[m]).getValue();
		String className = ((IString) cf.function.constantStore[c]).getValue();
		Type parameterTypes = cf.function.typeConstantStore[p];
		int reflect = r;
		arity = parameterTypes.getArity();
		try {
			sp = callJavaMethod(methodName, className, parameterTypes, reflect, stack, sp);
		} catch (Throw e) {
			thrown = Thrown.getInstance(e.getException(), e.getLocation(), new ArrayList<Frame>());
			postOp = Opcode.POSTOP_HANDLEEXCEPTION;
			return sp; // TODO break INSTRUCTION;
		}
		return sp;
	}

//	public void insnAPPLY(int function, int arity) {
//		FunctionInstance fun_instance;
//		Function fun = functionStore.get(function);
//		assert arity <= fun.nformals;
//		assert fun.scopeIn == -1;
//		fun_instance = FunctionInstance.applyPartial(fun, root, this, arity, stack, sp);
//		sp = sp - arity;
//		stack[sp++] = fun_instance;
//		return;
//	}
	public int insnAPPLY(Object[] stack, int sp, int function, int arity) {
		FunctionInstance fun_instance;
		Function fun = functionStore.get(function);
		assert arity <= fun.nformals;
		assert fun.scopeIn == -1;
		fun_instance = FunctionInstance.applyPartial(fun, root, this, arity, stack, sp);
		sp = sp - arity;
		stack[sp++] = fun_instance;
		return sp;
	}

//	public void insnAPPLYDYN(int arity) {
//		FunctionInstance fun_instance;
//		Object src = stack[--sp];
//		if (src instanceof FunctionInstance) {
//			fun_instance = (FunctionInstance) src;
//			assert arity + fun_instance.next <= fun_instance.function.nformals;
//			fun_instance = fun_instance.applyPartial(arity, stack, sp);
//		} else {
//			throw new RuntimeException("Unexpected argument type for APPLYDYN: " + asString(src));
//		}
//		sp = sp - arity;
//		stack[sp++] = fun_instance;
//	}
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

	// public void insnCALLPRIM(int muprim, int arity) {
	// postOp = 0;
	// try {
	// sp = RascalPrimitive.values[muprim].execute(stack, sp, arity, stacktrace);
	// } catch (Exception exception) {
	// if (!(exception instanceof Thrown)) {
	// throw exception;
	// }
	// thrown = (Thrown) exception;
	// thrown.stacktrace.add(cf);
	// sp = sp - arity;
	// postOp = Opcode.POSTOP_HANDLEEXCEPTION;
	// }
	// }

	// public void insnSUBSCRIPTARRAY() {
	// sp--;
	// stack[sp - 1] = ((Object[]) stack[sp - 1])[((Integer) stack[sp])];
	// }
	public int insnSUBSCRIPTARRAY(Object[] stack, int sp) {
		sp--;
		stack[sp - 1] = ((Object[]) stack[sp - 1])[((Integer) stack[sp])];
		return sp;
	}

	// public void insnSUBSCRIPTLIST() {
	// sp--;
	// stack[sp - 1] = ((IList) stack[sp - 1]).get((Integer) stack[sp]);
	// }
	public int insnSUBSCRIPTLIST(Object[] stack, int sp) {
		sp--;
		stack[sp - 1] = ((IList) stack[sp - 1]).get((Integer) stack[sp]);
		return sp;
	}

//	public void insnLESSINT() {
//		sp--;
//		stack[sp - 1] = ((Integer) stack[sp - 1]) < ((Integer) stack[sp]) ? Rascal_TRUE : Rascal_FALSE;
//	}
	public int insnLESSINT(Object[] stack, int sp) {
		sp--;
		stack[sp - 1] = ((Integer) stack[sp - 1]) < ((Integer) stack[sp]) ? Rascal_TRUE : Rascal_FALSE;
		return sp;
	}

//	public void insnGREATEREQUALINT() {
//		sp--;
//		stack[sp - 1] = ((Integer) stack[sp - 1]) >= ((Integer) stack[sp]) ? Rascal_TRUE : Rascal_FALSE;
//	}
	public int insnGREATEREQUALINT(Object[] stack, int sp) {
		sp--;
		stack[sp - 1] = ((Integer) stack[sp - 1]) >= ((Integer) stack[sp]) ? Rascal_TRUE : Rascal_FALSE;
		return sp;
	}

	// public void insnADDINT() {
	// sp--;
	// stack[sp - 1] = ((Integer) stack[sp - 1]) + ((Integer) stack[sp]);
	// }
	public int insnADDINT(Object[] stack, int sp) {
		sp--;
		stack[sp - 1] = ((Integer) stack[sp - 1]) + ((Integer) stack[sp]);
		return sp;
	}

	// public void insnSUBTRACTINT() {
	// sp--;
	// stack[sp - 1] = ((Integer) stack[sp - 1]) - ((Integer) stack[sp]);
	// }
	public int insnSUBTRACTINT(Object[] stack, int sp) {
		sp--;
		stack[sp - 1] = ((Integer) stack[sp - 1]) - ((Integer) stack[sp]);
		return sp;
	}

	// public void insnANDBOOL() {
	// sp--;
	// stack[sp - 1] = ((IBool) stack[sp - 1]).and((IBool) stack[sp]) ;
	// }
	public int insnANDBOOL(Object[] stack, int sp) {
		sp--;
		stack[sp - 1] = ((IBool) stack[sp - 1]).and((IBool) stack[sp]);
		return sp;
	}

//	public void insnTYPEOF() {
//		if (stack[sp - 1] instanceof HashSet<?>) { // For the benefit of set
//													// matching
//			@SuppressWarnings("unchecked")
//			HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
//			if (mset.isEmpty()) {
//				stack[sp - 1] = tf.setType(tf.voidType());
//			} else {
//				IValue v = mset.iterator().next();
//				stack[sp - 1] = tf.setType(v.getType());
//			}
//		} else {
//			stack[sp - 1] = ((IValue) stack[sp - 1]).getType();
//		}
//	}
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

	// public void insnSUBTYPE() {
	// sp--;
	// stack[sp - 1] = vf.bool(((Type) stack[sp - 1]).isSubtypeOf((Type) stack[sp]));
	// }
	public int insnSUBTYPE(Object[] stack, int sp) {
		sp--;
		stack[sp - 1] = vf.bool(((Type) stack[sp - 1]).isSubtypeOf((Type) stack[sp]));
		return sp;
	}

	// public void insnCHECKARGTYPE() {
	// sp--;
	// Type argType = ((IValue) stack[sp - 1]).getType();
	// Type paramType = ((Type) stack[sp]);
	// stack[sp - 1] = argType.isSubtypeOf(paramType) ? Rascal_TRUE : Rascal_FALSE;
	// }
	public int insnCHECKARGTYPE(Object[] stack, int sp) {
		sp--;
		Type argType = ((IValue) stack[sp - 1]).getType();
		Type paramType = ((Type) stack[sp]);
		stack[sp - 1] = argType.isSubtypeOf(paramType) ? Rascal_TRUE : Rascal_FALSE;
		return sp;
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
			thrown = Thrown.getInstance((IValue) obj, null, stacktrace);
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

	public void insnSTORELOCKWP(Object[] stack,int sp, Frame cf,int constant) {
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
		cf = root;

		stack = cf.stack;

		cf.stack[0] = vf.list(args); // pass the program argument to
		cf.stack[1] = vf.mapWriter().done();

		sp = func.nlocals;
		cf.sp = this.sp;

		Object result = dynRun(n, cf);
		return result;
	}

	public Object dynRun(int n, Frame cf) {
		System.out.println("Unimplemented Base called !");
		return PANIC;
	}

	public Object return1Helper(Object[] stock) {
		Object rval = null;
		if (cf.isCoroutine) {
			rval = Rascal_TRUE;
			int[] refs = cf.function.refs;
			if (arity != refs.length) {
				throw new RuntimeException("Coroutine " + cf.function.name + ": arity of return (" + arity + ") unequal to number of reference parameters (" + refs.length + ")");
			}
			for (int i = 0; i < arity; i++) {
				Reference ref = (Reference) stack[refs[arity - 1 - i]];
				ref.stack[ref.pos] = stack[--sp];
			}
		} else {
			rval = stack[sp - 1];
		}
		cf = cf.previousCallFrame;
		if (cf != null) {
			stack = cf.stack;
			sp = cf.sp;
			stack[sp++] = rval;
		}
		return rval;
	}
	public Object return1Helper() {
		Object rval = null;
		if (cf.isCoroutine) {
			rval = Rascal_TRUE;
			int[] refs = cf.function.refs;
			if (arity != refs.length) {
				throw new RuntimeException("Coroutine " + cf.function.name + ": arity of return (" + arity + ") unequal to number of reference parameters (" + refs.length + ")");
			}
			for (int i = 0; i < arity; i++) {
				Reference ref = (Reference) stack[refs[arity - 1 - i]];
				ref.stack[ref.pos] = stack[--sp];
			}
		} else {
			rval = stack[sp - 1];
		}
		cf = cf.previousCallFrame;
		if (cf != null) {
			stack = cf.stack;
			sp = cf.sp;
			stack[sp++] = rval;
		}
		return rval;
	}

	public void jvmCREATE(int fun, int arity) {
		cccf = cf.getCoroutineFrame(functionStore.get(fun), root, arity, sp);
		cccf.previousCallFrame = cf;
		cf = cccf;

		stack = cf.stack;
		sp = cf.sp;
		dynRun(fun, cf); // Run untill guard, leaves coroutine instance in stack.
	}

	public int jvmCREATE(Object[] stack, int sp, Frame cf, int fun, int arity) {
		cccf = cf.getCoroutineFrame(functionStore.get(fun), root, arity, sp);
		cccf.previousCallFrame = cf;
		cf = cccf;

		stack = cf.stack;
		sp = cf.sp;
		dynRun(fun, cf); // Run untill guard, leaves coroutine instance in stack.
		return ++sp;
	}

	public void jvmCREATEDYN(int arity) {
		FunctionInstance fun_instance;

		Object src = stack[--sp];

		if (!(src instanceof FunctionInstance)) {
			throw new RuntimeException("Unexpected argument type for CREATEDYN: " + src.getClass() + ", " + src);
		}

		// In case of partial parameter binding
		fun_instance = (FunctionInstance) src;
		cccf = cf.getCoroutineFrame(fun_instance, arity, sp);
		cccf.previousCallFrame = cf;
		cf = cccf;

		stack = cf.stack;
		sp = cf.sp;
		dynRun(fun_instance.function.funId, cf);
	}

	public int jvmCREATEDYN(Object[] stack, int sp, Frame cf, int arity) {
		FunctionInstance fun_instance;

		Object src = stack[--sp];

		if (!(src instanceof FunctionInstance)) {
			throw new RuntimeException("Unexpected argument type for CREATEDYN: " + src.getClass() + ", " + src);
		}

		// In case of partial parameter binding
		fun_instance = (FunctionInstance) src;
		cccf = cf.getCoroutineFrame(fun_instance, arity, sp);
		cccf.previousCallFrame = cf;
		cf = cccf;

		stack = cf.stack;
		sp = cf.sp;
		dynRun(fun_instance.function.funId, cf);  // Guard will increment sp of calling function to allow Rascal_T?F storage.
		return cf.sp ;
	}

	public int typeSwitchHelper() {
		IValue val = (IValue) stack[--sp];
		Type t = null;
		if (val instanceof IConstructor) {
			t = ((IConstructor) val).getConstructorType();
		} else {
			t = val.getType();
		}
		return ToplevelType.getToplevelTypeAsInt(t);
	}

	public boolean guardHelper() {
		Object rval = stack[sp - 1];
		boolean precondition;
		if (rval instanceof IBool) {
			precondition = ((IBool) rval).getValue();
		} else {
			throw new RuntimeException("Guard's expression has to be boolean!");
		}
		return precondition;
	}

	public void yield1Helper(int arity2, int ep) {
		// Stores a Rascal_TRUE value into the stack of the NEXT? caller.
		// The inline yield1 does the return
		Coroutine coroutine = activeCoroutines.pop();
		ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;

		coroutine.start.previousCallFrame.stack[coroutine.start.previousCallFrame.sp++] = Rascal_TRUE;

		int[] refs = cf.function.refs;

		for (int i = 0; i < arity2; i++) {
			Reference ref = (Reference) stack[refs[arity2 - 1 - i]];
			ref.stack[ref.pos] = stack[--sp];
		}

		cf.hotEntryPoint = ep;
		cf.sp = sp;

		coroutine.frame = cf;
		coroutine.suspended = true;

		cf = cf.previousCallFrame;
		sp = cf.sp;
		stack = cf.stack;
	}

	public void yield0Helper(int ep) {
		// Stores a Rascal_TRUE value into the stack of the NEXT? caller.
		// The inline yield0 does the return
		Coroutine coroutine = activeCoroutines.pop();
		ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;

		coroutine.start.previousCallFrame.stack[coroutine.start.previousCallFrame.sp++] = Rascal_TRUE;

		cf.hotEntryPoint = ep;
		cf.sp = sp;

		coroutine.frame = cf;
		coroutine.suspended = true;

		cf = cf.previousCallFrame;
		sp = cf.sp;
		stack = cf.stack;
	}

	public Object callHelper(int funid, int arity, int ep) {
		Frame tmp;
		Function fun;
		Object rval;

		if (cf.hotEntryPoint != ep) {
			fun = functionStore.get(funid);
			// In case of partial parameter binding
			if (arity < fun.nformals) {
				FunctionInstance fun_instance = FunctionInstance.applyPartial(fun, root, this, arity, stack, sp);
				sp = sp - arity;
				stack[sp++] = fun_instance;
				return NONE;
			}
			tmp = cf.getFrame(fun, root, arity, sp);
			cf.nextFrame = tmp;
		} else {
			tmp = cf.nextFrame;
			fun = tmp.function;
		}
		tmp.previousCallFrame = cf;

		this.cf = tmp;
		this.stack = cf.stack;
		this.sp = cf.sp;

		rval = dynRun(fun.funId, cf); // In a full inline version we can call the
										// function directly (name is known).

		if (rval.equals(YIELD)) {
			// drop my stack
			cf.hotEntryPoint = ep;
			cf.sp = sp;

			cf = cf.previousCallFrame;
			sp = cf.sp;
			stack = cf.stack;
			return YIELD; // Will cause the inline call to return YIELD
		} else {
			cf.hotEntryPoint = 0;
			cf.nextFrame = null; // Allow GC to clean
			return NONE; // Inline call will continue execution
		}
	}

	public int jvmNEXT0(Object[] stock, int spp, Frame cff) {
		Coroutine coroutine = (Coroutine) stack[--sp];

		// Merged the hasNext and next semantics
		if (!coroutine.hasNext()) {
			stack[sp++] = Rascal_FALSE;
			return sp;
		}
		// put the coroutine onto the stack of active coroutines
		activeCoroutines.push(coroutine);
		ccf = coroutine.start;
		coroutine.next(cf);

		// Push something on the stack of the prev yielding function
		coroutine.frame.stack[coroutine.frame.sp++] = null;

		cf.sp = sp;

		coroutine.frame.previousCallFrame = cf;

		cf = coroutine.entryFrame;

		stack = cf.stack;
		sp = cf.sp;
		dynRun(coroutine.entryFrame.function.funId, cf);
		return sp;
	}

	public Object exhaustHelper() {
		if (cf == ccf) {
			activeCoroutines.pop();
			ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
		}

		cf = cf.previousCallFrame;
		if (cf == null) {
			return Rascal_FALSE;
		}
		stack = cf.stack;
		sp = cf.sp;
		stack[sp++] = Rascal_FALSE; // 'Exhaust' has to always return FALSE,

		return NONE;// i.e., signal a failure;
	}

	// jvmOCALL has an issue
	// There are 3 possible ways to reset the stack pointer sp
	// 1: Done by nextFrame
	// 2: Not done by nextFrame (there is no frame)
	// 3: todo after the constructor call.
	// Problem there was 1 frame and the function failed.
	public void jvmOCALL(int ofun, int arity) {
		boolean stackPointerAdjusted = false;
		cf.sp = sp;

		OverloadedFunctionInstanceCall ofun_call = null;
		OverloadedFunction of = overloadedStore.get(ofun);

		ofun_call = of.scopeIn == -1 ? new OverloadedFunctionInstanceCall(cf, of.functions, of.constructors, root, null, arity) : OverloadedFunctionInstanceCall
				.computeOverloadedFunctionInstanceCall(cf, of.functions, of.constructors, of.scopeIn, null, arity);

		Frame frame = ofun_call.nextFrame(functionStore);

		while (frame != null) {
			stackPointerAdjusted = true; // See text
			cf = frame;
			stack = cf.stack;
			sp = cf.sp;
			Object rsult = dynRun(cf.function.funId, cf);
			if (rsult.equals(NONE)) {
				return; // Alternative matched.
			}
			frame = ofun_call.nextFrame(functionStore);
		}
		Type constructor = ofun_call.nextConstructor(constructorStore);
		if (stackPointerAdjusted == false)
			sp = sp - arity;
		stack[sp++] = vf.constructor(constructor, ofun_call.getConstructorArguments(constructor.getArity()));
	}

	public void jvmOCALLDYN(int typesel, int arity) {
		Object funcObject = stack[--sp];
		OverloadedFunctionInstanceCall ofunCall = null;
		cf.sp = sp;

		// Get function types to perform a type-based dynamic
		// resolution
		Type types = cf.function.codeblock.getConstantType(typesel);
		// Objects of two types may appear on the stack:
		// 1. FunctionInstance due to closures whom will have no overloading
		if (funcObject instanceof FunctionInstance) {
			FunctionInstance fun_instance = (FunctionInstance) funcObject;
			cf = cf.getFrame(fun_instance.function, fun_instance.env, arity, sp);
			stack = cf.stack;
			sp = cf.sp;
			dynRun(cf.function.funId, cf);
			return;
		}
		// 2. OverloadedFunctionInstance due to named Rascal
		// functions
		OverloadedFunctionInstance of_instance = (OverloadedFunctionInstance) funcObject;
		ofunCall = new OverloadedFunctionInstanceCall(cf, of_instance.functions, of_instance.constructors, of_instance.env, types, arity);

		boolean stackPointerAdjusted = false;
		Frame frame = ofunCall.nextFrame(functionStore);
		while (frame != null) {
			stackPointerAdjusted = true; // See text at OCALL
			cf = frame;
			stack = cf.stack;
			sp = cf.sp;
			Object rsult = dynRun(cf.function.funId, cf);
			if (rsult.equals(NONE))
				return; // Alternative matched.
			frame = ofunCall.nextFrame(functionStore);
		}
		Type constructor = ofunCall.nextConstructor(constructorStore);
		if (stackPointerAdjusted == false)
			sp = sp - arity;
		stack[sp++] = vf.constructor(constructor, ofunCall.getConstructorArguments(constructor.getArity()));
	}

	public Object return0Helper(Object[] st0ck, int spp, Frame cpf) {

		Object rval = null;

		boolean returns = cf.isCoroutine;
		if (returns) {
			rval = Rascal_TRUE;
		}

		if (cf == ccf) {
			activeCoroutines.pop();
			ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
		}

		cf = cf.previousCallFrame;

		stack = cf.stack;
		sp = cf.sp;

		if (returns) {
			stack[sp++] = rval;
		}
		return rval;
	}

	public Object calldynHelper(int arity, int ep) {
		// In case of CALLDYN, the stack top value of type 'Type'
		// leads to a constructor call
		// This instruction is a monstrosity it should be split in three.

		Frame tmp;
		Object rval;

		if (cf.hotEntryPoint != ep) {
			if (stack[sp - 1] instanceof Type) {
				Type constr = (Type) stack[--sp];
				arity = constr.getArity();
				IValue[] args = new IValue[arity];
				for (int i = arity - 1; i >= 0; i--) {
					args[i] = (IValue) stack[sp - arity + i];
				}
				sp = sp - arity;
				stack[sp++] = vf.constructor(constr, args);
				return NONE; // DO not return continue execution
			}

			if (stack[sp - 1] instanceof FunctionInstance) {
				FunctionInstance fun_instance = (FunctionInstance) stack[--sp];
				// In case of partial parameter binding
				if (fun_instance.next + arity < fun_instance.function.nformals) {
					fun_instance = fun_instance.applyPartial(arity, stack, sp);
					sp = sp - arity;
					stack[sp++] = fun_instance;
					return NONE;
				}
				tmp = cf.getFrame(fun_instance.function, fun_instance.env, fun_instance.args, arity, sp);
				cf.nextFrame = tmp;
			} else {
				throw new RuntimeException("Unexpected argument type for CALLDYN: " + asString(stack[sp - 1]));
			}
		} else {
			tmp = cf.nextFrame;
		}

		tmp.previousCallFrame = cf;

		this.cf = tmp;
		this.stack = cf.stack;
		this.sp = cf.sp;

		rval = dynRun(cf.function.funId, cf); // In a inline version we can call the
												// function directly.

		if (rval.equals(YIELD)) {
			// Save reentry point
			cf.hotEntryPoint = ep;
			cf.sp = sp;

			// drop my stack, and return
			cf = cf.previousCallFrame;
			sp = cf.sp;
			stack = cf.stack;
			return YIELD; // Will cause the inline call to return YIELD
		} else {
			cf.hotEntryPoint = 0;
			cf.nextFrame = null; // Allow GC to clean
			return NONE; // Inline call will continue execution
		}
	}

	public void failReturnHelper() {
		// repair stack after failreturn;
		// Small helper can be inlined ?
		// The inline part returns the failure.
		cf = cf.previousCallFrame;
		stack = cf.stack;
		sp = cf.sp;
	}

	// Next methods are for debug use only. Single step..
	// A field for tracing only used by dummy dinsnXXX()
	public int jmpTarget = 0;

	public void dinsnTYPESWITCH(int target) {
		jmpTarget = target;
	}

	public void dinsnJMPTRUE(int target) {
		jmpTarget = target;
	}

	public void dinsnYIELD1(int target) {
		jmpTarget = target;
	}

	public void dinsnYIELD0(int target) {
		jmpTarget = target;
	}

	public void dinsnJMPFALSE(int target) {
		jmpTarget = target;
	}

	public void dinsnOCALALT(int target) {
		jmpTarget = target;
	}

	public void dinsnJMP(int target) {
		jmpTarget = target;
	}

	public void dinsnPOP() {
//		jmpTarget = sp;
	}

	public void dinsnGUARD() {
//		jmpTarget = sp;
	}

	public void dinsnEXHAUST() {
//		jmpTarget = sp;
	}

	public void dinsnOCALL(int target) {
		jmpTarget = target;
	}

	public void dinsnCALL(int target) {
		jmpTarget = target;
	}

	public void dinsnCALLDYN(int target) {
		jmpTarget = target;
	}

	public void dinsnLOADCON(int target) {
		jmpTarget = target;
	}

	public void dinsnLOADLOC3() {
		jmpTarget = 3;
	}

	public void dinsnFAILRETURN() {
		jmpTarget = 3;
	}

	// Next metods are forced by the interface implementation
	// temporarily needed to facilitate 3 RVM implementations.
	@Override
	public IValue executeProgram(String uid_main, IValue[] args) {
		throw new UnsupportedOperationException();
	}

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

}
