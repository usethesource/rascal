 module lang::rascal::tests::library::String
  /*******************************************************************************
       * Copyright (c) 2009-2015 CWI
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
  
test bool center1() = center("a", 0) == "a";
test bool center2() = center("a", 1) == "a";
test bool center3() = center("a", 2) == "a ";
test bool center4() = center("a", 3) == " a ";
  
test bool center5() = center("ab", 0, "-") == "ab";
test bool center6() = center("ab", 1, "-") == "ab";
test bool center7() = center("ab", 2, "-") == "ab";
test bool center8() = center("ab", 3, "-") == "ab-";
test bool center9() = center("ab", 4, "-") == "-ab-";
  
test bool center10() = center("ab", 3, "-+") == "ab-";
test bool center11() = center("ab", 4, "-+") == "-ab-";
test bool center12() = center("ab", 5, "-+") == "-ab-+";
test bool center13() = center("ab", 6, "-+") == "-+ab-+";
  
// charAt
  
test bool charAt1() = String::charAt("abc", 0) == 97;
test bool charAt2() = String::charAt("abc", 1) == 98;
test bool charAt3() = String::charAt("abc", 2) == 99;
test bool charAt4() = charAt("abc", 0) == 97;
  	
@expected{
IndexOutOfBounds
}
test bool charAtError1() = String::charAt("abc", 3) == 99;
  		
// contains
  
test bool contains1() = contains("abc", "a");
test bool contains2() = contains("abc", "c");
test bool contains3() = !contains("abc", "x");
test bool contains4() = !contains("abc", "xyzpqr");
test bool contains5() = contains("abracadabra", "bra");

// deescape

test bool deescape1() = deescape("\\\"") == "\"";
test bool deescape2() = deescape("\\n") == "\n";
test bool deescape3() = deescape("\\uAA11") == "\uAA11";
test bool deescape4() = deescape("\\U012345") == "\U012345";
test bool deescape5() = deescape("\\a0F") == "\a0f";

  		
// endsWith

test bool endsWith1() = String::endsWith("abc", "abc");
test bool endsWith2() = endsWith("abc", "abc");
test bool endsWith3() = String::endsWith("abcdef", "def");
test bool endsWith4() = !String::endsWith("abcdef", "abc");
  	
// findAll
  
test bool findAll1() = findAll("abc", "a") == [0];
test bool findAll2() = findAll("abc", "c") == [2];
test bool findAll3() = findAll("abc", "x") == [];
test bool findAll4() = findAll("abc", "xyzpqr") == [];
test bool findAll5() = findAll("abracadabra", "bra") == [1, 8];
  	
// findFirst
  
test bool findFirst1() = findFirst("abc", "a") == 0;
test bool findFirst2() = findFirst("abc", "c") == 2;
test bool findFirst3() = findFirst("abc", "x") == -1;
test bool findFirst4() = findFirst("abc", "xyzpqr") == -1;
test bool findFirst5() = findFirst("abracadabra", "bra") == 1;
  	
// findLast
  
test bool findLast1() = findLast("abc", "a") == 0;
test bool findLast2() = findLast("abc", "c") == 2;
test bool findLast3() = findLast("abc", "x") == -1;
test bool findLast4() = findLast("abc", "xyzpqr") == -1;
test bool findLast5() = findLast("abracadabra", "bra") == 8;
  		
// isEmpty
  	
test bool isEmpty1() = isEmpty("");
test bool isEmpty2() = isEmpty("abc") == false;
  
// left
  
test bool left1() = left("a", 0) == "a";
test bool left2() = left("a", 1) == "a";
test bool left3() = left("a", 2) == "a ";
  
test bool left4() = left("ab", 0, "-") == "ab";
test bool left5() = left("ab", 1, "-") == "ab";
test bool left6() = left("ab", 2, "-") == "ab";
test bool left7() = left("ab", 3, "-") == "ab-";
test bool left8() = left("ab", 4, "-") == "ab--";
  
test bool left9()  = left("ab", 3, "-+") == "ab-";
test bool left10() = left("ab", 4, "-+") == "ab-+";
test bool left11() = left("ab", 5, "-+") == "ab-+-";
test bool left12() = left("ab", 6, "-+") == "ab-+-+";
  		
// replaceAll

test bool replaceAll0() = replaceAll("a", "", "A") == "a"; 
test bool replaceAll1() = replaceAll("a", "a", "A") == "A";
test bool replaceAll2() = replaceAll("a", "x", "X") == "a";
test bool replaceAll3() = replaceAll("a", "aa", "A") == "a";
  		
test bool replaceAll4() = replaceAll("abracadabra", "a", "A") == "AbrAcAdAbrA";
test bool replaceAll5() = replaceAll("abracadabra", "a", "A") == "AbrAcAdAbrA";
test bool replaceAll6() = replaceAll("abracadabra", "a", "AA") == "AAbrAAcAAdAAbrAA";
test bool replaceAll7() = replaceAll("abracadabra", "ab", "AB") == "ABracadABra";
  	
// replaceFirst

test bool replaceFirst0() = replaceFirst("a", "", "A") == "a";   
test bool replaceFirst1() = replaceFirst("a", "a", "A") == "A";
test bool replaceFirst2() = replaceFirst("a", "x", "X") == "a";
test bool replaceFirst3() = replaceFirst("a", "aa", "A") == "a";
test bool replaceFirst4() = replaceFirst("abracadabra", "a", "A") == "Abracadabra";
test bool replaceFirst5() = replaceFirst("abracadabra", "a", "AA") == "AAbracadabra";
test bool replaceFirst6() = replaceFirst("abracadabra", "ab", "AB") == "ABracadabra";
  	
// replaceLast
test bool replaceLast0() = replaceLast("a", "", "A") == "a";   
test bool replaceLast1() = replaceLast("a", "a", "A") == "A";
test bool replaceLast2() = replaceLast("a", "x", "X") == "a";
test bool replaceLast3() = replaceLast("a", "aa", "A") == "a";
test bool replaceLast4() = replaceLast("abracadabra", "a", "A") == "abracadabrA";
test bool replaceLast5() = replaceLast("abracadabra", "a", "AA") == "abracadabrAA";
test bool replaceLast6() = replaceLast("abracadabra", "ab", "AB") == "abracadABra";
  
// reverse
  
test bool reverse1() = String::reverse("") == "";
test bool reverse2() = reverse("") == "";
test bool reverse3() = String::reverse("abc") == "cba";
  
// right
  
test bool right1() = right("a", 0) == "a";
test bool right2() = right("a", 1) == "a";
test bool right3() = right("a", 2) == " a";
  
test bool right4() = right("ab", 0, "-") == "ab";
test bool right5() = right("ab", 1, "-") == "ab";
test bool right6() = right("ab", 2, "-") == "ab";
test bool right7() = right("ab", 3, "-") == "-ab";
test bool right8() = right("ab", 4, "-") == "--ab";
  
test bool right9()  = right("ab", 3, "-+") == "-ab";
test bool right10() = right("ab", 4, "-+") == "-+ab";
test bool right11() = right("ab", 5, "-+") == "-+-ab";
test bool right12() = right("ab", 6, "-+") == "-+-+ab";
  
// size
  
test bool size1() = String::size("") == 0;
test bool size2() = size("") == 0;
test bool size3() = String::size("abc") == 3;
  
// startsWith
  
test bool startsWith1() = String::startsWith("abc", "abc");
test bool startsWith2() = startsWith("abc", "abc");
test bool startsWith3() = String::startsWith("abcdef", "abc");
test bool startsWith4() = !String::startsWith("abcdef", "def");
  	
// substring
  
test bool substring1() = substring("abc", 0) == "abc";
test bool substring2() = substring("abc", 1) == "bc";
test bool substring3() = substring("abc", 2) == "c";
test bool substring4() = substring("abc", 3) == "";
test bool substring5() = substring("abc", 1, 2) == "b";
test bool substring6() = substring("abc", 1, 3) == "bc";
  	
		@expected{
IndexOutOfBounds
}
test bool substringWrongIndex1() = substring("abc", 4) == "abc";
  	
		@expected{
IndexOutOfBounds
}
test bool substringWrongIndex2() = substring("abc", 1, 4) == "abc";
  		
// toInt
  
test bool toInt1() = toInt("0") == 0;
test bool toInt2() = toInt("1") == 1;
test bool toInt3() = toInt("0001") == 1;
test bool toInt4() = toInt("-1") == -1;
test bool toInt5() = toInt("12345") == 12345;
 
		@expected{
IllegalArgument
}
test bool toIntError1() = toInt("abc") == 0;
  
// toLowerCase
  
test bool toLowerCase1() = String::toLowerCase("") == "";
test bool toLowerCase2() = toLowerCase("") ==  "";
test bool toLowerCase3() = String::toLowerCase("ABC") == "abc";
test bool toLowerCase4() = String::toLowerCase("ABC123") == "abc123";
  		
// toReal
  
test bool toReal1() = toReal("0.0") == 0.0;
test bool toReal2() = toReal("1.0") == 1.0;
test bool toReal3() = toReal("0001.0") == 1.0;
test bool toReal4() = toReal("-1.0") == -1.0;
test bool toReal5() = toReal("1.2345") == 1.2345;
  	
@expected{
IllegalArgument
}
test bool toRealError1() = toReal("abc") == 0;
  
// toUpperCase
  
test bool toUpperCase1() = String::toUpperCase("") == "";
test bool toUpperCase2() = toUpperCase("") == "";
test bool toUpperCase3() = String::toUpperCase("abc") == "ABC";
test bool toUpperCase4() = String::toUpperCase("abc123") == "ABC123";
  	
  
  	
 
  	
  
 
