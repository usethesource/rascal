module lang::rascalcore::check::Test1 
                                  
data RuntimeException = 
      ArithmeticException(str message); 
    
@expected{ArithmeticException}
bool div() = 2/0 == 0; 