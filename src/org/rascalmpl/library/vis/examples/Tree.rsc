@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module vis::examples::Tree

import vis::Figure;
import vis::Render;

// Tree: 1 node

public void tree1(){

   nodes =
     	[ box(id("A"), width(20), height(20), resizable(false), fillColor("green"))
     	];
     	
    edges = 
    	[    	  
    	];
    	  
    render(tree(nodes, edges, gap(10)));
}

public void tree1a(){

   nodes =
     	[ box(id("A"), width(20), height(20), fillColor("green"))
     	];
     	
    edges = 
    	[    	  
    	];
    	  
    render(hcat([ ellipse(size(30)), tree(nodes, edges, gap(10))],
                  child(center())
                ));
}

public void tree1b(){

   nodes =
     	[ box(id("A"), width(20), height(20), fillColor("green"),  popup("A"))
     	];
     	
    edges = 
    	[    	  
    	];
    	  
    render(tree(nodes, edges, gap(10)));
}


public void tree22(){

   nodes1 =
     	[ box(id("A"), width(40), height(40), fillColor("green"), popup("A"))
     	];
     	
    edges = 
    	[    	  
    	];
    	
    nodes2 =
     	[ box(id("C"), width(20), height(20), fillColor("white"), popup("C"))
     	];
    	  
    t1 = tree(nodes1, edges, gap(10), top());
    t2 = tree(nodes2, edges, gap(10), top());
    
    render(hcat([t1, t2], gap(100), bottom()));
}

// Tree: 2 nodes

public void tree2(){

   nodes =
     	[ box(id("A"), fillColor("green")),
     	  box(id("B"), fillColor("red"))
     	];
     	
    edges = 
    	[ edge("A", "B")    	  
    	];
    	  
    render(tree(nodes, edges, gap(10), top(), size(20), lineWidth(1)));
}

public void tree2b(){

   nodes =
     	[ box(id("A"), fillColor("green"), popup("A")),
     	  box(id("B"), fillColor("red"), popup("B"))
     	];
     	
    edges = 
    	[ edge("A", "B")    	  
    	];
    	  
    render(tree(nodes, edges, gap(10), top(), size(20), lineWidth(1)));
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
    	  
    render(tree( nodes, edges, gap(10), size(20), lineWidth(1)));
}

// Tree: 3 nodes and hcat

public void tree3a(){

   nodes =
     	[ box(id("A"), fillColor("green")),
     	  box(id("B"), fillColor("red")),
     	  box(id("C"), fillColor("blue"))
     	];
     	
    edges = 
    	[ edge("A", "B"),
          edge("A", "C")
    	];
    	  
    render(hcat([ ellipse(size(30)), 
                  tree(nodes, edges, gap(10), size(20), lineWidth(1))
                ]));
}

// Tree: 4 nodes

public void tree4(){

   nodes =
     	[ box(id("A"), fillColor("green")),
     	  box(id("B"), fillColor("red")),
     	  box(id("C"), fillColor("blue")),
     	  box(id("D"), fillColor("purple"))
     	];
     	
    edges = 
    	[ edge("A", "B"),
          edge("A", "C"),
          edge("A", "D")
    	];
    	  
    render(tree(nodes, edges, gap(10), top(), size(20), lineWidth(1)));
}

// Tree

public void tree5a(){

   nodes =
     	[ box(id("A"), fillColor("green")),
     	  box(id("B"), fillColor("red")),
     	  box(id("C"), fillColor("blue")),
     	  box(id("E"), fillColor("lightblue")),
     	  box(id("G"), fillColor("brown"))
     	];
    edges = 
    	[ edge("A", "B"),
    	  edge("A", "C"),
    	  edge("B", "E"),
    	  edge("B", "G")
    	];
    	    
    render(tree(nodes, edges, gap(10), size(20), lineWidth(1)));
}


// Tree

public void tree5b(){

   nodes =
     	[ box(id("A"), fillColor("green")),
     	  box(id("B"), fillColor("red")),
     	  box(id("C"), fillColor("blue")),
     	  box(id("E"), fillColor("lightblue")),
     	  box(id("G"), fillColor("brown"))
     	];
    edges = 
    	[ edge("A", "B"),
    	  edge("A", "C"),
    	  edge("C", "E"),
    	  edge("C", "G")
    	];
    	    
    render(tree(nodes, edges, gap(10), top(), size(20), lineWidth(1)));
}


