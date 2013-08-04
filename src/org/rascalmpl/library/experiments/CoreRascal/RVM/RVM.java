package org.rascalmpl.library.experiments.CoreRascal.RVM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions.Instructions;
import org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions.Opcode;
import org.rascalmpl.values.ValueFactoryFactory;
//import org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions.OPCODE;

public class RVM {

	public IValueFactory vf;
	private IBool TRUE;
	private IBool FALSE;
	private boolean debug = true;
	
	private Map<String, Integer> constMap;
	ArrayList<IValue> constStore;
	
	private ArrayList<Function> codeStore;
	private Map<String, Integer> codeMap;

	public RVM(IValueFactory vf) {
		this.vf = vf;
		TRUE = vf.bool(true);
		FALSE = vf.bool(false);
		constStore = new ArrayList<IValue>();
		codeStore = new ArrayList<Function>();
		constMap = new HashMap<String, Integer>();
		codeMap = new HashMap<String, Integer>();
	}
	
	public void declare(Function f){
		codeMap.put(f.name, codeStore.size());
		codeStore.add(f);
	}
	
	public void declareRecursive(String name){
		codeMap.put(name, codeStore.size());  // hack to ensure that recursive fac is defined
	}
	
	public void declareConst(String name, IValue val){
		constMap.put(name, constStore.size());
		constStore.add(val);
	}
	
	public void setDebug(boolean b){
		debug = b;
	}
	
	public IValue executeProgram(String main, IValue[] args) {

		for(Function f : codeStore){
			f.instructions.done(f.name, constMap, codeMap);
		}
		// Simulate a call to "main" here.
		Function function = codeStore.get(codeMap.get(main));
		if (function == null) {
			throw new RuntimeException("Code for main not found: " + main);
		}
		Frame cf = new Frame(0, null, function.maxstack, function);
		IValue[] stack = cf.stack;
		if (args.length != function.nformals) {
			throw new RuntimeException(main	+ " called with wrong number of arguaments: " + args.length);
		}
		for (int i = 0; i < args.length; i++) {
			stack[i] = args[i];
		}
		
		int[] instructions = function.instructions.getInstructions();
		int pc = 0;
		int sp = function.nlocals;

		NEXT_INSTRUCTION: while (true) {
			int op = instructions[pc++];
			if (debug) {
				int startpc = pc -1;
				for (int i = 0; i < sp; i++) {
					System.out.println("\t\t" + i + ": " + stack[i]);
				}
				System.out.println(cf.function.name + "[" + startpc + "] " + cf.function.instructions.toString(startpc));
			}
			
			switch (op) {

			case Opcode.OP_LOADCON:
				stack[sp++] = constStore.get(instructions[pc++]);
				continue;

			case Opcode.OP_LOADLOC:
					stack[sp++] = stack[instructions[pc++]];
					continue;
			
			case Opcode.OP_LOADVAR: {
					int s = instructions[pc++];
					int pos = instructions[pc++];
					for (Frame fr = cf.previous; fr != null; fr = fr.previous) {
						if (fr.scope == s) {
							stack[sp++] = fr.stack[pos];
							continue NEXT_INSTRUCTION;
						}
					}
					throw new RuntimeException("Cannot happen: load var cannot find matching scope");
				}
			
			case Opcode.OP_STORELOC: {
					stack[instructions[pc++]] = stack[--sp];
					continue;
				}
			
			case Opcode.OP_STOREVAR:
				int s = instructions[pc++];
				int pos = instructions[pc++];
				
				for (Frame fr = cf.previous; fr != null; fr = fr.previous) {
					if (fr.scope == s) {
						fr.stack[pos] = stack[--sp];
						continue NEXT_INSTRUCTION;
					}
				}
				
				throw new RuntimeException("Cannot happen: load var cannot find matching scope");

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
				throw new RuntimeException("Cannot happen: label instruction at runtime");

			case Opcode.OP_CALL:
				Function fun = codeStore.get(instructions[pc++]);
				instructions = fun.instructions.getInstructions();
				Frame nextFrame = new Frame(fun.scope, cf, fun.maxstack, fun);
				for (int i = 0; i < fun.nformals; i++) {
					nextFrame.stack[i] = stack[sp - fun.nformals + i];
				}
				cf.pc = pc;
				cf.sp = sp - fun.nlocals;
				cf = nextFrame;
				stack = cf.stack;
				sp = fun.nlocals;
				pc = 0;
				//if(debug) System.out.println("Enter " + fun.name);
				continue;

			case Opcode.OP_RETURN:
				//if(debug)System.out.println("Leave " + cf.function.name + ", back in " + cf.previous.function.name);
				IValue rval = stack[sp - 1];
				cf = cf.previous;
				if (cf == null)
					return rval;
				instructions = cf.function.instructions.getInstructions();
				stack = cf.stack;
				sp = cf.sp;
				pc = cf.pc;
				stack[sp++] = rval;
				continue;

			case Opcode.OP_CALLPRIM:
				switch (instructions[pc++]) {
				case Primitive.addition_int_int:
					stack[sp - 2] = ((IInteger) stack[sp - 2]).add((IInteger) stack[sp - 1]);
					sp--;
					continue;
				case Primitive.multiplication_int_int:
					stack[sp - 2] = ((IInteger) stack[sp - 2]).multiply((IInteger) stack[sp - 1]);
					sp--;
					continue;
				case Primitive.equal_int_int:
					stack[sp - 2] = ((IInteger) stack[sp - 2]).equal((IInteger) stack[sp - 1]).getValue() ? TRUE : FALSE;
					sp--;
					continue;
				case Primitive.greater_int_int:
					stack[sp - 2] = ((IInteger) stack[sp - 2]).greater((IInteger) stack[sp - 1]).getValue() ? TRUE : FALSE;
					sp--;
					continue;
				case Primitive.substraction_int_int:
					stack[sp - 2] = ((IInteger) stack[sp - 2]).subtract((IInteger) stack[sp - 1]);
					sp--;
					continue;
				default:
					throw new RuntimeException("Cannot happen: unknown primitive  " + instructions[pc-1]);
				}
				
			case Opcode.OP_HALT:
				if (debug) {
					System.out.println("Program halted:");
					for (int i = 0; i < sp; i++) {
						System.out.println(i + ": " + stack[i]);
					}
				}
				return stack[sp - 1];
				
			default:
				throw new RuntimeException("Cannot happen: RVM main loop -- cannot decode instruction");
			}
		}
	}

