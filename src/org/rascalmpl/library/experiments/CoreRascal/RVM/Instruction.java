package org.rascalmpl.library.experiments.CoreRascal.RVM;

public class Instruction {
    final OPCODE op;
    final private Object[] args;
    
    Instruction(OPCODE op){
    	this.op = op;
    	this.args = null;
    }
    
    Instruction(OPCODE op, String arg){
    	this.op = op;
    	this.args = new Object[] {arg};
    }
    
    Instruction(OPCODE op, int arg){
    	this.op = op;
    	this.args = new Object[] {arg};
    }
    
    Instruction(OPCODE op, int arg1, int arg2){
    	this.op = op;
    	this.args = new Object[] {arg1, arg2};
    }
    
    public Instruction(OPCODE op, Primitive arg) {
		this.op = op;
		this.args = new Object[] { arg };
	}

	OPCODE getOp() {
    	return op;
    }
    
    int getIntArg(int n){
    	return (int) args[n];
    }
    
    String getStringArg(int n){
    	return (String) args[n];
    }
    
    Primitive getPrimitiveArg(int n){
    	return (Primitive) args[n];
    }
    
    public String toString(){
    	String sargs = "";
    	switch(op){
		case CALL:
		case JMP:
		case JMPFALSE:
		case JMPTRUE:
		case LABEL:
		case LOADCON:
			sargs = getStringArg(0); 
			break;
		case CALLPRIM:
			sargs = "" + getPrimitiveArg(0); 
			break;
		case LOADVAR:
		case STOREVAR:
			sargs = getIntArg(0) + ", " + getIntArg(1);
			break;
		default:	
    	}
    	return op + " " + sargs;
    }
}
