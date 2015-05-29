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
      Alignment align, int lineWidth, str lineColor, bool sizeFromParent];


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

public map[str, list[IFigure] ] defs = ();

public void clearWidget() { 
    println("clearWidget <screenWidth> <screenHeight>");
    widget = (); widgetOrder = [];adjust=[]; defs=(); 
    seq = 0; occur = 0;
    }
              
str visitFig(IFigure fig) {
    if (ifigure(str id, list[IFigure] f):= fig) {
         return 
    "<widget[id].begintag> <for(d<-f){><visitFig(d)><}><widget[id].endtag>\n";
         }
    if (ifigure(str content):=fig) return content;
    return "";
    }
    
str visitDefs(str id) {
    if (defs[id]?)
     return "\<defs\>
           ' <for (f<-defs[id]){> \<marker id=\"m_<f.id>\" 
           ' markerWidth=<getWidth(f)>  markerHeight=<getHeight(f)>
           ' refX = <getWidth(f)/2>   refY = <getHeight(f)/2> orient=\"auto\"
           ' \> 
           ' <visitFig(f)>
           ' \</marker\> <}>
           '\</defs\>
           ";
    return "";
    }
    
// ' refX = <getWidth(f)/2>   refY = <getHeight(f)/2> orient=\"auto\"
//  orient=\"auto\"
    
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
           <for (d<-widgetOrder) {> <widget[d].script> <}>
           <for (d<-reverse(adjust)) {> <d> <}>
       ' }
       ' onload=initFunction;
       '\</script\>
       '\</head\>
       '\<body\>
       ' <visitFig(fig)>      
       '\</body\>     
		'\</html\>
		";
    // println(res);
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
     Alignment align = centerMid, int lineWidth = 1, 
     str fillColor = "none", str lineColor = "black", bool display = true)
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
        '<style("stroke-width",lineWidth)>
        ;       
        "
       , width, height, width, height, align, 1, "", false >;
       seq=seq+1;
       state += <id, fillColor>;
       old+=fillColor;
       widgetOrder += id;
    fig = ifigure(id, [fig1]);
    println("site=<site>");
	if (display) htmlDisplay(site);
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
    
bool getSizeFromParent(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].sizeFromParent;
    return false;
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

str borderStyle(Figure f) {
      return 
      "<style("border-width",f.borderWidth)> 
      '<style("border-style",f.borderStyle)>
      '<style("border-color",f.borderColor)>";
     }
    
str style(str key, str v) {
    if (isEmpty(v)) return "";
    return ".style(\"<key>\",\"<v>\")";
    }
    
