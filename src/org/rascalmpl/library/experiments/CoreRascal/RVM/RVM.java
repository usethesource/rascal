package org.rascalmpl.library.experiments.CoreRascal.RVM;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Opcode;
import org.rascalmpl.values.ValueFactoryFactory;

public class RVM {

	public final IValueFactory vf;
	private final IBool TRUE;
	private final IBool FALSE;
	private final boolean debug = false;;
	private final boolean listing = false;
	
	private final ArrayList<Function> functionStore;
	private final Map<String, Integer> functionMap;
	private final PrintWriter stdout;
	
	private final TypeFactory tf = TypeFactory.getInstance(); 
	private final TypeStore typeStore = new TypeStore();
	private final Types types;
	
	private final ArrayList<Type> constructorStore;
	private final Map<String, Integer> constructorMap;

	public RVM(IValueFactory vf) {
		super();

		this.vf = vf;
		this.types = new Types(this.vf);
		stdout = new PrintWriter(System.out, true);
		TRUE = vf.bool(true);
		FALSE = vf.bool(false);
		functionStore = new ArrayList<Function>();
		constructorStore = new ArrayList<Type>();

		functionMap = new HashMap<String, Integer>();
		constructorMap = new HashMap<String, Integer>();
		
		Primitive.init(vf);
	}
	
	public void setStdOut(PrintWriter stdout){
		//this.stdout = stdout;
	}
	
	public void setDebug(boolean b){
		//listing = b;
	}
	
	public void setListing(boolean b){
		//listing = b;
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
	
	public Object executeProgram(String main, IValue[] args) {

		for (Function f : functionStore) {
			f.codegen(functionMap, constructorMap, listing);
		}
		// Perform a call to "main"

		Function function = functionStore.get(functionMap.get(main));
		if (function == null) {
			throw new RuntimeException("PANIC: Code for main not found: " + main);
		}
		Frame cf = new Frame(1, null, function.maxstack, function);
		Object[] stack = cf.stack;
		if (args.length != function.nformals) {
			throw new RuntimeException("PANIC: " + main + " called with wrong number of arguments: " + args.length);
		}
		for (int i = 0; i < args.length; i++) {
			stack[i] = args[i];
		}

		int[] instructions = function.codeblock.getInstructions();
		int pc = 0;
		int sp = function.nlocals;
		
		Stack<Coroutine> activeCoroutines = new Stack<>();
		
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
					}
					stdout.println(cf.function.name + "[" + startpc + "] " + cf.function.codeblock.toString(startpc));
				}

