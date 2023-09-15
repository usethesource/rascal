@license{
   Copyright (c) 2009-2015 CWI
   All rights reserved. This program and the accompanying materials
   are made available under the terms of the Eclipse Public License v1.0
   which accompanies this distribution, and is available at
   http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl - CWI}
module lang::rascal::tests::library::ValueIO

import ValueIO;
import util::UUID;

data Bool(str def = "2") = btrue() | bfalse(bool falsity = true) | band(Bool left, Bool right) | bor(Bool left, Bool right);

data C = c(Maybe[int] i);
data Maybe[&T] = none() | some(&T t);

alias X[&T] = list[&T];

alias Y = int;

loc value_io_test = |memory://test-tmp/value-io-<"<uuidi()>">.test|;

/*TODO: cleanup generated files as in Java version */

private bool  binaryWriteRead(type[&T] _, value exp) {
   writeBinaryValueFile(value_io_test,exp);
   if (&T N := readBinaryValueFile(value_io_test) && N == exp) return true;
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

loc value_io2_test = |memory://test-tmp/value-io2-<"<uuidi()>">.test|;

 bool textWriteRead(type[&T] g, value exp) {
   writeTextValueFile(value_io2_test,exp);
   
   if (&T N := readTextValueFile(g, value_io2_test) && N == exp) {
     return true;
   }
   
   return false;
}

test bool textParametrizedAdt1() = textWriteRead(#Maybe[int], some(1));
test bool textParametrizedAdt2() = textWriteRead(#Maybe[value], none());
test bool textParametrizedAdt3() = textWriteRead(#C, c(some(1)));

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

loc compression_off = |memory://test-tmp/compression-off-<"<uuidi()>">.test|;

test bool disablingCompressionWorks(value v) {
   writeBinaryValueFile(compression_off,v, compression=false);
   return readBinaryValueFile(compression_off) == v;
}

data NestedValue
	= inAList(list[value] lvs)
	| inASet(set[value] svs)
	| inItself(NestedValue nv)
	;

loc compression_shared = |memory://test-tmp/compression-shared-<"<uuidi()>">.test|;

@maxDepth{20}
test bool disablingCompressionWorksWithSharedValues(set[NestedValue] a, set[NestedValue] b, NestedValue c, value d) {
	lab = [a,b];
	joined = <a,b,inAList(lab), inASet({a,c}), inAList([lab, d])>;
   writeBinaryValueFile(compression_shared, joined, compression=false);
   return readBinaryValueFile(compression_shared) == joined;
}

loc parsetree1 = |memory://test-tmp/parsetree1-<"<uuidi()>">.test|;

@Ignore{FOR NOW}
test bool writingParseTreeWorks() {
	t = parseNamedModuleWithSpaces(|project://rascal/src/org/rascalmpl/library/lang/rascal/syntax/Rascal.rsc|);
	writeBinaryValueFile(parsetree1, t);
	return readBinaryValueFile(parsetree1) == t;
}

@Ignore{FOR NOW}
test bool writingParseTreeWorksWithoutCompression() {
	t = parseNamedModuleWithSpaces(|project://rascal/src/org/rascalmpl/library/lang/rascal/syntax/Rascal.rsc|);
	writeBinaryValueFile(parsetree1, t, compression=false);
	return readBinaryValueFile(parsetree1) == t;
}

alias XX = loc;
data ExtraAliases
    = al0(int x)
    | al1(rel[XX, str, ExtraAliases] dt);
    
test bool aliasesNested(ExtraAliases e) = binaryWriteRead(#ExtraAliases, e);

// Reified types
 
private bool  binaryWriteRead(type[&T] typ) {
   writeBinaryValueFile(value_io_test,typ);
   rtyp = readBinaryValueFile(value_io_test);
   if (type[&T] N := rtyp && N == typ) return true;
   return false;
}

test bool reifyBool()   = binaryWriteRead(#bool);
test bool reifyStr()    = binaryWriteRead(#str);
test bool reifyInt()    = binaryWriteRead(#int);
test bool reifyReal()   = binaryWriteRead(#real);
test bool reifyRat()    = binaryWriteRead(#rat);
test bool reifyNum()    = binaryWriteRead(#num);
test bool reifyNode()   = binaryWriteRead(#node);
test bool reifyVoid()   = binaryWriteRead(#void);
test bool reifyValue()  = binaryWriteRead(#value);
test bool reifyList()   = binaryWriteRead(#list[int]);
test bool reifySet()    = binaryWriteRead(#set[int]);
test bool reifyLrel1()  = binaryWriteRead(#lrel[int,str]);
test bool reifyLrel2()  = binaryWriteRead(#lrel[int i, str s]);
test bool reifyRel1()   = binaryWriteRead(#rel[int,str]);
test bool reifyRel2()   = binaryWriteRead(#rel[int i, str s]);
test bool reifyMap1()   = binaryWriteRead(#map[int,str]);
test bool reifyMap2()   = binaryWriteRead(#map[int k,str v]);
test bool reifyFun1()   = binaryWriteRead(#int (int));
test bool reifyFun2()   = binaryWriteRead(#int (int _n));
test bool reifyPar1()   = binaryWriteRead(#&T);
test bool reifyPar2()   = binaryWriteRead(#&T <: num);

alias A[&T] = list[&T];
alias B[&T] = list[A[&T]];

test bool reifyAlias1() = binaryWriteRead(#A[int]);
test bool reifyAlias2() = binaryWriteRead(#B[int]);
