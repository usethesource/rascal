module lang::rascalcore::compile::Examples::Tst1

map[&A,set[&B]] foo(rel[&A,&B] r) = (a:r[a] | a <- r);

//rel[&A,&B] foo(lrel[&A,&B] r) { a = r[0]; return {<a,r[a][0]>}; }


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
