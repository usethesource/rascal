package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.InternalCompilerError;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.MuPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;

public enum Opcode {
	/*
	 * Instructions for the RVM. Each instruction has 
	 * 	- a unique opcode (should be consecutive!)
	 * 	- a pc increment, i.e., the number code elements for this instruction
	 * 
	 * OPCODENAME(	opcode,	pc increment)
	 */
	LOADCON			    (0, 	1),
	LOADVAR 			(1, 	1),
	LOADLOC 			(2,		1),
	STOREVAR 			(3, 	1),
	STORELOC 			(4, 	1),
	CALL 				(5, 	1),
	RETURN1 			(6, 	1),
	JMP 				(7, 	1),
	JMPTRUE 			(8, 	1),
	JMPFALSE 			(9, 	1),
	LABEL 				(10, 	0),
	HALT 				(11, 	1),
	POP 				(12, 	1),
	CALLDYN				(13,	1),
	PUSH_ROOT_FUN		(14,	1),
	NEXT0				(15,	1),
	NEXT1				(16,	1),
	YIELD0				(17,	1),
	YIELD1				(18,	1),
	CREATE				(19,	1),
	CREATEDYN           (20,    1),
	PRINTLN				(21,	1),
	RETURN0				(22,	1),
	LOADLOCREF			(23,	1),
	LOADVARREF			(24,	1),
	LOADLOCDEREF		(25,	1),
	LOADVARDEREF		(26,	1),
	STORELOCDEREF		(27,	1),
	STOREVARDEREF		(28,	1),
	PUSHCONSTR			(29,	1),
	CALLCONSTR			(30,	1),
	PUSH_NESTED_FUN		(31, 	1),
	LOADTYPE			(32,	1),
	LOADBOOL			(33,	1),
	LOADINT				(34,	1),
	FAILRETURN			(35, 	1),
	PUSHOFUN        	(36,    1),
	OCALL           	(37,    2),
	OCALLDYN	    	(38,	2),
	CALLJAVA        	(39,    6),
	THROW           	(40,    1),
	TYPESWITCH			(41,	1),
	UNWRAPTHROWNLOC     (42,    1),
	FILTERRETURN		(43, 	1),
	EXHAUST             (44,    1),
	GUARD               (45,    1),
	SUBSCRIPTARRAY		(46,    1),
	SUBSCRIPTLIST		(47,    1),
	LESSINT				(48,	1),
	GREATEREQUALINT		(49,	1),
	ADDINT				(50,	1),
	SUBTRACTINT			(51,	1),
	ANDBOOL				(52,	1),
	TYPEOF				(53,	1),
	SUBTYPE				(54,	1),
	CHECKARGTYPEANDCOPY	(55,	2),
	LOADLOC0			(56, 	1),
	LOADLOC1			(57, 	1),
	LOADLOC2			(58, 	1),
	LOADLOC3			(59, 	1),
	LOADLOC4			(60, 	1),
	LOADLOC5			(61, 	1),
	LOADLOC6			(62, 	1),
	LOADLOC7			(63, 	1),
	LOADLOC8			(64, 	1),
	LOADLOC9			(65, 	1),
	UNUSED				(66, 	1),	//unused
	LOADLOCKWP          (67,    1),
	LOADVARKWP          (68,    1),
	STORELOCKWP         (69,    1),
	STOREVARKWP         (70,    1),
	UNWRAPTHROWNVAR     (71,    1),
	APPLY               (72,    1),
	APPLYDYN            (73,    1),

	SWITCH   			(74,	2),
	RESETLOC 			(75,    1),
	RESETVAR			(76,	1),
	VISIT               (77,    3),
	CHECKMEMO			(78,	1),
	PUSHEMPTYKWMAP      (79, 	1),
	VALUESUBTYPE		(80,	1),
	CALLMUPRIM0         (81,    1),
	CALLMUPRIM1			(82,    1),
	CALLMUPRIM2			(83,    1),
	CALLMUPRIMN			(84,    1),
	
