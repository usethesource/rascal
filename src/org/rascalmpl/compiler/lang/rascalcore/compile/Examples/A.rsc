module  lang::rascalcore::compile::Examples::A
     
import IO;
import lang::rascalcore::compile::Examples::B;
  
void printA() {
    println("A");
}

void printAB() {
    printA();
    printB();
}