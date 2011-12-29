
@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module List

import Exception;
import Integer;

@doc{
Synopsis: Delete an element from a list.

Description:
Delete the `n`-th element from a list. A new list without the `n`-th element is returned as result.
The `IndexOutOfBounds` exception is thrown when n is not a valid index.

Examples:
<screen>
import List;
delete([1, 2, 3], 1);
delete(["zebra", "elephant", "snake", "owl"], 2);
</screen>
}
@javaClass{org.rascalmpl.library.List}
public java list[&T] delete(list[&T] lst, int n);

@doc{
Synopsis: The set of all legal indices of a list.

Description:
Returns the set of all legal index values for a list. Also see [$List/index] for a function that returns a list
of __ordered__ index values.

Examples:
<screen>
import List;
domain([1, 3, 5]);
domain(["zebra", "elephant", "snake", "owl"]);
// Compare this with the result of [$List/index]:
index([1, 3, 5]);
</screen>
}
@javaClass{org.rascalmpl.library.List}
public java set[int] domain(list[&T] lst);

@doc{
Synopsis: Drop elements from the head of a list.

Description:
Drop `n` elements (or `size(lst)` elements if `size(lst) < n`) from the head of `lst`.
See [$List/take] to get elements from the head of a list].

Examples:
<screen>
import List;
drop(2, [5, 1, 7, 3]);
drop(10, [5, 1, 7, 3]);
drop(2, ["zebra", "elephant", "snake", "owl"]);
</screen>
}
@javaClass{org.rascalmpl.library.List}
public java list[&T] drop(int n, list[&T] lst) ;

@doc{
Synopsis: Remove multiple occurrences of elements in a list. The first occurrence remains.

Examples:
<screen>
import List;
dup([3, 1, 5, 3, 1, 7, 1, 2]);
</screen>
}
public list[&T] dup(list[&T] lst) {
  done = {};
  return for (e <- lst, e notin done) {
    done += e;
    append e;
  }
}

@doc{
Synopsis: Pick a random element from a list.

Description:
Get an arbitrary element from a list. See [$List/takeOneFrom] for a function that also removes the selected element.

Examples:
<screen>
import List;
getOneFrom(["zebra", "elephant", "snake", "owl"]);
getOneFrom(["zebra", "elephant", "snake", "owl"]);
getOneFrom(["zebra", "elephant", "snake", "owl"]);
</screen>
}
@javaClass{org.rascalmpl.library.List}
public java &T getOneFrom(list[&T] lst);

@doc{
Synopsis: Get the first element(s) from a list.

Description:
# Returns the first element of a list or throws `EmptyList` when the list is empty. This is identical to [$List/top].
# Returns the first `n` elements of a list or throws `IndexOutOfBounds` when the list is too short. This is similar to [$List/take].

Examples:
<screen errors>
import List;
// Get the first element:
head([1, 2, 3]);
head(["zebra", "elephant", "snake", "owl"]);
// An exception is thrown when taking the head of an empty list:
head([]);
// Get the first n elements:
head([1, 2, 3, 4], 2);
head(["zebra", "elephant", "snake", "owl"], 2);
// An exception is thrown when the second argument exceeds the length of the list:
head([1, 2, 3, 5], 5);
</screen>
}
@javaClass{org.rascalmpl.library.List}
public java &T head(list[&T] lst) throws EmptyList;

// Get the first n elements of a list
@javaClass{org.rascalmpl.library.List}
public java list[&T] head(list[&T] lst, int n) throws IndexOutOfBounds;


@doc{
Synopsis: Split a list in a head and a tail.

Description:
This function is identical to [$List/pop].

Examples:
<screen>
import List;
headTail([3, 1, 4, 5]);
pop([3, 1, 4, 5]);
headTail(["zebra", "elephant", "snake", "owl"]);
</screen>
}
public tuple[&T, list[&T]] headTail(list[&T] lst) throws EmptyList {
  if ([&T h, list[&T] t] := lst)
    return <h, t>;
  throw EmptyList();
}

@doc{
Synopsis: A list of legal index values of a list.

Description:
Returns a list of all legal index values for a given list `lst`. See [$List/domain] for a function that returns a _set_ of legal index values.

Examples:
<screen>
import List;
index([1, 3, 5]);
index(["zebra", "elephant", "snake", "owl"]);
// Compare with the result of [$List/domain]:
domain([1, 3, 5]);
domain(["zebra", "elephant", "snake", "owl"]);
</screen>

Benefits:
This function is useful in [For] loops over lists.
}
public list[int] index(list[&T] lst) = upTill(size(lst));


