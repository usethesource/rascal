module experiments::Compiler::GLL::GR8

// Indirect left recursion

syntax A = B "c" 
         | C "d"
         | "e"
         ;

syntax B = A "f";

syntax C = A "g";