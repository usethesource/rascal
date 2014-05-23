
@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}


module List

import util::Math;
import Map;

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

Questions:

QChoice: When you delete an element from a list, the number of elements:
g: Decreases by one.
b: Decreases with the number of occurrences of the deleted element.
b: Stays the same.
b: Increases by one.

QType:
prep: import List;
make: L = list[arb[bool,int,str],4,6]
make: I = int[0,3]
test:  delete(<L>, <I>)

QValue:
prep: import List;
make: L = list[arb[bool,int,str],4,6]
make: I = int[0,3]
expr: C = delete(<L>, <I>)
hint: <C>
test: delete(<L>, <I>) == <?>

}
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] delete(list[&T] lst, int n);

//@doc{
//Synopsis: The set of all legal indices of a list [Deprecated, use index]
//
//Description:
//Returns the set of all legal index values for a list. Also see [$List/index] for a function that returns a list
//of __ordered__ index values.
//
//Examples:
//<screen>
//import List;
//domain([1, 3, 5]);
//domain(["zebra", "elephant", "snake", "owl"]);
//// Compare this with the result of [$List/index]:
//index([1, 3, 5]);
//</screen>
//
//Questions:
//QChoice: The number of elements in the domain of a list is:
//g: Equal to the number of elements in the list.
//b: Depends on the number of repeated elements in the list.
//b: One less than the number of elements in the list.
//b: One larger than the number of elements in the list.
//
//QType:
//prep: import List;
//make: L = list[arb[int,str],3,5]
//test: domain(<L>)
//
//QValue:
//prep: import List;
//make: L = list[arb[int,str],0,5]
//expr: H = domain(<L>)
//hint: <H>
//test: domain(<L>) == <?>
//
//
//}
//@javaClass{org.rascalmpl.library.Prelude}
//public java set[int] domain(list[&T] lst);

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

Questions:

QChoice: Dropping $n$ elements from a list returns a list with
g: n elements less than the original list.
b: 1 element less that the original list.
b: n elements.
b: n-1 elements.
b: n-1 elements less than the original list.

QType:
prep: import List;
make: L = list[arb[bool,int,str],4,6]
make: N = int[0,3]
expr: H = drop(<N>, <L>)
hint: <H>
test: drop(<N>, <L>)

QValue:
prep: import List;
make: L = list[arb[bool,int,str],4,6]
make: N = int[0,3]
expr: H = drop(<N>, <L>)
hint: <H>
test: drop(<N>, <L>) == <?>

}
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] drop(int n, list[&T] lst) ;

@doc{
Synopsis: Remove multiple occurrences of elements in a list. The first occurrence remains.

Examples:
<screen>
import List;
dup([3, 1, 5, 3, 1, 7, 1, 2]);
</screen>

Questions:

QType:
prep: import List;
make: L = list[arb[bool,int,str],4,6]
expr: H = dup(<L>)
hint: <H>
test: dup(<L>)


QValue:
prep: import List;
make: L = list[arb[bool,int,str],4,6]
make: I = int[0,3]
make: J = int[0,3]
expr: L1 = [<L>[<I>], <L>[<J>], *<L>, <L>[<J>], <L>[<I>]]
expr: H = dup(<L1>)
hint: <H>
test: dup(<L1>) == <?>



}
public list[&T] dup(list[&T] lst) {
  done = {};
  return for (e <- lst, e notin done) {
    done = done + {e};
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

Questions:

QType:
prep: import List;
make: L = list[arb[int,str],1,6]
expr: H =  getOneFrom(<L>)
hint: <H>
test: getOneFrom(<L>)

}
@javaClass{org.rascalmpl.library.Prelude}
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

Questions:

QType:
prep: import List;
make: L = list[arb[int,str],1,6]
test: head(<L>)

QValue:
prep: import List;
make: L = list[arb[int,str],1,6]
expr: H = head(<L>)
hint: <H>
test: head(<L>) == <?>


}
@javaClass{org.rascalmpl.library.Prelude}
public java &T head(list[&T] lst) throws EmptyList;

