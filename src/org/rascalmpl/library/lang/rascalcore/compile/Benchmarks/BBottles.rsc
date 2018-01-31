module lang::rascalcore::compile::Benchmarks::BBottles

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
  
value main() {
  for(int i <- [0 .. 200]){
	sing();
  }
  return 0;
}
