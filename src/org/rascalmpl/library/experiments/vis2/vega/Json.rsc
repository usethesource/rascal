module experiments::vis2::vega::Json
import Prelude;
import lang::json::IO;

public data JSON 
	= null() 
	| object(map[str, JSON] properties) 
	| array(list[JSON] values) 
	| number(real n)
	| string(str s) 
	| boolean(bool b)
	| ivalue(type[value] t, value v)
	;

public data PADDING = paddin(int left = 30, int bottom = 30, int top = 10, int right = 10);

  
public data VEGA =  vega(list[AXE] axes=[], list[SCALE] scales=[], 
                         list[DATUM] \data=[], PADDING padding= paddin(), 
                         list[MARK] marks = [], list[LEGEND] legends = [],
                         list[int] viewport = []);

public data AXE =   axe(str scale = "", str \type= "", map[str, value]  properties = (), str title=""
, bool grid = false, str format = "", str orient = "", int tickSize = 99999, 
  int tickPadding = 99999, int ticks = 99999, list[value] values = []);



public data MARK = mark(str name = "", str \type = "", list[MARK] marks =[], 
    map[str, value]  properties = (), DATUM from  = datum(), list[SCALE] scales=[]);
    
public data DATUM = datum(str name="", list[TRANSFORM] transform = [], str source = "", str \data="");

public data TRANSFORM = transform(str \type="", list[str] keys = [], str point ="", str height = "", str \value ="");

public data DOMAIN = ref(str \data="", str field = "");

public data RANGE = lit(str key = "")|array(list[value] values =[]);

public data SCALE = scale(str name = "", str \type = "", DOMAIN domain = ref(), RANGE range = lit(),
             bool nice = false, bool zero = true, bool round = false, real padding = 99999.);
             
public data LEGEND = legend(str size = "", str shape = "", str fill = "",
                     str stroke = "", str orient = "", str title = "", 
                     str format = "", list[str] values = [], 
                     map[str, value] properties = ());

JSON Object(map[str, JSON] m) {
    // println(m);
    map[str, JSON] q = (p:m[p] | p<-m, m[p]!=null());
    return isEmpty(q)? null(): object(q);
    }
    
JSON toJson(DOMAIN domain) {
     switch (domain) {
         case ref(): return  Object(("data":toJson(domain.\data),"field":toJson(domain.field)));
         }
     return null();
     }
     
JSON toJson(RANGE range) {
     switch (range) {
         case lit() : return toJson(range.key);
         case array() : return toJson(range.values);
         }
     return null();
     }

JSON toJson(AXE axe) {
     switch (axe) {
         case axe() : return Object(("scale":toJson(axe.scale), "type":toJson(axe.\type),
         "properties": propToJson(axe.properties), "title":toJson(axe.title)
         , "grid":toJson(axe.grid), "orient":toJson(axe.orient), "format":toJson(axe.format)
         , "tickSize":toJson(axe.tickSize,99999), "tickPadding":toJson(axe.tickPadding,99999)
         , "ticks":toJson(axe.ticks, 99999), "values":toJson(axe.values)
         ));     
         }
     return null();
     }   
 
JSON toJson(SCALE scale) {
     switch (scale) {
         case scale() : return Object(("name":toJson(scale.name), "type":toJson(scale.\type), "domain":toJson(scale.domain), 
            "range":toJson(scale.range), "nice":toJson(scale.nice)
            ,"zero":toJson(scale.zero), "round":toJson(scale.round)
            ,"padding":toJson(scale.padding, 99999.)
            ));     
         }
     return null();
     } 
      
JSON toJson(PADDING padding) {
     switch (padding) {
         case paddin() : return Object(("left":toJson(padding.left), "bottom":toJson(padding.bottom), "top":toJson(padding.top), "right":toJson(padding.right)));     
         }
     return null();
     } 
     
JSON toJson(TRANSFORM transform) { 
     switch (transform) {
         case transform(): return Object(("type":string(transform.\type), "keys": toJson(transform.keys), 
               "point":toJson(transform.point),  "height":toJson(transform.height),
               "value":toJson(transform.\value)));
         }
     return null();    
     }
 
 JSON toJson(MARK mark) { 
     switch (mark) {
        case mark(): return Object (("name": toJson(mark.name),"type":toJson(mark.\type), 
           "marks": toJson(mark.marks), "properties": propToJson(mark.properties),
           "from": toJson(mark.from), "scales": toJson(mark.scales)));
        }
     return null();
     }
     
