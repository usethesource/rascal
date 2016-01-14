module experiments::Compiler::Examples::Tst5
      
 value main(){
 
       return [ y | <x, y> <- 
                   [<a, 10*a> | a <- [1,2,3]],  
                   y > 10 ];
 
 }