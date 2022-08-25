# Func

.Synopsis
Func is a tiny functional language; we present several interpreters for it.

.Syntax

.Types

.Function

.Details

.Description
Func is a functional language with the following features:

* A program consists of a number of function declarations.
* A function declaration consists of a name, zero or more formal parameter names and an expression.
* An expression can be one of:
  **  an integer constant.
  **  a variable.
  **  arithmetic operators `+`, `-`, `*` and `/`.
  **  comparison operators `<`, `\<=`, `>` and `>=`.
  **  a call of a function.
  **  an `if` expression.
  **  a sequence of expressions (`;`).
  **  an assignment (`:=`).
  **  a `let` expression to introduce new bindings for local variables.
  **  an address of a variables (denoted by `&`).
  **  derefence of a variable (denoted by `*`).


Some features add more complexity to an interpreter, therefore
we present four interpreters ((Eval0)), ((Eval1)), ((Eval2)) and ((Eval2))
that implement increasingly complex features:


|====
| Feature              | Eval0 | Eval1 | Eval2 | Eval3

| function declaration | y     | y     | y     | y
| integer constant     | y     | y     | y     | y
| variable             | y     | y     | y     | y
| arithmetic operators | y     | y     | y     | y
| comparison operators | y     | y     | y     | y
| call                 | y     | y     | y     | y
| if                   | y     | y     | y     | y
| let                  |       | y     | y     | y
| sequence             |       |       | y     | y
| assignment           |       |       | y     | y
| address operator     |       |       |       | y
| dereference operator |       |       |       | y
|====

.Examples
Here are several versions of the factorial function
that use more and more features of the Func language:

`F0.func`:
[source,rascal]
----
include::{LibDir}demo/lang/Func/programs/F0.func[]
----

`F1.func`:
[source,rascal]
----
include::{LibDir}demo/lang/Func/programs/F1.func[]
----

`F2.func`:
[source,rascal]
----
include::{LibDir}demo/lang/Func/programs/F2.func[]
----

`F3.func`:
[source,rascal]
----
include::{LibDir}demo/lang/Func/programs/F3.func[]
----

                
For convenience, we use two versions of these examples for each _F_~i~:

*  A file _F~i~_`.func` that contains the code as shown above.
*  A file _F~i~_`.rsc` a Rascal file that declares a string variable _F~i~_ with the same content.


For instance, `F0.rsc` looks like this 
[source,rascal]
----
include::{LibDir}demo/lang/Func/programs/F0.rsc[]
----

NOTE: Note the escaped `<` character in `\\<=`. This is necessary since `<` and `>` are used
in strings to enclose interpolations (insertion of the value of a Rascal expression).
Both symbols need to be escaped when used as literal symbol and not as interpolation.
                
.Benefits

.Pitfalls

