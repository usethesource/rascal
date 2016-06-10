@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module experiments::vis2::sandbox::Demo
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import experiments::vis2::sandbox::Steden;
import experiments::vis2::sandbox::SinAndCos;
import experiments::vis2::sandbox::Graph;
import experiments::vis2::sandbox::Flower;
import experiments::vis2::sandbox::Shapes;
import util::Math;
import Prelude;

void ex(str title, Figure b, bool debug = false) = render(b, debug = debug, align = centerMid);

// --------------------------------------------------------------------------

public Figure newNgon(str lc, Figure el) {
      return atXY(0, 0, ngon(n = 5,  grow=1.0, align = centerMid, lineColor= lc, 
             lineWidth = 8, fillColor = "white", padding=<0,0,0,0>,
      fig = el));
      }

public Figure demo1() {
          list[str] colors = ["antiquewhite", "yellow", "red","blue" ,"grey","magenta"];
           return hcat(size=<300, 400>, figs=[
             (idNgon(5, 20) |newNgon(e, it)| e<-colors)
             ,
             (idNgon(5, -1) |newNgon(e, it)| e<-colors)
           ]);}
void tdemo1()  {render(demo1());}


 void tfdemo1(loc l) {
      writeFile(l, toHtmlString(demo1(), debug = false, width = 600, height = 600));
      }

// --------------------------------------------------------------------------

public Figure newBox(str lc, Figure el) {
      return atXY(10, 10, box(align = centerMid, lineColor= lc, 
             fillColor = "none", fig = el, lineWidth = 8));
      }
public Figure demo2() { 
         list[str] colors = ["green", "red", "blue", "grey", "magenta", "brown"];
         return hcat(fillColor="none", borderWidth = 0, size=<400, 400>, hgap = 6, figs = [
           (
           atXY(10, 10, box(align = centerMid, 
             lineColor="grey", fillColor = "yellow", lineOpacity=1.0, size=<30, 40>))
           |newBox(e, 
          it)| e<-colors)
          ,
           (atXY(10, 10, box(align = centerMid, lineColor="grey", fillColor = "yellow", lineOpacity=1.0))
           |newBox(e, it)| e<-colors)
            ]);
          }
void tdemo2(){ render(demo2()); }

 void tfdemo2(loc l) {
      writeFile(l, toHtmlString(demo2(), debug = false, width = 600, height = 600));
      }

// --------------------------------------------------------------------------


public Figure newEllipse(str lc, Figure el) {
      return atXY(0, 0, ellipse(lineColor= lc, lineWidth = 8, 
           fillColor = "white", padding=<0,0,0,0>, 
      fig = el));
      }
public Figure demo3() {
      list[str] colors = ["red","blue" ,"grey","magenta", "brown", "green"];
      return hcat(fillColor="none", size=<400, 200>, hgap = 6, figs = [
      (idEllipse(17, 12) |newEllipse(e,  it)| e<-colors)
      ,
      // frame(
      (idEllipse(-1, -1) |newEllipse(e, it)| e<-colors)
      // , shrink=1.0)
      ]);
      ;
      }
      
void tdemo3()  {render(demo3(), debug = false);}

void tfdemo3(loc l) {
      // println(schoolPlot());
      writeFile(l, toHtmlString(demo3(), debug = false, width = 600, height = 600));
      }
// ---------------------------------------------------------------------------

list[Vertex] innerGridH(int n) {
     num s = 1.0/n;
     return [move(0, y), line(1, y)|y<-[s,2*s..1]];
     }
     
list[Vertex] innerGridV(int n) {
     num s = 1.0/n;
     return [move(x, 0), line(x, 1)|x<-[s,2*s..1]];
     }

list[Vertex] innerSchoolPlot1() {
     num s =0.5;
     return [move(10, i), line(10-i, 10)|i<-[s,s+0.5..10]];
     }
     
