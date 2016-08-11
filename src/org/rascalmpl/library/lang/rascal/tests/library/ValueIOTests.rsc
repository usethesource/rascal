module lang::rascal::tests::library::ValueIOTests
/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the EclipseLicense v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
*******************************************************************************/
import ValueIO;
import IO;
import util::Reflective;

data Bool(str def = "2") = btrue() | bfalse(bool falsity = true) | band(Bool left, Bool right) | bor(Bool left, Bool right);

data Maybe[&T] = none() | some(&T t);

alias X[&T] = list[&T];

alias Y = int;

/*TODO: cleanup generated files as in Java version */

private bool  binaryWriteRead(type[&T] typ, value exp) {
   writeBinaryValueFile(|test-temp:///value-io.test|,exp);
   if (&T N := readBinaryValueFile(|test-temp:///value-io.test|) && N == exp) return true;
   return false;
   }
   
test bool binBool() = binaryWriteRead(#bool, true);
 
test bool binInt() = binaryWriteRead(#int, 1);
 
test bool binReal() = binaryWriteRead(#real, 2.5);
 
test bool binStr1() = binaryWriteRead(#str, "\"abc\"");
 
test bool binStr2() = binaryWriteRead(#str, "\"ab\\nc\"");
 
test bool binLoc() = binaryWriteRead(#loc, |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>));
 
test bool binList() = binaryWriteRead(#list[int], [1,2,3]);
 
test bool binSet() = binaryWriteRead(#set[int], {1,2,3});
 
test bool binMap() = binaryWriteRead(#map[int, int], (1:10, 2:20));
 
test bool binTuple() = binaryWriteRead(#tuple[int, bool, str], <1,true,"abc">);
 
test bool binAdt() = binaryWriteRead(#Bool, band(bor(btrue(),bfalse()),band(btrue(),btrue())));
 
test bool binParametrizedAdt1() = binaryWriteRead(#Maybe[value], none());
 
test bool binParametrizedAdt2() = binaryWriteRead(#Maybe[int], some(1));
 
test bool binParamAliasListInt() = binaryWriteRead(#X[int], [1]);
 
test bool binParamAliasInt() = binaryWriteRead(#Y, 1);
 
private bool textWriteRead(type[&T] typ, value exp) {
   writeTextValueFile(|test-temp:///value-io2.temp|,exp);
   if (&T N := readTextValueFile(|test-temp:///value-io2.temp|) && N == exp) return true;
   return false;
   }
   
private bool textWriteRead1(type[&T] typ, value exp) {
   writeTextValueFile(|test-temp:///value-io2.temp|,exp);
   if (&T N := readTextValueFile(typ, |test-temp:///value-io2.temp|) && N == exp) return true;
   return false;
   }
   
test bool textBool() = textWriteRead(#bool, true);
 
test bool textInt() = textWriteRead(#int, 1);
 
test bool textReal() = textWriteRead(#real, 2.5);
 
test bool textStr1() = textWriteRead(#str, "\"abc\"");
 
test bool textStr2() = textWriteRead(#str, "\"ab\\nc\"");
 
test bool textLoc() = textWriteRead(#loc, |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>));
 
test bool textList() = textWriteRead(#list[int], [1,2,3]);
 
test bool textSet() = textWriteRead(#set[int], {1,2,3});
 
test bool textMap() = textWriteRead(#map[int, int], (1:10, 2:20));
 
test bool textTuple() = textWriteRead(#tuple[int, bool, str], <1,true,"abc">);
 
@ignore
test bool textAdt() = textWriteRead1(#Bool, band(bor(btrue(),bfalse()),band(btrue(),btrue())));
 

test bool valueText(value v) = textWriteRead(#value, v);
test bool nodeText(node v) = textWriteRead(#node, v);
test bool strText(str v) = textWriteRead(#str, v);
test bool mapText(map[value, value] v) = textWriteRead(#map[value,value], v);
test bool setText(set[value] v) = textWriteRead(#set[value], v);
test bool listText(list[value] v) = textWriteRead(#list[value], v);
test bool tupleText(tuple[value,value,value] v) = textWriteRead(#tuple[value,value,value], v);
test bool numText(num v) = textWriteRead(#num, v);

test bool valueBinary(value v) = binaryWriteRead(#value, v);
test bool nodeBinary(node v) = binaryWriteRead(#node, v);
test bool strBinary(str v) = binaryWriteRead(#str, v);
test bool mapBinary(map[value, value] v) = binaryWriteRead(#map[value,value], v);
test bool setBinary(set[value] v) = binaryWriteRead(#set[value], v);
test bool listBinary(list[value] v) = binaryWriteRead(#list[value], v);
test bool tupleBinary(tuple[value,value,value] v) = binaryWriteRead(#tuple[value,value,value], v);
test bool numBinary(num v) = binaryWriteRead(#num, v);

test bool disablingCompressionWorks(value v) {
   writeBinaryValueFile(|test-temp:///compression-off.test|,v, compression=false);
   return readBinaryValueFile(|test-temp:///compression-off.test|) == v;
}

data NestedValue
	= inAList(list[value] lvs)
	| inASet(set[value] svs)
	| inItself(NestedValue nv)
	;

@maxDepth{20}
test bool disablingCompressionWorksWithSharedValues(set[NestedValue] a, set[NestedValue] b, NestedValue c, value d) {
	lab = [a,b];
	joined = <a,b,inAList(lab), inASet({a,c}), inAList([lab, d])>;
   writeBinaryValueFile(|test-temp:///compression-shared.test|, joined, compression=false);
   return readBinaryValueFile(|test-temp:///compression-shared.test|) == joined;
}

test bool writingParseTreeWorks() {
	t = parseNamedModuleWithSpaces("lang::rascal::syntax::Rascal");
	writeBinaryValueFile(|test-temp:///parsetree1|, t);
	return readBinaryValueFile(|test-temp:///parsetree1|) == t;
}
test bool writingParseTreeWorksWithoutCompression() {
	t = parseNamedModuleWithSpaces("lang::rascal::syntax::Rascal");
	writeBinaryValueFile(|test-temp:///parsetree1|, t, compression=false);
	return readBinaryValueFile(|test-temp:///parsetree1|) == t;
}
