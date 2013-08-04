package org.rascalmpl.library.experiments.CoreRascal.RVM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.LoadCon;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Opcode;
//import org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions.OPCODE;
import org.rascalmpl.values.ValueFactoryFactory;

public class RVM {

	public IValueFactory vf;
	private IBool TRUE;
	private IBool FALSE;
	private boolean debug = true;
	private boolean listing = false;
	
	private Map<String, Integer> constMap;
	ArrayList<IValue> constStore;
	
	private ArrayList<Function> codeStore;
	private Map<String, Integer> codeMap;

	public RVM() {
		vf = ValueFactoryFactory.getValueFactory();
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
	
	public void setListing(boolean b){
		listing = b;
	}
	
	public void executeProgram(String main, IValue[] args) {

		for(Function f : codeStore){
			f.instructions.done(f.name, constMap, codeMap, false);
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
					nextFrame.stack[i] = stack[sp - i - 1];
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
					return;
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
				return;
				
			default:
				throw new RuntimeException("Cannot happen: RVM main loop -- cannot decode instruction");
			}
		}
	}

	

}
