module experiments::vis2::Tst2

data HAlign = left() | hcenter() | right();
data VAlign = top() | vcenter() | bottom();

public data Figure(
		real fillOpacity = 1.0
	
		//, tuple[HAlign, VAlign] align = <hcenter(), vcenter()>
		//, HAlign halign = hcenter()
		//, VAlign valign = vcenter()
	) =
	
	emptyFigure()
   | box(Figure inner)      // rectangular box with inner element
//   | box()			        // rectangular box
 
   ;
 