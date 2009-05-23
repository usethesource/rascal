module demo::ColoredTrees

data ColoredTree = leaf(int N) 
          | red(ColoredTree left, ColoredTree right) 
          | black(ColoredTree left, ColoredTree right);
          
public int cntRed(ColoredTree t){
   int c = 0;
   visit(t) {
     case red(_,_): c = c + 1;
   };
   return c;
}

public int addLeaves(ColoredTree t){
   int c = 0;
   visit(t) {
     case leaf(int N): c = c + N;
   };
   return C;
}

data ColoredTree = green(ColoredTree left, ColoredTree right);

public ColoredTree makeGreen(ColoredTree t){
   return visit(t) {
     case red(l, r) => green(l, r);      
   };
}

public void test(){
  rb = red(black(leaf(1), red(leaf(2),leaf(3))));
  assert cntRed(rb) == 2;
  assert addLeaves(rb) == 6;
  assert makeGreen(rb) == green(black(leaf(1), green(leaf(2),leaf(3))));
  return true;
}