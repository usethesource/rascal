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
module lang::rascal::tests::functionality::Backtracking
  
// testSimple
  	
test bool testSimple1() = int i <- [1,4] && int k := i && k >= 4;
test bool testSimple2() = int i <- [1,4] && int j <- [2,1] && int k := i + j && k >= 5;

// testRange
test bool testRange1() = int i <- [1..10] && int k := i && k >= 4;
test bool testRange2() = int i <- [1..10] && int j <- [2,1] && int k := i + j && k >= 5;
test bool testRange3() = int i <- [1,3..10] && int k := i && k >= 4;
test bool testRange4() = int i <- [1,3..10] && int j <- [2,1] && int k := i + j && k >= 5;
  	
// testList
  		
test bool testList1() = ([*int L1, int N, *int L2] := [1,2,3,4]) && (N == 3) && 
  					    (L1 == [1,2]) && (N == 3) && (L2 == [4]);
  		
test bool testList2() = ([*int L1, int N, *int L2] := [1,2,3,4]) && ((N == 3) || (N==4)) &&  
  		                (L1 == [1,2]) && (N == 3) && (L2 == [4]);
  		
test bool testList3() = ([*int _, int N, *int _] := [1,2,3,4]) && 
  			            ([*int _, int M, *int _] := [3,4]) && (N > M) &&  (N == 4);
  
test bool testList4() = [1, [*int _, int _, *int _], 3] := [1, [1,2,3,2], 3];
test bool testList5() = [*int _, int N, *int _]:= [1,2,3,2] && N > 1;
  		
test bool testList6() = [1, [*int _, int N, *int _], 3] := [1, [10,20], 3] && N > 10;
  	
// testSet
  		
test bool testSet1() = ({*int _, int N} := {1,2,3,4}) && (N == 1);
test bool testSet2() = ({*int _, int N} := {1,2,3,4}) && (N == 2);
test bool testSet3() = ({*int _, int N} := {1,2,3,4}) && (N == 3);
test bool testSet4() = ({*int _, int N} := {1,2,3,4}) && (N == 4);
  	
test bool testSet5() = ({*int _, int N, *int _} := {1,2,3,4}) && (N == 1);
test bool testSet6() = ({*int _, int N, *int _} := {1,2,3,4}) && (N == 2);
test bool testSet7() = ({*int _, int N, *int _} := {1,2,3,4}) && (N == 3);
test bool testSet8() = ({*int _, int N, *int _} := {1,2,3,4}) && (N == 4);
  		
test bool testSet9() = {1, {*int _, int _}, 3} := {1, {1,2,3}, 3};
test bool testSet10() = {*int _, int N}:= {1,2,3,2} && N == 1;
test bool testSet11() = {*int _, int N}:= {1,2,3,2} && N == 2;
test bool testSet12() = {*int _, int N}:= {1,2,3,2} && N == 3;
  		
test bool testSet13() = {1, {*int _, int _, *int _}, 3} := {1, {1,2,3}, 3};
test bool testSet14() = {1, {*int _, int N, *int _}, 3} := {1, {1,2,3}, 3} && N == 1;
test bool testSet15() = {1, {*int _, int N, *int _}, 3} := {1, {1,2,3}, 3} && N == 2;
test bool testSet16() = {1, {*int _, int N, *int _}, 3} := {1, {1,2,3}, 3} && N == 3;
 
// checking for issue #1197  	
test bool testSet17() = { <a,b> | {int a, int b} := {1,2} } == {<1,2>, <2,1>};
  	
// and
  
test bool and1() = int i <- [0, 1] && ["a","b"][i] == "a";
test bool and2() = int i <- [0, 1] && ["a","b"][i] == "b";

 
