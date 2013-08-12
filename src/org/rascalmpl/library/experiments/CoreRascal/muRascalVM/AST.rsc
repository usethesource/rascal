module experiments::CoreRascal::muRascalVM::AST

import Type;

public data Declaration = 
		  FUNCTION(str name, int scope, int nformals, int nlocals, int maxStack, list[Instruction] instructions)
		;

public data RVMProgram = rvm(list[Symbol] types, map[str, Declaration] declarations, list[Instruction] instructions);

data Instruction =
	   	  LOADCON(value val)
		| LOADVAR(int scope, int pos)
		| LOADLOC(int pos)
		| STOREVAR(int scope, int pos)
		| STORELOC(int pos)
		| CALL(str name)
		| CALLPRIM(str name)
		| RETURN1()
		| JMP(str label)
		| JMPTRUE(str label)
		| JMPFALSE(str label)
		| LABEL(str label)
		| HALT()
		| POP()
		| CALLDYN()
		| LOADFUN(str name)
		| LOAD_NESTED_FUN(str name, int scope)
		| CREATE(str fun)
		| NEXT0()
		| NEXT1()
		| YIELD0()
		| YIELD1()
		| INIT()
		| CREATEDYN()
		| HASNEXT()
		| PRINTLN()
		| RETURN0()
		| LOADCONREF(int pos)
		| LOADLOCREF(int pos)
		| STORELOCREF(int pos)
		;
	
