module lang::rascalcore::compile::Examples::Tst1


data D = d(int i, int j = 0);
node n0 = d(1, j = 2);
node n1 = d(3);