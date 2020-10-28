module lang::rascalcore::compile::Examples::Tst1

test bool higherOrder() {
    int add(int a, int b) { return a + b; };
    int doSomething(int (int a, int b) F) { return F(1,2); }; 
    int sub(int a, int b) { return a - b; }
    if (doSomething(add) != 3) return false;
    if (doSomething(sub) != -1) return false;
    return true;
}

//import  lang::rascalcore::compile::Examples::Tst2;
//
//C c(int i) {
//  if (i == 0 || i mod 3 != 0) 
//    fail c;
//  else
//    return c(i / 3);
//}
//
//C c(int i) {
//  if (i == 0 || i mod 2 != 0) 
//    fail c;
//  else
//    return c(i / 2);
//}
//
//C c(int i) = c(i / 7) when i mod 7 == 0, i != 0;

//test bool bt1() = c(7 * 5 * 3 * 2) == c(1);
//test bool bt2() = c(5 * 3 * 2) == c(1);
// test bool bt3() = c(3 * 2) == c(1);
//test bool bt(int i) = (j := i mod 100) && c(xxx) := c(j) && xxx <= j;
//value main() = c(0);
