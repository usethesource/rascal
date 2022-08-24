# RedeclaredType

.Synopsis
A type with the same name has been declared before.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
Some declarations introduce new type names. Most important are an 
link:/Rascal#Declarations-AlgebraicDataType[algebraic data type] and link:/Rascal#Declarations-Alias[alias].
This error signals that the same type name is used for incompatible purposes.

Remedy: rename one of the type names.

.Examples
[source,rascal-shell,error]
----
data D = d(int x);
alias D = str;
----

[source,rascal-shell,error]
----
alias D = int;
alias D = str;
----

.Benefits

.Pitfalls

