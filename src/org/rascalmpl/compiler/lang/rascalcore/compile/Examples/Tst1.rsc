module lang::rascalcore::compile::Examples::Tst1

test bool closures1() {
    int f(int (int i) g, int j) { return g(j);}
    if (f(int (int i) { return i + 1; }, 0) != 1) return false;
    return true;
}

test bool closures2() {
    int x = 1;
    int f(int (int i) g, int j) { return g(j);}
    if (f(int (int i) { x = x * 2; return i + x; }, 1) != 3 || (x != 2))
        return false;
    return true;
}

bool() x = bool() { return false; } ;

void changeX(bool() newX) { x = newX; }
 
bool getX() = x();
 
value main() { //test bool closureVariables() {
    x = bool() { return false; } ;
    b1 = getX() == false;
    changeX(bool() { return true; });
    return b1 && getX();
}   

//value main(){ //test bool higherOrderFunctionCompatibility1() {
//   // the parameter function is specific to int
//   int parameter(int _) { return 0; }
//   
//   // the higher order function expects to call the
//   // parameter function with other things too
//   int hof(int (value) p, value i) { return p(i); }
//   
//   // still this is ok, since functions in Rascal
//   // are partial. This call should simply succeed:
//   //if (hof(parameter, 1) != 0) {
//   //  return false;
//   //}
//   
//   // but the next call produces a CallFailed, since
//   // the parameter function is not defined on strings:
//   //try {
//     // statically allowed! but dynamically failing
//     hof(parameter, "string");
//   //  return false;
//   //} 
//   //catch CallFailed(_):
//     return true; 
//}