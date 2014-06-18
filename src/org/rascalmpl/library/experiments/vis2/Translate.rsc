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
 
// List of library components to be included

private list[str] libJS = ["Barchart", "Button", "Figure", "Graph", "Scatterplot",  "Texteditor", "Textfield"];

// Translation a list of PRIMs to one HTML page

public str trPrims(str title, int width, int height, list[PRIM] prims){
	// TODO: get rid of this absolute path
	vis2 = "/Users/paulklint/git/rascal/src/org/rascalmpl/library/experiments/vis2";
	
	prims = "<for(prim <- prims){>
			'	<trPrim(prim, "svg")><}>";
	println(prims);
	
	return "\<html\>
		'\<head\>
        '	\<title\><title>\</title\>
        '	\<script src=\"http://d3js.org/d3.v3.min.js\" charset=\"utf-8\"\>\</script\>
        '	<for(file <- libJS){>
        '	\<script src=\"<vis2>/lib/<file>.js\"\>\</script\>  <}>
        '	\<script src=\"<vis2>/CodeMirror/lib/codemirror.js\"\>\</script\>
        '
        '	\<link rel=\"stylesheet\" type=\"text/css\" href=\"<vis2>/lib/Figure.css\"\>
        '	\<link rel=\"stylesheet\" href=\"<vis2>/CodeMirror/lib/codemirror.css\"\>
		'\</head\>
		'\<body\>
		'\<script\>
		'	svg = d3.select(\"body\").append(\"svg\").attr(\"width\", <width>).attr(\"height\", <height>);
		'	defs = svg.append(\"defs\");
		'	<prims>
		'
		'	var editor = CodeMirror.fromTextArea(document.getElementById(\"texteditor\"), {
    	'		mode: \"javascript\",
    	'		lineNumbers: true,
    	'		value: \"function myScript(){return 100;}\\n\"
  		'	});
		'\</script\>
		'\</body\>
		'\</html\>
		";
}

// The graphical primitives that are defined in the library (vis2/lib) and their translation

data PRIM = rect_prim(BBox bb, FProperties fp);

private str trPrim(rect_prim(BBox bb, FProperties fp), str selection) = 
	"makeRect(<selection>, <bb.x>, <bb.y>, <bb.width>, <bb.height>, <trProps(fp)>);";

data PRIM = circle_prim(BBox bb, FProperties fp);

// TODO

data PRIM = text_prim(BBox bb, str txt, int ascent, FProperties fp);

private str trPrim(text_prim(BBox bb, str txt, int ascent, FProperties fp), str selection) = 
	"makeText(<selection>, <bb.x>, <bb.y+ascent>, <bb.width>, <bb.height>, { text_value: \"<txt>\", <trPropsContent(fp)> });";

data PRIM = barchart_prim(BBox bb, FProperties fp);

private str trPrim(barchart_prim(BBox bb, FProperties fp), str selection) = 
	"makeBarchart(<selection>, <bb.x>, <bb.y>, <bb.width>, <bb.height>, <trProps(fp)>);";
	
data PRIM = scatterplot_prim(BBox bb, FProperties fp);

private str trPrim(scatterplot_prim(BBox bb, FProperties fp), str selection) = 
	"makeScatterplot(<selection>, <bb.x>, <bb.y>, <bb.width>, <bb.height>, <trProps(fp)>);";

data PRIM = graph_prim(BBox bb,  list[str] nodes, Edges edges, FProperties fp);

private str trPrim(graph_prim(BBox bb, list[str] nodes, Edges edges, FProperties fp), str selection) {
	graphProps = "nodes: [" + intercalate(", ", nodes) + "], edges: [" + intercalate(",", ["{source: <e.from>, target: <e.to>, <trPropsContent(e.props)> }" | Edge e <- edges]) + "]";
	return "makeGraph(<selection>, <bb.x>, <bb.y>, <bb.width>, <bb.height>, { <graphProps>, <trPropsContent(fp)> });";
}

data PRIM = texteditor_prim(BBox bb, FProperties fp);

private str trPrim(texteditor_prim(BBox bb, FProperties fp), str selection) = 
	"makeTexteditor(<selection>, <bb.x>, <bb.y>, <bb.width>, <bb.height>, <trProps(fp)>);";
	
data PRIM = button_prim(BBox bb, str label, str callback_id, FProperties fp);

