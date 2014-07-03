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
	
data HAlign = left() | hcenter() | right();

data VAlign = top() | vcenter() | bottom();

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

data Event 
	= on()
	| on(str event, Bind[value] binder)
	| on(str event,Figure fig)
	;

public data Figure(
		tuple[int,int] pos = <0,0>,
		int xpos = 0,
		int ypos = 0,
		tuple[int,int] size = <0,0>,
		int width = 0,
		int height = 0,
		tuple[int,int] gap = <0,0>,
		int hgap = 0,
		int vgap = 0,
   
    	// lines
    
		int lineWidth = 1,
		str lineColor = "black",
		list[int] lineStyle = [],
		real lineOpacity = 1.0,
	
		// areas

		str fillColor = "white",
		real fillOpacity = 1.0,
		tuple[int, int] rounded = <0, 0>,
	
		tuple[HAlign, VAlign] align = <hcenter(), vcenter()>,
	
		HAlign halign = hcenter(),
		VAlign valign = vcenter(),

		// fonts and text
	
		str font = "Helvetica",
		int fontSize = 12,

		// interaction
	
		Event event = on(),
	
		// data sets
	
		list[value] dataset = []
	) =
	
	emptyFigure()

// atomic primitives
	
   | text(value text)		    		// text label
   
// primitives/containers

   | box(Figure fig=emptyFigure())      // rectangular box with inner element
                   
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
   
   | barchart()
   | scatterplot()
  
// graph
   | graph(map[str, Figure] nodes = (), Figures edges = [])
   | edge(str from, str to, str label)
   
// | _texteditor()
   ;
 



