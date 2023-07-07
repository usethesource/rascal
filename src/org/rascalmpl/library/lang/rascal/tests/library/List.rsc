@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl - CWI}
@contributor{Vadim Zaytsev - vadim@grammarware.net - UvA}
module lang::rascal::tests::library::List
 
import Exception;
import List;

// concat

//concat1 violates stricter typing rules of compiler
//test bool concat1() = concat([]) == [];
test bool concat2() = concat([[]]) == [];
test bool concat3() = concat(concat([[[]]])) == [];
test bool concat4() = concat([[1]]) == [1];
test bool concat5() = concat([[1],[],[2,3]]) == [1,2,3];
test bool concat6() = concat([[1,2],[3],[4,5],[]]) == [1,2,3,4,5];

// delete
test bool delete1() = delete([0,1,2], 0) == [1,2];
test bool delete2() = delete([0,1,2], 1) == [0,2];
test bool delete3() = delete([0,1,2], 2) == [0,1];
@expected{
IndexOutOfBounds
} test bool delete4() { delete([0,1,2], 3); return false; } 
  		
// distribution
test bool distribution1()  = distribution([]) == ();
test bool distribution2()  = distribution([1]) == (1:1);
test bool distribution3()  = distribution([1,2]) == (1:1, 2:1);
test bool distribution4()  = distribution([1,2,2]) == (1:1, 2:2);
test bool distribution5()  = distribution([2,2,2]) == (2:3);
test bool distribution6()  = distribution([[]]) == ([]:1);

// drop
test bool drop1()  = drop(0,[])    == [];
test bool drop2()  = drop(0,[1])   == [1];
test bool drop3()  = drop(0,[1,2]) == [1,2];
test bool drop4()  = drop(1,[])    == [];
test bool drop5()  = drop(1,[1])   == [];
test bool drop6()  = drop(1,[1,2]) == [2];

// dup
test bool dup1()  = dup([])        == [];
test bool dup2()  = dup([1])       == [1];
test bool dup3()  = dup([1,2])     == [1,2];
test bool dup4()  = dup([1,1])     == [1];
test bool dup5()  = dup([1,1,2])   == [1,2];
test bool dup6()  = dup([1,1,2,2]) == [1,2];

// elementAt - deprecated!
@ignoreCompiler{
Remove-after-transtion-to-compiler: Other exception
} @expected{
NoSuchElement
} test bool elementAt1() {[][0]; return false;}
@ignoreInterpreter{
Other exception
} @expected{
IndexOutOfBounds
} test bool elementAt1() {[][0]; return false;}
test bool elementAt2()  = [1,2,3][0] == 1;
test bool elementAt3()  = [1,2,3][1] == 2;
test bool elementAt4()  = [1,2,3][2] == 3;
@expected{
IndexOutOfBounds
} test bool elementAt5() {[1,2,3][3]; return false;}
test bool elementAt6()  = [1,2,3][-1] == 3;
test bool elementAt7()  = [1,2,3][-2] == 2;
test bool elementAt8()  = [1,2,3][-3] == 1;
@expected{
IndexOutOfBounds
} test bool elementAt9() {[1,2,3][-4]; return false;}

// getOneFrom
@expected{
EmptyList
} test bool getOneFrom1() {getOneFrom([]); return false;} 
test bool getOneFrom2() {int N = getOneFrom([1]); return N == 1;}
test bool getOneFrom3() {int N = getOneFrom([1,2]); return  (N == 1) || (N == 2);}
test bool getOneFrom4() {int N = getOneFrom([1,2,3]); return  (N == 1) || (N == 2) || (N == 3);}
test bool getOneFrom5() {real D = getOneFrom([1.0,2.0]); return  (D == 1.0) || (D == 2.0);}
test bool getOneFrom6() {str S = getOneFrom(["abc","def"]); return  (S == "abc") || (S == "def");}
  
// head/1
@expected{
EmptyList
} test bool head1a() {head([]);return false;}
test bool head2a() = head([1]) == 1;
test bool head3a() = head([1, 2]) == 1;