str trPrim(button_prim(BBox bb, str label, str id, FProperties fp), str selection) = 
	"makeButton(<selection>, <bb.x>, <bb.y>, <bb.width>, <bb.height>, { label: \"<label>\", callback: \"<id>\", site: \"http://<fig_site.authority>\", <trPropsContent(fp)> });";

data PRIM = textfield_prim(BBox bb, str callback_id, FProperties fp);

str trPrim(textfield_prim(BBox bb, str id, FProperties fp), str selection) = 
	"makeTextfield(<selection>, <bb.x>, <bb.y>, <bb.width>, <bb.height>, { callback: \"<id>\", site: \"http://<fig_site.authority>\", <trPropsContent(fp)> });";

data PRIM = define(BBox bb, str id, str class, list[PRIM] prims);

private str trPrim(define(BBox bb, str id, str class, list[PRIM] prims), str selection){
	return "var <id> = svg.append(\"g\").attr(\"class\",\"<class>\").attr(\"id\", \"<id>\").attr(\"width\", <bb.width>).attr(\"height\", <bb.height>);\n"
	+ intercalate(";\n", [trPrim(p, id) | p <- prims]);
}

data PRIM = use(str id, BBox bb);

// TODO

// Default rule for missing cases

private default str trPrim(p, str selection) {
	throw "trPrim: no rule for <p>";	
}

/**********************************************************************************************************/

// Translates a Figure to a list of PRIMs

private loc fig_site = |http://localhost:8081|;

public list[PRIM] trFig(Figure f loc site = |http://localhost:8081|) {
	fig_site = site;
	<w, h> = sizeOf(f,  getDefaultProperties());
	<x, y> = getPos(f.props, <0, 0>);
	return tr(f, bbox(x, y, w, h), getDefaultProperties());
}

// Size computation and translation of figures

private BBox reduce(bb, FProperties fps){
	<width, height> = getSize(fps, <bb.width, bb.height>);
	return bbox(bb.x, bb.y, width, height);
}

default bool isComposition(Figure f) = false;

// ---------- box ----------

Size sizeOf(_box(FProperties fps), FProperty pfps...) {
	afps = combine(fps, pfps);
	<w, h> = getSize(afps, <10, 10>);
	lw = getLineWidth(afps);
	w += lw;
	h += lw;
	if(hasPos(afps)){
		<x, y> = getPos(afps);
		return <x + w, y + h>;
	}
	return <w, h>;
}

Size sizeOf(_box(Figure inner, FProperties fps), FProperty pfps...) {
    afps = combine(fps, pfps);
    <inner_width, inner_height> = sizeOf(inner, afps);
	<xgap, ygap> = getGap(afps);
	lw = getLineWidth(afps);
	
    if(hasSize(afps)){
	    <outer_width, outer_height> = getSize(afps);
		return <max(outer_width + lw, inner_width + 2*xgap), max(outer_height + lw, inner_height + 2*ygap)>;
	} else {
		return <inner_width + 2*xgap + lw, inner_height + 2*ygap + lw>;
	}
}

private list[PRIM] tr(_box(FProperties fps), bb, FProperties pfps) {
	fps1 = combine(fps, pfps);
	return [rect_prim(reduce(bb, fps1), fps1)];
}

private list[PRIM] tr(_box(Figure inner, FProperties fps), bb, FProperties pfps) {
	println("box with inner: inner = <inner>, bb = <bb>, fps = <fps>, pfps = <pfps>");
	afps = combine(fps, pfps);
	ifps = isComposition(inner) ? afps : combine(inner.props, afps);
	<outer_width, outer_height> = getSize(afps, getSize(bb));
	<xgap, ygap> = getGap(afps);
	println("box with inner: gap: <xgap>, <ygap>");
	<inner_width, inner_height> = sizeOf(inner, afps);
	outer_width = max(inner_width + 2 * xgap, outer_width);
	outer_height = max(inner_height + 2 * ygap, outer_height);
	
	otrans = rect_prim(bbox(bb.x, bb.y, outer_width, outer_height), afps);
	
	println("box with inner: otrans = <otrans>");
	println("box with inner: <ifps>, <getAlign(ifps)>");
	ipos = align(getPos(bb), <outer_width, outer_height>, <inner_width, inner_height>, <xgap, ygap>, getAlign(ifps));
	
	itrans = tr(inner, bbox(ipos.x, ipos.y, inner_width, inner_height), combine(fps, afps));
	println("box with inner: itrans = <itrans>");
	println("box with inner: outer_width: <outer_width>, outer_height: <outer_height>");
	return otrans + itrans;
}

