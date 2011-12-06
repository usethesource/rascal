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

@doc{Get n elements from the head of the list, or size(l) if size(l) < n}
@javaClass{org.rascalmpl.library.List}
public java list[&T] take(int n, list[&T] l);

@doc{Drop n elements from the head of the list, or size(l) if size(l) < n}
@javaClass{org.rascalmpl.library.List}
public java list[&T] drop(int n, list[&T] l) ;
	
@doc{Get n elements from the head of the list, size(l) if size(l) < n}
public list[&T] mix(list[&T] l, list[&T] r){
	return [l[i],r[i]| i <- [0 .. (min(size(l),size(r)) - 1)]] + drop(size(r),l) + drop(size(l),r);
}

@doc{Delete nth element from list}
@javaClass{org.rascalmpl.library.List}
public java list[&T] delete(list[&T] lst, int n);

@doc{A set of all legal index values for a list}
@javaClass{org.rascalmpl.library.List}
public java set[int] domain(list[&T] lst);

@doc{Get the first element of a list}
@javaClass{org.rascalmpl.library.List}
public java &T head(list[&T] lst) throws EmptyList;

@doc{Return the last element of a list, if any}
public &T last(list[&T] lst) throws EmptyList {
  if ([list[&T] p, &T l] := lst) 
    return l;
  throw EmptyList();
}

@doc{Remove multiple occurrences of elements in a list. The first occurrence remains.}
public list[&T] dup(list[&T] lst) {
  done = {};
  return for (e <- lst, e notin done) {
    done += e;
    append e;
  }
}

@doc{Return all but the last element of a list}
@javaClass{org.rascalmpl.library.List}
public java list[&T] prefix(list[&T] lst) ;

@doc{Get the first n elements of a list}
@javaClass{org.rascalmpl.library.List}
public java list[&T] head(list[&T] lst, int n) throws IndexOutOfBounds;

@doc{Get an arbitrary element from a list}
@javaClass{org.rascalmpl.library.List}
public java &T getOneFrom(list[&T] lst);

@doc{Get the indices of a list}
public list[int] index(list[&T] lst) = upTill(size(lst));

@doc{Returns the list 0,1..n-1, this is slightly faster than [0..n], since the returned values are shared}
@javaClass{org.rascalmpl.library.List}
public java list[int] upTill(int n);

@doc{Add an element at a specific position in a list}
@javaClass{org.rascalmpl.library.List}
public java list[&T] insertAt(list[&T] lst, int n, &T elm) throws IndexOutOfBounds;
 
@doc{Is list empty?}
@javaClass{org.rascalmpl.library.List}
public java bool isEmpty(list[&T] lst);

@doc{Apply a function to each element of a list}
public list[&U] mapper(list[&T] lst, &U (&T) fn) =  [fn(elm) | &T elm <- lst];

@doc{Largest element of a list}
public &T max(list[&T] lst) =
	(head(lst) | max(e,it) | e <- tail(lst));

@doc{Smallest element of a list}
public &T min(list[&T] lst) =
	(head(lst) | min(e,it) | e <- tail(lst));

@doc{Return all permutations of a list}
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

@doc{Apply function F to successive elements of a list}
public &T reducer(list[&T] lst, &T (&T, &T) fn, &T unit)
{
  &T result = unit;
  for(&T elm <- lst){
     result = fn(result, elm);
  }
  return result;
}

@doc{Elements of a list in reverse order}
@javaClass{org.rascalmpl.library.List}
public java list[&T] reverse(list[&T] lst);

@doc{Number of elements in a list}
@javaClass{org.rascalmpl.library.List}
public java int size(list[&T] lst);

@doc{Sublist from start of length len}
@javaClass{org.rascalmpl.library.List}
public java list[&T] slice(list[&T] lst, int begin, int len);

@doc{Merge the elements of two sorted lists into one list}
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

@doc{Split a list into two halves}
public tuple[list[&T],list[&T]] split(list[&T] l) {
	half = size(l)/2;
	return <take(half,l), drop(half,l)>;
}

@doc{Sort the elements of a list}
public list[&T] sort(list[&T] l, bool (&T a, &T b) lessOrEqual) {
	if(size(l) <= 1) {
		return l;
	} else {
		<left,right> = split(l);
		return merge(sort(left,lessOrEqual),sort(right,lessOrEqual),lessOrEqual);
	}
}

