module experiments::Compiler::Examples::Tst1
import ValueIO;
import IO;
import util::Reflective;


lexical Id = [a-z][a-z0-9]*;
syntax A = Id;
syntax As = {A ","}+;



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
    t = parseNamedModuleWithSpaces("experiments::Compiler::Examples::Tst2");
    iprintln(t);
    writeBinaryValueFile(|test-temp:///parsetree1|, t);
    r = readBinaryValueFile(|test-temp:///parsetree1|);
    println(diff(t,r));
    return r == t;
}




value main(){
    t = [As] "a,a";
    writeBinaryValueFile(|test-temp:///parsetree1|, t);
    r = readBinaryValueFile(|test-temp:///parsetree1|);
    println(diff(t,r));
    return r == t;
}
