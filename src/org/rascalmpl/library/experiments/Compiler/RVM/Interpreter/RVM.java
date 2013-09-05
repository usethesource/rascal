package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Opcode;


public class RVM {

	public final IValueFactory vf;
	private final Boolean TRUE;
	private final Boolean FALSE;
	private final IBool Rascal_TRUE;
	private final IBool Rascal_FALSE;
	
	private boolean debug = true;
	private boolean listing = false;
	private boolean finalized = false;
	
	private final ArrayList<Function> functionStore;
	private final Map<String, Integer> functionMap;
	// Resolver to manage overloading
	private final Map<String, Integer[]> resolver;
	
	//private final TypeFactory tf = TypeFactory.getInstance(); 
	private final TypeStore typeStore = new TypeStore();
	private final Types types;
	
	private final ArrayList<Type> constructorStore;
	private final Map<String, Integer> constructorMap;
	private PrintWriter stdout;

	public RVM(IValueFactory vf, PrintWriter stdout, boolean debug) {
		super();

		this.vf = vf;
		this.stdout = stdout;		
		this.debug = debug;
		this.finalized = false;
		
		this.types = new Types(this.vf);
		
		TRUE = true;
		FALSE = false;
		Rascal_TRUE = vf.bool(true);
		Rascal_FALSE = vf.bool(false);
		functionStore = new ArrayList<Function>();
		constructorStore = new ArrayList<Type>();

		functionMap = new HashMap<String, Integer>();
		resolver = new HashMap<String,Integer[]>();
		constructorMap = new HashMap<String, Integer>();
		
		RascalPrimitive.init(vf, stdout, this);
	}
	
	public RVM(IValueFactory vf){
		this(vf, new PrintWriter(System.out, true), false);
	}
	
	public void declare(Function f){
		if(functionMap.get(f.getName()) != null){
			throw new RuntimeException("PANIC: Double declaration of function: " + f.getName());
		}
		functionMap.put(f.getName(), functionStore.size());
		functionStore.add(f);
	}
	
	public void declareConstructor(IConstructor symbol) {
		Type constr = types.symbolToType(symbol, typeStore);
		constructorMap.put(constr.getName(), constructorStore.size());
		constructorStore.add(constr);
	}
	
