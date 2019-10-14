# A Calculator Language
  
.Synopsis
Calc illustraties the basic facilities of TypePal

.Description

By developing a type checker for *_Calc_*, a tiny pocket calculator language, we illustrate elementary usage of TypePal.
The full source code of Calc can be found at https://github.com/cwi-swat/typepal/tree/master/src/examples/calc.
See <<Examples of Typecheckers>> for a list of all available type checker examples.

== Syntax of Calc

[source,rascal]
----
module lang::calc::Syntax

extend lang::CommonLex;     //<1>

start syntax Calc
    = Decl+                 //<2>
    ;

syntax Decl
    = "var" Id "=" Exp ";"  //<3>
    ;
   
syntax Exp 
   = Id                             //<4>
   | Integer                        //<5>
   | Boolean                        //<6>
   | bracket "(" Exp ")"            //<7>
   > left Exp "*" Exp               //<8>                               
   > left Exp "+" Exp               //<9>
   > "if" Exp "then" Exp "else" Exp //<10>
   ;   

keyword Reserved
    = "var" | "if" | "then" | "else"
    ;     
----  

<1> For brevity we use some common lexical definitions  in `lang::CommonLex`. They take, amongst others, care of whitespace, comments and
    definitions for integer and Boolean constants (`Integer` and `Boolean`) as well as identifiers (`Id`).
<2> A Calc program consists of one or more declarations (`Decl`).
<3> Each declaration introduces a new variable and binds it to the value of an expression.
<4> An expression can be an identifier (that should have been introduced in a preceding declaration), or
<5> an integer constant, or
<6> a Boolean constant, or
<7> be surrounded by parentheses, or
<8> a multiplication (arguments should have the same type), or
<9> an addition (arguments should have the same type), or
<10> a conditional expression (the first expression should have type Boolean, the other two should have the same type).

== Typechecking Calc

Type checking Calc amounts to checking the following:

- Every name that is used in an expression should have been defined in a declaration and has the type of the expression in that declaration.
- Addition or multiplication on integers yields an integer type.
- Addition or multiplication on Booleans yields a Boolean type.
- The condition in a condional expression should be Boolean.
- The then and else part of a conditional expression should have the same type; that type becomes the type of the conditional expression.

Let's now move to the Calc type checker described in `lang::calc::Checker`. We break it up in parts
and describe the parts as we go. You should be explicitly aware of the fact that type checking consists of two phases:

- _Collecting facts and constraints_: this is all about formulating constraints and attaching them to parts of the source program.
  In this phase types are unknown and constraints can only be created but not yet  solved.
  All computations on types have therefore to be postponed until the solving phase. We will see how in a minute.
- _Solving constraints_: constraints are solved (if possible) and types are assigned to parts of the source program.

=== Begin of type checker module for Calc

[source,rascal]
----
module lang::calc::Checker

import lang::calc::Syntax;                  // The Calc syntax
extend analysis::typepal::TypePal;          // TypePal
----

We import the Calc syntax `lang::calc::Syntax` and then we _extend_ the TypePal `analysis::typepal::TypePal`.

NOTE: There is a technical reason why we have to extend TypePal rather than import it. Some data types (e.g., `AType`)
and functions (e.g., `collect`) are already defined in TypePal itself and they are being extended in each type checker.

=== Extend AType

TypePal has a built-in data type to represent types: `AType`. This data type can be extended to represent language-specific types.

[source,rascal]
----
data AType   
    = boolType()    
    | intType()                                     
    ;
    
str prettyAType(boolType()) = "bool";
str prettyAType(intType()) = "int";
----

The values in Calc are Booleans and integers and we create two `AType` constants `boolType()` and `intType()` to represent these types.
`prettyAType` converts them back to a string representation; this is used in error reporting.

=== Collecting facts and constraints

