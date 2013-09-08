package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.MuPrimitive;



public enum Opcode {
	/*
	 * Instructions for the RVM. Each instruction has 
	 * 	- a unique opcode
	 * 	- a pc increment, i.e., the number code elements for this instruction
	 * 	- a sp increment, indicating its effect on the stack. Instructions marked 
	 * 	  with -1000 have an "arity" field that determines their stack effect.
	 * 
	 * OPCODENAME(	opcode,	pc,		sp)
	 * 						incr	incr
	 */
	LOADCON			(0, 	2, 		1),
	LOADVAR 		(1, 	3, 		1),
	LOADLOC 		(2,		2, 		1),
	STOREVAR 		(3, 	3, 		0),
	STORELOC 		(4, 	2, 		0),
	CALL 			(5, 	3, 		-1000),	// -1000 marks  varyadic instruction
	CALLPRIM 		(6, 	3, 		-1000),
	RETURN1 		(7, 	1, 		-1),
	JMP 			(8, 	2, 		0),
	JMPTRUE 		(9, 	2, 		-1),
	JMPFALSE 		(10, 	2, 		-1),
	LABEL 			(11, 	0, 		0),
	HALT 			(12, 	1, 		0),
	POP 			(13, 	1, 		-1),
	CALLDYN			(14,	2, 		-1000),
	LOADFUN			(15,	2, 		1), // TODO: to be renamed to LOAD_ROOT_FUN
	CREATE			(16,	3, 		-1000),
	NEXT0			(17,	1, 		0),
	NEXT1			(18,	1, 		1),
	YIELD0			(19,	1, 		0),
	YIELD1			(20,	1, 		1),
	INIT			(21,	2, 		-1000),
	CREATEDYN		(22,	2, 		-1000),
	HASNEXT			(23,	1, 		-1),
	PRINTLN			(24,	2, 		-1000),
	RETURN0			(25,	1, 		0),
	LOADLOCREF		(26,	2, 		1),
	LOADVARREF		(27,	3, 		1),
	LOADLOCDEREF	(28,	2, 		1),
	LOADVARDEREF	(29,	3, 		1),
	STORELOCDEREF	(30,	2, 		-1),
	STOREVARDEREF	(31,	3, 		-1),
	LOADCONSTR		(32,	2, 		1),
	CALLCONSTR		(33,	3, 		-1000), // TODO: plus number of formal parameters
	LOAD_NESTED_FUN	(34, 	3, 		1),
	LOADTYPE		(35,	2, 		1),
	CALLMUPRIM		(36,	3, 		-1000),
	LOADBOOL		(37,	2, 		1),
	LOADINT			(38,	2, 		1),
	DUP				(39, 	1, 		1),
	FAILRETURN		(40, 	1, 		0),
	LOADOFUN        (41,    2,      1),
	OCALL           (42,    3,      -1000),
	OCALLDYN	    (43,	2, 		-1000),
	;
	
	
	
	private final int op;
	private final int pc_incr;
	private final int sp_incr;
	
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
	static public final int OP_LOADLOCREF = 26;
	static public final int OP_LOADVARREF = 27;
	static public final int OP_LOADLOCDEREF = 28;
	static public final int OP_LOADVARDEREF = 29;
	static public final int OP_STORELOCDEREF = 30;
	static public final int OP_STOREVARDEREF = 31;
	static public final int OP_LOADCONSTR = 32;
	static public final int OP_CALLCONSTR = 33;
	static public final int OP_LOAD_NESTED_FUN = 34;
	static public final int OP_LOADTYPE = 35;
	static public final int OP_CALLMUPRIM = 36;
	static public final int OP_LOADBOOL = 37;
	static public final int OP_LOADINT = 38;
	static public final int OP_DUP = 39;
	static public final int OP_FAILRETURN = 40;
	static public final int OP_LOADOFUN = 41;
	static public final int OP_OCALL = 42;
	static public final int OP_OCALLDYN = 43;
	
	 Opcode(int op, int pc_incr, int sp_incr){
		this.op = op;
		this.pc_incr = pc_incr;
		this.sp_incr = sp_incr;
	}
	
