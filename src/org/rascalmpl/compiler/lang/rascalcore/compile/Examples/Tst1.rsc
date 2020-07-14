module lang::rascalcore::compile::Examples::Tst1

value main() { //test bool interpolateIndPatternDecl1(){ 
    int n = 3; 
    return (/<x:<n>>/ := "3" && x == "3");
}



 ///////////////////////////
//@javaClass{org.rascalmpl.library.Type}
//public java bool eq(value x, value y);
//
//@javaClass{org.rascalmpl.library.Prelude}
//public java bool isEmpty(list[&T] lst);

//test bool dtstDifference(list[&T] lst) {
//    //if(isEmpty(lst)) 
//    //  return true;
//    //  
//    for(&T elem <- lst) {
//        bool deleted = false;
//        lhs = lst - [elem];
//        rhs = [ *( (eq(elem,el) && !deleted) ? { deleted = true; []; } : [ el ]) | &T el <- lst ];
//        
//        //if (!eq(lhs,rhs) || typeOf(lhs) != typeOf(rhs)) {
//        //  throw "Removed <elem> from <lst> resulted in <lhs> instead of <rhs>";
//        //  
//        //}
//    }
//    return true;
//}

//value main(){
//    bool deleted = false;
//    lst = [1,2,3,1,2,4,4,4];
//    for(int elem <- lst) {
//        bool deleted = false;
//        lhs = lst - [elem];
//        rhs = [ *( (elem == el && !deleted) ? { deleted = true; []; } : [ el ]) | int el <- lst ]; 
//    }
//
//}

//value main(){
//    int n = 0;
//    lst = [1,2,3,1,2,3];
//    lst = [ *( (e > 2) ? {n += 1; []; } : [e]) | int e <- lst];
//    return <lst, n>;
//}



//import IO;
//data PAIR = pair(int a, int b);
//
//value main(){
//    res = {pair(1,2),pair(2,2),pair(2,3),pair(3,3)};
//    if ( { pair( a, b ), pair(b, b), *c } := res ) 
//              res = { *c, pair( a, b ) };
//    return res;
//}


//value main(){
//    res = {pair(1,2),pair(2,2),pair(2,3),pair(3,3)};
//    return { pair(a, b) } := res ;
//}

//value main(){
//    res = {<1,2>,<2,2>,<2,3>,<3,3>};
//    return { < a, b > } := res ;
//}

//value main(){
//    res = {<1,2>,<2,2>,<2,3>,<3,3>};
//    return { < a, b >, <b, b>, *c } := res ;
//}

//value main(){
//    res = {<1,2>,<2,2>,<2,3>,<3,3>};
//    if ( { < a, b >, <b, b>, *c } := res ) 
//              res = { *c, < a, b > };
//    return res;
//}

//rel[int,int] removeIdPairs(rel[int,int] inp){
//   res = inp;
//   //solve(res) {
//         println(res);
//         if ( { < a, b >, < b, b >, *c } := res ) 
//              res = { *c, < a, b > };
//         //else ;
//   //}
//   return res;
//}
 
//value main() //test bool removeIdPairs1() 
//    = removeIdPairs({});// == {};
//test bool removeIdPairs2() = removeIdPairs({<1,2>,<2,3>}) == {<1,2>,<2,3>};
//value main() // test bool removeIdPairs3() 
//    = removeIdPairs({<1,2>,<2,3>,<2,2>});// == {<1,2>,<2,3>};
//test bool removeIdPairs4() = removeIdPairs({<1,2>,<2,2>,<2,3>,<3,3>}) == {<1,2>,<2,3>};
//test bool removeIdPairs5() = removeIdPairs({<2,2>,<1,2>,<2,2>,<2,3>,<3,3>}) == {<1,2>,<2,3>};
//test bool removeIdPairs6() = removeIdPairs({<2,2>,<3,3>,<1,2>,<2,2>,<2,3>,<3,3>}) == {<1,2>,<2,3>};
