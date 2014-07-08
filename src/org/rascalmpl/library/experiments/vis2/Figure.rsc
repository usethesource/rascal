module experiments::vis2::Figure

import util::Math;
import List;
import Set;
import IO;
import String;
import ToString;

/* Properties */

alias Cursor[&T] = &T;

data Bind[&T]
    = bind(Cursor[&T] accessor)
    | bind(Cursor[&T] accessor, &T val)
    | delete(Cursor[&T] accessor)
//    | add(Cursor[&T] accessor, value(value model))
	;

alias Position = tuple[num hpos, num vpos];

public Position topLeft      = <0.0, 0.0>;
public Position topMiddle    = <0.5, 0.0>;
public Position topRight     = <1.0, 0.0>;

public Position middleLeft   = <0.0, 0.5>;
public Position middle       = <0.5, 0.5>;
public Position midleRight   = <0.5, 1.0>;

public Position bottomLeft   = <0.0, 1.0>;
public Position bottomMiddle = <0.5, 1.0>;
public Position bottomRight  = <1.0, 1.0>;

/*
 * Figure: a visual element, the principal visualization datatype
 * Note: for experimentation purposes this is a small extract from the real thing: vis/Figure.rsc
 */
 
public alias Figures = list[Figure];

data Event 
	= on()
	| on(str eventName, Bind[value] binder)
	| on(str eventName, Figure fig)
	;
	
data XYData = xyData(lrel[num,num] pairs, 				// <x, y> values
		     		 str color="black", 				// color of line
			 		 bool area = false);				// fill area below line

alias LabeledData = lrel[str label, num val];			// <label, number> values

alias Dataset[&Kind] = map[str name, &Kind values];

data Axis 
	= axis(str label ="",  str tick = "d")
	;
	
data Margin = margin(int left = 0, int right = 0, int top = 0, int bottom = 0);

/*
	ngo,
	polygon
	image
	link
	gradient(numr)
	texture(loc image)
	lineCap flat, rounded, padded
	lineJoin	smooth, sharp(r), clipped
	dashOffset
		
	linestyle: color, width, cap, join, dashing, dashOffset
*/

data Vertex
	= vertex(num x, num y)
	| vertexBy(num x, num y)
	;
	
alias Vertices = list[Vertex];

public data Figure(
		tuple[int,int] size = <-1,-1>,
		int width = -1,
		int height = -1,
		Position pos = <0.5, 0.5>, // TODO should be middle,
	
		num grow = 1.0,
		tuple[int,int] gap = <0,0>,
		int hgap = 0,
		int vgap = 0,
   
    	// lines
    
		int lineWidth = 1,			
		str lineColor = "black", 		
		list[int] lineDashing = [],	
		real lineOpacity = 1.0,
	
		// areas

		str fillColor    = "white", 			
		real fillOpacity = 1.0,	
		str fillRule     = "nonzero",		// or "evenodd"
		
		tuple[int, int] rounded = <0, 0>,
	
		

		// fonts and text
		
		str fontFamily = "sans-serif",
		str fontName = "Helvetica", 	// was: font
		int fontSize = 12,
		str fontStyle = "normal",
		str fontWeight = "normal",
		str textDecoration	= "none",	// text-decoration
		
		// interaction
	
		Event event = on(),
	
		// data sets
	
		Dataset dataset = ()
	) =
	
	emptyFigure()

// atomic primitives
	
   | text(value text)		    			// text label
   
// primitives/containers

   | box(Figure fig=emptyFigure())      	// rectangular box with inner element
   
   | polygon(Vertices vertices)
   
   | polyline(Vertices vertices)
   
   | shape(Vertices vertices, 				// Arbitrary shape
   			bool shapeConnected = true, 	// Connect vertices with line/curve
   			bool shapeClosed = true, 		// Make a closed hape
   			bool shapeCurved = false, 		// Connect vertices with a spline
   			bool fillEvenOdd = true)		// The fill rule to be used.
                   
   | hcat(Figures figs=[]) 					// horizontal and vertical concatenation
   
   | vcat(Figures figs=[]) 					// horizontal and vertical concatenation
                   
   | overlay(Figures figs=[])				// overlay (stacked) composition
   
   | grid(list[Figures] figArray = [[]])	// grid of figures

// transformations

   | move(int x, int y, Figure fig)			// Move to position relative to origin of enclosing Figure
   | moveX(int x, Figure fig)
   | moveY(int y, Figure fig)
   
   | scaleX(num factor, Figure fig)
   | scaleY(num factor, Figure fig)
   | scale(num xfactor, num yfactor, Figure fig)
   | scale(num factor, Figure fig)
   
   | rotate(num angle, Figure fig)

// interaction

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
   
// visibility control

   | visible(bool condition=true, Figure fig = emptyFigure())
   
   | choice(int selection = 0, Figures figs = [])
  
/*
   | _computeFigure(bool() recomp,Figure () computeFig, FProperties props)
 
   | _combo(list[str] choices, Def d, FProperties props)
 
*/

// More advanced figure elements

// charts
   
   | barChart(Axis xAxis=axis(), Axis yAxis=axis(), Dataset[LabeledData] dataset = (), str flavor ="barChart")
   | vegaBarChart(Axis xAxis=axis(), Axis yAxis=axis(), Dataset[LabeledData] dataset = (), str flavor ="barChart")
   | scatterPlot()
   | lineChart(Axis xAxis=axis(), Axis yAxis=axis(), Dataset[XYData] dataset = (), str flavor ="lineChart")
  
// graph

   | graph(map[str, Figure] nodes = (), Figures edges = [], str flavor="layeredGraph")
   | edge(str from, str to, str label)
   
   ;
 



