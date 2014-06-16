module experiments::vis2::Translate

import experiments::vis2::Figure;
import experiments::vis2::Properties;
import util::Math;
import IO;
import List;

/*
 * Translate a Figure to Javascript in two steps:
 * 1. trFig: Figure -> list[PRIM]
 * 2. trPrims: list[PRIM] -> str
 */

// The graphical primitives that are defined in the library (vis2/lib)

data PRIM =
	  rect_prim(BBox bb, FProperties fp)
	| circle_prim(BBox bb, FProperties fp)
	| barchart_prim(BBox bb, FProperties fp)
	| scatterplot_prim(BBox bb, FProperties fp)
	| graph_prim(BBox bb,  list[str] nodes, Edges edges, FProperties fp)
	| define(str id, list[PRIM] prims)
	| use(str id, BBox bb)
	;
	
// Translation a list of PRIMs to one HTML page
	
str trPrims(str title, int width, int height, list[PRIM] prims){
	// TODO: get rid of this absolute path
	vis2 = "/Users/paulklint/git/rascal/src/org/rascalmpl/library/experiments/vis2";
	
	return "\<html\>
		'\<head\>
        '	\<title\><title>\</title\>
        '	\<script src=\"http://d3js.org/d3.v3.min.js\" charset=\"utf-8\"\>\</script\>
        '	\<script src=\"<vis2>/lib/Figure.js\"\>\</script\>
        '	\<script src=\"<vis2>/lib/Barchart.js\"\>\</script\>
        '	\<script src=\"<vis2>/lib/Scatterplot.js\"\>\</script\>
        '	\<script src=\"<vis2>/lib/Graph.js\"\>\</script\>
        '	\<link rel=\"stylesheet\" type=\"text/css\" href=\"<vis2>/lib/Figure.css\"\>
		'\</head\>
		'\<body\>
		'\<script\>
		'	var svg = d3.select(\"body\").append(\"svg\").attr(\"width\", <width>).attr(\"height\", <height>);
		'	<for(prim <- prims){>
		'   	<trPrim(prim)><}>
		'\</script\>
			
		'\</body\>
		'\</html\>
		";
}

// Translate one PRIM to JS

str trPrim(rect_prim(BBox bb, FProperties fp)) = "makeRect(svg, <bb.x>, <bb.y>, <bb.width>, <bb.height>, <trProps(fp)>);";

str trPrim(barchart_prim(BBox bb, FProperties fp)) = "makeBarchart(svg, <bb.x>, <bb.y>, <bb.width>, <bb.height>, <trProps(fp)>);";

str trPrim(scatterplot_prim(BBox bb, FProperties fp)) = "makeScatterplot(svg, <bb.x>, <bb.y>, <bb.width>, <bb.height>, <trProps(fp)>);";

str trPrim(graph_prim(BBox bb, list[str] nodes, Edges edges, FProperties fp)) {
	graphProps = "{ nodes: <nodes>, edges: [" + intercalate(",", ["{source: <v1>, target: <v2>}" | edge(v1, v2, *ps) <- edges]) + "] }";
	return "makeGraph(svg, <bb.x>, <bb.y>, <bb.width>, <bb.height>, combine(<graphProps>,<trProps(fp)>));";
}

