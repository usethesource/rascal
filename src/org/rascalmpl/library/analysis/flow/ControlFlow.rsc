@doc{
Synopsis: Intermediate notation for control flow graphs

Description:

Control flow graphs are a unifying concept for units of executable
code in programming languages. This module defines a common
intermediate representation which is designed to be produced from [M3]
models and [M3] ASTs for real programming languages. If (and only if) the translation
is faithful to the semantics of the respective programming language,
then downstream analyses and visualizations are accurate.
}
module analysis::flow::ControlFlow

extend analysis::graphs::LabeledGraph;

@doc{
Synopsis: control points in source code

Description: 

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
  | \exit(loc id)  // exit node of an executable unit
  ;

data ControlEdge
  = \choice(loc id, bool condition) // if-then-else, while, do-while
  | \case(loc id, int index)        // switch case with an index
  | \case(loc id, str label)        // switch case with a string
  | \jump(loc id)                   // unconditional jump
  ;
       
alias ControlFlow = rel[ControlNode from, ControlEdge edge, ControlNode to];


