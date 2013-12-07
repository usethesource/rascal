module experiments::Compiler::GLL::GR10

syntax A = B "a"
         | C "a"
         | "a"
         | "c"
         ;
         
syntax B = C "b"
         | "b"
         ;
         
syntax C = A "c"
         | A "b"
         | "c"
         ;

str input = "cba";