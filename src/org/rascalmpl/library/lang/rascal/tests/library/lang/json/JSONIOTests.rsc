module lang::rascal::tests::library::lang::json::JSONIOTests

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
	
// only single constructors supported for now
data DATA1 = data1(int n);
data DATA2 = data2(str n);
data DATA3 = data3(int n, str kw = "abc");
data Enum = x() | y() | z();
data DATA4 = data4(Enum e = x());

test bool jsonWithBool1(bool dt) = writeRead(#bool, dt);
test bool jsonWithInt1(int dt) = writeRead(#int, dt);
test bool jsonWithReal1(real dt) = writeRead(#real, dt);
test bool jsonWithRat1(rat dt) = writeRead(#rat, dt);
test bool jsonWithNum1(num dt) = writeRead(#num, dt);

test bool jsonWithLoc1(loc dt) = writeRead(#loc, dt);
test bool jsonWithStr1(str dt) = writeRead(#str, dt);
test bool jsonWithDatetime1(datetime dt) = writeRead(#datetime, dt);
test bool jsonWithList1(list[int] dt) = writeRead(#list[int], dt);
test bool jsonWithSet1(set[int] dt) = writeRead(#set[int], dt);
test bool jsonWithMap1(map[int, int] dt) = writeRead(#map[int,int], dt);
test bool jsonWithNode1(node  dt) = writeRead(#node, dt);

test bool jsonWithDATA11(DATA1 dt) = writeRead(#DATA1, dt);
test bool jsonWithDATA21(DATA2 dt) = writeRead(#DATA2, dt);

test bool jsonRandom1(value dt) = writeRead(#value, dt);

test bool json1() = writeRead(#DATA1, data1(123));
test bool json2() = writeRead(#DATA2, data2("123"));
test bool json3() = writeRead(#DATA3, data3(123,kw="123"));
test bool json4(Enum e) = writeRead(#DATA4, data4(e=e));

test bool originTracking() {
   example = readJSON(#node, |std:///lang/rascal/tests/library/lang/json/glossary.json|, trackOrigins=true);   
   content = readFile(|std:///lang/rascal/tests/library/lang/json/glossary.json|);

   poss = [<x.src, x.line> | /node x := example, x.line?]; // every node has a .src field, otherwise this fails with an exception

   for (<loc p, int line> <- poss) {
      assert content[p.offset] == "{";                // all nodes start with a {
      assert content[p.offset + p.length - 1] == "}"; // all nodes end with a }
      assert p.begin.line == line;
   }

   return true;
}