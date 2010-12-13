module vis::Figure

import Integer;
import Real;
import List;
import Set;
import IO;

/*
 * Declarations and library functions for Rascal Visualization
 */
 
 /*
  * Colors and color management
  */

alias Color = int;

@doc{Gray color (0-255)}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public Color java gray(int gray);

@doc{Gray color (0-255) with transparency}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public Color java gray(int gray, real alpha);

@doc{Gray color as percentage (0.0-1.0)}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public Color java gray(real perc);

@doc{Gray color with transparency}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public Color java gray(real perc, real alpha);

@doc{Named color}
@reflect{Needs calling context when generating an exception}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public Color java color(str colorName);

@doc{Named color with transparency}
@reflect{Needs calling context when generating an exception}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public Color java color(str colorName, real alpha);

@doc{Sorted list of all color names}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public list[str] java colorNames();

@doc{RGB color}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public Color java rgb(int r, int g, int b);

@doc{RGB color with transparency}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public Color java rgb(int r, int g, int b, real alpha);

@doc{Interpolate two colors (in RGB space)}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public int java interpolateColor(Color from, Color to, real percentage);

@doc{Create a list of interpolated colors}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public list[Color] java colorSteps(Color from, Color to, int steps);

@doc{Create a colorscale from a list of numbers}
public Color(&T <: num) colorScale(list[&T <: num] values, Color from, Color to){
   mn = min(values);
   range = max(values) - mn;
   sc = colorSteps(from, to, 10);
   return Color(int v) { return sc[(9 * (v - mn)) / range]; };
}

@doc{Create a fixed color palette}
private list[str] p12 = [ "navy", "violet", "yellow", "aqua", 
                          "red", "darkviolet", "maroon", "green",
                          "teal", "blue", "olive", "lime"];

@doc{Return named color from fixed palette}
public str palette(int n){
  try 
  	return p12[n];
  catch:
    return "black";
}

@doc{Create a list of font names}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public list[str] java fontNames();

/*
 * FProperty -- visual properties of visual elements
 */
 
 public FProperty left(){
   return hanchor(0.0);
 }
 
 public FProperty hcenter(){
   return hanchor(0.5);
 }
 
 public FProperty right(){
   return hanchor(1.0);
 }
 
 public FProperty top(){
   return vanchor(0.0);
 }
 
 public FProperty vcenter(){
   return vanchor(0.5);
 }
 
 public FProperty bottom(){
   return vanchor(1.0);
 }
 
 public FProperty center(){
   return anchor(0.5, 0.5);
 }
 
public alias FProperties = list[FProperty];

data FProperty =
/* sizes */
     width(num width)                   // sets width of element
   | height(num height)                 // sets height of element
   | size(num size)					    // sets width and height to same value
   | size(num hor, num vert)            // sets width and height to separate values
   | gap(num amount)                    // sets hor and vert gap between elements in composition to same value
   | gap(num hor, num vert) 			// sets hor and vert gap between elements in composition to separate values
   | hgap(num hor)                      // sets hor gap
   | vgap(num vert)                     // set vert gap
   
/* alignment */
   | anchor(num h, num v)				// horizontal (0=left; 1=right) & vertical anchor (0=top,1=bottom)
   | hanchor(num h)
   | vanchor(num v)
   
/* line and border properties */
   | lineWidth(num lineWidth)			// line width
   | lineColor(Color lineColor)		    // line color
   | lineColor(str colorName)           // named line color
   
   | fillColor(Color fillColor)			// fill color of shapes and text
   | fillColor(str colorName)           // named fill color
   
/* wedge properties */
   | fromAngle(num angle)
   | toAngle(num angle)
   | innerRadius(num radius)

/* shape properties */
   | shapeConnected()                   // shapes consist of connected points
   | shapeClosed()    		 		    // closed shapes
   | shapeCurved()                      // use curves instead of straight lines
 
/* font and text properties */
   | font(str fontName)             	// named font
   | fontSize(int isize)                // font size
   | fontColor(Color textColor)         // font color
   | fontColor(str colorName)
   | textAngle(num angle)               // text rotation
   
/* interaction properties */  
   | mouseOver(Figure inner)            // add figure when mouse is over current figure
   | contentsHidden()                   // contents of container is hidden
   | contentsVisible()                  // contents of container is visible
   | pinned()                           // position pinned-down, cannot be dragged
   | doi(int d)                        // limit visibility to nesting level d
   
/* other properties */
   | id(str name)                       // name of elem (used in edges and various layouts)
   | hint(str name)                     // hint for various compositions
   ;

/*
 * Vertex and Edge: auxiliary data types
 */

data Vertex = 
     _vertex(num x, num y, FProperties props)             	    // vertex in a shape          
   | _vertex(num x, num y, Figure marker, FProperties props)    // vertex with marker
   ;
   
public Vertex vertex(num x, num y, FProperty props ...){
   return _vertex(x, y, props);
}

public Vertex vertex(num x, num y, Figure marker, FProperty props ...){
   return _vertex(x, y, marker, props);
}
   
data Edge =			 							// edge between between two elements in complex shapes like tree or graph
     _edge(str from, str to, FProperties prop)
   | _edge(str from, str to, Figure toArrow, FProperties prop)
   | _edge(str from, str to, Figure toArrow, Figure fromArrow, FProperties prop)
   ;
   