// head/2
test bool head1b() = head([1, 2, 3, 4], 0) == [];
test bool head2b() = head([1, 2, 3, 4], 1) == [1];
test bool head3b() = head([1, 2, 3, 4], 2) == [1,2];
test bool head4b() = head([1, 2, 3, 4], 3) == [1,2,3];
test bool head5b() = head([1, 2, 3, 4], 4) == [1,2,3,4];
@expected{
IndexOutOfBounds
} test bool head6b() {head([],1);return false;}
@expected{
IndexOutOfBounds
} test bool head7b() {head([],3);return false;}
@expected{
IndexOutOfBounds
} test bool head8b() {head([1,2],3);return false;}
@expected{
IndexOutOfBounds
} test bool head9b() {head([],-1);return false;}

// headTail - see pop

// index
test bool index1() = index([]) == [];
test bool index2() = index([1]) == [0];
test bool index3() = index([1,2]) == [0,1];
test bool index4() = index([10..1]) == [0..9];

// indexOf
test bool indexOf1() = indexOf([],1) == -1;
test bool indexOf2() = indexOf([1],1) == 0;
test bool indexOf3() = indexOf([1,2],1) == 0;
test bool indexOf4() = indexOf([2,1],1) == 1;
test bool indexOf5() = indexOf([1,2,1],1) == 0;

// insertAt  
test bool insertAt1() = insertAt([], 0, 1) == [1];
@expected{
IndexOutOfBounds
} test bool insertAt2() {insertAt([], 1, 1) == [1]; return false;}
test bool insertAt3() = insertAt([2,3], 0, 1) == [1, 2, 3];
test bool insertAt4() = insertAt([2,3], 1, 1) == [2, 1, 3];
test bool insertAt5() = insertAt([2,3], 2, 1) == [2, 3, 1];

// intercalate
test bool intercalate1()  = intercalate(",", []) == "";
test bool intercalate2()  = intercalate("!", [1]) == "1";
test bool intercalate3()  = intercalate(",", [1,2]) == "1,2";

// intersperse
test bool intersperse1()  = intersperse(0, []) == [];
test bool intersperse2()  = intersperse(0, [1]) == [1];
test bool intersperse3()  = intersperse(0, [1,2]) == [1,0,2];

// isEmpty
test bool isEmpty1()  = isEmpty([]);
test bool isEmpty2()  = !isEmpty([0]);
test bool isEmpty3()  = !isEmpty([1,2]);

// last
@expected{
EmptyList
} test bool last1() {last([]);return false;}
test bool last2() = last([1]) == 1;
test bool last3() = last([1, 2]) == 2;

// lastIndexOf
test bool lastIndexOf1() = lastIndexOf([],1) == -1;
test bool lastIndexOf2() = lastIndexOf([1],1) == 0;
test bool lastIndexOf3() = lastIndexOf([1,2],1) == 0;
test bool lastIndexOf4() = lastIndexOf([2,1],1) == 1;
test bool lastIndexOf5() = lastIndexOf([1,2,1],1) == 2;

// mapper 
test bool mapper1() = mapper([], int (int n) {return n*10;}) == [];
test bool mapper2() = mapper([1,2], int (int n) {return n;}) == [1,2];
test bool mapper3() = mapper([1,2,3], int (int n) {return n+1;}) == [2,3,4];
  
// max
@expected{
EmptyList
} test bool max1() {max([]); return false;}
test bool max2() = max([1, 1, 1]) == 1;
test bool max3() = max([1, 2, 3, 2, 1]) == 3;
test bool max4() = max([-1, -2, -3]) == -1;

// merge
test bool merge1() = merge([],[]) == [];
test bool merge2() = merge([1],[]) == [1];
test bool merge3() = merge([],[2]) == [2];
test bool merge4() = merge([1],[2]) == [1,2];
test bool merge5() = merge([2],[2]) == [2,2];
test bool merge6() = merge([3],[2]) == [2,3];
  	
// min
@expected{
EmptyList
} test bool min1() {min([]); return false;}
test bool min2() = min([1, 1, 1]) == 1;
test bool min3() = min([1, 2, 3, 2, 1]) == 1;
test bool min4() = min([-1, -2, -3]) == -3;