	CALLPRIM0         	(85,    2),
	CALLPRIM1			(86,    2),
	CALLPRIM2			(87,    2),
	CALLPRIMN			(88,    2),
	RESETLOCS			(89,	1),
	PUSHACCU            (90,	1),
	POPACCU				(91,    1),
	PUSHLOC				(92,	1),
	PUSHCON				(93,	1),
	PUSHLOCREF			(94,	1),
	PUSHTYPE			(95,	1),
	PUSHLOCDEREF		(96,	1),
	PUSHVAR				(97,    1),
	PUSHVARREF          (98,    1),
	PUSHVARDEREF	    (99,    1),
	PUSHLOCKWP			(100,   1),
	PUSHVARKWP          (101,   1),
	
	PUSHCALLMUPRIM0     (102,    1),
	PUSHCALLMUPRIM1		(103,    1),
	PUSHCALLMUPRIM2		(104,    1),
	PUSHCALLMUPRIMN		(105,    1),
	
	PUSHCALLPRIM0       (106,    2),
	PUSHCALLPRIM1		(107,    2),
	PUSHCALLPRIM2		(108,    2),
	PUSHCALLPRIMN		(109,    2),
	
	CORETURN0			(110,	1),
	CORETURN1			(111,	1),
	;
	
	
	private final int op;
	private final int pc_incr;
	
	public final static Opcode[] values = Opcode.values();
	
