module experiments::vis2::sandbox::IFigure
import Prelude;
import util::Webserver;
import lang::json::IO;
import util::HtmlDisplay;
import util::Math;
import experiments::vis2::sandbox::Figure;
// '<style("border","<f.lineWidth<0?1:f.lineWidth>px solid <f.lineColor>")>
private loc base = |std:///experiments/vis2/sandbox|;


public data IFigure = ifigure(str id, list[IFigure] child);

public data IFigure = ifigure(str content);

public data IFigure = iemptyFigure();


bool debug = true;
int screenWidth = 400;
int screenHeight = 400;

int seq = 0;
int occur = 0;


alias Elm = tuple[void(str, str) f, int seq, str id, str begintag, str endtag, str script, int width, int height,
      int x, int y, 
      Alignment align, int lineWidth, str lineColor];


alias State = tuple[str name, str v];
public list[State] state = [];
public list[str] old = [];

public void setDebug(bool b) {
   debug = b;
   }

public void null(str event, str id ){return;}
      
int getInt(str id) = toInt(state[widget[id].seq].v);

void setInt(str id, int v) {state[widget[id].seq].v = "<v>";}

str getStr(str id) = state[widget[id].seq].v;

void setStr(str id, str v) {state[widget[id].seq].v =  v;}

public map[str, Elm] widget = ();

public list[str] widgetOrder = [];

public list[str] adjust = [];

public void clearWidget() { widget = (); widgetOrder = [];}
              
str visitFig(IFigure fig) {
    if (ifigure(str id, list[IFigure] f):= fig) {
         return 
    "<widget[id].begintag> <for(d<-f){><visitFig(d)><}><widget[id].endtag>\n";
         }
    if (ifigure(str content):=fig) return content;
    return "";
    }
    
IFigure fig;
//   'table { border-collapse: collapse;} 
// 'table td, table th {padding: 0;}              
str getIntro() {
   // println(widgetOrder);
   res = "\<html\>
        '\<head\>      
        '\<style\>
        
        
        '\</style\>    
        '\<script src=\"IFigure.js\"\>\</script\>
        '\<script src=\"http://d3js.org/d3.v3.min.js\" charset=\"utf-8\"\>\</script\>  
        '\<script\>
        '  function doFunction(id) {
        '    return function() {
        '    askServer(\"<getSite()>/getValue/\"+id, {},
        '            function(t) {
        '                  for (var d in t) {
        '                   var e = d3.select(\"#\"+d); 
        '                   // e.text(t[d]);
        '                  e.attr(\"fill\", \"\"+t[d]);
        '                   }
        '                });  
        '   };
        ' }
       ' function initFunction() {
           <for (d<-reverse(widgetOrder)) {> <widget[d].script> <}>
           <for (d<-adjust) {> <d> <}>
       ' }
       ' onload=initFunction;
       '\</script\>
       '\</head\>
       '\<body\>
       ' <visitFig(fig)>      
       '\</body\>     
		'\</html\>
		";
    println(res);
	return res;
	}

Response page(get(), /^\/$/, map[str,str] _) { 
	return response(getIntro());
}

list[State] diffNewOld() {
    return [state[i]|i<-[0..size(state)], state[i].v!=old[i]];
    }

Response page(post(), /^\/getValue\/<name:[a-zA-Z0-9_]+>/, map[str, str] parameters) {
	// println("post: getValue: <name>, <parameters>");
	widget[name].f("click", name);
	list[State] changed = diffNewOld();
	str res = toJSON(toMap(changed), true);
	old = [s.v|s<-state];
	return response("<res>");
}

default Response page(get(), str path, map[str, str] parameters) {
   // println("File response: <base+path>");
   return response(base + path); 
   }

private loc startFigureServer() {
  	loc site = |http://localhost:8081|;
  
  while (true) {
    try {
      //println("Trying ... <site>");
      serve(site, dispatchserver(page));
      return site;
    }  
    catch IO(_): {
      site.port += 1; 
    }
  }
}

private loc site = startFigureServer();

private str getSite() = "<site>"[1 .. -1];


      
public void _render(IFigure fig1, int width = 400, int height = 400, 
     Alignment align = centerMid, int lineWidth = 0, int lineColor = 0,
     str fillColor = "none", str lineColor = "black")
     {
     screenWidth = width;
     screenHeight = height;
     str id = "figureArea";
     
    str begintag= beginTag(id, align);
    // println(getWidth(fig1));
    str endtag = endTag();   
    widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")<style("border","1px solid black")> 
        '<stylePx("width", width)><stylePx("height", height)>
        ;       
        "
       , width, height, width, height, align, 1, "" >;
       seq=seq+1;
       state += <id, fillColor>;
       old+=fillColor;
       widgetOrder += id;
    fig = ifigure(id, [fig1]);
    println("site=<site>");
	htmlDisplay(site);
}

