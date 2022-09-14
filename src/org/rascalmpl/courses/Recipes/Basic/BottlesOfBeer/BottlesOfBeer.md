---
title: Bottles Of Beer
---

#### Synopsis

A Rascal version of a generator for the _99 Bottles of Beer_ song.

#### Syntax

#### Types

#### Function

#### Description

Programs that generate the lyrics for the song _99 Bottles of Beer_ are a popular way to compare programming languages.
At [99-bottles-of-beer.net](http://99-bottles-of-beer.net/) you can find versions in nearly 1500 different languages
and the lyrics can be found [here](http://99-bottles-of-beer.net/lyrics.html).

#### Examples

Here is our version:
```rascal
include::{LibDir}demo/basic/Bottles.rsc[tags=module]
```

                
<1> We use an auxiliary function `bottles` that returns the word "bottle" adjusted for the actual number of bottles that is available.
Observe how we use the patterns `0`, `1` and `int n` in the definition of three variants of this function.

<2> _Pattern-directed invocation_ (see [function declaration]((Rascal:Declarations-Function))) will determine at the call site which function will be called. The
general case is labeled with `default` to indicate that if the case for 0 and 1 do not match, this alternative should handle the other cases. 

<3> The main function is `sing` that iterates over the numbers 99 down to 1 (as described by the range `[99 .. 0]`)
and prints appropriate lyrics. Observe how the value  of the `bottles` function is interpolated several times in the string.

Here is the result:

```rascal-shell
import demo::basic::Bottles;
sing();
```


#### Benefits

#### Pitfalls

