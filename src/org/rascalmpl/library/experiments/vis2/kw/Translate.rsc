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

str trProperties(Figure child, Figure parent){
	properties = [];
	
	if(child.pos != parent.pos)						properties += "\"xpos\": <numArg(child.pos[0])>, 
															      '\"ypos\": <numArg(child.pos[1])> ";
	
	if(child.xpos != parent.xpos) 					properties += "\"xpos\": <numArg(child.xpos)>";
	if(child.ypos != parent.ypos) 					properties += "\"ypos\": <numArg(child.ypos)>";
	
	if(child.size != parent.size) 					properties += "\"definedWidth\": <numArg(child.size[0])>,
												  		          '\"definedHeight\": <numArg(child.size[1])> ";
												  
	if(child.width != parent.width) 				properties += "\"definedWidth\": <numArg(child.width)>";
	if(child.height != parent.height) 				properties += "\"definedHeight\": <numArg(child.height)>";
	
	if(child.gap != parent.gap) 					properties += "\"hgap\": <numArg(child.gap[0])>,
												  		          '\"vgap\": <numArg(child.gap[1])> ";
												  
	if(child.hgap != parent.hgap) 					properties += "\"hgap\": <numArg(child.hgap)>";
	if(child.vgap != parent.vgap) 					properties += "\"vgap\": <numArg(child.vgap)>";
	
	if(child.lineWidth != parent.lineWidth) 		properties += "\"lineWidth\": <numArg(child.lineWidth)>";
	
	if(child.lineColor != parent.lineColor) 		properties += "\"lineColor\": <strArg(child.lineColor)>";
	
	if(child.lineStyle != parent.lineStyle) 		properties += "\"lineStyle\": <child.lineStyle>";			// TODO
	
	if(child.fillColor != parent.fillColor) 		properties += "\"fillColor\": <strArg(child.fillColor)>";
	
	if(child.lineOpacity != parent.lineOpacity)		properties += "\"lineOpacity\": <numArg(child.lineOpacity)>";
	
	if(child.fillOpacity != parent.fillOpacity)		properties += "\"fillOpacity\": <numArg(child.fillOpacity)>";
	
	if(child.rounded != parent.rounded)				properties += "\"rx\": <numArg(child.rounded[0])>, 
																  '\"ry\": <numArg(child.rounded[1])>";
	
	if(child.align != parent.align) 				properties += "\"halign\": <trHAlign(child.align[0])>,
												  	  	  		  '\"valign\": <trVAlign(child.align[1])>"; 		// TODO add numArg
												  	  
	if(child.halign != parent.halign) 				properties += "\"halign\": <trHAlign(child.halign)>";
	
	if(child.valign != parent.valign) 				properties += "\"valign\": <trVAlign(child.valign)>";
												  	  
												  
	if(child.font != parent.font) 					properties += "\"fontName\": <strArg(child.font)>";
	
	if(child.fontSize != parent.fontSize) 			properties += "\"fontSize\": <numArg(child.fontSize)>";
	
	if(child.dataset != parent.dataset) 			properties += trDataset(child.dataset);
	
	if(child.event != parent.event){
	
	println("trProperties: <child.event>");
		switch(child.event){
			case on(str event, binder: bind(accessor)):
				if(isCursor(accessor)){ 	
					properties += [
						"\"event\": \"<event>\"",
						"\"type\":  \"<typeOf(accessor)>\"", 
						"\"accessor\": <trPath(toPath(accessor))>"
						];
 				} else {
   				  	throw "on: accessor <accessor> in binder is not a cursor";
   				}
			case  on(str event, bind(accessor, replacement)): {
				println("accessor: <accessor>, isCursor: <isCursor(accessor)>");
				if(isCursor(accessor)){ 	
					properties += [
						"\"event\":  		\"<event>\"",
						"\"type\": 			\"<typeOf(accessor)>\"",
						"\"accessor\": 		<trPath(toPath(accessor))>",
						"\"replacement\":	<replacement>"
						];	
 				} else {
   					throw "on: accessor <accessor> in binder is not a cursor";
   				}
   				}
   			case on(str event, Figure fig):
   					properties += [
   						"\"event\":  		\"<event>\"",
						"\"extra_figure\":	<trJson(fig, parent)>"
						];
		}
	}
	
	return properties == [] ? "" : "<for(p <- properties){>, <p><}>";
}

str trDataset(list[num] values1) 		= "\"dataset\": <values1>";
str trDataset(lrel[num,num] values2)	= "\"dataset\": [" + intercalate(",", ["[<v1>,<v2>]" | <v1, v2> <- values2]) + "]";

// Graphical elements	

// Figure without properties of its own

str trSimple(str kind, Figure child, Figure parent) = "{\"figure\": \"<kind>\" <trProperties(child, parent)> }";

// ---------- box ----------

str trJson(figure: box(), Figure parent) {
	inner = figure.fig; 
	println("inner = <inner>");
	return 
	"{\"figure\": \"box\"" +
		(getName(inner) == "emptyFigure" ? "" : ", \"inner\": <trJson(inner, parent)>") +
		"<trProperties(figure, parent)> }";
}

// ---------- text ----------

str trJson(figure: text(value v), Figure parent) = 
	"{\"figure\": \"text\", \"textValue\": <valArgQuoted(v)> <trProperties(figure, parent)> }";

