module experiments::vis2::sandbox::IFigure
import Prelude;
import util::Webserver;
import lang::json::IO;
import util::HtmlDisplay;
import util::Math;
import experiments::vis2::sandbox::Figure;

private loc base = |std:///experiments/vis2/sandbox|;

// public alias Alignment = tuple[num hpos, num vpos];

public Alignment topLeft      	= <0.0, 0.0>;
public Alignment topMid         = <0.5, 0.0>;
public Alignment topRight     	= <1.0, 0.0>;

public Alignment leftMid   		= <0.0, 0.5>;
public Alignment centerMid      = <0.5, 0.5>;
public Alignment rightMid   	= <1.0, 0.5>;

public Alignment bottomLeft   	= <0.0, 1.0>;
public Alignment bottomMid		= <0.5, 1.0>;
public Alignment bottomRight	= <1.0, 1.0>;

data IEvent 
	= on()
	| on(str eventName, void(str, str) callback)
	;

public data IFigure = ifigure(str id, list[IFigure] child);

public data IFigure = ifigure(str content);

public data IFigure = iemptyFigure();

int seq = 0;

alias Elm = tuple[void(str, str) f, int seq, str id, str begintag, str endtag, str script, int width, int height,
      int w, int h, 
      Alignment align];


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

str innerFigureBegin(IFigure fig1) {
     println("inner:<getId(fig1)> <getWidth(fig1)>");
     if (getWidth(fig1)>=0) {
       num x =  getAlign(fig1).hpos * (100 - getWidth(fig1));
       num y =  getAlign(fig1).vpos * (100 -getHeight(fig1));
       return
       "
       ' \<svg x=\"<toInt(x)>%\" y =\"<toInt(y)>%\" width=\"<getWidth(fig1)>%\" height=\"<getHeight(fig1)>%\"\>
       ";  
       } 
     return "";
     }
     
str innerFigureEnd(IFigure fig1) {    
     if (getWidth(fig1)>=0) return"\</svg\>";
     return "";
     }
     
public void render(Figure fig1, int width = 100, int height = 100, 
     Alignment align = centerMid,
     str fillColor = "white", str lineColor = "black")
     {
        clearWidget();
        _render(_translate(fig1), width = width, height = height, align = align, fillColor = fillColor, lineColor = lineColor);
     }

