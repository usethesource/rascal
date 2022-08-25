# Relation

.Synopsis
An unordered set of tuples.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description

In mathematics, given sets _D_~1~, _D_~2~, ... _D_~n~, a
_n_-ary relation _R_ is characterized by _R_ &subseteq;  _D_~1~ &times; _D_~2~ &times; ... &times; _D_~n~.
In other words, _R_ consists of a set of tuples < _V~1~_, ..., _V~n~_ > where each _V_~i~ is an element of
the set _D_~i~. When _n_ = 2, we call the relation a http://en.wikipedia.org/wiki/Relation_(mathematics)[binary relation].

In http://en.wikipedia.org/wiki/Relational_algebra[database theory], a relation is a table with a heading and an unordered set of tuples:

|====
| _D~1~ Name~1~_ | _D~2~ Name~2~_ | ... | _D~n~ Name~n~_

| _V~11~_        | _V~12~_        | ... | _V~1n~_       
| _V~21~_        | _V~22~_        | ... | _V~2n~_        
| _V~31~_        | _V~32~_        | ... | _V~3n~_        
| ...            | ...            | ... |                
|====



In Rascal, a relation is a set of tuples and is characterized by the type:
`rel[_D_~1~ _Name_~1~, _D_~2~ _Name_~2~, ..., _D_~n~ _Name_~n~]` 
See [Relation Values]((Rascal:Values-Relation)) and  for a description of relations and their operators
(since relations are sets all set operators also apply to them, see [Set Values]((Rascal:Values-Set)))
and [functions on relations]((Libraries:Libraries-Relation))
(and here again, since relations are sets all set operators also apply to them, 
see [functions on sets]((Libraries:Libraries-Set))).


.Examples
## Relations in Daily Life

*  The _parent-of_ or _friend-of_ relation between people.
   ![]((char-relation.jpg))
   http://www.translatedmemories.com/bookpgs/Pg10-11CharRelation.jpg[credit]
*  A character relation map, describing the relations between the characters in a play or soap series.
*  A listing of the top 2000 songs of all times including the position, artist name, song title, the year the song was published.
   ![]((top2000-2010.jpg))
   http://top2011.radio2.nl/lijst/2010[credit]


## Relations in computer science

*  A relational data base.
*  Login information including user name, password, home directory, etc.


## Relations in Rascal

*  A parent child relation:
[source,rascal]
----
rel[str parent, str child] = {
<"Paul", "Eva">,
<"Paul", "Thomas">,
<"Jurgen", "Simon">,
<"Jurgen", "David">,
<"Tijs", "Mats">
};
----
*  A fragment of the top 2000 relation:
[source,rascal]
----
rel[int position, str artist, str title, int year] Top2000 = {
<1, "Eagles", "Hotel California",1977>,
<2, "Queen", "Bohemian rhapsody", 1975>,
<3, "Boudewijn de Groot", "Avond", 1997>,
...
};
----

.Benefits

.Pitfalls

