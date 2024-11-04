module lang::rascalcore::compile::Examples::Tst0

import ValueIO;
import IO;

data D = d1(int n);

void main(){
    v = d1(42);
    writeBinaryValueFile(|home:///tmp.txt|, v);
    println("written <v>");
}