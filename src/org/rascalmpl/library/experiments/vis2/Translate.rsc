module experiments::vis2::Translate

import experiments::vis2::Figure;
import Node;
import String;
import IO;
import List;
import util::Cursor;
import Type;
import lang::json::IO;
import util::Math;

/*
 * Translate a Figure to JSON
 */
 
/******************** Translate figure properties ************************************/

void check(str property, str val, set[str] allowed){
	if(val notin allowed){
		throw "property <property> has value <val> but should be one of <allowed>";
	}
}

str propsToJSON(Figure child, Figure parent){
	properties = [];
	
	//if(child.pos != parent.pos)						properties += "\"xpos\": <numArg(child.pos[0])>, 
	//														      '\"ypos\": <numArg(child.pos[1])> ";
	//
	//if(child.xpos != parent.xpos) 					properties += "\"xpos\": <numArg(child.xpos)>";
	//if(child.ypos != parent.ypos) 					properties += "\"ypos\": <numArg(child.ypos)>";
	
	if(child.size != parent.size) 					properties += "\"definedWidth\": <numArg(child.size[0])>,
												  		          '\"definedHeight\": <numArg(child.size[1])> ";
												  
	if(child.width != parent.width) 				properties += "\"definedWidth\": <numArg(child.width)>";
	if(child.height != parent.height) 				properties += "\"definedHeight\": <numArg(child.height)>";
	
	if(child.gap != parent.gap) 					properties += "\"hgap\": <numArg(child.gap[0])>,
												  		          '\"vgap\": <numArg(child.gap[1])> ";
												  
	if(child.hgap != parent.hgap) 					properties += "\"hgap\": <numArg(child.hgap)>";
	if(child.vgap != parent.vgap) 					properties += "\"vgap\": <numArg(child.vgap)>";
	
	if(child.strokeWidth != parent.strokeWidth) 	properties += "\"stroke-width\": <numArg(child.strokeWidth)>";
	
	if(child.stroke != parent.stroke) 				properties += "\"stroke\": <strArg(child.stroke)>";
	
	if(child.strokeDashArray != parent.strokeDashArray) 		
													properties += "\"stroke-dasharray\": <child.strokeDashArray>";			// TODO
	
	if(child.fill != parent.fill) 					properties += "\"fill\": <strArg(child.fill)>";
	
	if(child.strokeOpacity != parent.strokeOpacity)		properties += "\"stroke-opacity\": <numArg(child.strokeOpacity)>";
	
	if(child.fillOpacity != parent.fillOpacity)		properties += "\"fill-opacity\": <numArg(child.fillOpacity)>";
	
	if(child.fillRule != parent.fillRule){
		check("fillRule", child.fillRule, {"nonzero", "evenodd"});
		properties += "\"fill-rule\": <strArg(child.fillRule)>";
	}
	
	if(child.rounded != parent.rounded)				properties += "\"rx\": <numArg(child.rounded[0])>, 
																  '\"ry\": <numArg(child.rounded[1])>";
	
	if(child.align != parent.align) 				properties += "\"halign\": <trHAlign(child.align[0])>,
												  	  	  		  '\"valign\": <trVAlign(child.align[1])>"; 		// TODO add numArg
												  	  
	if(child.halign != parent.halign) 				properties += "\"halign\": <trHAlign(child.halign)>";
	
	if(child.valign != parent.valign) 				properties += "\"valign\": <trVAlign(child.valign)>";
												  	  
	if(child.fontFamily != parent.fontFamily) 		properties += "\"font-family\": <strArg(child.fontFamily)>";											  
	if(child.fontName != parent.fontName) 			properties += "\"font-name\": <strArg(child.fontName)>";
	
	if(child.fontSize != parent.fontSize) 			properties += "\"font-size\": <numArg(child.fontSize)>";

	check("fontStyle", child.fontStyle, {"normal", "italic"});
	if(child.fontStyle != parent.fontStyle) 		properties += "\"font-style\": <strArg(child.fontStyle)>";

	check("fontWeight", child.fontWeight, {"normal", "bold"});
	if(child.fontWeight != parent.fontWeight) 		properties += "\"font-weight\": <strArg(child.fontWeight)>";

	//check("textDecoration", child.textDecoration, {"none","underline", "overline", "line-through"});
	//if(child.textDecoration != parent.textDecoration) 
													//properties += "\"text-decoration\": <strArg(child.textDecoration)>";
	
	if(child.dataset != parent.dataset) 			properties += trDataset(child.dataset);
	
	if(child.event != parent.event && child.event != on()){
		if(!isLegalEvent(child.event.eventName)){
			throw "non-existing event name: <child.event>";
		}
	
	    println("propsToJSON: <child.event>");
		switch(child.event){
			case on(str event, binder: bind(accessor)):
				if(isCursor(accessor)){ 	
					properties += [
						"\"event\": \"<event>\"",
						//"\"type\":  \"<typeOf(accessor)>\"", 
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
					//	"\"type\": 			\"int()\", 					//\"<typeOf(accessor)>\"",
						"\"accessor\": 		<trPath(toPath(accessor))>",
						"\"replacement\":	<toJSON(replacement)>"
						];	
 				} else {
   					throw "on: accessor <accessor> in binder is not a cursor";
   				}
   				}
   			case on(str event, Figure fig):
   					properties += [
   						"\"event\":  		\"<event>\"",
						"\"extra_figure\":	<figToJSON(fig, parent)>"
						];
		    default:
		    	throw "No case for <child.event>";
		}
	}
	
	return properties == [] ? "" : "<for(p <- properties){>, <p><}>";
}

