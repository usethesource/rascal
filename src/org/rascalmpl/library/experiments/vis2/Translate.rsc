module experiments::vis2::Translate

import experiments::vis2::Figure;
import experiments::vis2::Properties;
import Node;
import String;
import IO;
import List;

/*
 * Translate a Figure to HTML + JSON
 */


// Translation a figure to one HTML page

public str fig2html(str title, map[str,str] state, Figure fig, loc site=|http://localhost:8081|){
	// TODO: get rid of this absolute path
	vis2 = "/Users/paulklint/git/rascal/src/org/rascalmpl/library/experiments/vis2";
	
	//init_callbacks();
	
	fig_site = site;
	
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
		'	askServer(\"<getSite()>/initial_figure\");
  		'\</script\>
		'\</body\>
		'\</html\>
		";
}

str trJson(Figure f, map[str,str] state, loc site) {
	fig_site = site;
	return trJson(f);
}

/******************** Translate figure primitives ************************************/


// Graphical elements

str trJson(_box(FProperties fps)) = 
	"{\"figure\": \"box\" <trPropsJson(fps)> }";

str trJson(_box(Figure inner, FProperties fps)) = 
	"{\"figure\": \"box\" <trPropsJson(fps,sep=", ")> 
    ' \"inner\":  <trJson(inner)> 
    '}";

str trJson(_text(str txt, FProperties fps)) = 
	"{\"figure\": \"text\", \"textValue\": \"<txt>\"<trPropsJson(fps)> }";

str trJson(_text(use(str var_name), FProperties fps)) = 
	"{\"figure\": \"text\", \"textValue\": {\"use\": \"<var_name>\"}<trPropsJson(fps)> }";

str trJson(_hcat(list[Figure] figs, FProperties fps)) = 
	"{\"figure\": \"hcat\"<trPropsJson(fps, sep=", ")> 
    ' \"inner\":   [<intercalate(",\n", [trJson(f) | f <- figs])> 
    '          ] 
    '}";

str trJson(_vcat(list[Figure] figs, FProperties fps)) = 
	"{\"figure\": \"vcat\"<trPropsJson(fps, sep=", ")> 
    ' \"inner\":  [<intercalate(",\n", [trJson(f) | f <- figs])>
    '         ] 
    '}";

// Layouts

str trJson(_barchart(FProperties fps)) = 
	"{\"figure\": \"barchart\" <trPropsJson(fps)> }";

str trJson(_scatterplot(FProperties fps)) = 
	"{\"figure\": \"scatterplot\" <trPropsJson(fps)> }";

str trJson(_graph(Figures nodes, Edges edges, FProperties fps)) = 
	"{\"figure\": \"graph\" <trPropsJson(fps, sep=", ")> 
	' \"nodes\":  [<intercalate(",\n", [trJson(f) | f <- nodes])>
	'         ], 
	' \"edges\":  [<intercalate(",\n", ["{\"source\": <from>, \"target\": <to>}"| _edge(from,to,efps) <- edges])>
	'         ]
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

// Interaction elements

// ---------- textInput ----------

str trJson(_textInput(void (str) scallback, FProperties fps)) {
 cbid = def_callback_str(scallback);
 return "{\"figure\": \"textInput\" <trPropsJson(fps, sep=", ")> \"site\": \"<getSite()>\", \"onClick\": \"<cbid>\" }";
} 
 
str trJson(_textInput(def(type[&T] var_type, str var_name), FProperties fps)) {
 return "{\"figure\": \"textInput\" <trPropsJson(fps, sep=", ")> \"site\": \"<getSite()>\", \"var_type\": \"<var_type>\", \"var_name\": \"<var_name>\" }";
}

str trJson(_fswitch(use(str var_name), Figures figs, FProperties fps)) = 
	
	"{\"figure\": \"fswitch\"<trPropsJson(fps, sep=", ")> 
	  \"selector\":	 {\"use\": \"<var_name>\"},
    ' \"inner\":   [<intercalate(",\n", [trJson(f) | f <- figs])> 
    '          ] 
    '}";

str trJson(_rangeInput(int low, int high, int step, def(type[&T] var_type, str var_name), FProperties fps)) =   
	"{ \"figure\": \"rangeInput\"<trPropsJson(fps, sep=", ")> 
	'  \"low\":	 		<low>,
	'  \"high\":		<high>,
	'  \"step\":		<step>,
	'   \"site\":		\"<getSite()>\", 
	'	\"var_type\": 	\"<var_type>\", 
	'	\"var_name\": 	\"<var_name>\"
    '}";

  
    
// Default

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

str trPropJson(gap(int width, int height)) 		= "\"hgap\": <width>, \"vgap\": <height>";

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
	return "\"halign\": <xa>, \"valign\": <ya>";
}	

str trPropJson(width(int w))					= "\"definedWidth\": <w>";
str trPropJson(width(use(str name)))			= "\"definedWidth\": {\"use\": \"<name>\"}";

