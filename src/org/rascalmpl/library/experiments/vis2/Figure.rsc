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
import lang::json::IO;


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
   In lines the third column determines the tooltips belonging to the points
*/
		 		 
alias XYLabeledData     = lrel[num xx, num yy, str label];	

/* Dataype belonging to candlesticks */

alias BoxData     = lrel[str date,  num low , num open, num close, num high];

alias BoxLabeledData     = lrel[str date,  num low , num open, num close, num high, str tooltip];

alias BoxHeader = tuple[str, str, str, str, str];
	
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
	| combochart(list[Chart] charts =[], ChartOptions options = chartOptions(), bool tickLabels = false,
	  int tooltipColumn = 1)
	| piechart(XYLabeledData xyLabeledData, ChartOptions options = chartOptions(), bool tickLabels = false,
	  int tooltipColumn = 1)
	| linechart(XYLabeledData xyLabeledData, ChartOptions options = chartOptions(), bool tickLabels = false,
	  int tooltipColumn = 1)
	| linechart(XYData xyData, ChartOptions options = chartOptions(), bool tickLabels = false,
	   int tooltipColumn = 1)
	| scatterchart(XYLabeledData xyLabeledData, ChartOptions options = chartOptions(), bool tickLabels = false,
	   int tooltipColumn = 1)
	| scatterchart(XYData xyData, ChartOptions options = chartOptions(), bool tickLabels = false,
	   int tooltipColumn = 1)
	| barchart(XYLabeledData xyLabeledData , ChartOptions options = chartOptions(), bool tickLabels = false,
	  int tooltipColumn = 1)
	| candlestickchart(BoxData boxData , BoxHeader header, ChartOptions options = chartOptions(), bool tickLabels = false,
	  int tooltipColumn = 1)
	| candlestickchart(BoxLabeledData boxLabeledData , BoxHeader header, ChartOptions options = chartOptions(), bool tickLabels = false,
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
 
                
data Legend (bool none = false,
             str alignment = "",
             int maxLines = -1,
             str position ="") = legend()
            ;
            
           
data ViewWindow(int max = -1, int min = -1) = viewWindow();


data Gridlines(str color = "", int count =-1) = gridlines();


data Series (
    str color ="",
    str curveType = "",
    int lineWidth = -1,
    str pointShape = "",
    int pointSize = -1,
    str \type=""
    ) = series();
    

data Bar (value groupWidth = "") = bar();

data Animation(
      int duration = -1,
      str easing = "",
      bool startup = false
      ) = animation();

data Tick(num v = -1, str f  ="") = tick();

data TextStyle(str color="", str fontName="", int fontSize=-1, 
       bool bold = false, bool italic = false) = textStyle();
    
data Axis(str title="",
          num minValue = -1,
          num maxValue = -1,
          ViewWindow viewWindow = ViewWindow::viewWindow(),
          bool slantedText = true,
          bool logScale = false,
          int slantedTextAngle = -1, 
          str textPosition = "",
          str format = "", 
           Gridlines gridlines =  Gridlines::gridlines() ,
          list[Tick] tick = [],
          TextStyle titleTextSyle = textStyle(),
          TextStyle textStyle = textStyle())
          = axis();
          
               
data Candlestick( 
     bool hollowIsRising = false,
     CandlestickColor fallingColor = candlestickColor(),
     CandlestickColor risingColor = candlestickColor()
     ) = candlestick(); 
     

data CandlestickColor( 
     str fill = "",
     str stroke = "",
     int strokeWidth = -1
     ) = candlestickColor(); 
     
                      
data ChartOptions (str title = "",
             Animation animation = Animation::animation(),
             Axis hAxis = axis(),
             Axis vAxis = axis(),
             ChartArea chartArea = ChartArea::chartArea(),
             Bar bar = Bar::bar(),
             int width=-1,
             int height = -1,
             bool forceIFrame = true,
             bool is3D = false, 
             Legend legend = Legend::legend(),
             int lineWidth = -1,
             int pointSize = -1,
             bool interpolateNulls = false,
             str curveType = "",
             str seriesType = "",
             str pointShape = "",
             bool isStacked = false,
             Candlestick candlestick = Candlestick::candlestick(),
             list[Series] series = []
             ) = chartOptions()
            ;
            

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
        if (!isEmpty(c.color)) s.color = c.color;
        if (!isEmpty(c.curveType)) s.curveType = c.curveType;
        if (c.lineWidth>=0)  s.lineWidth = c.lineWidth;
        if (!isEmpty(c.pointShape)) s.pointShape = c.pointShape;
        if (c.pointSize>=0) s.pointSize = c.pointSize;
        options.series += [s];
        }
    return options;
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
	
