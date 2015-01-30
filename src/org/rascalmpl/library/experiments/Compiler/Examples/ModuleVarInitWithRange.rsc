module experiments::Compiler::Examples::ModuleVarInitWithRange

//TODO: temporary generation for the range does not work as it should

list[int] ints = [0 .. 10];

value main(list[value] args) = ints;