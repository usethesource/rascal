module demo::Factorial

public int fac(int N)
{
  return N <= 0 ? 1 : N * fac(N - 1);
}

public void test(){
  return fac(47) == 258623241511168180642964355153611979969197632389120000000000;
}