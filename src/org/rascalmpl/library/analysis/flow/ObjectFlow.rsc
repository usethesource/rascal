@doc{
.Synopsis
Intermediate Language and Basic Algorithms for object flow analysis
  
.Description
  
The object flow language from the Tonella and Potrich book 
"Reverse Engineering Object Oriented Code" is an intermediate
representation for object flow. We may translate for example
Java to this intermediate language and then analyze object flow
based on the simpler language.
  
The implementation in this file is intended to work with ((data:analysis::m3::Core-M3)) models
}
@bibliography{
@book{tonella,
 author = {Tonella, Paolo and Potrich, Alessandra},
 title = {Reverse Engineering of Object Oriented Code (Monographs in Computer Science)},
 year = {2004},
 isbn = {0387402950},
 publisher = {Springer-Verlag New York, Inc.},
 address = {Secaucus, NJ, USA},
} 
}
module analysis::flow::ObjectFlow

import List;
extend analysis::graphs::Graph;

data FlowProgram = flowProgram(set[FlowDecl] decls, set[FlowStm] statements);

public loc emptyId = |id:///|;

@doc{Figure 2.1}
data FlowDecl 
	= attribute(loc id)
	| method(loc id, list[loc] formalParameters)
	| constructor(loc id, list[loc] formalParameters)
	;

@doc{Figure 2.1}
data FlowStm
	= newAssign(loc target, loc class, loc ctor, list[loc] actualParameters)
	| assign(loc target, loc cast, loc source)
	| call(loc target, loc cast, loc receiver, loc method, list[loc] actualParameters)
	;
	
alias OFG = rel[loc from, loc to];

@doc{Figure 2.2}
OFG buildFlowGraph(FlowProgram p)
  = { <as[i], fps[i]> | newAssign(_, _, c, as) <- p.statements, constructor(c, fps) <- p.decls, i <- index(as) }
  + { <cl + "this", x> | newAssign(x, cl, _, _) <- p.statements }
  + { <y, x> | assign(x, _, y) <- p.statements}
  + { <as[i], fps[i]> | call(_, _, _, m, as) <- p.statements, method(m, fps) <- p.decls, i <- index(as) }
  + { <y, m + "this"> | call(_, _, y, m, _) <- p.statements }
  + { <m + "return", x> | call(x, _, _, m, _) <- p.statements, x != emptyId}
  ;

@doc{Section 2.4}
rel[loc,&T] propagate(OFG g, rel[loc,&T] gen, rel[loc,&T] kill, bool back) {
  rel[loc,&T] IN = { };
  rel[loc,&T] OUT = gen + (IN - kill);
  if (!back) {
    g = g<to,from>;
  }

  solve (IN, OUT) {
    // Tonella would say:
    //   IN = { <n,\o> | n <- carrier(g), p <- g[n], \o <- OUT[p] }; <==>
    //   IN = { <n,\o> | n <- carrier(g), p <- g[n], \o <- OUT[p] }; <==> 
    //   IN = { <n,\o> | <n,_> <- g, p <- g[n], \o <- OUT[p] }; <==>
    //   IN = { <n,\o> | <n,p> <- g, \o <- OUT[p] }; <==>
    IN = g o OUT;
    OUT = gen + (IN - kill);
  }

  return OUT;
}
