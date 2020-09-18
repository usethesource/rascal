module lang::rascal::tests::imports::Imports9

// Note: import is only used when running tests with interpreter
import lang::rascal::tests::imports::M9;

test bool Test91() { 
	int f(3) = 3;
	return f(3) == 3;
}

@ignoreCompiler{INCOMPATIBILITY: Overloading of functions across scopes no longer supported}
test bool Test92() { 
	int f(3) = 30;
	return f(1) == 1 && f(3) == 30;
}

@ignoreCompiler{INCOMPATIBILITY: Overloading of functions across scopes no longer supported}
test bool Test93() { 
	int f(3) = 300;
	return f(5) == 50 && f(3) == 300;
}

test bool Test94() { 
	int f(3) = 3000;
	default int f(int n) = 20 * n;
	return f(5) == 100;
}

@ignoreCompiler{INCOMPATIBILITY: Overloading of functions across scopes no longer supported}
test bool Test95() { 
	int f(3) = 3;
	default int f(int n) = 20 * n;
	
	int g(int n){
		int f(4) = 44;
		default int f(int n) = 15 * n;
		
		return f(n);
	}
	
	int h(int n){
		int f(4) = 48;
		default int f(int n) = 20 * n;
		
		return f(n);
	}
	return f(1) == 1 && f(3) == 3 && f(5) == 100 && g(4) == 44 && g(5) == 75 && h(4) == 48 && h(5) == 100;
}

