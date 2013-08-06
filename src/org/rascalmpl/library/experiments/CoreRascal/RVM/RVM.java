package org.rascalmpl.library.experiments.CoreRascal.RVM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Opcode;
import org.rascalmpl.values.ValueFactoryFactory;

public class RVM {

	public IValueFactory vf;
	private IBool TRUE;
	private IBool FALSE;
	private boolean debug = true;
	private boolean listing = false;
	
	private Map<String, Integer> constantMap;
	ArrayList<IValue> constantStore;
	
	private ArrayList<Function> functionStore;
	private Map<String, Integer> functionMap;

	public RVM(IValueFactory vf) {
		this.vf = vf;
		TRUE = vf.bool(true);
		FALSE = vf.bool(false);
		constantStore = new ArrayList<IValue>();
		functionStore = new ArrayList<Function>();
		constantMap = new HashMap<String, Integer>();
		functionMap = new HashMap<String, Integer>();
		Primitive.init(vf);
	}
	
	public void declare(Function f){
		if(functionMap.get(f.name) != null){
			throw new RuntimeException("PANIC: Double declaration of function: " + f.name);
		}
		functionMap.put(f.name, functionStore.size());
		functionStore.add(f);
	}
	
	public void declareConst(String name, IValue val){
		if(constantMap.get(name) != null){
			throw new RuntimeException("PANIC: Double declaration of constant: " + name);
		}
		constantMap.put(name, constantStore.size());
		constantStore.add(val);
	}
	
	public void setDebug(boolean b){
		debug = b;
	}
	
	public void setListing(boolean b){
		listing = b;
	}
	
