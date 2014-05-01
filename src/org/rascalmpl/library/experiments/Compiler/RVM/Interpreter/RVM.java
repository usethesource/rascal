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
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.control_exceptions.Throw;	// TODO: remove import: NOT YET: JavaCalls generate a Throw
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Opcode;
import org.rascalmpl.uri.URIResolverRegistry;


public class RVM {

	public final IValueFactory vf;
	private final TypeFactory tf;
//	private final Boolean TRUE;
//	private final Boolean FALSE;
	private final IBool Rascal_TRUE;
	private final IBool Rascal_FALSE;
	private final IString NONE; 
	
	private boolean debug = true;
	private boolean listing = false;
	private boolean finalized = false;
	
	private final ArrayList<Function> functionStore;
	private final Map<String, Integer> functionMap;
	
	// Function overloading
	private final Map<String, Integer> resolver;
	private final ArrayList<OverloadedFunction> overloadedStore;
	
	private final TypeStore typeStore = new TypeStore();
	private final Types types;
	
	private final ArrayList<Type> constructorStore;
	private final Map<String, Integer> constructorMap;
	
	private final Map<IValue, IValue> moduleVariables;
	PrintWriter stdout;
	PrintWriter stderr;
	
	// Management of active coroutines
	Stack<Coroutine> activeCoroutines = new Stack<>();
	Frame ccf = null; // The start frame of the current active coroutine (coroutine's main function)
	Frame cccf = null; // The candidate coroutine's start frame; used by the guard semantics 
	//IEvaluatorContext ctx;
	RascalExecutionContext rex;
	List<ClassLoader> classLoaders;
	
	// An exhausted coroutine instance
	public static Coroutine exhausted = new Coroutine(null) {

		@Override
		public void next(Frame previousCallFrame) {
			throw new CompilerError("Attempt to activate an exhausted coroutine instance.");
		}
		
		@Override
		public void suspend(Frame current) {
			throw new CompilerError("Attempt to suspend an exhausted coroutine instance.");
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
			throw new CompilerError("Attempt to copy an exhausted coroutine instance.");
		}  
	};


	public RVM(RascalExecutionContext rex) {
		super();

		this.vf = rex.getValueFactory();
		tf = TypeFactory.getInstance();
		
		this.rex = rex;
		//this.ctx = ctx;
		this.classLoaders = rex.getClassLoaders();
		this.stdout = rex.getStdOut();
		this.stderr = rex.getStdErr();
		this.debug = rex.getDebug();
		this.finalized = false;
		
		this.types = new Types(this.vf);
		
//		TRUE = true;
//		FALSE = false;
		Rascal_TRUE = vf.bool(true);
		Rascal_FALSE = vf.bool(false);
		NONE = vf.string("$nothing$");
		functionStore = new ArrayList<Function>();
		constructorStore = new ArrayList<Type>();

		functionMap = new HashMap<String, Integer>();
		constructorMap = new HashMap<String, Integer>();
		
		resolver = new HashMap<String,Integer>();
		overloadedStore = new ArrayList<OverloadedFunction>();
		
		moduleVariables = new HashMap<IValue,IValue>();
		
		MuPrimitive.init(vf, stdout, rex.getProfile());
		RascalPrimitive.init(this, rex);
		Opcode.init(stdout, rex.getProfile());
	}
	
	URIResolverRegistry getResolverRegistry() { return rex.getResolverRegistry(); }
	IRascalMonitor getMonitor() {return rex.getMonitor();}
	PrintWriter getStdErr() { return rex.getStdErr(); }
	PrintWriter getStdOut() { return rex.getStdOut(); }
	Configuration getConfiguration() { return rex.getConfiguration(); }
	List<ClassLoader> getClassLoaders() { return rex.getClassLoaders(); }
	IEvaluatorContext getEvaluatorContext() { return rex.getEvaluatorContext(); }

	
	public void declare(Function f){
		if(functionMap.get(f.getName()) != null){
			throw new CompilerError("Double declaration of function: " + f.getName());
		}
		functionMap.put(f.getName(), functionStore.size());
		functionStore.add(f);
	}
	
	public void declareConstructor(String name, IConstructor symbol) {
		Type constr = types.symbolToType(symbol, typeStore);
		if(constructorMap.get(name) != null) {
			throw new CompilerError("Double declaration of constructor: " + name);
		}
		constructorMap.put(name, constructorStore.size());
		constructorStore.add(constr);
	}
	
	public Type symbolToType(IConstructor symbol) {
		return types.symbolToType(symbol, typeStore);
	}
	
	public void addResolver(IMap resolver) {
		for(IValue fuid : resolver) {
			String of = ((IString) fuid).getValue();
			int index = ((IInteger) resolver.get(fuid)).intValue();
			this.resolver.put(of, index);
		}
	}
	
	public void fillOverloadedStore(IList overloadedStore) {
		for(IValue of : overloadedStore) {
			ITuple ofTuple = (ITuple) of;
			String scopeIn = ((IString) ofTuple.get(0)).getValue();
			if(scopeIn.equals("")) {
				scopeIn = null;
			}
			IList fuids = (IList) ofTuple.get(1);
			int[] funs = new int[fuids.length()];
			int i = 0;
			for(IValue fuid : fuids) {
				String name = ((IString) fuid).getValue();
				Integer index = functionMap.get(name);
				if(index == null){
					throw new CompilerError("No definition for " + fuid + " in functionMap");
				}
				funs[i++] = index;
			}
			fuids = (IList) ofTuple.get(2);
			int[] constrs = new int[fuids.length()];
			i = 0;
			for(IValue fuid : fuids) {
				Integer index = constructorMap.get(((IString) fuid).getValue());
				if(index == null){
					throw new CompilerError("No definition for " + fuid + " in constructorMap");
				}
				constrs[i++] = index;
			}
			this.overloadedStore.add(new OverloadedFunction(funs, constrs, scopeIn));
		}
	}
	
	/**
	 * Narrow an Object as occurring on the RVM runtime stack to an IValue that can be returned.
	 * Note that various non-IValues can occur:
	 * - Coroutine
	 * - Reference
	 * - FunctionInstance
	 * - Object[] (is converted to an IList)
	 * @param result to be returned
	 * @return converted result or an exception
	 */
	private IValue narrow(Object result){
//		if(result instanceof Boolean) {
//			return vf.bool((Boolean) result);
//		}
		if(result instanceof Integer) {
			return vf.integer((Integer)result);
		}
		if(result instanceof IValue) {
			return (IValue) result;
		}
		if(result instanceof Thrown) {
			((Thrown) result).printStackTrace(stdout);
			return vf.string(((Thrown) result).toString());
		}
		if(result instanceof Object[]) {
			IListWriter w = vf.listWriter();
			Object[] lst = (Object[]) result;
			for(int i = 0; i < lst.length; i++){
				w.append(narrow(lst[i]));
			}
			return w.done();
		}
		throw new CompilerError("Cannot convert object back to IValue: " + result);
	}
	