	public static Opcode fromInteger(int finalCode){
		return values[finalCode];
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
	static public final int OP_RETURN1 = 6;
	static public final int OP_JMP = 7;
	static public final int OP_JMPTRUE = 8;
	static public final int OP_JMPFALSE = 9;
	static public final int OP_LABEL = 10;
	static public final int OP_HALT = 11;
	static public final int OP_POP = 12;
	static public final int OP_CALLDYN = 13;
	static public final int OP_PUSH_ROOT_FUN = 14;	
	static public final int OP_NEXT0 = 15;
	static public final int OP_NEXT1 = 16;
	static public final int OP_YIELD0 = 17;
	static public final int OP_YIELD1 = 18;
	static public final int OP_CREATE = 19;
	static public final int OP_CREATEDYN = 20;
	static public final int OP_PRINTLN = 21;
	static public final int OP_RETURN0 = 22;
	static public final int OP_LOADLOCREF = 23;
	static public final int OP_LOADVARREF = 24;
	static public final int OP_LOADLOCDEREF = 25;
	static public final int OP_LOADVARDEREF = 26;
	static public final int OP_STORELOCDEREF = 27;
	static public final int OP_STOREVARDEREF = 28;
	static public final int OP_PUSHCONSTR = 29;
	static public final int OP_CALLCONSTR = 30;
	static public final int OP_PUSH_NESTED_FUN = 31;
	static public final int OP_LOADTYPE = 32;
	static public final int OP_LOADBOOL = 33;
	static public final int OP_LOADINT = 34;
	static public final int OP_FAILRETURN = 35;
	static public final int OP_PUSHOFUN = 36;
	static public final int OP_OCALL = 37;
	static public final int OP_OCALLDYN = 38;
	static public final int OP_CALLJAVA = 39;
	static public final int OP_THROW = 40;
	static public final int OP_TYPESWITCH = 41;
	static public final int OP_UNWRAPTHROWNLOC = 42;
	static public final int OP_FILTERRETURN = 43;
	static public final int OP_EXHAUST = 44;
	static public final int OP_GUARD = 45;
	static public final int OP_SUBSCRIPTARRAY = 46;
	static public final int OP_SUBSCRIPTLIST = 47;
	static public final int OP_LESSINT = 48;
	static public final int OP_GREATEREQUALINT = 49;
	static public final int OP_ADDINT = 50;
	static public final int OP_SUBTRACTINT = 51;
	static public final int OP_ANDBOOL = 52;
	static public final int OP_TYPEOF = 53;
	static public final int OP_SUBTYPE = 54;
	static public final int OP_CHECKARGTYPEANDCOPY = 55;
	static public final int OP_LOADLOC0 = 56;
	static public final int OP_LOADLOC1 = 57;
	static public final int OP_LOADLOC2 = 58;
	static public final int OP_LOADLOC3 = 59;
	static public final int OP_LOADLOC4 = 60;
	static public final int OP_LOADLOC5 = 61;
	static public final int OP_LOADLOC6 = 62;
	static public final int OP_LOADLOC7 = 63;
	static public final int OP_LOADLOC8 = 64;
	static public final int OP_LOADLOC9 = 65;
/* 66 unused */
	static public final int OP_LOADLOCKWP = 67;
	static public final int OP_LOADVARKWP = 68;
	static public final int OP_STORELOCKWP = 69;
	static public final int OP_STOREVARKWP = 70;
	static public final int OP_UNWRAPTHROWNVAR = 71;
	static public final int OP_APPLY = 72;
	static public final int OP_APPLYDYN = 73;
	
	static public final int OP_SWITCH = 74;
	static public final int OP_RESETLOC = 75;
	static public final int OP_RESETVAR = 76;
	
	static public final int OP_VISIT = 77;
	static public final int OP_CHECKMEMO = 78;
	static public final int OP_PUSHEMPTYKWMAP = 79;
	static public final int OP_VALUESUBTYPE = 80;
	static public final int OP_CALLMUPRIM0 = 81;
	static public final int OP_CALLMUPRIM1 = 82;
	static public final int OP_CALLMUPRIM2 = 83;
	static public final int OP_CALLMUPRIMN = 84;
	
	static public final int OP_CALLPRIM0 = 85;
	static public final int OP_CALLPRIM1 = 86;
	static public final int OP_CALLPRIM2 = 87;
	static public final int OP_CALLPRIMN = 88;
	
	static public final int OP_RESETLOCS = 89;
	static public final int OP_PUSHACCU = 90;
	static public final int OP_POPACCU = 91;
	
	static public final int OP_PUSHLOC = 92;
	static public final int OP_PUSHCON = 93;
	
	
	static public final int OP_PUSHLOCREF = 94;
	static public final int OP_PUSHTYPE = 95;
	static public final int OP_PUSHLOCDEREF	= 96;
	static public final int OP_PUSHVAR	= 97;
	static public final int OP_PUSHVARREF = 98;
	static public final int OP_PUSHVARDEREF	= 99;
	static public final int OP_PUSHLOCKWP = 100;
	static public final int OP_PUSHVARKWP = 101;	
	
	static public final int OP_PUSHCALLMUPRIM0 = 102;
	static public final int OP_PUSHCALLMUPRIM1 = 103;
	static public final int OP_PUSHCALLMUPRIM2 = 104;
	static public final int OP_PUSHCALLMUPRIMN = 105;
	
	static public final int OP_PUSHCALLPRIM0 = 106;
	static public final int OP_PUSHCALLPRIM1 = 107;
	static public final int OP_PUSHCALLPRIM2 = 108;
	static public final int OP_PUSHCALLPRIMN = 109;
	
	static public final int OP_CORETURN0 = 110;
	static public final int OP_CORETURN1 = 111;
	
	static public final int OP_CALLMUPRIM3 = 112;
	static public final int OP_PUSHCALLMUPRIM3 = 113;
    
	static public final int OP_CALLPRIM3 = 114;
	static public final int OP_CALLPRIM4 = 115;
	static public final int OP_CALLPRIM5 = 116;
    
	static public final int OP_PUSHCALLPRIM3 = 117;
	static public final int OP_PUSHCALLPRIM4 = 118;
	static public final int OP_PUSHCALLPRIM5 = 119;
	
	/*
	 * Meta-instructions that are generated dynamically during execution and
	 * will never occur in generated code.
	 */
	static public final int POSTOP_CHECKUNDEF = 120;
	static public final int POSTOP_HANDLEEXCEPTION = 121;
	
	 Opcode(int op, int pc_incr){
		this.op = op;
		this.pc_incr = pc_incr;
	}
	
	public int getPcIncrement(){
		return pc_incr;
	}
	
	public int getOpcode(){
		return op;
	}
	
	public static String toString(CodeBlock cb, Opcode opc, int pc){
		long instruction = cb.finalCode[pc];
		Opcode opc1 = Opcode.fromInteger(CodeBlock.fetchOp(instruction));
		int arg1 = CodeBlock.fetchArg1(instruction);
		int arg2 = CodeBlock.fetchArg2(instruction);
		switch(opc1){
		case LOADCON:
			return "LOADCON " + cb.getConstantValue(arg1);
			
		case PUSHCON:
			return "PUSHCON " + cb.getConstantValue(arg1);	
			
		case LOADVAR:
			return "LOADVAR " + arg1 + ", " 
						      + arg2;
		case PUSHVAR:
			return "PUSHVAR " + arg1 + ", " 
						      + arg2;
			
		case LOADLOC:
			return "LOADLOC " + arg1;
			
		case PUSHLOC:
			return "PUSHLOC " + arg1;
			
		case STOREVAR:
			return "STOREVAR " + arg1 + ", " 
							   + arg2;	
			
		case STORELOC:
			return "STORELOC " + arg1;
			
		case CALL:
			return "CALL " + cb.getFunctionName(arg1)  + ", " 
						   + arg2;
		case RETURN0:
			return "RETURN0";
			
		case RETURN1:
			return "RETURN1 " + arg1;
			
		case CORETURN0:
			return "CORETURN0";
			
		case CORETURN1:
			return "CORETURN1 " + arg1;
			
		case JMP:
			return "JMP " + arg1;
			
		case JMPTRUE:
			return "JMPTRUE " + arg1;
			
		case JMPFALSE:
			return "JMPFALSE " + arg1;
			
		case LABEL:
			break;
			
		case HALT:
			return "HALT";
			
		case POP: 
			return "POP";	
			
		case CALLDYN:
			return "CALLDYN " + arg1;
			
		case PUSH_ROOT_FUN:
			return "PUSH_ROOT_FUN " + cb.getFunctionName(arg1) ;
			
		case NEXT0:
			return "NEXT0";
			
		case NEXT1:
			return "NEXT1";
			
		case YIELD0:
			return "YIELD0";
		
		case YIELD1:
			return "YIELD1 " + arg1;
		
		case CREATE:
			return "CREATE " + cb.getFunctionName(arg1) + ", " 
							 + arg2;
			
		case CREATEDYN:
			return "CREATEDYN " + arg1;
		
		case PRINTLN:
			return "PRINTLN " + arg1;
		
		
		
		case LOADLOCREF:
			return "LOADLOCREF " + arg1;
			
		case PUSHLOCREF:
			return "PUSHLOCREF " + arg1;
			
		case LOADVARREF:
			return "LOADVARREF " + arg1 + ", " 
							     + arg2;
		case PUSHVARREF:
			return "PUSHVARREF " + arg1 + ", " 
							     + arg2;
		case LOADLOCDEREF:
			return "LOADLOCDEREF " + arg1;
			
		case PUSHLOCDEREF:
			return "PUSHLOCDEREF " + arg1;
			
		case LOADVARDEREF:
			return "LOADVARDEREF " + arg1 + ", " 
								   + arg2;
			
		case PUSHVARDEREF:
			return "PUSHVARDEREF " + arg1 + ", " 
								   + arg2;
			
		case STORELOCDEREF:
			return "STORELOCDEREF " + arg1;
		
		case STOREVARDEREF:
			return "STOREVARDEREF " + arg1 + ", " 
									+ arg2;
			
		case PUSHCONSTR:
			return "PUSHCONSTR " + arg1;
		
//		case CALLCONSTR:
//			return "CALLCONSTR " + arg1 + ", " 
//								 + arg2  /*+ ", " + cb.getConstantValue(cb.finalCode[pc + 1])*/ ;
		
		case PUSH_NESTED_FUN:
			return "PUSH_NESTED_FUN " + arg1 + ", " 
									  + arg2;
			
		case LOADTYPE:
			return "LOADTYPE " + arg1;
			
		case PUSHTYPE:
			return "PUSHTYPE " + arg1;
			
		case LOADBOOL:
			return "LOADBOOL " + (arg1 == 1);
			
		case LOADINT:
			return "LOADINT " + arg1;
			
		case FAILRETURN:
			return "FAILRETURN";
			
		case PUSHOFUN:
			return "LOADOFUN " + cb.getOverloadedFunctionName(arg1);
			
		case OCALL:
			return "OCALL " +  cb.getOverloadedFunctionName(arg1)  + ", " 
						    + arg2 + ", " 
						    + cb.getConstantValue(cb.finalCode[pc + 1]);
			
		case OCALLDYN:
			return "OCALLDYN " + cb.getConstantType(arg1) + ", " 
							   + arg2 + ", "
							   + cb.getConstantValue((int) cb.finalCode[pc + 1]);
			
		case CALLJAVA:	
			return "CALLJAVA " + cb.getConstantValue((int) cb.finalCode[pc + 1]) + ", " 
							   + cb.getConstantValue((int) cb.finalCode[pc + 2]) + ", " 
							   + cb.getConstantType((int) cb.finalCode[pc + 3]) + ","
							   + cb.getConstantType((int) cb.finalCode[pc + 4]) + ","
							   + cb.finalCode[pc + 5] ;
			
		case THROW:
			return "THROW " +  cb.getConstantValue(arg1);
			
		case TYPESWITCH:
			return "TYPESWITCH " + cb.getConstantValue(arg1);
			
		case UNWRAPTHROWNLOC:
			return "UNWRAPTHROWNLOC " + arg1;
			
		case FILTERRETURN:
			return "FILTERRETURN";
			
		case EXHAUST:
			return "EXHAUST";
			
		case GUARD:
			return "GUARD";
			
		case SUBSCRIPTARRAY:
			return "SUBSCRIPTARRAY";
			
		case SUBSCRIPTLIST:
			return "SUBSCRIPTLIST";
			
		case LESSINT:
			return "LESSINT";
			
		case GREATEREQUALINT:
			return "GREATEREQUALINT";
			
		case ADDINT:
			return "ADDINT";
			
		case SUBTRACTINT:
			return "SUBTRACTINT";
			
		case ANDBOOL:
			return "ANDBOOL";
			
		case TYPEOF:
			return "TYPEOF";
			
		case SUBTYPE:
			return "SUBTYPE";
			
		case CHECKARGTYPEANDCOPY:
			return "CHECKARGTYPEANDCOPY " + arg1 + ", " 
								  		  + cb.getConstantType(arg2) + ", "
								  		  + cb.finalCode[pc + 1];
					 			  
		case LOADLOC0:
			return "LOADLOC0";
		case LOADLOC1:
			return "LOADLOC1";
		case LOADLOC2:
			return "LOADLOC2";
		case LOADLOC3:
			return "LOADLOC3";
		case LOADLOC4:
			return "LOADLOC4";
		case LOADLOC5:
			return "LOADLOC5";
		case LOADLOC6:
			return "LOADLOC6";
		case LOADLOC7:
			return "LOADLOC7";
		case LOADLOC8:
			return "LOADLOC8";
		case LOADLOC9:
			return "LOADLOC9";
				
		case LOADLOCKWP:
			return "LOADLOCKWP " + cb.getConstantValue(arg1);
			
		case PUSHLOCKWP:
			return "PUSHLOCKWP " + cb.getConstantValue(arg1);
			
		case LOADVARKWP:
			return "LOADVARKWP " + cb.getConstantValue(arg1) + ", " 
								 + cb.getConstantValue(arg2);
			
		case PUSHVARKWP:
			return "PUSHVARKWP " + cb.getConstantValue(arg1) + ", " 
								 + cb.getConstantValue(arg2);
			
		case STORELOCKWP:
			return "STORELOCKWP " + cb.getConstantValue(arg1);
			
		case STOREVARKWP:
			return "STOREVARKWP " + cb.getFunctionName(arg1) + ", " 
								  + cb.getConstantValue(arg2);
			
		case UNWRAPTHROWNVAR:
			return "UNWRAPTHROWNVAR " + arg1 + ", " +
									  + arg2;
			
		case APPLY:
			return "APPLY " + cb.getFunctionName(arg1) + ", "
						    + arg2;
			
		case APPLYDYN:
			return "APPLYDYN " + arg1;
			
		case SWITCH:
			return "SWITCH " + cb.getConstantValue(arg1) + ", " 
							 + arg2 + ", "
							 + cb.finalCode[pc + 1];
			
		case RESETLOCS:
			return "RESETLOCS " + cb.getConstantValue(arg1);
			
		case RESETLOC:
			return "RESETLOC " + arg1;
			
		case RESETVAR:
			return "RESETVAR " + arg1 + ", " 
						       + arg2;
			
		case VISIT:
			return "VISIT bottomUp=" 	+ cb.getConstantValue(arg1) + ", " +
		                 "continuing="  + cb.getConstantValue(arg1) + ", " +
			             "fixedpoint="  + cb.getConstantValue((int) cb.finalCode[pc + 1]) + ", " +
		                 "rebuild="  	+ cb.getConstantValue((int) cb.finalCode[pc + 2]);
		case CHECKMEMO:
				return "CHECKMEMO";
				
		case PUSHEMPTYKWMAP:
			return "PUSHEMPTYKWMAP";
			
		case VALUESUBTYPE:
			return "VALUESUBTYPE " + cb.getConstantType(arg1) ;
			
		case CALLMUPRIM0:
			return "CALLMUPRIM0 " + MuPrimitive.fromInteger(arg1).name();
		case CALLMUPRIM1:
			return "CALLMUPRIM1 " + MuPrimitive.fromInteger(arg1).name();
		case CALLMUPRIM2:
			return "CALLMUPRIM2 " + MuPrimitive.fromInteger(arg1).name();
		case CALLMUPRIMN:
			return "CALLMUPRIMN " + MuPrimitive.fromInteger(arg1).name() +  ", " 
				     + arg2;
			
		case PUSHCALLMUPRIM0:
			return "PUSHCALLMUPRIM0 " + MuPrimitive.fromInteger(arg1).name();
		case PUSHCALLMUPRIM1:
			return "PUSHCALLMUPRIM1 " + MuPrimitive.fromInteger(arg1).name();
		case PUSHCALLMUPRIM2:
			return "PUSHCALLMUPRIM2 " + MuPrimitive.fromInteger(arg1).name();
		case PUSHCALLMUPRIMN:
			return "PUSHCALLMUPRIMN " + MuPrimitive.fromInteger(arg1).name() +  ", " 
				     + arg2;
		
		case CALLPRIM0:
			return "CALLPRIM0 " + RascalPrimitive.fromInteger(arg1).name() +  ", " 
							    + cb.getConstantValue(cb.finalCode[pc + 1]);
		case CALLPRIM1:
			return "CALLPRIM1 " + RascalPrimitive.fromInteger(arg1).name() +  ", " 
							    + cb.getConstantValue(cb.finalCode[pc + 1]);
		case CALLPRIM2:
			return "CALLPRIM2 " + RascalPrimitive.fromInteger(arg1).name() +  ", " 
							    + cb.getConstantValue(cb.finalCode[pc + 1]);
			
		case CALLPRIMN:
			return "CALLPRIMN " + RascalPrimitive.fromInteger(arg1).name() +  ", " 
							   + arg2 + ", "
							   + cb.getConstantValue(cb.finalCode[pc + 1]);
			
		case PUSHCALLPRIM0:
			return "PUSHCALLPRIM0 " + RascalPrimitive.fromInteger(arg1).name() +  ", " 
							    + cb.getConstantValue(cb.finalCode[pc + 1]);
		case PUSHCALLPRIM1:
			return "PUSHCALLPRIM1 " + RascalPrimitive.fromInteger(arg1).name() +  ", " 
							    + cb.getConstantValue(cb.finalCode[pc + 1]);
		case PUSHCALLPRIM2:
			return "PUSHCALLPRIM2 " + RascalPrimitive.fromInteger(arg1).name() +  ", " 
							    + cb.getConstantValue(cb.finalCode[pc + 1]);
			
		case PUSHCALLPRIMN:
			return "PUSHCALLPRIMN " + RascalPrimitive.fromInteger(arg1).name() +  ", " 
							   + arg2 + ", "
							   + cb.getConstantValue(cb.finalCode[pc + 1]);
		case PUSHACCU:
				return "PUSHACCU";
		case POPACCU:
			return "POPACCU";
			
		default:
			break;
		}	
		
		throw new InternalCompilerError("unrecognized opcode " + opc);
	}
}
