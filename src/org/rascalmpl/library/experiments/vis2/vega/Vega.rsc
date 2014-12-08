module experiments::vis2::vega::Vega
import Prelude;
import lang::json::IO;
import experiments::vis2::vega::ParameterTypes;

public data JSON 
	= null() 
	| object(map[str, JSON] properties) 
	| array(list[JSON] values) 
	| number(real n)
	| string(str s) 
	| boolean(bool b)
	| ivalue(type[value] t, value v)
	;

public data PADDING = padding(int left = 30, int bottom = 30, int top = 10, int right = 10);

  
public data VEGA =  vega(list[AXE] axes=[], list[SCALE] scales=[], 
                         list[DATUM] \data=[], PADDING padding= PADDING::padding(), 
                         list[MARK] marks = [], list[LEGEND] legends = [],
                         list[int] viewport = []);

public data AXE =   axe(str scale = "", str \type= "", map[str, value]  properties = (), str title=""
, bool grid = false, str format = "", str orient = "", int tickSize = 99999, 
  int tickPadding = 99999, int ticks = 99999, list[value] values = []);



public data MARK = mark(str name = "", str \type = "", list[MARK] marks =[], 
    map[str, value]  properties = (), DATUM from  = datum(), list[SCALE] scales=[]);
    
public data DATUM = datum(str name="", list[TRANSFORM] transform = [], str source = "", str \data="");

public data TRANSFORM = transform(str \type="", list[str] keys = [], str point ="", str height = "", str \value ="",
      str by = "", str key = "", str field = "", str expr="", str \test = "");

public data DOMAIN = ref(str \data="", str field = "");

public data RANGE = lit(str key = "")|array(list[value] values =[]);

public data SCALE = scale(str name = "", str \type = "", DOMAIN domain = ref(), RANGE range = lit(),
             bool sort = true, bool nice = false, bool zero = true, 
             bool round = false, real padding = 99999.);
             
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
         return Object(("scale":toJson(axe.scale), "type":toJson(axe.\type),
         "properties": propToJson(axe.properties), "title":toJson(axe.title)
         , "grid":toJson(axe.grid), "orient":toJson(axe.orient), "format":toJson(axe.format)
         , "tickSize":toJson(axe.tickSize,99999), "tickPadding":toJson(axe.tickPadding,99999)
         , "ticks":toJson(axe.ticks, 99999), "values":toJson(axe.values)
         ));     
     } 
 
JSON toJson(SCALE scale) {
    return Object(("name":toJson(scale.name), "type":toJson(scale.\type), "domain":toJson(scale.domain), 
            "range":toJson(scale.range), "nice":toJson(scale.nice)
            ,"zero":toJson(scale.zero), "round":toJson(scale.round)
            ,"padding":toJson(scale.padding, 99999.)
            ,"sort":toJson(scale.sort)
            ));     
       }
     
      
JSON toJson(PADDING padding) {
      return Object(("left":toJson(padding.left), "bottom":toJson(padding.bottom), "top":toJson(padding.top), "right":toJson(padding.right)));     
     } 
     
JSON toJson(TRANSFORM transform) { 
              JSON r =  Object(
                (
                "type":toJson(transform.\type)
               , "key":toJson(transform.key)
               ,"field": toJson(transform.field) 
               ,"point": toJson(transform.point)
               ,"by": toJson(transform.by)
               ,"expr": toJson(transform.expr)
               ,"test": toJson(transform.\test)
               ,"keys": toJson(transform.keys) 
               ,"height":toJson(transform.height)
               ,"value":toJson(transform.\value)
               ));           
               return r;
         }
   
 
 JSON toJson(MARK mark) { 
      return Object (("name": toJson(mark.name),"type":toJson(mark.\type), 
           "marks": toJson(mark.marks), "properties": propToJson(mark.properties),
           "from": toJson(mark.from), "scales": toJson(mark.scales)));
        }
     
JSON toJson(LEGEND legend) { 
     return Object ((
            "fill": toJson(legend.fill), 
            "shape": toJson(legend.shape),
            "stroke": toJson(legend.stroke), 
            "orient": toJson(legend.orient), 
            "title": toJson(legend.title), 
            "format": toJson(legend.format), 
            "values": toJson(legend.values), 
            "properties": propToJson(legend.properties)));       
        }
   
     
JSON toJson(map[str, value] prop) = isEmpty(prop)?null():Object((q : propToJson(prop[q])|q<-prop));
 
JSON toJson(int v) = v!=0?number(0.0+v):null();

JSON toJson(int v, int dfault) = v!=dfault?number(0.0+v):null();

JSON toJson(real v) = v!=0?number(v):null();

JSON toJson(real v, real dfault) = v<dfault?number(v):null();
 
JSON toJson(str s) {return isEmpty(s)?null():string(s);}

JSON toJson(bool b) {return b?boolean(b):null();}

JSON toJson(bool b, bool dfault) {return b!=dfault?boolean(b):null();}

JSON _toJson(list[str] a) = isEmpty(a)?null():array([toJson(q)|q<-a]); 

JSON _toJson(list[int] a) = isEmpty(a)?null():array([toJson(q)|q<-a]); 

JSON _toJson(list[real] a) = isEmpty(a)?null():array([toJson(q)|q<-a]); 

JSON _toJson(list[bool] a) = isEmpty(a)?null():array([toJson(q)|q<-a]); 

JSON _toJson(list[SCALE] a) = isEmpty(a)?null():array([toJson(q)|q<-a]);

JSON _toJson(list[AXE] a) = isEmpty(a)?null():array([toJson(q)|q<-a]);

