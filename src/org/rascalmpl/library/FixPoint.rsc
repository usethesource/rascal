module FixPoint

import IO;

@doc{Call-by-value version of fix point combinator}
public &F (&V) fix( ( &F (&V) ) ( &F (&V) ) f ) = &F (&V v) { return f(fix(f))(v); };

@doc{Generator}
public &F (&V) (&F (&V)) FGen(&F (&F (&V), &V) f) = &F (&V) (&F (&V) f1) { return &F (&V v) { return f(f1, v); }; };

@doc{Composition}
public &V2 (&V1) (&V2 (&V1)) o(&V2 (&V2 (&V1), &V1) f1, &V2 (&V2 (&V2), &V2) f2) 
			= &V2 (&V1) (&V2 (&V1) f) { return FGen(f2)(FGen(f1)(f)); };
public &V2 (&V1) (&V2 (&V1)) o(&V2 (&V1) (&V2 (&V1)) f1, &V2 (&V2 (&V2), &V2) f2) 
			= &V2 (&V1) (&V2 (&V1) f) { return FGen(f2)(f1(f)); };

@doc{Fibonacci function reformulated using a fix point combinator}
public int fibfix(int n) = fix(FGen(ffib))(n);
@doc{Factorial function reformulated using a fix point combinator}
public int factfix(int n) = fix(FGen(ffact))(n);

@doc{Kinda partially evaluated Fibonacci and Factorial functions}
public str pfibfix(int n) = fix(FGen(fpfib))(n);
public str pfactfix(int n) = fix(FGen(fpfact))(n);

@doc{Fibonacci function extended with the println functionality}
public int fibprintfix(int n) = fix( o(ffib, fprint1) )(n);
@doc{Factorial function extended with the println functionality}
public int factprintfix(int n) = fix( o(ffact, fprint1) )(n);

@doc{Weird Factorial function extended with the println functionality: fact(n) = (n+1)*fact(n-1);}
public int factweird(int n) = fix( o( o(ffact, ffactweird), fprint1) )(n);

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

// @doc{
//	public int fact(1) = 1;
//	public int fact(0) = 1;
//	public default int fact(int n) = n*fact(n-1);
// }
public int ffact(int (int) fact, 1) = 1;
public int ffact(int (int) fact, 0) = 1;
public default int ffact(int (int) fact, int n) = n*fact(n-1);

// @doc{
//	public str pfact1(1) = "1";
//	public str pfact1(0) = "1";
//	public default str pfact1(int n) = "<n>" + " * <pfact1(n-1)>";
// }
public str fpfact(str (int) pfact, 1) = "1";
public str fpfact(str (int) pfact, 0) = "1";
public default str fpfact(str (int) pfact, int n) = "<n>" + " * <pfact(n-1)>";

// @doc{
//	public str pfib1(1) = "0";
//	public str pfib1(0) = "1";
//	public default str pfib1(int n) = "<pfib1(n-1)>" + " + <pfib1(n-2)>";
// }
public str fpfib(str (int) pfib, 0) = "0";
public str fpfib(str (int) pfib, 1) = "1";
public default str fpfib(str (int) pfib, int n) = "<pfib(n-1)>" + " + <pfib(n-2)>";
// @doc{
//				public int print(int n) { println("<n>"); return f(n); }
//public int f(int n) { println("<n>"); return super(n); }
// }
public int fprint(int (int) f, int n) { int res = f(n); println("<res>"); return res; }
public int (int) (int (int)) Fprint = FGen(fprint);

public int print1(int n) { println("<n>"); return n; }
public int fprint1(int (int) f, int n) = print1(f(n));

// @doc{
//@override_open public int fact(int n) = fact(n-1) + super(n);
// }
public int ffactweird1(int (int) super, int (int) fact, 0) = 1;
public int ffactweird1(int (int) super, int (int) fact, 1) = 1;
public default int ffactweird1(int (int) super, int (int) fact, int n) = fact(n-1) + super(n);
public int ffactweird(int (int) super, int n) = fix(FGen(int (int (int) fact, int n) { return ffactweird1(super, fact, n); }))(n);

// @doc{
//	public int fib(1) = 0;
//	public int fib(0) = 1;
//	public default int fib(int n) = fib(n-1) + fib(n-2);
// }
public int ffibb(int (int) fib, 0) = 0;
public int ffibb(int (int) fib, 1) = 1;
public default int ffibb(int (int) fib, int n) { if(n%2 == 0) return fib(n-1) + fib(n-2); else fail; }

public int fibb(0) = 0;
public int fibb(1) = 1;
public default int fibb(int n) { if(n%2 == 0) return fibb(n-1) + fibb(n-2); else fail; }

// @doc{
//	public int fact(1) = 1;
//	public int fact(0) = 1;
//	public default int fact(int n) = n*fact(n-1);
// }
public int factt(1) = 1;
public int factt(0) = 1;
public default int factt(int n) { if(n%2 != 0) return n*factt(n-1); else fail; }

public int plus(int (int) f1, int (int) f2, int n) = f1(n);
public default int plus(int (int) f1, int (int) f2, int n) = f2(n);

