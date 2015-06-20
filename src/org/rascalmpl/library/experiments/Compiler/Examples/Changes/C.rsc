module experiments::Compiler::Examples::Changes::C

 
 
import experiments::Compiler::Examples::Changes::B1;
import experiments::Compiler::Examples::Changes::B2;

value main(list[value] args) = experiments::Compiler::Examples::Changes::B1::Bvar + b1fun() + b2fun() + "C";