module experiments::Compiler::Examples::RascalRuntimeExceptions

value main(list[value] args) {
	map[int,str] m = (0:"0", 1:"1",2:"2");
	
	str trace = "";
	
	try {
		m[3];
	} catch NoSuchKey(k): {
		trace = trace + "<k> (not found)";
	} finally {
		trace = "map key: " + trace;
	}
	return trace;
}