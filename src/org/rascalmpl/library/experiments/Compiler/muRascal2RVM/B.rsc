module experiments::Compiler::muRascal2RVM::B

extend experiments::Compiler::muRascal2RVM::A;

syntax A = "c" > "d";
//public data A = cc() | dd();

type[value] getA() = #A;