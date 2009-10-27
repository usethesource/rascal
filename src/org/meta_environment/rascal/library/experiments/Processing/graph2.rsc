module experiments::Processing::graph

import experiments::Processing::Core;
import experiments::Processing::Graph;
import IO;
import Map;

rel[str,str] g1 = {<"a", "b">, <"a", "c">, <"a", "d">, <"d", "e">, <"e", "a">};

Graph G = "graph"();

public void graphSetup() {
println("graphSetup");
	size(1024, 768);                 // size of the screen
	font = createFont("Serif", 13);  // create and set font
	textFont(font);
	stroke(0);                     // all strokes will be black
 
	                                
	noLoop();                        // only draw once
}

int nodeColor = 0;
int edgeColor = 0;

void drawNode(int x, int y, str label, int count) {
println("drawNode(<x>, <y>, <label>, <count>)");
	fill(nodeColor);
	stroke(0);
	//strokeWeight(0.5);

	ellipse(x, y, 50, 50);
	int w = textWidth(label);

	//if (count > w+2) {
		fill(0);
		textAlign(CENTER, CENTER);
		text(label, x, y);
	//}
}

void drawEdge(int fromx, int fromy, int tox, int toy) {
println("drawEdge(<fromx>,<fromy>,<tox>,<toy>)");
	stroke(edgeColor);
	strokeWeight(0.35);
	line(fromx, fromy, tox, toy);
}

public void graphDraw() { 
    draw(G);                      // draw the graph
}

public void main(){
   	G = graph(g1, drawNode, drawEdge);
	processing(setup(graphSetup), draw(graphDraw));
}