module experiments::vis2::kw::Translate

import experiments::vis2::kw::Figure;
import Node;
import String;
import IO;
import List;
import util::Cursors;
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

str flat(list[str] properties) = properties == [] ? "" : "<for(p <- properties){>, <p><}>";

str trProperties(Figure fig){
	def = box();
	properties = [];
	if(fig.width != def.width) 		properties += "\"definedWidth\": <numArg(fig.width)>";
	if(fig.height != def.height) 	properties += "\"definedHeight\": <numArg(fig.height)>";
	
	
	if(fig.xpos != def.xpos) 		properties += "\"xpos\": <numArg(fig.xpos)>";
	if(fig.ypos != def.ypos) 		properties += "\"ypos\": <numArg(fig.ypos)>";
	
	if(fig.hgap != def.hgap) 		properties += "\"hgap\": <numArg(fig.hgap)>";
	if(fig.vgap != def.vgap) 		properties += "\"vgap\": <numArg(fig.vgap)>";
	
	if(fig.lineWidth != def.lineWidth) properties += "\"lineWidth\": <numArg(fig.lineWidth)>";
	
	if(fig.lineColor != def.lineColor) properties += "\"lineColor\": <numArg(fig.lineColor)>";
	
	if(fig.lineStyle != def.lineStyle) properties += "\"lineStyle\": <fig.lineStyle>";			// TODO
	
	if(fig.fillColor != def.fillColor) properties += "\"fillColor\": <strArg(fig.fillColor)>";
	if(fig.lineOpacity != def.lineOpacity) properties += "\"lineOpacity\": <numArg(fig.lineOpacity)>";
	if(fig.fillOpacity != def.fillOpacity) properties += "\"fillOpacity\": <numArg(fig.fillOpacity)>";
	
	if(fig.font != def.font) 	properties += "\"fontName\": <strArg(fig.font)>";
	if(fig.fontSize != def.fontSize) properties += "\"fontSize\": <numArg(fig.fontSize)>";
	
	if(fig.dataset != def.dataset) properties += trDataset(fig.dataset);
/*	
	if(fig.on != def.on){
		switch(fig.on){
			case on(str event, binder: bind(&T accessor)):
				if(isCursor(binder.accessor)){ 	
					properties += [
						"\"event\": \"<event>\"",
						"\"type\":  \"<typeOf(binder.accessor)>\"", 
						"\"accessor\": <trPath(toPath(binder.accessor))>"
						];
 				} else {
   				  	throw "on: accessor <accessor> in binder is not a cursor";
   				}
			case  on(str event, binder: bind(&T accessor, &T replacement)):
				if(isCursor(binder.accessor)){ 	
					properties += [
						"\"event\":  		\"<event>\"",
						"\"type\": 			\"<typeOf(binder.accessor)>\"",
						"\"accessor\": 		<trPath(toPath(binder.accessor))>",
						"\"replacement\":	<replacement>"
						];	
 				} else {
   					throw "on: accessor <accessor> in binder is not a cursor";
   				}
   			case on(str event, Figure fig):
   					properties += [
   						"\"event\":  		\"<event>\"",
						"\"extra_figure\":	<trJson(fig)>"
						];
		}
	}
	*/
	return properties == [] ? "" : "<for(p <- properties){>, <p><}>";
}
//str trPropJson(dataset(list[num] values1)) 		= "\"dataset\": <values1>";
//str trPropJson(dataset(lrel[num,num] values2))	= "\"dataset\": [" + intercalate(",", ["[<v1>,<v2>]" | <v1, v2> <- values2]) + "]";

// Graphical elements

str trJson(fig: box()) = 
	"{\"figure\": \"box\" <trProperties(fig)> }";

str trJson(fig: box(Figure inner)) = 
	"{\"figure\": \"box\", \"inner\": <trJson(inner)> <trProperties(fig)> }";


