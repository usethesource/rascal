@doc{
.Synopsis
Intermediate notation for control flow graphs

.Description

Control flow graphs are a unifying concept for units of executable
code in programming languages. This module defines a common
intermediate representation which is designed to be produced from ((data:analysis::m3::Core-M3))
models and ((data:analysis::m3::AST)) for real programming languages. If (and only if) the translation
is faithful to the semantics of the respective programming language,
then downstream analyses and visualizations are accurate.
}
module analysis::flow::ControlFlow

@doc{
.Synopsis
control points in source code

.Description

Control points in executable units of code are either straightline
code (block), or forks. Each executable unit has an entry and an exit
node. This is the simplest model for control flow nodes which may hold
all the possible structures we find in real executable units, but it
does require an analysis which resolves the locations of each block
and the labels which are used to jump to. 
}
data ControlNode
  = \block(loc id) // intermediate nodes in an executable unit
  | \entry(loc id) // start node of an executable unit
  | \exit(loc id)  // exit node of an executable unit or a join point
  ;

@doc{
.Synopsis
identify control edges

.Description

A control edge goes from ControlEdge to ControlEdge and is identified
by the condition which activates it. For normal structured control
flow (`choice`) like if, while and do-while this is a boolean condition going either
left (true) or right (false). We also have edges labeled by `case` (data)
and edges which are unconditional (`jump`). 

Each edge is identified by
a location which should resolve to the identifying source code. For
`choice` this would be the code of the conditional, for `case` the label
of the code to jump to and for `jump` the code of the jump instruction. Note that
edge identification is redundant information, making it easier to index
back into ((data:analysis::m3::Core-M3)) models or ((data:analysis::m3::AST)) models.
}
data ControlEdge
  = \choice(loc id, bool condition) // if-then-else, while, do-while
  | \case(loc id)                   // switch case 
  | \jump(loc id)                   // fall-through, break, continue, goto, return, yield
  ;
       
alias ControlFlow = rel[ControlNode from, ControlEdge edge, ControlNode to];

data CFG = cfg(loc id, ControlFlow graph = {}, ControlNode \start = entry(id), ControlNode end = exit(id));
