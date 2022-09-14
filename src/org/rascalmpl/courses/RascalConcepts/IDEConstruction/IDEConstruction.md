---
title: IDE Construction
---

#### Synopsis

Extend an IDE with interactive, language-specific, features (Eclipse or VSCode)

#### Syntax

#### Types

#### Function

#### Description

Meta-programs become most useful, when they are integrated with an Interactive Development Environment (IDE). 

A Rascal program running inside Eclipse can get access to many of the services provided by Eclipse such as syntax highlighting,
outlining, documentation hovering and much more. Similarly Rascal programs running inside VScode have access to all features
of the [language server protocol](https://microsoft.github.io/language-server-protocol/), and more.

For Eclipse, Rascal uses the services of the IDE Meta-tooling Platform, or [IMP](http://www.eclipse.org/imp/) for short, a collection of API and tools to support constructing IDEs for programming languages and domain specific languages. 

For VScode, Rascal builds on top of the [language server protocol](https://microsoft.github.io/language-server-protocol/) for its own services, and for services that
Rascal programmers create for their own languages.

To instantiate an IDE for a language implemented using Rascal, use the following steps:

*  Define the grammar for the language.
*  Define a parse function for the language.
*  Register the language with Eclipse or VScode.

You find more information on these topics in the [Rascal Eclipse](/Eclipse.md) and [Rascal Language Servers](/RascalLanguageServers.md) documentation.


