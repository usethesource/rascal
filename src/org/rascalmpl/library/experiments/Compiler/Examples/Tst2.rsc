module experiments::Compiler::Examples::Tst2


public default int f(int n) = n;

public int f(0) = -1;




public value main(list[value] args) {
	x = f(0);
//	y = f(5);
	return x;
}