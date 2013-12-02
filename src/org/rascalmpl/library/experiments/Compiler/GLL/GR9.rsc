module experiments::Compiler::GLL::GR9

syntax A = B A "a"
         | D A "b"
         | "c"
         ;

syntax B = "x" | ();

syntax D = "y" | ();