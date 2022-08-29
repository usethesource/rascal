# No Such Field

.Synopsis
Field of an abstract data type value cannot be found at runtime.

.Types
`data RuntimeException = NoSuchField(str name);`
       
.Usage
`import Exception;` (only needed when `NoSuchField` is used in `catch`)

.Description
Selecting a field from an abstract datatype depends on the actual constructor 
being used at run time. This exception is thrown when a non-existent field is accessed.


.Examples

Consider this highly simplified view on persons:
```rascal-shell,error
data Person = man(str name, bool beard) | woman(str name, bool necklace);
jane = woman("jane", false);
```
The field `beard` is evidently only applicable to a `man` but not to a woman
(didn't we say "simplified", above):

```rascal-shell,continue,error
jane.beard;
```
