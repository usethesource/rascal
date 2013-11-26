package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import java.io.PrintWriter;
import java.util.TreeMap;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.MuPrimitive;



public enum Opcode {
	/*
	 * Instructions for the RVM. Each instruction has 
	 * 	- a unique opcode
	 * 	- a pc increment, i.e., the number code elements for this instruction
	 * 
	 * OPCODENAME(	opcode,	pc increment)
	 */
	LOADCON			    (0, 	2),
	LOADVAR 			(1, 	3),
	LOADLOC 			(2,		2),
	STOREVAR 			(3, 	3),
	STORELOC 			(4, 	2),
	CALL 				(5, 	3),
	CALLPRIM	 		(6, 	3),
	RETURN1 			(7, 	2),
	JMP 				(8, 	2),
	JMPTRUE 			(9, 	2),
	JMPFALSE 			(10, 	2),
	LABEL 				(11, 	0),
	HALT 				(12, 	1),
	POP 				(13, 	1),
	CALLDYN				(14,	2),
	LOADFUN				(15,	2), // TODO: to be renamed to LOAD_ROOT_FUN
	CREATE				(16,	3),
	NEXT0				(17,	1),
	NEXT1				(18,	1),
	YIELD0				(19,	1),
	YIELD1				(20,	2),
	INIT				(21,	2),
	CREATEDYN			(22,	2),
	HASNEXT				(23,	1),
	PRINTLN				(24,	2),
	RETURN0				(25,	1),
	LOADLOCREF			(26,	2),
	LOADVARREF			(27,	3),
	LOADLOCDEREF		(28,	2),
	LOADVARDEREF		(29,	3),
	STORELOCDEREF		(30,	2),
	STOREVARDEREF		(31,	3),
	LOADCONSTR			(32,	2),
	CALLCONSTR			(33,	3), // TODO: plus number of formal parameters
	LOAD_NESTED_FUN		(34, 	3),
	LOADTYPE			(35,	2),
	CALLMUPRIM			(36,	3),
	LOADBOOL			(37,	2),
	LOADINT				(38,	2),
	FAILRETURN			(39, 	1),
	LOADOFUN        	(40,    2),
	OCALL           	(41,    3),
	OCALLDYN	    	(42,	3),
	CALLJAVA        	(43,    5),
	THROW           	(44,    1),
	JMPSWITCH			(45,	2),
	UNWRAPTHROWN        (46,    2),
	FILTERRETURN		(47, 	1),
	EXHAUST             (48,    1),
	GUARD               (49,    1),
	SUBSCRIPTARRAY		(50,    1),
	SUBSCRIPTLIST		(51,    1),
	LESSINT				(52,	1),
	GREATEREQUALINT		(53,	1),
	ADDINT				(54,	1),
	SUBTRACTINT			(55,	1),
	ANDBOOL				(56,	1),
	TYPEOF				(57,	1),
	SUBTYPE				(58,	1),
	CHECKARGTYPE		(59,	1)
	;
	
	
	private final int op;
	private final int pc_incr;
	
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
	static public final int OP_FAILRETURN = 39;
	static public final int OP_LOADOFUN = 40;
	static public final int OP_OCALL = 41;
	static public final int OP_OCALLDYN = 42;
	static public final int OP_CALLJAVA = 43;
	static public final int OP_THROW = 44;
	static public final int OP_JMPSWITCH = 45;
	static public final int OP_UNWRAPTHROWN = 46;
	static public final int OP_FILTERRETURN = 47;
	static public final int OP_EXHAUST = 48;
	static public final int OP_GUARD = 49;
	static public final int OP_SUBSCRIPTARRAY = 50;
	static public final int OP_SUBSCRIPTLIST = 51;
	static public final int OP_LESSINT = 52;
	static public final int OP_GREATEREQUALINT = 53;
	static public final int OP_ADDINT = 54;
	static public final int OP_SUBTRACTINT = 55;
	static public final int OP_ANDBOOL = 56;
	static public final int OP_TYPEOF = 57;
	static public final int OP_SUBTYPE = 58;
	static public final int OP_CHECKARGTYPE = 59;
	
	 Opcode(int op, int pc_incr){
		this.op = op;
		this.pc_incr = pc_incr;
	}
	 
	static long opFrequencies[];
	static boolean profiling = false;
	private static PrintWriter stdout;
	
	public static void init(PrintWriter stdoutWriter, boolean doProfile) {
	  stdout = stdoutWriter;
	  profiling = doProfile;
      opFrequencies = new long[values.length];
	}
	
	public static void use(int op){
		opFrequencies[op]++;
	}
	
	public static void exit(){
		if(profiling)
			printProfile();
	}
	
	private static void printProfile(){
		stdout.println("\nOpcode Frequencies");
		long total = 0;
		TreeMap<Long,String> data = new TreeMap<Long,String>();
		for(int i = 0; i < values.length; i++){
			if(opFrequencies[i] > 0 ){
				data.put(opFrequencies[i], values[i].name());
				total += opFrequencies[i];
			}
		}
		for(long t : data.descendingKeySet()){
			stdout.printf("%30s: %3d%% (%d)\n", data.get(t), t * 100 / total, t);
		}
	}
	
	public int getPcIncrement(){
		return pc_incr;
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
			return "RETURN1 " + cb.finalCode[pc + 1];
			
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
			return "YIELD1 " + cb.finalCode[pc + 1];
		
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
			
		case FAILRETURN:
			return "FAILRETURN";
			
		case LOADOFUN:
			return "LOADOFUN " + cb.finalCode[pc + 1]  + " [" + cb.getFunctionName(cb.finalCode[pc + 1]) + "]";
			
		case OCALL:
			return "OCALL " + cb.finalCode[pc + 1]  + ", " + cb.finalCode[pc + 2] + " [" + cb.getFunctionName(cb.finalCode[pc + 1]) + "]";
			
		case OCALLDYN:
			return "OCALLDYN " + cb.finalCode[pc + 1] + ", " + cb.finalCode[pc + 2] + " [" + cb.getConstantType(cb.finalCode[pc + 1]) + "]";
			
		case CALLJAVA:	
			return "CALLJAVA " + cb.getConstantValue(cb.finalCode[pc + 1]) + ", " + cb.getConstantValue(cb.finalCode[pc + 2]) + ", " + cb.getConstantType(cb.finalCode[pc + 3]) + "," + cb.finalCode[pc + 4] ;
			
		case THROW:
			return "THROW";
			
		case JMPSWITCH:
			return "JMPSWITCH " + cb.getConstantValue(cb.finalCode[pc + 1]);
			
		case UNWRAPTHROWN:
			return "UNWRAPTHROWN " + cb.finalCode[pc + 1];
			
		case FILTERRETURN:
			return "FILTERRETURN";
			
		case EXHAUST:
			return "TERMINATE";
			
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
			
		case CHECKARGTYPE:
			return "CHECKARGTYPE";
		
		default:
			break;
		}	
		
		throw new RuntimeException("PANIC: unrecognized opcode " + opc);
	}
}
