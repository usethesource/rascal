module viz::Figure::ExampleTree

import viz::Figure::Core;
import viz::Figure::Render;

// Tree: 1 node

public void tree1(){

   nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")])
     	];
     	
    edges = 
    	[    	  
    	];
    	  
    render(tree([gap(10), top()], nodes, edges, "A"));
}

public void tree22(){

   nodes1 =
     	[ box([id("A"), width(20), height(20), fillColor("green"), popup("A")]),
          box([id("B"), width(20), height(20), fillColor("blue"), popup("B")])
     	];
     	
    edges = 
    	[    	  
    	];
    	
    nodes2 =
     	[ box([id("C"), width(20), height(20), fillColor("white"), popup("C")]),
     	  box([id("D"), width(20), height(20), fillColor("black"), popup("D")])
     	];
    	  
    t1 = tree([gap(10), top()], nodes1, edges, "A");
    t2 = tree([gap(10), top()], nodes2, edges, "C");
    
    render(hcat([gap(100)], [t1, t2]));
}

// Tree: 2 nodes

public void tree2(){

   nodes =
     	[ box([id("A"), fillColor("green")]),
     	  box([id("B"), fillColor("red")])
     	];
     	
    edges = 
    	[ edge("A", "B")    	  
    	];
    	  
    render(tree([gap(10), top(), size(20), lineWidth(1)], nodes, edges, "A"));
}

// Tree: 3 nodes
public void tree3(){

   nodes =
     	[ box([id("A"), fillColor("green")]),
     	  box([id("B"), fillColor("red")]),
     	  box([id("C"), fillColor("blue")])
     	];
     	
    edges = 
    	[ edge("A", "B"),
          edge("A", "C")
    	];
    	  
    render(tree([gap(10), size(20), lineWidth(1)], nodes, edges, "A"));
}

// Tree: 3 nodes and hcat

public void tree3a(){

   nodes =
     	[ box([id("A"), fillColor("green")]),
     	  box([id("B"), fillColor("red")]),
     	  box([id("C"), fillColor("blue")])
     	];
     	
    edges = 
    	[ edge("A", "B"),
          edge("A", "C")
    	];
    	  
    render(hcat([ ellipse([size(30)]), 
                  tree([gap(10), top(), size(20), lineWidth(1)], nodes, edges, "A")
                ]));
}

// Tree: 4 nodes

public void tree4(){

   nodes =
     	[ box([id("A"), fillColor("green")]),
     	  box([id("B"), fillColor("red")]),
     	  box([id("C"), fillColor("blue")]),
     	  box([id("D"), fillColor("purple")])
     	];
     	
    edges = 
    	[ edge("A", "B"),
          edge("A", "C"),
          edge("A", "D")
    	];
    	  
    render(tree([gap(10), top(), size(20), lineWidth(1)], nodes, edges, "A"));
}

// Tree

public void tree4a(){

   nodes =
     	[ box([id("A"), fillColor("green")]),
     	  box([id("B"), fillColor("red")]),
     	  box([id("C"), fillColor("blue")]),
     	  box([id("D"), fillColor("purple")]), 	
     	  box([id("E"), fillColor("lightblue")]),
          box([id("F"), fillColor("orange")]),
     	  box([id("G"), fillColor("brown")]),
     	  box([id("H"), fillColor("black")]),
     	  box([id("I"), fillColor("grey")]),
     	  box([id("J"), fillColor("white")]),
     	  box([id("K"), fillColor("deeppink")])
     	];
    edges = 
    	[ edge("A", "B"),
    	  edge("A", "C"),
    	  edge("A", "K"),
    	  edge("A", "D"),
    	  edge("B", "E"),
    	  edge("B", "F"),
    	  edge("B", "G")
    //	  edge("D", "H"),
    //	  edge("D", "I"),
    //	  edge("D", "J")
    	];
    	    
    render(tree([gap(10), top(), size(20), lineWidth(1)], nodes, edges, "A"));
}


// Tree

