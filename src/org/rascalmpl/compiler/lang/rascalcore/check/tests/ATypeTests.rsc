module lang::rascalcore::check::tests::ATypeTests


// Work in progress

//import lang::rascalcore::check::AType;
//import analysis::typepal::Exception;
//
//// Properties of ATypes
//// Note: asubtype and alub throw TypeUnavailable for a type variable (tvar) in the type
//
//test bool reflexEq1(AType x) = x == x;
//test bool transEq1(AType x, AType y, AType z) = (x == y && y == z) ==> (x == z);
//test bool commutativeEq1(AType x, AType y) = (x == y) <==> (y == x);
//
//// ATypes are partially ordered
//
//test bool reflexLTE(AType x) {
//    try {
//        return asubtype(x, x);
//    } catch TypeUnavailable():
//        return true;
//}
//test bool antiSymmetricLTE(AType x, AType y) {
//    try {
//        return (asubtype(x, y) && asubtype(y, x)) ==> asubtype(x, y);
//    } catch TypeUnavailable():
//        return true;
//}
//test bool transLTE(AType x, AType y, AType z){
//    try {
//        return (asubtype(x, y) && asubtype(y, z)) ==> asubtype(x, z);
//    } catch TypeUnavailable():
//        return true;
//}
//
//// Lub
//
//test bool lubWellDefined(AType x, AType y){
//    try {
//        z = alub(x, y);
//        return asubtype(x, z) && asubtype(y, z);
//    } catch TypeUnavailable():
//        return true;    
//}