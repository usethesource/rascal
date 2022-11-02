---
title: Function Declaration
keywords:
  - public
  - private
  - java
  - test
  - default
  - "&"

---

#### Synopsis

Declare a function.

#### Syntax

```rascal
Modifiers Type Name( Type~1~ Var~1~, ..., Type~n~ Var~n~ ) Body
Modifiers Type Name( Type~1~ Var~1~, ..., Type~n~ Var~n~ Type~0~ Name~0~... ) Body
Modifiers Type Name( Pattern~1~, ..., Pattern~n~) Body
Modifiers Type Name( Pattern~1~, ..., Pattern~n~, Type~0~ Name~0~...) Body
```

where `Body` is one of:

```rascal
{ 
   Statements
}

throws Exception~1~, ..., Exception~n~

= Expression;
```

and where `Modifiers` may be:

```rascal
public
private
default
java
test

public java
private java
public test
private test
public default
private default
```

#### Types

#### Function

#### Description

##  Variant 1 

A function declaration introduces a new function with name _name_, typed formal parameters `Type~1~ Var~1~`, a return type _Type_
and a _Statement_ that forms the function body.
The type of _Statement_ should be equal to _Type_.

The formal parameters may be used in _Statement_ and get their value when function _Name_ is invoked.

##  Variant 2 

A function may have a variable list of arguments, this has as syntax variant 2 given above.

The last parameter of a function may be followed by `...` and this has as effect that all remaining actual parameters
that occur in a call to this function are collected as list value of the last formal parameter. 
Inside the function body, the type of this parameter will therefore be `list[Type~0~]`.

##  Variant 3 and 4 


All formal parameter of a function can be ((Patterns)). There are some restrictions however:

*  A Pattern in formal parameter positions may not refer to variables in the scope.
*  Patterns in formal parameter positions may not introduce fresh variables without an explicit type. 
*  The last parameter, if followed by `...` can only be a normal typed parameters, not just any pattern.


##  Body types 


*  Functions with list of statements as bodies must eventually use `return` or `fail` on every control flow path. 
*  The declarations to `throw` an exception are documentation only
*  Single expressions can be bodies of functions, the return value is the value of the expression.


##  Parameterized types in function declaration 


The types that occur in function declarations may also contain ((Type Parameters)).
In this way functions can be defined for arbitrary types. The type variable is bound (statically) at by the types of the parameters given at location of the call. The result type must be used at least once in any of the parameters.

##  Overloading 

 
Function definitions may be overloaded, i.e. a function with the same name may be defined twice and 
a function may redefine a constructor of an ((Algebraic Data Type)) or a ((Syntax Definition)).

There are some restrictions however:

*  Overloaded alternatives for the same function name but with different patterns must return the same type.
*  Overloaded alternatives for the same function name must have mutually exclusive patterns, unless one alternative is labeled `default` and the other is not. The patterns of formal parameters are mutually exclusive if for at least one parameter position:
**  They range over incomparable types, as in `int f(int a)` and `int f(real a)`, or
**  They range over different alternatives of an ((Algebraic Data Type)), as in `int f(and(Bool a, Bool b))` and `int f(or(Bool a, Bool b))`
**  They range over different alternatives of a ((Syntax Definition))
**  And note that deep matches using the `/` alternative are considered to be of type `value` and therefore overlap with all other patterns.
*  Overlapping patterns are allowed if the one alternative has the `default` modified while the other does not.
*  If a function is fallible, it uses the `fail` statement to back-track to a different alternative, then there must be a `default` alternative defined which can handle the general case. An [AlgebraicDataType] or a [SyntaxDefinition] with the same name and return type counts as a `default` alternative.
*  `default` functions may not fail.


##  Modifiers 

The _Modifiers_ affect _visibility_ and _special behaviour_ of functions:

*  _Visibility_: `private` declares that a function is only visible in the current module. 
  `public` declares that it is visible outside the module as well. When visibility is not specified, `private` is assumed.
