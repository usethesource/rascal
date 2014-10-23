module lang::rascal::checker::tests::AP1

// Compute a set of of cubes of odd numbers
public set[int] cubes(int n) = {i * i * i | i <- [0  .. n], i % 2 != 0 };

data ColoredTree = leaf(int N)
                 | red(ColoredTree left, ColoredTree right) 
                 | black(ColoredTree left, ColoredTree right);
          
// Count the number of black nodes (using switch and recursion)
          
public int cntBlack1(ColoredTree t){
   switch(t) {
     case leaf(_):    	return 0;
     case black(l,r): 	return 1 + cntBlack1(l) + cntBlack1(r);
     case red(l,r): 	return cntBlack1(l) + cntBlack1(r);
   };
}

// Count the number of black nodes (using visit)

public int cntBlack2(ColoredTree t){
   int c = 0;
   visit(t) {
     case black(_,_): c = c + 1;
   };
   return c;
}

// Aux function to count red nodes.

public int cntRed(ColoredTree t){
   int c = 0;
   visit(t) {
     case red(_,_): c = c + 1;
   };
   return c;
}

// Check whether there are more black then red nodes

public bool moreBlack(ColoredTree t) = cntBlack1(t) > cntRed(t);

// Alternative solution in one function:

public bool moreBlack2(ColoredTree t){
   int cb = 0;
   int cr = 0;
   visit(t) {
     case red(_,_): cr = cr + 1;
     case black(_,_): cb = cb + 1;
   };
   return cb > cr;
}

// Prime numbers

bool divides(int a, int b) = b mod a == 0;

public bool isPrime(int n) = !any(i <- [2 .. n], divides(i, n));

public list[int] primes(int max) = [ i | i <- [2 .. max], isPrime(i) ];

// More efficient version using Sieve of Eratosthenes 

bool isPrime(int a, list[int] prevPrimes) = !any(p <- prevPrimes, divides(p,a)); 

public list[int] primeSieve(int max) {
  plist = [];
  for( i <- [2 .. max]){
    if(isPrime(i, plist))
       plist += [i];
  }
  return plist;
}

// A very dense version of the same:

// Example of reducer:

public void reduce1(){
	L = [1, 3, 5, 7];
    N = (0 | it + e | int e <- L);
    M = (1 | it * e | int e <- L);
}

// Sieve defined using a reducer:

public list[int] primeSieve2(int max) = ([] | isPrime(i, it) ? it + [i] : it | i <- [2..max]);