public alias Edges = list[Edge];
   
public Edge edge(str from, str to, FProperty props ...){
  return _edge(from, to, props);
}

public Edge edge(str from, str to, Figure toArrow, FProperty props ...){
  return _edge(from, to, toArrow, props);
}

public Edge edge(str from, str to, Figure toArrow, Figure fromArrow, FProperty props ...){
  return _edge(from, to, toArrow, fromArrow, props);
}

/*
 * Figure: a visual element, the principal visualization datatype
 */
 
public alias Figures = list[Figure];
 
data Figure = 
/* atomic primitives */

     _text(str s, FProperties props)		    // text label
   
   												// file outline
   | _outline(map[int,Color] coloredLines, FProperties props)
   
   
/* primitives/containers */

   | _box(FProperties props)			          	// rectangular box
   | _box(Figure inner, FProperties props)       // rectangular box with inner element
   
   | _ellipse(FProperties props)                 // ellipse with inner element
   | _ellipse(Figure inner, FProperties props)   // ellipse with inner element
   
   | _wedge(FProperties props)			      	// wedge
   | _wedge(Figure inner, FProperties props)     // wedge with inner element
   
   | _space(FProperties props)			      	// invisible box (used for spacing)
   | _space(Figure inner, FProperties props)     // invisible box with visible inner element
 
/* composition */
   
   | _use(Figure elem)                           // use another elem
   | _use(Figure elem, FProperties props)
                       
   | _hcat(Figures figs, FProperties props)     // horizontal concatenation
                     
   | _vcat(Figures figs, FProperties props)     // vertical concatenation
                   
   | _hvcat(Figures figs, FProperties props) // horizontal and vertical concatenation
                   
   | _overlay(Figures figs, FProperties props)// overlay (stacked) composition
   
                								// shape of to be connected vertices
   | _shape(list[Vertex] points, FProperties props)
                         
   | _grid(Figures figs, FProperties props)// placement on fixed grid
   
  								                // composition by 2D packing
   | _pack(Figures figs, FProperties props)
   
  												 // composition of nodes and edges as graph
   | _graph(Figures nodes, Edges edges, FProperties props)
   
                							    // composition of nodes and edges as tree
   | _tree(Figures nodes, Edges edges, FProperties props)
   
   | _treemap(Figures nodes, Edges edges, FProperties props)
   
/* transformation */

   | _rotate(num angle, Figure fig, FProperties props)			    // Rotate element around its anchor point
   | _scale(num perc, Figure fig, FProperties props)	   		    // Scale element (same for h and v)
   | _scale(num xperc, num yperc, Figure fig, FProperties props)	// Scale element (different for h and v)
   ;


public Figure text(str s, FProperty props ...){
  return _text(s, props);
}

public Figure outline (map[int,Color] coloredLines, FProperty props ...){
  return _outline(coloredLines, props);
}

public Figure box(FProperty props ...){
  return _box(props);
}

public Figure box(Figure fig, FProperty props ...){
  return _box(fig, props);
}

public Figure ellipse(FProperty props ...){
  return _ellipse(props);
}

public Figure ellipse(Figure fig, FProperty props ...){
  return _ellipse(fig, props);
}

public Figure wedge(FProperty props ...){
  return _wedge(props);
}

public Figure wedge(Figure fig, FProperty props ...){
  return _wedge(fig, props);
}  

public Figure space(FProperty props ...){
  return _space(props);
}

public Figure space(Figure fig, FProperty props ...){
  return _space(fig, props);
}

public Figure use(Figure fig, FProperty props ...){
  return _use(fig, props);
}

public Figure hcat(Figures figs, FProperty props ...){
  return _hcat(figs, props);
}

public Figure vcat(Figures figs, FProperty props ...){
  return _vcat(figs, props);
}

public Figure hvcat(Figures figs, FProperty props ...){
  return _hvcat(figs, props);
}

public Figure overlay(Figures figs, FProperty props ...){
  return _overlay(figs, props);
}

public Figure shape(list[Vertex] points, FProperty props ...){
  return _shape(points, props);
}

public Figure grid(Figures figs, FProperty props ...){
  return _grid(figs, props);
}

public Figure pack(Figures figs, FProperty props ...){
  return _pack(figs, props);
}

public Figure graph(Figures nodes, Edges edges, FProperty props...){
  return _graph(nodes, edges, props);
}

public Figure tree(Figures nodes, Edges edges, FProperty props...){
  return _tree(nodes, edges, props);
}

public Figure treemap(Figures nodes, Edges edges, FProperty props...){
  return _treemap(nodes, edges, props);
}

public Figure rotate(num angle, Figure fig, FProperty props...){
  return _rotate(angle, fig, props);
}

public Figure scale(num perc, Figure fig, FProperty props...){
  return _scale(perc, fig, props);
}

public Figure scale(num xperc, num yperc, Figure fig, FProperty props...){
  return _scale(xperc, yperc, fig, props);
}
   
/*
 * Wishlist:
 * - arrows
 * - textures
 * - boxes with round corners
 * - dashed/dotted lines
 * - ngons
 * - bitmap import and display
 * - svg/png/pdf export
 * - new layouts (circuar) treemap, icecle
 */

