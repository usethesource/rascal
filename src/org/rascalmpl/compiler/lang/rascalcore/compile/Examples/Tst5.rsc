module lang::rascalcore::compile::Examples::Tst5


&T <: num makeSmallerThan(&T <: num n) {
    &T <: num res;
    if (int i := n) {
        res = i;
        return res;    
    }
    return n;
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


