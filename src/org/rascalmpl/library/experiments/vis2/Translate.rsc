module experiments::vis2::Translate

import experiments::vis2::Figure;
import experiments::vis2::Properties;
import Node;
import IO;
import List;

/*
 * Translate a Figure to HTML + JSON
 */

// Translation a figure to one HTML page

public str fig2html(str title, Figure fig, loc site=|http://localhost:8081|){
	// TODO: get rid of this absolute path
	vis2 = "/Users/paulklint/git/rascal/src/org/rascalmpl/library/experiments/vis2";
	
	init_callbacks();
	fig_site = site;
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

/******************** Translate figure primitives ************************************/

str trJson(_box(FProperties fps)) = "{figure: \"box\" <trPropsJson(fps)> }";

str trJson(_box(Figure inner, FProperties fps)) = "{figure: \"box\" <trPropsJson(fps,sep=", ")> inner: <trJson(inner)> }";

str trJson(_text(str txt, FProperties fps)) = "{figure: \"text\", textValue: \"<txt>\"<trPropsJson(fps)> }";

str trJson(_text(str() txt, FProperties fps)) = trJson(_text(txt(), fps));

str trJson(_hcat(list[Figure] figs, FProperties fps)) = "{figure: \"hcat\"<trPropsJson(fps, sep=", ")> inner: [<intercalate(", ", [trJson(f) | f <- figs])> ] }";

str trJson(_vcat(list[Figure] figs, FProperties fps)) = "{figure: \"vcat\"<trPropsJson(fps, sep=", ")> inner: [<intercalate(", ", [trJson(f) | f <- figs])>] }";

str trJson(_barchart(FProperties fps)) = "{figure: \"barchart\" <trPropsJson(fps)> }";

str trJson(_scatterplot(FProperties fps)) = "{figure: \"scatterplot\" <trPropsJson(fps)> }";

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


// ---------- textfield ----------

str trJson(_textfield(void (str) scallback, FProperties fps)) {
 id = def_callback_str(scallback);
 return "{figure: \"textfield\" <trPropsJson(fps, sep=", ")> site: \"<getSite()>\", callback: \"<id>\" }";
} 

default str trJson(Figure f) { throw "trJson: cannot translate <f>"; }

/**************** Tranlate properties *************************/

str trPropsJson(FProperties fps str sep = ""){
	res = "";
	
	for(fp <- fps){
		attr = getName(fp);
			t = trPropJson(fp);
			if(t != "")
				res += ", " + t;
	}
	return res + sep;
}

str trPropJson(pos(int xpos, int ypos)) 		= "";
//str trPropJson(size(int xsize, int ysize))	= "width: <xsize>, height <ysize>";

str trPropJson(gap(int width, int height)) 		= "hgap: <width>, vgap: <height>";

str trPropJson(align(HAlign xalign, VAlign yalign)){
	xa = 0.5;
	switch(xalign){
		case left():	xa = 0.0;
		case right():	xa = 1.0;
	}
	ya = 0.5;
	switch(yalign){
		case top():		ya = 0.0;
		case bottom():	ya = 1.0;
	}
	return "halign: <xa>, valign: <ya>";
}	

str trPropJson(lineWidth(int n)) 				= "lineWidth: <n>";
str trPropJson(lineStyle(list[int] dashes))		= "lineStyle: <dashes>";
str trPropJson(fillColor(str s)) 				= "fillColor: \"<s>\"";
str trPropJson(fillColor(str() sc)) 			= "fillColor: \"<sc()>\"";

str trPropJson(lineColor(str s))				= "lineColor:\"<s>\"";
str trPropJson(lineOpacity(real r))				= "lineOpacity:\"<r>\"";
str trPropJson(fillOpacity(real r))				= "fill_opacity:\"<r>\"";
str trPropJson(rounded(int rx, int ry))			= "rx: <rx>, ry: <ry>";
str trPropJson(dataset(list[num] values1)) 		= "dataset: <values1>";
str trPropJson(dataset(lrel[num,num] values2))	= "dataset: [" + intercalate(",", ["[<v1>,<v2>]" | <v1, v2> <- values2]) + "]";

str trPropJson(font(str fontName))				= "font: \"<fontName>\"";
str trPropJson(fontSize(int fontSize))			= "fontSize: <fontSize>";
str trPropJson(fontBaseline(str s))				= "???";
str trPropJson(textAngle(num  r))				= "???";

str trPropJson(onClick(void () vcallback))		= "onClick: \"<def_callback(vcallback)>\", site: \"<getSite()>\"";

default str trPropJson(FProperty fp) 			= (size(int xsize, int ysize) := fp) ? "width: <xsize>, height: <ysize>" : "unknown: <fp>";

/******************* Utilities for callback management ********************/

private loc fig_site = |http://localhost:8081|;

public str getSite() = "<fig_site>"[1 .. -1];

// void()

private int ncallback = 0;
private map[str, void()] callbacks = ();
private map[void(), str] seen_callbacks = ();

private void init_callback(){
	ncallback = 0;
	callbacks = ();
	seen_callbacks = ();
}

public str def_callback(void () vcallback){
	if(!callbacks?){
		init_callbacks();
	}
	if(seen_callbacks[vcallback]?){
		return seen_callbacks[vcallback];
	}
	ncallback += 1;
	str cid = "<ncallback>";
	println("callbacks = <callbacks>, cid = <cid>");
	callbacks[cid] = vcallback;
	seen_callbacks[vcallback] = cid;
	println("def_callback: <cid>, <callbacks>, <seen_callbacks>");
	return cid;
}

public void do_callback(str fun, map[str, str] parameters){
    println("do_callback: <fun>, <parameters>, <callbacks>");
	callbacks[fun]();
}

// void(str s)

private int ncallback_str = 0;
private map[str, void(str)] callbacks_str = ();
private map[void(str), str] seen_callbacks_str = ();

private void init_callback_str(){
	ncallback_str = 0;
	callbacks_str = ();
	seen_callbacks_str = ();
}

private str def_callback_str(void (str) scallback){
	if(!callbacks_str?){
		init_callbacks();
	}
	if(seen_callbacks_str[scallback]?){
		return seen_callbacks_str[scallback];
	}
	ncallback_str += 1;
	id = "<ncallback_str>";
	callbacks_str[id] = scallback;
	seen_callbacks_str[scallback] = id;
	return id;
}

public void do_callback_str(str fun, map[str, str] parameters){
    println("do_callback_str: <fun>, <parameters>, <callbacks>");
	callbacks_str[fun](parameters["callback_str_arg"]);
}

private void init_callbacks(){
	println("init_callbacks");
	init_callback();
	init_callback_str();
}
