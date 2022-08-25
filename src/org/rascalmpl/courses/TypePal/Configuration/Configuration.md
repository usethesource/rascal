# TypePal Configuration
  
.Synopsis
Configuration options for TypePal

.Description

TypePal provides configuration options for

* _Name Resolution & Overloading_: configures how names are resolved and which overloading is allowed.
* _Operations on Types_: configures how operations like subtype and least-upper-bound (lub) are defined.
* _Retrieval of Types_: configures how named and structured types are handled.
* _Extension Points_: configures operations before an after solving.
* _Miscellaneous_: utility functions that can be configured.
* _Verbosity_: configures the verbosity of TypePal.

Here is an overview:

image::TypePalConfig.png[600,600,align="center"]

== Name Resolution & Overloading

=== isAcceptableSimple
[source,rascal]
----
/* Configuration field */ Accept (TModel tm, loc def, Use use) isAcceptableSimple
----

Here

* `tm` is a given TModel
* `def` is a proposed definition
* `use` is the use 
  (characterized by the `Use` data type that contains, name, occurrence, scope and identifier roles of the use, see <<Identifier Use>>)
  for which the definition is proposed.

`isAcceptableSimple` accepts or rejects a proposed definition for the use of a simple name in a particular role. 
The returned `Accept` data type is defined as:
[source,rascal]
----
data Accept 
    = acceptBinding()
    | ignoreContinue()
    | ignoreSkipPath()
    ;
----