// mix
test bool mix1() = mix([],[]) == [];
test bool mix2() = mix([],[1]) == [1];
test bool mix3() = mix([],[1,2]) == [1,2];
test bool mix4() = mix([1],[]) == [1];
test bool mix5() = mix([1,2],[]) == [1,2];
test bool mix6() = mix([1,3],[2]) == [1,2,3];
test bool mix7() = mix([1,3],[2,4]) == [1,2,3,4];

// permutations
test bool permutations1()  = permutations([]) == {[]};
test bool permutations2()  = permutations([1]) == {[1]};
test bool permutations3()  = permutations([1,2]) == {[1,2],[2,1]};
test bool permutations4()  = permutations([1,2,3]) ==  {[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]};
test bool permutations5()  = permutations([1,1]) == {[1,1]};
test bool permutations6()  = permutations([1,1,1]) == {[1,1,1]};

// pop
@expected{
EmptyList
} test bool pop1() {pop([]);return false;}
test bool pop2() = pop([1]) == <1,[]>;
test bool pop3() = pop([1,2]) == <1,[2]>;
test bool pop4() = pop([1,2,3]) == <1,[2,3]>;

// prefix
test bool prefix1() = prefix([])    == [];
test bool prefix2() = prefix([1])   == [];
test bool prefix3() = prefix([1,2]) == [1];
test bool prefix4() = prefix(prefix([1,2])) == [];

// push
test bool push1() = push(0,[]) == [0];
test bool push2() = push(1,[2]) == [1,2];
test bool push3() = push(1,[2,3]) == [1,2,3];

// reducer - deprecated!

// remove
test bool remove1() = remove([],0) == [];
test bool remove2() = remove([1],0) == [];
test bool remove3() = remove([1,2],0) == [2];
test bool remove4() = remove([1,2],1) == [1];
test bool remove5() = remove([1,2],2) == [1,2];
test bool remove6() = remove([1,2],-1) == [1,2];
test bool remove7() = remove([1],-1) == [1];
test bool remove8() = remove([],0) == [];

// reverse
test bool reverse1() = reverse([]) == [];
test bool reverse2() = reverse([1]) == [1];
test bool reverse3() = reverse([1,1]) == [1,1];
test bool reverse4() = reverse([1,2]) == [2,1];
test bool reverse5() = reverse([1,2,3]) == [3,2,1];
  
// size
test bool size1() = 0 == size([]);
test bool size2() = 1 == size([1]);
test bool size3() = 2 == size([1,2]);
test bool size4() = 3 == size([1,2,3]);
  	
// slice
test bool slice1() = slice([1,2,3,4], 0, 0) == [];
test bool slice2() = slice([1,2,3,4], 0, 1) == [1];
test bool slice3() = slice([1,2,3,4], 0, 2) == [1,2];
test bool slice4() = slice([1,2,3,4], 0, 3) == [1,2,3];
test bool slice5() = slice([1,2,3,4], 0, 4) == [1,2,3,4];
test bool slice6() = slice([1,2,3,4], 1, 0) == [];
test bool slice7() = slice([1,2,3,4], 1, 1) == [2];
test bool slice8() = slice([1,2,3,4], 1, 2) == [2,3];
test bool slice9() = slice([1,2,3,4], 3, 0) == [];
test bool slice10() = slice([1,2,3,4], 3, 1) == [4];

// sort/1
test bool sort1a() = sort([]) == [];
test bool sort1b() = sort([1]) == [1];
test bool sort1c() = sort([1, 2]) == [1,2];
test bool sort1d() = sort([2, 1]) == [1,2];
test bool sort1e() = sort([2,-1,4,-2,3]) == [-2,-1,2,3,4];
test bool sort1f() = sort([1,2,3,4,5,6]) == [1,2,3,4,5,6];
test bool sort1g() = sort([1,1,1,1,1,1]) == [1,1,1,1,1,1];
test bool sort1h() = sort([1,1,0,1,1]) == [0,1,1,1,1];
test bool sort1i() = sort(["mango", "strawberry", "pear", "pineapple", "banana", "grape", "kiwi"]) == ["banana","grape","kiwi","mango","pear","pineapple","strawberry"];

