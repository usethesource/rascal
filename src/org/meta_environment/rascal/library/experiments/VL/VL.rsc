module experiments::VL::VL
import Integer;
import List;
import Set;
import IO;

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
public Color java rgb(int r, int g, int b);

@doc{RGB color with transparency}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public Color java rgb(int r, int g, int b, real alpha);

@doc{Interpolate two colors (in RGB space)}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public list[Color] java interpolateColor(Color from, Color to, real percentage);

@doc{Create a list of interpolated colors}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public list[Color] java colorSteps(Color from, Color to, int steps);

@doc{Create a colorscale}
public Color(int) colorScale(list[int] values, Color from, Color to){
   mn = min(values);
   range = max(values) - mn;
   sc = colorSteps(from, to, 10);
   return Color(int v) { return sc[(9 * (v - mn)) / range]; };
}

data VPROP =
/* sizes */
     width(int width)
   | height(int height)
   | height2(int height2)               // TODO: height(list[int] heights)
   | size(int size)                     // size of varies elems
   | visible(bool visible)				// is elem visible?
   | gap(int amount)                    // gap between elements in composition
   
/* direction and alignment */
   | horizontal()                       // horizontal composition
   | vertical()                         // vertical composition
   | top()                              // top alignment
   | center()                           // center alignment
   | bottom()                           // bottom alignment
   | left()                             // left alignment
   | right()                            // right alignment
   
/* transformations */
//   | move(int byX)        			// translate in X direction
//   | vmove(int byY)                     // translate in Y direction
//     | rotate(int angle)
//  | scale(real perc)

 
 /* line and border attributes */
   | lineWidth(int lineWidth)			// line width
   | lineColor(Color lineColor)		    // line color
   | lineColor(str colorName)           // named line color
   
   | fillColor(Color fillColor)			// fill color
   | fillColor(str colorName)           // named fill color
   
 /* text attributes */
   | text(str s)                        // the text itself
   | font(str fontName)                 // named font
   | fontSize(int size)                 // font size
   | textAngle(int angle)               // rotation
   
/* other */
   | name(str name)                     // name of elem (used in edges and layouts)
   ;

data VELEM = 
/* drawing primitives */
     rect(list[VPROP] props)			// rectangle
   | line(list[VPROP] props)			// straight line between two points
   | dot(list[VPROP] props)				// dot
   | area(list[VPROP] props)			// area
   | label(list[VPROP] props)			// text label
   | edge(list[VPROP], str from, str to) // edge between between two elements
   
/* composition */
   | combine(list[VELEM] elems)
   | combine(list[VPROP] props, list[VELEM] elems)
   
   | overlay(list[VELEM] elems) 
   | overlay(list[VPROP] props, list[VELEM] elems)
   
   | grid(list[VELEM] elems) 
   | grid(list[VPROP] props, list[VELEM] elems)
   
   | graph(list[VPROP], list[VELEM] nodes, list[VELEM] edges)
   ;

@doc{Render a panel}
@reflect{Needs calling context when calling argument function}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public void java render(VELEM elem);

public void rect1(){
	render(rect([ width(100), height(200) ]));
}

public void rect2(){
	render(rect([ width(100), height(200), fillColor("green") ]));
}

public void rect3(){
	render(rect([ width(100), height(200), fillColor("green"), lineColor("red")]));
}

public void rect4(){
	render(rect([ width(100), height(200), fillColor(rgb(0, 0, 205)) ]));  
}

public void rect5(){
	render(rect([ width(100), height(200), fillColor(rgb(0, 0, 205, 0.2)) ]));  // TODO alpha does not work
}

public void lab1(){
	render(label([ text("Een label"), fontSize(20), fillColor("black")]));
}

public void rlab1(){
	render(combine([vertical(), center(), gap (10)],
	               [
	                
	                label([ text("Een label"), fontSize(20), fillColor("black"), textAngle(-90) ]),
	                rect([ width(100), height(200), fillColor("red")])
	                
	                ]));
}

public void rlab2(){
	render(overlay([center()],
	              [rect([ width(100), height(200), fillColor("red") ]),
			       label([ text("Een label"), fontSize(20), fillColor("black")])
			      ]
		));
}

public void r3(){
	render(combine([fillColor("yellow"), gap(10)],
	              [rect([ width(100), height(200), fillColor("red") ]),
			       rect([ width(150), height(100), rotate(-30)]),
			       rect([ width(200), height(50), fillColor("green") ])
			      ]
		));
}

public void r4(){
	render(combine([vertical(),right()],
	              [rect([ width(100), height(200), fillColor("red") ]),
			       rect([ width(150), height(100), fillColor("blue") ]),
			       rect([ width(200), height(50), fillColor("green") ])
			      ]
		));
}

public void bar1(){ 
    dt1 = [10, 12, 17, 0, 15, 7, 20, 40, 60];  
    colors = colorScale(dt1, color("blue"), color("red"));  
	b = combine([
                lineColor(0),
                lineWidth(1),
	            fillColor(125),
	            width(10)
               ],
               [ rect([height(d * 8), fillColor(colors(d))]) | d <- dt1 ]
               );
    render(b);
}