str trDataset(Dataset ds) = 
	"\"dataset\": [ <intercalate(",\n", [ trSeries(key, ds[key]) | key <- ds ])> ]";

str trSeries(str key, series: xyData(lrel[num,num] pairs)	) {
	xy = intercalate(",\n", [" {\"x\": <x>, \"y\": <y>}"| <x,y> <- series.pairs]);
	return
		"{ \"key\":    \"<key>\",
		'  \"values\": [ <xy> 
		'	           ], 
		'  \"color\":  \"<series.color>\", 
		'  \"area\":   <series.area>
		'}";
}

str trSeries(str key, series: lrel[str label, num val] labeledValues) {
	values = intercalate(",\n", [" {\"label\": \"<label>\", \"value\": <val>}"| <label, val> <- series]);
	return
		"{ \"key\":    \"<key>\",
		'  \"values\": [ <values> 
		'	           ]
		'}";
}

str trSeries(str key, Dataset[&T] ds) {
	throw "trSeries: not recoginzed: <ds>";
}
	
str trVertices(list[Vertex] vertices, bool shapeClosed = true, bool shapeCurved = true, bool shapeConnected = true) {
	<width, height>  = bbox(vertices);
	
	str path = "M <vertices[0].x> <vertices[0].y> "; // Move to start point
	int n = size(vertices);
	if(shapeConnected && shapeCurved && n > 2){
		path += "Q <toInt((vertices[0].x + vertices[1].x)/2.0)> <toInt((vertices[0].y + vertices[1].y)/2.0)> <vertices[1].x> <vertices[1].y> ";
		for(int i <- [2 ..n]){
			v = vertices[i];
			path += "<isAbsolute(v) ? "T" : "t"> <v.x> <v.y> "; // Smooth point on quadartic curve
		}
	} else {
		for(int i <- [1 .. n]){
			v = vertices[i];
			path += "<shapeConnected ? (isAbsolute(v) ? "L" : "l") : (isAbsolute(v) ? "M" : "m") > <v.x> <v.y> ";
		}
	}
	
	if(shapeConnected && shapeClosed) path += "Z";
	
	return "\"path\":    \"<path>\",
		   ' \"width\":  <width>,
		   ' \"height\": <height>
		   ";		   
}

bool isAbsolute(Vertex v) = (getName(v) == "vertex");

tuple[num, num] bbox(list[Vertex] vertices){	// TODO: assumes all points are positive
	maxX = maxY = 0;
	x = y = 0;
	for(int i <- index(vertices)){
		v = vertices[0];
		if(isAbsolute(v)){
			x = v.x; y = v.y;
			
		} else {
			x += v.x; y += v.y;
		}
		maxX = max(x, maxX);
		maxY = max(y, maxY);	
	}
	return <maxX, maxY>;

}

/**************** Utilities for translating properties *************************/

