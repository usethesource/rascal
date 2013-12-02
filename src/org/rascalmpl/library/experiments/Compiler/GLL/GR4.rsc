module experiments::Compiler::GLL::GR4

syntax E = E "+" E
         | E "-" E
         | "a"
         ;

str input = "a+a-a+a";