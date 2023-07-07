@license{
     Copyright (c) 2009-2015 CWI
     All rights reserved. This program and the accompanying materials
     are made available under the terms of the Eclipse Public License v1.0
     which accompanies this distribution, and is available at
     http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl - CWI}
module lang::rascal::tests::functionality::DataType
import Exception;
import List;

// bool
    	
test bool testBool1() = true == true;
test bool testBool2() = !(true == false);
test bool testBool3() = true != false;	
    
test bool testBool4() = (!true) == false;
test bool testBool5() = (!false) == true;
    		
test bool testBool6() = (true && true) == true;	
test bool testBool7() = (true && false) == false;	
test bool testBool8() = (false && true) == false;	 
test bool testBool9() = (false && false) == false;	
    		
test bool testBool10() = (true || true) == true;	
test bool testBool11() = (true || false) == true;	
test bool testBool12() = (false || true) == true;	
test bool testBool13() = (false || false) == false;	
    		
test bool testBool14() = (true ==> true) == true;	
test bool testBool15() = (true ==> false) == false;	
test bool testBool16() = (false ==> true)  == true;	
test bool testBool17() = (false ==> false) == true;
    		
test bool testBool18() = (true <==> true) == true;	
test bool testBool19() = (true <==> false) == false;	
test bool testBool20() = (false <==> true) == false;	
test bool testBool21() = (false <==> false) == true;
    		
test bool testBool22() = false  <= false;
test bool testBool23() = false  <= true;
test bool testBool24() = !(true  <= false);
test bool testBool25() = true   <= true;
    		
test bool testBool26() = !(false < false);
test bool testBool27() = false  < true;
test bool testBool28() = !(true  < false);
test bool testBool29() = !(true  < true);
    		
test bool testBool30() = false  >= false;
test bool testBool31() = true   >= false;
test bool testBool32() = !(false >= true);
test bool testBool33() = true   >= true;
    		
test bool testBool34() = !(false > false);
test bool testBool35() = true   > false;
test bool testBool36() = !(false > true);
test bool testBool37() = !(true   > true);
    
// 	testInt
    
test bool testInt1() = 1 == 1;
test bool testInt2() = 1 != 2;
    		
test bool testInt3() = -1 == -1;
test bool testInt4() = -1 != 1;
    		
test bool testInt5() = 1 + 1 == 2;
test bool testInt6() = -1 + 2 == 1;
test bool testInt7() = 1 + (-2) == -1;
    		
test bool testInt8() = 2 - 1 == 1;	
test bool testInt9() = 2 - 3 == -1;	
test bool testInt10() = 2 - -1 == 3;	
test bool testInt11() = -2 - 1 == -3;	
    		
test bool testInt12() = 2 * 3 == 6;	
test bool testInt13() = -2 * 3 == -6;	
test bool testInt14() = 2 * (-3) == -6;
test bool testInt15() = -2 * (-3) == 6;	
    		
test bool testInt16() = 8 / 4 == 2;	
test bool testInt17() = -8 / 4 == -2;
test bool testInt18() = 8 / -4 == -2;	
test bool testInt19() = -8 / -4 == 2;
    		
test bool testInt20() = 7 / 2 == 3;	
test bool testInt21() = -7 / 2 == -3;
test bool testInt22() = 7 / -2 == -3;	
test bool testInt23() = -7 / -2 == 3;	
    		
test bool testInt24() = 0 / 5 == 0;	
test bool testInt25() = 5 / 1 == 5;	
    		
test bool testInt26() = 5 % 2 == 1;	
test bool testInt27() = -5 % 2 == -1;
test bool testInt28() = 5 % -2 == 1;		
    		
test bool testInt29() = -2 <= -1;
test bool testInt30() = -2 <= 1;
test bool testInt31() = 1 <= 2;
test bool testInt32() = 2 <= 2;
test bool testInt33() = !(2 <= 1);
    		
test bool testInt34() = -2 < -1;
test bool testInt35() = -2 < 1;
test bool testInt36() = 1 < 2;
test bool testInt37() = !(2 < 2);
    		
test bool testInt38() = -1 >= -2;
test bool testInt39() = 1 >= -1;
test bool testInt40() = 2 >= 1;
test bool testInt41() = 2 >= 2;
test bool testInt42() = !(1 >= 2);
    		
test bool testInt43() = -1 > -2;
test bool testInt44() = 1 > -1;
test bool testInt45() = 2 > 1;
test bool testInt46() = !(2 > 2);
test bool testInt47() = !(1 > 2);
    		
test bool testInt48() = (3 > 2 ? 3 : 2) == 3;
    	
// 	valueEquals
    
test bool valueEquals()  {value x = 1.0; value y = 2; return x != y; }
    	
//	 testReal	
    
test bool testReal1() = 1.0 == 1.0;
test bool testReal2() = 1.0 != 2.0;
    		
test bool testReal3() = -1.0 == -1.0;
test bool testReal4() = -1.0 != 1.0;
    		
test bool testReal5() = 1.0 == 1;
test bool testReal6() = 1.00 == 1.0;
test bool testReal7() = 1 == 1.0;
    		
test bool testReal8()  {value x = 1.0; value y = 1; return x == y; }
test bool testReal9() {value x = 1.0; value y = 2; return x != y; }
    		
test bool testReal10() = 1.0 + 1.0 == 2.0;
test bool testReal11() = -1.0 + 2.0 == 1.0;
test bool testReal12() = 1.0 + (-2.0) == -1.0;
    		
test bool testReal13() = 1.0 + 1 == 2.0;
test bool testReal14() = -1 + 2.0 == 1.0;
test bool testReal15() = 1.0 + (-2) == -1.0;
    		
test bool testReal16() = 2.0 - 1.0 == 1.0;	
test bool testReal17() = 2.0 - 3.0 == -1.0;	
test bool testReal18() = 2.0 - -1.0 == 3.0;	
test bool testReal19() = -2.0 - 1.0 == -3.0;
    		
test bool testReal20() = 2.0 - 1 == 1.0;	
test bool testReal21() = 2 - 3.0 == -1.0;	
test bool testReal22() = 2.0 - -1 == 3.0;	
test bool testReal23() = -2 - 1.0 == -3.0;
    		
test bool testReal24() = 2.0 * 3.0 == 6.00;	
test bool testReal25() = -2.0 * 3.0 == -6.00;	
test bool testReal26() = 2.0 * (-3.0) == -6.00;
test bool testReal27() = -2.0 * (-3.0) == 6.00;	
    		
test bool testReal28() = 2.0 * 3 == 6.0;	
test bool testReal29() = -2 * 3.0 == -6.0;	
test bool testReal30() = 2.0 * (-3) == -6.0;
test bool testReal31() = -2 * (-3.0) == 6.0;	
    		
test bool testReal32() = 8.0 / 4.0 == 2e0;	
test bool testReal33() = -8.0 / 4.0 == -2e0;
test bool testReal34() = 8.0 / -4.0 == -2e0;	
test bool testReal35() = -8.0 / -4.0 == 2e0;
    		
// TODO, I don't get it, why does the previous have 1 digit precision and this
// one two digits
test bool testReal36() = 7.0 / 2.0 == 3.5;	
test bool testReal37() = -7.0 / 2.0 == -3.5;
test bool testReal38() = 7.0 / -2.0 == -3.5;	
test bool testReal39() = -7.0 / -2.0 == 3.5;	
    		
test bool testReal40() = 0.0 / 5.0 == 0.0;	
test bool testReal41() = 5.0 / 1.0 == 5.0;	
    		
test bool testReal42() = 7 / 2.0 == 3.5;	
test bool testReal43() = -7.0 / 2 == -3.5;
test bool testReal44() = 7 / -2.0 == -3.5;	
test bool testReal45() = -7.0 / -2 == 3.5;	
    		
test bool testReal46() = -2.0 <= -1.0;
test bool testReal47() = -2.0 <= 1.0;
test bool testReal48() = 1.0 <= 2.0;
test bool testReal49() = 2.0 <= 2.0;
test bool testReal50() = !(2.0 <= 1.0);
    		
test bool testReal51() = -2 <= -1.0;
test bool testReal52() = -2.0 <= 1;
test bool testReal53() = 1 <= 2.0;
test bool testReal54() = 2.0 <= 2;
test bool testReal55() = !(2 <= 1.0);
    		
test bool testReal56() = -2.0 < -1.0;
test bool testReal57() = -2.0 < 1.0;
test bool testReal58() = 1.0 < 2.0;
test bool testReal59() = !(2.0 < 2.0);
    		