str getId(IFigure f) {
    if (ifigure(id, _) := f) return id;
    return "";
    }
    
void setId(Figure f) {  
    if (isEmpty(f.id)) f.id = "i<id>";
    id = id +1;    
    }

int getWidth(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].width;
    return -1;
    }
    
int getHeight(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].height;
    return -1;
    }

int getLineWidth(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].lineWidth;
    return -1;
    }
    
str getLineColor(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].lineColor;
    return "";
    }

int getX(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].x;
    return -1;
    }
    
int getY(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].y;
    return -1;
    }
    
int getHgap(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].hgap;
    return -1;
    }
    
int getVgap(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].vgap;
    return -1;
    }
    
Alignment getAlign(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].align;
    return <-1, -1>;
    } 
    
void(str, str) getCallback(Event e) {
    if (on(_, void(str, str) callback):=e) return callback;
    return null; 
    }
       
str getEvent(Event e) {
    if (on(str eventName, _):=e) return eventName;
    return ""; 
    }
    
str debugStyle() {
    if (debug) return style("border","1px solid black");
    return "";
    }
    
str style(str key, str v) {
    if (isEmpty(v)) return "";
    return ".style(\"<key>\",\"<v>\")";
    }
    
str style(str key, int v) {
    if (v<0) return "";
    return ".style(\"<key>\",\"<v>\")";
    }

str stylePx(str key, int v) {
    if (v<0) return "";
    return ".style(\"<key>\",\"<v>px\")";
    }
    
str attrPx(str key, int v) {
    if (v<0) return "";
    return ".attr(\"<key>\",\"<v>px\")";
    }  
       
str attr(str key, str v) {
    if (isEmpty(v)) return "";
    return ".attr(\"<key>\",\"<v>\")";
    }

str attr1(str key, str v) {
    if (isEmpty(v)) return "";
    return ".attr(\"<key>\",<v>)";
    }    

str attr(str key, int v) {
    if (v<0) return "";
    return ".attr(\"<key>\",\"<v>\")";
    }
    
str attr(str key, real v) {
    if (v<0) return "";
    return ".attr(\"<key>\",\"<precision(v, 1)>\")";
    } 
        
str on(str ev, str proc) {
    if (isEmpty(ev)) return "";
    return ".on(\"<ev>\", <proc>)";
    }         
// -----------------------------------------------------------------------
// '.text(\"<s>\") 
int getTextWidth(Figure f, str s) {
     if (f.width>=0) return f.width;
     int fw =  f.fontSize<0?12:f.fontSize;
     return size(s)*fw;
     }
 
 int getTextHeight(Figure f) {
     if (f.height>=0) return f.height;
     int fw =  (f.fontSize<0?12:f.fontSize)+5;
     return fw;
     }   
IFigure _text(str id, Figure f, str s) {
    str begintag="\<div  id=\"<id>\"\>";
    int width = f.width;
    int height = f.height;
    Alignment align =  width<0?topLeft:f.align;
    str endtag = "\</div\>"; 
    widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")
        '<debugStyle()>
        '<style("background-color", "<f.fillColor>")>
        '<stylePx("width", width)><stylePx("height", height)>
        '<stylePx("font-size", f.fontSize)>
        '<style("font-style", f.fontStyle)>
        '<style("font-family", f.fontFamily)>
        '<style("font-weight", f.fontWeight)>
        '<style("color", f.fontColor)>
        '.text(\"<s>\") 
        ';"
        , getTextWidth(f, s), getTextHeight(f), 0, 0, align, f.lineWidth, f.lineColor >;
         seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       return ifigure(id, []);
    }
    
str beginTag(str id, Alignment align) {
    return "
           '\<table  cellspacing=\"0\" cellpadding=\"0\" id=\"<id>\"\>\<tr\>
           ' \<td <vAlign(align)> <hAlign(align)>\>";
    }
    
str endTag() {
   return "\</td\>\</tr\>\</table\>"; 
   }



int getRecWidth(Figure f) {
         // int lw = f.lineWidth<0?0:f.lineWidth;
         int lw = 0;
         if (f.width>=0)  return f.width+2*lw; 
         return -1;
         }

int getRecHeight(Figure f) {
         // int lw = f.lineWidth<0?0:f.lineWidth;
         int lw = 0;
         if (f.height>=0) return f.height+2*lw;
         return -1;
         } 
         
int getAtX(Figure f) {
         return toInt(f.at[0]);
         }
         
