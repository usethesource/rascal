module experiments::Compiler::Rascal2muRascal::Examples::Example2

public int d3 = 1;

public int inc(int n) = n + 1;
public int fac(int n) = (n <= 1) ? 1 : n * fac(n - 1);

public data DATA = d1(int i) | d2(str s);
		
public DATA main(list[value] args) {
	int n1 = fac(10);
	int n2 = inc(10);
	
	DATA v = d1(n1 + n2 + d3);
	
	return v;
}
