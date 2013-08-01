package org.rascalmpl.library.experiments.CoreRascal.RVM;

public enum OPCODE {
	LOADCON, 
	LOADVAR, 
	STOREVAR, 
	CALL, 
	CALLPRIM,
	RETURN, 
	JMP, 
	JMPTRUE,
	JMPFALSE,
	LABEL,
	HALT
}
