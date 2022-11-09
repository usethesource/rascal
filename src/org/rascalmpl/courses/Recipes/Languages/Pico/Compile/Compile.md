---
title: Compile
---

#### Synopsis

Compile a Pico program to assembly language.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

#### Examples

The Pico compiler translates Pico programs to ((Pico-Assembly)) language programs.

```rascal-include
demo::lang::Pico::Compile
```

Notes:

<1> We introduce `Instrs` as an alias for a list of assembly language instructions.
<2> The compiler consists of the functions `compileExp`, `compileStat`, `compileStats`, `compileDecls` and `compileProgram`.
    They all have a program fragment as argument and return the corresponding list of instructions.
<3> When compiling expressions, note how _list splicing_ (see ((Rascal:Values-List))) is used to insert the instructions that are generated for the operands of an operator into the list of instructions for the whole expression.
<4> In order to conveniently write code generators for statements, we introduce a unique label generator. The global variable `nLabel` contains
    the index of the last generated label and `nextLabel` uses this to generate a new, unique label.
<5> Consider code generation for an if-the-else statement:
    *  Two fresh labels mark the start of the code for the else part (`elseLab`) and the end of the whole statement (`endLab`).
    *  The code that is generated consists of the following:
        *  Code for the test.
        *  A gofalse to the code for the else-part.
        *  Code for the then-part and a jump to the end of the statement.
        *  Code for the else-part that starts with the label `elsePart`.
        *  The label `endLab` that marks the end of the code for the if-then-else statement.
<6>  Compiling a list of statements conveniently uses a list comprehension and list splicing.
<7>  Compiling declarations allocates memory locations of the appropriate type for each declared variable.
<8>   `compileProgram` compiles a gives Pico program to assembly language.

Here is an example:
```rascal-shell
import demo::lang::Pico::Compile;
compileProgram("begin declare x : natural; x := 47 end");
```

Here is the compilation of the factorial program:
```rascal-shell,continue
compileProgram("begin declare input : natural,  
               '              output : natural,           
               '             repnr : natural,
               '              rep : natural;
               '      input := 14;
               '      output := 1;
               '      while input - 1 do        
               '          rep := output;
               '          repnr := input;
               '          while repnr - 1 do
               '             output := output + rep;
               '             repnr := repnr - 1
               '          od;
               '          input := input - 1
               '      od
               'end");
```

#### Benefits

#### Pitfalls

