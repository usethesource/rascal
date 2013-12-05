module experiments::Compiler::Examples::Tst
	
	//public str main(list[value] args) {str a = "a\\bc"; return "1<a>2";}
    
  //  public test bool testStringInterpolation4() {str a = "a\\tc"; return "1<a>2" == "1a\\tc2";}
 	//public test bool testStringInterpolation5() {str a = "a\\nc"; return "1<a>2" == "1a\\nc2";}
 	//public test bool testStringInterpolation6() {str a = "a\\fc"; return "1<a>2" == "1a\\fc2";}
 	//public test bool testStringInterpolation7() {str a = "a\\rc"; return "1<a>2" == "1a\\rc2";}
  //  		
 	//public test bool testStringInterpolation8() {str a = "a\\\"c"; return "1<a>2" == "1a\\\"c2";}
 	//public test bool testStringInterpolation9() {str a = "a\\\'c"; return "1<a>2" == "1a\\\'c2";}
 	//public test bool testStringInterpolation10() {str a = "a\\\\c"; return "1<a>2" == "1a\\\\c2";}
 	
 	
// 	  data D = d(int i) | d();
// 	  
//      D d(int i) { if (i % 2 == 0) fail d; else return d();}
//
//  	  public bool main(list[value] args) = d(2) := d(2);
      //public test bool fail2() = d(3) == d();
      
 //     public bool main(list[value] args)  {
	//	return {for (x <- [1,2,3]) { f = () { append x; }; f();}} 
	//	    == [1,2,3];
	//}

import Boolean;
public bool main(list[value] args)  = fromString("true"); // == true && fromString("false") == false;