str style(str key, num v) {
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
        , getTextWidth(f, s), getTextHeight(f), 0, 0, align, f.lineWidth, f.lineColor, f.sizeFromParent >;
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

bool hasInnerCircle(Figure f)  {
     if (!((box():=f) || (ellipse():=f) || (circle():=f) || (ngon():=f))) return false;
     f =  f.fig;
     while (at(_, _, Figure g):= f|| atX(_,Figure g):=f || atY(_,Figure g):=f) {
          f = g;
          }
     return (circle():=f) || (ellipse():=f) || (ngon():=f);
     }
                         
 IFigure _rect(str id, Figure f,  IFigure fig = iemptyFigure(), Alignment align = <0, 0>) {   
      //if (f.lineWidth<0) {
      //     if (hasInnerCircle(f)) f.lineWidth = 2;
      //     else f.lineWidth = 1; 
      //     }  
      int lw = f.lineWidth;   
      if (isEmpty(f.fillColor)) f.fillColor = "none";
      if (isEmpty(f.lineColor)) f.lineColor = "black"; 
      
      if (getAtX(fig)>0 || getAtY(fig)>0) f.align = topLeft;
      if ((f.width<0 || f.height<0) && (getWidth(fig)<0 || getHeight(fig)<0)) 
                      f.align = centerMid;
      int offset1 = round((0.5-f.align[0])*lw);
      int offset2 = round((0.5-f.align[1])*lw);
      // println("<id>: align = <lw> <f.align> <0.5-f.align[0]>  <offset> getAtX(fig)=<getAtX(fig)>");     
      str begintag=
         "\<svg id=\"<id>_svg\"\> \<rect id=\"<id>\"/\> 
         '\<foreignObject  id=\"<id>_fo\" x=\"<getAtX(fig)+offset1>\" y=\"<getAtY(fig)+offset2>\"
         , width=\"<screenWidth>px\" height=\"<screenHeight>px\"\>
         '<beginTag("<id>_fo_table", f.align)>
         ";
        
       // println("<begintag>");
       str endtag = endTag();
       if (getWidth(fig)>0 && getHeight(fig)>0){
           if (f.width<0) f.width = getWidth(fig)+lw+getAtX(fig)+hPadding(f);
           if (f.height<0) f.height = getHeight(fig)+lw+getAtY(fig)+vPadding(f);
           }
      
       endtag += "\</foreignObject\>\</svg\>"; 
       widget[id] = <getCallback(f.event), seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")
        '<on(getEvent(f.event), "doFunction(\"<id>\")")>
        '<attr("x", 0)><attr("y", 0)> 
        '<attr("width", f.width)><attr("height", f.height)>
        '<styleInsideSvg(id, f, fig)>
        ",f.width, f.height, getAtX(f), getAtY(f), f.align, 
          f.lineWidth, f.lineColor, f.sizeFromParent >;
         seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       //if (fig!=emptyFigure())
       //        adjust+= "adjustTableW(<[getId(fig)]>, \"<id>\", <-f.lineWidth>, 
       //        <-hPadding(f)>, <-vPadding(f)>);\n";
       return ifigure(id, [fig]);
       } 
           
 
 
 str styleInsideSvgOverlay(str id, Figure f) {
      int lw = f.lineWidth<0?1:f.lineWidth; 
      return " 
        '<style("stroke-width",f.lineWidth)>
        '<style("stroke","<f.lineColor>")>
        '<style("fill", "<f.fillColor>")> 
        '<style("stroke-dasharray", cv(f.lineDashing))> 
        '<style("fill-opacity", f.fillOpacity)> 
        '<style("stroke-opacity", f.lineOpacity)>      
        ';       
        'd3.select(\"#<id>_svg\")
        '<attr("width", f.width)><attr("height", f.height)>
        ;
        ";
      } 
      
 str cv(list[int] ld) {
      if (isEmpty(ld)) return "";
      str r = "<head(ld)> <for(d<-tail(ld)){> , <d> <}>";
      return r;
      }
 
 str styleInsideSvg(str id, Figure f,  IFigure fig) {  
    int x  =getAtX(fig);
    int y = getAtX(fig);
    int lw = f.lineWidth<0?0:f.lineWidth;
    // println(f);
    // if ((ellipse():=f) || (circle():=f) || (ngon():=f)) lw = 0; 
    // int width = f.width<0?screenWidth:f.width;
    // int height = f.height<0?screenHeight:f.height;
    int width = f.width;
    int height = f.height;
    str bId = id; 
    switch (f) {
        case ngon():bId = "<id>_rect";
        case ellipse():bId = "<id>_rect";
        case circle():bId = "<id>_rect";
        }  
     int hpad = hPadding(f);
     int vpad = vPadding(f);       
    return "
        '<style("stroke-width",f.lineWidth)>
        '<style("stroke",f.lineColor)>
        '<style("stroke-dasharray", cv(f.lineDashing))> 
        '<style("fill", f.fillColor)> 
        '<style("fill-opacity", f.fillOpacity)> 
        '<style("stroke-opacity", f.lineOpacity)>      
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
        '<style("width", width)><style("height", height)>
        '<_padding(f.padding)> 
        '<debugStyle()>
        ';
        "
        + ((!isEmpty(getId(fig))&& f.width<0)?"adjust0(\"<id>\", \"<getId(fig)>\", <lw>, <hpad>, <vpad>);\n":"");     
      }

num rxL(num rx, num ry) = rx * sqrt(rx*rx+ry*ry)/ry;

num ryL(num rx, num ry) = ry * sqrt(rx*rx+ry*ry)/rx;
         
num cxL(Figure f) =  
      (((ellipse():=f)?(f.rx):(f.r)) + (f.lineWidth>=0?(f.lineWidth)/2.0:0));
num cyL(Figure f) =  
      (((ellipse():=f)?(f.ry):(f.r)) + (f.lineWidth>=0?(f.lineWidth)/2.0:0));
     
 IFigure _ellipse(str id, Figure f,  IFigure fig = iemptyFigure(), Alignment align = <0.5, 0.5>) {
      if (f.lineWidth<0) f.lineWidth = 1;
      int lw = f.lineWidth;
      str tg = "";
      switch (f) {
          case ellipse(): {tg = "ellipse"; 
                           if (f.width>=0 && f.rx<0) f.rx = (f.width-lw)/2;
                           if (f.height>=0 && f.ry<0) f.ry = (f.height-lw)/2;
                           if (f.rx<0 || f.ry<0 || getAtX(fig)>0 || getAtY(fig)>0) f.align = centerMid; 
                           bool bx  = false;          
                           if (f.rx<0 && getWidth(fig)>=0) {
                              f.rx = getAtX(fig)+(getWidth(fig)+lw+hPadding(f))/2.0;
                              bx = true;
                              }
                           bool by  = false; 
                           if (f.ry<0 && getHeight(fig)>=0) {
                              f.ry = getAtY(fig)+(getHeight(fig)+lw+vPadding(f))/2.0;
                              by = true;
                              }
                           if (!hasInnerCircle(f)&& (bx || by)) { 
                              num rx = f.rx; num ry = f.ry;
                              if (bx) {
                                  rx -= hPadding(f);
                                  f.rx = rxL(rx, ry);
                                  f.rx+= hPadding(f);
                                  }
                              if (by) {
                                  ry -= vPadding(f);
                                  f.ry = ryL(rx, ry);
                                  f.ry+= vPadding(f);
                                  }
                              }
               
                           if (f.width<0 && f.rx>=0) f.width= round(f.rx*2+lw);
                           if (f.height<0 && f.ry>=0) f.height = round(f.ry*2+lw);                         
                           }
          case circle(): {tg = "circle";
                          if (f.width>=0 && f.height>=0 && f.r<0) 
                                       f.r = (max[f.width, f.height]-lw)/2;
                          if (f.r<0 || getAtX(fig)>0 || getAtY(fig)>0) f.align = centerMid; 
                          bool b  = false;             
                          int d = max([getWidth(fig), getHeight(fig)]);
                          if (f.r<0 && d>=0) {
                               f.r = max([getAtX(fig),getAtY(fig)])+
                                     (d+lw+max([hPadding(f), vPadding(f)]))/2.0;
                               b = true;
                               }
                          if (!hasInnerCircle(f)&& b) { 
                              num r = f.r - max([hPadding(f), vPadding(f)]);
                              f.r = rxL(r, r)+ max([hPadding(f), vPadding(f)]);
                              }
                          if (f.width<0 && f.r>=0) f.width= round(f.r*2+lw);
                          if (f.height<0 && f.r>=0) f.height = round(f.r*2+lw);                 
                          }
          } 
       if (f.cx>0 || f.cy>0) {
           int x = f.at[0]+round(f.cx)-round((ellipse():=f)?f.rx:f.r)-lw/2;
           int y=  f.at[1]+round(f.cy)-round((ellipse():=f)?f.ry:f.r)-lw/2;
           f.at = <x, y>;
           }     
       str begintag =
         "\<svg id=\"<id>_svg\"\>\<rect id=\"<id>_rect\"/\> \<<tg> id=\"<id>\"/\> 
         '\<foreignObject  id=\"<id>_fo\" x=\"<getAtX(fig)>\" y=\"<getAtY(fig)>\",width=\"<screenWidth>px\" height=\"<screenHeight>px\"\>
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
        '<attr("cx", toP(cxL(f)))><attr("cy", toP(cyL(f)))> 
        '<attr("width", f.width)><attr("height", f.height)>
        '<ellipse():=f?"<attr("rx", toP(f.rx))><attr("ry", toP(f.ry))>":"<attr("r", toP(f.r))>">
        '<styleInsideSvg(id, f, fig)>
        ", f.width, f.height, getAtX(f), getAtY(f), f.align, f.lineWidth, f.lineColor, f.sizeFromParent >;
         seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       return ifigure(id, [fig]);
       }
       
num rescale(num d, Rescale s) = s[1][0] + (d-s[0][0])*(s[1][1]-s[1][0])/(s[0][1]-s[0][0]);
       
str toP(num d, Rescale s) {
        num e = rescale(d, s);
        // println("toP:<s>  <d> -\> <e>");
        num v = abs(e);
        return "<toInt(e)>.<toInt(v*10)%10><toInt(v*100)%10>";
        }
        
str toP(num d) {
        if (d<0) return "";
        return toP(d, <<0,1>, <0, 1>>);
        }
      
str translatePoints(Figure f, Rescale scaleX, Rescale scaleY) {
       Points p;
       if (polygon():=f) {
           p = f.points;         
       }
       if (ngon():=f) {
            int lw = corner(f)/2;
             num angle = 2 * PI() / f.n;
             p  = [<f.r+lw+f.r*cos(i*angle), f.r+lw+f.r*sin(i*angle)>|int i<-[0..f.n]];
             }
       return "<toP(p[0][0], scaleX)>, <toP(p[0][1], scaleY)>" + 
            "<for(t<-tail(p)){> <toP(t[0], scaleX)> , <toP(t[1], scaleY)> <}>";
       }
       
str extraCircle(str id, Figure f) {
       if (ngon():=f) {
            return "\<circle id=\"<id>_circle\"/\>";
            }
       return "";
       } 
       
int corner(Figure f) {
     return corner(f.n, f.lineWidth);
    }
    
int corner(int n, int lineWidth) {
     num angle = PI() - 2 * PI() / n;
     int lw = lineWidth<0?0:lineWidth;
     return toInt(lw/sin(0.5*angle));
    }  
   

num rR(Figure f)  = ngon():=f?f.r+corner(f)/2:-1;


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
         num width = rescale(max([p.x|p<-f.points]), f.scaleX);  
         return toInt(width);
         }

