# UndeclaredField

.Synopsis
A field name is used that has not been declared.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
Fields of link:/Rascal#Values-Tuple[tuple] (hence also of link:/Rascal#Values-Relation[relation]
 and link:/Rascal#Values-ListRelation[listrelation])
and constructors of link:/Rascal#Declarations-AlgebraicDataType[algebraic data types] may have names.
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

