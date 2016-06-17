module experiments::vis2::sandbox::IDemo
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import experiments::vis2::sandbox::Steden;
import Prelude;
import util::Math;

str current = "white";

Figure ft() = hcat(figs=[box(size=<150, 150>), choiceInput(size=<30, 100>)]);

void tft() = render(ft());

Figure butt() = hcat(figs= [
    buttonInput("Click me", id = "aap"
    
    )
    , box(size=<50, 50>
        , fig=text("teun", id="teun"
        , fillColor = "yellow"      
        ) 
       , id = "mies"
      , event = on("click", 
    void (str e, str n, str v) {
       if (style("mies").fillColor=="green") {       
          style("mies", fillColor="red");
          //style("mies", visibility="hidden");
          //style("teun", visibility="hidden");
          }
       else {
          //style("mies", visibility="visible");
          //style("teun", visibility="visible");
          style("mies", fillColor="green");
          }
       }
    )
    )  
    ]);
    
void tbutt()= render(butt(), debug = false);

void tfbutt(loc l)= writeFile(l, toHtmlString(butt(), debug = false));

void click(str e, str n, str v) = println(n);

Figure lay() = tree(box(fig=buttonInput("click", size=<40, 40>, event=on(click))), [ box(size=<80, 80>, 
      fig = buttonInput("click", size=<40, 40>, event=on(click)))]);

void tlay() = render(lay());

Figure counter() = hcat(figs= [
    buttonInput("Incr", id = "aap", size=<50, 50>
    , event = on("click", 
    void (str e, str n, str v) {  
       str t1 = textProperty("mies1").plain;
       int d1 = isEmpty(t1)?0:toInt(t1);
       str t2 = textProperty("mies2").plain;
       int d2 = isEmpty(t2)?0:toInt(t2);
       if (d1%2==0) style("box1", fillColor="red"); 
              else style("box1", fillColor="green");
       if (d2%2==0) style("box2", fillColor="green"); 
             else style("box2", fillColor="red");
       textProperty("mies1", text="<d1+1>");
       textProperty("mies2", text="<d2-1>");
       // attr("box3", width = 25);
       }
    )
    )
    , buttonInput("Decr", id = "noot", size=<50, 50>
    , event = on("click", 
    void (str e, str n, str v) {
       str t1 = textProperty("mies1").plain;
       int d1 = isEmpty(t1)?0:toInt(t1);
       str t2 = textProperty("mies2").plain;
       int d2 = isEmpty(t2)?0:toInt(t2);
       if (d1%2==0) style("box1", fillColor="red"); 
              else style("box1", fillColor="green");
       if (d2%2==0) style("box2", fillColor="green"); 
              else style("box2", fillColor="red");
       textProperty("mies1", text="<d1-1>");
       textProperty("mies2", text="<d2+1>");
       // attr("box3", width=50);
       }
    )
    )
    , box(id = "box1", size=<50, 50>, fig=text("0", id = "mies1")) 
    , box(id = "box2", size=<50, 50>, fig=text("0", id = "mies2")) 
    // , box(id = "box3", width=100, height = 50) 
    ]);
    
void tcounter()= render(counter(), debug = false);

//Figure cell(str color) = vcat(figs=[box(size=<120, 20>, fillColor=color
//   , fig = button("",  id = color, event= on( "click", 
//        void(str e, str n) {
//               current = n;
//          }
//        )
//   )),
//   text(color, fontSize=12, fontWeight="bold")]);
   
Figure cell(str color) = vcat(figs=[buttonInput("", size=<120, 20>, fillColor=color
   , id = color, event= on( "click", 
        void(str e, str n, str v) {
               current = n;
          }
        )
   ),
   text(color, fontSize=12, fontWeight="bold")]);