int getPolHeight(Figure f) {
         if (f.height>=0) return f.height;
         num height = rescale(max([p.y|p<-f.points]), f.scaleY);     
         return toInt(height);
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
        '<attr("points", translatePoints(f, f.scaleX, f.scaleY))>
        '<style("fill-rule", f.fillEvenOdd?"evenodd":"nonzero")>
        '<styleInsideSvgOverlay(id, f)>
        ", f.width, f.height, getAtX(f), getAtY(f),  f.align, f.lineWidth, f.lineColor, f.sizeFromParent >;
         seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       return ifigure(id, [fig]);
       }
       
 
num xV(Vertex v) = (line(num x, num y):=v)?x:((move(num x, num y):=v)?x:0);

num yV(Vertex v) = (line(num x, num y):=v)?y:((move(num x, num y):=v)?y:0);

int getPathWidth(Figure f) {
        if (shape(list[Vertex] vs):= f) {
             if (f.width>=0) return f.width;
             // num width = rescale(max([xV(p)|p<-vs]), f.scaleX)-rescale(min([xV(p)|p<-vs]),f.scaleX);     
             // return toInt(width);
             return screenWidth;
            }
         return -1;
         }

int getPathHeight(Figure f) {
         if (shape(list[Vertex] vs):= f) {
             if (f.height>=0) return f.height;
             //num height = rescale(max([yV(p)|p<-vs]), f.scaleY) - rescale(min([yV(p)|p<-vs]), f.scaleY);     
             //return toInt(height);
             return screenHeight;
         }
      }
      
