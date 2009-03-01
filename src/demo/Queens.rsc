module Queens

import Integer;
import List;
import IO;

/*
 * Experiments with the 8-queens puzzle.
 */

/*
 * Version 1: A standard recursive solution for arbitrary N that uses
 * a map to represent the board.
 */

public bool consistent(map[int,int] B, int n)
{
    return all(int i <- [1, 2 .. n - 1], B[i] != B[n] && abs(B[i] - B[n]) != abs(n - i));
}

int N = 8;

public void q(map[int,int] B, int row)
{
   if(row > N)
   	  println(B);
   else {
      for(int col <- [1, 2 .. N]){
          B[row] = col;
   	      if(consistent(B, row)){
     	     	  q(B, row + 1);
   	      }
      }
   }
}

public void queens1()
{
	q((), 0);
}

/*
 * Versions 2 & 3 share the same consistent function. It has a list
 * of integers as argument that represents the board.
 */

public bool consistent(int B...)
{ 
  bool res =  all(int i <- domain(B), int j <- domain(B),
      	          i != j ==> (B[i] != B[j] && abs(B[i] - B[j]) != abs(j - i))); 
  //println("consistent(<B>) = <res>");
  return res;		
}

/*
 * Version 2: uses generators and is for fixed length 8.
 * The "consistent" calls can be omitted (except the last one), but they
 * speed up the computation since they prune many false alternatives.
 */

public void queens2()
{
   list[int] R = [1 .. 8];
   int nsolutions = 0;
   
   for(int Q1 <- R,
       int Q2 <- R, consistent(Q1, Q2),
       int Q3 <- R, consistent(Q1, Q2, Q3),
       int Q4 <- R, consistent(Q1, Q2, Q3, Q4),
       int Q5 <- R, consistent(Q1, Q2, Q3, Q4, Q5),
       int Q6 <- R, consistent(Q1, Q2, Q3, Q4, Q5, Q6),
       int Q7 <- R, consistent(Q1, Q2, Q3, Q4, Q5, Q6, Q7),
       int Q8 <- R, consistent(Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8)
       ){
        nsolutions = nsolutions + 1;
     	println(Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8);
   }
   println("<nsolutions> solutions");
}

/*
 * Version 3: Use a variable argument to represent the board.
 */
public int queens3(int B ...)
{
  int nsolutions = 0;
  if(size(B) == N){
     println(B);
     nsolutions = nsolutions + 1;
  } else {
     for(int Q <- [1 .. N], consistent(B + Q))
         nsolutions = nsolutions + queens3(B + Q);
  }
  return nsolutions;
}

public bool test()
{
   N = 5;
   return queens3() == 10;
}