	public int getPcIncrement(){
		return pc_incr;
	}
	
	public int getSpIncrement(){
		return sp_incr;
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
			return "CALL " + cb.finalCode[pc + 1]  + ", " + cb.finalCode[pc + 2] + " [" + cb.getFunctionName(cb.finalCode[pc + 1]) + "]";
			
		case CALLPRIM:
			return "CALLPRIM " + cb.finalCode[pc + 1] +  ", " + cb.finalCode[pc + 2] + " [" + RascalPrimitive.fromInteger(cb.finalCode[pc + 1]).name() + "]";
			
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
			return "CALLDYN " + cb.finalCode[pc + 1];
			
		case LOADFUN:
			return "LOADFUN " + cb.finalCode[pc + 1]  + " [" + cb.getFunctionName(cb.finalCode[pc + 1]) + "]";
			
		case CREATE:
			return "CREATE " + cb.finalCode[pc + 1] + " [" + cb.getFunctionName(cb.finalCode[pc + 1]) + ", " + cb.finalCode[pc + 2] + "]";
			
		case NEXT0:
			return "NEXT0";
			
		case NEXT1:
			return "NEXT1";
			
		case YIELD0:
			return "YIELD0";
		
		case YIELD1:
			return "YIELD1";
		
		case INIT:
			return "INIT " + cb.finalCode[pc + 1];
		
		case CREATEDYN:
			return "CREATEDYN " + cb.finalCode[pc + 1];
			
		case HASNEXT:
			return "HASNEXT";
			
		case PRINTLN:
			return "PRINTLN " + cb.finalCode[pc + 1];
		
		case RETURN0:
			return "RETURN0";
		
		case LOADLOCREF:
			return "LOADLOCREF " + cb.finalCode[pc + 1];
			
		case LOADVARREF:
			return "LOADVARREF " + cb.finalCode[pc + 1] + ", " + cb.finalCode[pc + 2];
		
		case LOADLOCDEREF:
			return "LOADLOCDEREF " + cb.finalCode[pc + 1];
			
		case LOADVARDEREF:
			return "LOADVARDEREF " + cb.finalCode[pc + 1] + ", " + cb.finalCode[pc + 2];
			
		case STORELOCDEREF:
			return "STORELOCDEREF " + cb.finalCode[pc + 1];
		
		case STOREVARDEREF:
			return "STOREVARDEREF " + cb.finalCode[pc + 1] + ", " + cb.finalCode[pc + 2];
			
		case LOADCONSTR:
			return "LOADCONSTR " + cb.finalCode[pc + 1];
		
		case CALLCONSTR:
			return "CALLCONSTR " + cb.finalCode[pc + 1] + ", " + cb.finalCode[pc + 2];
		
		case LOAD_NESTED_FUN:
			return "LOAD_NESTED_FUN " + cb.finalCode[pc + 1] + ", " + cb.finalCode[pc + 2];
			
		case LOADTYPE:
			return "LOADTYPE " + cb.finalCode[pc + 1];
			
		case CALLMUPRIM:
			return "CALLMUPRIM " + cb.finalCode[pc + 1] +  ", " + cb.finalCode[pc + 2] + " [" + MuPrimitive.fromInteger(cb.finalCode[pc + 1]).name() + "]";
			
		case LOADBOOL:
			return "LOADBOOL " + cb.finalCode[pc + 1];
			
		case LOADINT:
			return "LOADINT " + cb.finalCode[pc + 1];
		case DUP:
			return "DUP";
			
		case FAILRETURN:
			return "FAILRETURN";
			
		case LOADOFUN:
			return "LOADOFUN " + cb.finalCode[pc + 1]  + " [" + cb.getFunctionName(cb.finalCode[pc + 1]) + "]";
			
		case OCALL:
			return "OCALL " + cb.finalCode[pc + 1]  + ", " + cb.finalCode[pc + 2] + " [" + cb.getFunctionName(cb.finalCode[pc + 1]) + "]";
			
		case OCALLDYN:
			return "OCALLDYN " + cb.finalCode[pc + 1];
				
		default:
			break;
		}	
		
		throw new RuntimeException("PANIC: unrecognized opcode " + opc);
	}
}
