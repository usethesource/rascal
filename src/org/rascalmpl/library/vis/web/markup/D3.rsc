@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module vis::web::markup::D3
import Prelude;
import util::Math;
import lang::json::IO;
import util::HtmlDisplay;

alias Att =  map[str, value];

alias Css = map[str, Att];

alias PlotData = tuple[str title, str name, list[str] xticks, list[str] yticks, list[tuple[str name, str color, str style, list[tuple[num, num]] d]] fs] ;



public str toCss(Css css) {
   str r = "\<style\>\n";
   for (str x<-css) {
       r+="<x>{<for(str y<-css[x]){><y>:<css[x][y]>;<}>}\n";
       }
   r+="\</style\>\n";
   return r;
   }
   
public str CSS(Css css) {
   return toCss(css);
   }
   
str translate(value v) {
   if (str e := v) return "\"<e>\"";
   return "<display(v)>";
   }

public str W3(str tg, Att att, str txt) {
   return "\<<tg><for(str d<-att) {> <d>=<translate(att[d])><}>\>\n<txt>\</<tg>\>";
   }
   
public str W3(str tg, Att att) {
   return "\<<tg><for(str d<-att) {> <d>=<translate(att[d])><}>\>\</<tg>\>";
   }

public str W3(str tg, str txt) {
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
     return "<W3(script_, e+";")>";
}

str JavaScript(value e ...) {
     return JavaScriptString(toStr(e));
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
     return JavaScriptString("d3"+D3(json(file, <[err, dat], e>)));
     }
     
public str JavaScriptCsv(str file, str err, str dat, value e ...) {
 str s = "function(<err>, <dat>) {
          ' <toStr(e)> }\n";
     return JavaScriptString("d3"+D3(csv(file, s)));
     }
     
public str JavaScriptTsv(str file, str err, str dat, value e ...) {
 str s = "function(<err>, <dat>) {
          ' <toStr(e)> }\n";
     return JavaScriptString("d3"+D3(tsv(file, s)));
     }
 
public loc publish(loc location, list[tuple[str name, list[tuple[num x, num y]] d]]  d, str header, str body) { 
     list[str] hd= [g.name|g<-d]; list[str] coord = ["x", "y"];
     // println(d);
     list[map[str path, /*list[map[str, value]]*/ value dt]] jsonData = [("name":name,
         "path":[(coord[i] : r[i]|i<-[0..2])+("kind":name)|
         tuple[num, num] r<-v])|<str name, list[tuple[num, num]] v> <-d]; 
      publish(location, jsonData,  header, body);  
      return location;    
      }
      
public loc publish(loc location, list[map[str name, value v]] jsonData, str header, str body) {    
      writeTextJSonFile(location+"data.json", jsonData);
      publish(location, header 
       + W3(script_,(src_: "http://d3js.org/d3.v3.min.js", charset_:"utf-8"))
      , body);     
      return location;    
      }

str(str) f(int d) {
    str g(str r) = ".f(<d>)"+".g<r>";
    return g;
    }

str h(str s) = ".h<s>";

str h() = ".h";

 
public str d3(str s)=  "d3<s>";

str row (list[value] e) {
     if (isEmpty(e)) return "";
     str r = "<_display(head(e))>";
     for (t<-tail(e)) r+=", <_display(t)>";
     return r;
     }

str row (list[str] e) {
     if (isEmpty(e)) return "";
     str r = "<display(head(e))>";
     for (str t<-tail(e)) r+=", <_display(t)>";
     return r;
     }

value display(value v) {
       if (str e := v) {
         return "\"<e>\"";
       }
       return _display(v);
     }
     
