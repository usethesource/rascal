@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
//START
module demo::common::ColoredTrees

// Define ColoredTrees with red and black nodes and integer leaves

data ColoredTree = leaf(int N)      /*1*/
                 | red(ColoredTree left, ColoredTree right) 
                 | black(ColoredTree left, ColoredTree right);
          
// Count the number of red nodes
          
public int cntRed(ColoredTree t){
   int c = 0;
   visit(t) {
     case red(_,_): c = c + 1;      /*2*/
   };
   return c;
}

// Compute the sum of all integer leaves

public int addLeaves(ColoredTree t){
   int c = 0;
   visit(t) {
     case leaf(int N): c = c + N;   /*3*/
   };
   return c;
}

// Add green nodes to ColoredTree

data ColoredTree = green(ColoredTree left, ColoredTree right); /*4*/

// Transform red nodes into green nodes

public ColoredTree makeGreen(ColoredTree t){
   return visit(t) {
     case red(l, r) => green(l, r)   /*5*/
   };
}
