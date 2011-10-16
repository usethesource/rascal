module demo::basic::Bottles

import IO;

str bottles(int n) { /*1*/
  switch(n){
    case 0: return "no more bottles";
    case 1: return "1 bottle";
    default: return "<n> bottles";
  }
}

public void sing(){ /*2*/
  for(n <- [99 .. 1]){
       println("<bottles(n)> of beer on the wall, <bottles(n)> of beer.");
       println("Take one down, pass it around, <bottles(n-1)> of beer on the wall.\n");
  }  
  println("No more bottles of beer on the wall, no more bottles of beer.");
  println("Go to the store and buy some more, 99 bottles of beer on the wall.");
}