str trJson(fig: text(value v)) = 
	"{\"figure\": \"text\", \"textValue\": <valArgQuoted(v)> <trProperties(fig)> }";
	
str trJson(fig: hcat(list[Figure] figs)) = 
	"{\"figure\": \"hcat\",
    ' \"inner\":   [<intercalate(",\n", [trJson(f) | f <- figs])> 
    '              ] 
    '<trProperties(fig)>
    '}";
    
str trJson(fig: vcat(list[Figure] figs)) = 
	"{\"figure\": \"vcat\",
    ' \"inner\":   [<intercalate(",\n", [trJson(f) | f <- figs])> 
    '              ] 
    '<trProperties(fig)>
    '}";

str trJson(fig: barchart()) = 
	"{\"figure\": \"barchart\" <trProperties(fig)> }";

str trJson(fig: scatterplot()) = 
	"{\"figure\": \"scatterplot\" <trProperties(fig)> }";

/*

// Layouts


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
*/


// Visibility control elements

// ---------- choice ----------
/*
str trJson(fig: choice(int sel, Figures figs)) { 
	if(isCursor(sel)){
	   return 
		"{\"figure\": 	\"choice\",
		' \"selector\":	<trPath(toPath(sel))>,
    	' \"inner\":   [<intercalate(",\n", [trJson(f) | f <- figs])> 
   	    '              ] 
   	    ' <trProperties(fig)>
    	'}";
    } else {
    	throw "choice: selector should be a cursor: sel";
    }
 }
*/ 
 // ---------- visible ----------
 
 str trJson(fig: visible(bool vis, Figure fig)) { 
	if(isCursor(vis)){
	   return 
		"{\"figure\":	\"visible\",
		' \"selector\":	<trPath(toPath(vis))>,
    	' \"inner\":   	<trJson(fig)>
    	' <trProperties(fig)> 
    	'}";
    } else {
    	throw "fswitch: selector should be a cursor: sel";
    }
 }   

// ---------- input elements ----------

// ---------- buttonInput ----------

str trJson(fig: buttonInput(str trueText, str falseText)) =
	"{\"figure\": 		\"buttonInput\",
 	' \"trueText\":		<strArg(trueText)>,
 	' \"falseText\":	<strArg(falseText)>
 	' <trProperties(fig)> 
 	'}";
 
// ---------- checboxInput ----------

str trJson(fig: checkboxInput()) =
	"{\"figure\":	\"checkboxInput\" <trProperties(fig)>  }";
   
// ---------- strInput ----------
 
str trJson(fig: strInput()) =
 	"{\"figure\": \"strInput\" <trProperties(fig)> }";
 	
// ---------- choiceInput ----------

str trJson(fig: choiceInput(list[str] choices)) =
	"{\"figure\": 		 \"choiceInput\",
	' \"choices\":		 <choices>
	' <trProperties(fig)> 
	'}";

// ---------- colorInput ----------

str trJson(fig: colorInput(FProperties fps)) =
	"{\"figure\": 		 \"colorInput\"  <trProperties(fig)> }";

// ---------- numInput ----------

str trJson(fig: numInput()) =
	"{\"figure\": 		 \"numInput\" <trProperties(fig)> }";

// ---------- rangeInput ----------

str trJson(fig: rangeInput(int low, int high, int step)) =
	"{ \"figure\":			\"rangeInput\", 
	'  \"min\":	 			<numArg(low)>,
	'  \"max\":				<numArg(high)>,
	'  \"step\":			<numArg(step)>
	' <trProperties(fig)> 
	'}";
    
// Catch missing cases

default str trJson(Figure f) { throw "trJson: cannot translate <f>"; }

/**************** Tranlate properties *************************/
/*
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
*/
str trPath(Path path){
    accessor = "Figure.model";
	for(nav <- path){
		switch(nav){
		 	case root(str name):		accessor += ".<name>"; 
			case field(str name): 		accessor += ".<name>";
  			case subscript(int index):	accessor += "[<index>]";
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
/*
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
*/