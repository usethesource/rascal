@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}

@synopsis{Library functions for sets.}
@usage{

}
@description{
The following library functions are defined for sets:
(((TOC)))
}
module Set

import Exception;
import List;
import util::Math;


@synopsis{Classify elements in a set.}
@examples{
We classify animals by their number of legs.
```rascal-shell
import Set;
```
Create a map from animals to number of legs.
```rascal-shell,continue
legs = ("bird": 2, "dog": 4, "human": 2, "snake": 0, "spider": 8, "millepede": 1000, "crab": 8, "cat": 4);
```
Define function `nLegs` that returns the number of legs for each animal (or `0` when the animal is unknown):
```rascal-shell,continue
int nLegs(str animal){
    return legs[animal] ? 0;
}
```
Now classify a set of animals:
```rascal-shell,continue
classify({"bird", "dog", "human", "spider", "millepede", "zebra", "crab", "cat"}, nLegs);
```
}
public map[&K,set[&V]] classify(set[&V] input, &K (&V) getClass) = toMap({<getClass(e),e> | e <- input});




@synopsis{Group elements in a set given an equivalence function.}
@examples{
We classify animals by their number of legs.
```rascal-shell
import Set;
```
Create a map from animals to number of legs.
```rascal-shell,continue
legs = ("bird": 2, "dog": 4, "human": 2, "snake": 0, "spider": 8, "millepede": 1000, "crab": 8, "cat": 4);
```
Define function `nLegs` that returns the number of legs fro each animal (or `0` when the animal is unknown):
```rascal-shell,continue
int nLegs(str animal){
    return legs[animal] ? 0;
}
bool similar(str a, str b) = nLegs(a) == nLegs(b);
```
Now group a set of animals:
```rascal-shell,continue
group({"bird", "dog", "human", "spider", "millepede", "zebra", "crab", "cat"}, similar);
```
WARNING: check compiler.
}
public set[set[&T]] group(set[&T] input, bool (&T a, &T b) similar) {
  sinput = sort(input, bool (&T a, &T b) { return similar(a,b) ? false : a < b ; } );
  lres = while (!isEmpty(sinput)) {
    h = head(sinput);
    sim = h + takeWhile(tail(sinput), bool (&T a) { return similar(a,h); });
	append toSet(sim);
	sinput = drop(size(sim), sinput);
  }
  return toSet(lres); 
}


@synopsis{Map set elements to a fixed index.}
@examples{
```rascal-shell
import Set;
index({"elephant", "zebra", "snake"});
```
}
public map[&T,int] index(set[&T] s) {
  sl = toList(s);
  return (sl[i] : i | i <- index(sl));
}






