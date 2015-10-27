module experiments::vis2::sandbox::Demo
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import experiments::vis2::sandbox::Steden;
import experiments::vis2::sandbox::Nederland;
import experiments::vis2::sandbox::SinAndCos;
import experiments::vis2::sandbox::AEX;
import experiments::vis2::sandbox::Graph;
import experiments::vis2::sandbox::Flower;
import util::Math;
import Prelude;

void ex(str title, Figure b, bool debug = false) = render(b, debug = debug, align = centerMid);

// --------------------------------------------------------------------------

public Figure newNgon(str lc, Figure el) {
      return at(0, 0, ngon(n =7,  align = topLeft, lineColor= lc, 
             lineWidth = 20, fillColor = "white", padding=<0,0,0,0>, 
      fig = el));
      }

public Figure demo1() = (idNgon(50) |newNgon(e, it)| 
              e<-["antiquewhite", "yellow", "red","blue" ,"grey","magenta"]);
void tdemo1()  {render(rotate(30, demo1()), debug = false, align = centerMid);}

// --------------------------------------------------------------------------

public Figure newBox(str lc, Figure el) {
      return at(10, 10, box(align = topLeft, lineColor= lc, 
             fillColor = "white", fig = el), lineWidth = 20);
      }
public Figure demo2() = (
         rotate(0, 
           at(10, 10, box(size=<50, 200> , align = bottomRight, 
             lineColor="grey", fillColor = "yellow", lineOpacity=1.0))
          )
           |newBox(e, 
          it)| e<-["green", "red", "blue", "grey", "magenta", "brown"]);
void tdemo2(){ render(rotate(45, frame(fig=demo2())), align = centerRight, debug = false); }

 void tfdemo2(loc l) {
      // println(schoolPlot());
      writeFile(l, toHtmlString(rotate(45, demo2()), debug = false, width = 600, height = 600));
      }

// --------------------------------------------------------------------------


public Figure newEllipse(str lc, Figure el) {
      return at(0, 0, ellipse(align = topLeft, lineColor= lc, lineWidth = 19, 
           fillColor = "white", padding=<0,0,0,0>, 
      fig = el));
      }
public Figure demo3() = (idEllipse(100, 75) |newEllipse(e, 
      it)| e<-["red","blue" ,"grey","magenta", "brown", "green"]);
      
void tdemo3()  {render(demo3(), debug = false, align = topLeft);}
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
     
Figure tst0() = circle(padding=<30, 30, 30, 30>, lineWidth = 2, fillColor = "yellow", lineColor = "red", fig= box(fillColor = "lightblue",
        width = 100, height = 100, lineWidth = 4, lineColor = "blue"));
        
void tst() = render(tst0()); 
        
void tst(loc l) = writeFile(l, toHtmlString(hcat(figs=[shape(innerSchoolPlot1()+innerSchoolPlot2(), fillColor = "none",
        scaleX=<<0,10>,<0,400>>, scaleY=<<0,10>,<400,0>>, width = 400, height = 400, 
        lineColor = "blue")])));  
     
Figure schoolPlot() {
     return  overlay(lineWidth=1, width = 400, height = 400, figs = [
        shape(innerSchoolPlot1()+innerSchoolPlot2(), fillColor = "none",
        scaleX=<<0,10>,<0,400>>, scaleY=<<0,10>,<400,0>>, width = 400, height = 400, 
        lineColor = "blue"), 
        circle(r=40, cx = 200, cy = 200, fillColor = "yellow"
        ,lineWidth = 10, lineColor = "red", lineOpacity=0.5, fillOpacity=0.5, fig = text("Hello")
        )
        ,at(50, 50, circle(lineWidth=10, lineColor= "red", fillColor = "none",  padding=<10, 10, 10, 10>, fig= at(0,0, 
             box(width=50, height = 50, fillColor = "antiquewhite")
             )))
        ,at(250, 250, circle(lineWidth=10, fillColor="none", lineColor="brown", padding=<5, 5, 5, 5>
        , fig=ngon(n=7, r=40, lineWidth = 10, lineColor = "grey", fillColor = "none")))
        ])
        ;
     } 