str trVertices(Figure f) {
     if (shape(list[Vertex] vertices):=f) {
       return trVertices(f.vertices, shapeClosed= f.shapeClosed, shapeCurved = f.shapeCurved, shapeConnected= f.shapeConnected,
       scaleX = f.scaleX, scaleY=f.scaleY);
       }
     return emptyFigure;
     }
      
str trVertices(list[Vertex] vertices, bool shapeClosed = false, bool shapeCurved = true, bool shapeConnected = true,
    Rescale scaleX=<<0,1>, <0, 1>>, Rescale scaleY=<<0,1>, <0, 1>>) {
	//<width, height>  = bbox(vertices
	str path = "M<toP(vertices[0].x, scaleX)> <toP(vertices[0].y, scaleY)>"; // Move to start point
	int n = size(vertices);
	if(shapeConnected && shapeCurved && n > 2){
	    // println("OKOK");
		path += "Q<toP((vertices[0].x + vertices[1].x)/2.0, scaleX)> <toP((vertices[0].y + vertices[1].y)/2.0, scaleY)> <toP(vertices[1].x, scaleX)> <toP(vertices[1].y, scaleY)>";
		for(int i <- [2 ..n]){
			v = vertices[i];
			path += "<isAbsolute(v) ? "T" : "t"><toP(v.x, scaleX)> <toP(v.y, scaleY)>"; // Smooth point on quadartic curve
		}
	} else {
		for(int i <- [1 .. n]){
			v = vertices[i];
			path += "<directive(v)><toP(v.x, scaleX)> <toP(v.y, scaleY)>";
		}
	}
	
	if(shapeConnected && shapeClosed) path += "Z";
	
	return path;		   
}

bool isAbsolute(Vertex v) = (getName(v) == "line" || getName(v) == "move");

str directive(Vertex v) = ("line": "L", "lineBy": "l", "move": "M", "moveBy": "m")[getName(v)];

str mS(Figure f, str v) = ((emptyFigure():=f)?"": v);
       
