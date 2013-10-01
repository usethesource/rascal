module experiments::Compiler::Examples::Capture

public int (int) f() { int n = 100; return int (int i) { return i + n; }; }

public int (int) h(int n1) { int n2 = 50; int k(int i) { return n1 + n2 + i; } return k; } 

public value main(list[value] args) {
	g = f();
	res1 = g(11);
	
	l = h(1);
	res2 = l(2);
	
	return res1 + res2; // 111 + 53 == 164
}
