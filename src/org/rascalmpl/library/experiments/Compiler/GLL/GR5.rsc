module experiments::Compiler::GLL::GR5

syntax E = E "+" E
         | E "-" E
         | E "*" E
         | E "/" E
         | "-" E
         | E "+"
         | "(" E ")"
         | Num
         ;
         
syntax Num = "1" | "2" | "3";

str input = "1+2/3--4+++4+(3-4*4/5)";
