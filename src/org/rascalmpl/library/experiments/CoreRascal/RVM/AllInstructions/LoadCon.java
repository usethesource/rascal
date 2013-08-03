package org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions;

public class LoadCon extends Instruction {

	String constant;
	
	LoadCon(Instructions ins, String constant){
		super(ins, Opcode.LOADCON);
		this.constant = constant;
	}
	
	public String toString() { return "LOADCON " + constant + "[" + ins.constMap.get(constant) + "]"; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		Object o = ins.constMap.get(constant);
		if(o == null){
			throw new RuntimeException("Cannot happen: undefined constant " + constant);
		}
		ins.addCode((int)o);
	}

}