// Get the first n elements of a list
@javaClass{org.rascalmpl.library.Prelude}
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

Questions:

QType:
prep: import List;
make: L = list[arb[int,str],1,5]
test: headTail(<L>)

QValue:
prep: import List;
make: L = list[arb[int,str],1,5]
expr: H = headTail(<L>)
hint: <H>
test: headTail(<L>) == <?>
}
public tuple[&T, list[&T]] headTail(list[&T] lst) throws EmptyList {
	 if(!isEmpty(lst))
    return <head(lst), tail(lst)>;
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

Questions:

QChoice: The number of elements in the index of a list is:
g: Equal to the number of elements in the list.
b: Depends on the number of repeated elements in the list.
b: One less than the number of elements in the list.
b: One larger than the number of elements in the list.

QType:
prep: import List;
make: L = list[arb[int,str],3,5]
test: index(<L>)

QValue:
prep: import List;
make: L = list[arb[int,str],0,5]
expr: H = index(<L>)
hint: <H>
test: index(<L>) == <?>
}
public list[int] index(list[&T] lst) = upTill(size(lst));


@doc{
Synopsis: Index of first occurrence of an element in a list.

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

Questions:

Qtype:
prep: import List;
make: L = list[arb[int,str],4,6]
make: I = int[0,3]
expr: E = <L>[<I>]
test: indexOf(<L>, <E>)

QValue:
prep: import List;
make: L = list[arb[int,str],4,6]
make: I = int[0,3]
expr: E = <L>[<I>]
expr: H = indexOf(<L>, <E>)
hint: <H>
test: indexOf(<L>, <E>) == <?>





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

Questions:

QChoice: After inserting an element in a list with $N$ elements, the new list has
g: N+1 elements.
b: N elements.
b: N-1 elements.
b: a length that depends on the inserted element.

QType:
prep: import List;
make: E = arb[int,str]
make: L = list[same[E],4,6]
make: I = int[0,3]
test: insertAt(<L>, <I>, <E>)

QValue:
prep: import List;
make: E = arb[int,str]
make: L = list[same[E],4,6]
make: I = int[0,3]
expr: H = insertAt(<L>, <I>, <E>)
hint: <H>
test: insertAt(<L>, <I>, <E>) == <?>




}
@javaClass{org.rascalmpl.library.Prelude}
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

Questions:

QValue:
prep: import List;
make: L = list[int,0,5]
expr: H = intercalate(";", <L>) 
hint: <H>
test: intercalate(";", <L>) == <?>
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

Questions:

QType:
prep: import List;
make: L = list[arb[int,str], 0, 6]
test: isEmpty(<L>)

QValue:
prep: import List;
make: L = list[arb[int,str], 0, 6]
expr: H = isEmpty(<L>)
hint: <H>
test: isEmpty(<L>) == <?>


}
@javaClass{org.rascalmpl.library.Prelude}
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

Questions:

QType:
prep: import List;
make: L = list[int]
type: same[L]
list:
int incr(int x) { return x + 1; }
test: mapper(<L>, incr)


QValue:
prep: import List;
make: L = list[int]
expr: H = mapper(<L>, int(int n){ return n + 1; })
hint: <H>
list:
int incr(int x) { return x + 1; }
test: mapper(<L>, incr) == <?>
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

Questions:

QType:
prep: import List;
make: L = list[arb[int,str], 1, 6]
test: max(<L>)

QValue:
prep: import List;
make: L = list[arb[int,str], 1, 6]
expr: H = max(<L>)
hint: <H>
test: max(<L>) == <?>
}
public &T max(list[&T] lst) throws EmptyList =
	(head(lst) | (e > it ? e : it) | e <- tail(lst));
	
