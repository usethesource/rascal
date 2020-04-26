module lang::rascalcore::compile::Examples::Tst1

data D = d(int i, int j = 0);
node n1 = d(3);

value main() = !(n1 has j);


//=============
//set[int] g(set[set[int]] ns)
//    = { *x | st <- ns, {1, *int x} := st || {2, *int x} := st };