int getAtY(Figure f) {
         return toInt(f.at[1]);
         }
          
int getAtX(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].x;
    return 0;
    }
    
int getAtY(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].y;
    return 0;
    }   
    
int hPadding(Figure f) = f.padding[0]+f.padding[2];     

int vPadding(Figure f) = f.padding[1]+f.padding[3]; 
                         
 IFigure _rect(str id, Figure f,  IFigure fig = iemptyFigure(), Alignment align = <0, 0>) {
      int lw = f.lineWidth<0?0:f.lineWidth;   
      if (isEmpty(f.fillColor)) f.fillColor = "none";
      if (isEmpty(f.lineColor)) f.lineColor = "black";
      if (f.lineWidth<0) f.lineWidth = 1;
      if (f.width<0 || f.height<0 || getAtX(fig)>0 || getAtY(fig)>0) f.align = topLeft;
      int lw2 = lw/2;
      int offset = round((0.5-align[0])*2*lw2);
      println("align = <lw2> <align> <0.5-align[0]>  <offset> getAtX(fig)=<getAtX(fig)>");
     
      str begintag=
         "\<svg id=\"<id>_svg\"\> \<rect id=\"<id>\"/\> 
         '\<foreignObject  id=\"<id>_fo\" x=\"<getAtX(fig)+offset>\" y=\"<getAtY(fig)+lw2>\", width=\"<screenWidth>px\" height=\"<screenHeight>px\"\>
         '<beginTag("<id>_fo_table", f.align)>
         ";
       // println("<begintag>");
       str endtag = endTag();
       if (f.width<0) f.width = getWidth(fig)+lw+getAtX(fig)+hPadding(f);
       if (f.height<0) f.height = getHeight(fig)+lw+getAtY(fig)+vPadding(f);
       endtag += "\</foreignObject\>\</svg\>"; 
       widget[id] = <getCallback(f.event), seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")
        '<on(getEvent(f.event), "doFunction(\"<id>\")")>
        '<attr("x", 0)><attr("y", 0)> 
        '<attr("width", f.width)><attr("height", f.height)>
        '<styleInsideSvg(id, f, fig)>
        ",f.width+hPadding(f), f.height+vPadding(f), getAtX(f), getAtY(f), f.align, f.lineWidth, f.lineColor >;
         seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       return ifigure(id, [fig]);
       } 
           
 num cxL(Figure f) =  f.cx>=0?f.cx:(((ellipse():=f)?f.rx:f.r) + (f.lineWidth>=0?f.lineWidth/2:0));
 num cyL(Figure f) =  f.cy>=0?f.cy:(((ellipse():=f)?f.ry:f.r) + (f.lineWidth>=0?f.lineWidth/2:0));
 
 str styleInsideSvgOverlay(str id, Figure f) {
      int lw = f.lineWidth<0?1:f.lineWidth; 
      return " 
        '<style("stroke-width",f.lineWidth)>
        '<style("stroke","<f.lineColor>")>
        '<style("fill", "<f.fillColor>")>     
        ';
        ' adjustSvgAttr(\"<id>_svg\", \"<id>\", <lw/2>); 
        'd3.select(\"#<id>_svg\")
        '<attr("width", f.width)><attr("height", f.height)>
        ;
        ";
      } 
 
 str styleInsideSvg(str id, Figure f,  IFigure fig) {  
    int x  =getAtX(fig);
    int y = getAtX(fig);
    int lw = f.lineWidth<0?0:f.lineWidth;
    if (ellipse():=f || circle():=f) lw = 0;
    if (ngon():=f) {lw = 0;}
    str bId = id; 
    println("styleInside svg: <hPadding(f)>");
    int width = f.width;
    int height = f.height;
    switch (f) {
        case ngon():bId = "<id>_rect";
        case ellipse():bId = "<id>_rect";
        case circle():bId = "<id>_rect";
        }
    // adjust+="adjustSvgAttr(\"<id>_svg\", \"<bId>\", 0);\n";
    // adjust+="adjustSvgAttr(\"<id>_fo\", \"<bId>\", 0);\n"; 
    // adjust+="adjustSvgStyle(\"<id>_fo_table\", \"<bId>\", <-lw1>);\n"; 
    return "
        '<style("stroke-width",f.lineWidth)>
        '<style("stroke","<f.lineColor>")>
        '<style("fill", "<f.fillColor>")>     
        ';
        'd3.select(\"#<id>_svg\")
        '<attr("width", width)><attr("height", height)>
        ';
        
        'd3.select(\"#<id>_fo\")
        '<attr("width", width)><attr("height", height)>
        '<debugStyle()>
        ';    
        '       
        'd3.select(\"#<id>_fo_table\")
        '<style("width", width-lw)><style("height", height-lw)>
        '<_padding(f.padding)> 
        '<debugStyle()>
        ';
        ";      
      }
  // 'd3.select(\"#<id>_table\")
 //      '<_padding(f.padding)>  
  //      '<debugStyle()>
  //      ; 
  
