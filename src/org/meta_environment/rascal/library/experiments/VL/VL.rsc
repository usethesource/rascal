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
     width(int width)
   | height(int height)
   | visible(bool visible)				// is elem visible?
   | title(str title)					// title of elem
   | horizontal()                       // horizontal mode
   | vertical()                         // vertical mode
   | lineWidth(int lineWidth)			// line width used by elem
   | fillStyle(COLOR fillStyle)			// fill color used by elem
   | strokeStyle(COLOR strokeStyle)		// stroke color used by elem
   ;

data VELEM = 
     rect(list[VPROP] props)
   | line(list[VPROP] props)
   | dot(list[VPROP] props)
   | concat(list[VPROP] props, list[VELEM] elem)
   | merge(list[VPROP] props, list[VELEM] elems)
   | overlay(list[VPROP] props, list[VELEM] elems)
   ;
     
data Panel = panel(list[VPROP] props, list[VELEM] elms);

@doc{Render a panel}
@reflect{Needs calling context when calling argument function}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public void java render(VELEM elem);

public void b0(){
	render(rect([width(10), height(20)]));
}


public void m1(){ 
    d1 = [10, 12, 17, 15, 7];           
	bar1 = merge([
                width(10),
                strokeStyle(0),
                lineWidth(1),
	            fillStyle(125),
	            gap(10)
               ],
               [
                 rect([ values(d1), height(int (int i) {return d1[i] * 8;})])
               ]);
    render(bar1);
}

public void m1(){ 
    d1 = [10, 12, 17, 15, 7]; 
    width = 10;          
	bar1 = concat([
                strokeStyle(0),
                lineWidth(1),
	            fillStyle(125),
	            align(bottom)
               ],
               [ rect(width, d * 8) | d <- d1]
               );
    render(bar1);
}

public void c1(){          
    d1 = [10, 12, 17, 15, 7];           
	bar1 = overlay([
                width(20),
                strokeStyle(0),
                lineWidth(1),
	            fillStyle(125),
	            gap(10),
	            offset(10)
               ],
               [
               	 line([ values(d1), height(int (int i) {return 2 * d1[i] * 8;})]),
                 dot([  values(d1), height(int (int i) {return 2 * d1[i] * 8;})])
               ]);
    render(bar1);
}

public void m1v(){    
    d1 = [10, 12, 17, 15, 7];           
	bar1 = merge([
				vertical(1),
                width(10),
                strokeStyle(0),
                lineWidth(1),
	            fillStyle(125),
	            gap(10)
               ],
               [
                 rect([ values(d1),  height(int (int i) {return d1[i] * 8;})])
               ]);
    render(bar1);
}

public void c1v(){   
    d1 = [10, 12, 17, 15, 7];           
	bar1 = concat([
				vertical(1),
                width(10),
                strokeStyle(0),
                lineWidth(1),
	            fillStyle(125),
	            gap(10)
               ],
               [
                 rect([ values(d1),  height(int (int i) {return d1[i] * 8;})])
               ]);
    render(bar1);
}

public void c1o(){          
    d1 = [10, 12, 17, 15, 7];           
	bar1 = concat([
                width(10),
                strokeStyle(0),
                lineWidth(1),
	            fillStyle(125),
	            gap(10)
               ],
               [
                 rect([ values(d1),  
                        height(int (int i) {return d1[i] * 8;}),
                        offset(int (int i) {return 50 + i * 10;})
                      ])
               ]);
    render(bar1);
}

public void c1vo(){          
    d1 = [10, 12, 17, 15, 7];           
	bar1 = concat([
	            //vertical(1),
                width(10),
                strokeStyle(0),
                lineWidth(1),
	            fillStyle(125),
	            gap(10)
               ],
               [
                 rect([ values(d1),  
                        vertical(1),
                        height(int (int i) {return d1[i] * 8;}),
                        offset(int (int i) {return 50 + i * 10;})
                      ])
               ]);
    render(bar1);
}

public void c2(){           
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];          
	bar2 = concat([
               	width(10),
               	strokeStyle(0),
               	lineWidth(1),
               	gap(10)
               ],
               [ rect([values(d1),height(int (int i) {return d1[i] * 8;}), fillStyle(125)]),
                 rect([values(d2), height(int (int i) {return d2[i] * 8;}), fillStyle(250)])
               ]
               );
    render(bar2);
}

public void c2(){ 
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];
    m = max(size(d1), size(d2));
    width = 10;        
    gap = 5;  
	bar1 = concat([
                strokeStyle(0),
                lineWidth(1),
	            align(bottom)
               ],
               for(int i <- [0 .. m]){
                   if(i < size(d1)) append [fillStyle(125), rect(width, d1[i] * 8)];
                   if(i < size(d2)) append [fillStyle(250), rect(width, d2[i] * 8)];
                   append move(gap);
               }
               );
    render(bar1);
}

public void c2v(){    
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];          
	bar2 = concat([
	            vertical(1),
               	width(10),
               	strokeStyle(0),
               	lineWidth(1),
               	gap(50)
               ],
               [ rect([values(d1),horizontal(1),height(int (int i) {return d1[i] * 8;}), fillStyle(125)]),
                 rect([values(d2),horizontal(1), height(int (int i) {return d2[i] * 8;}), fillStyle(250)])
               ]
               );
    render(bar2);
}

public void m2(){           
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];          
	bar2 = merge([
               	width(10),
               	strokeStyle(0),
               	lineWidth(1),
               	gap(10),
	            fillStyle(125)
               ],
               [ rect([values(d1),height(int (int i) {return d1[i] * 8;}), fillStyle(125)]),
                 rect([values(d2), height(int (int i) {return d2[i] * 8;}), fillStyle(250)])
               ]
               );
    render(bar2);
}

public void m2v(){    
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];          
	bar2 = merge([
	            vertical(1),
               	width(10),
               	strokeStyle(0),
               	lineWidth(1),
               	gap(10),
	            fillStyle(125)
               ],
               [ rect([values(d1),height(int (int i) {return d1[i] * 8;}), fillStyle(125)]),
                 rect([values(d2), height(int (int i) {return d2[i] * 8;}), fillStyle(250)])
               ]
               );
    render(bar2);
}

***/