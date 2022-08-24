# Operators

.Synopsis
The Rascal operators.

.Syntax

.Types

.Function

.Details

.Description
An _operator expression_ consists of an operator and one or more operands. The evaluation order of the operands depends on the operator. 
The operator is applied to the operands and the resulting value (or values in some cases) is the result of the operator expression. 

All operators are summarized in the following table. They are listed from highest precedence
to lowest precedence. In other words, operators listed earlier in the table bind stronger.
 
[cols="25,30,45"]
|====
| Operator                          | See                                           | Short Description


| `_Exp_ . _Name_`
| <<Location-FieldSelection,Location>>,
  <<DateTime-FieldSelection,DateTime>>,
  <<Tuple-FieldSelection,Tuple>>,
  <<Relation-FieldSelection.Relation>>,
| Select named field from structured value


| `_Exp_~1~ [ _Name_ = _Exp_~2~ ]`
| <<FieldAssignment>>
| Change value of named field of structured value


| `_Exp_ < _field_~1~, ... >`
| <<FieldProjection>>
| Select fields from relation or structured value


| `_Exp_ is _Name_`
| <<ParseTree>>,
  <<Concrete Syntax>>,
  <<Algebraic Data Type>>
| Returns true if and only if the constructor name of the value produced by _Exp_ is equal to _Name_


| `_Exp_ has _Name_` 
| <<ParseTree>>,
  <<Concrete Syntax>>,
  <<Algebraic Data Type>>
| Returns true if and only if the constructor (node or parse tree) of the value produced by _Exp_ has any field labeled _Name_


| `_Exp_~1~ [ _Exp_~2~ , _Exp_~3~, .... ]`
| <<List-Subscription,List>>,
  <<Map-Subscription,Map>>,
  <<Tuple-Subscription,Tuple>>,
  <<Relation-Subscription,Relation>>
| Retrieve values for given index/key from list, map, tuple or relation.


| `_Exp_~1~ [ _Exp_~2~ , _Exp_~3~ .. _Exp_~4~ ]`
| <<Values-List-Slice,List>>,
  <<String-Slice,String>>,
  <<Node-Slice,Node>>
| Retrieve a slice from a list, string, or node.


|  `_Exp_?`
| <<Boolean-IsDefined,Boolean>>
| Test whether an expression has a defined value


|  `!_Exp_`
| <<Boolean-Negation,Boolean>>
| Negate a Boolean value


| `- _Exp_`
| <<Number-Negation,Number>>
| Negation of numbers


| `_Exp_ +`
| <<Relation-TransitiveClosure,Relation>>,
  <<ListRelation-TransitiveClosure,ListRelation>>,
| Transitive closure on relation or list relation


| `_Exp_ *`
| <<Relation-ReflexiveTransitiveClosure,Relation>>,
  <<ListRelation-ReflexiveTransitiveClosure,ListRelation>>
| Reflexive transitive closure on relation or list relation


| `_Exp_ @ _Name_`
| <<Expressions-Selection>>
| Value of annotation _Name_ of _Exp_'s value


| `_Exp_~1~ [@ _Name_ = _Exp_~2~]`
| <<Expressions-Replacement>>
| Assign value of _Exp_~2~ to annotation _Name_ of _Exp_~1~'s value


| `_Exp_~1~ o _Exp_~2~`
| <<Relation-Composition,Relation>>,
  <<Map-Composition,Map>>
| _Exp_~1~ and _Exp_~2~ should evaluate to a relation or map; return their composition. Note: the letter "o" is thus a keyword


| `_Exp_~1~ / _Exp_~2~`
| <<Number-Division,Number>>
| Divide two numbers


| `_Exp_~1~ % _Exp_~2~`
| <<Number-Remainder,Number>>
| Remainder on numbers

| `_Exp_~1~ * _Exp_~2~`
| <<Number-Multiplication,Number>>,
  <<List-Product,List>>,
  <<Set-Product,Set>>,
  <<Relation-CartesianProduct,Relation>>
| Multiply numbers; product of list, set, or relation


| `_Exp_~1~ & _Exp_~2~`
| <<List-Intersection,List>>,
  <<Set-Intersection,Set>>,
  <<Map-Intersection,Map>>
| Intersection of list, set (including relation), or map


| `_Exp_~1~ + _Exp_~2~`
| <<Number-Addition,Number>>,
  <<String-Concatenation,String>>,
  <<List-Concatenation,List Concatenation>>,
  <<List-Insert, List Insert>>,<<List-Append, List Append>>,
  <<Tuple-Concatenation>>,
  <<Set-Union,Set>>,
  <<Map-Union,Map>>,
  <<Location-AddSegment,Location>>
