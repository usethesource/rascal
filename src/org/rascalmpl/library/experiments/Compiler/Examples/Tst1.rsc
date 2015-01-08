module experiments::Compiler::Examples::Tst1

import IO;
//str printResult(int n) = " <n>; ";
//str printResult(str s) = s + s;

int j0(0) = 0;
int j1(1) = 1; 
default int j3(int n) = 2*n; 
	
default int j4(int n) = 2*n - 1; 
		
int k(int n) = (n%2 == 0) ? { fail; } : 2*n; 
int l(int n) = (n%2 == 0) ? n*(n-1) : { fail; }; 

test bool nonDeterministicChoiceAndNormalComposition25() {
    list[int] inputs = [0,1,2,3,4,5,6,7,8,9,10];    
    
    list[int] outputs6 = [ ((k + l) o j4 + j0 + j1)(n) | int n <- inputs ]; 
    //list[int] outputs8 = [0,1] + [ 2*(2*n-1) | int n <- inputs - [0,1] ];
     
    // expected [0,1,6,10,14,18,22,26,30,34,38]
    //println("outputs6 = <outputs6>");
    //println("outputs8 = <outputs6>");
    //println("outputs6 == outputs8 = <outputs6 == outputs8>");
    return outputs6 == [0,1,6,10,14,18,22,26,30,34,38];
}

//int xxxx() { }  


