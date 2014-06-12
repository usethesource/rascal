module experiments::vis2::Properties

import Node;
import List;
import IO;

data FProperty =
      pos(int xpos, int ypos)
    | size(int xsize, int ysize)
    | gap(int xgap, int ygap)
	| lineWidth(int n)
	| lineColor(str c)
	| lineStyle(list[int] dashes)
	| fillColor(str c)
	| align(HAlign xalign, VAlign yalign)
	| dataset(list[num] values1)
	| dataset(lrel[num,num] values2)
	;
	
data HAlign = left() | hcenter() | right();

data VAlign = top() | vcenter() | bottom();
	
public alias FProperties = list[FProperty];

FProperties getDefaultProperties() = []; //[size(50,50), gap(0,0)];

FProperties combine(FProperty fp, FProperties fps) = [fp, *fps];

FProperties combine(FProperties fps1, FProperties fps2) = fps1 + fps2;

alias Size 	= tuple[int width, int height];
alias Pos  	= tuple[int x, int y];
alias Gap 	= tuple[int width, int height];
alias Align	= tuple[HAlign xalign, VAlign yalign];

data BBox = bbox(int x, int y, int width, int height);

Pos getPos(BBox bb) = <bb.x, bb.y>;

Size getSize(BBox bb) = <bb.width, bb.height>;

Size getSize(FProperties fps, Size def) {
	for(fp <- fps)
		if (size(int xsize, int ysize) := fp) return <xsize, ysize>;
	return def;
}

Size getSize(FProperties fps) = getSize(fps, <0,0>);

Size getGap(FProperties fps, Size def) {
	for(fp <- fps)
		if (gap(int width, int height) := fp) return <width, height>;
	return def;
}

Size getGap(FProperties fps)  = getGap(fps, <0,0>);

Pos getPos(FProperties fps, Pos def) {
	for(fp <- fps)
		if (pos(int xpos, int ypos) := fp) return <xpos, ypos>;
	return def;
}

Size getPos(FProperties fps)  = getPos(fps, <0,0>);

Align getAlign(FProperties fps, Align def) {
	for(fp <- fps)
		if (align(HAlign xalign, VAlign yalign) := fp) return <xalign, yalign>;
	return def;
}

Align getAlign(FProperties fps) = getAlign(fps, <hcenter(), vcenter()>);

// Translate properties to a javascript map

str trProps(FProperties fps) {
	seen = {};
	res = for(fp <- fps){
		attr = getName(fp);
		if(attr notin seen){
			seen += attr;
			t = trProp(fp);
			if(t != "")
				append t;
		}
	}
	return "{ <intercalate(", ", res)> }";
}

str trProp(pos(int xpos, int ypos)) 		= "x: <xpos>, y: <ypos>";
//str trProp(size(int xsize, int ysize))	= "width: <xsize>, height <ysize>";
str trProp(gap(int width, int height)) 		= "";
str trProp(align(HAlign xalign, VAlign yalign)) 	
											= "";
str trProp(lineWidth(int n)) 				= "stroke_width: <n>";
str trProp(lineStyle(list[int] dashes))		= "stroke_dasharray: <dashes>";
str trProp(fillColor(str s)) 				= "fill: \"<s>\"";
str trProp(lineColor(str s))				= "stroke:\"<s>\"";
str trProp(dataset(list[num] values1)) 		= "dataset: <values1>";
str trProp(dataset(lrel[num,num] values2))	= "dataset: [" + intercalate(",", ["[<v1>,<v2>]" | <v1, v2> <- values2]) + "]";

default str trProp(FProperty fp) 			= (size(int xsize, int ysize) := fp) ? "width: <xsize>, height: <ysize>" : "unknown: <fp>";