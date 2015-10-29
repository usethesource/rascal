module experiments::vis2::sandbox::IDemo
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import Prelude;

str current = "white";

Figure butt() = hcat(figs= [
    buttonInput("Click me", id = "aap"
    
    )
    , box(size=<50, 50>
        , fig=text("teun", id="teun"
        , fillColor = "yellow"      
        ) 
       , id = "mies"
      , event = on("click", 
    void (str n, str e, str v) {
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
    buttonInput("Incr", id = "aap"
    , event = on("click", 
    void (str e, str n, str v) {  
       str t1 = textProperty("mies1").text;
       int d1 = isEmpty(t1)?0:toInt(t1);
       str t2 = textProperty("mies2").text;
       int d2 = isEmpty(t2)?0:toInt(t2);
       if (d1%2==0) style("box1", fillColor="red"); 
              else style("box1", fillColor="green");
       if (d2%2==0) style("box2", fillColor="green"); 
              else style("box2", fillColor="red");
       textProperty("mies1", text="<d1+1>");
       textProperty("mies2", text="<d2-1>");
       attr("box3", width = 25);
       }
    )
    )
    , buttonInput("Decr", id = "noot"
    , event = on("click", 
    void (str e, str n, str v) {
       str t1 = textProperty("mies1").text;
       int d1 = isEmpty(t1)?0:toInt(t1);
       str t2 = textProperty("mies2").text;
       int d2 = isEmpty(t2)?0:toInt(t2);
       if (d1%2==0) style("box1", fillColor="red"); 
              else style("box1", fillColor="green");
       if (d2%2==0) style("box2", fillColor="green"); 
              else style("box2", fillColor="red");
       textProperty("mies1", text="<d1-1>");
       textProperty("mies2", text="<d2+1>");
       attr("box3", width=50);
       }
    )
    )
    , box(id = "box1", size=<50, 50>, fig=text("0", id = "mies1")) 
    , box(id = "box2", size=<50, 50>, fig=text("0", id = "mies2")) 
    , box(id = "box3", width=100, height = 50) 
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
         rangeInput(val=1.0, low = 0.0, high = 1.0, step= 0.1, 
         event=on("mouseup", void(str e, str n, real v)
            {
            // println(d);
            style("b1", fillOpacity = v);
            }
            )
          )
   ,
         rangeInput(val=100, 
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
                attr("aap", grow = v);
            }))
            , 
            box(size=<200, 200>, fillColor = "antiquewhite"
            , fig = 
                 // overlay(size=<100, 100>, lineWidth = 1, grow = 1.0, id="aap", figs=[
                   circle(fillColor= "red", r = 100,  grow= 1.0, align = centerMid, id = "aap"              
                   ,fig = box(fillColor="yellow", fig = text("Hello"), size=<50, 50>)
                   )
            //      ]
            //  )
            )
            ,
            buttonInput("push", event = on ("click", void(str e, str n, str v) {
                property("q", \value=2); 
                attr("aap", grow = 2);    
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

Figure string() = strInput(event=on( 
    void(str e, str n , str v) {
       println(v);
       }
     ));
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
