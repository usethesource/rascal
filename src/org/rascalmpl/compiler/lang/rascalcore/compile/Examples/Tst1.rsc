module lang::rascalcore::compile::Examples::Tst1

        
&L strange(&L <: num arg1, &R <: &L arg2){
  return arg2;
}

value main() = strange(3, "abc");


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
