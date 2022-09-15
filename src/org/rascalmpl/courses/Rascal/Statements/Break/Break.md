---
title: Break
keywords:
  - break

---

#### Synopsis

End the execution of a while, do or for loop.

#### Syntax

#### Types

#### Function

#### Description

A `break` statement is only allowed inside the body of a ((While)), ((Do)) or ((For)) statement
and is associated with the innermost loop statement in which it is contained.
Its effect is to end the execution of the loop.

Also see ((Continue)) and ((Fail)).

#### Examples

Here is an example using break to find the first number divisible by 3:
```rascal-shell
import IO;
for(int i <- [1 .. 10]){
    if(i % 3 == 0){
       println("i = <i> is divisible by 3");
       break;
    }
}
```

#### Benefits

#### Pitfalls

