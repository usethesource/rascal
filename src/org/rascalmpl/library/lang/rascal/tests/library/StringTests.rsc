 module tests::library::StringTests
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
  
  import String;
  
  // center
  
  		public test bool center1()=center("a", 0) == "a";
  		public test bool center2()=center("a", 1) == "a";
  		public test bool center3()=center("a", 2) == "a ";
  		public test bool center4()=center("a", 3) == " a ";
  
  		public test bool center5()=center("ab", 0, "-") == "ab";
  		public test bool center6()=center("ab", 1, "-") == "ab";
  		public test bool center7()=center("ab", 2, "-") == "ab";
  		public test bool center8()=center("ab", 3, "-") == "ab-";
  		public test bool center9()=center("ab", 4, "-") == "-ab-";
  
  		public test bool center10()=center("ab", 3, "-+") == "ab-";
  		public test bool center11()=center("ab", 4, "-+") == "-ab-";
  		public test bool center12()=center("ab", 5, "-+") == "-ab-+";
  		public test bool center13()=center("ab", 6, "-+") == "-+ab-+";
  
  // charAt
  
  		public test bool charAt1()=String::charAt("abc", 0) == 97;
  		public test bool charAt2()=String::charAt("abc", 1) == 98;
  		public test bool charAt3()=String::charAt("abc", 2) == 99;
  		public test bool charAt4()=charAt("abc", 0) == 97;
  	
  		@expected{IndexOutOfBounds}
  		public test bool charAtError1()=String::charAt("abc", 3) == 99;
  		
  		 // contains
  
  		public test bool contains1()=contains("abc", "a");
  		public test bool contains2()=contains("abc", "c");
  		public test bool contains3()=!contains("abc", "x");
  		public test bool contains4()=!contains("abc", "xyzpqr");
  		public test bool contains5()=contains("abracadabra", "bra");
  		
  // endsWith

  		public test bool endsWith1()=String::endsWith("abc", "abc");
  		public test bool endsWith2()=endsWith("abc", "abc");
  		public test bool endsWith3()=String::endsWith("abcdef", "def");
  		public test bool endsWith4()=!String::endsWith("abcdef", "abc");
  	
  // findAll
  
  		public test bool findAll1()=findAll("abc", "a") == [0];
  		public test bool findAll2()=findAll("abc", "c") == [2];
  		public test bool findAll3()=findAll("abc", "x") == [];
  		public test bool findAll4()=findAll("abc", "xyzpqr") == [];
  		public test bool findAll5()=findAll("abracadabra", "bra") == [1, 8];
  	
  // findFirst
  
  		public test bool findFirst1()=findFirst("abc", "a") == 0;
  		public test bool findFirst2()=findFirst("abc", "c") == 2;
  		public test bool findFirst3()=findFirst("abc", "x") == -1;
  		public test bool findFirst4()=findFirst("abc", "xyzpqr") == -1;
  		public test bool findFirst5()=findFirst("abracadabra", "bra") == 1;
  	
  // findLast
  
  		public test bool findLast1()=findLast("abc", "a") == 0;
  		public test bool findLast2()=findLast("abc", "c") == 2;
  		public test bool findLast3()=findLast("abc", "x") == -1;
  		public test bool findLast4()=findLast("abc", "xyzpqr") == -1;
  		public test bool findLast5()=findLast("abracadabra", "bra") == 8;
  
  // left
  
  		public test bool left1()=left("a", 0) == "a";
  		public test bool left2()=left("a", 1) == "a";
  		public test bool left3()=left("a", 2) == "a ";
  
  		public test bool left4()=left("ab", 0, "-") == "ab";
  		public test bool left5()=left("ab", 1, "-") == "ab";
  		public test bool left6()=left("ab", 2, "-") == "ab";
  		public test bool left7()=left("ab", 3, "-") == "ab-";
  		public test bool left8()=left("ab", 4, "-") == "ab--";
  
  		public test bool left9()=left("ab", 3, "-+") == "ab-";
  		public test bool left10()=left("ab", 4, "-+") == "ab-+";
  		public test bool left11()=left("ab", 5, "-+") == "ab-+-";
  		public test bool left12()=left("ab", 6, "-+") == "ab-+-+";
  	
  // isEmpty
  	
  		public test bool isEmpty1()=isEmpty("");
  		public test bool isEmpty2()=isEmpty("abc") == false;
  
  // reverse
  
  		public test bool reverse1()=String::reverse("") == "";
  		public test bool reverse2()=reverse("") == "";
  		public test bool reverse3()=String::reverse("abc") == "cba";
  
  // right
  
  		public test bool right1()=right("a", 0) == "a";
  		public test bool right2()=right("a", 1) == "a";
  		public test bool right3()=right("a", 2) == " a";
  
  		public test bool right4()=right("ab", 0, "-") == "ab";
  		public test bool right5()=right("ab", 1, "-") == "ab";
  		public test bool right6()=right("ab", 2, "-") == "ab";
  		public test bool right7()=right("ab", 3, "-") == "-ab";
  		public test bool right8()=right("ab", 4, "-") == "--ab";
  
  		public test bool right9()=right("ab", 3, "-+") == "-ab";
  		public test bool right10()=right("ab", 4, "-+") == "-+ab";
  		public test bool right11()=right("ab", 5, "-+") == "-+-ab";
  		public test bool right12()=right("ab", 6, "-+") == "-+-+ab";
  
  // size
  
  		public test bool size1()=String::size("") == 0;
  		public test bool size2()=size("") == 0;
  		public test bool size3()=String::size("abc") == 3;
  
  // startsWith
  
  		public test bool startsWith1()=String::startsWith("abc", "abc");
  		public test bool startsWith2()=startsWith("abc", "abc");
  		public test bool startsWith3()=String::startsWith("abcdef", "abc");
  		public test bool startsWith4()=!String::startsWith("abcdef", "def");
  	
  // substring
  
  		public test bool substring1()=substring("abc", 0) == "abc";
  		public test bool substring2()=substring("abc", 1) == "bc";
  		public test bool substring3()=substring("abc", 2) == "c";
  		public test bool substring4()=substring("abc", 3) == "";
  		public test bool substring5()=substring("abc", 1, 2) == "b";
  		public test bool substring6()=substring("abc", 1, 3) == "bc";
  	
  		@expected{IndexOutOfBounds}
  		public test bool substringWrongIndex1()=substring("abc", 4) == "abc";
  	
  		@expected{IndexOutOfBounds}
  		public test bool substringWrongIndex2()=substring("abc", 1, 4) == "abc";
  
  // toLowerCase
  
  		public test bool toLowerCase1()=String::toLowerCase("") == "";
  		public test bool toLowerCase2()=toLowerCase("") ==  "";
  		public test bool toLowerCase3()=String::toLowerCase("ABC") == "abc";
  		public test bool toLowerCase4()=String::toLowerCase("ABC123") == "abc123";
  
  // toUpperCase
  
  		public test bool toUpperCase1()=String::toUpperCase("") == "";
  		public test bool toUpperCase2()=toUpperCase("") == "";
  		public test bool toUpperCase3()=String::toUpperCase("abc") == "ABC";
  		public test bool toUpperCase4()=String::toUpperCase("abc123") == "ABC123";
  	
  // toInt
  
  		public test bool toInt1()=toInt("0") == 0;
  		public test bool toInt2()=toInt("1") == 1;
  		public test bool toInt3()=toInt("0001") == 1;
  		public test bool toInt4()=toInt("-1") == -1;
  		public test bool toInt5()=toInt("12345") == 12345;
  	
 
  		@expected{IllegalArgument}
  		public test bool toIntError1()=toInt("abc") == 0;
  	
  // toReal
  
  		public test bool toReal1()=toReal("0.0") == 0.0;
  		public test bool toReal2()=toReal("1.0") == 1.0;
  		public test bool toReal3()=toReal("0001.0") == 1.0;
  		public test bool toReal4()=toReal("-1.0") == -1.0;
  		public test bool toReal5()=toReal("1.2345") == 1.2345;
  	
   		@expected{IllegalArgument}
  		public test bool toRealError1()=toReal("abc") == 0;
  	
  // replaceAll
  
  		public test bool replaceAll1()=replaceAll("a", "a", "A") == "A";
  		public test bool replaceAll2()=replaceAll("a", "x", "X") == "a";
  		public test bool replaceAll3()=replaceAll("a", "aa", "A") == "a";
  		
  		public test bool replaceAll4()=replaceAll("abracadabra", "a", "A") == "AbrAcAdAbrA";
  		public test bool replaceAll5()=replaceAll("abracadabra", "a", "A") == "AbrAcAdAbrA";
  		public test bool replaceAll6()=replaceAll("abracadabra", "a", "AA") == "AAbrAAcAAdAAbrAA";
  		public test bool replaceAll7()=replaceAll("abracadabra", "ab", "AB") == "ABracadABra";
  	
  // replaceFirst
  
  		public test bool replaceFirst1()=replaceFirst("a", "a", "A") == "A";
  		public test bool replaceFirst2()=replaceFirst("a", "x", "X") == "a";
  		public test bool replaceFirst3()=replaceFirst("a", "aa", "A") == "a";
  		public test bool replaceFirst4()=replaceFirst("abracadabra", "a", "A") == "Abracadabra";
  		public test bool replaceFirst5()=replaceFirst("abracadabra", "a", "AA") == "AAbracadabra";
  		public test bool replaceFirst6()=replaceFirst("abracadabra", "ab", "AB") == "ABracadabra";
  	
  // replaceLast
  
  		public test bool replaceLast1()=replaceLast("a", "a", "A") == "A";
  		public test bool replaceLast2()=replaceLast("a", "x", "X") == "a";
  		public test bool replaceLast3()=replaceLast("a", "aa", "A") == "a";
  		public test bool replaceLast4()=replaceLast("abracadabra", "a", "A") == "abracadabrA";
  		public test bool replaceLast5()=replaceLast("abracadabra", "a", "AA") == "abracadabrAA";
  		public test bool replaceLast6()=replaceLast("abracadabra", "ab", "AB") == "abracadABra";
  	
 