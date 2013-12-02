module experiments::Compiler::GLL::GR7

// Gamma2 grammar

syntax S = S S S
         | S S
         | "b"
         ;
         
str input1 = "b";
str input2 = "bbb";
str input3 = "bbbbb";
str input4 = "bbbbbbbb";
