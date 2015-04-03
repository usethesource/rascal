module experiments::Compiler::Examples::Tst3

// Illustrates that sp calculation is not precise!
import Exception;

value main(list[value] args) = tst();

test bool tst(){
	try { 
		[0,1,2][3]; 
	} catch IndexOutOfBounds(int i): 
		return true; 
	return false; 
}