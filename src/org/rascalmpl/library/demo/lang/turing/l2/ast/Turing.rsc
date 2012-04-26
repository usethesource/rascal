module demo::lang::turing::l2::ast::Turing

extend demo::lang::turing::l1::ast::Turing;

data Statement
	= jumpAlwaysLabel(str name)
	| jumpSetLabel(str name)
	| jumpUnsetLabel(str name)
	| label(str name)
	| loop(int count, list[Statement] body)
	;
