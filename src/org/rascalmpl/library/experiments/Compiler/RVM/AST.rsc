module experiments::Compiler::RVM::AST

import Type;

public data Declaration = 
		  FUNCTION(str name, int scope, int nformals, int nlocals, int maxStack, list[Instruction] instructions)
		;

public data RVMProgram = rvm(list[Symbol] types, map[str, Declaration] declarations, list[Instruction] instructions);

data Instruction =
	   	  LOADCON(value val)
	   	| LOADTYPE(Symbol \type)
		| LOADVAR(int scope, int pos)
		| LOADLOC(int pos)
		| STOREVAR(int scope, int pos)
		| STORELOC(int pos)
		| CALLCONSTR(str name)
		| CALL(str name)
		| CALLPRIM(str name, int arity)
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
		| LOADCONSTR(str name)
		| CREATE(str fun, int arity)
		| NEXT0()
		| NEXT1()
		| YIELD0()
		| YIELD1()
		| INIT(int arity)
		| CREATEDYN(int arity)
		| HASNEXT()
		| PRINTLN()
		| RETURN0()
		| LOADLOC_AS_REF(int pos)
		| LOADVAR_AS_REF(int scope, int pos)
		| LOADLOCREF(int pos)
		| LOADVARREF(int scope, int pos)
		| STORELOCREF(int pos)
		| STOREVARREF(int scope, int pos)
		;
	