// ---------- text ----------

Size sizeOf(_text(computedStr txt, FProperties fps),  FProperty pfps...){
	afps = combine(fps, pfps);
	sz = getSize(afps, <100,100>);
	println("sizeOF text: <sz>");
	return sz;
}

Size sizeOf(_text(str txt, FProperties fps),  FProperty pfps...){
	afps = combine(fps, pfps);
	sz = textSize(txt, getFont(afps), getFontSize(afps));
	println("sizeOF text: <sz>");
	return sz;
}

private list[PRIM] tr(_text(computedStr txt, FProperties fps), bb, FProperties pfps) = tr(_text(txt(), fps), bb, pfps);

private list[PRIM] tr(_text(str txt, FProperties fps), bb, FProperties pfps) {
	println("tr _text: fps <fps>, bb <bb>, pfps <pfps>");
	afps = combine(fps, pfps);
	return [text_prim(bb, txt, fontAscent(getFont(afps),getFontSize(afps)), afps)];
}

// ---------- hcat ----------

Size sizeOf(_hcat(list[Figure] figs, FProperties fps),  FProperty pfps...) {
	afps = combine(fps, pfps);
	<hg, vg> = getGap(afps);
    sizes = [ sizeOf(f, afps) | f <- figs ];
    w = (0 | it + hg + sz.width | sz <- sizes) - hg;
    h = (0 | max(it, sz.height) | sz <- sizes); 
    return <w, h>;
}

bool isComposition(_hcat(_, _)) = true;

private list[PRIM] tr(_hcat(list[Figure] figs, FProperties fps),  BBox bb, FProperty pfps...){
	afps = combine(fps, pfps);
	int x = bb.x;
	int y = bb.y;
	<outer_width, outer_height> = getSize(bb);
	<xgap, ygap> = getGap(afps);
	tfigs = [];
	for(f <- figs){
		<f_width, f_height> = sizeOf(f, afps);
		p = valign(<x,y>, <outer_width, outer_height>, <f_width, f_height>, <0, 0>, getAlign(afps));
		tfigs += tr(f, bbox(p.x, p.y, f_width, f_height), afps);
		x += xgap + f_width;
	}
	return tfigs;
}

// ---------- vcat ----------

Size sizeOf(_vcat(list[Figure] figs, FProperties fps),  FProperty pfps...) {
	afps = combine(fps, pfps);
	<hg, vg> = getGap(afps);
    sizes = [ sizeOf(f, afps) | f <- figs ];
    w = (0 | max(it, sz.width) | sz <- sizes);
    h = (0 | it + vg + sz.height | sz <- sizes) - vg; 
    return <w, h>;
}

bool isComposition(_vcat(_, _)) = true;

private list[PRIM] tr(_vcat(list[Figure] figs, FProperties fps),  BBox bb, FProperty pfps...){
	afps = combine(fps, pfps);
	int x = bb.x;
	int y = bb.y;
	<outer_width, outer_height> = getSize(bb);
	<xgap, ygap> = getGap(afps);
	tfigs = [];
	for(f <- figs){
		<f_width, f_height> = sizeOf(f, afps);
		p = halign(<x,y>, <outer_width, outer_height>, <f_width, f_height>, <0, 0>, getAlign(afps));
		tfigs += tr(f, bbox(p.x, p.y, f_width, f_height), afps);
		y += ygap + f_height;
	}
	return tfigs;
}

// ---------- barchart ----------

Size sizeOf(_barchart(FProperties fps),  FProperty pfps...){
	afps = combine(fps, pfps);
	res = getSize(afps, <200,200>);
	println("sizeOF _barchart: <res>");
	return res;
}

private list[PRIM] tr(_barchart(FProperties fps), bb, FProperties pfps) {
	println("tr _barchart: fps <fps>, bb <bb>, pfps <pfps>");
	fps1 = combine(fps, pfps);
	return [barchart_prim(bb, fps1)];
}

// ---------- scatterplot ----------

Size sizeOf(_scatterplot(FProperties fps),  FProperty pfps...){
	afps = combine(fps, pfps);
	res = getSize(afps, <200,200>);
	println("sizeOF _scatterplot: <res>");
	return res;
}