public void tree5(){

   nodes =
     	[ box([id("A"), fillColor("green")]),
     	  box([id("B"), fillColor("red")]),
     	  box([id("C"), fillColor("blue")]),
     	  box([id("D"), fillColor("purple")]), 	
     	  box([id("E"), fillColor("lightblue")]),
          box([id("F"), fillColor("orange")]),
     	  box([id("G"), fillColor("brown")]),
     	  box([id("H"), fillColor("black")]),
     	  box([id("I"), fillColor("grey")]),
     	  box([id("J"), fillColor("white")]),
     	  box([id("K"), fillColor("deeppink")])
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
    	    
    render(tree([gap(10), top(), size(20), lineWidth(1)], nodes, edges, "A"));
}

// Tree

public void tree6(){

   nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green")]),
     	  box([id("B"), width(20), height(20), fillColor("red")]),
     	  box([id("C"), width(20), height(20), fillColor("blue")]),
     	  box([id("D"), width(20), height(20), fillColor("purple")]),
     	  box([id("E"), width(20), height(20), fillColor("lightblue")]),
     	  box([id("F"), width(20), height(20), fillColor("orange")]),
     	  box([id("G"), width(20), height(20), fillColor("brown")]),
     	  box([id("H"), width(20), height(20), fillColor("black")]),
     	  box([id("I"), width(20), height(20), fillColor("grey")]),
     	  box([id("J"), width(20), height(20), fillColor("white")])
     	];
    edges = 
    	[ edge([lineWidth(1)], "A", "B"),
    	  edge([lineWidth(1)], "B", "C"),
    	  edge([lineWidth(1)], "B", "D"),
    	  edge([lineWidth(1)], "A", "E"),
    	  edge([lineWidth(1)], "A", "H"),
    	  edge([lineWidth(1)], "E", "F"),
    	  edge([lineWidth(1)], "E", "G"),
    	  edge([lineWidth(1)], "E", "I"),
    	  edge([lineWidth(1)], "E", "J")
    	  
    	];
    	    
    render(tree([gap(10), top()], nodes, edges, "A"));
}

// Tree with text popups.

public FProperty popup(str s){
	return mouseOver(box([width(0), height(0), gap(1), fillColor("yellow")], text(s)));
}

public void tree7(){
   nodes =
     	[ box([id("A"), width(20), height(20), fillColor("green"), popup("Text For Box A")]),
     	  box([id("B"), width(20), height(20), fillColor("red"), popup("Text For Box B")]),
     	  box([id("C"), width(20), height(20), fillColor("blue"), popup("Text For Box C")]),
     	  box([id("D"), width(20), height(20), fillColor("purple"), popup("Text For Box D")]),
     	  box([id("E"), width(20), height(20), fillColor("lightblue"), popup("Text For Box E")]),
     	  box([id("F"), width(20), height(20), fillColor("orange"), popup("Text For Box F")]),
     	  box([id("G"), width(20), height(20), fillColor("brown"), popup("Text For Box G")]),
     	  box([id("H"), width(20), height(20), fillColor("black"), popup("Text For Box H")]),
     	  box([id("I"), width(20), height(20), fillColor("grey"), popup("Text For Box I")]),
     	  box([id("J"), width(20), height(20), fillColor("white"), popup("Text For Box J")])
     	];
    edges = 
    	[ edge([lineWidth(1)], "A", "B"),
    	  edge([lineWidth(1)], "B", "C"),
    	  edge([lineWidth(1)], "B", "D"),
    	  edge([lineWidth(1)], "A", "E"),
    	  edge([lineWidth(1)], "A", "H"),
    	  edge([lineWidth(1)], "E", "F"),
    	  edge([lineWidth(1)], "E", "G"),
    	  edge([lineWidth(1)], "E", "I"),
    	  edge([lineWidth(1)], "E", "J")
    	  
    	];	    
    render(tree([gap(10), top(), lineWidth(1), fillColor("black")], nodes, edges, "A"));
}

public void tree8(){
   nodes =
     	[ ellipse([id("A"), width(20), height(20), fillColor("green")], popup("Text For ellipse A")),
     	  ellipse([id("B"), width(20), height(20), fillColor("red")], popup("Text For ellipse B")),
     	  ellipse([id("C"), width(20), height(20), fillColor("blue")], popup("Text For ellipse C")),
     	  ellipse([id("D"), width(20), height(20), fillColor("purple")], popup("Text For ellipse D")),
     	  ellipse([id("E"), width(20), height(20), fillColor("lightblue")], popup("Text For ellipse E")),
     	  ellipse([id("F"), width(20), height(20), fillColor("orange")], popup("Text For ellipse F")),
     	  ellipse([id("G"), width(20), height(20), fillColor("brown")], popup("Text For ellipse G")),
     	  ellipse([id("H"), width(20), height(20), fillColor("black")], popup("Text For ellipse H")),
     	  ellipse([id("I"), width(20), height(20), fillColor("grey")], popup("Text For ellipse I")),
     	  ellipse([id("J"), width(20), height(20), fillColor("white")], popup("Text For ellipse J"))
     	];
    edges = 
    	[ edge([lineWidth(1)], "A", "B"),
    	  edge([lineWidth(1)], "B", "C"),
    	  edge([lineWidth(1)], "B", "D"),
    	  edge([lineWidth(1)], "A", "E"),
    	  edge([lineWidth(1)], "A", "H"),
    	  edge([lineWidth(1)], "E", "F"),
    	  edge([lineWidth(1)], "E", "G"),
    	  edge([lineWidth(1)], "E", "I"),
    	  edge([lineWidth(1)], "E", "J")
    	  
    	];	    
    render(tree([gap(10), top(), lineWidth(1), fillColor("black")], nodes, edges, "A"));
}

