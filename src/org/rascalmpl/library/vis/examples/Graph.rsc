@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module vis::examples::Graph

import vis::Figure;
import vis::Render; 

public void graph1(){
     nodes =
     	[ box(text("A"), id("A"), width(20), height(20), fillColor("green"))
     	];
    edges = 
    	[
    	];
    	    
    render(graph(nodes, edges, size(400),gap(40)));
}


public void K2(){
     nodes =
     	[ box(text("A"), id("A"), size(20), fillColor("green")),
     	  box(text("B"), id("B"), size(20), fillColor("red"))
     	];
    edges = 
    	[ edge("A", "B")   	 
    	];
    	    
    render(graph(nodes, edges, size(400),gap(40)));
}

// Graph of 3 nodes

public void K3(){
     nodes =
        [ box(text("A"), id("A"), size(20), fillColor("green")),
     	  box(text("B"), id("B"), size(20), fillColor("red")),
     	  box(text("C"), id("C"), size(20), fillColor("blue"))
     	];
    edges = 
    	[ edge("A", "B"),
    	  edge("B", "C"),
    	  edge("A", "C")
    	];
    	    
    render(graph(nodes, edges, size(400),gap(40)));
}

public void K33(){

   nodes1 =
     	[ box(text("A1"), id("A1"), size(20), fillColor("green")),
     	  box(text("B1"), id("B1"), size(20), fillColor("red")),
     	  box(text("C1"), id("C1"), size(20), fillColor("blue"))
     	];
    edges1 = 
    	[ edge("A1", "B1"),
    	  edge("B1", "C1"),
    	  edge("C1", "A1")
    	];
    	
    g1 = graph(nodes1, edges1, size(200),gap(40));

    nodes2 =
     	[ box(text("A2"), id("A2"), size(20), fillColor("green")),
     	  box(text("B2"), id("B2"), size(20), fillColor("red")),
     	  box(text("C2"), id("C2"), size(20), fillColor("blue"))
     	];
    edges2 = 
    	[ edge("A2", "B2"),
    	  edge("B2", "C2"),
    	  edge("C2", "A2")
    	];
    g2 = graph(nodes2, edges2, size(200),gap(40));
    
    render(hcat([g1, g2]));
}

public FProperty popup(str s){
	return mouseOver(box(text(s), gap(1), fillColor("yellow")));
}

public void graph4(){
     nodes =
     	[ box(text("A"), id("A"), size(20), fillColor("green"), popup("A")),
     	  box(text("B"), id("B"),  size(30), fillColor("red"),  popup("B")),
     	  ellipse(text("C"), id("C"),  size(20), fillColor("blue"), popup("C")),
     	  ellipse(text("D"), id("D"), size(20, 40), fillColor("purple"), popup("D"))
     	];
    edges = 
    	[ edge("A", "B"),
    	  edge("B", "C"),
    	  edge("C", "D"),
    	  edge("D", "A")
    	];
    	    
    render(graph(nodes, edges, size(400),gap(40)));
}

public void K4(){
     nodes =
     	[ box(text("A"), id("A"), size(20), fillColor("green")),
     	  box(text("B"), id("B"),  size(30), fillColor("red")),
     	  ellipse(text("C"), id("C"),  size(20), fillColor("blue")),
     	  ellipse(text("D"), id("D"), size(20, 40), fillColor("purple"))
     	];
    edges = 
    	[ edge("A", "B"), edge("A", "C"), edge("A", "D"),
    	  edge("B", "A"), edge("B", "C"), edge("B", "D"),
    	  edge("C", "A"), edge("C", "B"), edge("C", "D"),
    	  edge("D", "A"), edge("D", "B"), edge("D", "C")
    	];
    	    
    render(graph(nodes, edges, size(400),gap(40)));
}

