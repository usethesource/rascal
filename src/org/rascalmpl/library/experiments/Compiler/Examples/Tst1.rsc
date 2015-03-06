module experiments::Compiler::Examples::Tst1

//import lang::rascal::tests::types::StaticTestingUtils;
import IO;

value main(list[value] args) {
	try {
		throw "exception by main";
	} catch s: { return true;}
	
	return false;
}