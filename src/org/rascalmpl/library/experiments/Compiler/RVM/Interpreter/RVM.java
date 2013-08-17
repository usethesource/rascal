package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Opcode;


public class RVM {

	public final IValueFactory vf;
	private final IBool TRUE;
	private final IBool FALSE;
	private boolean debug = true;
	private boolean listing = false;
	
	private final ArrayList<Function> functionStore;
	private final Map<String, Integer> functionMap;
	
	private final TypeFactory tf = TypeFactory.getInstance(); 
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
		
		this.types = new Types(this.vf);
		
		TRUE = vf.bool(true);
		FALSE = vf.bool(false);
		functionStore = new ArrayList<Function>();
		constructorStore = new ArrayList<Type>();

		functionMap = new HashMap<String, Integer>();
		constructorMap = new HashMap<String, Integer>();
		
		Primitive.init(vf);
	}
	
	public RVM(IValueFactory vf){
		this(vf, new PrintWriter(System.out, true), false);
	}
	
	public void declare(Function f){
		if(functionMap.get(f.name) != null){
			throw new RuntimeException("PANIC: Double declaration of function: " + f.name);
		}
		functionMap.put(f.name, functionStore.size());
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
	
	public Object executeProgram(String main, IValue[] args) {

		// Finalize the instruction generation of all functions
		for (Function f : functionStore) {
			f.finalize(functionMap, constructorMap, listing);
		}
		
		// Search for the "#module_init" function and check arguments

		Function init_function = functionStore.get(functionMap.get("#module_init"));
		if (init_function == null) {
			throw new RuntimeException("PANIC: Code for #module_init not found");
		}
		
		if (init_function.nformals != 0) {
			throw new RuntimeException("PANIC: " + "function \"#module_init\" should have one argument");
		}
		
		// Search for the "main" function and check arguments

		Function main_function = functionStore.get(functionMap.get("main"));
		if (main_function == null) {
			throw new RuntimeException("PANIC: No function \"main\" found");
		}
				
		if (main_function.nformals != 1) {
					throw new RuntimeException("PANIC: function \"main\" should have one argument");
		}
		
		// Perform a call to #module_init" at scope level = 0
		
		Frame cf = new Frame(0, null, init_function.maxstack, init_function);
		Frame root = cf; // we need the notion of the root frame, which represents the root environment
		Object[] stack = cf.stack;
		
		stack[0] = args; // pass the program argument to #module_init
		

		int[] instructions = init_function.codeblock.getInstructions();
		int pc = 0;
		int sp = init_function.nlocals;
		
		Stack<Coroutine> activeCoroutines = new Stack<>();
		Frame ccf = null; // the start frame (i.e., the frame of the coroutine's main function) of the current active coroutine
		
		try {
			NEXT_INSTRUCTION: while (true) {
				if(pc < 0 || pc >= instructions.length){
					throw new RuntimeException("PANIC: " + main + " illegal pc: " + pc);
				}
				int op = instructions[pc++];

				if (debug) {
					int startpc = pc - 1;
					for (int i = 0; i < sp; i++) {
						stdout.println("\t" + i + ": " + stack[i]);
						//System.out.println("\t" + i + ": " + stack[i]);
					}
					stdout.println(cf.function.name + "[" + startpc + "] " + cf.function.codeblock.toString(startpc));
					//System.out.println(cf.function.name + "[" + startpc + "] " + cf.function.codeblock.toString(startpc));
				}


				switch (op) {

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
					throw new RuntimeException("PANIC: LOAD_NESTED_FUNCTION cannot find matching scope: " + scope);	
				}
				
				case Opcode.OP_LOADCONSTR:
					Type constructor = constructorStore.get(instructions[pc++]);
				
				case Opcode.OP_LOADLOC:
				case Opcode.OP_LOADLOC_AS_REF:
					stack[sp++] = (op == Opcode.OP_LOADLOC) ? stack[instructions[pc++]] 
															: new Reference(stack, instructions[pc++]);
					continue;
				
				case Opcode.OP_LOADLOCREF: {
					Reference ref = (Reference) stack[instructions[pc++]];
					stack[sp++] = ref.stack[ref.pos];
					continue;
				}
				case Opcode.OP_LOADVAR:
				case Opcode.OP_LOADVAR_AS_REF: {
					int s = instructions[pc++];
					int pos = instructions[pc++];
					for (Frame fr = cf; fr != null; fr = fr.previousScope) {
						if (fr.scopeId == s) {
							stack[sp++] = (op == Opcode.OP_LOADVAR) ? fr.stack[pos] 
																	: new Reference(fr.stack, pos);
							continue NEXT_INSTRUCTION;
						}
					}
					throw new RuntimeException("PANIC: LOADVAR cannot find matching scope: " + s);
				}
				
				case Opcode.OP_LOADVARREF: {
					int s = instructions[pc++];
					int pos = instructions[pc++];
					for (Frame fr = cf; fr != null; fr = fr.previousScope) {
						if (fr.scopeId == s) {
							Reference ref = (Reference) fr.stack[pos];
							stack[sp++] = ref.stack[ref.pos];
							continue NEXT_INSTRUCTION;
						}
					}
					throw new RuntimeException("PANIC: LOADVARREF cannot find matching scope: " + s);
				}
				
				case Opcode.OP_STORELOC: {
					stack[instructions[pc++]] = stack[sp - 1];  /* CHANGED: --sp to sp -1; value remains on stack */
					continue;
				}
				
				case Opcode.OP_STORELOCREF:
					Reference ref = (Reference) stack[instructions[pc++]];
					ref.stack[ref.pos] = stack[sp - 1];         /* CHANGED: --sp to sp - 1; value remains on stack */
					continue;
				
				case Opcode.OP_STOREVAR:
					int s = instructions[pc++];
					int pos = instructions[pc++];

					for (Frame fr = cf; fr != null; fr = fr.previousScope) {
						if (fr.scopeId == s) {
							fr.stack[pos] = stack[sp - 1];		/* CHANGED: --sp to sp -1; value remains on stack */
							continue NEXT_INSTRUCTION;
						}
					}

					throw new RuntimeException("PANIC: STOREVAR cannot find matching scope: " + s);
	
				case Opcode.OP_STOREVARREF:
					s = instructions[pc++];
					pos = instructions[pc++];

					for (Frame fr = cf; fr != null; fr = fr.previousScope) {
						if (fr.scopeId == s) {
							ref = (Reference) fr.stack[pos];
							ref.stack[ref.pos] = stack[sp - 1];	/* CHANGED: --sp to sp -1; value remains on stack */
							continue NEXT_INSTRUCTION;
						}
					}

					throw new RuntimeException("PANIC: STOREVARREF cannot find matching scope: " + s);


				case Opcode.OP_JMP:
					pc = instructions[pc];
					continue;

				case Opcode.OP_JMPTRUE:
					if (stack[sp - 1].equals(TRUE)) {
						pc = instructions[pc];
					} else
						pc++;
					sp--;
					continue;

				case Opcode.OP_JMPFALSE:
					if (stack[sp - 1].equals(FALSE)) {
						pc = instructions[pc];
					} else
						pc++;
					sp--;
					continue;

				case Opcode.OP_POP:
					sp--;
					continue;

				case Opcode.OP_LABEL:
					throw new RuntimeException("PANIC: label instruction at runtime");
				
				case Opcode.OP_CALLCONSTR:
					constructor = constructorStore.get(instructions[pc++]);
					int arity = constructor.getArity();
					args = new IValue[arity]; 
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
						fun = fun_instance.function;
						previousScope = fun_instance.env;
					} else if(op == Opcode.OP_CALL) {
						fun = functionStore.get(instructions[pc++]);
						previousScope = cf;
					} else {
						throw new RuntimeException("PANIC: unexpected argument type when CALLDYN is executed");
					}
						
					instructions = fun.codeblock.getInstructions();
					
					Frame nextFrame = new Frame(fun.scope, cf, previousScope, fun.maxstack, fun);
					
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
							return rval;
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
					return stack[sp - 1];

				case Opcode.OP_PRINTLN:
					stdout.println(((IString) stack[sp - 1]).getValue());
					continue;
					
				case Opcode.OP_CALLPRIM:
					Primitive prim = Primitive.fromInteger(instructions[pc++]);
					sp = prim.invoke(stack, sp);
					continue;
				
				case Opcode.OP_INIT:
					Object src = stack[--sp];
					Coroutine coroutine;
					if(src instanceof Coroutine){
						coroutine = (Coroutine) src; 
						fun = coroutine.frame.function;
					} else if(src instanceof FunctionInstance) {
						FunctionInstance fun_instance = (FunctionInstance) src;
						fun = fun_instance.function;
						Frame frame = new Frame(fun.scope, null, fun_instance.env, fun.maxstack, fun);
						coroutine = new Coroutine(frame);
					} else {
						throw new RuntimeException("PANIC: unexpected argument type when INIT is executed.");
					}
					
					// the main function of coroutine may have formal parameters
					for (int i = fun.nformals - 1; i >= 0; i--) {
						coroutine.frame.stack[i] = stack[sp - fun.nformals + i];
					}
					coroutine.frame.sp = fun.nlocals;
					coroutine.suspend(coroutine.frame);
					sp = sp - fun.nformals;							/* CHANGED: place coroutine back on stack */
					stack[sp++] = coroutine;						
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
							throw new RuntimeException("PANIC: unexpected argument type when CREATEDYN is executed.");
						}
					}
					Frame frame = new Frame(fun.scope, null, previousScope, fun.maxstack, fun);
					coroutine = new Coroutine(frame);
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
						return rval;
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
								
				default:
					throw new RuntimeException("PANIC: RVM main loop -- cannot decode instruction");
				}
			}
		} catch (Exception e) {
			stdout.println("PANIC: exception caused by invoking a primitive or illegal instruction sequence: " + e);
			e.printStackTrace();
		}
		return FALSE;
	}
}
