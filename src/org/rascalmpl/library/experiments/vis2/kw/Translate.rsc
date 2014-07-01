module experiments::vis2::kw::Translate

import experiments::vis2::kw::Figure;
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

str trProperties(Figure fig){
	def = box();
	properties = [];
	
	if(fig.pos != def.pos) 					properties += "\"xpos\": <numArg(fig.xpos[0])>,
												  		  '\"ypos\": <numArg(fig.xpos[1])> ";
	if(fig.xpos != def.xpos) 				properties += "\"xpos\": <numArg(fig.xpos)>";
	if(fig.ypos != def.ypos) 				properties += "\"ypos\": <numArg(fig.ypos)>";
	
	if(fig.size != def.size) 				properties += "\"definedWidth\": <numArg(fig.size[0])>,
												  		  '\"definedHeight\": <numArg(fig.size[1])> ";
												  
	if(fig.width != def.width) 				properties += "\"definedWidth\": <numArg(fig.width)>";
	if(fig.height != def.height) 			properties += "\"definedHeight\": <numArg(fig.height)>";
	
	if(fig.gap != def.gap) 					properties += "\"hgap\": <numArg(fig.gap[0])>,
												  		  '\"vgap\": <numArg(fig.gap[1])> ";
												  
	if(fig.hgap != def.hgap) 				properties += "\"hgap\": <numArg(fig.hgap)>";
	if(fig.vgap != def.vgap) 				properties += "\"vgap\": <numArg(fig.vgap)>";
	
	if(fig.lineWidth != def.lineWidth) 		properties += "\"lineWidth\": <numArg(fig.lineWidth)>";
	
	if(fig.lineColor != def.lineColor) 		properties += "\"lineColor\": <numArg(fig.lineColor)>";
	
	if(fig.lineStyle != def.lineStyle) 		properties += "\"lineStyle\": <fig.lineStyle>";			// TODO
	
	if(fig.fillColor != def.fillColor) 		properties += "\"fillColor\": <strArg(fig.fillColor)>";
	
	if(fig.lineOpacity != def.lineOpacity)	properties += "\"lineOpacity\": <numArg(fig.lineOpacity)>";
	
	if(fig.fillOpacity != def.fillOpacity)	properties += "\"fillOpacity\": <numArg(fig.fillOpacity)>";
	
	// rounded tr trPropJson(rounded(int rx, int ry))			= "\"rx\": <numArg(rx)>, \"ry\": <numArg(ry)>";
	
	if(fig.align != def.align) 				properties += "\"halign\": <trHALign(numArg(fig.align[0]))>,
												  	  	  '\"valign\": <trVAlign(numArg(fig.align[1]))>";
												  	  
	if(fig.halign != def.halign) 			properties += "\"halign\": <trHALign(numArg(fig.halign))>";
	
	if(fig.valign != def.valign) 			properties += "\"valign\": <trHALign(numArg(fig.valign))>";
												  	  
												  
	if(fig.font != def.font) 				properties += "\"fontName\": <strArg(fig.font)>";
	
	if(fig.fontSize != def.fontSize) 		properties += "\"fontSize\": <numArg(fig.fontSize)>";
	
	if(fig.dataset != def.dataset) 			properties += trDataset(fig.dataset);
	
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
	
	return properties == [] ? "" : "<for(p <- properties){>, <p><}>";
}
//str trPropJson(dataset(list[num] values1)) 		= "\"dataset\": <values1>";
//str trPropJson(dataset(lrel[num,num] values2))	= "\"dataset\": [" + intercalate(",", ["[<v1>,<v2>]" | <v1, v2> <- values2]) + "]";

// Graphical elements	

str trJson(figure: box()) {
	inner = figure.fig; 
	return 
	"{\"figure\": \"box\"" +
		(inner == emptyFigure() ? "" : ", \"inner\": <trJson(inner)>") +
		"<trProperties(figure)> }";
}
str trJson(figure: text(value v)) = 
	"{\"figure\": \"text\", \"textValue\": <valArgQuoted(v)> <trProperties(figure)> }";
	
