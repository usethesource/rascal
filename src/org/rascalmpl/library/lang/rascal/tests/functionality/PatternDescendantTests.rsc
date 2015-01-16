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