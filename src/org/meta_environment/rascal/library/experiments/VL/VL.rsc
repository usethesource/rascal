module experiments::VL::VL
import Integer;
import List;

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
     width(int width)
   | height(int height)
   | height2(int height2)
   | visible(bool visible)				// is elem visible?
   | title(str title)					// title of elem
   | horizontal()                       // horizontal composition
   | vertical()                         // vertical composition
   | top()                              // top alignment
   | center()                           // center alignment
   | bottom()                           // bottom alignment
   | left()                             // left alignment
   | right()                            // right alignment
   | size(int size)                     // size of varies elems
   | lineWidth(int lineWidth)			// line width used by elem
   | fillStyle(COLOR fillStyle)			// fill color used by elem
   | strokeStyle(COLOR strokeStyle)		// stroke color used by elem
   ;

data VELEM = 
/* primitives */
     rect(list[VPROP] props)
   | line(list[VPROP] props)
   | dot(list[VPROP] props)
   | area(list[VPROP] props)
   
/* composition */
   | concat(list[VPROP] props, list[VELEM] elem)
   | overlay(list[VPROP] props, list[VELEM] elems)
   ;
     
data Panel = panel(list[VPROP] props, list[VELEM] elms);

@doc{Render a panel}
@reflect{Needs calling context when calling argument function}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public void java render(VELEM elem);

public void b0(){
	render(rect([ width(100), height(200)]));
}

public void r3(){
	render(concat([vertical(),right()],
	              [rect([ width(100), height(200)]),
			       rect([ width(150), height(100)]),
			       rect([width(200), height(50)])
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
               [ rect([height(d * 8)]) | d <- d1]
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
            	   concat([], [ line([height(dt1[i] * 8), height2(dt1[i+1] * 8)]) | i <- [0 .. size(dt1) - 2]]),
            	   concat([], [ dot([height(d * 8)]) | d <- dt1])
               ]
               );
    render(bar1);
}

public void o1(){

render(overlay([], [rect([width(100), height(200)]), 
                    rect([width(150), height(100)])
                   ])
      );

}