---
title: Operators
---

.Synopsis
The Rascal operators.

.Syntax

.Types

.Function

.Description
An _operator expression_ consists of an operator and one or more operands. The evaluation order of the operands depends on the operator. 
The operator is applied to the operands and the resulting value (or values in some cases) is the result of the operator expression. 

All operators are summarized in the following table. They are listed from highest precedence
to lowest precedence. In other words, operators listed earlier in the table bind stronger.
 

| Operator                          | See                                           | Short Description |
| --- | --- | --- |
| `Exp . Name` |
| ((Location-FieldSelection)), |
  ((DateTime-FieldSelection)), |
  ((Tuple-FieldSelection)), |
  ((Relation-FieldSelection)), |
| Select named field from structured value |
| `Exp~1~ [ Name = Exp~2~ ]` |
| ((FieldAssignment)) |
| Change value of named field of structured value |
| `Exp < field~1~, ... >` |
| ((FieldProjection)) |
| Select fields from relation or structured value |
| `Exp is Name` |
| ((Library:ParseTree)), |
  ((Expressions-ConcreteSyntax)), |
  ((Algebraic Data Type)) |
| Returns true if and only if the constructor name of the value produced by _Exp_ is equal to _Name_ |
| `Exp has Name`  |
| ((Library:ParseTree)), |
  ((Expressions-ConcreteSyntax)), |
  ((Algebraic Data Type)) |
| Returns true if and only if the constructor (node or parse tree) of the value produced by _Exp_ has any field labeled _Name_ |
| `Exp~1~ [ Exp~2~ , Exp~3~, .... ]` |
| ((List-Subscription)), |
  ((Map-Subscription)), |
  ((Tuple-Subscription)), |
  ((Relation-Subscription)) |
| Retrieve values for given index/key from list, map, tuple or relation. |
| `Exp~1~ [ Exp~2~ , Exp~3~ .. Exp~4~ ]` |
| ((Expressions-Values-List-Slice)), |
  ((String-Slice)), |
  ((Node-Slice)) |
| Retrieve a slice from a list, string, or node. |
|  `Exp?` |
| ((Boolean-IsDefined)) |
| Test whether an expression has a defined value |
|  `!Exp` |
| ((Boolean-Negation)) |
| Negate a Boolean value |
| `- Exp` |
| ((Number-Negation)) |
| Negation of numbers |
| `Exp +` |
| ((Relation-TransitiveClosure)), |
  ((ListRelation-TransitiveClosure)), |
| Transitive closure on relation or list relation |
| `Exp *` |
| ((Relation-ReflexiveTransitiveClosure)), |
  ((ListRelation-ReflexiveTransitiveClosure)) |
| Reflexive transitive closure on relation or list relation |
| `Exp @ Name` |
| ((Expressions-Selection)) |
| Value of annotation _Name_ of _Exp_'s value |
| `Exp~1~ [@ Name = Exp~2~]` |
| ((Annotations)) |
| Assign value of _Exp_~2~ to annotation _Name_ of _Exp_~1~'s value |
| `Exp~1~ o Exp~2~` |
| ((Relation-Composition)), |
  ((Map-Composition)) |
| _Exp_~1~ and _Exp_~2~ should evaluate to a relation or map; return their composition. Note: the letter "o" is thus a keyword |
| `Exp~1~ / Exp~2~` |
| ((Number-Division)) |
| Divide two numbers |
| `Exp~1~ % Exp~2~` |
| ((Number-Remainder)) |
| Remainder on numbers |
| `Exp~1~ * Exp~2~` |
| ((Number-Multiplication)), |
  ((List-Product)), |
  ((Set-Product)), |
  ((Relation-CartesianProduct)) |
| Multiply numbers; product of list, set, or relation |
| `Exp~1~ & Exp~2~` |
| ((List-Intersection)), |
  ((Set-Intersection)), |
  ((Map-Intersection)) |
| Intersection of list, set (including relation), or map |
| `Exp~1~ + Exp~2~` |
| ((Number-Addition)), |
  ((String-Concatenation)), |
  ((List-Concatenation)), |
  ((List-Insert)),((List-Append)), |
  ((Tuple-Concatenation)), |
  ((Set-Union)), |
  ((Map-Union)), |
  ((Location-AddSegment)) |
