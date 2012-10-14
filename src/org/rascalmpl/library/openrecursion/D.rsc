module openrecursion::D

import IO;

public int fib(0) = 0;
public int fib(1) = 1;
public default int fib(int n) = fib(n-1) + fib(n-2);

public int fact(0) = 1;
public int fact(1) = 1;
public default int fact(int n) = n*fact(n-1);

public int f1(0) = 1;
public int f1(1) = 1;
public default int f1(int n) = n + f1(n-1);

public int printIt(int n) { println("n == <n>"); return n; }
