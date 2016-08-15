# Choice

.Synopsis
Multiple-choice question.

.Syntax
[source,subs="quotes"]
----
QChoice _OptName_: _MarkedText_ 
_GoodOrBad_~1~: _Choice_~1~
_GoodOrBad_~2~: _Choice_~2~
...
----

.Types

.Function

.Details

.Description
Asks a multiple-choice questions described by _MarkedText_.
_OptName_ is an optional name of the question (enclosed between `[` and `]`).
If _OptName_ is missing, the question gets a unique number as name.

Each possible _Choice_ is preceded by a good (`g`) or bad (`b`) marker.
When generating a question 3 choices (including one good answer) are presented in random order.

Providing more good and bad answers will therefore create more variation in the generated questions.

.Examples
[source,rascal]
----
QChoice[Faster]: Which means of transportation is faster:
b: Apache Helicopter
g: High-speed train
b: Ferrari F430
b: Hovercraft
----
will produce the question `Faster` in the questions section below.

And, by the way, the http://en.wikipedia.org/wiki/High-speed_rail[High-speed train] wins with over 570 km/hour compared to
http://en.wikipedia.org/wiki/Ferrari_F430[Ferrari] (315 km/hour), http://en.wikipedia.org/wiki/Boeing_AH-64_Apache[Apache] (293 km/hour)
and http://en.wikipedia.org/wiki/Hovercraft[Hovercraft] (137 km/hour).

.Benefits

.Pitfalls

