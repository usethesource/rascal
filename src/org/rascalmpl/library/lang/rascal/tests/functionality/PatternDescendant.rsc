module lang::rascal::tests::functionality::PatternDescendant

data F = f(F left, F right) | g(int N);

// descendant1
 
test bool descendant1() = /int N := 1 && N == 1;
test bool descendant2() =  !/int _ := true;
        
test bool descendant3() = !(/int _ := []);
test bool descendant4() = /int N := [1] && N == 1;
  
test bool descendant5() = /int N := [1,2,3,2] && N > 2;

test bool descendant6a() = /4 := 4;
test bool descendant6b() = /4 !:= 3;
test bool descendant6c() = !/4 := 3;
test bool descendant6d() = !(/4 := 3);
test bool descendant6e() = !/4 !:= 4;
test bool descendant6f() = !(/4 !:= 4);
test bool descendant6g() = /!4 := 3;
test bool descendant6h() = /!4 !:= 4;
test bool descendant6i() = !/!4 !:= 3;
test bool descendant6j() = !(/!4 !:= 3);

test bool descendant7a() = !/4 := [1,2,3,2];
test bool descendant7b() = /!4 := [1,2,3,2];
@ignoreInterpreter{
TBD
}
test bool descendant7c() = /!4 !:= [1,2,3,4];
@ignoreInterpreter{
TBD
}
test bool descendant7d() = !/!4 := [1,2,3,4];

test bool descendant10() = /int N := (1 : 10) && (N == 1 || N == 10);
    
test bool descendant11() = !(/int _ := {});
test bool descendant12() = /int N := {1} && N == 1;
test bool descendant13() = /int N := {<false,1>} && N == 1;
        
test bool descendant14() = /int N := ("a" : 1) && N == 1;
test bool descendant15() = /int N := <"a", 1> && N == 1;
        
test bool descendant16() = [1, /int N, 3] := [1, [1,2,3,2], 3] && N == 1;
test bool descendant17() = [1, /int N, 3] := [1, [1,2,3,2], 3] && N == 2;

test bool descendant18() = <1, /int N, 3> := <1, [1,2,3,2], 3> && N == 1;
test bool descendant19() = <1, /int N, 3> := <1, [1,2,3,2], 3> && N == 2;

// descendant2
 
data RECT = rect(int w, int h, str color = "white");
 
test bool descendant21() = [n | /int n <- [1,2,3]] == [1,2,3];
test bool descendant22() = [b | /bool b <- [true,false,true]] == [true,false,true];
test bool descendant23() = [s | /str s <- ["a","b"]] == ["a","b"];
        
test bool descendant24() = {n | /int n <- {1,2,3}} == {1,2,3};
test bool descendant25() = {n | /int n <- {<1,2,3>}} == {1,2,3};
test bool descendant26() = {v | /value v <- {<1,"b",true>}} == {1,"b",true, <1,"b",true>}; 

@ignoreInterpreter{
Not implemented
}
test bool descendant27() = {n | /int n := [1, "f"(2, kw1=3, kw2=4), 5]}  == {1,2,3,4,5};
@ignoreInterpreter{
Not implemented
} 
test bool descendant28() = {s | /str s := [1, rect(10,20), 5, rect(30,40,color="red")]}  == {"red"};
    

// descendant3

test bool descendant30() = /g(2) := f(g(1),f(g(2),g(3)));
test bool descendant31() = [1, /g(2), 3] := [1, f(g(1),f(g(2),g(3))), 3];
test bool descendant32() = [1, !/g(5), 3] := [1, f(g(1),f(g(2),g(3))), 3];

@ignoreCompiler{
FIXME: Typechecker: missing constraints
}	
test bool descendant33() = [1, [F] /f(/g(2), F _), 3] := [1, f(g(1),f(g(2),g(3))), 3];
test bool descendant34() = [1, /f(/g(2),/g(3)), 3] := [1, f(g(1),f(g(2),g(3))), 3];
  		
test bool descendant35() = [1, F outer: /f(/F inner: g(2), _), 3] := [1, f(g(1),f(g(2),g(3))), 3] && outer == f(g(1),f(g(2),g(3))) && inner == g(2);
  			
