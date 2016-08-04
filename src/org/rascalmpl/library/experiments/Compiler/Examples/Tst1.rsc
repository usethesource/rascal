module experiments::Compiler::Examples::Tst1
import ValueIO;
import IO;
import util::Reflective;

syntax As = "a"*;

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
     nd = [As] "aaaa";
    writeBinaryValueFile(|test-temp:///parsetree1|, nd);
    r = readBinaryValueFile(|test-temp:///parsetree1|);
    println(nd);
    println(r);
    return r == nd;
}
