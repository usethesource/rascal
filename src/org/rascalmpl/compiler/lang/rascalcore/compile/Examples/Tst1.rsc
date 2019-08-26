module lang::rascalcore::compile::Examples::Tst1
 
value main() {
    n = 0;
    if( ([*int x,*int y] := [1,2,3] && int z := 3) || ([*int x,*int y] := [4,5,6] && int z := 4) )  {
        n = n + 1;
        fail;
    }
    return n;// == 8;
}
//value main() { 
//    n = 0;
//    if( [*int x, *int y] := [1])  {
//        n = n + 1;
//        fail;
//    }
//    return n;//== 8;
//}    

// bool main() {
//   
//    b = 4 > 5 || int x <- [1..3];
//      
//    return b;
//}                     
  
 
// bool main() {
//   
//    b = int q := 4 || int p := 3;
//      
//    return b;
//}                     
 
// int main() {
//    n = 0;
//    if( int q := 4 || int p := 3 )  {
//        n = n + 1;
//        fail;
//    }
//    return n; // == 8;
//}         
     
//-----------------------------------------
//import IO;
//int main() { bool b = true; c = b && true; return c ? 1 : 2;; }

//int main() { bool b = [x*,y,*z] := [1,2,3] && true; return 13; }

//bool main() { bool b = [x*,y,*z] := [1,2,3] ; return b; }
  
//str main() {
//    n = 0;
//    
//   x = "<while(n < 0) { n += 1;>x< }>";
//   return x;
//} 
 
//public default (&T <:num) xxx([(&T <: num) hd, *(&T <: num) tl])
//    = (hd | it + i | i <- tl);          

//value main() = [ x | x <- [ n | n <- [1..4], n >= 2]];  

//value main() = [*int x] := [1,2,3] || 3 > 2;

//value main() = [*int x, y] := [1,2,3] || 5 > 7;
                       
                 
//int main() {
//    n = 0;
//    l = if( ([*int x,*int y] := [1,2,3] && int z := 3) || ([*int x,*int y] := [4,5,6] && int z := 4) )  {
//        n = n + 1;
//        fail;
//    }
//    
//    return n; 
//} 

//bool f(bool b) = b;
//
//bool main() {
//    return f([*int x, int  y, *int z] := [1,2,3] &&  4 > 5);
//}     
//   
 
// bool main() {
//    b = [int x] := [1];
//    return b;
//}  
//    
//bool main() {
//    b = [int x] := [1] &&  5 > 4; 
//    return b;
//}        
       
 
//bool main() {
//    b = [*int x, int  y, *int z] := [1,2,3] &&  [*int p, int q, *int r] := [4,5,6];
//    return b;
//}   

//int main() {
//    int n = 0;
//    if([*int x, int  y, *int z] := [1,2,3]){
//        n += 1;
//        fail;
//    }
//    return n;
//}    
         
//int main() {
//    int n = 0;
//    if(5 > 4 && [*int p, int q, *int r] := [4,5,6] && 7 > 6){
//        n += 1;
//        fail;
//    }
//    return n;
//}          
    
//int main() {
//    int n = 0;
//    if([*int x, int  y, *int z] := [1,2,3] && [*int p, int q, *int r] := [4,5,6]){
//        n += 1;
//        fail;
//    }
//    return n;
//}         
       
//int main() {
//    int n = 0;
//    if([*int x, int  y, *int z] := [1,2,3] && [*int p, int q, *int r] := [4,5,6] && q > 4){
//        n += 1;
//        fail;
//    }
//    return n;
//}        