@bootstrapParser
module lang::rascalcore::check::tests::DataTypeTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

data D = d(int ival);

test bool andError1() = unexpectedType("3 && true;");

test bool andError2() = unexpectedType("3 ==\> true;");

test bool condExpError1() = unexpectedType("1 ? 2 : 3;");
  	
test bool addError1() = unexpectedType("3 + true;");
  	
test bool subError1() = unexpectedType("3 - true;");

test bool uMInusError1() = unexpectedType("- true;");

test bool timesError1() = unexpectedType("3 * true;");

test bool divError1() = unexpectedType("3 / true;");
  	
test bool modError1() = unexpectedType("3 % true;");

test bool orError1() = unexpectedType("3 || true;");

test bool addError1() = unexpectedType("3 + true;");

test bool addError1() = unexpectedType("3 + true;");


test bool UndefinedLocationError11() = uninitialized("loc Loc; Loc.url;");

test bool UndefinedLocationError21() = uninitialized("loc Loc; Loc.url = \"abc\";");

test bool UndefinedLocationError31() = uninitialized("loc Loc; Loc[url = \"abc\"];");

  	
test bool WrongLocFieldError11() = undefinedField("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc.bla;");
  	
test bool WrongLocFieldError21() = undefinedField("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc[bla=3];");
  	
test bool URLFieldError11() = unexpectedType("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc.uri=true;");

//test bool URLFieldError21() = parseError("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc.uri=\"? ??\";");

test bool LengthFieldError1() = unexpectedType("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc.length=true;");

test bool OffsetFieldError1() = unexpectedType("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc.offset=true;");

 
test bool BeginLineFieldError1() = undefinedField("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc.beginLine=true;");
 
test bool EndLineFieldError1() = unexpectedType("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc.end=\<true,12\>;");
  	
test bool BeginColumnFieldError1() = unexpectedType("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc.begin=\<true,12\>;");

test bool SubscriptError21() = undeclaredVariable("L[5];");

test bool UndefinedSetElementError1() = undeclaredVariable("{X};");
  	
test bool inError1() = unexpectedType("1 in 3;");
 
test bool productError1() = unexpectedType("{1,2,3} * true;");

test bool tupleError11() = undefinedField("tuple[int key, str val] T = \<1, \"abc\"\>; T.zip == \"abc\";");

test bool tupleError21() = uninitialized("tuple[int key, str val] T; T.key;");
	
test bool UndefinedDataTypeAccess11() = uninitialized("someD.ival", initialDecls= ["data D = d(int ival);"]);

test bool UndefinedDataTypeAccess21() = uninitialized("someD.ival = 3", initialDecls= ["data D = d(int ival);"]);

test bool UndefinedMapElementError11() = undeclaredVariable("(X:2);");
  	
test bool UndefinedMapElementError21() = undeclaredVariable("(1:Y);");

test bool UndefinedTupleElementError11() = undeclaredVariable("\<1,X,3\>;");
  	   
test bool UndefinedRelationElementError11() = undeclaredVariable("{\<1,10\>, \<X,20\>};");
  	    
test bool UndefinedRelationElementError21() = undeclaredVariable("{\<1,10\>, \<10, Y\>};");
  	
test bool UndefinedRelationElementError31() = undeclaredVariable("{\<1,10\>, T, \<3, 30\>};");

test bool compError1() = unexpectedType("1 o 3;");
  	
test bool closError11() = unexpectedType("1*;");
 
test bool closError21() = unexpectedType("1+;");
  
test bool namedRelationError1() = undefinedField("rel[int from, int to] R = {\<1,10\>, \<2,20\>}; R.zip == {10,20};");
 
test bool undefined1() = uninitialized("int T; (T ? 13) == 13;");
 
test bool remainder1() = unexpectedType("num n = 5; n % 2 == 1;"); 

test bool remainder2() = unexpectedType("num n = 2; 5 % n == 1;");  		
  
  
