module lang::rascal::checker::tests::Nested

public int f1() {
	int x = 3;
	
	int f2() {
		return x + y;
	}
	
	int y = 4;
	
	return f2();
}