@doc{
Synopsis: Merge the elements of two sorted lists into one list.

Description:
Merge the elements of two sorted lists into one list using the built-in ordering between values.
Optional, a comparison function `lessOrEqual` may be given for a user-defined ordering between values.


Examples:

<screen>
import List;
merge([1, 3, 5], [2, 7, 9, 15]);
merge(["ape", "elephant", "owl", "snale", "zebra"], ["apple", "berry", "orange", "pineapple"]);
// Merge two lists of strings and use their length as ordering:
import String;
merge(["ape", "owl", "snale", "zebra", "elephant"], ["apple", "berry", "orange", "pineapple"], bool(str x, str y){ return size(x) <= size(y); });
</screen>

Questions:

QType:
prep: import List;
make: L = list[arb[int,str]]
make: M = same[L]
expr: L1 = sort(<L>)
expr: M1 = sort(<M>)
test: merge(<L1>, <M1>)

QValue:
prep: import List;
make: L = list[arb[int,str]]
make: M = same[L]
expr: L1 = sort(<L>)
expr: M1 = sort(<M>)
expr: H = merge(<L1>, <M1>)
hint: <H>
test: merge(<L1>, <M1>)

QValue:
prep: import List;
prep: import String;
make: L = list[str]
make: M = same[L]
expr: L1 = sort(<L>,bool(str x, str y){return size(x) > size(y);})
expr: M1 = sort(<M>,bool(str x, str y){return size(x) > size(y);})
expr: H = merge(<L1>, <M1>, bool(str x, str y){return size(x) > size(y);})
hint: <H>
test: merge(<L1>, <M1>, <?>) == <H>

}

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
Synopsis: Determine the smallest element in a list.

Examples:
<screen>
import List;
min([1, 3, 5, 2, 4]);
min(["zebra", "elephant", "snake", "owl"]);
</screen>

Questions:

QType:
prep: import List;
make: L = list[arb[int,str], 1, 6]
test: min(<L>)

QValue:
prep: import List;
make: L = list[arb[int,str], 1, 6]
expr: H = min(<L>)
hint: <H>
test: min(<L>) == <?>
}
public &T min(list[&T] lst) =
	(head(lst) | (e < it ? e : it) | e <- tail(lst));

@doc{
Synopsis: Mix the elements of two lists.

Description:
Let n be the minimum of the length of the two lists `l` and `r`.
`mix` returns a list in which the first `n` elements are taken alternately from the left and the right list,
followed by the remaining elements of the longest list.

Examples:
<screen>
import List;
mix([3, 1, 7, 5, 9], [15, 25, 35]);
mix([3, 1, 7], [15, 25, 35, 45, 55]);
mix([3, 1, 7], ["elephant", "snake"]);
</screen>

Questions:

QChoice: Given two lists of length $N$, respectively, $M$. What is the length of the mixed list:
g: N + M.
b: N + M - 1.
b: N.
b: M.
b: N * M.

QType:
prep: import List;
make: L = list[arb[int,str]]
make: M = same[L]
test: mix(<L>, <M>)

QValue:
prep: import List;
make: L = list[arb[int,str]]
make: M = same[L]
expr: H =  mix(<L>, <M>)
hint: <H>
test: mix(<L>, <M>) == <?>


}
public list[&T] mix(list[&T] l, list[&T] r){
	return [elementAt(l,i),elementAt(r,i)| i <- [0 .. min(size(l),size(r))]] + drop(size(r),l) + drop(size(l),r);
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

Questions:

QType:
prep: import List;
make: L = list[arb[int,str], 1, 6]
test: last(<L>)

QValue:
prep: import List;
make: L = list[arb[int,str], 1, 6]
expr: H =  last(<L>)
hint: <H>
test: last(<L>) == <?>
}
public &T last(list[&T] lst) throws EmptyList {
  if(lst == [] ) { throw EmptyList(); }
  if([*p, l] := lst){
  	return l;
  }
}