private list[PRIM] tr(_scatterplot(FProperties fps), bb, FProperties pfps) {
	println("tr _scatterplot: fps <fps>, bb <bb>, pfps <pfps>");
	fps1 = combine(fps, pfps);
	return [scatterplot_prim(bb, fps1)];
}

// ---------- graph ----------

Size sizeOf(_graph(Figures nodes, Edges edges, FProperties fps),  FProperty pfps...){
	afps = combine(fps, pfps);
	res = getSize(afps, <200,200>);
	println("sizeOF _graph: <res>");
	
	return res;
}

private list[PRIM] tr(_graph(Figures nodes, Edges edges, FProperties fps), bb, FProperties pfps) {
	println("tr _graph: fps <fps>, bb <bb>, pfps <pfps>");
	fps1 = combine(fps, pfps);
	pref = "XXX";
	sizes = [sizeOf(nd, fps1) | nd <- nodes];
	defs = [ define(bbox(0, 0, sizes[i].width, sizes[i].height), "<pref>_<i>", "node", trFig(nodes[i])) | i <- index(nodes)];
	ids = ["<pref>_<i>" | i <- index(nodes) ];
	res =  defs + [graph_prim(bb, ids, edges, fps1)];
	println("_graph ==\> <res>");
	return res;
}

// ---------- texteditor ----------

Size sizeOf(_texteditor(FProperties fps),  FProperty pfps...){
	afps = combine(fps, pfps);
	res = getSize(afps, <200,200>);
	println("sizeOF texteditor: <res>");
	return res;
}

private list[PRIM] tr(_texteditor(FProperties fps), bb, FProperties pfps) {
	println("tr _texteditor: fps <fps>, bb <bb>, pfps <pfps>");
	fps1 = combine(fps, pfps);
	return [texteditor_prim(bb, fps1)];
}

// ---------- button ----------

Size sizeOf(_button(str label, void () vcallback, FProperties fps),  FProperty pfps...){
	afps = combine(fps, pfps);
	res = getSize(afps, <100,50>);
	println("sizeOF button: <res>");
	return res;
}

private list[PRIM] tr(_button(str label, void () vcallback, FProperties fps), bb, FProperties pfps) {
	println("tr _button: fps <fps>, bb <bb>, pfps <pfps>");
	afps = combine(fps, pfps);
	id = def_callback(vcallback);
	return [button_prim(bb, label, id, afps)];
}

// ---------- textfield ----------

Size sizeOf(_textfield(void (str) scallback, FProperties fps),  FProperty pfps...){
	afps = combine(fps, pfps);
	res = getSize(afps, <100,50>);
	println("sizeOF textfield: <res>");
	return res;
}

private list[PRIM] tr(_textfield(void (str) scallback, FProperties fps), bb, FProperties pfps) {
	println("tr _textfield: fps <fps>, bb <bb>, pfps <pfps>");
	fps1 = combine(fps, pfps);
	id = def_callback_str(scallback);
	return [textfield_prim(bb, id, fps1)];
}

// Utilities for callback management

// void()

private int ncallback = 0;
private map[str, void()] callbacks = ();
private map[void(), str] seen_callbacks = ();

public void do_callback(str fun, map[str, str] parameters){
    println("do_callback: <fun>, <parameters>, <callbacks>");
	callbacks[fun]();
}

private str def_callback(void () vcallback){
	if(seen_callbacks[vcallback]?)
		return seen_callbacks[vcallback];
	ncallback += 1;
	id = "<ncallback>";
	callbacks[id] = vcallback;
	seen_callbacks[vcallback] = id;
	return id;
}

// void(str s)

private int ncallback_str = 0;
private map[str, void(str)] callbacks_str = ();
private map[void(str), str] seen_callbacks_str = ();

public void do_callback_str(str fun, map[str, str] parameters){
    println("do_callback_str: <fun>, <parameters>, <callbacks>");
	callbacks_str[fun](parameters["callback_str_arg"]);
}

private str def_callback_str(void (str) scallback){
	if(seen_callbacks_str[scallback]?)
		return seen_callbacks_str[scallback];
	ncallback_str += 1;
	id = "<ncallback_str>";
	callbacks_str[id] = scallback;
	seen_callbacks_str[scallback] = id;
	return id;
}

// Utilities for alignment

private Pos align(Pos position, Size container, Size fig, Gap gap, Align align){
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

private Pos halign(Pos position, Size container, Size fig, Gap gap, Align align){
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

private Pos valign(Pos position, Size container, Size fig, Gap gap, Align align){
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