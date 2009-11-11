module experiments::VL::VL

data COLOR =
     color(str name)
   | color(real gray)
   | color(real gray, real alpha)
   | color(real red, real green, real blue)
   | color(real red, real green, real blue, real alpha)
   ;

data VPROP =

     left(int left) 					// offset from left border
   | left(int(int i) leftFun) 
    		
   | right(int right)					// offset from right border
   | right(int(int i) rightFun) 
   	
   | top(int top)						// offset from top
   | top(int(int i) topFun) 
   			
   | bottom(int bottom)
   | bottom(int(int i) bottomFun) 		// offset from bottom
   
   | width(int width)
   | width(int(int i) widthFun)     	// width of elem
   
   | height(int height)
   | height(int(int i) heightFun)		// height of elem
   
   | visible(bool visible)				// is elem visible?
   
   | title(str title)					// title of elem
   
   | values(list[value] values) 		// data for elem
   | values(list[value]() valuesFun)
   
   | lineWidth(int lineWidth)			// line width used by elem
   | lineWidth(int(int i) lineWidthFun)
   
   | fillStyle(COLOR fillStyle)			// fill color used by elem
   | fillStyle(COLOR(int i) fillStyleFun)
   
   | strokeStyle(COLOR strokeStyle)		// stroke color used by elem
   | strokeStyle(COLOR(int i) strokeStyleFun)
   ;

data VELEM = 
     bar(list[VPROP] props)
     ;
     
data Panel = panel(list[VPROP] props, list[VELEM] elms);

@doc{Render a panel}
@reflect{Needs calling context when calling argument function}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public void java render(Panel p);

public void main(){

	d = [10, 12, 17, 15, 7];

    P = panel([width(200), height(200)],
              [bar([values(d),
                    bottom(20),
                    width(20),
                    height(int (int i) {return d[i] * 8;}),
                    left(int (int i) {return i * 25;})
                   ])
              ]);
    render(P);

}