bool isLegalEvent(str event) = event in {

// Form events
	"blur",				// Fires the moment that the element loses focus
	"change",			// Fires the moment when the value of the element is changed
	"contextmenu",		// Script to be run when a context menu is triggered
	"focus",			// Fires the moment when the element gets focus
	"formchange",		// Script to be run when a form changes
	"forminput",		// Script to be run when a form gets user input
	"input",			// Script to be run when an element gets user input
	"invalid",			// Script to be run when an element is invalid
	"select",			// Fires after some text has been selected in an element
	"submit",			// Fires when a form is submitted
	
// Keyboard events
	"keydown",			// Fires when a user is pressing a key
	"keypress",			// Fires when a user presses a key
	"keyup",			// Fires when a user releases a key
	
// Mouse events
	"click",			// Fires on a mouse click on the element
	"dbclick",			// Fires on a mouse double-click on the element
	"drag",				// Script to be run when an element is dragged
	"dragend",			// Script to be run at the end of a drag operation
	"dragenter",		// Script to be run when an element has been dragged to a valid drop target
	"dragleave",		// Script to be run when an element leaves a valid drop target
	"dragover",			// Script to be run when an element is being dragged over a valid drop target
	"dragstart",		// Script to be run at the start of a drag operation
	"drop",				// Script to be run when dragged element is being dropped
	"mousedown",		// Fires when a mouse button is pressed down on an element
	"mousemove",		// Fires when the mouse pointer moves over an element
	"mouseeout",		// Fires when the mouse pointer moves out of an element
	"mouseover",		// Fires when the mouse pointer moves over an element
	"mouseup",			// Fires when a mouse button is released over an element
	"mousewheel",		// Script to be run when the mouse wheel is being rotated
	"scroll"			// Script to be run when an element's scrollbar is being scrolled
	};

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

/******************** Translate figures ************************************/
		
// Graphical elements	

// Figure without properties of its own

str nopropsToJSON(str kind, Figure child, Figure parent) = "{\"figure\": \"<kind>\" <propsToJSON(child, parent)> }";

// ---------- box ----------

str figToJSON(figure: box(), Figure parent) {
	inner = figure.fig; 
	println("inner = <inner>");
	return 
	"{\"figure\": \"box\"" +
		(getName(inner) == "emptyFigure" ? "" : ", \"inner\": <figToJSON(inner, figure)>") +
		"<propsToJSON(figure, parent)> }";
}

// ---------- text ----------

str figToJSON(figure: text(value v), Figure parent) = 
	"{\"figure\": \"text\", \"textValue\": <valArgQuoted(v)> <propsToJSON(figure, parent)> }";

// ---------- polygon -------

str figToJSON(figure: polygon(Vertices vertices), Figure parent) =
	"{\"figure\": \"shape\", <trVertices(vertices, shapeConnected=true, shapeCurved=false, shapeClosed=true)> <propsToJSON(figure, parent)> }";
	
// ---------- shape -------

str figToJSON(figure: shape(Vertices vertices), Figure parent) =
	"{\"figure\": \"shape\", <trVertices(vertices, shapeClosed=figure.shapeClosed, shapeCurved=figure.shapeCurved)>, \"fill-rule\": \"<figure.fillEvenOdd ? "evenodd" : "nonzero">\" <propsToJSON(figure, parent)> }";

// ---------- hcat ----------
	
str figToJSON(figure: hcat(), Figure parent){ 
	figs = figure.figs;
	return
	"{\"figure\": \"hcat\",
    ' \"inner\":   [<intercalate(",\n", [figToJSON(f, figure) | f <- figs])> 
    '              ] 
    '<propsToJSON(figure, parent)>
    '}";
}

// ---------- vcat ----------
    
str figToJSON(figure: vcat(), Figure parent) { 
	figs = figure.figs;
	return
	"{\"figure\": \"vcat\",
    ' \"inner\":   [<intercalate(",\n", [figToJSON(f, figure) | f <- figs])> 
    '              ] 
    '<propsToJSON(figure, parent)>
    '}";
}

// ---------- Charts ----------

map[str, set[str]] chartFlavors = (
	"barChart":	
		{"barChart"},
	"lineChart":
		{"lineChart", "lineWithFocusChart"}
);

// ---------- Utility for all charts -------------------

str trChart(str chartType, Figure chart, Figure parent) {
	if(chart.flavor != chartType){
		if(!chartFlavors[chartType]? || chart.flavor notin chartFlavors[chartType]){
			throw "Unknow chart flavor \"<chart.flavor>\" for <chartType>";
		}
	}
	xaxis = chart.xAxis;
	yaxis = chart.yAxis;
	dataset = chart.dataset;
	return
	"{\"figure\": \"<chartType>\",
	' \"flavor\": \"<chart.flavor>\", 
	' \"xAxis\":  {\"label\": \"<xaxis.label>\", \"tick\": \"<xaxis.tick>\" },
	' \"yAxis\":  {\"label\": \"<yaxis.label>\", \"tick\": \"<yaxis.tick>\" }
	'  <propsToJSON(chart, parent)> 
	'}";
}

