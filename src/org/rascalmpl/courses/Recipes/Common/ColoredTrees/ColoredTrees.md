---
title: Colored Trees
---

#### Synopsis

Computations on binary trees.

#### Syntax

#### Types

#### Function

#### Description

We consider binary trees---trees with exactly two children---that have integers as their leaves. 
Our trees can have red and black nodes and we want to perform the following operations on them:

*  Count the number of red nodes.
*  Compute the sum of all the integers that occur in the leaves.
*  Extend the tree data type with green nodes.
*  Replace all red nodes by green ones.

#### Examples

The definition of ColoredTrees is as follows:
```rascal-include
demo::common::ColoredTrees
```
       
<1> We define the data type of `ColoredTrees` with constructors `leaf`, `red` and `black`.

<2> `cntRed` counts all red nodes by visiting all nodes and incrementing
the counter `c` for each red one.

<3> `addLeaves` visits all nodes and adds the integers in each leaf node.

<4> `coloredTrees` are extended with a new constructor `green`.

<5> `makeGreen` visits all nodes and turns red nodes in green ones.

We can now explore ColoredTrees:
```rascal-shell
import demo::common::ColoredTrees;
rb = red(black(leaf(1), red(leaf(2),leaf(3))), black(leaf(3), leaf(4)));
```
Count the red nodes in `rb`:
```rascal-shell,continue
cntRed(rb);
```
and compute the sum of all leaves:
```rascal-shell,continue
addLeaves(rb);
```
Finally, we convert all red nodes:
```rascal-shell,continue
makeGreen(rb);
```

#### Benefits

This example illustrates the fully automatic visiting of the elements of a structured data type.
Compare this with the traditional programming style in which a switch statement is used to determine
the constructor and recursion is used to visit substructures. This style becomes particularly cumbersome
for data types with large numbers of constructors such as, for instance, abstract syntax trees for real
programming languages.

#### Pitfalls

The visit statement is based on a new paradigm one has to learn.