list[Vertex] innerSchoolPlot2() {
     num s = 0.5;
     return [move(i, 0), line(0, 10-i)|i<-[s,s+0.5..10]];
     }
     
Figure tst0() = circle(padding=<22, 22, 22, 22>, lineWidth = 2, fillColor = "yellow", lineColor = "red", fig= box(fillColor = "lightblue",
        width = 100, height = 100, lineWidth = 4, lineColor = "blue"));
        
void tst() = render(tst0()); 
        
void tst(loc l) = writeFile(l, toHtmlString(hcat(figs=[shape(innerSchoolPlot1()+innerSchoolPlot2(), fillColor = "none",
        scaleX=<<0,10>,<0,400>>, scaleY=<<0,10>,<400,0>>, width = 400, height = 400, 
        lineColor = "blue")])));  
     
Figure schoolPlot() {
     return  overlay(lineWidth=1, width = 400, height = 400, figs = [
        shape(innerSchoolPlot1()+innerSchoolPlot2(), fillColor = "none",
        scaleX=<<0,10>,<0,400>>, scaleY=<<0,10>,<400,0>>, width = 400, height = 400, 
        lineColor = "blue")
         , 
       circle(r=40, cx = 200, cy = 200, fillColor = "yellow"
        ,lineWidth = 10, lineColor = "red", lineOpacity=0.5, fillOpacity=0.5, fig = text("Hello")
        )
        ,
        atXY(50, 50, circle(lineWidth=10, lineColor= "red", fillColor = "none",  padding=<0, 0, 0, 0>, fig= atXY(0,0, 
             box(width=50, height = 50, lineColor="grey", fillColor = "antiquewhite")
             )))
        ,atXY(250, 250, circle(lineWidth=10, fillColor="none", lineColor="brown", padding=<0, 0, 0, 0>
        , fig=ngon(n=7, r=40, lineWidth = 10, lineColor = "grey", fillColor = "none")))
        ])
        ;
     } 

Figure simpleGrid(Figure f) {
     return overlay(fillColor="none", width = 400, height = 400, figs=[
        // box(lineWidth=0, lineColor="black", fig=
          shape(innerGridV(10)+innerGridH(10) 
             ,size=<398, 398>, scaleX=<<0,1>,<0,400>>, scaleY=<<0,1>,<0,400>>, fillColor = "none",
              lineColor = "lightgrey", lineWidth = 1)
       // , fillColor = "none")
        , f
        ]
     );
     }
     
Figure labeled(Figure g) {
     return hcat(lineWidth = 0, 
        figs = [
           vcat(figs=gridLabelY(), padding=<0,0,0,20>)
           , vcat(lineWidth = 0, figs = [box(fig=g, lineWidth=1),
           hcat(figs = gridLabelX())])
           ]);
     }
     
 list[Figure] gridLabelX() {
     return [box(lineWidth = 0, lineColor = "none", width = 40, height = 20, fig = text("<i>"))|i<-[1..10]];
     }
     
 list[Figure] gridLabelY() {
     return [box(lineWidth = 0, lineColor = "none", width = 40, height = 40, fig = text("<i>"))|i<-[9..0]];
     }
     
 Figure labeledGrid(Figure f) {
    return labeled(simpleGrid(f));   
    }
    
 Figure demo4() = labeledGrid(simpleGrid(schoolPlot()));
 
 // Figure demo4() = simpleGrid(schoolPlot());
    
 void tfdemo4(loc l) {
      // println(schoolPlot());
      writeFile(l, toHtmlString(demo4(), debug = false, width = 600, height = 600));
      }
      
 void tdemo4() {
      // println(schoolPlot());
      render( 
      demo4(), width = 600, height = 600, align = topLeft, debug = false);
      }
      

// ----------------------------------------------------------------------------------

