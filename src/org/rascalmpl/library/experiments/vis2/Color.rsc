module experiments::vis2::Color

/*
  * Colors and color management
  */

alias Color = int;

@doc{Gray color (0-255)}
@javaClass{org.rascalmpl.library.vis.util.FigureColorUtils}
public java Color gray(int gray);

@doc{Gray color (0-255) with transparency}
@javaClass{org.rascalmpl.library.vis.util.FigureColorUtils}
public java Color gray(int gray, real alpha);

@doc{Gray color as percentage (0.0-1.0)}
@javaClass{org.rascalmpl.library.vis.util.FigureColorUtils}
public java Color gray(real perc);

@doc{Gray color with transparency}
@javaClass{org.rascalmpl.library.vis.util.FigureColorUtils}
public java Color gray(real perc, real alpha);

@doc{Named color}
@reflect{Needs calling context when generating an exception}
@javaClass{org.rascalmpl.library.vis.util.FigureColorUtils}
public java Color color(str colorName);

@doc{Named color with transparency}
@reflect{Needs calling context when generating an exception}
@javaClass{org.rascalmpl.library.vis.util.FigureColorUtils}
public java Color color(str colorName, real alpha);

@doc{Sorted list of all color names}
@javaClass{org.rascalmpl.library.vis.util.FigureColorUtils}
public java list[str] colorNames();

@doc{RGB color}
@javaClass{org.rascalmpl.library.vis.util.FigureColorUtils}
public java Color rgb(int r, int g, int b);

@doc{RGB color with transparency}
@javaClass{org.rascalmpl.library.vis.util.FigureColorUtils}
public java Color rgb(int r, int g, int b, real alpha);

@doc{Interpolate two colors (in RGB space)}
@javaClass{org.rascalmpl.library.vis.util.FigureColorUtils}
public java Color interpolateColor(Color from, Color to, real percentage);

@doc{Create a list of interpolated colors}
@javaClass{org.rascalmpl.library.vis.util.FigureColorUtils}
public java list[Color] colorSteps(Color from, Color to, int steps);

@doc{Create a colorscale from a list of numbers}
public Color(&T <: num) colorScale(list[&T <: num] values, Color from, Color to){
   mn = min(values);
   range = max(values) - mn;
   sc = colorSteps(from, to, 10);
   return Color(int v) { return sc[(9 * (v - mn)) / range]; };
}

@doc{Create a fixed color palette}
public list[str] p12 = [ "yellow", "aqua", "navy", "violet", 
                          "red", "darkviolet", "maroon", "green",
                          "teal", "blue", "olive", "lime"];
                          
public list[Color] p12Colors = [color(s) | s <- p12];

@doc{Return named color from fixed palette}
public str palette(int n){
  try 
  	return p12[n];
  catch:
    return "black";
}

public Color arbColor(){
	return rgb(toInt(arbReal() * 255.0),toInt(arbReal() * 255.0),toInt(arbReal() * 255.0));
}
