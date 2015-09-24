module experiments::Compiler::Examples::Tst3

value main( int counter = 0){
   
    void inc1(int counter = 10){
        counter += 1;
    }
    void inc2(){
        counter += 1;
    }
   inc1();
   inc2();
   return counter;
}