list[list[Figure]] colorArr() {
      int n = size(colors);
      // int n = 1;
      int c = 6;
      int r = n/c;
      int pt = 0;
      list[list[Figure]] res = [];
      for (int i<-[0..r]) { 
          list[Figure] fs = [];
          for (int j<-[0..c]) {
              fs+= cell(colors[pt]);
              pt+=1;
          }
          res+=[fs];
      }
    list[Figure] ts = [];
    while (pt< n) {
        ts+=cell(colors[pt]);
        pt+=1;
        }
    res+=[ts];
    return res;
    }
    
Figure palette() = vcat(figs=[
    grid(figArray=colorArr(), vgap = 2, hgap = 2)
       ,hcat(figs=[    
         buttonInput("choose 1", id = "c1"
         ,event = on("click", void(str e, str n, str v) {
                style("b1", fillColor = current);
                })
         )
         ,buttonInput("choose 2", id = "c2"
         , event = on("click", void (str e, str n, str v) {
                style("b2", fillColor = current);})
         )             
         ])

, box(id="b1", size=<400, 150>, fig = circle(r=40, id="b2"))
 ,
         rangeInput(\value=1.0, low = 0.0, high = 1.0, step= 0.1, 
         event=on("mouseup", void(str e, str n, real v)
            {
            // println(d);
            style("b1", fillOpacity = v);
            }
            )
          )
   ,
         rangeInput(\value=1.0, low = 0.0, high = 1.0, step= 0.1,
         event=on("mouseup", void(str e, str n, real v)
            {
            style("b2", fillOpacity = v);
            }
            )
          )
]);

void tpalette() = render(palette());


Figure range() = vcat(
     figs= [rangeInput(id="q", low=0, high = 2, \value = 1, step = 0.01, event=on("change", void(str e, str n, real v)
            {
                attr("aap", bigger = v);
            }))
            , 
            box(size=<200, 200>, fillColor = "antiquewhite"
            , fig = 
                 // overlay(size=<100, 100>, lineWidth = 1, bigger = 1.0, id="aap", figs=[
                   circle(fillColor= "red", r = 100,  bigger= 1.0, align = centerMid, id = "aap"              
                   ,fig = box(fillColor="yellow", fig = text("Hello"), size=<50, 50>)
                   )
            //      ]
            //  )
            )
            ,
            buttonInput("push", event = on ("click", void(str e, str n, str v) {
                property("q", \value=2); 
                attr("aap", bigger = 2);    
               }))
            ]

)
;

void trange()= render(range());
void frange(loc l) = writeFile(l, toHtmlString(range()));


Figure choice() = hcat(figs=[
     choiceInput(id = "c1", choices = ["aap", "noot", "mies"], \value="noot", event = 
      on( void(str e, str n , str v) {println(property("c2"));}))
      ,choiceInput(id = "c2", choices = ["x", "y", "z"], event = 
      on( void(str e, str n , str v) {
         println(v);
         property("c1", \value = "mies");
      }))
      ]);
void tchoice()= render(choice());

Figure string() = vcat(figs=[strInput(event=on( 
    void(str e, str n , str v) {
       value w =  property(n).\value;
       if (str s:= w) {
           textProperty("aap", html = s);
           }
       }
     )),
     box(fig=text("aap", id = "aap"))
     ]
     );
void tstring()= render(string());

Figure checkbox() = hcat(figs= [
    checkboxInput(id="check", choices=["aap","noot", "mies"], \value=("noot":true)
    ,event = on(void(str e, str n , int v) {2;})
    )
    ,buttonInput("flip", event=
         on(
           void(str e, str n , str v) {
               value x = property("check").\value; 
               if (map[str, bool] y := x){
                  y["noot"]= !y["noot"];
                  property("check", \value = y);
               }
            }
         ))]);
     
void tcheckbox()= render(checkbox());

Figure button(int i, int j) = buttonInput("<i*3+j+1>", size=<50, 30>,
  event = on(update));
  
str buf = "";

str bufa = "";

void update(str e, str n, str v) {
    buf+=v;
    textProperty("display",text= buf);
    }

