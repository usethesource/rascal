module lang::rascalcore::compile::Examples::Tst1

import ValueIO;
import IO;

data D(bool K = false) = d1(int n);

void main(){
    v = readBinaryValueFile(#D, |home:///tmp.txt|);
    println("K = <v.K>, <v.K?>");
}