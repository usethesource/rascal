module lang::rascalcore::compile::Examples::Tst1

//value f(&T x){
//    return [&T _, *&T _] := [x];
//}

&T head({&T h, *&T _}) = h;
 
data X = x(int i);

int main() {
  set[X] l = {x(2)};
  
  int n = head(l).i;     // computeFieldType: Cannot access fields on type `&T`
  return n;
  //println(head2(l).i);  // no error
}  

//tuple[&A] foo(lrel[&A,&B] r) = r[0];

//rel[&A,list[&B]] foo(lrel[&A,&B] r) = {<a,r[a]> | a <- r};



//data D = d1() | d2();
//
//value main(){
//    D x = d1();
//    return x is d1;
//}

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
