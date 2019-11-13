module lang::rascal::tests::library::lang::json::JSONIOTests

import IO;
import String;
import Node;
import Type;
import util::Math;
import lang::json::IO;
import util::UUID;

loc targetFile = |test-temp:///test-<"<uuidi()>">.json|;

bool writeRead(&T dt) = writeRead(type(typeOf(dt), ()), dt);

    
bool jsonFeaturesSupported(value v) {
    for (/num r := v, size("<r>") > 10) {
         // json can only contain double precision video's
         // so let's ignore the cases
        return false;
    }
    for (/node n := v, getAnnotations(n) != ()) {
         // json reader/writer can't handle annotations at the moment
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
	
data DATA1 = f1(int n) | f1(int n, str s) | rec1(DATA1 d1, DATA1 d2);

data DATA2 = f2(int n, str kw = "abc") | f2(int n, str s) | rec2(DATA2 d1, DATA2 d2, int n = 0);

test bool jsonWithBool1(bool dt) = writeRead(dt);
test bool jsonWithInt1(int dt) = writeRead(dt);
test bool jsonWithReal1(real dt) = writeRead(dt);
test bool jsonWithRat1(rat dt) = writeRead(dt);
test bool jsonWithNum1(num dt) = writeRead(dt);

test bool jsonWithLoc1(loc dt) = writeRead(dt);
test bool jsonWithStr1(str dt) = writeRead(dt);
test bool jsonWithDatetime1(datetime dt) = writeRead(dt);
test bool jsonWithList1(list[int] dt) = writeRead(dt);
test bool jsonWithSet1(set[int] dt) = writeRead(dt);
test bool jsonWithMap1(map[int, int]  dt) = writeRead(dt);
test bool jsonWithNode1(node  dt) = writeRead(dt);

test bool jsonWithDATA11(DATA1 dt) = writeRead(dt);
test bool jsonWithDATA21(DATA2 dt) = writeRead(dt);

test bool jsonRandom1(&T dt) = writeRead(dt);

test bool jsonWithBool2(bool dt) = writeRead(dt);
test bool jsonWithInt2(int dt) = writeRead(dt);
test bool jsonWithReal2(real dt) = writeRead(dt);
test bool jsonWithRat2(rat dt) = writeRead(dt);
test bool jsonWithNum2(num dt) = writeRead(dt);

test bool jsonWithLoc2(loc dt) = writeRead(dt);
test bool jsonWithStr2(str dt) = writeRead(dt);
test bool jsonWithDatetime2(datetime dt) = writeRead(dt);
test bool jsonWithList2(list[int] dt) = writeRead(dt);
test bool jsonWithSet2(set[int] dt) = writeRead(dt);
test bool jsonWithMap2(map[int, int]  dt) = writeRead(dt);
test bool jsonWithNode2(node  dt) = writeRead(dt);

test bool jsonRandom2(&T dt) = writeRead(dt);

data D 
    = date(datetime dt)
    | uri(loc l)
    | string(str s)
    | integer(int n)
    | float(real r)
    | lists(list[D] ls)
    | maps(map[str,str] strmaps)
    | maps2(map[str,D] dmaps)
    | nested(D d1, D d2)
    | kwparams(int x = 2, D d = integer(0))
    ;
    
@ignore{Currently not working with datetimes not as ints}
test bool jsonStreaming1(D dt) {
    if (!jsonFeaturesSupported(dt)) {
        return true;
    }
    writeJSON(targetFile, dt, dateTimeAsInt=false);
    return readJSON(#D, targetFile) == dt;
}

test bool jsonStreaming2(D dt) {
    if (!jsonFeaturesSupported(dt)) {
        return true;
    }
    writeJSON(targetFile, dt, dateTimeAsInt=true);
    return readJSON(#D, targetFile) == dt;
}

