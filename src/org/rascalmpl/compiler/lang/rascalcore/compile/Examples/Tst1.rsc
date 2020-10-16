module lang::rascalcore::compile::Examples::Tst1
 
str h(0) = "0"; 
str h(1) = "1"; 
default str h(int n) { fail; } 

str i(0) = "1"; 
str i(1) = "2"; 
default str i(int n) = "<n + 1>"; 

value main() = (h + i)(1);