 module tests::library::MapTests
  /*******************************************************************************
   * Copyright (c) 2009-2013 CWI
   * All rights reserved. This program and the accompanying materials
   * are made available under the terms of the Eclipse Public License v1.0
   * which accompanies this distribution, and is available at
   * http://www.eclipse.org/legal/epl-v10.html
   *
   * Contributors:
  
   *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
   *   * Paul Klint - Paul.Klint@cwi.nl - CWI
   *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
   *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
   *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
  *******************************************************************************/
  import Map;
  
  // getOneFrom
  
  		public test bool getOneFrom1()=getOneFrom((1:10)) == 1;
  		public test bool getOneFrom2(){int N = getOneFrom((1:10, 2:20)); return (N == 1) || (N ==2);}
  	
  // getOneFromError1
  
  		@expected{EmptyMap}
  		public test bool getOneFrom3()=getOneFrom(());
  	
  // invertUnique
  
  		public test bool invertUnique1()=invertUnique(()) == ();
  		public test bool invertUnique2()=invertUnique((1:10)) == (10:1);
  		public test bool invertUnique3()=invertUnique((1:10, 2:20)) == (10:1, 20:2);
  		public test bool invertUnique4()=invertUnique(([[]]:0,[[2]]:2,[[1,2],[2,1]]:1,[[1]]:3)) == (0:[[]],2:[[2]],1:[[1,2],[2,1]],3:[[1]]);
  	
  // invertError
  
  		@expected{MultipleKey}
  		public test bool invertError1() { invertUnique((1:10, 2:10)); return true; }
  	
  // invert
  
  		public test bool invert1()=invert(()) == ();
  		public test bool invert2()=invert((1:10)) == (10:{1});
  		public test bool invert3()=invert((1:10, 2:20)) == (10:{1}, 20:{2});
  		public test bool invert4()=invert((1:10, 2:10, 3:30, 4:30)) == (10: {1,2}, 30:{3,4});	
  	
  // isEmpty
  
  		public test bool isEmpty1()=isEmpty(());
  		public test bool isEmpty2()=isEmpty((1:10)) == false;
  
  // domain
  
  		public test bool domain1()=domain(()) == {};
  		public test bool domain2()=domain((1:10, 2:20)) == {1,2};
  
  int inc(int n) {return n + 1;}
  int dec(int n) {return n - 1;}
  
  // mapper
  
  		public test bool mapper1() =  mapper((), inc, inc) == ();
  		public test bool mapper2() =  mapper((1:10,2:20), inc, inc) == (2:11,3:21);
  		public test bool mapper3() =  mapper((), inc, dec) == ();
  		public test bool mapper4() =  mapper((1:10,2:20), inc, dec) == (2:9,3:19);
  
  // range
  
  		public test bool range1()=range(()) == {};
  		public test bool range2()=range((1:10, 2:20)) == {10,20};
  
  // size
  
  		public test bool size1()=size(()) == 0;
  		public test bool size2()=size((1:10)) == 1;
  		public test bool size3()=size((1:10,2:20)) == 2;
  
  // toList

  		public test bool toList1()=toList(()) == [];
  		public test bool toList2()=toList((1:10)) == [<1,10>];
  		public test bool toList3(){list[tuple[int,int]] L = toList((1:10, 2:20)); return (L == [<1,10>,<2,20>]) || (L == [<2,20>,<1,10>]);}
  
  // toRel
  
  		public test bool toRel1()=toRel(()) == {};
  		public test bool toRel2()=toRel((1:10)) == {<1,10>};
  		public test bool toRel3(){rel[int,int] R = toRel((1:10, 2:20)); return R == {<1,10>,<2,20>};}
  
  // toString
  
  		public test bool toString1()=toString(()) == "()";
  		public test bool toString2()=toString((1:10)) == "(1:10)";
 
  	
  // mapExpressions
  
  		public test bool mapExpressions1() { value n = 1; value s = "string"; return map[int, int] _ := ( n : n ) && map[str, str] _ := ( s : s ) && map[int, str] _ := ( n : s ); }
 
  	
  	// Tests related to the correctness of the dynamic types of maps produced by the library functions;
  	// incorrect dynamic types make pattern matching fail;
  
  // testDynamicTypes
  
  		public test bool testDynamicTypes1() { map[value a, value b] m = ("1":"1",2:2,3:3); return map[int, int] _ := m - ("1":"1") && (m - ("1":"1")).a == {2,3} && (m - ("1":"1")).b == {2,3}; }
  		public test bool testDynamicTypes2() { map[value a, value b] m1 = ("1":"1",2:2,3:3); map[value a, value b] m2 = (2:2,3:3); return map[int, int] _ := m1 & m2 && (m1 & m2).a == {2,3} && (m2 & m1).b == {2,3}; }

  
 