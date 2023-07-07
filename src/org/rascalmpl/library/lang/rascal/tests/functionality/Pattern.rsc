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
module lang::rascal::tests::functionality::Pattern
 
import List;

data F = f(int N) | f(int N, int M) | f(int N, value f, bool B) | g(str S);
data F1 = f1(int N, int M = 10, bool B = false) | f1(str S);
  
// matchLiteral
  
test bool matchLiteral1() = true     := true;
test bool matchLiteral2() = !(true    := false);
test bool matchLiteral3() = true     !:= false;
  
test bool matchLiteral4() = 1        := 1;
test bool matchLiteral5() = !(2       := 1);
test bool matchLiteral6() = 2        !:= 1;
  
test bool matchLiteral7() = 1.5      := 1.5;
test bool matchLiteral8() = !(2.5     := 1.5);
test bool matchLiteral9() = 2.5      !:= 1.5;
  		
test bool matchLiteral10() = !(1.0     := 1.5);
test bool matchLiteral11() = 1.0      !:= 1.5;
  
test bool matchLiteral12()=  "abc"  := "abc";
test bool matchLiteral13() = "def" !:= "abc";
test bool matchLiteral14()=  "def" !:= "abc";
  
//	matchADT
  
test bool matchADT1() = f(1)                   := f(1);
test bool matchADT2() = f(1, g("abc"), true) := f(1, g("abc"), true);
test bool matchADT3() = g("abc") !:= f(1);
test bool matchADT4() = f(1, 2)!:= f(1);
test bool matchADT5() = f(1, 2)!:= f(1);	
test bool matchADT6() = f(_):= f(1);
test bool matchADT7() = f(_,_):= f(1,2);
test bool matchADT8() = f(_,_,_):= f(1,2.5,true);
  
//	matchADTWithKeywords
  		
test bool matchADTwithKeywords1() = f1(1)                   := f1(1);
test bool matchADTwithKeywords2() = f1(1, M=10)             := f1(1);
test bool matchADTwithKeywords3() = f1(1, B=false, M=10)    := f1(1);
test bool matchADTwithKeywords4() = f1(1, M=20)             := f1(1, B=false, M=20);
  		
test bool matchADTwithKeywords5() = f1(1, M=X)             := f1(1, B=false, M=20) && X == 20;
  	
//	matchNode
  		
test bool matchNode1() ="f"(1)                := "f"(1);
test bool matchNode2() ="f"(1, "g"("abc"), true) := "f"(1, "g"("abc"), true);
test bool matchNode3() = "g"(1)               !:= "f"(1);

@ignoreInterpreter{
to be determined
}
test bool matchNode4() = !"g"(1)              := "f"(1);
test bool matchNode5() = "f"(1, 2)            !:= "f"(1);

@ignoreInterpreter{
to be determined
}
test bool matchNode6() = !"f"(1, 2)           := "f"(1);
test bool matchNode7() = "f"(1, 2)            !:= "f"(1, 2, 3);

@ignoreInterpreter{
to be determined
}
test bool matchNode8() = !"f"(1, 2)           := "f"(1, 2, 3);

test bool matchNode9() = "f"(_)                := "f"(1);
test bool matchNode10() = "f"(_,_)              := "f"(1,2);
test bool matchNode11() = "f"(_,_,_)            := "f"(1,2.5,true);
test bool matchNode12() = "f"(1,_,3)            := "f"(1,2,3);
test bool matchNode13() = "f"(_,2,_)            := "f"(1,2,3);
  
// matchNodeWithKeywords
  
test bool matchNodeWithKeywords1() ="f1"(1)                := "f1"(1);
  		
test bool matchNodeWithKeywords2() ="f1"(1)               !:= "f1"(2);
test bool matchNodeWithKeywords3() ="f1"(1, M=10)          := "f1"(1, M=10);
test bool matchNodeWithKeywords4() ="f1"(1)                := "f1"(1, M=10);
test bool matchNodeWithKeywords5() ="f1"(1, M=10)         !:= "f1"(1, M=20);
test bool matchNodeWithKeywords6() ="f1"(1, M=10)         !:= "f1"(1);
test bool matchNodeWithKeywords7() ="f1"(1, M=10)         !:= "f1"(1, B=false);
  		
test bool matchNodeWithKeywords8() ="f1"(1, B=false, M=10) := "f1"(1, M=10, B=false);
test bool matchNodeWithKeywords9() ="f1"(1, M=20, B=false) := "f1"(1, B=false, M=20);
test bool matchNodeWithKeywords10() ="f1"(1, M=20)          := "f1"(1, B=false, M=20);
test bool matchNodeWithKeywords11() ="f1"(1)                := "f1"(1, B=false, M=20);
test bool matchNodeWithKeywords12() ="f1"(1, B=false, M=10) !:= "f1"(1, M=20, B=false);
test bool matchNodeWithKeywords13() ="f1"(1, M=10, B=false)!:= "f1"(1, B=false, M=20);
test bool matchNodeWithKeywords14() ="f1"(1, M=_, B=false)  := "f1"(1, B=false, M=20);
test bool matchNodeWithKeywords15() ="f1"(_, M=20, B=false) := "f1"(1, B=false, M=20);
  		
