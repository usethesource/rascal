@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module analysis::statistics::markup::D3
import Prelude;
import display::Display;

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
            '<head>\n<body>\n\</html\>"
           ;                     
}

/* -------------------------------------------------------------------------- */

data ScriptElm = var(map[str, value] m1)
             |function(JavaScript p)
             |function(str d, JavaScript p1)
             |function(str d, str i, JavaScript p1)
             |function(str p2)
             |function(str d, str p2)
             |function(str d, str i, str p2)
             |expr(str d)
             |call(str obj, Bundle bundle);

data JavaScript = program(list[ScriptElm scriptElm]);

data Bundle = attr(map[str, value])|
              attr(map[str, value], Bundle bundle) |
              enter()|enter(Bundle bundle)|
              exit()| exit(Bundle bundle)|
              dat(str d)| dat(str d, Bundle bundle)|
              style(map[str, value] styl)| 
              style(map[str, value] styl, Bundle bundle)|
              transition()| transition(Bundle bundle) |
              duration(int t)| duration(int t, Bundle bundle) |
              add(str obj)| add(str obj, Bundle bundle) |
              remove(str obj)| remove(str obj, Bundle bundle) |
              on(str event, ScriptElm s)| on(str event, ScriptElm s, Bundle bundle) |
              selectAll(str obj, Bundle bundle) |
              select(str obj, Bundle bundle) |
              csv(str l, ScriptElm callback) |
              csv(str l, ScriptElm accessor, ScriptElm callback) |
              tsv(str l, ScriptElm callback) |
              tsv(str l, ScriptElm accessor, ScriptElm callback) |
              text(str file, ScriptElm callback) |
              csv_parse(str d) |
              csv_parse(str d, ScriptElm accessor) |
              json(str l, ScriptElm callback)           
              ;
             
