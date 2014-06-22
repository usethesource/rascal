module experiments::vis2::Translate

import experiments::vis2::Figure;
import experiments::vis2::Properties;
import util::Math;
import IO;
import List;
//import lang::json::IO;

/*
 * Translate a Figure to Javascript in two steps:
 * 1. trFig: Figure -> list[PRIM]
 * 2. trPrims: list[PRIM] -> str
 */
 
// List of library components to be included

private list[str] libJS = ["JSFigure"];

// Translation a list of PRIMs to one HTML page

public str fig2html(str title, Figure fig){
	// TODO: get rid of this absolute path
	vis2 = "/Users/paulklint/git/rascal/src/org/rascalmpl/library/experiments/vis2";
	
	fig_in_json = trJson(fig);
	println(fig_in_json);
	
	return "\<html\>
		'\<head\>
        '	\<title\><title>\</title\>
        '	\<link rel=\"stylesheet\" href=\"<vis2>/lib/reset.css\" /\>
        '	\<link rel=\"stylesheet\" href=\"<vis2>/lib/Figure.css\" /\>
        '	\<script src=\"http://d3js.org/d3.v3.min.js\" charset=\"utf-8\"\>\</script\>
        '	\<script src=\"<vis2>/JSFigure.js\"\>\</script\>
        
		'\</head\>
		'\<body\>
		'\<div id=\"figurearea\"\> 
		'\</div\>
		'\<script\>
		'	drawFigure(<fig_in_json>);
  		'\</script\>
		'\</body\>
		'\</html\>
		";
}



/**********************************************************************************************************/

// Translates a Figure to a list of PRIMs

private loc fig_site = |http://localhost:8081|;

//public list[PRIM] trFig(Figure f loc site = |http://localhost:8081|) {
//	fig_site = site;
//	<w, h> = sizeOf(f,  getDefaultProperties());
//	<x, y> = getPos(f.props, <0, 0>);
//	return tr(f, bbox(x, y, w, h), getDefaultProperties());
//}
//
//// Size computation and translation of figures
//
//private BBox reduce(bb, FProperties fps){
//	<width, height> = getSize(fps, <bb.width, bb.height>);
//	return bbox(bb.x, bb.y, width, height);
//}
//
//default bool isComposition(Figure f) = false;

// ---------- box ----------

str trJson(_box(FProperties fps)) = "{figure: \"box\" <trPropsJson(fps)> }";

str trJson(_box(Figure inner, FProperties fps)) = "{figure: \"box\" <trPropsJson(fps,sep=", ")> inner: <trJson(inner)> }";

// ---------- text ----------

str trJson(_text(str txt, FProperties fps)) = "{figure: \"text\", textValue: \"<txt>\"<trPropsJson(fps)> }";

// ---------- hcat ----------

str trJson(_hcat(list[Figure] figs, FProperties fps)) = "{figure: \"hcat\"<trPropsJson(fps, sep=", ")> inner: [<intercalate(", ", [trJson(f) | f <- figs])> ] }";

// ---------- vcat ----------

str trJson(_vcat(list[Figure] figs, FProperties fps)) = "{figure: \"vcat\"<trPropsJson(fps, sep=", ")> inner: [<intercalate(", ", [trJson(f) | f <- figs])>] }";


// ---------- barchart ----------

str trJson(_barchart(FProperties fps)) = "{figure: \"barchart\" <trPropsJson(fps)> }";

// ---------- scatterplot ----------

str trJson(_scatterplot(FProperties fps)) = "{figure: \"scatterplot\" <trPropsJson(fps)> }";


// ---------- graph ----------

//Size sizeOf(_graph(Figures nodes, Edges edges, FProperties fps),  FProperty pfps...){
//	afps = combine(fps, pfps);
//	res = getSize(afps, <200,200>);
//	println("sizeOF _graph: <res>");
//	
//	return res;
//}
//
//private list[PRIM] tr(_graph(Figures nodes, Edges edges, FProperties fps), bb, FProperties pfps) {
//	println("tr _graph: fps <fps>, bb <bb>, pfps <pfps>");
//	fps1 = combine(fps, pfps);
//	pref = "XXX";
//	sizes = [sizeOf(nd, fps1) | nd <- nodes];
//	defs = [ define(bbox(0, 0, sizes[i].width, sizes[i].height), "<pref>_<i>", "node", trFig(nodes[i])) | i <- index(nodes)];
//	ids = ["<pref>_<i>" | i <- index(nodes) ];
//	res =  defs + [graph_prim(bb, ids, edges, fps1)];
//	println("_graph ==\> <res>");
//	return res;
//}

str trJson(_graph(Figures nodes, Edges edges, FProperties fps)) = 
	"{figure: \"graph\" <trPropsJson(fps, sep=", ")> 
	' nodes: [<intercalate(", ", [trJson(f) | f <- nodes])>], 
	' edges: [<intercalate(", ", ["{source: <from>, target: <to>}"| _edge(from,to,efps) <- edges])>]
	'}";


// ---------- texteditor ----------

//Size sizeOf(_texteditor(FProperties fps),  FProperty pfps...){
//	afps = combine(fps, pfps);
//	res = getSize(afps, <200,200>);
//	println("sizeOF texteditor: <res>");
//	return res;
//}
//
//private list[PRIM] tr(_texteditor(FProperties fps), bb, FProperties pfps) {
//	println("tr _texteditor: fps <fps>, bb <bb>, pfps <pfps>");
//	fps1 = combine(fps, pfps);
//	return [texteditor_prim(bb, fps1)];
//}

// ---------- button ----------

//Size sizeOf(_button(str label, void () vcallback, FProperties fps),  FProperty pfps...){
//	afps = combine(fps, pfps);
//	res = getSize(afps, <100,50>);
//	println("sizeOF button: <res>");
//	return res;
//}
//
//private list[PRIM] tr(_button(str label, void () vcallback, FProperties fps), bb, FProperties pfps) {
//	println("tr _button: fps <fps>, bb <bb>, pfps <pfps>");
//	afps = combine(fps, pfps);
//	id = def_callback(vcallback);
//	return [button_prim(bb, label, id, afps)];
//}

// ---------- textfield ----------

//Size sizeOf(_textfield(void (str) scallback, FProperties fps),  FProperty pfps...){
//	afps = combine(fps, pfps);
//	res = getSize(afps, <100,50>);
//	println("sizeOF textfield: <res>");
//	return res;
//}
//
//private list[PRIM] tr(_textfield(void (str) scallback, FProperties fps), bb, FProperties pfps) {
//	println("tr _textfield: fps <fps>, bb <bb>, pfps <pfps>");
//	fps1 = combine(fps, pfps);
//	id = def_callback_str(scallback);
//	return [textfield_prim(bb, id, fps1)];
//}


default str trJson(Figure f) { throw "trJson: cannot translate <f>"; }

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