*  _Special Behaviour_:
   **  `java` declares that the body of the function is implemented in Java. The function should have a
       `javaClass` annotation that determines where the Java implementation can be found.
   **  `test` declares that this is a test function. A test function is a boolean function (currently) without arguments. 
        It can be called as any other function. However, it can also be called automatically
        by the unit test framework, by typing `:test` at the command line, see ((Help)).
   **  `default` declares an alternative for an overloaded function that will only be tried after 
        all non-default alternatives have been tried. Note that ((Algebraic Data Type))s and ((Syntax Definition))s 
        _implicitly_ define `default` functions that may be overloaded by normal ((Function Declarations))s.


#### Examples

Declare a function
```rascal-shell,continue
rel[int, int] invert(rel[int,int] R){
   return {<Y, X> | <int X, int Y> <- R };
}
```
Call it
```rascal-shell,continue
invert({<1,10>, <2,20>});
```

In the following example we illustrate the use of type variables in function declarations.
Declare an inversion function that is applicable to any binary relation:
```rascal-shell
rel[&T2, &T1] invert2(rel[&T1,&T2] R){  
   return {<Y, X> | <&T1 X, &T2 Y> <- R };
}
```
Now apply it to relations with different types:
```rascal-shell,continue
invert2({<1,10>, <2,20>});
invert2({<"mon", 1>, <"tue", 2>});
```
As another example declare a function that can be used to swap the elements of pairs of arbitrary types
(also see ((Tuple-Subscription))):
```rascal-shell,continue
tuple[&T2, &T1] swap(tuple[&T1, &T2] TP) { return <TP[1], TP[0]>;}
swap(<1, 2>);
swap(<"wed", 3>);
```

Here we use an overloaded definition with incomparable patterns:

```rascal-shell
int f(int i) = 1;
int f(real r) = 2;
f(0);
f(0.0);
```

And we may use `default`, as in:

```rascal-shell
int f(0) = 1;
default int f(int n) = n * f(n - 1);
f(0);
f(2);
```

In combination with an ((Algebraic Data Type)), which defines `default` functions implicitly for every alternative, 
we can define canonicalization functions. The same holds for ((Syntax Definition))s, see ((Action))s.

This definition implies a default function for t(), f() and neg(B):

```rascal-shell,continue
data B = t() | f() | neg(B);
```

The following definition will remove any nested neg before it is even constructed:

```rascal-shell,continue
B neg(neg(B b)) = b;
neg(t());
neg(neg(f()));
```

#### Benefits

* Overloaded functions can be extended by other modules that extend the current module, and even be "fused" by modules that extend different modules that define the same overloaded signature. In this way you can write language processors **modularly**. The open extensibility of overloaded functions in Rascal is a way to circumvent the classes ["Expression Problem".](https://en.wikipedia.org/wiki/Expression_problem)
* Overloaded functions including complex pattern matching at the parameter positions are as powerful as term rewriting systems; they _are_ a kind of term rewriting system. Together with the ((SyntaxDefinition)) and ((AlgebraicDataType)) features (serving as many-sorted algebraic signatures), the overloaded functions allow for "algebraic specification" as a style of programming in Rascal. 

#### Pitfalls

* In case of overlapping function definitions, the order in which the functions are tried is left undefined. The only exceptions are functions marked `default`, those will be tried after non-`default` functions.
* Neither the Rascal compiler not its static checker, or the interpreter, check for the **completeness** of overloaded function definitions. The question: is do they handle all the cases that the grammar of the input data-types provide for? And if so, is every choice of prioritizing the different alternatives leading to the same answer? This problem is akin to the properties of **termination**, **unique normal forms** and **confluence** of term rewriting systems. A set of overloaded alternative functions, including a data constructor with the same name, are comparable to a term rewriting system for a many-sorted algebraic signature. 
