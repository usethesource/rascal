module lang::rascalcore::compile::Examples::Tst2

data L = intNode(tuple[int,str] n) | fork(list[L] left);



tuple[int,str] f(intNode(n)) = n;

//int f( fork(list[L] left)) = 0;