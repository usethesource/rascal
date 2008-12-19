module Squares

public void main(list[str] argv){
  int N = toInt(argv[0]);
  map[int,int] squares = {};
  for(int I : [1 .. N]){
     squares[I] = I * I;
  }
  println("Table of squares from 1 to ", N);
  println(squares);
}