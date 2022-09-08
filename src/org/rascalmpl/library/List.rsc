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
@doc{
.Synopsis
Library functions for lists.

.Description

For operators on lists see [List]((Rascal:Values-List)) in the Rascal Language Reference.

The following functions are available for lists:
(((TOC)))
}
module List

import Exception;
import Map;
import IO;

@doc{
.Synopsis
Concatenate a list of lists.

.Examples
```rascal-shell
import List;
concat([]);
concat([[]]);
concat([[1]]);
concat([[1],[],[2,3]]);
concat([[1,2],[3],[4,5],[]]);
```
}
public list[&T] concat(list[list[&T]] xxs) 
  = [*xs | list[&T] xs <- xxs];

@doc{
.Synopsis
Delete an element from a list.

.Description
Delete the `n`-th element from a list. A new list without the `n`-th element is returned as result.
The `IndexOutOfBounds` exception is thrown when n is not a valid index.

.Examples
```rascal-shell
import List;
delete([1, 2, 3], 1);
delete(["zebra", "elephant", "snake", "owl"], 2);
```}
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] delete(list[&T] lst, int n);

@doc{
.Synopsis
Get the distribution of the elements of the list. That
is how often does each element occur in the list? 

.Examples
```rascal-shell
import List;
distribution([4,4,4,3,1,2,1,1,3,4]);
```
}
public map[&T element, int occurs] distribution(list[&T] lst) {
     map[&T element, int occurs] res = ();
     for (e <- lst) {
        res[e] ? 0 += 1;
     }
     return res; 
}


@doc{
.Synopsis
Drop elements from the head of a list.

.Description
Drop `n` elements (or `size(lst)` elements if `size(lst) < n`) from the head of `lst`.
See ((List-take)) to get elements from the head of a list].

