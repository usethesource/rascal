//@bootstrapParser
module lang::rascalcore::compile::Examples::Tst2


import Type;

@doc{
#### Synopsis

Read  a value from a binary file in PBF format.
}
public value readValueFile(loc file) {
  return readBinaryValueFile(#value, file);
}

@doc{
#### Synopsis

Get length of a file in number of bytes.
}
@javaClass{org.rascalmpl.library.Prelude}
public java int getFileLength(loc file);

@doc{
#### Synopsis

Read a typed value from a binary file.
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T readBinaryValueFile(type[&T] result, loc file);

public value readBinaryValueFile(loc file) {
  return readBinaryValueFile(#value, file);
}

@doc{
#### Synopsis

Read a typed value from a text file.
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T readTextValueFile(type[&T] result, loc file);

public value readTextValueFile(loc file) {
  return readTextValueFile(#value, file);
}

@doc{
#### Synopsis

If you have written a file containing reified types, then you can use this function
  to read them back.  
}
public &T readTextValueFileWithEmbeddedTypes(type[&T] result, loc file) {
  return readTextValueFile(type(result.symbol, result.definitions + #Symbol.definitions + #Production.definitions), file);
}

@doc{
#### Synopsis

Parse a textual string representation of a value.
}
public value readTextValueString(str input) {
  return readTextValueString(#value, input);
}

@doc{
#### Synopsis

Parse a textual string representation of a value and validate it against the given type.
}
@javaClass{org.rascalmpl.library.Prelude}
public java &T readTextValueString(type[&T] result, str input);
    
@doc{
#### Synopsis

Write a value to a file using an efficient binary file format.
}
@javaClass{org.rascalmpl.library.Prelude}
public java void writeBinaryValueFile(loc file, value val, bool compression = true);
    
@doc{
#### Synopsis

Write a value to a file using a textual file format.
}
@javaClass{org.rascalmpl.library.Prelude}
public java void writeTextValueFile(loc file, value val);


 
//data H1[&T] = h1(&T n);
//data H2[&\T] = h2(&T n);
//data H3[&\T] = h3(&\T n);
//data H4[&T] = h4(&\T n);

//test bool escapedTypeParameter1() { H1[int] x = h1(3); return x.n == 3; }
//value main() //test bool escapedTypeParameter2() 
//    { H2[int] x = h2(3); return x.n == 3; }
//test bool escapedTypeParameter3() { H3[int] x = h3(3); return x.n == 3; }
//test bool escapedTypeParameter4() { H4[int] x = h4(3); return x.n == 3; }

//data Exp1[&T] = tval(&T tval) | tval2(&T tval1, &T tval2) | ival(int x);
//
////int f(Exp1[str] e) = 1;
//int f(Exp1[int] e) = 3;
//default int f(Exp1[&T] e) = 10;
// 
////test bool overload1() = f(tval("abc")) == 1;
////test bool overload2() = f(tval(1)) == 3;
//value main() //test bool overload3() 
//    = f(tval(true)) == 3;
 
//int f(int n) = n;
//    
//value main() //test bool node1() 
//  {
//    num x = 3.5;
//    return f(x);
//}
