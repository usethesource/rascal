---
title: Insert
keywords:
  - insert

---

#### Synopsis

Insert a value in a tree during a ((Statements-Visit)).

#### Syntax

`insert Exp;`

#### Types

#### Function

#### Description

An insert statement may only occur in the action part of a ((Pattern With Action)), more precisely in
a case in a ((Expressions-Visit)) expression. The value matched by the pattern of this case is replaced by the value of _Exp_.

The following rule applies:

*  The static type of _Exp_ should be a subtype of the type of the value that is replaced.

#### Examples

Consider the following datatype `CTree` and assign a CTree value to variable `T`:
```rascal-shell
data CTree = leaf(int n) | red(CTree left, CTree right) | green(CTree left, CTree right);
CTree T = red(green(leaf(1), red(leaf(2), leaf(3))), red(leaf(4), leaf(5)));
```
We can now switch the arguments of all red nodes as follows:
```rascal-shell,continue
visit(T){
  case red(CTree l, CTree r): insert red(r,l);
}
```
Since this is a very common idiom, we also have a shorthand for it:
```rascal-shell,continue
visit(T){
  case red(CTree l, CTree r) => red(r,l)
}
```

#### Benefits

#### Pitfalls

There is a glitch in the Rascal syntax that _requires_ a semicolon after a case (as in the first example),
but refuses it in the abbreviated version using `=>` (the second example).

