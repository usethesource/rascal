module experiments::Compiler::Examples::IsDefined

import Exception;

value main(list[value] args) {
	str trace = "";
	map[int,str] m = (0 : "0", 1 : "1", 2 : "2");
	try {
		m[3];
	} catch NoSuchKey(k): {
		trace += "Caught no such key: <k>;";
	}
	
	if(m[2]?) {
		trace += " <m[2]>;";
	} else {
		trace += " not found the key <2>;";
	}
	
	if(m[3]?) {
		trace += " <m[3]>;";
	} else {
		trace += " did not find the key <3>;";
	}
	
	return trace;
}