int getEllipseWidth(Figure f, IFigure fig) {
         if (f.width>=0) return f.width;
         int lw = f.lineWidth<0?0:f.lineWidth;
         if (ellipse():=f) return toInt(2*f.rx)+lw;
         if (circle():=f || ngon():=f) return toInt(2*f.r)+lw;    
         return -1;
         }

int getEllipseHeight(Figure f, IFigure fig) {
         // int lw = getLineWidth(fig)<0?0:getLineWidth(fig);
         if (f.height>=0) return f.height;
         int lw = f.lineWidth<0?0:f.lineWidth;
         if (ellipse():=f) return toInt(2*f.ry)+lw;
         if (circle():=f || ngon():=f) return toInt(2* f.r)+lw;    
         return -1;
         }
     
 IFigure _ellipse(str id, Figure f,  IFigure fig = iemptyFigure(), Alignment align = <0.5, 0.5>) {
      int lw = f.lineWidth<0?0:f.lineWidth;
      str tg = "";
      switch (f) {
          case ellipse(): {tg = "ellipse"; 
                           if (f.rx<0 || f.ry<0 || getAtX(fig)>0 || getAtY(fig)>0) f.align = centerMid;
                           f.rx= (f.rx<0?(getWidth(fig)/2+lw/2+getAtX(fig)+hPadding(f)/2):f.rx);
                           f.ry= (f.ry<0?(getHeight(fig)/2+lw/2+getAtY(fig)+vPadding(f)/2):f.ry);
                           if (f.width<0) f.width= round(f.rx*2+lw)/*+hPadding(f)*/;
                           if (f.height<0) f.height = round(f.ry*2+lw)/*+vPadding(f)*/;                         
                           }
          case circle(): {tg = "circle";
                          if (f.r<0 || getAtX(fig)>0 || getAtY(fig)>0) f.align = centerMid;
                          f.r= (f.r<0?(max([getWidth(fig), getHeight(fig)])/2+lw/2+max([getAtX(fig),getAtY(fig)])
                                      +max([hPadding(f), vPadding(f)])):f.r)
                          ;
                          if (f.width<0) f.width= round(f.r*2+lw);
                          if (f.height<0) f.height = round(f.r*2+lw);                 
                          }
          }     
       int lw2 = lw/2; 
       int offset = round((0.5-align[0])*2*lw2);
       // println("offset <offset> align: <align[0]>");
       str begintag =
         "\<svg id=\"<id>_svg\"\>\<rect id=\"<id>_rect\"/\> \<<tg> id=\"<id>\"/\> 
         '\<foreignObject  id=\"<id>_fo\" x=\"<getAtX(fig)/*+offset*/>\" y=\"<getAtY(fig)/*+lw2*/>\",width=\"<screenWidth>px\" height=\"<screenHeight>px\"\>
         '<beginTag("<id>_fo_table", f.align)>
         ";
       str endtag = endTag();
       endtag += "\</foreignObject\>\</svg\>"; 
       widget[id] = <getCallback(f.event), seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>_rect\")
        '<style("fill","none")><style("stroke", debug?"black":"none")><style("stroke-width", 1)>
        '<attr("x", 0)><attr("y", 0)><attr("width", f.width)><attr("height", f.height)>
        ;
        'd3.select(\"#<id>\")
        '<on(getEvent(f.event), "doFunction(\"<id>\")")>
        '<attr("cx", cxL(f))><attr("cy", cyL(f))> 
        '<ellipse():=f?"<attr("rx", f.rx)><attr("ry", f.ry)>":"<attr("r", f.r)>">
        '<styleInsideSvg(id, f, fig)>
        ", f.width, f.height, getAtX(f), getAtY(f), f.align, f.lineWidth, f.lineColor >;
         seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       return ifigure(id, [fig]);
       }
       
str toP(num d) {
        num v = abs(d);
        return "<toInt(d)>.<toInt(v*10)%10><toInt(v*100)%10>";
        }
      
str translatePoints(Figure f) {
       Points p;
       // int lw = f.lineWidth<0?0:f.lineWidth/2+1;
       if (polygon():=f) {
           p = f.points;         
       }
       if (ngon():=f) {
            int lw = corner(f)/2;
             num angle = 2 * PI() / f.n;
             p  = [<f.r+lw+f.r*cos(i*angle), f.r+lw+f.r*sin(i*angle)>|int i<-[0..f.n]];
             }
       return "<toP(p[0][0])>, <toP(p[0][1])>" + 
            "<for(t<-tail(p)){> <toP(t[0])> , <toP(t[1])> <}>";
       }
       
