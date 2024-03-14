module lang::rascalcore::compile::Examples::Tst5


syntax Expr = "e";

syntax Expr =
  right 
    ( right postIncr: Expr "++" 
    | right postDecr: Expr "--" 
    )
  > left 
      ( left div: Expr "/" !>> [/] Expr 
      | left remain: Expr "%" Expr
      )
  ;

syntax Expr =
   castPrim: "(" "PrimType" ")" Expr 
  > left 
      ( left div: Expr "/" !>> [/] Expr 
      | left remain: Expr "%" Expr
      )
  ;
  
  
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
