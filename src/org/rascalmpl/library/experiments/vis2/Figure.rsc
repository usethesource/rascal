module experiments::vis2::Figure

import util::Math;
import List;
import Set;
import IO;
import String;
import ToString;

import  experiments::vis2::Properties;

/*
 * Figure: a visual element, the principal visualization datatype
 * Note: for experimentation purposes this is a small extract from the real thing: vis/Figure.rsc
 */
 
public alias Figures = list[Figure];

alias computedStr 	= str();

public data Figure = 
/* atomic primitives */
	
     _text(str s, FProperties props)		    // text label
   | _text(computedStr sv, FProperties props)
   
/* primitives/containers */

   | _box(FProperties props)			        // rectangular box
   | _box(Figure inner, FProperties props)      // rectangular box with inner element
   
   | _ellipse(FProperties props)                // ellipse with inner element
   | _ellipse(Figure inner, FProperties props)  // ellipse with inner element
                   
   | _hcat(Figures figs, FProperties props) 	// horizontal and vertical concatenation
   | _vcat(Figures figs, FProperties props) 	// horizontal and vertical concatenation
                   
   | _overlay(Figures figs, FProperties props)	// overlay (stacked) composition

// charts
   
   | _barchart(FProperties props)
   | _scatterplot(FProperties props)
   
 // graph
   | _graph(Figures nodes, Edges edges, FProperties props)
   | _texteditor(FProperties props)
   
// interaction

   | _button(str label, void () vcallback, FProperties props)
   | _textfield(void (str) scallback, FProperties props)
   ;
 
data Edge =			 							// edge between between two elements in complex shapes like tree or graph
     _edge(int from, int to, FProperties props)
   ;
   
public alias Edges = list[Edge];
   
public Edge edge(int from, int to, FProperty props ...){
  return _edge(from, to, props);
}

public Figure text(str s, FProperty props ...){
  return _text(s, props);
}

public Figure text(computedStr sv, FProperty props ...){
  return _text(sv, props);
}

public Figure box(FProperty props ...){
  return _box(props);
}

public Figure box(Figure fig, FProperty props ...){
  return _box(fig, props);
}

public Figure hcat(Figures figs, FProperty props ...){
  return _hcat(figs,props);
}

public Figure vcat(Figures figs, FProperty props ...){
  return _vcat(figs,props);
}

public Figure graph(Figures nodes, Edges edges, FProperty props...){
	return _graph(nodes, edges, props);
}

public Figure hvcat(Figures figs, FProperty props ...){
  return _widthDepsHeight(_hvcat(figs, props),[]);
}

public Figure barchart(FProperty props ...){
  return _barchart(props);
}

public Figure scatterplot(FProperty props ...){
  return _scatterplot(props);
}

public Figure texteditor(FProperty props ...){
  return _texteditor(props);
}

public Figure button(str label, void () vcallback, FProperty props ...){
  return _button(label, vcallback, props);
}

public Figure textfield(void (str) scallback, FProperty props ...){
  return _textfield(scallback, props);
}

/*
 * Utitlity functions
*/

@doc{Create a list of font names}
@javaClass{org.rascalmpl.library.experiments.vis2.FigureUtils}
public java list[str] fontNames();

@doc{Compute the size of a text string}
@javaClass{org.rascalmpl.library.experiments.vis2.FigureUtils}
public java tuple[int,int] textSize(str text, str fontName, int fontSize, bool bold = false, bool italic = false);

@doc{Compute the ascent size (part of glyphs above baseline) of a font}
@javaClass{org.rascalmpl.library.experiments.vis2.FigureUtils}
public java int fontAscent(str fontName, int fontSize, bool bold = false, bool italic = false);

@doc{ompute the descent size (part of glyphs below baseline) of a font}
@javaClass{org.rascalmpl.library.experiments.vis2.FigureUtils}
public java int fontDescent(str fontName, int fontSize, bool bold = false, bool italic = false);

