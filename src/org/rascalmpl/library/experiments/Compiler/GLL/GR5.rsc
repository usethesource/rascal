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
         
syntax Num = "1" | "2" | "3" | "4" | "5" | "6";

str input1 = "1+2/3--4+++4+(3-4*4/5)";
str input2 = "1+2/3++4+(5/-6)";
