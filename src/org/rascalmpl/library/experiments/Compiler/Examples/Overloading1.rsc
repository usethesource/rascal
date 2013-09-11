module experiments::Compiler::Examples::Overloading1

public int f(0) = -1;
public int f(int n) = n;

public int f("0") = -2;
public int f(str s) = -3;

public int f(int n, str s) = -4;

public value main(list[value] args) {
	x = f(0);
	y = f(5);
	k = f("0");
	l = f("5");
	z = f(0,"1");
	return x + k;
}