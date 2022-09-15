---
title: Labelled Pattern
keywords:
  - :

---

#### Synopsis

Labelled abstract pattern.

#### Syntax

#### Types

#### Function

#### Description

A labelled pattern matches the same values as _Pat_, but has as side-effect that the matched value is assigned to _Var_.

#### Examples

```rascal-shell
import IO;
data ColoredTree = leaf(int N)
                 | red(ColoredTree left, ColoredTree right) 
                 | black(ColoredTree left, ColoredTree right);
T = red(red(black(leaf(1), leaf(2)), black(leaf(3), leaf(4))), black(leaf(5), leaf(4)));
for(/M:black(_,leaf(4)) := T)
    println("Match <M>");
```
We use an *anonymous variable* `_` at a position where we don't care about the actual value that is matched.

#### Benefits

#### Pitfalls