IFigure _shape(str id, Figure f,  IFigure fig = iemptyFigure()) {
       // str begintag= beginTag("<id>_table", topLeft); 
       num top = 0, bottom = screenHeight, left = 0, right = screenHeight;
       if (shape(list[Vertex] vs):= f) {
           top = min([yV(p)|p<-vs]);
           bottom = max([yV(p)|p<-vs]);
           left = min([xV(p)|p<-vs]);
           right = max([xV(p)|p<-vs]);
       } 
       f.lineWidth = f.lineWidth<0?1:f.lineWidth;   
       if (f.scaleY==<<0,1>,<0,1>>) {
            f.height = toInt(bottom-top)+ 100;
            f.scaleY=<<top, bottom>,<bottom-top+50, 50>>;
            }
       if (f.scaleX==<<0,1>,<0,1>>) {
            f.width = toInt(right - left)+100;
            f.scaleX=<<left, right>,<50, right-left+50>>;
            }
       if (isEmpty(f.fillColor)) f.fillColor = "none";
       if (isEmpty(f.lineColor)) f.lineColor = "black";
       str begintag = "";
       begintag+=
         "\<svg id=\"<id>_svg\"\><visitDefs(id)>\<path id=\"<id>\"/\>       
         ";
       str endtag = "\</svg\>"; 
       widget[id] = <getCallback(f.event), seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")
        '<on(getEvent(f.event), "doFunction(\"<id>\")")>
        '<attr("d", "<trVertices(f)>")> 
        '<style("marker-start", mS(f.startMarker, "url(#m_<id>_start)"))>
        '<style("marker-mid", mS(f.midMarker, "url(#m_<id>_mid)"))>
        '<style("marker-end",  mS(f.endMarker,"url(#m_<id>_end)"))>
        '<style("fill-rule", f.fillEvenOdd?"evenodd":"nonzero")>
        '<styleInsideSvgOverlay(id, f)>
        ", f.width, f.height, getAtX(f), getAtY(f),  f.align, f.lineWidth, f.lineColor, f.sizeFromParent >;
         seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       return ifigure(id, [fig]);
       }
                 
