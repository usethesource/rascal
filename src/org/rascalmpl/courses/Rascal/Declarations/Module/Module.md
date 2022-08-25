# Module Declaration

.Synopsis
Declare a module.

.Index
module

.Syntax
[source,rascal,subs="quotes"]
----
module _Name_
_Imports_;
_Declaration~1~_;
...
_Declaration~n~_;
----

.Types

.Function

.Details

.Description
A module declaration consists of:

*  A module name.
*  Zero or more imports;
*  Zero or more declarations.


The module name _Name_ will be used when the current module is imported in another module. 
A module name is in general a qualified name of the form:
[source,rascal,subs="quotes"]
----
_Name~1~_::_Name~2~_:: ... ::_Name~n~_
----
which corresponds to a path relative to the root of the current workspace.

The constituents of a module are shown in the figure below.

![]((module-parts.png))


An ((Import)) declares other modules that are used by the current module.
Following imports, a module may contain declarations (in arbitrary order, but a <<Syntax Definition>> can
occur directly following the imports) for:

*  <<Syntax Definition>>
*  <<Variable Declaration>>
*  <<Function Declaration>>
*  <<Algebraic Data Type>>
*  <<Alias Declaration>>
*  <<Annotation Declaration>>
*  <<Tag Declaration>>


Each declaration may contain a `private` or `public` keyword that determines 
the _visibility_ of the declared entity. 

The entities that are _visible inside_ a module are

*  The private or public entities declared in the module itself.

*  The public entities declared in any imported module.


The only entities that are _visible outside_ the module, are the public entities declared in the module itself. If different imported modules declare the same visible name, it can be disambiguated by explicitly qualifying it with its module name:

[source,rascal,subs="quotes"]
----
_Module_ :: _Name_
----

Each module resides in a separate file with extension `.rsc`.

.Examples
Here is the `Hello` module:

[source,rascal]
----
include::{LibDir}demo/basic/Hello.rsc[tags=module]
----

                
It defines a module with the name `demo::basic::Hello` and imports the [IO]((Libraries:Prelude-IO)) library.
Finally, it declares the `hello` function.

The actual source of this module can be found in `library/demo/basic/Hello.rsc` in the Rascal sources.

More ways to write this example are discussed in the [Hello]((Recipes:Basic-Hello)) example in [Recipes]((Recipes)).

.Benefits

.Pitfalls

