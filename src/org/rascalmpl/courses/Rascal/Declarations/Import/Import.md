---
title: Import
keywords:
  - import

---

#### Synopsis

Declare the import a module.

#### Syntax

`import QualifiedName;`

#### Types

#### Function

#### Description

An import has as effect that all public entities declared in module _QualifiedName_ are made available to the importing module. Circular imports are allowed. All publicly visible entities in the imported module become available in the importing module.

Import is _non-transitive_, i.e., the visible entities from an imported module are not re-exported by the importing module.

#### Examples

Here, is how to import the [IO]((Library:module:IO)) library:
```rascal-shell
import IO;
println("IO library was imported.");
```

#### Benefits

#### Pitfalls

