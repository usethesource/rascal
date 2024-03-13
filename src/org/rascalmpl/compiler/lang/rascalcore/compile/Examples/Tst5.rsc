module lang::rascalcore::compile::Examples::Tst5

// #1464
//int f6(&T x) { return x; }

//#1855

import ParseTree;
//import IO;
bool main(){
    
    if(appl(r:regular(\iter-star(_)), 
                args:
                !
                []) 
                := appl(regular(\iter-star(sort("A"))), [char(0)])){
        return true;
    } else {
        return false;
    }
}


//import List;
//
//value main(){
//    myList = [<1,2>,<2,2>];
//    return sort(myList, bool (<int i, _>, <int j, _>) { return i < j; });
//}

//value main(){
//    if([1, int x] !:= [1]) return x;
//    return -1;
//}



//import util::Maybe;
//
// &T testFunction(Maybe[&T] _, &T x) = x;
// 
// value main() = testFunction(just(3), 5);
// 
//value edits(value x: ![]) {return x;}
//
//value main(){
//    return ![] := 
//            [1];
//}
