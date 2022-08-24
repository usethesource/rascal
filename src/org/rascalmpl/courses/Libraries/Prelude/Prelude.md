# Prelude

.Synopsis
The Rascal standard prelude.

.Description

The `Prelude` library provides core functionality for handling all of Rascal's data-types.
See the link:/Rascal[Rascal Language Reference] for details of the language Rascal itself.

For 

* numeric functions, see the <<Math>> library,
* statistical functions see <<analysis-statistics>>,
* graph functions see <<analysis-graphs>>,
* other common utilitites see <<Libraries-util>>.

`Prelude` provides the following:

loctoc::[2]

.Benefits
This library makes it possible to import all core Rascal functionality with a single import.

.Pitfalls

* The prelude may contain more functionality than you need; this leads to an unnecessary longer import 
  and pollution of your name space.

* The precise contents of the prelude are under discussion.

* In the future, Rascal may automatically load the prelude.