@doc{
Synopsis: Return index of last occurrence of elt in lst, or -1 if elt is not found.

Description:
Also see [$List/indexOf].

Examples:
<screen>
import List;
lastIndexOf([3, 1, 4, 5, 4], 4);
lastIndexOf([3, 1, 4, 5, 4], 7);
lastIndexOf(["zebra", "owl", "elephant", "snake", "owl"], "owl");
</screen>

Questions:
Qtype:
prep: import List;
make: L = list[arb[int,str],4,6]
make: I = int[0,3]
expr: E = <L>[<I>]
test: lastIndexOf(<L>, <E>)

QValue:
prep: import List;
make: L = list[arb[int,str],3,4]
make: I = int[0,2]
expr: E = <L>[<I>]
expr: L1 = reverse(<L>) + <L>
expr: H = lastIndexOf(<L1>, <E>)
hint: <H>
test: lastIndexOf(<L1>, <E>) == <?>
}
public int lastIndexOf(list[&T] lst, &T elt) {
	for(i <- reverse(index(lst))) {
		if(lst[i] == elt) return i;
	}
	return -1;
}

public list[&T] remove(list[&T] lst, int indexToDelete) =
	[ lst[i] | i <- index(lst), i != indexToDelete ];

	

@doc{
Synopsis: Compute all permutations of a list.

Examples:
<screen>
import List;
permutations([1,2,3]);
</screen>

Questions:
QChoice: How many permutations has a list of N elements:
g: 1 x 2 x ... x N.
g: N x (N-1) x ... x 2 x 1.
g: N!
b: 1 + 2 + ... + N.
b: 1 + 2 + ... + N-1.

QType:
prep: import List;
make: L = list[arb[int,str]]
test: permutations(<L>)

QValue:
prep: import List;
prep: import Set;
make: L = list[arb[int,str]]
expr: H = size(permutations(<L>))
hint: <H>
test: size(permutations(<L>)) == <?>

QValue:
prep: import List;
make: L = list[arb[int,str],0,3]
expr: H = permutations(<L>)
hint: <H>
test: permutations(<L>) == <?>


}
public set[list[&T]] permutations(list[&T] lst) =
	permutationsBag(distribution(lst));

map[&T element, int occurs] removeFromBag(map[&T element, int occurs] b, &T el) =
	removeFromBag(b,el,1);

map[&T element, int occurs] removeFromBag(map[&T element, int occurs] b, &T el, int nr) =
	!(b[el] ?) ? b : (b[el] <= nr ? b - (el : b[el]) : b + (el : b[el] - nr)); 

set[list[&T]] permutationsBag(map[&T element, int occurs] b) =
	isEmpty(b) ? {[]} : 
	{ [e] + rest | e <- domain(b),
				   rest <- permutationsBag(removeFromBag(b,e))};

@doc{
Synopsis: Get the distribution of the elements of the list. That
is how often does each element occur in the list? 

Examples:
<screen>
import List;
distribution([4,4,4,3,1,2,1,1,3,4]);
</screen>
}
public map[&T element, int occurs] distribution(list[&T] lst) {
	res = while(!isEmpty(lst)) {
		<<e,occurs>,lst> = takeSame(lst);
		append <e,occurs>;
	}
	return toMapUnique(res);
}

tuple[tuple[&T el, int occurs] head, list[&T] rest] takeSame(list[&T] lst){
	&T h = head(lst);
	int occrs = size([el | &T el <- lst, el == h]);
	list[&T] rst = [el | &T el <- lst, el != h];
	return <<h, occrs>, rst>;
}

@doc{
Synopsis: Take elements from the front of the list as long as a predicate is true.

Examples:
<screen>
import List;
bool isEven(int a) = a mod 2 == 0;
takeWhile([2,4,6,8,1,2,3,4,5],isEven);
</screen>

Questions:

QType:
prep: import List;
make: L = list[int[-20,20]]
test: takeWhile(<L>, bool(int x){ return x > 0;})


QValue:
prep: import List;
make: L = list[int[-20,20]]
expr: H = takeWhile(<L>, bool(int x){ return x > 0;})
hint: <H>
test: takeWhile(<L>, bool(int x){ return x > 0;}) == <?>

QValue:
prep: import List;
make: L = list[int[-20,2],3,6]
expr: M = takeWhile(<L>, bool(int x){ return x < 0;})
hint: bool(int x){ return x < 0;}
test: takeWhile(<L>, <?>) == <M>



}

