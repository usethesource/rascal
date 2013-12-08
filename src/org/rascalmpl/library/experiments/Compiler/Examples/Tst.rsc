module experiments::Compiler::Examples::Tst
 	
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


data T1 = \int() | \void() | string(str s);
data T2 = \int() | \void() | string(str s);

bool fT1(T1::\int()) = true;
	
bool fT2(T2::\int()) = true;
	