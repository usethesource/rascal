module openrecursion::G

import IO;

public int fib(0) = 0;
public int fib(1) = 1;
public default int fib(int n) = fib(n-1) + fib(n-2) + fib(n-1, n-2);

public int fib(int i, int j) = i+j;

public int printIt(int n) { println("n == <n>"); return n; }


public int f(int (int) id, int i) = id(i,i);
public int id(int i, int j) = i+j;


