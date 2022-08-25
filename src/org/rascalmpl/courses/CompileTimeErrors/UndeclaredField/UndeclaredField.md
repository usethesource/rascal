# UndeclaredField

.Synopsis
A field name is used that has not been declared.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
Fields of [tuple]((Rascal:Values-Tuple)) (hence also of [relation]((Rascal:Values-Relation))
 and [listrelation]((Rascal:Values-ListRelation)))
and constructors of [algebraic data types]((Rascal:Declarations-AlgebraicDataType)) may have names.
This error is generated when a reference is made to an undeclared field.

Remedies:

*  Fix the field name in the reference.
*  Declare a new field as used in the reference.

.Examples
Use of the undeclared field `gender`:
[source,rascal-shell,error]
----
tuple[str name, int age] Jo = <"Jo", 33>;
Jo.gender;
----
A similar example now expressed as ADT:
[source,rascal-shell,error]
----
data Person = person(str name, int age);
jo = person("Jo", 33);
jo.gender;
----

.Benefits

.Pitfalls

