---
title: Interpreter
---

.Synopsis
Directly execute the statements of a program.

.Syntax

.Types

.Function
       
.Usage

.Description

There are two methods to execute a program that is written in some source language:

*  An http://en.wikipedia.org/wiki/Interpreter_(computing)[Interpreter] directly executes the source statements (but see the variations below).
*  A ((Compiler)) translates the source program to some efficient executable form. That executable form is then executed by a hardware
  processor.


Interpreters exist in many flavours:

1.  Direct execution of the source.
2.  First parse the source text and build an ((Abstract Syntax Tree)) that is then interpreted.
3.  As (2), but convert the AST first to an intermediate form that is more suitable for execution.
  Then interpret that intermediate form.
4.  As (2), but compile frequently executed parts of the the AST to executable code.


Clearly, going down this list, the interpreter more and more starts resembling a compiler.

The advantages of interpreters versus compiler are:

*  Interpreter:
**  Pro: simpler than compiler, faster development loop, better debugging facilities, better error messages.
**  Con: slower.
*  Compiler:
**  Pro: fast execution.
**  Con: complex, optimizations are error-prone.


.Examples

.Benefits

.Pitfalls

