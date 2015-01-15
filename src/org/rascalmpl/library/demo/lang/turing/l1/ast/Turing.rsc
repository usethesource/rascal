module demo::lang::turing::l1::ast::Turing

import ParseTree;

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

 
data Statement(loc origin = |unknown:///|);
