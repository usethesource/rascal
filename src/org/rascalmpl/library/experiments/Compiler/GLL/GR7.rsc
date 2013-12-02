module experiments::Compiler::GLL::GR7

// Gamma2 grammar

syntax S = S S S
         | S S
         | "b"
         ;
