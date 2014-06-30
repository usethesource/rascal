module experiments::vis2::Translate

import experiments::vis2::Figure;
//import experiments::vis2::Properties;
import Node;
import String;
import IO;
import List;
import util::Cursor;
import Type;

/*
 * Translate a Figure to HTML + JSON
 */

// Translation a figure to one HTML page

public str fig2html(str title, str site_as_str){
	// TODO: get rid of this absolute path
	vis2 = "/Users/paulklint/git/rascal/src/org/rascalmpl/library/experiments/vis2";
	
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
		'	askServer(\"<site_as_str>/initial_figure\");
  		'\</script\>
		'\</body\>
		'\</html\>
		";
}

/******************** Translate figure primitives ************************************/

// Graphical elements

str trJson(_box(FProperties fps)) = 
	"{\"figure\": \"box\" <trPropsJson(fps)> }";

str trJson(_box(Figure inner, FProperties fps)) = 
	"{\"figure\": \"box\" <trPropsJson(fps,sep=", ")> 
    ' \"inner\":  <trJson(inner)> 
    '}";

str trJson(_text(value v, FProperties fps)) = 
	"{\"figure\": \"text\", \"textValue\": <valArgQuoted(v)> <trPropsJson(fps)> }";

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

// Visibility control elements

// ---------- fswitch ----------

str trJson(_choice(int sel, Figures figs, FProperties fps)) { 
	if(isCursor(sel)){
	   return 
		"{\"figure\": 	\"choice\"<trPropsJson(fps, sep=", ")> 
		' \"selector\":	<trPath(toPath(sel))>,
    	' \"inner\":   [<intercalate(",\n", [trJson(f) | f <- figs])> 
   	    '              ] 
    	'}";
    } else {
    	throw "choice: selector should be a cursor: sel";
    }
 }
 
// ---------- visible ----------
 
 str trJson(_visible(bool vis, Figure fig, FProperties fps)) { 
	if(isCursor(vis)){
	   return 
		"{\"figure\":	\"visible\"<trPropsJson(fps, sep=", ")> 
		' \"selector\":	<trPath(toPath(vis))>,
    	' \"inner\":   	<trJson(fig)> 
    	'}";
    } else {
    	throw "fswitch: selector should be a cursor: sel";
    }
 }   

// ---------- input elements ----------

// ---------- buttonInput ----------

str trJson(_buttonInput(str trueText, str falseText, FProperties fps)) =
	"{\"figure\": 		\"buttonInput\" <trPropsJson(fps, sep=", ")>
 	' \"trueText\":		<strArg(trueText)>,
 	' \"falseText\":	<strArg(falseText)>
 	'}";
 
// ---------- checboxInput ----------

str trJson(_checkboxInput(FProperties fps)) =
	"{\"figure\":	\"checkboxInput\" <trPropsJson(fps)> }";
   
// ---------- strInput ----------
 
str trJson(_strInput(FProperties fps)) =
 	"{\"figure\": \"strInput\" <trPropsJson(fps)> }";
 	
// ---------- choiceInput ----------

str trJson(_choiceInput(list[str] choices, FProperties fps)) =
	"{\"figure\": 		 \"choiceInput\" <trPropsJson(fps, sep=", ")>
	' \"choices\":		 <choices>
	'}";

// ---------- colorInput ----------

str trJson(_colorInput(FProperties fps)) =
	"{\"figure\": 		 \"colorInput\" <trPropsJson(fps)> }";

// ---------- numInput ----------

str trJson(_numInput(FProperties fps)) =
	"{\"figure\": 		 \"numInput\" <trPropsJson(fps)> }";

// ---------- rangeInput ----------

str trJson(p: _rangeInput(int low, int high, int step, FProperties fps)) =
	"{ \"figure\":			\"rangeInput\"<trPropsJson(fps, sep=", ")> 
	'  \"min\":	 			<numArg(low)>,
	'  \"max\":				<numArg(high)>,
	'  \"step\":			<numArg(step)>
	'}";
    
// Catch missing cases

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

/*
= root(str name)
  | field(str name)
  | field(int position)
  | argument(int position)
  | argument(str name)
  | element(int index)
  | sublist(int from, int to)
  | lookup(value key)
  | select(list[int] indices)
  | select(list[str] labels)
  */

