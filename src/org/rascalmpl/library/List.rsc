@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Vadim Zaytsev - vadim@grammarware.net - UvA}

@synopsis{Library functions for lists.}
@description{
The following library functions are available for lists:
(((TOC)))
}
module List

import Exception;
import Map;
import IO;


@synopsis{Concatenate a list of lists.}
@examples{
```rascal-shell
import List;
concat([]);
concat([[]]);
concat([[1]]);
concat([[1],[],[2,3]]);
concat([[1,2],[3],[4,5],[]]);
```
}
list[&T] concat(list[list[&T]] xxs) 
  = [*xs | list[&T] xs <- xxs];


@synopsis{Delete an element from a list.}
@description{
Delete the `n`-th element from a list. A new list without the `n`-th element is returned as result.
The `IndexOutOfBounds` exception is thrown when n is not a valid index.
}
@examples{
```rascal-shell
import List;
delete([1, 2, 3], 1);
delete(["zebra", "elephant", "snake", "owl"], 2);
```
}
@javaClass{org.rascalmpl.library.Prelude}
java list[&T] delete(list[&T] lst, int n);


@synopsis{Get the distribution of the elements of the list. That
is how often does each element occur in the list?}
@examples{
```rascal-shell
import List;
distribution([4,4,4,3,1,2,1,1,3,4]);
```
}
map[&T element, int occurs] distribution(list[&T] lst) {
     map[&T element, int occurs] res = ();
     for (e <- lst) {
        res[e] ? 0 += 1;
     }
     return res; 
}



