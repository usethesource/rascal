 module tests::library::ListTests
 /*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
*******************************************************************************/
  import Exception;
  import List;
  	
  // delete	
  	
  		public test bool delete1() = delete([0,1,2], 0) == [1,2];
  		public test bool delete2() = delete([0,1,2], 1) == [0,2];
  		public test bool delete3() = delete([0,1,2], 2) == [0,1];
  
  /*
  // domain on Lists has been removed
  
  		public test bool domain1() = domain([]) == {};
  		public test bool domain2()  = domain([1]) == {0};
  		public test bool domain3() = domain([1, 2]) == {0, 1};
 */
  	
  // getOneFrom
  
  		public test bool getOneFrom1() {int N = List::getOneFrom([1]); return N == 1;}
  		public test bool getOneFrom2() {int N = getOneFrom([1]); return N == 1;}
  		public test bool getOneFrom3() {int N = List::getOneFrom([1,2]); return  (N == 1) || (N == 2);}
  		public test bool getOneFrom4() {int N = List::getOneFrom([1,2,3]); return  (N == 1) || (N == 2) || (N == 3);}
  		public test bool getOneFrom5() {real D = List::getOneFrom([1.0,2.0]); return  (D == 1.0) || (D == 2.0);}
  		public test bool getOneFrom6() {str S = List::getOneFrom(["abc","def"]); return  (S == "abc") || (S == "def");}
  
  // getOneFromError
  
	  @expected{EmptyList}
	  	public test bool getOneFromError1() {
	  		getOneFrom([]);
	  		return false;
	  	}
  
  // head
  
  		public test bool head1() = List::head([1]) == 1;
  		public test bool head2() = head([1]) == 1;
  		public test bool head3() = List::head([1, 2]) == 1;
  
  		public test bool head4() = head([1, 2, 3, 4], 0) == [];
  		public test bool head5() = head([1, 2, 3, 4], 1) == [1];
  		public test bool head6() = head([1, 2, 3, 4], 2) == [1,2];
  		public test bool head7() = head([1, 2, 3, 4], 3) == [1,2,3];
  		public test bool head8() = head([1, 2, 3, 4], 4) == [1,2,3,4];
  	
  
	  	@expected{EmptyList}
	  	public test bool head9() {
	  		head([]);
	  		return false;
	  		}
	  	
	  	@expected{IndexOutOfBounds}
	  	public test bool head10() {
	  		head([], 3);
	  		return false;
	  		}	
	  		
	 	 @expected{IndexOutOfBounds}
	  	public test bool head11() {
	  		head([1,2,3], 4);
	  		return false;
	  		}
  
  		
  // insertAt
  
  		public test bool insertAt1() =List::insertAt([], 0, 1) == [1];
  		public test bool insertAt2() =insertAt([], 0, 1) == [1];
  		public test bool insertAt3() =List::insertAt([2,3], 1, 1) == [2,1, 3];
  		public test bool insertAt4() =insertAt([2,3], 1, 1) == [2, 1, 3];
  		public test bool insertAt5() =List::insertAt([2,3], 2, 1) == [2,3,1];
  		public test bool insertAt6() =insertAt([2,3], 2, 1) == [2, 3, 1];
  	
  	  	@expected{IndexOutOfBounds}
  	   	public test bool insertAt7() {insertAt([1,2,3], 4, 5); return false;}
  	
  // isEmpty
  
  	   	public test bool isEmpty1()  =isEmpty([]);
  	   	public test bool isEmpty2()  =isEmpty([1,2]) == false;
  	
  // mapper 
  
  	   	public test bool mapper1() {int inc(int n) {return n + 1;} return mapper([1, 2, 3], inc) == [2, 3, 4];}
  
  // max
  
  	   	public test bool max1() = List::max([1, 2, 3, 2, 1]) == 3;
  	   	public test bool max2() = max([1, 2, 3, 2, 1]) == 3;
  	
  // min
  
  		public test bool min1() = List::min([1, 2, 3, 2, 1]) == 1;
  		public test bool min2() = min([1, 2, 3, 2, 1]) == 1;
  		
  // permutations
  
  		public test bool permutations1()  =permutations([]) == {[]};
  		public test bool permutations2()  =permutations([1]) == {[1]};
  		public test bool permutations3()  =permutations([1,2]) == {[1,2],[2,1]};
  		public test bool permutations4()  =permutations([1,2,3]) ==  {[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]};
  
  // distribution
  
  		public test bool distribution1()  =distribution([]) == ();
  		public test bool distribution2()  =distribution([1]) == (1:1);
  		public test bool distribution3()  =distribution([1,2]) == (1:1, 2:1);
  		public test bool distribution4()  =distribution([1,2, 2]) == (1:1, 2:2);
  	
  // reducer
  
  		public test bool reducer1() {
  		     int add(int x, int y){return x + y;};
  		     return reducer([1, 2, 3, 4], add, 0) == 10;
  		}
  	
  // reverse 
  
  		public test bool reverse1() = List::reverse([]) == [];
  		public test bool reverse2() = reverse([]) == [];
  		public test bool reverse3() = List::reverse([1]) == [1];
  		public test bool reverse4() = List::reverse([1,2,3]) == [3,2,1];
  
  	
  // size
  
  		public test bool size1() = List::size([]) == 0;
  		public test bool size2() = size([]) == 0;
  		public test bool size3() = List::size([1]) == 1;
  		public test bool size4() = List::size([1,2,3]) == 3;
  	
  // slice
  
  		public test bool slice1() = slice([1,2,3,4], 0, 0) == [];
  		public test bool slice2() = slice([1,2,3,4], 0, 1) == [1];
  		public test bool slice3() = slice([1,2,3,4], 0, 2) == [1,2];
  		public test bool slice4() = slice([1,2,3,4], 0, 3) == [1,2,3];
  		public test bool slice5()  =slice([1,2,3,4], 0, 4) == [1,2,3,4];
  		public test bool slice6() = slice([1,2,3,4], 1, 0) == [];
  		public test bool slice7() = slice([1,2,3,4], 1, 1) == [2];
  		public test bool slice8() = slice([1,2,3,4], 1, 2) == [2,3];
  		public test bool slice9() = slice([1,2,3,4], 3, 0) == [];
  		public test bool slice10() = slice([1,2,3,4], 3, 1) == [4];
  	
  
  // sort
  		public test bool sort1() =List::sort([]) == [];
  		public test bool sort2() =sort([]) == [];
  		public test bool sort3() =List::sort([1]) == [1];
  		public test bool sort4() =sort([1]) == [1];
  		public test bool sort5() =List::sort([2, 1]) == [1,2];
  		public test bool sort6() =sort([2, 1]) == [1,2];
  		public test bool sort7() =List::sort([2,-1,4,-2,3]) == [-2,-1,2,3, 4];
  		public test bool sort8() =sort([2,-1,4,-2,3]) == [-2,-1,2,3, 4];
  		public test bool sort9() =sort([1,2,3,4,5,6]) == [1,2,3,4,5,6];
  		public test bool sort10() =sort([1,1,1,1,1,1]) == [1,1,1,1,1,1];
  		public test bool sort11() =sort([1,1,0,1,1]) == [0,1,1,1,1];
  	
  // sum
  
  		public test bool sum1() =sum([]) == 0;
  		public test bool sum2() =sum([1]) == 1;
  		public test bool sum3() =sum([1,2]) == 3;
  		public test bool sum4() =sum([1,2,3]) == 6;
  	
  // sortWithCompareFunction 	
  
  		public test bool sortWithCompare1() = sort([1, 2, 3]) == [1,2,3];
  		public test bool sortWithCompare2() = sort([1, 2, 3], bool(int a, int b){return a < b;}) == [1,2,3];
  		public test bool sortWithCompare3() = sort([1, 2, 3], bool(int a, int b){return a > b;}) == [3,2,1];
  		
  		@expected{IllegalArgument}
         public test bool sortWithCompare4() {sort([1, 2, 3], bool(int a, int b){return a <= b;}); return false ;}
          
  		@expected{IllegalArgument}
         public test bool sortWithCompare5() {sort([1, 0, 1], bool(int a, int b){return a <= b;});  return false;}
 
  // tail
  
  		public test bool tail1() = List::tail([1]) == [];
  		public test bool tail2() = tail([1]) == [];
  		public test bool tail3() = List::tail([1, 2]) == [2];
  		public test bool tail4() = tail([1, 2, 3]) + [4, 5, 6]  == [2, 3, 4, 5, 6];
  		public test bool tail5() = tail([1, 2, 3]) + tail([4, 5, 6])  == [2, 3, 5, 6];
  
  		public test bool tail6() = tail([1, 2, 3], 2) == [2,3];
  		public test bool tail7() = tail([1, 2, 3], 0) == [];
  		
  		public test bool tail8() = tail(tail([1, 2])) == tail([3]);
  		
  		public test bool tail9() { L = [1,2]; return tail(tail(L)) == tail(tail(L));}
  		public test bool tail10() { L1 = [1,2,3]; L2 = [2,3]; return tail(tail(L1)) == tail(L2);}
  		public test bool tail11() { L1 = [1,2]; L2 = [3]; return tail(tail(L1)) == tail(L2);}
  		public test bool tail12() { L1 = [1,2]; L2 = [3]; return {tail(tail(L1)), tail(L2)} == {[]};}
  			
  		@expected{EmptyList}
	  	public test bool tail13() {
	  		tail([]); return false;
	  		}
	  
	  	@expected{IndexOutOfBounds}
	  	public test bool tail14() {
	  		tail([1,2,3], 4); return false;
	  		}
  	
  // takeOneFrom
  
  		public test bool takeOneFrom1() {<E, L> = takeOneFrom([1]); return (E == 1) && (L == []);}
  		public test bool takeOneFrom2() {<E, L> = List::takeOneFrom([1,2]); return ((E == 1) && (L == [2])) || ((E == 2) && (L == [1]));}
  	
	 	@expected{EmptyList}
	  	public test bool takeOneFrom3() {
	  		takeOneFrom([]);
	  		return false;
	  		}
  	
  // toMap
  
  		public test bool toMapUnique1() = List::toMapUnique([]) == ();
  		public test bool toMapUnique2() = toMapUnique([]) == ();
  		public test bool toMapUnique3() = List::toMapUnique([<1,10>, <2,20>]) == (1:10, 2:20);
  
  		@expected{MultipleKey}		
  		public test bool toMapUnique4() = List::toMapUnique([<1,10>, <1,20>]) == (1:10, 2:20);

  // toMap
  
  		public test bool toMap5() = List::toMap([]) == ();
  		public test bool toMap6() = toMap([]) == ();
  		public test bool toMap7() = List::toMap([<1,10>, <2,20>]) == (1:{10}, 2:{20});
  		public test bool toMap8() = List::toMap([<1,10>, <2,20>, <1,30>]) == (1:{10,30}, 2:{20});
  	
  		public test bool toMap9() = List::toSet([]) == {};
  		public test bool toMap10() = toSet([]) == {};
  		public test bool toMap11() = List::toSet([1]) == {1};
  		public test bool toMap12() = toSet([1]) == {1};
  		public test bool toMap13() = List::toSet([1, 2, 1]) == {1, 2};
  	
  // testToString
  
  		public test bool toString1() = List::toString([]) == "[]";
  		public test bool toString2() = toString([]) == "[]";
  		public test bool toString3() = List::toString([1]) == "[1]";
  		public test bool toString4() = List::toString([1, 2]) == "[1,2]";
  	
  // listExpressions3
  
  	public test bool listExpressions1() { 
  		value n = 1; 
  		value s = "string"; 
  		return list[int] _ := [ n ] && 
  		list[str] _ := [ s, s, *[ s, s ] ]; 
  		}
  	
  	
  // Tests related to the correctness of the dynamic types of lists produced by the library functions;
  // incorrect dynamic types make pattern matching fail;
  
  	
  // testDynamicTypes
  
  		public test bool dynamicTypes1() { list[value] lst = ["1",2,3]; return list[int] _ := slice(lst, 1, 2); }
  		public test bool dynamicTypes2() { list[value] lst = ["1",2,3]; return list[int] _ := lst - "1"; }
  		public test bool dynamicTypes3() { list[value] lst = ["1",2,3]; return list[int] _ := lst - ["1"]; }
  		public test bool dynamicTypes4() { list[value] lst = ["1",2,3]; return  list[int] _ := delete(lst, 0); }
  		public test bool dynamicTypes5() { list[value] lst = ["1",2,3]; return  list[int] _ := drop(1, lst); }
  		public test bool dynamicTypes6() { list[value] lst = [1,2,"3"]; return  list[int] _ := head(lst, 2); }
  		public test bool dynamicTypes7() { list[value] lst = [1,2,"3"]; return  list[int] _ := prefix(lst); }
  		public test bool dynamicTypes8() { list[value] lst = ["1",2,3]; return  list[int] _ := tail(lst); }
  		public test bool dynamicTypes9() { list[value] lst = [1,2,"3"]; return  list[int] _ := take(2, lst); }	
  		public test bool dynamicTypes10() { return [str _, *int _] := ["1",2,3]; }
  	
 