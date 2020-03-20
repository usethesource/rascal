module lang::rascalcore::compile::Examples::Tst1


data D = d1() | d2();

value main(){
    D x = d1();
    return x is d1;
}

//set[str] f(){
//    res = {};
//    res += 1;
//    return res;
//}
 
//str f(str n, bool c = true) = n;

//int f(int n, bool c = true) = n;
//
//void main(){
//    f(3, c=3,s =4);
//}