void clear(str e, str n, str v) {
    bufa = buf;
    buf=""; 
    clearTextProperty("display");
    }
    
void op(str e, str n, str v) {
    int d1 = toInt(bufa);
    int d2 = toInt(buf);
    switch (v) {
        case "+": buf = "<d1+d2>";
        case "-": buf = "<d1-d2>";
        case "*": buf = "<d1*d2>";
        case "/": buf = "<d1/d2>";
    }
    textProperty("display",text= buf);
    }

list[list[Figure]] buttonArr() {
      list[list[Figure]] res = [];
      for (int i<-[0..3]) { 
          list[Figure] fs = [];
          for (int j<-[0..3]) {
              fs+= button(i, j);
          }
          res+=[fs];
      }
    list[Figure] last1= [ 
        buttonInput("0", size=<50, 30>, event=on(update)),
        buttonInput("ac", size=<50, 30>, event=on(clear)),
        buttonInput("+", size=<50, 30>, event = on(op))];
     list[Figure] last2= [ 
        buttonInput("-", size=<50, 30>, event=on(op)),
        buttonInput("*", size=<50, 30>, event=on(op)),
        buttonInput("/", size=<50, 30>, event = on(op))];
    return res+=[last1, last2];
    }


Figure pocket() {
     return vcat(figs = [box(size=<150, 30>, fig = text("", id = "display", size=<140, 20> ))
         , grid(figArray = buttonArr())]);
     }
     
void tpocket() = render(pocket());

str currentColor = "blue";
bool animating = false;