str trPrim(define(str id, list[PRIM] prims, FProperties fp){

}

default str trPrim(p) {
	throw "trPrim: no rule for <p>";	
}


// Size computation of nestes figures

BBox reduce(bb, FProperties fps){
	<width, height> = getSize(fps, <bb.width, bb.height>);
	return bbox(bb.x, bb.y, width, height);
}

// Compute the size of a Figure

Size sizeOf(_box(FProperties fps), FProperty pfps...) {
	afps = combine(fps, pfps);
	<w, h> = getSize(afps, <10, 10>);
	if(hasPos(afps)){
		<x, y> = getPos(afps);
		return <x + w, y + h>;
	}
	return <w, h>;
}

Size sizeOf(_box(Figure inner, FProperties fps), FProperty pfps...) {
    afps = combine(fps, pfps);
    <outer_width, outer_height> = getSize(afps);
	<inner_width, inner_height> = sizeOf(inner, afps);
	<xgap, ygap> = getGap(afps);
	return <max(outer_width, inner_width + 2*xgap), max(outer_height, inner_height + 2*ygap)>;
}

Size sizeOf(_hcat(list[Figure] figs, FProperties fps),  FProperty pfps...) {
	afps = combine(fps, pfps);
	<hg, vg> = getGap(afps);
    sizes = [ sizeOf(f, afps) | f <- figs ];
    w = (0 | it + hg + sz.width | sz <- sizes) - hg;
    h = (0 | max(it, sz.height) | sz <- sizes); 
    return <w, h>;
}

Size sizeOf(_vcat(list[Figure] figs, FProperties fps),  FProperty pfps...) {
	afps = combine(fps, pfps);
	<hg, vg> = getGap(afps);
    sizes = [ sizeOf(f, afps) | f <- figs ];
    w = (0 | max(it, sz.width) | sz <- sizes);
    h = (0 | it + vg + sz.height | sz <- sizes) - vg; 
    return <w, h>;
}

Size sizeOf(_barchart(FProperties fps),  FProperty pfps...){
	afps = combine(fps, pfps);
	res = getSize(afps, <200,200>);
	println("sizeOF _barchart: <res>");
	return res;
}

Size sizeOf(_scatterplot(FProperties fps),  FProperty pfps...){
	afps = combine(fps, pfps);
	res = getSize(afps, <200,200>);
	println("sizeOF _scatterplot: <res>");
	return res;
}

Size sizeOf(_graph(Figures nodes, Edges edges, FProperties fps),  FProperty pfps...){
	afps = combine(fps, pfps);
	res = getSize(afps, <200,200>);
	println("sizeOF _graph: <res>");
	
	return res;
}

// Translates a Figure to a list of PRIMs

list[PRIM] trFig(Figure f) {
	<w, h> = sizeOf(f,  getDefaultProperties());
	<x, y> = getPos(f.props, <0, 0>);
	return tr(f, bbox(x, y, w, h), getDefaultProperties());
}

list[PRIM] tr(_box(FProperties fps), bb, FProperties pfps) {
	fps1 = combine(fps, pfps);
	return [rect_prim(reduce(bb, fps1), fps1)];
}

list[PRIM] tr(_box(Figure inner, FProperties fps), bb, FProperties pfps) {
	println("box with inner: inner = <inner>, bb = <bb>, fps = <fps>, pfps = <pfps>");
	afps = combine(fps, pfps);
	<outer_width, outer_height> = getSize(afps, getSize(bb));
	<xgap, ygap> = getGap(afps);
	println("box with inner: gap: <xgap>, <ygap>");
	<inner_width, inner_height> = sizeOf(inner, afps);
	outer_width = max(inner_width + 2 * xgap, outer_width);
	outer_height = max(inner_height + 2 * ygap, outer_height);
	
	otrans = rect_prim(bbox(bb.x, bb.y, outer_width, outer_height), afps);
	
	println("box with inner: otrans = <otrans>");
	
	ipos = align(getPos(bb), <outer_width, outer_height>, <inner_width, inner_height>, <xgap, ygap>, getAlign(afps));
	
	itrans = tr(inner, bbox(ipos.x, ipos.y, inner_width, inner_height), combine(fps, afps));
	println("box with inner: itrans = <itrans>");
	println("box with inner: outer_width: <outer_width>, outer_height: <outer_height>");
	return otrans + itrans;
}

list[PRIM] tr(_barchart(FProperties fps), bb, FProperties pfps) {
	println("tr _barchart: fps <fps>, bb <bb>, pfps <pfps>");
	fps1 = combine(fps, pfps);
	return [barchart_prim(bb, fps1)];
}

list[PRIM] tr(_scatterplot(FProperties fps), bb, FProperties pfps) {
	println("tr _scatterplot: fps <fps>, bb <bb>, pfps <pfps>");
	fps1 = combine(fps, pfps);
	return [scatterplot_prim(bb, fps1)];
}

list[PRIM] tr(_graph(Figures nodes, Edges edges, FProperties fps), bb, FProperties pfps) {
	println("tr _graph: fps <fps>, bb <bb>, pfps <pfps>");
	fps1 = combine(fps, pfps);
	pref = "XXX";
	defs = [ define("<pref>:<i>", trFig(nodes[i])) | i <- index(nodes)];
	ids = ["<pref>:<i>" | i <- index(nodes) ];
	res =  defs + [graph_prim(bb, ids, edges, fps1)];
	println("_graph ==\> <res>");
	return res;
}

Pos align(Pos position, Size container, Size fig, Gap gap, Align align){
	println("align: position = <position>, container = <container>, fig = <fig>, gap = <gap>, align = <align>");
	dx = dy = 0;
	switch(align.xalign){
		case left(): 	dx = gap.width;
		case hcenter():	dx = (container.width - fig.width)/2;
		case right():	dx = container.width - fig.width - gap.width;
	}
	
	switch(align.yalign){
		case top(): 	dy = gap.height;
		case vcenter():	dy = (container.height - fig.height)/2;
		case bottom():	dy = container.height - fig.height - gap.height;
	}
	res = <position.x + dx, position.y + dy>;
	
	println("res = <res>");
	return res;
}

Pos halign(Pos position, Size container, Size fig, Gap gap, Align align){
	println("halign: position = <position>, container = <container>, fig = <fig>,  gap = <gap>, align = <align>");
	dx = 0;
	switch(align.xalign){
		case left(): 	dx = gap.width;
		case hcenter():	dx = (container.width - fig.width)/2;
		case right():	dx = container.width - fig.width - gap.width;
	}
	
	res = <position.x + dx, position.y>;
	println("res = <res>");
	return res;
}

Pos valign(Pos position, Size container, Size fig, Gap gap, Align align){
	println("valign: position = <position>, container = <container>, fig = <fig>,  gap = <gap>, align = <align>");
	dy = 0;
	switch(align.yalign){
		case top(): 	dy = gap.height;
		case vcenter():	dy = (container.height - fig.height)/2;
		case bottom():	dy = container.height - fig.height - gap.height;
	}
	res = <position.x, position.y + dy>;
	println("res = <res>");
	return res;
}

list[PRIM] tr(_hcat(list[Figure] figs, FProperties fps),  BBox bb, FProperty pfps...){
	afps = combine(fps, pfps);
	int x = bb.x;
	int y = bb.y;
	<outer_width, outer_height> = getSize(bb);
	<xgap, ygap> = getGap(afps);
	tfigs = [];
	for(f <- figs){
		<f_width, f_height> = sizeOf(f, afps);
		p = valign(<x,y>, <outer_width, outer_height>, <f_width, f_height>, <0, 0>, getAlign(combine(f.props, afps)));
		tfigs += tr(f, bbox(p.x, p.y, f_width, f_height), afps);
		x += xgap + f_width;
	}
	return tfigs;
}

list[PRIM] tr(_vcat(list[Figure] figs, FProperties fps),  BBox bb, FProperty pfps...){
	afps = combine(fps, pfps);
	int x = bb.x;
	int y = bb.y;
	<outer_width, outer_height> = getSize(bb);
	<xgap, ygap> = getGap(afps);
	tfigs = [];
	for(f <- figs){
		<f_width, f_height> = sizeOf(f, afps);
		p = halign(<x,y>, <outer_width, outer_height>, <f_width, f_height>, <0, 0>, getAlign(combine(f.props, afps)));
		tfigs += tr(f, bbox(p.x, p.y, f_width, f_height), afps);
		y += ygap + f_height;
	}
	return tfigs;
}