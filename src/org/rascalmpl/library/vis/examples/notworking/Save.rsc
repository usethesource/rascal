@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module vis::examples::Save

import vis::Figure;
import vis::Render;


public void a(){

	A = box(size(100), lineColor("black"), lineWidth(2), fillColor("violet"));
	B = ellipse(size(100), lineColor("black"), lineWidth(1), fillColor("yellow"));
	renderSave(hcat([A, B, A], gap(10)), |cwd:///tmp.png|);
}
/*
public void out1(){
  renderSave(box(size(100), lineColor("black"), lineWidth(1), fillColor("violet")),
                 |cwd:///tmp.png|
        );
}

// Sized outer box, with right-aligned inner box of 100x200
public void bbr(){
	renderSave(box( box(size(100,200), fillColor("green"), lineColor("red")),
	                width(150), height(300), gap(5,30), right()),
	           |cwd:///tmp.png|
	);
}


// Tree: 3 nodes
public void tree3(){

   nodes =
     	[ box(id("A"), fillColor("green")),
     	  box(id("B"), fillColor("red")),
     	  box(id("C"), fillColor("blue"))
     	];
     	
    edges = 
    	[ edge("A", "B"),
          edge("A", "C")
    	];
    	  
    //renderSave(tree(nodes, edges, gap(10), size(20), lineWidth(1)), |cwd:///tmp.png| );
}

// Tree

public void tree5c(){

   nodes =
     	[ box(id("A"), fillColor("green")),
     	  box(id("B"), fillColor("red")),
     	  box(id("C"), fillColor("blue")),
     	  box(id("D"), fillColor("purple")), 	
     	  box(id("E"), fillColor("lightblue")),
          box(id("F"), fillColor("orange")),
     	  box(id("G"), fillColor("brown")),
     	  box(id("H"), fillColor("black")),
     	  box(id("I"), fillColor("grey")),
     	  box(id("J"), fillColor("white")),
     	  box(id("K"), fillColor("deeppink"))
     	];
    edges = 
    	[ edge("A", "B"),
    	  edge("A", "C"),
    	  edge("A", "K"),
    	  edge("A", "D"),
    	  edge("B", "E"),
    	  edge("B", "F"),
    	  edge("B", "G"),
    	  edge("D", "H"),
    	  edge("D", "I"),
    	  edge("D", "J")
    	];
    	    
    renderSave(tree(nodes, edges, gap(10), top(), size(20), lineWidth(2)),
    |cwd:///tmp.png|
    );
}

public void K(int n){
    nodes = [box(id("<i>"), width(20), height(20), fillColor("blue")) | int i <- [1 .. n] ];
    edges = [[edge("<i>", "<j>") | int j <- [ 1 .. n], j != i] | int i <- [ 1 .. n ] ];
    renderSave(graph(nodes, edges, size(400)),
               |cwd:///tmp.png|
    );
}

*/
