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

     Ftext(FProperties props, str s)		  		// text label
   | Ftext(str s)			              		// text label
   
   												// file outline
   | Foutline(FProperties props, map[int,Color] coloredLines)
   | Foutline(map[int,Color] coloredLines)
   
   
/* primitives/containers */

   | Fbox(FProperties props)			          	// rectangular box
   | Fbox(FProperties props, Figure inner)       // rectangular box with inner element
   
   | Fellipse(FProperties props)			      	// ellipse
   | Fellipse(FProperties props, Figure inner)   // ellipse with inner element
   
   | Fwedge(FProperties props)			      	// wedge
   | Fwedge(FProperties props, Figure inner)     // wedge with inner element
   
   | Fspace(FProperties props)			      	// invisible box (used for spacing)
   | Fspace(FProperties props, Figure inner)     // invisible box with visible inner element
 
/* composition */
   
   | Fuse(Figure elem)                           // use another elem
   | Fuse(FProperties props, Figure elem)
 
   | Fhcat(Figures figs)                        // horizontal concatenation
   | Fhcat(FProperties props, Figures figs)
   
   | Fvcat(Figures figs)                        // vertical concatenation
   | Fvcat(FProperties props, Figures figs)
   
   | Fhvcat(Figures figs)                       // horizontal and vertical concatenation
   | Fhvcat(FProperties props, Figures figs)
   
   | Foverlay(Figures figs)                     // overlay (stacked) composition
   | Foverlay(FProperties props, Figures figs)
   
   | Fshape(list[Vertex] points)                 // shape of to be connected vertices
   | Fshape(FProperties props,list[Vertex] points)
   
   | Fgrid(Figures figs)                        // placement on fixed grid
   | Fgrid(FProperties props, Figures figs)
   
   | Fpack(Figures figs)                        // composition by 2D packing
   | Fpack(FProperties props, Figures figs)
   
   | Fgraph(Figures nodes, list[Edge] edges)     // composition of nodes and edges as graph
   | Fgraph(FProperties, Figures nodes, list[Edge] edges)
   
                							    // composition of nodes and edges as tree
   | Ftree(Figures nodes, list[Edge] edges) 
   | Ftree(FProperties, Figures nodes, list[Edge] edges)
   
   | Ftreemap(Figures nodes, list[Edge] edges) 
   | Ftreemap(FProperties, Figures nodes, list[Edge] edges)
   
/* transformation */

   | rotate(num angle, Figure fig)			    // Rotate element around its anchor point
   | scale(num perc, Figure fig)	   		    // Scale element (same for h and v)
   | scale(num xperc, num yperc, Figure fig)	// Scale element (different for h and v)
   ;
   
public Figure text(str s, FProperty props ...){
  return Ftext(props, s);
}

public Figure outline (map[int,Color] coloredLines, FProperty props ...){
  return Foutline(props, coloredLines);
}

public Figure box(FProperty props ...){
  return Fbox(props);
}

public Figure box(Figure fig, FProperty props ...){
  return Fbox(props, fig);
}

public Figure ellipse(FProperty props ...){
  return Fellipse(props);
}

public Figure ellipse(Figure fig, FProperty props ...){
  return Fellipse(props, fig);
}

public Figure wedge(FProperty props ...){
  return Fwedge(props);
}

public Figure wedge(Figure fig, FProperty props ...){
  return Fwedge(props, fig);
}  

public Figure space(FProperty props ...){
  return Fspace(props);
}

public Figure space(Figure fig, FProperty props ...){
  return Fspace(props, fig);
}

public Figure use(Figure fig, FProperty props ...){
  return Fuse(props, fig);
}

public Figure hcat(Figures figs, FProperty props ...){
  return Fhcat(props, figs);
}

public Figure vcat(Figures figs, FProperty props ...){
  return Fvcat(props, figs);
}

public Figure hvcat(Figures figs, FProperty props ...){
  return Fhvcat(props, figs);
}

public Figure overlay(Figures figs, FProperty props ...){
  return Foverlay(props, figs);
}

public Figure shape(list[Vertex] points, FProperty props ...){
  return Fshape(props, points);
}

public Figure grid(Figures figs, FProperty props ...){
  return Fgrid(props, figs);
}

public Figure pack(Figures figs, FProperty props ...){
  return Fpack(props, figs);
}

public Figure graph(Figures nodes, list[Edge] edges, FProperty props...){
  return Fgraph(props, nodes, edges);
}

public Figure tree(Figures nodes, list[Edge] edges, FProperty props...){
  return Ftree(props, nodes, edges);
}

public Figure treemap(Figures nodes, list[Edge] edges, FProperty props...){
  return Ftreemap(props, nodes, edges);
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

