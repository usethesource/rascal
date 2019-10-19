module lang::rascalcore::compile::Examples::Tst1
  data Prop = or(Prop, Prop) | f(int n);   
bool main() {
  N = 1; {int N = 2; N == 2;}; return N == 1;
}

//value main() = { (li == 10) ? 10 : li | li <- [1,2,3] };    
//           
//value main() { 
//    return {<N,M,X> | int N <- [1 .. 3], int M <- [10 .. 12]};     
//}
//bool isEmpty(map[&K,&V] M) = true;
//
////public rel[&K,&V] toRel(map[&K,set[&V]] M) = isEmpty(M) ? {} : {<k,v> | &K k <- M, &V v <- M[k]};
//public rel[&K,&V] toRel(map[&K,list[&V]] M) = {<k,v> | &K k <- M, &V v <- M[k]}; 
//                     
//// ComprehensionTCTests
//public map[str, int] mapper(map[str, int] M)
//{
//  return (key : M[key] | str key <- M);
//}    
//                
//public int mix(){
//    sizeL = 10;
//    sizeR = 20;
//    minSize = sizeL < sizeR ? sizeL : sizeR;
//    return minSize;        
//} 
//     
//value main() = [ (li == 10) ? 10 : li | li <- [1,2,3] ];   
//       
//value main()
//   =      [ M | 
//            int N := 1, 
//            M > 0, 
//            int M := 13
//          ];    
//                               
//                       
// value main() 
//    = [<N,M> | int N <- [1 .. 3], (N==1) ? true : M > 0, int M <- [10 .. 12]]; 
//    
//                 
//value main() 
//    = [ X | 
//        int X <- {}
//      ];
//
//public tuple[list[&T],list[&U]] unzip(list[tuple[&T,&U]] lst) =
//    <[t | <t,_> <- lst], [u | <_,u> <- lst]>;
//        