module lang::rascalcore::compile::Examples::Tst1

layout Whitespace = [\ \t\n]*;
syntax A = "a";
syntax B = "b";

syntax P[&T] = "{" &T ppar "}";

test bool PA9() 
    = "<(P[A]) `{a}`>" == "<[P[A]] "{a}">";
    
value main() = PA9();
    
    //import List;
//
//list[int] f([*int x, *int y]) { if(size(x) == size(y)) return x; fail; }
////default list[int] f(list[int] l) = l;
//
//value main(){ //test bool overloadingPlusBacktracking2(){
//    return f([1,2,3,4]);// == [1, 2];
//}