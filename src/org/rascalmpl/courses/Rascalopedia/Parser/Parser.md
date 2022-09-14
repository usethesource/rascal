---
title: Parser
---

.Synopsis
Check that a text adheres to the rules of a language (and return a ((ParseTree-ParseTree))).

.Syntax

.Types

.Function
       
.Usage

.Description

A [parser](http://en.wikipedia.org/wiki/Parsing) checks that a text in language _L_ indeed adheres 
to the syntax rules of language _L_. There are two possible answers:

*  _Yes_. A ((ParseTree-ParseTree)) is returned that shows how the text adheres to the syntax rules.
*  _No_. Error messages pin point the location where the text deviates from the syntax rules.


This is shown below:


![]((parser.png))


.Examples

.Benefits

.Pitfalls