public list[&T] takeWhile(list[&T] lst, bool (&T a) take) {
	i = 0;
	return while(i < size(lst) && take(lst[i])) {
		append lst[i];
		i+=1;
	}
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

Questions:

QType:
prep: import List;
make: L = list[arb[int,str],1,5]
test: pop(<L>)

QValue:
prep: import List;
make: L = list[arb[int,str],1,5]
expr: H = pop(<L>)
hint: <H>
test: pop(<L>) == <?>

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

Questions:
QChoice: Takking the prefix of a list with $N$ elements returns a list with
g: N-1 elements.
b: N elements.
b: N+1 elements.
b: N * (N-1) elements.

QType:
prep: import List;
make: L = list[arb[bool,int,str],4,6]
test: prefix(<L>)

QValue:
prep: import List;
make: L = list[arb[bool,int,str],4,6]
expr: H = prefix(<L>)
hint: <H>
test: prefix(<L>) == <?>

}
@javaClass{org.rascalmpl.library.Prelude}
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

Questions:


QType:
prep: import List;
make: E = arb[int,str]
make: L = list[same[E]]
test: push(<E>, <L>)

QValue:
prep: import List;
make: E = arb[int,str]
make: L = list[same[E]]
expr: H =  push(<E>, <L>)
hint: <H>
test: push(<E>, <L>) == <?>

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

Questions:

QType:
prep: import List;
make: L = list[arb[int,str],0,5]
test: reverse(<L>)

QValue:
prep: import List;
make: L = list[arb[int,str],0,5]
expr: H =  reverse(<L>) 
hint: <H>
test: reverse(<L>) == <?>
}
@javaClass{org.rascalmpl.library.Prelude}
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
@javaClass{org.rascalmpl.library.Prelude}
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

Questions:

QChoice: Computing `slice(L, B, N)` returns a list with:
g: N elements.
b: B elements.
b: B + N elements.
b: N - 1 elements.
b: B - 1 elements.

QType:
prep: import List;
make: L = list[arb[int,str],3,5]
make: B = int[0,2]
make: E = int[2,5]
test: slice(<L>,<B>,<E>)

QValue:
prep: import List;
make: L = list[arb[int,str],3,5]
make: B = int[0,2]
make: E = int[2,5]
expr:  H =  slice(<L>,<B>,<E>)
hint: <H>
test: slice(<L>,<B>,<E>) == <?>


}
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] slice(list[&T] lst, int begin, int len);

@doc{
Synopsis: Split a list into two halves.

Examples:
<screen>
import List;
split([3, 1, 4, 5, 7]);
split(["zebra", "elephant", "snake", "owl"]);
</screen>

Questions: 

QType:
prep: import List;
make: L = list[arb[int,str]]
test: split(<L>)


QValue:
prep: import List;
make: L = list[arb[int,str]]
expr: H = split(<L>)
hint: <H>
test: split(<L>) == <?>


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
# Give an additional `lessThan` function that will be used to compare elements.

Examples:
<screen>
import List;
import String;
sort([10, 4, -2, 11, 100, 5]);
fruits = ["mango", "strawberry", "pear", "pineapple", "banana", "grape", "kiwi"];
sort(fruits);
sort(fruits, bool(str a, str b){ return size(a) > size(b); });
</screen>

Questions:

QChoice: Sorting a listing with $N$ elements gives a list with:
g: N elements.
b: N - 1 elements.
b: N! elements.
b: N * (N - 1) elements.

QType:
prep: import List;
make: L = list[arb[int,str],1,5]
test: sort(<L>)

QValue:
prep: import List;
make: L = list[arb[int,str],1,5]
expr: H = sort(<L>)
hint: <H>
test: sort(<L>) == <?>




}
public list[&T] sort(list[&T] lst) =
	sort(lst, bool (&T a,&T b) { return a < b; } );
	
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] sort(list[&T] l, bool (&T a, &T b) less) ;

