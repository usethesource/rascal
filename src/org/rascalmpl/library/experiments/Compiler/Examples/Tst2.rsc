module experiments::Compiler::Examples::Tst2

//list[int] ints = [0 .. 10];

//value main(list[value] args) = ints;

//set[int] ints = { x | x <- [1,2,3,4]};

value ints = {int a = 10; int b = 20; int c = 30; int d = 3; a+b+c+d;};

value main(list[value] args) { int z = 100; ints;}