Vertices hilbert(num x0, num y0, num xis, num xjs, num yis, num yjs, int n){
	/* x0 and y0 are the coordinates of the bottom left corner */
	/* xis & xjs are the i & j components of the unit x vector this frame */
	/* similarly yis and yjs */
	if (n<= 0){
   	return [line(x0+(xis+yis)/2, y0+(xjs+yjs)/2)];
	} else {
		return [ *hilbert(x0,             y0,             yis/2,  yjs/2,  xis/2,  xjs/2,  n-1),
   				 *hilbert(x0+xis/2,       y0+xjs/2,       xis/2,  xjs/2,  yis/2,  yjs/2,  n-1),
  				 *hilbert(x0+xis/2+yis/2, y0+xjs/2+yjs/2, xis/2,  xjs/2,  yis/2,  yjs/2,  n-1),
   				 *hilbert(x0+xis/2+yis,   y0+xjs/2+yjs,   -yis/2, -yjs/2, -xis/2, -xjs/2, n-1) ];
   	}
}

Figure demo5() = shape(hilbert(0, 0, 300, 0, 0, 300, 5), 
								startMarker=box(size=<10,10>, fillColor="red"),
								midMarker=box(size=<3,3>,fillColor="blue"),
								endMarker=box(size=<10,10>,fillColor="green"), size=<400, 400>
					   );

void hilbert1(){
	render(shape(hilbert(0, 0, 300, 0, 0, 300, 5)));
	
	
}

void hilbert2(){
	ex("hilbert2", demo5());
}

void hilbert3(){
	ex("hilbert3", hcat(,
						figs = [ box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 1))),
							     box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 2))),
							     box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 3))),
							     box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 4))),
							     box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 5)))
							 ]));
}

void hilberts(){
	ex("hilberts", grid(figArray=[
								   [text("1   ", fontSize=30),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 1))),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 1), 
															startMarker=box(size=<10,10>,fillColor="red"),
															midMarker=box(size=<3,3>,fillColor="blue"),
															endMarker=box(size=<10,10>,fillColor="green")))
							       ],
							       [text("2   ", fontSize=30),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 2))),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 2), 
															startMarker=box(size=<10,10>,fillColor="red"),
															midMarker=box(size=<3,3>,fillColor="blue"),
															endMarker=box(size=<10,10>,fillColor="green")))
							       ],
							       [text("3   ", fontSize=30),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 3))),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 3), 
															startMarker=box(size=<10,10>,fillColor="red"),
															midMarker=box(size=<3,3>,fillColor="blue"),
															endMarker=box(size=<10,10>,fillColor="green")))
							       ],
							       [text("4   ", fontSize=30),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 4))),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 4), 
															startMarker=box(size=<10,10>,fillColor="red"),
															midMarker=box(size=<3,3>,fillColor="blue"),
															endMarker=box(size=<10,10>,fillColor="green")))
							       ],
							       [text("5   ", fontSize=30),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 5))),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 5), 
															startMarker=box(size=<10,10>,fillColor="red"),
															midMarker=box(size=<3,3>,fillColor="blue"),
															endMarker=box(size=<10,10>,fillColor="green")))
							       ],
							       [text("6   ", fontSize=30),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 6))),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 6), 
															startMarker=box(size=<10,10>,fillColor="red"),
															midMarker=box(size=<3,3>,fillColor="blue"),
															endMarker=box(size=<10,10>,fillColor="green")))
							       ],
							       [text("7   ", fontSize=30),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 7))),
								   	box(size=<400,400>, fig=shape(hilbert(0, 0, 300, 0, 0, 300, 7), 
															startMarker=box(size=<10,10>,fillColor="red"),
															midMarker=box(size=<3,3>,fillColor="blue"),
															endMarker=box(size=<10,10>,fillColor="green")))
							       ]
								   ]));
}


