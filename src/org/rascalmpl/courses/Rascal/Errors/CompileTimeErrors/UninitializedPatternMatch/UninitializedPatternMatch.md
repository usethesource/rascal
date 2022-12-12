---
title: UninitializedPatternMatch
---

#### Synopsis

Pattern matching has not been properly initialized.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

[Pattern matching](/docs/RascalConcepts/PatternMatching/) requires two ingredients:

*  One of the many [patterns]((Rascal:Rascal-Patterns)).
*  A non-void subject value to which the pattern is applied.

This error is generated when the subject is void.

Remedy: replace the subject by a non-void value.

#### Examples

Here is a (contrived) example that produces this error:
```rascal-shell,error
void dummy() { return; }
int n := dummy();
```

#### Benefits

* As far as we know there is no other way to trigger this static [error]((CompileTimeErrors)).

#### Pitfalls

* The error message seems to point to the pattern for the cause but the cause is in the static type (`void`) of the subject on the right.
