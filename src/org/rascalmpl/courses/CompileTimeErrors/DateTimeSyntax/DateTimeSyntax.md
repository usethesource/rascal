---
title: DateTimeSyntax
---

#### Synopsis

A datetime value is syntactically incorrect.

#### Syntax

#### Types

#### Function
       
#### Usage

#### Description

Datetime values have a rather complex format as described in [datetime]((Rascal:Values-DateTime)).
Some errors in the format are treated as syntax errors, others generate the `DateTimeSyntax` error.

Remedy: Fix the datetime value notation.

#### Examples

A correct datetime value:
```rascal-shell
$2013-07-15T09:15:23.123+03:00$;
```
Certain errors, like a wrong day number (here: 40) lead to a parse error:
```rascal-shell,error
$2013-07-40T09:15:23.123+03:00$;
```

Others, like a wrong month number (here: 15) lead to a DateTimeSyntax error
```rascal-shell,continue,error
$2010-15-15T09:15:23.123+03:00$;
```

#### Benefits

#### Pitfalls

