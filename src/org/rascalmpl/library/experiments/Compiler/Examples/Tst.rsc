module experiments::Compiler::Examples::Tst

str bottles(0)     = "no more bottles"; 
str bottles(1)     = "1 bottle";
default str bottles(int n) = "<n> bottles"; 

public str sing() {
  return 
  "<for(n <- [2 .. 1]){>
  'xxx of beer on the wall, <111> of beer.
  'Take one down, pass it around, <bottles(n-1)> of beer on the wall
  'No more bottles of beer on the wall, no more bottles of beer.
  'Go to the store and buy some more, 99 bottles of beer on the wall.
  <}>";
}  
  
value main(list[value] args) = sing();