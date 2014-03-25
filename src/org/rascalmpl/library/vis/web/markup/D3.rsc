module vis::web::markup::D3
import Prelude;
import util::Math;
import lang::json::IO;
import util::HtmlDisplay;

alias Att =  map[str, str];

alias Css = map[str, Att];



public str toCss(Css css) {
   str r = "\<style\>\n";
   for (str x<-css) {
       r+="<x>{<for(str y<-css[x]){><y>:<css[x][y]>;<}>}\n";
       }
   r+="\</style\>\n";
   return r;
   }

public str Z(str tg, Att att, str txt) {
   return "\<<tg><for(str d<-att) {> <d>=\"<att[d]>\"<}>\>\n<txt>\</<tg>\>";
   }
   
public str Z(str tg, Att att) {
   return "\<<tg><for(str d<-att) {> <d>=\"<att[d]>\"<}>\>\</<tg>\>";
   }

public str Z(str tg, str txt) {
    return "\<<tg>\><txt>\</<tg>\>";
   }
   
public str html(str head, str body) {
	return "\<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"
            '  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"\>
            '  \<html xmlns=\"http://www.w3.org/1999/xhtml\"\>
            '
            '
            '\<header\><head>\</header\>\n\<body\><body>\</body\>\n\</html\>"
           ;                     
}



str JavaScriptString(str e) {
     return "<Z(script_, e+";")>";
}

str JavaScript(value e ...) {
     return JavaScriptString(toString(e));
}



/*
public str JavaScriptCsv(str file, str error, str dat, value e ...) {
     return JavaScript(call(d3_, csv("<file>", e)));
     }
     
public str JavaScriptTsv(str file, str error, str dat, value e ...) {
     return JavaScript(call(d3_, tsv("<file>", e)));
     }
*/

public str JavaScriptJson(str file, str err, str dat, value e ...) {
 str s = "function(<err>, <dat>) {
          ' <toString(e)> }\n";
     return JavaScriptString("d3"+C(json(file, s)));
     }
 
public loc publish(loc location, list[tuple[str name, list[tuple[num x, num y]] d]]  d, str header, str body) { 
     list[str] hd= [g.name|g<-d]; list[str] coord = ["x", "y"];
     // println(d);
     list[map[str path, /*list[map[str, value]]*/ value dt]] jsonData = [("name":name,
         "path":[(coord[i] : r[i]|i<-[0..2])+("kind":name)|
         tuple[num, num] r<-v])|<str name, list[tuple[num, num]] v> <-d]; 
     println(jsonData);   
      writeTextJSonFile(location+"data.json", jsonData); 
      writeFile(location+"index.html",  html(header, body));    
      return location;    
      }

str(str) f(int d) {
    str g(str r) = ".f(<d>)"+".g<r>";
    return g;
    }

str h(str s) = ".h<s>";

str h() = ".h";


/*
public void main() {
   println((d3 o f (4) o h ) ());
   }
*/  
public str d3(str s)=  "d3<s>";

value display(value v) {
    if (str e := v) {
          return e;
          }
    if (list[value] e:= v) {
          if (isEmpty(e)) return "[]";
          str r = "[<display(head(e))>";
          for (t<-tail(e)) r+=", <display(t)>";
          return r+"]";
          }
    if (map[str, value] e := v) {
          if (isEmpty(e)) return "{}";
          str key = getOneFrom(e);
          r = "{<key>:<display(e[key])>";
          e = delete(e, key);
          for (t<-e) r+=",<t>:<display(e[t])>";
          return r+"}";
          }
    if  (tuple[value g] d := v) {
          return display(d.g);
          } 
    if  (tuple[str n, value v] d := v) {
          return "var <d.n> = <display(d.v)>";
          } 
    // println("display:<v>");
    return v;
    }
              
str display(str key, map[str, value] m) {
      return "<for(k<-m) {>.<key>(\"<k>\",<display(m[k])>)<}>";    
      }
      

str toString(value js ...) {
           return "<for(e<-js){> <display(e)>;\n<}>";
    }
    

public str svg(str s) = ".svg()<s>";

public str enter(str s) = ".enter()<s>";


public  str(str) \append(str d) {
    str g(str r) = ".append(\"<d>\")"+r;
    return g;
    }

public str exit(str s) = ".exit()<s>";           

public str transition(str s) =".transition()<s>";

public str(str) \data(str d) {
    str g(str r) = ".data(<d>)"+r;
    return g;
    }                 
    
public str(str) attr(map[str, value] v) {
    str g(str r) = display("attr", v)+r;
    return g;
    }
    
    