	public Object executeProgram(String main, IValue[] args) {

		for (Function f : functionStore) {
			f.instructions.done(f.name, constantMap, functionMap, listing);
		}
		// Perform a call to "main"

		Function function = functionStore.get(functionMap.get(main));
		if (function == null) {
			throw new RuntimeException("PANIC: Code for main not found: "+ main);
		}
		Frame cf = new Frame(0, null, function.maxstack, function);
		Object[] stack = cf.stack;
		if (args.length != function.nformals) {
			throw new RuntimeException("PANIC: " + main + " called with wrong number of arguaments: " + args.length);
		}
		for (int i = 0; i < args.length; i++) {
			stack[i] = args[i];
		}

		int[] instructions = function.instructions.getInstructions();
		int pc = 0;
		int sp = function.nlocals;
		
		Stack<Coroutine> activeCoroutines = new Stack<>();
		
		try {
			NEXT_INSTRUCTION: while (true) {
				int op = instructions[pc++];

				if (debug) {
					int startpc = pc - 1;
					for (int i = 0; i < sp; i++) {
						System.out.println("\t" + i + ": " + stack[i]);
					}
					System.out.println(cf.function.name + "[" + startpc + "] " + cf.function.instructions.toString(startpc));
				}

				switch (op) {

				case Opcode.OP_LOADCON:
					stack[sp++] = constantStore.get(instructions[pc++]);
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
					throw new RuntimeException(
							"PANIC: label instruction at runtime");

				case Opcode.OP_CALLDYN:
					
				case Opcode.OP_CALL:
					Function fun;
					Frame previousScope;
					int spdelta = 0;
					if(op == Opcode.OP_CALLDYN && stack[sp - 1] instanceof Closure){
						Closure clos = (Closure) stack[sp - 1];
						fun = clos.function;
						previousScope = clos.frame;
						spdelta = 1;
					} else {
						fun = (op == Opcode.OP_CALL) ? functionStore.get(instructions[pc++]) : (Function) stack[--sp];
						previousScope = cf;
					}
						
					instructions = fun.instructions.getInstructions();
					
					Frame nextFrame = new Frame(fun.scope + 1, cf, previousScope, fun.maxstack, fun);
					
					for (int i = fun.nformals - 1; i >= 0; i--) {
						nextFrame.stack[i] = stack[sp - fun.nformals + i];
					}
					cf.pc = pc;
					cf.sp = sp - fun.nformals - spdelta;
					cf = nextFrame;
					stack = cf.stack;
					sp = fun.nlocals;
					pc = 0;
					continue;

				case Opcode.OP_RETURN:
					Object rval = stack[sp - 1];
					cf = cf.previousCallFrame;
					if (cf == null)
						return rval;
					instructions = cf.function.instructions.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					stack[sp++] = rval;
					continue;

				case Opcode.OP_HALT:
					if (debug) {
						System.out.println("Program halted:");
						for (int i = 0; i < sp; i++) {
							System.out.println(i + ": " + stack[i]);
						}
					}
					return stack[sp - 1];

				case Opcode.OP_CALLPRIM:
					Primitive prim = Primitive.fromInteger(instructions[pc++]);
					sp = prim.invoke(stack, sp);
					continue;
				
				case Opcode.OP_START:
					Coroutine coroutine = (Coroutine) stack[--sp];
					// put the coroutine onto the stack of active coroutines
					activeCoroutines.push(coroutine);
					coroutine.resume(cf);
					
					fun = coroutine.frame.function;
					instructions = coroutine.frame.function.instructions.getInstructions();
					
					// coroutine's main function may have formal parameters
					for (int i = fun.nformals - 1; i >= 0; i--) {
						coroutine.frame.stack[i] = stack[sp - fun.nformals + i];
					}
					cf.pc = pc;
					cf.sp = sp - fun.nformals;
					cf = coroutine.frame;
					stack = cf.stack;
					sp = fun.nlocals;
					pc = 0;
					continue;
					
				case Opcode.OP_CREATE:
					fun = functionStore.get(instructions[pc++]);
					Frame frame = new Frame(fun.scope + 1, null, fun.maxstack, fun);
					coroutine = new Coroutine(frame);
					stack[sp++] = coroutine;
					continue;
					
				case Opcode.OP_NEXT_0:
				case Opcode.OP_NEXT_1:
					coroutine = (Coroutine) stack[--sp];
					activeCoroutines.push(coroutine);
					coroutine.resume(cf);
					
					fun = coroutine.frame.function;
					instructions = coroutine.frame.function.instructions.getInstructions();
					
					if(op == Opcode.OP_NEXT_1)
						coroutine.frame.stack[coroutine.frame.sp++] = stack[--sp];
					
					cf.pc = pc;
					cf.sp = sp;
					
					cf = coroutine.frame;
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					continue;
					
				case Opcode.OP_YIELD_0:	
				case Opcode.OP_YIELD_1:
					coroutine = activeCoroutines.pop();
					Frame prev = coroutine.init.previousCallFrame;
					cf.pc = pc;
					cf.sp = sp;
					coroutine.suspend(cf);
					cf = prev;
					rval = null;
					if(op == Opcode.OP_YIELD_1) {
						rval = stack[sp - 1];
						if(cf == null)
							return rval;
					}
					instructions = cf.function.instructions.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					if(op == Opcode.OP_YIELD_1 && rval != null) {
						stack[sp++] = rval;
					}
					continue;
				
				case Opcode.OP_CREATEDYN:
					continue;
					
				case Opcode.OP_HASNEXT:
					continue;

				default:
					throw new RuntimeException("PANIC: RVM main loop -- cannot decode instruction");
				}
			}
		} catch (Exception e) {
			System.err.println("PANIC: exception caused by invoking a primitive");
			e.printStackTrace();
		}
		return FALSE;
	}

