module experiments::Compiler::Examples::Tmp

import experiments::Compiler::Examples::MyLibrary;

value main(list[value] args) { 
	  return return replaceAll("abracadabra", "a", "A");
}  