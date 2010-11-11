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
    	    
    render(graph(nodes, edges, size(400)));
}


public void K2(){
     nodes =
     	[ box(text("A"), id("A"), size(20), fillColor("green")),
     	  box(text("B"), id("B"), size(20), fillColor("red"))
     	];
    edges = 
    	[ edge([lineWidth(1)], "A", "B")   	 
    	];
    	    
    render(graph(nodes, edges, size(400)));
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
    	  edge("C", "A")
    	];
    	    
    render(graph(nodes, edges, size(400)));
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
    	
    g1 = graph(nodes1, edges1, size(200));

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
    g2 = graph(nodes2, edges2, size(200));
    
    render(hcat([g1, g2], gap(20)));
}

public void graph4(){
     nodes =
     	[ box(text("A"), id("A"), size(20), fillColor("green")),
     	  box(text("B"), id("B"),  size(30), fillColor("red")),
     	  ellipse(text("C"), id("C"),  size(20), fillColor("blue")),
     	  ellipse(text("D"), id("D"), size(20, 40), fillColor("purple"))
     	];
    edges = 
    	[ edge("A", "B"),
    	  edge("B", "C"),
    	  edge("C", "D"),
    	  edge("D", "A")
    	];
    	    
    render(graph(nodes, edges, size(400)));
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
    	    
    render(graph(nodes, edges, size(400)));
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
    	    
    render(graph(nodes, edges, size(400)));
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
    	    
    render(graph(nodes, edges, size(400)));
}

public void K(int n){
    nodes = [box(id("<i>"), size(20), fillColor("blue")) | int i <- [1 .. n] ];
    edges = [[edge("<i>", "<j>") | int j <- [ 1 .. n], j != i] | int i <- [ 1 .. n ] ];
    render(graph(nodes, edges, size(400)));
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
    	    
    render(graph(nodes, edges, size(400)));
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
    	    
    render(graph(nodes, edges, size(400)));
}

// Trees

public void T22(){
     nodes =
     
      [ box(text("A"), id("A"), size(20), fillColor("green")),
     	  box(text("B"), id("B"),  size(20), fillColor("red")),
     	  box(text("C"), id("C"),  size(20), fillColor("blue")),
     	  box(text("D"), id("D"), size(20), fillColor("purple")),
     	  box(text("E"), id("E"), size(20), fillColor("lightblue")),
     	  box(text("F"), id("F"), size(20), fillColor("orange")),
     	  box(text("G"), id("G"), size(20), fillColor("brown"))
     	];
     	
    edges = 
    	[ edge("A", "B"),  edge("A", "C"),
    	  edge("B", "D"), edge("B", "E"), 
    	  edge("C", "F"),  edge("C", "G")	   
    	];
    	    
    render(graph(nodes, edges, size(400)));
}

public void T33(){
     nodes =
     
        [ box(text("A"), id("A"), size(20), fillColor("green")),
     	  box(text("B"), id("B"),  size(20), fillColor("red")),
     	  box(text("C"), id("C"),  size(20), fillColor("blue")),
     	  box(text("D"), id("D"), size(20), fillColor("purple")),
     	  box(text("E"), id("E"), size(20), fillColor("lightblue")),
     	  box(text("F"), id("F"), size(20), fillColor("orange")),
     	  box(text("G"), id("G"), size(20), fillColor("brown")),
     	  box(text("H"), id("H"), size(20), fillColor("black")),
     	  box(text("I"), id("I"), size(20), fillColor("grey")),
     	  box(text("J"), id("J"), size(20), fillColor("white")),
     	  box(text("K"), id("K"), size(20), fillColor("deeppink")),
     	  box(text("L"), id("L"), size(20), fillColor("deeppink")),
     	  box(text("M"), id("M"), size(20), fillColor("deeppink"))
     	];
     	
    edges = 
    	[ edge("A", "B"),  edge("A", "C"),  edge("A", "D"),
    	  edge("B", "E"), edge("B", "F"), edge("B", "G"),
    	  edge("C", "H"),  edge("C", "I"),  edge("C", "J"),
    	  edge("D", "K"),   edge("D", "L"),  edge("D", "M")
    	];
    	    
    render(graph(nodes, edges, size(200)));
}

