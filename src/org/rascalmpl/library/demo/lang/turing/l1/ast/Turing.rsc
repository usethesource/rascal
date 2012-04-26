module demo::lang::turing::l1::ast::Turing

data Program = program(list[Statement] statements);

data Statement
	= jump(Condition con, int line)
	| write(Language val)
	| move(Direction dir)
	;
	
data Condition
	= \any()
	| \set()
	| unset()
	;
	
data Language
	= \set()
	| unset()
	;
	
data Direction 
	= forward()
	| backward()
	;
