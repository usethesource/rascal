# Uninit

.Synopsis
Find unitialized variables in a Pico program.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description

.Examples
Uninitialized variables are variables that are used without being initialized.
This means that there is a path in the control flow graph from the entry point of the program
to a specific use of a variable, where that path does not contain a definition of that variable.

This can be computed as follows:
```rascal
include::{LibDir}demo/lang/Pico/Uninit.rsc[tags=module]
```

                
<1> First, we determine the variable definitions of the program,
<2> and its control flow graph.
<3> Next we ask for every use of a variable the question: can it be reached from the entries
    of the program without encountering a definition? This determined as follows:

    *  `rangeR(D, {occ.item})` is the set of definition for the variable were are looking at. See [Rascal:Relation/rangeR].
    *  `reachX` determines the reachability in a graph while excluding certain nodes, see [Rascal:Graph/reachX]. Here
        `reachX(CFG.graph, CFG.entry, rangeR(D, {occ.item}))` determines the nodes in the graph that can be reached from the
         entry point of the program without passing a definition of the current variable.
    *  `any(CFNode N <- reachX( ... ), N has location && occ.location \<= N.location)` yields true if there is such a reachable node
        that covers the location of the current variable.
<4> The complete comprehension returns the set of occurrences of uninitialized variables.


The function `uninitProgram` performs this analysis on the source text of a Pico program.

Here is a simple example, where variable `p` is used without intialization:
```rascal-shell
import demo::lang::Pico::Uninit;
uninitProgram("begin declare n : natural, m : natural, p : natural; n := 10; m := n + p end");
```


.Benefits

.Pitfalls