	/**
	 * Represent any object that can occur on the RVM stack stack as string
	 * @param some stack object
	 * @return its string representation
	 */
	@SuppressWarnings("rawtypes")
	private String asString(Object o){
		if(o == null)
			return "null";
//		if(o instanceof Boolean)
//			return ((Boolean) o).toString() + " [Java]";
		if(o instanceof Integer)
			return ((Integer)o).toString() + " [Java]";
		if(o instanceof IValue)
			return ((IValue) o).toString() +" [IValue]";
		if(o instanceof Type)
			return ((Type) o).toString() + " [Type]";
		if(o instanceof Object[]){
			StringBuilder w = new StringBuilder();
			Object[] lst = (Object[]) o;
			w.append("[");
			for(int i = 0; i < lst.length; i++){
				w.append(asString(lst[i]));
				if(i < lst.length - 1)
						w.append(", ");
			}
			w.append("]");
			return w.toString() + " [Object[]]";
		}
		if(o instanceof Coroutine){
			return "Coroutine[" + ((Coroutine)o).frame.function.getName() + "]";
		}
		if(o instanceof Function){
			return "Function[" + ((Function)o).getName() + "]";
		}
		if(o instanceof FunctionInstance){
			return "Function[" + ((FunctionInstance)o).function.getName() + "]";
		}
		if(o instanceof OverloadedFunctionInstance) {
			OverloadedFunctionInstance of = (OverloadedFunctionInstance) o;
			String alts = "";
			for(Integer fun : of.functions) {
				alts = alts + functionStore.get(fun).getName() + "; ";
			}
			return "OverloadedFunction[ alts: " + alts + "]";
		}
		if(o instanceof Reference){
			Reference ref = (Reference) o;
			return "Reference[" + ref.stack + ", " + ref.pos + "]";
		}
		if(o instanceof IListWriter){
			return "ListWriter[" + ((IListWriter) o).toString() + "]";
		}
		if(o instanceof ISetWriter){
			return "SetWriter[" + ((ISetWriter) o).toString() + "]";
		}
		if(o instanceof IMapWriter){
			return "MapWriter[" + ((IMapWriter) o).toString() + "]";
		}
		if(o instanceof Matcher){
			return "Matcher[" + ((Matcher) o).pattern() + "]";
		}
		if(o instanceof Thrown) {
			return "THROWN[ " + asString(((Thrown) o).value) + " ]";
		}
		
		if(o instanceof StringBuilder){
			return "StringBuilder[" + ((StringBuilder) o).toString() + "]";
		}
		if(o instanceof HashSet){
			return "HashSet[" + ((HashSet<?>) o).toString() + "]";
		}
		if(o instanceof HashMap){
			return "HashMap[" + ((HashMap<?, ?>) o).toString() + "]";
		}
		if(o instanceof Map.Entry){
			return "Map.Entry[" + ((Map.Entry) o).toString() + "]";
		}
		throw new CompilerError("asString cannot convert: " + o);
	}
	
	public void finalize(){
		// Finalize the instruction generation of all functions, if needed
		if(!finalized){
			finalized = true;
			for(Function f : functionStore) {
				f.finalize(functionMap, constructorMap, resolver, listing);
			}
			for(OverloadedFunction of : overloadedStore) {
				of.finalize(functionMap);
			}
		}
	}

	public String getFunctionName(int n) {
		for(String fname : functionMap.keySet()) {
			if(functionMap.get(fname) == n) {
				return fname;
			}
		}
		throw new CompilerError("Undefined function index " + n);
	}

	public String getConstructorName(int n) {
		for(String cname : constructorMap.keySet()) {
			if(constructorMap.get(cname) == n) {
				return cname;
			}
		}
		throw new CompilerError("Undefined constructor index " + n);
	}
	
	public String getOverloadedFunctionName(int n) {
		for(String ofname : resolver.keySet()) {
			if(resolver.get(ofname) == n) {
				return ofname;
			}
		}
		throw new CompilerError("Undefined overloaded function index " + n);
	}
	
	public IValue executeFunction(String uid_func, IValue[] args){
		// Assumption here is that the function called is not nested one
		// and does not use global variables
		Function func = functionStore.get(functionMap.get(uid_func));
		Frame root = new Frame(func.scopeId, null, func.maxstack, func);
		Frame cf = root;
		
		// Pass the program arguments to main
		for(int i = 0; i < args.length; i++){
			cf.stack[i] = args[i]; 
		}
		Object o = executeProgram(root, cf);
		if(o instanceof Thrown){
			throw (Thrown) o;
		}
		return narrow(o); 
	}
	
