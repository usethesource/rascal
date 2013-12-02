module experiments::Compiler::GLL::GR6

syntax E = E "*" E
         | E "+" E
         | ()
         | "a"
         ;