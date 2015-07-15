
module experiments::Compiler::Examples::Tst6
import ParseTree;
import IO;

syntax A = "a";
syntax As0 = A* as0;
syntax As1 = A+ as1;

	
value main(list[value] args){

	switch([As1] "aaa"){
		case (As1) `<A+ as>`: 		
				return "<as>" == "aaa"; 
		default: 		 
				return false;
	}
	throw "fail due to missing match";
}
