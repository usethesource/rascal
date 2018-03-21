module lang::rascalcore::check::Test3

//import ParseTree;
//import IO;

import lang::rascal::tests::functionality::CallTestsAux;

//syntax XYZ = "x" | "y" | "z";

data C = c(int i);

// voidFun
  
test bool voidFun() {void f(){ } f(); return true;}
        
int fac(int n){ return (n <= 0) ? 1 : (n * fac(n - 1));}

test bool testFac() = fac(0) == 1 && fac(1) == 1 && fac(2) == 2 && 
                      fac(3) == 6 && fac(4) == 24;
    
int facNT(int n)  { 
    if(n == 0) { return 1;} 
    int z = facNT(n-1); return z*n; 
}
  
// facNotTailRec
  
test bool facNotTailRec() = facNT(0) == 1 && facNT(1) == 1 && facNT(2) == 2 && 
                            facNT(3) == 6 && facNT(4) == 24;          
              
// formalsAreLocal
  
test bool formalsAreLocal(){
    int fac(int n) { if (n == 0) { return 1; } int z = n; int m = fac(n - 1); return z * m; }
    return fac(0) == 1 && fac(1) == 1 && fac(2) == 2 && 
           fac(3) == 6 && fac(4) == 24;
}
  
// higherOrder
        
test bool higherOrder() {
    int add(int a, int b) { return a + b; };
    int doSomething(int (int a, int b) F) { return F(1,2); }; 
    int sub(int a, int b) { return a - b; }
    if (doSomething(add) != 3) return false;
    if (doSomething(sub) != -1) return false;
    return true;
}
    
// closures
  
test bool closures() {
    int x = 1;
    int f(int (int i) g, int j) { return g(j);}
    if (f(int (int i) { return i + 1; }, 0) != 1) return false;
    if (f(int (int i) { x = x * 2; return i + x; }, 1) != 3 || (x != 2))
        return false;
    return true;
}
        
// closuresVariables

bool() x = bool() { return false; } ;

void changeX(bool() newX) { x = newX; }
 
bool getX() = x();
 
test bool closureVariables() {
    x = bool() { return false; } ;
    b1 = getX() == false;
    changeX(bool() { return true; });
    return b1 && getX();
}   