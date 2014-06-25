module experiments::vis2::Properties

alias CallBack[&T] = &T(&T);

data Use = use(str name);

data Def
    = def(type[&T], str name)
    | def(type[&T], str name, CallBack[&T] callback)
//    | defFig(str name, Figure(str) callback)
	;
	
data FProperty =
      pos(int xpos, int ypos)
      
    | xpos(int p)
    | xpos(Use u)
    
    | ypos(int p)
    | ypos(Use u)
    
    | size(int xsize, int ysize)
    
    | width(int w)
    | width(Use u)
    
    | height(int h)
    | height(Use u)
    
    | gap(int xgap, int ygap)
    
    | hgap(int g)
    | hgap(Use u)
    
    | vgap(int g)
    | vgap(Use u)
    
    // lines
    
	| lineWidth(int n)
	| lineWidth(Use u)
	
	| lineColor(str c)
	| lineColor(Use u)
	
	| lineStyle(list[int] dashes)
	
	| lineOpacity(real op)
	| lineOpacity(Use u)

	| fillColor(str c)
	| fillColor(Use u)
	
	| fillOpacity(real op)
	| fillOpacity(Use u)
	
	| rounded(int rx, int ry)
	
	| align(HAlign xalign, VAlign yalign)
	
	| halign(HAlign ha)
	| halign(Use u)
	
	| valign(VAlign va)
	| valign(Use u)
	
	// fonts and text
	
	| font(str fontName)
	| font(Use u)
	
	| fontSize(int fontSize)
	| fontSize(Use u)
	
	// interaction
	
	| onClick(Def d)
	
	// data sets	
	
	| dataset(list[num] values1)
	| dataset(lrel[num,num] values2)
	;
	
data HAlign = left() | hcenter() | right();

data VAlign = top() | vcenter() | bottom();
	
public alias FProperties = list[FProperty];