/**********************  venndiagrams ******************************/
public Figure vennDiagram0() = overlay(
     size=<350, 150>,
     figs = [
           box(fillColor="none", align = topLeft, size=<350, 150>,
             fig = ellipse(width=200, height = 100, fillColor = "red", fillOpacity = 0.7))
          ,box(fillColor="none",align = topRight, size=<350, 150>,
             fig = ellipse(width=200, height = 100, fillColor = "green",fillOpacity = 0.7))
          ,box(fillColor="none",align = bottomMid, size=<350, 150>,
            fig = ellipse(width=200, height = 100, fillColor = "blue", fillOpacity = 0.7))
     ]
     );

public Figure demo6() = vennDiagram0();
     
public void vennDiagram() = render(vennDiagram0(), align =  centerMid);

void vennDiagram(loc l)= writeFile(l, toHtmlString(vennDiagram0(), debug = false));

/**********************  markers ******************************/

Figure marker() = shape([move(10, 10), line(200, 200)], scaleX=<<0, 1>,<0, 1>>, scaleY=<<0,1>,<0,1>>, 
     startMarker = box(lineWidth = 1, size=<50,50>, fig = circle(r=20, fillColor = "blue", fig = text("hello")), fillColor = "red"));

void tmarker()=render(marker());

Figure plotg(num(num) g, list[num] d) {
     Figure f = shape( [move(d[0], g(d[0]))]+[line(x, g(x))|x<-tail(d)]
         scaleX=<<-1,1>,<20,320>>,  scaleY=<<0,1>,<100,300>>,
         size=<400, 220>,shapeCurved=  true, lineColor = "red"
         , startMarker = box(width=10, height = 10,  fillColor = "blue", lineWidth = 0)
         , midMarker = ngon(n=3, r=4, fillColor = "purple", lineWidth = 0)
         , endMarker = ngon(n=3, r=10, fillColor = "green", lineWidth = 0)
         );
     return f;
     }
     
num(num) gg(num a) = num(num x) {return a*x*x;};


num g1(num x) = x*x;

Figure demo7() = plotg(gg(0.5), [-1,-0.9..1.1]);


public Figure hcat0() = hcat(figs = [box(fillColor="red"), box(fillColor="blue"), box(width=50, height=50)], width = 100, height = 100);

public void thcat() = render(hcat0(), align =  centerMid);

void thfcat(loc l)= writeFile(l, toHtmlString(hcat0(), debug = false));

Figure title(str s, Figure f) = 
     vcat(align = topLeft, size=<70, 51>, figs = [f, box(fillColor="antiqueWhite", lineWidth=0, height=15, width = 70, fig=text(s, fontSize = 9, height = 10))]);


/**********************  flag ******************************/

Figure dutch() = title("Holland", vcat(lineWidth = 0,
               figs=[box(fillColor= "#AE1C28")
                    ,box(fillColor="#FFF")
                    ,box(fillColor="#21468B" )
                    ]));
                    
Figure luxembourg() = title("Luxembourg", vcat(lineWidth = 0,
               figs=[box(fillColor= "#ed2939")
                    ,box(fillColor="#FFF")
                    ,box(fillColor="#00A1DE" )
                    ]));
                    
Figure german() = title("Germany", vcat(lineWidth = 0,figs=[
                     box(fillColor="#000000")
                    ,box(fillColor="#D00")
                    ,box(fillColor="#FFCE00")
                    ]));
  
Figure italian() = title("Italy", hcat(lineWidth = 0, figs=[ 
                      box(fillColor="#009246")
                     ,box(fillColor="#FFF")
                     ,box(fillColor="#ce2b37")
                    ]));  
                    
Figure  belgium(int width = -1, int height = -1) = 
                  title("Belgium", hcat(
                     lineWidth = 0,
                     width = width, 
                     height = height,
                     figs=[
                     box(lineWidth = 0,fillColor="#000000")
                    ,box(lineWidth = 0,fillColor="#FAE042")
                    ,box(lineWidth = 0,fillColor="#ED2939")
                    ])); 
                    
