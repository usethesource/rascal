module experiments::Compiler::Examples::Concrete1

import experiments::Compiler::Examples::ParseTreeDataType;

syntax A = "a";

value main(list[value] args) {
return (A) `a`;
}