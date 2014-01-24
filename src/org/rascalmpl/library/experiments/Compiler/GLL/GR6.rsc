module experiments::Compiler::GLL::GR6

syntax E = E "*" E
         | E "+" E
         | ()
         | "a"
         ;
         
str input1 = "a+a+a";
str input2 = "a+a+a";
str input3 = "a*a*a";
str input4 = "a+a*a+a*a";
str input5 = "a+a*a++++a++a";

str input6 = "a+*+a*a";
str input7 = "a*++a+*a";