str extraCircle(str id, Figure f) {
       if (ngon():=f) {
            return "\<circle id=\"<id>_circle\"/\>";
            }
       return "";
       } 
       
int corner(Figure f) {
     return corner(f.n, f.lineWidth);
     // return lw;
    }
    
int corner(int n, int lineWidth) {
     num angle = PI() - 2 * PI() / n;
     int lw = lineWidth<0?0:lineWidth;
     return toInt(lw/sin(0.5*angle));
     // return lw;
    }  
   
num cR(Figure f)  = ngon():=f?(f.r + corner(f)/2+1):-1;

// num cR(Figure f)  = ngon():=f?(f.r):-1;

num rR(Figure f)  = ngon():=f?f.r+corner(f)/2:-1;

num rR1(Figure f)  = ngon():=f?f.r+corner(f):-1;

int getNgonWidth(Figure f, IFigure fig) {
         if (f.width>=0) return f.width;
         int r = toInt(rR(f));
         // int lw = f.lineWidth<0?0:2;
         int lw = 1;
         // if (ngon():=f) return 2*r+2*lw+max([getAtX(fig), getAtY(fig)]);   
         if (ngon():=f) return 2*r+lw;     
         return -1;
         }

int getNgonHeight(Figure f, IFigure fig) {
         if (f.height>=0) return f.height;
         int r = toInt(rR(f));
         // int lw = f.lineWidth<0?0:2;
         int lw = 1;
         // println("r=<r>");
         // if (ngon():=f) return 2*r+2*lw+max([getAtX(fig), getAtY(fig)]);   
         if (ngon():=f) return 2*r+lw;      
         return -1;
         }

int getPolWidth(Figure f) {
         if (f.width>=0) return f.width;
         num width = max([p.x|p<-f.points]);     
         return toInt(width+10);
         }

int getPolHeight(Figure f) {
         if (f.height>=0) return f.height;
         num height = max([p.y|p<-f.points]);     
         return toInt(height+10);
         }
       
IFigure _polygon(str id, Figure f,  IFigure fig = iemptyFigure()) {
       // str begintag= beginTag("<id>_table", topLeft); 
       f.width = getPolWidth(f);
       f.height = getPolHeight(f);
       str begintag = "";
       begintag+=
         "\<svg id=\"<id>_svg\"\>\<polygon id=\"<id>\"/\>       
         ";
       str endtag = "\</svg\>"; 
       widget[id] = <getCallback(f.event), seq, id, begintag, endtag, 
        "  
        'd3.select(\"#<id>\")
        '<on(getEvent(f.event), "doFunction(\"<id>\")")>
        '<attr("points", translatePoints(f))> 
        '<style("fill-rule", f.fillEvenOdd?"evenodd":"nonzero")>
        '<styleInsideSvgOverlay(id, f)>
        ", f.width, f.height, getAtX(f), getAtY(f),  f.align, f.lineWidth, f.lineColor >;
         seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       return ifigure(id, [fig]);
       }
       
 // data Vertex
	//= line(num x, num y)
	//| lineBy(num x, num y)
	//| move(num x, num y)
	//| moveBy(num x, num y)
	//;

str toV(Vertex v) {
     switch(v) {
         case line(num x, num y): return "{\"x\":<toP(v.x)>, \"y\":<toP(v.y)>}";
         }
     }
       
str translateShapePoints(Figure f) {
        
       if (shape(list[Vertex] vs):= f) {
       if (f.shapeClosed) vs += vs[0];
       // int lw = f.lineWidth<0?0:f.lineWidth/2+1;
       return "[<toV(vs[0])>" + 
            "<for(v<-tail(vs)){>,<toV(v)> <}>]";
       }
}

num xV(Vertex v) = line(num x, num y):=v?x:-1;

num yV(Vertex v) = line(num x, num y):=v?y:-1;

int getPathWidth(Figure f) {
        if (shape(list[Vertex] vs):= f) {
             if (f.width>=0) return f.width;
             num width = max([xV(p)|p<-vs]);     
             return toInt(width+10);
            }
         return -1;
         }

int getPathHeight(Figure f) {
         if (shape(list[Vertex] vs):= f) {
             if (f.height>=0) return f.height;
             num height = max([yV(p)|p<-vs]);     
              return toInt(height+10);
         }
      }
       
