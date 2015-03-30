module experiments::vis2::sandbox::IFigure
import Prelude;
import util::Webserver;
import lang::json::IO;
import util::HtmlDisplay;
import util::Math;
import experiments::vis2::sandbox::Figure;
import util::Reflective;

private loc base = |std:///experiments/vis2/sandbox|;



// public alias Alignment = tuple[num hpos, num vpos];

//public Alignment topLeft      	= <0.0, 0.0>;
//public Alignment topMid         = <0.5, 0.0>;
//public Alignment topRight     	= <1.0, 0.0>;
//
//public Alignment leftMid   		= <0.0, 0.5>;
//public Alignment centerMid      = <0.5, 0.5>;
//public Alignment rightMid   	= <1.0, 0.5>;
//
//public Alignment bottomLeft   	= <0.0, 1.0>;
//public Alignment bottomMid		= <0.5, 1.0>;
//public Alignment bottomRight	= <1.0, 1.0>;

//data IEvent 
//	= on()
//	| on(str eventName, void(str, str) callback)
//	;

public data IFigure = ifigure(str id, list[IFigure] child);

public data IFigure = ifigure(str content);

public data IFigure = iemptyFigure();

int seq = 0;
int occur = 0;


alias Elm = tuple[void(str, str) f, int seq, str id, str begintag, str endtag, str script, int width, int height,
      int w, int h, 
      Alignment align, int hgap, int vgap];


alias State = tuple[str name, str v];
public list[State] state = [];
public list[str] old = [];

public void null(str event, str id ){return;}
      
int getInt(str id) = toInt(state[widget[id].seq].v);

void setInt(str id, int v) {state[widget[id].seq].v = "<v>";}

str getStr(str id) = state[widget[id].seq].v;

void setStr(str id, str v) {state[widget[id].seq].v =  v;}

public map[str, Elm] widget = ();

public void clearWidget() { widget = ();}
              
str visitFig(IFigure fig) {
    if (ifigure(str id, list[IFigure] f):= fig) {
         return 
    "<widget[id].begintag> <for(d<-f){><visitFig(d)><}><widget[id].endtag>\n";
         }
    if (ifigure(str content):=fig) return content;
    return "";
    }
    
IFigure fig;
                  