@doc{
Synopsis: Index of first occurence of an element in a list.

Description:
Return index of first occurrence of `elt` in `lst`, or `-1` if `elt` is not found.
Also see [$List/lastIndexOf].

Examples:
<screen>
import List;
indexOf([3, 1, 4, 5], 4);
indexOf([3, 1, 4, 5], 7);
indexOf(["zebra", "elephant", "snake", "owl"], "snake");
indexOf(["zebra", "elephant", "snake", "owl"], "eagle");
</screen>
}
public int indexOf(list[&T] lst, &T elt) {
	for(i <- index(lst)) {
		if(lst[i] == elt) return i;
	}
	return -1;
}

@doc{
Synopsis: Insert an element at a specific position in a list.

Description:
Returns a new list with the value of `elm` inserted at index position `n` of the old list.

Examples:
<screen errors>
import List;
insertAt([1,2,3], 1, 5);
insertAt(["zebra", "elephant", "snake", "owl"], 2, "eagle");
//An exception is thrown when the index position is outside the list:
insertAt([1,2,3], 10, 5);
</screen>
}
@javaClass{org.rascalmpl.library.List}
public java list[&T] insertAt(list[&T] lst, int n, &T elm) throws IndexOutOfBounds;

@doc{
Synopsis: Join a list of values into a string separated by a separator.

Examples:
<screen>
import List;
intercalate("/", [3]);
intercalate("/", [3, 1, 4, 5]);
intercalate(", ", [3, 1, 4, 5]);
intercalate(", ", ["zebra", "elephant", "snake", "owl"]);
</screen>
}
public str intercalate(str sep, list[value] l) = 
	(l == []) ? "" : ( "<head(l)>" | it + "<sep><x>" | x <- tail(l) );

@doc{
Synopsis: Test whether a list is empty.

Description:
Returns `true` when a list is empty and `false` otherwise.

Examples:
<screen>
import List;
isEmpty([]);
isEmpty([1, 2, 3]);
</screen>
}
@javaClass{org.rascalmpl.library.List}
public java bool isEmpty(list[&T] lst);

@doc{
Synopsis: Apply a function to all list elements and return list of results.

Description:
Apply a function `fn` to each element of `lst` and return the list of results.

Examples:
<screen>
import List;
int incr(int x) { return x + 1; }
mapper([1, 2, 3, 4], incr);
</screen>
}
public list[&U] mapper(list[&T] lst, &U (&T) fn) =  [fn(elm) | &T elm <- lst];

@doc{
Synopsis: Determine the largest element in a list.

Examples:
<screen>
import List;
max([1, 3, 5, 2, 4]);
max(["zebra", "elephant", "snake", "owl"]);
</screen>
}
public &T max(list[&T] lst) =
	(head(lst) | (e > it ? e : it) | e <- tail(lst));
	
@doc{
Synopsis: Merge the elements of two sorted lists into one list.

Examples:

<screen>
import List;
merge([1, 3, 5], [2, 7, 9, 15], bool(int x, int y){ return x <= y; });
merge(["ape", "elephant", "owl", "snale", "zebra"], ["apple", "berry", "orange", "pineapple"], bool(str x, str y){ return x <= y; });
</screen>
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
Synopsis: Determine the smallest element in a list.

Examples:
<screen>
import List;
min([1, 3, 5, 2, 4]);
min(["zebra", "elephant", "snake", "owl"]);
</screen>
}
public &T min(list[&T] lst) =
	(head(lst) | (e < it ? e : it) | e <- tail(lst));

@doc{
Synopsis: Mix the elements of two lists.

Description:
Let n be the minimum of the length of the two lists `l` and `r`.
`mix` returns a list in which the first `n` elements are taken alternatingly from the left and the right list,
followed by the remaining elements of the longest list.

Examples:
<screen>
import List;
mix([3, 1, 7, 5, 9], [15, 25, 35]);
mix([3, 1, 7], [15, 25, 35, 45, 55]);
mix([3, 1, 7], ["elephant", "snake"]);
</screen>
}
public list[&T] mix(list[&T] l, list[&T] r){
	return [l[i],r[i]| i <- [0 .. (min(size(l),size(r)) - 1)]] + drop(size(r),l) + drop(size(l),r);
}

