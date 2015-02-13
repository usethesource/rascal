@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module experiments::vis2::Figure

import util::Math;
import Prelude;


/* Properties */

// Position for absolute placement of figure in parent

alias Position = tuple[num x, num y];

// Alignment for relative placement of figure in parent

public alias Alignment = tuple[num hpos, num vpos];

public Alignment topLeft      	= <0.0, 0.0>;
public Alignment top          	= <0.5, 0.0>;
public Alignment topRight     	= <1.0, 0.0>;

public Alignment left   		= <0.0, 0.5>;
public Alignment center       	= <0.5, 0.5>;
public Alignment right   	 	= <1.0, 0.5>;

public Alignment bottomLeft   	= <0.0, 1.0>;
public Alignment bottom 		= <0.5, 1.0>;
public Alignment bottomRight	= <1.0, 1.0>;

// Events and bindings for input elements

data Event 
	= on()
	| on(str eventName, Bind binder)
	| on(str eventName, Figure fig)
	;
	
//alias Cursor[&T] = &T;

data Bind
    = bind(value accessor)
    | bind(value accessor, value val)
//    | delete(Cursor[&T] accessor)
//    | add(Cursor[&T] accessor, value(value model))
	;

// Data formats for various chart elements

alias XYData 			= lrel[num x, num y];

/* 
   In bars the first column (rank) determines the bar placement order.	
   The third column is the category label. 
*/
		 		 
alias XYLabeledData     = lrel[num xx, num yy, str label];	

	
//data Margin = margin(int left = 0, int right = 0, int top = 0, int bottom = 0);

/*
	link
	gradient(numr)
	texture(loc image)
	lineCap flat, rounded, padded
	lineJoin	smooth, sharp(r), clipped
	dashOffset
		
	linestyle: color, width, cap, join, dashing, dashOffset
*/

// Vertices for defining shapes.

data Vertex
	= line(num x, num y)
	| lineBy(num x, num y)
	| move(num x, num y)
	| moveBy(num x, num y)
	;
	
alias Vertices = list[Vertex];

alias Points = lrel[num x, num y];

public alias Figures = list[Figure];

public num nullFunction(list[num] x) { return 0;}

public data Figure(
        // Naming
        str id = "",
		// Dimensions and Alignmenting
		
		tuple[int,int] size = <0,0>,
		tuple[int, int, int, int] padding = <0, 0, 0, 0>,
		int width = 0,
		int height = 0,
		Position at = <0,0>,
		Alignment align = <0.5, 0.5>, // TODO should be middle,
		num grow = 1.0,
		tuple[int,int] gap = <0,0>,
		int hgap = 0,
		int vgap = 0,
   
    	// Line properties
    
		int lineWidth = 1,			
		str lineColor = "black", 		
		list[int] lineDashing = [],	
		real lineOpacity = 1.0,
	
		// Area properties

		str fillColor    = "white", 			
		real fillOpacity = 1.0,	
		str fillRule     = "evenodd",
		
		tuple[int, int] rounded = <0, 0>,

		// Font and text properties
		
		str fontFamily = "Helvetica, Arial, Verdana, sans-serif",
		str fontName = "Helvetica",
		int fontSize = 12,
		str fontStyle = "normal",		// normal|italic|oblique|initial|inherit
		str fontWeight = "normal",		//normal|bold|bolder|lighter|number|initial|inherit; normal==400, bold==700
		str fontColor = "black",
		str textDecoration	= "none",	// none|underline|overline|line-through|initial|inherit
		
		// Interaction
	
		Event event = on(),
		
		// Tooltip
		str tooltip = ""
	) =
	
	emptyFigure()

// atomic primitivesreturn [[z] +[*((c[z]?)?c[z]:"null")|c<-m]|z<-x];
	
   | text(value text)		    			// text label
   | markdown(value text)					// text with markdown markup (TODO: make flavor of text?)
   | math(value text)						// text with latex markup
   
