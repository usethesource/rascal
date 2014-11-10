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
  
public data VEGA =  vega(list[AXE] axes=[], list[SCALE] scales=[], 
                         list[DATUM] \data=[], PADDING padding= padding(), 
                         list[MARK] marks = [], list[int] viewport = []);

public data AXE =   axe(str scale = "", str \type= "", map[str, value]  properties = (), str title=""
, bool grid = false, str format = "", str orient = "", int tickSize = 99999, 
  int tickPadding = 99999);

public data PADDING = padding(int left = 30, int bottom = 30, int top = 10, int right = 10);

public data MARK = mark(str name = "", str \type = "", list[MARK] marks =[], 
    map[str, value]  properties = (), DATUM from  = datum(), list[SCALE] scales=[]);
    
public data DATUM = datum(str name="", list[TRANSFORM] transform = [], str source = "", str \data="");

public data TRANSFORM = transform(str \type="", list[str] keys = [], str point ="", str height = "", str \value ="");

public data DOMAIN = ref(str \data="", str field = "");

public data RANGE = lit(str key = "");

public data SCALE = scale(str name = "", str \type = "", DOMAIN domain = ref(), RANGE range = lit(),
             bool nice = false, bool zero = true, bool round = false, real padding = 99999.);

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
         }
     return null();
     }

JSON toJson(AXE axe) {
     switch (axe) {
         case axe() : return Object(("scale":toJson(axe.scale), "type":toJson(axe.\type),
         "properties": propToJson(axe.properties), "title":toJson(axe.title)
         , "grid":toJson(axe.grid), "orient":toJson(axe.orient), "format":toJson(axe.format)
         , "tickSize":toJson(axe.tickSize,99999), "tickPadding":toJson(axe.tickPadding,99999)
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
         case padding() : return Object(("left":toJson(padding.left), "bottom":toJson(padding.bottom), "top":toJson(padding.top), "right":toJson(padding.right)));     
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
            "data":toJson(vega.\data) ,  "padding": toJson(vega.padding), "marks" : toJson(vega.marks),
            "viewport" : toJson(vega.viewport)));
         } 
    return null();
    }
    
public str toJSON(VEGA vega) {
    return toJSON(toJson(vega));
    }
  
 public void Main() {
     println(toJson(b));
     }