test bool testReal60() = -2 < -1.0;
test bool testReal61() = -2.0 < 1;
test bool testReal62() = 1 < 2.0;
test bool testReal63() = !(2.0 < 2);
    		
test bool testReal64() = -1.0 >= -2.0;
test bool testReal65() = 1.0 >= -1.0;
test bool testReal66() = 2.0 >= 1.0;
test bool testReal67() = 2.0 >= 2.0;
test bool testReal68() = !(1.0 >= 2.0);
    		
test bool testReal69() = -1 >= -2.0;
test bool testReal70() = 1.0 >= -1;
test bool testReal71() = 2 >= 1.0;
test bool testReal72() = 2.0 >= 2;
test bool testReal73() = !(1 >= 2.0);
    		
test bool testReal74() = -1.0 > -2.0;
test bool testReal75() = 1.0 > -1.0;
test bool testReal76() = 2.0 > 1.0;
test bool testReal77() = !(2.0 > 2.0);
test bool testReal78() = !(1.0 > 2.0);
    		
test bool testReal79() = -1 > -2.0;
test bool testReal80() = 1.0 > -1;
test bool testReal81() = 2 > 1.0;
test bool testReal82() = !(2.0 > 2);
test bool testReal83() = !(1 > 2.0);
    		
test bool testReal84() = ((3.5 > 2.5) ? 3.5 : 2.5) == 3.5;
    		
test bool testReal85() = ((3.5 > 2) ? 3.5 : 2) == 3.5;
test bool testReal86() = ((3.5 > 4) ? 3.5 : 2) == 2;
    	
//	 testNumber
    
test bool testNumber1() {num n = 1; return n == 1;}
test bool testNumber2() {num n = 1;  return 1 == n;}
    		
test bool testNumber3() {num n = 1;  return n != 2;}
test bool testNumber4() {num n = 1;  return 2 != n;}
    		
test bool testNumber5() {num n = 1;  return n + 1 == 2;}
test bool testNumber6() {num n = 1;  return 1 + n == 2;}
    		
test bool testNumber7() {num n = 2; return n - 1 == 1;}	
test bool testNumber8() {num n = 2; return 1 - n == -1;}	
    		
test bool testNumber9() {num n = 2; return n * 3 == 6;}
test bool testNumber10() {num n = 2; return 3 * n == 6;}
    		
test bool testNumber11() {num n = 8; return n / 4 == 2;}	
test bool testNumber12() {num n = 4; return 8 / n == 2;}	
    	
test bool testNumber13() {num n = 1; return n <= 2;}
test bool testNumber14() {num n = 1; return 0 <= n;}
    		
test bool testNumber15() {num n = 1; return n < 2;} 
test bool testNumber16() {num n = 1; return 0 < n;}
    		
test bool testNumber17() {num n = 2; return n >= 1;}
test bool testNumber18() {num n = 1; return 2 >= n;}
    		
test bool testNumber19() {num n = 2; return n > 1;}         
test bool testNumber20() {num n = 1; return 2 > n;}
    		
test bool testNumber21() {num n = 1; return 2 > n;}
    		
test bool testNumber22() {num n35 = 3.5; num n2 = 2; return ((n35 > n2) ? 3.5 : 2) == 3.5;}
    	
//	 testString
    
test bool testString1() = "" == "";
test bool testString2() = "abc" != "";
test bool testString3() = "abc" == "abc";
test bool testString4() = "abc" != "def";
    		
test bool testString5() = "abc" + "" == "abc";
test bool testString6() = "abc" + "def" == "abcdef";
    		
test bool testString7() = "" <= "";
test bool testString8() = "" <= "abc";
test bool testString9() = "abc" <= "abc";
test bool testString10() = "abc" <= "def";
    		
test bool testString11() = !("" < "");
test bool testString12() = "" < "abc";
test bool testString13() = !("abc" < "abc");
test bool testString14() = "abc" < "def";
    		
test bool testString15() = "" >= "";
test bool testString16() = "abc" >= "";
test bool testString17() = "abc" >= "abc";
test bool testString18() = "def" >= "abc";
    		
test bool testString19() = !("" > "");
test bool testString20() = "abc" > "";
test bool testString21() = !("abc" > "abc");
test bool testString22() = "def" > "abc";
    	
//	 stringEscapes
    
test bool testStringEscapes1a() = "\\b" == "\\b";
test bool testStringEscapes1b() = "\\" + "b" == "\\b";

test bool testStringEscapes2a() = "\\t" == "\\t";
test bool testStringEscapes2b() = "\\" + "t" == "\\t";

test bool testStringEscapes3a() = "\\n" == "\\n";
test bool testStringEscapes3b() = "\\" + "n" == "\\n";

test bool testStringEscapes4a() = "\\f" == "\\f";
test bool testStringEscapes4b() = "\\" + "f" == "\\f";

test bool testStringEscapes5a() = "\\r" == "\\r";
test bool testStringEscapes5b() = "\\" + "r" == "\\r";
    		
test bool testStringEscapes6a() = "\"\"" == "\"\"";
test bool testStringEscapes6b() = "\"" + "\"" == "\"\"";

test bool testStringEscapes7a() = "\\\'" == "\\\'";
test bool testStringEscapes7b() = "\\" + "\'" == "\\\'";

test bool testStringEscapes8a() = "\\\\" == "\\\\";
test bool testStringEscapes8b() = "\\" + "\\" == "\\\\";

test bool testStringEscapes9a() = "\"\<" == "\"\<";
test bool testStringEscapes9b() = "\"" + "\<" == "\"\<";

test bool testStringEscapes10a() = "\"\>" == "\"\>";
test bool testStringEscapes10b() = "\"" + "\>" == "\"\>";
    		
test bool testStringEscapes11() = "\a20" == " ";
test bool testStringEscapes12() = "\U01F35D" == "🍝";
test bool testStringEscapes13() = "\u2713" == "✓";
    	
//	 stringInterpolation
    
test bool testStringInterpolation1() {str a = "abc"; return "1<a>2" == "1abc2";}
test bool testStringInterpolation2() {int a = 789; return "1<a>2" == "17892";}
    		
test bool testStringInterpolation3() {str a = "a\\bc"; return "1<a>2" == "1a\\bc2";}
test bool testStringInterpolation4() {str a = "a\\tc"; return "1<a>2" == "1a\\tc2";}
test bool testStringInterpolation5() {str a = "a\\nc"; return "1<a>2" == "1a\\nc2";}
test bool testStringInterpolation6() {str a = "a\\fc"; return "1<a>2" == "1a\\fc2";}
test bool testStringInterpolation7() {str a = "a\\rc"; return "1<a>2" == "1a\\rc2";}
    		
test bool testStringInterpolation8() {str a = "a\\\"c"; return "1<a>2" == "1a\\\"c2";}
test bool testStringInterpolation9() {str a = "a\\\'c"; return "1<a>2" == "1a\\\'c2";}
test bool testStringInterpolation10() {str a = "a\\\\c"; return "1<a>2" == "1a\\\\c2";}
    		
test bool testStringInterpolation11() {str a = "a\<c"; return "1<a>2" == "1a\<c2";}
test bool testStringInterpolation12() {str a = "a\>c"; return "1<a>2" == "1a\>c2";}

test bool testStringInterpolation13() {str a = "abc"; return "1\\<a>2" == "1\\abc2";}
test bool testStringInterpolation14() {str a = "abc"; return "1<a>\\2" == "1abc\\2";}
test bool testStringInterpolation15() {str a = "abc"; return "1\<<a>2" == "1\<abc2";}
test bool testStringInterpolation16() {str a = "abc"; return "1<a>\>2" == "1abc\>2";}

test bool testStringInterpolation17() {str a = "a\\bc"; return "1\\<a>2" == "1\\a\\bc2";}
test bool testStringInterpolation18() {str a = "a\\bc"; return "1<a>\\2" == "1a\\bc\\2";}
test bool testStringInterpolation19() {str a = "a\\bc"; return "1\<<a>2" == "1\<a\\bc2";}
test bool testStringInterpolation20() {str a = "a\\bc"; return "1<a>\>2" == "1a\\bc\>2";}
    	
loc Loc = |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>);
loc Loc2 = |file:///home/paulk/pico2.trm|(0,1,<2,3>,<4,5>);	
    
// testLocation    		
    		
