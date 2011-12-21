module demo::vis::VisADT

import vis::Figure;
import vis::Render;

data ColoredTree = leaf(int N)
                 | red(ColoredTree left, ColoredTree right) 
                 | black(ColoredTree left, ColoredTree right)
                 | green(ColoredTree left, ColoredTree right)
                 ;

public Figure visColoredTree(leaf(int N)) = 
	box(text("<N>"), gap(2), fillColor("lightyellow"));                    /*1*/

public Figure visColoredTree(red(ColoredTree left, ColoredTree right)) = 
	visNode("red", left, right);                                           /*2*/

public Figure visColoredTree(black(ColoredTree left, ColoredTree right)) = 
	visNode("black", left, right);

public Figure visColoredTree(green(ColoredTree left, ColoredTree right)) = 
	visNode("green", left, right);

public Figure visNode(str color, ColoredTree left, ColoredTree right) =     /*3*/
	tree(ellipse(fillColor(color)), [visColoredTree(left), visColoredTree(right)]);

public ColoredTree  rb = red(black(leaf(1), red(leaf(2),leaf(3))), green(leaf(3), leaf(4)));