IFigure _ngon(str id, Figure f,  IFigure fig = iemptyFigure(), Alignment align = <0.5, 0.5>) {
       int lw = f.lineWidth<0?1:f.lineWidth;
       if (f.r<0 || getAtX(fig)>0 || getAtY(fig)>0) f.align = centerMid;
       bool b  = false;             
       int d = max([getWidth(fig), getHeight(fig)]);
       if (f.r<0 && d>=0) {
                f.r = max([getAtX(fig),getAtY(fig)])+
                (d+lw+max([hPadding(f), vPadding(f)]))/2.0;
                b = true;
                }
       if (!hasInnerCircle(f)&& b) { 
             num r = f.r - max([hPadding(f), vPadding(f)]);
             f.r = rxL(r, r);
             f.r += max([hPadding(f), vPadding(f)]);
             }
       if (f.width<0 && f.r>=0) f.width= round(f.r*2+lw);
       if (f.height<0 && f.r>=0) f.height = round(f.r*2+lw);
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
        '<attr("cx", toP(cxL(f)))><attr("cy", toP(cyL(f)))><attr("r", toP(f.r))>
        
        'd3.select(\"#<id>\")
        '<on(getEvent(f.event), "doFunction(\"<id>\")")>
        '<attr("points", translatePoints(f, f.scaleX, f.scaleY))> 
        '<attr("width", f.width)><attr("height", f.height)>
        '<styleInsideSvg(id, f, fig)>
        ", f.width, f.height, getAtX(f), getAtY(f),  f.align, f.lineWidth, f.lineColor, f.sizeFromParent >;
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
       
//str vGap(Alignment align, int vgap) {
//       if (align == bottomLeft || align == bottomMid || align == bottomRight) return style("padding-bottom", vgap);
//       if (align == centerLeft || align ==centerMid || align ==centerRight)  return style("padding-top", vgap)+style("padding-bottom", vgap);
//       if (align == topLeft || align == topMid || align == topRight) return style("padding-top", vgap);
//       }
//       
//str hGap(Alignment align, int hgap) {
//       if (align == bottomLeft || align == centerLeft || align == topLeft) return style("padding-left", hgap);
//       if (align == bottomMid || align == centerMid || align == topMid) return style("padding-left", hgap)+style("padding-right", hgap);
//       if (align == bottomRight || align == centerRight || align == topRight) return style("padding-right", hgap);  
//       }
       
str _padding(tuple[int, int, int, int] p) {
       return stylePx("padding-left", p[0])+stylePx("padding-top", p[1])
             +stylePx("padding-right", p[2])+stylePx("padding-bottom", p[3]);     
       }
//   '<style("background-color", "<f.fillColor>")>;
// <style("fill","none")><style("stroke","black")><style("stroke-width",1)> 
bool isSvg(str s) =  startsWith(s, "\<svg");
   
IFigure _overlay(str id, Figure f, IFigure fig1...) {
       int lw = f.lineWidth<0?0:f.lineWidth; 
       if (f.lineWidth<0) f.lineWidth = 0;  
       if (f.width<0 && min([getWidth(g)|g<-fig1])>=0) f.width = max([getAtX(g)+getWidth(g)|g<-fig1]);
       if (f.height<0 && min([getHeight(g)|g<-fig1])>=0) f.height = max([getAtY(g)+getHeight(g)|g<-fig1]);
       if (isEmpty(f.fillColor)) f.fillColor = "none";
       if (isEmpty(f.lineColor)) f.lineColor = "black";
       str begintag =
         "\<svg id=\"<id>_svg\"\>\<rect id=\"<id>\"/\>";
       str endtag="\</svg\>";
         //   '\<p\>\</p/\>
        widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\") 
        '<attr("width", f.width)><attr("height", f.height)>  
        '<styleInsideSvgOverlay(id, f)>    
        ';
        <for (q<-fig1){> 
        'd3.select(\"#<getId(q)>_svg\")<attrPx("x", getAtX(q))><attrPx("y", getAtY(q))>   
        ;<}> 
        <for (q<-fig1){> 
         '<getSizeFromParent(q)?"adjustFrame(\"<getId(q)>\", <f.width>, <f.height>);":"">
         <}>  
        "
        , f.width, f.height, getAtX(f), getAtY(f), f.align, f.lineWidth, f.lineColor, f.sizeFromParent >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       return ifigure(id ,fig1);
       }
       
IFigure _button(str id, Figure f, str txt, bool addSvgTag) {
       int width = f.width;
       int height = f.height; 
       str begintag = "";
       if (addSvgTag) {
          begintag+=
         "\<svg id=\"<id>_svg\"\> \<rect id=\"<id>\"/\> 
         '\<foreignObject id=\"<id>_fo\" x=0 y=0, width=\"<screenWidth>px\" height=\"<screenHeight>px\"\>";
         }
       begintag+="                    
            '\<button id=\"<id>\"\>
            "
            ;
       str endtag="
            '\</button\>
            "
            ;
       if (addSvgTag) {
            endtag += "\</foreignObject\>\</svg\>"; 
          }
        widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\") 
        '<stylePx("width", width)><stylePx("height", height)>   
        '<debugStyle()>
        '<style("background-color", "<f.fillColor>")> 
        '.text(\"<txt>\")   
        ;"
        , width, height, getAtX(f), getAtY(f), f.align, f.lineWidth, f.lineColor, f.sizeFromParent >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       return ifigure(id ,[]);
       }
       
set[IFigure] getUndefCells(list[IFigure] fig1) {
     return {q|q<-fig1,(getWidth(q)<0 || getHeight(q)<0)};
     }
     
int widthDefCells(list[IFigure] fig1) {
     if (isEmpty(fig1)) return 0;
     return sum([0]+[getWidth(q)|q<-fig1,getWidth(q)>=0]);
     }

int heightDefCells(list[IFigure] fig1) {
     if (isEmpty(fig1)) return 0;
     return sum([0]+[getHeight(q)|q<-fig1,getHeight(q)>=0]);
     }    
                
IFigure _hcat(str id, Figure f, bool addSvgTag, IFigure fig1...) {
       int width = f.width;
       int height = f.height; 
       str begintag = "";
       if (addSvgTag) {
          begintag+=
         "\<svg id=\"<id>_svg\"\> \<rect id=\"<id>\"/\> 
         '\<foreignObject id=\"<id>_fo\" x=0 y=0, width=\"<screenWidth>px\" height=\"<screenHeight>px\"\>";
         }
       begintag+="                    
            '\<table id=\"<id>\" cellspacing=\"0\" cellpadding=\"0\"\>
            '\<tr\>"
            ;
       str endtag="
            '\</tr\>
            '\</table\>
            "
            ;
       if (addSvgTag) {
            endtag += "\</foreignObject\>\</svg\>"; 
            }
         //   '\<p\>\</p/\>
        widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\") 
        '<stylePx("width", width)><stylePx("height", height)>
        '<attrPx("width", width)><attrPx("height", height)>      
        '<debugStyle()>
        '<style("background-color", "<f.fillColor>")> 
        '<style("border-spacing", "<f.hgap> <f.vgap>")> 
        '<style("stroke-width",f.lineWidth)>
        '<_padding(f.padding)>     
        ;   
        "
        , width, height, getAtX(f), getAtY(f), f.align, f.lineWidth, f.lineColor, f.sizeFromParent >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       // if (width>=0 && height>=0)
       adjust+=  "adjustTableW(<[getId(c)|c<-fig1]>, \"<id>\", <f.lineWidth<0?0:-f.lineWidth>, 
               <-hPadding(f)>, <-vPadding(f)>);\n";
       return ifigure(id ,[td("<id>_<getId(g)>", f, g, width, height)| g<-fig1]);
       }
       
