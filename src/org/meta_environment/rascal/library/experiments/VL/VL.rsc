module experiments::VL::VL
import Integer;
import List;

alias Color = int;

@doc{Gray color}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public void java gray(real perc);

@doc{Gray color with transparency}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public Color java gray(real perc, real alpha);

@doc{Named color}
@reflect{Needs calling context when generating an exception}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public Color java color(str colorName);

@doc{RGB color}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public Color java rgb(real r, real g, real b);

@doc{RGB color with transparency}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public Color java rgb(real r, real g, real b, real alpha);

@doc{Create a colorscale}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public list[Color] java colorScale(Color from, Color To, int steps);

data VPROP =
/* sizes */
     width(int width)
   | height(int height)
   | height2(int height2)               // TODO: height(list[int] heights)
   | size(int size)                     // size of varies elems
   | visible(bool visible)				// is elem visible?
   | title(str title)					// title of elem
   
/* direction and alignment */
   | horizontal()                       // horizontal composition
   | vertical()                         // vertical composition
   | top()                              // top alignment
   | center()                           // center alignment
   | bottom()                           // bottom alignment
   | left()                             // left alignment
   | right()                            // right alignment
 
 /* stroke attributes */
   | lineWidth(int lineWidth)			// line width used by elem
   | fillStyle(Color fillStyle)			// fill color used by elem
   | strokeStyle(Color strokeStyle)		// stroke color used by elem
   
 /* text attributes */
   | text(str s)  
   ;

data VELEM = 
/* drawing primitives */
     rect(list[VPROP] props)			// rectangle
   | line(list[VPROP] props)			// line
   | dot(list[VPROP] props)				// dot
   | area(list[VPROP] props)			// area
   | label(list[VPROP] props)			// text label
   
/* composition */
   | concat(list[VELEM] elems)
   | concat(list[VPROP] props, list[VELEM] elems)
   
   | overlay(list[VELEM] elems) 
   | overlay(list[VPROP] props, list[VELEM] elems)
   ;

@doc{Render a panel}
@reflect{Needs calling context when calling argument function}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public void java render(VELEM elem);

public void b0(){
	render(rect([ width(100), height(200) ]));
}

public void r3(){
	render(concat([vertical(),right()],
	              [rect([ width(100), height(200) ]),
			       rect([ width(150), height(100) ]),
			       rect([ width(200), height(50) ])
			      ]
		));
}

public void b1(){ 
    d1 = [10, 12, 17, 15, 7];      
	bar1 = concat([
                strokeStyle(0),
                lineWidth(1),
	            fillStyle(125),
	            width(10)
               ],
               [ rect([height(d * 8)]) | d <- d1 ]
               );
    render(bar1);
}

public void b2(){ 
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];
    m = max(size(d1), size(d2));   
    list[VELEM] bars = [];
    for(int i <- [0 .. m]){	
                   bars += [rect([fillStyle(125), height((d1[i] ? 0) * 8)]),
                            rect([fillStyle(250), height((d2[i] ? 0) * 8)])
                           ];              
               }
	bar1 = concat([
                strokeStyle(0),
                lineWidth(1),
	          	width(10),
	          	top()
               ],
               bars
               );
    render(bar1);
}

public void b2v(){ 
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];
    m = max(size(d1), size(d2));   
    list[VELEM] bars = [];
    for(int i <- [0 .. m]){	
                   bars += [
                            concat([vertical()],
                                   [rect([fillStyle(125), height((d1[i] ? 0) * 8)]),
                                    rect([fillStyle(250), height((d2[i] ? 0) * 8)])
                                   ])
                           ];              
               }
	bar1 = concat([
                strokeStyle(0),
                lineWidth(1),
	          	width(10)
               ],
               bars
               );
    render(bar1);
}

public void d1(){ 
    dt1 = [10, 12, 17, 15, 7];      
	bar1 = concat([
                strokeStyle(0),
                lineWidth(0),
	            fillStyle(125),
	            width(10),
	            size(5)
               ],
               [ dot([height(d * 8)]) | d <- dt1]
               );
    render(bar1);
}

public void l1(){ 
    dt1 = [10, 12, 17, 15, 7];      
	bar1 = concat([
                strokeStyle(0),
                lineWidth(0),
	            fillStyle(125),
	            width(10),
	            size(5)
               ],
               [ line([height(dt1[i] * 8), height2(dt1[i+1] * 8)]) | i <- [0 .. size(dt1) - 2]]
               );
    render(bar1);
}

public void a1(){ 
    dt1 = [10, 12, 17, 15, 7];      
	bar1 = concat([
                strokeStyle(0),
                lineWidth(2),
	            fillStyle(125),
	            width(50),
	            size(5)
               ],
               [ area([height(dt1[i] * 8), height2(dt1[i+1] * 8)]) | i <- [0 .. size(dt1) - 2]]
               );
    render(bar1);
}

public void dl1(){ 
    dt1 = [10, 12, 17, 15, 7];      
	bar1 = overlay([
                strokeStyle(0),
                lineWidth(0),
	            fillStyle(125),
	            width(50),
	            size(20)
               ],
               [
            	   concat([ line([height(dt1[i] * 8), height2(dt1[i+1] * 8)]) | i <- [0 .. size(dt1) - 2]]),
            	   concat([ dot([height(d * 8)]) | d <- dt1])
               ]
               );
    render(bar1);
}

public void o1(){

render(overlay([ rect([width(100), height(200)]), 
                 rect([width(150), height(100)])
               ])
      );
}