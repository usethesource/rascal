# Program to find the factorial of a number


from time import clock;

def fact(n):
  if n == 0:
    return 1
  else:
    return n * fact(n-1)


def measure():
  start = clock();
  for i in range(1,10000):
      fact(500);
  print "10000 x fac(500) used %f seconds\n" % (clock() - start)

measure();