JSON toJson(LEGEND legend) { 
     switch (legend) {
        case legend(): return Object ((
            "fill": toJson(legend.fill), 
            "shape": toJson(legend.shape),
            "stroke": toJson(legend.stroke), 
            "orient": toJson(legend.orient), 
            "title": toJson(legend.title), 
            "format": toJson(legend.format), 
            "values": toJson(legend.values), 
            "properties": propToJson(legend.properties)));       
        }
     return null();
     }
     
JSON toJson(map[str, value] prop) = isEmpty(prop)?null():Object((q : propToJson(prop[q])|q<-prop));
 
JSON toJson(int v) = v!=0?number(0.0+v):null();

JSON toJson(int v, int dfault) = v!=dfault?number(0.0+v):null();

JSON toJson(real v) = v!=0?number(v):null();

JSON toJson(real v, real dfault) = v<dfault?number(v):null();
 
JSON toJson(str s) {return isEmpty(s)?null():string(s);}

JSON toJson(bool b) {return b?boolean(b):null();}

JSON toJson(bool b, bool dfault) {return b!=dfault?boolean(b):null();}

JSON toJson(list[value] a) =  isEmpty(a)?null():array([toJson(q)|q<-a]);
    
JSON toJson(DATUM  datum) {
    switch (datum) {  
    case datum(): return Object (("name": toJson(datum.name), "transform" : toJson(datum.transform),
         "source":toJson(datum.source), "data": toJson(datum.\data)));
    }
    return null();
    }
    
JSON propToJson(value v) {
    if (real r := v) {return toJson(r,99999.);}
    if (int d := v) {return toJson(d,99999);}
    if (bool b := v) {return toJson(b);}
    if (str  s := v) {return toJson(s);}
    if (map[str, value] m := v) {return toJson(m);}
    if (list[value] l := v) {return toJson(l);}
    return null();
  }
    
 //    vega(list[AXE] axes=[], list[SCALE] scales=[], list[DATUM] \data=[], PADDING padding= padding(), list[MARK] marks = []);
 
 public JSON toJson(VEGA vega) {
    switch (vega) { 
         case vega():  return Object (("axes":toJson(vega.axes), "scales":toJson(vega.scales),
            "legends":toJson(vega.legends),
            "data":toJson(vega.\data) ,  "padding": toJson(vega.padding), "marks" : toJson(vega.marks),
            "viewport" : toJson(vega.viewport)));
         } 
    return null();
    }
    
public str toJSON(VEGA vega) {
    return toJSON(toJson(vega));
    }

// ---------------- COLORS -----------------------------------------------------------
alias Color = int;

@doc{Create a fixed color palette}
public list[str] color12 = [ "red", "aqua", "navy", "violet", 
                          "yellow", "darkviolet", "maroon", "green",
                          "teal", "blue", "olive", "lime"];

@doc{Named color}
@reflect{Needs calling context when generating an exception}
@javaClass{org.rascalmpl.library.vis.util.FigureColorUtils}
public java Color color(str colorName);

@javaClass{org.rascalmpl.library.vis.util.FigureColorUtils}	
public java str getHexDecimal(Color c);

public list[str] hexColors(list[str] colors) = [getHexDecimal(color(s)) | s <- colors]; 

// Update

public VEGA setAxe(VEGA vega, str name, AXE a) {
    return visit(vega) {
          case axe(scale=name) => a
          } 
    }


public LEGEND createLegend(str k, str v, str title) {
    LEGEND r = legend();
    switch (v) {
       case "fill": r = legend(fill=k, title = title);
       case "stroke": r = legend(stroke=k, title = title);
       case "size": r = legend(size=k, title = title);
       case "shape": r = legend(shape=k, title = title);
       }
    return r; 
    }
    
public VEGA setAxe(VEGA vega, str name, AXE a) {
    return visit(vega) {
          case axe(scale=name) => a
          } 
    }