| Add numbers; concatenate string, list or tuple;  |
  union on set (including relation), or map; |
  concatenate location and string |
| `Exp~1~ - Exp~2~` |
| ((Number-Subtraction)), |
  ((List-Difference)), |
  ((Set-Difference)), |
  ((Map-Difference)) |
| Subtract numbers; difference of list, set (including relation), or map |
| `Exp~1~ join Exp~2~` |
| ((Relation-Join)) |
| Join on relation |
| `Exp~1~ in Exp~2~` |
| ((List-in)),  |
  ((Set-in)), |
  ((Map-in)) |
| Membership test for element in list, map, set (including relation) |
| `Exp~1~ notin Exp~2~` |
| ((List-notin)), |
  ((Set-notin)), |
  ((Map-notin)) |
| Negated membership test for element in  list, map, set (including relation) |
| `Exp~1~ <= Exp~2~` |
| ((Number-LessThanOrEqual)), |
  ((String-LessThanOrEqual)), |
  ((Location-LessThanOrEqual)), |
  ((DateTime-LessThanOrEqual)), |
  ((List-SubList)), |
  ((Set-SubSet)), |
  ((Map-SubMap)) |
| Less than or equal on all values |
| `Exp~1~ < Exp~2~` |
| ((Number-LessThan)), |
  ((String-LessThan)), |
  ((Location-LessThan)), |
  ((DateTime-LessThan)), |
  ((List-StrictSubList)), |
  ((Set-StrictSubSet)), |
  ((Map-StrictSubMap)) |
| Less than on all values |
| `Exp~1~ >= Exp~2~` |
| ((Number-GreaterThanOrEqual)), |
  ((String-GreaterThanOrEqual)), |
  ((Location-GreaterThanOrEqual)), |
  ((DateTime-GreaterThanOrEqual)), |
  ((List-SuperList)), |
  ((Set-SuperSet)), |
  ((Map-SuperMap)) |
| Greater than or equal on all values |
| `Exp~1~ > Exp~2~` |
| ((Number-GreaterThan)), |
  ((String-GreaterThan)), |
  ((Location-GreaterThan)), |
  ((DateTime-GreaterThan)), |
  ((List-StrictSuperList)), |
  ((Set-StrictSuperSet)), |
  ((Map-StrictSuperMap)) |
| Greater than on all values. |
|  `Pat := Exp` |
| ((Boolean-Match)) |
| Pattern matches value of expression |
|  `Pat !:= Exp` |
| ((Boolean-NoMatch)) |
| Pattern does not match value of expression |
| `Exp~1~ == Exp~2~` |
| ((Number-Equal)), |
  ((String-Equal)), |
  ((Location-Equal)), |
  ((DateTime-Equal)), |
  ((List-Equal)), |
  ((Set-Equal)), |
  ((Map-Equal)) |
| Equality on all values |
| `Exp~1~ != Exp~2~` |
| ((Number-NotEqual)), |
  ((String-NotEqual)), |
  ((Location-NotEqual)), |
  ((DateTime-NotEqual)), |
  ((List-NotEqual)), |
  ((Set-NotEqual)), |
  ((Map-NotEqual)) |
| Inequality on all values |
| `Exp~1~ ? Exp~2~` |
| ((Boolean-IfDefinedElse)) |
| Value of expression when it is defined, otherwise alternative value |
| `Exp~1~ ? Exp~2~ : Exp~3~` |
| ((Value-Conditional)) |
| Conditional expression for all types |
| `Exp~1~ ==> Exp~2~` |
| ((Boolean-Implication)) |
| Implication on Boolean values |
| `Exp~1~ <==> Exp~2~` |
| ((Boolean-Equivalence)) |
| Equivalence on Boolean values |
| `Exp~1~ && Exp~2~` |
| ((Boolean-And)) |
| And on Boolean values |
| `Exp~1~ \|\| Exp~2~` |
| ((Boolean-Or)) |
| Or on Boolean values |


.Examples

.Benefits

.Pitfalls

