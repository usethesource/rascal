module lang::rascalcore::compile::Examples::Tst1
 
 
//int main(){
//    M = ();
//    M += (1:10);
//    M["a"]="A";
//    return M[1];
//}
//data L(str e = "e", str f = e + e) = n(str g = f + f);
// 
//set[str] f(){
//    res = {};
//    res += 1;
//   
//    return res;
//}
//
//public list[&T] dup(list[&T] lst) {
//  done = {};
//  return for (e <- lst, e notin done) {
//    done = done + {e};
//    append e;
//  }
//}
//
//test bool compositeAndBothBTCnt() {
//    n = 0;
//    if( [*int _, int  _, *int _] := [1,2,3] && [*int _, int  _, *int _] := [4, 5, 6] )  {
//        n = n + 1;
//        fail;
//    }
//    return n == 9;
//}
//
//int g(n) { n = 10; return n; }
//
//int h(int x = 1, int y = x + x) = 0;
//
//data D(int nn = -3, int m = nn + nn);
//
//tuple[int,int] tp() = <1,2>;
//
//value k(){
//    <x, y> = tp();
//    y += 1;
//    return y;
//}
//
//
//
//void l(){
//    M += 1;
//}
//
//int M = 3;
//
data E = e(int n) | f(str s);
value m(E x){
    switch(x){
        case f(n): return n;
        case e(n): return n + 1;
    }
    return 0;
}

value main(){
    for(s:str x <- {"a", "b", "c"}){
    ;}
    s += 1;
    return s + 1;

}
