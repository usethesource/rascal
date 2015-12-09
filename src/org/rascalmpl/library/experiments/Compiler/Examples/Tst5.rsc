module experiments::Compiler::Examples::Tst5


value main(){
    m1 = ("a" : "A", "b" : "B", "c" : "C", "d": "D");
    m2 = ("A" : 10, "B" : 20, "C" : 30, "D" : 40);
    
    return [ a | str a <- m1, A := m1[a], A in m2, m2[A] >= 20 ];

}