	public ITuple executeProgram(IList directives, IInteger repeats, IEvaluatorContext ctx) {
		String func = "main";
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		List<IValue> functions = new ArrayList<>();
		for(IValue directive : directives) {
			String constr = ((IConstructor) directive).getName();
			String name = null;
			
			// Loading constants
			if(constr.equals("intconst")) {
				name = ((IString) ((IConstructor) directive).get("value")).getValue();
				rvm.declareConst(name, rvm.vf.integer(name));
			} else if(constr.equals("relconst")) {
				name = ((IString) ((IConstructor) directive).get("value")).getValue();
				rvm.declareConst(name, rvm.vf.real(name));
			} else if(constr.equals("ratconst")) {
				name = ((IString) ((IConstructor) directive).get("value")).getValue();
				rvm.declareConst(name, rvm.vf.rational(name));
			} else if(constr.equals("boolconst")) {
				name = ((IString) ((IConstructor) directive).get("value")).getValue();
				if(name.equals("TRUE")) {
					rvm.declareConst(name, rvm.TRUE);
				} else if(name.equals("FALSE")) {
					rvm.declareConst(name, rvm.FALSE);
				}
			} else if(constr.equals("function")) {
				functions.add(directive);
			} else {
				throw new RuntimeException("Unknown directive: " + constr);
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
			Instructions instructions = new Instructions();
			
			// Loading instructions
			for(int i = 0; i < code.length(); i++) {
				IConstructor instruction = (IConstructor) code.get(i);
				String opcode = ((IString) instruction.get("opcode")).getValue();
				IList operands = (IList) instruction.get("operands");
				
				if(opcode.equals("LOADCON")) {
					instructions = instructions.loadcon(((IString) operands.get(0)).getValue());
				} else if(opcode.equals("LOADVAR")) {
					instructions = instructions.loadvar(Integer.parseInt(((IString) operands.get(0)).getValue()), 
														Integer.parseInt(((IString) operands.get(1)).getValue()));
				} else if(opcode.equals("LOADLOC")) {
					instructions = instructions.loadloc(Integer.parseInt(((IString) operands.get(0)).getValue()));
				} else if(opcode.equals("STOREVAR")) {
					instructions = instructions.storevar(Integer.parseInt(((IString) operands.get(0)).getValue()), 
														 Integer.parseInt(((IString) operands.get(1)).getValue()));
				} else if(opcode.equals("STORELOC")) {
					instructions = instructions.storeloc(Integer.parseInt(((IString) operands.get(0)).getValue()));
				} else if(opcode.equals("LABEL")) {
					instructions = instructions.label(((IString) operands.get(0)).getValue());
				} else if(opcode.equals("CALLPRIM")) {
					String operand = ((IString) operands.get(0)).getValue();
					
					if(operand.equals("addition_int_int")) {
						instructions = instructions.callprim(Primitive.addition_int_int);
					} else if(operand.equals("equal_int_int")) {
						instructions = instructions.callprim(Primitive.equal_int_int);
					} else if(operand.equals("greater_int_int")) {
						instructions = instructions.callprim(Primitive.greater_int_int);
					} else if(operand.equals("multiplication_int_int")) {
						instructions = instructions.callprim(Primitive.multiplication_int_int);
					} else if(operand.equals("substraction_int_int")) {
						instructions = instructions.callprim(Primitive.substraction_int_int);
					} else {
						throw new RuntimeException("Unknown primitive operation: " + operand);
					}
					
				} else if(opcode.equals("CALL")) {
					instructions = instructions.call(((IString) operands.get(0)).getValue());
				} else if(opcode.equals("RETURN")) {
					instructions = instructions.ret();
				} else if(opcode.equals("JMP")) {
					instructions = instructions.jmp(((IString) operands.get(0)).getValue());
				} else if(opcode.equals("JMPTRUE")) {
					instructions = instructions.jmptrue(((IString) operands.get(0)).getValue());
				} else if(opcode.equals("JMPFALSE")) {
					instructions = instructions.jmpfalse(((IString) operands.get(0)).getValue());
				} else if(opcode.equals("HALT")) {
					instructions = instructions.halt();
				} else { 
					throw new RuntimeException("Unknown instruction: " + opcode + " has been used");
				}
				
			}
			rvm.declare(new Function(name, scope, nformals, nlocals, maxstack, instructions));
		}
		
		long start = System.currentTimeMillis();
		IValue result = null;
		for(int i = 0; i < repeats.intValue(); i++)
			result = rvm.executeProgram(func, new IValue[] {});
		long now = System.currentTimeMillis();
		return vf.tuple(result, vf.integer(now - start));

	}

}