value _display(value v) {
    if (str e := v) {
          return e;
          }
    if (list[value] e:= v) {
          return "[<row(e)>]";
          }
    if (map[str, value] e := v) {   // Java Script Object
          if (isEmpty(e)) return "{}";
          str key = getOneFrom(e);
          r = "{<key>:<display(e[key])>";
          e = delete(e, key);
          for (t<-e) r+=",<t>:<_display(e[t])>";
          return r+"}";
          }
    if  (tuple[value g] d := v) {  // Java Script Statement
          return _display(d.g);
          } 
    if  (tuple[str varname, value v] d := v) {  // Java Script Variable
          if (contains(d.varname,".")) return "<d.varname> = <_display(d.v)>";
          return "var <d.varname> = <_display(d.v)>";
          } 
    if  (tuple[bool defined, value condition, value v] d := v) {  // Java Script Variable
          // if (contains(d.varname,".")) return "<d.varname> = <_display(d.v)>";
          return d.defined ? "if (<_display(condition)>) 
              {<JS(d.v)>}" 
          :
          "if ( ! (<_display(condition)>)) 
              {<JS(d.v)>}"
          ;
          } 
    if  (tuple[list[str] parameters, value body] d := v) {  // Java Script Function
          return "function(<row(d.parameters)>) {<toStr(d.body)>}";
          } 
    if  (tuple[str fname, list[str] parameters, value body] d := v) {  // Java Script Function
          return "function <d.fname>(<row(d.parameters)>) {<toStr(d.body)>}";
          } 
    // println("display:<v>");
    return v;
    }
              
str display(str key, map[str, value] m) {
      return "<for(k<-m) {>.<key>(\"<k>\",<display(m[k])>)<}>";    
      }
      

str toStr(value js ...) {
           return "<for(e<-js){> <display(e)>;\n<}>";
    }

public str JS(value statement ...) = toStr(statement);

str toStr(list[value] js) {
           return "<for(e<-js){> <display(e)>;\n<}>";
    }   

public str svg(str s) = ".svg()<s>";

public str enter(str s) = ".enter()<s>";

public str symbol(str s) = ".symbol()<s>";

public str category10(str s) = ".category10()<s>";

public str category20(str s) = ".category20()<s>";

public  str(str) \append(str d) {
    str g(str r) = ".append(\"<d>\")"+r;
    return g;
    }

public str \exit(str s) = ".exit()<s>";  

public str _remove(str s) = ".remove()<s>";            

public str transition(str s) =".transition()<s>";

public str(str) \data(value d) {
    str g(str r) = ".data(<display(d)>)"+r;
    return g;
    }                 
    
public str(str) attr(map[str, value] v) {
    str g(str r) = display("attr", v)+r;
    return g;
    }
    