@doc{Sort the elements of a list}
public list[&T] sort(list[&T] lst) =
	sort(lst, bool (&T a,&T b) { return a <= b; } );

@doc{Join list of values into string separated by sep}
public str intercalate(str sep, list[value] l) = 
	(l == []) ? "" : ( "<head(l)>" | it + "<sep><x>" | x <- tail(l) );


@doc{All but the first element of a list}
@javaClass{org.rascalmpl.library.List}
public java list[&T] tail(list[&T] lst);
 
@doc{Last n elements of a list}
@javaClass{org.rascalmpl.library.List}
public java list[&T] tail(list[&T] lst, int len) throws IndexOutOfBoundsError;
 
@doc{Remove an arbitrary element from a list, returns the element and the modified list}
@javaClass{org.rascalmpl.library.List}
public java tuple[&T, list[&T]] takeOneFrom(list[&T] lst);

public tuple[&T, list[&T]] headTail(list[&T] lst) throws EmptyList {
  if ([&T h, list[&T] t] := lst)
    return <h, t>;
  throw EmptyList();
}

public tuple[&T, list[&T]] pop(list[&T] lst) throws EmptyList =
  headTail(lst);

public &T top(list[&T] lst) throws EmptyList = head(lst);

public list[&T] push(&T elem, list[&T] lst) = [elem] + lst;

@doc{Convert a list of tuples to a map; first elements are associated with a set of second elements}
@javaClass{org.rascalmpl.library.List}
public java map[&A,set[&B]] toMap(list[tuple[&A, &B]] lst) throws DuplicateKey;

@doc{Convert a list of tuples to a map; result must be a map}
@javaClass{org.rascalmpl.library.List}
public java map[&A,&B] toMapUnique(list[tuple[&A, &B]] lst) throws DuplicateKey;

@doc{Convert a list to a set}
@javaClass{org.rascalmpl.library.List}
public java set[&T] toSet(list[&T] lst);

@doc{
  Convert a list to relation, where each tuple encodes which elements are followed by each other.
  This function will return an empty relation for empty lists and for singleton lists
}
public rel[&T,&T] toRel(list[&T] lst) {
  return { <from,to> | [_*, &T from, &T to, _*] := lst };
}

@doc{Convert a list to a string}
@javaClass{org.rascalmpl.library.List}
public java str toString(list[&T] lst);

@doc{Return index of first occurence of elt in lst, or -1 if elt is not found.}
public int indexOf(list[&T] lst, &T elt) {
	for(i <- index(lst)) {
		if(lst[i] == elt) return i;
	}
	return -1;
}

@doc{Return index of first occurence of elt in lst, or -1 if elt is not found.}
public int lastIndexOf(list[&T] lst, &T elt) {
	for(i <- reverse(index(lst))) {
		if(lst[i] == elt) return i;
	}
	return -1;
}

@doc{Make a list of pairs from two lists of the same length.}
public list[tuple[&T first, &U second]] zip(list[&T] a, list[&U] b) {
	if(size(a) != size(b))
		throw IllegalArgument(<size(a),size(b)>, "List size mismatch");
	return [<a[i], b[i]> | i <- index(a)];
}

@doc{Make a list of triples from three lists of the same length.}
public list[tuple[&T first, &U second, &V third]] zip(list[&T] a, list[&U] b, list[&V] c) {
	if(size(a) != size(b) || size(a) != size(c))
		throw IllegalArgument(<size(a),size(b),size(c)>, "List size mismatch");
	return [<a[i], b[i], c[i]> | i <- index(a)];
}

@doc{Make a pair of lists from a list of pairs.}
public tuple[list[&T],list[&U]] unzip(list[tuple[&T,&U]] lst) =
	<[t | <t,_> <- lst], [u | <_,u> <- lst]>;

@doc{Make a triple of lists from a list of triples.}
public tuple[list[&T],list[&U],list[&V]] unzip(list[tuple[&T,&U,&V]] lst) =
	<[t | <t,_,_> <- lst], [u | <_,u,_> <- lst], [w | <_,_,w> <- lst]>;