Figure simpleGrid(Figure f) {
     return overlay(fillColor="none", width = 400, height = 400, figs=[
        // box(lineWidth=0, lineColor="black", fig=
          shape(innerGridV(10)+innerGridH(10) 
             ,size=<398, 398>, scaleX=<<0,1>,<0,400>>, scaleY=<<0,1>,<0,400>>, fillColor = "none",
              lineColor = "lightgrey")
       // , fillColor = "none")
        , f
        ]
     );
     }
     
Figure labeled(Figure g) {
     return hcat(lineWidth = 0, 
        figs = [
           vcat(figs=gridLabelY(), padding=<0,0,0,20>)
           , vcat(lineWidth = 0, figs = [box(fig=g, lineWidth=1), hcat(figs = gridLabelX())])
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
    
 Figure demo4() = labeledGrid(schoolPlot());
    
 void tfdemo4(loc l) {
      // println(schoolPlot());
      writeFile(l, toHtmlString(demo4(), debug = false, width = 600, height = 600));
      }
      
 void tdemo4() {
      // println(schoolPlot());
      render( 
      demo4(), width = 600, height = 600, align = topLeft, debug = false);
      }
   
      
Figure demoFig() = grid(figArray=[
            [demo1(), demo2()]
             , [demo3() , demo4()]
             , [demo5(), demo6()]
             , [demo7(), demo8()]
             ,[demo9(), demo10()]          
             ,[demo15(), demo13()]
             ,[demo14(), demo11()]
             ,[demo16(), demo17()]
             ,[demo18(), demo19()]
            ]);
                  
void demo() = render(demoFig(),
     width = 800, height = 1800);
     
 void fdemo(loc l) {
      // println(schoolPlot());
      writeFile(l, toHtmlString(demoFig(), debug = false, width = 800, height = 800));
      }


Figure butt() = hcat(figs= [
    button("Click me", id = "aap"
    , event = on("click", 
    void (str n, str e) {
       if (getStyle("mies").fillColor=="green")
          setStyle("mies", style(fillColor="red"));
       else 
          setStyle("mies", style(fillColor="green"));
       }
    ))
    , box(size=<50, 50>, id = "mies")  
    ]);
    
void tbutt()= render(butt(), debug = false);

void tfbutt(loc l)= writeFile(l, toHtmlString(butt(), debug = false));

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
								endMarker=box(size=<10,10>,fillColor="green")
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
           frame(align = topLeft,
             fig = ellipse(width=200, height = 100, fillColor = "red", fillOpacity = 0.7))
          ,frame(align = topRight,
             fig = ellipse(width=200, height = 100, fillColor = "green",fillOpacity = 0.7))
          ,frame(align = bottomMid,
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
     vcat(align = topLeft, size=<70, 50>, figs = [f, box(fillColor="antiqueWhite", height=15, width = 70, fig=text(s, fontSize = 9, width = 60, height = 10))]);


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
                     box( fillColor="#0000")
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
                     box(lineWidth = 0,fillColor="#0000")
                    ,box(lineWidth = 0,fillColor="#FAE042")
                    ,box(lineWidth = 0,fillColor="#ED2939")
                    ])); 
                    
Figure france() = title("France", hcat(lineWidth = 0, figs=[ 
                      box(fillColor="#002395")
                     ,box(fillColor="#FFF")
                     ,box(fillColor="#ED2939")
                    ]));  
                    
void tbelgium() = render(belgium(width = 201, height = 50));

void fbelgium(loc l) = writeFile(l, toHtmlString(belgium(width = 200, height = 50)));
                    
Figure flags() = grid(hgap=5, vgap = 5, figArray=[[dutch(), luxembourg(), german()],[italian(), belgium(), france()]] ,
                    width = 400, height = 200);  
 
list[tuple[str, Figure]] flagNodes = [<"nl", dutch()>, <"be", belgium()>
    , <"lu", luxembourg()>, <"de", german()>, <"fr", france()>, <"it", italian()>];
    
    list[Edge] flagEdges = [edge("nl", "be"), edge("nl", "de"), edge("be", "lu"),
                  edge("be", "fr"), edge("be", "de"), edge("fr", "it")];
                  
                 
Figure gflags() = graph(flagNodes, flagEdges, size=<300, 600>);

void tgflags() = render(gflags(), align =  centerMid);

void ftgflags(loc l) = writeFile(l, toHtmlString(gflags()));
   
Figure demo8() = flags(); 

Figure demo9() = steden(); 

Figure demo10() = sinAndCos();   

Figure demo11() = gflags(); 

Figure demo12() = nederland(7); 

Figure demo13() = fsm();                
                    
void tflags() = render(flags());           

void tsteden() = render(steden(width=800, height = 800));

void tnederland() = render(nederland(5, width=400, height = 200));

void tsincos() = render(sinAndCos());

void taex() = render(aex());

void tfsm() = render(fsm());

Figure klokBox(int r, int n) =  box(lineWidth = 0, fillOpacity=1.0, size=<2*n, 2*n>, fillColor= "none", align = topMid, fig=at(0, 0, box(size=<10, n>, rounded=<10, 10>, 
lineWidth = 1, fillColor="yellow")));

Figure klokFrame() {
     int r = 80; 
     int d = 2;  
     list[Figure] cs =
        [
          at(15+r+toInt(sin((PI()*2*i)/12)*r), toInt(12+r-cos((PI()*2*i)/12)*r), text("<i>"))|int i<-[12, 3, 6, 9]
          ]
         +circle(r=r-10, fillColor = "silver", lineColor = "red")
        +[at(17+r+toInt(sin((PI()*2*i)/12)*(r-10)), toInt(17+r-cos((PI()*2*i)/12)*(r-10)), circle(r=d, fillColor="black"))|int i<-[12, 3, 6, 9]
        ]
        +rotate(20, klokBox(r, 70))       
        +rotate(0, klokBox(r, 50))
        +circle(r=5, fillColor = "red")
        ;
     return box(fig=overlayBox(200, 200, cs), align = centerMid);
     }
     
Figure demo14() = klokFrame();

void tklok()= render(klokFrame());


void fklok(loc l) = writeFile(l, toHtmlString(klokFrame()));

Figure ovBox(str color) =  box(lineWidth = 0, fillOpacity=0.8, size=<100, 100>, fillColor= "none", align = centerMid, fig=box(size=<50, 10>, fillColor=color));

Figure ov() = overlay(figs = [
    rotate(45, ovBox("red"))
   ,rotate(135, ovBox("green"))
   ,rotate(0, ovBox("magenta"))
   ,rotate(90, ovBox("blue"))
    ]);

void tov()= render(ov());

Figure demo15()= ov();

list[Figure] rgbFigs = [box(fillColor="red",size=<50,100>), box(fillColor="green", size=<200,200>), box(fillColor="blue",  size=<10,10>)];

public Figure hcat11() = 
       box(padding=<0, 0, 0, 0>, lineWidth = 10, fillColor = "antiquewhite", lineColor = "blue"
       ,fig= ellipse(padding=<0, 0, 0, 0>
             ,fig=hcat(lineWidth=2, lineColor="brown", figs=rgbFigs) 
             // ,size=<100, 50>
       ,lineWidth = 10, lineColor= "yellow", fillColor="lightgrey",align = centerMid
       )
 )
;

void tfhcat11(loc l)= writeFile(l,
     toHtmlString(hcat11, debug = false));
void thcat11(){ render(hcat11(), debug = false);}

Figure demo16()= hcat11();


Figure bigger(num grow) = circle(r=30, fig = 
    box(size=<40, 10>, fig= rotate(-30, ngon(n=3, r=3, lineWidth=0, fillColor= "white")), 
        fillColor="antiquewhite", lineWidth = 2, lineColor="brown"),
    fillColor = "beige", lineColor = "green", grow = grow);

Figure big() = vcat(figs = [bigger(1.0), bigger(1.5), bigger(2)]);

Figure demo17() = big();

void tbig() = render(big());

Figure demo18() = flower();

void tflower() = render(flower());

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
