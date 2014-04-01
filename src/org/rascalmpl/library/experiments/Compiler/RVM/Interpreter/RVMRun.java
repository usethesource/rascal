package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

public class RVMRun {

	public final IValueFactory vf;
	private final TypeFactory tf;
	protected final Boolean TRUE;
	protected final Boolean FALSE;
	protected final IBool Rascal_TRUE;
	protected final IBool Rascal_FALSE;
	protected final IString NONE;
	protected final IString YIELD0;
	protected final IString YIELD1;
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

	// An exhausted coroutine instance
	public static Coroutine exhausted = new Coroutine(null) {

		@Override
		public void next(Frame previousCallFrame) {
			throw new RuntimeException(
					"Internal error: an attempt to activate an exhausted coroutine instance.");
		}

		@Override
		public void suspend(Frame current) {
			throw new RuntimeException(
					"Internal error: an attempt to suspend an exhausted coroutine instance.");
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
			throw new RuntimeException(
					"Internal error: an attempt to copy an exhausted coroutine instance.");
		}
	};

	public RVMRun(IValueFactory vf, IEvaluatorContext ctx, boolean debug,
			boolean profile) {
		super();

		this.vf = vf;
		tf = TypeFactory.getInstance();

		this.ctx = ctx;
		this.stdout = ctx.getStdOut();
		this.stderr = ctx.getStdErr();
		this.debug = debug;

		this.types = new Types(this.vf);

		TRUE = true;
		FALSE = false;
		Rascal_TRUE = vf.bool(true);
		Rascal_FALSE = vf.bool(false);

		// Return types used in code generator
		NONE = vf.string("$nothing$");
		YIELD0 = vf.string("$yield0$");
		YIELD1 = vf.string("$yield1$");
		FAILRETURN = vf.string("$failreturn$");
		PANIC = vf.string("$panic$");

		functionStore = new ArrayList<Function>();
		constructorStore = new ArrayList<Type>();

		functionMap = new HashMap<String, Integer>();
		constructorMap = new HashMap<String, Integer>();

		resolver = new HashMap<String, Integer>();
		overloadedStore = new ArrayList<OverloadedFunction>();

		moduleVariables = new HashMap<IValue, IValue>();

		MuPrimitive.init(vf, stdout, profile);
		RascalPrimitive.init(vf, this, profile);
		Opcode.init(stdout, false);
	}

	public RVMRun(IValueFactory vf) {
		this(vf, null, false, false);
	}

	public Type symbolToType(IConstructor symbol) {
		return types.symbolToType(symbol, typeStore);
	}

