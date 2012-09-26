module openrecursion::E

import openrecursion::D;

public int printedFib(int n) = (print o fib)(n);
public int printedFact(int n) = (print o fact)(n);

public int printedFibOpen(int n) = (print + fib)(n);
public int printedFactOpen(int n) = (print + fact)(n);

public int printedFibFact(int n) = (print o fact o fib)(n);
public int printedFactFib(int n) = (print o fact o fib)(n);

public int printedFibFactOpen(int n) = (print + fact + fib)(n);
public int printedFactFibOpen(int n) = (print + fact + fib)(n);