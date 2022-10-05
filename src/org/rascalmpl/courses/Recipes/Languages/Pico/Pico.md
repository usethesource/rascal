---
title: Pico
---

#### Synopsis

The classical toy language, including a specialized IDE.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

Pico is a toy language that has been used as example over the years in many projects and disguises.
Pico has a single purpose in life: being so simple that specifications (or implementations) of every possible language aspect are so simple that they fit on a few pages. It can be summarized as follows:

*  There are two types: natural numbers and strings.

*  Variables have to be declared.

*  Statements are assignment, if-then-else and while-do.

*  Expressions may contain naturals, strings, variables, addition (`+`), subtraction (`-`) and concatenation (`||`).

*  The operators `+` and `-` have operands of type natural and their result is natural.

*  The operator `||` has operands of type string and its results is also of type string.

*  Tests in if-then-else statement and while-statement should be of type natural.


The following aspects of the Pico language will be discussed:

(((TOC)))

#### Examples

Here is a -- not so simple -- Pico program that computes the factorial function:


```rascal,lineNumbers
// highlight-next-line
begin declare input : natural, 
              output : natural,           
              repnr : natural,
              rep : natural;
      input := 14;
      output := 1;
// highlight-next-line
      while input - 1 do 
          rep := output;
          repnr := input;
          while repnr - 1 do
             output := output + rep;
             repnr := repnr - 1
          od;
          input := input - 1
      od
end
```

Notes:
	
* Pico programs do not have input/output statements, so we use variables for that purpose.
* Pico has no multiplication operator so we have to simulate it with repeated addition. Simplicity comes at a price.

#### Benefits

* Pico is such a small, but functionally complete, language that it is easy to demonstrate how to specify its syntax and semantics.

#### Pitfalls

* Pico is a small programming language, not really a domain specific language. Some aspects of DSL design and implementation are not demonstrated by Pico.
