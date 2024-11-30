module lang::rascalcore::compile::Examples::Tst0

import ValueIO;
import Reflective;

data D = d1(int n);

void main(){
    v = d1(42);
    writeBinaryValueFile(|home:///tmp.txt|, v);
    println("written <v>");
}