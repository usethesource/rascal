---
title: "Call"
keywords: "(,)"
---

#### Synopsis

Functions and constructors can be called or invoked in a uniform style.

#### Syntax

* function call with positional parameters `Name ( Exp~1~, Exp~2~, ... )`
* function call with keyword parameters `Name (Name~1~ = Exp~1~, Name~2~ = Exp~2~, ...)`
* function call with both positional and keyword parameters `Name (Exp~1~, Exp~2~, ..., Name~1~ = Exp~1~, Name~2~ = Exp~2~, ...)`
* function calls with computed functions `Exp ( Exp~1~, Exp~2~, ..., Name~1~ = Exp~1~, Name~2~ = Exp~2~, ...) 

#### Types

//

| `Exp~1~`  | `Exp~2~` | ... | `Name ( Exp~1~, Exp~2~, ... )`  |
| --- | --- | --- | --- |
| `T~1~`    | `T~2~`   | ... | Determined by _Name_, _T~i~_ and function declarations  |


#### Function

#### Description

For the purpose of calling a function or a constructor, we make no distinction between these two concepts.
A constructor is a function that constructs an application of an algebraic data-type constructor definition,
so it has no body, but it is a function which can be called.

First, the actual parameter expressions _Exp_~i~ are evaluated resulting in values _V_~i~.
Based on _Name_ and the argument types _T_~i~, the identity of the function to be called is determined.

The values _V_~i~ are bound to the formal parameter names of the 
declared functions and the function body is executed.
The value returned by the function is used as value of the function call.

For the keyword parameters a similar evaluation produces values for each expression and those values
are bound to the respective names. 
* The order of keyword parameters is irrelevant in the call syntax, as opposed to the order of the positional parameters. 
* Notably, values are _also_ bound for the keyword parameters which are _not listed_ in the call site. For those values, _default_ expressions are evaluation which are retrieved from the ((Declarations-Function)) signature. 
* For ((Declarations-Function))s those default parameters are computed and bound at the time of calling the function
* For ((AlgebraicDataType)) constructors, the missing default parameters are computed, lazily, at the moment of ((FieldProjection)).

For more information:
* see ((Declarations-Function)) for more details about function declarations.
* see ((AlgebraicDataType)) for more details about constructor declarations.

In case of [function overloading]((Declarations-Function)), where there a more definitions of the same function (the same name and argument arity), there is a selection process called "dynamic dispatch". The functions are tried in arbitrary order,
and if their signature [matches]((PatternMatching)), and their body does not ((Fail)), then the return value of that function is used. Otherwise, the next function alternative is tried until a succesful alternative is found. 
If the match of the signature is non-unitary, it involves backtracking, then a single function
may be tried many times.

If the name of the function in the call is

#### Examples

First declare a function `square` with argument _n_ that returns _n^2_:
```rascal-shell,continue
int square(int n) { return n * n; }
```

Next call `square`. This results in the following steps:

* Based on the name `square` and the int argument 12 we identify the function to be called
  (= the function `square` we just defined).
* Compute the value of the actual parameter (= 12).
* Bind the formal parameter `n` to the actual value 12.
* Execute the body of `square`.
* The return value of square is the vale of the call:

```rascal-shell,continue
square(12);
```

#### Benefits

* calls with positional parameters are the classical way of function invocation in Mathematics and Computer Science
* calls with keyword parameters provide readability at the call site and we do not have to remember the invocation order
* calls with keyword parameters are always bound by defaults, so there is never a `null` reference

#### Pitfalls

