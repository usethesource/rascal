---
title: Eval
---

.Synopsis
A Lisp interpreter.

.Syntax

.Types

.Function
       
.Usage

.Description

Here is the core of our Lisp interpreter. Its basic functionality is to take

*  An `Lval` and an Environment (both defined in ((Lisra-Runtime))).
*  Distinguish the various forms an `Lval` can have and compute the
  effect of evaluating it.
*  Return a `Result` that captures the value just computed and possibleside-effects
on the environment.


Rascal provides pattern-directed dispatch: a function with the same name
can have complete patterns as arguments. When called, a pattern match determines which
variant of the function will be called. This is used extensively in the definitions below:

```rascal
include::{LibDir}demo/lang/Lisra/Eval.rsc[tags=module]
```

                
We now explain the different cases in more detail:

<1> An integer constant evaluates to itself. Note how `Integer(int x)` is used as first
    argument of this `eval` function. It is a pattern that describes that the constructor `Integer`
    with an `int` argument `x` is to be matched.
<2> An atom evaluates to the value to which it is bound or to itself. `find` (see [Runtime]) is used
    to search for the atom in question. The first argument is `var:Atom(str name)`, a pattern that matches
    an `Atom`. The `var:` prefix binds the complete atom to a variable `var` to be used in the body of the function.
<3> A quoted list evaluates to itself. The pattern `List([Atom("quote"), exp*])` matches a `List` constructor
    whose first element is `Atom("quote")`. `exp*` means that the remaining list elements are assignment to `exp`.
    There are two cases: if the argument list has size 1, its first element is used, otherwise a list with all elements of `exp`
    vare returned. This ensures that `List([Atom("quote"), Integer(17)])` evaluates to  `Integer(17)` and not to `List([ Integer(17)]`.
<4> Evaluates a `set!` expression that assigns the value of `exp` to variable `var`.

<5> Evaluates the `if` expression. The test `tst` is evaluated and is not false, the value of `conseq` is returned and otherwise
    that of `alt`.

<6> Evaluates a `block` expression. The list of expressions `exps` is evaluated one by one. Observe that in the for loop
    `<val, e> = eval(exp, e);` captures both the value and the environment that results from executing one expression. That new environment is
    is used to evaluate the next expression(s) in the list. The value of the last expression and a possible modied environment are returned.

<7> Evaluate a `define` expression that binds the value of `exp` to variable `var`.
    The value of the expression is bound `var` in the local scope.

<8> Evaluate a lambda expression. Essentially we return a `Closure` value that contains the expression in the lambda expression
    properly wrapped to do variable binding and environment management. 
    A Closure contains a function that return type `Results` and has two arguments:
   `list[lval] args` the actual parameter values when the closure is applied, and
   `Env e` the environment at the site of the call.
    In the body of the closure we construct a new environment `makeEnv(vars, args, tail(callEnv, size(defEnv)))` that binds the variables
    in the lambda expression to the actual parameter values. What is special here is that we shorten the calling environment to the
    same length as the defining environment. This implements _lexical scoping_ and avoids that names are visible in the called
    function that were not visible when the function was defined. Remember that Rascal values are immutable, meaning that after a value was 
    created it cannot be changed. Using the above trick, we ensure that the called function has access to the most recent version of
    its environment.

<9> Evaluates an arbitrary list. As a special case, the empty list is returned as false.
    Otherwise, all elements are evaluated and the auxiliary function ` apply` is used to apply the value of the first element to the values of   
    the remaining elements.

<10> Apply an `Lval` to a list of arguments and return a `Result`. The first case handles a `Closure`; it amounts
     to calling the function in the closure (environment handling and parameter binding are done in the closure as discussed above.

<11> Definition of all built-in functions.

<12> A default function that prints an error message when an undefined function is called.

.Examples

```rascal-shell
import demo::lang::Lisra::Runtime;
import demo::lang::Lisra::Eval;
eval(Integer(5));
eval(Atom("x"));
eval(List([Atom("+"), Integer(5), Integer(7)]));
```

.Benefits

*  A very modular, rule-based, type safe Lisp interpreter.

.Pitfalls

*  It is no pleasure to type in `Lval`s directly, that is why a parser is needed, see ((Lisra-Parse)).