value display(value v) {
    if (ScriptElm e := v) {
          return toString(e);
          }
    if (str e := v) {
          return "\"<e>\"";
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
    return v;
    }
              
str display(str key, map[str, value] m) {
      return "<for(k<-m) {>.<key>(\"<k>\",<display(m[k])>)<}>";    
      }
              
str toString(Bundle b) {
              str r = "";
              top-down visit(b) {
                 case enter(): r+=".enter()";
                 case enter(_): r+=".enter()";
                 case exit(): r+=".exit()";
                 case exit(_): r+=".exit()";
                 case transition(): r+=".transition()";
                 case transition(_): r+=".transition()";
                 case dat(str d): r+=".data(<d>)";
                 case dat(str d, _): r+=".data(<d>)";
                 case attr(map[str, value] v): r+= display("attr", v);
                 case attr(map[str, value] v, _): r+= display("attr", v);
                 case style(map[str, value] v): r+= display("style", v);
                 case style(map[str, value] v, _): r+= display("style", v);
                 case add(str d): r+=".append(\"<d>\")";
                 case add(str d, _): r+=".append(\"<d>\")";
                 case remove(): r+=".exit()";
                 case remove(_): r+=".exit()";
                 case duration(int d): r+=".duration(<d>)";
                 case duration(int d, _): r+=".duration(<d>)";
                 case on(str event, str d): r+=".on(\"<event>\", <d>)";
                 case on(str event, str d, _): r+=".on(\"<event>\", <d>)";
                 case on(str event, ScriptElm e): {r+=".on(\"<event>\", <toString(e)>)"; return r;}
                 case on(str event, ScriptElm e,  Bundle c): {r+=".on(\"<event>\", <toString(e)>)";return r+ toString(c);}
                 case selectAll(str obj, _): r+=".selectAll(\"<obj>\")";
                 case select(str obj, _): r+=".select(\"<obj>\")";
                 case text(str l, ScriptElm callback) : return r+ ".text(\"<l>\", <toString(callback)>)";           
                 case csv(str l, ScriptElm callback) : return r+ ".csv(\"<l>\", <toString(callback)>)";
                 case csv(str l, ScriptElm acc, ScriptElm callback) : r+= ".csv(\"<l>\" , <toString(acc)>, <toString(callback)>)"; 
                 case tsv(str l, ScriptElm callback) : return r+ ".tsv(\"<l>\", <toString(callback)>)";
                 case tsv(str l, ScriptElm acc, ScriptElm callback) : r+= ".tsv(\"<l>\" , <toString(acc)>, <toString(callback)>)";                   
                 case csv_parse(str d, ScriptElm acc) : return r+".csv.parse(<d>, <toString(acc)>)";
                 case csv_parse(str d) : return r+".csv.parse(<d>)";  
                 case json(str l, ScriptElm callback) : return r+ ".json(\"<l>\", <toString(callback)>)";       
                 }
              return r;
              }
              
str toString(ScriptElm s) {
     str r = "";
     switch(s) {
          case var(map[str, value] m): return
          "  var <for(k<-m) {><k>= <display(m[k])>,<}>"[..-1]+"\n";
          case function(str s) : return "function() {
          ' <s> }\n";
          case function(str d, str s) : return "function(<d>) {
          ' <s> }\n";
          case function(str d, str i, str s) : return "function(<d>, <i>) {
          ' <s> }\n";
          case function(JavaScript p) : return "function() {
          ' <toString(p)> }\n"; 
          case function(str d, JavaScript p) : return "function(<d>) {
          ' <toString(p)> }\n"; 
           case function(str d, str i, JavaScript p) : return "function(<d>, <i>) {
          ' <toString(p)> }\n"; 
          case expr(str d) : return "<d>";
          case call(str obj, Bundle b): return "<obj><toString(b)>";
          }
     return "Q";    
     }
 
str toString(JavaScript js) {
    if (program(list[ScriptElm] es):=js) {
           return "<for(e<-es){> <toString(e)>;\n<}>";
           }
    }             

/*
public str selectAll(str obj, Bundle bundle) {
            return "d3.selectAll(\"<obj>\")"+toString(bundle)+";\n";
            }
*/
          

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
public str   g_= "g";
public str   use_= "use";
public str   defs_= "defs";
public str   symbol_= "symbol";
public str   script_ = "script";
public str   charset_ = "charset";
public str   d3_ = "d3";


public str JavaScript(ScriptElm e ...) {
     return "<Z(script_, toString(program(e)))>";
}

public str JavaScriptCsv(str file, str error, str dat, ScriptElm e ...) {
     return JavaScript(call(d3_, csv("<file>", function(error, dat, program(e)))));
     }
     
public str JavaScriptTsv(str file, str error, str dat, ScriptElm e ...) {
     return JavaScript(call(d3_, tsv("<file>", function(error, dat, program(e)))));
     }

public str JavaScriptJson(str file, str error, str dat, ScriptElm e ...) {
     return JavaScript(call(d3_, json("<file>", function(error, dat, program(e)))));
     }
      
public void main() {
 int r = 12;
 str header = Z(title_, (), "d3.js Three Little Circles")+
 Z(button_, "Run") +
 Z(script_,(src_: "http://d3js.org/d3.v3.min.js", charset_:"utf-8"));
  str body = Z(h1_, (id_: "three_little_circles"), "Three Little Circles") +
  Z(script_, (type_:"text/javascript"), 
       "function debug(obj) {
       'var output = \'\';
       ' for (property in obj) {
       '   output += property + \': \' + obj[property]+\'; \';
       '   }
       '  alert(output);
       ' }\n") + 
   JavaScript([
           var(
           (
            "w":360,
            "h":580       
           )) 
           ,
           call(d3_, selectAll(h1_, style(("color": "red")))),
           var((svg_: 
            call(d3_, select(body_, add(svg_, attr((width_: expr("w"), height_:expr("h")), style(("fill":"yellow"))
              ))))))
           ])
  +JavaScriptCsv("aap.txt", "error", "dataset",
             expr("debug(dataset[0])"),
                
                call(svg_, 
                 selectAll(".main", dat("dataset", 
                  enter(add(circle_,
                   attr((class_:"main", 
                         cx_:function("d", "return d.x;"), 
                         cy_:function("d", "return d.y;"), 
                         r_:r))  
               )))))
               ,    
               var(("border": call(svg_, selectAll(".border", dat("dataset")))))
               ,   
               call(d3_, selectAll(button_, on("click", 
               function(program([
                  call(svg_, 
                  selectAll(".main", 
                     transition(duration( 5000, 
                        style(("fill": "red"))
                  ))))
                  ,         
                  call("border", enter(
                      add(circle_,
                         attr((class_:"border", 
                               cx_:function("d", "return d.x;"), 
                               cy_:function("d", "return d.y;"), 
                               r_:r+20), 
                            style(("fill":"none",
                                   "stroke":"black"),
                               transition(duration(1000, 
                                   attr(("r":12))
                               ))
                            ))
                      )))
                  ]
             ))     
           )))                         
          )
  +Z(p_, 
  "Once upon a time, there were three little circles. 
  '    This tutorial shows you how to manipulate them using selections.")  
      ;
      
  println(header);
   println(body);
  htmlDisplay("dotplugin", "d3/index", html(
         header
    , body));
}
 