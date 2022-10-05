@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module demo::vis::VisADT

//import vis::Figure;
//import vis::Render;
//
//data ColoredTree = leaf(int N)
//                 | red(ColoredTree left, ColoredTree right) 
//                 | black(ColoredTree left, ColoredTree right)
//                 | green(ColoredTree left, ColoredTree right)
//                 ;
//
//Figure visColoredTree(leaf(int N)) = 
//	box(text("<N>"), gap(2), fillColor("lightyellow")); // <1>
//
//Figure visColoredTree(red(ColoredTree left, ColoredTree right)) = 
//	visNode("red", left, right); // <2>
//
//Figure visColoredTree(black(ColoredTree left, ColoredTree right)) = 
//	visNode("black", left, right);
//
//Figure visColoredTree(green(ColoredTree left, ColoredTree right)) = 
//	visNode("green", left, right);
//
//Figure visNode(str color, ColoredTree left, ColoredTree right) = // <3>
//	tree(ellipse(fillColor(color)), [visColoredTree(left), visColoredTree(right)]);
//
//ColoredTree  rb = red(black(leaf(1), red(leaf(2),leaf(3))), green(leaf(3), leaf(4)));