.Examples
```rascal-shell
import List;
drop(2, [5, 1, 7, 3]);
drop(10, [5, 1, 7, 3]);
drop(2, ["zebra", "elephant", "snake", "owl"]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] drop(int n, list[&T] lst) ;

@synopsis{Remove multiple occurrences of elements in a list. The first occurrence remains.}
@examples{
```rascal-shell
import List;
dup([3, 1, 5, 3, 1, 7, 1, 2]);
```
}
public list[&T] dup(list[&T] lst) 
  = ([] | (ix in it) ? it : it + [ix] | &T ix <- lst);

@deprecated{use the indexing instead}
@javaClass{org.rascalmpl.library.Prelude}
public java &T elementAt(list[&T] lst, int index); 

@doc{
.Synopsis
Pick a random element from a list.

.Description
Get an arbitrary element from a list. See ((List-takeOneFrom)) for a function that also removes the selected element.

.Examples
```rascal-shell
import List;
getOneFrom(["zebra", "elephant", "snake", "owl"]);
getOneFrom(["zebra", "elephant", "snake", "owl"]);
getOneFrom(["zebra", "elephant", "snake", "owl"]);
```}
@javaClass{org.rascalmpl.library.Prelude}
public java &T getOneFrom(list[&T] lst);

@doc{
.Synopsis
Pick first element from a list.

.Description
Get the first element from a list. As opposed to ((List-getOneFrom)) this function always returns the same (first) list element.
}
public &T getFirstFrom([&T f, *&T _]) = f;
public &T getFirstFrom(list[&T] _ :[]) { throw EmptyList(); }

@doc{
.Synopsis
Get the first element(s) from a list.

.Description

* Returns the first element of a list or throws `EmptyList` when the list is empty. 
  This is identical to ((List-top)).
* Returns the first `n` elements of a list or throws `IndexOutOfBounds` when the list is too short. 
  This is similar to ((take)).

.Examples
```rascal-shell,error
import List;
```
Get the first element:
```rascal-shell,continue,error
head([1, 2, 3]);
head(["zebra", "elephant", "snake", "owl"]);
```
An exception is thrown when taking the head of an empty list:
```rascal-shell,continue,error
head([]);
```
Get the first n elements:
```rascal-shell,continue,error
head([1, 2, 3, 4], 2);
head(["zebra", "elephant", "snake", "owl"], 2);
```
An exception is thrown when the second argument exceeds the length of the list:
```rascal-shell,continue,error
head([1, 2, 3, 5], 5);
```}
public &T head([&T h, *&T _]) = h; 
public &T head(list[&T] _:[]) { throw EmptyList(); }

// Get the first n elements of a list
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] head(list[&T] lst, int n) throws IndexOutOfBounds;


@doc{
.Synopsis
Split a list in a head and a tail.

.Description
This function is identical to ((List-pop)).

.Examples
```rascal-shell
import List;
headTail([3, 1, 4, 5]);
pop([3, 1, 4, 5]);
headTail(["zebra", "elephant", "snake", "owl"]);
```
}
public tuple[&T, list[&T]] headTail([&T h, *&T t]) = <h, t>; 
public tuple[&T, list[&T]] headTail(list[&T] _:[]) { throw EmptyList(); }

@doc{
.Synopsis
A list of legal index values of a list.

.Description
Returns a list of all legal index values for a given list `lst`.

.Examples
```rascal-shell
import List;
index([1, 3, 5]);
index(["zebra", "elephant", "snake", "owl"]);
```

.Benefits
This function is useful in [for]((Rascal:Statements-For)) loops over lists.
}
public list[int] index(list[&T] lst) = upTill(size(lst));


@doc{
.Synopsis
Index of first occurrence of an element in a list.

.Description
Return index of first occurrence of `elt` in `lst`, or `-1` if `elt` is not found.
Also see ((List-lastIndexOf)).

.Examples
```rascal-shell
import List;
indexOf([3, 1, 4, 5], 4);
indexOf([3, 1, 4, 5], 7);
indexOf(["zebra", "elephant", "snake", "owl"], "snake");
indexOf(["zebra", "elephant", "snake", "owl"], "eagle");
```}
public int indexOf(list[&T] lst, &T elt) {
	for(int i <- [0..size(lst)]) {
		if(lst[i] == elt) return i;
	}
	return -1;
}

@doc{
.Synopsis
Insert an element at a specific position in a list.

.Description
Returns a new list with the value of `elm` inserted at index position `n` of the old list.

.Examples
```rascal-shell,error
import List;
insertAt([1,2,3], 1, 5);
insertAt(["zebra", "elephant", "snake", "owl"], 2, "eagle");
```
An exception is thrown when the index position is outside the list:
```rascal-shell,continue,error
insertAt([1,2,3], 10, 5);
```}
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] insertAt(list[&T] lst, int n, &T elm) throws IndexOutOfBounds;

@doc{
.Synopsis
Join a list of values into a string separated by a separator.

.Examples
```rascal-shell
import List;
intercalate("/", [3]);
intercalate("/", [3, 1, 4, 5]);
intercalate(", ", [3, 1, 4, 5]);
intercalate(", ", ["zebra", "elephant", "snake", "owl"]);
```}
public str intercalate(str sep, list[value] l) = 
	(isEmpty(l)) ? "" : ( "<head(l)>" | it + "<sep><x>" | x <- tail(l) );

@doc{
.Synopsis
Intersperses a list of values with a separator.

.Examples
```rascal-shell
import List;
intersperse(", ", ["a","b","c"]);
intersperse(0, [1, 2, 3]);
intersperse(1, []);
intersperse([], [1]);
```}
public list[&T] intersperse(&T sep, list[&T] xs) = 
  (isEmpty(xs))? [] : ([head(xs)] | it + [sep,x] | x <- tail(xs));

@doc{
.Synopsis
Test whether a list is empty.

.Description
Returns `true` when a list is empty and `false` otherwise.

.Examples
```rascal-shell
import List;
isEmpty([]);
isEmpty([1, 2, 3]);
```}
@javaClass{org.rascalmpl.library.Prelude}
public java bool isEmpty(list[&T] lst);

@doc{
.Synopsis
Return the last element of a list, if any.

.Description
Also see ((List-tail)) that returns a list of one or more of the last elements of a list.

.Examples
```rascal-shell
import List;
last([1]);
last([3, 1, 4, 5]);
last(["zebra", "elephant", "snake", "owl"]);
tail([3, 1, 4, 5]);
```}
public &T last([*&T _, &T l]) = l;
public &T last(list[&T] _:[]) { throw EmptyList(); }

@doc{
.Synopsis
Return index of last occurrence of elt in lst, or -1 if elt is not found.

.Description
Also see ((List-indexOf)).

.Examples
```rascal-shell
import List;
lastIndexOf([3, 1, 4, 5, 4], 4);
lastIndexOf([3, 1, 4, 5, 4], 7);
lastIndexOf(["zebra", "owl", "elephant", "snake", "owl"], "owl");
```}
public int lastIndexOf(list[&T] lst, &T elt) {
	for(i <- reverse(index(lst))) {
		if(lst[i] == elt) return i;
	}
	return -1;
}

@doc{
.Synopsis
Apply a function to all list elements and return list of results.

.Description
Apply a function `fn` to each element of `lst` and return the list of results.

.Examples
```rascal-shell
import List;
int incr(int x) { return x + 1; }
mapper([1, 2, 3, 4], incr);
```}
public list[&U] mapper(list[&T] lst, &U (&T) fn) =  [fn(elm) | &T elm <- lst];

@doc{
.Synopsis
Determine the largest element in a list.

.Examples
```rascal-shell
import List;
max([1, 3, 5, 2, 4]);
max(["zebra", "elephant", "snake", "owl"]);
```}
public &T max([&T h, *&T t]) = (h | e > it ? e : it | e <- t);
public &T max(list[&T] _:[]) { throw EmptyList(); }
	
@doc{
.Synopsis
Merge the elements of two sorted lists into one list.

.Description
Merge the elements of two sorted lists into one list using the built-in ordering between values.
Optional, a comparison function `lessOrEqual` may be given for a user-defined ordering between values.


.Examples

```rascal-shell
import List;
merge([1, 3, 5], [2, 7, 9, 15]);
merge(["ape", "elephant", "owl", "snale", "zebra"], ["apple", "berry", "orange", "pineapple"]);
```
Merge two lists of strings and use their length as ordering:
```rascal-shell,continue
import String;
merge(["ape", "owl", "snale", "zebra", "elephant"], ["apple", "berry", "orange", "pineapple"], bool(str x, str y){ return size(x) <= size(y); });
```}
public list[&T] merge(list[&T] left, list[&T] right){
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

public list[&T] merge(list[&T] left, list[&T] right, bool (&T a, &T b) lessOrEqual){
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

@doc{
.Synopsis
Determine the smallest element in a list.

.Examples
```rascal-shell
import List;
min([1, 3, 5, 2, 4]);
min(["zebra", "elephant", "snake", "owl"]);
```}
public &T min([&T h, *&T t]) = (h | e < it ? e : it | e <- t);
public &T min(list[&T] _: []) { throw EmptyList(); }

@doc{
.Synopsis
Mix the elements of two lists.

.Description
Let n be the minimum of the length of the two lists `l` and `r`.
`mix` returns a list in which the first `n` elements are taken alternately from the left and the right list,
followed by the remaining elements of the longest list.

.Examples
```rascal-shell
import List;
mix([3, 1, 7, 5, 9], [15, 25, 35]);
mix([3, 1, 7], [15, 25, 35, 45, 55]);
mix([3, 1, 7], ["elephant", "snake"]);
```}
public list[&T] mix(list[&T] l, list[&T] r){
	sizeL = size(l);
	sizeR = size(r);
	minSize = sizeL < sizeR ? sizeL : sizeR;
	return [elementAt(l,i),elementAt(r,i)| i <- [0 .. minSize]] + drop(sizeR,l) + drop(sizeL,r);
}
@doc{
.Synopsis
Compute all permutations of a list.

.Examples
```rascal-shell
import List;
permutations([1,2,3]);
```}
set[list[&T]] permutations(list[&T] lst) =
	permutationsBag(distribution(lst));

private set[list[&T]] permutationsBag(map[&T element, int occurs] b) =
	isEmpty(b) ? {[]} : 
	{ [e] + rest | e <- b, rest <- permutationsBag(removeFromBag(b,e))};

@doc{
.Synopsis
Pop top element from list, return a tuple.
.Description
This function is identical to ((headTail)).
Also see ((List-push)) and ((List-top)).

.Examples
```rascal-shell
import List;
pop([3, 1, 4, 5]);
headTail([3, 1, 4, 5]);
pop(["zebra", "elephant", "snake", "owl"]);
```}
public tuple[&T, list[&T]] pop(list[&T] lst) = headTail(lst);

@doc{
.Synopsis
Return all but the last element of a list.

.Examples
```rascal-shell
import List;
prefix([3, 1, 4, 5]);
prefix([]);
prefix(["zebra", "elephant", "snake", "owl"]);
```}
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] prefix(list[&T] lst) ;

@doc{
.Synopsis
Push an element in front of a list.

.Description
Also see ((List-pop)) and ((List-top)).

.Examples
```rascal-shell
import List;
push(7, [3, 1, 4, 5]);
push("eagle", ["zebra", "elephant", "snake", "owl"]);
```}
public list[&T] push(&T elem, list[&T] lst) = [elem] + lst;

@doc{
.Synopsis
Apply a function to successive elements of list and combine the results (__deprecated__).

.Description
Apply the function `fn` to successive elements of list `lst` starting with `unit`.

.Examples
```rascal-shell
import List;
int add(int x, int y) { return x + y; }
reducer([10, 20, 30, 40], add, 0); 
```

.Benefits

.Pitfalls
WARNING:
This function is *deprecated*, use a [reducer]((Rascal:Expressions-Reducer)) instead.
}
public &T reducer(list[&T] lst, &T (&T, &T) fn, &T unit)
{
  &T result = unit;
  for(&T elm <- lst){
     result = fn(result, elm);
  }
  return result;
}

public list[&T] remove(list[&T] lst, int indexToDelete) =
	[ lst[i] | i <- index(lst), i != indexToDelete ];

private map[&T element, int occurs] removeFromBag(map[&T element, int occurs] b, &T el) =
	removeFromBag(b,el,1);

private map[&T element, int occurs] removeFromBag(map[&T element, int occurs] b, &T el, int nr) =
	!(b[el] ?) ? b : (b[el] <= nr ? b - (el : b[el]) : b + (el : b[el] - nr)); 

@doc{
.Synopsis
Reverse a list.

.Description
Returns a list with the elements of `lst` in reverse order.

.Examples
```rascal-shell
import List;
reverse([1,4,2,3]);
reverse(["zebra", "elephant", "snake", "owl"]);
```}
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] reverse(list[&T] lst);

@doc{
.Synopsis
Determine the number of elements in a list.

.Examples
```rascal-shell
import List;
size([20, 10, 30]);
size(["zebra", "elephant", "snake", "owl"]);
```}
@javaClass{org.rascalmpl.library.Prelude}
public java int size(list[&T] lst);

@doc{
.Synopsis
Compute a sublist of a list.

.Description
Returns a sublist of `lst` from index `start` of length `len`.

NOTE: In most cases it is better to use the built-in [slice]((Rascal:List-Slice)) notation,
see the example below.

.Examples
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
public java list[&T] slice(list[&T] lst, int begin, int len);

@doc{
.Synopsis
Sort the elements of a list.

.Description
Sort the elements of a list:

*  Use the built-in ordering on values to compare list elements.
*  Give an additional `lessThan` function that will be used to compare elements.

.Examples
```rascal-shell
import List;
import String;
sort([10, 4, -2, 11, 100, 5]);
fruits = ["mango", "strawberry", "pear", "pineapple", "banana", "grape", "kiwi"];
sort(fruits);
sort(fruits, bool(str a, str b){ return size(a) > size(b); });
```}
public list[&T] sort(list[&T] lst) =
	sort(lst, bool (&T a, &T b) { return a < b; } );
	
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] sort(list[&T] l, bool (&T a, &T b) less) ;

@doc{
.Synopsis 
Check whether a list is sorted or not.

.Description

Checks whether or not a list is sorted by searching for any out-of-order elements.
The empty list is defined to be "sorted" and what sorted means is defined the
higher-order parameter "less" which should implement a partial-order relation
between the two parameters.
}
public bool isSorted(list[&T] l, bool (&T a, &T b) less = bool (&T a, &T b) { return a < b; })
 = !any([*_, &T a, &T b, *_] := l, less(b, a));

@doc{
.Synopsis
Shuffle a list.

.Description
Returns a random (unbiased) shuffled list.

.Examples
```rascal-shell
import List;
shuffle([1,4,2,3]);
shuffle(["zebra", "elephant", "snake", "owl"]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] shuffle(list[&T] l);

