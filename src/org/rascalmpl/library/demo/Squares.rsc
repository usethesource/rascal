module demo::Squares

import IO;

// Print a table of squares

public void squares(int N){
  println("Table of squares from 1 to <N>");
  for(int I <- [1 .. N]){
      I2 = I * I;
      println("<I> squared = <I2>");
  } 
}