test bool descendant36() = [1, /g(int N1), 3] := [1, f(g(1),f(g(2),g(3))), 3] && N1 == 1;
test bool descendant37() = [1, /g(int N2), 3] := [1, f(g(1),f(g(2),g(3))), 3] && N2 == 2;
test bool descendant38() = [1, /g(int N3), 3] := [1, f(g(1),f(g(2),g(3))), 3] && N3 == 3;

data A = a(int i);
data B = b(str s);

data C = c (A x, B y);

test bool descendant40() = [ n | /int n := [c(a(3), b("a")), c(a(4), b("b"))] ] == [3, 4];
test bool descendant41() = [ n | /a(int n) := [c(a(3), b("a")), c(a(4), b("b"))] ] == [3, 4];
test bool descendant42() = [ s | /str s := [c(a(3), b("a")), c(a(4), b("b"))] ] == ["a", "b"];
test bool descendant43() = [ s | /b(str s) := [c(a(3), b("a")), c(a(4), b("b"))] ] == ["a", "b"];

data D = d(map[A,B] ab);

test bool descendant50() = {n | /int n := [d((a(1) : b("one"), a(2) : b("two"))), d((a(10) : b("ten"), a(20) : b("twenty")))] } == {1, 2, 10, 20};
test bool descendant51() = {n | /a(int n) := [d((a(1) : b("one"), a(2) : b("two"))), d((a(10) : b("ten"), a(20) : b("twenty")))] } == {1, 2, 10, 20};
test bool descendant52() = {s | /str s := [d((a(1) : b("one"), a(2) : b("two"))), d((a(10) : b("ten"), a(20) : b("twenty")))] } == {"one", "two", "ten", "twenty"};
test bool descendant53() = {s | /b(str s) := [d((a(1) : b("one"), a(2) : b("two"))), d((a(10) : b("ten"), a(20) : b("twenty")))] } == {"one", "two", "ten", "twenty"};

data NODE = nd(NODE lhs, NODE rhs) | leaf(int n);

test bool descendant60(){
    nd1 = "leaf"(1);
    nd2 = "nd"(nd1,"leaf"(2));
    nd3 = "nd"("leaf"(3),"leaf"(4));
    nd4 = "nd"(nd2,nd3);
    return { v | /v:"nd"(node _, "leaf"(int _)) <- "nd"(nd4,"leaf"(0)) }==
           {"nd"("leaf"(1),"leaf"(2)),"nd"("leaf"(3),"leaf"(4))};
   }
   
test bool descendant61() {
    cnd1 = leaf(1);
    cnd2 = nd(cnd1,leaf(2));
    cnd3 = nd(leaf(3),leaf(4));
    cnd4 = nd(cnd2,cnd3);
    
    return { v | /v: nd(NODE _, leaf(int _))    <-  nd(cnd4,leaf(0)) } ==
           {nd(leaf(3), leaf(4)), nd(leaf(1),leaf(2))};        
}

test bool descendant62() {
    nd1 = "leaf"(1);
    nd2 = "nd"(nd1,"leaf"(2));
    nd3 = "nd"("leaf"(3),"leaf"(4));
    nd4 = "nd"(nd2,nd3);

    return [ v | /v:"nd"(node _, "leaf"(int _)) <- "nd"(nd4,"leaf"(0)) ] ==
           ["nd"("leaf"(1), "leaf"(2)),"nd"("leaf"(3),"leaf"(4))];
}

test bool descendant63() {
    cnd1 = leaf(1);
    cnd2 = nd(cnd1,leaf(2));
    cnd3 = nd(leaf(3),leaf(4));
    cnd4 = nd(cnd2,cnd3);
    
    return [ v | /v: nd(NODE _, leaf(int _))    <-  nd(cnd4,leaf(0)) ] ==
           [ nd(leaf(1), leaf(2)), nd(leaf(3), leaf(4)) ];
}

test bool descendant64() {
    n = 0;
    for(/_:[*value _] := [ 1, [2], [3,[4,6,[7]]], [[8,[9]],[[[10]]]] ]) {
        n += 1;
    }
    return n == 11;
}

test bool descendant65() {
    n = 0;
    for(/1 := [1,2,3,[1,2,3,[1,2,3]]]) {
        n += 1;
    }
    return n == 3;
}

test bool descendant66() {
	return [ v | /v:<value _, int _> <- <<<1,2>,<3,4>>,0> ] == [<1,2>, <3,4>];
}

