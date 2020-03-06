module lang::rascalcore::compile::Examples::Tst1
 
//set[str] f(){
//    res = {};
//    res += 1;
//    return res;
//}
 
//str f(str n, bool c = true) = n;

int f(int n, bool c = true) = n;

void main(){
    f(3, c=3,s =4);
}
