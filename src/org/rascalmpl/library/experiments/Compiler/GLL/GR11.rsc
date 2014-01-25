module experiments::Compiler::GLL::GR11

syntax E = A "+" D
         | A "-" D
         | "a"
         ;
 
syntax A = B "c";
  
syntax B = E
  	     | "b"
  	     ;
  
syntax D = "k" F;
      
syntax F = E
         | "g"
         ;
         
str input = "ac+kac-kac+ka";