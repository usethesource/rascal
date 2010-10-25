module vis::examples::Graph

import vis::Figure;
import vis::Render; 

public void graph1(){
     nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")], text("A"))
     	];
    edges = 
    	[
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}


public void K2(){
     nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")], text("A")),
     	  box([id("B"),  width(20), height(20), fillColor("red")], text("B"))
     	];
    edges = 
    	[ edge([lineWidth(1)], "A", "B")   	 
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}

// Graph of 3 nodes

public void K3(){
     nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")], text("A")),
     	  box([id("B"),  width(20), height(20), fillColor("red")], text("B")),
     	  box([id("C"),  width(20), height(20), fillColor("blue")], text("C"))
     	];
    edges = 
    	[ edge("A", "B"),
    	  edge("B", "C"),
    	  edge("C", "A")
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}

public void K33(){

   nodes1 =
     	[ box([id("A1"), width(20), height(20), fillColor("green")], text("A1")),
     	  box([id("B1"),  width(20), height(20), fillColor("red")], text("B1")),
     	  box([id("C1"),  width(20), height(20), fillColor("blue")], text("C1"))
     	];
    edges1 = 
    	[ edge("A1", "B1"),
    	  edge("B1", "C1"),
    	  edge("C1", "A1")
    	];
    	
    g1 = graph([width(200), height(200)], nodes1, edges1);

    nodes2 =
     	[ box([id("A2"), width(20), height(20), fillColor("white")], text("A2")),
     	  box([id("B2"),  width(20), height(20), fillColor("grey")], text("B2")),
     	  box([id("C2"),  width(20), height(20), fillColor("black")], text("C2"))
     	];
    edges2 = 
    	[ edge("A2", "B2"),
    	  edge("B2", "C2"),
    	  edge("C2", "A2")
    	];
    g2 = graph([width(200), height(200)], nodes2, edges2);
    
    render(hcat([gap(20)], [g1, g2]));
}

