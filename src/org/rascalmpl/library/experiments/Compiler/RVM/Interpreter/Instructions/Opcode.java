package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Primitive;

public enum Opcode {
	
	/*
	 * OPCODENAME(opcode, pc_increment)
	 */
	LOADCON (0, 2),
	LOADVAR (1, 3),
	LOADLOC (2, 2),
	STOREVAR (3, 3),
	STORELOC (4, 2),
	CALL (5, 2),
	CALLPRIM (6, 2), // TODO: plus number of formal parameters
	RETURN1 (7, 1),
	JMP (8, 2),
	JMPTRUE (9, 2),
	JMPFALSE (10, 2),
	LABEL (11, 0),
	HALT (12, 1),
	POP (13, 1),
	CALLDYN(14,1),
	LOADFUN(15,2), // TODO: to be renamed to LOAD_ROOT_FUN
	CREATE(16,2),
	NEXT0(17,1),
	NEXT1(18,1),
	YIELD0(19,1),
	YIELD1(20,1),
	INIT(21,1),
	CREATEDYN(22,1),
	HASNEXT(23,1),
	PRINTLN(24,1),
	RETURN0(25,1),
	LOADLOC_AS_REF(26,2),
	LOADVAR_AS_REF(27,3),
	LOADLOCREF(28,2),
	LOADVARREF(29,3),
	STORELOCREF(30,2),
	STOREVARREF(31,3),
	LOADCONSTR(32,2),
	CALLCONSTR(33,2), // TODO: plus number of formal parameters
	LOAD_NESTED_FUN(34, 3),
	LOADTYPE(35,2)
	;
	
	private final int op;
	private final int incr;
	
	private final static Opcode[] values = Opcode.values();
	
	public static Opcode fromInteger(int n){
		return values[n];
	}
	
	// TODO: compiler does not like Opcode.LOADCON.getOpcode() in case expressions
	// Here is a -- hopefully temporary -- hack that introduces explicit constants:
	// Beware! Should be in sync with the above operator codes!
	public static final int OP_LOADCON = 0;
	static public final int OP_LOADVAR = 1;
	static public final int OP_LOADLOC = 2;
	static public final int OP_STOREVAR= 3;
	static public final int OP_STORELOC = 4;
	static public final int OP_CALL = 5;
	static public final int OP_CALLPRIM = 6;
	static public final int OP_RETURN1 = 7;
	static public final int OP_JMP = 8;
	static public final int OP_JMPTRUE = 9;
	static public final int OP_JMPFALSE = 10;
	static public final int OP_LABEL = 11;
	static public final int OP_HALT = 12;
	static public final int OP_POP = 13;
	static public final int OP_CALLDYN = 14;
	static public final int OP_LOADFUN = 15;	
	static public final int OP_CREATE = 16;
	static public final int OP_NEXT0 = 17;
	static public final int OP_NEXT1 = 18;
	static public final int OP_YIELD0 = 19;
	static public final int OP_YIELD1 = 20;
	static public final int OP_INIT = 21;
	static public final int OP_CREATEDYN = 22;
	static public final int OP_HASNEXT = 23;
	static public final int OP_PRINTLN = 24;
	static public final int OP_RETURN0 = 25;
	static public final int OP_LOADLOC_AS_REF = 26;
	static public final int OP_LOADVAR_AS_REF = 27;
	static public final int OP_LOADLOCREF = 28;
	static public final int OP_LOADVARREF = 29;
	static public final int OP_STORELOCREF = 30;
	static public final int OP_STOREVARREF = 31;
	static public final int OP_LOADCONSTR = 32;
	static public final int OP_CALLCONSTR = 33;
	static public final int OP_LOAD_NESTED_FUN = 34;
	static public final int OP_LOADTTYPE = 35;
	
	 Opcode(int op, int incr){
		this.op = op;
		this.incr = incr;
	}
	
	public int getIncrement(){
		return incr;
	}
	
