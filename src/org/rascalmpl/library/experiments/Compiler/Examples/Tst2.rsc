module experiments::Compiler::Examples::Tst2

   
    
import Traversal;
import IO;

void doit() {
    l = [1,"one",2,"two",3,"three"];
    bottom-up visit(l) {
        case int n : println("<n> is an int");
        case value x:  { println("No case for <x>"); }
    }
}

value main(list[value] args) { doit(); return true;}