@doc{
Synopsis: Return the last element of a list, if any.

Description:
Also see [$List/tail] that returns a list of one or more of the last elements of a list.

Examples:
<screen>
import List;
last([1]);
last([3, 1, 4, 5]);
last(["zebra", "elephant", "snake", "owl"]);
tail([3, 1, 4, 5]);
</screen>
}
public &T last(list[&T] lst) throws EmptyList {
  if(lst == [] ) { throw EmptyList(); }
  if([list[&T] p, &T l] := lst){
  	return l;
  }
}

@doc{
Synopsis: Return index of last occurence of elt in lst, or -1 if elt is not found.

Description:
Also see [$List/indexOf].

Examples:
<screen>
import List;
lastIndexOf([3, 1, 4, 5, 4], 4);
lastIndexOf([3, 1, 4, 5, 4], 7);
lastIndexOf(["zebra", "owl", "elephant", "snake", "owl"], "owl");
</screen>
}
public int lastIndexOf(list[&T] lst, &T elt) {
	for(i <- reverse(index(lst))) {
		if(lst[i] == elt) return i;
	}
	return -1;
}

@doc{
Synopsis: Compute all permutations of a list.

Examples:
<screen>
import List;
permutations([1,2,3]);
</screen>
}
public set[list[&T]] permutations(list[&T] lst)
{
  int N = size(lst);
  if(N <= 1)
  	return {[lst]};
  	
  set[list[&T]] result = {};
  
  for(int i <- domain(lst)){
   
  	set[list[&T]] perm = permutations(head(lst, i) + tail(lst, N - i -1));
  	
  	for(list[&T] sub <- perm){
  		result = result + {[lst[i], sub]};
  	}
  }
  return result;
}

@doc{
Synopsis: Pop top element from list, return a tuple.
Description:
This function is identical to [headTail].
Also see [$List/push] and [$List/top].

Examples:
<screen>
import List;
pop([3, 1, 4, 5]);
headTail([3, 1, 4, 5]);
pop(["zebra", "elephant", "snake", "owl"]);
</screen>

}
public tuple[&T, list[&T]] pop(list[&T] lst) throws EmptyList =
  headTail(lst);

@doc{
Synopsis: Return all but the last element of a list.

Examples:
<screen>
import List;
prefix([3, 1, 4, 5]);
prefix([]);
prefix(["zebra", "elephant", "snake", "owl"]);
</screen>
}
@javaClass{org.rascalmpl.library.List}
public java list[&T] prefix(list[&T] lst) ;

@doc{
Synopsis: Push an element in front of a list.

Description:
Also see [$List/pop] and [$List/top].

Examples:
<screen>
import List;
push(7, [3, 1, 4, 5]);
push("eagle", ["zebra", "elephant", "snake", "owl"]);
</screen>
}
public list[&T] push(&T elem, list[&T] lst) = [elem] + lst;

@doc{
Synopsis: Apply a function to successive elements of list and combine the results (__deprecated__).

Description:
Apply the function `fn` to successive elements of list `lst` starting with `unit`.

Examples:
<screen>
import List;
int add(int x, int y) { return x + y; }
reducer([10, 20, 30, 40], add, 0); 
</screen>

Benefits:

Pitfalls:
This function is __deprecated__, use a [$Expressions/Reducer] instead.
}
public &T reducer(list[&T] lst, &T (&T, &T) fn, &T unit)
{
  &T result = unit;
  for(&T elm <- lst){
     result = fn(result, elm);
  }
  return result;
}



@doc{
Synopsis: Reverse a list.

Description:
Returns a list with the elements of `lst` in reverse order.

Examples:
<screen>
import List;
reverse([1,4,2,3]);
reverse(["zebra", "elephant", "snake", "owl"]);
</screen>
}
@javaClass{org.rascalmpl.library.List}
public java list[&T] reverse(list[&T] lst);

@doc{
Synopsis: Determine the number of elements in a list.

Examples:
<screen>
import List;
size([20, 10, 30]);
size(["zebra", "elephant", "snake", "owl"]);
</screen>

Questions:

QValue:
desc: Create a list of the right size.
list:
import List;
list[str] text = <?>;
test: size(text) == 3;

QValue:
desc: Determine the number of elements in a list
list:
import List;
text = ["abc", "def", "ghi"];
test: <?>(text) == 3;
}
@javaClass{org.rascalmpl.library.List}
public java int size(list[&T] lst);

