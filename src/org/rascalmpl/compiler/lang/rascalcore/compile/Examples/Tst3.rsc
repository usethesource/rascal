module lang::rascalcore::compile::Examples::Tst3

import List;

int() makeGet1(int n){
    return int() { return n; };
}

test bool closure1(){
    f = makeGet1(3);
    return f() == 3;
}

int() makeGet2(int x){
    int n = x;
    return int() { return n; };
}

test bool closure2(){
    f = makeGet2(3);
    return f() == 3;
}

test bool closure3(){
    x = 3;
    f = makeGet1(x);
    x = 4;
    return f() == 3;
}

int() makeInc1(int n){
    return int() { n += 1; return n; };
}

test bool closure4(){
    x = 3;
    f = makeInc1(x);
   
    return f() == 4 && x == 3 && f() == 5;
}
int() makeInc2(int n){
    m = n;
    return int() { m += 1; return m; };
}

test bool closure5(){
    f = makeInc2(3);
   
    return f() == 4 && f() == 5;
}

test bool closure6(){
    
    int() make(int b){
        return int(){ b += 1; return b; };
    }

    int f(int c){
        return (make(c)());
    }
    
    return f(10) == 11;
}

test bool closure7(){
    
    int() make(int b){
        int x = b;
        return int(){ x += 1; return x; };
    }

    int f(int c){
        return (make(c)());
    }
    
    return f(10) == 11;
}

//test bool closure8(bool b = false){
//    
//    int() make(int b){
//        return int(){ b += 1; return b; };
//    }
//
//    int f(int c){
//        return (make(c)());
//    }
//    
//    return f(10) == 11;
//}

//test bool closure9(){
//    
//    int() make(int b, int delta=1){
//        return int(){ b += delta; return b; };
//    }
//
//    int f(int c, int delta = 1){
//        return (make(c,delta=delta)());
//    }
//    
//    return f(10,delta=2) == 12;
//}


//bool closure10(bool kw = false){
//    bool isLess(int a, int b) = a < b;
//    
//    list[int] sortIt(list[int] L){
//        return sort(L, isLess);
//    }
//    
//    return sortIt([10, 3, 5]) == [3, 5, 10];
//}
//
//test bool doClosure10() = closure10();

bool closure11(int n){
    int(int) makeAddN() = int (int m) { return m + n; };
    
    return makeAddN()(5) == (5 + n);
}

test bool doClosure11(int n) = closure11(n);

bool closure12(int n){
    int(int) makeAdd(int k) = int (int m) { return k + m; };
    
    return makeAdd(n)(5) == (5 + n);
}

test bool doClosure12(int n) = closure12(n);

//data Calculator;
//data Requirement;
//void solver1(){
//    set[Calculator] calculators = {};
//    set[Requirement] requirements = {};
//  
//    void solved(Calculator calc){
//        calculators -= calc;  
//    }
//     void solved(Requirement req){
//        requirements -= req;
//    }  
//       
//    bool evalCalc(Calculator calc) = true;
//     void scheduleCalc(Calculator calc) { }
//     
//    void evalOrScheduleCalc(Calculator calc){
//        try {
//            if(evalCalc(calc)){
//                solved(calc);
//            } else {
//                scheduleCalc(calc);
//            }
//         } catch _:;
//     }
//}


//value main(){
//    i = 0;
//    do {
//        // TODO: ok when list match
//        if({x} := {1})
//            i += 1;
//        else 
//            return i;
//    
//    } while(i <= 10);
//    return i;
//}
//
//
//value main() { //test bool fail5() {
//    str trace = "";
//    if(true) {
//        if(false) {
//           ;
//        } else {
//           trace += "fail inner!";
//           fail;
//        }
//    } else {
//        trace += "else outer!";
//    }
//    return trace ; //== "fail inner!else outer!";
//}
//
//
//
//value main(){ //test bool ifThen3() {
//    int n = 10; 
//    l:if(int i <- [1,2,3]){ 
//        if (i % 2 != 0) { 
//            n = n + 4; 
//            fail l;
//        } 
//        n = n - 4;
//      } 
//    return n ;//== 10;
//}


