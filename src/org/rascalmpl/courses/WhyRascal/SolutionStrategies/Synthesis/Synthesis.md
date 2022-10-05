---
title: Synthesis
---

#### Synopsis

Strategies to synthesise results.

#### Syntax

#### Types

#### Function

#### Description

![Synthesis,Workflow]((define-synthesis.png))

Results are synthesized as shown in the Figure above. This consists of the following steps:

*  Determine the results of the synthesis phase. Wide range of results is possible including:

  **  Generated source code.

  **  Generated abstract representations, like finite automata or other formals models that capture properties of the SUI.

  **  Generated data for visualizations that will be used by visualization tools. 

*  If source code is to be generated, there are various options.

  **  Print strings with embedded variables.

  **  Convert abstract syntax trees to strings (perhaps using forms of pretty printing).

  **  Use a grammar of the target source language, also for code generation. 
      Note that this approach guarantees the generation of syntactically correct source code as opposed to code 
      generation using print statements or string templates.

*  If other output is needed (e.g., an automaton or other formal structure) write data declarations to represent that output.

*  Finally, write functions and rewrite rules that generate the desired results.


The Rascal features that are frequently used for synthesis are:

*  Syntax definitions or data declarations to define output formats.

*  Pattern matching (used in many Rascal statements).

*  Visits of datastructures and on-the-fly code generation.

*  Rewrite rules.

#### Examples

#### Benefits

#### Pitfalls