@doc{
Synopsis: Sum the elements of a list.

Examples:
<screen>
import List;
sum([3, 1, 4, 5]);
sum([3, 1.5, 4, 5]);
</screen>

Questions:

QType:
prep: import List;
make: L = list[num,2,7]
test: sum(<L>)

QValue:
prep: import List;
make: L = list[int,2,7]
expr: H = sum(<L>)
hint: <H>
test: sum(<L>)




}
public num sum(list[num] l) = (size(l) == 0) ? 0 : (head(l) | it + e | e <- tail(l));

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
// Try an error case:
tail([]);
// Last n elements:
tail([10, 20, 30, 40, 50, 60], 3);
// Try an error case:
tail([10, 20, 30, 40, 50, 60], 10);
</screen>


Questions:
QChoice: Computing `tail(L, M)` of a list with $N$ ($N$ > $M$) elements returns a list with
g: M elements.
b: N-M elements.
b: N elements.
b: N * M elements.

QType:
prep: import List;
make: L = list[arb[bool,int,str],4,6]
make: N = int[0,3]
test: tail(<L>, <N>)

QValue:
prep: import List;
make: L = list[arb[bool,int,str],4,6]
make: N = int[0,3]
expr: H = tail(<L>,<N>)
hint: <H>
test: tail(<L>,<N>) == <?>

}
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] tail(list[&T] lst) throws EmptyList;
 
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] tail(list[&T] lst, int len) throws IndexOutOfBounds;

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

Questions:
QChoice: Computing `take(M, L)` of a list with $N$ ($N$ > $M$) elements returns a list with
g: M elements.
b: N-M elements.
b: N elements.
b: N * M elements.

QType:
prep: import List;
make: L = list[arb[bool,int,str],4,6]
make: N = int[0,3]
test: take(<N>, <L>)

QValue:
prep: import List;
make: L = list[arb[bool,int,str],4,6]
make: N = int[0,3]
expr: H = take(<N>, <L>)
hint: <H>
test: take(<N>, <L>) == <?>
}
@javaClass{org.rascalmpl.library.Prelude}
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

Questions:

QType:
prep: import List;
make: L = list[arb[int,str], 1, 6]
test: takeOneFrom(<L>)
}
@javaClass{org.rascalmpl.library.Prelude}
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


Questions:

QType:
prep: import List;
make: L = list[tuple[int,int], 5, 8]
test: toMap(<L>)

QValue:
prep: import List;
make: L = list[tuple[int,int], 5, 8]
expr: H = toMap(<L>)
hint: <H>
test: toMap(<L>) == <?>



}
@javaClass{org.rascalmpl.library.Prelude}
public java map[&A,set[&B]] toMap(list[tuple[&A, &B]] lst) throws MultipleKey;

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

Questions:

QType:
prep: import List;
prep: import Set;
make: K = set[int, 6, 6]
make: V = list[int, 6, 6]
expr: KL = toList(<K>)
expr: P = zip(<KL>, <V>)
test: toMapUnique(<P>)

QValue:
prep: import List;
prep: import Set;
make: K = set[int, 6, 6]
make: V = list[int, 6, 6]
expr: KL = toList(<K>)
expr: P =  zip(<KL>, <V>)
expr: H = toMapUnique(<P>)
hint: <H>
test: toMapUnique(<P>) == <?>
}
@javaClass{org.rascalmpl.library.Prelude}
public java map[&A,&B] toMapUnique(list[tuple[&A, &B]] lst) throws MultipleKey;