public str(str) style(map[str, value] v) {
    str g(str r) = display("style", v)+r;
    return g;
    }

public str(str) duration(int  d) {
    str g(str r) = ".duration(<d>)"+r;
    return g;
    }
    
public str(str) selectAll(str  d) {
    str g(str r) = ".selectAll(\"<d>\")"+r;
    return g;
    }
    
public str(str) select(str  d) {
    str g(str r) = ".select(\"<d>\")"+r;
    return g;
    }

public str(str) scale(value v) {
    str g(str r) = ".scale(<display(v)>)"+r;
    return g;
    } 
        
public str(str) domain(value v) {
    str g(str r) = ".domain(<display(v)>)"+r;
    return g;
    } 

public str(str) range(value  v) {
    str g(str r) = ".range(<display(v)>)"+r;
    return g;
    } 

public str(str) orient(str s) {
    str g(str r) = ".orient(\"<s>\")"+r;
    return g;
    }   
         
public str linear(str s)  = ".linear()"+s;

public str axis(str s)  = ".axis()"+s;

public str line(str s)  = ".line()"+s;
public str line()  = ".line()";


public str(str) interpolate(value v) {
    str g(str r) = ".interpolate(<display(v)>)"+r;
    return g;
    }       

public str(str) x(value v) {
    str g(str r) = ".x(<display(v)>)"+r;
    return g;
    } 

public str(str) y(value v) {
    str g(str r) = ".y(<display(v)>)"+r;
    return g;
    } 

public str(str) ticks(value v) {
    str g(str r) = ".ticks(<display(v)>)"+r;
    return g;
    } 

public str(str) tickFormat(value v) {
    str g(str r) = ".tickFormat(<display(v)>)"+r;
    return g;
    } 
    
public str(str)  innerTickSize(value v) {
    str g(str r) = ".innerTickSize(<display(v)>)"+r;
    return g;
    } 

public str(str)  outerTickSize(value v) {
    str g(str r) = ".outerTickSize(<display(v)>)"+r;
    return g;
    } 
    
public str(str)  call(str script) {
    str g(str r) = ".call(<script>)"+r;
    return g;
    } 

public str(str) json(str file, value script) {
    str g(str r) = ".json(<file>, <display(script)>)"+r;
    return g;
    } 
    
public str(str) on(str event, value script) {
    str g(str r) = ".on(<event>, <display(script)>)"+r;
    return g;
    }                    
    /*                             
                 text(str l, str callback) : return r+ ".text(\"<l>\", <toString(callback)>)";           
                 csv(str l, str callback) : return r+ ".csv(\"<l>\", <toString(callback)>)";
                 csv(str l, str acc, str callback) : = ".csv(\"<l>\" , <toString(acc)>, <toString(callback)>)"; 
                 tsv(str l, str callback) : return r+ ".tsv(\"<l>\", <toString(callback)>)";
                 tsv(str l, str acc, str callback) : return r+ ".tsv(\"<l>\" , <toString(acc)>, <toString(callback)>)";
                 scale(str e): return r+ ".scale(<toString(e)>)";
                 scale(str e, Bundle c): {=".scale(<toString(e)>)";return r+ toString(c);}
                 linear(_): = ".scale.linear()";  
                 call(str f):  return r+".call(<toString(f)>)";              
                 csv_parse(str d, str acc) : return r+".csv.parse(<d>, <toString(acc)>)";
                 csv_parse(str d) : return r+".csv.parse(<d>)";  
                 json(str l, str callback) : return r+ ".json(\"<l>\", <toString(callback)>)";  
                 \filter(str e): return r+".filter(<toString(e)>)";
                 \filter(str e, B
   */
   
   str C(str(str x) f) {
       return f("");
       }
       
  str labx(int d) {
       switch(d) {
        case 0: return "0";
        case 1: return "\u03A0/6";
        case 2: return "\u03A0/3";
        case 3: return "\u03A0/2";
        case 4: return "2\u03A0/3";
        case 5: return "\u03A0/6";
        case 6: return "\u03A0";
        default: return "<d>";
        }
        return "";    
    }
    
 str laby(int d) {
       num r(num v) = round(v,0.01);
       return "<r((d-5.0)/5.0)>";
       }
 
 str labx(int d) {      
       switch(d) {
        case 0: return "0";
        case 1: return "\u03A0/6";
        case 2: return "\u03A0/3";
        case 3: return "\u03A0/2";
        case 4: return "2\u03A0/3";
        case 5: return "\u03A0/6";
        case 6: return "\u03A0";
        default: return "<d>";
        }
        return "";    
    }


