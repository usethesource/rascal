module experiments::vis2::Figure

import util::Math;
import List;
import Set;
import IO;
import String;
import ToString;

import  experiments::vis2::Properties;

data Visualization =
	   visualization(str name, map[str,str] context, Figure fig)
	|  visualization(str name, Figure fig)
	;

/*
 * Figure: a visual element, the principal visualization datatype
 * Note: for experimentation purposes this is a small extract from the real thing: vis/Figure.rsc
 */
 
public alias Figures = list[Figure];

alias computedStr 	= str();

public data Figure = 
/* atomic primitives */
	
     _text(str s, FProperties props)		    // text label
//   | _text(Use u, FProperties props)
   
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

   | _strInput(Bind[str] sbinder, FProperties props)
   
   | _numInput(Bind[num] nbinder, FProperties props)
   
   | _colorInput(Bind[str] sbinder, FProperties props)
   
    | _fswitch(int sel, Figures figs, FProperties props)
   
    | _rangeInput(int low, int high, int step, Bind[int] ibinder, FProperties props)

// TODO   
/*
   | _mouseOver(Figure under, Figure over,FProperties props)   
       
   | _computeFigure(bool() recomp,Figure () computeFig, FProperties props)
 
   | _combo(list[str] choices, Def d, FProperties props)
   
   | _choice(list[str] choices, Def d, FProperties props)
   
   | _checkbox(str text, bool checked, Def d, FProperties props)
*/
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

//public Figure text(Use u, FProperty props ...){
//  return _text(u, props);
//}

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

public Figure strInput(Bind[str] sbinder, FProperty props ...){
  return _strInput(sbinder, props);
}

public Figure numInput(Bind[num] nbinder, FProperty props ...){
  return _numInput(nbinder, props);
}

public Figure colorInput(Bind[str] sbinder, FProperty props ...){
  return _colorInput(sbinder, props);
}

public Figure fswitch(int sel, Figures figs, FProperty props ...){
 	return _fswitch(sel, figs, props);
}

public Figure rangeInput(int low, int high, int step, Bind[int] binder, FProperty props...){
   return _rangeInput(low, high, step, binder, props);
}
