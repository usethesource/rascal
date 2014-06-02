module vis::web::examples::HelloWorld
import vis::web::markup::D3;
import util::HtmlDisplay;

public void mainW3() {
    htmlDisplay(publish(
          |file:///tmp/helloworld|
         ,CSS((h1_: (color_: "red"))) 
         , W3(h1_, "Hello World")));
 
   }
   
   
public void mainD3() {
    htmlDisplay(publish(
          |file:///tmp/helloworld|
          , [("name":"Hello"), ("name":"World")]      
          , CSS((h1_: (color_: "blue"))) 
         , JavaScriptJson("\"data.json\"", "error", "dat",
            <
            d3_ + D3(
            selectAll(h1_) o \data(<"dat">) o  enter o \append(h1_)
            o text(<["d"], <"return d.name">>))            
            >
          )
    ));
   }
   
public void mainSVG() {
    htmlDisplay(publish(
          |file:///tmp/helloworld|      
          , [("name":"Hello", "x":50, "y":50), ("name":"World", "x":150, "y":150)]      
          , ""
          , JavaScriptJson("\"data.json\"", "error", "dat",
           <"svg",
                "d3" + D3(
                 select(body_) o \append(svg_) o attr((
                              width_: <"400">, height_:<"400">
                              ))      
                 )
           > 
           ,
           <
           svg_ + D3(
            selectAll(rect_) o \data(<"dat">) o  enter o \append(text_) 
           o attr((stroke_:"green"
           , fill_:"none"
           , x_: <["d"], <"return d.x">>
           , y_: <["d"], <"return d.y">>))
           o text(<["d"], <"return d.name">>)  
           )           
          >
          )
    ));
   }
   
 public (void()) helloWorldW3 = mainW3;
 
 public (void()) helloWorldD3 = mainD3;
 
 public (void()) helloWorldSVG = mainSVG;
 
 