str trPropJson(height(int h))					= "\"definedHeight\": <h>";
str trPropJson(height(use(str name)))			= "\"definedHeight\": {\"use\": \"<name>\"}";

str trPropJson(xpos(int x))						= "\"xpos\": <x>";
str trPropJson(xpos(use(str name)))				= "\"xpos\": {\"use\": \"<name>\"}";

str trPropJson(ypos(int y))						= "\"ypos\": <y>";
str trPropJson(ypos(use(str name)))				= "\"ypos\": {\"use\": \"<name>\"}";

str trPropJson(hgap(int g))						= "\"hgap\": <h>";
str trPropJson(hgap(use(str name)))				= "\"hgap\": {\"use\": \"<name>\"}";

str trPropJson(vgap(int g))						= "\"vgap\": <h>";
str trPropJson(vgap(use(str name)))				= "\"vgap\": {\"use\": \"<name>\"}";

str trPropJson(lineWidth(int n)) 				= "\"lineWidth\": <n>";
str trPropJson(lineWidth(use(str name)))		= "\"lineWidth\": {\"use\": \"<name>\"}";

str trPropJson(lineColor(str s))				= "\"lineColor\":\"<s>\"";
str trPropJson(lineColor(use(str name)))		= "\"lineColor\": {\"use\": \"<name>\"}";

str trPropJson(lineStyle(list[int] dashes))		= "\"lineStyle\": <dashes>";

str trPropJson(fillColor(str s)) 				= "\"fillColor\": \"<s>\"";
str trPropJson(fillColor(use(str name)))		= "\"fillColor\": {\"use\": \"<name>\"}";

str trPropJson(lineOpacity(real r))				= "lineOpacity:\"<r>\"";
str trPropJson(lineOpacity(use(str name)))		= "\"lineOpacity\": {\"use\": \"<name>\"}";

str trPropJson(fillOpacity(real r))				= "\"fill_opacity\":\"<r>\"";
str trPropJson(fillOpacity(use(str name)))		= "\"fillOpacity\": {\"use\": \"<name>\"}";

str trPropJson(rounded(int rx, int ry))			= "\"rx\": <rx>, \"ry\": <ry>";
str trPropJson(dataset(list[num] values1)) 		= "\"dataset\": <values1>";
str trPropJson(dataset(lrel[num,num] values2))	= "\"dataset\": [" + intercalate(",", ["[<v1>,<v2>]" | <v1, v2> <- values2]) + "]";

str trPropJson(font(str fontName))				= "\"fontName\": \"<fontName>\"";
str trPropJson(font(use(str name)))				= "\"fontName\": {\"use\": \"<name>\"}";

str trPropJson(fontSize(int fontSize))			= "\"fontSize\": <fontSize>";
str trPropJson(fontSize(use(str name)))			= "\"fontSize\": {\"use\": \"<name>\"}";


//str trPropJson(onClick(void () vcallback))		= "\"onClick\": \"<def_callback(vcallback)>\", \"site\": \"<getSite()>\"";

str trPropJson(prop: onClick(def(type[&T] tp, str var_name, CallBack[&T] callback))){
	println("prop = <prop>");		
	return "\"onClick\": \"<def_callback(callback)>\", \"var_type\": \"<tp>\", \"var_name\": \"<var_name>\", \"site\": \"<getSite()>\"";	
}

default str trPropJson(FProperty fp) 			= (size(int xsize, int ysize) := fp) ? "\"definedWidth\": <xsize>, \"definedHeight\": <ysize>" : "unknown: <fp>";

/******************* Utilities for callback management ********************/

private loc fig_site = |http://localhost:8081|;

public str getSite() = "<fig_site>"[1 .. -1];

// CallBack[&T]

private int ncallback = 0;
private map[str, value] callbacks = ();
private map[value, str] seen_callbacks = ();

private void init_callback(){
	ncallback = 0;
	callbacks = ();
	seen_callbacks = ();
}

public str def_callback(CallBack[&T] callback){
println("A");
	if(!callbacks?){
		init_callbacks();
	}
	if(seen_callbacks[callback]?){
		return seen_callbacks[callback];
	}
	ncallback += 1;
	str cid = "<ncallback>";
	println("callbacks = <callbacks>, cid = <cid>");
	callbacks[cid] = callback;
	seen_callbacks[callback] = cid;
	println("def_callback: <cid>, <callbacks>, <seen_callbacks>");
	return cid;
}

public  map[str, str] do_callback(str fun, map[str, str] state){
    println("do_callback: <fun>, <state>, <callbacks>");
    
    tp = state["var_type"];
    var_name = state["var_name"];
 	var_value = state[var_name];
    callback = callbacks[fun];
    switch(tp){
    	case "int":	{ if( int(int) callback := callbacks[fun]) { state[var_name] = "<callback(toInt(var_value))>"; return state; } }
    	case "str":	{ if( str(str) callback := callbacks[fun]) { state[var_name] = "<callback(var_value)>"; return state; } }
    };
    throw "do_callback: unsupported type <tp> for <var_name>";
}