str trPath(Path path){
    accessor = "Figure.model";
	for(nav <- path){
		switch(nav){
		 	case root(str name):		accessor += ".<name>"; 
			case field(str name): 		accessor += ".<name>";
			case field(int position): 	accessor += "[<position>]";
  			case argument(int index):	accessor += "[\\\"#args\\\"][<index>]";
  			case element(int index):	accessor += "[<index>]";
  			case lookup(value key):		accessor += "[<key>]";
  			case select(list[int] indices):
  										accessor += "";		// TODO
  			case select(list[str] labels):
  										accessor += "";		// TODO
  		}
  	}
  	println("trPath: <path>: <accessor>");
  	return "\"<accessor>\"";
}

str numArg(num n) 	= isCursor(n) ? "{\"use\": <trPath(toPath(n))>}" : "<n>";

str strArg(str s) 	= isCursor(s) ? "{\"use\": <trPath(toPath(s))>}" : "\"<s>\"";

str valArg(value v) = isCursor(v) ? "{\"use\": <trPath(toPath(v))>}" : "<v>";
str valArgQuoted(value v) = isCursor(v) ? "{\"use\": <trPath(toPath(v))>}" : "\"<v>\"";		

str trPropJson(width(int w))					= "\"definedWidth\": <numArg(w)>";
str trPropJson(height(int h))					= "\"definedHeight\": <numArg(h)>";

str trPropJson(xpos(int x))						= "\"xpos\": <numArg(x)>";

str trPropJson(ypos(int y))						= "\"ypos\": <numArg(y)>";

str trPropJson(hgap(int g))						= "\"hgap\": <numArg(g)>";

str trPropJson(vgap(int g))						= "\"vgap\": <numArg(g)>";

str trPropJson(lineWidth(int n)) 				= "\"lineWidth\": <numArg(n)>";

str trPropJson(lineColor(str s))				= "\"lineColor\":<strArg(s)>";

str trPropJson(lineStyle(list[int] dashes))		= "\"lineStyle\": <dashes>";			// TODO

str trPropJson(fillColor(str s)) 				= "\"fillColor\": <strArg(s)>";

str trPropJson(lineOpacity(real r))				= "lineOpacity:\"<numArg(r)>\"";

str trPropJson(fillOpacity(real r))				= "\"fill_opacity\": <numArg(r)>";

str trPropJson(rounded(int rx, int ry))			= "\"rx\": <numArg(rx)>, \"ry\": <numArg(ry)>";
str trPropJson(dataset(list[num] values1)) 		= "\"dataset\": <values1>";
str trPropJson(dataset(lrel[num,num] values2))	= "\"dataset\": [" + intercalate(",", ["[<v1>,<v2>]" | <v1, v2> <- values2]) + "]";

str trPropJson(font(str fontName))				= "\"fontName\": <strArg(fontName)>";

str trPropJson(fontSize(int fontSize))			= "\"fontSize\": <numArg(fontSize)>";

str trPropJson(prop: on(str event, binder: bind(&T accessor))){
	println("prop = <prop>");	
	if(isCursor(binder.accessor)){ 	
		return 
			"\"event\":  		\"<event>\",
			'\"type\": 			\"<typeOf(binder.accessor)>\", 
			'\"accessor\": 		<trPath(toPath(binder.accessor))>
			";	
 	} else {
   		throw "on: accessor <accessor> in binder is not a cursor";
   }
}

str trPropJson(prop: on(str event, binder: bind(&T accessor, &T replacement))){
	println("prop = <prop>");	
	if(isCursor(binder.accessor)){ 	
		return 
			"\"event\":  		\"<event>\",
			'\"type\": 			\"<typeOf(binder.accessor)>\", 
			'\"accessor\": 		<trPath(toPath(binder.accessor))>,
			'\"replacement\":	<replacement>
			";	
 	} else {
   		throw "on: accessor <accessor> in binder is not a cursor";
   }
}

str trPropJson(prop: on(str event, Figure fig)){
	println("prop = <prop>");	
	
	return 
		"\"event\":  		\"<event>\",
		'\"extra_figure\":	<trJson(fig)>
		";	
}

default str trPropJson(FProperty fp) 			= (size(int xsize, int ysize) := fp) ? "\"definedWidth\": <xsize>, \"definedHeight\": <ysize>" : "unknown: <fp>";