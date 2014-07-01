module experiments::vis2::Tst

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

/*
 * Figure: a visual element, the principal visualization datatype
 * Note: for experimentation purposes this is a small extract from the real thing: vis/Figure.rsc
 */
 
public alias Figures = list[Figure];

data EventHandler 
	= handle()
	| handle(str event, Bind[value] binder)
	| handle(str event,Figure fig)
	;

public data Figure(
	
		tuple[HAlign, VAlign] align = <hcenter(), vcenter()>,
		HAlign halign = hcenter(),
		VAlign valign = vcenter(),	
		EventHandler on = handle()
	) =
	
	 emptyFigure()
	
   | text(value text)		   

   | box(Figure fig=emptyFigure())
 
  ;