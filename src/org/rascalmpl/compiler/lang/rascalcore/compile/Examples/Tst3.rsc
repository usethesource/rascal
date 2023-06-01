module lang::rascalcore::compile::Examples::Tst3

import String;
import lang::json::IO;
import util::UUID;
import IO;

loc targetFile = |test-temp:///test-<"<uuidi()>">.json|;

bool jsonFeaturesSupported(value v) {
    for (/num r := v, size("<r>") > 10) {
         // json can only contain double precision numbers (doubles)
         // so let's ignore the cases where we get higher random numbers
        return false;
    }
    
    return true;
}    

bool writeRead(type[&T] returnType, &T dt) {
    if (!jsonFeaturesSupported(dt)) {
        return true;
    }
    json = toJSON(dt);
    return fromJSON(returnType, json) == dt;
}
    
//// only single constructors supported for now
data DATA1 = data1(int n);
data DATA2 = data2(str n);
data DATA3 = data3(int n, str kw = "abc");
data Enum = x() | y() | z();
data DATA4 = data4(Enum e = x());
//
//test bool jsonWithBool1(bool dt) = writeRead(#bool, dt);
//test bool jsonWithInt1(int dt) = writeRead(#int, dt);
//test bool jsonWithReal1(real dt) = writeRead(#real, dt);
//test bool jsonWithRat1(rat dt) = writeRead(#rat, dt);
//test bool jsonWithNum1(num dt) = writeRead(#num, dt);
//
//test bool jsonWithLoc1(loc dt) = writeRead(#loc, dt);
//test bool jsonWithStr1(str dt) = writeRead(#str, dt);
//test bool jsonWithDatetime1(datetime dt) = writeRead(#datetime, dt);
//test bool jsonWithList1(list[int] dt) = writeRead(#list[int], dt);
//test bool jsonWithSet1(set[int] dt) = writeRead(#set[int], dt);
//test bool jsonWithMap1(map[int, int] dt) = writeRead(#map[int,int], dt);
//test bool jsonWithNode1(node  dt) = writeRead(#node, dt);
//
//test bool jsonWithDATA11(DATA1 dt) = writeRead(#DATA1, dt);
//test bool jsonWithDATA21(DATA2 dt) = writeRead(#DATA2, dt);
//
//test bool jsonRandom1(value dt) = writeRead(#value, dt);
//
//test bool json1() = writeRead(#DATA1, data1(123));
//test bool json2() = writeRead(#DATA2, data2("123"));
//test bool json3() = writeRead(#DATA3, data3(123,kw="123"));
//test bool json4(Enum e) = writeRead(#DATA4, data4(e=e));

value main(){ //test bool originTracking() {
   example = readJSON(#node, |std:///lang/rascal/tests/library/lang/json/glossary.json|, trackOrigins=true);   
   content = readFile(|std:///lang/rascal/tests/library/lang/json/glossary.json|);

   lrel[loc,int] poss = [<x.src, x.line> | /node x := example, x.line?]; // every node has a .src field, otherwise this fails with an exception

   for (<loc p, str line> <- poss) {
   println("<p>, <line>");
      assert p.begin.line == line;
   }

   return true;
}
//data x = x() | x(int x);
//
////x x(int n) { return x(2 * n); }
////
//x x = x(3);
//
//value main(){
//    return x.x;
//}


//test bool comprehension2() 
//    = (k:k*k | k <- [1..5]) == (1:1,2:4,3:9,4:16);

//test bool matchNestedList14() {
//     if([*list[int] L] := [[1,2]] && L == [[1,2,3]]){
//         return false;
//      } else {
//         return true;
//     }
//}

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
