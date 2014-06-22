module experiments::vis2::Properties

data FProperty =
      pos(int xpos, int ypos)
    | size(int xsize, int ysize)
    | gap(int xgap, int ygap)
    
    // lines
    
	| lineWidth(int n)
	| lineColor(str c)
	| lineStyle(list[int] dashes)
	| lineOpacity(real op)
	
	// areas
	| fillColor(str c)
	| fillColor(str() sc)
	| fillOpacity(real op)
	| rounded(int rx, int ry)
	| align(HAlign xalign, VAlign yalign)
	
	// fonts and text
	
	| font(str fontName)
	| fontSize(int fontSize)
	| fontBaseline(str s)
	| textAngle(num  r)
	
	// interaction
	
	| onClick(void () callback)
	
	// data sets	
	
	| dataset(list[num] values1)
	| dataset(lrel[num,num] values2)
	;
	
data HAlign = left() | hcenter() | right();

data VAlign = top() | vcenter() | bottom();
	
public alias FProperties = list[FProperty];

