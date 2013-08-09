package org.rascalmpl.library.experiments.CoreRascal.RVM;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Opcode;
import org.rascalmpl.values.ValueFactoryFactory;

public class RVM {

	public final IValueFactory vf;
	private final IBool TRUE;
	private final IBool FALSE;
	private boolean debug = false;;
	private boolean listing = false;
	
	private final ArrayList<Function> functionStore;
	private final Map<String, Integer> functionMap;
	private PrintWriter stdout;
	
	private final TypeStore typeStore = new TypeStore();
	private final ArrayList<Type> constructorStore;
	private final Map<String, Integer> constructorMap;

	public RVM(IValueFactory vf) {
		this.vf = vf;
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
		this.stdout = stdout;
	}
	
	public void setDebug(boolean b){
		listing = b;
	}
	
	public void setListing(boolean b){
		listing = b;
	}
	
	public void declare(Function f){
		if(functionMap.get(f.name) != null){
			throw new RuntimeException("PANIC: Double declaration of function: " + f.name);
		}
		functionMap.put(f.name, functionStore.size());
		functionStore.add(f);
	}
	
	public void declareConstructor(IValue constructor) {
		Type constr = null; // TODO: IValue -> Type
		if(constr == null)
			throw new RuntimeException("Not implemented yet");
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
		Frame cf = new Frame(0, null, function.maxstack, function);
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

	public ITuple executeProgram(IList directives, IInteger repeats, IEvaluatorContext ctx) {
		String func = "main";
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		rvm.setStdOut(ctx.getStdOut());
		List<IValue> functions = new ArrayList<>();
		HashMap<String, IValue> constantMap = new HashMap<String, IValue>();
		
		for(IValue directive : directives) {
			String constr = ((IConstructor) directive).getName();
			String name = null;
			
			// Loading constants
			switch(constr) {
			case "intconst":
				name = ((IString) ((IConstructor) directive).get("value")).getValue();
				constantMap.put(name, rvm.vf.integer(name));
				break;
			case "relconst":
				name = ((IString) ((IConstructor) directive).get("value")).getValue();
				constantMap.put(name, rvm.vf.real(name));
				break;
			case "ratconst":
				name = ((IString) ((IConstructor) directive).get("value")).getValue();
				constantMap.put(name, rvm.vf.rational(name));
				break;
			case "boolconst":
				name = ((IString) ((IConstructor) directive).get("value")).getValue();
				if(name.equals("TRUE")) {
					constantMap.put(name, rvm.TRUE);
				} else if(name.equals("FALSE")) {
					constantMap.put(name, rvm.FALSE);
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
			CodeBlock instructions = new CodeBlock(null);
			
			// Loading instructions
			for(int i = 0; i < code.length(); i++) {
				IConstructor instruction = (IConstructor) code.get(i);
				String opcode = ((IString) instruction.get("opcode")).getValue();
				IList operands = (IList) instruction.get("operands");
				
				switch(opcode) {
				case "LOADCON":
					instructions = instructions.LOADCON(constantMap.get(((IString) operands.get(0)).getValue()));
					break;
				case "LOADVAR":
					instructions = instructions.LOADVAR(Integer.parseInt(((IString) operands.get(0)).getValue()), 
														Integer.parseInt(((IString) operands.get(1)).getValue()));
					break;
				case "LOADLOC":
					instructions = instructions.LOADLOC(Integer.parseInt(((IString) operands.get(0)).getValue()));
					break;
				case "STOREVAR":
					instructions = instructions.STOREVAR(Integer.parseInt(((IString) operands.get(0)).getValue()), 
							 							 Integer.parseInt(((IString) operands.get(1)).getValue()));
					break;
				case "STORELOC":
					instructions = instructions.STORELOC(Integer.parseInt(((IString) operands.get(0)).getValue()));
					break;
				case "LABEL":
					instructions = instructions.LABEL(((IString) operands.get(0)).getValue());
					break;
				case "CALLPRIM":
					instructions = instructions.CALLPRIM(Primitive.valueOf(((IString) operands.get(0)).getValue()));	
					break;

				case "CALL":
					instructions = instructions.CALL(((IString) operands.get(0)).getValue());
					break;
				case "RETURN0":
					instructions = instructions.RETURN0();
				case "RETURN1":
					instructions = instructions.RETURN1();
					break;
				case "JMP":
					instructions = instructions.JMP(((IString) operands.get(0)).getValue());
					break;
				case "JMPTRUE":
					instructions = instructions.JMPTRUE(((IString) operands.get(0)).getValue());
					break;
				case "JMPFALSE":
					instructions = instructions.JMPFALSE(((IString) operands.get(0)).getValue());
					break;
				case "HALT":
					instructions = instructions.HALT();
					break;
				
				case "CREATE":
					instructions = instructions.CREATE(((IString) operands.get(0)).getValue());
					break;
					
				case "CREATEDYN":
					instructions = instructions.CREATEDYN();
					break;
				
				case "INIT":
					instructions = instructions.INIT();
					break;
					
				case "NEXT0":
					instructions = instructions.NEXT0();
					break;
					
				case "NEXT1":
					instructions = instructions.NEXT1();
					break;
					
				case "YIELD0":
					instructions = instructions.YIELD0();
					break;
					
				case "YIELD1":
					instructions = instructions.YIELD1();
					break;
					
				case "HASNEXT":
					instructions = instructions.HASNEXT();
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
	
	// Get integer field from an instruction
	
	private int getIntField(IConstructor instruction, String field){
		return ((IInteger) instruction.get(field)).intValue();
	}
	
	// Get String field from an instruction
	
	private String getStrField(IConstructor instruction, String field){
		return ((IString) instruction.get(field)).getValue();
	}
	
	// Execute an RV program from Rascal
	
	public ITuple executeProgram(IConstructor program, IBool debug, IInteger repeat, IEvaluatorContext ctx) {
		String func = "main";
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		rvm.setStdOut(ctx.getStdOut());
		rvm.setDebug(debug.getValue());
		
		IMap declarations = (IMap) program.get("declarations");
		
		for(IValue dname : declarations) {
			IConstructor declaration = (IConstructor) declarations.get(dname);
		
			if(declaration.getName().contentEquals("FUNCTION")){
				
				String name = ((IString) declaration.get("name")).getValue();
				Integer scope = ((IInteger) declaration.get("scope")).intValue();
				Integer nlocals = ((IInteger) declaration.get("nlocals")).intValue();
				Integer nformals = ((IInteger) declaration.get("nformals")).intValue();
				Integer maxstack = ((IInteger) declaration.get("maxStack")).intValue();
				IList code = (IList) declaration.get("instructions");
				CodeBlock codeblock = new CodeBlock(null);

				// Loading instructions
				for(int i = 0; i < code.length(); i++) {
					IConstructor instruction = (IConstructor) code.get(i);
					String opcode = instruction.getName();

					switch(opcode) {
					case "LOADCON":
						codeblock.LOADCON(instruction.get("val"));
						break;
					case "LOADVAR":
						codeblock.LOADVAR(getIntField(instruction,"scope"), getIntField(instruction, "pos"));
						break;
					case "LOADLOC":
						codeblock.LOADLOC(getIntField(instruction,"pos"));
						break;
					case "STOREVAR":
						codeblock.STOREVAR(getIntField(instruction,"scope"), getIntField(instruction, "pos"));
						break;
					case "STORELOC":
						codeblock.STORELOC(getIntField(instruction,"pos"));
						break;
					case "LABEL":
						codeblock = codeblock.LABEL(getStrField(instruction,"label"));
						break;
					case "CALLPRIM":
						codeblock.CALLPRIM(Primitive.valueOf(getStrField(instruction,"name")));	
						break;
					case "CALL":
						codeblock.CALL(getStrField(instruction,"name"));
					case "CALLDYN":
						codeblock.CALLDYN();
						break;
					case "RETURN0":
						codeblock.RETURN0();
					case "RETURN1":
						codeblock.RETURN1();
						break;
					case "JMP":
						codeblock.JMP(getStrField(instruction,"label"));
						break;
					case "JMPTRUE":
						codeblock.JMPTRUE(getStrField(instruction,"label"));
						break;
					case "JMPFALSE":
						codeblock.JMPFALSE(getStrField(instruction,"label"));
						break;
					case "HALT":
						codeblock.HALT();
						break;
					case "CREATE":
						codeblock.CREATE(getStrField(instruction,"fun"));
						break;
					case "CREATEDYN":
						codeblock.CREATEDYN();
						break;
					case "INIT":
						codeblock.INIT();
						break;
					case "NEXT0":
						codeblock.NEXT0();
						break;
					case "NEXT1":
						codeblock.NEXT1();
						break;
					case "YIELD0":
						codeblock.YIELD0();
						break;
					case "YIELD1":
						codeblock.YIELD1();
						break;
					case "HASNEXT":
						codeblock.HASNEXT();
						break;	
					case "POP":
						codeblock.POP();
						break;
					default:
						throw new RuntimeException("PANIC: Unknown instruction: " + opcode + " has been used");
					}

				}
				rvm.declare(new Function(name, scope, nformals, nlocals, maxstack, codeblock));
			}
		}
		
		long start = System.currentTimeMillis();
		Object result = null;
		for(int i = 0; i < repeat.intValue(); i++)
			result = rvm.executeProgram(func, new IValue[] {});
		long now = System.currentTimeMillis();
		return vf.tuple((IValue)result, vf.integer(now - start));
	}
	
}
