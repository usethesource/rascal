  module tests::functionality::DataTypeTests
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
    
    // bool
    	
    		public test bool testBool1() = true == true;
    		public test bool testBool2() = !(true == false);
    		public test bool testBool3() = true != false;	
    
    		public test bool testBool4() = (!true) == false;
    		public test bool testBool5() = (!false) == true;
    		
    		public test bool testBool6() = (true && true) == true;	
    		public test bool testBool7() = (true && false) == false;	
    		public test bool testBool8() = (false && true) == false;	 
    		public test bool testBool9() = (false && false) == false;	
    		
    		public test bool testBool10() = (true || true) == true;	
    		public test bool testBool11() = (true || false) == true;	
    		public test bool testBool12() = (false || true) == true;	
    		public test bool testBool13() = (false || false) == false;	
    		
    		public test bool testBool14() = (true ==> true) == true;	
    		public test bool testBool15() = (true ==> false) == false;	
    		public test bool testBool16() = (false ==> true)  == true;	
    		public test bool testBool17() = (false ==> false) == true;
    		
    		public test bool testBool18() = (true <==> true) == true;	
    		public test bool testBool19() = (true <==> false) == false;	
    		public test bool testBool20() = (false <==> true) == false;	
    		public test bool testBool21() = (false <==> false) == true;
    		
    		public test bool testBool22() = false  <= false;
    		public test bool testBool23() = false  <= true;
    		public test bool testBool24() = !(true  <= false);
    		public test bool testBool25() = true   <= true;
    		
    		public test bool testBool26() = !(false < false);
    		public test bool testBool27() = false  < true;
    		public test bool testBool28() = !(true  < false);
    		public test bool testBool29() = !(true  < true);
    		
    		public test bool testBool30() = false  >= false;
    		public test bool testBool31() = true   >= false;
    		public test bool testBool32() = !(false >= true);
    		public test bool testBool33() = true   >= true;
    		
    		public test bool testBool34() = !(false > false);
    		public test bool testBool35() = true   > false;
    		public test bool testBool36() = !(false > true);
    		public test bool testBool37() = !(true   > true);
    
    //	testInt
    
    		public test bool testInt1() = 1 == 1;
    		public test bool testInt2() = 1 != 2;
    		
    		public test bool testInt3() = -1 == -1;
    		public test bool testInt4() = -1 != 1;
    		
    		public test bool testInt5() = 1 + 1 == 2;
    		public test bool testInt6() = -1 + 2 == 1;
    		public test bool testInt7() = 1 + (-2) == -1;
    		
    		public test bool testInt8() = 2 - 1 == 1;	
    		public test bool testInt9() = 2 - 3 == -1;	
    		public test bool testInt10() = 2 - -1 == 3;	
    		public test bool testInt11() = -2 - 1 == -3;	
    		
    		public test bool testInt12() = 2 * 3 == 6;	
    		public test bool testInt13() = -2 * 3 == -6;	
    		public test bool testInt14() = 2 * (-3) == -6;
    		public test bool testInt15() = -2 * (-3) == 6;	
    		
    		public test bool testInt16() = 8 / 4 == 2;	
    		public test bool testInt17() = -8 / 4 == -2;
    		public test bool testInt18() = 8 / -4 == -2;	
    		public test bool testInt19() = -8 / -4 == 2;
    		
    		public test bool testInt20() = 7 / 2 == 3;	
    		public test bool testInt21() = -7 / 2 == -3;
    		public test bool testInt22() = 7 / -2 == -3;	
    		public test bool testInt23() = -7 / -2 == 3;	
    		
    		public test bool testInt24() = 0 / 5 == 0;	
    		public test bool testInt25() = 5 / 1 == 5;	
    		
    		public test bool testInt26() = 5 % 2 == 1;	
    		public test bool testInt27() = -5 % 2 == -1;
    		public test bool testInt28() = 5 % -2 == 1;		
    		
    		public test bool testInt29() = -2 <= -1;
    		public test bool testInt30() = -2 <= 1;
    		public test bool testInt31() = 1 <= 2;
    		public test bool testInt32() = 2 <= 2;
    		public test bool testInt33() = !(2 <= 1);
    		
    		public test bool testInt34() = -2 < -1;
    		public test bool testInt35() = -2 < 1;
    		public test bool testInt36() = 1 < 2;
    		public test bool testInt37() = !(2 < 2);
    		
    		public test bool testInt38() = -1 >= -2;
    		public test bool testInt39() = 1 >= -1;
    		public test bool testInt40() = 2 >= 1;
    		public test bool testInt41() = 2 >= 2;
    		public test bool testInt42() = !(1 >= 2);
    		
    		public test bool testInt43() = -1 > -2;
    		public test bool testInt44() = 1 > -1;
    		public test bool testInt45() = 2 > 1;
    		public test bool testInt46() = !(2 > 2);
    		public test bool testInt47() = !(1 > 2);
    		
    		public test bool testInt48() = (3 > 2 ? 3 : 2) == 3;
    	
    //	valueEquals
    
    		public test bool assertTrue1()  {value x = 1.0; value y = 2; return x != y; }
    	
    //	testReal	
    
    		public test bool testReal1() = 1.0 == 1.0;
    		public test bool testReal2() = 1.0 != 2.0;
    		
    		public test bool testReal3() = -1.0 == -1.0;
    		public test bool testReal4() = -1.0 != 1.0;
    		
    		public test bool testReal5() = 1.0 == 1;
    		public test bool testReal6() = 1.00 == 1.0;
    		public test bool testReal7() = 1 == 1.0;
    		
    		public test bool testReal8()  {value x = 1.0; value y = 1; return x == y; }
    		public test bool testReal9() {value x = 1.0; value y = 2; return x != y; }
    		
    		public test bool testReal10() = 1.0 + 1.0 == 2.0;
    		public test bool testReal11() = -1.0 + 2.0 == 1.0;
    		public test bool testReal12() = 1.0 + (-2.0) == -1.0;
    		
    		public test bool testReal13() = 1.0 + 1 == 2.0;
    		public test bool testReal14() = -1 + 2.0 == 1.0;
    		public test bool testReal15() = 1.0 + (-2) == -1.0;
    		
    		public test bool testReal16() = 2.0 - 1.0 == 1.0;	
    		public test bool testReal17() = 2.0 - 3.0 == -1.0;	
    		public test bool testReal18() = 2.0 - -1.0 == 3.0;	
    		public test bool testReal19() = -2.0 - 1.0 == -3.0;
    		
    		public test bool testReal20() = 2.0 - 1 == 1.0;	
    		public test bool testReal21() = 2 - 3.0 == -1.0;	
    		public test bool testReal22() = 2.0 - -1 == 3.0;	
    		public test bool testReal23() = -2 - 1.0 == -3.0;
    		
    		public test bool testReal24() = 2.0 * 3.0 == 6.00;	
    		public test bool testReal25() = -2.0 * 3.0 == -6.00;	
    		public test bool testReal26() = 2.0 * (-3.0) == -6.00;
    		public test bool testReal27() = -2.0 * (-3.0) == 6.00;	
    		
    		public test bool testReal28() = 2.0 * 3 == 6.0;	
    		public test bool testReal29() = -2 * 3.0 == -6.0;	
    		public test bool testReal30() = 2.0 * (-3) == -6.0;
    		public test bool testReal31() = -2 * (-3.0) == 6.0;	
    		
    		public test bool testReal32() = 8.0 / 4.0 == 2e0;	
    		public test bool testReal33() = -8.0 / 4.0 == -2e0;
    		public test bool testReal34() = 8.0 / -4.0 == -2e0;	
    		public test bool testReal35() = -8.0 / -4.0 == 2e0;
    		
    		// TODO, I don't get it, why does the previous have 1 digit precision and this
    		// one two digits
    		public test bool testReal36() = 7.0 / 2.0 == 3.5;	
    		public test bool testReal37() = -7.0 / 2.0 == -3.5;
    		public test bool testReal38() = 7.0 / -2.0 == -3.5;	
    		public test bool testReal39() = -7.0 / -2.0 == 3.5;	
    		
    		public test bool testReal40() = 0.0 / 5.0 == 0.0;	
    		public test bool testReal41() = 5.0 / 1.0 == 5.0;	
    		
    		public test bool testReal42() = 7 / 2.0 == 3.5;	
    		public test bool testReal43() = -7.0 / 2 == -3.5;
    		public test bool testReal44() = 7 / -2.0 == -3.5;	
    		public test bool testReal45() = -7.0 / -2 == 3.5;	
    		
    		public test bool testReal46() = -2.0 <= -1.0;
    		public test bool testReal47() = -2.0 <= 1.0;
    		public test bool testReal48() = 1.0 <= 2.0;
    		public test bool testReal49() = 2.0 <= 2.0;
    		public test bool testReal50() = !(2.0 <= 1.0);
    		
    		public test bool testReal51() = -2 <= -1.0;
    		public test bool testReal52() = -2.0 <= 1;
    		public test bool testReal53() = 1 <= 2.0;
    		public test bool testReal54() = 2.0 <= 2;
    		public test bool testReal55() = !(2 <= 1.0);
    		
    		public test bool testReal56() = -2.0 < -1.0;
    		public test bool testReal57() = -2.0 < 1.0;
    		public test bool testReal58() = 1.0 < 2.0;
    		public test bool testReal59() = !(2.0 < 2.0);
    		
    		public test bool testReal60() = -2 < -1.0;
    		public test bool testReal61() = -2.0 < 1;
    		public test bool testReal62() = 1 < 2.0;
    		public test bool testReal63() = !(2.0 < 2);
    		
    		public test bool testReal64() = -1.0 >= -2.0;
    		public test bool testReal65() = 1.0 >= -1.0;
    		public test bool testReal66() = 2.0 >= 1.0;
    		public test bool testReal67() = 2.0 >= 2.0;
    		public test bool testReal68() = !(1.0 >= 2.0);
    		
    		public test bool testReal69() = -1 >= -2.0;
    		public test bool testReal70() = 1.0 >= -1;
    		public test bool testReal71() = 2 >= 1.0;
    		public test bool testReal72() = 2.0 >= 2;
    		public test bool testReal73() = !(1 >= 2.0);
    		
    		public test bool testReal74() = -1.0 > -2.0;
    		public test bool testReal75() = 1.0 > -1.0;
    		public test bool testReal76() = 2.0 > 1.0;
    		public test bool testReal77() = !(2.0 > 2.0);
    		public test bool testReal78() = !(1.0 > 2.0);
    		
    		public test bool testReal79() = -1 > -2.0;
    		public test bool testReal80() = 1.0 > -1;
    		public test bool testReal81() = 2 > 1.0;
    		public test bool testReal82() = !(2.0 > 2);
    		public test bool testReal83() = !(1 > 2.0);
    		
    		public test bool testReal84() = ((3.5 > 2.5) ? 3.5 : 2.5) == 3.5;
    		
    		public test bool testReal85() = ((3.5 > 2) ? 3.5 : 2) == 3.5;
    		public test bool testReal86() = ((3.5 > 4) ? 3.5 : 2) == 2;
    	
    //	testNumber
    
    		public test bool testNumber1() {num n = 1; return n == 1;}
    		public test bool testNumber2() {num n = 1;  return 1 == n;}
    		
    		public test bool testNumber3() {num n = 1;  return n != 2;}
    		public test bool testNumber4() {num n = 1;  return 2 != n;}
    		
    		
    		public test bool testNumber5() {num n = 1;  return n + 1 == 2;}
    		public test bool testNumber6() {num n = 1;  return 1 + n == 2;}
    		
    		public test bool testNumber7() {num n = 2; return n - 1 == 1;}	
    		public test bool testNumber8() {num n = 2; return 1 - n == -1;}	
    		
    		
    		public test bool testNumber9() {num n = 2; return n * 3 == 6;}
    		public test bool testNumber10() {num n = 2; return 3 * n == 6;}
    		
    		public test bool testNumber11() {num n = 8; return n / 4 == 2;}	
    		public test bool testNumber12() {num n = 4; return 8 / n == 2;}	
    	
    		public test bool testNumber13() {num n = 1; return n <= 2;}
    		public test bool testNumber14() {num n = 1; return 0 <= n;}
    		
    		public test bool testNumber15() {num n = 1; return n < 2;} 
    		public test bool testNumber16() {num n = 1; return 0 < n;}
    		
    		public test bool testNumber17() {num n = 2; return n >= 1;}
    		public test bool testNumber18() {num n = 1; return 2 >= n;}
    		
    		public test bool testNumber19() {num n = 2; return n > 1;}         
    		public test bool testNumber20() {num n = 1; return 2 > n;}
    		
    		public test bool testNumber21() {num n = 1; return 2 > n;}
    		
    		public test bool testNumber22() {num n35 = 3.5; num n2 = 2; return ((n35 > n2) ? 3.5 : 2) == 3.5;}
    	
    //	testString
    
    		public test bool testString1() = "" == "";
    		public test bool testString2() = "abc" != "";
    		public test bool testString3() = "abc" == "abc";
    		public test bool testString4() = "abc" != "def";
    		
    		public test bool testString5() = "abc" + "" == "abc";
    		public test bool testString6() = "abc" + "def" == "abcdef";
    		
    		public test bool testString7() = "" <= "";
    		public test bool testString8() = "" <= "abc";
    		public test bool testString9() = "abc" <= "abc";
    		public test bool testString10() = "abc" <= "def";
    		
    		public test bool testString11() = !("" < "");
    		public test bool testString12() = "" < "abc";
    		public test bool testString13() = !("abc" < "abc");
    		public test bool testString14() = "abc" < "def";
    		
    		public test bool testString15() = "" >= "";
    		public test bool testString16() = "abc" >= "";
    		public test bool testString17() = "abc" >= "abc";
    		public test bool testString18() = "def" >= "abc";
    		
    		public test bool testString19() = !("" > "");
    		public test bool testString20() = "abc" > "";
    		public test bool testString21() = !("abc" > "abc");
    		public test bool testString22() = "def" > "abc";
    	
    //	stringEscapes
    
    		public test bool testStringEscapes1() = "\\b" == "\\b";
    		public test bool testStringEscapes2() = "\\t" == "\\t";
    		public test bool testStringEscapes3() = "\\n" == "\\n";
    		public test bool testStringEscapes4() = "\\f" == "\\f";
    		public test bool testStringEscapes5() = "\\r" == "\\r";
    		
    		public test bool testStringEscapes6() = "\"\"" == "\"\"";
    		public test bool testStringEscapes7() = "\\\'" == "\\\'";
    		public test bool testStringEscapes8() = "\\\\" == "\\\\";
    		public test bool testStringEscapes9() = "\"\<" == "\"\<";
    		public test bool testStringEscapes10() = "\"\>" == "\"\>";
    		
    		public test bool testStringEscapes11() = "\a20" == " ";
    		public test bool testStringEscapes12() = "\U01F35D" == "🍝";
    		public test bool testStringEscapes13() = "\u2713" == "✓";
    	
    //	stringInterpolation
    
    		public test bool testStringInterpolation1() {str a = "abc"; return "1<a>2" == "1abc2";}
    		public test bool testStringInterpolation2() {int a = 789; return "1<a>2" == "17892";}
    		
    		public test bool testStringInterpolation3() {str a = "a\\bc"; return "1<a>2" == "1a\\bc2";}
    		public test bool testStringInterpolation4() {str a = "a\\tc"; return "1<a>2" == "1a\\tc2";}
    		public test bool testStringInterpolation5() {str a = "a\\nc"; return "1<a>2" == "1a\\nc2";}
    		public test bool testStringInterpolation6() {str a = "a\\fc"; return "1<a>2" == "1a\\fc2";}
    		public test bool testStringInterpolation7() {str a = "a\\rc"; return "1<a>2" == "1a\\rc2";}
    		
    		public test bool testStringInterpolation8() {str a = "a\\\"c"; return "1<a>2" == "1a\\\"c2";}
    		public test bool testStringInterpolation9() {str a = "a\\\'c"; return "1<a>2" == "1a\\\'c2";}
    		public test bool testStringInterpolation10() {str a = "a\\\\c"; return "1<a>2" == "1a\\\\c2";}
    		
    		public test bool testStringInterpolation11() {str a = "a\<c"; return "1<a>2" == "1a\<c2";}
    		public test bool testStringInterpolation12() {str a = "a\>c"; return "1<a>2" == "1a\>c2";}
    	
    loc Loc = |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>);
    loc Loc2 = |file:///home/paulk/pico2.trm|(0,1,<2,3>,<4,5>);	
    
    // testLocation    		
    		
    		public test bool testLocation1() {Loc ; return true;}
    		public test bool testLocation2() =  Loc == Loc;
    		public test bool testLocation3() = Loc != Loc2;
    		
    		public test bool testLocationFieldUse1() = Loc.uri == "file:///home/paulk/pico.trm";
    		public test bool testLocationFieldUse2() = Loc.offset == 0;
    		public test bool testLocationFieldUse3() = Loc.length == 1;
    		public test bool testLocationFieldUse5() = Loc.begin.line == 2;
    		public test bool testLocationFieldUse6() = Loc.begin.column == 3;
    		public test bool testLocationFieldUse7() = Loc.end.line == 4;
    		public test bool testLocationFieldUse8() = Loc.end.column == 5;
    		public test bool testLocationFieldUse9() = Loc.path == "/home/paulk/pico.trm";
    		
    		public test bool testLocationFieldUpdate1() { loc l = Loc; l.uri = "file:///home/paulk/pico2.trm"; l.uri == "file:///home/paulk/pico2.trm";}
    		public test bool testLocationFieldUpdate2() { loc l = Loc; l.offset = 10; l.offset == 10;}
    		public test bool testLocationFieldUpdate3() { loc l = Loc; l.length = 11; l.length == 11;}
    		public test bool testLocationFieldUpdate4() { loc l = Loc; l.end.line = 14; l.end.line == 14;}
    		public test bool testLocationFieldUpdate5() { loc l = Loc; l.begin.line = 1; l.begin.line == 1;}
    		public test bool testLocationFieldUpdate6() { loc l = Loc; l.begin.column = 13; l.begin.column == 13;}
    		public test bool testLocationFieldUpdate7() { loc l = Loc; l.end.column = 15; l.end.column == 15;}
    		
    		public test bool testLocationFieldUpdate8() {loc l = Loc[uri= "file:///home/paulk/pico.trm"]; l == |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>);}
    		public test bool testLocationFieldUpdate9() {loc l = Loc[offset = 10]; l == |file:///home/paulk/pico.trm|(10,1,<2,3>,<4,5>);}
    		public test bool testLocationFieldUpdate10() {loc l = Loc[length = 11]; l ==  |file:///home/paulk/pico.trm|(0,11,<2,3>,<4,5>);}
    		public test bool testLocationFieldUpdate12() {loc l = Loc[begin = <1,4>]; l == |file:///home/paulk/pico.trm|(0,1,<1,4>,<4,5>);}
    		public test bool testLocationFieldUpdate13() {loc l = Loc[end = <14,38>]; l ==  |file:///home/paulk/pico.trm|(0,1,<2,3>,<14,38>);}
    	
    		public test bool testLocation12() = |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>) == |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>);
    		public test bool testLocation13() = !(|file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>) == |file:///home/paulk/pico.trm|(0,2,<2,3>,<4,5>));
    		public test bool testLocation14() = !(|file:///home/paulk/pico1.trm|(0,1,<2,3>,<4,5>) == |file:///home/paulk/pico2.trm|(0,1,<2,3>,<4,5>));
    		
    		public test bool testLocation15() = |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>) != |file:///home/paulk/pico2.trm|(0,1,<2,3>,<4,5>);
    		public test bool testLocation16() = |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>) != |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,7>);
    		public test bool testLocation17() = !(|file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>) != |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>));
    		public test bool testLocation18() = |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) != |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,7>);
    		
    		public test bool testLocation19() = !(|file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) < |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>));
    		public test bool testLocation20() = |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) < |file:///home/paulk/pico.trm|(2,2,<2,3>,<4,5>);
    		public test bool testLocation21() = !(|file:///home/paulk/pico.trm|(1,1,<2,3>,<4,5>) < |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>));
    		public test bool testLocation22() = !(|file:///home/paulk/pico.trm|(1,2,<2,3>,<4,5>) < |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>));
    		
    		
    		public test bool testLocation23() = |file:///home/paulk/pico1.trm|(2,1,<2,3>,<4,5>) <= |file:///home/paulk/pico2.trm|(2,1,<2,3>,<4,5>);
    		public test bool testLocation24() = !(|file:///home/paulk/pico2.trm|(2,1,<2,3>,<4,5>) <= |file:///home/paulk/pico1.trm|(2,1,<2,3>,<4,5>));
    		public test bool testLocation25() = |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) <= |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>);
    		public test bool testLocation26() = |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) <= |file:///home/paulk/pico.trm|(2,2,<2,3>,<4,5>);
    		public test bool testLocation27() = !(|file:///home/paulk/pico.trm|(2,2,<2,3>,<4,5>) <= |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>));
    		
    		
    		public test bool testLocation28() = |file:///home/paulk/pico2.trm|(2,1,<2,3>,<4,5>) > |file:///home/paulk/pico1.trm|(2,1,<2,3>,<4,5>);
    		public test bool testLocation29() = !(|file:///home/paulk/pico1.trm|(2,1,<2,3>,<4,5>) > |file:///home/paulk/pico2.trm|(2,1,<2,3>,<4,5>));
    		public test bool testLocation30() = !(|file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) > |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>));
    		public test bool testLocation31() = |file:///home/paulk/pico.trm|(2,2,<2,3>,<4,5>) > |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>);
    		public test bool testLocation32() = !(|file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) > |file:///home/paulk/pico.trm|(2,2,<2,3>,<4,5>));
    		
    		public test bool testLocation33() = |file:///home/paulk/pico2.trm|(2,1,<2,3>,<4,5>) >= |file:///home/paulk/pico1.trm|(2,1,<2,3>,<4,5>);
    		public test bool testLocation34() = !(|file:///home/paulk/pico1.trm|(2,1,<2,3>,<4,5>) >= |file:///home/paulk/pico2.trm|(2,1,<2,3>,<4,5>));
    		public test bool testLocation35() = |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) >= |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>);
    		public test bool testLocation36() = |file:///home/paulk/pico.trm|(2,2,<2,3>,<4,5>) >= |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>);
    		public test bool testLocation37() = !(|file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) >= |file:///home/paulk/pico.trm|(2,2,<2,3>,<4,5>));
    		
    		public test bool testLocation38() = |file:///xxx|(45,1,<1,45>,<1,46>) <= |file:///xxx|(40,6,<1,40>,<1,46>);
    		public test bool testLocation39() = |file:///xxx|(45,1,<1,45>,<1,46>) <= |file:///xxx|(40,7,<1,40>,<1,47>);
    	  	
    //	testList
    	
    		public test bool testList1() = [] == [];
    		public test bool testList2() = [] != [1];
    		public test bool testList3() = [1] == [1];
    		public test bool testList4() = [1] != [2];
    		public test bool testList5() = [1, 2] == [1, 2];
    		public test bool testList6() = [1, 2] != [2, 1];
    		
    		public test bool testList7() = [] + [] == [];
    		public test bool testList8() = [1, 2, 3] + [] == [1, 2, 3];
    		public test bool testList9() = [] + [1, 2, 3] == [1, 2, 3];
    		public test bool testList10() = [1, 2] + [3, 4, 5] == [1, 2, 3, 4, 5];	
    		
    		public test bool testList11() = ([1, 2] + [3, 4]) + [5] == [1, 2, 3, 4, 5];	
    		public test bool testList12() = [1, 2] + ([3, 4] + [5]) == [1, 2, 3, 4, 5];	
    		public test bool testList13() = [1, 2] + [3, 4] + [5] == [1, 2, 3, 4, 5];
    		
    		public test bool testList14() = [1, 2] + 3 == [1, 2, 3];
    		public test bool testList15() = 1 +  [2, 3] == [1, 2, 3];
    		
    		public test bool testList16() = [1,2,1,2,3,4,3,4,5] - 1 == [2,1,2,3,4,3,4,5];
    		public test bool testList17() = [1,2,1,2,3,4,3,4,5] - 2 == [1,1,2,3,4,3,4,5];
    		public test bool testList18() = [1,2,1,2,3,4,3,4,5] - 5 == [1,2,1,2,3,4,3,4];
    		
    		public test bool testList19() = [1,2,1,2,3,4,3,4,5] - [1] == [2,1,2,3,4,3,4,5];
    		public test bool testList20() = [1,2,1,2,3,4,3,4,5] - [2] == [1,1,2,3,4,3,4,5];
    		public test bool testList21() = [1,2,1,2,3,4,3,4,5] - [5] == [1,2,1,2,3,4,3,4];
    		
    		public test bool testList22() = [1,2,1,2,3,4,3,4,5] - [1,1] == [2,2,3,4,3,4,5];
    		public test bool testList23() = [1,2,1,2,3,4,3,4,5] - [1,1,1] == [2,2,3,4,3,4,5];
    		
    		public test bool testList24() = [1,2,1,2,3,4,3,4,5] - [1,2] == [1,2,3,4,3,4,5];
    		public test bool testList25() = [1,2,1,2,3,4,3,4,5] - [2,3] == [1,1,2,4,3,4,5];
    		
    		public test bool testList26() = [] & [1,2,4] == [];
    		public test bool testList27() = [1,2,3] & [] == [];
    		public test bool testList28() = [1,2,3,4,5,4,3,2,1] & [1,2,4] == [1,2,4,4,2,1];
    		
    		
    		public test bool testList29() = [] <= [];
    		public test bool testList30() = [] <= [1];
    		
    /*TODO:REMOVE?*/		
    // These commented out tests assume that <= etc. are ("half") ordering operations
    // Currently they are strictly subset implementations.
    //		public test bool testList() = [2, 1, 0] <= [2, 3];
    //		public test bool testList() = [2, 1] <= [2, 3, 0];
    		public test bool testList31() = [2, 1] <= [2, 1];
    		public test bool testList32() = [2, 1] <= [2, 1, 0];
    		
    		public test bool testList33() = [] < [1];
    //		public test bool testList() = [2, 1, 0] < [2, 3];
    //		public test bool testList() = [2, 1] < [2, 3, 0];
    		public test bool testList34() = [2, 1] < [2, 1, 0];
    		
    		public test bool testList35() = [] >= [];
    //		public test bool testList() = [1] >= [];
    //		public test bool testList() = [2, 3] >= [2, 1, 0];
    //		public test bool testList() = [2, 3, 0] >= [2, 1];
    		public test bool testList36() = [2, 1] >= [2, 1];
    		public test bool testList37() = [2, 1, 0] >= [2, 1];
    		
    		public test bool testList38() = [1] > [];
    //		public test bool testList() = [2, 3] > [2, 1, 0];
    //		public test bool testList() = [2, 3, 0] > [2, 1];
    		public test bool testList39() = [2, 1, 0] > [2, 1];
    		
    		public test bool testList40() = [] * [] == [];
    		public test bool testList41() = [1] * [9] == [<1,9>];
    		public test bool testList42() = [1, 2] * [9] == [<1,9>, <2,9>];
    		public test bool testList43() = [1, 2, 3] * [9] == [<1,9>, <2,9>, <3,9>];
    		public test bool testList44() = [1, 2, 3] * [9, 10] == [<1,9>, <1,10>, <2,9>, <2,10>, <3,9>, <3,10>];
    		
    		public test bool testList45() = 2 in [1, 2, 3];
    		public test bool testList46() = 3 notin [2, 4, 6];
    		
    		public test bool testList47() = (2 > 3 ? [1,2] : [1,2,3]) == [1,2,3];
    
    	@expected{IndexOutOfBounds}
    	public test bool  SubscriptError11() {
    		[1,2][5];return false;
    	}
    	
    //	listSplicing
    
    		public test bool testListSplicing1() =  [1,2,3] == [1,2,3];
    		public test bool testListSplicing2() = [*1,2,3] == [1,2,3];
    		public test bool testListSplicing3() = [1,*2,3] == [1,2,3];
    		public test bool testListSplicing4() = [1,2,*3] == [1,2,3];
    		public test bool testListSplicing5() = [*1,*2,3] == [1,2,3];
    		
    		public test bool testListSplicing6() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1] == [[1,2]];}
    		public test bool testListSplicing7() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1] == [1,2];}
    		
    		public test bool testListSplicing8() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1,3] == [[1,2],3];}
    		public test bool testListSplicing9() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1,3] == [1,2,3];}
    		
    		public test bool testListSplicing10() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1,L2] == [[1,2],[3,4]];}
    		public test bool testListSplicing11() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1,L2] == [1,2,[3,4]];}
    		public test bool testListSplicing12() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1,*L2] == [[1,2],3,4];}
    		public test bool testListSplicing13() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1,*L2] == [1,2,3,4];}
    		
    		public test bool testListSplicing14() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1,L2,5] == [[1,2],[3,4],5];}
    		public test bool testListSplicing15() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1,L2,5] == [1,2,[3,4],5];}
    		public test bool testListSplicing16() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1,*L2,5] == [[1,2],3,4,5];}
    		public test bool testListSplicing17() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1,*L2,5] == [1,2,3,4,5];}
    		
    		public test bool testListSplicing18() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [[L1]] == [[[1,2]]];}
    		public test bool testListSplicing19() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [[*L1]] == [[1,2]];}
    		
    		public test bool testListSplicing20() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [[L1],3] == [[[1,2]],3];}
    		public test bool testListSplicing21() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [[*L1],3] == [[1,2],3];}	
    		
    		public test bool testListSplicing22() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [[L1],[L2]] == [[[1,2]],[[3,4]]];}
    		public test bool testListSplicing23() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [[*L1],[L2]] == [[1,2],[[3,4]]];}
    		
    		public test bool testListSplicing24() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [[L1],[*L2]] == [[[1,2]],[3,4]];}
    		public test bool testListSplicing25() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [[*L1],[*L2]] == [[1,2],[3,4]];}
    		
    		public test bool testListSplicing26() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*[*L1],[*L2]] == [1,2,[3,4]];}
    		public test bool testListSplicing27() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [[*L1],*[*L2]] == [[1,2],3,4];}
    		public test bool testListSplicing28() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*[*L1],*[*L2]] == [1,2,3,4];}
    	
    		public test bool testListSplicing29() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1,[L2]] == [[1,2],[[3,4]]];}
    		public test bool testListSplicing30() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1,[L2]] == [1,2,[[3,4]]];}
    		public test bool testListSplicing31() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1,[*L2]] == [[1,2],[3,4]];}
    		public test bool testListSplicing32() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1,[*L2]] == [1,2,[3,4]];}
    		public test bool testListSplicing33() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1,*[*L2]] == [1,2,3,4];}
    
    		public test bool testListSplicing34() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1,[L2],5] == [[1,2],[[3,4]],5];}
    		public test bool testListSplicing35() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1,[L2],5] == [1,2,[[3,4]],5];}
    		public test bool testListSplicing36() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1,[*L2],5] == [[1,2],[3,4],5];}
    		
    		public test bool testListSplicing37() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; list[list[list[int]]] L3 = [[L1]]; return L3 == [[[1,2]]];}
    		public test bool testListSplicing38() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; list[value] L3 = [[L1],3]; return L3 == [[[1,2]],3];}
    		public test bool testListSplicing39() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; list[list[list[int]]] L3 = [[L1],[L2]]; return L3 == [[[1,2]],[[3,4]]];}
    		public test bool testListSplicing40() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; list[value] L3 = [L1,[L2]]; return L3 == [[1,2],[[3,4]]];}
    		public test bool testListSplicing41() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; list[value] L3 = [L1,[L2],5]; return L3 == [[1,2],[[3,4]],5];}
    
    //	testSetInListSplicing
    
    		public test bool testSetInListSplicing1() {list[int] L1 = [1,2]; set[int] S2 = {3,4}; return [L1,S2,5] == [[1,2],{3,4},5];}
    		public test bool testSetInListSplicing2() {list[int] L1 = [1,2]; set[int] S2 = {3,4}; return [*L1,S2,5] == [1,2,{3,4},5];}
    		public test bool testSetInListSplicing3() {list[int] L1 = [1,2]; set[int] S2 = {3,4}; return ([L1,*S2,5] == [[1,2],3,4,5]) || ([L1,*S2,5] == [[1,2],4,3,5]);}
    		public test bool testSetInListSplicing4() {list[int] L1 = [1,2]; set[int] S2 = {3,4}; return ([*L1,*S2,5] == [1,2,3,4,5]) || ([*L1,*S2,5] == [1,2,4,3,5]) ;}
    
    //	testRange
    		
    		public test bool testRange1() = [1 .. 1] == [];
    		public test bool testRange2() = [1 .. 2] == [1];
    		public test bool testRange3() = [1 .. -1] == [1, 0];
    		public test bool testRange4() = [1, 2 .. 10] == [1,2,3,4,5,6,7,8,9];
    		public test bool testRange5() = [1, 3 .. 10] == [1,3,5,7,9];
    		public test bool testRange6() = [1, -2 .. 10] == [];
    		public test bool testRange7() = [1, -3 .. -10] == [1,-3,-7];
    	
    //	testSet
    		
    		public test bool testSet1() = {} == {};
    		public test bool testSet2() = {} != {1};
    		public test bool testSet3() = {1} == {1};
    		public test bool testSet4() = {1} != {2};
    		public test bool testSet5() = {1, 2} == {1, 2};
    		public test bool testSet6() = {1, 2} == {2, 1};
    		public test bool testSet7() = {1, 2, 3, 1, 2, 3} == {3, 2, 1};	
    		
    		public test bool testSet8() = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    		public test bool testSet9() = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 2, 3, 4, 5, 6, 7, 8, 9, 1};
    		public test bool testSet10() = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 9, 3, 4, 5, 6, 7, 8, 2, 1};
    		public test bool testSet11() = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 9, 7, 4, 5, 6, 3, 8, 2, 1};
    		public test bool testSet12() = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 9, 7, 6, 5, 4, 3, 8, 2, 1};
    		
    		public test bool testSet13() = {{1}, {2}} == {{2}, {1}};
    		public test bool testSet14() = {{}} == {{}};
    		public test bool testSet15() = {{}, {}} == {{}};
    		public test bool testSet16() = {{}, {}, {}} == {{}};
    		
    		public test bool testSet17() = {{1, 2}, {3,4}} == {{2,1}, {4,3}};	
    	
    		public test bool testSet18() = {} + {} == {};
    		public test bool testSet19() = {1, 2, 3} + {} == {1, 2, 3};
    		public test bool testSet20() = {} + {1, 2, 3} == {1, 2, 3};
    		public test bool testSet21() = {1, 2} + {3, 4, 5} == {1, 2, 3, 4, 5};	
    		public test bool testSet22() = {1, 2, 3, 4} + {3, 4, 5} == {1, 2, 3, 4, 5};
    		public test bool testSet23() = {{1, 2}, {3,4}} + {{5,6}} == {{1,2},{3,4},{5,6}};
    		public test bool testSet24() = 1 + {2,3} == {1,2,3};
    		public test bool testSet25() = {1,2} + 3 == {1,2,3};
    		
    		public test bool testSet26() = {} - {} == {};
    		public test bool testSet27() = {1, 2, 3} - {} == {1, 2, 3};
    		public test bool testSet28() = {} - {1, 2, 3} == {};
    		public test bool testSet29() = {1, 2, 3} - {3, 4, 5} == {1, 2};	
    		public test bool testSet30() = {1, 2, 3, 4} - {1, 2, 3, 4, 5} == {};
    		public test bool testSet31() = {{1, 2}, {3,4}, {5,6}} - {{3,4}} == {{1,2}, {5,6}};
    		public test bool testSet32() = {1,2,3} - 3 == {1,2};
    		
    		public test bool testSet33() = {} & {} == {};
    		public test bool testSet34() = {1, 2, 3} & {} == {};
    		public test bool testSet35() = {} & {1, 2, 3} == {};
    		public test bool testSet36() = {1, 2, 3} & {3, 4, 5} == {3};	
    		public test bool testSet37() = {1, 2, 3, 4} & {3, 4, 5} == {3, 4};	
    		public test bool testSet38() = {{1,2},{3,4},{5,6}} & {{2,1}, {8,7}, {6,5}} == {{1,2},{5,6}};
    		
    		public test bool testSet39() = {} <= {};
    		public test bool testSet40() = {} <= {1};
    		public test bool testSet41() = {2, 1} <= {1, 2};
    		public test bool testSet42() = {2, 1} <= {1, 2, 3};
    		public test bool testSet43() = {2, 1} <= {2, 1, 0};
    	
    		public test bool testSet44() = {} < {1};
    		public test bool testSet45() = {2, 1} < {2, 1, 3};
    	
    		public test bool testSet46() = {} >= {};
    		public test bool testSet47() = {1} >= {};
    		public test bool testSet48() = {2, 3} >= {2};
    	
    		public test bool testSet49() = {1} > {};
    		public test bool testSet50() = {2, 1, 3} > {2, 3};
    		
    		public test bool testSet51() = {} * {} == {};
    		public test bool testSet52() = {1} * {9} == {<1,9>};
    		public test bool testSet53() = {1, 2} * {9} == {<1,9>, <2,9>};
    		public test bool testSet54() = {1, 2, 3} * {9} == {<1,9>, <2,9>, <3,9>};
    		public test bool testSet55() = {1, 2, 3} * {9, 10} == {<1,9>, <1,10>, <2,9>, <2,10>, <3,9>, <3,10>};
    		
    		
    		public test bool testSet56() = 2 in {1, 2, 3};
    		public test bool testSet57() = {4,3} in {{1, 2}, {3,4}, {5,6}};
    		
    		public test bool testSet58() = 5 notin {1, 2, 3};
    		public test bool testSet59() = {7,8} notin {{1, 2}, {3,4}, {5,6}};
    		
    		public test bool testSet60() = ((3 > 2) ? {1,2} : {1,2,3}) == {1,2};
    		
    		public test bool testSet61() = {<"a", [1,2]>, <"b", []>, <"c", [4,5,6]>} != {};
   
    	//
    	// Some nested set patterns to test backtracking behaviour.
    	//
    	
    data TYPESET = SET(str name) | SUBTYPES(TYPESET tset) | INTERSECT(set[TYPESET] tsets);
    
    //	testSet
    		
    		public test bool testSet62() = {INTERSECT({TYPESET t1, *TYPESET rest}), TYPESET t2} :=  {INTERSECT({SET("a"), SET("b")}), SET("c")};
    		public test bool testSet63() = {INTERSECT({TYPESET t1, *TYPESET rest}),  t1} :=  {INTERSECT({SET("a"), SET("b")}), SET("a")};
    		public test bool testSet64() = {INTERSECT({TYPESET t1, *TYPESET rest}),  t1} :=  {INTERSECT({SET("b"), SET("a")}), SET("a")};
    
    		public test bool testSet65() =  { <t1, t2> | INTERSECT({TYPESET t1, *TYPESET t2}) :=  INTERSECT({SET("b"), SET("a")})} == 
    						   { <SET("b"),{SET("a")}>, <SET("a"),{SET("b")}>};
    	
    	
    		public test bool testSet66() = {<t1, rest, t2> | {INTERSECT({TYPESET t1, *TYPESET rest}),  t2} :=  {INTERSECT({SET("a"), SET("b")}) , SET("b")}}==
    				           { <SET("a"),{SET("b")},SET("b")>, <SET("b"),{SET("a")},SET("b")>};
    
    		public test bool testSet67() = {<t1, rest> | {INTERSECT({TYPESET t1, *TYPESET rest}),  t1} :=  {INTERSECT({SET("a"), SET("b")}), SET("b")}}==
    				           {<SET("b"),{SET("a")}>};
    
    // TYPESET tests moved to SetMatch[12]

    // testSetMultiVariable
    
    		public test bool testSetMultiVariable1() = {*value S1, *value S2} := {} && (S1 == {}) && (S2 == {});
    		public test bool testSetMultiVariable2() = {*S1, *S2} := {} && (S1 == {}) && (S2 == {});
    		
    		public test bool testSetMultiVariable3() = {*int S1, *int S2} := {100} && ((S1 == {100} && S2 == {}) || (S1 == {} && S2 == {100}));
    		public test bool testSetMultiVariable4() = {*S1, *S2} := {100} && ((S1 == {100} && S2 == {}) || (S1 == {} && S2 == {100}));
    		
    		public test bool testSetMultiVariable5()  {R = for({*int S1, *int S2} := {100}) append <S1, S2>; R == [<{100}, {}>, <{}, {100}> ];}
    		public test bool testSetMultiVariable6()  {R = for({*S1, *S2} := {100}) append <S1, S2>; R == [<{100}, {}>, <{}, {100}> ];}
    
    		public test bool testSetMultiVariable7()  {R = for({*S1, *S2} := {100}) append <S1, S2>; R == [<{100}, {}>, <{}, {100}> ];}
    		//
    		// TODO: the following test requires a specific implementation specific
    		// set representation and, thus, should be refactored. To check
    		// splicing, without taking order into account, the list 'R' is now
    		// converted to a set.
    		//
    		public test bool testSetMultiVariable8()  {R = for({*S1, *S2} := {100, 200}) append <S1, S2>; {*R} == {<{200,100}, {}>, <{200}, {100}>, <{100}, {200}>, <{}, {200,100}>};}
    		public test bool testSetMultiVariable9()  {R = for({*int S1, *S2} := {100, "a"})  append <S1, S2>; R == [<{100}, {"a"}>, <{},{100,"a"}>];}
    		public test bool testSetMultiVariable10()  {R = for({*int S1, *str S2} := {100, "a"}) append <S1, S2>; R == [<{100}, {"a"}>];}
    		
    		public test bool testSetMultiVariable11()  {R = for({*str S1, *S2} := {100, "a"})  append <S1, S2>; R == [<{"a"},{100}>, <{},{100,"a"}>];}
    		public test bool testSetMultiVariable12()  {R = for({*str S1, *int S2} := {100, "a"})  append <S1, S2>; R == [<{"a"},{100}>];}
    		
    		public test bool testSetMultiVariable13() = !({*str S1, *str S2} := {100, "a"});
    		public test bool testSetMultiVariable14() = !({*int S1, *int S2} := {100, "a"});
    
      
    	public test bool addSetError1() {
    		return {1,2,3} + true=={1,2,3,true};
    	}
        
    		public test bool testSet68() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1} == {{1,2}};}
    		public test bool testSet69() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*S1} == {1,2};}
    		
    		public test bool testSet70() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,3} == {{1,2},3};}
    		public test bool testSet71() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*S1,3} == {1,2,3};}
    		
    		public test bool testSet72() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,S2} == {{1,2},{3,4}};}
    		public test bool testSet73() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*S1,S2} == {1,2,{3,4}};}
    		public test bool testSet74() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,*S2} == {{1,2},3,4};}
    		public test bool testSet75() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*S1,*S2} == {1,2,3,4};}
    	
    		
    		public test bool testSet76() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,S2,5} == {{1,2},{3,4},5};}
    		public test bool testSet77() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*S1,S2,5} == {1,2,{3,4},5};}
    		public test bool testSet78() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,*S2,5} == {{1,2},3,4,5};}
    		public test bool testSet79() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*S1,*S2,5} == {1,2,3,4,5};}
    		
    		public test bool testSet80() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {{S1}} == {{{1,2}}};}
    		
    		public test bool testSet81() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {{*S1}}   == {{1,2}};}
    		public test bool testSet82() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*{*S1}} == {1,2};}
    		
    		public test bool testSet83() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {{S1},3} == {{{1,2}},3};}
    		public test bool testSet84() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*{S1},3} == {{1,2},3};}
    		public test bool testSet85() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*{*S1},3} == {1,2,3};}
    		
    		public test bool testSet86() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*{*S1},2} == {1,2};}
    	
    		public test bool testSet87() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {{S1},{S2}} == {{{1,2}},{{3,4}}};}
    		public test bool testSet88() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*{S1},{S2}} == {{1,2},{{3,4}}};}
    		public test bool testSet89() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {{S1},*{S2}} == {{{1,2}},{3,4}};}
    		public test bool testSet90() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*{S1},*{S2}} == {{1,2},{3,4}};}
    		public test bool testSet91() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*{*S1},*{*S2}} == {1,2,3,4};}
    		
    		
    		public test bool testSet92() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,{S2}} == {{1,2},{{3,4}}};}
    		public test bool testSet93() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,{*S2}} == {{1,2},{3,4}};}
    		public test bool testSet94() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,*{*S2}} == {{1,2},3,4};}
    		
    		
    		public test bool testSet95() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,{S2},5} == {{1,2},{{3,4}},5};}
    		public test bool testSet96() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,{*S2},5} == {{1,2},{3,4},5};}
    		public test bool testSet97() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,*{*S2},5} == {{1,2},3,4,5};}
    		public test bool testSet98() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*S1,*{*S2},5} == {1,2,3,4,5};}
    		
    		public test bool testSet99() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; set[set[set[int]]] S3 = {{S1}}; return S3 == {{{1,2}}};}
    		
    		public test bool testSet100() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; set[value] S3 = {{S1},3}; return S3 == {{{1,2}},3};}
    		public test bool testSet101() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; set[set[set[int]]] S3 = {{S1},{S2}};return  S3 == {{{1,2}},{{3,4}}};}
    		public test bool testSet102() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; set[value] S3 = {S1,{S2}}; return S3 == {{1,2},{{3,4}}};}
    		public test bool testSet103() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; set[value] S3 = {S1,{S2},5}; return S3 == {{1,2},{{3,4}},5};}
    	
    
    // testListInSetSplicing
    
    		public test bool testListInSplicing1() {list[int] L1 = [1,2]; set[int] S2 = {3,4}; return {L1,S2,5} == {[1,2],{3,4},5};}
    		public test bool testListInSplicing2() {list[int] L1 = [1,2]; set[int] S2 = {3,4}; return {*L1,S2,5} == {1,2,{3,4},5};}
    		public test bool testListInSplicing3() {list[int] L1 = [1,2]; set[int] S2 = {3,4}; return {L1,*S2,5} == {[1,2],3,4,5};}
    		public test bool testListInSplicing4() {list[int] L1 = [1,2]; set[int] S2 = {3,4}; return {*L1,*S2,5} == {1,2,3,4,5};}
    	
    //	testMap
    		
    		public test bool testMap1() = () == ();
    		public test bool testMap2() = (1:10) != ();
    		public test bool testMap3() = (1:10) == (1:10);
    		public test bool testMap4() = (1:10) != (2:20);
    		
    		public test bool testMap5() = () + () == ();
    		public test bool testMap6() = (1:10) + () == (1:10);
    		public test bool testMap7() = (1:10) + (2:20) == (1:10, 2:20);
    		public test bool testMap8() = (1:10, 2:20) + (2:25) == (1:10, 2:25);
    		
    		public test bool testMap9() = () - () == ();
    		public test bool testMap10() = (1:10, 2:20) - () == (1:10,2:20);
    		public test bool testMap11() = (1:10, 2:20) - (2:20) == (1:10);
    		public test bool testMap12() = (1:10, 2:20) - (2:25) == (1:10); // This is current behaviour; is this ok?
    	
    		public test bool testMap13() = () & () == ();
    		public test bool testMap14() = (1:10) & () == ();
    		public test bool testMap15() = (1:10, 2:20, 3:30, 4:40) & (2:20, 4:40, 5:50) == (2:20, 4:40);
    		public test bool testMap16() = (1:10, 2:20, 3:30, 4:40) & (5:50, 6:60) == ();
    		
    		public test bool testMap17() = () <= ();
    		public test bool testMap18() = () <= (1:10);
    		public test bool testMap19() = (1:10) <= (1:10);
    		public test bool testMap20() = (1:10) <= (1:10, 2:20);
    		
    		public test bool testMap21() = !(() < ());
    		public test bool testMap22() = () < (1:10);
    		public test bool testMap23() = !((1:10) < (1:10));
    		public test bool testMap24() = (1:10) < (1:10, 2:20);
    		
    		public test bool testMap25() = () >= ();
    		public test bool testMap26() = (1:10) >= ();
    		public test bool testMap27() = (1:10) >= (1:10);
    		public test bool testMap28() = (1:10, 2:20) >= (1:10);
    		
    		public test bool testMap29() = !(() > ());
    		public test bool testMap30() = (1:10) > ();
    		public test bool testMap31() = !((1:10) > (1:10));
    		public test bool testMap32() = (1:10, 2:20) > (1:10);
    		
    		
    		public test bool testMap33() = 1 in (1:10, 2:20);
    		public test bool testMap34() = !(3 in (1:10, 2:20));
    		
    		public test bool testMap35() = 3 notin (1:10, 2:20);
    		public test bool testMap36() = !(2 notin (1:10, 2:20));
    		
    		public test bool testMap37() {map[str,list[int]] m = ("a": [1,2], "b": [], "c": [4,5,6]); return m["a"] == [1,2];}
   
    	 
    	 @expected{Throw}
    	 public void NoKeyError1(){
    		 (1:10, 2:20)[3];return;
    	 }
    	 
    	 @expected{Throw}  // MultipleKey
    	 public void MultipleKeyError1(){
    		 (1:10, 1:10);return;
    	 }
    	
    	// testTuple
    		
    		public test bool testTuple1() = <1, 2.5, true> == <1, 2.5, true>;
    		public test bool testTuple2() = <1, 2.5, true> != <0, 2.5, true>;
    		public test bool testTuple3() = <{1,2}, 3> == <{2,1}, 3>;
    		public test bool testTuple4() = <1, {2,3}> == <1, {3,2}>;
    		public test bool testTuple5() = <{1,2}, {3,4}> == <{2,1},{4,3}>;
    		
    		public test bool testTuple6() = <1>           >= <1>;
    		public test bool testTuple7() = <2>           >= <1>;
    		public test bool testTuple8() = <1,2>         >= <1>;
    		public test bool testTuple9() = <1,2>         >= <1,2>;
    		public test bool testTuple10() = <1,2>         >= <1, 1>;
    		public test bool testTuple11() = <1,"def">   >= <1, "abc">;
    		public test bool testTuple12() = <1, [2,3,4]>  >= <1, [2,3]>;
    		public test bool testTuple13() = <1, [2,3]>    >= <1, [2,3]>;
    		
    		public test bool testTuple14() = !(<1>          > <1>);
    		public test bool testTuple15() = <2>           > <1>;
    		public test bool testTuple16() = <1,2>         > <1>;
    		public test bool testTuple17() = !(<1,2>        > <1,2>);
    		public test bool testTuple18() = <1,2>         > <1, 1>;
    		public test bool testTuple19() = <1,"def">   > <1, "abc">;
    		public test bool testTuple20() = <1, [2,3,4]>  > <1, [2,3]>;
    		public test bool testTuple21() = !(<1, [2,3]>   > <1, [2,3]>);
    		
    		public test bool testTuple22() = <1>           <= <1>;
    		public test bool testTuple23() = <1>           <= <2>;
    		public test bool testTuple24() = <1>           <= <1,2>;
    		public test bool testTuple25() = <1,2>         <= <1,2>;
    		public test bool testTuple26() = <1,1>         <= <1, 2>;
    		public test bool testTuple27() = <1,"abc">   <= <1, "def">;
    		public test bool testTuple28() = <1, [2,3]>    <= <1, [2,3,4]>;
    		public test bool testTuple29() = <1, [2,3]>    <= <1, [2,3]>;
    		
    		public test bool testTuple30() = !(<1>          < <1>);
    		public test bool testTuple31() = <1>           < <2>;
    		public test bool testTuple32() = <1>           < <1,2>;
    		public test bool testTuple33() = !(<1,2>        < <1,2>);
    		public test bool testTuple34() = <1,1>         < <1, 2>;
    		public test bool testTuple35() = <1,"abc">   < <1, "def">;
    		public test bool testTuple36() = <1, [2,3]>    < <1, [2,3,4]>;
    		public test bool testTuple37() = !(<1, [2,3]>   < <1, [2,3]>);
    		
    		public test bool testTuple38() = <1, "a", true> + <1.5, "def"> == <1, "a", true> + <1.5, "def">;
    	
    // namedTuple
    		
    		public test bool testNamedTuple1() {tuple[int key, str val] T = <1, "abc">; return T.key == 1;}
    		public test bool testNamedTuple2() {tuple[int key, str val] T = <1, "abc">; return T.val == "abc";}	
    	
   // 	testRelation
   
    		public test bool testRelation1() = {} == {};
    		public test bool testRelation2() = {<1,10>} == {<1,10>};
    		public test bool testRelation3() = {<1,2,3>} == {<1,2,3>};
    		public test bool testRelation4() = {<1,10>, <2,20>} == {<1,10>, <2,20>};
    		public test bool testRelation5() = {<1,10>, <2,20>, <3,30>} == {<1,10>, <2,20>, <3,30>};
    		public test bool testRelation6() = {<1,2,3>, <4,5,6>} == {<4,5,6>, <1,2,3>};
    		public test bool testRelation7() = {<1,2,3,4>, <4,5,6,7>} == {<4,5,6,7>, <1,2,3,4>};
    		
    		public test bool testRelation8() = {} != {<1,2>, <3,4>};
    		public test bool testRelation9() = !({<1,2>, <3,4>} == {});
    		
    		public test bool testRelation10() = {<1, {1,2,3}>, <2, {2,3,4}>} ==  {<1, {1,2,3}>, <2, {2,3,4}>};
    		public test bool testRelation11() = {<1, {1,2,3}>, <2, {2,3,4}>} ==  {<2, {2,3,4}>, <1, {1,2,3}>};
    		public test bool testRelation12() = {<1, {1,2,3}>, <2, {2,3,4}>} ==  {<2, {4,3,2}>, <1, {2,1,3}>};
    		
    		public test bool testRelation13() = {<1,10>} + {} == {<1,10>};
    		public test bool testRelation14() = {} + {<1,10>}  == {<1,10>};
    		public test bool testRelation15() = {<1,10>} + {<2,20>} == {<1,10>, <2,20>};
    		public test bool testRelation16() = {<1,10>, <2,20>} + {<3,30>} == {<1,10>, <2,20>, <3,30>};
    		public test bool testRelation17() = {<1,10>, <2,20>} + {<2,20>, <3,30>} == {<1,10>, <2,20>, <3,30>};
    		
    		public test bool testRelation18() = {<1,10>} - {} == {<1,10>};
    		public test bool testRelation19() = {} - {<1,10>}  == {};
    		public test bool testRelation20() = {<1,10>, <2,20>} - {<2,20>, <3,30>} == {<1,10>};
    		
    		public test bool testRelation21() = {<1,10>} & {} == {};
    		public test bool testRelation22() = {} & {<1,10>}  == {};
    		public test bool testRelation23() = {<1,10>, <2,20>} & {<2,20>, <3,30>} == {<2,20>};
    		public test bool testRelation24() = {<1,2,3,4>, <2,3,4,5>} & {<2,3,4,5>,<3,4,5,6>} == {<2,3,4,5>};
    		
    		public test bool testRelation25() = <2,20> in {<1,10>, <2,20>, <3,30>};
    		public test bool testRelation26() = <1,2,3> in {<1,2,3>, <4,5,6>};
    		
    		public test bool testRelation27() = <4,40> notin {<1,10>, <2,20>, <3,30>};
    		public test bool testRelation28() = <1,2,4> notin {<1,2,3>, <4,5,6>};
    		
    		public test bool testRelation29() = {} o {} == {};
    		public test bool testRelation30() = {<1,10>,<2,20>} o {} == {};
    		public test bool testRelation31() = {} o {<10,100>, <20,200>} == {};
    		public test bool testRelation32() = {<1,10>,<2,20>} o {<10,100>, <20,200>} == {<1,100>, <2,200>};
    		
    		public test bool testRelation33() = {<1, "a">, <2, "b">} * {<false, 0>, <true, 1>} == {<<1,"a">,<false,0>>,<<2,"b">,<false,0>>,<<1,"a">,<true,1>>,<<2,"b">,<true,1>>};
    
    		public test bool testRelation34() = {<1,2>} join {<2,3>} == {<1,2,2,3>};
    		public test bool testRelation35() = {<1,2>} join {} == {};
    		public test bool testRelation36() = {} join {<2,3>} == {};
    		public test bool testRelation37() = {} join {} == {};
    		public test bool testRelation38() = {<1,2>} join {3} == {<1,2,3>};
    		public test bool testRelation39() = {<1,2>} join {3, 4} == {<1,2,3>, <1,2,4>};
    		public test bool testRelation40() = {1} join {2} == {<1,2>};
    		public test bool testRelation41() = {1,2,3} join {2} == {<1,2>, <2,2>, <3,2>};
    		
    
    		public test bool testRelation42() = {} + == {};
    		public test bool testRelation43() = {} * == {};
    		
    		public test bool testRelation44() = {<1,2>, <2,3>, <3,4>} + == {<1,2>, <2,3>, <3,4>, <1, 3>, <2, 4>, <1, 4>};
    		
    		public test bool testRelation45() = {<1,2>, <2,3>, <3,4>} * == {<1,2>, <2,3>, <3,4>, <1, 3>, <2, 4>, <1, 4>, <1, 1>, <2, 2>, <3, 3>, <4, 4>};
    		
    		public test bool testRelation46() = {<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}+ ==	{<1,2>, <2,3>, <3,4>, <4,2>, <4,5>, <1, 3>, <2, 4>, <3, 2>, <3, 5>, <4, 3>, <1, 4>, <2, 2>, <2, 5>, <3, 3>, <4, 4>, <1, 5>};
    		
    		public test bool testRelation47() = {<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}* == {<1,2>, <2,3>, <3,4>, <4,2>, <4,5>, <1, 3>, <2, 4>, <3, 2>, <3, 5>, <4, 3>, <1, 4>, <2, 2>, <2, 5>, <3, 3>, <4, 4>, <1, 5>, <1, 1>, <5, 5>};
      	
    // namedRelation
    
    		public test bool namedRelation1() {rel[int from, int to] R = {<1,10>, <2,20>}; return R.from == {1,2};}
    		public test bool namedRelation2() {rel[int from, int to] R = {<1,10>, <2,20>}; return R.to == {10,20};}
    
    /* Issue :f constructor overlaps with NODE */
    /*
    data NODE1 = val(value V) | f() | f1(NODE1 a);
    	public test bool good1()  {
    		return f1(val(1)) == f1(val(1));
    	}
    */
    data NODE = i(int I) | s(str x)  | st(set[NODE] s) | l(list[NODE]) | m(map[NODE,NODE] m) | f() | f(NODE a) | f(NODE a, NODE b) | g() | g(NODE a) | g(NODE a,NODE b);
    	
    //	node
    
    		public test bool node1()=f() == f();
    		public test bool node2()=f() != g();
    		public test bool node3() {NODE n = f(); NODE m = g(); return n != m;}
    		public test bool node4()=f(i(1)) == f(i(1));
    		public test bool node5()=f(i(1)) != g(i(1));
    		public test bool node6() {NODE n = f(i(1)); NODE m = g(i(1)); return n != m;}
    		public test bool node7()=f(i(1),i(2)) == f(i(1),i(2));
    		public test bool node8()=f(i(1),i(2)) != f(i(1),i(3));
    		public test bool node9() { NODE n = f(i(1),i(2)); NODE m = f(i(1),i(3)); return n != m;}
    		public test bool node10()=f(i(1),g(i(2),i(3))) == f(i(1),g(i(2),i(3)));
    		public test bool node11()=f(i(1),g(i(2),i(3))) != f(i(1),g(i(2),i(4)));
    		public test bool node12() {NODE n = f(i(1),g(i(2),i(3))); NODE m = f(i(1),g(i(2),i(4))); return n != m;}
    		public test bool node13()=f(i(1),g(i(2),st({i(3),i(4),i(5)}))) == f(i(1),g(i(2),st({i(3),i(4),i(5)})));
    		public test bool node14() { NODE n = f(i(1),g(i(2),st({i(3),i(4),i(5)}))); NODE m = f(i(1),g(i(2),st({i(3),i(4),i(5),i(6)}))); return n != m;}
    		public test bool node15()=f(i(1),g(i(2),l([i(3),i(4),i(5)]))) == f(i(1),g(i(2),l([i(3),i(4),i(5)])));
    		public test bool node16() { NODE n = f(i(1),g(i(2),l([i(3),i(4),i(5)]))); NODE m = f(i(1),g(i(2),l([i(3),i(4),i(5),i(6)]))); return  n != m;}
    		public test bool node17()=f(i(1),g(i(2),m((i(3):i(3),i(4):i(4),i(5):i(5))))) == f(i(1),g(i(2),m((i(3):i(3),i(4):i(4),i(5):i(5)))));
    		public test bool node18() {NODE n = f(i(1),g(i(2),m((i(3):i(3),i(4):i(4),i(5):i(5))))); NODE x = f(i(1),g(i(2),m((i(3):i(3),i(4):i(4),i(5):i(0))))); return n != x;}
    
    		public test bool node19()=f()                       <= f();
    		public test bool node20()=f()                       <= g();
    		public test bool node21()=f()                       <= f(i(1));
    		public test bool node22()=f(i(1))                   <= f(i(1));
    		public test bool node23()=f(i(1), i(2))             <= f(i(1), i(3));
    		public test bool node24()=f(i(1), i(2))             <= g(i(1), i(3));
    		public test bool node25()=f(i(1), s("abc"))       <= f(i(1), s("def"));
    		public test bool node26()=f(i(1), l([i(2), i(3)]))  <= f(i(1), l([i(2),i(3),i(4)]));
    		public test bool node27()=f(i(1), l([i(2), i(3)]))  <= f(i(1), l([i(2),i(3)]));
    		
    		public test bool node28()=!(f()                      < f());
    		public test bool node29()=f()                       < g();
    		public test bool node30()=f()                       < f(i(1));
    		public test bool node31()=!(f(i(1))                  < f(i(1)));
    		public test bool node32()=f(i(1), i(2))             < f(i(1), i(3));
    		public test bool node33()=f(i(1), i(2))             < g(i(1), i(3));
    		public test bool node34()=f(i(1), s("abc"))       < f(i(1), s("def"));
    		public test bool node35()=f(i(1), l([i(2), i(3)]))  < f(i(1), l([i(2),i(3),i(4)]));
    		public test bool node36()=!(f(i(1), l([i(2), i(3)])) < f(i(1), l([i(2),i(3)])));
    		
    		public test bool node37()=f()                          >= f();
    		public test bool node38()=g()                          >= f();
    		public test bool node39()=f(i(1))                      >= f();
    		public test bool node40()=f(i(1))                      >= f(i(1));
    		public test bool node41()=f(i(1), i(3))                >= f(i(1), i(2));
    		public test bool node42()=g(i(1), i(2))                >= f(i(1), i(3));
    		public test bool node43()=f(i(1), s("def"))          >= f(i(1), s("abc"));
    		public test bool node44() =f(i(1), l([i(2),i(3),i(4)])) >= f(i(1), l([i(2),i(3)]));
    		public test bool node45()=f(i(1), l([i(2), i(3)]))     >= f(i(1), l([i(2),i(3)]));
    		
    		public test bool node46()=!(f()                         > f());
    		public test bool node47()=g()                          > f();
    		public test bool node48()=f(i(1))                      > f();
    		public test bool node49()=!(f(i(1))                     > f(i(1)));
    		public test bool node50()=f(i(1), i(3))                > f(i(1), i(2));
    		public test bool node51()=g(i(1), i(2))                > f(i(1), i(3));
    		public test bool node52()=f(i(1), s("def"))          > f(i(1), s("abc"));
    		public test bool node53()=  f(i(1), l([i(2),i(3),i(4)])) > f(i(1), l([i(2),i(3)]));
    		public test bool node54()=!(f(i(1), l([i(2), i(3)]))    > f(i(1), l([i(2),i(3)])));
    
    data D = d(int ival);
    	
    // undefined
    	
    		public test bool undefined2() {T = (1:10); return (T[1] ? 13) == 10;}
    		public test bool undefined3() {T = (1:10); return (T[2] ? 13) == 13;}
    		
    		public test bool undefined4() {T = (1:10); return T[1] ? == true;}
    		public test bool undefined5() {T = (1:10); return T[2] ? == false;}

    
   
 