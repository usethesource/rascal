---
title: Choice
---

#### Synopsis

Multiple-choice question.

#### Syntax

```
QChoice _OptName_: _MarkedText_ 
_GoodOrBad_~1~: _Choice_~1~
_GoodOrBad_~2~: _Choice_~2~
...
```

#### Types

#### Function

#### Description

Asks a multiple-choice questions described by _MarkedText_.
_OptName_ is an optional name of the question (enclosed between `[` and `]`).
If _OptName_ is missing, the question gets a unique number as name.

Each possible _Choice_ is preceded by a good (`g`) or bad (`b`) marker.
When generating a question 3 choices (including one good answer) are presented in random order.

Providing more good and bad answers will therefore create more variation in the generated questions.

#### Examples

```rascal
QChoice[Faster]: Which means of transportation is faster:
b: Apache Helicopter
g: High-speed train
b: Ferrari F430
b: Hovercraft
```
will produce the question `Faster` in the questions section below.

And, by the way, the [High-speed train](http://en.wikipedia.org/wiki/High-speed_rail) wins with over 570 km/hour compared to
[Ferrari](http://en.wikipedia.org/wiki/Ferrari_F430) (315 km/hour), [Apache](http://en.wikipedia.org/wiki/Boeing_AH-64_Apache) (293 km/hour)
and [Hovercraft](http://en.wikipedia.org/wiki/Hovercraft) (137 km/hour).

#### Benefits

#### Pitfalls

