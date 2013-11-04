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
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Opcode;


public class RVM {

	public final IValueFactory vf;
	private final Boolean TRUE;
	private final Boolean FALSE;
	private final IBool Rascal_TRUE;
	private final IBool Rascal_FALSE;
	private final IString NONE; 
	private final Failure FAILURE = Failure.getInstance();
	
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
	private IMap grammars;
	
	
	private final Map<IValue, IValue> moduleVariables;
	PrintWriter stdout;
	PrintWriter stderr;
	
	// Management of active coroutines
	Stack<Coroutine> activeCoroutines = new Stack<>();
	Frame ccf = null; // the start frame (of the coroutine's main function) of the current active coroutine
	IEvaluatorContext ctx;


	public RVM(IValueFactory vf, IEvaluatorContext ctx, boolean debug) {
		super();

		this.vf = vf;
		
		this.ctx = ctx;
		this.stdout = ctx.getStdOut();
		this.stderr = ctx.getStdErr();
		this.debug = debug;
		this.finalized = false;
		
		this.types = new Types(this.vf);
		
		TRUE = true;
		FALSE = false;
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
		
		MuPrimitive.init(vf);
		RascalPrimitive.init(vf, this);
	}
	
	public RVM(IValueFactory vf){
		this(vf, null, false);
	}
	
	public void declare(Function f){
		if(functionMap.get(f.getName()) != null){
			throw new RuntimeException("PANIC: Double declaration of function: " + f.getName());
		}
		functionMap.put(f.getName(), functionStore.size());
		functionStore.add(f);
	}
	
