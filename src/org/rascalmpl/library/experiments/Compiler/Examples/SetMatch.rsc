module experiments::Compiler::Examples::SetMatch

value main(list[value] args) { 
	res = {};
 	for({*int a, *int b, *int c} := {1,2,3,4,5,6,7,8,9}) { res = res + {[a,b,c]}; }
    return res;   
}