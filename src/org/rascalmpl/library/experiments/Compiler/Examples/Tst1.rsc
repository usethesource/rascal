module experiments::Compiler::Examples::Tst1

//import lang::rascal::tests::types::StaticTestingUtils;
import IO;

value main(list[value] args) {


int x;

x = x ? 1;
x = x ? 2;


return x == 1;
}