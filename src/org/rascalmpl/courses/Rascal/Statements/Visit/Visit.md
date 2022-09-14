---
title: "Visit"
keywords: "visit"
---

.Synopsis

The ((Expressions-Visit)) expression can also be used directly as a statement

.Syntax 

See ((Expressions-Visit)).

.Types

.Function

.Description

See ((Expressions-Visit)) for the details.

.Examples
```rascal-shell
x = [[1],[2],[3]];
if (true) {
  // this visit is a nested statement in an if block:
  visit (x) {
    case int i => i + 1
  }
}
```

.Benefits

.Pitfalls

