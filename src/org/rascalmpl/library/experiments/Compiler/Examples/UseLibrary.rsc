module experiments::Compiler::Examples::UseLibrary

import experiments::Compiler::Examples::MyLibrary;

value main(list[value] args) { 
	  return replaceAll("abracadabra", "a", "A");
}  