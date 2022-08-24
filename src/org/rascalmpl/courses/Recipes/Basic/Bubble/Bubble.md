# Bubble

.Synopsis
Variout styles to write bubble sort.

.Syntax

.Types

.Function

.Details

.Description
http://en.wikipedia.org/wiki/Bubble_sort[Bubble sort] is a classical (albeit not the most efficient) technique to sort lists of values.
We present here several styles to implement bubble sort. 
Also see link:/Libraries#List-sort[sort] for a more efficient library function for sorting.

.Examples
[source,rascal]
----
include::{LibDir}demo/basic/Bubble.rsc[tags=module]
----
                
`sort1` is a classic, imperative style, implementation of bubble sort: it iterates over consecutive pairs of elements and
when a not-yet-sorted pair is encountered, the elements are exchanged, and `sort1` is applied recursively to the whole list.

`sort2` uses list matching and consists of a switch with two cases:

*  a case matching a list with two consecutive elements that are unsorted. Observe that when the pattern of a case matches,
   the case as a whole can still fail.
*  a default case.


`sort3` also uses list matching but in a more declarative style: as long as there are unsorted elements in the list (possibly with intervening elements), exchange them.

`sort4` is identical to `sort3`, except that the shorter `*`-notation for list variables is used and that the type declaration for the
the non-list variables has been omitted.

`sort5` uses tail recursion to reach a fixed point instead of a while loop. One alternative matches lists with out-of-order elements, while the default alternative returns the list if no out-of-order elements are found.

Let's put them to the test:
[source,rascal-shell]
----
import demo::basic::Bubble;
L = [9,8,7,6,5,4,3,2,1];
sort1(L);
sort2(L);
sort3(L);
sort4(L);
sort5(L);
----


.Benefits

.Pitfalls

