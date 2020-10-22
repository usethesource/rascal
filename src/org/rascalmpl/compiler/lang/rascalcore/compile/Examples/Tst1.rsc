module lang::rascalcore::compile::Examples::Tst1

//import List;
//import Node;
import Exception;

test bool higherOrderVoidFunctionCompatibility() {
   bool hof (void(int s) g) { g(0); return true; }
   void ff(int _) { return; }
   
   try {
     return hof(ff);
   }  
   catch CallFailed(_): 
     return false;
}