	public void declareConstructor(String name, IConstructor symbol) {
		Type constr = types.symbolToType(symbol, typeStore);
		if(constructorMap.get(name) != null) {
			throw new RuntimeException("PANIC: Double declaration of constructor: " + name);
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
				Integer index = functionMap.get(((IString) fuid).getValue());
				if(index == null){
					throw new RuntimeException("No definition for " + fuid + " in functionMap");
				}
				funs[i++] = index;
			}
			fuids = (IList) ofTuple.get(2);
			int[] constrs = new int[fuids.length()];
			i = 0;
			for(IValue fuid : fuids) {
				Integer index = constructorMap.get(((IString) fuid).getValue());
				if(index == null){
					throw new RuntimeException("No definition for " + fuid + " in constructorMap");
				}
				constrs[i++] = index;
			}
			this.overloadedStore.add(new OverloadedFunction(funs, constrs, scopeIn));
		}
	}
	
	public void setGrammars(IMap grammer){
		this.grammars = grammars;
	}
	
	public IMap getGrammars(){
		return grammars;
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
		if(result instanceof Boolean) {
			return vf.bool((Boolean) result);
		}
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
		throw new RuntimeException("PANIC: Cannot convert object back to IValue: " + result);
	}
	
	/**
	 * Represent any object that can occur on the RVM stack stack as string
	 * @param some stack object
	 * @return its string representation
	 */
	private String asString(Object o){
		if(o == null)
			return "null";
		if(o instanceof Boolean)
			return ((Boolean) o).toString() + " [Java]";
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
			return "HashSet[" + ((HashSet) o).toString() + "]";
		}
		throw new RuntimeException("PANIC: asString cannot convert: " + o);
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
		throw new RuntimeException("PANIC: undefined function index " + n);
	}

	public String getConstructorName(int n) {
		for(String cname : constructorMap.keySet()) {
			if(constructorMap.get(cname) == n) {
				return cname;
			}
		}
		throw new RuntimeException("PANIC: undefined constructor index " + n);
	}
	
	public String getOverloadedFunctionName(int n) {
		for(String ofname : resolver.keySet()) {
			if(resolver.get(ofname) == n) {
				return ofname;
			}
		}
		throw new RuntimeException("PANIC: undefined overloaded function index " + n);
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
	
	// Execute a function instance, i.e., a function in its environment
	// Note: the root frame is the environment of the non-nested functions, which enables access to the global variables
	// Note: the return type is 'Object' as this method is used to implement call to overloaded functions, its alternatives may fail
	private Object executeFunction(Frame root, FunctionInstance func, IValue[] args){
		Frame cf = new Frame(func.function.scopeId, null, func.env, func.function.maxstack, func.function);
		
		// Pass function arguments and account for the case of a variable number of parameters
		if(func.function.isVarArgs) {
			for(int i = 0; i < func.function.nformals - 1; i++) {
				cf.stack[i] = args[i];
			}
			IListWriter writer = vf.listWriter();
			for(int i = func.function.nformals - 1; i < args.length; i++) {
				writer.append(args[i]);
			}
			cf.stack[func.function.nformals - 1] = writer.done();
		} else {
			for(int i = 0; i < args.length; i++){
				cf.stack[i] = args[i]; 
			}
		}
		return executeProgram(root, cf);
	}
	
	private String trace = "";
	
	public String getTrace() {
		return trace;
	}
	
	public void appendToTrace(String trace) {
		this.trace = this.trace + trace + "\n";
	}
	
	
	public IValue executeProgram(String uid_main, IValue[] args) {
		
		finalize();
		
		Function main_function = functionStore.get(functionMap.get(uid_main));

		if (main_function == null) {
			throw new RuntimeException("PANIC: No function " + uid_main + " found");
		}
		
		if (main_function.nformals != 1) {
			throw new RuntimeException("PANIC: function " + uid_main + " should have one argument");
		}
		
		Frame root = new Frame(main_function.scopeId, null, main_function.maxstack, main_function);
		Frame cf = root;
		cf.stack[0] = vf.list(args); // pass the program argument to main_function as a IList object
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
	
	private Object executeProgram(Frame root, Frame cf) {
		Object[] stack = cf.stack;		                              		// current stack
		int sp = cf.function.nlocals;				                  	// current stack pointer
		int [] instructions = cf.function.codeblock.getInstructions(); 	// current instruction sequence
		int pc = 0;				                                      	// current program counter
				
		try {
			NEXT_INSTRUCTION: while (true) {
//				if(pc < 0 || pc >= instructions.length){
//					throw new RuntimeException(cf.function.name + " illegal pc: " + pc);
//				}
				int op = instructions[pc++];

				if (debug) {
					int startpc = pc - 1;
					for (int i = 0; i < sp; i++) {
						stdout.println("\t" + (i < cf.function.nlocals ? "*" : "") + i + ": " + asString(stack[i]));
					}
					stdout.println(cf.function.name + "[" + startpc + "] " + cf.function.codeblock.toString(startpc));
				}

				switch (op) {
				
				case Opcode.OP_JMP:
					pc = instructions[pc];
					continue;

				case Opcode.OP_JMPTRUE:
					if (stack[sp - 1].equals(TRUE) || stack[sp - 1].equals(Rascal_TRUE)) {
						pc = instructions[pc];
					} else
						pc++;
					sp--;
					continue;

				case Opcode.OP_JMPFALSE:
					if (stack[sp - 1].equals(FALSE) || stack[sp - 1].equals(Rascal_FALSE)) {
						pc = instructions[pc];
					} else
						pc++;
					sp--;
					continue;
					
				case Opcode.OP_JMPSWITCH:
					IValue val = (IValue) stack[--sp];
					Type t = null;
					if(val instanceof IConstructor) {
						t = ((IConstructor) val).getConstructorType();
					} else {
						t = val.getType();
					}
					int labelIndex = ToplevelType.getToplevelTypeAsInt(t);
					IList labels = (IList) cf.function.constantStore[instructions[pc++]];
					pc = ((IInteger) labels.get(labelIndex)).intValue();
					continue;
					
				case Opcode.OP_POP:
					sp--;
					continue;

				case Opcode.OP_LOADBOOL:
					stack[sp++] = instructions[pc++] == 1 ? true : false;
					continue;
					
				case Opcode.OP_LOADINT:
					stack[sp++] = instructions[pc++];
					continue;
					
				case Opcode.OP_LOADCON:
					stack[sp++] = cf.function.constantStore[instructions[pc++]];
					continue;
					
				case Opcode.OP_LOADTYPE:
					stack[sp++] = cf.function.typeConstantStore[instructions[pc++]];
					continue;
					
				case Opcode.OP_LOADLOC:
				case Opcode.OP_LOADLOCREF:
					int pos = instructions[pc++];
					Object rval = (op == Opcode.OP_LOADLOC) ? stack[pos] 
															: new Reference(stack, pos);
					
					if(op == Opcode.OP_LOADLOC && rval == null) {
						// EXCEPTION HANDLING
						List<Frame> stacktrace = new ArrayList<Frame>();
						stacktrace.add(cf);
						Thrown thrown = RuntimeExceptions.uninitializedVariable(pos, null, stacktrace);
						for(Frame f = cf; f != null; f = f.previousCallFrame) {
							int handler = f.function.getHandler(pc - 1, thrown.value.getType());
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
								continue NEXT_INSTRUCTION;
							}
						}
						// If a handler has not been found in the caller functions...
						return thrown;
					}
					
					stack[sp++] = rval;
					continue;
				
				case Opcode.OP_LOADLOCDEREF: {
					Reference ref = (Reference) stack[instructions[pc++]];
					stack[sp++] = ref.stack[ref.pos];
					continue;
				}
				
				case Opcode.OP_STORELOC: {
					stack[instructions[pc++]] = stack[sp - 1];
					continue;
				}
				
				case Opcode.OP_UNWRAPTHROWN: {
					stack[instructions[pc++]] = ((Thrown) stack[--sp]).value;
					continue;
				}
				
				case Opcode.OP_STORELOCDEREF:
					Reference ref = (Reference) stack[instructions[pc++]];
					ref.stack[ref.pos] = stack[sp - 1]; // TODO: We need to re-consider how to guarantee safe use of both Java objects and IValues    
					continue;
				
				case Opcode.OP_LOADFUN:
					// Loads functions that are defined at the root
					stack[sp++] = new FunctionInstance(functionStore.get(instructions[pc++]), root);
					continue;
					
				case Opcode.OP_LOAD_NESTED_FUN: { 
					// Loads nested functions and closures (anonymous nested functions):
					// First, gets the function code
					Function fun = functionStore.get(instructions[pc++]);
					int scopeIn = instructions[pc++];
					// Second, looks up the function environment frame into the stack of caller frames
					for(Frame env = cf; env != null; env = env.previousCallFrame) {
						if (env.scopeId == scopeIn) {
							stack[sp++] = new FunctionInstance(fun, env);
							continue NEXT_INSTRUCTION;
						}
					}
					throw new RuntimeException("LOAD_NESTED_FUN cannot find matching scope: " + scopeIn);	
				}
				
				case Opcode.OP_LOADOFUN:
					OverloadedFunction of = overloadedStore.get(instructions[pc++]);
					if(of.scopeIn == -1) {
						stack[sp++] = new OverloadedFunctionInstance(of.functions, of.constructors, root);
						continue;
					}
					for(Frame env = cf; env != null; env = env.previousCallFrame) {
						if (env.scopeId == of.scopeIn) {
							stack[sp++] = new OverloadedFunctionInstance(of.functions, of.constructors, env);
							continue NEXT_INSTRUCTION;
						}
					}
					throw new RuntimeException("Could not find matching scope when loading a nested overloaded function: " + of.scopeIn);
				
				case Opcode.OP_LOADCONSTR:
					Type constructor = constructorStore.get(instructions[pc++]);
				
				case Opcode.OP_LOADVAR:
				case Opcode.OP_LOADVARREF: {
					int s = instructions[pc++];
					pos = instructions[pc++];
					
					if(pos == -1){
						rval = moduleVariables.get(cf.function.constantStore[s]);
						if(op == Opcode.OP_LOADVAR && rval == null) {
							// EXCEPTION HANDLING
							List<Frame> stacktrace = new ArrayList<Frame>();
							stacktrace.add(cf);
							Thrown thrown = RuntimeExceptions.uninitializedVariable(pos, null, stacktrace);
							for(Frame f = cf; f != null; f = f.previousCallFrame) {
								int handler = f.function.getHandler(pc - 1, thrown.value.getType());
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
									continue NEXT_INSTRUCTION;
								}
							}
							// If a handler has not been found in the caller functions...
							return thrown;
						}
						
						stack[sp++] = rval;
						continue NEXT_INSTRUCTION;
					}
					
					for (Frame fr = cf; fr != null; fr = fr.previousScope) {
						if (fr.scopeId == s) {
							rval = (op == Opcode.OP_LOADVAR) ? fr.stack[pos] 
																	: new Reference(fr.stack, pos);
							if(op == Opcode.OP_LOADLOC && rval == null) {
								// EXCEPTION HANDLING
								List<Frame> stacktrace = new ArrayList<Frame>();
								stacktrace.add(cf);
								Thrown thrown = RuntimeExceptions.uninitializedVariable(pos, null, stacktrace);
								for(Frame f = cf; f != null; f = f.previousCallFrame) {
									int handler = f.function.getHandler(pc - 1, thrown.value.getType());
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
										continue NEXT_INSTRUCTION;
									}
								}
								// If a handler has not been found in the caller functions...
								return thrown;
							}
							
							stack[sp++] = rval;
							continue NEXT_INSTRUCTION;
						}
					}
					throw new RuntimeException("LOADVAR or LOADVARREF cannot find matching scope: " + s);
				}
				
				case Opcode.OP_LOADVARDEREF: {
					int s = instructions[pc++];
					pos = instructions[pc++];
					
					
					for (Frame fr = cf; fr != null; fr = fr.previousScope) {
						if (fr.scopeId == s) {
							ref = (Reference) fr.stack[pos];
							stack[sp++] = ref.stack[ref.pos];
							continue NEXT_INSTRUCTION;
						}
					}
					throw new RuntimeException("LOADVARDEREF cannot find matching scope: " + s);
				}
				
				case Opcode.OP_STOREVAR:
					int s = instructions[pc++];
					pos = instructions[pc++];
					
					if(pos == -1){
						IValue mvar = cf.function.constantStore[s];
						moduleVariables.put(mvar, (IValue)stack[sp -1]);
						continue NEXT_INSTRUCTION;
					}

					for (Frame fr = cf; fr != null; fr = fr.previousScope) {
						if (fr.scopeId == s) {
							fr.stack[pos] = stack[sp - 1];	// TODO: We need to re-consider how to guarantee safe use of both Java objects and IValues
							continue NEXT_INSTRUCTION;
						}
					}

					throw new RuntimeException("STOREVAR cannot find matching scope: " + s);
	
				case Opcode.OP_STOREVARDEREF:
					s = instructions[pc++];
					pos = instructions[pc++];

					for (Frame fr = cf; fr != null; fr = fr.previousScope) {
						if (fr.scopeId == s) {
							ref = (Reference) fr.stack[pos];
							ref.stack[ref.pos] = stack[sp - 1];	/* CHANGED: --sp to sp -1; value remains on stack */
							continue NEXT_INSTRUCTION;
						}
					}

					throw new RuntimeException("STOREVARDEREF cannot find matching scope: " + s);
				
				case Opcode.OP_CALLCONSTR:
					constructor = constructorStore.get(instructions[pc++]);
					int arity = instructions[pc++];
					assert arity == constructor.getArity();
					IValue[] args = new IValue[arity]; 
					for(int i = 0; i < arity; i++) {
						args[arity - 1 - i] = (IValue) stack[--sp];
					}
					stack[sp++] = vf.constructor(constructor, args);
					continue;
					
				case Opcode.OP_CALLDYN:				
				case Opcode.OP_CALL:
					
					// In case of CALLDYN, the stack top value of type 'Type' leads to a constructor call
					if(op == Opcode.OP_CALLDYN && stack[sp - 1] instanceof Type) {
						Type constr = (Type) stack[--sp];
						arity = constr.getArity();
						args = new IValue[arity]; 
						for(int i = arity - 1; i >=0; i--) {
							args[i] = (IValue) stack[sp - arity + i];
						}
						sp = sp - arity;
						stack[sp++] = vf.constructor(constr, args);
						continue NEXT_INSTRUCTION;
					}
					
					Function fun = null;
					Frame previousScope = null;
					
					if(op == Opcode.OP_CALLDYN && stack[sp - 1] instanceof FunctionInstance){
						FunctionInstance fun_instance = (FunctionInstance) stack[--sp];
						arity = instructions[pc++]; // TODO: add assert
						fun = fun_instance.function;
						previousScope = fun_instance.env;
					} else if(op == Opcode.OP_CALL) {
						fun = functionStore.get(instructions[pc++]);
						arity = instructions[pc++];
						assert arity == fun.nformals;
						previousScope = cf;
					} else {
						throw new RuntimeException("unexpected argument type for CALLDYN: " + asString(stack[sp - 1]));
					}
						
					instructions = fun.codeblock.getInstructions();
					
					Frame nextFrame = new Frame(fun.scopeId, cf, previousScope, fun.maxstack, fun);
					
					for (int i = fun.nformals - 1; i >= 0; i--) {
						nextFrame.stack[i] = stack[sp - fun.nformals + i];
					}
					cf.pc = pc;
					cf.sp = sp - fun.nformals;
					cf = nextFrame;
					stack = cf.stack;
					sp = fun.nlocals;
					pc = 0;
					continue;
					
				case Opcode.OP_OCALLDYN:
					// Get function types to perform a type-based dynamic resolution
					Type types = cf.function.codeblock.getConstantType(instructions[pc++]);		
					arity = instructions[pc++];
					// Objects of three types may appear on the stack:
					// 	1. FunctionInstance due to closures
					// 	2. OverloadedFunctionInstance due to named Rascal functions
					Object funcObject = stack[--sp];
					// Get function arguments from the stack
					args = new IValue[arity]; 
					for(int i = arity - 1; i >= 0; i--) {
						args[i] = (IValue) stack[sp - arity + i];
					}			
					sp = sp - arity;
					
					if(funcObject instanceof FunctionInstance) {
						FunctionInstance fun_instance = (FunctionInstance) funcObject;
						rval = executeFunction(root, fun_instance, args);
						if(rval != NONE) {
							stack[sp++] = rval;
						}
						continue NEXT_INSTRUCTION;
					}
					
					OverloadedFunctionInstance of_instance = (OverloadedFunctionInstance) funcObject;
					
					if(debug) {
						this.appendToTrace("OVERLOADED FUNCTION CALLDYN: ");
						this.appendToTrace("	with alternatives:");
						for(int index : of_instance.functions) {
							this.appendToTrace("		" + getFunctionName(index));
						}
					}
					// TODO: Re-think of the cases of polymorphic and var args function alternatives
					// The most straightforward solution would be to check the arity and let pattern matching on formal parameters do the rest
					NEXT_FUNCTION: 
					for(int index : of_instance.functions) {
						fun = functionStore.get(index);
						for(Type type : types) {
							if(type == fun.ftype) {
								FunctionInstance fun_instance = new FunctionInstance(fun, of_instance.env);
										
								if(debug) {
									this.appendToTrace("		" + "try alternative: " + getFunctionName(index));
								}
										
								rval = executeFunction(root, fun_instance, args);
								if(rval == FAILURE) {
									continue NEXT_FUNCTION;
								} else {
									if(rval != NONE) {
										stack[sp++] = rval;
									}
									continue NEXT_INSTRUCTION;
								}													
							}
						}
					}
					
					for(int index : of_instance.constructors) {
						constructor = constructorStore.get(index);
						for(Type type : types) {
							if(type == constructor) {
								
								if(debug) {
									this.appendToTrace("		" + "try constructor alternative: " + getConstructorName(index));
								}
								
								stack[sp++] = vf.constructor(constructor, args);
								continue NEXT_INSTRUCTION;													
							}
						}
					}
					throw new RuntimeException("Call to an overloded function: either all functions have failed, or some function scope has not been found!");
					
				case Opcode.OP_OCALL:					
					of = overloadedStore.get(instructions[pc++]);
					arity = instructions[pc++];
					
					if(debug) {
						this.appendToTrace("OVERLOADED FUNCTION CALL: " + getOverloadedFunctionName(instructions[pc - 2]));
						this.appendToTrace("	with alternatives:");
						for(int index : of.functions) {
							this.appendToTrace("		" + getFunctionName(index));
						}
					}
					
					// Get arguments from the stack
					args = new IValue[arity]; 
					for(int i = arity - 1; i >= 0; i--) {
						args[i] = (IValue) stack[sp - arity + i];
					}			
					sp = sp - arity;
					
					Frame environment = root;				
					if(of.scopeIn != -1) {
						boolean found = false;
						for(Frame env = cf; env != null; env = env.previousCallFrame) {
							if (env.scopeId == of.scopeIn) {
								environment = env;
								found = true;
								break;
							}
						}
						if(!found) {
							throw new RuntimeException("Could not find matching scope when loading a nested overloaded function: " + of.scopeIn);
						}
					}
					
					NEXT_FUNCTION: 
					for(int index : of.functions) {
						fun = functionStore.get(index);
						FunctionInstance fun_instance = new FunctionInstance(fun, environment);
						
						if(debug) {
							this.appendToTrace("		" + "try alternative: " + getFunctionName(index));
						}
								
						rval = executeFunction(root, fun_instance, args);
						if(rval == FAILURE) {
							continue NEXT_FUNCTION;
						} 
						else if(rval instanceof Thrown) {
							// EXCEPTION HANDLING
							Thrown thrown = (Thrown) rval;
							thrown.stacktrace.add(cf);
							// First, try to find a handler in the current frame function,
							// given the current instruction index and the value type,
							// then, if not found, look up the caller function(s)
							for(Frame f = cf; f != null; f = f.previousCallFrame) {
								int handler = f.function.getHandler(pc - 1, thrown.value.getType());
								if(handler != -1) {
									if(f != cf) {
										cf = f;
										instructions = cf.function.codeblock.getInstructions();
										stack = cf.stack;
										sp = cf.sp;
										pc = cf.pc;
									}
									pc = handler;
									// Put the thrown value on the stack
									stack[sp++] = thrown;
									continue NEXT_INSTRUCTION;
								}
							}
							// If a handler has not been found in the caller functions...
							return (Thrown) rval;
							
						} else {
							if(rval != NONE) {
								stack[sp++] = rval;
							}
							continue NEXT_INSTRUCTION;
						}							
					}
					
					int index = of.constructors[0];
					constructor = constructorStore.get(index);
					
					if(debug) {
						this.appendToTrace("		" + "try constructor alternative: " + getConstructorName(index));
					}
									
					stack[sp++] = vf.constructor(constructor, args);
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_FAILRETURN:
					rval = Failure.getInstance();
					// TODO: Need to re-consider management of active coroutines
					cf = cf.previousCallFrame;
					if(cf == null) {
						return rval;
					} else {
						throw new RuntimeException("PANIC: FAILRETURN should return from the program execution given the current design!");
					}
					
				case Opcode.OP_FILTERRETURN:
				case Opcode.OP_RETURN0:
				case Opcode.OP_RETURN1:
				
					rval = null;
					boolean returns = cf.isCoroutine || op == Opcode.OP_RETURN1 || op == Opcode.OP_FILTERRETURN;
					if(op == Opcode.OP_RETURN1 || cf.isCoroutine) {
						if(cf.isCoroutine) {
							rval = Rascal_TRUE;
							if(op == Opcode.OP_RETURN1) {
								arity = instructions[pc++];
								int[] refs = cf.function.refs;
								if(arity != refs.length) {
									throw new RuntimeException("Coroutine " + cf.function.name + ": arity of return (" + arity  + ") unequal to number of reference parameters (" +  refs.length + ")");
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
					continue;
					
				case Opcode.OP_CALLJAVA:
					String methodName =  ((IString) cf.function.constantStore[instructions[pc++]]).getValue();
					String className =  ((IString) cf.function.constantStore[instructions[pc++]]).getValue();
					Type parameterTypes = cf.function.typeConstantStore[instructions[pc++]];
					int reflect = instructions[pc++];
					arity = parameterTypes.getArity();
					sp = callJavaMethod(methodName, className, parameterTypes, reflect, stack, sp);
					continue;
					
				case Opcode.OP_INIT:
					arity = instructions[pc++];
					Object src = stack[--sp];
					Coroutine coroutine;
					if(src instanceof Coroutine){
						coroutine = (Coroutine) src; 
						fun = coroutine.frame.function;
					} else if(src instanceof FunctionInstance) {
						FunctionInstance fun_instance = (FunctionInstance) src;
						fun = fun_instance.function;
						Frame frame = new Frame(fun.scopeId, null, fun_instance.env, fun.maxstack, fun);
						coroutine = new Coroutine(frame);
					} else {
						throw new RuntimeException("Unexpected argument type for INIT: " + src.getClass() + ", " + src);
					}
					
					if(coroutine.isInitialized()) {
						throw new RuntimeException("Trying to initialize a coroutine, which has already been initialized: " + fun.getName() + " (corounine's main), called in " + cf.function.getName());
					}
					// the main function of coroutine may have formal parameters,
					// therefore, INIT may take a number of arguments == formal parameters - arguments already passed to CREATE
					if(arity != fun.nformals - coroutine.frame.sp) {
						throw new RuntimeException("Too many or too few arguments to INIT, the expected number: " + (fun.nformals - coroutine.frame.sp) + "; coroutine's main: " + fun.getName() + ", called in " + cf.function.getName());
					}
					Coroutine newCoroutine = coroutine.copy();
					for (int i = arity - 1; i >= 0; i--) {
						newCoroutine.frame.stack[coroutine.frame.sp + i] = stack[sp - arity + i];
					}
					newCoroutine.frame.sp = fun.nlocals;
					sp = sp - arity;							/* Place coroutine back on stack */
					stack[sp++] = newCoroutine;
					
					// Now, instead of simply suspending a coroutine during INIT, let it execute until GUARD, which has been delegated the INIT's suspension
					newCoroutine.suspend(newCoroutine.frame);
					// put the coroutine onto the stack of active coroutines
					activeCoroutines.push(newCoroutine);
					ccf = newCoroutine.start;
					newCoroutine.next(cf);
					
					fun = newCoroutine.frame.function;
					instructions = newCoroutine.frame.function.codeblock.getInstructions();
				
					cf.pc = pc;
					cf.sp = sp;
					
					cf = newCoroutine.frame;
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					
					continue;
					
				case Opcode.OP_GUARD:
					rval = stack[sp - 1];
					boolean precondition;
					if(rval instanceof IBool) {
						precondition = ((IBool) rval).getValue();
					} else if(rval instanceof Boolean) {
						precondition = (Boolean) rval;
					} else {
						throw new RuntimeException("Guard's expression has to be boolean!");
					}
					
					if(cf == ccf) {
						coroutine = activeCoroutines.pop();
						ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
						Frame prev = cf.previousCallFrame;
						if(precondition) {
							coroutine.suspend(cf);
						}
						--sp;
						cf.pc = pc;
						cf.sp = sp;
						cf = prev;
						instructions = cf.function.codeblock.getInstructions();
						stack = cf.stack;
						sp = cf.sp;
						pc = cf.pc;	
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
					
					continue;
					
				case Opcode.OP_CREATE:
				case Opcode.OP_CREATEDYN:
					if(op == Opcode.OP_CREATE){
						fun = functionStore.get(instructions[pc++]);
						previousScope = null;
					} else {
						src = stack[--sp];
						if(src instanceof FunctionInstance) {
							FunctionInstance fun_instance = (FunctionInstance) src;
							fun = fun_instance.function;
							previousScope = fun_instance.env;
						} else {
							throw new RuntimeException("unexpected argument type for CREATEDYN: " + asString(src));
						}
					}
					arity = instructions[pc++];
					Frame frame = new Frame(fun.scopeId, null, previousScope, fun.maxstack, fun);
					// the main function of coroutine may have formal parameters,
					// therefore, CREATE may take a number of arguments <= formal parameters
					if(arity > fun.nformals)
						throw new RuntimeException("Too many arguments to CREATE or CREATEDYN, expected <= " + fun.nformals);
					for (int i = arity - 1; i >= 0; i--) {
						frame.stack[i] = stack[sp - arity + i];
					}
					frame.sp = arity;
					coroutine = new Coroutine(frame);
					sp = sp - arity;
					stack[sp++] = coroutine;
					continue;
				
				case Opcode.OP_NEXT0:
				case Opcode.OP_NEXT1:
					coroutine = (Coroutine) stack[--sp];
					
					// Merged the hasNext and next semantics
					if(!coroutine.hasNext()) {
						if(op == Opcode.OP_NEXT1) {
							--sp;
						}
						stack[sp++] = FALSE;
						continue NEXT_INSTRUCTION;
					}
					// put the coroutine onto the stack of active coroutines
					activeCoroutines.push(coroutine);
					ccf = coroutine.start;
					coroutine.next(cf);
					
					fun = coroutine.frame.function;
					instructions = coroutine.frame.function.codeblock.getInstructions();
				
					coroutine.frame.stack[coroutine.frame.sp++] = 		// Always leave an entry on the stack
							(op == Opcode.OP_NEXT1) ? stack[--sp] : null;
					
					cf.pc = pc;
					cf.sp = sp;
					
					cf = coroutine.frame;
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					continue;
					
				case Opcode.OP_YIELD0:	
				case Opcode.OP_YIELD1:
					coroutine = activeCoroutines.pop();
					ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
					Frame prev = coroutine.start.previousCallFrame;
					rval = Rascal_TRUE; // In fact, yield has to always return TRUE
					if(op == Opcode.OP_YIELD1) {
						arity = instructions[pc++];
						int[] refs = cf.function.refs;
						if(arity != refs.length) {
							throw new RuntimeException("The return within a coroutine has to take the same number of arguments as the number of its reference parameters; arity: " + arity + "; reference parameter number: " + refs.length);
						}
						for(int i = 0; i < arity; i++) {
							ref = (Reference) stack[refs[arity - 1 - i]];
							ref.stack[ref.pos] = stack[--sp];
						}
					}
					cf.pc = pc;
					cf.sp = sp;
					coroutine.suspend(cf);
					cf = prev;
					if(op == Opcode.OP_YIELD1 && cf == null)
						return rval;
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					stack[sp++] = rval;	 								// Corresponding next will always find an entry on the stack
					continue;
					
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
					continue;
					
				case Opcode.OP_HASNEXT:
					coroutine = (Coroutine) stack[--sp];
					stack[sp++] = coroutine.hasNext() ? TRUE : FALSE;
					continue;
					
				case Opcode.OP_CALLPRIM:
					RascalPrimitive prim = RascalPrimitive.fromInteger(instructions[pc++]);
					arity = instructions[pc++];
					try {
						sp = prim.invoke(stack, sp, arity);
					} catch(InvocationTargetException targetException) {
						if(!(targetException.getTargetException() instanceof Thrown)) {
							throw targetException;
						}
						// EXCEPTION HANDLING
						Thrown thrown = (Thrown) targetException.getTargetException();
						thrown.stacktrace.add(cf);
						sp = sp - arity;
						for(Frame f = cf; f != null; f = f.previousCallFrame) {
							int handler = f.function.getHandler(pc - 1, thrown.value.getType());
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
								continue NEXT_INSTRUCTION;
							}
						}
						// If a handler has not been found in the caller functions...
						return thrown;
					}
					
					continue;
					
				case Opcode.OP_CALLMUPRIM:
					MuPrimitive muprim = MuPrimitive.fromInteger(instructions[pc++]);
					arity = instructions[pc++];
					sp = muprim.invoke(stack, sp, arity);
					continue;
								
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
					arity = instructions[pc++];
					StringBuilder w = new StringBuilder();
					for(int i = arity - 1; i >= 0; i--){
						String str = (stack[sp - 1 - i] instanceof IString) ? ((IString) stack[sp - 1 - i]).toString() : asString(stack[sp - 1 - i]);
						w.append(str).append(" ");
					}
					stdout.println(w.toString());
					sp = sp - arity + 1;
					continue;	
					
				case Opcode.OP_THROW:
					Object obj = stack[--sp];
					Thrown thrown = null;
					if(obj instanceof IValue) {
						List<Frame> stacktrace = new ArrayList<Frame>();
						stacktrace.add(cf);
						thrown = Thrown.getInstance((IValue) obj, null, stacktrace);
					} else {
						// Then, an object of type 'Thrown' is on top of the stack
						thrown = (Thrown) obj;
					}
					// First, try to find a handler in the current frame function,
					// given the current instruction index and the value type,
					// then, if not found, look up the caller function(s)
					for(Frame f = cf; f != null; f = f.previousCallFrame) {
						int handler = f.function.getHandler(pc - 1, thrown.value.getType());
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
							continue NEXT_INSTRUCTION;
						} 
					}
					// If a handler has not been found in the caller functions...
					return thrown;
								
				default:
					throw new RuntimeException("RVM main loop -- cannot decode instruction");
				}
			}
		} catch (Exception e) {
			e.printStackTrace(stderr);
			throw new RuntimeException("PANIC: (instruction execution): " + e.getMessage());
			//stdout.println("PANIC: (instruction execution): " + e.getMessage());
			//e.printStackTrace();
			//stderr.println(e.getStackTrace());
		}
	}
	
	int callJavaMethod(String methodName, String className, Type parameterTypes, int reflect, Object[] stack, int sp){
		Class<?> clazz;
		try {
			clazz = this.getClass().getClassLoader().loadClass(className);
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
				parameters[nformals] = this.ctx;
			}
			stack[sp - nformals] =  m.invoke(instance, parameters);
			return sp - nformals + 1;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			// TODO Auto-generated catch block
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
