# Arithmetic Exception

.Synopsis
An arithmetic exception occurred.

.Types
`data RuntimeException = ArithmeticException(str message);`
       
.Usage
`import Exception;` (only needed when `ArithmeticException` is used in `catch`)

.Details

.Description
This error is generated when an illegal arithmetic operation occurs or when
a numeric function  is called with an out-of-range argument.

Remedies:

*  Check the validity of the argument before you call the function or apply the operator.
*  Catch the `ArithmeticException` yourself, see [try catch]((Rascal:Statements-TryCatch)) statement.

.Examples
Division by 0 gives an error:
```rascal-shell,error
3/0;
```
Giving an out-of-range argument to a mathematical function also gives an error:
```rascal-shell,error
import util::Math;
tan(-550000000000000000000000);
```
We can also catch the `ArithmeticException` error. First import the Rascal exceptions (which are also included in `Prelude`)
and `IO`:
```rascal-shell,error
import Exception;
import IO;
try println(3/0); catch ArithmeticException(msg): println("The message is: <msg>");
```

.Benefits

.Pitfalls

