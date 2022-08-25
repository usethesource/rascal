# List

.Synopsis
An ordered sequence of values.

.Syntax

.Types

.Function
       
.Usage

.Details

.Description
A list is a sequence of values with the following properties:

*  The list maybe empty.
*  The values in the list are _ordered_.
*  The same value may occur more than once.
*  The list has a size that is equal to the number of values in the list.
*  Each element in a list _L_ has an index. The first element has index 0. The last element has index `size(_L_)-1`.


Formally, a list can be defined as follows. Given the domains `ELEM` (elements) and `LIST` (lists) and the functions:
[source,rascal]
----
nil :             -> LIST
cons: ELEM x LIST -> LIST
head: LIST        -> ELEM
tail: LIST        -> LIST
----
`nil` and `cons` are so-called _constructor functions_ that define the values in `LIST`. They can be paraphrased as:

*  The _empty list_ `nil` is an element of `LIST`.
*  If `e` is an element of `ELEM` and `l` is an element of LIST, then `cons(e, l)` is also an element in `LIST`.


`head` (take the first element) and `tail` (take the remainder of a list)
are defined functions characterized by the axioms:
[source,rascal]
----
head(cons(e, l)) = e
tail(cons(e, l)) = l
----
The cases `head(nil)` and `tail(nil)` are left undefined (and usually correspond to a runtime error in a programming language).

In Rascal, lists are surrounded by brackets `[` and `]` and the elements are separated by commas.
Each list has a type of the form `list[_T_]`, where _T_ is the smallest common type of all list elements.
Read the description of [lists and their operators]((Rascal:Values-List))
and of [library functions on lists]((Libraries:Libraries-List)).

.Examples
## Lists in Daily Life

*  A line of people waiting for the super market checkout or bus stop. 
   ![]((queue.png))
   http://www.realbollywood.com[credit]
*  The wagons of a train.
*  The Top 100 Music Charts.
   ![]((hot100.png))
   http://www.billboard.com/charts/hot-100#/charts/hot-100[credit]
*  Twitter users ordered according to number of followers.
*  A _to do_ list.


## Lists in computer science

*  The locations in a computer memory.
*  The list of processes that use most cpu time.
*  The list of procedures that are called by a given procedure.


## Lists in Rascal

*  The empty list: `[]`. Its type is `list[void]`.
*  A list of integers: `[3, 1, 4]`. Its type is `list[int]`.
*  A list of mixed-type values: `[3, "a", 4]`. Its type is `list[value]`.

.Benefits

.Pitfalls

