module experiments::Compiler::Examples::Tst3

//import ValueIO;
//import IO;

@doc{Read a typed value from a text file.}
@javaClass{org.rascalmpl.library.Prelude}
@reflect{Uses URI Resolver Registry}
public java &T readTextValueFile(type[&T] result, loc file);

public value readTextValueFile(loc file) {
  return readTextValueFile(#value, file);
}

   
value main(list[value] args) =  readTextValueFile(|file:///tmp/xxx|); // textWriteRead(#int, 1);
   

//private bool textWriteRead(type[&T] typ, value exp) {
//   writeTextValueFile(|file:///tmp/xxx|,exp);
//   println("Wrote <exp>");
//   if (&T N := readTextValueFile(|file:///tmp/xxx|) && N == exp) return true;
//   return false;
//   }
   