public void K5(){
     nodes =
        [ box(text("A"), id("A"), size(20), fillColor("green")),
     	  box(text("B"), id("B"),  size(20), fillColor("red")),
     	  box(text("C"), id("C"),  size(20), fillColor("blue")),
     	  box(text("D"), id("D"), size(20), fillColor("purple")),
     	  box(text("E"), id("E"), size(20), fillColor("lightblue"))
     	];
  
    edges = 
    	[ edge("A", "B"), edge("A", "C"), edge("A", "D"), edge("A", "E"),
    	  edge("B", "A"), edge("B", "C"), edge("B", "D"), edge("B", "E"),
    	  edge("C", "A"), edge("C", "B"), edge("C", "D"), edge("C", "E"),
    	  edge("D", "A"), edge("D", "B"), edge("D", "C"), edge("D", "E"),
    	  edge("E", "A"), edge("E", "B"), edge("E", "C"), edge("E", "D")
    	];
    	    
    render(graph(nodes, edges, size(400),gap(40)));
}

public void K6(){
     nodes =
        [ box(text("A"), id("A"), size(20), fillColor("green")),
     	  box(text("B"), id("B"),  size(20), fillColor("red")),
     	  box(text("C"), id("C"),  size(20), fillColor("blue")),
     	  box(text("D"), id("D"), size(20), fillColor("purple")),
     	  box(text("E"), id("E"), size(20), fillColor("lightblue")),
     	  box(text("F"), id("F"), size(20), fillColor("orange"))
     	];
     	
    edges = 
    	[ edge("A", "B"), edge("A", "C"), edge("A", "D"), edge("A", "E"), edge("A", "F"),
    	  edge("B", "A"), edge("B", "C"), edge("B", "D"), edge("B", "E"), edge("B", "F"),
    	  edge("C", "A"), edge("C", "B"), edge("C", "D"), edge("C", "E"), edge("C", "F"),
    	  edge("D", "A"), edge("D", "B"), edge("D", "C"), edge("D", "E"), edge("D", "F"),
    	  edge("E", "A"), edge("E", "B"), edge("E", "C"), edge("E", "D"), edge("E", "F"),
    	  edge("F", "A"), edge("F", "B"), edge("F", "C"), edge("F", "D"), edge("F", "E")
    	];
    	    
    render(graph(nodes, edges, size(400),gap(40)));
}

public void K(int n){
    nodes = [box(id("<i>"), size(20), fillColor("blue")) | int i <- [1 .. n] ];
    edges = [*[edge("<i>", "<j>") | int j <- [ 1 .. n], j != i] | int i <- [ 1 .. n ] ];
    render(graph(nodes, edges, size(400),gap(40)));
}

public void graph5(){
     nodes =
        [ box(text("A"), id("A"), size(20), fillColor("green")),
     	  box(text("B"), id("B"),  size(20), fillColor("red")),
     	  box(text("C"), id("C"),  size(20), fillColor("blue")),
     	  box(text("D"), id("D"), size(20), fillColor("purple")),
     	  box(text("E"), id("E"), size(20), fillColor("lightblue"))
     	];
     	
    edges = 
    	[ edge("A", "B"),
    	  edge("B", "C"),
    	  edge("C", "D"),
    	  edge("D", "E"),
    	  edge("E", "A")
    	];
    	    
    render(graph(nodes, edges, size(400),gap(40)));
}
public void graph6(){
     nodes =
        [ box(text("A"), id("A"), size(20), fillColor("green")),
     	  box(text("B"), id("B"),  size(20), fillColor("red")),
     	  box(text("C"), id("C"),  size(20), fillColor("blue")),
     	  box(text("D"), id("D"), size(20), fillColor("purple")),
     	  box(text("E"), id("E"), size(20), fillColor("lightblue")),
     	  box(text("F"), id("F"), size(20), fillColor("orange"))
     	];
     	
    edges = 
    	[ edge("A", "B"),
    	  edge("B", "C"),
    	  edge("C", "D"),
    	  edge("D", "E"),
    	  edge("E", "F"),
    	  edge("F", "A")
    	];
    	    
    render(graph(nodes, edges, size(400),gap(40)));
}


