---
title: Lisra
details:
  - Lisra-Syntax
  - Runtime
  - Lisra-Parse
  - Pretty
  - Eval
  - Test

---

#### Synopsis

A Lisp interpreter in Rascal.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

Writing a Lisp interpreter is a classical challenge. 
Popular word has that all large applications evolve until they include a Lisp interpreter.
(A variant says the same about including an email client in every large application).

We will closely follow and *reuse parts of* Peter Norvig's excellent page
on [Lispy](http://norvig.com/lispy.html), a Lisp interpreter written in Python.
The Lisp variant to be implemented is the following subset of the [Scheme](http://en.wikipedia.org/wiki/Scheme_(programming_language))
 language:


| Form | Syntax | Semantics and Example |
| --- | --- | --- |
| [variable reference](http://www.schemers.org/Documents/Standards/R5RS/HTML/r5rs-Z-H-7.html#%_sec_4.1.1) |
| _var_ |
| A symbol is interpreted as a variable name; |
  its value is the variable's |
  value. Example: `x` |
| [constant literal](http://www.schemers.org/Documents/Standards/R5RS/HTML/r5rs-Z-H-7.html#%_sec_4.1.2) |
| _number_ |
| A number evaluates to itself. Example: `12` |
| [quotation](http://www.schemers.org/Documents/Standards/R5RS/HTML/r5rs-Z-H-7.html#%_sec_4.1.2) |
| `(quote exp)` |
| Return the _exp_ literally; do not evaluate it. Example: |
  `(quote (a b c)) =>; (a b c)` |
| [conditional](http://www.schemers.org/Documents/Standards/R5RS/HTML/r5rs-Z-H-7.html#%_sec_4.1.5) |
| `(if _test conseq alt_)` |
| Evaluate _test_; if true, |
  evaluate and return _conseq_; otherwise evaluate and return  |
  _alt_. <br />Example: `(if (< 10 20) (+ 1 1) (+ 3 3)) => 2` |
| [assignment](http://www.schemers.org/Documents/Standards/R5RS/HTML/r5rs-Z-H-7.html#%_sec_4.1.6) |
| `(set! _var exp_)` |
| Evaluate _exp_ and assign that value to |
  _var_, which must have been previously defined (with a |
  `define` or as a parameter to an enclosing procedure). |
   Example: `(set! x2 (* x x))` |
| [definition](http://www.schemers.org/Documents/Standards/R5RS/HTML/r5rs-Z-H-8.html#%_sec_5.2) |
| `(define var exp)` |
| Define a new variable in the innermost environment and give it |
  the value of evaluating the expression _exp_.  |
  Examples: `(define r 3)` or `(define square (lambda (x) (* x x)))` |
| [procedure](http://www.schemers.org/Documents/Standards/R5RS/HTML/r5rs-Z-H-7.html#%_sec_4.1.4) |
| `(lambda (_var..._) exp)` |
| Create a procedure |
  with parameter(s) named _var..._ and the expression as the body. |
  Example: `(lambda (r) (* r r))` |
| [sequencing](http://www.schemers.org/Documents/Standards/R5RS/HTML/r5rs-Z-H-7.html#%_sec_4.2.3) |
| `(begin _exp..._)` |
| Evaluate each of the expressions in left-to-right order, and return the final value. |
  Example: `(begin (set! x 1) (set! x (+ x 1)) (* x 2)) => 4 |
| [procedure call](http://www.schemers.org/Documents/Standards/R5RS/HTML/r5rs-Z-H-7.html#%_sec_4.1.3) |
| `(_proc exp..._)` |
| If _proc_ is |
   anything other than one of the symbols `if`, `set!`, `define`, |
   `lambda`, `begin`, or `quote` then it is treated as a procedure.  It is |
   evaluated using the same rules defined here. All the expressions |
   are evaluated as well, and then the procedure is called with the list |
   of expressions as arguments.  |
   Example: <`(square 12) => 144 |


In this table, _var_ must be a symbol--an identifier such as x or square--and number must be an integer number, 
while the other italicized words can be any expression. The notation _exp_... means zero or more repetitions of _exp_.

A Lisp interpreter consists of the following parts:

*  A _parser_ that reads a Lisp program in text form and converts it to a runtime representation that is suitable for the interpreter.
*  The _interpreter_ itself that executes the program in runtime representation and computes its outcome.
*  A _pretty printer_ that converts the outcome in internal representation back to text.
*  Finally, an interactive  _console_ is needed that interact with the user.


We discuss all these aspects:

(((TOC)))

#### Examples

#### Benefits

#### Pitfalls

