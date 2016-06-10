# UninitializedPatternMatch

.Synopsis
Pattern matching has not been properly initialized.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
link:/Rascal#Concepts-PatternMatching[Pattern matching] requires two ingredients:

*  One of the many link:/Rascal#Rascal-Patterns[patterns].
*  A non-void subject value to which the pattern is applied.

This error is generated when the subject is void.

Remedy: replace the subject by a non-void value.

.Examples
here is a (contrived) example that produces this error:
[source,rascal-shell,error]
----
void dummy() { return; }
int n := dummy();
----

.Benefits

.Pitfalls

