module lang::rascalcore::compile::Examples::Tst5

// #1464 en #1446
int f6(&T x) { return x; }

value main() = f6(1);

 &T get(list[&T] _) = 1;

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