@synopsis{Drop elements from the head of a list.}
@description{
Drop `n` elements (or `size(lst)` elements if `size(lst) < n`) from the head of `lst`.
See ((List-take)) to get elements from the head of a list].
}
@examples{
```rascal-shell
import List;
drop(2, [5, 1, 7, 3]);
drop(10, [5, 1, 7, 3]);
drop(2, ["zebra", "elephant", "snake", "owl"]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
java list[&T] drop(int n, list[&T] lst) ;

@synopsis{Remove multiple occurrences of elements in a list. The first occurrence remains.}
@examples{
```rascal-shell
import List;
dup([3, 1, 5, 3, 1, 7, 1, 2]);
```
}
list[&T] dup(list[&T] lst) 
  = ([] | (ix in it) ? it : it + [ix] | &T ix <- lst);

@deprecated{
use the indexing instead
}
@javaClass{org.rascalmpl.library.Prelude}
java &T elementAt(list[&T] lst, int index); 


@synopsis{Pick a random element from a list.}
@description{
Get an arbitrary element from a list. See ((List-takeOneFrom)) for a function that also removes the selected element.
}
@examples{
```rascal-shell
import List;
getOneFrom(["zebra", "elephant", "snake", "owl"]);
getOneFrom(["zebra", "elephant", "snake", "owl"]);
getOneFrom(["zebra", "elephant", "snake", "owl"]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
java &T getOneFrom(list[&T] lst);


@synopsis{Pick first element from a list.}
@description{
Get the first element from a list. As opposed to ((List-getOneFrom)) this function always returns the same (first) list element.
}
&T getFirstFrom([&T f, *&T _]) = f;
&T getFirstFrom(list[&T] _ :[]) { throw EmptyList(); }


@synopsis{Get the first element(s) from a list.}
@description{
* Returns the first element of a list or throws `EmptyList` when the list is empty. 
  This is identical to ((List-top)).
* Returns the first `n` elements of a list or throws `IndexOutOfBounds` when the list is too short. 
  This is similar to ((take)).
}
@examples{
```rascal-shell
import List;
```
Get the first element:
```rascal-shell,continue
head([1, 2, 3]);
head(["zebra", "elephant", "snake", "owl"]);
```
An exception is thrown when taking the head of an empty list:
```rascal-shell,continue,error
head([]);
```
Get the first n elements:
```rascal-shell,continue
head([1, 2, 3, 4], 2);
head(["zebra", "elephant", "snake", "owl"], 2);
```
An exception is thrown when the second argument exceeds the length of the list:
```rascal-shell,continue,error
head([1, 2, 3, 5], 5);
```
}
&T head([&T h, *&T _]) = h; 
&T head(list[&T] _:[]) { throw EmptyList(); }

// Get the first n elements of a list
@javaClass{org.rascalmpl.library.Prelude}
java list[&T] head(list[&T] lst, int n) throws IndexOutOfBounds;



@synopsis{Split a list in a head and a tail.}
@description{
This function is identical to ((List-pop)).
}
@examples{
```rascal-shell
import List;
headTail([3, 1, 4, 5]);
pop([3, 1, 4, 5]);
headTail(["zebra", "elephant", "snake", "owl"]);
```
}
tuple[&T, list[&T]] headTail([&T h, *&T t]) = <h, t>; 
tuple[&T, list[&T]] headTail(list[&T] _:[]) { throw EmptyList(); }


@synopsis{A list of legal index values of a list.}
@description{
Returns a list of all legal index values for a given list `lst`.
}
@examples{
```rascal-shell
import List;
index([1, 3, 5]);
index(["zebra", "elephant", "snake", "owl"]);
```
}
@benefits{
This function is useful in for loops over lists.
}
list[int] index(list[&T] lst) = upTill(size(lst));



@synopsis{Index of first occurrence of an element in a list.}
@description{
Return index of first occurrence of `elt` in `lst`, or `-1` if `elt` is not found.
Also see ((List-lastIndexOf)).
}
@examples{
```rascal-shell
import List;
indexOf([3, 1, 4, 5], 4);
indexOf([3, 1, 4, 5], 7);
indexOf(["zebra", "elephant", "snake", "owl"], "snake");
indexOf(["zebra", "elephant", "snake", "owl"], "eagle");
```
}
int indexOf(list[&T] lst, &T elt) {
	for(int i <- [0..size(lst)]) {
		if(lst[i] == elt) return i;
	}
	return -1;
}


@synopsis{Insert an element at a specific position in a list.}
@description{
Returns a new list with the value of `elm` inserted at index position `n` of the old list.
}
@examples{
```rascal-shell
import List;
insertAt([1,2,3], 1, 5);
insertAt(["zebra", "elephant", "snake", "owl"], 2, "eagle");
```
An exception is thrown when the index position is outside the list:
```rascal-shell,continue,error
insertAt([1,2,3], 10, 5);
```
}
@javaClass{org.rascalmpl.library.Prelude}
java list[&T] insertAt(list[&T] lst, int n, &T elm) throws IndexOutOfBounds;


@synopsis{Join a list of values into a string separated by a separator.}
@examples{
```rascal-shell
import List;
intercalate("/", [3]);
intercalate("/", [3, 1, 4, 5]);
intercalate(", ", [3, 1, 4, 5]);
intercalate(", ", ["zebra", "elephant", "snake", "owl"]);
```
}
str intercalate(str sep, list[value] l) = 
	(isEmpty(l)) ? "" : ( "<head(l)>" | it + "<sep><x>" | x <- tail(l) );


@synopsis{Intersperses a list of values with a separator.}
@examples{
```rascal-shell
import List;
intersperse(", ", ["a","b","c"]);
intersperse(0, [1, 2, 3]);
intersperse(1, []);
intersperse([], [1]);
```
}
list[&T] intersperse(&T sep, list[&T] xs) = 
  (isEmpty(xs))? [] : ([head(xs)] | it + [sep,x] | x <- tail(xs));


@synopsis{Test whether a list is empty.}
@description{
Returns `true` when a list is empty and `false` otherwise.
}
@examples{
```rascal-shell
import List;
isEmpty([]);
isEmpty([1, 2, 3]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
java bool isEmpty(list[&T] lst);


@synopsis{Return the last element of a list, if any.}
@description{
Also see ((List-tail)) that returns a list of one or more of the last elements of a list.
}
@examples{
```rascal-shell
import List;
last([1]);
last([3, 1, 4, 5]);
last(["zebra", "elephant", "snake", "owl"]);
tail([3, 1, 4, 5]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
java &T last(list[&T] lst) throws EmptyList;

@synopsis{Return index of last occurrence of elt in lst, or -1 if elt is not found.}
@description{
Also see ((List-indexOf)).
}
@examples{
```rascal-shell
import List;
lastIndexOf([3, 1, 4, 5, 4], 4);
lastIndexOf([3, 1, 4, 5, 4], 7);
lastIndexOf(["zebra", "owl", "elephant", "snake", "owl"], "owl");
```
}
int lastIndexOf(list[&T] lst, &T elt) {
	for(i <- reverse(index(lst))) {
		if(lst[i] == elt) return i;
	}
	return -1;
}


@synopsis{Apply a function to all list elements and return list of results.}
@description{
Apply a function `fn` to each element of `lst` and return the list of results.
}
@examples{
```rascal-shell
import List;
int incr(int x) { return x + 1; }
mapper([1, 2, 3, 4], incr);
```
}
list[&U] mapper(list[&T] lst, &U (&T) fn) =  [fn(elm) | &T elm <- lst];


@synopsis{Determine the largest element in a list.}
@examples{
```rascal-shell
import List;
max([1, 3, 5, 2, 4]);
max(["zebra", "elephant", "snake", "owl"]);
```
}
&T max([&T h, *&T t]) = (h | e > it ? e : it | e <- t);
&T max(list[&T] _:[]) { throw EmptyList(); }
	

@synopsis{Merge the elements of two sorted lists into one list.}
@description{
Merge the elements of two sorted lists into one list using the built-in ordering between values.
Optional, a comparison function `lessOrEqual` may be given for a user-defined ordering between values.
}
@examples{
```rascal-shell
import List;
merge([1, 3, 5], [2, 7, 9, 15]);
merge(["ape", "elephant", "owl", "snale", "zebra"], ["apple", "berry", "orange", "pineapple"]);
```
Merge two lists of strings and use their length as ordering:
```rascal-shell,continue
import String;
merge(["ape", "owl", "snale", "zebra", "elephant"], ["apple", "berry", "orange", "pineapple"], bool(str x, str y){ return size(x) <= size(y); });
```
}
list[&T] merge(list[&T] left, list[&T] right){
  res = while(!isEmpty(left) && !isEmpty(right)) {
    if(head(left) <= head(right)) {
      append head(left);
      left = tail(left);
    } else {
      append head(right);
      right = tail(right);
    }
  }
  return res + left + right;
}

list[&T] merge(list[&T] left, list[&T] right, bool (&T a, &T b) lessOrEqual){
  res = while(!isEmpty(left) && !isEmpty(right)) {
    if(lessOrEqual(head(left),head(right))) {
      append head(left);
      left = tail(left);
    } else {
      append head(right);
      right = tail(right);
    }
  }
  return res + left + right;
}


@synopsis{Determine the smallest element in a list.}
@examples{
```rascal-shell
import List;
min([1, 3, 5, 2, 4]);
min(["zebra", "elephant", "snake", "owl"]);
```
}
&T min([&T h, *&T t]) = (h | e < it ? e : it | e <- t);
&T min(list[&T] _: []) { throw EmptyList(); }


@synopsis{Mix the elements of two lists.}
@description{
Let n be the minimum of the length of the two lists `l` and `r`.
`mix` returns a list in which the first `n` elements are taken alternately from the left and the right list,
followed by the remaining elements of the longest list.
}
@examples{
```rascal-shell
import List;
mix([3, 1, 7, 5, 9], [15, 25, 35]);
mix([3, 1, 7], [15, 25, 35, 45, 55]);
mix([3, 1, 7], ["elephant", "snake"]);
```
}
list[&T] mix(list[&T] l, list[&T] r){
	sizeL = size(l);
	sizeR = size(r);
	minSize = sizeL < sizeR ? sizeL : sizeR;
	return [elementAt(l,i),elementAt(r,i)| i <- [0 .. minSize]] + drop(sizeR,l) + drop(sizeL,r);
}

@synopsis{Compute all permutations of a list.}
@examples{
```rascal-shell
import List;
permutations([1,2,3]);
```
}
set[list[&T]] permutations(list[&T] lst) =
	permutationsBag(distribution(lst));

private set[list[&T]] permutationsBag(map[&T element, int occurs] b) =
	isEmpty(b) ? {[]} : 
	{ [e] + rest | e <- b, rest <- permutationsBag(removeFromBag(b,e))};


@synopsis{Pop top element from list, return a tuple.}
@description{
This function is identical to ((headTail)).
Also see ((List-push)) and ((List-top)).
}
@examples{
```rascal-shell
import List;
pop([3, 1, 4, 5]);
headTail([3, 1, 4, 5]);
pop(["zebra", "elephant", "snake", "owl"]);
```
}
tuple[&T, list[&T]] pop(list[&T] lst) = headTail(lst);


@synopsis{Return all but the last element of a list.}
@examples{
```rascal-shell
import List;
prefix([3, 1, 4, 5]);
prefix([]);
prefix(["zebra", "elephant", "snake", "owl"]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
java list[&T] prefix(list[&T] lst) ;


@synopsis{Push an element in front of a list.}
@description{
Also see ((List-pop)) and ((List-top)).
}
@examples{
```rascal-shell
import List;
push(7, [3, 1, 4, 5]);
push("eagle", ["zebra", "elephant", "snake", "owl"]);
```
}
list[&T] push(&T elem, list[&T] lst) = [elem] + lst;


@synopsis{Apply a function to successive elements of list and combine the results (__deprecated__).}
@description{
Apply the function `fn` to successive elements of list `lst` starting with `unit`.
}
@examples{
```rascal-shell
import List;
int add(int x, int y) { return x + y; }
reducer([10, 20, 30, 40], add, 0); 
```
}
@benefits{

}
@pitfalls{
:::warning
This function is *deprecated*, use a reducer expression instead. E.g. `(init | f(it, e) | e <- lst)`.
:::
}
&T reducer(list[&T] lst, &T (&T, &T) fn, &T unit)
{
  &T result = unit;
  for(&T elm <- lst){
     result = fn(result, elm);
  }
  return result;
}

list[&T] remove(list[&T] lst, int indexToDelete) =
	[ lst[i] | i <- index(lst), i != indexToDelete ];

private map[&T element, int occurs] removeFromBag(map[&T element, int occurs] b, &T el) =
	removeFromBag(b,el,1);

private map[&T element, int occurs] removeFromBag(map[&T element, int occurs] b, &T el, int nr) =
	!(b[el] ?) ? b : (b[el] <= nr ? b - (el : b[el]) : b + (el : b[el] - nr)); 


@synopsis{Reverse a list.}
@description{
Returns a list with the elements of `lst` in reverse order.
}
@examples{
```rascal-shell
import List;
reverse([1,4,2,3]);
reverse(["zebra", "elephant", "snake", "owl"]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
java list[&T] reverse(list[&T] lst);


@synopsis{Determine the number of elements in a list.}
@examples{
```rascal-shell
import List;
size([20, 10, 30]);
size(["zebra", "elephant", "snake", "owl"]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
java int size(list[&T] lst);


@synopsis{Compute a sublist of a list.}
@description{
Returns a sublist of `lst` from index `start` of length `len`.

:::warning
In most cases it is better to use the built-in slice notation,
see the example below.
:::
}
@examples{
```rascal-shell
import List;
slice([10, 20, 30, 40, 50, 60], 2, 3);
slice(["zebra", "elephant", "snake", "owl"], 1, 2);
```
Here are the equivalent expressions using the slice notation:
```rascal-shell
[10, 20, 30, 40, 50, 60][2 .. 5];
["zebra", "elephant", "snake", "owl"][1 .. 3];
```
WARNING: In the slice notation the upper bound is exclusive.
}
@javaClass{org.rascalmpl.library.Prelude}
java list[&T] slice(list[&T] lst, int begin, int len);


@synopsis{Sort the elements of a list.}
@description{
Sort the elements of a list:

*  Use the built-in ordering on values to compare list elements.
*  Give an additional `lessThan` function that will be used to compare elements.
}
@examples{
```rascal-shell
import List;
import String;
sort([10, 4, -2, 11, 100, 5]);
fruits = ["mango", "strawberry", "pear", "pineapple", "banana", "grape", "kiwi"];
sort(fruits);
sort(fruits, bool(str a, str b){ return size(a) > size(b); });
```
}
list[&T] sort(list[&T] lst) =
	sort(lst, bool (&T a, &T b) { return a < b; } );
	
@javaClass{org.rascalmpl.library.Prelude}
java list[&T] sort(list[&T] l, bool (&T a, &T b) less) ;


@synopsis{Check whether a list is sorted or not.}
@description{
Checks whether or not a list is sorted by searching for any out-of-order elements.
The empty list is defined to be "sorted" and what sorted means is defined the
higher-order parameter "less" which should implement a partial-order relation
between the two parameters.
}
bool isSorted(list[&T] l, bool (&T a, &T b) less = bool (&T a, &T b) { return a < b; })
 = !any([*_, &T a, &T b, *_] := l, less(b, a));


@synopsis{Shuffle a list.}
@description{
Returns a random (unbiased) shuffled list.
}
@examples{
```rascal-shell
import List;
shuffle([1,4,2,3]);
shuffle(["zebra", "elephant", "snake", "owl"]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
java list[&T] shuffle(list[&T] l);


@synopsis{Shuffle a list with a seed.}
@description{
Returns a random (unbiased) shuffled list, every call with the same seed shuffles in the same order.
}
@examples{
```rascal-shell
import List;
shuffle([1,2,3,4]);
shuffle([1,2,3,4]);
shuffle([1,2,3,4], 1);
shuffle([1,2,3,4], 1);
```
}
@javaClass{org.rascalmpl.library.Prelude}
java list[&T] shuffle(list[&T] l, int seed);


@synopsis{Split a list into two halves.}
@examples{
```rascal-shell
import List;
split([3, 1, 4, 5, 7]);
split(["zebra", "elephant", "snake", "owl"]);
```
}
tuple[list[&T],list[&T]] split(list[&T] l) {
	half = size(l)/2;
	return <take(half,l), drop(half,l)>;
}


@synopsis{Sum the elements of a list.}
@examples{
```rascal-shell
import List;
sum([3, 1, 4, 5]);
sum([3, 1.5, 4, 5]);
```
}
(&T <:num) sum([(&T <: num) hd, *(&T <: num) tl]) = (hd | it + i | i <- tl);
(&T <:num) sum(list[&T] _: []) { throw EmptyList(); }


@synopsis{Get the tail element(s) from a list.}
@description{
*  Return a list consisting of all but the first element of `lst`.
*  Return a list consisting of the last `n` elements of `lst`.
}
@examples{
All but first element:
```rascal-shell,continue
import List;
tail([10,20,30]);
```
Try an error case:
```rascal-shell,continue,error
tail([]);
```
Last n elements:
```rascal-shell,continue
tail([10, 20, 30, 40, 50, 60], 3);
```
Try an error case:
```rascal-shell,continue,error
tail([10, 20, 30, 40, 50, 60], 10);
```
}
list[&T] tail([&T _, *&T t]) = t;
list[&T] tail(list[&T] _:[]) { throw EmptyList(); }
 
@javaClass{org.rascalmpl.library.Prelude}
java list[&T] tail(list[&T] lst, int len) throws IndexOutOfBounds;


@synopsis{Get number of elements from the head of a list.}
@description{
Get `n` elements (or `size(lst)` elements if `size(lst) < n`) from the head of the list.
See ((List-drop)) to remove elements from the head of a list.
}
@examples{
```rascal-shell
import List;
take(2, [3, 1, 4, 5]);
take(6, [3, 1, 4, 5]);
take(2, ["zebra", "elephant", "snake", "owl"]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
java list[&T] take(int n, list[&T] lst);


@synopsis{Remove an arbitrary element from a list, returns the element and the modified list.}
@description{
Select an arbitrary element from `lst`, and return a tuple consisting of:

*  the selected element, and 
*  a new list consisting of all elements of `lst` except the selected element.


See ((List-getOneFrom)) to only selected an element from a list.
}
@examples{
```rascal-shell
import List;
takeOneFrom([10,20,30,40,50]);
takeOneFrom([10,20,30,40,50]);
takeOneFrom([10,20,30,40,50]);
takeOneFrom(["zebra", "elephant", "snake", "owl"]);
takeOneFrom(["zebra", "elephant", "snake", "owl"]);
takeOneFrom(["zebra", "elephant", "snake", "owl"]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
java tuple[&T, list[&T]] takeOneFrom(list[&T] lst);


@synopsis{Take elements from the front of the list as long as a predicate is true.}
@examples{
```rascal-shell
import List;
bool isEven(int a) = a mod 2 == 0;
takeWhile([2,4,6,8,1,2,3,4,5],isEven);
```
}
list[&T] takeWhile(list[&T] lst, bool (&T a) take) {
	i = 0;
	return while(i < size(lst) && take(lst[i])) {
		append lst[i];
		i+=1;
	}
}


@synopsis{Convert a list of pairs to a map; first elements are associated with a set of second elements.}
@description{
Convert a list of tuples to a map in which the first element of each tuple is 
associated with the set of second elements from all tuples with the same first element. Keys should be unique.
}
@examples{
```rascal-shell
import List;
toMap([<1,10>, <1, 11>, <2, 20>, <3, 30>, <3, 31>]);
```
}
@pitfalls{
`toMap` collects all values in tuples with the same first value in a set.
Contrast this with `toMapUnique` that associates each first tuple value with the second tuple value,
but imposes the constraint that those keys are unique.
}
@javaClass{org.rascalmpl.library.Prelude}
java map[&A,list[&B]] toMap(list[tuple[&A, &B]] lst) throws MultipleKey;


@synopsis{Convert a list of tuples to a map; result must be a map.}
@description{
Convert a list of tuples to a map; result must be a map.
}
@examples{
```rascal-shell
import List;
toMapUnique([<1,10>, <2, 20>, <3, 30>]);
```
Let's explore an error case:
```rascal-shell,continue,error
toMapUnique([<1,10>, <1, 11>, <2, 20>, <3, 30>]);
```
}
@pitfalls{
The keys in a map are unique by definition.
`toMapUnique` throws a `MultipleKey` exception when the list contains more than one tuple with the same first value.
}
@javaClass{org.rascalmpl.library.Prelude}
java map[&A,&B] toMapUnique(list[tuple[&A, &B]] lst) throws MultipleKey;


@synopsis{Take the top element of a list.}
@description{
This function is identical to ((List-head)).
Also see ((List-pop)) and ((List-push)).
}
@examples{
```rascal-shell
import List;
top([3, 1, 4, 5]);
top(["zebra", "elephant", "snake", "owl"]);
```
}
&T top([&T t, *&T _]) = t;


@synopsis{Convert a list to a relation.}
@description{
Convert a list to relation, where each tuple encodes which elements are followed by each other.
  This function will return an empty relation for empty lists and for singleton lists.
}
@examples{
```rascal-shell
import List;
toRel([3, 1, 4, 5]);
toRel(["zebra", "elephant", "snake", "owl"]);
```
}
rel[&T,&T] toRel(list[&T] lst) {
  return { <from,to> | [*_, from, to, *_] := lst };
}


@synopsis{Convert a list to a set.}
@description{
Convert `lst` to a set.
}
@examples{
```rascal-shell
import List;
toSet([10, 20, 30, 40]);
toSet(["zebra", "elephant", "snake", "owl"]);
```
Note that the same can be done using splicing
```rascal-shell,continue
l = [10,20,30,40];
s = {*l};
```
}
@deprecated{
Please use {*myList} instead.
}
@javaClass{org.rascalmpl.library.Prelude}
java set[&T] toSet(list[&T] lst);


@synopsis{Convert a list to a string.}
@description{
Convert `lst` to a string.
}
@examples{
```rascal-shell
import List;
toString([10, 20, 30]);
toString(["zebra", "elephant", "snake", "owl"]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
java str toString(list[&T] lst);



@synopsis{Convert a list to an indented string.}
@description{
Convert `lst` to a indented string.
}
@examples{
```rascal-shell
import List;
itoString([10, 20, 30]);
itoString(["zebra", "elephant", "snake", "owl"]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
java str itoString(list[&T] lst);



@synopsis{Make a pair (triple) of lists from a list of pairs (triples).}
@description{
Also see ((List-unzip3));
}
@examples{
```rascal-shell
import List;
unzip2([<3,"thirty">, <1,"ten">, <4,"forty">]);
unzip3([<3,"thirty",300>, <1,"ten",100>, <4,"forty",400>]);
```
}
tuple[list[&T],list[&U]] unzip2(list[tuple[&T,&U]] lst) =
	<[t | <t,_> <- lst], [u | <_,u> <- lst]>;

// Make a triple of lists from a list of triples.
tuple[list[&T],list[&U],list[&V]] unzip3(list[tuple[&T,&U,&V]] lst) =
	<[t | <t,_,_> <- lst], [u | <_,u,_> <- lst], [w | <_,_,w> <- lst]>;


@synopsis{Returns the list 0,1..n-1.}
@description{
Returns the list `0`, `1`, .., `n-1`, this is slightly faster than `[0..n]`, since the returned values are shared.
}
@examples{
```rascal-shell
import List;
upTill(10);
```
}
@javaClass{org.rascalmpl.library.Prelude}
java list[int] upTill(int n);


@synopsis{Make a list of pairs from two (three) lists of the same length.}
@description{
Also see ((List-unzip3)).
}
@examples{
```rascal-shell
import List;
zip2([3, 1, 4], ["thirty", "ten", "forty"]);
zip3([3, 1, 4], ["thirty", "ten", "forty"], [300, 100, 400]);
```
}
list[tuple[&T first, &U second]] zip2(list[&T] a, list[&U] b) {
	if(size(a) != size(b))
		throw IllegalArgument(<size(a),size(b)>, "List size mismatch");
	return [<elementAt(a,i), elementAt(b,i)> | i <- index(a)];
}

list[tuple[&T first, &U second, &V third]] zip3(list[&T] a, list[&U] b, list[&V] c) {
	if(size(a) != size(b) || size(a) != size(c))
		throw IllegalArgument(<size(a),size(b),size(c)>, "List size mismatch");
	return [<elementAt(a,i), elementAt(b,i), elementAt(c,i)> | i <- index(a)];
}