// sort/2
test bool sort2a() = sort([1,2,3], bool(int a, int b){return a < b;}) == [1,2,3];
test bool sort2b() = sort([1,3,2], bool(int a, int b){return a < b;}) == [1,2,3];
test bool sort2c() = sort([1,3,2], bool(int a, int b){return a > b;}) == [3,2,1];
test bool sort2d() = sort([3,2,1], bool(int a, int b){return a > b;}) == [3,2,1];
@expected{
IllegalArgument
} test bool sort2e() {sort([1,2,3], bool(int a, int b){return a <= b;}); return false;}
@expected{
IllegalArgument
} test bool sort2f() {sort([1,2,3], bool(int a, int b){return a >= b;}); return false;}

bool less(int a, int b) = a < b;
bool lesseq(int a, int b) = a <= b;
bool greater(int a, int b) = a > b;
bool greatereq(int a, int b) = a  >= b;

test bool sort2g() = sort([1,2,3], less) == [1,2,3];
test bool sort2h() = sort([1,3,2], less) == [1,2,3];
test bool sort2i() = sort([1,3,2], greater) == [3,2,1];
test bool sort2j() = sort([3,2,1], greater) == [3,2,1];
@expected{
IllegalArgument
} test bool sort2k() {sort([1,2,3], lesseq); return false;}
@expected{
IllegalArgument
} test bool sort2l() {sort([1,2,3], greatereq); return false;}


test bool shuffleFirstIndex(list[value] v) = v == [] || ({ shuffle(v)[0] | _ <- [0..50 * size(v)]} == {*v});
test bool shuffleLastIndex(list[value] v) = v == [] || ({ shuffle(v)[size(v) - 1] | _ <- [0..50 * size(v)]} == {*v});
test bool shuffleStable(list[value] v, int x) = v == [] || shuffle(v, x) == shuffle(v, x);

// split
test bool split1() = split([]) == <[],[]>;
test bool split2() = split([1]) == <[],[1]>;
test bool split3() = split([1,2]) == <[1],[2]>;
test bool split4() = split([1,2,3]) == <[1],[2,3]>;
test bool split5() = split([1,2,3,4]) == <[1,2],[3,4]>;

// sum
test bool sum1() = sum([0]) == 0;
test bool sum2() = sum([1]) == 1;
test bool sum3() = sum([1,2]) == 3;
test bool sum4() = sum([1,2,3]) == 6;
@expected{
EmptyList
} test bool sum5() {sum([]); return false;}

// tail/1
@expected{
EmptyList
} test bool tail1a() {tail([]);return false;}
test bool tail2a() = tail([1]) == [];
test bool tail3a() = tail([1, 2]) == [2];
test bool tail4a() = tail(tail([1, 2])) == [];

// tail/2
test bool tail1b() = tail([1, 2, 3, 4], 0) == [];
test bool tail2b() = tail([1, 2, 3, 4], 1) == [4];
test bool tail3b() = tail([1, 2, 3, 4], 2) == [3,4];
test bool tail4b() = tail([1, 2, 3, 4], 3) == [2,3,4];
test bool tail5b() = tail([1, 2, 3, 4], 4) == [1,2,3,4];
@expected{
IndexOutOfBounds
} test bool tail6b() {tail([],1);return false;}
@expected{
IndexOutOfBounds
} test bool tail7b() {tail([],3);return false;}
@expected{
IndexOutOfBounds
} test bool tail8b() {tail([1,2],3);return false;}
@expected{
IndexOutOfBounds
} test bool tail9b() {tail([],-1);return false;}
  	
// takeOneFrom
test bool takeOneFrom1() {<E, L> = takeOneFrom([1]); return (E == 1) && (L == []);}
test bool takeOneFrom2() {<E, L> = takeOneFrom([1,2]); return ((E == 1) && (L == [2])) || ((E == 2) && (L == [1]));}
@expected{
EmptyList
} test bool takeOneFrom3() {takeOneFrom([]); return false;}

