module util::tasks::Vis

import vis::Figure;
import vis::Render;
import util::tasks::Manager;
import IO;
map[str,Color] nodeColor = (
	"DesugaredTree" : rgb(64,200,64),
	"ImplodedTree" : rgb(64,255,64),
	"NameInfo" : rgb(64,64,200),
	"DefInfo" : rgb(200,64,64),
	"Flattened" : rgb(200,200,64),
	"TopDecl" : rgb(128,128,128),
	"Scope" : rgb(240,240,64),
	"Contents" : rgb(64,128,64)
	);
	
map[int,Color] statusColor = (
	0 : rgb(96,255,96),
	1 : rgb(255,255,96),
	2 : rgb(255,192,96),
	3 : rgb(255,96,96)
	);
	 
public void visDepsFromTr(Transaction tr) {
	visDepsFromGraph(getDependencyGraph(tr));
}

public void visDepsFromGraph(tuple[rel[str,str,str,int],rel[str,str,str]] g) {
	Figures figs = [factNode(nodeId,name,longName,status) | <nodeId,name,longName,status> <- g[0]];
	Edges edges = [arrow(from, to, typ) | <from,to,typ> <- g[1]];
	render(graph(figs, edges, size(500), gap(50), hint("layered")));
}


private FProperty popup(str s){
	return mouseOver(box(text(s), gap(3,1), lineWidth(0), fillColor("yellow")));
}

private Edge arrow(str from, str to, str typ) {
	if(typ == "\<-\>")
		return edge(to, from, toArrow(), fromArrow());
	else
		return edge(to, from, toArrow());
}
private FProperty toArrow() {
	return toArrow(shapeDiamond(size(4)));
//	return toArrow(shape([vertex(0,0), vertex(3,5), vertex(6,0)], shapeConnected(true)));
}

private FProperty fromArrow() {
	return fromArrow(ellipse(size(4)));
}

private Color getColor(str name) {
	return nodeColor[name] ? rgb(128,128,128);
}

private Color getColor(int status) {
	return statusColor[status] ? rgb(0, 0, 0);
}

private Figure factNode(str nodeId, str name, str longName, int status) {
	return box(text(name), id(nodeId),
			size(0),
			gap(4), 
			lineColor(getColor(name)),
			fillColor(getColor(status)),
			lineWidth(2),
			fontColor("black"),
			fontSize(8),
			popup(longName));
}