map[str, str] getTooltipMap(int tooltipColumn) {
    str typ = tooltipColumn < 0 || tooltipColumn == 2 ? "string":"number";
    return ("type": typ, "role":"tooltip");
    }

tuple[list[map[str, str]] header, map[tuple[value, int], list[value]] \data] 
   tData(Chart c, int tooltipColumn) {
     list[list[value]] r = [];
     list[map[str, str]] h = [];
     switch(c) {
        case line(XYData x): {r = [[d[0], d[1]]|d<-x]; h = [("type":"number")];}
        case area(XYData x): {r = [[d[0], d[1]]|d<-x]; h = [("type":"number")];}
        case line(XYLabeledData x): {
                                     r = [[d[0], d[1], tooltipColumn>=0?"<d[tooltipColumn]>":d[2]]|d<-x];
                                     h = [("type":"number"), getTooltipMap(tooltipColumn)];
                                    }
        case area(XYLabeledData x): {
                                      r = [[d[0], d[1], tooltipColumn>=0?"<d[tooltipColumn]>":d[2]]|d<-x];
                                      h = [("type":"number"), getTooltipMap(tooltipColumn)];
                                    }
        case bar(XYLabeledData x): {
                                    r = [[d[0], d[1], tooltipColumn>=0?"<d[tooltipColumn]>":d[2]]|d<-x]; 
                                    h = [("type":"number"), getTooltipMap(tooltipColumn)];
                                   }           
        }
     map[tuple[value, int], list[value]] q  = ();
     for (d<-r) {
         int i = 0;
         while(q[<d[0], i>]?) i = i + 1;
         q[<d[0], i>] = tail(d);
         }
     return <h, q>;
     }
     
bool hasXYLabeledData(Chart c) {
     switch(c) {
         case line(XYLabeledData _): return true;
         case area(XYLabeledData _): return true;
         case bar(XYLabeledData _): return true;
         }
     return false;
     } 

list[list[value]] strip(XYLabeledData b, bool tickLabels) {
     if (tickLabels) { 
         map[num, list[value]] m = (e[0]:[e[2], e[1], e[2]]|e<-b);
         list[num] x = sort(domain(m));
         return [[("type":"string"), ("type":"number"), ("type":"string", "role":"tooltip")]]
               +[m[i]|i<-x];
          }
     else {
         map[num, list[value]] m = (e[0]:[e[0], e[1], e[2]]|e<-b);
         list[num] x = sort(domain(m));
         return [[("type":"number"), ("type":"number"), ("type":"string", "role":"tooltip")]]
               +[m[i]|i<-x];
         }
     }
     
list[list[value]] strip(XYData b) {
        return [[("type":"number"), ("type":"number")]]
               +[[d[0],d[1]]|d<-b];
     } 


list[list[value]] strip(BoxData b, BoxHeader h) {
     return [[h[0], h[1], h[2], h[3], h[4]]]+[[d[0],d[1],d[2],d[3],d[4]]|d<-b];
     }  

list[list[value]] strip(BoxLabeledData b, BoxHeader h) {
     return [[h[0], h[1], h[2], h[3], h[4], ("type":"string","role":"tooltip")]]+[[d[0],d[1],d[2],d[3],d[4], d[5]]|d<-b];
     } 
     
                
list[list[value]] joinData(list[Chart] charts, bool tickLabels, int tooltipColumn) {
   list[tuple[list[map[str, str]] header, map[tuple[value, int], list[value]]\data]] m = [tData(c, tooltipColumn)|c<-charts];   
   set[tuple[value, int]] d = union({domain(c.\data)|c <-m});   
   list[tuple[value, int]] x = sort(toList(d));
   int i = 0;
   for (c<-charts) {
       if (hasXYLabeledData(c)) break;
       i = i +1;
       }
   if (!tickLabels || i == size(charts)) 
      return [[("type":"number")]+[*c.\header|c<-m]]+[[z[0]] +[*((c.\data[z]?)?c.\data[z]:"null")|c<-m]|z<-x];
   else {
      map[value, value] lab =  (z:m[i].\data[z][1]|z<-x);
      m = [tData(c, tooltipColumn)|c<-charts];  
      return 
           [[("type":"string")]+[*c.\header|c<-m]]+
           [[lab[z]] +[*((c.\data[z]?)?c.\data[z]:"null")|c<-m]|z<-x];
      }
   }
  
   
public map[str, value] adt2map(node t) {
   map[str, value] r = getKeywordParameters(t);
   for (d<-r) {
        if (node n := r[d]) {
           r[d] = adt2map(n);
        }
        if (list[node] l:=r[d]) {
           r[d] = [adt2map(e)|e<-l];
           }
      }
   return r;
   }
   
public str adt2json(node t) {
   return toJSON(adt2map(t), true);
   }