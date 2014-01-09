module experiments::Compiler::Examples::SetMatchMix

value main(list[value] args) { 
	res = {};
 	for({6, *int a, int x, *int b, int y, 2, *int c} := {1,2,3,4,5,6,7,8,9}) { res = res + {{a,b,c}}; }
    return res;   
}