Figure france() = title("France", hcat(lineWidth = 0, figs=[ 
                      box(fillColor="#002395")
                     ,box(fillColor="#FFF")
                     ,box(fillColor="#ED2939")
                    ]));  
                    
void tbelgium() = render(grid(figArray=[[belgium(width = 201, height = 50)]]));

void fbelgium(loc l) = writeFile(l, toHtmlString(belgium(width = 200, height = 50)));
                    
Figure flags() = grid(hgap=5, vgap = 5, figArray=[
                    [dutch()
                    , luxembourg(), german()
                    ]
                   ,[italian(), belgium(), france()]
                   ],
                    width = 400, height = 200);  
                    
 
list[tuple[str, Figure]] flagNodes = [<"nl", dutch()>, <"be", belgium()>
    , <"lu", luxembourg()>, <"de", german()>, <"fr", france()>, <"it", italian()>];
    
    list[Edge] flagEdges = [edge("nl", "be"), edge("nl", "de"), edge("be", "lu"),
                  edge("be", "fr"), edge("be", "de"), edge("fr", "it")];
                  
                 
Figure gflags() = graph(nodes=flagNodes, edges=flagEdges, size=<300, 600>);

void tgflags() = render(box(fig=gflags(), size=<400, 400>, align =  centerMid));

void fgflags(loc l) = writeFile(l, toHtmlString(flags()));

void ftgflags(loc l) = writeFile(l, toHtmlString(gflags()));
   
Figure demo8() = flags(); 

Figure demo9() = steden(); 

Figure demo10() = sinAndCos();   

Figure demo11() = box(fig=gflags(), size=<400, 400>); 

Figure demo13() = fsm();                
                    
void tflags() = render(flags());           

void tsteden() = render(frame(atXY(100, 100, box(fig=steden(width=400, height = 400)))));

void fsteden(loc l) = writeFile(l, toHtmlString(frame(atXY(100, 100, hcat(figs=[steden(width=400, height = 400)])))));

void tsincos() = render(sinAndCos());


void tfsm() = render(fsm());

Figure klokBox(int r, int n) =  atXY(0, n/2, box(size=<10, n>,  rounded=<10, 10>, lineWidth = 1, lineColor="black", fillColor="yellow"));

Figure klokFrame() {
     int r = 80; 
     int d = 2;
     int cx = 20 + r;
     int cy = 20 + r; 
     int r1 = 95; 
     list[Figure] cs =
          [circle(r=r, cx= cx, cy = cy, fillColor = "silver", lineColor = "red")]
          +
          [
           atXY(20-6+r+toInt(sin((PI()*2*i)/12)*r1), toInt(20-5+r-cos((PI()*2*i)/12)*r1), htmlText("<i>", size=<20, 20>))|int i<-[12, 3, 6, 9]
          ]
        
        +[atXY(20-1+r+toInt(sin((PI()*2*i)/12)*(r)), toInt(20+r-cos((PI()*2*i)/12)*(r)), circle(r=d, fillColor="black"))|int i<-[12, 3, 6, 9]
         ]
        +box(lineColor = "none", fillColor= "none", fig=atXY(20, 20, rotateDeg(210, box(lineColor="none", size=<2*r, 2*r>, fig =klokBox(r, 70), align = centerMid))))      
        +box(lineColor = "none",fillColor= "none",fig=atXY(20, 20, rotateDeg(180, box(lineColor="none", size=<2*r, 2*r>, fig =klokBox(r, 50), align = centerMid))))  
          +[circle(r=5, cx = cx, cy = cy, fillColor = "red")]
        ;
     return box(fig=overlay(figs=cs, size=<220, 220>), size=<250, 250>);
     }
     
Figure demo14() = klokFrame();

void tklok()= render(klokFrame());


void fklok(loc l) = writeFile(l, toHtmlString(klokFrame()));

Figure ovBox(str color) =  box(lineColor = "none", fillOpacity=0.8, size=<100, 100>, fillColor= "none", align = centerMid, fig=box(size=<50, 10>
, fillColor=color));

