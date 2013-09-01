module experiments::Compiler::RVM::AST

import Type;

public data Declaration = 
		  FUNCTION(str qname, int nformals, int nlocals, int maxStack, list[Instruction] instructions)
		;

public data RVMProgram = rvm(list[Symbol] types, map[str, Declaration] declarations, list[Instruction] instructions);

data Instruction =
          LOADBOOL(bool bval)
        | LOADINT(int nval)  
	   	| LOADCON(value val)
	   	| LOADTYPE(Symbol \type)
		| LOADVAR(str fuid, int pos)
		| LOADLOC(int pos)
		| STOREVAR(str fuid, int pos)
		| STORELOC(int pos)
		| CALLCONSTR(str fuid)
		| CALL(str fuid)
		| CALLPRIM(str name, int arity)
		| CALLMUPRIM(str name, int arity)
		| RETURN1()
		| JMP(str label)
		| JMPTRUE(str label)
		| JMPFALSE(str label)
		| LABEL(str label)
		| HALT()
		| POP()
		| CALLDYN()
		| LOADFUN(str fuid)
		| LOAD_NESTED_FUN(str fuid, str scopeIn)
		| LOADCONSTR(str fuid)
		| CREATE(str fuid, int arity)
		| NEXT0()
		| NEXT1()
		| YIELD0()
		| YIELD1()
		| INIT(int arity)
		| CREATEDYN(int arity)
		| HASNEXT()
		| PRINTLN(int arity)
		| RETURN0()
		| LOADLOCREF(int pos)
		| LOADVARREF(str fuid, int pos)
		| LOADLOCDEREF(int pos)
		| LOADVARDEREF(str fuid, int pos)
		| STORELOCDEREF(int pos)
		| STOREVARDEREF(str fuid, int pos)
		| DUP()
		;
	
