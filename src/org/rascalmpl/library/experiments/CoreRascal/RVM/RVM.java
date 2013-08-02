package org.rascalmpl.library.experiments.CoreRascal.RVM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class RVM {
	List<Frame> frames;
	Map<String, Function> codeStore;
	ArrayList<IValue> constStore;
	IValueFactory vf;
	private IBool TRUE;
	private IBool FALSE;
	private boolean debug = false;
	private Map<String, Integer> constMap;

	RVM() {
		vf = ValueFactoryFactory.getValueFactory();
		TRUE = vf.bool(true);
		FALSE = vf.bool(false);
		constStore = new ArrayList<IValue>();
		codeStore = new HashMap<String, Function>();
		constMap = new HashMap<String, Integer>();
	}
	
	public static void main(String[] args) {
		RVM rvm = new RVM();
		rvm.loadProgram();
		long start = System.currentTimeMillis();
		rvm.executeProgram("main_repeat", new IValue[] {});
		long now = System.currentTimeMillis();
		System.out.println("RVM: elapsed time in msecs:" + (now - start));
	}
	
	void declare(Function f){
		codeStore.put(f.name, f);
	}
	
	void declareConst(String name, IValue val){
		constMap.put(name, constStore.size());
		constStore.add(val);
	}

	void def_main_test() {
		declare(new Function("main_test", 0, 0, 1, 6,
				new Instructions(constMap).
					add(OPCODE.LOADCON, "FOUR").
					add(OPCODE.STORELOC, 0).
					add(OPCODE.HALT).
					done()));
	}

	void def_fac() {
		declare(new Function("fac", 1, 1, 1, 6, 
				new Instructions(constMap).
					add(OPCODE.LOADLOC, 0).
					add(OPCODE.LOADCON, "ONE").
					add(OPCODE.CALLPRIM, Primitive.equal_int_int).
					add(OPCODE.JMPFALSE, "L").
					add(OPCODE.LOADCON, "ONE").
					add(OPCODE.RETURN).
					add(OPCODE.LABEL, "L").
					add(OPCODE.LOADLOC, 0).
					add(OPCODE.LOADLOC, 0).
					add(OPCODE.LOADCON, "ONE").
					add(OPCODE.CALLPRIM, Primitive.substraction_int_int).
					add(OPCODE.CALL, "fac").
					add(OPCODE.CALLPRIM, Primitive.multiplication_int_int).
					add(OPCODE.RETURN).
					done()));
		
		declare(new Function("main_fac", 0, 0, 0, 7,
				new Instructions(constMap).
					add(OPCODE.LOADCON, "THOUSAND").
					add(OPCODE.CALL, "fac").
					add(OPCODE.HALT).
					done()));
	}

	void def_main_repeat() {
		// void repeat(int n, int cnt) {
		// while (cnt > 0){
		// fac(n);
		// cnt -= 1;
		// }
		// }
		declare(new Function("main_repeat", 0, 0, 2, 20,
				new Instructions(constMap).
					add(OPCODE.LOADCON, "THOUSAND").
					add(OPCODE.STORELOC, 0). // n
					add(OPCODE.LOADCON, "THOUSAND").
					add(OPCODE.STORELOC, 1). // cnt
					add(OPCODE.LABEL, "L").
					add(OPCODE.LOADLOC, 1). // cnt
					add(OPCODE.LOADCON, "ZERO").
					add(OPCODE.CALLPRIM, Primitive.greater_int_int).
					add(OPCODE.JMPTRUE, "M").
					add(OPCODE.HALT).
					add(OPCODE.LABEL, "M").
					add(OPCODE.LOADLOC, 0).
					add(OPCODE.CALL, "fac").
					add(OPCODE.POP).
					add(OPCODE.LOADLOC, 1).
					add(OPCODE.LOADCON, "ONE").
					add(OPCODE.CALLPRIM, Primitive.substraction_int_int).
					add(OPCODE.STORELOC, 1).
					add(OPCODE.JMP, "L").
					done()));
	}

	void loadProgram() {
		// Given an ADT of the RVN program, store in internal format here

		declareConst("ZERO", vf.integer(0));
		declareConst("ONE", vf.integer(1));
		declareConst("TWO", vf.integer(2));
		declareConst("FOUR", vf.integer(4));
		declareConst("THOUSAND", vf.integer(1000));
		def_main_test();
		def_fac();
		def_main_repeat();
	}

	void executeProgram(String main, IValue[] args) {

		frames = new LinkedList<Frame>();
		// Simulate a call to "main" here.
		Function function = codeStore.get(main);
		if (function == null) {
			throw new RuntimeException("Code for main not found: " + main);
		}
		Frame cf = new Frame(0, null, function.maxstack, function);
		IValue[] stack = cf.stack;
		if (args.length != function.nformals) {
			throw new RuntimeException(main
					+ " called with wrong number of arguaments: " + args.length);
		}
		for (int i = 0; i < args.length; i++) {
			stack[i] = args[i];
		}
		frames.add(cf);
		Instruction[] instructions = function.instructions.getInstructions();
		int pc = 0;
		int sp = function.nlocals;

		NEXT_INSTRUCTION: while (true) {
			Instruction instruction = instructions[pc++];
			if (debug) {
				System.out.print("Stack:");
				for (int i = 0; i < sp; i++) {
					System.out.println("\t" + i + ": " + stack[i]);
				}
				System.out.println((pc - 1) + ": " + instruction.toString());
			}

			switch (instruction.getOp()) {

			case LOADCON:
				stack[sp++] = constStore.get(instruction.getIntArg(0));
				continue;

			case LOADLOC: {
					int pos = instruction.getIntArg(0);
					stack[sp++] = stack[pos];
					continue;
				}
			
			case LOADVAR: {
					int s = instruction.getIntArg(0);
					int pos = instruction.getIntArg(1);
					for (Frame fr = cf.previous; fr != null; fr = fr.previous) {
						if (fr.scope == s) {
							stack[sp++] = fr.stack[pos];
							continue NEXT_INSTRUCTION;
						}
					}
					throw new RuntimeException("Cannot happen: load var cannot find matching scope");
				}
			
			case STORELOC: {
					int pos = instruction.getIntArg(0);
					stack[pos] = stack[--sp];
					continue;
				}
			
			case STOREVAR:
				int s = instruction.getIntArg(0);
				int pos = instruction.getIntArg(1);
				
				for (Frame fr = cf.previous; fr != null; fr = fr.previous) {
					if (fr.scope == s) {
						fr.stack[pos] = stack[--sp];
						continue NEXT_INSTRUCTION;
					}
				}
				
				throw new RuntimeException("Cannot happen: load var cannot find matching scope");

			case JMP:
				pc = instruction.getIntArg(0);
				continue;

			case JMPTRUE:
				if (stack[sp - 1].equals(TRUE)) {
					pc = instruction.getIntArg(0);
				}
				sp--;
				continue;
				
			case JMPFALSE:
				if (stack[sp - 1].equals(FALSE)) {
					pc = instruction.getIntArg(0);
				}
				sp--;
				continue;
				
			case POP:
				sp--;
				continue;

			case LABEL:
				break;

			case CALL:
				String fname = instruction.getStringArg(0);
				Function fun = codeStore.get(fname);
				instructions = fun.instructions.getInstructions();
				Frame nextFrame = new Frame(fun.scope, cf, fun.maxstack, fun);
				for (int i = 0; i < fun.nformals; i++) {
					nextFrame.stack[i] = stack[sp - i - 1];
				}
				cf.pc = pc;
				cf.sp = sp - fun.nlocals;
				cf = nextFrame;
				stack = cf.stack;
				sp = fun.nlocals;
				pc = 0;
				continue;

			case RETURN:
				IValue rval = stack[sp - 1];
				cf = cf.previous;
				if (cf == null)
					return;
				instructions = cf.function.instructions.getInstructions();
				stack = cf.stack;
				sp = cf.sp;
				pc = cf.pc;
				stack[sp++] = rval;
				continue;

			case CALLPRIM:
				switch (instruction.getPrimitiveArg(0)) {
				case addition_int_int:
					stack[sp - 2] = ((IInteger) stack[sp - 2]).add((IInteger) stack[sp - 1]);
					sp--;
					continue;
				case multiplication_int_int:
					stack[sp - 2] = ((IInteger) stack[sp - 2]).multiply((IInteger) stack[sp - 1]);
					sp--;
					continue;
				case equal_int_int:
					stack[sp - 2] = ((IInteger) stack[sp - 2]).equal((IInteger) stack[sp - 1]).getValue() ? TRUE : FALSE;
					sp--;
					continue;
				case greater_int_int:
					stack[sp - 2] = ((IInteger) stack[sp - 2]).greater((IInteger) stack[sp - 1]).getValue() ? TRUE : FALSE;
					sp--;
					continue;
				case substraction_int_int:
					stack[sp - 2] = ((IInteger) stack[sp - 2]).subtract((IInteger) stack[sp - 1]);
					sp--;
					continue;
				default:
					break;
				}
				
			case HALT:
				if (debug) {
					System.out.println("Program halted:");
					for (int i = 0; i < sp; i++) {
						System.out.println(i + ": " + stack[i]);
					}
				}
				return;
				
			default:
				throw new RuntimeException(
						"Cannot happen: cannot decode instruction");
			}
		}
	}

	

}