public str(str)  text(value v) {
    str g(str r) = ".text(<display(v)>)"+r;
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
    
public str(str) size(int  d) {
    str g(str r) = ".size(<d>)"+r;
    return g;
    }
    
public str(str) selectAll(value  d) {
    str g(str r) = ".selectAll(<display(d)>)"+r;
    return g;
    }
    
public str(str) select(value  d) {
    str g(str r) = ".select(<display(d)>)"+r;
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
// public str line()  = ".line()";


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
    
public str(str)  call(value script) {
    str g(str r) = ".call(<display(script)>)"+r;
    return g;
    }
    
public str(str)  each(value script) {
    str g(str r) = ".each(<display(script)>)"+r;
    return g;
    } 
    
public str(str)  \filter(value script) {
    str g(str r) = ".filter(<display(script)>)"+r;
    return g;
    } 

public str(str) json(str file, value script) {
    str g(str r) = ".json(<file>, <display(script)>)"+r;
    return g;
    }
    
public str(str) tsv(str file, value script) {
    str g(str r) = ".tsv(<file>, <display(script)>)"+r;
    return g;
    } 
    
public str(str) csv(str file, value script) {
    str g(str r) = ".csv(<file>, <display(script)>)"+r;
    return g;
    }  
    
public str(str) on(str event, value script) {
    str g(str r) = ".on(\'<event>\', <display(script)>)"+r;
    return g;
    }                    
    
   
   str D3(str(str x) f) {
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

public loc publish(loc location, PlotData  pd, str header, str body) { 
     list[tuple[str name, str color, str style, list[tuple[num , num ]] d]] d = pd.fs;
     list[str] coord = ["x", "y"];
     list[str] tx  = pd.xticks;
     list[str] ty  = pd.yticks;
     tuple[str, list[str], list[str], list[map[str key, /*list[map[str, value]]*/ value dt]]] 
        jsonData = <pd.title , tx, ty, [("name":name, "color":cl, "style":st,
         "path":[(coord[i] : r[i]|i<-[0..2])+("kind":name)|
         tuple[num, num] r<-v])|<str name, str cl, str st, list[tuple[num, num]] v> <-d]>;  
      writeTextJSonFile(location+"data.json", jsonData);   
      publish(location, header, body);  
      return location;    
      }
      
public loc publish(loc location, str header, str body) {     
     writeFile(location+"index.html",  html(header, body));    
     return location;    
     }
      
 public loc publish(loc location, PlotData  pd, str body
   ) {  
     list[tuple[str name, str color, str style, list[tuple[num , num ]] d]] 
     d = pd.fs;
     list[str] names = [e.name|e<-d];
     str checkButtons = buttons("Points", pd.name, "markerButton", <["all"]+names,  ["points", "lines", "splines"]>);
     str header0 =toCss((
            "td":
            (
             "vertical-align":"top"
            //  ,"border-collapse":"collapse"     
            // ,"border":"1px solid green"
             )  
           ,"table":
            ("border-collapse":"collapse"
           //  , "border":"1px solid green"
            )      
             ));    
      writeFile(location+"index.html", html(header0, 
      W3(table_, W3(tr_, W3(td_,
         body
      )
      + W3(td_, checkButtons)
      ) 
      )));   
      return location;    
      }
      
str scatterFunction(PlotData p, int symbolSize, str style) {
      str head =  "svg"+D3(\append("g") o attr((style_: "clip-path:url(#clippath);")) o
           selectAll(".points") o \data(<"dat[3]">) o  enter o \append("g"));
      str points = head+D3(\filter(
      <["d"], [<"return force || 
      (sessionStorage.getItem(\'points\')?
            sessionStorage.getItem(\'points\').indexOf(\':<p.name>_\'+d.name+\':\') \>= 0
            :
      (d.style == \"points\" <style=="points"? "|| d.style==\"default\"":"">))">]>)
      o
       \filter(<["d"],[  
                      < "return choice==\"<p.name>_all\" || choice == \'<p.name>_\'+d.name">
                      ]>)
      );
      return JS(<"scatterFunction", ["choice", "force"], < 
           points+D3(
           attr(("fill":<["q"],[
            <"return q.color==\"none\"?colormap(q.name):q.color">
            ]>
            ,
            class_: <["q"], [    
              <"return \"<p.name> \"+ \"points \"+q.name">
              ]>
            )) 
           o selectAll("g.point") o \data(<["d", "i"], [<"return d.path">]>) o  enter o \append("path") o
           attr(( 
              transform_: <["d", "i"], [<"return \"translate(\"+x_scale(d.x) +\",\"+y_scale(d.y) + \")\"">]>,
               //"d": "function(d, i, j) {return d3.svg.symbol()();}"
               "d": <"d3.svg"+D3(symbol o size(symbolSize))>
           ))o \append("title") o text(<["d", "i"],  [<"return d.name">]>)         
           )>>);
      }

 
 
 str lineFunction(PlotData p,str style) {  
    str head =  "svg"+D3(\append("g") o attr((style_: "clip-path:url(#clippath);")) o
           selectAll(".lines") o \data(<"dat[3]">) o  enter o \append("path"));
    str lines = head+D3(\filter(
       <["d"], [<"
            return force || 
            (sessionStorage.getItem(\'lines\')?
            sessionStorage.getItem(\'lines\').indexOf(\':<p.name>_\'+d.name+\':\') \>= 0
            :
            (d.style == \"lines\" <style=="lines"? "|| d.style==\"default\"":""> ))
            ">]>) 
            o \filter(<["d"],[  
                      < "return choice==\"<p.name>_all\" || choice == \'<p.name>_\'	+d.name">
                      ]>)
          );
    return JS(<"lineFunction", ["choice", "force"], <
           lines+D3(
           attr((
              class_: <["q"], [         
                   <"return \"<p.name> \"+\"lines \"+q.name">]>,
              "d": <["q"], [<"return lineLinear(q.path)">]>,    
              "stroke":<["q"], [<"return q.color==\"none\"?colormap(q.name):q.color">]>
              
              
           ) )  
           o \append("title") o text(<["d", "i"], [<"return d.name">]>)
           
         )>>) ;
    }
    
str splineFunction(PlotData p, str style) {  
    str head =  "svg"+D3(\append("g") o attr((style_: "clip-path:url(#clippath);")) o
           selectAll(".splines") o \data(<"dat[3]">) o  enter o \append("path"));
    str lines = head+D3(\filter(
       <["d"], [
            //  <"alert(sessionStorage.getItem(\'splines\'))">,        
             <
            "return force || 
            (sessionStorage.getItem(\'splines\')?
            sessionStorage.getItem(\'splines\').indexOf(\':<p.name>_\'+d.name+\':\') \>= 0
            :
            (d.style == \"splines\" <style=="splines"? "|| d.style==\"default\"":""> ))
            ">]>)
             o \filter(<["d"],[  
                      < "return choice==\"<p.name>_all\" || choice == \'<p.name>_\'+d.name">
                      ]>)
       );
    return JS(<"splineFunction", ["choice", "force"], [
          <lines+D3(
           attr((
              class_: <["q"], [
               <"return \"<p.name> \"+\"splines \"+q.name">]>,
              "d": <["q"], [
                 <"return lineMonotone(q.path)">]>,
              "stroke":<["q"], [<"return q.color==\"none\"?colormap(q.name):q.color">]>
           ) )  o \append("title") o text(<["d", "i"], [<"return d.name">]>)
          
         )
          >]>) ;
    }
    
public str buttons(str title, str tagName, str class, tuple[list[str] names,  list[str] vs] t
   ) {
   str checkBox = CheckBox(title, tagName, class, t);
   str resetButton = ResetButton(tagName);
   str textButton =  labelNames();
   return W3(table_,
      W3(tr_, W3(td_, textButton))+
      W3(tr_, W3(td_, checkBox))+
      W3(tr_, W3(td_, resetButton))
      );
   }
   
str CheckBox(str title, str tagName, str class, tuple[list[str] names,  list[str lab] vs] t) {
   str s = "";
   for (str name<-t.names) s+=W3(tr_, W3(td_, W3(b_, name))+  
   "<for(str v<-t.vs){>
      <W3(td_,  (id_: "<v>"), W3(label_, ("for":"<v>"),  v)
      +
      W3(input_
    , ("type":"checkbox", "name": "<tagName>_<name>",  class_: "<class> <v> <tagName>_<name>",
    "value":v, 
     "onclick":
       JS(
         <"window.frames[\'<tagName>\'].dotting(this)">
     ) 
   ) 
        )) 
      >
   <}>");
   return W3(h4_, title)+W3(table_, s
   );     
   }

str ResetButton(str tagName) {
   return W3(input_, ("type":"button", "value":"reset",
      "onclick":
       JS(
         <"window.frames[\'<tagName>\'].resetting(this)">
     ))); 
   }
     
/*   
public str radioButton(str title, str class, tuple[list[str] names,  list[tuple[value val, str lab]] vs] t,
   bool mark) {
   str s = "";
   for (str name<-t.names) s+=W3(tr_, W3(td_, W3(b_, name))+  
   "<for(v<-t.vs){>
      <W3(td_, (id_: "<v.val>"), W3(label_, ("for":"<v.val>"),  v.lab)
      +
      W3(input_
    , ("type":"radio", "name": "<name>",  class_: "<class> <name>", "value" :v.val
     , "onclick":
       JS(
         <"window.frames[0].dotting(this)">
     ,   <"x", "document.getElementsByClassName(\'<class>\')">
     ,   <"if( this.name==\'all\'){for (var i = 0;i\<x.length;i++) {if (x[i].value==this.value) x[i].checked = true;}} ">
     ) 
   ) 
        )) 
      >
   <}>");
   return W3(h4_, title)+W3(table_, s
   );     
   }
*/
public str labelNames() {
   str xl = W3(tr_, W3(td_, W3(input_, ("type":"text", id_: "xlabel", "placeholder":"x axis", "onkeydown":JS(
     <"if (event.keyCode==13) window.frames[0].labelingX(this)">)))));
    str yl = W3(tr_, W3(td_, W3(input_, ("type":"text", id_: "ylabel", "placeholder":"y axis", "onkeydown":JS(
     <"if (event.keyCode==13) window.frames[0].labelingY(this)">)))));
   return W3(h4_, "axe labels")+W3(table_, xl+yl);
   }
   
public str plotEl(loc location, 
   PlotData pd, int width = 800, int height = 400, int margin = 60,
   str style = "splines", int symbolSize = 32, bool mark = false, str xLabel = "x",
   str yLabel = "y", num factor = 1.0, bool remember = false,
   bool x_axis = true, bool y_axis = true
   ) {
 // -----------------------------------
   list[tuple[str name, str color, str style, list[tuple[num , num ]] d]] 
   d = pd.fs;
   list[str] coord = ["x", "y"];
   list[str] tx  = pd.xticks;
   list[str] ty  = pd.yticks;
   tuple[str, list[str], list[str], list[map[str key,  value dt]]] 
        jsonData = <pd.title , tx, ty, [("name":name, "color":cl, "style":st,
         "path":[(coord[i] : r[i]|i<-[0..2])+("kind":name)|
         tuple[num, num] r<-v])|<str name, str cl, str st, list[tuple[num, num]] v> <-d]>; 
     // println(jsonData);
   writeTextJSonFile(location+"<pd.name>.json", jsonData); 
// ------------------------------------
   tuple[str header, str body, PlotData dat] r =
       plotEl(pd
        , width = width, height = height     
        , style=style, symbolSize=symbolSize
        , xLabel = xLabel, yLabel = yLabel, margin =margin
        , remember = remember, x_axis = x_axis, y_axis = y_axis
        ); 
    writeFile(location+"<pd.name>.html",  html(r.header+
         W3(script_,(src_: "http://d3js.org/d3.v3.min.js", charset_:"utf-8")), r.body));             
    return W3(iframe_,
           (src_:"<pd.name>.html"
            , "scrolling":"no"
           , id_:"<pd.name>", width_:width+10, height_:height+(x_axis?height/5:height/5))
      );
     
   }
   
 public tuple[str header, str body, PlotData dat] plotEl(
   PlotData p, int width = 800, int height = 400, int margin = 60,
   str style = "splines", int symbolSize = 32, bool mark = false, str xLabel = "x",
   str yLabel = "y", bool remember = false, bool x_axis = true,
   bool y_axis = true
   ) {
   str header = W3(title_, p.title)+
      // W3(script_,(src_: "http://d3js.org/d3.v3.min.js", charset_:"utf-8"))+
      toCss((
            ".grid line":(fill_:"none", stroke_:"lightgrey"/*"antiquewhite"*/, "opacity":"0.7"),
            ".axis line":(fill_:"green"),
            ".axis": ("font-size":"8pt","font-family":"sans-serif", // fill_:"none",
            "stroke-width":"0",stroke_:"black") 
            ,".axis path":(fill_:"none")
            , ".tick":(fill_:"black")
            , ".lines":(fill_:"none")
            , ".line":(fill_:"none")       
            ,".splines":(fill_:"none")
           // , ".dots":("stroke-width":"0", stroke_:"none", fill_:"black")
            , ".tx":("fill":"black","stroke":"none", "font-family":"sans-serif", "font-size":"10pt", "stroke-width":"1"
            , "text-anchor":"middle")
           // , "td":("border-style":"solid", "padding":"4px", "border-width":"1px", "border-color":"light-grey"
           //      ,  "vertical-align":"top") 
            , "table":(
                 "border-collapse":"collapse"
               // , "border":"1px solid red"
               )
            , ".label": ("font-size":"10pt","font-family":"sans-serif", "font-style":"italic")
            , "h1": ("font-size":"1em")
             ));
      str body =  W3(h1_,  p.title)  +
      
      
       JavaScriptJson("\"<p.name>.json\"", "error", "dat",
        
         <"width", width>,
         <"height",height>,
         <"margin",margin> ,   
         <"svg",
                "d3" + D3(
                 select(body_) o \append(svg_) o attr((
                              width_: <"width">, height_:<"height">
                              ))
                 )
         > 
      ,
      <"names",  "new Array()">          
      ,
    <
     "for (var i=0; i\<dat[3].length;i++) {names[i] = dat[3][i].name;}"
    >
      ,
      <"colormap","d3.scale" + D3(category10 o domain(<"names">))>
      ,
      <"choice", "\"<p.name>_all\"">
      ,
     (!remember)?<"clean()">:""
      ,
      < 
          "x_scale",
              "d3.scale"+ D3(
                 linear o domain([0,<"dat[1].length-1">]) o range(<["margin", "width-margin"]>)
               )
      >       
       , 
      <
          "y_scale",
              "d3.scale"+ D3(
                 linear o domain([0, <"dat[2].length-1">]) o range(<["height-margin", "margin"]>)
                 )
      >    
      , 
      <
         "x_axis", "d3.svg" + D3(
                   axis o scale(<"x_scale">) o ticks(<"dat[1].length">)
                   o tickFormat(<["d"], [<"return dat[1][d]">]>)
                      )
  
       >
       ,
       <"y_axis", "d3.svg"+D3(
         axis o scale(<"y_scale">) o orient("left") o ticks(<"dat[2].length">)
         o tickFormat(<["d"],[<"return dat[2][d]">]>
                    )
         )
       >
       ,
       <
        "svg"+D3(\append("defs") o \append("clipPath") o attr((id_:"clippath"))
        o \append("rect") o attr((x_:<"margin">, y_:<"margin">, width_:<"width-2*margin">
           , height_:<"height-2*margin">)))
       >
       //,
       //<"qline", "d3.svg"+D3(line o
       //        x(<["d"], [<"return x_scale(d.x)">]>)
       //      o y(<["d"], [<"return y_scale(d.y)">]>)
       //     ) >
       ,   
       <
       "lineMonotone", "d3.svg.line()"+D3(
             interpolate("monotone") o 
               x(<["d"], [<"return x_scale(d.x)">]>)
             o y(<["d"], [<"return y_scale(d.y)">]>)
             )
        >
        ,      
        <
       "lineLinear", "d3.svg.line()"+D3(
             interpolate("linear")  o 
               x(<["d"], [<"return x_scale(d.x)">]>)
             o y(<["d"], [<"return y_scale(d.y)">]>)           
             ) 
        >
        
        /* */
        ,     
       x_axis?<"svg"+D3(
              \append(g_) o attr((
                   class_:"axis", 
                   transform_: <"\"translate(0, \" +(height-margin)+ \")\"">
              ))
            o  call(<"x_axis">)                  
        )
        >:<"">
        ,
        y_axis?<"svg"+D3(\append(g_) o attr((
           class_:"axis", 
           transform_: <"\"translate(\"+margin+\", 0)\"">
           ))
            o  call(<"y_axis">)
         )
       > :<"">          
        ,       
         <"svg"+ D3(\append("g") o attr((transform_:<"\"translate(0,\"+margin+\")\"">)) o attr((
          class_:"grid"   
          )) o \append("g") o call(
                 <"x_axis"+D3(innerTickSize(<"height-2*margin">) o tickFormat("") o outerTickSize(0)                       
               )>
               )
          )>  
         ,
         <"svg"+ D3(\append("g") o attr((
           class_:"grid" ,
           transform_:<"\"translate(\"+margin+\",0)\"">
            )) o call(
             <"y_axis"+ D3(innerTickSize(<"-width+2*margin">)  o tickFormat("") o outerTickSize(0)
             )>
             )   
          )>
          ,
       <"xLabel",   <"\"<xLabel>\"">>
       ,
       <"yLabel",   <"\"<yLabel>\"">>
       ,
       <"getLabel",["s", "v"], [<"return sessionStorage.getItem(s)?sessionStorage.getItem(s):v">]>
       ,
      x_axis? <
       "svg"+D3(\append(text_) o attr((class_: "labelX label", text_anchor_:"end",
           x_:width/2, y_: height-20)) o text(<"getLabel(\"xLabel\", xLabel)">))
       > :<"">
       ,
       y_axis? <
       "svg"+D3(\append(text_) o attr((class_: "labelY label", text_anchor_:"end",
           y_: 0, x_:-height/2, "dy":".75em", transform_:"rotate(-90)")) o text(<"getLabel(\"yLabel\", yLabel)">))
       >  :<""> 
          /* */
          ,
           <scatterFunction(p, symbolSize, style)>
           , 
           <"scatterFunction(\"<p.name>_all\", false)">
           ,
           <lineFunction(p, style)>
           ,
           <"lineFunction(\"<p.name>_all\", false)">
           ,
           <splineFunction(p, style)>
           ,
           <"splineFunction(\"<p.name>_all\", false)">
           ,
          <"hasClass",["element", "cls"], [
          <"return (\' \' + element.className + \' \').indexOf(\' \' + cls + \' \') \> -1;">
          ]>
          ,
          <"getClass", ["e"], [
             <"if (hasClass(e,\'points\')) return \'points\'">
             ,<"if (hasClass(e,\'lines\')) return \'lines\'">
             ,<"if (hasClass(e,\'splines\')) return \'splines\'">
             ,<"return \'\'">
          ]>
          ,
          <"plotFunction",["e", "n"], <"switch(e) {
             case \"lines\": lineFunction(n, true); break;
             case \"splines\": splineFunction(n, true); break;
             case \"points\":  scatterFunction(n, true); break;
             case \"default\":  scatterFunction(n, false); 
                                splineFunction(n, false);
                                lineFunction(n, false);
                                break;
             default: return \"\";
             }">>
             ,
             <"selectDisplayedGraphs",[], [
                     <"x", "window.parent.document.getElementsByClassName(\'markerButton\')"> 
                     ,              
                     <"lines", "\":\"">
                     ,
                     <"splines", "\":\"">
                     ,
                     <"points",  "\":\"">
                     ,
                              
                     <"for (var i = 0;i\<x.length;i++) {if(x[i].name!=\'<p.name>_all\') x[i].checked=false;}">
                     ,
                      <"svg"+D3(selectAll(".splines")
                      o
                      \each(<["d", "i"],[ 
                      <"window.parent.document.getElementsByClassName(\'splines <p.name>_\' +d.name)[0].checked = true">
                      ,
                      <"splines=splines.concat(\':<p.name>_\', d.name, \':\')">
                      ]>)
                      )> 
                       ,
                    <"svg"+D3(selectAll(".lines")
                      o
                      \each(<["d", "i"],[ 
                      <"window.parent.document.getElementsByClassName(\'lines <p.name>_\' +d.name)[0].checked = true">
                      ,
                      <"lines=lines.concat(\':<p.name>_\',d.name, \':\')">
                      ]>)
                      )> 
                       ,
                    <"svg"+D3(selectAll(".points")
                      o
                      \each(<["d", "i"],[ 
                      <"window.parent.document.getElementsByClassName(\'points <p.name>_\' +d.name)[0].checked = true">
                      ,
                      <"points=points.concat(\':<p.name>_\',d.name, \':\')">
                      ]>)
                      )>
            //  , <"alert(splines[1])">
             , <"sessionStorage.setItem(\'lines\',lines)"> 
             , <"sessionStorage.setItem(\'splines\',splines)"> 
             , <"sessionStorage.setItem(\'points\',points)">
             ]> 
          ,
          <"window.dotting", <["e"], [
                     <"choice","e.name">
                     ,
                     <"if ( ! e.checked ) {       
                      svg"+D3(selectAll(<"\".\"+getClass(e)">)     // <"\"\"+getClass(e)+\"\"">)  
                      o 
                      \filter(<["d"],[  
                      < "return choice==\"<p.name>_all\" || choice == \'<p.name>_\'+d.name">
                      ]>)
                      o \data([])
                      o \exit o _remove )+"}"
                      >
                    ,
                     <"if (e.checked || e.value == \"default\") { 
                         plotFunction(e.value, choice); 
                         }   
                     ">
                     ,
                     
                    
                    <"selectDisplayedGraphs()">                     
                   ] >>
                ,
                <"clean", [], [
                  <"sessionStorage.removeItem(\'splines\')">
                   ,
                   <"sessionStorage.removeItem(\'lines\')">
                   ,
                   <"sessionStorage.removeItem(\'points\')">
                ] >
                ,
                <"window.resetting", <["e"], [
                   <"clean()">
                   ,
                   <"svg"+D3(selectAll(".<p.name>")     // <"\"\"+getClass(e)+\"\"">)  
                      o \data([])
                      o \exit o _remove )      
                     >
                , <"plotFunction(\'default\', \'<p.name>_all\')">
                , <"selectDisplayedGraphs()">
                    
                ]>>
        ,
        <"window.labelingX",<["e"], [    
           <"text", "svg"+D3(select(".labelX"))>
           ,<"xLabel=e.value">
           ,<"text.text(xLabel)"> 
           ,<"sessionStorage.setItem(\"xLabel\",xLabel)">   
           ]>>
        ,
        <"window.labelingY",<["e"], [    
           <"text", "svg"+D3(select(".labelY"))>
           ,<"yLabel=e.value">
           ,<"text.text(yLabel)"> 
           ,<"sessionStorage.setItem(\"yLabel\",yLabel)">    
           ]>>  
        ,
        <"window.loadLabel",<[],  [ 
              <"text", "svg"+D3(select(".labelY"))>
             ,<"text.text(getLabel(\"yLabel\", yLabel))">
             , <"text", "svg"+D3(select(".labelX"))>
             ,<"text.text(getLabel(\"xLabel\", xLabel))">
             ,<"selectDisplayedGraphs()">
             ,<"setFontSize()">
            ] >>
        ,
         <"window.setFontSize",<[],  [ 
           
           <"window.pageSized", "window.innerWidth/50">,
            <"window.pageSized", "Math.max(pageSized, 10)">,
            <"window.pageSized", "Math.min(pageSized, 30)">,
            // <"alert(pageSized)">,
            <"document.body.style.fontSize","window.pageSized+\"pt\"">     
        ] >>
        ,            
        remember?<"window.onload", <"window.loadLabel()">> :"window.setFontSize()"                   
        );
       
   return <header, body, p>;  
   }
 
 public void plot(PlotData p, int width = 800, int height = 400, int margin = 60,
   str style = "splines", int symbolSize = 32, bool mark = false, str xLabel = "x",
   str yLabel = "y", bool remember = true) {  
   
      str iframe =
         plotEl(|file:///tmp/d3|, p
        , width = width, height = height     
        , style=style, symbolSize=symbolSize
        , xLabel = xLabel, yLabel = yLabel, margin =margin,
        remember = remember
        );
      
      htmlDisplay(publish(
            // |project://chart/src/aap|
              |file:///tmp/d3|
            , p
            , iframe));
 

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
public str color_= "color";
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
public str   input_checked = "input checked";
public str   svg_= "svg";
public str   circle_= "circle";
public str   ellipse_= "ellipse";
public str   rect_= "rect";
public str   line_= "line";
public str   polyline_= "polyline";
public str   polygon_= "polygon";
public str   text_= "text";
public str   text_anchor_= "text-anchor";
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
   
   // num r(num v) = round(v,0.0004);
   
   num r(num v) =v;
   
   num r3(num v) =v * v *v;
   
   // num r3(num v) = round(v*v*v,0.0004);
   
   int n= 10;
   tuple[str, str, str, list[tuple[num, num]]] dsin = <"sin", "none", "default", [<(i*6.0/n), 5*r(sin(2*i*PI()/n))+5>|i<-[0..n+1]]>;
   tuple[str, str, str, list[tuple[num, num]]] dcos = <"cos", "none", "points", [<(i*6.0/n), 5*r(cos(2*i*PI()/n))+5>|i<-[0..n+1]]>;
   tuple[str, str, str, list[tuple[num, num]]] dsin3 = <"sin3", "none", "lines", [<(i*6.0/n), 5*r3(sin(2*i*PI()/n))+5>|i<-[0..n+1]]>;
   tuple[str, str, str, list[tuple[num, num]]] dcos3 = <"cos3", "none", "splines", [<(i*6.0/n), 5*r3(cos(2*i*PI()/n))+5>|i<-[0..n+1]]>;
   
   list[tuple[str, str, str, list[tuple[num, num]]]] d = [dsin, dcos, dsin3, dcos3];

   PlotData p = <"Sin and Cos", "sinc", [labx(i)|i<-[0..7]], [laby(i)|i<-[0..11]], d>;
   plot(p,  symbolSize = 20, style = "splines");
   /*
    list[list[tuple[num, num]]] d = [[<(i*step)*nTickx, (-y+f(x+(i*step)*width))*nTicky/height >| i<-[0..ub+1]]|f<-g];
    list[tuple[str, str, list[tuple[num, num]]]] w =  [<"<i>","blue", d[i]>|i<-[0..size(d)]];
    PlotData p = <"plot", xt, yt, w>;
    plot(p, width = 600, height = 600); 
    */ 
   }   
   