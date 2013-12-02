module experiments::Compiler::GLL::GR8

// Indirect left recursion

syntax A = B "c" 
         | C "d"
         | "e"
         ;

syntax B = A "f";

syntax C = A "g";

str input1 = "efcfc";   // Only using the first alternative
str input2 = "egdgdgd"; // Only using the second alternative
str input3 = "egdfcgd"; // Using both alternatives