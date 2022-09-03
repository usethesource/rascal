# Empty List

.Synopsis
Illegal operation on an empty list.

.Syntax

.Types
`data RuntimeException = EmptyList();`
       
.Usage
`import Exception;` (only needed when `EmptyList` is used in `catch`)

.Details

.Description
Rascal provides many operations and functions on lists, see [list values]((Rascal:Values-List)) 
and [list functions]((Library:List)).
This error is generated when a function or operation cannot handle the empty list.

Remedies:

* Guard the function or operation with a test on the empty list ([isEmpty]((Library:List-isEmpty))) and 
  take alternative action in that case.
* Catch the `EmptyList` yourself, see [try catch]((Rascal:Statements-TryCatch)).


.Examples

Import the `List` library and introduce `L` with an empty list as value:
```rascal-shell,error
import List;
L = [];
```
Taking the head of an empty list gives an error:
```rascal-shell,continue,error
head(L);
```
This is the case when taking the tail as well:
```rascal-shell,continue,error
tail(L);
```
We can also catch the `EmptyList` error. First import the Rascal exceptions (which are also included in `Prelude`)
and `IO`:
```rascal-shell,continue,error
import Exception;
import IO;
try 
  println(head(L)); 
catch EmptyList(): 
  println("Cannot take head of empty list");
```

.Benefits

.Pitfalls

