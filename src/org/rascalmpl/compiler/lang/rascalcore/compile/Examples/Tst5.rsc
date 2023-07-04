module lang::rascalcore::compile::Examples::Tst5


value main() = 42;

data X = xx() | yy() | zz();    
  
// dispatchTest1
  
test bool  dispatchTest1() { 
    int f1(xx()) = 1;
    int f1(yy()) = 2;
    int f1(zz()) = 3;
    return [f1(xx()),f1(yy()),f1(zz())] == [1,2,3];
}