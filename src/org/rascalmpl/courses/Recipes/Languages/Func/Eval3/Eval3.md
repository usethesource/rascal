# Eval3

.Synopsis
A complete Func interpreter including support for the address and dereference operators.

.Syntax

.Types

.Function

.Details

.Description

Interpreter Eval3 supports the following features of Func: 


|====
| Feature              | Eval3

| function declaration | y
| integer constant     | y
| variable             | y
| arithmetic operators | y
| comparison operators | y
| call                 | y
| if                   | y
| let                  | y
| sequence             | y
| assignment           | y
| __address operator__ | y
| __dereference operator__ | y
|====



The main additions are the address and dereference operators.

.Examples
[source,rascal]
----
include::{LibDir}demo/lang/Func/Eval3.rsc[tags=module]
----

                

We apply `eval3` to example `F3`:
[source,rascal]
----
include::{LibDir}demo/lang/Func/programs/F3.func[]
----

                
Let's try this.
[source,rascal-shell]
----
import demo::lang::Func::Load;
import demo::lang::Func::Eval3;
import demo::lang::Func::programs::F3;
eval3("fact", [10], load(F3));
----



.Benefits

.Pitfalls