@doc{
.Synopsis
Shuffle a list with a seed.

.Description
Returns a random (unbiased) shuffled list, every call with the same seed shuffles in the same order.

.Examples
```rascal-shell
import List;
shuffle([1,2,3,4]);
shuffle([1,2,3,4]);
shuffle([1,2,3,4], 1);
shuffle([1,2,3,4], 1);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] shuffle(list[&T] l, int seed);

@doc{
.Synopsis
Split a list into two halves.

.Examples
```rascal-shell
import List;
split([3, 1, 4, 5, 7]);
split(["zebra", "elephant", "snake", "owl"]);
```}
public tuple[list[&T],list[&T]] split(list[&T] l) {
	half = size(l)/2;
	return <take(half,l), drop(half,l)>;
}

@doc{
.Synopsis
Sum the elements of a list.

.Examples
```rascal-shell
import List;
sum([3, 1, 4, 5]);
sum([3, 1.5, 4, 5]);
```}
public (&T <:num) sum([(&T <: num) hd, *(&T <: num) tl]) = (hd | it + i | i <- tl);
public (&T <:num) sum(list[&T] _: []) { throw EmptyList(); }

@doc{
.Synopsis
Get the tail element(s) from a list.

.Description

*  Return a list consisting of all but the first element of `lst`.
*  Return a list consisting of the last `n` elements of `lst`.

.Examples
```rascal-shell,error
```
All but first element:
```rascal-shell,continue,error
import List;
tail([10,20,30]);
```
Try an error case:
```rascal-shell,continue,error
tail([]);
```
Last n elements:
```rascal-shell,continue,error
tail([10, 20, 30, 40, 50, 60], 3);
```
Try an error case:
```rascal-shell,continue,error
tail([10, 20, 30, 40, 50, 60], 10);
```}
public list[&T] tail([&T _, *&T t]) = t;
public list[&T] tail(list[&T] _:[]) { throw EmptyList(); }
 
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] tail(list[&T] lst, int len) throws IndexOutOfBounds;

@doc{
.Synopsis
Get number of elements from the head of a list.

.Description
Get `n` elements (or `size(lst)` elements if `size(lst) < n`) from the head of the list.
See ((List-drop)) to remove elements from the head of a list.

.Examples
```rascal-shell
import List;
take(2, [3, 1, 4, 5]);
take(6, [3, 1, 4, 5]);
take(2, ["zebra", "elephant", "snake", "owl"]);
```}
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] take(int n, list[&T] lst);

@doc{
.Synopsis
Remove an arbitrary element from a list, returns the element and the modified list.

.Description
Select an arbitrary element from `lst`, and return a tuple consisting of:

*  the selected element, and 
*  a new list consisting of all elements of `lst` except the selected element.


See ((List-getOneFrom)) to only selected an element from a list.

.Examples
```rascal-shell
import List;
takeOneFrom([10,20,30,40,50]);
takeOneFrom([10,20,30,40,50]);
takeOneFrom([10,20,30,40,50]);
takeOneFrom(["zebra", "elephant", "snake", "owl"]);
takeOneFrom(["zebra", "elephant", "snake", "owl"]);
takeOneFrom(["zebra", "elephant", "snake", "owl"]);
```}
@javaClass{org.rascalmpl.library.Prelude}
public java tuple[&T, list[&T]] takeOneFrom(list[&T] lst);

@doc{
.Synopsis
Take elements from the front of the list as long as a predicate is true.

.Examples
```rascal-shell
import List;
bool isEven(int a) = a mod 2 == 0;
takeWhile([2,4,6,8,1,2,3,4,5],isEven);
```}
public list[&T] takeWhile(list[&T] lst, bool (&T a) take) {
	i = 0;
	return while(i < size(lst) && take(lst[i])) {
		append lst[i];
		i+=1;
	}
}

@doc{
.Synopsis
Convert a list of pairs to a map; first elements are associated with a set of second elements.

.Description
Convert a list of tuples to a map in which the first element of each tuple is 
associated with the set of second elements from all tuples with the same first element. Keys should be unique.

.Examples
```rascal-shell
import List;
toMap([<1,10>, <1, 11>, <2, 20>, <3, 30>, <3, 31>]);
```

.Pitfalls
`toMap` collects all values in tuples with the same first value in a set.
Contrast this with `toMapUnique` that associates each first tuple value with the second tuple value,
but imposes the constraint that those keys are unique.}
@javaClass{org.rascalmpl.library.Prelude}
public java map[&A,list[&B]] toMap(list[tuple[&A, &B]] lst) throws MultipleKey;

@doc{
.Synopsis
Convert a list of tuples to a map; result must be a map.

.Description
Convert a list of tuples to a map; result must be a map.

.Examples
```rascal-shell,error
import List;
toMapUnique([<1,10>, <2, 20>, <3, 30>]);
```
Let's explore an error case:
```rascal-shell,continue,error
toMapUnique([<1,10>, <1, 11>, <2, 20>, <3, 30>]);
```

.Pitfalls
The keys in a map are unique by definition.
`toMapUnique` throws a `MultipleKey` exception when the list contains more than one tuple with the same first value.}
@javaClass{org.rascalmpl.library.Prelude}
public java map[&A,&B] toMapUnique(list[tuple[&A, &B]] lst) throws MultipleKey;

@doc{
.Synopsis
Take the top element of a list.
.Description
This function is identical to ((List-head)).
Also see ((List-pop)) and ((List-push)).

.Examples
```rascal-shell
import List;
top([3, 1, 4, 5]);
top(["zebra", "elephant", "snake", "owl"]);
```}
public &T top([&T t, *&T _]) = t;

@doc{
.Synopsis
Convert a list to a relation.
.Description
  Convert a list to relation, where each tuple encodes which elements are followed by each other.
  This function will return an empty relation for empty lists and for singleton lists.

.Examples
```rascal-shell
import List;
toRel([3, 1, 4, 5]);
toRel(["zebra", "elephant", "snake", "owl"]);
```}
public rel[&T,&T] toRel(list[&T] lst) {
  return { <from,to> | [*_, from, to, *_] := lst };
}

@doc{
.Synopsis
Convert a list to a set.

.Description
Convert `lst` to a set.

.Examples
```rascal-shell
import List;
toSet([10, 20, 30, 40]);
toSet(["zebra", "elephant", "snake", "owl"]);
```
Note that the same can be done using splicing
```rascal-shell,continue
l = [10,20,30,40];
s = {*l};
```}
@deprecated{Please use {*myList} instead.}
@javaClass{org.rascalmpl.library.Prelude}
public java set[&T] toSet(list[&T] lst);

@doc{
.Synopsis
Convert a list to a string.

.Description
Convert `lst` to a string.

.Examples
```rascal-shell
import List;
toString([10, 20, 30]);
toString(["zebra", "elephant", "snake", "owl"]);
```}
@javaClass{org.rascalmpl.library.Prelude}
public java str toString(list[&T] lst);


@doc{
.Synopsis
Convert a list to an indented string.

.Description
Convert `lst` to a indented string.

.Examples
```rascal-shell
import List;
itoString([10, 20, 30]);
itoString(["zebra", "elephant", "snake", "owl"]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java str itoString(list[&T] lst);


@doc{
.Synopsis
Make a pair (triple) of lists from a list of pairs (triples).

.Description

Also see ((List-unzip3));

.Examples
```rascal-shell
import List;
unzip2([<3,"thirty">, <1,"ten">, <4,"forty">]);
unzip3([<3,"thirty",300>, <1,"ten",100>, <4,"forty",400>]);
```}
public tuple[list[&T],list[&U]] unzip2(list[tuple[&T,&U]] lst) =
	<[t | <t,_> <- lst], [u | <_,u> <- lst]>;

// Make a triple of lists from a list of triples.
public tuple[list[&T],list[&U],list[&V]] unzip3(list[tuple[&T,&U,&V]] lst) =
	<[t | <t,_,_> <- lst], [u | <_,u,_> <- lst], [w | <_,_,w> <- lst]>;

@doc{
.Synopsis
Returns the list 0,1..n-1.
.Description
Returns the list `0`, `1`, .., `n-1`, this is slightly faster than `[0..n]`, since the returned values are shared.

.Examples
```rascal-shell
import List;
upTill(10);
```}
@javaClass{org.rascalmpl.library.Prelude}
public java list[int] upTill(int n);

@doc{
.Synopsis
Make a list of pairs from two (three) lists of the same length.

.Description

Also see ((List-unzip3)).

.Examples
```rascal-shell
import List;
zip2([3, 1, 4], ["thirty", "ten", "forty"]);
zip3([3, 1, 4], ["thirty", "ten", "forty"], [300, 100, 400]);
```}
public list[tuple[&T first, &U second]] zip2(list[&T] a, list[&U] b) {
	if(size(a) != size(b))
		throw IllegalArgument(<size(a),size(b)>, "List size mismatch");
	return [<elementAt(a,i), elementAt(b,i)> | i <- index(a)];
}

public list[tuple[&T first, &U second, &V third]] zip3(list[&T] a, list[&U] b, list[&V] c) {
	if(size(a) != size(b) || size(a) != size(c))
		throw IllegalArgument(<size(a),size(b),size(c)>, "List size mismatch");
	return [<elementAt(a,i), elementAt(b,i), elementAt(c,i)> | i <- index(a)];
}