test bool testLocation1() {Loc ; return true;}
test bool testLocation2() =  Loc == Loc;
test bool testLocation3() = Loc != Loc2;
    		
test bool testLocationFieldUse1() = Loc.uri == "file:///home/paulk/pico.trm";
test bool testLocationFieldUse2() = Loc.offset == 0;
test bool testLocationFieldUse3() = Loc.length == 1;
test bool testLocationFieldUse5() = Loc.begin.line == 2;
test bool testLocationFieldUse6() = Loc.begin.column == 3;
test bool testLocationFieldUse7() = Loc.end.line == 4;
test bool testLocationFieldUse8() = Loc.end.column == 5;
test bool testLocationFieldUse9() = Loc.path == "/home/paulk/pico.trm";
    		
test bool testLocationFieldUpdate1() { loc l = Loc; l.uri = "file:///home/paulk/pico2.trm"; return l.uri == "file:///home/paulk/pico2.trm";}
test bool testLocationFieldUpdate2() { loc l = Loc; l.offset = 10; return l.offset == 10;}
test bool testLocationFieldUpdate3() { loc l = Loc; l.length = 11; return l.length == 11;}
test bool testLocationFieldUpdate4() { loc l = Loc; l.end.line = 14; return l.end.line == 14;}
test bool testLocationFieldUpdate5() { loc l = Loc; l.begin.line = 1; return l.begin.line == 1;}
test bool testLocationFieldUpdate6() { loc l = Loc; l.begin.column = 13; return l.begin.column == 13;}
test bool testLocationFieldUpdate7() { loc l = Loc; l.end.column = 15; return l.end.column == 15;}
    		
test bool testLocationFieldUpdate8() {loc l = Loc[uri= "file:///home/paulk/pico.trm"]; return l == |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>);}
test bool testLocationFieldUpdate9() {loc l = Loc[offset = 10]; return l == |file:///home/paulk/pico.trm|(10,1,<2,3>,<4,5>);}
test bool testLocationFieldUpdate10() {loc l = Loc[length = 11]; return l ==  |file:///home/paulk/pico.trm|(0,11,<2,3>,<4,5>);}
test bool testLocationFieldUpdate12() {loc l = Loc[begin = <1,4>]; return l == |file:///home/paulk/pico.trm|(0,1,<1,4>,<4,5>);}
test bool testLocationFieldUpdate13() {loc l = Loc[end = <14,38>]; return l ==  |file:///home/paulk/pico.trm|(0,1,<2,3>,<14,38>);}
    	
test bool testLocation12() = |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>) == |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>);
test bool testLocation13() = !(|file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>) == |file:///home/paulk/pico.trm|(0,2,<2,3>,<4,5>));
test bool testLocation14() = !(|file:///home/paulk/pico1.trm|(0,1,<2,3>,<4,5>) == |file:///home/paulk/pico2.trm|(0,1,<2,3>,<4,5>));
    		
test bool testLocation15() = |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>) != |file:///home/paulk/pico2.trm|(0,1,<2,3>,<4,5>);
test bool testLocation16() = |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>) != |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,7>);
test bool testLocation17() = !(|file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>) != |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>));
test bool testLocation18() = |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) != |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,7>);
    		
test bool testLocation19() = !(|file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) < |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>));
test bool testLocation20() = |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) < |file:///home/paulk/pico.trm|(2,2,<2,3>,<4,5>);
test bool testLocation21() = !(|file:///home/paulk/pico.trm|(1,1,<2,3>,<4,5>) < |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>));
test bool testLocation22() = !(|file:///home/paulk/pico.trm|(1,2,<2,3>,<4,5>) < |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>));
    	
test bool testLocation23() = |file:///home/paulk/pico1.trm|(2,1,<2,3>,<4,5>) <= |file:///home/paulk/pico2.trm|(2,1,<2,3>,<4,5>);
test bool testLocation24() = !(|file:///home/paulk/pico2.trm|(2,1,<2,3>,<4,5>) <= |file:///home/paulk/pico1.trm|(2,1,<2,3>,<4,5>));
test bool testLocation25() = |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) <= |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>);
test bool testLocation26() = |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) <= |file:///home/paulk/pico.trm|(2,2,<2,3>,<4,5>);
test bool testLocation27() = !(|file:///home/paulk/pico.trm|(2,2,<2,3>,<4,5>) <= |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>));
    		
test bool testLocation28() = |file:///home/paulk/pico2.trm|(2,1,<2,3>,<4,5>) > |file:///home/paulk/pico1.trm|(2,1,<2,3>,<4,5>);
test bool testLocation29() = !(|file:///home/paulk/pico1.trm|(2,1,<2,3>,<4,5>) > |file:///home/paulk/pico2.trm|(2,1,<2,3>,<4,5>));
test bool testLocation30() = !(|file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) > |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>));
test bool testLocation31() = |file:///home/paulk/pico.trm|(2,2,<2,3>,<4,5>) > |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>);
test bool testLocation32() = !(|file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) > |file:///home/paulk/pico.trm|(2,2,<2,3>,<4,5>));
    		
