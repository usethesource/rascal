module test::Test2

import IO;

/* Changes to global variables in combination with recursion does not seem to work
   Environment issue?
*/

list[int] G = [];

int f (int a){

  if(a == 0)
  	return 0;
  println("Before G=<G>");
  G = G + f(a - 1);
  println("After G=<G>");
  return a;
}

public void test(){
  f(4);
  println("Final G=<G>");
}

/* Gives as output:


test();
Before G=[]
Before G=[]
Before G=[]
Before G=[]
After G=[0]
After G=[1]
After G=[2]
After G=[3]
Final G=[3]   <-- Only the outermost change is visible
done.

*/

