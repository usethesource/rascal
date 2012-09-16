module FixPoint

import IO;

@doc{Call-by-value version of fix point combinator}
public &F (&V) fix( ( &F (&V) ) ( &F (&V) ) f ) = &F (&V v) { return f(fix(f))(v); };

@doc{Fibonacci function reformulated using a fix point combinator}
public int fibfix(int n) = fix(Ffib)(n);
@doc{Factorial function reformulated using a fix point combinator}
public int factfix(int n) = fix(Ffact)(n);

@doc{Kinda partially evaluated Fibonacci and Factorial functions}
public str pfibfix(int n) = fix(Fpfib)(n);
public str pfactfix(int n) = fix(Fpfact)(n);

@doc{Fibonacci function extended with the println functionality}
public int fibprintfix(int n) = fix( int (int) (int (int) f) { return Fprint1(Ffib(f)); } )(n);
@doc{Factorial function extended with the println functionality}
public int factprintfix(int n) = fix( int (int) (int (int) f) { return Fprint1(Ffact(f)); } )(n);

@doc{Anonymous Fibonacci and Factorial functions}
public void anonymousFib(int n) {
	println("anonymous fib (<n>): <fix(
		int (int) (int (int) f) { return
			int (int n) { 
				switch(n) {
	 				case 0: return 0;
	 				case 1: return 1; 
	 				default: return f(n-1) + f(n-2);
				}
			};
		}
	)(n)>");
}
public void anonymousFact(int n) {
	println("anonymous fact (<n>): <fix(
		int (int) (int (int) f) { return
			int (int n) { 
				switch(n) {
	 				case 0: return 1;
	 				case 1: return 1; 
	 				default: return n*f(n-1);
				}
			};
		}
	)(n)>");
}

// @doc{
//	public int fib(1) = 0;
//	public int fib(0) = 1;
//	public default int fib(int n) = fib(n-1) + fib(n-2);
// }
public int ffib(int (int) fib, 0) = 0;
public int ffib(int (int) fib, 1) = 1;
public default int ffib(int (int) fib, int n) = fib(n-1) + fib(n-2);
public int (int) Ffib(int (int) fib) = int (int n) { 
	return ffib(fib, n);
};

// @doc{
//	public int fact(1) = 1;
//	public int fact(0) = 1;
//	public default int fact(int n) = n*fact(n-1);
// }
public int ffact(int (int) fact, 1) = 1;
public int ffact(int (int) fact, 0) = 1;
public default int ffact(int (int) fact, int n) = n*fact(n-1);
public int (int) Ffact(int (int) fact) = int (int n) { 
	return ffact(fact, n);
};

// @doc{
//	public str pfact1(1) = "1";
//	public str pfact1(0) = "1";
//	public default str pfact1(int n) = "<n>" + " * <pfact1(n-1)>";
// }
public str fpfact(str (int) pfact, 1) = "1";
public str fpfact(str (int) pfact, 0) = "1";
public default str fpfact(str (int) pfact, int n) = "<n>" + " * <pfact(n-1)>";
public str (int) Fpfact(str (int) pfact) = str (int n) { 
	return fpfact(pfact, n);
};

// @doc{
//	public str pfib1(1) = "0";
//	public str pfib1(0) = "1";
//	public default str pfib1(int n) = "<pfib1(n-1)>" + " + <pfib1(n-2)>";
// }
public str fpfib(str (int) pfib, 0) = "0";
public str fpfib(str (int) pfib, 1) = "1";
public default str fpfib(str (int) pfib, int n) = "<pfib(n-1)>" + " + <pfib(n-2)>";
public str (int) Fpfib(str (int) pfib) = str (int n) { 
	return fpfib(pfib, n); 
};
// @doc{
//	public int print(int n) { println("<n>"); return f(n); }
// }
public int fprint(int (int) f, int n) { int res = f(n); println("<res>"); return res; }
public int (int) Fprint(int (int) f) = int (int n) {
	return fprint(f, n);
};

public int fprint1(int n) { println("<n>"); return n; }
public int (int) Fprint1(int (int) f) = int (int n) {
	return fprint1(f(n));
};