Figure ov() = overlay(figs = [
    rotateDeg(45, ovBox("red"))
   ,rotateDeg(135, ovBox("green"))
   ,rotateDeg(0, ovBox("magenta"))
   ,rotateDeg(90, ovBox("blue"))
    ]);

void tov()= render(ov());

Figure demo15()= ov();

list[Figure] rgbFigs = [box(fillColor="red",size=<50,100>), box(fillColor="antiquewhite", size=<200,200>), box(fillColor="blue",  size=<10,10>)];

public Figure hcat11() = 
       box(padding=<0, 0, 0, 0>, lineWidth = 10, lineColor = "darkgrey"
       ,fig= ellipse(padding=<0, 0, 0, 0>
             ,fig=hcat(lineWidth=2, lineColor="brown", figs=rgbFigs) 
       ,lineWidth = 10, lineColor= "yellow", grow = 1.00, fillColor="lightgrey",align = centerMid
       )
 )
;

void thcat11() = render(hcat11());

void tfhcat11(loc l)= writeFile(l,
     toHtmlString(hcat11(), debug = false));

Figure demo16()= hcat11();

Figure bigg(num bigger) = circle(r=30, fig = 
    box(size=<40, 10>, fig= rotateDeg(-30, ngon(n=3, r=3, lineWidth=0, fillColor= "white")), 
        fillColor="antiquewhite", lineWidth = 2, lineColor="brown"),
    fillColor = "beige", lineColor = "green", bigger = bigger);

Figure big() = vcat(figs = [bigg(1.0), bigg(1.5), bigg(2)]);

Figure demo17() = big();

void tbig() = render(big());

Figure demo18() = flower();

void tflower() = render(flower(), size=<800, 800>);

Figure cell(str s, int r = 15) = 
     circle(
         lineColor = "black", fillColor = "antiquewhite",
          r = r, id = s   , fig = text(s, fontWeight="bold")
         ,event=on(["mouseenter", "mouseleave"], void(str e, str n, str v){
            if (e=="mouseleave")
           style(n, fillColor="antiquewhite");
            else
              style(n, fillColor="red");
        }));
   
public Figure wirth() {
   Figure r = 
       tree(
         box(fig=text("A", size=<15, 15>,fontWeight="bold" ), fillColor="salmon", lineWidth= 0, size=<100, 35>, rounded= <25, 25>), [
           tree(cell("B"), [
              tree(cell("D"), [cell("I")])
              ,tree(cell("E"),
                 [cell("J"), cell("K"), cell("L")])
               ])
            , tree(cell("C", r = 25), [
               tree(cell("F"), [cell("O")])
              ,tree(cell("G", r = 30), [cell("M"), cell("N")])
              ,tree(cell("H"), [ cell("P")])           
              ])
            ]
          ,
          manhattan = true, pathColor = "green", orientation = downTop()
       );
   return r;         
   }


Figure demo19() = wirth();

void twirth() = render(wirth());

Figure place(str fill) = box(size=<25, 25>, fillColor = fill);

Figure elFig(num shrink, bool tt) {
     // println(shrink);
     return 
       ellipse(shrink = shrink, fillColor =  pickColor(),   lineWidth = 2  
       ,fig=box(shrink=0.6, align = centerMid, 
            fig=
             circle( shrink = 0.8, fillColor=pickColor(), lineWidth = 0 
             , tooltip = tt?box(fig=htmlText("", size=<50, 20>, fontSize=10), fillColor="antiqueWhite"):emptyFigure() 
             ,event = on("mouseenter", void(str e, str n, str v) {
              textProperty("<n>_tooltip#0", text= style(n).fillColor);      
              })       
              
          ) 
          , fillColor=pickColor())
       )  
       ;
     }
 
Figures elFigs(int n, bool tt) = n>0?
    [elFig(1-i/(2*n), tt)|num i<-[0,1..n]]
   :[elFig(1-i/(2*-n), tt)|num i<-[-n,-n-1..0]];

