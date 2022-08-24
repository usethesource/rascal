# TypePal: Name and Type Analysis Made Easy 
      
.Synopsis
TypePal is a framework for name analysis, type checking and type inference

.Usage
  
.Details
Overview PocketCalculator ConceptsAndDefinitions Architecture Reporting Collector Solver Configuration Utilities Examples

.Description
Many type checkers have already been written in Rascal with good results. However, type checkers for different languages have a lot in common and address similar
questions:

* How to handle declarations of names, in particular their scope and uses?
* How to distinguish the roles names can play, e.g., names of functions, data types, labels, constants or variables?
* How to handle imports and multiple name spaces?
* How to associate requirements/constraints relevant for type checking with the syntax definition of the language being type checked?
* How to represent the types that occur in a program?
* How to handle global or local type inference?
* How to handle overloading?
* How to give precise error messages (and avoid spurious messages)?
* How to extract information that is useful for later compiler stages or the IDE for, 
  e.g., code generation, use-def information or name completion?
  
TypePal is a Rascal framework that addresses the above questions and we describe here all its concepts, architecture and features
and also show some examples:
 
loctoc::[1]

.Examples


.Benefits

.Pitfalls
