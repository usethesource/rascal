module experiments::Compiler::Examples::Tst2

test bool all17()  = all(int i <- [0, 1] && [0, 1][i] == i);
  		
		// The following were all asserTrue, how can this have worked?
  		
@ignore{Changed semantics}
test bool all18()  = !(all(_ <- []));

//int f(0) = -1;
//
//default int f(int n) = n;
//
//public value main(list[value] args) {
//	return f(0);
//}