module tests::functionality::DataTypeTCTests

import StaticTestingUtils;

data D = d(int ival);

public test bool andError1() = unexpectedType("3 && true;");

public test bool andError2() = unexpectedType("3 ==\> true;");

public test bool condExpError1() = unexpectedType("1 ? 2 : 3;");
  	
public test bool addError1() = unexpectedType("3 + true;");
  	
public test bool subError1() = unexpectedType("3 - true;");

public test bool uMInusError1() = unexpectedType("- true;");

public test bool timesError1() = unexpectedType("3 * true;");

public test bool divError1() = unexpectedType("3 / true;");
  	
public test bool modError1() = unexpectedType("3 % true;");

public test bool orError1() = unexpectedType("3 || true;");

public test bool addError1() = unexpectedType("3 + true;");

public test bool addError1() = unexpectedType("3 + true;");


public test bool UndefinedLocationError11() = uninitialized("loc Loc; Loc.url;");

public test bool UndefinedLocationError21() = uninitialized("loc Loc; Loc.url = \"abc\";");

public test bool UndefinedLocationError31() = uninitialized("loc Loc; Loc[url = \"abc\"];");

  	
public test bool WrongLocFieldError11() = undefinedField("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc.bla;");
  	
public test bool WrongLocFieldError21() = undefinedField("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc[bla=3];");
  	
public test bool URLFieldError11() = unexpectedType("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc.uri=true;");

//public test bool URLFieldError21() = parseError("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc.uri=\"? ??\";");

public test bool LengthFieldError1() = unexpectedType("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc.length=true;");

public test bool OffsetFieldError1() = unexpectedType("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc.offset=true;");

 
public test bool BeginLineFieldError1() = undefinedField("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc.beginLine=true;");
 
public test bool EndLineFieldError1() = unexpectedType("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc.end=\<true,12\>;");
  	
public test bool BeginColumnFieldError1() = unexpectedType("loc Loc = |file:///home/paulk/pico2.trm|(0,1,\<1,4\>,\<4,5\>); Loc.begin=\<true,12\>;");

public test bool SubscriptError21() = undeclaredVariable("L[5];");

public test bool UndefinedSetElementError1() = undeclaredVariable("{X};");
  	
public test bool inError1() = unexpectedType("1 in 3;");
 
public test bool productError1() = unexpectedType("{1,2,3} * true;");

public test bool tupleError11() = undefinedField("tuple[int key, str val] T = \<1, \"abc\"\>; T.zip == \"abc\";");

public test bool tupleError21() = uninitialized("tuple[int key, str val] T; T.key;");
	
public test bool UndefinedDataTypeAccess11() = uninitialized("someD.ival", initialDecls= ["data D = d(int ival);"]);

public test bool UndefinedDataTypeAccess21() = uninitialized("someD.ival = 3", initialDecls= ["data D = d(int ival);"]);

public test bool UndefinedMapElementError11() = undeclaredVariable("(X:2);");
  	
public test bool UndefinedMapElementError21() = undeclaredVariable("(1:Y);");

public test bool UndefinedTupleElementError11() = undeclaredVariable("\<1,X,3\>;");
  	   
public test bool UndefinedRelationElementError11() = undeclaredVariable("{\<1,10\>, \<X,20\>};");
  	    
public test bool UndefinedRelationElementError21() = undeclaredVariable("{\<1,10\>, \<10, Y\>};");
  	
public test bool UndefinedRelationElementError31() = undeclaredVariable("{\<1,10\>, T, \<3, 30\>};");

public test bool compError1() = unexpectedType("1 o 3;");
  	
public test bool closError11() = unexpectedType("1*;");
 
public test bool closError21() = unexpectedType("1+;");
  
public test bool namedRelationError1() = undefinedField("rel[int from, int to] R = {\<1,10\>, \<2,20\>}; R.zip == {10,20};");
 
public test bool undefined1() = uninitialized("int T; (T ? 13) == 13;");
 
public test bool remainder1() = unexpectedType("num n = 5; n % 2 == 1;"); 

public test bool remainder2() = unexpectedType("num n = 2; 5 % n == 1;");  		
  