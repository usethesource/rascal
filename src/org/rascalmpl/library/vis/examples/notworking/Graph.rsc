
// See Introduction to the Theory of Computation, Michael Sipser, page 285)
/*
public void sat(){
     Figure g1 = graph([ shapeEllipse(text("A1 1"), id("A1"),  fillColor("lightgreen"),layer("BOTTOM")),
     	  shapeEllipse(text("B1 1"), id("B1"), fillColor("lightgreen"), layer("BOTTOM")),
     	  shapeEllipse(text("C1 2"), id("C1"), fillColor("lightgreen"), layer("q"))], [ edge("A1", "B1") , 
    	  edge("B1", "C1") ,  
    	  edge("C1", "A1")], id("Q1"), hint("leveled"), gap(40), size(200), layer("z"));
     Figure g2 = graph([ shapeEllipse(text("A2 1"), id("A2"),  fillColor("salmon"),layer("BOTTOM")),
     	  shapeEllipse(text("B2 2"), id("B2"),  fillColor("salmon"), layer("BOTTOM")),
     	  shapeEllipse(text("C2 2"), id("C2"), fillColor("salmon"), layer("q"))], [ edge("A2", "B2") , 
    	  edge("B2", "C2") ,  
    	  edge("C2", "A2")], id("Q2"), hint("leveled"), gap(40), size(200), layer("z"));
     Figure g3 = graph([ shapeEllipse(text("A3 1"), id("A3"),  fillColor("salmon"),layer("BOTTOM")),
     	  shapeEllipse(text("B3 2"), id("B3"),  fillColor("lightgreen"), layer("BOTTOM")),
     	  shapeEllipse(text("C3 2"), id("C3"),  fillColor("lightgreen"), layer("q"))], [ edge("A3", "B3") , 
    	  edge("B3", "C3") ,  
    	  edge("C3", "A3")], id("Q3"), hint("leveled"), gap(40), size(200), layer("z"));
     nodes =
     	[ g1 ,
     	  g2 ,
     	  g3, 
     	  box(text("1 1"), id("1"), size(20), fillColor("lightgreen"), layer("TOP")),
     	  box(text("-1 1"), id("-1"), size(20), fillColor("salmon"), layer("TOP")),
     	  box(text("2 2"), id("2"), size(20), fillColor("lightgreen"), layer("BOTTOM")),
     	  box(text("-2 2"), id("-2"), size(20), fillColor("salmon"), layer("BOTTOM")) 	  
     	];
    edges = 
    	[   
    	  edge("A1", "1"),
    	  edge("B1", "1"),
    	  edge("C1", "2"),
    	  
    	  edge("A2", "1"),  
    	  edge("B2", "-2"),
    	  edge("C2", "-2"),
    	  
    	  edge("A3", "-1"),
    	  edge("B3", "2"),
    	  edge("C3", "2"),
    	  edge("1","-1"),
    	  edge("2","-2")  	    	 
    	];
    Figure g = graph(nodes, edges, size(800), gap(50));	    
    render(g);
}

*/