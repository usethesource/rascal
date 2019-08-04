module lang::rascalcore::compile::Examples::Tst1

//int main() { bool b = true; c = b && true; return c ? 1 : 2;; }

//int main() { bool b = [x*,y,*z] := [1,2,3] && true; return 13; }
 
int main() {
    n = 0;
    if([*x,y,*z] := [1,2,3])  {
        n = n + 1;
       fail;
    }
    return n;
}

//int main() {
//    n = 0;
//    if([*x,y,*z] := [1,2,3])  {
//        n = n + 1;
//       fail;
//    }
//    return n;
//}

  
    
   
  
   