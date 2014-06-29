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
	;
	
data FProperty =
      pos(int xpos, int ypos)
      
    | xpos(int p)
    | ypos(int p)

    | size(int xsize, int ysize)
    | width(int w)
    | height(int h)
    
    | gap(int xgap, int ygap)
    | hgap(int g)
    | vgap(int g)
    
    // lines
    
	| lineWidth(int n)
	| lineColor(str c)
	| lineStyle(list[int] dashes)
	| lineOpacity(real op)
	
	// areas

	| fillColor(str c)
	| fillOpacity(real op)
	| rounded(int rx, int ry)
	
	| align(HAlign xalign, VAlign yalign)
	
	| halign(HAlign ha)
	| valign(VAlign va)

	// fonts and text
	
	| font(str fontName)
	| fontSize(int fontSize)

	// interaction
	
	| on(str event, Bind[value] binder)
	| on(str event, Figure fig)
	
	// data sets	
	
	| dataset(list[num] values1)
	| dataset(lrel[num,num] values2)
	;
	
data HAlign = left() | hcenter() | right();

data VAlign = top() | vcenter() | bottom();
	
public alias FProperties = list[FProperty];

public set[str] legalEvents = {

// Form events
	"blur",				// Fires the moment that the element loses focus
	"change",			// Fires the moment when the value of the element is changed
	"contextmenu",		// Script to be run when a context menu is triggered
	"focus",			// Fires the moment when the element gets focus
	"formchange",		// Script to be run when a form changes
	"forminput",		// Script to be run when a form gets user input
	"input",			// Script to be run when an element gets user input
	"invalid",			// Script to be run when an element is invalid
	"select",			// Fires after some text has been selected in an element
	"submit",			// Fires when a form is submitted
	
// Keyboard events
	"keydown",			// Fires when a user is pressing a key
	"keypress",			// Fires when a user presses a key
	"keyup",			// Fires when a user releases a key
	
// Mouse events
	"click",			// Fires on a mouse click on the element
	"dbclick",			// Fires on a mouse double-click on the element
	"drag",				// Script to be run when an element is dragged
	"dragend",			// Script to be run at the end of a drag operation
	"dragenter",		// Script to be run when an element has been dragged to a valid drop target
	"dragleave",		// Script to be run when an element leaves a valid drop target
	"dragover",			// Script to be run when an element is being dragged over a valid drop target
	"dragstart",		// Script to be run at the start of a drag operation
	"drop",				// Script to be run when dragged element is being dropped
	"mousedown",		// Fires when a mouse button is pressed down on an element
	"mousemove",		// Fires when the mouse pointer moves over an element
	"mouseeout",		// Fires when the mouse pointer moves out of an element
	"mouseover",		// Fires when the mouse pointer moves over an element
	"mouseup",			// Fires when a mouse button is released over an element
	"mousewheel",		// Script to be run when the mouse wheel is being rotated
	"scroll"			// Script to be run when an element's scrollbar is being scrolled
	};

/*
 * Figure: a visual element, the principal visualization datatype
 * Note: for experimentation purposes this is a small extract from the real thing: vis/Figure.rsc
 */
 
public alias Figures = list[Figure];

public data Figure = 
/* atomic primitives */
	
     _text(value v, FProperties props)		    // text label
   
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

   | _buttonInput(str trueText, str falseText, FProperties props)
   
   | _checkboxInput(FProperties props)

   | _strInput(FProperties props)
   
   | _numInput(FProperties props)
   
   | _colorInput(FProperties props)
   
   | _rangeInput(int low, int high, int step, FProperties props)
   
   | _choiceInput(list[str] choices, FProperties props)
   

// visibility control

   | _visible(bool yes, Figure fig, FProperties props)
   
   | _choice(int sel, Figures figs, FProperties props)

// TODO   
/*
       
   | _computeFigure(bool() recomp,Figure () computeFig, FProperties props)
 
   | _combo(list[str] choices, Def d, FProperties props)
 
*/
   ;
 
data Edge =			 							// edge between between two elements in complex shapes like tree or graph
     _edge(int from, int to, FProperties props)
   ;
   
public alias Edges = list[Edge];
   
public Edge edge(int from, int to, FProperty props ...){
  return _edge(from, to, props);
}

public Figure text(value v, FProperty props ...){
  return _text(v, props);
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

public Figure strInput(FProperty props ...){
  return _strInput(props);
}

public Figure numInput(FProperty props ...){
  return _numInput(props);
}

public Figure colorInput(FProperty props ...){
  return _colorInput(props);
}

public Figure buttonInput(str trueText, str falseText, FProperty props ...){
  return _buttonInput(trueText, falseText, props);
}

public Figure checkboxInput(FProperty props ...){
  return _checkboxInput(props);
}

public Figure choice(int sel, Figures figs, FProperty props ...){
 	return _choice(sel, figs, props);
}

public Figure visible(bool vis, Figure fig, FProperty props ...){
 	return _visible(vis, fig, props);
}

public Figure rangeInput(int low, int high, int step, FProperty props...){
   return _rangeInput(low, high, step, props);
}

public Figure choiceInput(list[str] choices, FProperty props...){
   return _choiceInput(choices, props);
}
