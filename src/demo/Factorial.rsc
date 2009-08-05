module demo::Factorial
import UnitTest;

// The factorial function N! = N * (N-1) * (N-2) * ... * 1;

public int fac(int N)
{
  return N <= 0 ? 1 : N * fac(N - 1);
}

// Observe that the integers in Rascal can have arbitrary size

public bool test(){
  assertEqual(fac(47), 258623241511168180642964355153611979969197632389120000000000);
  report("Factorial");
}