	public int getOpcode(){
		return op;
	}
	
	public static String toString(CodeBlock cb, Opcode opc, int pc){
		switch(opc){
		case LOADCON:
			return "LOADCON " + cb.finalCode[pc + 1]  + " [" + cb.getConstantValue(cb.finalCode[pc + 1]) + "]";
			
		case LOADVAR:
			return "LOADVAR " + cb.finalCode[pc + 1] + ", " + cb.finalCode[pc + 2];
			
		case LOADLOC:
			return "LOADLOC " + cb.finalCode[pc + 1];
			
		case STOREVAR:
			return "STOREVAR " + cb.finalCode[pc + 1] + ", " + cb.finalCode[pc + 2];	
			
		case STORELOC:
			return "STORELOC " + cb.finalCode[pc + 1];
			
		case CALL:
			return "CALL " + cb.finalCode[pc + 1]  + " [" + cb.getFunctionName(cb.finalCode[pc + 1]) + "]";
			
		case CALLPRIM:
			return "CALLPRIM " + cb.finalCode[pc + 1] + " [" + Primitive.fromInteger(cb.finalCode[pc + 1]).name() + "]";
			
		case RETURN1:
			return "RETURN1";
			
		case JMP:
			return "JMP " + cb.finalCode[pc + 1];
			
		case JMPTRUE:
			return "JMPTRUE " + cb.finalCode[pc + 1];
			
		case JMPFALSE:
			return "JMPFALSE " + cb.finalCode[pc + 1];
			
		case LABEL:
			break;
			
		case HALT:
			return "HALT";
			
		case POP: 
			return "POP";	
			
		case CALLDYN:
			return "CALLDYN";
			
		case LOADFUN:
			return "LOADFUN " + cb.finalCode[pc + 1]  + " [" + cb.getFunctionName(cb.finalCode[pc + 1]) + "]";
			
		case CREATE:
			return "CREATE " + cb.finalCode[pc + 1] + " [" + cb.getFunctionName(cb.finalCode[pc + 1]) + "]";
			
		case NEXT0:
			return "NEXT0";
			
		case NEXT1:
			return "NEXT1";
			
		case YIELD0:
			return "YIELD0";
		
		case YIELD1:
			return "YIELD1";
		
		case INIT:
			return "INIT";
		
		case CREATEDYN:
			return "CREATEDYN";
			
		case HASNEXT:
			return "HASNEXT";
			
		case PRINTLN:
			return "PRINTLN";
		
		case RETURN0:
			return "RETURN0";
		
		case LOADLOC_AS_REF:
			return "LOADLOC_AS_REF " + cb.finalCode[pc + 1];
			
		case LOADVAR_AS_REF:
			return "LOADVAR_AS_REF " + cb.finalCode[pc + 1] + ", " + cb.finalCode[pc + 2];
		
		case LOADLOCREF:
			return "LOADLOCREF " + cb.finalCode[pc + 1];
			
		case LOADVARREF:
			return "LOADVARREF " + cb.finalCode[pc + 1] + ", " + cb.finalCode[pc + 2];
			
		case STORELOCREF:
			return "STORELOCREF " + cb.finalCode[pc + 1];
		
		case STOREVARREF:
			return "STOREVARREF " + cb.finalCode[pc + 1] + ", " + cb.finalCode[pc + 2];
			
		case LOADCONSTR:
			return "LOADCONSTR " + cb.finalCode[pc + 1];
		
		case CALLCONSTR:
			return "CALLCONSTR " + cb.finalCode[pc + 1];
		
		case LOAD_NESTED_FUN:
			return "LOAD_NESTED_FUN " + cb.finalCode[pc + 1] + cb.finalCode[pc + 2];
			
		case LOADTYPE:
			return "LOADTYPE " + cb.finalCode[pc + 1];
			
		default:
			break;
		}	
		
		throw new RuntimeException("PANIC: unrecognized opcode " + opc);
	}
}
