---
title: Operators
---

#### Synopsis

The Rascal operators.

#### Syntax

#### Types

#### Function

#### Description

An _operator expression_ consists of an operator and one or more operands. The evaluation order of the operands depends on the operator. 
The operator is applied to the operands and the resulting value (or values in some cases) is the result of the operator expression. 

All operators are summarized in the following table. They are listed from highest precedence
to lowest precedence. In other words, operators listed earlier in the table bind stronger.
 

| Operator     | See  | Short Description |
| ---          | ---  | ---               |
| `Exp . Name` |      | Select named field from structured value |
|              | [Location fields]((Location-FieldSelection)) |
|              | [DateTime fields]((DateTime-FieldSelection)) |
|              | [Tuple fields]((Tuple-FieldSelection))  |
|              | [Relation columns]((Relation-FieldSelection)) |
| `Exp~1~ [ Name = Exp~2~ ]` |  ((FieldAssignment))   |  Change value of named field of structured value |
| `Exp < field~1~, ... >` | ((FieldProjection)) |  Select fields from relation or structured value |
| `Exp is Name` |                        | Check if `Exp` has name `Name` |
|               | ((Library:ParseTree)), |
|               | ((Expressions-ConcreteSyntax)), |
|               | ((Algebraic Data Type)) |
| `Exp has Name`  |   | Check if a field with `Name` is present on `Exp`
|                 | ((Library:ParseTree)) | | 
|                 | ((Expressions-ConcreteSyntax)) | | 
|                 | ((Algebraic Data Type)) | |
| `Exp~1~ [ Exp~2~ , Exp~3~, .... ]` |  | Project values for given key from list, map, tuple or relation. |
|  | [list]((List-Subscription))  | | 
|  | [map]((Map-Subscription))  | |
|  | [tuple]((Tuple-Subscription))  | |
|  | [relation]((Relation-Subscription)) | |
| `Exp~1~ [ Exp~2~ , Exp~3~ .. Exp~4~ ]` | | Retrieve a slice from a list, string, or node. |
|  | [list]((Expressions-Values-List-Slice)) | |
|  | [string]((String-Slice)) | |
|  | [node]((Node-Slice)) | |
|  `Exp?` | ((Boolean-IsDefined)) | Test whether an expression has a defined value  |
|  `!Exp` | ((Boolean-Negation)) | Negate a Boolean value |
| `- Exp` | ((Number-Negation)) | Negation of numbers |
| `Exp +` |                     | Transitive closure on relation or list relation |
|         | [Relation +]((Relation-TransitiveClosure)) | |
|         | [List +]((ListRelation-TransitiveClosure)) | |
| `Exp *` |             |  Reflexive transitive closure on relation or list relation |
|         | [Relation *]((Relation-ReflexiveTransitiveClosure)), |
|         | [List *]((ListRelation-ReflexiveTransitiveClosure)) |
| `Exp~1~ o Exp~2~` | | _Exp_~1~ and _Exp_~2~ should evaluate to a relation or map; return their composition. Note: the letter "o" is thus a keyword |
| | [relation]((Relation-Composition)) | |
| |  [map]((Map-Composition)) | |
| `Exp~1~ / Exp~2~` | ((Number-Division)) | Divide two numbers |
| `Exp~1~ % Exp~2~` |((Number-Remainder)) | Remainder on numbers |
| `Exp~1~ * Exp~2~` | | Products | 
|  | [number]((Number-Multiplication)) | Multiply numbers |
|  |  [list]((List-Product)) | Cartesian product  on lists | 
|  |  [set]((Set-Product)) |Cartesian product  on sets |
|  |  [relation]((Relation-CartesianProduct)) | Cartesian product  on relations |
| `Exp~1~ & Exp~2~` | Intersection | 
|  | [list intersection]((List-Intersection)) | | 
|  | [set intersection]((Set-Intersection)) | | 
|  | [map intersection]((Map-Intersection)) | |
| `Exp~1~ + Exp~2~` |  | Sums |
|  | ((Number-Addition)) | Add numbers |
|  |  ((String-Concatenation)) | Concatenate strings | 
 |  | ((List-Concatenation)) | Concatenate lists |