public Figure shrink(bool tt) {resetColor();return grid(size=<400, 400>, figArray=[elFigs(5, tt), elFigs(-5, tt)], align = centerMid, borderWidth=1);}

void tshrink() = render(box(fig=shrink(false), size=<400, 400>));

Figure _tetris1() = 
       grid( vgap=0, hgap= 0
       , 
       figArray=[
       [place("blue"), emptyFigure()]
      ,[place("blue"), emptyFigure()]
      ,[place("blue"), place("blue")]
       ]);
       
Figure emptFigure() = box(size=<10, 10>);
       
Figure _tetris2() = 
       grid(vgap=0, hgap= 0,
       figArray=[
       [emptyFigure(), place("blue")]
      ,[emptyFigure(), place("blue")]
      ,[place("blue"), place("blue")]
       ]);
       
Figure _tetris3() = 
       grid(vgap=0, hgap= 0,
       figArray=[
       [place("red"), place("red")]
      ,[place("red"), place("red")]
       ]);
       
Figure _tetris4() = 
       grid(vgap=0, hgap= 0,
       figArray=[
       [place("yellow"), place("yellow"), place("yellow")]
      ,[emptyFigure(), place("yellow"), emptyFigure()]
       ]);
       
Figure _tetris5() = 
       grid(vgap=0, hgap= 0, 
       figArray=[
       [emptyFigure(), place("darkmagenta"), place("darkmagenta")]
      ,[place("darkmagenta"), place("darkmagenta"), emptyFigure()]
       ]);
       
Figure _tetris6() = 
       grid(vgap=0, hgap= 0, 
       figArray=[
       [place("brown")]
      ,[place("brown")]
      ,[place("brown")]
      ,[place("brown")]
       ]);
       
public Figure tetris() = hcat(borderStyle="ridge", borderWidth = 4, 
lineWidth = 1, align = bottomRight, 
figs=[_tetris1(), _tetris2(), _tetris3(), _tetris4(), _tetris5(), _tetris6()]);
       
public void tetris1() = render(tetris());

public void ftetris1(loc l) = writeFile(l, toHtmlString(
    grid(hgap=4, vgap = 4, id="aap", figArray=[[_tetris1(),  _tetris2()]])
));

// ---------------------------------------------------------------------------------------------------------------------------------------------------------------
public list[list[Figure]] figures() = 
[
             [demo1(), demo2()]
             ,[demo3() , demo4()]
             ,[demo5(), demo6()]
             , [demo7(), demo8()]
             ,[demo9(), demo10()]          
             ,[demo15(), demo13()]
             ,[demo14(),demo11()]
             ,[demo16(), demo17()]
             ,[demo18(), demo19()]
            ,[tetris(), box(fig=shrink(false), size=<400, 400>)]
            ,[decision(), triangle()]
            ];
            
Figure demoFig() = grid(vgap=4, figArray=figures());
            

                  
void demo() = render(demoFig(),
     width = 1800, height = 2000);
     
void fdemo(loc l) {
      writeFile(l, toHtmlString(demoFig(), debug = false, width = 800, height = 1800));
      }
      
 Figure sb(Figure tt) {return box(size=<20, 20>, fillColor = "antiquewhite", 
     tooltip = frame(atXY(30, 30, box(fig=tt, fillColor="whitesmoke", visibility="visible", lineWidth =1)))
 );}
 
 // list[Figure] sb(list[Figure] tt) {return  mapper(tt, sb);}
 
list[Figure] sb(list[Figure] tt) = [sb(f)|Figure f<-tt];
 
 Figure summary() = grid(figArray = [[sb(x)|x<-y]|y<-figures()]);
 
 void tsummary()= render(summary(), align = topLeft);
 
 void fsummary(loc l) {
      writeFile(l, toHtmlString(summary(),  align = topLeft));
      }
