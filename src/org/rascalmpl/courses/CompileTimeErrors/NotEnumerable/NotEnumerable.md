# NotEnumerable

.Synopsis
A value that cannot be enumerated is used in an enumerator.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
An [enumerator]((Rascal:Comprehensions-Enumerator)) like `int n <- V` is used in 
[comprehensions]((Rascal:Expressions-Comprehensions)) to enumerate the values in `V`.
This error is produced when `V` is a value that does not support enumeration.
This is typically the case for atomic values like numbers, Booleans and Strings,

Remedy: modify the expression in the enumerator to return a value that supports enumeration.

.Examples
```rascal-shell,error
int x <- 17
b <- true
```

.Benefits

.Pitfalls

