---
title: Analysis
---

.Synopsis
Strategies to analyze software systems.

.Syntax

.Types

.Function

.Description
The analysis workflow is shown in the figure ((Analysis-Workflow)) below and consists of two steps:

*  Determine the results that are needed for the synthesis phase.

*  Write the Rascal code to perform the analysis. This may amount to:

  **  Reordering extracted facts to make them more suitable for the synthesis phase.

  **  Enriching extracted facts. Examples are computing transitive closures of extracted facts 
      (e.g., A may call B in one or more calls), or performing data reduction by abstracting aways details
      (i.e., reducing a program to a finite automaton).

  **  Combining enriched, extracted, facts to create new facts.

![Analysis,Workflow]((define-analysis.png))

As before, validate, validate and validate the results of analysis. Essentially the same approach can be used as for validating the facts. Manual checking of answers on random samples of the SUI may be mandatory. It also happens frequently that answers inspire new queries that lead to new answers, and so on.

The Rascal features that are frequently used for analysis are:

*  List, set and map comprehensions.

*  The built-in operators and library functions, in particular for lists, maps, sets and relations.

*  Pattern matching (used in many Rascal statements).

*  Visits and switches to further process extracted facts.

*  The solve statement for constraint solving.

*  Rewrite rules to simplify results and to enforce constraints.

.Examples

.Benefits

.Pitfalls

