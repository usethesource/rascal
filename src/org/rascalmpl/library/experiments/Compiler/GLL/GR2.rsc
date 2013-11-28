module experiments::Compiler::GLL::GR2

syntax S = A "a" "d"
	     | B "a" "b"
	     ;
	     
syntax A = E;

syntax E = B;

syntax B = C;

syntax C = "a";

str input = "aad";
