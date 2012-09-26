module openrecursion::D

import IO;

public int fib(0) = 0;
public int fib(1) = 1;
public default int fib(int n) = fib(n-1) + fib(n-2);

public int fact(0) = 1;
public int fact(1) = 1;
public default int fact(int n) = n*fact(n-1);

public int print(int n) { println("n == <n>"); return n; }