alias PlotData = tuple[str title, list[str] xticks, list[str] yticks, list[tuple[str name, str color, list[tuple[num, num]] d]] fs] ;

public loc publish(loc location, PlotData  pd, str header, str body) { 
     list[tuple[str name, str color, list[tuple[num , num ]] d]] d = pd.fs;
     list[str] coord = ["x", "y"];
     list[str] tx  = pd.xticks;
     list[str] ty  = pd.yticks;
     tuple[str, list[str], list[str], list[map[str key, /*list[map[str, value]]*/ value dt]]] 
        jsonData = <pd.title , tx, ty, [("name":name, "color":cl,
         "path":[(coord[i] : r[i]|i<-[0..2])+("kind":name)|
         tuple[num, num] r<-v])|<str name, str cl, list[tuple[num, num]] v> <-d]>; 
     println(jsonData);   
      writeTextJSonFile(location+"data.json", jsonData); 
      writeFile(location+"index.html",  html(header, body));    
      return location;    
      }
   
   

  
 public void plot(PlotData p, int width = 800, int height = 400, int margin = 50) {  
   str header = Z(title_, p.title)+
      Z(script_,(src_: "http://d3js.org/d3.v3.min.js", charset_:"utf-8"))+
      toCss((
            ".tick line":(fill_:"none", stroke_:"antiquewhite"),
            ".line":(fill_:"none"),
            ".axis": ("font-size":"8pt","font-family":"sans-serif", fill_:"none"), 
            // ".tick":(fill_:"none", stroke_:"green"),
            ".frame":(stroke_:"black")
            // ,".grid":(stroke_:"red")
             ));
      str body = Z(h1_,  p.title) 
      
       + JavaScriptJson("\"data.json\"", "error", "dat",
        
         <"width", width>,
         <"height",height>,
         <"margin",margin> ,   
         <"svg",
                "d3" + C(
                 select(body_) o \append(svg_) o attr((
                              width_: "width", height_:"height"
                              ))
                 )
         >            
      ,
      < 
          "x_scale",
              "d3.scale"+ C(
                 linear o domain([0,"dat[1].length-1"]) o range(["margin", "width-margin"])
               )
      >       
       , 
      <
          "y_scale",
              "d3.scale"+ C(
                 linear o domain([0, "dat[2].length-1"]) o range(["height-margin", "margin"])
                 )
      >    
      , 
      <
         "x_axis", "d3.svg" + C(
                   axis o scale("x_scale") o ticks("dat[1].length")
                   o tickFormat("function(d){
                             return dat[1][d];}")
                     )
  
       >
       ,
       <"y_axis", "d3.svg"+C(
         axis o scale("y_scale") o orient("right") o ticks("dat[2].length")
         o tickFormat("function(d){
                             return dat[2][d];}")
         )
       >
       ,
       <
        "svg"+C(\append("defs") o \append("clipPath") o attr((id_:"\"clippath\""))
        o \append("rect") o attr((x_:"margin", y_:"margin", width_:"width-2*margin"
           , height_:"height-2*margin")))
       >
       ,
       <"svg"+C(
              \append(g_) o attr((
                 "class":"\"frame axis\"", 
                  // "transform": "\"translate(0, \" +height/2+ \")\""
                   "transform": "\"translate(0, \" +(height-margin)+ \")\""
              ))
            o  call("x_axis")
        )
        >
        ,
        <"svg"+C(\append(g_) o attr((
           "class":"\"frame axis\"", 
           "transform": "\"translate(\"+margin+\", 0)\""
           ))
            o  call("y_axis")
         )
       >
       ,
       <
       "line", "d3.svg"+C(
             line o interpolate("\"monotone\"") o 
                x("
                   function(d) {return x_scale(d.x);}
                 ")
             o
                y("
                  function(d) {return y_scale(d.y);}
               ")
             )
        > 
        ,
        
         <"svg"+ C(\append("g") o attr((transform_:"\"translate(0,\"+margin+\")\"")) o attr((
          "class":"\"grid\""   
          )) o \append("g") o call(
                 "x_axis"+C(innerTickSize("height-2*margin") o tickFormat("\"\"") o outerTickSize(0)                       
               )
               )
          )>  
         ,
         <"svg"+ C(\append("g") o attr((
           "class":"\"grid\"" ,
           transform_:"\"translate(\"+margin+\",0)\""
            )) o call(
             "y_axis"+ C(innerTickSize("width-2*margin")  o tickFormat("\"\"") o outerTickSize(0)
             )
             )   
          )>
          ,
          <"svg"+C(
           \append("g") o attr((style_: "\"clip-path:url(#clippath);\"")) o
           selectAll(".line") o \data("dat[3]") o  enter o \append("path") o
           attr((
              "class":"\"line\"", 
              "d": "function(q){return line(q.path);}",
               "stroke":"function(q){return q.color;}"
           ))
         )>    
              
        );
      
      htmlDisplay(publish(|project://chart/src/aap|, p, 
         header , body));
    }
 
 public str id_= "id";
public str class_= "class";
public str style_= "style"; 
public str onerror_="onerror";
public str onclick_="onclick"; 
public str onmousedown_="onmousedown"; 
public str onmouseout_="onmouseout"; 
public str onmouseover_="onmouseover"; 
public str onmouseup_="onmouseup"; 
public str onchange_="onchange"; 
public str onsubmit_="onsubmit"; 
public str onselect_="onselect";
public str onload_="onload";
public str href_="href";
public str target_="target";
public str cellpadding_="cellpadding";
public str cellspacing_="cellspacing";
public str border_="border";
     
public str fill_= "fill";
public str stroke_= "stroke"; 

public str stroke_width_= "stroke-width";
public str points_= "points";
public str d_= "d";
public str r_= "r"; 
public str cx_="cx";
public str cy_= "cy";
public str x_="x";
public str y_= "y";
public str width_="width";
public str  height_= "height";
public str  type_= "type";
public str   a_= "a";
public str   src_ = "src";
public str   body_= "body";
public str   head_= "head";
public str   title_= "title";
public str   meta_= "meta";
public str   h1_= "h1";
public str   h2_= "h2";
public str   h3_= "h3";
public str   h4_= "h4";
public str   img_= "img";
public str   p_= "p";
public str   br_= "br";
public str   i_= "i";
public str   b_= "b";
public str   tt_= "tt";
public str   div_= "div";
public str   span_= "span";
public str   pre_= "pre";
public str   table_= "table";
public str   tr_= "tr";
public str   td_= "td";
public str   th_= "th";
public str   thead_= "thead";
public str   foot_= "foot";
public str   tbody_= "tbody";
public str   tfoot_= "tfoot";
public str   caption_= "caption";
public str   iframe_= "iframe";
public str   samp_= "samp";
public str   em_= "em";
public str   strong_= "strong";
public str   small_= "small";
public str   code_= "code";
public str   kbd_= "kbd";
public str   var_= "var";
public str   li_= "li";
public str   ol_= "ol";
public str   ul_= "ul";
public str   dl_= "dl";
public str   dt_= "dt";
public str   dd_= "dd";
public str   sub_= "sub";
public str   sup_= "sup";
public str   button_= "button";
public str   form_= "form";
public str   fieldset_= "fieldset";
public str   label_= "label";
public str   input_= "input";
public str   svg_= "svg";
public str   circle_= "circle";
public str   ellipse_= "ellipse";
public str   rect_= "rect";
public str   line_= "line";
public str   polyline_= "polyline";
public str   polygon_= "polygon";
public str   text_= "text";
public str   path_= "path";
public str   transform_ = "transform";
public str   g_= "g";
public str   use_= "use";
public str   defs_= "defs";
public str   symbol_= "symbol";
public str   script_ = "script";
public str   charset_ = "charset";
public str   d3_ = "d3";
 
public void main() {
   
   num r(num v) = round(v,0.0004);
   
   int n= 100;
   tuple[str, str, list[tuple[num, num]]] dsin = <"sin", "red", [<(i*6.0/100), 5*r(sin(r(2*i*PI()/n)))+5>|i<-[0..n+1]]>;
   tuple[str, str, list[tuple[num, num]]] dcos = <"cos", "green", [<(i*6.0/100), 5*r(cos(r(2*i*PI()/n)))+5>|i<-[0..n+1]]>;
   
   list[tuple[str, str, list[tuple[num, num]]]] d = [dsin, dcos];

   PlotData p = <"Sin and Cos", [labx(i)|i<-[0..7]], [laby(i)|i<-[0..11]], d>;
   plot(p);
   /*
    list[list[tuple[num, num]]] d = [[<(i*step)*nTickx, (-y+f(x+(i*step)*width))*nTicky/height >| i<-[0..ub+1]]|f<-g];
    list[tuple[str, str, list[tuple[num, num]]]] w =  [<"<i>","blue", d[i]>|i<-[0..size(d)]];
    PlotData p = <"plot", xt, yt, w>;
    plot(p, width = 600, height = 600); 
    */ 
   }   
   