@doc{
Synopsis: Compute a sublist of a list.

Description:
Returns a sublist of `lst` from index `start` of length `len`.

Examples:
<screen>
import List;
slice([10, 20, 30, 40, 50, 60], 2, 3);
slice(["zebra", "elephant", "snake", "owl"], 1, 2);
</screen>
}
@javaClass{org.rascalmpl.library.List}
public java list[&T] slice(list[&T] lst, int begin, int len);

@doc{
Synopsis: Split a list into two halves.

Examples:
<screen>
import List;
split([3, 1, 4, 5, 7]);
split(["zebra", "elephant", "snake", "owl"]);
</screen>
}
public tuple[list[&T],list[&T]] split(list[&T] l) {
	half = size(l)/2;
	return <take(half,l), drop(half,l)>;
}

@doc{
Synopsis: Sort the elements of a list.

Description:
Sort the elements of a list:
# Use the built-in ordering on values to compare list elements.
# Give an additional `lessThanOrEqual` function that will be used to compare elements.

Examples:
<screen>
import List;
import String;
sort([10, 4, -2, 11, 100, 5]);
fruits = ["mango", "strawberry", "pear", "pineapple", "banana", "grape", "kiwi"];
sort(fruits);
sort(fruits, bool(str a, str b){ return size(a) >= size(b); });
</screen>
}
public list[&T] sort(list[&T] lst) =
	sort(lst, bool (&T a,&T b) { return a <= b; } );
	
@javaClass{org.rascalmpl.library.List}
public java list[&T] sort(list[&T] l, bool (&T a, &T b) lessOrEqual) ;

@doc{
Synopsis: Sum the elements of a list.

Examples:
<screen>
import List;
sum([3, 1, 4, 5]);
sum([3, 1.5, 4, 5]);
</screen>
}
public num sum(list[num] l) = (head(l) | it + e | e <- tail(l));

@doc{
Synopsis: Get the tail element(s) from a list.

Description:
# Return a list consisting of all but the first element of `lst`.
# Return a list consisting of the last `n` elements of `lst`.

Examples:
<screen errors>
// All but first element:
import List;
tail([10,20,30]);
tail([]);
// Last n elements:
tail([10, 20, 30, 40, 50, 60], 3);
tail([10, 20, 30, 40, 50, 60], 10);
</screen>
}
@javaClass{org.rascalmpl.library.List}
public java list[&T] tail(list[&T] lst);
 
@javaClass{org.rascalmpl.library.List}
public java list[&T] tail(list[&T] lst, int len) throws IndexOutOfBoundsError;

@doc{
Synopsis: Get number of elements from the head of a list.

Description:
Get `n` elements (or `size(lst)` elements if `size(lst) < n`) from the head of the list.
See [$List/drop] to remove elements from the head of a list.

Examples:
<screen>
import List;
take(2, [3, 1, 4, 5]);
take(6, [3, 1, 4, 5]);
take(2, ["zebra", "elephant", "snake", "owl"]);
</screen>
}
@javaClass{org.rascalmpl.library.List}
public java list[&T] take(int n, list[&T] lst);

@doc{
Synopsis: Remove an arbitrary element from a list, returns the element and the modified list.

Description:
Select an arbitrary element from `lst`, and return a tuple consisting of:
* the selected element, and 
* a new list consisting of all elements of `lst` except the selected element.


See [$List/getOneFrom] to only selected an element from a list.

Examples:
<screen>
import List;
takeOneFrom([10,20,30,40,50]);
takeOneFrom([10,20,30,40,50]);
takeOneFrom([10,20,30,40,50]);
takeOneFrom(["zebra", "elephant", "snake", "owl"]);
takeOneFrom(["zebra", "elephant", "snake", "owl"]);
takeOneFrom(["zebra", "elephant", "snake", "owl"]);
</screen>
}
@javaClass{org.rascalmpl.library.List}
public java tuple[&T, list[&T]] takeOneFrom(list[&T] lst);

@doc{
Synopsis: Convert a list of pairs to a map; first elements are associated with a set of second elements.

Description:
Convert a list of tuples to a map in which the first element of each tuple is 
associated with the set of second elements from all tuples with the same first element. Keys should be unique.

Examples:
<screen>
import List;
toMap([<1,10>, <1, 11>, <2, 20>, <3, 30>, <3, 31>]);
</screen>

Pitfalls:
`toMap` collects all values in tuples with the same first value in a set.
Contrast this with `toMapUnique` that associates each first tuple value with the second tuple value,
but imposes the constraint that those keys are unique.
}
@javaClass{org.rascalmpl.library.List}
public java map[&A,set[&B]] toMap(list[tuple[&A, &B]] lst) throws DuplicateKey;

