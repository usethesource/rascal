# IDE Construction

.Synopsis
Extend an IDE with interactive, language-specific, features (Eclipse only).

.Syntax

.Types

.Function

.Details

.Description

Meta-programs become most useful, when they are integrated with an Interactive Development Environment (IDE). 

A Rascal program running inside Eclipse can get access to many of the services provided by Eclipse such as syntax highlighting,
outlining, documentation hovering and much more.

Rascal uses the services of the IDE Meta-tooling Platform, or http://www.eclipse.org/imp/[IMP] for short, a collection of API and tools to support constructing IDEs for programming languages and domain specific languages. Rascal is also part of the collection of IMP tools and (will be) hosted shortly on eclipse.org.

Using the [IDE library]((Library:util-IDE)), you can instantiate the services that IMP provides for any language implemented in Rascal.

To instantiate an IDE for a language implemented using Rascal, use the following steps:

*  Define the grammar for the language.
*  Define a parse function for the language.
*  Register the language.


The following IDE features are available


.Examples

.Benefits

.Pitfalls

