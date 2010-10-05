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

@doc{RGB color}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public Color java rgb(int r, int g, int b);

@doc{RGB color with transparency}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public Color java rgb(int r, int g, int b, real alpha);

@doc{Interpolate two colors (in RGB space)}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public list[Color] java interpolateColor(Color from, Color to, real percentage);

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

   
/* alignment */
   | anchor(num h, num v)				// horizontal (0=left; 1=right) & vertical anchor (0=top,1=bottom)
   | hanchor(num h)
   | vanchor(num v)
   
/* line and border properties */
   | lineWidth(int lineWidth)			// line width
   | lineColor(Color lineColor)		    // line color
   | lineColor(str colorName)           // named line color
   
   | fillColor(Color fillColor)			// fill color of shapes and text
   | fillColor(str colorName)           // named fill color
   
/* wedge properties */
   | fromAngle(num angle)
   | toAngle(num angle)
   | innerRadius(num radius)
   
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
   
/* other properties */
   | id(str name)                       // name of elem (used in edges and various layouts)
   | connectedShape()                   // shapes consist of connected points
   | closedShape()    		 		    // closed shapes
   | curvedShape()                      // use curves instead of straight lines
   ;

/*
 * Vertex and Edge: auxiliary data types
 */

data Vertex = 
     vertex(num x, num y)             	// vertex in a shape          
   | vertex(num x, num y, Figure marker)  // vertex with marker
   ;
   
data Edge =
     edge(str from, str to) 			 	// edge between between two elements in complex shapes like tree or graph
   | edge(FProperties, str from, str to) 	// 
   ;

/*
 * Figure: a visual element, the principal visualization datatype
 */
 
public alias Figures = list[Figure];
 
data Figure = 
/* atomic primitives */

     text(FProperties props, str s)		  		// text label
   | text(str s)			              		// text label
   
   												// file outline
   | outline(FProperties props, map[int,Color] coloredLines)
   | outline(map[int,Color] coloredLines)
   
   
/* primitives/containers */

   | box(FProperties props)			          	// rectangular box
   | box(FProperties props, Figure inner)       // rectangular box with inner element
   
   | ellipse(FProperties props)			      	// ellipse
   | ellipse(FProperties props, Figure inner)   // ellipse with inner element
   
   | wedge(FProperties props)			      	// wedge
   | wedge(FProperties props, Figure inner)     // wedge with inner element
   
   | space(FProperties props)			      	// invisible box (used for spacing)
   | space(FProperties props, Figure inner)     // invisible box with visible inner element
 
/* composition */
   
   | use(Figure elem)                           // use another elem
   | use(FProperties props, Figure elem)
 
   | hcat(Figures elems)                        // horizontal concatenation
   | hcat(FProperties props, Figures elems)
   
   | vcat(Figures elems)                        // vertical concatenation
   | vcat(FProperties props, Figures elems)
   
   | hvcat(Figures elems)                       // horizontal and vertical concatenation
   | hvcat(FProperties props, Figures elems)
   
   | overlay(Figures elems)                     // overlay (stacked) composition
   | overlay(FProperties props, Figures elems)
   
   | shape(list[Vertex] points)                 // shape of to be connected vertices
   | shape(FProperties props,list[Vertex] points)
   
   | grid(Figures elems)                        // placement on fixed grid
   | grid(FProperties props, Figures elems)
   
   | pack(Figures elems)                        // composition by 2D packing
   | pack(FProperties props, Figures elems)
   
   | graph(Figures nodes, list[Edge] edges)     // composition of nodes and edges as graph
   | graph(FProperties, Figures nodes, list[Edge] edges)
   
                							    // composition of nodes and edges as tree
   | tree(Figures nodes, list[Edge] edges, str root) 
   | tree(FProperties, Figures nodes, list[Edge] edges, str root)
   
   | treemap(Figures nodes, list[Edge] edges, str root) 
   | treemap(FProperties, Figures nodes, list[Edge] edges, str root)
   
/* transformation */

   | rotate(num angle, Figure elem)			    // Rotate element around its anchor point
   | scale(num perc, Figure)					// Scale element (same for h and v)
   | scale(num xperc, num yperc, Figure elem)	// Scale element (different for h and v)
   ;
   
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

