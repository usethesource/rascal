 module tests::functionality::AssignmentTests
  
  	
  // testSimple
  
  		public test bool testSimple1() {bool b = true; return b == true;}
  		public test bool testSimple2() {b = true; return b == true;}
  	
  // testInteger
  
  		public test bool testInteger1() {int N = 3; N += 2; return N==5;}
  		public test bool testInteger2() {int N = 3; N -= 2; return N==1;}
  		public test bool testInteger3() {int N = 3; N *= 2; return N==6;}
  		public test bool testInteger4() {int N = 6; N /= 2; return N==3;}
  		public test bool testInteger5() {int N = 6; N ?= 2; return N==6;}
  	
  // testTuple
  
  		public test bool testTuple1() {int a = 1; int b = 2; <a, b> = <b, a>; return (a == 2) && (b == 1);}
  		public test bool testTuple2() {<a, b> = <1, 2>; return (a == 1) && (b == 2);}
  		public test bool testTuple3() {tuple[str name, int cnt] T = <"abc", 1>; T.name = "def"; return T.name == "def";}
  		public test bool testTuple4() {tuple[str name, int cnt] T = <"abc", 1>; return T[name = "def"] == <"def", 1>;}
  
  	
  // testList1
  
  		public test bool testList1() {list[int] L = []; return L == [];}
  		public test bool testList2() {list[int] L = [0,1,2]; L[1] = 10; return L == [0,10,2];}
  		public test bool testList3() {L = [0,1,2]; L[1] = 10; return L == [0,10,2];}
  		public test bool testList4() {list[list[int]] L = [[0,1],[2,3]]; L[1][0] = 20; return L == [[0,1],[20,3]];}
  		public test bool testList5() {L = [[0,1],[2,3]]; L[1][0] = 20; return  L == [[0,1],[20,3]];}
  		
  		public test bool testList6() {list[int] L = [1,2,3]; L += [4]; return  L==[1,2,3,4];}
  		public test bool testList7() {list[int] L = [1,2,3]; L -= [2]; return  L==[1,3];}
  		public test bool testList8() {list[int] L = [1,2,3]; L ?= [4]; return  L==[1,2,3];}
  	
  // testList2
  
  		public test bool testList10() {list[list[int]] L = [[1,2,3],[10,20,30]]; L[0] += [4]; return  L==[[1,2,3,4],[10,20,30]];}
  		public test bool testList11() {list[list[int]] L = [[1,2,3],[10,20,30]]; L[0] -= [2]; return  L==[[1,3],[10,20,30]];}
  		public test bool testList12() {list[list[int]] L = [[1,2,3],[10,20,30]]; L[0] ?= [4]; return  L==[[1,2,3],[10,20,30]];}
  	
  // testMap1
  
  		public test bool testMap1() {map[int,int] M = (); return M == ();}
  		public test bool testMap2() {map[int,int] M = (1:10, 2:20);return  M == (1:10, 2:20);}
  		
  		public test bool testMap3() {map[int,int] M = (1:10, 2:20); M += (3:30); return M==(1:10, 2:20,3:30);}
  		public test bool testMap4() {map[int,int] M = (1:10, 2:20); M -= (2:20); return M==(1:10);}
  		public test bool testMap5() {map[int,int] M = (1:10, 2:20); M ?= (3:30); return M==(1:10, 2:20);}
  	
  // testMap2
  
  		public test bool testMap7() {map[int,list[int]] M = (0:[1,2,3],1:[10,20,30]); M[0] += [4]; return M==(0:[1,2,3,4],1:[10,20,30]);}
  		public test bool testMap8() {map[int,list[int]] M = (0:[1,2,3],1:[10,20,30]); M[0] -= [2]; return M==(0:[1,3],1:[10,20,30]);}
  		public test bool testMap9() {map[int,list[int]] M = (0:[1,2,3],1:[10,20,30]); M[0] ?= [4]; return M==(0:[1,2,3],1:[10,20,30]);}
  		public test bool testMap10() {map[int, list[int]] M = (0:[1,2,3],1:[10,20,30]); M[2] ?= [4]; return M==(0:[1,2,3],1:[10,20,30], 2:[4]);}
  		
  	
  // testSet
  
  		public test bool testSet1() {set[int] S = {}; return S == {};}
  		public test bool testSet2() {set[int] S = {0,1,2}; return S == {0, 1, 2};}
  		
  		public test bool testSet3() {set[int] L = {1,2,3}; L += {4}; return  L=={1,2,3,4};}
  		public test bool testSet4() {set[int] L = {1,2,3}; L -= {2}; return L=={1,3};}
  		public test bool testSet5() {set[int] L = {1,2,3}; L ?= {4}; return L=={1,2,3};}

  
   data D = listfield(list[int] ints) | intfield(int i);
  	
  // testADT
  
          public test bool testADT1() {
              D d = listfield([1,2]); 
              d.ints += [3]; 
              return d == listfield([1,2,3]);
              }
   
           public test bool testADT2() {
              D d = listfield([1,2]); 
              d.ints -= [2]; 
              return d == listfield([1]);
              }
            
  		public test bool testADT3() {
              D d = intfield(2); 
              d.i += 3;
              if (d != intfield(5)) return false;
              d.i -= 3;
              if (d != intfield(2)) return false;
              d = intfield(5);
              d.i *= 3;
              if (d != intfield(15)) return false;
              d = intfield(6);
              d.i /= 3;
              if (d != intfield(2)) return false;
              return true;
              }
  	
  data F = f() | f(int n) | g(int n) | deep(F f);
  anno int F @ pos;
  
  // testAnnotations
  
  	public test bool testAnnotations1() {	
  		F X = f(); X @ pos = 1; 
  		if (X @ pos != 1) return false;
  	    X = f(); X @ pos = 2; X @ pos += 3; 
  	    if ( X @ pos != 5) return false;
  		X = f(); X @ pos = 3; X @ pos -= 2; 
  		if ( X @ pos != 1) return false;
  	     X = f(); X @ pos = 2; X @ pos *= 3; 
  		if (X @ pos != 6) return false;
  		X = f(); 
  		X @ pos = 6; X @ pos /= 3;  
  		if (X @ pos != 2) return false;
  		X = f(); X @ pos = 6; X @ pos ?= 3;  
  		if (X @ pos != 6) return false;
  		X = f(); X @ pos ?= 3;  
  		if (X @ pos != 3) return false;
  		return true;
  	}
  	
  // assigningClosureToVariableBug877
  
  	public test bool assigningClosureToVariableBug8771() {	
  	    bool() x = bool(){ return true; };
  		return x() == true;
  		}
  	
  
  	
 