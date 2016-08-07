module experiments::Compiler::Examples::Tst1
import ValueIO;
import IO;
import util::Reflective;

syntax As = "a"*;

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

test bool writingParseTreeWorks() {
    t = parseNamedModuleWithSpaces("lang::rascal::syntax::Rascal");
    writeBinaryValueFile(|test-temp:///parsetree1|, t);
    return readBinaryValueFile(|test-temp:///parsetree1|) == t;
}

value main(){
    nd = [As] "aaa";
    writeBinaryValueFile(|test-temp:///parsetree1|, nd);
    r = readBinaryValueFile(|test-temp:///parsetree1|);
    println(nd);
    println(r);
    //println(binaryWriteRead(#num, nd));
    //return binaryWriteRead(#Bool, bor(btrue(), btrue()));
    //return binaryWriteRead(#Bool, band(bor(btrue(),bfalse()),band(btrue(),btrue())));
    return true;
}