str trJson(figure: hcat()){ 
	figs = figure.figs;
	return
	"{\"figure\": \"hcat\",
    ' \"inner\":   [<intercalate(",\n", [trJson(f) | f <- figs])> 
    '              ] 
    '<trProperties(figure)>
    '}";
}
    
str trJson(figure: vcat()) { 
	figs = figure.figs;
	return
	"{\"figure\": \"vcat\",
    ' \"inner\":   [<intercalate(",\n", [trJson(f) | f <- figs])> 
    '              ] 
    '<trProperties(figure)>
    '}";
}

str trJson(figure: barchart()) = 
	"{\"figure\": \"barchart\" <trProperties(figure)> }";

str trJson(figure: scatterplot()) = 
	"{\"figure\": \"scatterplot\" <trProperties(figure)> }";

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

// ---------- conditional ----------

str trJson(figure: experiments::vis2::kw::Figure::choice()) { 
	int sel = figure.sel;
	figures = figure.figs;
	if(isCursor(sel)){
	   return 
		"{\"figure\": 	\"choice\",
		' \"selector\":	<trPath(toPath(sel))>,
    	' \"inner\":   [<intercalate(",\n", [trJson(f) | f <- figs])> 
   	    '              ] 
   	    ' <trProperties(figure)>
    	'}";
    } else {
    	throw "choice: selector should be a cursor: sel";
    }
 }

 // ---------- visible ----------
 
 str trJson(figure: visible()) { 
 	bool vis = figure.vis;
 	Figure fig1 = figure.fig;
	if(isCursor(vis)){
	   return 
		"{\"figure\":	\"visible\",
		' \"selector\":	<trPath(toPath(vis))>,
    	' \"inner\":   	<trJson(fig)>
    	' <trProperties(figure)> 
    	'}";
    } else {
    	throw "fswitch: selector should be a cursor: sel";
    }
 }   

// ---------- input elements ----------

// ---------- buttonInput ----------

str trJson(figure: buttonInput(str trueText, str falseText)) {
	trueText = figure.trueText;
	falseText = figure.falseText;
	return 
	"{\"figure\": 		\"buttonInput\",
 	' \"trueText\":		<strArg(trueText)>,
 	' \"falseText\":	<strArg(falseText)>
 	' <trProperties(figure)> 
 	'}";
} 
// ---------- checboxInput ----------

str trJson(figure: checkboxInput()) =
	"{\"figure\":	\"checkboxInput\" <trProperties(figure)>  }";
   
// ---------- strInput ----------
 
str trJson(figure: strInput()) =
 	"{\"figure\": \"strInput\" <trProperties(figure)> }";
 	
// ---------- choiceInput ----------

str trJson(figures: choiceInput()) {
	choices = figure.choices;
	return
	"{\"figure\": 		 \"choiceInput\",
	' \"choices\":		 <choices>
	' <trProperties(figure)> 
	'}";
}
	

// ---------- colorInput ----------

str trJson(figure: colorInput()) =
	"{\"figure\": 		 \"colorInput\"  <trProperties(figure)> }";

// ---------- numInput ----------

str trJson(figure: numInput()) =
	"{\"figure\": 		 \"numInput\" <trProperties(figure)> }";

// ---------- rangeInput ----------

str trJson(figure: rangeInput(int low, int high, int step)) {
	low = figure.low;
	high = figure.high;
	step = figure.step;
	return
	"{ \"figure\":			\"rangeInput\", 
	'  \"min\":	 			<numArg(low)>,
	'  \"max\":				<numArg(high)>,
	'  \"step\":			<numArg(step)>
	' <trProperties(figure)> 
	'}";
}
	
    
// Catch missing cases

default str trJson(Figure f) { throw "trJson: cannot translate <f>"; }

/**************** Utilities for tranlating properties *************************/

str trHAlign(HAlign xalign){
	xa = 0.5;
	switch(xalign){
		case left():	xa = 0.0;
		case right():	xa = 1.0;
	}
	return "<xa>";
}

str trVAlign(VAlign yalign){
	ya = 0.5;
	switch(yalign){
		case top():		ya = 0.0;
		case bottom():	ya = 1.0;
	}
	return "<ya>";
}

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