	/**
	 * Narrow an Object as occurring on the RVM runtime stack to an IValue that
	 * can be returned. Note that various non-IValues can occur: - Coroutine -
	 * Reference - FunctionInstance - Object[] (is converted to an IList)
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
		throw new RuntimeException(
				"PANIC: Cannot convert object back to IValue: " + result);
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
			return "Coroutine[" + ((Coroutine) o).frame.function.getName()
					+ "]";
		}
		if (o instanceof Function) {
			return "Function[" + ((Function) o).getName() + "]";
		}
		if (o instanceof FunctionInstance) {
			return "Function[" + ((FunctionInstance) o).function.getName()
					+ "]";
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
		throw new RuntimeException(
				"PANIC: undefined overloaded function index " + n);
	}

	public IValue executeFunction(String uid_func, IValue[] args) {
		// Assumption here is that the function called is not nested one
		// and does not use global variables
		Object[] oldStack = stack;
		Frame oldCF = cf;
		int[] oldInstructions = instructions;
		int oldSP = sp;
		int oldPC = pc;
		int oldPos = pos;
		int oldPostOp = postOp;
		ArrayList<Frame> oldstacktrace = stacktrace;
		Thrown oldthrown = thrown;
		int oldarity = arity;
		String oldlast_function_name = last_function_name;

		Function func = functionStore.get(functionMap.get(uid_func));
		Frame root = new Frame(func.scopeId, null, func.maxstack, func);
		cf = root;

		// Pass the program arguments to main
		for (int i = 0; i < args.length; i++) {
			cf.stack[i] = args[i];
		}
		Object o = executeProgram(root, cf);

		stack = oldStack;
		cf = oldCF;
		instructions = oldInstructions;
		sp = oldSP;
		pc = oldPC;
		pos = oldPos;
		postOp = oldPostOp;
		stacktrace = oldstacktrace;
		thrown = oldthrown;
		arity = oldarity;
		last_function_name = oldlast_function_name;

		if (o instanceof Thrown) {
			throw (Thrown) o;
		}
		return narrow(o);
	}

	public IValue executeFunction(FunctionInstance func, IValue[] args) {
		Object[] oldStack = stack;
		Frame oldCF = cf;
		int[] oldInstructions = instructions;
		int oldSP = sp;
		int oldPC = pc;
		int oldPos = pos;
		int oldPostOp = postOp;
		ArrayList<Frame> oldstacktrace = stacktrace;
		Thrown oldthrown = thrown;
		int oldarity = arity;
		String oldlast_function_name = last_function_name;

		Frame root = new Frame(func.function.scopeId, null, func.env,
				func.function.maxstack, func.function);
		cf = root;

		// Pass the program arguments to main
		for (int i = 0; i < args.length; i++) {
			cf.stack[i] = args[i];
		}
		Object o = executeProgram(root, cf);

		stack = oldStack;
		cf = oldCF;
		instructions = oldInstructions;
		sp = oldSP;
		pc = oldPC;
		pos = oldPos;
		postOp = oldPostOp;
		stacktrace = oldstacktrace;
		thrown = oldthrown;
		arity = oldarity;
		last_function_name = oldlast_function_name;

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
	public Object[] stack; // current stack
	public Frame cf; // current frame
	public Frame root; // Root frame of a program
	public int sp; // current stack pointer
	int[] instructions; // current instruction sequence
	int instruction; // TODO current active instruction (remove)
	int op; // TODO current opcode (remove)
	int pc; // current program counter
	int postOp;
	int pos;
	ArrayList<Frame> stacktrace;
	Thrown thrown;
	int arity;
	String last_function_name;
	//
	// Overloading specific
	Stack<OverloadedFunctionInstanceCall> ocalls = new Stack<OverloadedFunctionInstanceCall>();
	OverloadedFunctionInstanceCall c_ofun_call = null;

	Object globalReturnValue = null;

	// Two fields for tracing only used by dummy dinsnXXX()
	public int jmpTarget = 0;
	public int prevSP = 0;

	public Object executeProgram(Frame root, Frame cfinit) {
		this.cf = cfinit;
		stack = cf.stack; // current stack
		sp = cf.function.nlocals; // current stack pointer
		instructions = cf.function.codeblock.getInstructions(); // current
																// instruction
																// sequence
		pc = 0; // current program counter
		postOp = 0;
		pos = 0;
		last_function_name = "";

		try {
			NEXT_INSTRUCTION: while (true) {
				instruction = instructions[pc++];
				op = CodeBlock.fetchOp(instruction);

				if (debug) {
					int startpc = pc - 1;
					if (!last_function_name.equals(cf.function.name))
						stdout.printf("[%03d] %s\n", startpc, cf.function.name);

					for (int i = 0; i < sp; i++) {
						stdout.println("\t   "
								+ (i < cf.function.nlocals ? "*" : " ") + i
								+ ": " + asString(stack[i]));
					}
					stdout.printf("%5s %s\n", "",
							cf.function.codeblock.toString(startpc));
				}

				Opcode.use(instruction);

				INSTRUCTION: switch (op) {

				case Opcode.OP_POP:
					insnPOP();
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADLOC0:
					insnLOADLOC0();
					if (postOp == Opcode.POSTOP_CHECKUNDEF) {
						pos = 0;
						break;
					}
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADLOC1:
					insnLOADLOC1();
					if (postOp == Opcode.POSTOP_CHECKUNDEF) {
						pos = 1;
						break;
					}
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADLOC2:
					insnLOADLOC2();
					if (postOp == Opcode.POSTOP_CHECKUNDEF) {
						pos = 2;
						break;
					}
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADLOC3:
					insnLOADLOC3();
					if (postOp == Opcode.POSTOP_CHECKUNDEF) {
						pos = 3;
						break;
					}
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADLOC4:
					insnLOADLOC4();
					if (postOp == Opcode.POSTOP_CHECKUNDEF) {
						pos = 4;
						break;
					}
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADLOC5:
					insnLOADLOC5();
					if (postOp == Opcode.POSTOP_CHECKUNDEF) {
						pos = 5;
						break;
					}
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADLOC6:
					insnLOADLOC6();
					if (postOp == Opcode.POSTOP_CHECKUNDEF) {
						pos = 6;
						break;
					}
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADLOC7:
					insnLOADLOC7();
					if (postOp == Opcode.POSTOP_CHECKUNDEF) {
						pos = 7;
						break;
					}
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADLOC8:
					insnLOADLOC8();
					if (postOp == Opcode.POSTOP_CHECKUNDEF) {
						pos = 8;
						break;
					}
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADLOC9:
					insnLOADLOC9();
					if (postOp == Opcode.POSTOP_CHECKUNDEF) {
						pos = 9;
						break;
					}
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADLOC:
					insnLOADLOC(CodeBlock.fetchArg1(instruction));
					if (postOp == Opcode.POSTOP_CHECKUNDEF) {
						pos = CodeBlock.fetchArg1(instruction);
						break;
					}
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADBOOL:
					insnLOADBOOL(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADINT:
					insnLOADINT(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADCON:
					insnLOADCON(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADLOCREF:
					insnLOADLOCREF(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_CALLMUPRIM:
					insnCALLMUPRIM(CodeBlock.fetchArg1(instruction),
							CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_JMP:
					insnJMP(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_JMPTRUE:
					insnJMPTRUE(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_JMPFALSE:
					insnJMPFALSE(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_TYPESWITCH:
					insnTYPESWITCH(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_JMPINDEXED:
					insnJMPINDEXED(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADTYPE:
					insnLOADTYPE(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADLOCDEREF:
					insnLOADLOCDEREF(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_STORELOC:
					insnSTORELOC(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_UNWRAPTHROWNLOC:
					insnUNWRAPTHROWNLOC(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_STORELOCDEREF:
					insnSTORELOCDEREF(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADFUN:
					insnLOADFUN(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOAD_NESTED_FUN:
					insnLOAD_NESTED_FUN(CodeBlock.fetchArg1(instruction),
							CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADOFUN:
					insnLOADOFUN(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADCONSTR:
					insnLOADCONSTR(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADVAR:
					insnLOADVAR(CodeBlock.fetchArg1(instruction),
							CodeBlock.fetchArg2(instruction),
							CodeBlock.isMaxArg2(CodeBlock
									.fetchArg2(instruction)));
					if (postOp == Opcode.POSTOP_CHECKUNDEF)
						break INSTRUCTION;
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADVARREF:
					insnLOADVARREF(CodeBlock.fetchArg1(instruction),
							CodeBlock.fetchArg2(instruction),
							CodeBlock.isMaxArg2(CodeBlock
									.fetchArg2(instruction)));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADVARDEREF:
					insnLOADVARDEREF(CodeBlock.fetchArg1(instruction),
							CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_STOREVAR:
					insnSTOREVAR(CodeBlock.fetchArg1(instruction),
							CodeBlock.fetchArg2(instruction),
							CodeBlock.isMaxArg2(CodeBlock
									.fetchArg2(instruction)));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_UNWRAPTHROWNVAR:
					insnUNWRAPTHROWNVAR(CodeBlock.fetchArg1(instruction),
							CodeBlock.fetchArg2(instruction),
							CodeBlock.isMaxArg2(CodeBlock
									.fetchArg2(instruction)));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_STOREVARDEREF:
					insnSTOREVARDEREF(CodeBlock.fetchArg1(instruction),
							CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_CALLCONSTR:
					insnCALLCONSTR(CodeBlock.fetchArg1(instruction),
							CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_CALLDYN:
					insnCALLDYN(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_CALL:
					insnCALL(CodeBlock.fetchArg1(instruction),
							CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_OCALL:
					insnOCALL(CodeBlock.fetchArg1(instruction),
							CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_OCALLDYN:
					insnOCALLDYN(CodeBlock.fetchArg1(instruction),
							CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_FAILRETURN:
					insnFAILRETURN();
					continue NEXT_INSTRUCTION;

				case Opcode.OP_RETURN0:
					insnRETURN0();
					if (globalReturnValue != null)
						return globalReturnValue; // Callers stack does not
													// exist in RVM.
					continue NEXT_INSTRUCTION;

				case Opcode.OP_RETURN1:
					insnRETURN1(CodeBlock.fetchArg1(instruction));
					if (globalReturnValue != null)
						return globalReturnValue; // Callers stack does not
													// exist in RVM.
					continue NEXT_INSTRUCTION;

				case Opcode.OP_FILTERRETURN:
					insnFILTERRETURN(CodeBlock.fetchArg1(instruction));
					if (globalReturnValue != null)
						return globalReturnValue; // Callers stack does not
													// exist in RVM.
					continue NEXT_INSTRUCTION;

				case Opcode.OP_CALLJAVA:
					insnCALLJAVA();
					if (postOp == Opcode.POSTOP_HANDLEEXCEPTION)
						break INSTRUCTION;
					continue NEXT_INSTRUCTION;

				case Opcode.OP_CREATE:
					insnCREATE(CodeBlock.fetchArg1(instruction),
							CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_CREATEDYN:
					insnCREATEDYN(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_GUARD:
					insnGUARD();
					continue NEXT_INSTRUCTION;

				case Opcode.OP_APPLY:
					insnAPPLY(CodeBlock.fetchArg1(instruction),
							CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_APPLYDYN:
					insnAPPLYDYN(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_NEXT0:
					insnNEXT0();
					continue NEXT_INSTRUCTION;

				case Opcode.OP_NEXT1:
					insnNEXT1(true);
					continue NEXT_INSTRUCTION;

				case Opcode.OP_YIELD0:
					insnYIELD0();
					continue NEXT_INSTRUCTION;

				case Opcode.OP_YIELD1:
					insnYIELD1(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_EXHAUST:
					insnEXHAUST();
					if (globalReturnValue != null)
						return globalReturnValue;
					continue NEXT_INSTRUCTION;

				case Opcode.OP_CALLPRIM:
					insnCALLPRIM(CodeBlock.fetchArg1(instruction),
							CodeBlock.fetchArg2(instruction));
					if (postOp == Opcode.POSTOP_HANDLEEXCEPTION)
						break INSTRUCTION;
					continue NEXT_INSTRUCTION;

				case Opcode.OP_SUBSCRIPTARRAY:
					insnSUBSCRIPTARRAY();
					continue NEXT_INSTRUCTION;

				case Opcode.OP_SUBSCRIPTLIST:
					insnSUBSCRIPTLIST();
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LESSINT:
					insnLESSINT();
					continue NEXT_INSTRUCTION;

				case Opcode.OP_GREATEREQUALINT:
					insnGREATEREQUALINT();
					continue NEXT_INSTRUCTION;

				case Opcode.OP_ADDINT:
					insnADDINT();
					continue NEXT_INSTRUCTION;

				case Opcode.OP_SUBTRACTINT:
					insnSUBTRACTINT();
					continue NEXT_INSTRUCTION;

				case Opcode.OP_ANDBOOL:
					insnANDBOOL();
					continue NEXT_INSTRUCTION;

				case Opcode.OP_TYPEOF:
					insnTYPEOF();
					continue NEXT_INSTRUCTION;

				case Opcode.OP_SUBTYPE:
					insnSUBTYPE();
					continue NEXT_INSTRUCTION;

				case Opcode.OP_CHECKARGTYPE:
					insnCHECKARGTYPE();
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LABEL:
					throw new RuntimeException("label instruction at runtime");

				case Opcode.OP_HALT:
					if (debug) {
						stdout.println("Program halted:");
						for (int i = 0; i < sp; i++) {
							stdout.println(i + ": " + stack[i]);
						}
					}
					return stack[sp - 1];

				case Opcode.OP_PRINTLN:
					insnPRINTLN(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_THROW:
					Object obj = stack[--sp];
					thrown = null;
					if (obj instanceof IValue) {
						stacktrace = new ArrayList<Frame>();
						stacktrace.add(cf);
						thrown = Thrown.getInstance((IValue) obj, null,
								stacktrace);
					} else {
						// Then, an object of type 'Thrown' is on top of the
						// stack
						thrown = (Thrown) obj;
					}
					postOp = Opcode.POSTOP_HANDLEEXCEPTION;
					break INSTRUCTION;

				case Opcode.OP_LOADLOCKWP:
					insnLOADLOCKWP(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADVARKWP:
					continue NEXT_INSTRUCTION;

				case Opcode.OP_STORELOCKWP:
					insnSTORELOCKWP(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_STOREVARKWP:
					continue NEXT_INSTRUCTION;

				case Opcode.OP_LOADCONT:
					insnLOADCONT(CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;

				case Opcode.OP_RESET:
					insnRESET();
					continue NEXT_INSTRUCTION;

				case Opcode.OP_SHIFT:
					insnSHIFT();
					continue NEXT_INSTRUCTION;

				default:
					throw new RuntimeException(
							"RVM main loop -- cannot decode instruction");
				}

				switch (postOp) {

				case Opcode.POSTOP_CHECKUNDEF:
				case Opcode.POSTOP_HANDLEEXCEPTION:
					// EXCEPTION HANDLING
					if (postOp == Opcode.POSTOP_CHECKUNDEF) {
						stacktrace = new ArrayList<Frame>();
						stacktrace.add(cf);
						thrown = RuntimeExceptions.uninitializedVariable(pos,
								null, stacktrace);
					}
					cf.pc = pc;
					// First, try to find a handler in the current frame
					// function,
					// given the current instruction index and the value type,
					// then, if not found, look up the caller function(s)
					for (Frame f = cf; f != null; f = f.previousCallFrame) {
						int handler = f.function.getHandler(f.pc - 1,
								thrown.value.getType());
						if (handler != -1) {
							if (f != cf) {
								cf = f;
								instructions = cf.function.codeblock
										.getInstructions();
								stack = cf.stack;
								sp = cf.sp;
								pc = cf.pc;
							}
							pc = handler;
							stack[sp++] = thrown;
							thrown = null;
							continue NEXT_INSTRUCTION;
						}
						if (c_ofun_call != null
								&& f.previousCallFrame == c_ofun_call.cf) {
							ocalls.pop();
							c_ofun_call = ocalls.isEmpty() ? null : ocalls
									.peek();
						}
					}
					// If a handler has not been found in the caller
					// functions...
					return thrown;
				}

			}
		} catch (Exception e) {
			e.printStackTrace(stderr);
			throw new RuntimeException(
					"PANIC: (instruction execution): instruction: "
							+ cf.function.codeblock.toString(pc - 1)
							+ "; message: " + e.getMessage(), e.getCause());
			// stdout.println("PANIC: (instruction execution): " +
			// e.getMessage());
			// e.printStackTrace();
			// stderr.println(e.getStackTrace());
		}
	}

	private void insnCREATEDYN(int arity) {
		Object src = stack[--sp];
		if (src instanceof FunctionInstance) {
			// In case of partial parameter binding
			FunctionInstance fun_instance = (FunctionInstance) src;
			cccf = cf.getCoroutineFrame(fun_instance, arity, sp);
		} else {
			throw new RuntimeException("Unexpected argument type for INIT: "
					+ src.getClass() + ", " + src);
		}
		sp = cf.sp;
		// Instead of suspending a coroutine instance during INIT, execute it
		// until GUARD;
		// Let INIT postpone creation of an actual coroutine instance (delegated
		// to GUARD), which also implies no stack management of active
		// coroutines until GUARD;
		cccf.previousCallFrame = cf;

		cf.sp = sp;
		cf.pc = pc;
		instructions = cccf.function.codeblock.getInstructions();
		cf = cccf;
		stack = cf.stack;
		sp = cf.sp;
		pc = cf.pc;
	}

	int callJavaMethod(String methodName, String className,
			Type parameterTypes, int reflect, Object[] stack, int sp)
			throws Throw {
		Class<?> clazz = null;
		try {
			try {
				clazz = this.getClass().getClassLoader().loadClass(className);
			} catch (ClassNotFoundException e1) {
				// If the class is not found, try other class loaders
				for (ClassLoader loader : ctx.getEvaluator().getClassLoaders()) {
					try {
						clazz = loader.loadClass(className);
						break;
					} catch (ClassNotFoundException e2) {
						;
					}
				}
			}

			if (clazz == null) {
				throw new RuntimeException("Class not found: " + className);
			}

			Constructor<?> cons;
			cons = clazz.getConstructor(IValueFactory.class);
			Object instance = cons.newInstance(vf);
			Method m = clazz.getMethod(methodName,
					makeJavaTypes(parameterTypes, reflect));
			int nformals = parameterTypes.getArity();
			Object[] parameters = new Object[nformals + reflect];
			for (int i = 0; i < nformals; i++) {
				parameters[i] = stack[sp - nformals + i];
			}
			if (reflect == 1) {
				parameters[nformals] = this.ctx;
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
				jtypes[arity - 1] = this
						.getClass()
						.getClassLoader()
						.loadClass(
								"org.rascalmpl.interpreter.IEvaluatorContext");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return jtypes;
	}

	private static class JavaClasses implements
			ITypeVisitor<Class<?>, RuntimeException> {

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
		public Class<?> visitAbstractData(
				org.eclipse.imp.pdb.facts.type.Type type) {
			return IConstructor.class;
		}

		@Override
		public Class<?> visitSet(org.eclipse.imp.pdb.facts.type.Type type) {
			return ISet.class;
		}

		@Override
		public Class<?> visitSourceLocation(
				org.eclipse.imp.pdb.facts.type.Type type) {
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
		public Class<?> visitConstructor(
				org.eclipse.imp.pdb.facts.type.Type type) {
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
		public Class<?> visitParameter(
				org.eclipse.imp.pdb.facts.type.Type parameterType) {
			return parameterType.getBound().accept(this);
		}

		@Override
		public Class<?> visitExternal(
				org.eclipse.imp.pdb.facts.type.Type externalType) {
			return IValue.class;
		}

		@Override
		public Class<?> visitDateTime(Type type) {
			return IDateTime.class;
		}
	}

	public void inject(ArrayList<Function> functionStore2,
			ArrayList<OverloadedFunction> overloadedStore2,
			ArrayList<Type> constructorStore2, TypeStore typeStore2,
			Map<String, Integer> functionMap2) {
		// TODO check if we can generate code for them.
		this.functionStore = functionStore2;
		this.overloadedStore = overloadedStore2;
		this.constructorStore = constructorStore2;
		this.typeStore = typeStore2;
		this.functionMap = functionMap2;
	}

	public void insnPOP() {
		sp--;
	}

	public void insnLOADLOC0() {
		postOp = 0;
		if (stack[0] != null) {
			stack[sp++] = stack[0];
		} else {
			postOp = Opcode.POSTOP_CHECKUNDEF;
		}
	}

	public void insnLOADLOC1() {
		postOp = 0;
		if (stack[1] != null) {
			stack[sp++] = stack[1];
		} else {
			postOp = Opcode.POSTOP_CHECKUNDEF;
		}
	}

	public void insnLOADLOC2() {
		postOp = 0;
		if (stack[2] != null) {
			stack[sp++] = stack[2];
		} else {
			postOp = Opcode.POSTOP_CHECKUNDEF;
		}
	}

	public void insnLOADLOC3() {
		postOp = 0;
		if (stack[3] != null) {
			stack[sp++] = stack[3];
		} else {
			postOp = Opcode.POSTOP_CHECKUNDEF;
		}
	}

	public void insnLOADLOC4() {
		postOp = 0;
		if (stack[4] != null) {
			stack[sp++] = stack[4];
		} else {
			postOp = Opcode.POSTOP_CHECKUNDEF;
		}
	}

	public void insnLOADLOC5() {
		postOp = 0;
		if (stack[5] != null) {
			stack[sp++] = stack[5];
		} else {
			postOp = Opcode.POSTOP_CHECKUNDEF;
		}
	}

	public void insnLOADLOC6() {
		postOp = 0;
		if (stack[6] != null) {
			stack[sp++] = stack[6];
		} else {
			postOp = Opcode.POSTOP_CHECKUNDEF;
		}
	}

	public void insnLOADLOC7() {
		postOp = 0;
		if (stack[7] != null) {
			stack[sp++] = stack[7];
		} else {
			postOp = Opcode.POSTOP_CHECKUNDEF;
		}
	}

	public void insnLOADLOC8() {
		postOp = 0;
		if (stack[8] != null) {
			stack[sp++] = stack[8];
		} else {
			postOp = Opcode.POSTOP_CHECKUNDEF;
		}
	}

	public void insnLOADLOC9() {
		postOp = 0;
		if (stack[9] != null) {
			stack[sp++] = stack[9];
		} else {
			postOp = Opcode.POSTOP_CHECKUNDEF;
		}
	}

	public void insnLOADLOC(int i) {
		postOp = 0;
		if (stack[i] != null) {
			stack[sp++] = stack[i];
		} else {
			postOp = Opcode.POSTOP_CHECKUNDEF;
		}
	}

	public void insnLOADBOOL(int i) {
		// TODO solve loading of the right value in compile time
		stack[sp++] = i == 1 ? true : false;
	}

	public void insnLOADINT(int i) {
		// TODO solve loading of the right value in compile time
		stack[sp++] = i;
	}

	public void insnLOADCON(int arg1) {
		stack[sp++] = cf.function.constantStore[arg1];
	}

	public void insnLOADLOCREF(int i) {
		stack[sp++] = new Reference(stack, i);
	}

	public void insnCALLMUPRIM(int arg1, int arg2) {
		sp = MuPrimitive.values[arg1].execute(stack, sp, arg2);
	}

	public void insnJMP(int target) {
		// TODO will not return in the JVM version.
		pc = target;
	}

	public void insnJMPTRUE(int target) {
		// TODO will not return in the JVM version.
		sp--;
		if (stack[sp].equals(TRUE) || stack[sp].equals(Rascal_TRUE)) {
			pc = target;
		}
	}

	public void insnJMPFALSE(int target) {
		sp--;
		// TODO will not return in the JVM version.
		if (stack[sp].equals(FALSE) || stack[sp].equals(Rascal_FALSE)) {
			pc = target;
		}
	}

	public void insnTYPESWITCH(int i) {
		// TODO Will not return in this form
		// implemnt tha JVM switchtable
		IValue val = (IValue) stack[--sp];
		Type t = null;
		if (val instanceof IConstructor) {
			t = ((IConstructor) val).getConstructorType();
		} else {
			t = val.getType();
		}
		int labelIndex = ToplevelType.getToplevelTypeAsInt(t);
		IList labels = (IList) cf.function.constantStore[i];
		pc = ((IInteger) labels.get(labelIndex)).intValue();
	}

	public void insnJMPINDEXED(int i) {
		int labelIndex = ((IInteger) stack[--sp]).intValue();
		IList labels = (IList) cf.function.constantStore[i];
		pc = ((IInteger) labels.get(labelIndex)).intValue();
		return;
	}

	public void insnLOADTYPE(int i) {
		stack[sp++] = cf.function.typeConstantStore[i];
		return;
	}

	public void insnLOADLOCDEREF(int loc) {
		Reference ref = (Reference) stack[loc];
		stack[sp++] = ref.stack[ref.pos];
		return;
	}

	public void insnSTORELOC(int target) {
		stack[target] = stack[sp - 1];
	}

	public void insnUNWRAPTHROWNLOC(int target) {
		stack[target] = ((Thrown) stack[--sp]).value;
	}

	public void insnSTORELOCDEREF(int loc) {
		Reference ref = (Reference) stack[loc];
		ref.stack[ref.pos] = stack[sp - 1]; // TODO: We need to re-consider how
											// to guarantee safe use of both
											// Java objects and IValues
	}

	public void insnLOADFUN(int fun) {
		// Loads functions that are defined at the root
		stack[sp++] = new FunctionInstance(functionStore.get(fun), root, this);
	}

	public void insnLOAD_NESTED_FUN(int fun, int scopeIn) {
		// Loads nested functions and closures (anonymous nested functions)
		stack[sp++] = FunctionInstance.computeFunctionInstance(
				functionStore.get(fun), cf, scopeIn, this);
	}

	public void insnLOADOFUN(int ofun) {
		OverloadedFunction of = overloadedStore.get(ofun);
		stack[sp++] = of.scopeIn == -1 ? new OverloadedFunctionInstance(
				of.functions, of.constructors, root, functionStore,
				constructorStore, this) : OverloadedFunctionInstance
				.computeOverloadedFunctionInstance(of.functions,
						of.constructors, cf, of.scopeIn, functionStore,
						constructorStore, this);
	}

	public void insnLOADCONSTR(int construct) {
		Type constructor = constructorStore.get(construct);
		stack[sp++] = constructor;
	}

	public void insnLOADVAR(int scopeid, int pos, boolean maxArg2) {
		postOp = 0;
		Object rval;

		if (maxArg2) {
			rval = moduleVariables.get(cf.function.constantStore[scopeid]);
			if (rval == null) {
				postOp = Opcode.POSTOP_CHECKUNDEF;
				return; // TODO break INSTRUCTION;
			}
			stack[sp++] = rval;
			return;
		}

		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == scopeid) {
				rval = fr.stack[pos];
				if (rval == null) {
					postOp = Opcode.POSTOP_CHECKUNDEF;
					return; // TODO break INSTRUCTION;
				}
				stack[sp++] = rval;
				return;
			}
		}
		throw new RuntimeException("LOADVAR cannot find matching scope: "
				+ scopeid);
	}

	public void insnLOADVARREF(int scopeid, int pos, boolean maxarg2) {
		Object rval;
		if (maxarg2) {
			rval = moduleVariables.get(cf.function.constantStore[scopeid]);
			stack[sp++] = rval;
			return;
		}

		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == scopeid) {
				rval = new Reference(fr.stack, pos);
				stack[sp++] = rval;
				return;
			}
		}
		throw new RuntimeException(
				"LOADVAR or LOADVARREF cannot find matching scope: " + scopeid);
	}

	public void insnLOADVARDEREF(int scopeid, int pos) {
		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == scopeid) {
				Reference ref = (Reference) fr.stack[pos];
				stack[sp++] = ref.stack[ref.pos];
				return;
			}
		}
		throw new RuntimeException("LOADVARDEREF cannot find matching scope: "
				+ scopeid);
	}

	public void insnSTOREVAR(int scopeid, int pos, boolean maxarg2) {
		if (CodeBlock.isMaxArg2(pos)) {
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
		throw new RuntimeException("STOREVAR cannot find matching scope: "
				+ scopeid);
	}

	public void insnUNWRAPTHROWNVAR(int scopeid, int pos, boolean maxarg2) {
		if (maxarg2) {
			IValue mvar = cf.function.constantStore[scopeid];
			moduleVariables.put(mvar, (IValue) stack[sp - 1]);
			return;
		}
		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == scopeid) {
				// TODO: We need to re-consider how to guarantee safe use of
				// both Java objects and IValues
				fr.stack[pos] = ((Thrown) stack[--sp]).value;
				return;
			}
		}
		throw new RuntimeException(
				"UNWRAPTHROWNVAR cannot find matching scope: " + scopeid);
	}

	public void insnSTOREVARDEREF(int scopeid, int pos) {
		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == scopeid) {
				Reference ref = (Reference) fr.stack[pos];
				ref.stack[ref.pos] = stack[sp - 1];
				return;
			}
		}
		throw new RuntimeException("STOREVARDEREF cannot find matching scope: "
				+ scopeid);
	}

	public void insnCALLCONSTR(int constrctr, int arity) {
		Type constructor = constructorStore.get(constrctr);

		IValue[] args = null;
		if (arity == constructor.getArity()) {
			args = new IValue[arity];
		} else {
			// Constructors with keyword parameters
			Type type = (Type) stack[--sp];
			IMap kwargs = (IMap) stack[--sp];
			Object[] types = new Object[2 * constructor.getArity() + 2
					* kwargs.size()];
			int j = 0;
			for (int i = 0; i < constructor.getArity(); i++) {
				types[j++] = constructor.getFieldType(i);
				types[j++] = constructor.getFieldName(i);
			}
			args = new IValue[constructor.getArity() + kwargs.size()];
			for (int i = 0; i < type.getArity(); i++) {
				types[j++] = type.getFieldType(i);
				types[j++] = type.getFieldName(i);
				args[constructor.getArity() + i] = kwargs.get(vf.string(type
						.getFieldName(i)));
			}
			constructor = tf.constructorFromTuple(typeStore,
					constructor.getAbstractDataType(), constructor.getName(),
					tf.tupleType(types), constructor.getArity());
		}
		for (int i = 0; i < constructor.getPositionalArity(); i++) {
			args[constructor.getPositionalArity() - 1 - i] = (IValue) stack[--sp];
		}
		stack[sp++] = vf.constructor(constructor, args);
	}

	public void insnCALLDYN(int arity) {
		// In case of CALLDYN, the stack top value of type 'Type'
		// leads to a constructor call
		if (stack[sp - 1] instanceof Type) {
			Type constr = (Type) stack[--sp];
			arity = constr.getArity();
			IValue[] args = new IValue[arity];
			for (int i = arity - 1; i >= 0; i--) {
				args[i] = (IValue) stack[sp - arity + i];
			}
			sp = sp - arity;
			stack[sp++] = vf.constructor(constr, args);
			return;
		}

		// Specific to delimited continuations (experimental)
		if (stack[sp - 1] instanceof Coroutine) {
			Coroutine coroutine = (Coroutine) stack[--sp];
			// Merged the hasNext and next semantics
			activeCoroutines.push(coroutine);
			ccf = coroutine.start;
			coroutine.next(cf);
			instructions = coroutine.frame.function.codeblock.getInstructions();
			coroutine.frame.stack[coroutine.frame.sp++] = arity == 1 ? stack[--sp]
					: null;
			cf.pc = pc;
			cf.sp = sp;
			cf = coroutine.frame;
			stack = cf.stack;
			sp = cf.sp;
			pc = cf.pc;
			return;
		}
		cf.pc = pc;
		if (stack[sp - 1] instanceof FunctionInstance) {
			FunctionInstance fun_instance = (FunctionInstance) stack[--sp];
			// In case of partial parameter binding
			if (fun_instance.next + arity < fun_instance.function.nformals) {
				fun_instance = fun_instance.applyPartial(arity, stack, sp);
				sp = sp - arity;
				stack[sp++] = fun_instance;
				return;
			}
			cf = cf.getFrame(fun_instance.function, fun_instance.env,
					fun_instance.args, arity, sp);
		} else {
			throw new RuntimeException("Unexpected argument type for CALLDYN: "
					+ asString(stack[sp - 1]));
		}
		instructions = cf.function.codeblock.getInstructions();
		stack = cf.stack;
		sp = cf.sp;
		pc = cf.pc;
	}

	public void insnCALL(int funid, int arity) {
		Frame tmp;
		cf.pc = pc;
		Function fun = functionStore.get(funid);
		// In case of partial parameter binding
		if (arity < fun.nformals) {
			FunctionInstance fun_instance = FunctionInstance.applyPartial(fun,
					root, this, arity, stack, sp);
			sp = sp - arity;
			stack[sp++] = fun_instance;
			return;
		}
		tmp = cf.getFrame(fun, root, arity, sp);
		this.cf = tmp;

		this.instructions = cf.function.codeblock.getInstructions();
		this.stack = cf.stack;
		this.sp = cf.sp;
		this.pc = cf.pc;
	}

	public void insnOCALLDYN(int typesel, int arity) {
		Object funcObject = stack[--sp];
		// Get function arguments from the stack

		cf.sp = sp;
		cf.pc = pc;

		// Get function types to perform a type-based dynamic
		// resolution
		Type types = cf.function.codeblock.getConstantType(typesel);
		// Objects of three types may appear on the stack:
		// 1. FunctionInstance due to closures
		if (funcObject instanceof FunctionInstance) {
			FunctionInstance fun_instance = (FunctionInstance) funcObject;
			cf = cf.getFrame(fun_instance.function, fun_instance.env, arity, sp);
			instructions = cf.function.codeblock.getInstructions();
			stack = cf.stack;
			sp = cf.sp;
			pc = cf.pc;
			return;
		}
		// 2. OverloadedFunctionInstance due to named Rascal
		// functions
		OverloadedFunctionInstance of_instance = (OverloadedFunctionInstance) funcObject;
		c_ofun_call = new OverloadedFunctionInstanceCall(cf,
				of_instance.functions, of_instance.constructors,
				of_instance.env, types, arity);
		ocalls.push(c_ofun_call);

		if (debug) {
			this.appendToTrace("OVERLOADED FUNCTION CALLDYN: ");
			this.appendToTrace("	with alternatives:");
			for (int index : c_ofun_call.functions) {
				this.appendToTrace("		" + getFunctionName(index));
			}
		}

		Frame frame = c_ofun_call.nextFrame(functionStore);
		if (frame != null) {
			if (debug) {
				this.appendToTrace("		" + "try alternative: "
						+ frame.function.name);
			}
			cf = frame;
			instructions = cf.function.codeblock.getInstructions();
			stack = cf.stack;
			sp = cf.sp;
			pc = cf.pc;
		} else {
			Type constructor = c_ofun_call.nextConstructor(constructorStore);
			sp = sp - arity;
			stack[sp++] = vf
					.constructor(constructor, c_ofun_call
							.getConstructorArguments(constructor.getArity()));
		}
	}

	public void insnOCALL(int ofun, int arity) {
		cf.sp = sp;
		cf.pc = pc;

		OverloadedFunction of = overloadedStore.get(CodeBlock
				.fetchArg1(instruction));
		c_ofun_call = of.scopeIn == -1 ? new OverloadedFunctionInstanceCall(cf,
				of.functions, of.constructors, root, null, arity)
				: OverloadedFunctionInstanceCall
						.computeOverloadedFunctionInstanceCall(cf,
								of.functions, of.constructors, of.scopeIn,
								null, arity);

		ocalls.push(c_ofun_call);

		if (debug) {
			this.appendToTrace("OVERLOADED FUNCTION CALL: "
					+ getOverloadedFunctionName(CodeBlock
							.fetchArg1(instruction)));
			this.appendToTrace("	with alternatives:");
			for (int index : c_ofun_call.functions) {
				this.appendToTrace("		" + getFunctionName(index));
			}
		}

		Frame frame = c_ofun_call.nextFrame(functionStore);

		if (frame != null) {
			if (debug) {
				this.appendToTrace("		" + "try alternative: "
						+ frame.function.name);
			}
			cf = frame;
			instructions = cf.function.codeblock.getInstructions();
			stack = cf.stack;
			sp = cf.sp;
			pc = cf.pc;
		} else {
			Type constructor = c_ofun_call.nextConstructor(constructorStore);
			sp = sp - arity;
			stack[sp++] = vf
					.constructor(constructor, c_ofun_call
							.getConstructorArguments(constructor.getArity()));
		}
	}

	public void insnFAILRETURN() {
		assert cf.previousCallFrame == c_ofun_call.cf;

		Frame frame = c_ofun_call.nextFrame(functionStore);
		if (frame != null) {
			if (debug) {
				this.appendToTrace("		" + "try alternative: "
						+ frame.function.name);
			}
			cf = frame;
			instructions = cf.function.codeblock.getInstructions();
			stack = cf.stack;
			sp = cf.sp;
			pc = cf.pc;
		} else {
			cf = c_ofun_call.cf;
			instructions = cf.function.codeblock.getInstructions();
			stack = cf.stack;
			sp = cf.sp;
			pc = cf.pc;
			Type constructor = c_ofun_call.nextConstructor(constructorStore);
			stack[sp++] = vf
					.constructor(constructor, c_ofun_call
							.getConstructorArguments(constructor.getArity()));
		}
	}

	public void insnFILTERRETURN(int arity) {
		globalReturnValue = null;
		// Overloading specific
		if (c_ofun_call != null && cf.previousCallFrame == c_ofun_call.cf) {
			ocalls.pop();
			c_ofun_call = ocalls.isEmpty() ? null : ocalls.peek();
		}

		Object rval = null;
		boolean returns = cf.isCoroutine || op == Opcode.OP_RETURN1
				|| op == Opcode.OP_FILTERRETURN;
		if (op == Opcode.OP_RETURN1 || cf.isCoroutine) {
			if (cf.isCoroutine) {
				rval = Rascal_TRUE;
				if (op == Opcode.OP_RETURN1) {
					arity = CodeBlock.fetchArg1(instruction);
					int[] refs = cf.function.refs;
					if (arity != refs.length) {
						throw new RuntimeException(
								"Coroutine "
										+ cf.function.name
										+ ": arity of return ("
										+ arity
										+ ") unequal to number of reference parameters ("
										+ refs.length + ")");
					}
					for (int i1 = 0; i1 < arity; i1++) {
						Reference ref = (Reference) stack[refs[arity - 1 - i1]];
						ref.stack[ref.pos] = stack[--sp];
					}
				}
			} else {
				rval = stack[sp - 1];
			}
		}

		// if the current frame is the frame of a top active
		// coroutine,
		// then pop this coroutine from the stack of active
		// coroutines
		if (cf == ccf) {
			activeCoroutines.pop();
			ccf = activeCoroutines.isEmpty() ? null
					: activeCoroutines.peek().start;
		}

		cf = cf.previousCallFrame;
		if (cf == null) {
			globalReturnValue = rval;
			return;
		}
		instructions = cf.function.codeblock.getInstructions();
		stack = cf.stack;
		sp = cf.sp;
		pc = cf.pc;
		if (returns) {
			stack[sp++] = rval;
		}
	}

	public void insnRETURN0() {
		globalReturnValue = null;

		// Overloading specific
		if (c_ofun_call != null && cf.previousCallFrame == c_ofun_call.cf) {
			ocalls.pop();
			c_ofun_call = ocalls.isEmpty() ? null : ocalls.peek();
		}

		Object rval = null;
		boolean returns = cf.isCoroutine;
		if (returns) {
			rval = Rascal_TRUE;
		} else {
			rval = stack[sp - 1];
		}

		// if the current frame is the frame of a top active coroutine,
		// then pop this coroutine from the stack of active coroutines
		if (cf == ccf) {
			activeCoroutines.pop();
			ccf = activeCoroutines.isEmpty() ? null
					: activeCoroutines.peek().start;
		}
		cf = cf.previousCallFrame;
		if (cf == null) {
			if (returns) {
				globalReturnValue = rval;
				return; // TODO rval;
			} else {
				globalReturnValue = NONE;
				return; // TODO NONE;
			}
		}
		instructions = cf.function.codeblock.getInstructions();
		stack = cf.stack;
		sp = cf.sp;
		pc = cf.pc;
		if (returns) {
			stack[sp++] = rval;
		}
	}

	public void insnRETURN1(int arity) {
		globalReturnValue = null;

		// Overloading specific
		if (c_ofun_call != null && cf.previousCallFrame == c_ofun_call.cf) {
			ocalls.pop();
			c_ofun_call = ocalls.isEmpty() ? null : ocalls.peek();
		}

		Object rval = null;
		// boolean returns = cf.isCoroutine || op == Opcode.OP_RETURN1 || op ==
		// Opcode.OP_FILTERRETURN;
		if (cf.isCoroutine) {
			rval = Rascal_TRUE;
			int[] refs = cf.function.refs;
			if (arity != refs.length) {
				throw new RuntimeException("Coroutine " + cf.function.name
						+ ": arity of return (" + arity
						+ ") unequal to number of reference parameters ("
						+ refs.length + ")");
			}
			for (int i = 0; i < arity; i++) {
				Reference ref = (Reference) stack[refs[arity - 1 - i]];
				ref.stack[ref.pos] = stack[--sp];
			}
		} else {
			rval = stack[sp - 1];
		}

		// if the current frame is the frame of a top active coroutine,
		// then pop this coroutine from the stack of active coroutines
		if (cf == ccf) {
			activeCoroutines.pop();
			ccf = activeCoroutines.isEmpty() ? null
					: activeCoroutines.peek().start;
		}

		cf = cf.previousCallFrame;
		if (cf == null) {
			globalReturnValue = rval;
			return; // TODO rval;
		}
		instructions = cf.function.codeblock.getInstructions();
		stack = cf.stack;
		sp = cf.sp;
		pc = cf.pc;
		stack[sp++] = rval;
	}

	public void insnCALLJAVA() {
		postOp = 0;
		String methodName = ((IString) cf.function.constantStore[instructions[pc++]])
				.getValue();
		String className = ((IString) cf.function.constantStore[instructions[pc++]])
				.getValue();
		Type parameterTypes = cf.function.typeConstantStore[instructions[pc++]];
		int reflect = instructions[pc++];
		arity = parameterTypes.getArity();
		try {
			sp = callJavaMethod(methodName, className, parameterTypes, reflect,
					stack, sp);
		} catch (Throw e) {
			thrown = Thrown.getInstance(e.getException(), e.getLocation(),
					new ArrayList<Frame>());
			postOp = Opcode.POSTOP_HANDLEEXCEPTION;
			return; // TODO break INSTRUCTION;
		}
		return;
	}

	public void insnCALLJAVA(int m, int c, int p, int r) {
		postOp = 0;
		String methodName = ((IString) cf.function.constantStore[m]).getValue();
		String className = ((IString) cf.function.constantStore[c]).getValue();
		Type parameterTypes = cf.function.typeConstantStore[p];
		int reflect = instructions[r];
		arity = parameterTypes.getArity();
		try {
			sp = callJavaMethod(methodName, className, parameterTypes, reflect,
					stack, sp);
		} catch (Throw e) {
			thrown = Thrown.getInstance(e.getException(), e.getLocation(),
					new ArrayList<Frame>());
			postOp = Opcode.POSTOP_HANDLEEXCEPTION;
			return; // TODO break INSTRUCTION;
		}
		return;
	}

	public void insnCREATE(int fun, int arity) {

		cccf = cf.getCoroutineFrame(functionStore.get(fun), root, arity, sp);

		sp = cf.sp;
		cccf.previousCallFrame = cf;

		cf.sp = sp;
		cf.pc = pc;
		instructions = cccf.function.codeblock.getInstructions();
		cf = cccf;
		stack = cf.stack;
		sp = cf.sp;
		pc = cf.pc;
	}

	public void insnGUARD() {
		Object rval = stack[sp - 1];
		boolean precondition;
		if (rval instanceof IBool) {
			precondition = ((IBool) rval).getValue();
		} else if (rval instanceof Boolean) {
			precondition = (Boolean) rval;
		} else {
			throw new RuntimeException("Guard's expression has to be boolean!");
		}

		if (cf == cccf) {
			Coroutine coroutine = null;
			Frame prev = cf.previousCallFrame;
			if (precondition) {
				coroutine = new Coroutine(cccf);
				coroutine.isInitialized = true;
				coroutine.suspend(cf);
			}
			cccf = null;
			--sp;
			cf.pc = pc;
			cf.sp = sp;
			cf = prev;
			instructions = cf.function.codeblock.getInstructions();
			stack = cf.stack;
			sp = cf.sp;
			pc = cf.pc;
			stack[sp++] = precondition ? coroutine : exhausted;
			return;
		}

		if (!precondition) {
			cf.pc = pc;
			cf.sp = sp;
			cf = cf.previousCallFrame;
			instructions = cf.function.codeblock.getInstructions();
			stack = cf.stack;
			sp = cf.sp;
			pc = cf.pc;
			stack[sp++] = Rascal_FALSE;
			return;
		}
	}

	public void insnAPPLY(int function, int arity) {
		FunctionInstance fun_instance;
		Function fun = functionStore.get(function);
		assert arity <= fun.nformals;
		assert fun.scopeIn == -1;
		fun_instance = FunctionInstance.applyPartial(fun, root, this, arity,
				stack, sp);
		sp = sp - arity;
		stack[sp++] = fun_instance;
		return;
	}

	public void insnAPPLYDYN(int arity) {
		FunctionInstance fun_instance;
		Object src = stack[--sp];
		if (src instanceof FunctionInstance) {
			fun_instance = (FunctionInstance) src;
			assert arity + fun_instance.next <= fun_instance.function.nformals;
			fun_instance = fun_instance.applyPartial(arity, stack, sp);
		} else {
			throw new RuntimeException(
					"Unexpected argument type for APPLYDYN: " + asString(src));
		}
		sp = sp - arity;
		stack[sp++] = fun_instance;
	}

	public void insnNEXT0() {
		insnNEXT1(false);
	}

	public void insnNEXT1(boolean rets) {
		Coroutine coroutine = (Coroutine) stack[--sp];

		// Merged the hasNext and next semantics
		if (!coroutine.hasNext()) {
			if (rets) {
				--sp;
			}
			stack[sp++] = FALSE;
			return;
		}
		// put the coroutine onto the stack of active coroutines
		activeCoroutines.push(coroutine);
		ccf = coroutine.start;
		coroutine.next(cf);

		instructions = coroutine.frame.function.codeblock.getInstructions();

		coroutine.frame.stack[coroutine.frame.sp++] = rets ? stack[--sp] : null;
		// Always leave an entry on the stack

		cf.pc = pc;
		cf.sp = sp;

		cf = coroutine.frame;
		stack = cf.stack;
		sp = cf.sp;
		pc = cf.pc;
	}

	public void insnYIELD0() {
		Coroutine coroutine = activeCoroutines.pop();
		ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
		Frame prev = coroutine.start.previousCallFrame;
		IBool rval = Rascal_TRUE; // In fact, yield has to always return TRUE
		cf.pc = pc;
		cf.sp = sp;
		coroutine.suspend(cf);
		cf = prev;

		instructions = cf.function.codeblock.getInstructions();
		stack = cf.stack;
		sp = cf.sp;
		pc = cf.pc;
		stack[sp++] = rval; // Corresponding next will always find an entry on
							// the stack
	}

	public void insnYIELD1(int arity) {
		globalReturnValue = null;
		Coroutine coroutine = activeCoroutines.pop();
		ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
		Frame prev = coroutine.start.previousCallFrame;
		IBool rval = Rascal_TRUE; // In fact, yield has to always return TRUE
		int[] refs = cf.function.refs;

		if (arity != refs.length) {
			throw new RuntimeException(
					"The 'yield' within a coroutine has to take the same number of arguments as the number of its reference parameters; arity: "
							+ arity
							+ "; reference parameter number: "
							+ refs.length);
		}

		for (int i = 0; i < arity; i++) {
			Reference ref = (Reference) stack[refs[arity - 1 - i]]; // Takes
			// the reference parameters of the top active coroutine instance
			ref.stack[ref.pos] = stack[--sp];
		}
		cf.pc = pc;
		cf.sp = sp;
		coroutine.suspend(cf);
		cf = prev;
		if (cf == null) {
			globalReturnValue = rval;
			return; // TODO rval;
		}
		instructions = cf.function.codeblock.getInstructions();
		stack = cf.stack;
		sp = cf.sp;
		pc = cf.pc;
		stack[sp++] = rval; // Corresponding next will always find an entry on
							// the stack
	}

	public void insnEXHAUST() {
		globalReturnValue = null;
		if (cf == ccf) {
			activeCoroutines.pop();
			ccf = activeCoroutines.isEmpty() ? null
					: activeCoroutines.peek().start;
		}

		cf = cf.previousCallFrame;
		if (cf == null) {
			globalReturnValue = Rascal_FALSE;
			return; // TODO Rascal_FALSE; // 'Exhaust' has to always return
					// FALSE, i.e., signal a failure;
		}
		instructions = cf.function.codeblock.getInstructions();
		stack = cf.stack;
		sp = cf.sp;
		pc = cf.pc;
		stack[sp++] = Rascal_FALSE; // 'Exhaust' has to always return FALSE,
									// i.e., signal a failure;
	}

	public void insnCALLPRIM(int muprim, int arity) {
		postOp = 0;
		try {
			sp = RascalPrimitive.values[muprim].execute(stack, sp, arity);
		} catch (Exception exception) {
			if (!(exception instanceof Thrown)) {
				throw exception;
			}
			thrown = (Thrown) exception;
			thrown.stacktrace.add(cf);
			sp = sp - arity;
			postOp = Opcode.POSTOP_HANDLEEXCEPTION;
		}
	}

	public void insnSUBSCRIPTARRAY() {
		stack[sp - 2] = ((Object[]) stack[sp - 2])[((Integer) stack[sp - 1])];
		sp--;
	}

	public void insnSUBSCRIPTLIST() {
		stack[sp - 2] = ((IList) stack[sp - 2]).get((Integer) stack[sp - 1]);
		sp--;
	}

	public void insnLESSINT() {
		stack[sp - 2] = ((Integer) stack[sp - 2]) < ((Integer) stack[sp - 1]);
		sp--;
	}

	public void insnGREATEREQUALINT() {
		stack[sp - 2] = ((Integer) stack[sp - 2]) >= ((Integer) stack[sp - 1]);
		sp--;
	}

	public void insnADDINT() {
		stack[sp - 2] = ((Integer) stack[sp - 2]) + ((Integer) stack[sp - 1]);
		sp--;
	}

	public void insnSUBTRACTINT() {
		stack[sp - 2] = ((Integer) stack[sp - 2]) - ((Integer) stack[sp - 1]);
		sp--;
	}

	public void insnANDBOOL() {
		boolean b1 = (stack[sp - 2] instanceof Boolean) ? ((Boolean) stack[sp - 2])
				: ((IBool) stack[sp - 2]).getValue();
		boolean b2 = (stack[sp - 1] instanceof Boolean) ? ((Boolean) stack[sp - 1])
				: ((IBool) stack[sp - 1]).getValue();
		stack[sp - 2] = b1 && b2;
		sp--;
	}

	public void insnTYPEOF() {
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

	public void insnSUBTYPE() {
		stack[sp - 2] = ((Type) stack[sp - 2])
				.isSubtypeOf((Type) stack[sp - 1]);
		sp--;
		return;

	}

	public void insnCHECKARGTYPE() {
		Type argType = ((IValue) stack[sp - 2]).getType();
		Type paramType = ((Type) stack[sp - 1]);
		stack[sp - 2] = argType.isSubtypeOf(paramType);
		sp--;
		return;

	}

	public void insnLABEL() {
		throw new RuntimeException("label instruction at runtime");

	}

	public void insnHALT() {
		if (debug) {
			stdout.println("Program halted:");
			for (int i = 0; i < sp; i++) {
				stdout.println(i + ": " + stack[i]);
			}
		}
		return; // TODO stack[sp - 1];

	}

	public void insnPRINTLN(int arity) {
		StringBuilder w = new StringBuilder();
		for (int i = arity - 1; i >= 0; i--) {
			String str = (stack[sp - 1 - i] instanceof IString) ? ((IString) stack[sp
					- 1 - i]).toString()
					: asString(stack[sp - 1 - i]);
			w.append(str).append(" ");
		}
		stdout.println(w.toString());
		sp = sp - arity + 1;
	}

	public void insnTHROW() {
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

	public void insnLOADLOCKWP(int constant) {
		IString name = (IString) cf.function.codeblock
				.getConstantValue(constant);
		@SuppressWarnings("unchecked")
		Map<String, Map.Entry<Type, IValue>> defaults = (Map<String, Map.Entry<Type, IValue>>) stack[cf.function.nformals];
		Map.Entry<Type, IValue> defaultValue = defaults.get(name.getValue());
		for (Frame f = cf; f != null; f = f.previousCallFrame) {
			IMap kargs = (IMap) f.stack[f.function.nformals - 1];
			if (kargs.containsKey(name)) {
				IValue val = kargs.get(name);
				if (val.getType().isSubtypeOf(defaultValue.getKey())) {
					stack[sp++] = val;
					return;
				}
			}
		}
		stack[sp++] = defaultValue.getValue();
	}

	public void insnLOADVARKWP() {
		return;
	}

	public void insnSTORELOCKWP(int constant) {
		IValue val = (IValue) stack[sp - 1];
		IString name = (IString) cf.function.codeblock
				.getConstantValue(constant);
		IMap kargs = (IMap) stack[cf.function.nformals - 1];
		stack[cf.function.nformals - 1] = kargs.put(name, val);
	}

	public void insnSTOREVARKWP() {
		return;
	}

	public void insnLOADCONT(int scopeid) {
		assert stack[0] instanceof Coroutine;
		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == scopeid) {
				// TODO: unsafe in general case (the coroutine object should be
				// copied)
				stack[sp++] = fr.stack[0];
				return;
			}
		}
		throw new RuntimeException("LOADCONT cannot find matching scope: "
				+ scopeid);
	}

	public void insnRESET() {
		FunctionInstance fun_instance = (FunctionInstance) stack[--sp];
		// A fucntion of zero arguments
		cf.pc = pc;
		cf = cf.getCoroutineFrame(fun_instance, 0, sp);
		activeCoroutines.push(new Coroutine(cf));
		ccf = cf;
		instructions = cf.function.codeblock.getInstructions();
		stack = cf.stack;
		sp = cf.sp;
		pc = cf.pc;
		return;
	}

	public void insnSHIFT() {
		FunctionInstance fun_instance = (FunctionInstance) stack[--sp];
		// A function of one argument (continuation)
		Coroutine coroutine = activeCoroutines.pop();
		ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
		cf.pc = pc;
		cf.sp = sp;
		Frame prev = coroutine.start.previousCallFrame;
		coroutine.suspend(cf);
		cf = prev;
		sp = cf.sp;
		fun_instance.args = new Object[] { coroutine };
		cf = cf.getCoroutineFrame(fun_instance, 0, sp);
		activeCoroutines.push(new Coroutine(cf));
		ccf = cf;
		instructions = cf.function.codeblock.getInstructions();
		stack = cf.stack;
		sp = cf.sp;
		pc = cf.pc;
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

		return dynRun(n);
	}

	public Object dynRun(int n) {
		System.out.println("Unimplemented Base called !");
		return PANIC;
	}

	public Object return1Helper() {
		Object rval = null;
		if (cf.isCoroutine) {
			rval = Rascal_TRUE;
			int[] refs = cf.function.refs;
			if (arity != refs.length) {
				throw new RuntimeException("Coroutine " + cf.function.name
						+ ": arity of return (" + arity
						+ ") unequal to number of reference parameters ("
						+ refs.length + ")");
			}
			for (int i = 0; i < arity; i++) {
				Reference ref = (Reference) stack[refs[arity - 1 - i]];
				ref.stack[ref.pos] = stack[--sp];
			}
		} else {
			rval = stack[sp - 1];
		}
		cf = cf.previousCallFrame;
		return rval;
	}

	public void jvmCREATE(int fun, int arity) {
		cccf = cf.getCoroutineFrame(functionStore.get(fun), root, arity, sp);

		sp = cf.sp;
		cccf.previousCallFrame = cf;

		cf.sp = sp;

		cf = cccf;

		stack = cf.stack;
		sp = cf.sp;
		dynRun(fun); // Run untill guard, leaves coroutine instance in stack.
	}

	public void jvmCREATEDYN(int arity) {
		FunctionInstance fun_instance;

		Object src = stack[--sp];

		if (src instanceof FunctionInstance) {
			// In case of partial parameter binding
			fun_instance = (FunctionInstance) src;
			cccf = cf.getCoroutineFrame(fun_instance, arity, sp);
		} else {
			throw new RuntimeException(
					"Unexpected argument type for CREATEDYN: " + src.getClass()
							+ ", " + src);
		}
		cf.sp = sp;
		cccf.previousCallFrame = cf;

		cf = cccf;
		stack = cf.stack;
		sp = cf.sp;

		dynRun(fun_instance.function.funId);
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
		} else if (rval instanceof Boolean) {
			precondition = (Boolean) rval;
		} else {
			throw new RuntimeException("Guard's expression has to be boolean!");
		}
		return precondition;
	}

	public void yield1Helper(int arity2, int ep) {
		// Stores a Rascal_TRUE value in the stack of the NEXT? caller.
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
		// Stores a Rascal_TRUE value in the stack of the NEXT? caller.
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
				FunctionInstance fun_instance = FunctionInstance.applyPartial(
						fun, root, this, arity, stack, sp);
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

		rval = dynRun(fun.funId); // In a inline version we can call the
									// function directly.

		if (rval.equals(YIELD1)) {
			// drop my stack
			cf.hotEntryPoint = ep;
			cf.sp = sp;

			cf = cf.previousCallFrame;
			sp = cf.sp;
			stack = cf.stack;
			return YIELD1; // Will cause the inline call to return YIELD
		} else {
			cf.hotEntryPoint = 0 ;
			cf.nextFrame = null; // Allow GC to clean
			return NONE; // Inline call wil continue execution
		}
	}

	public void jvmNEXT0() {
		Coroutine coroutine = (Coroutine) stack[--sp];

		// Merged the hasNext and next semantics
		if (!coroutine.hasNext()) {
			stack[sp++] = FALSE;
			return;
		}
		// put the coroutine onto the stack of active coroutines
		activeCoroutines.push(coroutine);
		ccf = coroutine.start;
		coroutine.next(cf);

		// Push something on the stack of the prev yiellding function
		coroutine.frame.stack[coroutine.frame.sp++] = null;

		cf.sp = sp;

		coroutine.frame.previousCallFrame = cf;

		cf = coroutine.entryFrame;

		stack = cf.stack;
		sp = cf.sp;
		Object result = dynRun(coroutine.entryFrame.function.funId);
//		if (!result.equals(YIELD1))
//			System.out.println("Next did not recieve yield!");

	}

	public Object exhaustHelper() {
		if (cf == ccf) {
			activeCoroutines.pop();
			ccf = activeCoroutines.isEmpty() ? null
					: activeCoroutines.peek().start;
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

			// Specific to delimited continuations (experimental)
			if (stack[sp - 1] instanceof Coroutine) {
				// Coroutine coroutine = (Coroutine) stack[--sp];
				// // Merged the hasNext and next semantics
				// activeCoroutines.push(coroutine);
				// ccf = coroutine.start;
				// coroutine.next(cf);
				// instructions =
				// coroutine.frame.function.codeblock.getInstructions();
				// coroutine.frame.stack[coroutine.frame.sp++] = arity == 1 ?
				// stack[--sp] : null;
				// cf.pc = pc;
				// cf.sp = sp;
				// cf = coroutine.frame;
				// stack = cf.stack;
				// sp = cf.sp;
				// pc = cf.pc;
				return PANIC;
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
				tmp = cf.getFrame(fun_instance.function, fun_instance.env,
						fun_instance.args, arity, sp);
				cf.nextFrame = tmp;
			} else {
				throw new RuntimeException(
						"Unexpected argument type for CALLDYN: "
								+ asString(stack[sp - 1]));
			}
		} else {
			tmp = cf.nextFrame;
		}

		tmp.previousCallFrame = cf;

		this.cf = tmp;
		this.stack = cf.stack;
		this.sp = cf.sp;

		rval = dynRun(cf.function.funId); // In a inline version we can call the
											// function directly.

		if (rval.equals(YIELD1)) {
			// drop my stack
			cf.hotEntryPoint = ep;
			cf.sp = sp;

			cf = cf.previousCallFrame;
			sp = cf.sp;
			stack = cf.stack;
			return YIELD1; // Will cause the inline call to return YIELD
		} else {
			cf.hotEntryPoint = 0 ;
			cf.nextFrame = null; // Allow GC to clean
			return NONE; // Inline call will continue execution
		}
	}

	// Next methods are for debug only. Single step..
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

	public void dinsnJMP(int target) {
		jmpTarget = target;
	}

	public void dinsnPOP() {
		prevSP = sp;
	}

	public void dinsnGUARD() {
		prevSP = sp;
	}

	public void dinsnEXHAUST() {
		prevSP = sp;
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
}
