module Visiting::visiting

import IO;
import util::Math;
import util::Benchmark;

data ColoredTree =
     leaf(int n)
     | red(ColoredTree left, ColoredTree right)
     | black(ColoredTree left, ColoredTree right)
     | green(ColoredTree left, ColoredTree right)
     ;

public ColoredTree genTree(int leafChance, int minDepth,int maxDepth){
   if(maxDepth == 0 || minDepth <= 0 && toInt(arbReal() * 100.0) <= leafChance){ return leaf(arbInt()); }

   left = genTree(leafChance, minDepth-1, maxDepth -1);
   right = genTree(leafChance, minDepth-1, maxDepth -1);

   switch(arbInt(3)){
     case 0: return red(left,right);
     case 1: return black(left,right);
     default: return green(left,right);
   }
}

public tuple[int, int, int] countAll(ColoredTree t){
   cntRed = 0; cntBlack =0; cntGreen = 0;
   visit(t){
     case red(l,r) : cntRed += 1;
     case black(l,r) : cntBlack += 1;
     case green(l,r) : cntGreen += 1;   
   }
   return <cntRed, cntBlack, cntGreen>;
}

public ColoredTree allBlack(ColoredTree t){
   return 
   visit(t){
     case red(l,r) => black(l,r)
     case green(l,r) => black(l,r)
   };
}

public ColoredTree swapAll(ColoredTree t){
   return 
   visit(t){
     case red(l,r) => red(r,l)
     case black(l,r) => black(r,l)
     case green(l,r) => green(r,l)
   };
}

public void m(){
   N = 10;
   T = genTree(15, 10, 14);
   println("countAll for tree T = <countAll(T)>");
   begin1 = realTime();
   for(int j <- [1 .. N]){
       countAll(T);
   }
   used1 = (realTime() - begin1);
   println("countAll: <used1> millisec.");
   
   begin2 = realTime();
   for(int j <- [1 .. N]){
       allBlack(T);
   }
   used2 = (realTime() - begin2);
   println("allBlack: <used2> millisec.");
   
   
   begin3 = realTime();
   for(int j <- [1 .. N]){
       swapAll(T);
   }
   used3 = (realTime() - begin3);
   println("swapAll: <used3> millisec.");
}
