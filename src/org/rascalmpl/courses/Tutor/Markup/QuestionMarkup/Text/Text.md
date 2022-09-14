---
title: Text
---

.Synopsis
A text question with a free-format answer.

.Syntax
```
QText _OptName_: _Text_
a: _Answer_~1~
a: _Answer_~2~
...
```

.Types

.Function

.Description
Presents a text questions consisting of _Text_.
_OptName_ is an optional name of the question (enclosed between `[` and `]`).
If _OptName_ is missing, the question gets a unique number as name.

The user can give a free format answer, and that is accepted if it contains one of the given answers _answer_~1~, _Answer_~2~ as substring.

.Examples
The markup
```rascal
QText[Taller]: Which is taller, the Eiffel Tower or the Empire State Building?
a: Empire
```
Will generate question `Tall` in the questions section below.
The answer is obvious: the [Empire State Building](http://en.wikipedia.org/wiki/Empire_State_Building) is 443.2 meters tall while the [Eifel Tower](http://en.wikipedia.org/wiki/Eiffel_Tower) is 324 meters tall.
Any answer that contains `Empire` will be accepted.

.Benefits

.Pitfalls

