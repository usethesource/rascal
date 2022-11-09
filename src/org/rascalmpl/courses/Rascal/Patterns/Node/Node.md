---
title: Node pattern
---

#### Synopsis

Node in abstract pattern.

#### Syntax

```rascal
Pat ~1~ ( Pat~2~, ..., Pat~n~)

Name (Pat~2~, ..., Pat~m~)

Pat ~1~ ( Pat~12~, ..., Pat~1n~, KeywordLabel~1~ = Pat~22~, ..., KeywordLabel~n~ = Pat~2n~)

Name ( Pat~12~, ..., Pat~1n~, KeywordLabel~1~ = Pat~22~, ..., KeywordLabel~n~ = Pat~2n~)
```

#### Types

#### Function

#### Description

A node pattern matches a ((Values-Node)) value or a ((Values-Constructor)) value, provided that _Name_ matches with the constructor symbol of that value and _Pat_~2~, _Pat_~2~, ..., _Pat_~n~  match the children of that value in order. Any variables bound by nested patterns are available from left to right. 

If _Name_ identifies a ((Values-Constructor)) of an ((AlgebraicDataType)) then not only the name must match but also the arity and the declared types of the children of the constructor and its keyword parameters.

The label of a node can also be a Pattern itself (Pat~1~), in which case it must be of type `str`

Nodes may have keyword fields which can be matched via the literal KeywordLabel and a respective pattern for each field.

Note that during matching the keyword fields which are not listed in the pattern are ignored. This means that their presence or absence in the subject does not influence the match. When a keyword
field _is_ mentioned in the match pattern, then the match will fail or succeeed if the respective
pattern fails or succeeds.

#### Examples

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

#### Benefits

#### Pitfalls

