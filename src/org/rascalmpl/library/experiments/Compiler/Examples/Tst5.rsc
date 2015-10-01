module experiments::Compiler::Examples::Tst5

start syntax XorY = x : "x" | y : "y";

lexical Layout = [.;];
layout L = Layout* !>> [.;];


//@ignoreInterpreter{While this should work, the fix is too large, and there are workarounds}
//test bool concreteMatchVisitLayout() {
//  result = false;
//  visit ([start[XorY]] ".x.") {
//    case (Layout)`.`: result = true;
//  }
//  return result;
//}
//test bool concreteReplaceInLayout() 
//  = visit([start[XorY]] ".x;") {
//    case (Layout)`.` => (Layout)`;`
//  } == [start[XorY]] ";x;";

value main(){
    switch([XorY] "y"){
        case (XorY) `x`: throw "fail to due extra match"; 
        case y():        return true;
    }
    throw "fail due to missing match";
}

//value main() = y() := [XorY] "y";

//test bool concreteSwitch6(){
//    switch([XorY] "y"){
//        case x():        throw "fail to due extra match";
//        case y():        return true;
//    }
//    throw "fail due to missing match";
//}