public void bar2(){ 
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];
    m = max(size(d1), size(d2));   
    bars = [ combine([ rect([fillColor("green"), height((d1[i] ? 0) * 8)]),
                      rect([fillColor("red"), height((d2[i] ? 0) * 8)])
                    ])
           | int i <- [0 .. m]
           ];
    
	b = combine([
                lineColor(0),
                lineWidth(1),
	          	width(10),
	          	top(),
	          	gap(10)
               ],
               bars
               );
    render(b);
}

public void bar2v(){ 
    d1 = [10, 12, 17, 15, 7]; 
    d2 = [ 5,  6,  9,  7, 3, 20];
    m = max(size(d1), size(d2));   
    bars = [combine([vertical(), gap(0)],
                   [rect([fillColor("green"), height((d1[i] ? 0) * 8)]),
                    rect([fillColor("red"), height((d2[i] ? 0) * 8)])
                   ])
           | int i <- [0 .. m]
           ];
           
	b = combine([
                lineColor(0),
                lineWidth(1),
	          	width(10),
	          	gap(5)
               ],
               bars
               );
    render(b);
}

public void dot1(){
render(overlay([rect([width(100), height(100)]),
                dot([width(50), height(100), size(20)])
              ]));
}


public void dot2(){       
    dt1 = [10, 12, 17, 15, 7];      
	b = combine([
                lineColor("blue"),
                lineWidth(1),
	            fillColor("lightblue"),
	            width(10),
	            size(5)
               ],
               [space(20)] + [ dot([height(d * 8)]) | d <- dt1]
               );
    render(b);
}

public void l1(){ 
    dt1 = [10, 12, 17, 15, 7];      
	b = combine([
                lineColor("blue"),
                lineWidth(2),
	            fillColor(125),
	            width(20)
               ],
               [ line([height(dt1[i] * 8), height2(dt1[i+1] * 8)]) | i <- [0 .. size(dt1) - 2]]
               );
    render(b);
}

public void a1(){ 
    dt1 = [10, 12, 17, 15, 7];      
	b = combine([
                lineColor(0),
                lineWidth(1),
	            fillColor(125),
	            width(50),
	            size(5)
               ],
               [ area([height(dt1[i] * 8), height2(dt1[i+1] * 8)]) | i <- [0 .. size(dt1) - 2]]
               );
    render(b);
}

public void dl1(){      
    dt1 = [10, 12, 17, 15, 7];      
	b = combine([space(100),
	            overlay([
                lineColor("blue"),
                lineWidth(0),
	            fillColor("lightblue"),
	            width(50),
	            size(10)
               ],
               [
            	   combine([ line([height(dt1[i] * 8), height2(dt1[i+1] * 8)]) | i <- [0 .. size(dt1) - 2]]),
            	   combine([ dot([height(d * 8)]) | d <- dt1])
               ]
               )]);
    render(b);
}

public void o1(){

render(overlay([ rect([width(100), height(200)]), 
                 rect([width(100), height(200)])
               ])
      );
}

public void grid1(){
  rects = [rect([width(50), height(50),fillColor("red")]), rect([width(30), height(30),fillColor("yellow")]), 
           rect([width(30), height(30),fillColor("green")]), rect([width(70), height(50),fillColor("blue")]),
           rect([width(70), height(70),fillColor("black")])
           ];

  render(grid([width(120), gap(10), bottom()], rects));
}

public void graph1(){
     nodes =
     	[ rect([name("A"), width(10), height(20), fillColor("green")]),
     	  rect([name("B"), width(20), height(30), fillColor("red")]),
     	  rect([name("C"), width(30), height(20), fillColor("blue")])
     	];
    edges = 
    	[ edge([lineWidth(1)], "A", "B"),
    	  edge([lineWidth(1)], "B", "C"),
    	  edge([lineWidth(1)], "C", "A")
    	];
    	    
    render(graph([width(400), height(400)], nodes, edges));
}
alias CI = tuple[str name, int noa, int nom, int sloc];

set[CI] classes =
{ <"A", 1, 2, 100>,
  <"B", 5, 5, 10000>,
  <"C", 7, 7, 500>,
  <"D", 2, 10, 5000>,
  <"E", 10, 2, 1000>,
  <"F", 3, 3, 200>,
  <"G", 10, 10, 1000>
};

rel[str,str] inherits =
{ <"B", "A">,
  <"C", "B">,
  <"D", "C">,
  <"E", "B">,
  <"G", "F">
};

public void class1() {
   cscale = colorScale(toList(classes.sloc), color("green"), color("red"));
   render(combine([top()], [ rect([width(c.noa*5), height(c.nom*5), fillColor(cscale(c.sloc))]) | CI c <- classes]));
}

public void class2() {
   cscale = colorScale(toList(classes.sloc), color("green"), color("red"));
   nodes = [ rect([name(c.name), width(c.noa*5), height(c.nom*5), fillColor(cscale(c.sloc))]) | CI c <- classes];
   edges = [ edge([], from,to) | <str from, str to> <- inherits ];
   
   render(graph([width(400), height(400)], nodes, edges));      
}