// Graphical elements

   | box(Figure fig=emptyFigure())      	// rectangular box with inner element
   
   | ellipse(num cx = 0, num cy = 0, num rx=0, num ry=0, Figure fig=emptyFigure())
   
   | circle(num cx = 0, num cy = 0, num r=0, Figure fig=emptyFigure())
   
   | ngon(int n=3, num r=0, Figure fig=emptyFigure())	// regular polygon
   
   | polygon(Points points=[], bool fillEvenOdd = true)
   
   | shape(Vertices vertices, 				// Arbitrary shape
   			bool shapeConnected = true, 	// Connect vertices with line/curve
   			bool shapeClosed = false, 		// Make a closed shape
   			bool shapeCurved = false, 		// Connect vertices with a spline
   			bool fillEvenOdd = true,		// The fill rule to be used. (TODO: remove?)
   			Figure startMarker=emptyFigure(),
   			Figure midMarker=emptyFigure(), 
   			Figure endMarker=emptyFigure())
   
   | image(loc url=|home:///|)

// Figure composers
                   
   | hcat(Figures figs=[]) 					// horizontal and vertical concatenation
   | vcat(Figures figs=[]) 					// horizontal and vertical concatenation 
   | overlay(Figures figs=[])				// overlay (stacked) comAlignment
   | grid(list[Figures] figArray = [[]])	// grid of figures

// Figure transformations

   | at(int x, int y, Figure fig)			// Move to Alignment relative to origin of enclosing Figure
   | atX(int x, Figure fig)				// TODO: how to handle negative values?
   | atY(int y, Figure fig)
   
  	//TODO: avoid name clash with Math::util:scale
   | SCALE(num factor, Figure fig)
   
   | rotate(num angle, Figure fig)

// Input elements

   | buttonInput(str trueText = "", str falseText = "")
   | checkboxInput()
   | choiceInput(list[str] choices = [])
   | colorInput()
   
   // date
   // datetime
   // email
   // month
   // time
   // tel
   // week
   // url
   
   | numInput()
   | rangeInput(int low=0, int high=100, int step=1)
   | strInput()
   
// Visibility control elements

   | visible(bool condition=true, Figure fig = emptyFigure())
   
   | choice(int selection = 0, Figures figs = [])
  
/*
   | _computeFigure(bool() recomp,Figure () computeFig, FProperties props)
 
*/


// Charts
//	| chart(Chart c, ChartOptions options = chartOptions())
	| combo(list[Chart] charts =[], ChartOptions options = chartOptions(), bool tickLabels = false,
	  int tooltipColumn = 1)
	| piechart(XYLabeledData \data = [], ChartOptions options = chartOptions(), bool tickLabels = false,
	  int tooltipColumn = 1)


// Graphs

   | graph(lrel[str, Figure] nodes = [], Figures edges = [], str orientation = "topDown", int nodeSep = 50, int edgeSep=10, int layerSep= 30, str flavor="layeredGraph")
   | edge(str from, str to, str label)
   
// Trees
	| tree(Figure root, Figures children)
   ;
 


    
data ChartArea ( 
     value left = "",
     value width = "",
     value top = "",
     value height = "",
     value backgroundColor = ""
     ) = chartArea();
 
   
str trChartArea(ChartArea chartArea) {
    str r = "{";
        if (str v := chartArea.left)
             {if (!isEmpty(v)) r +="\"left\" : \"<v>\",";}
        else
             r += "\"left\" : <chartArea.left>";
        if (str v := chartArea.top)
             {if (!isEmpty(v)) r +="\"top\" : \"<v>\",";}
        else
             r += "\"top\" : <chartArea.top>,";
        if (str v := chartArea.width)
             {if (!isEmpty(v)) r +="\"width\" : \"<v>\",";}
        else
             r += "\"width\" : <chartArea.width>,";
        if (str v := chartArea.height)
             {if (!isEmpty(v)) r +="\"height\" : \"<v>\",";}
        else
             r += "\"height\" : <chartArea.height>,";
        if (str v := chartArea.backgroundColor)
             {if (!isEmpty(v)) r +="\"backgroundColor\" : \"<v>\",";}
        else
             r += "\"backgroundColor\" : <chartArea.backgroundColor>,";
        
    r = replaceLast(r,",", "");
    r+="}";
    return r;
    }
    
bool isEmpty (ChartArea chartArea) = 
      str left := chartArea.left && isEmpty(left)
   && str top := chartArea.top && isEmpty(top)
   && str width := chartArea.width && isEmpty(width)
   && str height := chartArea.height && isEmpty(height)
   && str backgroundColor := chartArea.backgroundColor && isEmpty(backgroundColor)
   ;
                
data Legend (bool none = false,
             str alignment = "",
             int maxLines = -1,
             str position ="") = legend()
            ;
            
bool isEmpty (Legend legend) = !legend.none && isEmpty(legend.alignment)
                && legend.maxLines == -1 && isEmpty(legend.position);

str trLegend(Legend legend) {
    if (legend.none) return "\'none\'";
    str r = "{";
    if  (!isEmpty(legend.alignment)) r +="\"alignment\" : \"<legend.alignment>\",";
    if  (!isEmpty(legend.position)) r +="\"position\" : \"<legend.position>\",";
    if  (legend.maxLines>=0) r+= "\"maxLines\" : \"<legend.maxLines>\",";
    r = replaceLast(r,",", "");
    r+="}";
    return r;
    }
           
data ViewWindow(int max = -1, int min = -1) = viewWindow();

bool isEmpty(ViewWindow w ) = w.max == -1 && w.min == -1;

str trViewWindow(ViewWindow w) {
    str r = "{";
    if  (w.min>=0) r+="\"min\" : <w.min>,";
    if  (w.max>=0) r+= "\"max\" : <w.max>,";
    r = replaceLast(r,",", "");
    r+="}";
    return r;
    }

data Gridlines(str color = "", int count =-1) = gridlines();

bool isEmpty(Gridlines g) = isEmpty(g.color) && g.count == -1 ;

str trGridlines(Gridlines g) {
    str r = "{";
    if  (!isEmpty(g.color)) r+="\"color\" : \"<g.color>\",";
    if  (g.count>=0) r+= "\"count\" : <g.count>,";
    r = replaceLast(r,",", "");
    r+="}";
    return r;
    }

data Series (
    str color ="",
    str curveType = "",
    int lineWidth = -1,
    str pointShape = "",
    int pointSize = -1,
    str \type=""
    ) = series();
    
bool isEmpty(Series s) = isEmpty(s.color) &&
       isEmpty(s.curveType) &&  isEmpty(s.pointShape) && isEmpty(s.\type) 
            && s.lineWidth == -1 && s.pointSize == -1;
            
str trSeries(Series s) {
    str r = "{";
    if  (!isEmpty(s.color)) r+="\"color\" : \"<s.color>\",";
    if  (!isEmpty(s.curveType)) r+="\"curveType\" : \"<s.curveType>\",";
    if  (s.lineWidth>=0) r+= "\"lineWidth\" : <s.lineWidth>,";
    if  (!isEmpty(s.pointShape)) r+="\"pointShape\" : \"<s.pointShape>\",";
    if  (s.pointSize>=0) r+= "\"pointSize\" : <s.pointSize>,";
    if  (!isEmpty(s.\type)) r+="\"type\" : \"<s.\type>\",";
    r = replaceLast(r,",", "");
    r+="}";
    return r;
    }

data Bar (value groupWidth = "") = bar();

str trBar(Bar bar) {
    str r = "{";
    if (str v := bar.groupWidth)
            if (!isEmpty(v)) r +="\"groupWidth\" : \"<v>\",";
        else
             r += "\"groupWidth\" : <bar.groupWidth>";
    r = replaceLast(r,",", "");
    r+="}";
    return r;
    }          
 
 bool isEmpty(Bar bar) {
    return (str v := bar.groupWidth && isEmpty(v));
    }
    
data Axis(str title="",
          int minValue = -1,
          int maxValue = -1,
          ViewWindow viewWindow = ViewWindow::viewWindow(),
          bool slantedText = true,
          int slantedTextAngle = -1, 
          str textPosition = "",
          str format = "", 
           Gridlines gridlines =  Gridlines::gridlines()) 
          = axis();
          
bool isEmpty (Axis axis) = axis.title=="" && axis.minValue==-1 && axis.maxValue == -1
     && isEmpty(axis.viewWindow) && axis.slantedText && axis.slantedTextAngle == -1
     && isEmpty(axis.textPosition) && isEmpty(axis.format);

str trAxis(Axis axis) {
    str r = "{";
    if  (!isEmpty(axis.title)) r +="\"title\" : \"<axis.title>\",";
    if  (axis.minValue>=0) r+="\"minValue\" : <axis.minValue>,";
    if  (axis.maxValue>=0) r+= "\"maxValue\" : <axis.maxValue>,";
    if (!isEmpty(axis.viewWindow)) r+="\"viewWindow\":<trViewWindow(axis.viewWindow)>,";
    if (!isEmpty(axis.gridlines)) r+="\"gridlines\":<trGridlines(axis.gridlines)>,";
    if (!axis.slantedText) r+="\"slantedText\":<axis.slantedText>,";
    if  (axis.slantedTextAngle>=0) r+="\"slantedTextAngle\" : <axis.slantedTextAngle>,";
    if  (!isEmpty(axis.textPosition)) r +="\"textPosition\" : \"<axis.textPosition>\",";
    if  (!isEmpty(axis.format)) r +="\"format\" : \"<axis.format>\",";
    r = replaceLast(r,",", "");
    r+="}";
    return r;
    }           
                
data ChartOptions (str title = "",
             Axis hAxis = axis(),
             Axis vAxis = axis(),
             ChartArea chartArea = ChartArea::chartArea(),
             Bar bar = Bar::bar(),
             int width=-1,
             int height = -1,
             bool forceIFrame = true,
             Legend legend = Legend::legend(),
             int lineWidth = -1,
             int pointSize = -1,
             bool interpolateNulls = false,
             str curveType = "",
             str seriesType = "",
             str pointShape = "",
             bool isStacked = false,
             list[Series] series = []
             ) = chartOptions()
            ;
            
str trOptions(ChartOptions options) {
            str r = "{";
            if  (!isEmpty(options.title)) r +="\"title\" : \"<options.title>\",";
            if  (!isEmpty(options.hAxis)) r +="\"hAxis\" : <trAxis(options.hAxis)>,";
            if  (!isEmpty(options.vAxis)) r +="\"vAxis\" : <trAxis(options.vAxis)>,";
            if  (!isEmpty(options.chartArea)) r +="\"chartArea\" : <trChartArea(options.chartArea)>,";
            if  (!isEmpty(options.bar)) r +="\"bar\" : <trBar(options.bar)>,";
            if  (options.width>=0) r+="\"width\" : <options.width>,";
            if  (options.height>=0) r+= "\"height\" : <options.height>,";
            r+= "\"forceIFrame\":<options.forceIFrame>,";
            /* if  (!isEmpty(options.legend)) */ 
            if (!options.legend.none) r +="\"legend\" : <trLegend(options.legend)>,";
            if  (options.lineWidth>=0) r+="\"lineWidth\" : <options.lineWidth>,";
            if  (options.pointSize>=0) r+="\"pointSize\" : <options.pointSize>,";
            if  (options.interpolateNulls)  r+= "\"interpolateNulls\":\"<options.interpolateNulls>\",";
            if  (!isEmpty(options.curveType)) r +="\"curveType\" : \"<options.curveType>\",";
            if  (!isEmpty(options.seriesType)) r +="\"seriesType\" : \"<options.seriesType>\",";
            if  (!isEmpty(options.pointShape)) r +="\"pointShape\" : \"<options.pointShape>\",";
            if  (options.isStacked) r +="\"isStacked\" : \"<options.isStacked>\",";
            if  (!isEmpty(options.series)) r+=  "\"series\" : [<intercalate(",", ["<trSeries(q)>" |q<-options.series])>],";
            r = replaceLast(r,",", "");
            r+="}";
            // println(chart);
            return r;
 
      }

ChartOptions updateOptions (list[Chart] charts, ChartOptions options) {
    options.series = [];
    for (c<-charts) {
        Series s = series();
        switch(c) {
            case Chart::line(XYData d1): s.\type = "line";
            case Chart::line(XYLabeledData d2): s.\type = "line";
            case Chart::area(XYData d3): s.\type=  "area";
            case Chart::area(XYLabeledData d4): s.\type=  "area";
            case Chart::bar(_) : s.\type = "bars";
            }
        s.color = c.color;
        s.curveType = c.curveType;
        s.lineWidth = c.lineWidth;
        s.pointShape = c.pointShape;
        s.pointSize = c.pointSize;
        options.series += [s];
        }
    return options;
    }
            
data Column  = column(str \type="", str label="", str role = "");      

str trColumn(Column c) {
    return
    "{\"label\":\"<c.label>\",
    ' \"type\": \"<c.\type>\",
    ' \"role\": \"<c.role>\"
    '}";
    }


data Chart(str name = "", str color = "", str curveType = "",
     int lineWidth = -1, str pointShape = "", int pointSize = -1)
    =
	  line(XYData xydata) 
	| line(XYLabeledData xylabeleddata) 
	| area(XYData xydata)
	| area(XYLabeledData xylabeleddata)
	| bar(XYLabeledData xylabeledData)
	;
	
alias XYLabeledData     = lrel[num xx, num yy, str label];	

map[tuple[value, int], list[value]] 
   tData(Chart c, bool inChart, int tooltipColumn) {
     list[list[value]] r = [];
     switch(c) {
        case line(XYData x): r = [[d[0], d[1]]|d<-x];
        case area(XYData x): r = [[d[0], d[1]]|d<-x];
        case line(XYLabeledData x): r = [[d[0], d[1], inChart?"<d[tooltipColumn]>":d[2]]|d<-x];
        case area(XYLabeledData x): r = [[d[0], d[1], inChart?"<d[tooltipColumn]>":d[2]]|d<-x];
        case bar(XYLabeledData x): r = [[d[0], d[1], inChart?"<d[tooltipColumn]>":d[2]]|d<-x];
            
        }
     map[tuple[value, int], list[value]] q  = ();
     for (d<-r) {
         int i = 0;
         while(q[<d[0], i>]?) i = i + 1;
         q[<d[0], i>] = tail(d);
         }
     return q;
     }

list[Column] cData(Chart c) {
     switch(c) {
        case line(XYData x): return [column(\type="number", label = c.name, role="data")];
        case area(XYData x): return [column(\type="number", label = c.name, role="data")];
        case line(XYLabeledData x): return [column(\type="number", label = c.name, role="data")
                                           ,column(\type="string", label = c.name, role="tooltip")
                                           ];
        case area(XYLabeledData y): return [column(\type="number", label = c.name, role="data")
                                           ,column(\type="string", label = c.name, role="tooltip")
                                           ];
        case bar(XYLabeledData x): return [column(\type="number", label = c.name, role="data")
                                           ,column(\type="string", label = c.name, role="tooltip")
                                          ];
        }
     return column();
     }
     
     
list[Column] joinColumn(list[Chart] charts, bool tickLabels) {
   int i = 0;
   for (c<-charts) {
       /*if (bar(_):=c)*/  if (size(cData(c))==2) break;
       i = i +1;
       }
     list[Column] r = [*cData(c)|c<-charts];
     if (!tickLabels || i == size(charts)) 
       return [column(\type="number", role="domain")]+r;
     else return [column(\type="string", role="domain", label = charts[i].name)]+ r;
     }
     
list[list[value]] strip(XYLabeledData d) {
     map[num, list[value]] m = (e[0]:[e[2], e[1]]|e<-d);
     list[num] x = sort(domain(m));
     return [m[i]|num i<-x];
     }
     
list[list[value]] joinData(list[Chart] charts, bool tickLabels, int tooltipColumn) {
   list[map[tuple[value, int], list[value]]] m = [tData(c, false, tooltipColumn)|c<-charts];   
   set[tuple[value, int]] d = union({domain(c)|c <-m });   
   list[tuple[value, int]] x = sort(toList(d));
   // println(x);
   int i = 0;
   for (c<-charts) {
       /*if (bar(_):=c)*/ if (size(cData(c))==2)  break;
       i = i +1;
       }
  
   // println("bar:<i>  <[[m[i][z][1]]|z<-x]>");
   if (!tickLabels || i == size(charts)) 
      return [[z[0]] +[*((c[z]?)?c[z]:"null")|c<-m]|z<-x];
   else {
      map[value, value] lab =  (z:m[i][z][1]|z<-x);
      m = [tData(c, true, tooltipColumn)|c<-charts];  
      return [[lab[z]] +[*((c[z]?)?c[z]:"null")|c<-m]|z<-x];
      }
   }
	
