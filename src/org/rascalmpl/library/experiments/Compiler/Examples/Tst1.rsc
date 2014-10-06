module experiments::Compiler::Examples::Tst1

import IO;
import Type;
//import util::Math;
import lang::json::IO;

//loc targetFile = |tmp:///test.json|;

//bool writeRead(&T dt) = writeRead(type(typeOf(dt), ()), dt);
//
bool writeRead(type[&T] returnType, &T dt) = fromJSON(returnType, json) == dt 
  when str json := toJSON(dt), bprintln("x = <dt>"), bprintln("json = <json>"), &T dt2 := fromJSON(returnType, json), bprintln("y = <dt2>"), bprintln(dt == dt2); 
	
data DATA1 = f1(int n) | f1(int n, str s) | rec1(DATA1 d1, DATA1 d2);

value main(list[value] args) = type(typeOf (f1(1328258127)), ());


//data DATA2 = f2(int n, str kw = "abc") | f2(int n, str s) | rec2(DATA2 d1, DATA2 d2, int n = 0);

//test bool jsonWithBool1(bool dt) = writeRead(dt);
//test bool jsonWithInt1(int dt) = writeRead(dt);
//test bool jsonWithReal1(real dt) = writeRead(dt);
//test bool jsonWithRat1(rat dt) = writeRead(dt);
//test bool jsonWithNum1(num dt) = writeRead(dt);
//
//test bool jsonWithLoc1(loc dt) = writeRead(dt);
//test bool jsonWithStr1(str dt) = writeRead(dt);
//test bool jsonWithDatetime1(datetime dt) = writeRead(dt);
//test bool jsonWithList1(list[int] dt) = writeRead(dt);
//test bool jsonWithSet1(set[int] dt) = writeRead(dt);
//test bool jsonWithMap1(map[int, int]  dt) = writeRead(dt);
//test bool jsonWithNode1(node  dt) = writeRead(dt);

//test bool jsonWithDATA11(DATA1 dt) = writeRead(dt);

//test bool jsonWithDATA21(DATA2 dt) = writeRead(dt);
//
//test bool jsonRandom1(&T dt) = writeRead(dt);
//
//test bool jsonWithBool2(bool dt) = writeRead(dt);
//test bool jsonWithInt2(int dt) = writeRead(dt);
//test bool jsonWithReal2(real dt) = writeRead(dt);
//test bool jsonWithRat2(rat dt) = writeRead(dt);
//test bool jsonWithNum2(num dt) = writeRead(dt);
//
//test bool jsonWithLoc2(loc dt) = writeRead(dt);
//test bool jsonWithStr2(str dt) = writeRead(dt);
//test bool jsonWithDatetime1(datetime dt) = writeRead(dt);
//test bool jsonWithList2(list[int] dt) = writeRead(dt);
//test bool jsonWithSet2(set[int] dt) = writeRead(dt);
//test bool jsonWithMap2(map[int, int]  dt) = writeRead(dt);
//test bool jsonWithNode2(node  dt) = writeRead(dt);
//
//test bool jsonWithDATA12(DATA1 dt) = writeRead(dt);
//test bool jsonWithDATA22(DATA2 dt) = writeRead(dt);
//
//test bool jsonRandom2(&T dt) = writeRead(dt);

//value main(list[value] args) {f2(-952181339,""); }

//value main(list[value] args) { DATA1 dt = f1(1328258127); str json := toJSON(dt) && bprintln("x = <dt>") && bprintln("json = <json>") && &T dt2 := fromJSON(#DATA1, json)/*, bprintln("y = <dt2>"), bprintln(dt == dt2)*/;  }

//value main(list[value] args) { DATA2 dt = f2(1443977792, ""); writeRead(dt); } //json = ["cons",["f2",2,[["int",1443977792],["str",""]]]]; bprintln("x = <dt>"); bprintln("y = <dt>"); fromJSON(#DATA2, "<json>") == dt; } //writeRead(dt); }

//value main(list[value] args) { DATA2 dt = f2(1443977792, ""); writeRead(dt); } //writeRead(dt); }


