module experiments::VL::Examples2

import experiments::VL::VLCore;
import experiments::VL::VLRender; 

public void graph1(){
     nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")])
     	];
    edges = 
    	[
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}


public void graph2(){
     nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")]),
     	  box([id("B"),  width(20), height(20), fillColor("red")])
     	];
    edges = 
    	[ edge([lineWidth(1)], "A", "B")   	 
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}

// Graph of 3 nodes

public void graph3(){
     nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")]),
     	  box([id("B"),  width(20), height(20), fillColor("red")]),
     	  box([id("C"),  width(20), height(20), fillColor("blue")])
     	];
    edges = 
    	[ edge([lineWidth(1)], "A", "B"),
    	  edge([lineWidth(1)], "B", "C"),
    	  edge([lineWidth(1)], "C", "A")
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}

public void graph4(){
     nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")]),
     	  box([id("B"),  width(20), height(20), fillColor("red")]),
     	  box([id("C"),  width(20), height(20), fillColor("blue")]),
     	  box([id("D"), width(20), height(20), fillColor("purple")])
     	];
    edges = 
    	[ edge([lineWidth(1)], "A", "B"),
    	  edge([lineWidth(1)], "B", "C"),
    	  edge([lineWidth(1)], "C", "D"),
    	  edge([lineWidth(1)], "D", "A")
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}

public void graph6(){
     nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")]),
     	  box([id("B"),  width(20), height(20), fillColor("red")]),
     	  box([id("C"),  width(20), height(20), fillColor("blue")]),
     	  box([id("D"), width(20), height(20), fillColor("purple")]),
     	  box([id("E"), width(20), height(20), fillColor("lightblue")]),
          box([id("F"), width(20), height(20), fillColor("orange")])
     	];
    edges = 
    	[ edge([lineWidth(1)], "A", "B"),
    	  edge([lineWidth(1)], "B", "C"),
    	  edge([lineWidth(1)], "C", "D"),
    	  edge([lineWidth(1)], "D", "E"),
    	  edge([lineWidth(1)], "E", "F"),
    	  edge([lineWidth(1)], "F", "A")
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}

// Graph

public void graphn(){
     nodes =
     	[ box([id("A"), width(10), height(20), fillColor("green")]),
     	  box([id("B"), width(20), height(30), fillColor("red")]),
     	  box([id("C"), width(30), height(20), fillColor("blue")]),
     	  box([id("D"), width(20), height(20), fillColor("purple")]),
     	  box([id("E"), width(20), height(20), fillColor("lightblue")]),
          box([id("F"), width(20), height(20), fillColor("orange")]),
     	  box([id("G"), width(20), height(20), fillColor("brown")]),
     	  box([id("H"), width(20), height(20), fillColor("black")]),
     	  box([id("I"), width(20), height(20), fillColor("grey")]),
     	  box([id("J"), width(20), height(20), fillColor("white")]),
     	  box([id("K"), width(20), height(20), fillColor("deeppink")])
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