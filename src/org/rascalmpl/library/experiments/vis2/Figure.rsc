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
	
data HAlign = left() | hcenter() | right();

data VAlign = top() | vcenter() | bottom();

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

alias Points = lrel[num,num];

/*
shape
	
	ngo,
	polygon
	
	gradient(numr)
	texture(loc image)
	lineCap flat, rounded, padded
	lineJoin	smooth, sharp(r), clipped
	dashing
	dashOffset
	
	trafo:
		moveto/moveby
		scale
		rotate
	linestyle: color, width, cap, join, dashing, dashOffset
*/

data Vertex
	= vertex(num x, num y)
	| vertexBy(num x, num y)
	;
	
alias Vertices = list[Vertex];

public data Figure(
		//tuple[int,int] pos = <0,0>,
		//int xpos = 0,
		//int ypos = 0,
		tuple[int,int] size = <0,0>,
		int width = 0,
		int height = 0,
		tuple[int,int] gap = <0,0>,
		int hgap = 0,
		int vgap = 0,
   
    	// lines
    
		int strokeWidth = 1,			// was: lineWidth
		str stroke = "black", 			// was: lineColor
		list[int] strokeDashArray = [],	// was: lineStyle
		real strokeOpacity = 1.0,		// was: lineOpacity
	
		// areas

		str fill = "white", 			// was: fillColor
		real fillOpacity = 1.0,			// was: fill-opacity
		str fillRule = "nonzero",		// or "evenodd"
		tuple[int, int] rounded = <0, 0>,
	
		tuple[HAlign, VAlign] align = <hcenter(), vcenter()>,
	
		HAlign halign = hcenter(),
		VAlign valign = vcenter(),

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
	
   | text(value text)		    		// text label
   
// primitives/containers

   | box(Figure fig=emptyFigure())      // rectangular box with inner element
   
   | polygon(Vertices vertices)
   
   | polyline(Vertices vertices)
   
   | shape(Vertices vertices, bool shapeConnected = true, bool shapeClosed = true, bool shapeCurved = false, bool fillEvenOdd = true)
                   
   | hcat(Figures figs=[]) 				// horizontal and vertical concatenation
   
   | vcat(Figures figs=[]) 				// horizontal and vertical concatenation
                   
//   | _overlay(Figures figs, FProperties props)	// overlay (stacked) composition

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
   | scatterPlot()
   | lineChart(Axis xAxis=axis(), Axis yAxis=axis(), Dataset[XYData] dataset = (), str flavor ="lineChart")
  
// graph

   | graph(map[str, Figure] nodes = (), Figures edges = [], str flavor="layeredGraph")
   | edge(str from, str to, str label)
   
   ;
 



