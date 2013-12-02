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