test bool testLocation33() = |file:///home/paulk/pico2.trm|(2,1,<2,3>,<4,5>) >= |file:///home/paulk/pico1.trm|(2,1,<2,3>,<4,5>);
test bool testLocation34() = !(|file:///home/paulk/pico1.trm|(2,1,<2,3>,<4,5>) >= |file:///home/paulk/pico2.trm|(2,1,<2,3>,<4,5>));
test bool testLocation35() = |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) >= |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>);
test bool testLocation36() = |file:///home/paulk/pico.trm|(2,2,<2,3>,<4,5>) >= |file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>);
test bool testLocation37() = !(|file:///home/paulk/pico.trm|(2,1,<2,3>,<4,5>) >= |file:///home/paulk/pico.trm|(2,2,<2,3>,<4,5>));
    		
test bool testLocation38() = |file:///xxx|(45,1,<1,45>,<1,46>) <= |file:///xxx|(40,6,<1,40>,<1,46>);
test bool testLocation39() = |file:///xxx|(45,1,<1,45>,<1,46>) <= |file:///xxx|(40,7,<1,40>,<1,47>);


test bool testLocation40() =
  |project://rascal/xxx.rsc|(5667,18,<160,16>,<160,34>)
< |project://rascal/xxx.rsc|(5661,24,<160,10>,<160,34>);

test bool testLocation41() =
  |project://rascal/xxx.rsc|(5667,18,<160,16>,<160,34>)
<= |project://rascal/xxx.rsc|(5661,24,<160,10>,<160,34>);

test bool testLocation42() =
  |project://rascal/xxx.rsc|(5661,24,<160,10>,<160,34>)
> |project://rascal/xxx.rsc|(5667,18,<160,16>,<160,34>);

test bool testLocation43() =
  |project://rascal/xxx.rsc|(5661,24,<160,10>,<160,34>)
>= |project://rascal/xxx.rsc|(5667,18,<160,16>,<160,34>);
    	  	
// 	testList
    	
test bool testList1() = [] == [];
test bool testList2() = [] != [1];
test bool testList3() = [1] == [1];
test bool testList4() = [1] != [2];
test bool testList5() = [1, 2] == [1, 2];
test bool testList6() = [1, 2] != [2, 1];
    		
test bool testList7() = [] + [] == [];
test bool testList8() = [1, 2, 3] + [] == [1, 2, 3];
test bool testList9() = [] + [1, 2, 3] == [1, 2, 3];
test bool testList10() = [1, 2] + [3, 4, 5] == [1, 2, 3, 4, 5];	
    		
test bool testList11() = ([1, 2] + [3, 4]) + [5] == [1, 2, 3, 4, 5];	
test bool testList12() = [1, 2] + ([3, 4] + [5]) == [1, 2, 3, 4, 5];	
test bool testList13() = [1, 2] + [3, 4] + [5] == [1, 2, 3, 4, 5];
    		
test bool testList14() = [1, 2] + 3 == [1, 2, 3];
test bool testList15() = 1 +  [2, 3] == [1, 2, 3];
    		
test bool testList16() = [1,2,1,2,3,4,3,4,5] - 1 == [2,1,2,3,4,3,4,5];
test bool testList17() = [1,2,1,2,3,4,3,4,5] - 2 == [1,1,2,3,4,3,4,5];
test bool testList18() = [1,2,1,2,3,4,3,4,5] - 5 == [1,2,1,2,3,4,3,4];
    		
test bool testList19() = [1,2,1,2,3,4,3,4,5] - [1] == [2,1,2,3,4,3,4,5];
test bool testList20() = [1,2,1,2,3,4,3,4,5] - [2] == [1,1,2,3,4,3,4,5];
test bool testList21() = [1,2,1,2,3,4,3,4,5] - [5] == [1,2,1,2,3,4,3,4];
    		
test bool testList22() = [1,2,1,2,3,4,3,4,5] - [1,1] == [2,2,3,4,3,4,5];
test bool testList23() = [1,2,1,2,3,4,3,4,5] - [1,1,1] == [2,2,3,4,3,4,5];
    		
test bool testList24() = [1,2,1,2,3,4,3,4,5] - [1,2] == [1,2,3,4,3,4,5];
test bool testList25() = [1,2,1,2,3,4,3,4,5] - [2,3] == [1,1,2,4,3,4,5];
    		
test bool testList26() = [] & [1,2,4] == [];
test bool testList27() = [1,2,3] & [] == [];
test bool testList28() = [1,2,3,4,5,4,3,2,1] & [1,2,4] == [1,2,4,4,2,1];
    		
test bool testList29() = [] <= [];
test bool testList30() = [] <= [1];
    		
    /*TODO:REMOVE?*/		
    // These commented out tests assume that <= etc. are ("half") ordering operations
    // Currently they are strictly subset implementations.
    //		test bool testList() = [2, 1, 0] <= [2, 3];
    //		test bool testList() = [2, 1] <= [2, 3, 0];
    
test bool testList31() = [2, 1] <= [2, 1];
test bool testList32() = [2, 1] <= [2, 1, 0];
    		
test bool testList33() = [] < [1];
//		test bool testList() = [2, 1, 0] < [2, 3];
//		test bool testList() = [2, 1] < [2, 3, 0];
test bool testList34() = [2, 1] < [2, 1, 0];
    		
test bool testList35() = [] >= [];
//		test bool testList() = [1] >= [];
//		test bool testList() = [2, 3] >= [2, 1, 0];
//		test bool testList() = [2, 3, 0] >= [2, 1];
test bool testList36() = [2, 1] >= [2, 1];
test bool testList37() = [2, 1, 0] >= [2, 1];
    		
test bool testList38() = [1] > [];
//		test bool testList() = [2, 3] > [2, 1, 0];
//		test bool testList() = [2, 3, 0] > [2, 1];
test bool testList39() = [2, 1, 0] > [2, 1];
    		
test bool testList40() = [] * [] == [];
test bool testList41() = [1] * [9] == [<1,9>];
test bool testList42() = [1, 2] * [9] == [<1,9>, <2,9>];
test bool testList43() = [1, 2, 3] * [9] == [<1,9>, <2,9>, <3,9>];
test bool testList44() = [1, 2, 3] * [9, 10] == [<1,9>, <1,10>, <2,9>, <2,10>, <3,9>, <3,10>];
    		
test bool testList45() = 2 in [1, 2, 3];
test bool testList46() = 3 notin [2, 4, 6];
    		
test bool testList47() = (2 > 3 ? [1,2] : [1,2,3]) == [1,2,3];

@ignoreInterpreter{
Not implemented
}
test bool testList48() = 1 >> [2, 3] == [1, 2, 3];

@ignoreInterpreter{
Not implemented
}
test bool testList49() = [2, 3] << 4 == [2, 3, 4];

    
	@expected{
IndexOutOfBounds
}
test bool  SubscriptError11() { 		[1,2][5];return false;  	}
    	
//	 listSplicing

@ignoreCompiler{
INCOMPATIBILITY: Splicing no longer allowed on arbitrary types
}
test bool testListSplicing1() =  [1,2,3] == [1,2,3];
@ignoreCompiler{
INCOMPATIBILITY: Splicing no longer allowed on arbitrary types
}
test bool testListSplicing2() = [*1,2,3] == [1,2,3];
@ignoreCompiler{
INCOMPATIBILITY: Splicing no longer allowed on arbitrary types
}
test bool testListSplicing3() = [1,*2,3] == [1,2,3];
@ignoreCompiler{
INCOMPATIBILITY: Splicing no longer allowed on arbitrary types
}
test bool testListSplicing4() = [1,2,*3] == [1,2,3];
@ignoreCompiler{
INCOMPATIBILITY: Splicing no longer allowed on arbitrary types
}
test bool testListSplicing5() = [*1,*2,3] == [1,2,3];
    		
test bool testListSplicing6() {list[int] L1 = [1,2]; return [L1] == [[1,2]];}
test bool testListSplicing7() {list[int] L1 = [1,2]; return [*L1] == [1,2];}
    		
test bool testListSplicing8() {list[int] L1 = [1,2]; return [L1,3] == [[1,2],3];}
test bool testListSplicing9() {list[int] L1 = [1,2]; return [*L1,3] == [1,2,3];}
    		
test bool testListSplicing10() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1,L2] == [[1,2],[3,4]];}
test bool testListSplicing11() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1,L2] == [1,2,[3,4]];}
test bool testListSplicing12() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1,*L2] == [[1,2],3,4];}
test bool testListSplicing13() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1,*L2] == [1,2,3,4];}
    		
test bool testListSplicing14() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1,L2,5] == [[1,2],[3,4],5];}
test bool testListSplicing15() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1,L2,5] == [1,2,[3,4],5];}
test bool testListSplicing16() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1,*L2,5] == [[1,2],3,4,5];}
test bool testListSplicing17() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1,*L2,5] == [1,2,3,4,5];}
    		
test bool testListSplicing18() {list[int] L1 = [1,2]; return [[L1]] == [[[1,2]]];}
test bool testListSplicing19() {list[int] L1 = [1,2]; return [[*L1]] == [[1,2]];}
    		
test bool testListSplicing20() {list[int] L1 = [1,2]; return [[L1],3] == [[[1,2]],3];}
test bool testListSplicing21() {list[int] L1 = [1,2]; return [[*L1],3] == [[1,2],3];}	
    		
test bool testListSplicing22() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [[L1],[L2]] == [[[1,2]],[[3,4]]];}
test bool testListSplicing23() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [[*L1],[L2]] == [[1,2],[[3,4]]];}
    		
test bool testListSplicing24() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [[L1],[*L2]] == [[[1,2]],[3,4]];}
test bool testListSplicing25() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [[*L1],[*L2]] == [[1,2],[3,4]];}
    		
test bool testListSplicing26() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*[*L1],[*L2]] == [1,2,[3,4]];}
test bool testListSplicing27() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [[*L1],*[*L2]] == [[1,2],3,4];}
test bool testListSplicing28() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*[*L1],*[*L2]] == [1,2,3,4];}
    	
test bool testListSplicing29() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1,[L2]] == [[1,2],[[3,4]]];}
test bool testListSplicing30() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1,[L2]] == [1,2,[[3,4]]];}
test bool testListSplicing31() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1,[*L2]] == [[1,2],[3,4]];}
test bool testListSplicing32() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1,[*L2]] == [1,2,[3,4]];}
test bool testListSplicing33() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1,*[*L2]] == [1,2,3,4];}
    
test bool testListSplicing34() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1,[L2],5] == [[1,2],[[3,4]],5];}
test bool testListSplicing35() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [*L1,[L2],5] == [1,2,[[3,4]],5];}
test bool testListSplicing36() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; return [L1,[*L2],5] == [[1,2],[3,4],5];}
    		
test bool testListSplicing37() {list[int] L1 = [1,2]; list[list[list[int]]] L3 = [[L1]]; return L3 == [[[1,2]]];}
test bool testListSplicing38() {list[int] L1 = [1,2]; list[value] L3 = [[L1],3]; return L3 == [[[1,2]],3];}
test bool testListSplicing39() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; list[list[list[int]]] L3 = [[L1],[L2]]; return L3 == [[[1,2]],[[3,4]]];}
test bool testListSplicing40() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; list[value] L3 = [L1,[L2]]; return L3 == [[1,2],[[3,4]]];}
test bool testListSplicing41() {list[int] L1 = [1,2]; list[int] L2 = [3,4]; list[value] L3 = [L1,[L2],5]; return L3 == [[1,2],[[3,4]],5];}
    
//	 testSetInListSplicing
    