| Add numbers; concatenate string, list or tuple; 
  union on set (including relation), or map;
  concatenate location and string
  
  
| `_Exp_~1~ - _Exp_~2~`
| <<Number-Subtraction,Number>>,
  <<List-Difference,List>>,
  <<Set-Difference,Set>>,
  <<Map-Difference,Map>>
| Subtract numbers; difference of list, set (including relation), or map


| `_Exp_~1~ join _Exp_~2~`
| <<Relation-Join,Relation>>
| Join on relation


| `_Exp_~1~ in _Exp_~2~`
| <<List-in,List>>, 
  <<Set-in,Set>>,
  <<Map-in,Map>>
| Membership test for element in list, map, set (including relation)


| `_Exp_~1~ notin _Exp_~2~`
| <<List-notin,List>>,
  <<Set-notin,Set>>,
  <<Map-notin,Map>>
| Negated membership test for element in  list, map, set (including relation)


| `_Exp_~1~ <= _Exp_~2~`
| <<Number-LessThanOrEqual,Number>>,
  <<String-LessThanOrEqual,String>>,
  <<Location-LessThanOrEqual,Location>>,
  <<DateTime-LessThanOrEqual,DateTime>>,
  <<List-SubList,List>>,
  <<Set-SubSet,Set>>,
  <<Map-SubMap,Map>>
| Less than or equal on all values

| `_Exp_~1~ < _Exp_~2~`
| <<Number-LessThan,Number>>,
  <<String-LessThan,String>>,
  <<Location-LessThan,Location>>,
  <<DateTime-LessThan,dateTime>>,
  <<List-StrictSubList,List>>,
  <<Set-StrictSubSet,Set>>,
  <<Map-StrictSubMap,Map>>
| Less than on all values


| `_Exp_~1~ >= _Exp_~2~`
| <<Number-GreaterThanOrEqual,Number>>,
  <<String-GreaterThanOrEqual,String>>,
  <<Location-GreaterThanOrEqual,Location>>,
  <<DateTime-GreaterThanOrEqual,DateTime>>,
  <<List-SuperList,List>>,
  <<Set-SuperSet,Set>>,
  <<Map-SuperMap,Map>>
| Greater than or equal on all values


| `_Exp_~1~ > _Exp_~2~`
| <<Number-GreaterThan,Number>>,
  <<String-GreaterThan,String>>,
  <<Location-GreaterThan,Location>>,
  <<DateTime-GreaterThan,DateTime>>,
  <<List-StrictSuperList,List>>,
  <<Set-StrictSuperSet,Set>>,
  <<Map-StrictSuperMap,Map>>
| Greater than on all values.


|  `_Pat_ := _Exp_`
| <<Boolean-Match>>
| Pattern matches value of expression

|  `_Pat_ !:= _Exp_`
| <<Boolean-NoMatch>>
| Pattern does not match value of expression


| `_Exp_~1~ == _Exp_~2~`
| <<Number-Equal,Number>>,
  <<String-Equal,String>>,
  <<Location-Equal,Location>>,
  <<DateTime-Equal,DateTime>>,
  <<List-Equal,List>>,
  <<Set-Equal,Set>>,
  <<Map-Equal,Map>>
| Equality on all values


| `_Exp_~1~ != _Exp_~2~`
| <<Number-NotEqual,Number>>,
  <<String-NotEqual,String>>,
  <<Location-NotEqual,Location>>,
  <<DateTime-NotEqual,DateTime>>,
  <<List-NotEqual,List>>,
  <<Set-NotEqual,Set>>,
  <<Map-NotEqual,Map>>
| Inequality on all values


| `_Exp_~1~ ? _Exp_~2~`
| <<Boolean-IfDefinedElse>>
| Value of expression when it is defined, otherwise alternative value

| `_Exp_~1~ ? _Exp_~2~ : _Exp_~3~`
| <<Value-Conditional>>
| Conditional expression for all types


| `_Exp_~1~ ==> _Exp_~2~`
| <<Boolean-Implication>>
| Implication on Boolean values


| `_Exp_~1~ <==> _Exp_~2~`
| <<Boolean-Equivalence>>
| Equivalence on Boolean values


| `_Exp_~1~ && _Exp_~2~`
| <<Boolean-And>>
| And on Boolean values


| `_Exp_~1~ \|\| _Exp_~2~`
| <<Boolean-Or>>
| Or on Boolean values

|====

.Examples

.Benefits

.Pitfalls