Figure flipflop() {
     animating = false;
     return          
     hcat(figs=[
          box(size=<100, 50>, id = "animate", fillColor = "antiquewhite"
          , event = on(["message","click"], 
           void(str e, str n, str v) {
                if (e=="message") {
                if (currentColor  == "blue") {
                     style("animate", fillColor = "red");
                     currentColor = "red";
                     style("vis", visibility = "visible");
                     }
                else {
                     style("animate", fillColor = "blue");
                     currentColor = "blue";
                     style("vis", visibility = "hidden");
                     } 
                } 
                if (e=="click") {
                    if (animating==false) {
                    timer("animate", delay=500, command = "start");
                    animating = true;           
                    }
               else {
                   timer("animate", command = "finish");
                   animating = false;                
                   }    
               }       
           })
       )
       ,
       box(size=<80, 80>, fillColor = "antiqueWhite", fig = box(id = "vis", size = <60, 60> , fillColor="yellow",
            fig = text("Hallo")))
       ]);
       }
           
  void tflipflop() = render(flipflop());
  
  void fflipflop() = writeFile(|file:///ufs/bertl/html/u.html|, toHtmlString(flipflop()));
  
  str bg = "antiqueWhite";
   
  Figure tx(str id) = text("", id = id, size=<20, 20>, fontWeight="bold", fillColor = bg);
  
  str abc = toUpperCase("abcdefghijklmnopqrstuvwxyz");
  
  str currentString = "";
  
  int delay1 = 200;
  
  int delay2 = 500;
  
  str setCell(str s, bool empty) {
      str r = "";
      if (empty) clearTextProperty(s);
      else 
        {
         r = abc[arbInt(26)];
         textProperty(s, text = r);
         style(s, fillColor = bg);
         }
      return r;
      }
 
 void randomString() {  
      style("input", fillColor = "lightgrey");
      disable("input"); 
      currentString=setCell("A", false)+
                    setCell("B", false)+
                    setCell("C", false);
                  // wait
      timer("boxx", delay = delay1, command = "timeout"); 
      }
 
  Figure memory() {  
         currentString = ""; 
         return vcat(figs= [hcat(borderStyle="ridge",borderWidth=2, hgap = 5, id = "hcat"
                  , figs = [tx("A"), tx("B"), tx("C")]
                  , event = on("click", void(str e, str n, str v) {println("H");})
                )
               ,strInput(size=<100,20>, id = "input", \value=" ", event=on(
               void(str e, str n, str v) {
                  style("boxx", fillColor = (toUpperCase(v)==currentString?"green":"red"));
                  property("input", \value="input");
                  randomString();              
                  }
                ))
          ,
          box(size=<50, 50>, id = "boxx", fillColor = "yellow"
             , event = on("message", void(str e, str n, str v) {
                       if (isDisabled("input")) {
                          setCell("A", true);
                          setCell("B", true);
                          setCell("C", true); 
                          enable("input"); 
                          timer("boxx", delay=delay2, command = "timeout");
                          }
                        else {
                          clearValueProperty("input");           
                          style("input", fillColor = "white");
                          }
                          }
                       ))
          ,rangeInput(id= "p1", low= 200, high=  2000,  step = 100, 
              \value =  delay1,
              event = on("change", void(str e, str n, int v) {
                   delay1 = v;
                   })            
              )
          ,rangeInput(id= "p2", low= 200, high=  2000,  step = 100, 
              \value =  delay2,
              event = on("change", void(str e, str n, int v) {
                   delay2 = v;
                   })            
              )
          ]);
          }
  
  void tmemory() {render(memory(), event = on(void(str e, str n, str v){randomString();}));}
  
  void fmemory() = writeFile(|file:///ufs/bertl/html/u.html|, toHtmlString(memory()));
  
  
  num phi  = PI()/2;
 
  
  Figure ring() = circle(id="c", r=180, fillColor = "blue", event = on(["message", "click"],
      void(str e, str n, str v){
         if (e=="message") {
             int N = 100; 
             num x =  (100-20)*sin(phi); 
             int R = (100)+ toInt(x); 
             num d = 1-(sin(phi)+1)/2;
             attr("c", r = R); 
             style("c", fillOpacity=d);
             phi = phi + PI()/N;
             if (phi>=2*PI()) phi = 0;
             }
         else {println("stop");timer("c", delay=10, command = "finish");}
         }
      ));
  
  void tring() = render(ring()
      ,event = on(void(str e, str n, str v){timer("c", delay=10, command = "start");})
      );
  
  
Figure smallTree() = tree( box(size=<50, 50>, fig=text("A")), [text("B", size=<50, 50>, fontColor="red", fillColor = "white"), box(size=<50, 50>, fig=text("C"))]
, size=<500, 500>, ySep = 10);

void ttree() = render(smallTree(), size=<400, 400>);

public void ftree(loc l) = writeFile(l, toHtmlString(smallTree()));
       
       
Figure tip() {
        Figure r() = 
          frame(
          atXY(250, 250, 
            vcat(figs=[
             box(fillColor = "blue", fig=circle(bigger=1.5, fig = text("Hallo"), fillColor = "red"), size=<100, 100>)
            ,box(size=<20, 20>, fillColor="black")])
         )
       )
        ;
       Figure b = box(id="outer", bigger=2, fig=atXY(50, 50, box(id=  "inner", size=<200, 200>, fillColor = "none"
           ,tooltip  = // r()
              // steden(width=400, height = 400)
              // vcat(figs=[d3()])
              d3()
              )
              //vcat(figs=[box(size=<30, 30>, fillColor="red")
              //           ,box(size=<40, 30>, fillColor="blue")
              //          ])
              //)
              )
              ,
              fillColor="green")      
           ;
      
       return b;
       }
       
loc jsonl =  |project://rascal|+"src";

public Figure pack() {return d3Pack(d = fileMap(jsonl, ".rsc"), fillOpacityNode = 0.15, fillLeaf="lightsalmon", 
fillNode = "royalblue", diameter = 1000);}

public void tpack() = render(pack(), fillColor = "white");

public Figure treemap() {return d3Treemap(d = fileMap(jsonl, ".rsc"));}

public void ttreemap() = render(treemap(), fillColor = "white");

public void fpack(loc l) = writeFile(l, toHtmlString(pack()));
       
       
 void ttip() {render(tip(),align = centerMid);}
 
 public void ftip(loc l) = writeFile(l, toHtmlString(tip()));
 
 DDD ddd_ = ddd(name = "aap", width = 50, height = 50, children=[
        ddd(name="noot", width = 20, height = 20
        ,children=[ddd(name="mies", width=20, height = 20), ddd(name="teun", width=20, height = 20)]
      ), ddd(name="mies", width= 70, height = 200, children=[
            ddd(name="weide", width = 100, height = 20)
            ,ddd(name="schaap", width = 20, height = 20)
      ])]);
 
 public Figure tre() {return d3Tree(d = ddd_, fillColor="none", lineColor="black");}
 
 void ttre() {render(tre());}
 
 public void ftre(loc l) = writeFile(l, toHtmlString(tre()));
 
 Figure cellq(str s, int r = 15) = 
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
         box(fig=text("A", size=<25, 15>,fontWeight="bold" ), fillColor="salmon", lineWidth= 0, bigger = 2.5,  rounded= <25, 25>), [
           tree(cellq("B"), [
              tree(cellq("D"), [cellq("I")])
              ,tree(cellq("E"),
                 [cellq("J"), cellq("K"), cellq("L")])
               ])
            , tree(cellq("C", r = 25), [
               tree(cellq("F"), [cellq("O")])
              ,tree(cellq("G", r = 30), [cellq("M"), cellq("N")])
              ,tree(cellq("H"), [ cellq("P")])           
              ])
            ]
       );
   return r;         
   }
 
 public Figure d3() = 
     d3Tree(tree(box(size=<40, 40>, fillColor="yellow"), [circle(r=40, fillColor= "blue"
          ,tooltip = frame(atXY(100, 100, box(size=<50, 50>, fillColor="red"), lineWidth = 4))
          )])
        width = 300, height = 400 );  
     // d3Tree(wirth(), width=800, height = 800);
 
 void td3() {render(d3());}
 
 public void fd3(loc l) = writeFile(l, toHtmlString(d3()));
 
 