	public Type symbolToType(IConstructor symbol) {
		return types.symbolToType(symbol, typeStore);
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
		if(result instanceof Boolean)
			return vf.bool((Boolean) result);
		if(result instanceof Integer)
			return vf.integer((Integer)result);
		if(result instanceof IValue)
			return (IValue) result;
		if(result instanceof Object[]){
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
		throw new RuntimeException("PANIC: asString cannot convert: " + o);
	}
	
	public void finalize(){
		// Finalize the instruction generation of all functions, if needed
		if(!finalized){
			finalized = true;
			for (Function f : functionStore) {
				f.finalize(functionMap, constructorMap, listing);
			}
		}
	}
	
	public IValue executeFunction(String uid_func, IValue[] args){
		Function func = functionStore.get(functionMap.get(uid_func));
		Frame root = new Frame(func.scopeId, null, func.maxstack, func);
		Frame cf = root;
		
		// Pass the program argument to main
		for(int i = 0; i < args.length; i++){
			cf.stack[i] = args[i]; 
		}
		return executeProgram(root, cf);
	}
	
	// Execute a function instance, i.e., in its environment
	public IValue executeFunction(Frame root, FunctionInstance func, IValue[] args){
		Frame cf = new Frame(func.function.scopeId, null, func.env, func.function.maxstack, func.function);
		
		// Pass the program argument to main
		for(int i = 0; i < args.length; i++){
			cf.stack[i] = args[i]; 
		}
		return executeProgram(root, cf);
	}
	
	public IValue executeProgram(String uid_main, String uid_module_init, IValue[] args) {
		
		finalize();
		
		Function main_function = functionStore.get(functionMap.get(uid_main));

		if (main_function == null) {
			throw new RuntimeException("PANIC: No function " + uid_main + " found");
		}
		
		if (main_function.nformals != 1) {
			throw new RuntimeException("PANIC: function " + uid_main + " should have one argument");
		}
			
		// Search for module initialization

		Function init_function = functionStore.get(functionMap.get(uid_module_init));

		if (init_function == null) {
			throw new RuntimeException("PANIC: Code for " + uid_module_init + " not found");
		}

		if (init_function.nformals != 1) {
			throw new RuntimeException("PANIC: " + "function " + uid_module_init + " should have one argument");
		}

		// Perform a call to #module_init" at scope level = 0

		// We need the notion of the root frame, which represents the root environment
		Frame root = new Frame(init_function.scopeId, null, init_function.maxstack, init_function);
		Frame cf = root;
		cf.stack[0] = args; // pass the program argument to #module_init 

		return executeProgram(root, cf);
	}
	
	public IValue executeProgram(Frame root, Frame cf) {
		Object[] stack = cf.stack;		                              // current stack
		int sp = cf.function.nlocals;				                  // current stacp pointer
		int[] instructions = cf.function.codeblock.getInstructions(); // current instruction sequence
		int pc = 0;				                                      // current program counter
		
		Stack<Coroutine> activeCoroutines = new Stack<>();
		Frame ccf = null; // the start frame (i.e., the frame of the coroutine's main function) of the current active coroutine
		
		try {
			NEXT_INSTRUCTION: while (true) {
				if(pc < 0 || pc >= instructions.length){
					throw new RuntimeException(cf.function.name + " illegal pc: " + pc);
				}
				int op = instructions[pc++];

				if (true) {
					int startpc = pc - 1;
					for (int i = 0; i < sp; i++) {
						//stdout.println("\t" + i + ": " + asString(stack[i]));
						System.out.println("\t" + i + ": " + asString(stack[i]));
					}
					//stdout.println(cf.function.name + "[" + startpc + "] " + cf.function.codeblock.toString(startpc));
					System.out.println(cf.function.name + "[" + startpc + "] " + cf.function.codeblock.toString(startpc));
				}


				switch (op) {

				case Opcode.OP_LOADBOOL:
					stack[sp++] = instructions[pc++] == 1 ? true : false;
					continue;
					
				case Opcode.OP_LOADINT:
					stack[sp++] = instructions[pc++];
					continue;
					
				case Opcode.OP_LOADCON:
					stack[sp++] = cf.function.constantStore[instructions[pc++]];
					continue;
					
				case Opcode.OP_LOADTTYPE:
					stack[sp++] = cf.function.typeConstantStore[instructions[pc++]];
					continue;

				case Opcode.OP_LOADFUN:
					// Loads functions that are defined at the root
					stack[sp++] = new FunctionInstance(functionStore.get(instructions[pc++]), root);
					continue;
					
				case Opcode.OP_LOAD_NESTED_FUN: { 
					// Loads nested functions and closures (anonymous nested functions):
					// First, gets the function code
					Function fun = functionStore.get(instructions[pc++]);
					int scope = instructions[pc++];
					// Second, looks up the function environment frame into the stack of caller frames
					for (Frame env = cf; env != null; env = env.previousCallFrame) {
						if (env.scopeId == scope) {
							stack[sp++] = new FunctionInstance(fun, env);
							continue NEXT_INSTRUCTION;
						}
					}
					throw new RuntimeException("LOAD_NESTED_FUNCTION cannot find matching scope: " + scope);	
				}
				
				case Opcode.OP_LOADCONSTR:
					Type constructor = constructorStore.get(instructions[pc++]);
				
				case Opcode.OP_LOADLOC:
				case Opcode.OP_LOADLOCREF:
					stack[sp++] = (op == Opcode.OP_LOADLOC) ? stack[instructions[pc++]] 
															: new Reference(stack, instructions[pc++]);
					continue;
				
				case Opcode.OP_LOADLOCDEREF: {
					Reference ref = (Reference) stack[instructions[pc++]];
					stack[sp++] = ref.stack[ref.pos];
					continue;
				}
				
				case Opcode.OP_LOADVAR:
				case Opcode.OP_LOADVARREF: {
					int s = instructions[pc++];
					int pos = instructions[pc++];
					
					for (Frame fr = cf; fr != null; fr = fr.previousScope) {
						if (fr.scopeId == s) {
							stack[sp++] = (op == Opcode.OP_LOADVAR) ? fr.stack[pos] 
																	: new Reference(fr.stack, pos);
							continue NEXT_INSTRUCTION;
						}
					}
					throw new RuntimeException("LOADVAR or LOADVARREF cannot find matching scope: " + s);
				}
				
				case Opcode.OP_LOADVARDEREF: {
					int s = instructions[pc++];
					int pos = instructions[pc++];
					for (Frame fr = cf; fr != null; fr = fr.previousScope) {
						if (fr.scopeId == s) {
							Reference ref = (Reference) fr.stack[pos];
							stack[sp++] = ref.stack[ref.pos];
							continue NEXT_INSTRUCTION;
						}
					}
					throw new RuntimeException("LOADVARDEREF cannot find matching scope: " + s);
				}
				
				case Opcode.OP_STORELOC: {
					stack[instructions[pc++]] = stack[sp - 1];
					continue;
				}
				
				case Opcode.OP_STORELOCDEREF:
					Reference ref = (Reference) stack[instructions[pc++]];
					ref.stack[ref.pos] = stack[sp - 1]; // TODO: We need to re-consider how to guarantee safe use of both Java objects and IValues    
					continue;
				
				case Opcode.OP_STOREVAR:
					int s = instructions[pc++];
					int pos = instructions[pc++];

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

				case Opcode.OP_POP:
					sp--;
					continue;
					
				case Opcode.OP_DUP:
					stack[sp] = stack[sp -1];
					sp++;
					continue;

				case Opcode.OP_LABEL:
					throw new RuntimeException("label instruction at runtime");
				
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
						throw new RuntimeException("unexpected argument type for CALLDYN: " + stack[sp - 1].getClass());
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
				
				case Opcode.OP_FAILRETURN:
					/*
					 * TODO: Now fail return acts as return without value, change when we understand what we need here,i.e.
					 * return to the function overloading resolution frame.
					 */
					
				case Opcode.OP_RETURN0:
				case Opcode.OP_RETURN1:
					Object rval = null;
					boolean returns = op == Opcode.OP_RETURN1; 
					if(returns) 
						rval = stack[sp - 1];
					
					// if the current frame is the frame of a top active coroutine, 
					// then pop this coroutine from the stack of active coroutines
					if(cf == ccf) {
						activeCoroutines.pop();
						ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
					}
					
					cf = cf.previousCallFrame;
					if(cf == null) {
						if(returns)
							return narrow(rval);
						else 
							return vf.string("None");
					}
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					if(returns)
						stack[sp++] = rval;
					continue;
					
				case Opcode.OP_HALT:
					if (debug) {
						stdout.println("Program halted:");
						for (int i = 0; i < sp; i++) {
							stdout.println(i + ": " + stack[i]);
						}
					}
					return narrow(stack[sp - 1]);

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
						throw new RuntimeException("unexpected argument type for INIT: " + src.getClass() + ", " + src);
					}
					
					// the main function of coroutine may have formal parameters,
					// therefore, INIT may take a number of arguments == formal parameters - arguments already passed to CREATE
					if(arity != fun.nformals - coroutine.frame.sp)
						throw new RuntimeException("Too many or too few arguments to INIT, the expected number: " + (fun.nformals - coroutine.frame.sp));
					Coroutine newCoroutine = coroutine.copy();
					for (int i = arity - 1; i >= 0; i--) {
						newCoroutine.frame.stack[coroutine.frame.sp + i] = stack[sp - arity + i];
					}
					newCoroutine.frame.sp = fun.nlocals;
					newCoroutine.suspend(newCoroutine.frame);
					sp = sp - arity;							/* CHANGED: place coroutine back on stack */
					stack[sp++] = newCoroutine;
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
							throw new RuntimeException("unexpected argument type for CREATEDYN: " + src.getClass() + ", " + src);
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
					// put the coroutine onto the stack of active coroutines
					activeCoroutines.push(coroutine);
					ccf = coroutine.start;
					coroutine.next(cf);
					
					fun = coroutine.frame.function;
					instructions = coroutine.frame.function.codeblock.getInstructions();
				
					coroutine.frame.stack[coroutine.frame.sp++] = 		// CHANGED: yield now always leaves an entry on the stack
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
					rval = (op == Opcode.OP_YIELD1) ? stack[--sp] : null;
					cf.pc = pc;
					cf.sp = sp;
					coroutine.suspend(cf);
					cf = prev;
					if(op == Opcode.OP_YIELD1 && cf == null)
						return narrow(rval);
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					//if(op == Opcode.OP_YIELD1 /* && rval != null */) {	/* CHANGED */
					stack[sp++] = rval;	 // corresponding next will always find an entry on the stack
					//}
					continue;
					
				case Opcode.OP_HASNEXT:
					coroutine = (Coroutine) stack[--sp];
					stack[sp++] = coroutine.hasNext() ? TRUE : FALSE;
					continue;
					
				case Opcode.OP_CALLPRIM:
					RascalPrimitive prim = RascalPrimitive.fromInteger(instructions[pc++]);
					arity = instructions[pc++];
					sp = prim.invoke(stack, sp, arity);
					continue;
					
				case Opcode.OP_CALLMUPRIM:
					MuPrimitive muprim = MuPrimitive.fromInteger(instructions[pc++]);
					arity = instructions[pc++];
					
					switch(muprim){
					
					case addition_mint_mint:
						assert arity == 2;
						stack[sp - 2] = ((Integer) stack[sp - 2]) + ((Integer) stack[sp - 1]);
						sp = sp - 1;
						break;
						
					case AND_U_U:	
					case and_mbool_mbool:
						assert arity == 2;
						boolean b1 =  (stack[sp - 2] instanceof Boolean) ? ((Boolean) stack[sp - 2]) : ((IBool) stack[sp - 2]).getValue();
						boolean b2 =  (stack[sp - 1] instanceof Boolean) ? ((Boolean) stack[sp - 1]) : ((IBool) stack[sp - 1]).getValue();
						stack[sp - 2] = b1 && b2;
						sp = sp - 1;
						break;
						
					case assign_pair:
						assert arity == 2;
						int v1 = ((Integer) stack[sp - 3]);
						int v2 = ((Integer) stack[sp - 2]);
						Object[] pair = (Object[]) stack[sp - 1];
						stack[v1] = pair[0];
						stack[v2] = pair[1];
						stack[sp - 3] = pair;
						sp = sp - 2;
						break;
						
					case assign_subscript_array_mint:
						assert arity == 3;
						Object[] ar = (Object[]) stack[sp - 3];
						int index = ((Integer) stack[sp - 2]);
						ar[index] = stack[sp - 1];
						stack[sp - 3] = stack[sp - 1];
						sp = sp - 2;
						break;
						
					case equal_mint_mint:
						assert arity == 2;
						stack[sp - 2] = ((Integer) stack[sp - 2]) == ((Integer) stack[sp - 1]);
						sp = sp - 1;
						break;
						
					case equal:
						assert arity == 2;
						if(stack[sp - 2] instanceof IValue && (stack[sp - 2] instanceof IValue)){
							stack[sp - 2] = ((IValue) stack[sp - 2]).isEqual(((IValue) stack[sp - 1]));
						} else if(stack[sp - 2] instanceof Type && (stack[sp - 2] instanceof Type)){
							stack[sp - 2] = ((Type) stack[sp - 2]) == ((Type) stack[sp - 1]);
						} else 
							throw new RuntimeException("equal -- not defined on " + stack[sp - 2].getClass() + " and " + stack[sp - 2].getClass());
						sp = sp - 1;
						break;
						
					case EQUIVALENT_U_U:	
					case equivalent_mbool_mbool:
						assert arity == 2;
						b1 =  (stack[sp - 2] instanceof Boolean) ? ((Boolean) stack[sp - 2]) : ((IBool) stack[sp - 2]).getValue();
						b2 =  (stack[sp - 1] instanceof Boolean) ? ((Boolean) stack[sp - 1]) : ((IBool) stack[sp - 1]).getValue();
						stack[sp - 2] = (b1 == b2);
						sp = sp - 1;
						break;
						
					case get_name_and_children:
						assert arity == 1;
						INode nd = (INode) stack[sp - 1];
						String name = nd.getName();
						Object[] elems = new Object[arity + 1];
						elems[0] = vf.string(name);
						for(int i = 0; i < arity; i++){
							elems[i + 1] = nd.get(i);
						}
						stack[sp - 1] =  elems;
						break;
						
					case get_tuple_elements:
						assert arity == 1;
						ITuple tup = (ITuple) stack[sp - 1];
						int nelem = tup.arity();
						elems = new Object[nelem];
						for(int i = 0; i < nelem; i++){
							elems[i] = tup.get(i);
						}
						stack[sp - 1] =  elems;
						break;
						
					case greater_equal_mint_mint:
						assert arity == 2;
						stack[sp - 2] = ((Integer) stack[sp - 2]) >= ((Integer) stack[sp - 1]);
						sp = sp - 1;
						break;
						
					case greater_mint_mint:
						assert arity == 2;
						stack[sp - 2] = ((Integer) stack[sp - 2]) > ((Integer) stack[sp - 1]);
						sp = sp - 1;
						break;
						
					case IMPLIES_U_U:	
					case implies_mbool_mbool:
						assert arity == 2;
						b1 =  (stack[sp - 2] instanceof Boolean) ? ((Boolean) stack[sp - 2]) : ((IBool) stack[sp - 2]).getValue();
						b2 =  (stack[sp - 1] instanceof Boolean) ? ((Boolean) stack[sp - 1]) : ((IBool) stack[sp - 1]).getValue();
						stack[sp - 2] = b1 ? b2 : true;
						sp = sp - 1;
						break;
						
					case is_bool:
						assert arity == 1;
						stack[sp - 2] = ((IValue) stack[sp - 1]).getType().isBool();
						break;
						
					case is_datetime:
						assert arity == 1;
						stack[sp - 2] = ((IValue) stack[sp - 1]).getType().isDateTime();
						break;
						
					case is_int:
						assert arity == 1;
						stack[sp - 2] = ((IValue) stack[sp - 1]).getType().isInteger();
						break;
						
					case is_list:
						assert arity == 1;
						stack[sp - 2] = ((IValue) stack[sp - 1]).getType().isList();
						break;
						
					case is_loc:
						assert arity == 1;
						stack[sp - 2] = ((IValue) stack[sp - 1]).getType().isSourceLocation();
						break;
						
					case is_lrel:
						assert arity == 1;
						stack[sp - 2] = ((IValue) stack[sp - 1]).getType().isListRelation();
						break;
						
					case is_map:
						assert arity == 1;
						stack[sp - 2] = ((IValue) stack[sp - 1]).getType().isMap();
						break;
						
					case is_node:
						assert arity == 1;
						stack[sp - 2] = ((IValue) stack[sp - 1]).getType().isNode();
						break;
						
					case is_num:
						assert arity == 1;
						stack[sp - 2] = ((IValue) stack[sp - 1]).getType().isNumber();
						break;
						
					case is_rat:
						assert arity == 1;
						stack[sp - 2] = ((IValue) stack[sp - 1]).getType().isRational();
						break;
						
					case is_real:
						assert arity == 1;
						stack[sp - 2] = ((IValue) stack[sp - 1]).getType().isReal();
						break;
						
					case is_rel:
						assert arity == 1;
						stack[sp - 2] = ((IValue) stack[sp - 1]).getType().isRelation();
						break;
						
					case is_set:
						assert arity == 1;
						stack[sp - 2] = ((IValue) stack[sp - 1]).getType().isSet();
						break;
						
					case is_str:
						assert arity == 1;
						stack[sp - 2] = ((IValue) stack[sp - 1]).getType().isString();
						break;
						
					case is_tuple:
						assert arity == 1;
						stack[sp - 2] = ((IValue) stack[sp - 1]).getType().isTuple();
						break;
						
					case less_equal_mint_mint:
						assert arity == 2;
						stack[sp - 2] = ((Integer) stack[sp - 2]) <= ((Integer) stack[sp - 1]);
						sp = sp - 1;
						break;
						
					case less_mint_mint:
						assert arity == 2;
						stack[sp - 2] = ((Integer) stack[sp - 2]) < ((Integer) stack[sp - 1]);
						sp = sp - 1;
						break;
						
					case make_array:
						assert arity >= 0;
						
						ar = new Object[arity];

						for (int i = arity - 1; i >= 0; i--) {
							ar[i] = stack[sp - arity + i];
						}
						sp = sp - arity + 1;
						stack[sp - 1] = ar;
						break;
						
					case make_array_of_size:
						assert arity == 1;
						int len = ((Integer)stack[sp - 1]);
						stack[sp - 1] = new Object[len];
						break;
						
					case mint:
						assert arity == 1;
						stack[sp - 1] = ((IInteger) stack[sp - 1]).intValue();
						break;
						
					case not_equal_mint_mint:
						assert arity == 2;
						stack[sp - 2] = ((Integer) stack[sp - 2]) != ((Integer) stack[sp - 1]);
						sp = sp - 1;
						break;
						
					case NOT_U:	
					case not_mbool:
						assert arity == 1;
						b1 =  (stack[sp - 1] instanceof Boolean) ? ((Boolean) stack[sp - 1]) : ((IBool) stack[sp - 1]).getValue();
						stack[sp - 1] = !b1;
						break;
						
					case OR_U_U:	
					case or_mbool_mbool:
						assert arity == 2;
						b1 =  (stack[sp - 2] instanceof Boolean) ? ((Boolean) stack[sp - 2]) : ((IBool) stack[sp - 2]).getValue();
						b2 =  (stack[sp - 1] instanceof Boolean) ? ((Boolean) stack[sp - 1]) : ((IBool) stack[sp - 1]).getValue();
						stack[sp - 2] = b1 || b2;
						sp = sp - 1;
						break;
						
					case rint:
						assert arity == 1;
						stack[sp -1] = vf.integer((Integer) stack[sp -1]);
						break;
						
					case size_array_or_list:
						assert arity == 1;
						if(stack[sp - 1] instanceof Object[]){
							stack[sp - 1] = ((Object[]) stack[sp - 1]).length;
						} else if(stack[sp - 1] instanceof IList){
							stack[sp - 1] = ((IList) stack[sp - 1]).length();
						} else
							throw new RuntimeException("size_array_or_list_mint -- not defined on " + stack[sp - 1].getClass());
						break;
						
					case sublist_list_mint_mint:
						assert arity == 3;
						IList lst = (IList) stack[sp - 3];
						int offset = ((Integer) stack[sp - 2]);
						int length = ((Integer) stack[sp - 1]);
						stack[sp - 3] = lst.sublist(offset, length);
						sp = sp - 2;
						break;
						
					case subscript_array_or_list_mint:
						assert arity == 2;
						if(stack[sp - 2] instanceof Object[]){
							stack[sp - 2] = ((Object[]) stack[sp - 2])[((Integer) stack[sp - 1])];
						} else if(stack[sp - 2] instanceof IList){
							stack[sp - 2] = ((IList) stack[sp - 2]).get((Integer) stack[sp - 1]);
						} else 
							throw new RuntimeException("subscript_array_or_list_mint -- Object[] or IList expected");
						sp = sp - 1;
						break;
						
					case subtraction_mint_mint:
						assert arity == 2;
						stack[sp - 2] = ((Integer) stack[sp - 2]) - ((Integer) stack[sp - 1]);
						sp =  sp - 1;
						break;
						
					case subtype:
						assert arity == 2;
						stack[sp - 2] = ((Type) stack[sp - 2]).isSubtypeOf((Type) stack[sp - 1]);
						sp = sp - 1;
						break;
						
					case typeOf:
						assert arity == 1;
						if(stack[sp - 1] instanceof Integer) {
							stack[sp - 1] = TypeFactory.getInstance().integerType();
						} else {
							stack[sp - 1] = ((IValue) stack[sp - 1]).getType();
						}
						break;
					
					case product_mint_mint:
						assert arity == 2;
						stack[sp - 2] = ((Integer) stack[sp - 2]) * ((Integer) stack[sp - 1]);
						sp = sp - 1;
						break;
					
					
					
					default:
						throw new RuntimeException("CALLMUPRIM -- unknown primitive");
					}
					continue;
								
				default:
					throw new RuntimeException("RVM main loop -- cannot decode instruction");
				}
			}
		} catch (Exception e) {
			stdout.println("PANIC: (instruction execution): " + e.getMessage());
			e.printStackTrace();
		}
		return Rascal_FALSE;
	}
		
}
