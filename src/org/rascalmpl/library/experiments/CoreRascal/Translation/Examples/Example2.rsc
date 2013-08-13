module experiments::CoreRascal::Translation::Examples::Example2

public int d3 = 1;

public int inc(int n) = n + 1;
public int fac(int n) = (n <= 1) ? 1 : n * fac(n - 1);
		
public int main(list[value] args) {
	int n1;
	n1 = fac(10);
	int n2;
	n2 = inc(10);
	return n1 + n2 + d3;
}
