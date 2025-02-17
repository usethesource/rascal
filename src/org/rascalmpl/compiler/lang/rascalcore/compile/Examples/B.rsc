module  lang::rascalcore::compile::Examples::B
import IO;
import lang::rascalcore::compile::Examples::A;
  
void printB() {
    println("B");
}

void printBA() {
    printB();
    printA();
}