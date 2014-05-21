 module tests::functionality::ComprehensionTests
 /*******************************************************************************
   * Copyright (c) 2009-2011 CWI
   * All rights reserved. This program and the accompanying materials
   * are made available under the terms of the Eclipse Public License v1.0
   * which accompanies this distribution, and is available at
   * http://www.eclipse.org/legal/epl-v10.html
   *
   * Contributors:
  
   *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
   *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
   *   * Paul Klint - Paul.Klint@cwi.nl - CWI
   *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
   *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
  *******************************************************************************/
 
 import Exception;
 import List;
  	
  // set comprehension
  
  		public test bool emptySetGeneratorError1()  = { X | int X <- {} } == {};
  
  		public test bool emptySetGeneratorError2()  = { X | int X <- [] } == {};
  		
  		public test bool setComprehension1()  = { X | int X <- {1}} == {1};
  		public test bool setComprehension2()  = { X | int X <- [1]} == {1};
  		
  		public test bool setComprehension3()  = { X | X <- {1}} == {1};
  		public test bool setComprehension4()  = { X | X <- [1]} == {1};
  		
  		public test bool setComprehension5()  = { X | int X <- {1, 2}} == {1,2};
  		public test bool setComprehension6()  = { X | int X <- [1, 2]} == {1,2};
  		
  		public test bool setComprehension7()  = { X | X <- {1, 2}} == {1,2};
  		public test bool setComprehension8()  = { X | X <- [1, 2]} == {1,2};
  		
  		public test bool setComprehension9()  = { X | int X <- {1, 1, 1}} == {1};
  		public test bool setComprehension10()  = { X | int X <- [1, 1, 1]} == {1};
  		
  		public test bool setComprehension11()  = { 1 | int X <- {1,2,3}} == {1};
  		public test bool setComprehension12()  = { 1 | int X <- [1,2,3]} == {1};
  		
  		public test bool setComprehension13()  = { 1 | int X <- {1,2,3}, true } == {1};
  		public test bool setComprehension14()  = { 1 | int X <- [1,2,3], true } == {1};
  		
  		public test bool setComprehension15()  = { 1 | int X <- {1,2,3}, false} 	== {};
  		public test bool setComprehension16()  = { 1 | int X <- [1,2,3], false} 	== {};
  		
  		public test bool setComprehension17()  = { X | int X <- {1,2,3}} == {1,2,3};
  		public test bool setComprehension18()  = { X | int X <- [1,2,3]} == {1,2,3};
  		
  		public test bool setComprehension19()  = {  X | int X <- {1,2,3}, true} == {1,2,3};
  		public test bool setComprehension20()  = {  X | int X <- [1,2,3], true} == {1,2,3};
  		
  		public test bool setComprehension21()  = {  X | int X <- {1,2,3}, false} 	== {};
  		public test bool setComprehension22()  = {  X | int X <- [1,2,3], false} 	== {};
  		
  		public test bool setComprehension23()  = {  X | int X <- {1,2,3}, X >= 2, X < 3} == {2};
  		public test bool setComprehension24()  = {  X | int X <- [1,2,3], X >= 2, X < 3} == {2};
  		
  		public test bool setComprehension25()  = {  X, 10*X | int X <- [1,2,3]} == {1,2,3,10,20,30};
  		public test bool setComprehension26()  = {  X, 10*X, 100*X | int X <- [1,2,3]} == {1,2,3,10,20,30, 100,200,300};	
  		
  		public test bool setComprehension27()  = {  {} | int X <- {1,2,3}} == {{}};
  		public test bool setComprehension28()  = {  {} | int X <- [1,2,3]} == {{}};
  		
  		public test bool setComprehension29()  = {  {} | int X <- {1,2,3}, true} == {{}};
  		public test bool setComprehension30()  = {  {} | int X <- [1,2,3], true} == {{}};
  		
  		public test bool setComprehension31()  = {  {} | int X <- {1,2,3}, false} == {};
  		public test bool setComprehension32()  = {  {} | int X <- [1,2,3], false} == {};
  		
  		public test bool setComprehension33()  = { <1,2,3> | int X <- {1,2,3}} 	== {<1,2,3>};
  		public test bool setComprehension34()  = { <1,2,3> | int X <- [1,2,3]} 	== {<1,2,3>};
  		
  		public test bool setComprehension35()  = { <1,2,3> | int X <- {1,2,3}, true} 	== {<1,2,3>};
  		public test bool setComprehension36()  = { <1,2,3> | int X <- [1,2,3], true} 	== {<1,2,3>};
  		
  		public test bool setComprehension37()  = { <1,2,3> | int X <- {1,2,3}, true, true} == {<1,2,3>};
  		public test bool setComprehension38()  = { <1,2,3> | int X <- [1,2,3], true, true} == {<1,2,3>};
  		
  		public test bool setComprehension39()  = { <1,2,3> | int X <- {1,2,3}, false}	== {} ;
  		public test bool setComprehension40()  = { <1,2,3> | int X <- [1,2,3], false}	== {} ;
  		
  		public test bool setComprehension41()  = { Y | list[int] Y <- [[1,2,3],[10,20,30],[100,200,300]] } == { [1,2,3],[10,20,30],[100,200,300]};
  		public test bool setComprehension42()  = {1 | 3 > 2} == {1} ;
  		public test bool setComprehension43()  = {1 | 2 > 3} == {} ;
  	
  // any
  		
  		public test bool any1()  = any(int X <- {1,2,3}, X > 2);
  		public test bool any2()  = any(    X <- {1,2,3}, X > 2);
  		public test bool any3()  = any(int X <- {1,2,3}, X > 2, X <10);
  		public test bool any4()  = any(int X <- {1,2,3}, X > 2 && X <10);
  		public test bool any5()  = any(    X <- {1,2,3}, X > 2 && X <10);
  		
  		public test bool any6()  = any(int X <- [1,2,3], X > 2);
  		public test bool any7()  = any(int X <- [1,2,3], X > 2, X < 10);
  		public test bool any8()  = any(int X <- [1,2,3], X > 2 && X < 10);
  		
  		public test bool any9()  = !(any(int X <- {1,2,3}, X > 10));
  		public test bool any10()  = !(any(int X <- [1,2,3], X > 10));
  		
  		public test bool any11()  = any(<int X, int Y> <- {<1,10>,<30,3>,<2,20>}, X > Y);
  		public test bool any12()  = any(<int X, int Y> <- [<1,10>,<30,3>,<2,20>], X > Y);
  		
  		public test bool any13()  = !(any(<int X, int Y> <- {<1,10>,<30,3>,<2,20>}, X > 100*Y));
  		public test bool any14()  = !(any(<int X, int Y> <- [<1,10>,<30,3>,<2,20>], X > 100*Y));
  		
  		public test bool any15()  = !(any(_ <- []));
  		public test bool any16()  = !(any(_ <- {}));
  		public test bool any17()  = !(any(_ <- ()));
  	
  // all
  		
  		public test bool all1()  = all(int X <- {1,2,3}, X >= 1);
  		public test bool all2()  = all(int X <- {1,2,3}, X >= 1, X < 10);
  		public test bool all3()  = all(int X <- {1,2,3}, X >= 1 && X < 10);
  		public test bool all4()  = all(int X <- [1,2,3], X >= 1);
  		public test bool all5()  = all(int X <- {1,2,3}, X >= 1, X < 10);
  		public test bool all6()  = all(int X <- {1,2,3}, X >= 1 && X < 10);
  		
  		public test bool all7()  = !(all(int X <- {1,2,3}, X >= 2));
  		public test bool all8()  = !(all(int X <- {1,2,3}, X >= 2, X <=2));
  		public test bool all9()  = !(all(int X <- {1,2,3}, X >= 2 && X <=2));
  		public test bool all10()  = !(all(int X <- [1,2,3], X >= 2));
  		public test bool all11()  = !(all(int X <- [1,2,3], X >= 2, X <= 2));
  		public test bool all12()  = !(all(int X <- [1,2,3], X >= 2 && X <= 2));
  		
  		public test bool all13()  = all(<int X, int Y> <- {<1,10>,<3,30>,<2,20>}, X < Y);
  		public test bool all14()  = all(<int X, int Y> <- [<1,10>,<3,30>,<2,20>], X < Y);
  		
  		public test bool all15()  = !(all(<int X, int Y> <- {<1,10>,<30,3>,<2,20>}, X < Y));
  		public test bool all16()  = !(all(<int X, int Y> <- [<1,10>,<30,3>,<2,20>], X < Y));
  		
  		public test bool all17()  = all(int i <- [0, 1] && [0, 1][i] == i);
  		
  		// The following were all asserTrue, how can this have worked?
  		
  		@ignore{Changed semantics}
  		public test bool all18()  = !(all(_ <- []));
  		@ignore{Changed semantics}
  		public test bool all19()  = !(all(_ <- {}));
  		@ignore{Changed semantics}
  		public test bool all20()  = !(all(_ <- ()));
  		
  		public test bool all21()  = all(k <- [1,2,3], (k % 2 == 0 || k % 2 == 1)?true:false);
  	
  
  
  
  // setComprehension
  		
  		public test bool setComprehension44()  = {X + 1 | int X <- {1,2,3}} == {2,3,4};
  		public test bool setComprehension45()  = {X + 1 | int X <- [1,2,3]} == {2,3,4};
  		
  		public test bool setComprehension46()  = {X | int X <- {1,2,3}, X + 1 < 3} == {1};
  		public test bool setComprehension47()  = {X | int X <- [1,2,3], X + 1 < 3} == {1};
  		
  		public test bool setComprehension48()  = {X - 1 | int X <- {1,2,3}} == {0,1,2};
  		public test bool setComprehension49()  = {X - 1 | int X <- [1,2,3]} == {0,1,2};
  		
  		public test bool setComprehension50()  = {X | int X <- {1,2,3}, X - 1 < 3} == {1,2,3};
  		public test bool setComprehension51()  = {X | int X <- [1,2,3], X - 1 < 3} == {1,2,3};
  		
  		public test bool setComprehension52()  = {X * 2 | int X <- {1,2,3}} == {2,4,6};
  		public test bool setComprehension53()  = {X * 2 | int X <- [1,2,3]} == {2,4,6};
  		
  		
  		public test bool setComprehension54()  = {*[X * 2] | int X <- {1,2,3}} == {2,4,6};
  		public test bool setComprehension55()  = {*[X * 2, X * 2 + 1] | int X <- {1,2,3}} == {2,3,4,5,6,7};
  
  set[int] fset(int n) { return {n, 3*n}; }	
  
  // setComprehension
  		
  		public test bool setComprehension56() = {fset(n) | n <- [ 1 .. 4 ]} == {{1,3},{2,6},{3,9}};
  		public test bool setComprehension57() = {*fset(n) | n <- [ 1 .. 4 ]} == {1,3,2,6,3,9};
  		
  		public test bool setComprehension58() = {{n, 3*n} | n <- [ 1 .. 4 ]} == {{1,3},{2,6},{3,9}};
  		public test bool setComprehension59() = {*{n, 3*n} | n <- [ 1 .. 4 ]} == {1,3,2,6,3,9};
  		public test bool setComprehension60() = {n, 3*n | n <- [ 1 .. 4 ]} == {1,3,2,6,3,9};
  	
  		public test bool setComprehension61() = {{5*n, fset(n)} | n <- [ 1 .. 4 ]} == {{5,{1,3}},{10,{2,6}},{15,{3,9}}};
  		public test bool setComprehension62() = {{5*n, *fset(n)} | n <- [ 1 .. 4 ]} == {{5,1,3},{10,2,6},{15,3,9}};
  		public test bool setComprehension63() = {5*n, fset(n) | n <- [ 1 .. 4 ]} == {5,{1,3},10,{2,6},15,{3,9}};
  		public test bool setComprehension64() = {5*n, *fset(n) | n <- [ 1 .. 4 ]} == {5,1,3,10,2,6,15,3,9};
  		
  		public test bool setComprehension65() = {{5*n, fset(n)} | n <- [ 1 .. 4 ]} == {{5,{1,3}},{10,{2,6}},{15,{3,9}}};
  		public test bool setComprehension66() = {{5*n, *fset(n)} | n <- [ 1 .. 4 ]} == {{5,1,3},{10,2,6},{15,3,9}};
  		public test bool setComprehension67() = {5*n, fset(n) | n <- [ 1 .. 4 ]} == {5,{1,3},10,{2,6},15,{3,9}};
  		public test bool setComprehension68() = {5*n, *fset(n) | n <- [ 1 .. 4 ]} == {5,1,3,10,2,6,15,3,9};
  	
  // setComprehensionNested
  
  		public test bool setComprehensionNested1()  = { {X + y | int y <- [1..X+1]} | int X <- {1,2,3}} == {{2}, {3,4}, {4,5,6}};
  		public test bool setComprehensionNested2()  = { *{X + y | int y <- [1..X+1]} | int X <- {1,2,3}} == {2, 3, 4, 5, 6};
  		public test bool setComprehensionNested3()  = { {X + y | int y <- [1..X+1], X < 2} | int X <- [1,2,3]} == {{2}, {}};
  		public test bool setComprehensionNested4()  = { *{X + y | int y <- [1..X+1], X < 2} | int X <- [1,2,3]} == {2};
  		public test bool setComprehensionNested5()  = { {X + y | int y <- [1..X+1], X > 2} | int X <- [1,2,3]} == {{}, {4,5,6}};
  		public test bool setComprehensionNested6()  = { *{X + y | int y <- [1..X+1], X > 2} | int X <- [1,2,3]} == {4, 5, 6};
  	
  // emptySetGeneratorError
  
  		public test bool emptySetGeneratorError3()  = [ X | int X <- {} ] == [];
  	
  // emptyListGeneratorError1
  
  		public test bool emptyListGeneratorError1()  = [ X | int X <- [] ] == [];
  	
  // emptyListGeneratorError
  
  		public test bool emptyListGeneratorError2()  = [ X |     X <- [] ] == [];
  	
  // listComprehension1
  		
  		public test bool listComprehension1()  = [ X | int X <- {1}] == [1];
  		public test bool listComprehension2()  = [ X | int X <- [1]] == [1];
  		public test bool listComprehension3()  = [ X |     X <- [1]] == [1];
  		
  		public test bool listComprehension4()  = {L = [ X | int X <- {1, 2}]; (L == [1,2]) || (L == [2, 1]);};
  		public test bool listComprehension5()  = [ X | int X <- [1, 2]] == [1,2];
  		public test bool listComprehension6()  = [ X |     X <- [1, 2]] == [1,2];
  		
  		public test bool listComprehension7()  = [ X | int X <- {1, 1, 1}] == [1];
  		public test bool listComprehension8()  = [ X | int X <- [1, 1, 1]] == [1, 1, 1];
  		
  		public test bool listComprehension9()  = [ 1 | int X <- {1,2,3}] == [1, 1, 1];
  		public test bool listComprehension10()  = [ 1 | int X <- [1,2,3]] == [1, 1, 1];
  		
  		public test bool listComprehension11()  = [ 1 | int X <- {1,2,3}, true ] == [1, 1, 1];
  		public test bool listComprehension12()  = [ 1 | int X <- [1,2,3], true ] == [1, 1, 1];
  		
  		public test bool listComprehension13()  = [ 1 | int X <- {1,2,3}, false] 	== [];
  		public test bool listComprehension14()  = [ 1 | int X <- [1,2,3], false] 	== [];
  		
  		public test bool listComprehension15()  = {L = [ X | int X <- {1,2}]; (L == [1,2]) || (L == [2, 1]);};
  		public test bool listComprehension16()  = [ X | int X <- [1,2,3]] == [1,2,3];
  		
  		public test bool listComprehension17()  = {L = [  X | int X <- {1,2}, true]; (L == [1,2]) || (L == [2, 1]);};
  		public test bool listComprehension18()  = [  X | int X <- [1,2,3], true] == [1,2,3];
  		
  		public test bool listComprehension19()  = [  X | int X <- {1,2,3}, false] == [];
  		public test bool listComprehension20()  = [  X | int X <- [1,2,3], false] == [];
  		
  		public test bool listComprehension21()  = [  X | int X <- {1,2,3}, X >= 2, X < 3] == [2];
  		public test bool listComprehension22()  = [  X | int X <- [1,2,3], X >= 2, X < 3] == [2];
  		
  		public test bool listComprehension23()  = [  X, 10*X | int X <- [1,2,3]] == [1,10,2,20,3,30];
  		public test bool listComprehension24()  = [  X, 10*X, 100*X | int X <- [1,2,3]] == [1,10,100,2,20,200,3,30,300];
  	
  // listComprehension
  		
  		public test bool listComprehension25()  = [  [] | int X <- {1,2,3}] == [[], [], []];
  		public test bool listComprehension26()  = [  [] | int X <- [1,2,3]] == [[], [], []];
  		
  		public test bool listComprehension27()  = [  [] | int X <- {1,2,3}, true] == [[], [], []];
  		public test bool listComprehension28()  = [  [] | int X <- [1,2,3], true] == [[], [], []];
  		
  		public test bool listComprehension29()  = [  [] | int X <- {1,2,3}, false] == [];
  		public test bool listComprehension30()  = [  [] | int X <- [1,2,3], false] == [];
  		
  		public test bool listComprehension31()  = [ <1,2,3> | int X <- {1,2,3}] == [<1,2,3>, <1,2,3>, <1,2,3>];
  		public test bool listComprehension32()  = [ <1,2,3> | int X <- [1,2,3]] == [<1,2,3>, <1,2,3>, <1,2,3>];
  		
  		public test bool listComprehension33()  = [ <1,2,3> | int X <- {1,2,3}, true] == [<1,2,3>, <1,2,3>, <1,2,3>];
  		public test bool listComprehension34()  = [ <1,2,3> | int X <- [1,2,3], true] == [<1,2,3>, <1,2,3>, <1,2,3>];
  		
  		public test bool listComprehension35()  = [ <1,2,3> | int X <- {1,2,3}, true, true] == [<1,2,3>, <1,2,3>, <1,2,3>];
  		public test bool listComprehension36()  = [ <1,2,3> | int X <- [1,2,3], true, true] == [<1,2,3>, <1,2,3>, <1,2,3>];
  		
  		public test bool listComprehension37()  = [ <1,2,3> | int X <- {1,2,3}, false]	== [] ;
  		public test bool listComprehension38()  = [ <1,2,3> | int X <- [1,2,3], false]	== [] ;
  	
  // listComprehension
  		
  		public test bool listComprehension39()  = [ [Y] | list[int] Y <- [[1,2,3],[10,20,30],[100,200,300]] ] == [ [[1,2,3]], [[10,20,30]],[[100,200,300]]];
  		public test bool listComprehension40()  = [ Y | list[int] Y <- [[1,2,3],[10,20,30],[100,200,300]] ] == [ [1,2,3], [10,20,30],[100,200,300]];
  		public test bool listComprehension41()  = [ *Y | list[int] Y <- [[1,2,3],[10,20,30],[100,200,300]] ] == [ 1,2,3, 10,20,30,100,200,300];
  		
  		public test bool listComprehension42()  = [1 | 3 > 2] == [1] ;
  		public test bool listComprehension43()  = [1 | 2 > 3] == [] ;
  		
  		public test bool listComprehension44()  = {L = [X + 1 | int X <- {1,2}]; (L == [2,3]) || (L == [3,2]);};
  		public test bool listComprehension45()  = [X + 1 | int X <- [1,2,3]] == [2,3,4];
  		
  		public test bool listComprehension46()  = [X | int X <- {1,2,3}, X + 1 < 3] == [1];
  		public test bool listComprehension47()  = [X | int X <- [1,2,3], X + 1 < 3] == [1];
  		
  		public test bool listComprehension48()  = {L = [X - 1 | int X <- {1,2}]; (L == [0,1]) || (L == [1,0]);};
  		public test bool listComprehension49()  = [X - 1 | int X <- [1,2,3]] == [0,1,2];
  		
  		public test bool listComprehension50()  = {L = [X | int X <- {2,3}, X - 1 < 3]; (L == [2,3]) || (L == [3,2]);};
  		public test bool listComprehension51()  = [X | int X <- [1,2,3], X - 1 < 3] == [1,2,3];
  		
  		public test bool listComprehension52()  = { L = [X * 2 | int X <- {2,3}]; (L == [4,6]) || (L == [6,4]);};
  		public test bool listComprehension53()  = [X * 2 | int X <- [1,2,3]] == [2,4,6];
  		
  		public test bool listComprehension54()  = [*{X * 2} | int X <- [1,2,3]] == [2,4,6];
  		
  		public test bool listComprehension55() = toSet([*{X * 2, X * 2 + 1} | int X <- [1,2,3]]) == {2,3,4,5, 6, 7};

  
  list[int] flist(int n) { return [n, 3*n];}	
  
  // listComprehension
  
  		public test bool listComprehension56() = [flist(n) | n <- [ 1 .. 4 ]] == [[1,3],[2,6],[3,9]];
  		public test bool listComprehension57() = [*flist(n) | n <- [ 1 .. 4 ]] == [1,3,2,6,3,9];
  
  		public test bool listComprehension58() = [[n, 3*n] | n <- [ 1 .. 4 ]] == [[1,3],[2,6],[3,9]];
  		
  		public test bool listComprehension59() = [5*n, flist(n) | n <- [ 1 .. 4 ]] == [5,[1,3],10,[2,6],15,[3,9]];
  		public test bool listComprehension60() = [5*n, *flist(n) | n <- [ 1 .. 4 ]] == [5,1,3,10,2,6,15,3,9];
  		
  		public test bool listComprehension61() = [[5*n, flist(n)] | n <- [ 1 .. 4 ]] == [[5,[1,3]],[10,[2,6]],[15,[3,9]]];
  		public test bool listComprehension62() = [[5*n, *flist(n)] | n <- [ 1 .. 4 ]] == [[5,1,3],[10,2,6],[15,3,9]];
  
  // listComprehensionNested
  
  		public test bool listComprehension63()  = [  [y | int y <- [0..X+1]] | int X <- [1,2,3]] == [[0,1], [0,1,2], [0,1,2,3]];
  		public test bool listComprehension64()  = [ *[y | int y <- [0..X+1]] | int X <- [1,2,3]] == [0,1, 0,1,2, 0,1,2,3];
  		public test bool listComprehension65()  = [ [y | int y <- [0..X+1], X < 2] | int X <- [1,2,3]] == [[0,1], [], []];
  		public test bool listComprehension66()  = [ *[y | int y <- [0..X+1], X < 2] | int X <- [1,2,3]] == [0,1];
  		public test bool listComprehension67()  = [ [y | int y <- [0..X+1], X > 2] | int X <- [1,2,3]] == [[], [], [0,1,2,3]];
  		public test bool listComprehension68()  = [ *[y | int y <- [0..X+1], X > 2] | int X <- [1,2,3]] == [0,1,2,3];
  	
  // relationComprehension
  		
  		public test bool relationComprehension1()  = {<X,Y> | int X <- {1}, int Y <- {2}} == {<1,2>};
  		public test bool relationComprehension2()  = {<X,Y> | int X <- [1,1,1], int Y <- [2,2,2]} == {<1,2>};
  		
  		public test bool relationComprehension3()  = {<1,2> | int X <- {1,2,3}} == {<1,2>};
  		public test bool relationComprehension4()  = {<1,2> | int X <- [1,2,3]} == {<1,2>};
  		
  		public test bool relationComprehension5()  = {<X,Y> | int X <- {1,2,3}, int Y <- {2,3,4}} ==  {<1, 2>, <1, 3>, <1, 4>, <2, 2>, <2, 3>, <2, 4>, <3, 2>, <3, 3>, <3, 4>};
  		public test bool relationComprehension6()  = {<X,Y> | int X <- [1,2,3], int Y <- [2,3,4]} ==  {<1, 2>, <1, 3>, <1, 4>, <2, 2>, <2, 3>, <2, 4>, <3, 2>, <3, 3>, <3, 4>};
  		
  		public test bool relationComprehension7()  = {<X,Y> | int X <- {1,2,3}, int Y <- {2,3,4}, true} ==	{<1, 2>, <1, 3>, <1, 4>, <2, 2>, <2, 3>, <2, 4>, <3, 2>, <3, 3>, <3, 4>};
  		public test bool relationComprehension8()  = {<X,Y> | int X <- [1,2,3], int Y <- [2,3,4], true} ==	{<1, 2>, <1, 3>, <1, 4>, <2, 2>, <2, 3>, <2, 4>, <3, 2>, <3, 3>, <3, 4>};
  		
  		
  		public test bool relationComprehension9()  = {<X,Y> | int X <- {1,2,3}, int Y <- {2,3,4}, false} == {};
  		public test bool relationComprehension10()  = {<X,Y> | int X <- [1,2,3], int Y <- [2,3,4], false} == {};
  		
  		public test bool relationComprehension11()  = {<X,Y> | int X <- {1,2,3}, int Y <- {2,3,4}, X >= Y} =={<2, 2>, <3, 2>, <3, 3>};
  		public test bool relationComprehension12()  = {<X,Y> | int X <- [1,2,3], int Y <- [2,3,4], X >= Y} =={<2, 2>, <3, 2>, <3, 3>};
  		
  		public test bool relationComprehension13()  = {<X,Y> | int X <- {1,2,3}, <X, int Y> <- {<1,10>, <7,70>, <3,30>,<5,50>}} == {<1, 10>, <3, 30>};
  		public test bool relationComprehension14()  = {<X,Y> | int X <- [1,2,3], <X, int Y> <- [<1,10>, <7,70>, <3,30>,<5,50>]} == {<1, 10>, <3, 30>};
  		
  		public test bool relationComprehension15()  = {<X,Y> | int X <- {1,2,3}, <X, str Y> <- {<1,"a">, <7,"b">, <3,"c">,<5,"d">}} == {<1, "a">, <3, "c">};
  		public test bool relationComprehension16()  = {<X,Y> | int X <- [1,2,3], <X, str Y> <- [<1,"a">, <7,"b">, <3,"c">,<5,"d">]} == {<1, "a">, <3, "c">};
  
  // emptyMapGeneratorError1
  
  		public test bool emptyMapGeneratorError1()  = ( X : 2 * X | int X <- {} ) == ();
  
  // emptyMapGeneratorError2
  
  		public test bool emptyMapGeneratorError2()  = ( X : 2 * X | int X <- [] ) == ();
  	
  // mapComprehension
  		
  		public test bool mapComprehension1()  = ( X : 2 * X | int X <- {1}) == (1:2);
  		public test bool mapComprehension2()  = ( X : 2 * X | int X <- [1]) == (1:2);
  		
  		public test bool mapComprehension3()  = ( X : 2 * X | int X <- {1, 2}) == (1:2,2:4);
  		public test bool mapComprehension4()  = ( X : 2 * X | int X <- [1, 2]) == (1:2,2:4);
  		
  		public test bool mapComprehension5()  = ( X: 2 * X| int X<- [1,2,3] ) == (1:2,2:4,3:6);
  	
  // mapComprehensionNested
  
  		public test bool mapComprehensionNested1()  = ( X: (2 * X + y : y | int y <- [1..X+1]) | int X <- [1,2,3] ) == (1:(3:1),2:(5:1,6:2),3:(7:1,8:2,9:3));
  		public test bool mapComprehensionNested2()  = ( X: (2 * X + y : y | int y <- [1..X+1], X < 2) | int X <- [1,2,3] ) == (1:(3:1), 2:(), 3:());
  		public test bool mapComprehensionNested3()  = ( X: (2 * X + y : y | int y <- [1..X+1], X > 2) | int X <- [1,2,3] ) == (1:(),2:(),3:(7:1,8:2,9:3));
  
  data TREE = i(int N) | f(TREE a,TREE b) | g(TREE a, TREE b);
  	
  // nodeGenerator()
  		
  		public test bool nodeGenerator1() = [ X | /int X <- f(i(1),g(i(2),i(3))) ] == [1,2,3];
  		
  		public test bool nodeGenerator2() = [ X | /value X <- f(i(1),g(i(2),i(3))) ] == [1,i(1),2,i(2),3,i(3),g(i(2),i(3))];
  		public test bool nodeGenerator3() = [ X | value X <- f(i(1),g(i(2),i(3))) ] == [i(1),g(i(2),i(3))];
  
  		public test bool nodeGenerator4() = [N | /value N <- f(i(1),i(2))] == [1,i(1),2,i(2)];
  		public test bool nodeGenerator5() = [N | value N <- f(i(1),i(2))] == [i(1), i(2)];
  		
  		public test bool nodeGenerator6() = [N | /TREE N <- f(i(1),i(2))] == [i(1),i(2)];
  		public test bool nodeGenerator7() = [N | TREE N <- f(i(1),i(2))] == [i(1),i(2)];
  		
  		public test bool nodeGenerator8() = [N | /int N <- f(i(1),i(2))] == [1,2];
  		
  		public test bool nodeGenerator9() = [N | /value N <- f(i(1),g(i(2),i(3)))] == [1,i(1),2,i(2),3,i(3),g(i(2),i(3))];
  		public test bool nodeGenerator10() = [N | value N <- f(i(1),g(i(2),i(3)))] == [i(1),g(i(2),i(3))];
  		
  		public test bool nodeGenerator11() = [N | /TREE N <- f(i(1),g(i(2),i(3)))] == [i(1),i(2),i(3),g(i(2),i(3))];
  		public test bool nodeGenerator12() = [N | TREE N <- f(i(1),g(i(2),i(3)))] == [i(1),g(i(2),i(3))];
  		
  		public test bool nodeGenerator13() = [N | /int N <- f(i(1),g(i(2),i(3)))] == [1,2,3];
 	
  // regularGenerators
  		
  		public test bool regularGenerators1()  = [S | /@<S:[a-z]+>@/ <- ["@abc@", "@def@"]] == ["abc","def"];
  		public test bool regularGenerators2()  = {S | /@<S:[a-z]+>@/ <- ["@abc@", "@def@"]} == {"abc", "def"};
  		public test bool regularGenerators3()  = {S | /@<S:[a-z]+>@/ <- {"@abc@", "@def@"}} == {"abc", "def"};
 
  
 