public AXE getAxe(VEGA vega, str name) {
    visit(vega) {
          case v:axe(scale=name): return v;
          }
    return axe();
    }
    
public SCALE getScale(VEGA vega, str name) {
    visit(vega) {
          case v:scale(name=name): return v;
          } 
    }
    
 public MARK getMark(VEGA vega, str name) {
    visit(vega) {
          case v:mark(\type=name): return v;
          }
    return mark();
    }
    
public VEGA setMark(VEGA vega, str name,  MARK m) {
    return visit(vega) {
          case mark(\type=name) => m
          } 
    }
  
public VEGA setScale(VEGA vega, str name, SCALE s) {
    return visit(vega) {
          case scale(name=name) => s
          } 
    }
   
     
public data TICKLABELS = tickLabels(int angle = 0,   int dx = 99999, int dy = 99999, 
       int title_dx = 99999, int title_dy = 99999,
       int fontSize = 99999, str fontStyle="italic", str fontWeight="normal",
       str fill = "black"
       );   

map [str, value] _tickLabels(str axe, TICKLABELS tickLabels) = 
                ("labels":("angle":("value":tickLabels.angle),
                 "dx":("value":tickLabels.dx),
                 "dy":("value":tickLabels.dy),
                 "fontSize":("value":tickLabels.fontSize)
                , "fontStyle":("value":tickLabels.fontStyle)
                , "fontWeight":("value":tickLabels.fontWeight)
                , "fill":("value":tickLabels.fill)
                ,"baseline":("value":"middle"), "align":("value":axe=="x"?"left":"right"))
                ,"title":("dx":("value":tickLabels.title_dx), "dy":("value":tickLabels.title_dy))
                );

VEGA update(VEGA r, bool grid = false, 
    map[str, str] title =  (), map[str, str] legends = (),
    map[str, str] format = (), map[str, int] ticks = (), map[str, list[str]] values = (),   
    map[str, TICKLABELS] tickLabels = (),
    map[str, str] interpolate = (),  map[str, str] shape = (), 
    list[str] palette = color12
    )
    {
        
        AXE ax = getAxe(r, "x"); 
        if (title["x"]?) ax.title = title["x"];
        if (format["x"]?) ax.format = format["x"];
        if (values["x"]?) ax.values = values["x"];
        if (ticks["x"]?) ax.ticks = ticks["x"];
        ax.grid = grid;
        if (tickLabels["x"]?) ax.properties += _tickLabels("x", tickLabels["x"]);           
        AXE ay = getAxe(r, "y");
        if (title["y"]?) ay.title = title["y"];
        if (format["y"]?) ay.format = format["y"];
        if (values["y"]?) ay.values = values["y"];
        if (ticks["y"]?) ay.ticks = ticks["y"];
        ay.grid = grid;  
        if (tickLabels["y"]?) ay.properties += _tickLabels("y", tickLabels["y"]);     
        r = setAxe(r, "x", ax);
        r = setAxe(r, "y", ay);
        
        r.legends = [createLegend(k, legends[k], (title[k]?)?title[k]:"")| k <-legends];
        if (!isEmpty(palette)) {
           SCALE color = getScale(r, "color");
           color.range = array(values = hexColors(palette)); 
           r = setScale(r, "color", color);
           }
       if (interpolate["all"]? || shape["all"]?) {
         MARK m1 = getMark(r, "line"); 
         MARK m2 =  getMark(r, "symbol");
         r = setMark(r, "line", mark(\type="line")); 
         r = setMark(r, "symbol", mark(\type="symbol"));     
        if (interpolate["all"]?) {
              m1.properties["enter"]+=("stroke":
              ("scale":"color", "field":"data.c"));
              m1.properties["enter"]+=("interpolate": 
                   ("value":interpolate["all"]));
              r = setMark(r, "line", m1);
             }   
        if (shape["all"]?) {          
             m2.properties["enter"]+=("fill": ("scale":"color", "field":"data.c"));    
             m2.properties+=("shape": ("value":shape["all"]));
             r = setMark(r, "symbol", m2);
             }    
        }  
        return r;
    }



// -------------------------------------------------------------------------------- 

 public void Main() {
     println(toJson(b));
     }