public void _render(IFigure fig1, int width = 100, int height = 100, 
     Alignment align = centerMid,
     str fillColor = "white", str lineColor = "black")
     {
     str id = "figureArea";
     str begintag=
     "
     ' \<svg width=\"<width>px\" height=\"<height>px\"\>
     '\<!-- \<rect id=\"<id>\" width=\"<100>%\" height=\"<100>%\" stroke=\"blue\" fill=\"none\"/\> --\>
     "; 
    println(getWidth(fig1));
    begintag += innerFigureBegin(fig1);
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
       , width, height, width, height, align >;
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
    
Alignment getAlign(IFigure f) {
    if (ifigure(id, _) := f) return widget[id].align;
    return <-1, -1>;
    } 
    
void(str, str) getCallback(IEvent e) {
    if (on(_, void(str, str) callback):=e) return callback;
    return null; 
    }
       
str getEvent(IEvent e) {
    if (on(str eventName, _):=e) return eventName;
    return "click"; 
    }
          
// -----------------------------------------------------------------------
IFigure _rect(str id, IEvent event = on(), int width = 50, int height = 50, str fillColor = "none",
       str lineColor = "black", IFigure fig = iemptyFigure(), Alignment align = centerMid,
       int h = 50, int w = 50) {
       str begintag="
                    ' \<rect id=\"<id>\" width=\"100%\" height=\"100%\"/\>
                    ";  
       begintag += innerFigureBegin(fig);
       str endtag=innerFigureEnd(fig);               
       widget[id] = <getCallback(event), seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")
        .on(\"<getEvent(event)>\", doFunction(\"<id>\"))        
        .attr(\"fill\",\"<fillColor>\").attr(\"stroke\",\"<lineColor>\");
        ", width, height, w, h, align >;
       seq=seq+1;
       state += <id, fillColor>;
       old+=fillColor;
       return ifigure(id, [fig]);
       }
           
IFigure _hcat(str id, IFigure fig1..., str fillColor = "white", str lineColor = "black", int width = 100, int height = 100, Alignment align = centerMid, 
       int w = -1, int h = -1) {
       println("width:<getWidth(fig1[0])>");
       int w1 = w<0?sum([getW(f)|f<-fig1])+size(fig1)*5:w;
       int h1 = h<0?max([getH(f)|f<-fig1])+5:h;
       str begintag="
            '\<foreignObject  width=\"100%\" height = \"100%\" x=\"0\" y=\"0\" \>
            '\<!-- \<body xmlns=\"http://www.w3.org/1999/xhtml\"\>--\>                     
            '\<table style=\"border:1px solid black\"\>
            '\<tr\>
            ";
       str endtag="
            '\</tr\>
            '\</table\>
            '\<p\>\</p/\>
            ' \<!--\</body \>--\>
            '\</foreignObject\>
            ";
        widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")       
        .attr(\"fill\",\"<fillColor>\").attr(\"stroke\",\"<lineColor>\");
        ", width, height, w1, h1, align >;
       seq=seq+1;
       state += <id, fillColor>;
       old+=fillColor;
       // return ifigure(id, []);
       return ifigure(id, [td("<id>_<getId(f)>", f, width = max([w, getW(f)]), height = max([h, getH(f)]))| f<-fig1]);
       }
       
IFigure _vcat(str id, IFigure fig1..., str fillColor = "white", str lineColor = "black", int width = 100, int height = 100, Alignment align = centerMid, 
       int w = -1, int h = -1) {
       int w1 = w<0?max([getW(f)|f<-fig1])+size(fig1)*5:w;
       int h1 = h<0?sum([getH(f)|f<-fig1])+5:h;
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
        .attr(\"fill\",\"<fillColor>\").attr(\"stroke\",\"<lineColor>\");
        ", width, height, w1, h1, align >;
       seq=seq+1;
       state += <id, fillColor>;
       old+=fillColor;
       // return ifigure(id, []);
       return ifigure(id, [td("<id>_<getId(f)>", f, tr = true, width = max([w, getW(f)]), height = max([h, getH(f)]))| f<-fig1]);
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

IFigure _grid(str id, list[list[IFigure]] figArray=[[]], str fillColor = "white", str lineColor = "black", int width = 100, int height = 100, Alignment align = centerMid, 
       int w = -1, int h = -1) {
       list[list[IFigure]] figArray1 = transpose(figArray);
       int w1 = w<0?max([sum([getW(f)|f<-fig1])|fig1<-figArray]):w;
       int h1 = h<0?max ([sum([getH(f)|f<-fig1])|fig1<-figArray1]): h;
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
        .attr(\"fill\",\"<fillColor>\").attr(\"stroke\",\"<lineColor>\");
        ", width, height, w1, h1, align >;
       seq=seq+1;
       state += <id, fillColor>;
       old+=fillColor;
       // return ifigure(id, []);
       list[tuple[list[IFigure] f, int idx]] fig1 = [<figArray[i], i>|int i<-[0..size(figArray)]];
       return ifigure(id, [tr("<id>_<f.idx>", f.f, 
                 width = max([max([w, getW(d)])|d<-f.f]), 
                 height = max([max([h, getH(d)])|d<-f.f])
       ,fillColor = fillColor, lineColor = lineColor)| f<-fig1]);
       }      
       
 IFigure td(str id, IFigure fig1, bool tr = false, int width = 50, int height = 50, Alignment align = centerMid,
 str fillColor = "white", str lineColor = "black") {
       str begintag = tr?"\<tr\>":"";
       begintag +=
     "
     ' \<td\>
     ' \<svg width=\"<width>px\" height=\"<height>px\"\>
     ' \<rect id=\"<id>\" width=\"<100>%\" x=\"0\" y=\"0\" height=\"<100>%\" stroke=\"black\" fill=\"none\"/\>
     "; 
    begintag += innerFigureBegin(fig1);
    str endtag=innerFigureEnd(fig1);
    endtag += 
    "
    ' \</svg\>\</td\>
    ";
    if (tr) endtag+="\</tr\>";
    widget[id] = <null, seq, id, begintag, endtag,
        "
        'd3.select(\"#<id>\").attr(\"fill\",\"<fillColor>\")
        '.attr(\"stroke\",\"<lineColor>\")
        ", width, height, width, height, align >;
       seq=seq+1;
       state += <id, fillColor>;
       old+= fillColor;
    return ifigure(id, [fig1]);
    }
    
 IFigure tr(str id, list[IFigure] figs, int width = 50, int height = 50, Alignment align = centerMid,
    str fillColor = "white", str lineColor = "black") {
    str begintag = "\<tr\>";
    str endtag="\</tr\>";
    widget[id] = <null, seq, id, begintag, endtag,
        "
        ", width, height, width, height, align >;
       seq=seq+1;
       state += <id, fillColor>;
       old+= fillColor;
    return ifigure(id, [td("<id>_<getId(f)>", f, width = max([width, getW(f)]), height = max([height, getH(f)])
    ,fillColor = fillColor, lineColor = lineColor)| f<-figs]);
    }
 
IFigure _translate(Figure f) {
    if (f.size != <0, 0>) {
       f.width = f.size[0];
       f.height = f.size[1];
       }
    switch(f) {
        case emptyFigure(): return iemptyFigure();
        case box(): return _rect(f.id, fig = 
                        _translate(f.fig)
                         ,width = f.width, height = f.height,
                         align = f.align, h = f.h, w = f.w, fillColor = f.fillColor,
                         lineColor = f.lineColor);
        case hcat(): return _hcat(f.id, [_translate(q)|q<-f.figs]
                          // ,width = f.width>0?f.width height = f.height,
                         align = f.align, h = f.h, w = f.w, fillColor = f.fillColor,
                         lineColor = f.lineColor);
        }
    }