module experiments::Compiler::Examples::Tst3

data NODE1 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2)|h(value V1, value V2, value V3);

NODE1 walk(NODE1 t) {
	return 
		visit(t) {
			case int N=>x when x:=N*2, x>=1
		};
}

//test bool When1()= walk(f(3)) == f(6);
//test bool When2()= walk(f(1,2,3)) == f(2,4,6);
//test bool When3()= walk(f(1,g(2,3))) == f(2, g(4, 6));
//test bool When4()= walk(f(1,g(2,[3,4,5]))) == f(2, g(4, [6, 8, 10]));

value main(list[value] args) =  walk(f(1,2));// == f(2,4);