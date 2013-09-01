module experiments::Compiler::Examples::ListMatch

value main(list[value] args) { 
	res = [];
 	for([*int a, *int b, *int c, *int d] := [1,2,3,4,5,6,7,8,9]) { res = res + [[a,b,c,d]]; }
    return res;   
}