IFigure _vcat(str id, Figure f,  bool addSvgTag, IFigure fig1...) {
       int width = f.width;
       int height = f.height; 
       str begintag = "";
       if (addSvgTag) {
          begintag+=
         "\<svg id=\"<id>_svg\"\> \<rect id=\"<id>\"/\> 
         '\<foreignObject id=\"<id>_fo\" x=0 y=0, width=\"<screenWidth>px\" height=\"<screenHeight>px\"\>";
         }
       begintag+="                 
            '\<table id=\"<id>\" cellspacing=\"0\" cellpadding=\"0\"\>"
           ;
       str endtag="\</table\>";
        widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\") 
        '<stylePx("width", width)><stylePx("height", height)> 
        '<attrPx("width", width)><attrPx("height", height)>       
        '<attr("fill",f.fillColor)><attr("stroke",f.lineColor)>
        '<style("border-spacing", "<f.hgap> <f.vgap>")>
        '<style("stroke-width",f.lineWidth)>
        '<_padding(f.padding)> 
        ;
        ", width, height, getAtX(f), getAtY(f), f.align, f.lineWidth, f.lineColor, f.sizeFromParent >;
        if (addSvgTag) {
            endtag += "\</foreignObject\>\</svg\>"; 
            }
       seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       // if (width>=0 && height>=0)
          adjust+=  "adjustTableH(<[getId(c)|c<-fig1]>, \"<id>\", <f.lineWidth<0?0:-f.lineWidth>, 
          <-hPadding(f)>, <-vPadding(f)>);\n";
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

IFigure _grid(str id, Figure f,  bool addSvgTag, list[list[IFigure]] figArray=[[]]) {
       if (isEmpty(f.fillColor)) f.fillColor = "none";
       if (isEmpty(f.lineColor)) f.lineColor = "black";
       if (f.lineWidth<0) f.lineWidth = 1;
       list[list[IFigure]] figArray1 = transpose(figArray);
       str begintag = "";
       if (addSvgTag) {
          begintag+=
         "\<svg id=\"<id>_svg\"\> \<rect id=\"<id>\"/\> 
         '\<foreignObject id=\"<id>_fo\" x=0 y=0, width=\"<screenWidth>px\" height=\"<screenHeight>px\"\>";
         }
       begintag+="                    
            '\<table id=\"<id>\" cellspacing=\"0\" cellpadding=\"0\"\>
            ";
       str endtag="
            '\</table\>
            ";
        widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")       
         '<debugStyle()>
         '<stylePx("width", f.width)><stylePx("height", f.height)> 
         '<attrPx("width", f.width)><attrPx("height", f.height)>   
         '<style("background-color", "<f.fillColor>")>
         '<style("border-spacing", "<f.hgap> <f.vgap>")>
         '<style("stroke-width",f.lineWidth)>
         '<_padding(f.padding)> 
         '<debugStyle()>; 
        ", f.width, f.height, getAtX(f), getAtY(f), f.align, f.lineWidth, f.lineColor, f.sizeFromParent >;
        if (addSvgTag) {
            endtag += "\</foreignObject\>\</svg\>"; 
            }
       seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       list[tuple[list[IFigure] f, int idx]] fig1 = [<figArray[i], i>|int i<-[0..size(figArray)]];
       adjust+=  "adjustTableWH(<[[getId(d)|d<-c] | c<-figArray]>, \"<id>\", <-f.lineWidth>, 
          <-hPadding(f)>, <-vPadding(f)>);\n";
       return ifigure(id, [tr("<id>_<g.idx>", f, f.width, f.height, g.f ) | g<-fig1]);
       }      
 //  '<hGap(f.align, f.hgap)>  
 //       '<vGap(f.align, f.vgap)>  
   
 IFigure td(str id, Figure f, IFigure fig1, int width, int height, bool tr = false) {
    str begintag = tr?"\<tr\>":"";
    begintag +="\<td  id=\"<id>\" <vAlign(f.align)> <hAlign(f.align)>\>";   
    str endtag = "\</td\>";
    if (tr) endtag+="\</tr\>";
    widget[id] = <null, seq, id, begintag, endtag,
        "
        'd3.select(\"#<id>\")
        '<debug?debugStyle():borderStyle(f)>       
        '<style("background-color", "<f.fillColor>")>  
          
        ", f.width, f.height, getAtX(f), getAtY(f), f.align, f.lineWidth, f.lineColor, f.sizeFromParent >;
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
        ", width, height, width, height, f.align, f.lineWidth, f.lineColor, f.sizeFromParent >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+= f.fillColor;
       widgetOrder+= id;
       
       // if (width<0) adjust+= "adjust1(\"<id>\", \"<getId(fig)>\");\n";
    return ifigure(id, [td("<id>_<getId(g)>", f, g, width,  height
    )| g<-figs]);
    }
    
