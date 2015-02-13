module lang::rascal::tests::functionality::PatternDescendantTests

data F = f(F left, F right) | g(int N);

// descendant2

test bool descendant15() = /g(2) := f(g(1),f(g(2),g(3)));
test bool descendant16() = [1, /g(2), 3] := [1, f(g(1),f(g(2),g(3))), 3];
test bool descendant17() = [1, !/g(5), 3] := [1, f(g(1),f(g(2),g(3))), 3];
  		
test bool descendant18() = [1, [F] /f(/g(2), F _), 3] := [1, f(g(1),f(g(2),g(3))), 3];
test bool descendant19() = [1, /f(/g(2),/g(3)), 3] := [1, f(g(1),f(g(2),g(3))), 3];
  		
test bool descendant() = [1, F outer: /f(/F inner: g(2), _), 3] := [1, f(g(1),f(g(2),g(3))), 3] && outer == f(g(1),f(g(2),g(3))) && inner == g(2);
  			
test bool descendant20() = [1, /g(int N1), 3] := [1, f(g(1),f(g(2),g(3))), 3] && N1 == 1;
test bool descendant21() = [1, /g(int N2), 3] := [1, f(g(1),f(g(2),g(3))), 3] && N2 == 2;
test bool descendant22() = [1, /g(int N3), 3] := [1, f(g(1),f(g(2),g(3))), 3] && N3 == 3;


data A = a(int i);
data B = b(str s);

data C = c (A x, B y);

test bool descendant30() = [ n | /int n := [c(a(3), b("a")), c(a(4), b("b"))] ] == [3, 4];
test bool descendant31() = [ n | /a(int n) := [c(a(3), b("a")), c(a(4), b("b"))] ] == [3, 4];
test bool descendant32() = [ s | /str s := [c(a(3), b("a")), c(a(4), b("b"))] ] == ["a", "b"];
test bool descendant33() = [ s | /b(str s) := [c(a(3), b("a")), c(a(4), b("b"))] ] == ["a", "b"];

data D = d(map[A,B] ab);

test bool descendant40() = {n | /int n := [d((a(1) : b("one"), a(2) : b("two"))), d((a(10) : b("ten"), a(20) : b("twenty")))] } == {1, 2, 10, 20};
test bool descendant41() = {n | /a(int n) := [d((a(1) : b("one"), a(2) : b("two"))), d((a(10) : b("ten"), a(20) : b("twenty")))] } == {1, 2, 10, 20};
test bool descendant42() = {s | /str s := [d((a(1) : b("one"), a(2) : b("two"))), d((a(10) : b("ten"), a(20) : b("twenty")))] } == {"one", "two", "ten", "twenty"};
test bool descendant43() = {s | /b(str s) := [d((a(1) : b("one"), a(2) : b("two"))), d((a(10) : b("ten"), a(20) : b("twenty")))] } == {"one", "two", "ten", "twenty"};
