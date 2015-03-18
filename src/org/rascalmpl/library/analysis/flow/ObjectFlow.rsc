@doc{
  The object flow language from the Tonella and Potrich book 
  "Reverse Engineering Object-oriented programs" is an intermediate
  representation for object flow. We may translate for example
  Java to this intermediate language and then analyze object flow
  based on the simpler language.
}
module analysis::flow::ObjectFlow

data FlowProgram = flowProgram(set[FlowDecl] decls, set[FlowStm] statements);

public loc emptyId = |id:///|;

data FlowDecl 
	= attribute(loc id)
	| method(loc id, list[loc] formalParameters)
	| constructor(loc id, list[loc] formalParameters)
	;

data FlowStm
	= newAssign(loc target, loc class, loc ctor, list[loc] actualParameters)
	| assign(loc target, loc cast, loc source)
	| call(loc target, loc cast, loc receiver, loc method, list[loc] actualParameters)
	;
	
	