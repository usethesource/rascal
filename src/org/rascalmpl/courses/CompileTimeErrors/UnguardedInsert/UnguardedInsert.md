# UnguardedInsert

.Synopsis
An `insert` occurs outside a `visit` expression.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description

An insert statement may only occur in the action part of a [pattern with action]((Rascal:PatternWithAction)), 
more precisely in a case in a 
[visit]((Rascal:Expressions-Visit)) expression. 

Remedies:

*  Use an auxiliary variable and list or set operations to insert the value where you want.
*  Place the insert statement inside a visit.

.Examples
Here is an example of the use of insert to swap the arguments of red nodes:

Our favorite data type, colored trees:
```rascal-shell,error
data CTree = leaf(int n) | red(CTree left, CTree right) | green(CTree left, CTree right);
```
An example tree:
```rascal-shell,continue,error
CTree T = red(green(leaf(1), red(leaf(2), leaf(3))), red(leaf(4), leaf(5)));
```
A visit to swap the arguments of red nodes:
```rascal-shell,continue,error
visit(T){ case red(CTree l, CTree r): insert red(r,l); }
```
An error occurs when insert is used outside a visit:
```rascal-shell,continue,error
insert red(leaf(1), leaf(2));
```

.Benefits

.Pitfalls