// Tree
// TODO: Multiple roots here
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
    	 // edge("A", "K"),
    	  edge("A", "D"),
    	  edge("B", "E"),
    	 // edge("B", "F"),
    	 // edge("B", "G"),
    	  edge("D", "H"),
    	 // edge("D", "I"),
    	  edge("D", "J")
    	];
    	    
    render(tree(nodes, edges, gap(10), child(top()), size(20), lineWidth(1)));
}

// Tree

public void tree6(){

   nodes =
     	[ box(id("A"), width(20), height(20), fillColor("green")),
     	  box(id("B"), width(20), height(20), fillColor("red")),
     	  box(id("C"), width(20), height(20), fillColor("blue")),
     	  box(id("D"), width(20), height(20), fillColor("purple")),
     	  box(id("E"), width(20), height(20), fillColor("lightblue")),
     	  box(id("F"), width(20), height(20), fillColor("orange")),
     	  box(id("G"), width(20), height(20), fillColor("brown")),
     	  box(id("H"), width(20), height(20), fillColor("black")),
     	  box(id("I"), width(20), height(20), fillColor("grey")),
     	  box(id("J"), width(20), height(20), fillColor("white"))
     	];
    edges = 
    	[ edge("A", "B"),
    	  edge("B", "C"),
    	  edge("B", "D"),
    	  edge("A", "E"),
    	  edge("A", "H"),
    	  edge("E", "F"),
    	  edge("E", "G"),
    	  edge("E", "I"),
    	  edge("E", "J")
    	  
    	];
    	    
    render(tree(nodes, edges, gap(10), child(top()), lineWidth(1)));
}

// Tree with text popups.

public FProperty popup(str s){
	return mouseOver(box(text(s), gap(1), fillColor("yellow")));
}

public void tree7(){
   nodes =
     	[ box(id("A"), width(20), height(20), fillColor("green"), popup("Text For Box A")),
     	  box(id("B"), width(20), height(20), fillColor("red"), popup("Text For Box B")),
     	  box(id("C"), width(20), height(20), fillColor("blue"), popup("Text For Box C")),
     	  box(id("D"), width(20), height(20), fillColor("purple"), popup("Text For Box D")),
     	  box(id("E"), width(20), height(20), fillColor("lightblue"), popup("Text For Box E")),
     	  box(id("F"), width(20), height(20), fillColor("orange"), popup("Text For Box F")),
     	  box(id("G"), width(20), height(20), fillColor("brown"), popup("Text For Box G")),
     	  box(id("H"), width(20), height(20), fillColor("black"), popup("Text For Box H")),
     	  box(id("I"), width(20), height(20), fillColor("grey"), popup("Text For Box I")),
     	  box(id("J"), width(20), height(20), fillColor("white"), popup("Text For Box J"))
     	];
    edges = 
    	[ edge("A", "B"),
    	  edge("B", "C"),
    	  edge("B", "D"),
    	  edge("A", "E"),
    	  edge("A", "H"),
    	  edge("E", "F"),
    	  edge("E", "G"),
    	  edge("E", "I"),
    	  edge("E", "J")
    	  
    	];	    
    render(tree(nodes, edges, gap(10), child(top()), lineWidth(1), fillColor("black")));
}

public void tree8(){
   nodes =
     	[ ellipse(id("A"), width(20), height(20), fillColor("green"), popup("Text For ellipse A")),
     	  ellipse(id("B"), width(20), height(20), fillColor("red"), popup("Text For ellipse B")),
     	  ellipse(id("C"), width(20), height(20), fillColor("blue"), popup("Text For ellipse C")),
     	  ellipse(id("D"), width(20), height(20), fillColor("purple"), popup("Text For ellipse D")),
     	  ellipse(id("E"), width(20), height(20), fillColor("lightblue"), popup("Text For ellipse E")),
     	  ellipse(id("F"), width(20), height(20), fillColor("orange"), popup("Text For ellipse F")),
     	  ellipse(id("G"), width(20), height(20), fillColor("brown"), popup("Text For ellipse G")),
     	  ellipse(id("H"), width(20), height(20), fillColor("black"), popup("Text For ellipse H")),
     	  ellipse(id("I"), width(20), height(20), fillColor("grey"), popup("Text For ellipse I")),
     	  ellipse(id("J"), width(20), height(20), fillColor("white"), popup("Text For ellipse J"))
     	];
    edges = 
    	[ edge("A", "B"),
    	  edge("B", "C"),
    	  edge("B", "D"),
    	  edge("A", "E"),
    	  edge("A", "H"),
    	  edge("E", "F"),
    	  edge("E", "G"),
    	  edge("E", "I"),
    	  edge("E", "J")
    	  
    	];	    
    render(tree(nodes, edges, gap(10), child(top()), lineWidth(1), fillColor("black")));
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
    	    
    render(tree(nodes, edges,gap(10), child(top()), lineWidth(1)));
}

