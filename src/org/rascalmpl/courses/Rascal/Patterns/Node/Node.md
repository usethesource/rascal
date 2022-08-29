# Node pattern

.Synopsis
Node in abstract pattern.

.Syntax

.Types

.Function

.Details

.Description

A node pattern matches a node value or a datatype value, provided that _Name_ matches with the constructor symbol of that value 
and _Pat_~1~, _Pat_~2~, ..., _Pat_~n~  match the children of that value in order.

.Examples
```rascal-shell
```
Match on node values (recall that the function symbol of a node has to be quoted, see [Values/Node]):
```rascal-shell,continue
import IO;
if("f"(A,13,B) := "f"("abc", 13, false))
   println("A = <A>, B = <B>");
```
Define a data type and use it to match:
```rascal-shell,continue
data Color = red(int N) | black(int N);
if(red(K) := red(13))
   println("K = <K>");
```

.Benefits

.Pitfalls

