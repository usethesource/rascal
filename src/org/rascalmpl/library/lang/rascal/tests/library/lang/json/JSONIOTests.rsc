module lang::rascal::tests::library::lang::json::JSONIOTests

import IO;
import Type;
import util::Math;
import lang::json::IO;

loc targetFile = |tmp:///test.json|;

bool writeRead(&T dt) = writeRead(type(typeOf(dt), ()), dt);

bool writeRead(type[&T] returnType, &T dt) {
	writeTextJSonFile(targetFile, dt);

	if (dt !:= readTextJSonFile(returnType, targetFile)) {
		throw "Read with type";
	}

	//if (dt !:= readTextJSonFile(targetFile)) {
	//	throw "Read without type";	
	//}

	return true;
}

bool strWriteRead(&T dt) = strWriteRead(type(typeOf(dt), ()), dt);

bool strWriteRead(type[&T] returnType, &T dt) {
	s = toJSON(dt);

	if (dt !:= fromJSON(returnType, s)) {
		throw "Read with type";
	}

	return true;
}

data DATA1 = f1(int n) | f1(int n, str s) | rec1(DATA1 d1, DATA1 d2);

data DATA2 = f2(int n, str kw = "abc") | f2(int n, str s) | rec2(DATA2 d1, DATA2 d2, int n = 0);

@ignore{} test bool jsonWithBool1(bool dt) = writeRead(dt);
@ignore{} test bool jsonWithInt1(int dt) = writeRead(dt);
@ignore{} test bool jsonWithReal1(real dt) = writeRead(dt);
@ignore{} test bool jsonWithRat1(rat dt) = writeRead(dt);
@ignore{} test bool jsonWithNum1(num dt) = writeRead(dt);

@ignore{} test bool jsonWithLoc1(loc dt) = writeRead(dt);
@ignore{} test bool jsonWithStr1(str dt) = writeRead(dt);
@ignore{} test bool jsonWithDatetime1(datetime dt) = writeRead(dt);
@ignore{} test bool jsonWithList1(list[int] dt) = writeRead(dt);
@ignore{} test bool jsonWithSet1(set[int] dt) = writeRead(dt);
@ignore{} test bool jsonWithMap1(map[int, int]  dt) = writeRead(dt);
@ignore{} test bool jsonWithNode1(node  dt) = writeRead(dt);

@ignore{} test bool jsonWithDATA11(DATA1 dt) = writeRead(dt);
@ignore{} test bool jsonWithDATA21(DATA2 dt) = writeRead(dt);

@ignore{} test bool jsonRandom1(&T dt) = strWriteRead(dt);

@ignore{} test bool jsonWithBool2(bool dt) = strWriteRead(dt);
@ignore{} test bool jsonWithInt2(int dt) = strWriteRead(dt);
@ignore{} test bool jsonWithReal2(real dt) = strWriteRead(dt);
@ignore{} test bool jsonWithRat2(rat dt) = strWriteRead(dt);
@ignore{} test bool jsonWithNum2(num dt) = strWriteRead(dt);

@ignore{} test bool jsonWithLoc2(loc dt) = strWriteRead(dt);
@ignore{} test bool jsonWithStr2(str dt) = strWriteRead(dt);
@ignore{} test bool jsonWithDatetime1(datetime dt) = writeRead(dt);
@ignore{} test bool jsonWithList2(list[int] dt) = strWriteRead(dt);
@ignore{} test bool jsonWithSet2(set[int] dt) = strWriteRead(dt);
@ignore{} test bool jsonWithMap2(map[int, int]  dt) = strWriteRead(dt);
@ignore{} test bool jsonWithNode2(node  dt) = strWriteRead(dt);

@ignore{} test bool jsonWithDATA12(DATA1 dt) = strWriteRead(dt);
@ignore{} test bool jsonWithDATA22(DATA2 dt) = strWriteRead(dt);

@ignore{} test bool jsonRandom2(&T dt) = strWriteRead(dt);

