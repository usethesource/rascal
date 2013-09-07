module experiments::Compiler::Examples::Overloading

import IO;

int f(0) = -1;
int f(int n) = n;
int f("0") = -2;
int f(str s) = -3;
int f(int n, str s) = -4;

value main(list[value] args) {
	x = f(0);
	y = f(5);
	k = f("0");
	l = f("5");
	z = f(0,"1");
	return z;
}