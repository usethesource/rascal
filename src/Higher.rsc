module demo::vis::Higher

import vis::Figure;
import vis::Render;

public bool intInput(str s){
	return /^[0-9]+$/ := s;
}

public Figure higher(){
	int H = 100;
    return vcat( [ textfield("<H>", void(str s){H = toInt(s);}, intInput),
	               box(width(100), vresizable(false), vsize(num(){return H;}), fillColor("red"))
	             ], shrink(0.5), resizable(false));
}