---
title: ControlFlow
---

#### Synopsis

Compute the control flow graph for a Pico program.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

#### Examples

A control flow graph shows how the entry and exit points of a program are connected with each other via all
decision points and statements in the program. Typically, an assignment statement is a single node in the graph
and an if-then-else statement creates a decision point (its test) that connects the then branch and the else branch.
The exits of each branch are connected to the exit of the if-then-else statement as a whole.

A control flow graph for Pico programs can be created as follows:
```rascal-include
demo::lang::Pico::ControlFlow
```

                
Notes:

<1> First we define a data type `CFNODE` that represents the various elements of a control flow graph:
    *  `entry`: the entry point of the program.
    *  `exit` the exit point of the program.
    *  `choice`: a decision point in the control flow.
    *  `statement`: a statement in the control flow.

<2> Next we define `CFGRAPH` , an alias for a tuple consisting of the following three elements:
    *  `entry`: the set of entry nodes of the graph.
    *  `graph`: the actual graph of `CFNODE`s.
    *  `exit`: the set of exit nodes.

   The computation of the control flow graph is defined by the functions 
  `cflowStat`, `cflowStats`, `cflowDecls` and `cflowProgram`.

<3> The control flow of an assignment statement is computed by wrapping
    the assignment statement as a `CFNODE` and return a `CFGRAPH` with the assignment
    statement as entry and exit node, and no internal connections.

<4> The control flow of an if-then-else statement is computed as follows:
    *  First the control flows of the then part and the else part are computed,
        yielding `CF1` and `CF2`.
    *  Next a set `E` is created that consist of a the test of the if-then-else statement
        wrapped as choice node.
    *  Finally, a `CFGRAPH` is returned consisting of the followng three elements:
        ***  The entry point set `E`.
        ***  A graph consisting of the connections between the entry point and both
             branches (`E * CF1.entry + E * CF2.entry`) and the internal graphs of both branches
             (`CF1.graph + CF2.graph`).
        ***  The union of exit nodes of both branches (`CF1.exit + CF2.exit`).

<5> The control flow of  while-statement is computed in a similar fashion,
    except that the exit of the loop body has to be connected with the entry
    of the while loop.

<6> The control flow graph for a series of statements is obtained by connecting
    the exits and entries of consecutive statements.

<7> The control flow graph of a complete program is obtained by
    creating an entry and an exit node and connecting them to the graph of
    the statements of the program.

<8> Shows the steps from text to control flow graph.

We can now create a CFG for a small Pico program:
```rascal-shell
import demo::lang::Pico::ControlFlow;
cflowProgram("begin declare n : natural, s : string; n := 10; s := \"a\"; while n do s := s + \"a\"; n := n - 1 od end");
```

Is the above not very motivating to move on to ((Pico-Visualize))?

#### Benefits

#### Pitfalls