test bool testSetInListSplicing1() {list[int] L1 = [1,2]; set[int] S2 = {3,4}; return [L1,S2,5] == [[1,2],{3,4},5];}
test bool testSetInListSplicing2() {list[int] L1 = [1,2]; set[int] S2 = {3,4}; return [*L1,S2,5] == [1,2,{3,4},5];}
test bool testSetInListSplicing3() {list[int] L1 = [1,2]; set[int] S2 = {3,4}; return ([L1,*S2,5] == [[1,2],3,4,5]) || ([L1,*S2,5] == [[1,2],4,3,5]);}
test bool testSetInListSplicing4() {list[int] L1 = [1,2]; set[int] S2 = {3,4}; return ([*L1,*S2,5] == [1,2,3,4,5]) || ([*L1,*S2,5] == [1,2,4,3,5]) ;}
    
//	 testRange
    		
test bool testRange1() = [1 .. 1] == [];
test bool testRange2() = [1 .. 2] == [1];
test bool testRange3() = [1 .. -1] == [1, 0];
test bool testRange4() = [1, 2 .. 10] == [1,2,3,4,5,6,7,8,9];
test bool testRange5() = [1, 3 .. 10] == [1,3,5,7,9];
test bool testRange6() = [1, -2 .. 10] == [];
test bool testRange7() = [1, -3 .. -10] == [1,-3,-7];
    	
//	 testSet
    		
test bool testSet1() = {} == {};
test bool testSet2() = {} != {1};
test bool testSet3() = {1} == {1};
test bool testSet4() = {1} != {2};
test bool testSet5() = {1, 2} == {1, 2};
test bool testSet6() = {1, 2} == {2, 1};
test bool testSet7() = {1, 2, 3, 1, 2, 3} == {3, 2, 1};	
    		
test bool testSet8() = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
test bool testSet9() = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 2, 3, 4, 5, 6, 7, 8, 9, 1};
test bool testSet10() = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 9, 3, 4, 5, 6, 7, 8, 2, 1};
test bool testSet11() = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 9, 7, 4, 5, 6, 3, 8, 2, 1};
test bool testSet12() = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10} == {10, 9, 7, 6, 5, 4, 3, 8, 2, 1};
    		
test bool testSet13() = {{1}, {2}} == {{2}, {1}};
test bool testSet14() = {{}} == {{}};
test bool testSet15() = {{}, {}} == {{}};
test bool testSet16() = {{}, {}, {}} == {{}};
    		
test bool testSet17() = {{1, 2}, {3,4}} == {{2,1}, {4,3}};	
    	
test bool testSet18() = {} + {} == {};
test bool testSet19() = {1, 2, 3} + {} == {1, 2, 3};
test bool testSet20() = {} + {1, 2, 3} == {1, 2, 3};
test bool testSet21() = {1, 2} + {3, 4, 5} == {1, 2, 3, 4, 5};	
test bool testSet22() = {1, 2, 3, 4} + {3, 4, 5} == {1, 2, 3, 4, 5};
test bool testSet23() = {{1, 2}, {3,4}} + {{5,6}} == {{1,2},{3,4},{5,6}};
test bool testSet24() = 1 + {2,3} == {1,2,3};
test bool testSet25() = {1,2} + 3 == {1,2,3};
    		
test bool testSet26() = {} - {} == {};
test bool testSet27() = {1, 2, 3} - {} == {1, 2, 3};
test bool testSet28() = {} - {1, 2, 3} == {};
test bool testSet29() = {1, 2, 3} - {3, 4, 5} == {1, 2};	
test bool testSet30() = {1, 2, 3, 4} - {1, 2, 3, 4, 5} == {};
test bool testSet31() = {{1, 2}, {3,4}, {5,6}} - {{3,4}} == {{1,2}, {5,6}};
test bool testSet32() = {1,2,3} - 3 == {1,2};
    		
test bool testSet33() = {} & {} == {};
test bool testSet34() = {1, 2, 3} & {} == {};
test bool testSet35() = {} & {1, 2, 3} == {};
test bool testSet36() = {1, 2, 3} & {3, 4, 5} == {3};	
test bool testSet37() = {1, 2, 3, 4} & {3, 4, 5} == {3, 4};	
test bool testSet38() = {{1,2},{3,4},{5,6}} & {{2,1}, {8,7}, {6,5}} == {{1,2},{5,6}};
    		
test bool testSet39() = {} <= {};
test bool testSet40() = {} <= {1};
test bool testSet41() = {2, 1} <= {1, 2};
test bool testSet42() = {2, 1} <= {1, 2, 3};
test bool testSet43() = {2, 1} <= {2, 1, 0};
    	
test bool testSet44() = {} < {1};
test bool testSet45() = {2, 1} < {2, 1, 3};
    	
test bool testSet46() = {} >= {};
test bool testSet47() = {1} >= {};
test bool testSet48() = {2, 3} >= {2};
    	
test bool testSet49() = {1} > {};
test bool testSet50() = {2, 1, 3} > {2, 3};
    		
test bool testSet51() = {} * {} == {};
test bool testSet52() = {1} * {9} == {<1,9>};
test bool testSet53() = {1, 2} * {9} == {<1,9>, <2,9>};
test bool testSet54() = {1, 2, 3} * {9} == {<1,9>, <2,9>, <3,9>};
test bool testSet55() = {1, 2, 3} * {9, 10} == {<1,9>, <1,10>, <2,9>, <2,10>, <3,9>, <3,10>};
    		
test bool testSet56() = 2 in {1, 2, 3};
test bool testSet57() = {4,3} in {{1, 2}, {3,4}, {5,6}};
    		
test bool testSet58() = 5 notin {1, 2, 3};
test bool testSet59() = {7,8} notin {{1, 2}, {3,4}, {5,6}};
    		
test bool testSet60() = ((3 > 2) ? {1,2} : {1,2,3}) == {1,2};
    		
test bool testSet61() = {<"a", [1,2]>, <"b", []>, <"c", [4,5,6]>} != {};
   
// Some nested set patterns to test backtracking behaviour.
    	
data TYPESET = SET(str name) | SUBTYPES(TYPESET tset) | INTERSECT(set[TYPESET] tsets);
    
// 	testSet
    		
test bool testSet62() = {INTERSECT({TYPESET _, *TYPESET _}), TYPESET _} :=  {INTERSECT({SET("a"), SET("b")}), SET("c")};
test bool testSet63() = {INTERSECT({TYPESET t1, *TYPESET _}),  t1} :=  {INTERSECT({SET("a"), SET("b")}), SET("a")};
test bool testSet64() = {INTERSECT({TYPESET t1, *TYPESET _}),  t1} :=  {INTERSECT({SET("b"), SET("a")}), SET("a")};
    
test bool testSet65() =  { <t1, t2> | INTERSECT({TYPESET t1, *TYPESET t2}) :=  INTERSECT({SET("b"), SET("a")})} == 
    						   		        { <SET("b"),{SET("a")}>, <SET("a"),{SET("b")}>};
    	  	
test bool testSet66() = {<t1, rest, t2> | {INTERSECT({TYPESET t1, *TYPESET rest}),  t2} :=  {INTERSECT({SET("a"), SET("b")}) , SET("b")}}==
    				           { <SET("a"),{SET("b")},SET("b")>, <SET("b"),{SET("a")},SET("b")>};
    
test bool testSet67() = {<t1, rest> | {INTERSECT({TYPESET t1, *TYPESET rest}),  t1} :=  {INTERSECT({SET("a"), SET("b")}), SET("b")}}==
    				           {<SET("b"),{SET("a")}>};
    
    // TYPESET tests moved to SetMatch[12]

// testSetMultiVariable
    
test bool testSetMultiVariable1() = {*value S1, *value S2} := {} && (S1 == {}) && (S2 == {});
test bool testSetMultiVariable2() = {*S1, *S2} := {} && (S1 == {}) && (S2 == {});
    		
test bool testSetMultiVariable3() = {*int S1, *int S2} := {100} && ((S1 == {100} && S2 == {}) || (S1 == {} && S2 == {100}));
test bool testSetMultiVariable4() = {*S1, *S2} := {100} && ((S1 == {100} && S2 == {}) || (S1 == {} && S2 == {100}));
    		
test bool testSetMultiVariable5()  {R = for({*int S1, *int S2} := {100}) append <S1, S2>; return toSet(R) == { <{100}, {}>, <{}, {100}> };}
test bool testSetMultiVariable6()  {R = for({*S1, *S2} := {100}) append <S1, S2>; return toSet(R) == { <{100}, {}>, <{}, {100}> };}
    
