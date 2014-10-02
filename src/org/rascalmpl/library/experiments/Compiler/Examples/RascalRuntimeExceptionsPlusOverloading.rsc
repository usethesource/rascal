module experiments::Compiler::Examples::RascalRuntimeExceptionsPlusOverloading

import Exception;

str trace = "";

void f(int i) {
    map[int,str] m = (0:"0", 1:"1",2:"2");
    trace = trace + "Bad function f; ";
    m[3];
}

void g(0) {
    try {
        return f(0);
    } catch NoSuchKey(k): {
		trace = trace + "map key: <k> (not found); ";
	} finally {
		trace = trace + "finally; ";
	}
	fail;
}

default void g(int i) {
    trace = trace + "default void g(int);";
}

value main(list[value] args) {
    trace = "";
    g(0);
    return trace;
}