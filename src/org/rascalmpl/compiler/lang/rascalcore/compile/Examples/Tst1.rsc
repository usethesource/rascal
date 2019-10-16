module lang::rascalcore::compile::Examples::Tst1

data Prop = f(); 
data Bool = and(list[Prop], list[Prop]) | t();

bool main(){ 
    return f(1) := 1;
} 
                 
// ComprehensionTCTests
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
        