test bool testSetMultiVariable7()  {R = for({*S1, *S2} := {100}) append <S1, S2>; return toSet(R) == { <{100}, {}>, <{}, {100}> };}
    		//
    		// TODO: the following test requires a specific implementation specific
    		// set representation and, thus, should be refactored. To check
    		// splicing, without taking order into account, the list 'R' is now
    		// converted to a set.
    		//
test bool testSetMultiVariable8()  {R = for({*S1, *S2} := {100, 200}) append <S1, S2>; return toSet(R) == {<{200,100}, {}>, <{200}, {100}>, <{100}, {200}>, <{}, {200,100}>};}
test bool testSetMultiVariable9()  {R = for({*int S1, *S2} := {100, "a"})  append <S1, S2>; return toSet(R) == { <{100}, {"a"}>, <{},{100,"a"}> };}
test bool testSetMultiVariable10()  {R = for({*int S1, *str S2} := {100, "a"}) append <S1, S2>; return toSet(R) == { <{100}, {"a"}> };}
    		
test bool testSetMultiVariable11()  {R = for({*str S1, *S2} := {100, "a"})  append <S1, S2>; return toSet(R) == { <{"a"},{100}>, <{},{100,"a"}> };}
test bool testSetMultiVariable12()  {R = for({*str S1, *int S2} := {100, "a"})  append <S1, S2>; return toSet(R) == { <{"a"},{100}> };}
    		
test bool testSetMultiVariable13() = !({*str _, *str _} := {100, "a"});
test bool testSetMultiVariable14() = !({*int _, *int _} := {100, "a"});
    
      
test bool addSetError1() { 		return {1,2,3} + true=={1,2,3,true}; }
    	
	// setSplicing
        
test bool testSetSplicing1() {set[int] S1 = {1,2}; return {S1} == {{1,2}};}
test bool testSetSplicing2() {set[int] S1 = {1,2}; return {*S1} == {1,2};}
    		
test bool testSetSplicing3() {set[int] S1 = {1,2}; return {S1,3} == {{1,2},3};}
test bool testSetSplicing4() {set[int] S1 = {1,2}; return {*S1,3} == {1,2,3};}
    		
test bool tetestSetSplicing5() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,S2} == {{1,2},{3,4}};}
test bool testSetSplicing6() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*S1,S2} == {1,2,{3,4}};}
test bool testSetSplicing7() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,*S2} == {{1,2},3,4};}
test bool testSetSplicing8() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*S1,*S2} == {1,2,3,4};}
    	
test bool testSetSplicing9() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,S2,5} == {{1,2},{3,4},5};}
test bool testSetSplicing10() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*S1,S2,5} == {1,2,{3,4},5};}
test bool testSetSplicing11() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,*S2,5} == {{1,2},3,4,5};}
test bool testSetSplicing12() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*S1,*S2,5} == {1,2,3,4,5};}
    		
test bool testSetSplicing13() {set[int] S1 = {1,2}; return {{S1}} == {{{1,2}}};}
    		
test bool testSetSplicing14() {set[int] S1 = {1,2}; return {{*S1}}   == {{1,2}};}
test bool testSetSplicing15() {set[int] S1 = {1,2};  return {*{*S1}} == {1,2};}
    		
test bool testSetSplicing16() {set[int] S1 = {1,2}; return {{S1},3} == {{{1,2}},3};}
test bool testSetSplicing17() {set[int] S1 = {1,2}; return {*{S1},3} == {{1,2},3};}
test bool testSetSplicing18() {set[int] S1 = {1,2}; return {*{*S1},3} == {1,2,3};}
    		
test bool testSetSplicing19() {set[int] S1 = {1,2}; return {*{*S1},2} == {1,2};}
    	
test bool testSetSplicing20() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {{S1},{S2}} == {{{1,2}},{{3,4}}};}
test bool testSetSplicing21() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*{S1},{S2}} == {{1,2},{{3,4}}};}
test bool testSetSplicing22() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {{S1},*{S2}} == {{{1,2}},{3,4}};}
test bool testSetSplicing23() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*{S1},*{S2}} == {{1,2},{3,4}};}
test bool testSetSplicing24() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*{*S1},*{*S2}} == {1,2,3,4};}
    		 		
test bool testSetSplicing25() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,{S2}} == {{1,2},{{3,4}}};}
test bool testSetSplicing26() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,{*S2}} == {{1,2},{3,4}};}
test bool testSetSplicing27() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,*{*S2}} == {{1,2},3,4};}
    				
test bool testSetSplicing28() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,{S2},5} == {{1,2},{{3,4}},5};}
test bool testSetSplicing29() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,{*S2},5} == {{1,2},{3,4},5};}
test bool testSetSplicing30() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {S1,*{*S2},5} == {{1,2},3,4,5};}
test bool testSetSplicing31() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; return {*S1,*{*S2},5} == {1,2,3,4,5};}
    		
test bool testSetSplicing32() {set[int] S1 = {1,2}; set[set[set[int]]] S3 = {{S1}}; return S3 == {{{1,2}}};}
    		
test bool testSetSplicing33() {set[int] S1 = {1,2}; set[value] S3 = {{S1},3}; return S3 == {{{1,2}},3};}
test bool testSetSplicing34() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; set[set[set[int]]] S3 = {{S1},{S2}};return  S3 == {{{1,2}},{{3,4}}};}
test bool testSetSplicing35() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; set[value] S3 = {S1,{S2}}; return S3 == {{1,2},{{3,4}}};}
test bool testSetSplicing36() {set[int] S1 = {1,2}; set[int] S2 = {3,4}; set[value] S3 = {S1,{S2},5}; return S3 == {{1,2},{{3,4}},5};}
    
// testListInSetSplicing
    
test bool testListInSplicing1() {list[int] L1 = [1,2]; set[int] S2 = {3,4}; return {L1,S2,5} == {[1,2],{3,4},5};}
test bool testListInSplicing2() {list[int] L1 = [1,2]; set[int] S2 = {3,4}; return {*L1,S2,5} == {1,2,{3,4},5};}
test bool testListInSplicing3() {list[int] L1 = [1,2]; set[int] S2 = {3,4}; return {L1,*S2,5} == {[1,2],3,4,5};}
test bool testListInSplicing4() {list[int] L1 = [1,2]; set[int] S2 = {3,4}; return {*L1,*S2,5} == {1,2,3,4,5};}
    	
// 	testMap
    		
test bool testMap1() = () == ();
test bool testMap2() = (1:10) != ();
test bool testMap3() = (1:10) == (1:10);
test bool testMap4() = (1:10) != (2:20);
    		
test bool testMap5() = () + () == ();
test bool testMap6() = (1:10) + () == (1:10);
test bool testMap7() = (1:10) + (2:20) == (1:10, 2:20);
test bool testMap8() = (1:10, 2:20) + (2:25) == (1:10, 2:25);
    		
test bool testMap9() = () - () == ();
test bool testMap10() = (1:10, 2:20) - () == (1:10,2:20);
test bool testMap11() = (1:10, 2:20) - (2:20) == (1:10);
test bool testMap12() = (1:10, 2:20) - (2:25) == (1:10); // This is current behaviour; is this ok?
    	
test bool testMap13() = () & () == ();
test bool testMap14() = (1:10) & () == ();
test bool testMap15() = (1:10, 2:20, 3:30, 4:40) & (2:20, 4:40, 5:50) == (2:20, 4:40);
test bool testMap16() = (1:10, 2:20, 3:30, 4:40) & (5:50, 6:60) == ();
    		
test bool testMap17() = () <= ();
test bool testMap18() = () <= (1:10);
test bool testMap19() = (1:10) <= (1:10);
test bool testMap20() = (1:10) <= (1:10, 2:20);
    		
test bool testMap21() = !(() < ());
test bool testMap22() = () < (1:10);
test bool testMap23() = !((1:10) < (1:10));
test bool testMap24() = (1:10) < (1:10, 2:20);
    		
test bool testMap25() = () >= ();
test bool testMap26() = (1:10) >= ();
test bool testMap27() = (1:10) >= (1:10);
test bool testMap28() = (1:10, 2:20) >= (1:10);
    		
test bool testMap29() = !(() > ());
test bool testMap30() = (1:10) > ();
test bool testMap31() = !((1:10) > (1:10));
test bool testMap32() = (1:10, 2:20) > (1:10);
    		 		
test bool testMap33() = 1 in (1:10, 2:20);
test bool testMap34() = !(3 in (1:10, 2:20));
    		
test bool testMap35() = 3 notin (1:10, 2:20);
test bool testMap36() = !(2 notin (1:10, 2:20));
    		