	public ITuple executeProgram(IList directives, IInteger repeats, IEvaluatorContext ctx) {
		String func = "main";
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		List<IValue> functions = new ArrayList<>();
		for(IValue directive : directives) {
			String constr = ((IConstructor) directive).getName();
			String name = null;
			
			// Loading constants
			switch(constr) {
			case "intconst":
				name = ((IString) ((IConstructor) directive).get("value")).getValue();
				rvm.declareConst(name, rvm.vf.integer(name));
				break;
			case "relconst":
				name = ((IString) ((IConstructor) directive).get("value")).getValue();
				rvm.declareConst(name, rvm.vf.real(name));
				break;
			case "ratconst":
				name = ((IString) ((IConstructor) directive).get("value")).getValue();
				rvm.declareConst(name, rvm.vf.rational(name));
				break;
			case "boolconst":
				name = ((IString) ((IConstructor) directive).get("value")).getValue();
				if(name.equals("TRUE")) {
					rvm.declareConst(name, rvm.TRUE);
				} else if(name.equals("FALSE")) {
					rvm.declareConst(name, rvm.FALSE);
				}
				break;
			case "function":
				functions.add(directive);
				break;
			default:
				throw new RuntimeException("PANIC: Unknown directive: " + constr);
			}
			
		}
		
		// Loading directives
		for(IValue f : functions) {
			IConstructor directive = (IConstructor) f;
			String name = ((IString) directive.get("name")).getValue();
			Integer scope = ((IInteger) directive.get("scope")).intValue();
			Integer nlocals = ((IInteger) directive.get("nlocals")).intValue();
			Integer nformals = ((IInteger) directive.get("nformals")).intValue();
			Integer maxstack = ((IInteger) directive.get("maxStack")).intValue();
			IList code = (IList) directive.get("instructions");
			CodeBlock instructions = new CodeBlock();
			
			// Loading instructions
			for(int i = 0; i < code.length(); i++) {
				IConstructor instruction = (IConstructor) code.get(i);
				String opcode = ((IString) instruction.get("opcode")).getValue();
				IList operands = (IList) instruction.get("operands");
				
				switch(opcode) {
				case "LOADCON":
					instructions = instructions.loadcon(((IString) operands.get(0)).getValue());
					break;
				case "LOADVAR":
					instructions = instructions.loadvar(Integer.parseInt(((IString) operands.get(0)).getValue()), 
														Integer.parseInt(((IString) operands.get(1)).getValue()));
					break;
				case "LOADLOC":
					instructions = instructions.loadloc(Integer.parseInt(((IString) operands.get(0)).getValue()));
					break;
				case "STOREVAR":
					instructions = instructions.storevar(Integer.parseInt(((IString) operands.get(0)).getValue()), 
							 							 Integer.parseInt(((IString) operands.get(1)).getValue()));
					break;
				case "STORELOC":
					instructions = instructions.storeloc(Integer.parseInt(((IString) operands.get(0)).getValue()));
					break;
				case "LABEL":
					instructions = instructions.label(((IString) operands.get(0)).getValue());
					break;
				case "CALLPRIM":
					instructions = instructions.callprim(Primitive.valueOf(((IString) operands.get(0)).getValue()));
							
					/* String operand = ((IString) operands.get(0)).getValue();
				
					switch(operand) {
					case "addition_int_int":
						instructions = instructions.callprim(Primitive.addition_int_int);
						break;
					case "equal_int_int":
						instructions = instructions.callprim(Primitive.equal_int_int);
						break;
					case "greater_int_int":
						instructions = instructions.callprim(Primitive.greater_int_int);
						break;
					case "multiplication_int_int":
						instructions = instructions.callprim(Primitive.multiplication_int_int);
						break;
					case "substraction_int_int":
						instructions = instructions.callprim(Primitive.substraction_int_int);
						break;
					default:
						throw new RuntimeException("PANIC: Unknown primitive operation: " + operand);
					}
					*/
					break;
				case "CALL":
					instructions = instructions.call(((IString) operands.get(0)).getValue());
					break;
				case "RETURN":
					instructions = instructions.ret();
					break;
				case "JMP":
					instructions = instructions.jmp(((IString) operands.get(0)).getValue());
					break;
				case "JMPTRUE":
					instructions = instructions.jmptrue(((IString) operands.get(0)).getValue());
					break;
				case "JMPFALSE":
					instructions = instructions.jmpfalse(((IString) operands.get(0)).getValue());
					break;
				case "HALT":
					instructions = instructions.halt();
					break;
				
				case "CREATE":
					break;
					
				case "CREATEDYN":
					break;
				
				case "START":
					break;
					
				case "NEXT_0":
					break;
					
				case "NEXT_1":
					break;
					
				case "YIELD_0":
					break;
					
				case "YIELD_1":
					break;
					
				case "HASNEXT":
					break;
				
				default:
					throw new RuntimeException("PANIC: Unknown instruction: " + opcode + " has been used");
				}
								
			}
			rvm.declare(new Function(name, scope, nformals, nlocals, maxstack, instructions));
		}
		
		long start = System.currentTimeMillis();
		Object result = null;
		for(int i = 0; i < repeats.intValue(); i++)
			result = rvm.executeProgram(func, new IValue[] {});
		long now = System.currentTimeMillis();
		return vf.tuple((IValue)result, vf.integer(now - start));

	}
	
}
