module experiments::vis2::Properties

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
	
	| onClick(Bind[value] binder)
	
	// data sets	
	
	| dataset(list[num] values1)
	| dataset(lrel[num,num] values2)
	;
	
data HAlign = left() | hcenter() | right();

data VAlign = top() | vcenter() | bottom();
	
public alias FProperties = list[FProperty];

