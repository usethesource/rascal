module experiments::vis2::Figure

import util::Math;
import List;
import Set;
import IO;
import String;
import ToString;
import experiments::vis2::vega::Json;

/* Properties */

// Position for absolute placement of figure in parent

alias Position = tuple[num x, num y];

// Alignment for relative placement of figure in parent

alias Alignment = tuple[num hpos, num vpos];

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
			 		 
alias LabeledData 		= lrel[str label, num val];		

alias ErrorData			= lrel[str label, num mean, num low, num high];	

alias Datasets[&T] 		= map[str name, &T values];

data Axis 
	= axis(str label ="",  str tick = "d")
	;
	
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
	
		// Dataset for chart-like layouts
	
		Datasets datasets = (),
		
		// Tooltip
		str tooltip = ""
	) =
	
	emptyFigure()

// atomic primitives
	
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

// More advanced figure elements

// Charts
   
   | barChart(Axis xAxis=axis(), Axis yAxis=axis(), Datasets[LabeledData] datasets = (), str orientation = "vertical", bool grouped = false, str flavor ="nvBarChart") 
   | vega(str dataFile = "",  VEGA() command = (){return VEGA();}, str \module ="experiments::vis2::vega::VegaChart", Datasets[LabeledData] datasets = ())      
   | scatterPlot()
   
   | lineChart(Axis xAxis=axis(), Axis yAxis=axis(), Datasets[XYData] datasets = (), bool area = false, str flavor ="nvLineChart")
     
// Graphs

   | graph(lrel[str, Figure] nodes = (), Figures edges = [], str orientation = "topDown", int nodeSep = 50, int edgeSep=10, int layerSep= 30, str flavor="layeredGraph")
   | edge(str from, str to, str label)
   
// Trees
	| tree(Figure root, Figures children)
   ;
 