Writing a type checker in TypePal amounts to collecting facts and constaints from a source program to be checked.
This is achieved by the `collect` function that has the form
[source,rascal,subs="verbatim,quotes"]
----
void collect(current: _syntactic pattern_, Collector c){ ... }`
----

where _syntactic pattern_ corresponds with a rule from the  language syntax, in this case `lang::calc::Syntax`.
The `Collector` argument provides methods to add facts and constraints.

NOTE: One `collect` declaration is needed per alternative in the syntax but some rules (i.e., chain rules) are handled automatically.

NOTE: By convention the syntactic pattern is named `current` and the Collector is named `c`.

WARNING: Each `collect` declaration is responsible for calling `collect` on relevant subparts (a run-time error is given for a missing `collect`).

==== Check Declaration

A declaration introduces a new name and associates the type of the righthand side expression with it.
[source,rascal]
----
void collect(current: (Decl) `var <Id name> = <Exp exp> ;`, Collector c){
    c.define("<name>", variableId(), current, defType(exp));
    collect(exp, c);
}
----
Here we define `name` as a variable and define its type as the same type as `exp`. Let's digest this:

- `c.define(...)` calls the `define` function in the `Collector` and adds this definition to the internal state of the Collector.
- `name` is a parse tree for the lexical syntax rule `Id`. 
  `"<name>"` reduces this tree to a string and this is how the variable can be used later on.
- `variableId()` defines that the role of this name (`IdRole`) is a variable. `variableId` is a built-in role `IdRole`.
- `current` is the parse tree of the whole declaration, we use it as unique identification of the definition of `name`.
- `defType` is used to define the type of `name` and it comes in several flavours. Here, we use another parse tree fragment `exp` as argument.
  `defType(exp)` should be read as _the same type as_ `exp` and the effect is that `name` is defined having the same type as `exp`.
  This implies that the type of `name` can only be known when the type of `exp` is known.

==== Check Exp: Id

An expression consisting of a single identifier, refers to a name introduced in another declaration
and gets the type introduced in that declaration.

[source,rascal]
----
void collect(current: (Exp) `<Id name>`, Collector c){
    c.use(name, {variableId()});
}
----

An expression consisting of a single identifier represents a _use_ of that identifier.
`c.use(_name_, _roles_)` records this. There are then two possibilities:

- a matching define is found for one of the given roles: use and definition are connected to each other.
- no matching define is found and an error is reported.

NOTE: In larger languages names may be defined in different scopes. Scopes do not play a role in Cal.

NOTE: We do not enforce _define-before-use_ in this example, but see <<XXX>> how to achieve this.

==== Check Exp: Boolean and Integer constants

[source,rascal]
----
void collect(current: (Exp) `<Boolean boolean>`, Collector c){
    c.fact(current, boolType());
}

void collect(current: (Exp) `<Integer integer>`, Collector c){
    c.fact(current, intType());
}
----

When encountering a Boolean or integer constant we record their type using `c.fact(current, _its type_)`.

NOTE: The second argument of `fact` maybe an `AType`, an arbitrary parse tree (in which case the type of that tree will be used),
      or a function that returns a type.

==== Check Exp: parentheses
[source,rascal]
----
void collect(current: (Exp) `( <Exp e> )`, Collector c){
    c.fact(current, e);
    collect(e, c);
}
----
The type of an expression enclosed by parentheses is the same as the same of the enclosed expression.

A final, essential, step is to collect constraints from the subpart `e`.

==== Check Exp: addition

Addition of integers given an integer result and addition of Booleans gives a Boolean result.
[source,rascal]
----
void collect(current: (Exp) `<Exp e1> + <Exp e2>`, Collector c){
     c.calculate("addition", current, [e1, e2],
        AType (Solver s) { 
            switch(<s.getType(e1), s.getType(e2)>){
                case <intType(), intType()>: return intType();
                case <boolType(), boolType()>: return boolType();
                default:
                    s.report(error(current, "`+` not defined for %t and %t", e1, e2));
            }
        });
      collect(e1, e2, c);
}
----

Checking addition is more complex since we need the type of sub expressions `e1` and `e2` in order to do our job.
The type of an arbitrary parse tree can be computed using 

`c.calculate(_informative label_, current, _list of dependencies_, AType(Solver s) { ... }`.

where

* _informative label_ is a label that can be used in error messages and log files.
* `current` is the parse tree for which we are computing a type.
* _list of dependencies_ is a list of other parse trees whose type is needed in the computation of the type of `current`.
* `AType(Solver s) { ... }` performs the type computation:
    ** it is only called when the types of all dependencies are known.
    ** it has a `Solver` as argument: a `Solver` manages the constraint solving process and is aware of all facts and solved constraints so far and
       knows, for instance, about the existence of type information for some parse tree.
    ** it either returns an `AType` or reports an error.
  
The above code could be paraphrased as follows:
_The type of `current` can only be computed when the types of `e1` and `e2` are known. When known,
get their types (using `s.getType`) and distinguish cases: two integer arguments give an integer result type and two Boolean arguments a Boolean result type; 
otherwise report an error._

A final, essential, step is to collect constraints from the subparts `e1` and `e2`.

==== Check Exp: multiplication
[source,rascal]
----
void collect(current: (Exp) `<Exp e1> * <Exp e2>`, Collector c){
     c.calculate("multiplication", current, [e1, e2],
        AType (Solver s) { 
            switch(<s.getType(e1), s.getType(e2)>){
                case <intType(), intType()>: return intType();
                case <boolType(), boolType()>: return boolType();
                default:
                    s.report(error(current, "`*` not defined for %t and %t", e1, e2));
            }
        });
      collect(e1, e2, c);
}
----
Checking multiplication follows exactly the same pattern as checking addition. Even in this simple example we see repetition of code that could be factored out.

==== Check Exp: conditional expression
[source,rascal]
----
void collect(current: (Exp) `if <Exp cond> then <Exp e1> else <Exp e2>`, Collector c){
    c.calculate("if Exp", current, [cond, e1, e2],
        AType(Solver s){
            s.requireEqual(cond, boolType(),
                           error(cond, "Condition should be Boolean, found %t", cond));
            s.requireEqual(e1, e2, 
                           error(current, "Equal types required, found %t and %t", e1, e2));
            return s.getType(e1);
        });
    collect(cond, e1, e2, c);
}
----
Checking a conditional expression amounts to checking that the condition has type Boolean and
that the then and else branch have the same type (which also becomes the type of the conditional expression as a whole).
In the above code we see `s.requireEqual(_arg1_, _arg2_, _message_)`. 
Here _arg1_ and _arg2_ may either be a subtree (in which case its type is used) or an `AType`.
`requireEqual` requires that both types are equal or reports an error.

A final, essential, step is to collect constraints from the subparts `cond`, `e1` and `e2`.

== Testing the Calc typechecker


=== Getting started

[source,rascal]
----
module lang::calc::Test

extend lang::calc::Checker;
extend analysis::typepal::TestFramework;    // TypePal test utilities
import ParseTree;                           // In order to parse tests
----

We need three ingredients for testing:

- `lang::calc::Checker`, the Calc type checker we have just completed.
- `analysis::typepal::TestFramework`, the TypePal test framework which enables test automation.
- `ParseTree` that provides parsing functionality that is needed to parse Calc source text.

=== Manual testing

[source,rascal]
----
TModel calcTModelFromTree(Tree pt){
    return collectAndSolve(pt);
}
----
Given a parse tree `pt` for a Calc program, we apply <<collectAndSolve>> to it. This creates a Collector, uses it to collect constraints from `pt` and then creates a Solver 
to solve all constraints. The result is a `TModel`. `calcTModelFromTree` will also be used during automated testing.

[source,rascal]
----
TModel calcTModelFromStr(str text){
    pt = parse(#start[Calc], text).top;
    return calcTModelFromTree(pt);
}
----
In order to obtain a parse tree, we need to parse it as shown above.

TModels contain much information but here we are only interested in the messages that have been generated during constraint solving.
They are contained in the `messsages` field of a TModel.

With the above machinery in place we can perform some experiments:

- `calcTModelFromStr("var x = 3;"),messages` gives no messages.
- `calcTModelFromStr("var x = 3+true;").messsages` reports that addition of integer and Boolean is illegal.

=== Automated testing
[source,rascal]
----
bool calcTests() {
     return runTests([|project://typepal-examples/src/lang/calc/tests.ttl|], 
                     #Calc, calcTModelFromTree);
}
----

TypePal's testing framework enables scripting of tests. Each script is written in TTL (TypePal Testing Language).
Use `runtests(_list of TTL files_, _start non-terminal_, _function to create a TModel_)` to execute the scripts.

Here is a sample script:

[source, TTL]
----
test Ok1 [[ var x = 1; ]]

test Ok2 [[ var x = 1; var y = x + 2; ]]

test Undef1 [[ var x = y; ]] 
expect { "Undefined variable `y`" }

test Add1 [[ var x = 1 + 2; ]]

test Add2 [[ var x = true + false; ]]

test Add3 [[ var x = 1 + true; ]] 
expect { "`+` not defined for `int` and `bool`" }

test If1 [[ var x = if true then 1 else 2; 
            var y = x + 1;]]
test If2 [[ var x = if true then 1 else false; ]]
expect { "Equal types required, found `int` and `bool`" }

test If3 [[ var x = if 1 then 2 else 3; ]] 
expect { "Condition should be Boolean, found `int`" }
----


and here is the produced output:

[source]
----
Test Ok1: true
Test Ok2: true
Test Undef1: true
Test Add1: true
Test Add2: true
Test Add3: true
Test If1: true
Test If2: true
Test If3: true
Test summary: 9 tests executed, 9 succeeded, 0 failed
----