test bool testMap37() {map[str,list[int]] m = ("a": [1,2], "b": [], "c": [4,5,6]); return m["a"] == [1,2];}
   
//@expected{NoSuchKey}
//test bool NoKeyError1(){		 (1:10, 2:20)[3]; return true;}
    	 
//@expected{MultipleKey}
//test bool MultipleKeyError1(){ (1:10, 1:10); return true;	 }
    	
// testTuple
    		
test bool testTuple1() = <1, 2.5, true> == <1, 2.5, true>;
test bool testTuple2() = <1, 2.5, true> != <0, 2.5, true>;
test bool testTuple3() = <{1,2}, 3> == <{2,1}, 3>;
test bool testTuple4() = <1, {2,3}> == <1, {3,2}>;
test bool testTuple5() = <{1,2}, {3,4}> == <{2,1},{4,3}>;
    		
test bool testTuple6() = <1>            >= <1>;
test bool testTuple7() = <2>            >= <1>;
test bool testTuple8() = <1,2>          >= <1>;
test bool testTuple9() = <1,2>          >= <1,2>;
test bool testTuple10() = <1,2>         >= <1, 1>;
test bool testTuple11() = <1,"def">     >= <1, "abc">;
test bool testTuple12() = <1, [2,3,4]>  >= <1, [2,3]>;
test bool testTuple13() = <1, [2,3]>    >= <1, [2,3]>;
    		
test bool testTuple14() = !(<1>         > <1>);
test bool testTuple15() = <2>           > <1>;
test bool testTuple16() = <1,2>         > <1>;
test bool testTuple17() = !(<1,2>       > <1,2>);
test bool testTuple18() = <1,2>         > <1, 1>;
test bool testTuple19() = <1,"def">     > <1, "abc">;
test bool testTuple20() = <1, [2,3,4]>  > <1, [2,3]>;
test bool testTuple21() = !(<1, [2,3]>  > <1, [2,3]>);
    		
test bool testTuple22() = <1>           <= <1>;
test bool testTuple23() = <1>           <= <2>;
test bool testTuple24() = <1>           <= <1,2>;
test bool testTuple25() = <1,2>         <= <1,2>;
test bool testTuple26() = <1,1>         <= <1, 2>;
test bool testTuple27() = <1,"abc">     <= <1, "def">;
test bool testTuple28() = <1, [2,3]>    <= <1, [2,3,4]>;
test bool testTuple29() = <1, [2,3]>    <= <1, [2,3]>;
    		
test bool testTuple30() = !(<1>         < <1>);
test bool testTuple31() = <1>           < <2>;
test bool testTuple32() = <1>           < <1,2>;
test bool testTuple33() = !(<1,2>       < <1,2>);
test bool testTuple34() = <1,1>         < <1, 2>;
test bool testTuple35() = <1,"abc">     < <1, "def">;
test bool testTuple36() = <1, [2,3]>    < <1, [2,3,4]>;
test bool testTuple37() = !(<1, [2,3]>  < <1, [2,3]>);
    		
test bool testTuple38() = <1, "a", true> + <1.5, "def"> == <1, "a", true> + <1.5, "def">;
    	
// namedTuple
    		
test bool testNamedTuple1() {tuple[int key, str val] T = <1, "abc">; return T.key == 1;}
test bool testNamedTuple2() {tuple[int key, str val] T = <1, "abc">; return T.val == "abc";}	
    	
// 	testRelation
   
test bool testRelation1() = {} == {};
test bool testRelation2() = {<1,10>} == {<1,10>};
test bool testRelation3() = {<1,2,3>} == {<1,2,3>};
test bool testRelation4() = {<1,10>, <2,20>} == {<1,10>, <2,20>};
test bool testRelation5() = {<1,10>, <2,20>, <3,30>} == {<1,10>, <2,20>, <3,30>};
test bool testRelation6() = {<1,2,3>, <4,5,6>} == {<4,5,6>, <1,2,3>};
test bool testRelation7() = {<1,2,3,4>, <4,5,6,7>} == {<4,5,6,7>, <1,2,3,4>};
    		
test bool testRelation8() = {} != {<1,2>, <3,4>};
test bool testRelation9() = !({<1,2>, <3,4>} == {});
    		
test bool testRelation10() = {<1, {1,2,3}>, <2, {2,3,4}>} ==  {<1, {1,2,3}>, <2, {2,3,4}>};
test bool testRelation11() = {<1, {1,2,3}>, <2, {2,3,4}>} ==  {<2, {2,3,4}>, <1, {1,2,3}>};
test bool testRelation12() = {<1, {1,2,3}>, <2, {2,3,4}>} ==  {<2, {4,3,2}>, <1, {2,1,3}>};
    		
test bool testRelation13() = {<1,10>} + {} == {<1,10>};
test bool testRelation14() = {} + {<1,10>}  == {<1,10>};
test bool testRelation15() = {<1,10>} + {<2,20>} == {<1,10>, <2,20>};
test bool testRelation16() = {<1,10>, <2,20>} + {<3,30>} == {<1,10>, <2,20>, <3,30>};
test bool testRelation17() = {<1,10>, <2,20>} + {<2,20>, <3,30>} == {<1,10>, <2,20>, <3,30>};
test bool testRelation17a() = {<1,10>, <2,20>} + <3,30> == {<1,10>, <2,20>, <3,30>};
test bool testRelation17b() = <1,10> + {<2,20>, <3,30>} == {<1,10>, <2,20>, <3,30>};
 		
test bool testRelation18() = {<1,10>} - {} == {<1,10>};
test bool testRelation19() = {} - {<1,10>}  == {};
test bool testRelation20() = {<1,10>, <2,20>} - {<2,20>, <3,30>} == {<1,10>};
    		
test bool testRelation21() = {<1,10>} & {} == {};
test bool testRelation22() = {} & {<1,10>}  == {};
test bool testRelation23() = {<1,10>, <2,20>} & {<2,20>, <3,30>} == {<2,20>};
test bool testRelation24() = {<1,2,3,4>, <2,3,4,5>} & {<2,3,4,5>,<3,4,5,6>} == {<2,3,4,5>};
    		
test bool testRelation25() = <2,20> in {<1,10>, <2,20>, <3,30>};
test bool testRelation26() = <1,2,3> in {<1,2,3>, <4,5,6>};
    		
test bool testRelation27() = <4,40> notin {<1,10>, <2,20>, <3,30>};
test bool testRelation28() = <1,2,4> notin {<1,2,3>, <4,5,6>};
    		
test bool testRelation29() = {} o {} == {};
test bool testRelation30() = {<1,10>,<2,20>} o {} == {};
test bool testRelation31() = {} o {<10,100>, <20,200>} == {};
test bool testRelation32() = {<1,10>,<2,20>} o {<10,100>, <20,200>} == {<1,100>, <2,200>};
    		
test bool testRelation33() = {<1, "a">, <2, "b">} * {<false, 0>, <true, 1>} == {<<1,"a">,<false,0>>,<<2,"b">,<false,0>>,<<1,"a">,<true,1>>,<<2,"b">,<true,1>>};
    
test bool testRelation34() = {<1,2>} join {<2,3>} == {<1,2,2,3>};
test bool testRelation35() = {<1,2>} join {} == {};
test bool testRelation36() = {} join {<2,3>} == {};
test bool testRelation37() = {} join {} == {};
test bool testRelation38() = {<1,2>} join {3} == {<1,2,3>};
test bool testRelation39() = {<1,2>} join {3, 4} == {<1,2,3>, <1,2,4>};
test bool testRelation40() = {1} join {2} == {<1,2>};
test bool testRelation41() = {1,2,3} join {2} == {<1,2>, <2,2>, <3,2>};
    		
test bool testRelation42() = {} + == {};
test bool testRelation43() = {} * == {};
    		
test bool testRelation44() = {<1,2>, <2,3>, <3,4>} + == {<1,2>, <2,3>, <3,4>, <1, 3>, <2, 4>, <1, 4>};
    		
test bool testRelation45() = {<1,2>, <2,3>, <3,4>} * == {<1,2>, <2,3>, <3,4>, <1, 3>, <2, 4>, <1, 4>, <1, 1>, <2, 2>, <3, 3>, <4, 4>};
    		
test bool testRelation46() = {<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}+ ==	{<1,2>, <2,3>, <3,4>, <4,2>, <4,5>, <1, 3>, <2, 4>, <3, 2>, <3, 5>, <4, 3>, <1, 4>, <2, 2>, <2, 5>, <3, 3>, <4, 4>, <1, 5>};
    		
