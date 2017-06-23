module experiments::Compiler::Examples::Tst1

int   fac(int n) = (n <= 1) ? 1 : n * fac(n-1);

real fac (real r) = r;

str bottles(0)     = "no more bottles"; 
str bottles(1)     = "1 bottle";
default str bottles(int n) = "<n> bottles"; 

public str sing() =
  "<for(n <- [99 .. 1]){>
  '<bottles(n)> of beer on the wall, <bottles(n)> of beer.
  'Take one down, pass it around, <bottles(n-1)> of beer on the wall
  'No more bottles of beer on the wall, no more bottles of beer.
  'Go to the store and buy some more, 99 bottles of beer on the wall.
  <}>";

value main() = 42;