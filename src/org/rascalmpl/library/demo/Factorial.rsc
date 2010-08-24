module demo::Factorial

// The factorial function N! = N * (N-1) * (N-2) * ... * 1;

public int fac(int N)
{
  return N <= 0 ? 1 : N * fac(N - 1);
}