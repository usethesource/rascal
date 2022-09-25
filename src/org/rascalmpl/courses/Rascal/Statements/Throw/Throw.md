---
title: Throw
keywords:
  - throw

---

#### Synopsis

Throw any value as an exception up the call stack.

#### Syntax

`throw Exp`

#### Types

#### Function

#### Description

A throw statement causes the immediate abortion of the execution of the current function with _Exp_ \'s value as exception value.
The exception can be caught by a ((Try Catch)) statement in the current function or in one of its callers.
If the exception is not caught, the execution of the Rascal program is terminated. The following rules apply:

* Although it is not necessary, it is a coding standard to use the ((AlgebraicDataType)) `RuntimeException`, see [RuntimeException]((Library:Exception-RuntimeException)) to throw as _Exp_.
* Use new ((AlgebraicDataType)) data declarations that extend the type `RuntimeException` to create your own exception kinds.
* In general _Exp_ can be any value. 

See ((TryCatch)) for handling thrown values.

#### Examples

Here is a variant of string concatenation for ball haters:
```rascal-shell,error
str conc(str x, str y) { 
  if ("ball" in {x, y}) 
    throw "I hate balls"; 
  return x + y; 
}
conc("fairy", "tale");
conc("foot", "ball");
```

Here we throw an integer:
```rascal-shell,error
import List;
aList = [1,2,3];
if (size(aList) == 3) {
  throw size(aList);
}
```

Here we throw a constructor of ((Exception-RuntimeException)):
```rascal-shell,error
import Exception;
// highlight-next-line
data RuntimeException = facUndefinedOn(int cause);
int fac(int n) {
  if (n < 0) {
    // highlight-next-line
    throw facUndefinedOn(n);
  }
  else if (n == 0) {
    return 1;
  }
  else {
    return n * fac(n - 1);
  }
}
fac(-1)
```

#### Benefits

#### Pitfalls

