module experiments::Compiler::Examples::E1E2

public int d3 = 1;

public int inc(int n) = n + 1;
public int fac(int n) = (n <= 1) ? 1 : n * fac(n - 1);

public data EDATA = e1(int i) | e2(str s);
		
public EDATA main(list[value] args) {
	int n1 = fac(10);
	int n2 = inc(10);
	
	type[EDATA] t = #EDATA;
	EDATA v = e1(n1 + n2 + d3);
	
	return v;
}

bool testE1E2() = main([]) == e1(3628812);
