module experiments::CoreRascal::muRascalVM::AST

public data Instruction = instruction(str opcode, list[str] operands);

public data Directive = 
		  intconst(str \value)
		| relconst(str \value)
		| ratconst(str \value)
		| boolconst(str \value)
		| function(str name, int scope, int nformals, int nlocals, int maxStack, list[Instruction] instructions)
		;

public data RascalVM = vm(list[Directive] directives, list[Instruction] instructions);
