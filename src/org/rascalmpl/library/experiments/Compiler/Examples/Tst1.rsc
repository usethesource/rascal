module experiments::Compiler::Examples::Tst1

test bool exceptionHandlingFinally4(){
        str n = "0";
        // No exceptions and no returns
        try {
            try {
                n = " 2";
            } 
            catch: {
                n = " 3";
            } 
            finally {
                n = " 6";
            }
        } 
        catch "0": {  
            ;
        } 
       
        return n == "0 2 6";
}   
