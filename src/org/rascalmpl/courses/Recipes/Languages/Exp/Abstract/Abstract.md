---
title: Abstract
---

#### Synopsis

A version of Exp based on abstract syntax.

#### Syntax

#### Types

#### Function

#### Description

The [abstract syntax]((AbstractSyntaxTree)) for a language is a
data type that is used to represent programs in the language in an _abstract_ form.
Abstract syntax has the following properties:

*  It is "abstract" in the sense that it does not contain textual details such as parentheses,
  layout, and the like.
*  While a language has one grammar (also known as, _concrete syntax_) it may have several abstract syntaxes
  for different purposes: type analysis, code generation, etc.

#### Examples

The abstract syntax for Exp looks like this:
```rascal-include
demo::lang::Exp::Abstract::Syntax
```

* Line 4 defines integer constants, e.g., `con(123)`.
* Line 5 defines multiplication, e.g., `mul(con(2),con(3))`.
* Line 6 defines addition, e.g., `add(con(2),con(3))`.

Given the abstract syntax for Exp, we can define an interpreter that evaluates
expressions. An interpreter, in this case, is a function that takes `Exp` as input
and produces `int` as output:

```rascal-include
demo::lang::Exp::Abstract::Eval
```

           
Here we see Rascal's _pattern-directed invocation_ in action (see [Function Declaration]((Rascal:Declarations-Function))).
The essence is this: in other languages the formal parameters in a function declaration
are just that: formal parameters, i.e., single names that can be used inside the function and
that are bound when the function is called.
In Rascal, however, the formal parameters are actually a _pattern_ and functions
can have arbitrarily complex patterns as (single) formal parameter.
These patterns may bind variables and thus introduce variables that can be used in tthe function body.

The big advantage of pattern-directed invocation is modularity and extensibility:

*  The treatment of the cases in the abstract syntax is decoupled.
*  If the abstract is extended later on with new cases, the functions for the old cases can be reused.


In this example we use this mechanism to define separate functions for each case in the abstract syntax.

<1> Defines the case for evaluating integer constants: they evaluate to themselves.
<2> Defines the case for evaluating multiplication: first evaluate the arguments `e1` and `e2`
    and return the multiplication of their values.
<3> Defines the case for evaluating addition: first evaluate the arguments `e1` and `e2`
    and return the addition of their values.


```rascal-shell
import demo::lang::Exp::Abstract::Syntax;
import demo::lang::Exp::Abstract::Eval;
eval(mul(con(7), con(3)));
eval(add(con(3), mul(con(4), con(5))));
```
Entering expressions in abstract syntax form is no fun, and this is where concrete syntax comes to the rescue.


#### Benefits

#### Pitfalls

