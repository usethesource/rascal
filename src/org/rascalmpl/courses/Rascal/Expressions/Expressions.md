# Expressions

.Synopsis
The expressions available in Rascal.

.Syntax

.Types

.Function

.Details
Values Operators

.Description
The expression is the basic unit of evaluation and may consist of the ingredients shown in the figure.

*  An elementary _literal value_, e.g. constants of the types <<Values-Boolean>>, <<Values-Integer>>, <<Values-Real>>, 
  <<Values-Number>>, <<Values-String>>, <<Values-Location>> or <<Values-DateTime>>.

*  A _structured value_ for <<Values-List>>, <<Values-Set>>, <<Values-Map>>, <<Values-Tuple>> or <<Values-Relation>>. 
  The elements are first evaluated before the structured value is built.

*  A _variable_ that evaluates to its current value.

*  A call to a function or constructor:

**  A _function call_. First the arguments are evaluated and the corresponding function is called. 
     The value returned by the function is used as value of the function call. See <<Call>>.
**  A _constructor_. First the arguments are evaluated and then a data value is constructed for the 
     corresponding type. This data value is used as value of the constructor. 
     Constructors are functions that can be used in all contexts where functions can be used. See <<Values-Constructor>>.

*  An operator expression. The operator is applied to the arguments; the evaluation order of the arguments depends 
  on the operator. The result returned by the operator is used as value of the operator expression.  See <<Operators>>.

*  <<Comprehensions>>.

*  A <<Visit>> expression.

*  A <<Boolean Any>> expression.

*  An <<Boolean All>> expression.

*  Some statements like <<If>>, <<For>>, <<While>> and <<Do>> can also be used in expressions, see <<Statement as Expression>>.

.Examples

.Benefits

.Pitfalls

