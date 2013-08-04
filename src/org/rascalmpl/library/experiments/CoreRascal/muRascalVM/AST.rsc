module experiments::CoreRascal::muRascalVM::AST

public data Instruction = instruction(str opcode, list[str] operands);

public data Directive = 
		  intconst(str \value)
		| relconst(str \value)
		| ratconst(str \value)
		| boolconst(str \value)
		| function(str name, int scope, int nlocals, int nformals, int maxStack, list[Instruction] instructions)
		;

public data RascalVM = vm(list[Directive] directives, list[Instruction] instructions);

public str LOADCON = "LOADCON";
public str LOADLOC = "LOADLOC";
public str LOADVAR = "LOADVAR";
public str STORELOC = "STORELOC";
public str STOREVAR = "STOREVAR";
public str LABEL = "LABEL";
public str CALLPRIM = "CALLPRIM";
public str CALL = "CALL";
public str RETURN = "RETURN";
public str JMP = "JMP";
public str JMPTRUE = "JMPTRUE";
public str JMPFALSE = "JMPFALSE";
public str CREATE = "CREATE";
public str RESUME = "RESUME";
public str YIELD = "YIELD";