str getIntro() {
   res = "\<html\>
        '\<head\>
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
           <for (d<-widget) {> <widget[d].script> <}>
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

str innerFigureBegin(IFigure fig1, int width, int height, Alignment align, int xgap, int ygap) {
     println("inner id:<getId(fig1)> width(fig1): <getWidth(fig1)> width:<width>");
     if (getWidth(fig1)>=0) {
       int hgap = align.hpos<0.25?cX(xgap, width):cX(-xgap, width);
       int vgap = align.vpos<0.25?cY(ygap, height):cY(-ygap, height);
       num x =  hgap + align.hpos * (100 - cX(getWidth(fig1), width));
       num y =  vgap + align.vpos * (100 - cY(getHeight(fig1), height));
       println("x = <x>  y = <y>  <align>");
       return
       "
       '\<svg x=\"<x>%\" y =\"<y>%\" width=\"<cX(getWidth(fig1), width)>%\" height=\"<cY(getHeight(fig1), height)>%\"\>"
       ;  
       } 
     return "";
     }
          
str innerFigureEnd(IFigure fig1) {    
     if (getWidth(fig1)>=0) return"\</svg\>";
     return "";
     }
     

     
int cX(int x, int parentW) = 100*x/parentW;

int cY(int y, int parentH) = 100*y/parentH;
     
public void _render(IFigure fig1, int width = 1000, int height = 1000, 
     Alignment align = centerMid, int hgap = 0, int vgap = 0,
     str fillColor = "none", str lineColor = "black")
     {
     str id = "figureArea";
     str begintag=
     "
     ' \<svg width=\"<width>px\" height=\"<height>px\"\>
     '\<rect id=\"<id>\" width=\"<100>%\" height=\"<100>%\" stroke-width =\"2\"/\>
     "; 
    println(getWidth(fig1));
    begintag += innerFigureBegin(fig1, width, height, align, hgap, vgap);
    str endtag=innerFigureEnd(fig1); 
    endtag += 
    "
    ' \</svg\>
    ";
    widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")
        .attr(\"fill\",\"<fillColor>\").attr(\"stroke\",\"<lineColor>\");       
        "
       , width, height, width, height, align, 0, 0 >;
       seq=seq+1;
       state += <id, fillColor>;
       old+=fillColor;
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

int getW(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].w;
    return -1;
    }
    
int getH(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].h;
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
IFigure _rect(str id, Figure f,  IFigure fig = iemptyFigure()) {
       str begintag="
                    ' \<rect id=\"<id>\" width=\"100%\" height=\"100%\"/\>"
                    ;
       // str begintag = "";  
       int width = f.width<0?getWidth(fig):f.width;
       int height = f.height<0?getHeight(fig) :f.height;
       //int w = f.w <0 ? 50 : f.w;
       //int h = f.h <0 ? 50 : f.h;
       begintag += innerFigureBegin(fig, width, height, f.align, f.hgap, f.vgap);
       str endtag=innerFigureEnd(fig);               
       widget[id] = <getCallback(f.event), seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")
        '<on(getEvent(f.event), "doFunction(\"<id>\")")>       
        '<attr("fill",f.fillColor)><attr("stroke",f.lineColor)><attr("stroke-width",f.lineWidth)>
        '<attr("stroke-opacity",f.lineOpacity)>
        ';
        ", width, height, f.w, f.h, f.align, f.hgap, f.vgap >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       return ifigure(id, [fig]);
       }
       
str valign(Alignment align) {
       if (align == bottomLeft || align == bottomMid || align == bottomRight) return "vertical-align:bottom";
       if (align == centerLeft || align ==centerMid || align ==centerRight) return "vertical-align:middle";
       if (align == topLeft || align == topMid || align == topRight) return "vertical-align:top";
       }
           
IFigure _hcat(str id, Figure f, IFigure fig1...) {
       println("width:<getWidth(fig1[0])>");
       int w = f.w<0?sum([getW(g)|g<-fig1])+size(fig1)*5+4:f.w*size(fig1);
       int h = f.h<0?max([getH(g)|g<-fig1])+8:f.h;
       int width = f.width < 0 ? w : f.width;
       int height = f.height < 0 ? h : f.height;
       str begintag="
            '\<foreignObject  width=\"100%\" height = \"100%\" x=\"0\" y=\"0\" \>
            '\<!-- \<body xmlns=\"http://www.w3.org/1999/xhtml\"\>--\>                     
            '\<table style=\"border:1px solid black\"\>
            '\<tr\>"
            ;
       str endtag="
            '\</tr\>
            '\</table\>
            '\<p\>\</p/\>
            ' \<!--\</body \>--\>
            '\</foreignObject\>"
            ;
        widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")       
        '<attr("fill",f.fillColor)><attr("stroke",f.lineColor)>;
        ", width, height, w, h, f.align, f.hgap, f.vgap >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       // return ifigure(id, []);
       return ifigure(id ,[td("<id>_<getId(g)>", f, g, max([f.width, getW(g)]), max([f.height, getH(g)]))| g<-fig1]);
       }
       
IFigure _vcat(str id, Figure f, IFigure fig1...) {
       int w = f.w<0?max([getW(g)|g<-fig1])+10:f.w;
       int h = (f.h<0?sum([getH(g)|g<-fig1]):f.h*size(fig1))+size(fig1)*20;
       int width = f.width < 0 ? w : f.width;
       int height = f.height < 0 ? h : f.height;
       str begintag="
            '\<foreignObject  width=\"100%\" height = \"100%\" x=\"0\" y=\"0\" \>
            '\<!-- \<body xmlns=\"http://www.w3.org/1999/xhtml\"\>--\>                     
            '\<table style=\"border:1px solid black\"\>"
           ;
       str endtag="
            '\</table\>
            '\<p\>\</p/\>
            ' \<!--\</body \>--\>
            '\</foreignObject\>"
            ;
        widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")       
        '<attr("fill",f.fillColor)><attr("stroke",f.lineColor)>;
        ", width, height, w, h, f.align, f.hgap, f.vgap >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       // return ifigure(id, []);
       return ifigure(id, [td("<id>_<getId(g)>", f, g, max([f.width, getW(g)]), max([f.height, getH(g)]),tr = true)| g<-fig1]);
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
       f.w = -1; f.h = -1;
       int w = max([max([getW(g)|g<-fig1])|fig1<-figArray])*size(figArray1)+max([size(fig1)*20|fig1<-figArray]);
       int h = max ([max([getH(g)|g<-fig1])|fig1<-figArray1])*size(figArray)+max([size(fig1)*20|fig1<-figArray1]);
       println("h=<h>"); println("w=<w>"); 
       int width = f.width < 0 ? w : f.width;
       int height = f.height < 0 ? h : f.height;
       str begintag="
            '\<foreignObject  width=\"100%\" height = \"100%\" x=\"0\" y=\"0\" \>
            '\<!-- \<body xmlns=\"http://www.w3.org/1999/xhtml\"\>--\>                     
            '\<table style=\"border:1px solid black\"\>
            ";
       str endtag="
            '\</table\>
            '\<p\>\</p/\>
            ' \<!--\</body \>--\>
            '\</foreignObject\>
            ";
        widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")       
        '<attr("fill",f.fillColor)><attr("stroke",f.lineColor)>;
        ", w, h, w, h, f.align, f.hgap, f.vgap >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+=f.fillColor;
       // return ifigure(id, []);
       list[tuple[list[IFigure] f, int idx]] fig1 = [<figArray[i], i>|int i<-[0..size(figArray)]];
       return ifigure(id, [tr("<id>_<g.idx>", f, 
                 sum([getW(d)|d<-g.f])+10*size(g.f), 
                 max([getH(d)|d<-g.f])+20, g.f)| g<-fig1]);
       }      
       
 IFigure td(str id, Figure f, IFigure fig1, int width, int height, bool tr = false) {
       str begintag = tr?"\<tr\>":"";
       begintag +=
     " \<td style=\"<valign(f.align)>\"\>
     ' \<svg width=\"<width>px\" height=\"<height>px\"\>
     ' \<rect id=\"<id>\" width=\"<100>%\" x=\"0\" y=\"0\" height=\"<100>%\" stroke=\"black\" fill=\"none\"/\>"
     ; 
    begintag += innerFigureBegin(fig1, width, height, f.align, f.hgap, f.vgap);
    str endtag=innerFigureEnd(fig1);
    endtag += 
    "
    '\</svg\>\</td\>"
    ;
    if (tr) endtag+="\</tr\>";
    widget[id] = <null, seq, id, begintag, endtag,
        "
        'd3.select(\"#<id>\")
        '<attr("fill",f.fillColor)><attr("stroke",f.lineColor)>
        ", width, height, width, height, f.align, f.hgap, f.vgap >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+= f.fillColor;
    return ifigure(id, [fig1]);
    }
    
 IFigure tr(str id, Figure f, int width, int height, list[IFigure] figs) {
    str begintag = "\<tr\>";
    str endtag="\</tr\>";
    widget[id] = <null, seq, id, begintag, endtag,
        "
        ", width, height, width, height, f.align, f.hgap, f.vgap >;
       seq=seq+1;
       state += <id, f.fillColor>;
       old+= f.fillColor;
    return ifigure(id, [td("<id>_<getId(g)>", f, g, getW(g),  getH(g)
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
    if (f.w<0) f.w = f.width;
    if (f.h<0) f.h = f.height;
    if (isEmpty(f.id)) {
         f.id = "i<occur>";
         occur = occur + 1;
         }
    switch(f) {
        case emptyFigure(): return iemptyFigure();
        case box():  return _rect(f.id, f, fig = _translate(f.fig));
        case hcat(): return _hcat(f.id, f, [_translate(q)|q<-f.figs]);
        case vcat(): return _vcat(f.id, f, [_translate(q)|q<-f.figs]);
        case grid(): return _grid(f.id, f, figArray= [[_translate(q)|q<-e]|e<-f.figArray]);
        }
    }
    
public void _render(Figure fig1, int width = 400, int height = 400, 
     Alignment align = centerMid,
     str fillColor = "white", str lineColor = "black")
     {
        clearWidget();
        id = 0;
        screenHeight = height;
        screenWidth = width;
        IFigure f = _translate(fig1);
        // println(f);
        _render(f , width = width, height = height, align = align, fillColor = fillColor, lineColor = lineColor);
     }