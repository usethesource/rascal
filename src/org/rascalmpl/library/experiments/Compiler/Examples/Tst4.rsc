module experiments::Compiler::Examples::Tst4

value main(list[value] args) {
   
    IN = {};
     
    solve (IN) {
            IN =  {S | int S <- {1,2,3,4,5,6,7}};
    };
    return IN;
}
