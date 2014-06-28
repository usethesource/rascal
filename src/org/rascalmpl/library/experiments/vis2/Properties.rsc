module experiments::vis2::Properties

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