// Tree

public void tree9(){

   nodes =
     	[ box([id("A"), width(10), height(20), fillColor("green")]),
     	  box([id("B"), width(20), height(60), fillColor("red")]),
     	  box([id("C"), width(60), height(20), fillColor("blue")]),
     	  box([id("D"), width(10), height(20), fillColor("purple")]),
     	  box([id("E"), width(30), height(20), fillColor("lightblue")]),
     	  box([id("F"), width(30), height(30), fillColor("orange")]),
     	  box([id("G"), width(30), height(50), fillColor("brown")])
     	];
    edges = 
    	[ edge("A", "B"),
    	  edge("B", "C"),
    	  edge("B", "D"),
    	  edge("A", "E"),
    	  edge("E", "F"),
    	  edge("E", "G")
    	];
    	    
    render(tree([gap(10), top(), lineWidth(1)], nodes, edges, "A"));
}

// TODO: 2 trees in a hcat

public void tree10(){
 nodes1 =
     	[ box([id("A"), width(20), height(20), fillColor("green"), popup("1:Text For Box A")]),
     	  box([id("B"), width(20), height(20), fillColor("red"), popup("1:Text For Box B")]),
     	  box([id("C"), width(20), height(20), fillColor("blue"), popup("1:Text For Box C")]),
     	  box([id("D"), width(20), height(20), fillColor("purple"), popup("1:Text For Box D")]),
     	  box([id("E"), width(20), height(20), fillColor("lightblue"), popup("1:Text For Box E")]),
     	  box([id("F"), width(20), height(20), fillColor("orange"), popup("1:ext For Box F")]),
     	  box([id("G"), width(20), height(20), fillColor("brown"), popup("1:Text For Box G")]),
     	  box([id("H"), width(20), height(20), fillColor("black"), popup("1:Text For Box H")]),
     	  box([id("I"), width(20), height(20), fillColor("grey"), popup("1:Text For Box I")]),
     	  box([id("J"), width(20), height(20), fillColor("white"), popup("1:Text For Box J")])
     	];
    edges1 = 
    	[ edge([lineWidth(1)], "A", "B"),
    	  edge([lineWidth(1)], "B", "C"),
    	  edge([lineWidth(1)], "B", "D"),
    	  edge([lineWidth(1)], "A", "E"),
    	  edge([lineWidth(1)], "A", "H"),
    	  edge([lineWidth(1)], "E", "F"),
    	  edge([lineWidth(1)], "E", "G"),
    	  edge([lineWidth(1)], "E", "I"),
    	  edge([lineWidth(1)], "E", "J")
    	  
    	];	
    	
   atree1 = tree([gap(10), top(), lineWidth(1), fillColor("black")], nodes1, edges1, "A");    
  
   nodes2 =
     	[ ellipse([id("A"), width(20), height(20), fillColor("green"), popup("2:Text For ellipse A")]),
     	  ellipse([id("B"), width(20), height(20), fillColor("red"), popup("2:Text For ellipse B")]),
     	  ellipse([id("C"), width(20), height(20), fillColor("blue"), popup("2:Text For ellipse C")]),
     	  ellipse([id("D"), width(20), height(20), fillColor("purple"), popup("2:Text For ellipse D")]),
     	  ellipse([id("E"), width(20), height(20), fillColor("lightblue"), popup("2:Text For ellipse E")]),
     	  ellipse([id("F"), width(20), height(20), fillColor("orange"), popup("2:Text For ellipse F")]),
     	  ellipse([id("G"), width(20), height(20), fillColor("brown"), popup("2:Text For ellipse G")]),
     	  ellipse([id("H"), width(20), height(20), fillColor("black"), popup("2:Text For ellipse H")]),
     	  ellipse([id("I"), width(20), height(20), fillColor("grey"), popup("2:Text For ellipse I")]),
     	  ellipse([id("J"), width(20), height(20), fillColor("white"), popup("2:Text For ellipse J")])
     	];
    edges2 = 
    	[ edge([lineWidth(1)], "A", "B"),
    	  edge([lineWidth(1)], "B", "C"),
    	  edge([lineWidth(1)], "B", "D"),
    	  edge([lineWidth(1)], "A", "E"),
    	  edge([lineWidth(1)], "A", "H"),
    	  edge([lineWidth(1)], "E", "F"),
    	  edge([lineWidth(1)], "E", "G"),
    	  edge([lineWidth(1)], "E", "I"),
    	  edge([lineWidth(1)], "E", "J")
    	  
    	];	    
    atree2 = tree([gap(10), top(), lineWidth(1), fillColor("black")], nodes2, edges2, "A");
    
    render(hcat([top()], [atree1, atree2]));
}
