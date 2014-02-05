module experiments::Compiler::Examples::Visit12

data NODE1 = f(value V) 
           | f(value V1, value V2) 
           | f(value V1, value V2, value V3) 
           | g(value V1, value V2) 
           | h(value V1, value V2)
           | h(value V1, value V2, value V3);

NODE1 walk(NODE1 t) {
    return visit(t) {
               case int N => x when int x := N*2 && x >= 1
           };
}

value main(list[value] args) {
    return walk(f(1,2,3)) == f(2,4,6) 
            && walk(f(1,g(2,3))) == f(2, g(4, 6))
            ;
}