@doc{
Synopsis: Convert a list of tuples to a map; result must be a map.

Description:
Convert a list of tuples to a map; result must be a map.

Examples:
<screen errors>
import List;
toMapUnique([<1,10>, <2, 20>, <3, 30>]);
//Let's explore an error case:
toMapUnique([<1,10>, <1, 11>, <2, 20>, <3, 30>]);
</screen>

Pitfalls:
The keys in a map are unique by definition.
`toMapUnique` throws a `MultipleKey` exception when the list contains more than one tuple with the same first value.
}
@javaClass{org.rascalmpl.library.List}
public java map[&A,&B] toMapUnique(list[tuple[&A, &B]] lst) throws DuplicateKey;

@doc{
Synopsis: Convert a list to a set.

Description:
Convert `lst` to a set.

Examples:
<screen>
import List;
toSet([10, 20, 30, 40]);
toSet(["zebra", "elephant", "snake", "owl"]);
</screen>
}
@javaClass{org.rascalmpl.library.List}
public java set[&T] toSet(list[&T] lst);

@doc{
Synopsis: Convert a list to a relation.
Description:
  Convert a list to relation, where each tuple encodes which elements are followed by each other.
  This function will return an empty relation for empty lists and for singleton lists.

Examples:
<screen>
import List;
toRel([3, 1, 4, 5]);
toRel(["zebra", "elephant", "snake", "owl"]);
</screen>
}
public rel[&T,&T] toRel(list[&T] lst) {
  return { <from,to> | [_*, &T from, &T to, _*] := lst };
}

@doc{
Synopsis: Returns the list 0,1..n-1.
Description:
Returns the list `0`, `1`, .., `n-1`, this is slightly faster than `[0..n]`, since the returned values are shared.

Examples:
<screen>
import List;
upTill(10);
</screen>
}
@javaClass{org.rascalmpl.library.List}
public java list[int] upTill(int n);

@doc{
Synopsis: Take the top element of a list.
Description:
This function is identical to [head].
Also see [$List/pop] and [$List/push].

Examples:
<screen>
import List;
top([3, 1, 4, 5]);
top(["zebra", "elephant", "snake", "owl"]);
</screen>

}
public &T top(list[&T] lst) throws EmptyList = head(lst);


@doc{
Synopsis: Convert a list to a string.

Description:
Convert `lst` to a string.

Examples:
<screen>
import List;
toString([10, 20, 30]);
toString(["zebra", "elephant", "snake", "owl"]);
</screen>
}
@javaClass{org.rascalmpl.library.List}
public java str toString(list[&T] lst);

@doc{
Synopsis: Make a pair (triple) of lists from a list of pairs (triples).

Description:
Also see [$List/unzip];

Examples:
<screen>
import List;
unzip([<3,"thirty">, <1,"ten">, <4,"forty">]);
unzip([<3,"thirty",300>, <1,"ten",100>, <4,"forty",400>]);
</screen>
}
public tuple[list[&T],list[&U]] unzip(list[tuple[&T,&U]] lst) =
	<[t | <t,_> <- lst], [u | <_,u> <- lst]>;

// Make a triple of lists from a list of triples.
public tuple[list[&T],list[&U],list[&V]] unzip(list[tuple[&T,&U,&V]] lst) =
	<[t | <t,_,_> <- lst], [u | <_,u,_> <- lst], [w | <_,_,w> <- lst]>;

@doc{
Synopsis: Make a list of pairs from two (three) lists of the same length.

Description:
Also see [$List/unzip].

Examples:
<screen>
import List;
zip([3, 1, 4], ["thirty", "ten", "forty"]);
zip([3, 1, 4], ["thirty", "ten", "forty"], [300, 100, 400]);
</screen>
}
public list[tuple[&T first, &U second]] zip(list[&T] a, list[&U] b) {
	if(size(a) != size(b))
		throw IllegalArgument(<size(a),size(b)>, "List size mismatch");
	return [<a[i], b[i]> | i <- index(a)];
}

public list[tuple[&T first, &U second, &V third]] zip(list[&T] a, list[&U] b, list[&V] c) {
	if(size(a) != size(b) || size(a) != size(c))
		throw IllegalArgument(<size(a),size(b),size(c)>, "List size mismatch");
	return [<a[i], b[i], c[i]> | i <- index(a)];
}

