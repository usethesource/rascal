module demo::lang::turing::l1::ast::Turing

data Program = program(list[Statement] statements);

data Statement
	= jumpAlways(int line)
	| jumpSet(int line)
	| jumpUnset(int line)
	| writeSet()
	| writeUnset()
	| moveForward()
	| moveBackward()
	;

anno loc Statement@location;
