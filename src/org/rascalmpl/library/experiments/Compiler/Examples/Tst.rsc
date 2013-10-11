module experiments::Compiler::Examples::Tst

value main(list[value] args) { 
	res = {};
 	for({*int a, *int b} := {1, 2}) { res = res + {{a, b}}; }
    return res;   
}