void main(){
    render(
    graph(size=<1000,1000>,
      nodes=[
      <"11",ellipse(fillColor="lightYellow",fig=text("closed",fontSize=14),bigger=2,id="myName1")>,
      <"10",ellipse(fillColor="lightYellow",fig=text("opened",fontSize=14),bigger=2,id="myName2")>
      ],
      edges=[
        edge("11","10",label="reset",id="myName3"),
        edge("11","10",label="open", id="myName4"),
        edge("10","10",label="reset",id="myName5"),
        edge("10","11",label="close",id="myName6")
      ]
      ,gap=<20,40>)
    );
}

Figure ov() = overlay(size=<200, 200>, figs=[atXY(10, 10, box(size=<150, 150>)), hcat(figs=[box(size=<50, 50>, lineWidth=4, fillColor="red")])]);

void tov() = render(ov());

/*
Figure ov() = hcat(figs=
    [overlay(figs=[box(size=<50, 50>, id = "red",   fillColor = "red"  ,event=on("click", void(str e, str n, str v){
                  style("blue", fillColor="red");
                  style("green", visibility = "visible");
                  }))
                  ,box(size=<50, 50>, id = "green", visibility="hidden", fillColor = "green",event=on("click", void(str e, str n, str v){
                       style("blue", fillColor="green");
                       style("green", visibility = "hidden");
                       }))
                 ]),
         box(size=<50, 50>, fillColor="blue", id = "blue")]);
  */
         
Figure rot() // = d3();
    = box(size=<20, 20>
     // , tooltip = overlay(figs=[circle(r=10), circle(r=20)])
      , tooltip = box(fig=smallTree())
    );

void trot() = render(rot());

public void frot(loc l) = writeFile(l, toHtmlString(rot()));



 
 
 
 
 
 
 