public void graph4(){
     nodes =
     	[ box([id("A"), size(20), fillColor("green")], text("A")),
     	  box([id("B"),  size(30), fillColor("red")], text("B")),
     	  ellipse([id("C"),  size(20), fillColor("blue")], text("C")),
     	  ellipse([id("D"), size(20, 40), fillColor("purple")], text("D"))
     	];
    edges = 
    	[ edge("A", "B"),
    	  edge("B", "C"),
    	  edge("C", "D"),
    	  edge("D", "A")
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}

public void K4(){
     nodes =
     	[box([id("A"), width(20), height(20), fillColor("green")], text("A")),
     	  box([id("B"),  width(20), height(20), fillColor("red")], text("B")),
     	  box([id("C"),  width(20), height(20), fillColor("blue")], text("C")),
     	  box([id("D"), width(20), height(20), fillColor("purple")], text("D"))
     	];
    edges = 
    	[ edge("A", "B"), edge("A", "C"), edge("A", "D"),
    	  edge("B", "A"), edge("B", "C"), edge("B", "D"),
    	  edge("C", "A"), edge("C", "B"), edge("C", "D"),
    	  edge("D", "A"), edge("D", "B"), edge("D", "C")
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}

public void K5(){
     nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")], text("A")),
     	  box([id("B"),  width(20), height(20), fillColor("red")], text("B")),
     	  box([id("C"),  width(20), height(20), fillColor("blue")], text("C")),
     	  box([id("D"), width(20), height(20), fillColor("purple")], text("D")),
     	  box([id("E"), width(20), height(20), fillColor("lightblue")], text("E"))
     	];
    edges = 
    	[ edge("A", "B"), edge("A", "C"), edge("A", "D"), edge("A", "E"),
    	  edge("B", "A"), edge("B", "C"), edge("B", "D"), edge("B", "E"),
    	  edge("C", "A"), edge("C", "B"), edge("C", "D"), edge("C", "E"),
    	  edge("D", "A"), edge("D", "B"), edge("D", "C"), edge("D", "E"),
    	  edge("E", "A"), edge("E", "B"), edge("E", "C"), edge("E", "D")
    	  
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}

public void K6(){
     nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")], text("A")),
     	  box([id("B"),  width(20), height(20), fillColor("red")], text("B")),
     	  box([id("C"),  width(20), height(20), fillColor("blue")], text("C")),
     	  box([id("D"), width(20), height(20), fillColor("purple")], text("D")),
     	  box([id("E"), width(20), height(20), fillColor("lightblue")], text("E")),
          box([id("F"), width(20), height(20), fillColor("orange")], text("F"))
     	];
    edges = 
    	[ edge("A", "B"), edge("A", "C"), edge("A", "D"), edge("A", "E"), edge("A", "F"),
    	  edge("B", "A"), edge("B", "C"), edge("B", "D"), edge("B", "E"), edge("B", "F"),
    	  edge("C", "A"), edge("C", "B"), edge("C", "D"), edge("C", "E"), edge("C", "F"),
    	  edge("D", "A"), edge("D", "B"), edge("D", "C"), edge("D", "E"), edge("D", "F"),
    	  edge("E", "A"), edge("E", "B"), edge("E", "C"), edge("E", "D"), edge("E", "F"),
    	  edge("F", "A"), edge("F", "B"), edge("F", "C"), edge("F", "D"), edge("F", "E")
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}

public void K(int n){
    nodes = [box([id("<i>"), width(20), height(20), fillColor("blue")]) | int i <- [1 .. n] ];
    edges = [[edge("<i>", "<j>") | int j <- [ 1 .. n], j != i] | int i <- [ 1 .. n ] ];
    render(graph([width(400), height(400)], nodes, edges));
}

public void graph5(){
     nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")], text("A")),
     	  box([id("B"),  width(20), height(20), fillColor("red")], text("B")),
     	  box([id("C"),  width(20), height(20), fillColor("blue")], text("C")),
     	  box([id("D"), width(20), height(20), fillColor("purple")], text("D")),
     	  box([id("E"), width(20), height(20), fillColor("lightblue")], text("E"))
     	];
    edges = 
    	[ edge("A", "B"),
    	  edge("B", "C"),
    	  edge("C", "D"),
    	  edge("D", "E"),
    	  edge("E", "A")
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}
public void graph6(){
     nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")], text("A")),
     	  box([id("B"),  width(20), height(20), fillColor("red")], text("B")),
     	  box([id("C"),  width(20), height(20), fillColor("blue")], text("C")),
     	  box([id("D"), width(20), height(20), fillColor("purple")], text("D")),
     	  box([id("E"), width(20), height(20), fillColor("lightblue")], text("E")),
          box([id("F"), width(20), height(20), fillColor("orange")], text("F"))
     	];
    edges = 
    	[ edge("A", "B"),
    	  edge("B", "C"),
    	  edge("C", "D"),
    	  edge("D", "E"),
    	  edge("E", "F"),
    	  edge("F", "A")
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}

// Trees

public void T22(){
     nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")], text("A")),
     	  box([id("B"),  width(20), height(20), fillColor("red")], text("B")),
     	  box([id("C"),  width(20), height(20), fillColor("blue")], text("C")),
     	  box([id("D"), width(20), height(20), fillColor("purple")], text("D")),
     	  box([id("E"), width(20), height(20), fillColor("lightblue")], text("E")),
          box([id("F"), width(20), height(20), fillColor("orange")], text("F")),
     	  box([id("G"), width(20), height(20), fillColor("brown")], text("G"))
     	];
    edges = 
    	[ edge("A", "B"),  edge("A", "C"),
    	  edge("B", "D"), edge("B", "E"), 
    	  edge("C", "F"),  edge("C", "G")	   
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}

public void T33(){
     nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")], text("A")),
     	  box([id("B"),  width(20), height(20), fillColor("red")], text("B")),
     	  box([id("C"),  width(20), height(20), fillColor("blue")], text("C")),
     	  box([id("D"), width(20), height(20), fillColor("purple")], text("D")),
     	  box([id("E"), width(20), height(20), fillColor("lightblue")], text("E")),
          box([id("F"), width(20), height(20), fillColor("orange")], text("F")),
     	  box([id("G"), width(20), height(20), fillColor("brown")], text("G")),
     	  box([id("H"), width(20), height(20), fillColor("black")], text("H")),
     	  box([id("I"), width(20), height(20), fillColor("grey")], text("I")),
     	  box([id("J"), width(20), height(20), fillColor("white")], text("J")),
     	  box([id("K"), width(20), height(20), fillColor("deeppink")], text("K")),
     	  box([id("L"), width(20), height(20), fillColor("deeppink")], text("L")),
     	  box([id("M"), width(20), height(20), fillColor("deeppink")], text("M"))
     	];
    edges = 
    	[ edge("A", "B"),  edge("A", "C"),  edge("A", "D"),
    	  edge("B", "E"), edge("B", "F"), edge("B", "G"),
    	  edge("C", "H"),  edge("C", "I"),  edge("C", "J"),
    	  edge("D", "K"),   edge("D", "L"),  edge("D", "M")
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}

public void T44(){
     nodes =
     	[  box([id("A"), width(20), height(20), fillColor("green")], text("A")),
     	  box([id("B"),  width(20), height(20), fillColor("red")], text("B")),
     	  box([id("C"),  width(20), height(20), fillColor("blue")], text("C")),
     	  box([id("D"), width(20), height(20), fillColor("purple")], text("D")),
     	  box([id("E"), width(20), height(20), fillColor("lightblue")], text("E")),
          box([id("F"), width(20), height(20), fillColor("orange")], text("F")),
     	  box([id("G"), width(20), height(20), fillColor("brown")], text("G")),
     	  box([id("H"), width(20), height(20), fillColor("black")], text("H")),
     	  box([id("I"), width(20), height(20), fillColor("grey")], text("I")),
     	  box([id("J"), width(20), height(20), fillColor("white")], text("J")),
     	  box([id("K"), width(20), height(20), fillColor("deeppink")], text("K")),
     	  box([id("L"), width(20), height(20), fillColor("deeppink")], text("L")),
     	  box([id("M"), width(20), height(20), fillColor("deeppink")], text("M")),
     	  box([id("N"), width(20), height(20), fillColor("deeppink")], text("N")),
     	  box([id("O"), width(20), height(20), fillColor("deeppink")], text("O")),
     	  box([id("P"), width(20), height(20), fillColor("deeppink")], text("P")),
     	  box([id("Q"), width(20), height(20), fillColor("deeppink")], text("Q")),
     	  box([id("R"), width(20), height(20), fillColor("deeppink")], text("R")),
     	  box([id("S"), width(20), height(20), fillColor("deeppink")], text("S")),
     	  box([id("T"), width(20), height(20), fillColor("deeppink")], text("T")),
     	  box([id("U"), width(20), height(20), fillColor("deeppink")], text("U"))
     	];
    edges = 
    	[ edge("A", "B"),  edge("A", "C"),  edge("A", "D"),  edge("A", "E"),
    	  edge("B", "F"), edge("B", "G"), edge("B", "H"), edge("B", "I"), 
    	  edge("C", "J"),  edge("C", "K"),  edge("C", "L"),  edge("C", "M"),
    	  edge("D", "N"),   edge("D", "O"),  edge("D", "P"),  edge("D", "Q"),
    	  edge("E", "R"),   edge("E", "S"), edge("E", "T"), edge("E", "U")   	   
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}

// Graph

public void graphn(){
     nodes =
     	[   box([id("A"), width(20), height(20), fillColor("green")], text("A")),
     	  box([id("B"),  width(20), height(20), fillColor("red")], text("B")),
     	  box([id("C"),  width(20), height(20), fillColor("blue")], text("C")),
     	  box([id("D"), width(20), height(20), fillColor("purple")], text("D")),
     	  box([id("E"), width(20), height(20), fillColor("lightblue")], text("E")),
          box([id("F"), width(20), height(20), fillColor("orange")], text("F")),
     	  box([id("G"), width(20), height(20), fillColor("brown")], text("G")),
     	  box([id("H"), width(20), height(20), fillColor("black")], text("H")),
     	  box([id("I"), width(20), height(20), fillColor("grey")], text("I")),
     	  box([id("J"), width(20), height(20), fillColor("white")], text("J")),
     	  box([id("K"), width(20), height(20), fillColor("deeppink")], text("K"))
     	];
    edges = 
    	[ edge([lineWidth(1)], "A", "B"),
    	  edge([lineWidth(1)], "B", "C"),
    	  edge([lineWidth(1)], "C", "D"),
    	  edge([lineWidth(1)], "D", "E"),
    	  edge([lineWidth(1)], "E", "F"),
    	  edge([lineWidth(1)], "F", "G"),
    	  edge([lineWidth(1)], "G", "H"),
    	  edge([lineWidth(1)], "H", "I"),
    	  edge([lineWidth(1)], "I", "J"),
    	  edge([lineWidth(1)], "J", "K"),
    	  
    	   edge([lineWidth(1)], "K", "A")
    	  
    	  //edge([lineWidth(1)], "A", "D")
    	 // edge([lineWidth(1)], "G", "K")
    	  
    	   
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}