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

Pico is a toy language that has been used as example over the years in many projects and disguishes,
Pico has a single purpose in life: being so simple that specifications of every possible language aspect are so simple that they fit on a few pages. It can be summarized as follows:

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


```rascal
begin declare input : natural, // <1>
              output : natural,           
              repnr : natural,
              rep : natural;
      input := 14;
      output := 1;
      while input - 1 do // <2>
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
	
<1> Pico programs do not have input/output statements, so we use variables for that purpose.
<2> Pico has no multiplication operator so we have to simulate it with repeated addition (yes, simplicity comes at a price!).



#### Benefits

#### Pitfalls