|  |  ((List-Insert)) ((List-Append)) | Insert and Append list elements |
|  |  ((Tuple-Concatenation)) | Concatenate tuples |
|  |  ((Set-Union)) | Set union |
|  |  ((Map-Union)) | Map "union" |
|  |  ((Location-AddSegment)) | Adding path segments to locations |
| `Exp~1~ - Exp~2~` | | Subtraction
|  | [numbers]((Number-Subtraction)) | subtraction of numbers |
|  |  [lists]((List-Difference)) | difference on lists | 
|  |  [sets]((Set-Difference)) | set difference |
|  |  [maps]((Map-Difference)) | map difference | 
| `Exp~1~ join Exp~2~` | ((Relation-Join)) Join on relation |
| `Exp~1~ in Exp~2~` | | test membership |
|  | [list]((List-in))  | list membership | 
|  |  [set]((Set-in)) | set membership |
|  |  [map]((Map-in)) | map key membership |
| `Exp~1~ notin Exp~2~` |  | inverse membership | 
|  | [list]((List-notin)) | list membership | 
|  |  [set]((Set-notin)) | set membership |
|  |  [map]((Map-notin)) | map key membership |
| `Exp~1~ <= Exp~2~` | reflexive less-than | 
|  | [number]((Number-LessThanOrEqual)) | numbers | 
|  | [string]((String-LessThanOrEqual)) | strings | 
|  |  [location]((Location-LessThanOrEqual)) | locations |
|  |  [datetime]((DateTime-LessThanOrEqual)) | datetime | 
|  |  [list]((List-SubList)) | list | 
|  |  [set]((Set-SubSet)) | set | 
|  |  [map]((Map-SubMap)) | map |
| `Exp~1~ < Exp~2~` | | irreflexive less-than |
|  | [number]((Number-LessThan)) | numbers |
|  |  [string]((String-LessThan)) |strings | 
|  |  [location]((Location-LessThan)) |locations |
|  |  [datetime]((DateTime-LessThan)) |datetime |
|  |  [list]((List-StrictSubList)) |list | 
|  |  [set]((Set-StrictSubSet)) |set |
|  |  [map]((Map-StrictSubMap)) | map |
| `Exp~1~ >= Exp~2~` | | reflexive greater-than |
|  | [number]((Number-GreaterThanOrEqual)) | numbers |
|  |  [string]((String-GreaterThanOrEqual)) |strings |
|  |  [location]((Location-GreaterThanOrEqual)) |locations |
|  |  [datetime]((DateTime-GreaterThanOrEqual)) |datetime |
|  |  [list]((List-SuperList)) |list |
|  |  [set]((Set-SuperSet)) |set |
|  |  [map]((Map-SuperMap)) |map |
| `Exp~1~ > Exp~2~` | | irreflexive greater-than |
|  | [number]((Number-GreaterThan)) | numbers |
|  |  [string]((String-GreaterThan)) |strings |
|  |   [location]((Location-GreaterThan)) |locations |
|  |  [datetime]((DateTime-GreaterThan)) |datetime |
|  |  [list]((List-StrictSuperList)) |list |
|  |  [set]((Set-StrictSuperSet)) |set |
|  |  [map]((Map-StrictSuperMap)) |map |
|  `Pat := Exp` | ((Boolean-Match)) | Pattern matches value of expression |
|  `Pat !:= Exp` | ((Boolean-NoMatch)) | Pattern does not match value of expression |
| `Exp~1~ == Exp~2~` | | Equality | 
|  | [numbers]((Number-Equal)) | | 
|  |  [strings]((String-Equal)) | | 
|  |  [locations]((Location-Equal)) | | 
|  |  [datetime]((DateTime-Equal)) | |
|  |  [lists]((List-Equal)) | |
|  |  [sets]((Set-Equal)) | |
|  |  [maps]((Map-Equal)) | |
| `Exp~1~ != Exp~2~` | | Inequality |
|  | [numbers]((Number-NotEqual)) | |
|  |  [strings]((String-NotEqual)) | |
|  |  [locations]((Location-NotEqual)) | |
|  |  [datetime]((DateTime-NotEqual)) | |
|  |  [lists]((List-NotEqual)) | |
|  |  [sets]((Set-NotEqual)) | |
|  |  [maps]((Map-NotEqual)) | |
| `Exp~1~ ? Exp~2~` | ((Boolean-IfDefinedElse)) | Value of expression when it is defined, otherwise alternative value |
| `Exp~1~ ? Exp~2~ : Exp~3~` | ((Value-Conditional)) | Conditional expression for all types |
| `Exp~1~ ==> Exp~2~` | ((Boolean-Implication)) |  Implication on Boolean values |
| `Exp~1~ <==> Exp~2~` | ((Boolean-Equivalence)) | Equivalence on Boolean values |
| `Exp~1~ && Exp~2~` | ((Boolean-And)) | And on Boolean values |
| `Exp~1~ \|\| Exp~2~` |  ((Boolean-Or)) | Or on Boolean values |


#### Examples

#### Benefits

#### Pitfalls