IFigure _shape(str id, Figure f,  IFigure fig = iemptyFigure()) {
       // str begintag= beginTag("<id>_table", topLeft); 
       f.width = getPathWidth(f);
       f.height = getPathHeight(f);
       str begintag = "";
       begintag+=
         "\<svg id=\"<id>_svg\"\>\<path id=\"<id>\"/\>       
         ";
       str endtag = "\</svg\>"; 
       widget[id] = <getCallback(f.event), seq, id, begintag, endtag, 
        "
        'var lineFunction = d3.svg.line()
        '.x(function(d){return d.x;})
        '.y(function(d){return d.y;})
        '.interpolate(\"basis\");  
        'd3.select(\"#<id>\")
        '<on(getEvent(f.event), "doFunction(\"<id>\")")>
        '<attr1("d", "lineFunction(<translateShapePoints(f)>)")> 
        '<styleInsideSvgOverlay(id, f)>
        ", f.width, f.height, getAtX(f), getAtY(f),  f.align, f.lineWidth, f.lineColor >;
         seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       return ifigure(id, [fig]);
       }
                 
IFigure _ngon(str id, Figure f,  IFigure fig = iemptyFigure(), Alignment align = <0.5, 0.5>) {
       int lw = f.lineWidth<0?0:f.lineWidth;
       if (f.r<0 || getAtX(fig)>0 || getAtY(fig)>0) f.align = centerMid;
       if (f.r<0) {
          f.r = max([getWidth(fig), getHeight(fig)])/2+max([getAtX(fig), getAtY(fig)])+lw/2
               +max([hPadding(f), vPadding(f)]);       
          }
       if (f.width<0) f.width= round(f.r*2+corner(f));
       if (f.height<0) f.height = round(f.r*2+corner(f)); 
       if (isEmpty(f.fillColor)) f.fillColor = "none";
       if (isEmpty(f.lineColor)) f.lineColor = "black";
       if (f.lineWidth<0) f.lineWidth = 1;
       str begintag = "";
       begintag+=
         "\<svg id=\"<id>_svg\"\>\<rect id=\"<id>_rect\"/\><extraCircle(id, f)> \<polygon id=\"<id>\"/\> 
         '\<foreignObject  id=\"<id>_fo\" 
              x=\"<getAtX(fig)>\" y=\"<getAtY(fig)>\"
              width=\"<screenWidth>px\" height=\"<screenHeight>px\"\>
               
         '<beginTag("<id>_fo_table", f.align)>
         ";
       str endtag =  endTag();
       endtag += "\</foreignObject\>\</svg\>"; 
       widget[id] = <getCallback(f.event), seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>_rect\")
        '<style("fill","none")><style("stroke", debug?"black":"none")><style("stroke-width", 1)>
        '<attr("x", 0)><attr("y", 0)><attr("width", f.width)><attr("height", f.height)>
        ;
        'd3.select(\"#<id>_circle\")
        '<style("fill","none")><style("stroke", debug?"black":"none")><style("stroke-width", 1)>
        '<attr("cx", cR(f))><attr("cy", cR(f))><attr("r", rR(f))>
        
        'd3.select(\"#<id>\")
        '<on(getEvent(f.event), "doFunction(\"<id>\")")>
        '<attr("points", translatePoints(f))> 
        '<styleInsideSvg(id, f, fig)>
        ", f.width, f.height, getAtX(f), getAtY(f),  f.align, f.lineWidth, f.lineColor >;
         seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       return ifigure(id, [fig]);
       }
 
       
str vAlign(Alignment align) {
       if (align == bottomLeft || align == bottomMid || align == bottomRight) return "valign=\"bottom\"";
       if (align == centerLeft || align ==centerMid || align ==centerRight)  return "valign=\"middle\"";
       if (align == topLeft || align == topMid || align == topRight) return "valign=\"top\"";
       }
str hAlign(Alignment align) {
       if (align == bottomLeft || align == centerLeft || align == topLeft) return "align=\"left\"";
       if (align == bottomMid || align == centerMid || align == topMid) return "align=\"center\"";
       if (align == bottomRight || align == centerRight || align == topRight) return "align=\"right\"";    
       }
       
str vGap(Alignment align, int vgap) {
       if (align == bottomLeft || align == bottomMid || align == bottomRight) return style("padding-bottom", vgap);
       if (align == centerLeft || align ==centerMid || align ==centerRight)  return style("padding-top", vgap)+style("padding-bottom", vgap);
       if (align == topLeft || align == topMid || align == topRight) return style("padding-top", vgap);
       }
       
str hGap(Alignment align, int hgap) {
       if (align == bottomLeft || align == centerLeft || align == topLeft) return style("padding-left", hgap);
       if (align == bottomMid || align == centerMid || align == topMid) return style("padding-left", hgap)+style("padding-right", hgap);
       if (align == bottomRight || align == centerRight || align == topRight) return style("padding-right", hgap);  
       }
       
