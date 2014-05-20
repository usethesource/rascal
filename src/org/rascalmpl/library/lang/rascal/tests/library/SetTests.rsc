 module tests::library::SetTests
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
  import Set;
  
  // getOneFrom
  
  		public test bool getOneFrom1() {int N = Set::getOneFrom({1});  return N == 1;}
  		public test bool getOneFrom2() {int N = Set::getOneFrom({1}); return  N == 1;}
  		public test bool getOneFrom3() {int N = getOneFrom({1}); return  N == 1;}
  		public test bool getOneFrom4() {int N = Set::getOneFrom({1, 2}); return  (N == 1) || (N == 2);}
  		public test bool getOneFrom5() {int N = Set::getOneFrom({1, 2, 3}); return  (N == 1) || (N == 2) || (N == 3);}
  		public test bool getOneFrom6() {real D = Set::getOneFrom({1.0,2.0}); return  (D == 1.0) || (D == 2.0);}
  		public test bool getOneFrom7() {str S = Set::getOneFrom({"abc","def"}); return  (S == "abc") || (S == "def");}
  		
  		@expected{EmptySet}
  		public test bool getOneFrom8() {Set::getOneFrom({});return false;}
  	
	  	@expected{EmptySet}
	  	public test bool getOneFromError1() {
	  		getOneFrom({});return false;
	  	}
  	
  // isEmpty
  
  		public test bool isEmpty1() = isEmpty({});
  		public test bool isEmpty2() = isEmpty({1,2}) == false;
  		
  // mapper
  
  		public test bool mapper1() {int inc(int n) {return n + 1;} return mapper({1, 2, 3}, inc) == {2, 3, 4};}
  
  //  max
  
  		public test bool max1() = Set::max({1, 2, 3, 2, 1}) == 3;
  		public test bool max2()  = max({1, 2, 3, 2, 1}) == 3;
  	
  // min 
  
  		public test bool min1() = Set::min({1, 2, 3, 2, 1}) == 1;
  		public test bool min2() = min({1, 2, 3, 2, 1}) == 1;
  	
  // power 
  
  		public test bool power1() = Set::power({}) == {{}};
  		public test bool power2() = Set::power({1}) == {{}, {1}};
  		public test bool power3() = Set::power({1, 2}) == {{}, {1}, {2}, {1,2}};
  		public test bool power4() = Set::power({1, 2, 3}) == {{}, {1}, {2}, {3}, {1,2}, {1,3}, {2,3}, {1,2,3}};
  		public test bool power5() =  Set::power({1, 2, 3, 4}) == { {}, {1}, {2}, {3}, {4}, {1,2}, {1,3}, {1,4}, {2,3}, {2,4}, {3,4}, {1,2,3}, {1,2,4}, {1,3,4}, {2,3,4}, {1,2,3,4}};
  
  	
  // reducer
  
          public test bool reducer1() {
  		    int add(int x, int y){return x + y;}
  			return reducer({1, 2, 3, 4}, add, 0) == 10;
  		}
  
  // size	
  		public test bool size1() = Set::size({}) == 0;
  		public test bool size2()  = size({}) == 0;
  		public test bool size3()  = Set::size({1}) == 1;
  		public test bool size4()  = Set::size({1,2,3}) == 3;
  
  // sum	
  
  		public test bool sum1()  = sum({}) == 0;
  		public test bool sum2()  = sum({1}) == 1;
  		public test bool sum3()  = sum({1,2}) == 3;
  		public test bool sum4()  = sum({1,2,3}) == 6;
  
  //	takeOneFrom
  
  		public test bool takeOneFrom1() {<E, SI> = Set::takeOneFrom({1}); return (E == 1) && (SI == {}) ;}
  		public test bool takeOneFrom2() {<E, SI> = Set::takeOneFrom({1,2}); return ((E == 1) && (SI == {2})) || ((E == 2) && (SI == {1}));}
          
	    @expected{EmptySet}
	  	public test bool takeOneFromError1() {
	  		getOneFrom({});return false;
	  	}  
  	    
  // toList
  
  		public test bool toList1() = Set::toList({}) == [];
  		public test bool toList2() = toList({}) == [];
  		public test bool toList3() = Set::toList({1}) == [1];
  		public test bool toList4() = (Set::toList({1, 2, 1}) == [1, 2]) || (Set::toList({1, 2, 1}) == [2, 1]);
  	
  	
  // toMap
  		public test bool toMap1() = Set::toMap({}) == ();
  		public test bool toMap2()  =toMap({}) == ();
  		public test bool toMap3() = Set::toMap({<1, "a">}) == (1 : {"a"});
  		public test bool toMap4() = Set::toMap({<1, "a">, <2, "b">, <1, "c">}) == (1 : {"a", "c"}, 2 : {"b"});
  	
  // toMapUniassertTrue
  	
  		public test bool toMapUnique1() = Set::toMapUnique({}) == ();
  		public test bool toMapUnique2() = toMapUnique({}) == ();
  		public test bool toMapUnique3() = Set::toMapUnique({<1, "a">}) == (1 : "a");
  		public test bool toMapUnique4() = Set::toMapUnique({<1, "a">, <2, "b">}) == (1 : "a", 2 : "b");
  		
	  	@expected{MultipleKey}
	  	public void toMapUniqueError1(){
	  		toMapUnique({<1,10>,<1,20>}) == (1:20);		
	  	}
  	
  // testToString 
  
  		public test bool testToString1() = Set::toString({}) == "{}";
  		public test bool testToString2() = toString({}) == "{}";
  		public test bool testToString3() = Set::toString({1}) == "{1}";
  		public test bool testToString4() { S = Set::toString({1, 2}); return (S == "{1,2}") || (S == "{2,1}");}	
  	
  // setExpressions3 
  
	  public test bool setExpressions1() {
	  		value n = 1; 
	  		value s = "string"; 
	  		return set[int] _ := { n } && set[str] _ := { s, s, *{ s, s } };
	  		}
  	
  // Tests related to the correctness of the dynamic types of sets produced by the library functions;
  // incorrect dynamic types make pattern matching fail;
  
  // testDynamicTypes
  
  		public test bool testDynamicTypes1() {set[value] s = {"1",2,3}; return set[int] _ := s - "1";}
  		public test bool testDynamicTypes2() {set[value] s = {"1",2,3}; return set[int] _ := s - {"1"}; }
  		public test bool testDynamicTypes3() {set[value] s = {"1",2,3}; return set[int] _ := s & {2,3};}
  		public test bool testDynamicTypes4() = {"1", *int _} := {"1",2,3}; 
  		
  
  
 