public void T44(){
     nodes =
     
        [ box(text("A"), id("A"), size(20), fillColor("green")),
     	  box(text("B"), id("B"),  size(20), fillColor("red")),
     	  box(text("C"), id("C"),  size(20), fillColor("blue")),
     	  box(text("D"), id("D"), size(20), fillColor("purple")),
     	  box(text("E"), id("E"), size(20), fillColor("lightblue")),
     	  box(text("F"), id("F"), size(20), fillColor("orange")),
     	  box(text("G"), id("G"), size(20), fillColor("brown")),
     	  box(text("H"), id("H"), size(20), fillColor("black")),
     	  box(text("I"), id("I"), size(20), fillColor("grey")),
     	  box(text("J"), id("J"), size(20), fillColor("white")),
     	  box(text("K"), id("K"), size(20), fillColor("deeppink")),
     	  box(text("L"), id("L"), size(20), fillColor("deeppink")),
     	  box(text("M"), id("M"), size(20), fillColor("deeppink")),
     	  box(text("N"), id("N"), size(20), fillColor("deeppink")),
     	  box(text("O"), id("O"), size(20), fillColor("deeppink")),
          box(text("P"), id("P"), size(20), fillColor("deeppink")),
          box(text("Q"), id("Q"), size(20), fillColor("deeppink")),
     	  box(text("R"), id("R"), size(20), fillColor("deeppink")),
     	  box(text("S"), id("S"), size(20), fillColor("deeppink")),
     	  box(text("T"), id("T"), size(20), fillColor("deeppink")),
     	  box(text("U"), id("U"), size(20), fillColor("deeppink"))        
     	];
     	
    edges = 
    	[ edge("A", "B"),  edge("A", "C"),  edge("A", "D"),  edge("A", "E"),
    	  edge("B", "F"), edge("B", "G"), edge("B", "H"), edge("B", "I"), 
    	  edge("C", "J"),  edge("C", "K"),  edge("C", "L"),  edge("C", "M"),
    	  edge("D", "N"),   edge("D", "O"),  edge("D", "P"),  edge("D", "Q"),
    	  edge("E", "R"),   edge("E", "S"), edge("E", "T"), edge("E", "U")   	   
    	];
    	    
    render(graph(nodes, edges, size(400), hint("lattice")));
}

// Graph

public void graphn(){
     nodes =
        [ box(text("A"), id("A"), size(20), fillColor("green")),
     	  box(text("B"), id("B"),  size(20), fillColor("red")),
     	  box(text("C"), id("C"),  size(20), fillColor("blue")),
     	  box(text("D"), id("D"), size(20), fillColor("purple")),
     	  box(text("E"), id("E"), size(20), fillColor("lightblue")),
     	  box(text("F"), id("F"), size(20), fillColor("orange")),
     	  box(text("G"), id("G"), size(20), fillColor("brown")),
     	  box(text("H"), id("H"), size(20), fillColor("black")),
     	  box(text("I"), id("I"), size(20), fillColor("grey")),
     	  box(text("J"), id("J"), size(20), fillColor("white")),
     	  box(text("K"), id("K"), size(20), fillColor("deeppink"))
     	];
     
    edges = 
    	[ edge("A", "B"),
    	  edge("B", "C"),
    	  edge("C", "D"),
    	  edge("D", "E"),
    	  edge("E", "F"),
    	  edge("F", "G"),
    	  edge("G", "H"),
    	  edge("H", "I"),
    	  edge("I", "J"),
    	  edge("J", "K"),
    	  edge("K", "A")
    	  
    	  //edge([lineWidth(1)], "A", "D")
    	  // edge([lineWidth(1)], "G", "K")  
    	];
    	    
    render(graph(nodes, edges, size(400)));
}