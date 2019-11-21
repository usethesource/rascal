module lang::rascalcore::compile::Examples::Tst2

import Exception;

@expected{IndexOutOfBounds}
value main(){ 
    list[int] L = [0,1,2,3]; 
    L[4] = 44; 
    L == [0,1,2,3,44]; 
    return false;    
}

//        
//test bool descendant11() = /int N := ("a" : 1) && N == 1;
//test bool descendant12() = /int N := <"a", 1> && N == 1;
        
//test bool descendant13() = [1, /int N, 3] := [1, [1,2,3,2], 3] && N == 1;
//test bool descendant14() = [1, /int N, 3] := [1, [1,2,3,2], 3] && N == 2;


//test bool keywordParam92(){
//    bool f11(bool c = false){
//        bool g11(){
//            return c;
//        }
//        return g11();
//    }
//    return f11() == false;
//}