@synopsis{Test whether a set is empty.}
@description{
Yields `true` if `s` is empty, and `false` otherwise.
}
@examples{
```rascal-shell
import Set;
isEmpty({1, 2, 3});
isEmpty({});
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool isEmpty(set[&T] st);


@synopsis{Apply a function to all set elements and return set of results.}
@description{
Return a set obtained by applying function `fn` to all elements of set `s`.
}
@examples{
```rascal-shell
import Set;
int incr(int x) { return x + 1; }
mapper({1, 2, 3, 4}, incr);
```
}
public set[&U] mapper(set[&T] st, &U (&T) fn)
{
  return {fn(elm) | &T elm <- st};
}


@synopsis{Determine the largest element of a set.}
@examples{
```rascal-shell
import Set;
max({1, 3, 5, 2, 4});
max({"elephant", "zebra", "snake"});
```
}
public &T max(set[&T] st) {
	<h,t> = takeOneFrom(st);
	return (h | e > it ? e : it | e <- t);
}


@synopsis{Smallest element of a set.}
@examples{
```rascal-shell
import Set;
min({1, 3, 5, 2, 4});
min({"elephant", "zebra", "snake"});
```
}

@synopsis{Determine the smallest element of a set.}
@examples{
```rascal-shell
import Set;
min({1, 3, 5, 4, 2});
```
}
public &T min(set[&T] st) {
	<h,t> = takeOneFrom(st);
	return (h | e < it ? e : it | e <- t);
}


@synopsis{Determine the powerset of a set.}
@description{
Returns a set with all subsets of `s`.
}
@examples{
```rascal-shell
import Set;
power({1,2,3,4});
```
}
public set[set[&T]] power(set[&T] st)
{
  // the power set of a set of size n has 2^n-1 elements 
  // so we enumerate the numbers 0..2^n-1
  // if the nth bit of a number i is 1 then
  // the nth element of the set should be in the
  // ith subset 
  stl = [*st];
  i = 0;
  res = while(i < pow(2,size(st))) {
	j = i;
	elIndex = 0;
	sub = while(j > 0) {;
	  if(j mod 2 == 1) {
		append stl[elIndex];
	  }
	  elIndex += 1;
	  j /= 2;
	}
	append {*sub};
	i+=1;
  }
  return {*res};
}


@synopsis{The powerset (excluding the empty set) of a set value.}
@description{
Returns all subsets (excluding the empty set) of `s`.
}
@examples{
```rascal-shell
import Set;
power1({1,2,3,4});
```
}
public set[set[&T]] power1(set[&T] st) = power(st) - {{}};

@synopsis{Apply a function to successive elements of a set and combine the results (__deprecated__).}
@description{
Apply the function `fn` to successive elements of set `s` starting with `unit`.
}
@examples{
```rascal-shell
import Set;
int add(int x, int y) { return x + y; }
reducer({10, 20, 30, 40}, add, 0); 
```
}
@pitfalls{
:::warning
This function is *deprecated*, use a reducer expression instead, such as `(init | fn(it,e) | e <- st)`.
:::
}
public &T reducer(set[&T] st, &T (&T,&T) fn, &T unit) =
	(unit | fn(it,elm) | elm <- st);

public &T reducer(set[&T] _:{}) { throw EmptySet(); }


@synopsis{Determine the number of elements in a set.}
@examples{
```rascal-shell
import Set;
size({1,2,3,4});
size({"elephant", "zebra", "snake"});
size({});
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java int size(set[&T] st);


public (&T <:num) sum(set[(&T <:num)] _:{}) {
	throw ArithmeticException(
		"For the emtpy set it is not possible to decide the correct precision to return.\n
		'If you want to call sum on empty set, use sum({0.000}+st) or sum({0r} +st) or sum({0}+st) 
		'to make the set non-empty and indicate the required precision for the sum of the empty set 
		");
}

@synopsis{Sum the elements of a set.}
@examples{
```rascal-shell
import Set;
sum({3, 1, 4, 5});
sum({3, 1.5, 4, 5});
```
}
public default (&T <:num) sum({(&T <: num) e, *(&T <: num) r})
	= (e | it + i | i <- r);



@synopsis{Pick an arbitrary element from a set.}
@description{
This _randomly_ picks one element from a set, unless the set is empty.

:::warning
Use ((getSingleFrom)) if you want the element from a singleton set. ((getOneFrom)) will silently
continue even if there are more element present, which can be a serious threat to the validity of your
analysis algorithm (arbitrary data is not considered).
:::
}
@examples{
```rascal-shell
import Set;
getOneFrom({"elephant", "zebra", "snake"});
getOneFrom({"elephant", "zebra", "snake"});
getOneFrom({"elephant", "zebra", "snake"});
getOneFrom({"elephant", "zebra", "snake"});
```
}
@benefits{
* Random sampling can be an effective test input selection strategy.
}
@pitfalls{
* The name ((getOneFrom)) does not convey randomness.
* ((getOneFrom)) drops all the other elements.
If you are sure there is only one element and you need it, then use ((getSingleFrom)). It will fail fast if your assumption is wrong.  
* If you need more then one element, then repeatedly calling ((getOneFrom)) will be expensive. Have a look at ((util::Sampling)) for more effective
sampling utilities.
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T getOneFrom(set[&T] st);


@synopsis{Get "first" element from a set.}
@description{
Get "first" element of a set. Of course, sets are unordered and do not have a first element.
However, we could assume that sets are internally ordered in some way and this ordering is reproducible (it's deterministic up to hashing collisions).
Applying `getFirstFrom` on the same set will always returns the same element.

:::warning
Use ((getSingleFrom)) if you want the element from a singleton set. ((getFirstFrom)) will silently
continue even if there are more element present, which can be a serious threat to the validity of your
analysis algorithm (arbitrary data is not considered).
:::
}
@benefits{
This function helps to make set-based code more deterministic, for instance, for testing purposes.
}
@pitfalls{
* The deterministic order is _undefined_. This means it may be stable between runs, but not between releases of Rascal.
* There are much better ways to iterate over the elements of a set:
   * Use the `<-` enumerator operator in a `for` loop or a comprehension.
   * Use list matching
* ((getFirstFrom)) drops all the other elements
   * If you are sure there is only one element and you need it, then use ((getSingleFrom)). It will fail fast if your assumption is wrong.
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T getFirstFrom(set[&T] st);

@synopsis{Get the only element from a singleton set.}
@description{
Get the only element of a singleton set. This fails with a ((CallFailed)) exception when the set is not a singleton.
}
@benefits{
* ((getSingleFrom)) fails _fast_ if the assumption (parameter `st` is a singleton) is not met. 
* If a binary relation `r` is injective (i.e. it models a function where each key only has one value) then all projections `r[key]` should produce singleton values: `{v}`. 
Using ((getSingleFrom)) to get the element out makes sure we fail fast in case our assumptions were wrong, or they have changed.
* Never use ((getFirstFrom)) or ((takeOneFrom)) if you can use ((getSingleFrom)).
}
@pitfalls{
* ((CallFailed)) exceptions are sometimes hard to diagnose. Look at the stack trace to see that it was ((getSingleFrom))
that caused it, and then look at  the parameter of ((CallFailed)) to see that the set was not a singleton.
}
public &T getSingleFrom(set[&T] st) = getFirstFrom(st) when size(st) == 1;

// TODO temporary? replacement due to unexplained behaviour of compiler
//public &T getFirstFrom({&T f, *&T _}) = f;
//public &T getFirstFrom(set[&T] _:{}) { throw EmptySet(); }


@synopsis{Remove an arbitrary element from a set, returns the element and a set without that element.}
@description{
Remove an arbitrary element from set `s` and return a tuple consisting of the element and a set without that element.
 Also see ((Set-getOneFrom)).
}
@examples{
```rascal-shell
import Set;
takeOneFrom({1, 2, 3, 4});
takeOneFrom({1, 2, 3, 4});
takeOneFrom({1, 2, 3, 4});
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java tuple[&T, set[&T]] takeOneFrom(set[&T] st);

 
@synopsis{Remove "first" element from a set, returns the element and a set without that element.}
@description{
element of a set.
}
public tuple[&T, set[&T]] takeFirstFrom({&T f, *&T r}) = <f, r>;  
public tuple[&T, set[&T]] takeFirstFrom(set[&T] _:{}) { throw EmptySet(); }  
 

@synopsis{Convert a set to a list.}
@examples{
```rascal-shell
import Set;
toList({1, 2, 3, 4});
toList({"elephant", "zebra", "snake"});
```
Note that the same result can be obtained using splicing:
```rascal-shell,continue
s = {1,2,3,4};
l = [*s];
```
}
@pitfalls{
Recall that the elements of a set are unordered and that there is no guarantee in which order the set elements will be placed in the resulting list.
}
public list[&T] toList(set[&T] st) = [*st];


@synopsis{Convert a set of tuples to a map; each key is associated with a set of values.}
@description{
Convert a set of tuples to a map in which the first element of each tuple 
is associated with the set of second elements of all tuples with the same first element.
}
@examples{
```rascal-shell
import Set;
toMap({<"a", 1>, <"b", 2>, <"a", 10>});
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java map[&A,set[&B]] toMap(rel[&A, &B] st);


@synopsis{Convert a set of tuples to a map (provided that there are no multiple keys).}
@description{
Convert a set of tuples to a map. The result should be a legal map (i.e., without multiple keys).
}
@examples{
```rascal-shell
import Set;
toMapUnique({<"a", 1>, <"b", 2>, <"c", 10>});
```
Now explore an erroneous example:
```rascal-shell,continue,error
toMapUnique({<"a", 1>, <"b", 2>, <"a", 10>});
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java map[&A,&B] toMapUnique(rel[&A, &B] st) throws MultipleKey;


@synopsis{Convert a set to a string.}
@examples{
```rascal-shell
import Set;
toString({1, 2, 3});
toString({"elephant", "zebra", "snake"});
```
}
@pitfalls{
Recall that the elements of a set are unordered and that there is no guarantee in which order the set elements will be placed in the resulting string.
}
@javaClass{org.rascalmpl.library.Prelude}
public java str toString(set[&T] st);


@synopsis{Convert a set to an indented string.}
@examples{
```rascal-shell
import Set;
toString({1, 2, 3});
toString({"elephant", "zebra", "snake"});
```
}
@pitfalls{
Recall that the elements of a set are unordered and that there is no guarantee in which order the set elements will be placed in the resulting string.
}
@javaClass{org.rascalmpl.library.Prelude}
public java str itoString(set[&T] st);



@synopsis{Sort the elements of a set.

Sort the elements of a set:

*  Use the built-in ordering on values to compare list elements.
*  Give an additional `lessThan` function that will be used to compare elements. 

This function `lessThan` (<) function should implement a strict partial order, meaning:

*  that it is not reflexive, i.e. never `a < a`
*  is anti-symmetric, i.e. never `a < b && b < a`.
*  is transitive, i.e. if `a < b` and `b < c` then `a < c`.}
@examples{
```rascal-shell
import Set;
import String;
sort({10, 4, -2, 11, 100, 5});
fruits = {"mango", "strawberry", "pear", "pineapple", "banana", "grape", "kiwi"};
sort(fruits);
sort(fruits, bool(str a, str b){ return size(a) > size(b); });
```
}
public list[&T] sort(set[&T] s) =
	sort(s, bool (&T a,&T b) { return a < b; } );
	
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] sort(set[&T] l, bool (&T a, &T b) less) ;


@synopsis{Produce the smallest `k` elements of a set as sorted by the `less` function}
@description{
This function is fast if `k` is relatively small, say 10 out of a 1000 elements.
It operates in O(n*k) time where n is the size of the set.
 
If `k` is a larger value, say `k > 10`, then it's perhaps better to just sort the entire set 
using the asympotically faster (n*log^2(n)) sort function and take the first `k` elements of the resulting list.

If `k` is a negative number, `top` will return the largest `abs(k)` elements of the set instead of the smallest.
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] top(int k, set[&T] l, bool (&T a, &T b) less) ;

public list[&T] top(int k, set[&T] l) = top(k, l, bool (&T a, &T b) { return a < b; });


@synopsis{Flatten a set of sets into a single set.}
public set[&T] union(set[set[&T]] sets) = {*s | s <- sets};


@synopsis{Compute the Jaccard similarity between two sets.}
real jaccard(set[value] x, set[value] y) = (1. * size(x & y)) / size(x + y);


@synopsis{Calculate the intersection of a set of sets.}
public set[&T] intersection(set[set[&T]] sets)  = (getFirstFrom(sets) | it & elem | elem <- sets);


@synopsis{Checks if all sets in the set are pairwise disjoined.}
@examples{
```rascal-shell
import Set;
isDisjoined({{1,2}, {3,4}, {5,6}});
isDisjoined({{1,2}, {1,4}, {5,6}});
isDisjoined({{1,2}, {1,4}, {1,6}});
```
}
public bool isDisjoined(set[set[&T]] sets) {
  list[set[&T]] setsAsList = toList(sets);

  for (elem1 <- [0..size(setsAsList)-1]) {
    for (elem2 <- [elem1+1..size(setsAsList)]) {
      if (setsAsList[elem1] & setsAsList[elem2] != {}) return false;
    }
  }

  return true;
}