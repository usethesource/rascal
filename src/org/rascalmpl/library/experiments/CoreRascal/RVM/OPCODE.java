package org.rascalmpl.library.experiments.CoreRascal.RVM;

public enum OPCODE {
	LOADCON, 
	LOADVAR,
	LOADLOC,
	STOREVAR, 
	STORELOC,
	CALL, 
	CALLPRIM,
	RETURN, 
	JMP, 
	JMPTRUE,
	JMPFALSE,
	LABEL,
	HALT
}