				switch (op) {

				case Opcode.OP_LOADCON:
					stack[sp++] = cf.function.constantStore[instructions[pc++]];
					continue;

				case Opcode.OP_LOADFUN:
					stack[sp++] = new Closure(functionStore.get(instructions[pc++]), cf);
					continue;

				case Opcode.OP_LOADLOC:
					stack[sp++] = stack[instructions[pc++]];
					continue;

				case Opcode.OP_LOADVAR: {
					int s = instructions[pc++];
					int pos = instructions[pc++];
					for (Frame fr = cf; fr != null; fr = fr.previousScope) {
						if (fr.scopeId == s) {
							stack[sp++] = fr.stack[pos];
							continue NEXT_INSTRUCTION;
						}
					}
					throw new RuntimeException("PANIC: load var cannot find matching scope: " + s);
				}

				case Opcode.OP_STORELOC: {
					stack[instructions[pc++]] = stack[--sp];
					continue;
				}

				case Opcode.OP_STOREVAR:
					int s = instructions[pc++];
					int pos = instructions[pc++];

					for (Frame fr = cf; fr != null; fr = fr.previousScope) {
						if (fr.scopeId == s) {
							fr.stack[pos] = stack[--sp];
							continue NEXT_INSTRUCTION;
						}
					}

					throw new RuntimeException("PANIC: load var cannot find matching scope: " + s);

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

				case Opcode.OP_CALLDYN:
					
				case Opcode.OP_CALL:
					Function fun;
					Frame previousScope;
					if(op == Opcode.OP_CALLDYN && stack[sp - 1] instanceof Closure){
						Closure clos = (Closure) stack[--sp];
						fun = clos.function;
						previousScope = clos.frame;
					} else {
						fun = (op == Opcode.OP_CALL) ? functionStore.get(instructions[pc++]) : (Function) stack[--sp];
						previousScope = cf;
					}
						
					instructions = fun.codeblock.getInstructions();
					
					Frame nextFrame = new Frame(fun.scope + 1, cf, previousScope, fun.maxstack, fun);
					
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
					stdout.println(((IString) stack[--sp]).getValue());
					continue;
					
				case Opcode.OP_CALLPRIM:
					Primitive prim = Primitive.fromInteger(instructions[pc++]);
					sp = prim.invoke(stack, sp);
					continue;
				
				case Opcode.OP_INIT:
					Coroutine coroutine = (Coroutine) stack[--sp];
					
					// coroutine's main function may have formal parameters
					fun = coroutine.frame.function;
					for (int i = fun.nformals - 1; i >= 0; i--) {
						coroutine.frame.stack[i] = stack[sp - fun.nformals + i];
					}
					coroutine.frame.sp = fun.nlocals;
					coroutine.suspend(coroutine.frame);
					sp = sp - fun.nformals;
					continue;
					
				case Opcode.OP_CREATE:
				case Opcode.OP_CREATEDYN:
					fun = (op == Opcode.OP_CREATEDYN) ? (Function) stack[--sp] : functionStore.get(instructions[pc++]);
					Frame frame = new Frame(fun.scope + 1, null, fun.maxstack, fun);
					coroutine = new Coroutine(frame);
					stack[sp++] = coroutine;
					continue;
				
				case Opcode.OP_NEXT0:
				case Opcode.OP_NEXT1:
					coroutine = (Coroutine) stack[--sp];
					// put the coroutine onto the stack of active coroutines
					activeCoroutines.push(coroutine);
					coroutine.next(cf);
					
					fun = coroutine.frame.function;
					instructions = coroutine.frame.function.codeblock.getInstructions();
					
					if(op == Opcode.OP_NEXT1)
						coroutine.frame.stack[coroutine.frame.sp++] = stack[--sp];
					
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
					Frame prev = coroutine.start.previousCallFrame;
					rval = null;
					if(op == Opcode.OP_YIELD1) {
						rval = stack[--sp];
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
					if(op == Opcode.OP_YIELD1 && rval != null) {
						stack[sp++] = rval;
					}
					continue;
					
				case Opcode.OP_HASNEXT:
					coroutine = (Coroutine) stack[--sp];
					stack[sp++] = coroutine.hasNext() ? TRUE : FALSE;
					continue;
				
				case Opcode.OP_LOADCONREF:
					pos = instructions[pc++];
					stack[sp++] = new Reference(stack, pos);
					continue;
				
				case Opcode.OP_LOADLOCREF:
					Reference ref = (Reference) stack[instructions[pc++]];
					stack[sp++] = ref.stack[ref.pos];
					continue;
					
				case Opcode.OP_STORELOCREF:
					ref = (Reference) stack[instructions[pc++]];
					ref.stack[ref.pos] = stack[--sp];
					continue;
							
				case Opcode.OP_LOADCONSTR:
					Type constructor = constructorStore.get(instructions[pc++]);
					
				case Opcode.OP_CALLCONSTR:
					constructor = constructorStore.get(instructions[pc++]);
					int arity = constructor.getArity();
					args = new IValue[arity]; 
					for(int i = 0; i < arity; i++) {
						args[arity - 1 - i] = (IValue) stack[--sp];
					}
					stack[sp++] = vf.constructor(constructor, args);
					continue;
				
				default:
					throw new RuntimeException("PANIC: RVM main loop -- cannot decode instruction");
				}
			}
		} catch (Exception e) {
			stdout.println("PANIC: exception caused by invoking a primitive or illegal instruction sequence");
			e.printStackTrace();
		}
		return FALSE;
	}
}