The default `isAcceptableSimple` returns acceptBinding()`.

Typical concerns addressed by `isAcceptableSimple` are:

* enforce definition before use;
* check access rights, e.g. visibility.

By comparing the offset of the source locations of definition, respectively, the use,
we enforce definition before use:

[source,rascal]
----
Accept myIsAcceptableSimple(TModel tm, loc def, Use use)
    = use.occ.offset > def.offset ? acceptBinding() : ignoreContinue();
----

=== isAcceptableQualified
[source,rascal]
----
/* Configuration field */ Accept (TModel tm, loc def, Use use) isAcceptableQualified
----
Here

* `tm` is a given TModel
* `def` is a proposed definition
* `use` is the use for which the definition is proposed.

`isAcceptableQualified` accepts or rejects a proposed definition for the use of a qualified name in a particular role.
  
=== isAcceptablePath
[source,rascal]
----
/* Configuration field */ 
Accept (TModel tm, loc defScope, loc def, Use use, PathRole pathRole) isAcceptablePath
----
Here

* `tm` is a given TModel;
* `defScope` is the scope in which the proposed definition occurs;
* `def` is a proposed definition;
* `use` is the use for which the definition is proposed;
* `pathRole` is the role of the semantic path.

`isAcceptablePath` accepts or rejects a proposed access path between use and definition.

To illustrate this, assume a language with modules and imports. A module may contain variable definitions
but these are not visible from outside the module. This can be enforced as follows:

[source,rascal]
----
Accept myIsAcceptablePath(TModel tm, loc def, Use use, PathRole pathRole) {
    return variableId() in use.idRoles ? ignoreContinue() : acceptBinding();
}
----

=== mayOverload
[source,rascal]
----
/* Configuration field */ bool (set[loc] defs, map[loc, Define] defines) mayOverload 
----

`mayOverload` determines whether a set of definitions (`defs`) are allowed to be overloaded,
given their definitions (`defines`).

In ((FWJava)) the only allowed overloading is between class names and constructor names.
[source,rascal]
----
bool fwjMayOverload (set[loc] defs, map[loc, Define] defines) {
    roles = {defines[def].idRole | def <- defs};  //<1>
    return roles == {classId(), constructorId()}; //<2>
}
----
<1> First collect all the roles in which the overloaded names have been defined.
<2> Only allow the combination of class name and constructor name.


== Operations on Types
Various operations on types can be configured by way of user-defined functions.

=== isSubType
[source,rascal]
----
/* Configuration field */ bool (AType l, AType r) isSubType 
----
Function that checks whether `l` is a subtype of `r`.

=== getLub
[source,rascal]
----
/* Configuration field */ AType (AType l, AType r) getLub
----
Function that computes the least upperbound of two types and `l` and `r`.

=== getMinAType
[source,rascal]
----
/* Configuration field */ AType() getMinAType 
----
Function that returns the _smallest_ type of the type lattice.

=== getMaxAType
[source,rascal]
----
/* Configuration field */ AType() getMaxAType
----
Function that returns the _largest_ type of the type lattice.

=== instantiateTypeParameters
[source,rascal]
----
/* Configuration field */ AType (Tree current, AType def, AType ins, AType act, Solver s) instantiateTypeParameters
----

The function `instantiateTypeParameters` defines instantiation of *language-specific* type parameters, where:

* `current` is a source code fragment for which type `act` has already been determined, but any *language-specific* type parameters
  in `act` may still need to be instantiated.
* `def` is the parameterized type of `act`.
* `ins` is an instantiated version of the type of `act` (i.e., with bound type parameters).
* `act` is the actual type found for `current` that needs to be instantiated.

`instantiateTypeParameters` will match `def` with `ins` and the resulting bindings will be used to instantiate `act`.
The instantiated version of `act` is returned.

In ((StructParameters)) parameterized structs (records) are defined. 
The formal type of such a struct is ``structDef(str name, list[str] formals)``, 
i.e., a struct has a name and a list of named formal type parameters.
The actual type of a struct is ``structType(str name, list[AType] actuals)``, 
i.e., a struct name followed by actual types for the parameters.

The definition of `instantiateTypeParameters` for this example is as follows:

[source,rascal]
----
AType structParametersInstantiateTypeParameters(Tree current, structDef(str name1, list[str] formals), structType(str name2, list[AType] actuals), AType t, Solver s){
    if(size(formals) != size(actuals)) throw checkFailed([]);
    bindings = (formals[i] : actuals [i] | int i <- index(formals));
    
    return visit(t) { case typeFormal(str x) => bindings[x] };
}

default AType structParametersInstantiateTypeParameters(Tree current, AType def, AType ins, AType act, Solver s) = act;
----

== Retrieval of Types

=== getTypeNamesAndRole
[source,rascal]
----
/* Configuration field */  tuple[list[str] typeNames, set[IdRole] idRoles] (AType atype) getTypeNamesAndRole
----
This function determines whether a given `atype` is a named type or not. This is needed for the customization
of indirect type computations such as ((useViaType)) and ((getTypeInType)). When `atype` is a named type
`getTypeNamesAndRole` returns:

* A list of names that may be associated with it. In most languages this will contain just a single element, the name of the type.
  In more sophisticated cases the list may contain a list of named types to be considered.
* A list of roles in which the type name can be bound.

Here are the definitions for <<Struct>:
[source,rascal]
----
tuple[list[str] typeNames, set[IdRole] idRoles] structGetTypeNamesAndRole(structType(str name)){
    return <[name], {structId()}>; //<1>
}

default tuple[list[str] typeNames, set[IdRole] idRoles] structGetTypeNamesAndRole(AType t){
    return <[], {}>;               //<2>
}
----
<1> A `structType(_name_)` has a name that is bound in the role `structId()`. Return the name and role.
<2> Any other type is unnamed; return an empty list of type names.

Another example is the Rascal type checker, where we need to model the case that all abstract data types are a subtype of `Tree`.
In that case `getTypeNamesAndRole` will return `<["A", "Tree"], _roles_>` for an abstract data type `A`. The net effect
is that when the search for a name in `A` fails, the search is continued in `Tree`.

=== getTypeInTypeFromDefine
[source,rascal]
----
/* Configuration field */  AType (Define containerDef, str selectorName, set[IdRole] idRolesSel, Solver s) getTypeInTypeFromDefine
----
In some extreme cases (think Rascal) the type of a field selection cannot be determined by considering all the fields
defined in a container type and as a last resort one needs to fall back to information that has been associated with the original definition
of the container type. `getTypeInTypeFromDefine` is called as a last resort from `getTypeInType`.

In the Rascal type checker common keyword parameters of data declarations are handled using `getTypeInTypeFromDefine`.


=== getTypeInNamelessType
[source,rascal]
----
/* Configuration field */ AType(AType containerType, Tree selector, loc scope, Solver s) getTypeInNamelessType
----
`getTypeInNamelessType` describes field selection on built-types that have not been explicitly declared with a name.
A case in point is a `length` field on a built-in string type.

In ((StaticFields)) this is done as follows:
[source,rascal]
----
AType staticFieldsGetTypeInNamelessType(AType containerType, Tree selector, loc scope, Solver s){
    if(containerType == strType() && "<selector>" == "length") return intType();
    s.report(error(selector, "Undefined field %q on %t", selector, containerType));
}
----

== Extension Points
    
=== preSolver
[source,rascal]
----
/* Configuration field */ TModel(map[str,Tree] namedTrees, TModel tm) preSolver
----
A function `preSolver` that can enrich or transform the TModel before the Solver is applied to it.

=== postSolver
[source,rascal]
----
/* Configuration field */ void (map[str,Tree] namedTrees, Solver s) postSolver
----
A function `postSolver` that can enrich or transform the TModel after constraint solving is complete.


== Miscellaneous

=== unescapeName
[source,rascal]
----
/* Configuration field */  str(str) unescapeName  
----
A function _unescapeName_ to define language-specific escape rules for names.
By default, all backslashes are removed from names.

=== validateConstraints
[source,rascal]
----
/* Configuration field */ bool validateConstraints = true
----
When `validateConstraints` is true, the validity of all constraints is checked before solving starts.
For all dependencies (in facts, calculators and requirements) a calculator needs to be present to solve that dependency.

== Verbosity

The verbosity of TypePal can be controlled with several configurations settings.

=== showTimes
[source,rascal]
----
/* Configuration field */ bool showTimes = false
----
When `showTimes` is true, the time of the Collector and Solver phases is printed.

=== showSolverSteps
[source,rascal]
----
/* Configuration field */ bool showSolverSteps = false
----
When `showSolverSteps` is true, each step of the Solver is printed.

=== showSolverIterations
[source,rascal]
----
/* Configuration field */ bool showSolverIterations = false
----
When `showSolverIterations` is true, information is printed about each iteration of the Solver.

=== showAttempts
[source,rascal]
----
/* Configuration field */ bool showAttempts = false
----
When `showAttempts` is true, the number of evaluation attempts per calculator or requirement is printed when solving is complete.

=== showTModel
[source,rascal]
----
/* Configuration field */ bool showTModel = false
----
When `showTModel` is true, the resulting TModel is printed when solving is complete.
