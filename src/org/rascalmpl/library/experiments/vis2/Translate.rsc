module experiments::vis2::Translate

import experiments::vis2::Figure;
import Node;
import String;
import IO;
import List;
import ListRelation;
import util::Cursor;
import Type;
import lang::json::IO;
import util::Math;

private str site = ""; // localhost as string (for current translation)

/*
 * Translate a Figure to JSON
 */
 
 str figToJSON(Figure fig, str s){
 	site = s;
 	// println(fig);
 	return r = figToJSON(fig, emptyFigure());
 }
 
 
/******************** Translate figure properties ************************************/

void check(str property, str val, set[str] allowed){
	if(val notin allowed){
		throw "property <property> has value <val> but should be one of <allowed>";
	}
}

str propsToJSON(Figure child, Figure parent){
	properties = [];
	defaults = emptyFigure();
	if (!isEmpty(child.id))   properties += "\"id\":<strArg(child.id)>";
	if (!isEmpty(child.tooltip))   properties += "\"tooltip\":<strArg(child.tooltip)>";
	if(child.size != parent.size /*&& child.size != defaults.size*/) 					
													properties += "\"width\": <numArg(child.size[0])>, \"height\": <numArg(child.size[1])> ";
						
properties += "\"padding_top\": <numArg(child.padding[0])>, \"padding_left\": <numArg(child.padding[1])>,  \"padding_bottom\": <numArg(child.padding[2])>, \"padding_right\": <numArg(child.padding[3])>";
												  
	if(child.width != parent.width) 				properties += "\"width\": <numArg(child.width)>";
	if(child.height != parent.height) 				properties += "\"height\": <numArg(child.height)>";
	
	if(child.align != parent.align) 				properties += "\"halign\": <numArg(child.align[0])>, \"valign\": <numArg(child.align[1])>";
	
	if(child.grow != parent.grow) 					properties += "\"grow\": <numArg(child.grow)>";											  	  
	
	if(child.gap != parent.gap) 					properties += "\"hgap\": <numArg(child.gap[0])>,
												  		          '\"vgap\": <numArg(child.gap[1])> ";
												  
	if(child.hgap != parent.hgap) 					properties += "\"hgap\": <numArg(child.hgap)>";
	if(child.vgap != parent.vgap) 					properties += "\"vgap\": <numArg(child.vgap)>";
	
	if(child.lineWidth != parent.lineWidth) 		properties += "\"stroke-width\": <numArg(child.lineWidth)>";
	
	if(child.lineColor != parent.lineColor) 		properties += "\"stroke\": <strArg(child.lineColor)>";
	
	if(child.lineDashing != parent.lineDashing) 	properties += "\"stroke-dasharray\": <child.lineDashing>";			// TODO
	
	if(child.fillColor != parent.fillColor) 		properties += "\"fill\": <strArg(child.fillColor)>";
	
	if(child.lineOpacity != parent.lineOpacity)		properties += "\"stroke-opacity\": <numArg(child.lineOpacity)>";
	
	if(child.fillOpacity != parent.fillOpacity)		properties += "\"fill-opacity\": <numArg(child.fillOpacity)>";
	
	if(child.fillRule != parent.fillRule){
		check("fillRule", child.fillRule, {"nonzero", "evenodd"});
		properties += "\"fill-rule\": <strArg(child.fillRule)>";
	}
	
	if(child.rounded != parent.rounded)				properties += "\"rx\": <numArg(child.rounded[0])>, \"ry\": <numArg(child.rounded[1])>";
	
  
												  	  
	if(child.fontFamily != parent.fontFamily) 		properties += "\"font-family\": <strArg(child.fontFamily)>";											  
	if(child.fontName != parent.fontName) 			properties += "\"font-name\": <strArg(child.fontName)>";
	
	if(child.fontSize != parent.fontSize) 			properties += "\"font-size\": <numArg(child.fontSize)>";

	check("fontStyle", child.fontStyle, {"normal", "italic", "oblique"});
	if(child.fontStyle != parent.fontStyle) 		properties += "\"font-style\": <strArg(child.fontStyle)>";

	check("fontWeight", child.fontWeight, {"normal", "bold", "bolder", "lighter"});
	if(child.fontWeight != parent.fontWeight) 		properties += "\"font-weight\": <strArg(child.fontWeight)>";

	check("textDecoration", child.textDecoration, {"none","underline", "overline", "line-through"});
	if(child.textDecoration != parent.textDecoration) 
													properties += "\"text-decoration\": <strArg(child.textDecoration)>";
	if(child.fontColor != parent.fontColor) 		properties += "\"font-color\": <strArg(child.fontColor)>";
	
//	if(child.dataset != parent.dataset) 			properties += trDataset(child.dataset);
	
	if(child.event != parent.event && child.event != on()){
		if(!isLegalEvent(child.event.eventName)){
			throw "non-existing event name: <child.event>";
		}
	
	    // println("propsToJSON: <child.event>");
		switch(child.event){
			case on(str event, binder: bind(accessor)): {
				if(isCursor(accessor)){ 
				    // println("QQQ bind1:<accessor>");	
					properties += [
						"\"event\": \"<event>\"",
						//"\"type\":  \"<typeOf(accessor)>\"", 
						"\"accessor\": <trCursor(accessor, deep = true)>"  // Must [1] behind
						];
				    // println("QQQ bind2:<properties>");
 				} else {
   				  	throw "on: accessor <accessor> in binder is not a cursor";
   				}
   				}
			case  on(str event, bind(accessor, replacement)): {
			    // println("bind:<trCursor(accessor)> <replacement>");
				// println("accessor: <accessor>, isCursor: <isCursor(accessor)>");
				if(isCursor(accessor)){ 	
					properties += [
						"\"event\":  		\"<event>\"",
					//	"\"type\": 			\"int()\", 					//\"<typeOf(accessor)>\"",
						"\"accessor\": 		<trCursor(accessor)>",
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


str trVertices(list[Vertex] vertices, bool shapeClosed = true, bool shapeCurved = true, bool shapeConnected = true) {
	//<width, height>  = bbox(vertices);
	
	str path = "M<toInt(vertices[0].x)> <toInt(vertices[0].y)>"; // Move to start point
	int n = size(vertices);
	if(shapeConnected && shapeCurved && n > 2){
		path += "Q<toInt((vertices[0].x + vertices[1].x)/2.0)> <toInt((vertices[0].y + vertices[1].y)/2.0)> <toInt(vertices[1].x)> <toInt(vertices[1].y)>";
		for(int i <- [2 ..n]){
			v = vertices[i];
			path += "<isAbsolute(v) ? "T" : "t"><toInt(v.x)> <toInt(v.y)>"; // Smooth point on quadartic curve
		}
	} else {
		for(int i <- [1 .. n]){
			v = vertices[i];
			path += "<directive(v)><toInt(v.x)> <toInt(v.y)>";
		}
	}
	
	if(shapeConnected && shapeClosed) path += "Z";
	
	return "\"path\":    \"<path>\"";		   
}

bool isAbsolute(Vertex v) = (getName(v) == "line" || getName(v) == "move");

str directive(Vertex v) = ("line": "L", "lineBy": "l", "move": "M", "moveBy": "m")[getName(v)];


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
/*
 = root(str name)
  | field(str name)
  | field(int position)
  | argument(int position)
  | argument(str name)
  | keywordParam(str name)
  | element(int index)
  | sublist(int from, int to)
  | lookup(value key)
  | select(list[int] indices)
  | select(list[str] labels)
  ;
  */
  
str trCursor(value v, bool deep = false){
	path = toPath(v);
    accessor = "Figure.model";
	for(nav <- path){
		switch(nav){
		 	//case root(str name):		accessor += ".<name>"; 		// ???
		 	
			case field(int position):	accessor += "[1][<position>]";
			//case field(str name): 		accessor += "[1][3].<name>";
			
			case argument(int position):accessor += "[1][2][<position>]";
			//case argument(str name):	accessor += "[1][3].<name>";
	
			case keywordParam(str name):accessor += "[1][3].<name>";
  
			case element(int index):	accessor += "[1][<index>]";
			
  			case sublist(int from, int to):	accessor += "[1].slice(<from>,<to>]";
  			
  			case lookup(value key):		accessor += "[1].lookup(<key>)";			// welk geval is dit ook alweer?
  			
  			//case select(list[int] indices):
  			//							accessor += "";		// TODO
  			//case select(list[str] labels):
  			//							accessor += "";		// TODO
  			default:
  				throw "Unsupported path element <nav>";
  		}
  	} 
  	if (deep) accessor += "[1]";   // Bert
  	// println("trCursor: <v>, <path>: <accessor>");
  	return "\"<accessor>\"";
}

str toJSNumber(num n) {
	s = "<n>";
	return (s[-1] == "." ? "<s>0" : s);
}

str escape(str s) = escape(s, (	"\"" : "\\\"", 
								"\'" : "\\\'",
								"\\" : "\\\\",
								"\n" : "\\n"	
								));


str numArg(num n) 	= isCursor(n) ? "{\"use\": <trCursor(n, deep = true)>}" : toJSNumber(n);

str strArg(str s) 	= isCursor(s) ? "{\"use\": <trCursor(s, deep= true)>}" : "\"<escape(s)>\"";

str locArg(loc v) = isCursor(v) ? "{\"use\": <trCursor(v, deep = true)>}" : 
					(v.scheme == "file" ? "\"<site>/<v.path>\"" : "\"<"<v>"[1..-1]>\"");

str valArg(value v) = isCursor(v) ? "{\"use\": <trCursor(v, deep = true)>}" : "<v>";

str valArgQuoted(value v) = isCursor(v) ? "{\"use\": <trCursor(v, deep = true)>}" : "\"<escape("<v>")>\"";		

/******************** Translate figures ************************************/
		
// Graphical elements	

// Figure without properties of its own

str basicToJSON(str kind, Figure child, Figure parent, str extraProps="") = 
	"{\"figure\": \"<kind>\" <isEmpty(extraProps) ? "" : ", <extraProps>"> <propsToJSON(child, parent)> }";

// ---------- box ----------

str figToJSON(figure: box(), Figure parent) {
	inner = figure.fig; 
	println("inner = <inner>");
	return getName(inner) == "emptyFigure"
		   ? "{\"figure\": \"box\" <propsToJSON(figure, parent)> }"
		   : "{\"figure\": \"box\",
    		 ' \"inner\":  <figToJSON(inner, figure)>
			 '  <propsToJSON(figure, parent)> 
	         '}";
}

// ---------- ellipse ----------

str figToJSON(figure: ellipse(), Figure parent) {
	inner = figure.fig; 
	println("inner = <inner>");
	rx = figure.rx > 0 ? ", \"rx\": <figure.rx>" : "";
	ry = figure.ry > 0 ? ", \"ry\": <figure.ry>" : "";
	return getName(inner) == "emptyFigure"
		   ? "{\"figure\": \"ellipse\" <rx> <ry> <propsToJSON(figure, parent)> }"
		   : "{\"figure\": \"ellipse\" <rx> <ry>,
    		 ' \"inner\":  <figToJSON(inner, figure)>
			 '  <propsToJSON(figure, parent)> 
	         '}";
}

// ---------- circle ----------

str figToJSON(figure: circle(), Figure parent) {
	inner = figure.fig; 
	println("inner = <inner>");
	r = figure.r > 0 ? ", \"rx\": <figure.r>, \"ry\": <figure.r>" : ""; 
	return getName(inner) == "emptyFigure"
		   ? "{\"figure\": \"ellipse\", \"circle\": \"true\" <r> <propsToJSON(figure, parent)> }"
		   : "{\"figure\": \"ellipse\", \"circle\": \"true\" <r>,
    		 ' \"inner\":  <figToJSON(inner, figure)>
			 '  <propsToJSON(figure, parent)> 
	         '}";
}

// ---------- ngon -------

str figToJSON(figure: ngon(), Figure parent) {
	inner = figure.fig; 
	println("inner = <inner>");
	return getName(inner) == "emptyFigure"
		   ? "{\"figure\": \"ngon\", \"n\": <figure.n>, \"r\": <figure.r> <propsToJSON(figure, parent)> }"
		   : "{\"figure\": \"ngon\", \"n\": <figure.n>, \"r\": <figure.r>,
    		 ' \"inner\":  <figToJSON(inner, figure)>
			 '  <propsToJSON(figure, parent)> 
	         '}";
}

str figToJSON(figure: ngon(), Figure parent) =
	"{\"figure\": \"ngon\", \"n\": <figure.n>, \"r\": <figure.r>  <propsToJSON(figure, parent)> }";

// ---------- polygon -------

str figToJSON(figure: polygon(), Figure parent) =
	"{\"figure\": \"polygon\", \"points\": <[[x,y] | <x, y> <- figure.points]>,
	' \"fill-rule\": \"<figure.fillEvenOdd ? "evenodd" : "nonzero">\" <propsToJSON(figure, parent)> 
	'}";


// ---------- text ----------

str figToJSON(figure: text(value v), Figure parent) = 
	"{\"figure\": \"text\", \"textValue\": <valArgQuoted(v)> <propsToJSON(figure, parent)> }";

// ---------- markdown ----------

str figToJSON(figure: markdown(value v), Figure parent) = 
	"{\"figure\": \"markdown\", \"textValue\": <valArgQuoted(v)> <propsToJSON(figure, parent)> }";

// ---------- math ----------

str figToJSON(figure: math(value v), Figure parent) = 
	"{\"figure\": \"math\", \"textValue\": <valArgQuoted(v)> <propsToJSON(figure, parent)> }";

	
// ---------- image ----------

str figToJSON(figure: image(), Figure parent) {
	//img = readFile(figure.url);
	return "{\"figure\": \"image\", \"url\": <locArg(figure.url)> <propsToJSON(figure, parent)> }";
}


	
// ---------- shape -------

str figToJSON(figure: shape(Vertices vertices), Figure parent) {
	startMarker = figure.startMarker;
	midMarker = figure.midMarker;
	endMarker = figure.endMarker;
	return
	"{\"figure\": \"shape\", <trVertices(vertices, shapeClosed=figure.shapeClosed, shapeCurved=figure.shapeCurved)>, 
	' \"fill-rule\": \"<figure.fillEvenOdd ? "evenodd" : "nonzero">\",
	' \"inner\": [ <startMarker == emptyFigure() ? "{\"figure\": \"empty\"}" : figToJSON(startMarker, figure)>,
	'            <midMarker   == emptyFigure() ? "{\"figure\": \"empty\"}" : figToJSON(midMarker, figure)>,
	'            <endMarker   == emptyFigure() ? "{\"figure\": \"empty\"}" : figToJSON(endMarker, figure)>
	'          ]
	' <propsToJSON(figure, parent)> }";
}

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

// ---------- overlay ----------
    
str figToJSON(figure: overlay(), Figure parent) { 
	figs = figure.figs;
	return
	"{\"figure\": \"overlay\",
    ' \"inner\":   [<intercalate(",\n", [figToJSON(f, figure) | f <- figs])> 
    '              ] 
    '<propsToJSON(figure, parent)>
    '}";
}

// ---------- grid ----------
    
str figToJSON(figure: grid(), Figure parent) { 
	figArray = figure.figArray;
	
	array = [ [ figToJSON(figArray[i][j], figure) | j <- index(figArray[i])] | i <- index(figArray)];
	inner = "[ <intercalate(",\n", [ "[ <intercalate(",\n", row)> ]" | row <- array ])> ]";
	
	return
	"{\"figure\": \"grid\",
    ' \"inner\":   <inner>
    '<propsToJSON(figure, parent)>
    '}";
}
   
// ---------- transformations ---------- 

// at

str figToJSON(figure: at(int x, int y, Figure fig), Figure parent) {
	return
	"{\"figure\": 	\"at\",
	' \"x\":		<x>,
	' \"y\":		<y>,
    ' \"inner\":  	<figToJSON(fig, figure)> 
    ' <propsToJSON(figure, parent)>
    '}";
}

str figToJSON(figure: atX(int x, Figure fig), Figure parent) {
	return
	"{\"figure\": 	\"atX\",
	' \"x\":		<x>,
    ' \"inner\":  	<figToJSON(fig, figure)> 
    ' <propsToJSON(figure, parent)>
    '}";
}

str figToJSON(figure: atY(int y, Figure fig), Figure parent) {
	return
	"{\"figure\": 	\"atY\",
	' \"y\":		<y>,
    ' \"inner\":  	<figToJSON(fig, figure)> 
    ' <propsToJSON(figure, parent)>
    '}";
}

// scale

str figToJSON(figure: SCALE(num factor, Figure fig), Figure parent) {
	return
	"{\"figure\": 	\"scale\",
	' \"xfactor\":	<factor>,
	' \"yfactor\":	<factor>,
    ' \"inner\":  	<figToJSON(fig, figure)> 
    ' <propsToJSON(figure, parent)>
    '}";
}

// rotate

str figToJSON(figure: rotate(num angle, Figure fig), Figure parent){
	return
	"{\"figure\": 	\"rotate\",
	' \"angle\":	<angle>,
    ' \"inner\":  	<figToJSON(fig, figure)> 
    ' <propsToJSON(figure, parent)>
    '}";
}

// ---------- Charts ----------

// --------- Describe all flavors of all chart layouts

map[str, set[str]] layoutFlavors = (
	"graph":
		{"layeredGraph", "springGraph"}
);

// ---------- Generate different output formats


num getClass(num lo, num hi, int N, num v) {
    num r = round(lo +  floor(N*(v-lo)/(hi-lo))*(hi-lo)/N, 0.01);
    // println("getClassR:<lo> <hi> <N> <r>");
    return r;
    }

XYData getHistogramData(tuple[int nTickMarks, list[num] \data] x) {
      XYData r =  [<getClass(min(x[1]), max(x[1]), x.nTickMarks, d), 1>|d <-x[1]];
      // println(r);
      return r;
      }

//XYData inject(XYData r, num(list[num]) f) {
//     if (f == nullFunction) {return r;}
//     return [<k, f([v[1]| v<-domainR(r,{k})])>|k<-domain(r)];
//     }
//     
//XYLabeledData inject(LabeledData r, num(list[num]) f) {
//     if (f == nullFunction) {return r;}
//     return [<k, f([v[1]|v<-domainR(r,{k})])>|k<-domain(r)];
//     }
// LabeledData



// ---------- Utility for all charts -------------------

    
 str trCombo(Figure chart, Figure parent) {
    list[Chart] charts = chart.charts;
    str d = 
      intercalate(",", joinData(charts, chart.tickLabels, chart.tooltipColumn));
    str c = intercalate(",", [trColumn(q)|q<-joinColumn(charts, chart.tickLabels)]);
    str cmd = "ComboChart";
    ChartOptions options = updateOptions(charts, chart.options);
    if (options.width>=0) chart.width = options.width;
    if (options.height>=0) chart.height = options.height;
    return 
    "{\"figure\": \"google\",
     '\"command\": \"<cmd>\",
    ' \"options\": <trOptions(options)>,
    ' \"data\": [<d>],
    '\"columns\": [<c>] 
    '  <propsToJSON(chart, parent)> 
    '}";   
   
    }
    
 str trPieChart(Figure chart, Figure parent) {
    str d = intercalate(",", strip(chart.\data));
    list[Column] columns = [
        column(\type="string", role="domain", label = "domain"),
        column(\type="number", role="data", label = "value")
        ];
    str c = intercalate(",", [trColumn(q)|q<-columns]);
    str cmd = "PieChart";
    ChartOptions options = chart.options;
    if (options.width>=0) chart.width = options.width;
    if (options.height>=0) chart.height = options.height;
    return 
    "{\"figure\": \"google\",
     '\"command\": \"<cmd>\",
    ' \"options\": <trOptions(options)>,
    ' \"data\": [<d>],
    '\"columns\": [<c>] 
    '  <propsToJSON(chart, parent)>  
    '}";   
   
    }

// ---------- barChart ----------

//str figToJSON(chart: barChart(), Figure parent) {
//
//	if(chart.orientation notin {"vertical", "horizontal"}){
//		throw "orientation has illegal value: <chart.orientation>";
//	}
//	return trChart("barChart", chart, parent, 
//		extraProps="\"orientation\": \"<chart.orientation>\", \"grouped\": <chart.grouped>");
//}


//str figToJSON(chart: vegaChart(), Figure parent) {
//	return trVega(chart, parent);
//}

str figToJSON(chart: combo(), Figure parent) {
    // println("trCombo");
	return trCombo(chart, parent);
}

str figToJSON(chart: piechart(), Figure parent) {
    // println("trPiechart");
	return trPieChart(chart, parent);
}

// ---------- lineChart ----------

// str figToJSON(chart: lineChart(), Figure parent) = trChart("lineChart", chart, parent, extraProps="\"area\": <chart.area>");


// ---------- graph ----------
// str orientation = "topDown", int nodeSep = 10, int edgeSep=10, int layerSep= 10, 

str figToJSON(figure: graph(), Figure parent) { 
	if(!layoutFlavors["graph"]? || figure.flavor notin layoutFlavors["graph"]){
		throw "Unknow graph flavor \"<figure.flavor>\"";
	}
	
	if(figure.orientation notin {"topDown", "leftRight"}){
	   throw "orientation has illegal value: <figure.orientation>";
	}
	nodes = figure.nodes;
	edges = figure.edges;
	println("nodes = <nodes>");
	println("edges = <edges>");
	return
	"{\"figure\": \"graph\", 
	' \"flavor\": \"<figure.flavor>\",
	' \"nodeSep\": <figure.nodeSep>,
	' \"edgeSep\": <figure.edgeSep>,
	' \"rankSep\": <figure.layerSep>,
	' \"rankDir\": <figure.orientation == "topDown" ? "\"TD\"" : "\"LR\"">,
	' \"nodes\":  [<intercalate(",\n", ["{ \"name\": \"<f[0]>\",
									    '  \"inner\":  <figToJSON(f[1], figure)>
 										'}" | f <- nodes])>
	'           ],  
	' \"edges\":  [<intercalate(",\n", ["{\"name\": \"<lab>\", \"source\" : \"<from>\", \"target\": \"<to>\" <propsToJSON(e, figure)>}"| e: edge(from,to,lab) <- edges])>
	'         ]
	' <propsToJSON(figure, parent)> 
	'}";
}

//str figToJSON(figure: graph(), Figure parent) { 
//	if(!layoutFlavors["graph"]? || figure.flavor notin layoutFlavors["graph"]){
//		throw "Unknow graph flavor \"<figure.flavor>\"";
//	}
//	nodes = figure.nodes;
//	edges = figure.edges;
//	println("nodes = <nodes>");
//	println("edges = <edges>");
//	return
//	"{\"figure\": \"graph\", 
//	' \"flavor\": \"<figure.flavor>\",
//	' \"nodes\":  [<intercalate(",\n", ["{ \"id\": \"<f>\", \"name\": \"<f>\",
//	'                                      \"value\" : {\"label\": \"<f>\",
//														\"inner\":  <figToJSON(nodes[f], parent)>
//	'												    <propsToJSON(nodes[f], parent)>}}" | f <- nodes])>
//	'             ],  
//	' \"edges\":  [<intercalate(",\n", ["{\"u\": \"<from>\", \"source\" : \"<from>\",
//	'									  \"v\": \"<to>\", \"target\": \"<to>\",
//	'									  \"value\": {\"label\": \"<label>\" <propsToJSON(e, parent)>}}"| e: edge(from,to,label) <- edges])>
//	'         ]
//	' <propsToJSON(figure, parent)> 
//	'}";
//}

// edge

str figToJSON(figure: edge(str from, str to, str lab)) {
	throw "edge should not be translated on its own";
}

// Visibility control elements

// ---------- choice ----------

//str figToJSON(figure: experiments::vis2::Figure::choice(), Figure parent) { 
//	int selection = figure.selection;
//	choices = figure.figs;
//	if(isCursor(selection)){
//	   return 
//		"{\"figure\": 	\"choice\",
//		' \"selector\":	<trCursor(selection)>,
//    	' \"inner\":   [<intercalate(",\n", [figToJSON(f, figure) | f <- choices])> 
//   	    '              ] 
//   	    ' <propsToJSON(figure, parent)>
//    	'}";
//    } else {
//    	throw "choice: selection should be a cursor: <selection>";
//    }
// }

 // ---------- visible ----------
 
 str figToJSON(figure: visible(), Figure parent) { 
 	bool condition = figure.condition;
 	Figure fig = figure.fig;
	if(isCursor(condition)){
	   return 
		"{\"figure\":	\"visible\",
		' \"selector\":	<trCursor(condition)>,
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

str figToJSON(figure: checkboxInput(), Figure parent) = basicToJSON("checkboxInput", figure, parent);
   
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

str figToJSON(figure: colorInput(), Figure parent) = basicToJSON("colorInput", figure, parent);

// ---------- numInput ----------

str figToJSON(figure: numInput(), Figure parent) = basicToJSON("numInput", figure, parent);

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
 
str figToJSON(figure: strInput(), Figure parent) = 
	basicToJSON("strInput", figure, parent);
	
// Catch missing cases

default str figToJSON(Figure f, Figure parent) { throw "figToJSON: cannot translate <f>"; }