// ---------- barChart ----------

str figToJSON(chart: barChart(), Figure parent) = trChart("barChart", chart, parent);

// ---------- lineChart ----------

str figToJSON(chart: lineChart(), Figure parent) = trChart("lineChart", chart, parent);

// ---------- graph ----------

str figToJSON(figure: graph(), Figure parent) { 
	nodes = figure.nodes;
	edges = figure.edges;
	println("nodes = <nodes>");
	println("edges = <edges>");
	return
	"{\"figure\": \"graph\", 
	' \"nodes\":  [<intercalate(",\n", ["{ \"id\": \"<f>\", 
	'                                      \"value\" : {\"label\": \"<f>\"
	'												    <propsToJSON(nodes[f], parent)>}}" | f <- nodes])>
	'             ],  
	' \"edges\":  [<intercalate(",\n", ["{\"u\": \"<from>\", 
	'									  \"v\": \"<to>\", 
	'									  \"value\": {\"label\": \"<label>\" <propsToJSON(e, parent)>}}"| e: edge(from,to,label) <- edges])>
	'         ]
	' <propsToJSON(figure, parent)> 
	'}";
}

// edge

str figToJSON(figure: edge(int from, int to)) {
	throw "edge should not be translated on its own";
}

// Visibility control elements

// ---------- choice ----------

str figToJSON(figure: experiments::vis2::Figure::choice(), Figure parent) { 
	int selection = figure.selection;
	choices = figure.figs;
	if(isCursor(selection)){
	   return 
		"{\"figure\": 	\"choice\",
		' \"selector\":	<trPath(toPath(selection))>,
    	' \"inner\":   [<intercalate(",\n", [figToJSON(f, figure) | f <- choices])> 
   	    '              ] 
   	    ' <propsToJSON(figure, parent)>
    	'}";
    } else {
    	throw "choice: selection should be a cursor: <selection>";
    }
 }

 // ---------- visible ----------
 
 str figToJSON(figure: visible(), Figure parent) { 
 	bool condition = figure.condition;
 	Figure fig = figure.fig;
	if(isCursor(condition)){
	   return 
		"{\"figure\":	\"visible\",
		' \"selector\":	<trPath(toPath(condition))>,
    	' \"inner\":   	<figToJSON(fig, figure)>
    	' <propsToJSON(figure, parent)> 
    	'}";
    } else {
    	throw "fswitch: condition should be a cursor: <condition>";
    }
 }   

// ---------- input elements ----------

// ---------- buttonInput ----------

str figToJSON(figure: buttonInput(), Figure parent) {
	trueText = figure.trueText;
	falseText = figure.falseText;
	return 
	"{\"figure\": 		\"buttonInput\",
 	' \"trueText\":		<strArg(trueText)>,
 	' \"falseText\":	<strArg(falseText)>
 	' <propsToJSON(figure, parent)> 
 	'}";
} 

// ---------- checboxInput ----------

str figToJSON(figure: checkboxInput(), Figure parent) = nopropsToJSON("checkboxInput", figure, parent);
   
// ---------- choiceInput ----------

str figToJSON(figure: choiceInput(), Figure parent) {
	choices = figure.choices;
	return
	"{\"figure\": 		 \"choiceInput\",
	' \"choices\":		 <choices>
	' <propsToJSON(figure, parent)> 
	'}";
}
	
// ---------- colorInput ----------

str figToJSON(figure: colorInput(), Figure parent) = nopropsToJSON("colorInput", figure, parent);

// ---------- numInput ----------

str figToJSON(figure: numInput(), Figure parent) = nopropsToJSON("numInput", figure, parent);

// ---------- rangeInput ----------

str figToJSON(figure: rangeInput(), Figure parent) {
	low = figure.low;
	high = figure.high;
	step = figure.step;
	return
	"{ \"figure\":			\"rangeInput\", 
	'  \"min\":	 			<numArg(low)>,
	'  \"max\":				<numArg(high)>,
	'  \"step\":			<numArg(step)>
	' <propsToJSON(figure, parent)> 
	'}";
}

// ---------- strInput ----------
 
str figToJSON(figure: strInput(), Figure parent) = nopropsToJSON("strInput", figure, parent);
	
// Catch missing cases

default str figToJSON(Figure f, Figure parent) { throw "figToJSON: cannot translate <f>"; }

