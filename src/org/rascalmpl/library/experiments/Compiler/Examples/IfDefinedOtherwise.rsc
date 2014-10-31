module experiments::Compiler::Examples::IfDefinedOtherwise

import Exception;

public str expectedResult = "2 Did not find the key 3; 1; -99";

value main(list[value] args) {

	str trace = "";
	
	map[int,str] m = (0 : "0", 1 : "1", 2 : "2");
	
	trace += m[2] ? " Not found the key <2>;";
	trace += m[3] ? " Did not find the key <3>;";
	
	int x;
	
	x = x ? 1;
	x = x ? 2;
	
	trace += " <x>;";
	
	try {
		x = [0,1,2,3][5] ? 3;
	} catch IndexOutOfBounds(index) : {
		x = x - 100;
	}
	
	trace += " <x>";
	
	return trace;
}