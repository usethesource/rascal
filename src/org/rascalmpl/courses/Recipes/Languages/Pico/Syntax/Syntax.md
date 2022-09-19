---
title: Syntax
---

#### Synopsis

Concrete syntax for Pico.

#### Syntax


#### Types

#### Function
       
#### Usage

#### Description

#### Examples

```rascal-include
demo::lang::Pico::Syntax
```

                
Notes:

*  `Id`, `Natural` and `String` are the basic lexical tokens of the Pico language.
*  `Layout` defines the white space and comments that may occur in a Pico program.
*  Some lexical rules are labeled with `@category="Comment"`. This is for the benefit of syntax highlighting.
*  The start symbol of the Pico grammar is called `Program`.
*  The rules for `Expression` describe the priority and associativity of the operators: all operators are left-associative and `||` has a higher priority then `+` and `-`.
*  Two auxiliary functions `program` are defined that parse a given string or a given location as Pico program.

#### Benefits

#### Pitfalls

