---
title: Extend
keywords:
  - extend
  - import

---

#### Synopsis

Declare an extended module

#### Syntax

```rascal
extend QualifiedName;
```

#### Types

#### Function

#### Description

An extend has as effect that all entities declared in module _QualifiedName_ are cloned into available to the extending module. Circular extends are _not_ allowed. All entities in the extended module become available in the importing module, and all overloaded functions and data-types are extended.

Extend is _transitive_, i.e., the visible entities from an imported module are re-exported by the extending module, and extended modules that extend other modules therefore leak their downstream definitions into the current module.

#### Examples

#### Benefits

* `extend` implements open extensibility in a way that also makes recursive definitions such as ((AlgebraicDataType))s and ((Function))s open to extention. Their recursive calls even though possibly pre-compiled and part of a library are still going to invoke the _extended_ version that is defined by the composition defined by the extend declarations of the current module.
* if you code your data types and functions nicely, there is no "Expression Problem" in Rascal

#### Pitfalls

* Extend quickly lets namespaces grow, and you have to make sure to manage that by using lots of overloading or descriptive names. Otherwise names can be prefixed by their original module for disambiguation. However, that disambiguaton will not un-extend the invoked entity: recursive calls will still invoke the current top module.