public Figure T1(){
 nodes1 =
     	[ box(id("A"), size(20), fillColor("green"), popup("1:Text For Box A")),
     	  box(id("B"), size(20), fillColor("red"), popup("1:Text For Box B")),
     	  box(id("C"), size(20), fillColor("blue"), popup("1:Text For Box C")),
     	  box(id("D"), size(20), fillColor("purple"), popup("1:Text For Box D")),
     	  box(id("E"), size(20), fillColor("lightblue"), popup("1:Text For Box E")),
     	  box(id("F"), size(20), fillColor("orange"), popup("1:Text For Box F")),
     	  box(id("G"), size(20), fillColor("brown"), popup("1:Text For Box G")),
     	  box(id("H"), size(20), fillColor("black"), popup("1:Text For Box H")),
     	  box(id("I"), size(20), fillColor("grey"), popup("1:Text For Box I")),
     	  box(id("J"), size(20), fillColor("white"), popup("1:Text For Box J"))
     	];
    edges1 = 
    	[ edge("A", "B"),
    	  edge("B", "C"),
    	  edge("B", "D"),
    	  edge("A", "E"),
    	  edge("A", "H"),
    	  edge("E", "F"),
    	  edge("E", "G"),
    	  edge("E", "I"),
    	  edge("E", "J")
    	];	
    	
   return tree(nodes1, edges1, gap(10), child(top()), lineWidth(1), fillColor("black"));    
}

public Figure T2(){
  nodes2 =
     	[ ellipse(id("A"), size(25), fillColor("green"), popup("2:Text For ellipse A")),
     	  ellipse(id("B"), size(25), fillColor("red"), popup("2:Text For ellipse B")),
     	  ellipse(id("C"), size(25), fillColor("blue"), popup("2:Text For ellipse C")),
     	  ellipse(id("D"), size(25), fillColor("purple"), popup("2:Text For ellipse D")),
     	  ellipse(id("E"), size(25), fillColor("lightblue"), popup("2:Text For ellipse E")),
     	  ellipse(id("F"), size(25), fillColor("orange"), popup("2:Text For ellipse F")),
     	  ellipse(id("G"), size(25), fillColor("brown"), popup("2:Text For ellipse G")),
     	  ellipse(id("H"), size(25), fillColor("black"), popup("2:Text For ellipse H")),
     	  ellipse(id("I"), size(25), fillColor("grey"), popup("2:Text For ellipse I")),
     	  ellipse(id("J"), size(25), fillColor("white"), popup("2:Text For ellipse J")),
     	  ellipse(id("K"), size(25), fillColor("pink"), popup("2:Text For ellipse K")),
     	  ellipse(id("L"), size(25), fillColor("snow"), popup("2:Text For ellipse L"))
     	];
    edges2 = 
    	[ edge("A", "B"),
    	  edge("B", "C"),
    	  edge("B", "D"),
    	  edge("A", "E"),
    	  edge("A", "H"),
    	  edge("E", "F"),
    	  edge("E", "G"),
    	  edge("E", "I"),
    	  edge("E", "J"),
    	  edge("J", "K"),
    	  edge("K", "L")
    	];	    
    return tree(nodes2, edges2, gap(10), child(top()), lineWidth(1), fillColor("black"));
}

public void tree10a(){
    render(hcat([T1(), T2()], child(top()), gap(20), child(top())));
}

public void tree10b(){
    render(hcat([T1(), T2()], child(top()), gap(20)));
}

public void tree10c(){
    render(hcat([T1(), T2()], child(top()), gap(20), child(bottom())));
}

public void tree10d(){
    render(vcat( [T1(), T2()], child(top()), gap(20), child(left())));
}

public void tree10e(){
    render(vcat( [T1(), T2()], child(top()), gap(20), child(center())));
}
public void tree10f(){
    render(vcat([T1(), T2()], child(top()), gap(20), child(right())));
}


