module lang::rascalcore::compile::Examples::Tst3
//import IO;
//import List;
//void main(){ //void checkModuleName(loc mloc, QualifiedName qualifiedModuleName, Collector c){
//    pcfgVal = [1];
//    if([int pcfg] := pcfgVal){ 
//      println(pcfg);
//    } else if(isEmpty(pcfgVal)){
//        return;
//    } else {
//        throw "Inconsistent value for \"pathconfig\"";
//    }
//}  

value main(){ //test bool matchListTuples8() {
    if([<1, int n, 3>] := [<1, 2, 3>], n == -2){
        return false;
    } else {
        return true;
    }
}

//void main(){
//    pcfgVal = [1];
//    if([int pcfg] := pcfgVal){ 
//        try {   
//            x =10;
//        } catch _: {
//            throw "abc";
//        }
//    } else if(pcfgVal == []){
//        return;
//    } else {
//        throw "inconsistent";
//    }
//}
//int main(){ //test bool matchListTuples8() {
//    if([int n] := [2]){
//        return 0;
//    } else {
//        return 1;
//    }
//}

//test bool matchSetLists8() {
//    if({[1, int n, 3]} := {[1, 2, 3]} && n == -2){
//        return false;
//    } else {
//        return true;
//    }
//}

// Same as interpreted
//data D = d(str s) | d(int n) | d();
//
//@doc{triggers issue #1234}
//test bool constructorDynamicMatch() {
//  value x = 1;
//  
//  // Due to issue #1234, `d(x)` would throw a MatchFailed() here */
//  return d(int i) := d(x) && i == 1;
//}


//data D = d(int n) | d(str s) | dl(list[D] ds);
//
//bool containsNoDstr(D t)
//    =  !(/d(str _) := t);
//    
////bool containsNoDstr(D t)
////    =  (/d(str _) := t) ? false : true;
//
//value main() = containsNoDstr(dl([d(1), d("a")]));
