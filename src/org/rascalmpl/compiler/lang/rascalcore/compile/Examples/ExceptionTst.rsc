module lang::rascalcore::compile::Examples::ExceptionTst
          
extend lang::rascalcore::compile::Examples::FailMessageTst;
 
data RuntimeException
    = TypePalUsage(str reason)                      // TypePal used incorrectly
    ;