test bool testRelation47() = {<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}* == {<1,2>, <2,3>, <3,4>, <4,2>, <4,5>, <1, 3>, <2, 4>, <3, 2>, <3, 5>, <4, 3>, <1, 4>, <2, 2>, <2, 5>, <3, 3>, <4, 4>, <1, 5>, <1, 1>, <5, 5>};
 
test bool testRelation48() = {<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}[7] == {};
test bool testRelation49() = {<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}[2] == {3};
test bool testRelation50() = {<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}[4] == {2, 5};
test bool testRelation51() = {<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}[_] == {2,3,4,5};

test bool testRelation52() = {<1,2,3>, <3,4,5>, <5,6,7>, <5,4,3>}[_] == {<4,3>,<6,7>,<4,5>,<2,3>};
test bool testRelation53() = {<1,2,3>, <3,4,5>, <5,6,7>, <5,4,3>}[_,_] == {3,5,7};
test bool testRelation54() = {<1,2,3>, <3,4,5>, <5,6,7>, <5,4,3>}[_,4] == {3,5};

test bool testRelation55() = {<1,2>, <2,3>, <3,4>, <4,2>, <4,5>}[{2,3}] == {3,4};
test bool testRelation56() = {<1,2,3>, <3,4,5>, <5,6,7>, <5,4,3>}[_,{2,5}] == {3};
test bool testRelation57() = {<1,2,3>, <3,4,5>, <5,6,7>, <5,4,3>}[{3,5},{1,4}] == {5,3};

test bool testRelation58() = {<{1,2,3}, 4>, <{3,4,5}, 6>, <{5,6,7}, 8>, <{1,2,3}, 2>, <{1,2,3}, 3>}[{1,2,3}] == {2,3,4};
test bool testRelation59() = {<{1,2,3}, 4, 4>, <{3,4,5}, 6, 7>, <{5,6,7}, 8, 9>, <{1,2,3}, 2, 4>, <{1,2,3}, 3, 0>}[{1,2,3}] == {<2,4>,<3,0>,<4,4>};
test bool testRelation60() = {<{1,2,3}, 4, 4>, <{3,4,5}, 6, 7>, <{5,6,7}, 8, 9>, <{1,2,3}, 2, 4>, <{1,2,3}, 3, 0>}[{1,2,3},{4,6}] == {4};

// namedRelation
    
test bool namedRelation1() { rel[int from, int to] R = {<1,10>, <2,20>}; return R.from == {1,2}; }
test bool namedRelation2() { rel[int from, int to] R = {<1,10>, <2,20>}; return R.to == {10,20}; }
    
    /* TODO: Issue :f constructor overlaps with NODE */
    /*
    data NODE1 = val(value V) | f() | f1(NODE1 a);
    	test bool good1()  {
    		return f1(val(1)) == f1(val(1));
    	}
    */
    
data NODE = i(int I) | s(str x)  | st(set[NODE] s) | l(list[NODE]) | m(map[NODE,NODE] m) | f() | f(NODE a) | f(NODE a, NODE b) | g() | g(NODE a) | g(NODE a,NODE b);
    	
// 	node
    
test bool node1() = f() == f();
test bool node2() = f() != g();
test bool node3() { NODE n = f(); NODE m = g(); return n != m;}
test bool node4() = f(i(1)) == f(i(1));
test bool node5() = f(i(1)) != g(i(1));
test bool node6() { NODE n = f(i(1)); NODE m = g(i(1)); return n != m;}
test bool node7() = f(i(1),i(2)) == f(i(1),i(2));
test bool node8() = f(i(1),i(2)) != f(i(1),i(3));
test bool node9() { NODE n = f(i(1),i(2)); NODE m = f(i(1),i(3)); return n != m;}
test bool node10() = f(i(1),g(i(2),i(3))) == f(i(1),g(i(2),i(3)));
test bool node11() = f(i(1),g(i(2),i(3))) != f(i(1),g(i(2),i(4)));
test bool node12() { NODE n = f(i(1),g(i(2),i(3))); NODE m = f(i(1),g(i(2),i(4))); return n != m;}
test bool node13() = f(i(1),g(i(2),st({i(3),i(4),i(5)}))) == f(i(1),g(i(2),st({i(3),i(4),i(5)})));
test bool node14() { NODE n = f(i(1),g(i(2),st({i(3),i(4),i(5)}))); NODE m = f(i(1),g(i(2),st({i(3),i(4),i(5),i(6)}))); return n != m;}
test bool node15() = f(i(1),g(i(2),l([i(3),i(4),i(5)]))) == f(i(1),g(i(2),l([i(3),i(4),i(5)])));
test bool node16() { NODE n = f(i(1),g(i(2),l([i(3),i(4),i(5)]))); NODE m = f(i(1),g(i(2),l([i(3),i(4),i(5),i(6)]))); return  n != m;}
test bool node17() = f(i(1),g(i(2),m((i(3):i(3),i(4):i(4),i(5):i(5))))) == f(i(1),g(i(2),m((i(3):i(3),i(4):i(4),i(5):i(5)))));
test bool node18() { NODE n = f(i(1),g(i(2),m((i(3):i(3),i(4):i(4),i(5):i(5))))); NODE x = f(i(1),g(i(2),m((i(3):i(3),i(4):i(4),i(5):i(0))))); return n != x;}
    
test bool node19() = f()                        <= f();
test bool node20() = f()                        <= g();
test bool node21() = f()                        <= f(i(1));
test bool node22() = f(i(1))                    <= f(i(1));
test bool node23() = f(i(1), i(2))              <= f(i(1), i(3));
test bool node24() = f(i(1), i(2))              <= g(i(1), i(3));
test bool node25() = f(i(1), s("abc"))          <= f(i(1), s("def"));
test bool node26() = f(i(1), l([i(2), i(3)]))   <= f(i(1), l([i(2),i(3),i(4)]));
test bool node27() = f(i(1), l([i(2), i(3)]))   <= f(i(1), l([i(2),i(3)]));
    		
test bool node28() = !(f()                      < f());
test bool node29() = f()                        < g();
test bool node30() = f()                        < f(i(1));
test bool node31() = !(f(i(1))                  < f(i(1)));
test bool node32() = f(i(1), i(2))              < f(i(1), i(3));
test bool node33() = f(i(1), i(2))              < g(i(1), i(3));
test bool node34() = f(i(1), s("abc"))          < f(i(1), s("def"));
test bool node35() = f(i(1), l([i(2), i(3)]))   < f(i(1), l([i(2),i(3),i(4)]));
test bool node36() = !(f(i(1), l([i(2), i(3)])) < f(i(1), l([i(2),i(3)])));
    		
test bool node37() = f()                           >= f();
test bool node38() = g()                           >= f();
test bool node39() = f(i(1))                       >= f();
test bool node40() = f(i(1))                       >= f(i(1));
test bool node41() = f(i(1), i(3))                 >= f(i(1), i(2));
test bool node42() = g(i(1), i(2))                 >= f(i(1), i(3));
test bool node43() = f(i(1), s("def"))             >= f(i(1), s("abc"));
test bool node44() = f(i(1), l([i(2),i(3),i(4)]))  >= f(i(1), l([i(2),i(3)]));
test bool node45() = f(i(1), l([i(2), i(3)]))      >= f(i(1), l([i(2),i(3)]));
    		
test bool node46() = !(f()                         > f());
test bool node47() = g()                           > f();
test bool node48() = f(i(1))                       > f();
test bool node49() = !(f(i(1))                     > f(i(1)));
test bool node50() = f(i(1), i(3))                 > f(i(1), i(2));
test bool node51() = g(i(1), i(2))                 > f(i(1), i(3));
test bool node52() = f(i(1), s("def"))             > f(i(1), s("abc"));
test bool node53() =  f(i(1), l([i(2),i(3),i(4)])) > f(i(1), l([i(2),i(3)]));
test bool node54() = !(f(i(1), l([i(2), i(3)]))    > f(i(1), l([i(2),i(3)])));
    
data D = d(int ival);
    	
// undefined
    	
test bool undefined2() {T = (1:10); return (T[1] ? 13) == 10;}
test bool undefined3() {T = (1:10); return (T[2] ? 13) == 13;}
    		
test bool undefined4() {T = (1:10); return T[1] ? == true;}
test bool undefined5() {T = (1:10); return T[2] ? == false;}

    
   
 
