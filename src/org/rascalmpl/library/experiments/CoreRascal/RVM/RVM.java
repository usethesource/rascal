package org.rascalmpl.library.experiments.CoreRascal.RVM;

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
	Map<String,Function> codeStore;
	Map<String,IValue> constStore;
	IValueFactory vf;
	private Frame mainFrame;
	private IBool TRUE;
	private IBool FALSE;
	
	RVM(){
		vf = ValueFactoryFactory.getValueFactory();
		TRUE = vf.bool(true);
		FALSE = vf.bool(false);
		constStore = new HashMap<String, IValue>();
		codeStore = new HashMap<String, Function>();
	}
	
	void loadProgram(){
		// Given an ADT of the RVN program, store in internal format here
		
		constStore.put("ONE", vf.integer(1));
		constStore.put("FOUR", vf.integer(4));
		
		Instruction[] facInstructions = new Instruction[]{
				new Instruction(OPCODE.LOADVAR, 1, 0),
				new Instruction(OPCODE.LOADCON, "ONE"),
				new Instruction(OPCODE.CALLPRIM, Primitive.equal_int_int),
				new Instruction(OPCODE.JMPFALSE, "L"),
				new Instruction(OPCODE.LOADVAR, 1, 0),
				new Instruction(OPCODE.RETURN),
				new Instruction(OPCODE.LABEL, "L"),
				new Instruction(OPCODE.LOADVAR, 1, 0),
				new Instruction(OPCODE.LOADVAR, 1, 0),
				new Instruction(OPCODE.LOADCON, "ONE"),
				new Instruction(OPCODE.CALLPRIM, Primitive.substraction_int_int),
				new Instruction(OPCODE.CALL, "fac"),
				new Instruction(OPCODE.CALLPRIM, Primitive.multiplication_int_int),
				new Instruction(OPCODE.RETURN)				
		};
		Function facFunction = new Function("fac", 1, 1, 5, facInstructions);
		codeStore.put("fac", facFunction);
		Instruction[] facCallInstructions = new Instruction[]{
				new Instruction(OPCODE.LOADCON, "ONE"),
				new Instruction(OPCODE.LOADCON, "ONE"),
				new Instruction(OPCODE.CALLPRIM, Primitive.equal_int_int),
				new Instruction(OPCODE.JMPTRUE, "L"),
				new Instruction(OPCODE.LOADCON, "ONE"),
				new Instruction(OPCODE.LABEL, "L"),
				new Instruction(OPCODE.LOADCON, "FOUR"),
				new Instruction(OPCODE.HALT),
				new Instruction(OPCODE.CALL, "fac"),
				new Instruction(OPCODE.HALT)
		};
		Function facCallFunction = new Function("main", 1, 1, 5, facCallInstructions);
		codeStore.put("main", facCallFunction);
		mainFrame =  new Frame(0, null, 3, facCallFunction);
	}
	
	int findLabel(Instruction[] instructions, String label){
		for(int i = 0; i < instructions.length; i++){
			Instruction ins = instructions[i];
			if(ins.op == OPCODE.LABEL && ins.getStringArg(0).equals(label))
				return i;
		}
		throw new RuntimeException("Cannot happen: undefined label: " + label);
	}
	void executeProgram(){
		
		frames = new LinkedList<Frame>();
		// Simulate a call to "fac" here.
		Frame cf = mainFrame;
		frames.add(cf);
		Instruction[] instructions = cf.function.instructions;
		int pc = 0;
		IValue[] stack = cf.stack;
		int sp = 0;
		NEXT_INSTRUCTION:
		while(true){
			Instruction instruction = instructions[pc++];
			System.out.println((pc -1) + ": " + instruction);
			
			switch(instruction.getOp()){
			
			case LOADCON:
				cf.stack[sp++] = constStore.get(instruction.getStringArg(0));
				continue;
				
			case LOADVAR:
				int s = instruction.getIntArg(0);
				int pos = instruction.getIntArg(1);
				if(s == cf.scope){
					stack[sp++] = stack[pos];
					continue;
				} else {
					for(Frame fr = cf.previous; fr != null; fr = fr.previous){
						if(fr.scope == s){
							stack[sp++] = fr.stack[pos];
							continue NEXT_INSTRUCTION;
						}
					}
				}
				throw new RuntimeException("Cannot happen: load var cannot find matching scope");
			
			case STOREVAR:
				break;
				
			case JMP:
				pc = findLabel(instructions, instruction.getStringArg(0)); 
				continue;
			case JMPTRUE:
				if(cf.stack[sp - 1].equals(TRUE)){
					pc = findLabel(instructions, instruction.getStringArg(0)); 
				} 
				sp--;
				continue;
			case LABEL:
				continue;
			case CALL:
				String fname = instruction.getStringArg(0);
				Function fun = codeStore.get(fname);
				Frame nextFrame = new Frame(0, cf, fun.maxstack, fun);
				for(int i = 0; i < fun.nformals; i++){
					nextFrame.stack[i] = stack[sp - i - 1];
				}
				cf.pc = pc;
				cf.sp = sp - fun.nlocals;
				cf = nextFrame;
				sp = fun.nlocals;
				pc = 0;
				continue;
				
			case RETURN:
				IValue rval = stack[sp - 1];
				cf = cf.previous;
				stack = cf.stack;
				sp = cf.sp;
				pc = cf.pc;
				stack[sp++] = rval;
				continue;
				
			case CALLPRIM:
				switch(instruction.getPrimitiveArg(0)){
				case addition_int_int:
					stack[sp - 2] = ((IInteger) stack[sp-2]).add((IInteger) stack[sp-1]); sp--; continue;
				case multiplication_int_int:
					stack[sp - 2] = ((IInteger) stack[sp-2]).multiply((IInteger) stack[sp-1]); sp--; continue;
				case equal_int_int:
					stack[sp - 2] = ((IInteger) stack[sp-2]).equal((IInteger) stack[sp-1]).getValue() ? TRUE : FALSE; 
					sp--; continue;
				case substraction_int_int:
					stack[sp - 2] = ((IInteger) stack[sp-2]).subtract((IInteger) stack[sp-1]); sp--; continue;
				default:
					break;
				}
			case HALT:
				System.out.println("Program halted:");
				for(int i = 0; i < sp; i++){
					System.out.println(i + ": " + stack[i]);
				}
				return;
			default:
				throw new RuntimeException("Cannot happen: cannot decode instruction");
			
			}
		}
	}
	
	public static void main(String[] args){
		RVM rvm = new RVM();
		rvm.loadProgram();
		rvm.executeProgram();
	}

}