@doc{
Synopsis: Convert a list to a set.

Description:
Convert `lst` to a set.

Examples:
<screen>
import List;
toSet([10, 20, 30, 40]);
toSet(["zebra", "elephant", "snake", "owl"]);
// Note that the same can be done using splicing
l = [10,20,30,40];
s = {*l};
</screen>

Questions:

QType:
prep: import List;
make: L = list[arb[int,str]]
test: toSet(<L>)

QValue:
prep: import List;
make: L = list[arb[int,str]]
expr: H = toSet(<L>)
hint: <H>
test: toSet(<L>) == <?>



}
@deprecated{Please use {*myList} instead.}
@javaClass{org.rascalmpl.library.Prelude}
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


Questions:

QType:
prep: import List;
make: L = list[arb[int,str],4,4]
test: toRel(<L>)

QValue:
prep: import List;
make: L = list[arb[int,str],4,4]
expr: H = toRel(<L>)
hint: <H>
test: toRel(<L>) == <?>

}
public rel[&T,&T] toRel(list[&T] lst) {
  return { <from,to> | [_*, from, to, _*] := lst };
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

Questions:

QType:
prep: import List;
make: N = int[2,5]
test: upTill(<N>)

QValue:
prep: import List;
make: N = int[2,5]
expr: H = upTill(<N>)
hint: <H>
test: upTill(<N>) == <?>

QValue:
prep: import List;
make: N = int[2,5]
expr: H = size(upTill(<N>))
hint: <H>
test: size(upTill(<N>)) == <?>



}
@javaClass{org.rascalmpl.library.Prelude}
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

Questions:

QType:
prep: import List;
make: L = list[arb[int,str],1,6]
test: top(<L>)

QValue:
prep: import List;
make: L = list[arb[int,str],1,6]
expr: H = top(<L>)
hint: <H>
test: top(<L>) == <?>

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

Questions:

QType:
prep: import List;
make: L = list[int]
test: toString(<L>)

QValue:
prep: import List;
make: L = list[int]
expr: H = toString(<L>)
hint: <H>
test: toString(<L>) == <?>
}
@javaClass{org.rascalmpl.library.Prelude}
public java str toString(list[&T] lst);


@doc{
Synopsis: Convert a list to an indented string.

Description:
Convert `lst` to a indented string.

Examples:
<screen>
import List;
itoString([10, 20, 30]);
itoString(["zebra", "elephant", "snake", "owl"]);
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java str itoString(list[&T] lst);


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

Questions:

QType:
prep: import List;
make: L = list[int, 4, 4]
make: M = list[int, 4, 4]
expr: Z = zip(<L>, <M>)
test: unzip(<Z>)



QValue:
prep: import List;
make: L = list[int, 4, 4]
make: M = list[int, 4, 4]
expr: Z = zip(<L>, <M>)
expr: H = unzip(<Z>)
hint: <H>
test: unzip(<Z>) == <?>

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

Questions:


QValue:
prep: import List;
make: L = list[int, 4, 4]
make: M = list[int, 4, 4]
expr: Z = zip(<L>,<M>)
hint: <Z>
test: zip(<L>, <M>) == <?>
}
public list[tuple[&T first, &U second]] zip(list[&T] a, list[&U] b) {
	if(size(a) != size(b))
		throw IllegalArgument(<size(a),size(b)>, "List size mismatch");
	return [<elementAt(a,i), elementAt(b,i)> | i <- index(a)];
}

public list[tuple[&T first, &U second, &V third]] zip(list[&T] a, list[&U] b, list[&V] c) {
	if(size(a) != size(b) || size(a) != size(c))
		throw IllegalArgument(<size(a),size(b),size(c)>, "List size mismatch");
	return [<elementAt(a,i), elementAt(b,i), elementAt(c,i)> | i <- index(a)];
}

@deprecated{use the indexing instead}
@javaClass{org.rascalmpl.library.Prelude}
public java &T elementAt(list[&T] lst, int index); 
