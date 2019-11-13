# Examples of Typecheckers
  
.Synopsis
Examples of type checkers built with TypePal.


.Description

TypePal is used in a new type checker for the Rascal meta-programming language and also in type checkers for half a dozen
domain-specific languages created by http://swat.engineering in the domains finance, forensics and privacy.

To get you started more quickly, we give below a list of complete TypePal-based type checkers ranging from very simple (intended only to illustrate specific TypePal features) to type checkers for FeatherweightJava and the complete Pascal programming language.
For each example we give a description, the most distinctive TypePal features that are used, and its source at GitHub.
Each example has the same structure:

* `Syntax.rsc` the syntax of the example language.
* `Checker.rsc` the type checker for the language.
* `Test.rsc` functions to call the type checker and test framework.
* `tests.ttl` test cases for the type checker.
* `examples` (optionally) a directory with example.

== Calc

[cols="1,8"]
|===
| *What*        | The pocket calculator language Calc; we already covered it <<A simple pocket calculator language>>
| *Illustrates* | fact, define, use, requireEqual, calculate, getType, report
| *Source*      | https://github.com/cwi-swat/typepal/tree/master/src/examples/calc
|===

== Pico
[cols="1,8"]
|===
| *What*        | Toy language with declared variables in single scope, assignment, if and while statement
| *Illustrates* | fact, define, use, enterScope, leaveScope, requireEqual, calculate, getType, report
| *Source*      | https://github.com/cwi-swat/typepal/tree/master/src/examples/pico
|===

== QL
[cols="1,8"]
|===
| *What*        | A questionnaire language, for describing forms with text entry fields and computed values
| *Illustrates* | fact, define, use, requireEqual, requireTrue, calculate, getType, report
| *Source*      | https://github.com/cwi-swat/typepal/tree/master/src/examples/ql
|===

== Fun
[cols="1,8"]
|===
| *What*        | Functional language with explicit types, function declarations and calls, let and if expressions
| *Illustrates* | fact, define, use, enterScope, leaveScope, requireEqual, calculate, getType, report
| *Source*      | https://github.com/cwi-swat/typepal/tree/master/src/examples/fun
|===

== ModFun

[cols="1,8"]
|===
| *What*        | Extension of <<Fun>> with modules
| *Illustrates* | PathRole, addPathToDef
| *Source*      | https://github.com/cwi-swat/typepal/tree/master/src/examples/modfun
|===

== Struct

[cols="1,8"]
|===
| *What*        | Simple named records
| *Illustrates* | useViaType, TypePalConfig, getTypeNamesAndRole
| *Source*      | https://github.com/cwi-swat/typepal/tree/master/src/examples/struct
|===

== Aliases
[cols="1,8"]
|===
| *What*        |  <<Struct>> extended with type aliases
| *Illustrates* | useViaType, TypePalConfig, getTypeNamesAndRole
| *Source*      | https://github.com/cwi-swat/typepal/tree/master/src/examples/aliases
|===


== StaticFields

[cols="1,8"]
|===
| *What*        | <<Struct>> extended with fields on non-record type
| *Illustrates* | useViaType, TypePalConfig, getTypeNamesAndRole, getTypeInNamelessType 
| *Source*      | https://github.com/cwi-swat/typepal/tree/master/src/examples/staticFields
|===

== StructParameters
[cols="1,8"]
|===
| *What*        | <<Struct>> with parameterized records
| *Illustrates* | useViaType, TypePalConfig, getTypeNamesAndRole, getTypeInNamelessType, instantiateTypeParameters
| *Source*      | https://github.com/cwi-swat/typepal/tree/master/src/examples/structParameters
|===

== SmallOO
[cols="1,8"]
|===
| *What*        | Small OO language without inheritance
| *Illustrates* | useViaType, TypePalConfig, getTypeNamesAndRole
| *Source*      | https://github.com/cwi-swat/typepal/tree/master/src/examples/smallOO
|===

== FWJava
[cols="1,8"]
|===
| *What*        | FeatherWeight Java, a minimal, Java-like, language with inheritance and constructors
| *Illustrates* | useViaType, addPathToDef, isSubType, TypePalConfig, getTypeNamesAndRole, mayOverload, preSolver, setScopeInfo, getScopeInfo
| *Source*      | https://github.com/cwi-swat/typepal/tree/master/src/examples/fwjava
|===

== Pascal
[cols="1,8"]
|===
| *What*        | The Pascal language (Second Edition, 1978)
| *Illustrates* |  useViaType, addPathToType, PathRole, isSubType, TypePalConfig, preCollectInitialization getTypeNamesAndRole 
| *Source*      | https://github.com/cwi-swat/typepal/tree/master/src/examples/pascal
|===

== UntypedFun
[cols="1,8"]
|===
| *What*        | Functional language with implicit (inferred) types, function declarations and calls, let and if expressions
| *Illustrates* | fact, define, use, enterScope, leaveScope, requireEqual, calculate, getType, report, newTypeVar, calculateEager, requireUnify, unify
| *Source*      | https://github.com/cwi-swat/typepal/tree/master/src/examples/untypedFun
|===
