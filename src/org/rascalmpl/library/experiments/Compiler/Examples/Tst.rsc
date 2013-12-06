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

// public bool main(list[value] args) = all(int X <- {1,2,3}, X >= 2);

//import IO;
//public value main(list[value] args) {
//    res = true;
//     O: if(int X <- {1,2,3}){ 
//           println("X = <X>");
//           if(X >= 2){
//              fail O; 
//           } else {
//             println("else: <X>");
//             res = false;
//             fail O;
//           }
//        };
//        return res;
//}
data TREE = i(int N) | f(TREE a,TREE b) | g(TREE a, TREE b);
public value main(list[value] args)  = [ X | /value X <- f(i(1),g(i(2),i(3))) ]; // == [1,i(1),2,i(2),3,i(3),g(i(2),i(3))];