JSON _toJson(list[MARK] a) = isEmpty(a)?null():array([toJson(q)|q<-a]);  

JSON _toJson(list[LEGEND] a) = isEmpty(a)?null():array([toJson(q)|q<-a]); 

JSON _toJson(list[TRANSFORM] a) = isEmpty(a)?null():array([toJson(q)|q<-a]);

JSON _toJson(list[DATUM] a) = isEmpty(a)?null():array([toJson(q)|q<-a]);  
 


JSON toJson(list[value] v) {
     switch (v) {
         case list[int] d:  return _toJson(d);
         case list[str] d:  return _toJson(d);
         case list[real] d:  return _toJson(d);
         case list[bool] d:  return _toJson(d);
         case list[SCALE] d:  return _toJson(d);
         case list[TRANSFORM] d:  return _toJson(d);
         case list[AXE] d:  return _toJson(d);
         case list[LEGEND] d:  return _toJson(d);
         case list[DATUM] d:  return _toJson(d);
         case list[MARK] d:  return _toJson(d);
         }
     }

    
JSON toJson(DATUM  datum) {
    return Object (("name": toJson(datum.name), "transform" : toJson(datum.transform),
         "source":toJson(datum.source), "data": toJson(datum.\data)));
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
   return Object (("axes":toJson(vega.axes), "scales":toJson(vega.scales),
            "legends":toJson(vega.legends),
            "data":toJson(vega.\data) ,  "padding": toJson(vega.padding), "marks" : toJson(vega.marks),
            "viewport" : toJson(vega.viewport)));
         } 
  
    
public str vegaToJSON(VEGA vega) {
    return toJSON(toJson(vega));
    }

// ---------------- COLORS -----------------------------------------------------------
alias Color = int;

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
    
public TRANSFORM getTransform(VEGA vega, str name) {
    visit(vega) {
          case v:transform(\type=name): return v;
          }
    return mark();
    }
    
public VEGA setTransform(VEGA vega, str name,  TRANSFORM m) {
    return visit(vega) {
          case transform(\type=name) => m
          } 
    }
  
public VEGA setScale(VEGA vega, str name, SCALE s) {
    return visit(vega) {
          case scale(name=name) => s
          } 
    }
   

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

str jsIndexOf(list[str] keys) {
           if (isEmpty(keys)) return "";
           str s = "[\"<head(keys)>\"";
           str t = "<for(q<-tail(keys)){>,\"<q>\"<}>";
           str u = s+t+"]";
           str r = "<u>.indexOf(d.data.c)";
           // println(r);
           return r;
           }
 
 // map[str, value] addProp(map[str, value] prop, map[str, map[str, value]] a)  {
 map[str, value] addProp(value prop, map[str, map[str, value]] a)  {
           // println("addProp1 <prop>");
           if (map[str, map[str, value]] m := prop) {
              m +=  a;
              // println("addProp2 <m>");
              return m;
              }
           return ();
           } 
           
 MARK lineMark(MARK m, str val) {    
        m.\type = "line";
        m.properties["enter"] = addProp(m.properties["enter"], ("stroke":
              ("scale":"color", "field":"data.c")));
        m.properties["enter"]= addProp(m.properties["enter"], ("interpolate": 
                   ("value":val)));
        return m;
        }
        
 MARK symbolMark(MARK m, str val) {        
        m.\type = "symbol";
        m.properties["enter"]= addProp(m.properties["enter"],("fill":
              ("scale":"color", "field":"data.c")));
        m.properties["enter"]= addProp(m.properties["enter"],("shape": 
                   ("value":val)));
        return m;
        }
        
MARK dupMark(MARK template, str key, str val, bool line) {
           if (key!="all")
           template.from.transform =  
              transform(\type="filter", \test = "d.data.c == \"<key>\"")
              + template.from.transform;
           MARK m = template.marks[0];
           template.marks = [line?lineMark(m, val):symbolMark(m, val)]; 
           return template;        
           }
           
VEGA update(VEGA r, bool grid = false, 
    map[str, str] title =  (), map[str, str] legends = (),
    map[str, str] format = (), map[str, int] ticks = (), map[str, list[str]] values = (),   
    map[str, TICKLABELS] tickLabels = (),
    map[str, str] interpolate = (),  map[str, str] shape = (), 
    list[str] palette = color12, list[str] groupOrder = []
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
       else {
           SCALE color = getScale(r, "color");
           color.range = array(values = hexColors(color12)); 
           r = setScale(r, "color", color);
           }       
       if (!isEmpty(interpolate) || !isEmpty(shape)) { 
        MARK mg = getMark(r, "group");
        r.marks = []; 
        if (interpolate["all"]?) {
              r.marks = r.marks + dupMark(mg, "all", interpolate["all"], true);
             }
        else r.marks = r.marks + [
            dupMark(mg, key, interpolate[key], true)|key<-interpolate
            ];   
        if (shape["all"]?) {  
             r.marks = r.marks + dupMark(mg, "all", shape["all"], false);
             }    
        else {
            println(shape);
            r.marks = r.marks + [
               dupMark(mg, key, shape[key], false)|key<-shape
               ];   
            }
        } 
        if (!isEmpty(groupOrder)) {
            TRANSFORM t = getTransform(r, "formula");
            t.expr = jsIndexOf(groupOrder);
            r = setTransform(r, "formula", t);
            } 
        return r;
    }



// -------------------------------------------------------------------------------- 

z a =    tst([1, 2, 3]);

data z = tst(list[int]);

 public void Main() {
      z t = tst([1,2,3]);
      w = visit(t) {
       case tst(list[int] q) => tst(q+4)
       }
     println(w);
     // println(toJson(b));
     }