	public IValue executeFunction(FunctionInstance func, IValue[] args){
		Frame root = new Frame(func.function.scopeId, null, func.env, func.function.maxstack, func.function);
		Frame cf = root;
		
		// Pass the program arguments to main
		for(int i = 0; i < args.length; i++) {
			cf.stack[i] = args[i]; 
		}
		Object o = executeProgram(root, cf);
		if(o instanceof Thrown){
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
	
	public String findVarName(Frame cf, int s, int pos){
		for (Frame fr = cf; fr != null; fr = fr.previousScope) {
			if (fr.scopeId == s) {
				return findLocalName(fr, pos);
			}
		}
		return "** unknown variable **";
	}
	
	public String findLocalName(Frame cf, int pos){
		IString name =  ((IString) cf.function.localNames.get(vf.integer(pos)));
		return (name != null) ? name.getValue() : "** unknown variable **";
	}
	
	
	public IValue executeProgram(String uid_main, IValue[] args) {
		
		finalize();
		
		Function main_function = functionStore.get(functionMap.get(uid_main));

		if (main_function == null) {
			throw RascalRuntimeException.noMainFunction(null);
		}
		
		if (main_function.nformals != 2) { // List of IValues and empty map of keyword parameters
			throw new CompilerError("Function " + uid_main + " should have two arguments");
		}
		
		Frame root = new Frame(main_function.scopeId, null, main_function.maxstack, main_function);
		Frame cf = root;
		cf.stack[0] = vf.list(args); // pass the program argument to main_function as a IList object
		cf.stack[1] = vf.mapWriter().done();
		cf.src = main_function.src;
		Object o = executeProgram(root, cf);
		if(o != null && o instanceof Thrown){
			throw (Thrown) o;
		}
		IValue res = narrow(o);
		if(debug) {
			stdout.println("TRACE:");
			stdout.println(getTrace());
		}
		return res;
	}
	
//	/*
//	 * The following instance variables are only used by executeProgram
//	 */
//	Object[] stack ;		                              			// current stack
//	int sp;				                  							// current stack pointer
//	int [] instructions ; 											// current instruction sequence
//	int pc;				                                      		// current program counter
//	int postOp;
//	int pos;
//	ArrayList<Frame> stacktrace;
//	Thrown thrown;
//	int arity;
//	String last_function_name;
//	
//	// Overloading specific
//	Stack<OverloadedFunctionInstanceCall> ocalls = new Stack<OverloadedFunctionInstanceCall>();
//	OverloadedFunctionInstanceCall c_ofun_call = null;
	
	private Object executeProgram(Frame root, Frame cf) {
		Object[] stack = cf.stack;		                              	// current stack
		int sp = cf.function.nlocals;				                  	// current stack pointer
		int [] instructions = cf.function.codeblock.getInstructions(); 	// current instruction sequence
		int pc = 0;				                                      	// current program counter
		int postOp = 0;
		int pos = 0;
		int varScope = -1;
		ArrayList<Frame> stacktrace = new ArrayList<Frame>();
		Thrown thrown = null;
		int arity;
		String last_function_name = "";
		String last_var_name = "unknown";
		
		// Overloading specific
		Stack<OverloadedFunctionInstanceCall> ocalls = new Stack<OverloadedFunctionInstanceCall>();
		OverloadedFunctionInstanceCall c_ofun_call = null;
		
		stack = cf.stack;		                              	// current stack
		sp = cf.function.nlocals;				                  	// current stack pointer
		instructions = cf.function.codeblock.getInstructions(); 	// current instruction sequence
		pc = 0;				                                      	// current program counter
		postOp = 0;
		pos = 0;
		last_function_name = "";
		
		try {
			NEXT_INSTRUCTION: while (true) {
//				if(pc < 0 || pc >= instructions.length){
//					throw new CompilerError(cf.function.name + " illegal pc: " + pc);
//				}
				int instruction = instructions[pc++];
				int op = CodeBlock.fetchOp(instruction);

				if (debug) {
					int startpc = pc - 1;
					if(!last_function_name.equals(cf.function.name))
						stdout.printf("[%03d] %s\n", startpc, cf.function.name);
					
					for (int i = 0; i < sp; i++) {
						stdout.println("\t   " + (i < cf.function.nlocals ? "*" : " ") + i + ": " + asString(stack[i]));
					}
					stdout.printf("%5s %s\n" , "", cf.function.codeblock.toString(startpc));
				}
				
				//Opcode.use(instruction);
				
				INSTRUCTION: switch (op) {
					
				case Opcode.OP_POP:
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC0:
					if(stack[0] != null){ stack[sp++] = stack[0]; continue NEXT_INSTRUCTION; }
					last_var_name = findLocalName(cf, 0); postOp = Opcode.POSTOP_CHECKUNDEF;	break;
					
				case Opcode.OP_LOADLOC1:
					if(stack[1] != null){ stack[sp++] = stack[1]; continue NEXT_INSTRUCTION; }
					last_var_name = findLocalName(cf, 1); postOp = Opcode.POSTOP_CHECKUNDEF;	break;
					
				case Opcode.OP_LOADLOC2:
					if(stack[2] != null){ stack[sp++] = stack[2]; continue NEXT_INSTRUCTION; }
					last_var_name = findLocalName(cf, 2); postOp = Opcode.POSTOP_CHECKUNDEF;	break;
					
				case Opcode.OP_LOADLOC3:
					if(stack[3] != null){ stack[sp++] = stack[3]; continue NEXT_INSTRUCTION; }
					last_var_name = findLocalName(cf, 3); postOp = Opcode.POSTOP_CHECKUNDEF;	break;
					
				case Opcode.OP_LOADLOC4:
					if(stack[4] != null){ stack[sp++] = stack[4]; continue NEXT_INSTRUCTION; }
					last_var_name = findLocalName(cf, 4); postOp = Opcode.POSTOP_CHECKUNDEF;	break;
					
				case Opcode.OP_LOADLOC5:
					if(stack[5] != null){ stack[sp++] = stack[5]; continue NEXT_INSTRUCTION; }
					last_var_name = findLocalName(cf, 5); postOp = Opcode.POSTOP_CHECKUNDEF;	break;
					
				case Opcode.OP_LOADLOC6:
					if(stack[6] != null){ stack[sp++] = stack[6]; continue NEXT_INSTRUCTION; }
					last_var_name = findLocalName(cf, 6); postOp = Opcode.POSTOP_CHECKUNDEF;	break;
					
				case Opcode.OP_LOADLOC7:
					if(stack[7] != null){ stack[sp++] = stack[7]; continue NEXT_INSTRUCTION; }
					last_var_name = findLocalName(cf, 7); postOp = Opcode.POSTOP_CHECKUNDEF;	break;
					
				case Opcode.OP_LOADLOC8:
					if(stack[8] != null){ stack[sp++] = stack[8]; continue NEXT_INSTRUCTION; }
					last_var_name = findLocalName(cf, 8); postOp = Opcode.POSTOP_CHECKUNDEF;	break;
					
				case Opcode.OP_LOADLOC9:
					if(stack[9] != null){ stack[sp++] = stack[9]; continue NEXT_INSTRUCTION; }
					last_var_name = findLocalName(cf, 9); postOp = Opcode.POSTOP_CHECKUNDEF;	break;
				
				case Opcode.OP_LOADLOC:
					pos = CodeBlock.fetchArg1(instruction);
					Object rval = stack[pos];
					if(rval != null){
						stack[sp++] = rval;
						continue NEXT_INSTRUCTION;
					}
					last_var_name = findLocalName(cf, pos);
					postOp = Opcode.POSTOP_CHECKUNDEF;	
					break;
					
				case Opcode.OP_LOADBOOL:
					stack[sp++] = CodeBlock.fetchArg1(instruction) == 1 ? Rascal_TRUE : Rascal_FALSE;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADINT:
					stack[sp++] = CodeBlock.fetchArg1(instruction);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADCON:
					stack[sp++] = cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOCREF:
					stack[sp++] = new Reference(stack, CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_CALLMUPRIM:				
					sp = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].execute(stack, sp, CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_JMP:
					pc = CodeBlock.fetchArg1(instruction);
					continue NEXT_INSTRUCTION;

				case Opcode.OP_JMPTRUE:
					if (((IBool) stack[sp - 1]).getValue()) {
						pc = CodeBlock.fetchArg1(instruction);
					}
					sp--;
					continue NEXT_INSTRUCTION;

				case Opcode.OP_JMPFALSE:
					if (!((IBool) stack[sp - 1]).getValue()) {
						pc = CodeBlock.fetchArg1(instruction);
					}
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_TYPESWITCH:
					IValue val = (IValue) stack[--sp];
					Type t = null;
					if(val instanceof IConstructor) {
						t = ((IConstructor) val).getConstructorType();
					} else {
						t = val.getType();
					}
					int labelIndex = ToplevelType.getToplevelTypeAsInt(t);
					IList labels = (IList) cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					pc = ((IInteger) labels.get(labelIndex)).intValue();
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_JMPINDEXED:
					labelIndex = ((IInteger) stack[--sp]).intValue();
					labels = (IList) cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					pc = ((IInteger) labels.get(labelIndex)).intValue();
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADTYPE:
					stack[sp++] = cf.function.typeConstantStore[CodeBlock.fetchArg1(instruction)];
					continue NEXT_INSTRUCTION;
					 
				case Opcode.OP_LOADLOCDEREF: {
					Reference ref = (Reference) stack[CodeBlock.fetchArg1(instruction)];
					stack[sp++] = ref.stack[ref.pos];
					continue NEXT_INSTRUCTION;
				}
				
				case Opcode.OP_STORELOC:
				case Opcode.OP_UNWRAPTHROWNLOC: {
					stack[CodeBlock.fetchArg1(instruction)] = (op == Opcode.OP_STORELOC) ? stack[sp - 1] : ((Thrown) stack[--sp]).value;
					continue NEXT_INSTRUCTION;
				}
				
				case Opcode.OP_STORELOCDEREF:
					Reference ref = (Reference) stack[CodeBlock.fetchArg1(instruction)];
					ref.stack[ref.pos] = stack[sp - 1]; // TODO: We need to re-consider how to guarantee safe use of both Java objects and IValues    
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_LOADFUN:
					// Loads functions that are defined at the root
					stack[sp++] = new FunctionInstance(functionStore.get(CodeBlock.fetchArg1(instruction)), root, this);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOAD_NESTED_FUN: { 
					// Loads nested functions and closures (anonymous nested functions)
					stack[sp++] = FunctionInstance.computeFunctionInstance(functionStore.get(CodeBlock.fetchArg1(instruction)), cf, CodeBlock.fetchArg2(instruction), this);
					continue NEXT_INSTRUCTION;
				}
				
				case Opcode.OP_LOADOFUN:
					OverloadedFunction of = overloadedStore.get(CodeBlock.fetchArg1(instruction));
					stack[sp++] = of.scopeIn == -1 ? new OverloadedFunctionInstance(of.functions, of.constructors, root, functionStore, constructorStore, this)
					                               : OverloadedFunctionInstance.computeOverloadedFunctionInstance(of.functions, of.constructors, cf, of.scopeIn, functionStore, constructorStore, this);
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_LOADCONSTR:
					Type constructor = constructorStore.get(CodeBlock.fetchArg1(instruction));  
					stack[sp++] = constructor;
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_LOADVAR:
				case Opcode.OP_LOADVARREF: {
					varScope = CodeBlock.fetchArg1(instruction);
					pos = CodeBlock.fetchArg2(instruction);
					
					if(CodeBlock.isMaxArg2(pos)){
						rval = moduleVariables.get(cf.function.constantStore[varScope]);
						if(op == Opcode.OP_LOADVAR && rval == null) {
							last_var_name = ((IString) cf.function.constantStore[varScope]).getValue();
							postOp = Opcode.POSTOP_CHECKUNDEF; break INSTRUCTION;
						}					
						stack[sp++] = rval;
						continue NEXT_INSTRUCTION;
					}
					
					for (Frame fr = cf; fr != null; fr = fr.previousScope) {
						if (fr.scopeId == varScope) {
							rval = (op == Opcode.OP_LOADVAR) ? fr.stack[pos] : new Reference(fr.stack, pos);
							if(op == Opcode.OP_LOADVAR && rval == null) {
								last_var_name = findVarName(fr, varScope, pos);
								postOp = Opcode.POSTOP_CHECKUNDEF; break INSTRUCTION;
							}						
							stack[sp++] = rval;
							continue NEXT_INSTRUCTION;
						}
					}
					throw new CompilerError("LOADVAR or LOADVARREF cannot find matching scope: " + varScope);
				}
				
				case Opcode.OP_LOADVARDEREF: {
					int s = CodeBlock.fetchArg1(instruction);
					pos = CodeBlock.fetchArg2(instruction);					
					
					for (Frame fr = cf; fr != null; fr = fr.previousScope) {
						if (fr.scopeId == s) {
							ref = (Reference) fr.stack[pos];
							stack[sp++] = ref.stack[ref.pos];
							continue NEXT_INSTRUCTION;
						}
					}
					throw new CompilerError("LOADVARDEREF cannot find matching scope: " + s);
				}
				
				case Opcode.OP_STOREVAR:
				case Opcode.OP_UNWRAPTHROWNVAR:
					int s = CodeBlock.fetchArg1(instruction);
					pos = CodeBlock.fetchArg2(instruction);
					
					if(CodeBlock.isMaxArg2(pos)){
						IValue mvar = cf.function.constantStore[s];
						moduleVariables.put(mvar, (IValue)stack[sp -1]);
						continue NEXT_INSTRUCTION;
					}

					for (Frame fr = cf; fr != null; fr = fr.previousScope) {
						if (fr.scopeId == s) {
							// TODO: We need to re-consider how to guarantee safe use of both Java objects and IValues
							fr.stack[pos] = (op == Opcode.OP_STOREVAR) ? stack[sp - 1] : ((Thrown) stack[--sp]).value;
							continue NEXT_INSTRUCTION;
						}
					}

					throw new CompilerError(((op == Opcode.OP_STOREVAR) ? "STOREVAR" : "UNWRAPTHROWNVAR") + " cannot find matching scope: " + s);
				
				case Opcode.OP_STOREVARDEREF:
					s = CodeBlock.fetchArg1(instruction);
					pos = CodeBlock.fetchArg2(instruction);

					for (Frame fr = cf; fr != null; fr = fr.previousScope) {
						if (fr.scopeId == s) {
							ref = (Reference) fr.stack[pos];
							ref.stack[ref.pos] = stack[sp - 1];
							continue NEXT_INSTRUCTION;
						}
					}

					throw new CompilerError("STOREVARDEREF cannot find matching scope: " + s);
				
				case Opcode.OP_CALLCONSTR:
					constructor = constructorStore.get(CodeBlock.fetchArg1(instruction));
					arity = CodeBlock.fetchArg2(instruction);
					//cf.src = (ISourceLocation) cf.function.constantStore[instructions[pc++]];
					
					IValue[] args = null;
					if(arity == constructor.getArity()) {
						args = new IValue[arity];
					} else {
						// Constructors with keyword parameters
						Type type = (Type) stack[--sp];
						IMap kwargs = (IMap) stack[--sp];
						Object[] types = new Object[2*constructor.getArity() + 2*kwargs.size()];
						int j = 0;
						for(int i = 0; i < constructor.getArity(); i++) {
							types[j++] = constructor.getFieldType(i);
							types[j++] = constructor.getFieldName(i);
						}
						args = new IValue[constructor.getArity() + kwargs.size()];
						for(int i = 0; i < type.getArity(); i++) {
							types[j++] = type.getFieldType(i);
							types[j++] = type.getFieldName(i);
							args[constructor.getArity() + i] = kwargs.get(vf.string(type.getFieldName(i)));
						}
						constructor = tf.constructorFromTuple(typeStore, constructor.getAbstractDataType(), constructor.getName(), tf.tupleType(types), constructor.getArity());
					}
					for(int i = 0; i < constructor.getPositionalArity(); i++) {
						args[constructor.getPositionalArity() - 1 - i] = (IValue) stack[--sp];
					}
					stack[sp++] = vf.constructor(constructor, args);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLDYN:				
				case Opcode.OP_CALL:
					
					// In case of CALLDYN, the stack top value of type 'Type' leads to a constructor call
					if(op == Opcode.OP_CALLDYN && stack[sp - 1] instanceof Type) {
						Type constr = (Type) stack[--sp];
						arity = constr.getArity();
						args = new IValue[arity]; 
						for(int i = arity - 1; i >= 0; i--) {
							args[i] = (IValue) stack[sp - arity + i];
						}
						sp = sp - arity;
						stack[sp++] = vf.constructor(constr, args);
						continue NEXT_INSTRUCTION;
					}
					
					// Specific to delimited continuations (experimental)
					if(op == Opcode.OP_CALLDYN && stack[sp - 1] instanceof Coroutine) {
						arity = CodeBlock.fetchArg1(instruction);
						Coroutine coroutine = (Coroutine) stack[--sp];
						// Merged the hasNext and next semantics
						activeCoroutines.push(coroutine);
						ccf = coroutine.start;
						coroutine.next(cf);
						instructions = coroutine.frame.function.codeblock.getInstructions();
						coroutine.frame.stack[coroutine.frame.sp++] = arity == 1 ? stack[--sp] : null;
						cf.pc = pc;
						cf.sp = sp;
						cf = coroutine.frame;
						stack = cf.stack;
						sp = cf.sp;
						pc = cf.pc;
						continue NEXT_INSTRUCTION;
					}
					
					cf.pc = pc;
					if(op == Opcode.OP_CALLDYN && stack[sp - 1] instanceof FunctionInstance){
						FunctionInstance fun_instance = (FunctionInstance) stack[--sp];
						arity = CodeBlock.fetchArg1(instruction);
						// In case of partial parameter binding
						if(fun_instance.next + arity < fun_instance.function.nformals) {
							fun_instance = fun_instance.applyPartial(arity, stack, sp);
							sp = sp - arity;
						    stack[sp++] = fun_instance;
						    continue NEXT_INSTRUCTION;
						}
						cf = cf.getFrame(fun_instance.function, fun_instance.env, fun_instance.args, arity, sp);
					} else if(op == Opcode.OP_CALL) {
						Function fun = functionStore.get(CodeBlock.fetchArg1(instruction));
						arity = CodeBlock.fetchArg2(instruction);
						// In case of partial parameter binding
						if(arity < fun.nformals) {
							FunctionInstance fun_instance = FunctionInstance.applyPartial(fun, root, this, arity, stack, sp);
							sp = sp - arity;
						    stack[sp++] = fun_instance;
						    continue NEXT_INSTRUCTION;
						}
						cf = cf.getFrame(fun, root, arity, sp);
					} else {
						throw new CompilerError("Unexpected argument type for CALLDYN: " + asString(stack[sp - 1]));
					}
					
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_OCALLDYN:
				case Opcode.OP_OCALL:					
					Object funcObject = (op == Opcode.OP_OCALLDYN) ? stack[--sp] : null;
					// Get function arguments from the stack
					arity = CodeBlock.fetchArg2(instruction);
					
					cf.src = (ISourceLocation) cf.function.constantStore[instructions[pc++]];
					cf.sp = sp;
					cf.pc = pc;
					
					OverloadedFunctionInstanceCall c_ofun_call_next = null;
					
					if(op == Opcode.OP_OCALLDYN) {
						// Get function types to perform a type-based dynamic resolution
						Type types = cf.function.codeblock.getConstantType(CodeBlock.fetchArg1(instruction));
						// Objects of three types may appear on the stack:
						// 	1. FunctionInstance due to closures
						if(funcObject instanceof FunctionInstance) {
							FunctionInstance fun_instance = (FunctionInstance) funcObject;
							cf = cf.getFrame(fun_instance.function, fun_instance.env, arity, sp);
							instructions = cf.function.codeblock.getInstructions();
							stack = cf.stack;
							sp = cf.sp;
							pc = cf.pc;
							continue NEXT_INSTRUCTION;
						}
					 	// 2. OverloadedFunctionInstance due to named Rascal functions
						OverloadedFunctionInstance of_instance = (OverloadedFunctionInstance) funcObject;
						c_ofun_call_next = new OverloadedFunctionInstanceCall(cf, of_instance.functions, of_instance.constructors, of_instance.env, types, arity);
					} else {
						of = overloadedStore.get(CodeBlock.fetchArg1(instruction));
						c_ofun_call_next = of.scopeIn == -1 ? new OverloadedFunctionInstanceCall(cf, of.functions, of.constructors, root, null, arity)
								                            : OverloadedFunctionInstanceCall.computeOverloadedFunctionInstanceCall(cf, of.functions, of.constructors, of.scopeIn, null, arity);
					}
					
					if(debug) {
						if(op == Opcode.OP_OCALL) {
							this.appendToTrace("OVERLOADED FUNCTION CALL: " + getOverloadedFunctionName(CodeBlock.fetchArg1(instruction)));
						} else {
							this.appendToTrace("OVERLOADED FUNCTION CALLDYN: ");
						}
						this.appendToTrace("	with alternatives:");
						for(int index : c_ofun_call_next.functions) {
							this.appendToTrace("		" + getFunctionName(index));
						}
					}
					
					Frame frame = c_ofun_call_next.nextFrame(functionStore);
					
					if(frame != null) {
						c_ofun_call = c_ofun_call_next;
						ocalls.push(c_ofun_call);
						if(debug) {
							this.appendToTrace("		" + "try alternative: " + frame.function.name);
						}
						cf = frame;
						instructions = cf.function.codeblock.getInstructions();
						stack = cf.stack;
						sp = cf.sp;
						pc = cf.pc;
					} else {
						constructor = c_ofun_call_next.nextConstructor(constructorStore);
						sp = sp - arity;
						stack[sp++] = vf.constructor(constructor, c_ofun_call_next.getConstructorArguments(constructor.getArity()));
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_FAILRETURN:
					assert cf.previousCallFrame == c_ofun_call.cf;
					
					frame = c_ofun_call.nextFrame(functionStore);				
					if(frame != null) {						
						if(debug) {
							this.appendToTrace("		" + "try alternative: " + frame.function.name);
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
						constructor = c_ofun_call.nextConstructor(constructorStore);
						stack[sp++] = vf.constructor(constructor, c_ofun_call.getConstructorArguments(constructor.getArity()));
						ocalls.pop();
						c_ofun_call = ocalls.isEmpty() ? null : ocalls.peek();
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_FILTERRETURN:
				case Opcode.OP_RETURN0:
				case Opcode.OP_RETURN1:
					
					// Overloading specific
					if(c_ofun_call != null && cf.previousCallFrame == c_ofun_call.cf) {
						ocalls.pop();
						c_ofun_call = ocalls.isEmpty() ? null : ocalls.peek();
					}
				
					rval = null;
					boolean returns = cf.isCoroutine || op == Opcode.OP_RETURN1 || op == Opcode.OP_FILTERRETURN;
					if(op == Opcode.OP_RETURN1 || cf.isCoroutine) {
						if(cf.isCoroutine) {
							rval = Rascal_TRUE;
							if(op == Opcode.OP_RETURN1) {
								arity = CodeBlock.fetchArg1(instruction);
								int[] refs = cf.function.refs;
								if(arity != refs.length) {
									throw new CompilerError("Coroutine " + cf.function.name + ": arity of return (" + arity  + ") unequal to number of reference parameters (" +  refs.length + ")");
								}
								for(int i = 0; i < arity; i++) {
									ref = (Reference) stack[refs[arity - 1 - i]];
									ref.stack[ref.pos] = stack[--sp];
								}
							}
						} else {
							rval = stack[sp - 1];
						}
					}
					
					// if the current frame is the frame of a top active coroutine, 
					// then pop this coroutine from the stack of active coroutines
					if(cf == ccf) {
						activeCoroutines.pop();
						ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
					}
					
					cf = cf.previousCallFrame;
					if(cf == null) {
						if(returns) {
							return rval; 
						} else { 
							return NONE;
						}
					}
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					if(returns) {
						stack[sp++] = rval;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLJAVA:
					String methodName =  ((IString) cf.function.constantStore[instructions[pc++]]).getValue();
					String className =  ((IString) cf.function.constantStore[instructions[pc++]]).getValue();
					Type parameterTypes = cf.function.typeConstantStore[instructions[pc++]];
					int reflect = instructions[pc++];
					arity = parameterTypes.getArity();
					try {
					    sp = callJavaMethod(methodName, className, parameterTypes, reflect, stack, sp);
					} catch(Throw e) {
						stacktrace.add(cf);
						thrown = Thrown.getInstance(e.getException(), e.getLocation(), stacktrace);
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; break INSTRUCTION;
					}
					
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_CREATE:
				case Opcode.OP_CREATEDYN:
					if(op == Opcode.OP_CREATE) {
						cccf = cf.getCoroutineFrame(functionStore.get(CodeBlock.fetchArg1(instruction)), root, CodeBlock.fetchArg2(instruction), sp);
					} else {
						arity = CodeBlock.fetchArg1(instruction);
						Object src = stack[--sp];
						if(src instanceof FunctionInstance) {
							// In case of partial parameter binding
							FunctionInstance fun_instance = (FunctionInstance) src;
							cccf = cf.getCoroutineFrame(fun_instance, arity, sp);
						} else {
							throw new CompilerError("Unexpected argument type for INIT: " + src.getClass() + ", " + src);
						}
					}
					sp = cf.sp;
					// Instead of suspending a coroutine instance during INIT, execute it until GUARD;
					// Let INIT postpone creation of an actual coroutine instance (delegated to GUARD), which also implies no stack management of active coroutines until GUARD;
					cccf.previousCallFrame = cf;
					
					cf.sp = sp;
					cf.pc = pc;
					instructions = cccf.function.codeblock.getInstructions();
					cf = cccf;
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_GUARD:
					rval = stack[sp - 1];
					boolean precondition;
					if(rval instanceof IBool) {
						precondition = ((IBool) rval).getValue();
//					} else if(rval instanceof Boolean) {
//						precondition = (Boolean) rval;
					} else {
						throw new CompilerError("Guard's expression has to be boolean!");
					}
					
					if(cf == cccf) {
						Coroutine coroutine = null;
						Frame prev = cf.previousCallFrame;
						if(precondition) {
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
						continue NEXT_INSTRUCTION;
					}
					
					if(!precondition) {
						cf.pc = pc;
						cf.sp = sp;
						cf = cf.previousCallFrame;
						instructions = cf.function.codeblock.getInstructions();
						stack = cf.stack;
						sp = cf.sp;
						pc = cf.pc;
						stack[sp++] = Rascal_FALSE;
						continue NEXT_INSTRUCTION;
					}
					
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_APPLY:
				case Opcode.OP_APPLYDYN:
					FunctionInstance fun_instance;
					if(op == Opcode.OP_APPLY) {
						Function fun = functionStore.get(CodeBlock.fetchArg1(instruction));
						arity = CodeBlock.fetchArg2(instruction);
						assert arity <= fun.nformals;
						assert fun.scopeIn == -1;
						fun_instance = FunctionInstance.applyPartial(fun, root, this, arity, stack, sp);
					} else {
						Object src = stack[--sp];
						if(src instanceof FunctionInstance) {
							fun_instance = (FunctionInstance) src;
							arity = CodeBlock.fetchArg1(instruction);
							assert arity + fun_instance.next <= fun_instance.function.nformals;
							fun_instance = fun_instance.applyPartial(arity, stack, sp);
						} else {
							throw new CompilerError("Unexpected argument type for APPLYDYN: " + asString(src));
						}
					}
					sp = sp - arity;
					stack[sp++] = fun_instance;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_NEXT0:
				case Opcode.OP_NEXT1:
					Coroutine coroutine = (Coroutine) stack[--sp];
					
					// Merged the hasNext and next semantics
					if(!coroutine.hasNext()) {
						if(op == Opcode.OP_NEXT1) {
							--sp;
						}
						stack[sp++] = Rascal_FALSE;
						continue NEXT_INSTRUCTION;
					}
					// put the coroutine onto the stack of active coroutines
					activeCoroutines.push(coroutine);
					ccf = coroutine.start;
					coroutine.next(cf);
					
					instructions = coroutine.frame.function.codeblock.getInstructions();
				
					coroutine.frame.stack[coroutine.frame.sp++] = 		// Always leave an entry on the stack
							(op == Opcode.OP_NEXT1) ? stack[--sp] : null;
					
					cf.pc = pc;
					cf.sp = sp;
					
					cf = coroutine.frame;
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_YIELD0:	
				case Opcode.OP_YIELD1:
					coroutine = activeCoroutines.pop();
					ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
					Frame prev = coroutine.start.previousCallFrame;
					rval = Rascal_TRUE; // In fact, yield has to always return TRUE
					if(op == Opcode.OP_YIELD1) {
						arity = CodeBlock.fetchArg1(instruction);
						int[] refs = cf.function.refs; 
						
						if(arity != refs.length) {
							throw new CompilerError("The 'yield' within a coroutine has to take the same number of arguments as the number of its reference parameters; arity: " + arity + "; reference parameter number: " + refs.length);
						}
						
						for(int i = 0; i < arity; i++) {
							ref = (Reference) stack[refs[arity - 1 - i]]; // Takes the reference parameters of the top active coroutine instance
							ref.stack[ref.pos] = stack[--sp];
						}
					}
					cf.pc = pc;
					cf.sp = sp;
					coroutine.suspend(cf);
					cf = prev;
					if(op == Opcode.OP_YIELD1 && cf == null) {
						return rval;
					}
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					stack[sp++] = rval;	 								// Corresponding next will always find an entry on the stack
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_EXHAUST:
					if(cf == ccf) {
						activeCoroutines.pop();
						ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
					}
					
					cf = cf.previousCallFrame;
					if(cf == null) {
						return Rascal_FALSE;    // 'Exhaust' has to always return FALSE, i.e., signal a failure;
					}
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					stack[sp++] = Rascal_FALSE; // 'Exhaust' has to always return FALSE, i.e., signal a failure;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLPRIM:
					arity = CodeBlock.fetchArg2(instruction);
					cf.src = (ISourceLocation) cf.function.constantStore[instructions[pc++]];
					try {
						sp = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].execute(stack, sp, arity, stacktrace);
					} catch(Exception exception) {
						if(!(exception instanceof Thrown)){
							throw exception;
						}
						thrown = (Thrown) exception;
						thrown.stacktrace.add(cf);
						sp = sp - arity;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; break INSTRUCTION;
					}
					
					continue NEXT_INSTRUCTION;
					
				// Some specialized MuPrimitives
					
				case Opcode.OP_SUBSCRIPTARRAY:
					stack[sp - 2] = ((Object[]) stack[sp - 2])[((Integer) stack[sp - 1])];
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_SUBSCRIPTLIST:
					stack[sp - 2] = ((IList) stack[sp - 2]).get((Integer) stack[sp - 1]);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LESSINT:
					stack[sp - 2] = vf.bool(((Integer) stack[sp - 2]) < ((Integer) stack[sp - 1]));
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_GREATEREQUALINT:
					stack[sp - 2] = vf.bool(((Integer) stack[sp - 2]) >= ((Integer) stack[sp - 1]));
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_ADDINT:
					stack[sp - 2] = ((Integer) stack[sp - 2]) + ((Integer) stack[sp - 1]);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_SUBTRACTINT:
					stack[sp - 2] = ((Integer) stack[sp - 2]) - ((Integer) stack[sp - 1]);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_ANDBOOL:
					stack[sp - 2] = ((IBool) stack[sp - 2]).and((IBool) stack[sp - 1]);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_TYPEOF:
					if(stack[sp - 1] instanceof HashSet<?>){	// For the benefit of set matching
						@SuppressWarnings("unchecked")
						HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
						if(mset.isEmpty()){
							stack[sp - 1] = tf.setType(tf.voidType());
						} else {
							IValue v = mset.iterator().next();
							stack[sp - 1] = tf.setType(v.getType());
						}
					} else {
						stack[sp - 1] = ((IValue) stack[sp - 1]).getType();
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_SUBTYPE:
					stack[sp - 2] = vf.bool(((Type) stack[sp - 2]).isSubtypeOf((Type) stack[sp - 1]));
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CHECKARGTYPE:
					Type argType =  ((IValue) stack[sp - 2]).getType();
					Type paramType = ((Type) stack[sp - 1]);
					stack[sp - 2] = vf.bool(argType.isSubtypeOf(paramType));
					sp--;
					continue NEXT_INSTRUCTION;
								
				case Opcode.OP_LABEL:
					throw new CompilerError("LABEL instruction at runtime");
					
				case Opcode.OP_HALT:
					if (debug) {
						stdout.println("Program halted:");
						for (int i = 0; i < sp; i++) {
							stdout.println(i + ": " + stack[i]);
						}
					}
					return stack[sp - 1];

				case Opcode.OP_PRINTLN:
					arity = CodeBlock.fetchArg1(instruction);
					StringBuilder w = new StringBuilder();
					for(int i = arity - 1; i >= 0; i--){
						String str = (stack[sp - 1 - i] instanceof IString) ? ((IString) stack[sp - 1 - i]).toString() : asString(stack[sp - 1 - i]);
						w.append(str).append(" ");
					}
					stdout.println(w.toString());
					sp = sp - arity + 1;
					continue NEXT_INSTRUCTION;	
					
				case Opcode.OP_THROW:
					Object obj = stack[--sp];
					thrown = null;
					cf.src = (ISourceLocation) cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					if(obj instanceof IValue) {
						stacktrace = new ArrayList<Frame>();
						stacktrace.add(cf);
						thrown = Thrown.getInstance((IValue) obj, null, stacktrace);
					} else {
						// Then, an object of type 'Thrown' is on top of the stack
						thrown = (Thrown) obj;
					}
					postOp = Opcode.POSTOP_HANDLEEXCEPTION; break INSTRUCTION;
					
				case Opcode.OP_LOADLOCKWP:
					IString name = (IString) cf.function.codeblock.getConstantValue(CodeBlock.fetchArg1(instruction));
					@SuppressWarnings("unchecked")
					Map<String, Map.Entry<Type, IValue>> defaults = (Map<String, Map.Entry<Type, IValue>>) stack[cf.function.nformals];
					Map.Entry<Type, IValue> defaultValue = defaults.get(name.getValue());
					for(Frame f = cf; f != null; f = f.previousCallFrame) {
						IMap kargs = (IMap) f.stack[f.function.nformals - 1];
						if(kargs.containsKey(name)) {
							val = kargs.get(name);
							if(val.getType().isSubtypeOf(defaultValue.getKey())) {
								stack[sp++] = val;
								continue NEXT_INSTRUCTION;
							}
						}
					}				
					stack[sp++] = defaultValue.getValue();
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADVARKWP:
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_STORELOCKWP:
					val = (IValue) stack[sp - 1];
					name = (IString) cf.function.codeblock.getConstantValue(CodeBlock.fetchArg1(instruction));
					IMap kargs = (IMap) stack[cf.function.nformals - 1];
					stack[cf.function.nformals - 1] = kargs.put(name, val);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_STOREVARKWP:
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADCONT:
					s = CodeBlock.fetchArg1(instruction);
					assert stack[0] instanceof Coroutine;
					for(Frame fr = cf; fr != null; fr = fr.previousScope) {
						if (fr.scopeId == s) {
							// TODO: unsafe in general case (the coroutine object should be copied)
							stack[sp++] = fr.stack[0];
							continue NEXT_INSTRUCTION;
						}
					}
					throw new CompilerError("LOADCONT cannot find matching scope: " + s);
				
				case Opcode.OP_RESET:
					fun_instance = (FunctionInstance) stack[--sp]; // A fucntion of zero arguments
					cf.pc = pc;
					cf = cf.getCoroutineFrame(fun_instance, 0, sp);
					activeCoroutines.push(new Coroutine(cf));
					ccf = cf;
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_SHIFT:
					fun_instance = (FunctionInstance) stack[--sp]; // A function of one argument (continuation)
					coroutine = activeCoroutines.pop();
					ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
					cf.pc = pc;
					cf.sp = sp;
					prev = coroutine.start.previousCallFrame;
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
					continue NEXT_INSTRUCTION;
								
				default:
					throw new CompilerError("RVM main loop -- cannot decode instruction");
				}
				
				switch(postOp){
				
				case Opcode.POSTOP_CHECKUNDEF:
				case Opcode.POSTOP_HANDLEEXCEPTION:
					// EXCEPTION HANDLING
					if(postOp == Opcode.POSTOP_CHECKUNDEF) {
						stacktrace = new ArrayList<Frame>();
						stacktrace.add(cf);
						thrown = RascalRuntimeException.uninitializedVariable(last_var_name, stacktrace);
					}
					cf.pc = pc;
					// First, try to find a handler in the current frame function,
					// given the current instruction index and the value type,
					// then, if not found, look up the caller function(s)
					for(Frame f = cf; f != null; f = f.previousCallFrame) {
						int handler = f.function.getHandler(f.pc - 1, thrown.value.getType());
						if(handler != -1) {
							if(f != cf) {
								cf = f;
								instructions = cf.function.codeblock.getInstructions();
								stack = cf.stack;
								sp = cf.sp;
								pc = cf.pc;
							}
							pc = handler;
							stack[sp++] = thrown;
							thrown = null;
							continue NEXT_INSTRUCTION;
						}
						if(c_ofun_call != null && f.previousCallFrame == c_ofun_call.cf) {
							ocalls.pop();
							c_ofun_call = ocalls.isEmpty() ? null : ocalls.peek();
						}
					}
					// If a handler has not been found in the caller functions...
					return thrown;
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace(stderr);
			throw new CompilerError("Instruction execution: instruction: " + cf.function.codeblock.toString(pc - 1) + "; message: "+ e.getMessage() + e.getCause() );
			//stdout.println("PANIC: (instruction execution): " + e.getMessage());
			//e.printStackTrace();
			//stderr.println(e.getStackTrace());
		}
	}
	
	int callJavaMethod(String methodName, String className, Type parameterTypes, int reflect, Object[] stack, int sp) throws Throw {
		Class<?> clazz = null;
		try {
			try {
				clazz = this.getClass().getClassLoader().loadClass(className);
			} catch(ClassNotFoundException e1) {
				// If the class is not found, try other class loaders
				for(ClassLoader loader : this.classLoaders) {
					//for(ClassLoader loader : ctx.getEvaluator().getClassLoaders()) {
					try {
						clazz = loader.loadClass(className);
						break;
					} catch(ClassNotFoundException e2) {
						;
					}
				}
			}
			
			if(clazz == null) {
				throw new CompilerError("Class not found: " + className);
			}
			
			Constructor<?> cons;
			cons = clazz.getConstructor(IValueFactory.class);
			Object instance = cons.newInstance(vf);
			Method m = clazz.getMethod(methodName, makeJavaTypes(parameterTypes, reflect));
			int nformals = parameterTypes.getArity();
			Object[] parameters = new Object[nformals + reflect];
			for(int i = 0; i < nformals; i++){
				parameters[i] = stack[sp - nformals + i];
			}
			if(reflect == 1) {
				parameters[nformals] = this.getEvaluatorContext();
			}
			stack[sp - nformals] =  m.invoke(instance, parameters);
			return sp - nformals + 1;
		} 
//		catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
			if(e.getTargetException() instanceof Throw) {
				throw (Throw) e.getTargetException();
			}
			e.printStackTrace();
		}
		return sp;
	}
	
	Class<?>[] makeJavaTypes(Type parameterTypes, int reflect){
		JavaClasses javaClasses = new JavaClasses();
		int arity = parameterTypes.getArity() + reflect;
		Class<?>[] jtypes = new Class<?>[arity];
		
		for(int i = 0; i < parameterTypes.getArity(); i++){
			jtypes[i] = parameterTypes.getFieldType(i).accept(javaClasses);
		}
		if(reflect == 1) {
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
		public Class<?> visitExternal(
				org.eclipse.imp.pdb.facts.type.Type externalType) {
			return IValue.class;
		}

		@Override
		public Class<?> visitDateTime(Type type) {
			return IDateTime.class;
		}
	}
}
