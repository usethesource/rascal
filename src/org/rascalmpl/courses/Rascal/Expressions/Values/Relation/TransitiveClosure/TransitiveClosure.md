---
title: Relation TransitiveClosure
keywords:
  - +

---

#### Synopsis

Transitive closure on binary relation values.

#### Syntax

`Exp +`

#### Types


|`Exp`              | `Exp +`            |
| --- | --- |
| `rel[T~1~, T~2~]` | `rel[T~1~, T~2~]`  |


#### Function

#### Description

Returns the transitive closure of a binary relation.
Transitive closure is defined by repeated composition of a relation.
If we define for a given relation R:

*  `R~1~ = R`
*  `R~2~ = R o R`
*  `R~3~ = R o R~2~` 
*  `...`


then the transitive closure R+ can be defined as

*  `R+ = R~1~ + R~2~ + R~3~ + ...`


#### Examples

```rascal-shell
{<1,2>, <2,3>, <3,4>}+;
```
We can also simply (but not necessarily efficiently) define transitive closure ourselves:
```rascal-shell,continue
rel[int,int] tclosure(rel[int,int] R) {
   tc = R;
   while(true){
      tc1 = tc;
      tc += tc o R;
      if(tc1 == tc)
         return tc;
   }
}
tclosure({<1,2>, <2,3>, <3,4>});
```

#### Benefits

#### Pitfalls

