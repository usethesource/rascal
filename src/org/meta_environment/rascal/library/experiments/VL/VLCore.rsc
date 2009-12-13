module experiments::VL::VLCore
import Integer;
import List;
import Set;
import IO;

alias Color = int;

@doc{Gray color (0-255)}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public void java gray(int gray);

@doc{Gray color (0-255) with transparency}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public Color java gray(int gray, real alpha);

@doc{Gray color as percentage (0.0-1.0)}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public void java gray(real perc);

@doc{Gray color with transparency}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public Color java gray(real perc, real alpha);

@doc{Named color}
@reflect{Needs calling context when generating an exception}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public Color java color(str colorName);

@doc{Named color with transparency}
@reflect{Needs calling context when generating an exception}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public Color java color(str colorName, real alpha);

@doc{RGB color}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public Color java rgb(int r, int g, int b);

@doc{RGB color with transparency}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public Color java rgb(int r, int g, int b, real alpha);

@doc{Interpolate two colors (in RGB space)}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public list[Color] java interpolateColor(Color from, Color to, real percentage);

@doc{Create a list of interpolated colors}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public list[Color] java colorSteps(Color from, Color to, int steps);

@doc{Create a colorscale}
public Color(int) colorScale(list[int] values, Color from, Color to){
   mn = min(values);
   range = max(values) - mn;
   sc = colorSteps(from, to, 10);
   return Color(int v) { return sc[(9 * (v - mn)) / range]; };
}

data VPROP =
/* sizes */
     width(int width)                   // sets width of element
   | height(int height)                 // sets height of element
   | size(int size)                     // sets width and height to same value
   | size(int hor, int vert)            // sets width and height to separate values
   | gap(int amount)                    // sets gap between elements in composition to same value
   | gap(int hor, int vert) 			// sets gap between elements in composition to separate values
   
/* direction and alignment */
   | horizontal()                       // horizontal composition
   | vertical()                         // vertical composition
   | top()                              // top alignment
   | center()                           // center alignment
   | bottom()                           // bottom alignment
   | left()                             // left alignment
   | right()                            // right alignment
   
/* transformations */
//   | move(int byX)        			// translate in X direction
//   | vmove(int byY)                     // translate in Y direction
//     | rotate(int angle)
//  | scale(real perc)

 
 /* line and border attributes */
   | lineWidth(int lineWidth)			// line width
   | lineColor(Color lineColor)		    // line color
   | lineColor(str colorName)           // named line color
   
   | fillColor(Color fillColor)			// fill color
   | fillColor(str colorName)           // named fill color
   
/* wedge/pie attributes */
   | fromAngle(int angle)
   | toAngle(int angle)
   | innerRadius(int radius)
   
 /* text attributes */
   | font(str fontName)                 // named font
   | fontSize(int size)                 // font size
   | textAngle(int angle)               // rotation
   
/* interaction */
   | mouseOver(list[VPROP] props)       // switch to new properties when mouse is over element
   | mouseOver(list[VPROP] props, VELEM inner)
                                        // display new inner element when mouse is over current element
   
/* other */
   | id(str name)                       // name of elem (used in edges and various layouts)
   | closed()    						// closed shapes
   | curved()                           // use curves instead of straight lines
   ;
   
data Vertex = 
     vertex(int x, int y)                // vertex in a shape
   | vertex(int x, int y, VELEM marker)  // vertex with marker
   ;
   
data Edge =
     edge(str from, str to) 			 // edge between between two elements
   | edge(list[VPROP], str from, str to) // edge between between two elements
   ;
   
data VELEM = 
/* drawing primitives */
     box(list[VPROP] props)			          	// rectangular box
   | box(list[VPROP] props, VELEM inner)
   | ellipse(list[VPROP] props)			      	// ellipse
   | ellipse(list[VPROP] props, VELEM inner)
   | text(list[VPROP] props, str s)		  		// text label
   | text(str s)			              		// text label
 
 
/* lines and curves */
   | shape(list[Vertex] points)
   | shape(list[VPROP] props,list[Vertex] points)
   
/* composition */
   | combine(list[VELEM] elems)
   | combine(list[VPROP] props, list[VELEM] elems)
   
   | overlay(list[VELEM] elems) 
   | overlay(list[VPROP] props, list[VELEM] elems)
   
   | grid(list[VELEM] elems) 
   | grid(list[VPROP] props, list[VELEM] elems)
   
   | pack(list[VELEM] elems) 
   | pack(list[VPROP] props, list[VELEM] elems)
   
   | pie(list[VELEM] elems) 
   | pie(list[VPROP] props, list[VELEM] elems)
   
   | graph(list[VELEM] nodes, list[Edge] edges)
   | graph(list[VPROP], list[VELEM] nodes, list[Edge] edges)
   
   | tree(list[VELEM] nodes, list[Edge] edges)
   | tree(list[VPROP], list[VELEM] nodes, list[Edge] edges)
   ;
   
/*
 * Wishlist:
 * - arrows
 * - textures
 * - boxes with round corners
 * - dashed/dotted lines
 */

