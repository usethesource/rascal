module experiments::Compiler::Examples::Tst3


data NODE1 = f(value V1, value V2) 
           | f(value V1, value V2, value V3) 
           ;

NODE1 walk(NODE1 t) = t;

value main(list[value] args) {
	
    return walk(f(1,2,3)) == f(2,4,6) 
           || 
           walk(f(1,f(2,3))) == f(2, f(4, 6))
            ;
}
