---
title: Visiting
---

#### Synopsis

Visiting tree structures and arbitrary values.

#### Syntax

#### Types

#### Function

#### Description

Visiting the elements of a data structure is one of the most common operations in our domain 
and the visitor design pattern is a solution known to every software engineer. 
Given a tree-like data structure we want to perform an operation on some (or all) nodes of the tree. The purpose of the visitor design pattern is to decouple the logistics of visiting each node from the actual operation on each node. In Rascal the logistics of visiting is completely automated.

Visiting is achieved by way of visit expressions that resemble the switch statement. A visit expression traverses an arbitrarily complex subject value and applies a number of cases to all its subtrees. 
All the elements of the subject are visited. When one of the cases matches the statements associated 
with that case are executed. These cases may:

*  cause some side effect, i.e., assign a value to local or global variables;
*  execute an [Insert]((Rascal:Statements-Insert)) statement that replaces the current element;
*  execute a [Fail]((Rascal:Statements-Fail)) statement that causes the match for 
   the current case to fail.

The value of a visit expression is the original subject value with all replacements made as dictated by matching cases. 
The traversal order in a visit expressions can be explicitly defined by the programmer. 

#### Examples

Examples of visiting are, for instance, given in the Recipes 
[ColoredTrees]((Recipes:Common-ColoredTrees)) and [Derivative]((Recipes:Common-Derivative)).

#### Benefits

#### Pitfalls

