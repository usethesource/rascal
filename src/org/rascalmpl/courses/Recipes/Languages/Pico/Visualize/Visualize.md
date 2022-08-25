# Visualize

.Synopsis
Visualize Pico Control Flow Graphs.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description

WARNING: The visualization library is being reimplemented and reorganized; 
the information provided here maybe inaccurate or even incorrect.

.Examples
[source,rascal]
----
include::{LibDir}demo/lang/Pico/Visualize.rsc[tags=module]
----

<1> We want to include the text of expressions in the relevant Figure nodes, this is achieved by `make`.
<2> An editor property is attached to each Figure node: clicking on the node opens an editor for the corresponding file.
<3> `visNode` implements the visualization per CFG node.
<4> Since Figure nodes in a visual graph need an `id` property, we define here a scheme to associate unique identifiers to each Figure node.
<5> The complete visualization of a CFG is implemented by `visCFG`: it gets the CFG graph as arguments and then
    *  creates all Figure edges,
    *  creates all Figure nodes,
    *  returns a Figure graph.


Let's now apply this:
[source,rascal-figure,width=,height=,file=cfg1.png]
----
                import demo::lang::Pico::ControlFlow;
import demo::lang::Pico::Visualize;
CFG = cflowProgram("begin declare n : natural, s : string; n := 10; s := \"a\"; while n do s := s + \"a\"; n := n - 1 od end");
render(visCFG(CFG.graph));
----

The resulting visualization looks like this: 


![]((cfg1.png))

.Benefits

.Pitfalls

