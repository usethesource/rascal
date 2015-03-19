module experiments::vis2::sandbox::IFigure
import Prelude;
import util::Webserver;
import lang::json::IO;
import util::HtmlDisplay;
import util::Math;

private loc base = |std:///experiments/vis2/sandbox|;

public alias Alignment = tuple[num hpos, num vpos];

public Alignment topLeft      	= <0.0, 0.0>;
public Alignment topMid         = <0.5, 0.0>;
public Alignment topRight     	= <1.0, 0.0>;

public Alignment leftMid   		= <0.0, 0.5>;
public Alignment centerMid      = <0.5, 0.5>;
public Alignment rightMid   	= <1.0, 0.5>;

public Alignment bottomLeft   	= <0.0, 1.0>;
public Alignment bottomMid		= <0.5, 1.0>;
public Alignment bottomRight	= <1.0, 1.0>;

data Event 
	= on()
	| on(str eventName, void(str, str) callback)
	;

public data Figure = figure(str id, list[Figure] child);

public data Figure = figure(str content);

public data Figure = emptyFigure();

int seq = 0;

alias Elm = tuple[void(str, str) f, int seq, str id, str begintag, str endtag, str script, int width, int height,
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
              
str visitFig(Figure fig) {
    if (figure(str id, list[Figure] f):= fig) {
         return 
    "<widget[id].begintag> <for(d<-f){><visitFig(d)><}><widget[id].endtag>\n";
         }
    if (figure(str content):=fig) return content;
    return "";
    }
    
Figure fig;
                  
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

str innerFigureBegin(Figure fig1) {
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
     
str innerFigureEnd(Figure fig1) {    
     if (getWidth(fig1)>=0) return"\</svg\>";
     return "";
     }

public void render(Figure fig1, int width = 100, int height = 100, 
     Alignment align = centerMid,
     str fillColor = "white", str strokeColor = "black")
     {
     str id = "figureArea";
     str begintag=
     "
     ' \<svg width=\"<width>px\" height=\"<height>px\"\>
     '\<!-- \<rect id=\"<id>\" width=\"<100>%\" height=\"<100>%\" stroke=\"blue\" fill=\"none\"/\> --\>
     "; 
    begintag += innerFigureBegin(fig1);
    str endtag=innerFigureEnd(fig1); 
    endtag += 
    "
    ' \</svg\>
    ";
    widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")
        .attr(\"fill\",\"<fillColor>\").attr(\"stroke\",\"<strokeColor>\");       
        "
       , width, height, align >;
       seq=seq+1;
       state += <id, fillColor>;
       old+=fillColor;
    fig = figure(id, [fig1]);
    println("site=<site>");
	htmlDisplay(site);
}

str getId(Figure f) {
    if (figure(id, _) := f) return id;
    return "";
    }

int getWidth(Figure f) {
    if (figure(id, _) := f) return widget[id].width;
    return -1;
    }
    
int getHeight(Figure f) {
    if (figure(id, _) := f) return widget[id].height;
    return -1;
    }

Alignment getAlign(Figure f) {
    if (figure(id, _) := f) return widget[id].align;
    return <-1, -1>;
    } 
    
void(str, str) getCallback(Event e) {
    if (on(_, void(str, str) callback):=e) return callback;
    return null; 
    }
       
str getEvent(Event e) {
    if (on(str eventName, _):=e) return eventName;
    return "click"; 
    }
          
// -----------------------------------------------------------------------
Figure rect(str id, Event event = on(), int width = 50, int height = 50, str fillColor = "none",
       str strokeColor = "black", Figure fig = emptyFigure(), Alignment align = centerMid) {
       str begintag="
                    ' \<rect id=\"<id>\" width=\"100%\" height=\"100%\"/\>
                    ";  
       begintag += innerFigureBegin(fig);
       str endtag=innerFigureEnd(fig);               
       widget[id] = <getCallback(event), seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\")
        .on(\"<getEvent(event)>\", doFunction(\"<id>\"))        
        .attr(\"fill\",\"<fillColor>\").attr(\"stroke\",\"<strokeColor>\");
        ", width, height, align >;
       seq=seq+1;
       state += <id, fillColor>;
       old+=fillColor;
       return figure(id, [fig]);
       }
       
Figure hcat(str id, Figure fig1..., str fillColor = "white", str strokeColor = "black", int width = 100, int height = 100, Alignment align = centerMid, 
       int cellWidth = 100, int cellHeight = 100) {
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
        .attr(\"fill\",\"<fillColor>\").attr(\"stroke\",\"<strokeColor>\");
        ", width, height, align >;
       seq=seq+1;
       state += <id, fillColor>;
       old+=fillColor;
       // return figure(id, []);
       return figure(id, [td("<id>_<getId(f)>", f, width = cellWidth, height = cellHeight)| f<-fig1]);
       }
       
  //  style = \"border: 1px solid red\"     
 Figure td(str id, Figure fig1, int width = 50, int height = 50, Alignment align = centerMid,
 str fillColor = "white", str strokeColor = "black") {
       str begintag=
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
    widget[id] = <null, seq, id, begintag, endtag, 
        "
        'd3.select(\"#<id>\").attr(\"fill\",\"<fillColor>\")
        '.attr(\"stroke\",\"<strokeColor>\")
        ", width, height, align >;
       seq=seq+1;
       state += <id, fillColor>;
       old+= fillColor;
    return figure(id, [fig1]);
    }