// takeWhile
test bool takeWhile1() = takeWhile([],bool(int x){ return x mod 2 == 0;}) == [];
test bool takeWhile2() = takeWhile([1,2],bool(int x){ return x mod 2 == 0;}) == [];
test bool takeWhile3() = takeWhile([2,1],bool(int x){ return x mod 2 == 0;}) == [2]; 
test bool takeWhile4() = takeWhile([5..-5],bool(int x){ return x < 0;}) == []; 
test bool takeWhile5() = takeWhile([5..-5],bool(int x){ return x > 0;}) == [5..0]; 
test bool takeWhile6() = takeWhile([-20..20],bool(int x){ return x < 0;}) == [-20..0]; 
test bool takeWhile7() = takeWhile([-20..20],bool(int x){ return x > 0;}) == []; 

// toMap
test bool toMap1() = toMap([]) == ();
test bool toMap2() = toMap([<1,10>, <2,20>]) == (1:[10], 2:[20]);
test bool toMap3() = toMap([<1,10>, <2,20>, <1,30>]) == (1:[10,30], 2:[20]);

// toMapUnique
test bool toMapUnique1() = toMapUnique([]) == ();
test bool toMapUnique2() = toMapUnique([<1,10>, <2,20>]) == (1:10, 2:20);
@expected{
MultipleKey
} test bool toMapUnique3() {toMapUnique([<1,10>, <1,20>]); return false;}

// top - see head

// toRel
test bool toRel1() = toRel([]) == {};
test bool toRel2() = toRel([1]) == {};
test bool toRel3() = toRel([1,2]) == {<1,2>};
test bool toRel4() = toRel([1,2,3]) == {<1,2>, <2,3>};
test bool toRel5() = toRel([1,2,3,4]) == {<1,2>, <2,3>, <3,4>};

// toSet
test bool toSet1() = toSet([]) == {};
test bool toSet2() = toSet([1]) == {1};
test bool toSet3() = toSet([1,2]) == {1, 2};
test bool toSet4() = toSet([1,2,1]) == {1, 2};
  	
// toString  
test bool toString1() = toString([]) == "[]";
test bool toString2() = toString([1]) == "[1]";
test bool toString3() = toString([1, 2]) == "[1,2]";

// itoString  
test bool itoString1() = itoString([]) == "[]";
test bool itoString2() = itoString([1]) == "[1]";
test bool itoString3() = itoString([1, 2]) == "[1,2]";
test bool itoString4() = itoString([1, [], 2]) == "[\n  1,\n  [],\n  2\n]";
  	
// listExpressions
 test bool listExpressions() { 
	value n = 1; 
	value s = "string"; 
	return list[int] _ := [ n ] && 
	list[str] _ := [ s, s, *[ s, s ] ]; 
}

// Tests related to the correctness of the dynamic types of lists produced by the library functions;
// incorrect dynamic types make pattern matching fail;
  
// testDynamicTypes
test bool dynamicTypes1() { list[value] lst = ["1",2,3]; return list[int] _ := slice(lst, 1, 2); }
test bool dynamicTypes2() { list[value] lst = ["1",2,3]; return list[int] _ := lst - "1"; }
test bool dynamicTypes3() { list[value] lst = ["1",2,3]; return list[int] _ := lst - ["1"]; }
test bool dynamicTypes4() { list[value] lst = ["1",2,3]; return  list[int] _ := delete(lst, 0); }
test bool dynamicTypes5() { list[value] lst = ["1",2,3]; return  list[int] _ := drop(1, lst); }
test bool dynamicTypes6() { list[value] lst = [1,2,"3"]; return  list[int] _ := head(lst, 2); }
test bool dynamicTypes7() { list[value] lst = [1,2,"3"]; return  list[int] _ := prefix(lst); }
test bool dynamicTypes8() { list[value] lst = ["1",2,3]; return  list[int] _ := tail(lst); }
test bool dynamicTypes9() { list[value] lst = [1,2,"3"]; return  list[int] _ := take(2, lst); }	
test bool dynamicTypes10() { return [str _, *int _] := ["1",2,3]; }
  	
 
