module experiments::VL::VL

/*
data COLOR =
     color(str name)
   | color(real gray)
   | color(real gray, real alpha)
   | color(real red, real green, real blue)
   | color(real red, real green, real blue, real alpha)
   ;
   */

alias COLOR = int;

data VPROP =

     left(int left) 					// offset from left border
   | left(int(int i) leftFun) 
    		
   | right(int right)					// offset from right border
   | right(int(int i) rightFun) 
   
   | gap(int gap)					   // gap between elements
   | gap(int(int i) gapFun) 
   	
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

/* simple bar chart */
public void b1(){
               
    d1 = [10, 12, 17, 15, 7];           
	bar1 = bar([bottom(20),
                width(10),
                strokeStyle(0),
                lineWidth(1),
	            values(d1),
	            fillStyle(125),
                height(int (int i) {return d1[i] * 8;}),
                gap(int (int i) {return i > 0 ? 5 : 0;})
               ]);

    P = panel([width(200), height(200)],   [bar1]);
    render(P);
}

/* multiple bars */
public void b2(){

	common = [ bottom(20),
               width(10),
               strokeStyle(0),
               lineWidth(1),
               gap(0)
             ];
               
    d1 = [10, 12, 17, 15, 7];           
	bar1 = bar([common,
	            values(d1),
	            fillStyle(125),
                height(int (int i) {return d1[i] * 8;})
               ]);
                   
    d2 = [5, 6, 9, 7, 3];
    bar2 = bar([common,
                values(d2),
                fillStyle(200),
                height(int (int i) {return d2[i] * 8;})
               ]);

    P = panel([width(200), height(200)],   [bar1, bar2]);
    render(P);

}
/* Stacked bar chart */
public void b3(){

	common = [ bottom(20),
               width(10),
               strokeStyle(0),
               lineWidth(1),
               left(int (int i) {return i * 15;})
             ];
               
    d1 = [10, 12, 17, 15, 7];           
	bar1 = bar([common,
	            values(d1),
	            fillStyle(125),
                height(int (int i) {return d1[i] * 8;})
               ]);
                   
    d2 = [5, 6, 9, 7, 3];
    bar2 = bar([common,
                values(d2),
                fillStyle(200),
                bottom(int (int i) {return d1[i] * 8;}),
                height(int (int i) {return d2[i] * 8;})
               ]);

    P = panel([width(200), height(200)],   [bar1, bar2]);
    render(P);

}