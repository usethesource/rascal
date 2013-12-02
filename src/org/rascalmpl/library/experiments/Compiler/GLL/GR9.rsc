module experiments::Compiler::GLL::GR9

syntax A = B A "a"
         | D A "b"
         | "c"
         ;

syntax B = "x" | ();

syntax D = "y" | ();

str input1 = "xca";
str input2 = "xcb";
str input3 = "cababaab";
str input4 = "xcabbbbb";
str input5 = "ycaaaabaaaa";