// ---------- hcat ----------
	
str trJson(figure: hcat(), Figure parent){ 
	figs = figure.figs;
	return
	"{\"figure\": \"hcat\",
    ' \"inner\":   [<intercalate(",\n", [trJson(f, figure) | f <- figs])> 
    '              ] 
    '<trProperties(figure, parent)>
    '}";
}

// ---------- vcat ----------
    
str trJson(figure: vcat(), Figure parent) { 
	figs = figure.figs;
	return
	"{\"figure\": \"vcat\",
    ' \"inner\":   [<intercalate(",\n", [trJson(f, figure) | f <- figs])> 
    '              ] 
    '<trProperties(figure, parent)>
    '}";
}

// ---------- barchart ----------

str trJson(figure: barchart(), Figure parent) = trSimple("barchart", figure, parent);

// ---------- barchart ----------

str trJson(figure: scatterplot(), Figure parent) =  trSimple("scatterplot", figure, parent);

// ---------- graph ----------

str trJson(figure: graph(), Figure parent) { 
	nodes = figure.nodes;
	edges = figure.edges;
	println("nodes = <nodes>");
	println("edges = <edges>");
	return
	"{\"figure\": \"graph\", 
	' \"nodes\":  [<intercalate(",\n", ["{ \"id\": \"<f>\", 
	'                                      \"value\" : {\"label\": \"<f>\", 
	'												    \"figure\": <trJson(nodes[f], parent)>}}" | f <- nodes])>
	'             ],  
	' \"edges\":  [<intercalate(",\n", ["{\"u\": \"<from>\", 
	'									  \"v\": \"<to>\", 
	'									  \"value\": {\"label\": \"<label>\" <trProperties(e, parent)>}}"| e: edge(from,to,label) <- edges])>
	'         ]
	' <trProperties(figure, parent)> 
	'}";
}

// edge

str trJSon(figure: edge(int from, int to)) {
	
	throw "edges should not be translated";
}

/*
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

str trJson(figure: experiments::vis2::kw::Figure::choice(), Figure parent) { 
	int selection = figure.selection;
	choices = figure.figs;
	if(isCursor(selection)){
	   return 
		"{\"figure\": 	\"choice\",
		' \"selector\":	<trPath(toPath(selection))>,
    	' \"inner\":   [<intercalate(",\n", [trJson(f, figure) | f <- choices])> 
   	    '              ] 
   	    ' <trProperties(figure, parent)>
    	'}";
    } else {
    	throw "choice: selection should be a cursor: <selection>";
    }
 }

 // ---------- visible ----------
 
 str trJson(figure: visible(), Figure parent) { 
 	bool condition = figure.condition;
 	Figure fig = figure.fig;
	if(isCursor(condition)){
	   return 
		"{\"figure\":	\"visible\",
		' \"selector\":	<trPath(toPath(condition))>,
    	' \"inner\":   	<trJson(fig, figure)>
    	' <trProperties(figure, parent)> 
    	'}";
    } else {
    	throw "fswitch: condition should be a cursor: <condition>";
    }
 }   

// ---------- input elements ----------

// ---------- buttonInput ----------

str trJson(figure: buttonInput(), Figure parent) {
	trueText = figure.trueText;
	falseText = figure.falseText;
	return 
	"{\"figure\": 		\"buttonInput\",
 	' \"trueText\":		<strArg(trueText)>,
 	' \"falseText\":	<strArg(falseText)>
 	' <trProperties(figure, parent)> 
 	'}";
} 

// ---------- checboxInput ----------

str trJson(figure: checkboxInput(), Figure parent) = trSimple("checkboxInput", figure, parent);
   
// ---------- choiceInput ----------

str trJson(figure: choiceInput(), Figure parent) {
	choices = figure.choices;
	return
	"{\"figure\": 		 \"choiceInput\",
	' \"choices\":		 <choices>
	' <trProperties(figure, parent)> 
	'}";
}
	
// ---------- colorInput ----------

str trJson(figure: colorInput(), Figure parent) = trSimple("colorInput", figure, parent);

// ---------- numInput ----------

str trJson(figure: numInput(), Figure parent) = trSimple("numInput", figure, parent);

// ---------- rangeInput ----------

str trJson(figure: rangeInput(), Figure parent) {
	low = figure.low;
	high = figure.high;
	step = figure.step;
	return
	"{ \"figure\":			\"rangeInput\", 
	'  \"min\":	 			<numArg(low)>,
	'  \"max\":				<numArg(high)>,
	'  \"step\":			<numArg(step)>
	' <trProperties(figure, parent)> 
	'}";
}

// ---------- strInput ----------
 
str trJson(figure: strInput(), Figure parent) = trSimple("strInput", figure, parent);
	
// Catch missing cases

default str trJson(Figure f, Figure parent) { throw "trJson: cannot translate <f>"; }

/**************** Utilities for translating properties *************************/

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
			case field(int position):	accessor += "[<position>]";
			
			case argument(str name):	accessor += ".<name>";
			case argument(int position):accessor += "[\\\"#args\\\"][<position>]";
			case keywordParam(str name):accessor += ".<name>";
  
			case element(int index):	accessor += "[<index>]";
  			case sublist(int from, int to):	accessor += ".slice(<from>,<to>]";
  			
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