module experiments::Compiler::Examples::Tst4

extend experiments::Compiler::Tests::TestUtils;

//test bool tst() = run("x = 7" , "switch(0){case 0: x = 0; case 1: x = 1; default: x = 2;}") == sw(0);


value main(list[value] args) = run("x = 7" , "switch(0){case 0: x = 0; case 1: x = 1; default: x = 2;}") ;