bool isCentered(Figure f) = (ellipse():=f) || (circle():=f) || (ngon():=f);

void addMarker(Figure f, str tg) {
   Figure g = emptyFigure();
   switch (tg) {
       case "start": g = f.startMarker;
       case "mid":   g = f.midMarker;
       case "end":   g = f.endMarker;
       }
       if (emptyFigure()!:=g) {
         g.id = "<f.id>_<tg>";
         list[IFigure] fs = (defs[f.id]?)?defs[f.id]:[];
         fs +=_translate(g, align = centerMid);
         defs[f.id] = fs;
       }
   }
    
Alignment nA(Figure f, Figure fig) =  
     (f.width<0 || f.height<0 || getAtX(fig)>0 || getAtY(fig)>0)?(isCentered(f)?centerMid:topLeft):f.align;
 
IFigure _translate(Figure f, Alignment align = <0.5, 0.5>, bool addSvgTag = false) {
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
        case frame():  {
                   f.sizeFromParent = true;
                   f.lineWidth = 0; f.fillColor="none";
                   return _rect(f.id, f, fig = _translate(f.fig, align = nA(f, f.fig)), align = align);
                   }
        case ellipse():  return _ellipse(f.id, f, fig = _translate(f.fig, align = nA(f, f.fig)), align = align);
        case circle():  return _ellipse(f.id, f, fig = _translate(f.fig, align = nA(f, f.fig)), align = align);
        case polygon():  return _polygon(f.id, f, align = align);
        case shape(list[Vertex] _):  {
                       addMarker(f, "start");
                       addMarker(f, "mid");
                       addMarker(f, "end");
                       return _shape(f.id, f);
                       }
        case ngon():  return _ngon(f.id, f, fig = _translate(f.fig, align = nA(f, f.fig)), align = align);
        case text(value s): {if (str t:=s) return _text(f.id, f, t, align = align);
                            return iemptyFigure();
                            }                 
        case hcat(): return _hcat(f.id, f, addSvgTag, [_translate(q, align = f.align)|q<-f.figs], align = align);
        case vcat(): return _vcat(f.id, f, addSvgTag, [_translate(q, align = f.align)|q<-f.figs], align = align);
        case overlay(): return _overlay(f.id, f, [_translate(q, addSvgTag = true)|q<-f.figs]);
        case grid(): return _grid(f.id, f, addSvgTag, figArray= [[_translate(q, align = f.align)|q<-e]|e<-f.figArray], align = align);
        case at(int x, int y, Figure fig): {fig.at = <x, y>; return _translate(fig, align = align);}
        case atX(int x, Figure fig):	{fig.at = <x, 0>; return _translate(fig, align = align);}			
        case atY(int y, Figure fig):	{fig.at = <0, y>; return _translate(fig, align = align);}	
        case button(str txt):  return _button(f.id, f,  txt, addSvgTag, align = align);
        }
    }
    
    
public void _render(Figure fig1, int width = 400, int height = 400, 
     Alignment align = centerMid, tuple[int, int] size = <0, 0>,
     str fillColor = "white", str lineColor = "black", bool display = true)
     {
        
        id = 0;
        screenHeight = height;
        screenWidth = width;
        if (size != <0, 0>) {
            screenWidth = size[0];
            screenHeight = size[1];
         }
       //if (fig1.lineWidth<0) {
       //    if (hasInnerCircle(fig1)) fig1.lineWidth = 2;
       //    else fig1.lineWidth = 1; 
       //    } 
        clearWidget();
        if (at(_, _, _):= fig1 || atX(_,_):=fig1 || atY(_,_):=fig1 
        // || fig1.height<0 || fig1.width<0 
        ){
             align = topLeft; 
             }    
        IFigure f = _translate(fig1, align = align);
        // println(f);
        _render(f , width = screenWidth, height = screenHeight, align = align, fillColor = fillColor, lineColor = lineColor
        , display = display);
     }
  
 //public void main() {
 //   clearWidget();
 //   IFigure fig0 = _rect("asbak",  emptyFigure(), fillColor = "antiquewhite", width = 50, height = 50, align = centerMid);
 //   _render(fig0);
 //   }
    