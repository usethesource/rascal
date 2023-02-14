module lang::rascalcore::compile::Examples::Tst0

import IO;
import String;
import lang::json::IO;
import util::UUID;

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
     println("writeRead: returnType = <returnType>");
    json = toJSON(dt);
    println("writeRead: json = <json>");
    fjson = fromJSON(returnType, json);
    println("writeRead: fjson = <fjson>");
    return fromJSON(returnType, json) == dt;
}

data DATA3 = data3(int n, str kw = "abc");
value main() // test bool json3()
    = writeRead(#DATA3, data3(123,kw="123"));
    
//alias T = tuple[int a, str s];
//
//T main() {
//    T x = <42, "abc">;
//    x.a ? 3 += 10;
//    return x;
//}