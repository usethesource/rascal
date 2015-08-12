module experiments::vis2::sandbox::IDemo
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import Prelude;

str current = "yellow";

Figure butt() = hcat(figs= [
    button("Click me", id = "aap"
    , event = on("click", 
    void (str n, str e) {
       if (style("mies").fillColor=="green") {       
          style("mies", fillColor="red");
          }
       else 
          style("mies", fillColor="green");
       }
    )
    )
    , box(size=<50, 50>, id = "mies")  
    ]);
    
void tbutt()= render(butt(), debug = false);

void tfbutt(loc l)= writeFile(l, toHtmlString(butt(), debug = false));

Figure counter() = hcat(figs= [
    button("Incr", id = "aap"
    , event = on("click", 
    void (str n, str e) {   
       str t1 = textLabel("mies1").text;
       int d1 = isEmpty(t1)?0:toInt(t1);
       str t2 = textLabel("mies2").text;
       int d2 = isEmpty(t2)?0:toInt(t2);
       if (d1%2==0) style("box1", fillColor="red"); 
              else style("box1", fillColor="green");
       if (d2%2==0) style("box2", fillColor="green"); 
              else style("box2", fillColor="red");
       textLabel("mies1", text="<d1+1>");
       textLabel("mies2", text="<d2-1>");
       attr("box3", width = 25);
       }
    )
    )
    , button("Decr", id = "noot"
    , event = on("click", 
    void (str n, str e) {
       str t1 = textLabel("mies1").text;
       int d1 = isEmpty(t1)?0:toInt(t1);
       str t2 = textLabel("mies2").text;
       int d2 = isEmpty(t2)?0:toInt(t2);
       if (d1%2==0) style("box1", fillColor="red"); 
              else style("box1", fillColor="green");
       if (d2%2==0) style("box2", fillColor="green"); 
              else style("box2", fillColor="red");
       textLabel("mies1", text="<d1-1>");
       textLabel("mies2", text="<d2+1>");
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
   
Figure cell(str color) = vcat(figs=[button("", size=<120, 20>, fillColor=color
   , id = color, event= on( "click", 
        void(str e, str n) {
               current = n;
          }
        )
   ),
   text(color, fontSize=12, fontWeight="bold")]);

list[list[Figure]] colorArr() {
      int c = 6;
      int r = size(colors)/c;
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
    while (pt<size(colors)) {
        ts+=cell(colors[pt]);
        pt+=1;
        }
    res+=[ts];
    return res;
    }
    
Figure palette() = vcat(figs=[
    grid(figArray=colorArr(), vgap = 2, hgap = 2)
       ,hcat(figs=[    
         button("choose 1", id = "c1"
         ,event = on("click", void(str e, str n) {
                style("b1", fillColor = current);
                })
         )
         ,button("choose 2", id = "c2"
         , event = on("click", void (str e, str n) {
                style("b2", fillColor = current);})
         )])

, box(id="b1", size=<400, 150>, fig = circle(r=40, id="b2"))
]);

void tpalette() = render(palette());
