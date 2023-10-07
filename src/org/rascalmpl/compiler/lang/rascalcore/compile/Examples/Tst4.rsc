@bootstrapParser
module lang::rascalcore::compile::Examples::Tst4

import Exception;

value main(){ //test bool higherOrderFunctionCompatibility1() {
   // the parameter function is specific to int
   int parameter(int _) { return 0; }
   
   // the higher order function expects to call the
   // parameter function with other things too
   int hof(int (value) p, value i) { return p(i); }
   
   // still this is ok, since functions in Rascal
   // are partial. This call should simply succeed:
   if (hof(parameter, 1) != 0) {
     return false;
   }
   
   // but the next call produces a CallFailed, since
   // the parameter function is not defined on strings:
   try {
     // statically allowed! but dynamically failing
     hof(parameter, "string");
     return false;
   } 
   catch CallFailed(_):
     return true; 
}