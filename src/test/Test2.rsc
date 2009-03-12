module test::Test2

import IO;

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