test bool matchNodeWithKeywords16() = "f1"(1, M=X) := "f1"(1, B=false, M=20) && X == 20;
  
//	matchVariable

test bool matchVariable1() = (n1 := 1) && (n1 == 1);
test bool matchVariable2() {int n2 = 1;return  (n2 := 1) && (n2 == 1);}
test bool matchVariable3()  {int n3 = 1; return (n3 !:= 2) && (n3 == 1);}
  
test bool matchVariable4() = (f(n5) := f(1)) && (n5 == 1);
test bool matchVariable5() {int n6 = 1; return (f(n6) := f(1)) && (n6 == 1);}
  		
test bool matchVariable6() = f(_) := f(1);
  	
//	matchTypedVariableBecomes

test bool matchTypedVariableBecomes1() = int N : 3 := 3 && N == 3;
  	
//	matchVariableBecomes

test bool matchVariableBecomes1() = N : 3 := 3 && N == 3;
  
// variableBecomesEquality

@IgnoreCompiler{
TODO: fails, assignment to N is not undone
}        
test bool matchVariableBecomesEquality1() {int N = 5; return N : 3 !:= 3 && N != 3;}

test bool matchVariableBecomesEquality2() {int N = 3; return N : 3 := 3 && N == 3;}
  		
test bool doubleVariableBecomes1() = !(([N : 3, N : 4] := [3,4]) && N == 3);

test bool doubleVariableBecomes2() = [N : 3, N : 3] := [3,3] && N == 3;
  	
// antiPattern

test bool antiPattern1() = !4 := 3;
test bool antiPattern2() = !4 !:= 4;
test bool antiPattern3() = !(!4 := 4);
test bool antiPattern4() = !!4 := 4;
test bool antiPattern5() = !!4 !:= 3;
test bool antiPattern6() = !(!!4 := 3);
test bool antiPattern7() = (!(!3 := 3));	
test bool antiPattern8() = ![1,2,3] := [1,2,4];
test bool antiPattern9() = ![1,2,3] !:= [1,2,3];
test bool antiPattern10() = !(![1,2,3] := [1,2,3]);

test bool antiPattern11() = ![1,2] := [1,2,3];
@ignoreInterpreter{
to be determined
}
test bool antiPattern12() = ![1,2,3] := [1,2];

@ignoreInterpreter{
to be determined
}
test bool antiPattern13() = !{1,2,3} := {1,2,4};

test bool antiPattern14() = !{1,2,3} !:= {1,2,3};
test bool antiPattern15() = !(!{1,2,3} := {1,2,3});

test bool antiPattern16() = !{1,2} := {1,2,4};

@ignoreInterpreter{
to be determined
}
test bool antiPattern17() = !{1,2,3} := {1,2};

test bool antiPattern18() = !<1,2,3> := <1,2,4>;
test bool antiPattern19() = !<1,2,3> !:= <1,2,3>;
test bool antiPattern20() = !(!<1,2,3> := <1,2,3>);

@ignoreInterpreter{
to be determined
}
test bool antiPattern21() = !<1,2> := <1,2,4>;
@ignoreInterpreter{
to be determined
}
test bool antiPattern22() = !<1,2,3> := <1,2>;
  	
// Match in loops

test bool matchInLoop1(){
	lst = [1, 2, 3];
	cnt = 0;
	for(int x <- lst){
		switch(x){
			case int n: cnt += n;
		}
	}
	return cnt == (0 | it + x | x <- lst);
}

test bool matchInLoop2(){
	lst = [1, 2, 3];
	cnt = 0;
	i = 0;
	while(i < size(lst)){
		switch(lst[i]){
			case int n: cnt += n;
		}
		i += 1;
	}
	return cnt == (0 | it + x | x <- lst);
}
  	
test bool nodeMatchBacktracking() {
    y = for("f"({int a, int b, *int _}) := "f"({1,2,3,4})) append <a,b>; 
    return size(y) == 12;
}

test bool tupleMatchBacktracking() {
    y = for(<{int a, int b, *int _}> := <{1,2,3,4}>) append <a,b>; 
    return size(y) == 12;
}

 test bool switchListOnValue1() {
      value yy = []; switch(yy) { case [] : return true; default: return false; }
}

test bool switchSetOnValue1() {
	value yy = {}; switch(yy) { case {} : return true; default: return false; }
}
 

  