str _padding(tuple[int, int, int, int] p) {
       return style("padding-left", p[0])+style("padding-top", p[1])
             +style("padding-right", p[2])+style("padding-bottom", p[3]);     
       }
//   '<style("background-color", "<f.fillColor>")>;
// <style("fill","none")><style("stroke","black")><style("stroke-width",1)>    
IFigure _overlay(str id, Figure f, IFigure fig1...) {
       int width = f.width;
       int height = f.height;
       if (width<0) width = max([getAtX(g)+getWidth(g)|g<-fig1]);
       if (height<0) height = max([getAtY(g)+getHeight(g)|g<-fig1]);
       if (isEmpty(f.fillColor)) f.fillColor = "none";
       if (isEmpty(f.lineColor)) f.lineColor = "black";
      if (f.lineWidth<0) f.lineWidth = 1;
       str begintag =
         "\<svg id=\"<id>_svg\"\>\<rect id=\"<id>\"/\>";
       str endtag="\</svg\>";
         //   '\<p\>\</p/\>
        widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\") 
        '<attr("width", width)><attr("height", height)>  
        '<styleInsideSvgOverlay(id, f)>    
        ';
        <for (q<-fig1){> 
        'd3.select(\"#<getId(q)>_svg\")<attrPx("x", getAtX(q))><attrPx("y", getAtY(q))>;<}>  
        "
        , width, height, getAtX(f), getAtY(f), f.align, f.lineWidth, f.lineColor >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       return ifigure(id ,fig1);
       }
                
IFigure _hcat(str id, Figure f, IFigure fig1...) {
       int width = f.width;
       int height = f.height;
       str begintag="                    
            '\<table id=\"<id>\"\>
            '\<tr\>"
            ;
       str endtag="
            '\</tr\>
            '\</table\>
            "
            ;
         //   '\<p\>\</p/\>
        widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\") 
        '<stylePx("width", width)><stylePx("height", height)>   
        '<debugStyle()>
        '<style("background-color", "<f.fillColor>")>        
        ;"
        , width, height, getAtX(f), getAtY(f), f.align, f.lineWidth, f.lineColor >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       return ifigure(id ,[td("<id>_<getId(g)>", f, g, width, height)| g<-fig1]);
       }
       
IFigure _vcat(str id, Figure f, IFigure fig1...) {
       int width = f.width;
       int height = f.height;
       str begintag="                 
            '\<table\>"
           ;
       str endtag="\</table\>";
        widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")       
        '<attr("fill",f.fillColor)><attr("stroke",f.lineColor)>;
        ", width, height, getAtX(f), getAtY(f), f.align, f.lineWidth, f.lineColor >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       return ifigure(id, [td("<id>_<getId(g)>", f, g,  width, height, tr = true)| g<-fig1]);
       }
      
list[list[IFigure]] transpose(list[list[IFigure]] f) {
       list[list[IFigure]] r = [[]|i<-[0..max([size(d)|d<-f])]];
       for (int i<-[0..size(f)]) {
            for (int j<-[0..size(f[i])]) {
                r[j] = r[j] + f[i][j];
            }
         } 
       return r; 
       }

IFigure _grid(str id, Figure f, list[list[IFigure]] figArray=[[]]) {
       if (isEmpty(f.fillColor)) f.fillColor = "none";
       if (isEmpty(f.lineColor)) f.lineColor = "black";
       if (f.lineWidth<0) f.lineWidth = 1;
       list[list[IFigure]] figArray1 = transpose(figArray);
       str begintag="                    
            '\<table id=\"<id>\"\>
            ";
       str endtag="
            '\</table\>
            ";
        widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")       
         '<debugStyle()>
         '<style("background-color", "<f.fillColor>")>
         '<style("border-spacing", "<f.hgap> <f.vgap>")>
         '<debugStyle()>; 
        ", f.width, f.height, getAtX(f), getAtY(f), f.align, f.lineWidth, f.lineColor >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       list[tuple[list[IFigure] f, int idx]] fig1 = [<figArray[i], i>|int i<-[0..size(figArray)]];
       return ifigure(id, [tr("<id>_<g.idx>", f, f.width, f.height, g.f ) | g<-fig1]);
       }      
       
 IFigure td(str id, Figure f, IFigure fig1, int width, int height, bool tr = false) {
    str begintag = tr?"\<tr\>":"";
    begintag +="\<td  id=\"<id>\" <vAlign(f.align)> <hAlign(f.align)>\>";   
    str endtag = "\</td\>";
    if (tr) endtag+="\</tr\>";
    widget[id] = <null, seq, id, begintag, endtag,
        "
        'd3.select(\"#<id>\")
        '<debugStyle()>       
        '<style("background-color", "<f.fillColor>")>  
        '<hGap(f.align, f.hgap)>  
        '<vGap(f.align, f.vgap)>  
        ", f.width, f.height, getAtX(f), getAtY(f), f.align, f.lineWidth, f.lineColor >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+= f.fillColor;
       widgetOrder+= id;
       // if (cellWidth<0) adjust+= "adjust1(\"<id>\", \"<getId(fig1)>\");\n";
    return ifigure(id, [fig1]);
    }
    
 IFigure tr(str id, Figure f, int width, int height, list[IFigure] figs) {
    str begintag = "\<tr\>";
    str endtag="\</tr\>";
    widget[id] = <null, seq, id, begintag, endtag,
        "
        ", width, height, width, height, f.align, f.lineWidth, f.lineColor >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+= f.fillColor;
       widgetOrder+= id;
       // if (width<0) adjust+= "adjust1(\"<id>\", \"<getId(fig)>\");\n";
    return ifigure(id, [td("<id>_<getId(g)>", f, g, width,  height
    )| g<-figs]);
    }
    
bool isCentered(Figure f) = (ellipse():=f) || (circle():=f) || (ngon():=f);
    
Alignment nA(Figure f, Figure fig) =  
     (f.width<0 || f.height<0 || getAtX(fig)>0 || getAtY(fig)>0)?(isCentered(f)?centerMid:topLeft):f.align;
 
IFigure _translate(Figure f, Alignment align = <0.5, 0.5>) {
    if (f.size != <0, 0>) {
       f.width = f.size[0];
       f.height = f.size[1];
       }
    if (f.gap != <0, 0>) {
       f.hgap = f.gap[0];
       f.vgap = f.gap[1];
       }
    if (f.cellWidth<0) f.cellWidth = f.width;
    if (f.cellHeight<0) f.cellHeight = f.height;
    if (isEmpty(f.id)) {
         f.id = "i<occur>";
         occur = occur + 1;
         }
    // println("translate: <f.id> <f.align>");
    switch(f) {
        case emptyFigure(): return iemptyFigure();
        case box():  return _rect(f.id, f, fig = _translate(f.fig, align = nA(f, f.fig)), align = align);
        case ellipse():  return _ellipse(f.id, f, fig = _translate(f.fig, align = nA(f, f.fig)), align = align);
        case circle():  return _ellipse(f.id, f, fig = _translate(f.fig, align = nA(f, f.fig)), align = align);
        case polygon():  return _polygon(f.id, f, align = align);
        case shape(list[Vertex] _):  return _shape(f.id, f);
        case ngon():  return _ngon(f.id, f, fig = _translate(f.fig, align = nA(f, f.fig)), align = align);
        case text(value s): {if (str t:=s) return _text(f.id, f, t, align = align);
                            return iemptyFigure();
                            }                 
        case hcat(): return _hcat(f.id, f, [_translate(q, align = f.align)|q<-f.figs], align = align);
        case vcat(): return _vcat(f.id, f, [_translate(q, align = f.align)|q<-f.figs], align = align);
        case overlay(): return _overlay(f.id, f, [_translate(q)|q<-f.figs]);
        case grid(): return _grid(f.id, f, figArray= [[_translate(q, align = f.align)|q<-e]|e<-f.figArray], align = align);
        case at(int x, int y, Figure fig): {fig.at = <x, y>; return _translate(fig, align = align);}
        case atX(int x, Figure fig):	{fig.at = <x, 0>; return _translate(fig, align = align);}			
        case atY(int y, Figure fig):	{fig.at = <0, y>; return _translate(fig, align = align);}	
        }
    }
    
    
public void _render(Figure fig1, int width = 400, int height = 400, 
     Alignment align = centerMid, tuple[int, int] size = <0, 0>,
     str fillColor = "white", str lineColor = "black")
     {
        
        id = 0;
        screenHeight = height;
        screenWidth = width;
        if (size != <0, 0>) {
            screenWidth = size[0];
            screenHeight = size[1];
         }
        println("clearWidget <screenWidth> <screenHeight>");
        clearWidget();
        IFigure f = _translate(fig1, align = align);
        // println(f);
        _render(f , width = screenWidth, height = screenHeight, align = align, fillColor = fillColor, lineColor = lineColor);
     }
  
 //public void main() {
 //   clearWidget();
 //   IFigure fig0 = _rect("asbak",  emptyFigure(), fillColor = "antiquewhite", width = 50, height = 50, align = centerMid);
 //   _render(fig0);
 //   }
    