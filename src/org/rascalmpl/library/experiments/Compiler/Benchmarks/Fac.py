# Program to find the factorial of a number


from time import clock;

def fact(n):
  if n == 0:
    return 1
  else:
    return n * fact(n-1)


def measure():
  start = clock();
  for i in range(1,100000):
      fact(20);
  print "1000000 x fac(20) used %f seconds\n" % (clock() - start)

measure();
