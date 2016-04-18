# Constructor

.Synopsis
Constructors create values for user-defined datatypes (Algebraic Datatypes).

.Syntax
`_Name_ ( _Exp_~1~, _Exp_~2~, ... )`

.Types

//

|====
| `_Exp~1~_` | `_Exp~2~_` | ... |  `_Name_ ( _Exp~1~_, _Exp~2~_, ... )` 

| `_T~1~_`   | `_T~2~_`   | ... | Depends on ADT declaration          
|====

.Function

.Details

.Description
In ordinary programming languages record types or classes exist to introduce a new type name for a collection of related, 
named, values and to provide access to the elements of such a collection through their name. 

In Rascal, algebraic data types provide this facility. They have to be declared, see <<Algebraic Data Type>>, and
then values can be created using calls to the declared constructor functions.
The constructor _Name_ should correspond (regarding name, arity and argument types) to one of the alternatives
in the ADT declaration.

First, the actual parameter expressions _Exp_~i~ are evaluated resulting in values _V_~i~.
Next, a data value is constructed in accordance with the declared data type
using the values _V_~i~ as arguments for the constructor. This data value is used as value of the constructor. 
Constructors are functions that can be used in all contexts where functions can be used.

Observe that the syntax of a constructor is identical to the syntax of an function <<Call>>.

.Examples

First, define a datatype `WF` for word frequencies:
[source,rascal-shell,continue]
----
data WF = wf(str word, int freq);
----
Then construct a new `WF` value by calling the constructor `wf` with appropriate arguments:
[source,rascal-shell,continue]
----
wf("Rascal", 10000);
----

.Benefits

.Pitfalls

