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
      int cellWidth, int cellHeight, 
      Alignment align, int hgap, int vgap];


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
     Alignment align = centerMid, int hgap = 0, int vgap = 0,
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
        '<hGap(align, hgap)>  
        '<vGap(align, vgap)>  
        ;       
        "
       , width, height, width, height, align, 0, 0 >;
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

int getCellWeight(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].cellWeight;
    return -1;
    }
    
int getCellHeight(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].cellHeight;
    return -1;
    }
    
int getHgap(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].hgap;
    return 0;
    }
    
int getVgap(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].vgap;
    return 0;
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
       
str attr(str key, str v) {
    if (isEmpty(v)) return "";
    return ".attr(\"<key>\",\"<v>\")";
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
        , width, height, f.cellWidth, f.cellHeight, align, f.hgap, f.vgap >;
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

IFigure _rect(str id, Figure f,  IFigure fig = iemptyFigure()) {
      int width = f.width;
       int height = f.height;
       Alignment align =  width<0?topLeft:f.align;
       str begintag= beginTag(id, align);
       
       str endtag = endTag();              
       widget[id] = <getCallback(f.event), seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")
        '<on(getEvent(f.event), "doFunction(\"<id>\")")>
        '<stylePx("width", f.width)><stylePx("height", height)> 
        '<style("border","<f.lineWidth<0?1:f.lineWidth>px solid <isEmpty(f.lineColor)?"black":f.lineColor>")>
        '<style("background-color", "<f.fillColor>")>  
        '<hGap(f.align, f.hgap)>  
        '<vGap(f.align, f.vgap)>    
        ';
        ", width, height, f.cellWidth, f.cellHeight, align, f.hgap, f.vgap >;
        //  '<style("opacity","<f.lineOpacity>)>
       seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       if (width<0) adjust+= "adjust1(\"<id>\", \"<getId(fig)>\");\n";
       return ifigure(id, [fig]);
       }
       
 IFigure _ellipse(str id, Figure f,  IFigure fig = iemptyFigure()) {
      str tg = "";
      switch (f) {
          case ellipse(): tg = "ellipse";
          case circle(): tg = "circle";
          }
      int width = f.width;
       int height = f.height;
       Alignment align =  f.align;
       str begintag= beginTag("<id>_", bottomLeft); 
       begintag+=
         "\<svg id=\"<id>_2\"/ width =\"<screenWidth>px\" height=\"<screenHeight>px\"\>\<<tg> id=\"<id>\"/\> 
         '\<foreignObject x=\"0\" y=\"0\" width=\"<screenWidth>px\" height=\"<screenHeight>px\"\>
         '<beginTag("<id>_1", f.align)>
         ";
       str endtag = endTag();
       endtag += "\</foreignObject\>\</svg\>"; 
       endtag += endTag(); 
       widget[id] = <getCallback(f.event), seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")
        '<on(getEvent(f.event), "doFunction(\"<id>__\")")>
        '<attr("cx", f.cx)><attr("cy", f.cy)> 
        '<ellipse():=f?"<attr("rx", f.rx)><attr("ry", f.ry)>":"<attr("r", f.r)>">
        '<style("stroke-width","<f.lineWidth>")>
        '<style("stroke","<f.lineColor>")>
        '<style("fill", "<f.fillColor>")>     
        ';
        'd3.select(\"#<id>_1\")
        '<hGap(align, f.hgap)>  
        '<vGap(align, f.vgap)> 
        '<debugStyle()>
        ; 
        ", width, height, f.cellWidth, f.cellHeight, align, f.hgap, f.vgap >;
         seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       if (width<0) {
           adjust+= "adjustSvg(\"<id>_2\", \"<id>\", <f.lineWidth<0?1:f.lineWidth>);\n";
           adjust+="adjust1(\"<id>_1\",\"<id>_\");\n";
           }
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
                
IFigure _hcat(str id, Figure f, IFigure fig1...) {
       int width = f.cellWidth;
       int height = f.cellHeight;
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
        , width, height, width, height, f.align, f.hgap, f.vgap >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       return ifigure(id ,[td("<id>_<getId(g)>", f, g, width, height)| g<-fig1]);
       }
       
IFigure _vcat(str id, Figure f, IFigure fig1...) {
       int width = f.cellWidth;
       int height = f.cellHeight;
       str begintag="                 
            '\<table\>"
           ;
       str endtag="\</table\>";
        widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")       
        '<attr("fill",f.fillColor)><attr("stroke",f.lineColor)>;
        ", width, height, width, height, f.align, f.hgap, f.vgap >;
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
       list[list[IFigure]] figArray1 = transpose(figArray);
       int cellWidth = f.cellWidth;
       int cellHeight = f.cellHeight;
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
         '<debugStyle()>; 
        ", f.width, f.height, cellWidth, cellHeight, f.align, f.hgap, f.vgap >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       widgetOrder+= id;
       list[tuple[list[IFigure] f, int idx]] fig1 = [<figArray[i], i>|int i<-[0..size(figArray)]];
       return ifigure(id, [tr("<id>_<g.idx>", f, cellWidth, cellHeight, g.f ) | g<-fig1]);
       }      
       
 IFigure td(str id, Figure f, IFigure fig1, int cellWidth, int cellHeight, bool tr = false) {
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
        ", f.width, f.height, cellWidth, cellHeight, f.align, f.hgap, f.vgap >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+= f.fillColor;
       widgetOrder+= id;
       // if (cellWidth<0) adjust+= "adjust1(\"<id>\", \"<getId(fig1)>\");\n";
    return ifigure(id, [fig1]);
    }
    
 IFigure tr(str id, Figure f, int cellWidth, int cellHeight, list[IFigure] figs) {
    str begintag = "\<tr\>";
    str endtag="\</tr\>";
    widget[id] = <null, seq, id, begintag, endtag,
        "
        ", cellWidth, cellHeight, cellWidth, cellHeight, f.align, f.hgap, f.vgap >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+= f.fillColor;
       widgetOrder+= id;
       // if (width<0) adjust+= "adjust1(\"<id>\", \"<getId(fig)>\");\n";
    return ifigure(id, [td("<id>_<getId(g)>", f, g, cellWidth,  cellHeight
    )| g<-figs]);
    }
 
IFigure _translate(Figure f) {
    if (f.size != <0, 0>) {
       f.width = f.size[0];
       f.height = f.size[1];
       }
    if (f.gap != <0, 0>) {
       f.hgap = f.gap[0];
       f.vgap = f.gap[1];
       }
    if (f.cellWidth<0) f.cellWidth = f.width;
    if (f.cellWidth<0) f.cellWidth = f.height;
    if (isEmpty(f.id)) {
         f.id = "i<occur>";
         occur = occur + 1;
         }
    switch(f) {
        case emptyFigure(): return iemptyFigure();
        case box():  return _rect(f.id, f, fig = _translate(f.fig));
        case ellipse():  return _ellipse(f.id, f, fig = _translate(f.fig));
        case circle():  return _ellipse(f.id, f, fig = _translate(f.fig));
        case text(value s): {if (str t:=s) return _text(f.id, f, t);
                            return iemptyFigure();
                            }                 
        case hcat(): return _hcat(f.id, f, [_translate(q)|q<-f.figs]);
        case vcat(): return _vcat(f.id, f, [_translate(q)|q<-f.figs]);
        case grid(): return _grid(f.id, f, figArray= [[_translate(q)|q<-e]|e<-f.figArray]);
        }
    }
    
public void _render(Figure fig1, int width = 400, int height = 400, 
     Alignment align = centerMid,
     str fillColor = "white", str lineColor = "black")
     {
        
        id = 0;
        screenHeight = height;
        screenWidth = width;
        clearWidget();
        IFigure f = _translate(fig1);
        // println(f);
        _render(f , width = width, height = height, align = align, fillColor = fillColor, lineColor = lineColor);
     }
  
 //public void main() {
 //   clearWidget();
 //   IFigure fig0 = _rect("asbak",  emptyFigure(), fillColor = "antiquewhite", width = 50, height = 50, align = centerMid);
 //   _render(fig0);
 //   }
    