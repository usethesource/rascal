module lang::rascalcore::compile::Examples::Tst1
//import Exception;
value main(){ //test bool higherOrderFunctionCompatibility1() {
   // the parameter function is specific to int
   int parameter(int _) { return 0; }
   
   // the higher order function expects to call the
   // parameter function with other things too
   int hof(int (value) p, value i) { return p(i); }
   
   // but the next call produces a CallFailed, since
   // the parameter function is not defined on strings:
   //try {
     // statically allowed! but dynamically failing
     hof(parameter, "string");
     return false;
   //} 
   //catch CallFailed(_):
   //  return true; 
}