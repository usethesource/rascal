module experiments::CoreRascal::muRascalVM::AST


//public data Instruction = instruction(str opcode, list[str] operands);

public data Declaration = 
//		  intconst(str \value)
//		| relconst(str \value)
//		| ratconst(str \value)
//		| boolconst(str \value)
		  FUNCTION(str name, int scope, int nformals, int nlocals, int maxStack, list[Instruction] instructions)
		;

public data RVMProgram = rvm(map[str, Declaration] declarations, list[Instruction] instructions);

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
		| CREATE(str fun)
		| NEXT0()
		| NEXT1()
		| YIELD0()
		| YIELD1()
		| INIT()
		| CREATEDYN()
		| HASNEXT()
		| PRINT(str txt)
		| RETURN0()
		;
	
