# Program to compute Fibonacci


from time import clock;

def fib(n):
  if n == 0:
    return 0
  elif n == 1:
    return 1
  else:
    return fib(n-1) + fib(n-2)


def measure():
  start = clock();
  for i in range(1,100):
      fib(20);
  print "100 x fib(20) used %f seconds\n" % (clock() - start)

measure();
