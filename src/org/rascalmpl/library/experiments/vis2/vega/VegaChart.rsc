module experiments::vis2::vega::VegaChart

import experiments::vis2::vega::Json;

import Prelude;

public VEGA setAxe(VEGA vega, str name, AXE a) {
    return visit(vega) {
          case axe(scale=name) => a
          } 
    }


public AXE getAxe(VEGA vega, str name) {
    visit(vega) {
          case v:axe(scale=name): return v;
          } 
    }
    
public SCALE getScale(VEGA vega, str name) {
    visit(vega) {
          case v:scale(name=name): return v;
          } 
    }
  

public void Main() {
     }      

VEGA  _stackedBar = vega(
            viewport = [1800, 1800]
             ,
            axes= [
                axe(scale="x", \type="x"
                , properties =("labels":("angle":("value":90),"dx":("value":1)
                ,"baseline":("value":"middle"), "align":("value":"left"))
                ,"title":("dy":("value":50))
                )
                )
                ,
                axe(scale = "y", \type ="y",
                properties =("labels":("fill":("value":"green")),
                "title":("dy":("value":-30))
                ))
                ]
            ,
            scales = 
                [scale(name="x", \type = "ordinal", 
                   domain = DOMAIN::ref(\data = "table", field = "data.x"),
                   range = RANGE::lit(key= "width")
                 ),
                 scale(name = "y", \type = "linear",
                   domain = DOMAIN::ref(\data="stats", field = "sum"),
                   range = RANGE::lit(key="height")
                   ,nice = true
                 ),
                 scale(name="color", \type = "ordinal",
                   range = RANGE::lit(key="category20")
                 )
                ]
               ,
              \data =[datum(name="table"),
                      datum(name="stats", 
                          transform= [
                              transform(keys=["data.x"],                       
                                \type = "facet"),      
                              transform(\value="data.y",
                                \type = "stats")
                              ],                     
                          source= "table")
                   ]
                   ,
             padding= padding(left=100, bottom = 30, top = 10, right = 10),
             marks = [mark(\type = "group",
                         marks=[
                             mark(\type="rect",
                                  properties = (
                                     "enter":(
                                       "fill":("scale":"color", "field":"data.c"),
                                       "width":("scale":"x", "offset":-1.0
                                       , "band":true
                                       ),
                                      "x":("scale":"x","field":"data.x"),
                                      "y":("scale":"y","field":"y"),
                                      "y2":("scale":"y","field":"y2")
                                      ),
                                     "update":  ("fillOpacity":("value":1.0)),
                                     "hover":   ("fillOpacity":("value":0.5))   
                                  )
                               
                                )
                             ]
                          ,
                        from = datum(\data="table",
                               transform = [
                                  transform(keys = ["data.c"],
                                    \type = "facet"),
                                  transform(point = "data.x", 
                                    height = "data.y",
                                    \type = "stack")
                               ] 
                         
                         )                       
                   )
                   ]
                  
                ); 
 
 public VEGA() stackedBar(bool grid = false) {
    return VEGA() {
        VEGA r = _stackedBar;
        AXE a = getAxe(r, "x");
        a.grid = grid;
        r = setAxe(r, "x", a);
        return r;
        };
    }
    
public VEGA stedenBar() {
    VEGA r = groupedBar();
    AXE a = getAxe(r, "x");
    map[str, value] p = a.properties;
    p["labels"] = ("angle":("value":90),"dx":("value":1)
          ,"baseline":("value":"middle"), "align":("value":"left"));
    p["title"] = ("dy":("value":50));
    a.properties = p;
    r= setAxe(r, "x", a);
    return r;
    }
               

 VEGA  _stackedArea = vega(
            viewport = [1800, 1800]
             ,
            axes= [
                axe(scale="x", \type="x", grid = true)
                ,
                axe(scale = "y", \type ="y", grid = true
                //, properties =("labels":("fill":("value":"green")),
                // "title":("dy":("value":-30)))
                  )
                ]
            ,
            scales = 
                [scale(name="x", \type = "linear", 
                   domain = DOMAIN::ref(\data = "table", field = "data.x"),
                   range = RANGE::lit(key= "width")
                 ),
                 scale(name = "y", \type = "linear",
                   domain = DOMAIN::ref(\data="stats", field = "sum"),
                   range = RANGE::lit(key="height")
                   ,nice = true
                 ),
                 scale(name="color", \type = "ordinal",
                   range = RANGE::lit(key="category10")
                 )
                ]
               ,
              \data =[datum(name="table"),
                      datum(name="stats", 
                          transform= [
                              transform(keys=["data.x"],                       
                                \type = "facet"),      
                              transform(\value="data.y",
                                \type = "stats")
                              ],                     
                          source= "table")
                   ]
                   ,
             padding= padding(left=100, bottom = 30, top = 10, right = 10),
             marks = [mark(\type = "group",
                         marks=[
                             mark(\type="area",
                                  properties = (
                                     "enter":(
                                      "fill":("scale":"color", "field":"data.c"),
                                      "interpolate":("value":"monotone"),
                                      "x":("scale":"x","field":"data.x"),
                                      "y":("scale":"y","field":"y"),
                                      "y2":("scale":"y","field":"y2")
                                      ),
                                     "update":  ("fillOpacity":("value":1.0)),
                                     "hover":   ("fillOpacity":("value":0.5))   
                                  )
                               
                                )
                             ]
                          ,
                        from = datum(\data="table",
                               transform = [
                                  transform(keys = ["data.c"],
                                    \type = "facet"),
                                  transform(point = "data.x", 
                                    height = "data.y",
                                    \type = "stack")
                               ] 
                         
                         )                       
                   )
                   ]
                  
                ); 
 
 public VEGA stackedArea() {
    return _stackedArea;
    }
    
VEGA  _groupedBar = vega(
            viewport = [1800, 1800]
             ,
            axes= [
                axe(scale="x", \type="x", 
                  tickSize = 0, tickPadding = 8
                //, properties =("labels":("angle":("value":90),"dx":("value":1)
                //    ,"baseline":("value":"middle"), "align":("value":"left"))
                //     ,"title":("dy":("value":50))
                //)
                ),
                axe(scale = "y", \type ="y"              
                )
                ]
            ,
            scales = 
                [scale(name="x", \type = "ordinal", 
                   domain = DOMAIN::ref(\data = "table", field = "data.x"),
                   range = RANGE::lit(key= "width"), padding = 0.2
                 ),
                 scale(name = "y", \type = "linear",
                   domain = DOMAIN::ref(\data="table", field = "data.y"),
                   range = RANGE::lit(key="height")
                   ,nice = true
                 ),
                 scale(name="color", \type = "ordinal",
                   range = RANGE::lit(key="category10")
                 )
                ]
               ,
              \data =[datum(name="table")]
             ,
             padding= padding(left=100, bottom = 30, top = 10, right = 10),
             marks = [mark(\type = "group",
                        from = datum(\data="table",
                                      transform = [
                                          transform(keys = ["data.x"],
                                          \type = "facet"
                                     )
                                    ] 
                                )
                            , 
                         scales =  [
                         scale(name="c", \type = "ordinal", 
                                   domain = DOMAIN::ref(\data = "table", field = "data.c"),
                                   range = RANGE::lit(key= "width"))             
                                   ],
                          properties = (
                                     "enter":(
                                        "x":("scale":"x", "field":"key")
                                        ,"width": ("scale":"x", "band":true))
                                     ),
                         marks=[
                             mark(\type="rect",
                                  properties = (
                                     "enter":(
                                      
                                      "fill":("scale":"color", "field":"data.c"),
                                      "width":("scale":"c","band":true),
                                      "x":("scale":"c","field":"data.c"),
                                      "y":("scale":"y","field":"data.y"),
                                      "y2":("scale":"y","value":0)
                                      ),
                                     "update":  ("fillOpacity":("value":1.0)),
                                     "hover":   ("fillOpacity":("value":0.5))   
                                  )
                               
                                )
                             ]
                                          
                          )
                   ]
                  
                ); 
             
public VEGA groupedBar() {
    return _groupedBar;
    }
    
VEGA  _groupedSymbol= 
vega(
            viewport = [1800, 1800]
             ,
            axes= [
                axe(scale="x", \type="x", 
                  tickSize = 0, tickPadding = 8
                ),
                axe(scale = "y", \type ="y"              
                )
                ]
            ,
            scales = 
                [scale(name="x", \type = "linear", 
                   domain = DOMAIN::ref(\data = "table", field = "data.x"),
                   range = RANGE::lit(key= "width")
                 ),
                 scale(name = "y", \type = "linear",
                   domain = DOMAIN::ref(\data="table", field = "data.y"),
                   range = RANGE::lit(key="height")
                   ,nice = true
                 ),
                 scale(name="color", \type = "ordinal",
                   range = RANGE::lit(key="category10")
                 )
                ]
               ,
              \data =[datum(name="table")]
             ,
             padding= padding(left=100, bottom = 30, top = 10, right = 10),
             marks = [mark(\type = "group",
                        from = datum(\data="table",
                                      transform = [
                                          transform(keys = ["data.x"],
                                          \type = "facet"
                                     )
                                    ] 
                                )
                            , 
                         scales =  [
                         scale(name="c", \type = "ordinal", 
                                   domain = DOMAIN::ref(\data = "table", field = "data.c"),
                                   range = RANGE::lit(key= "width"))             
                                   ],
                          //properties = (
                          //           "enter":(
                          //              "x":("scale":"x", "field":"key")
                          //              ,"width": ("scale":"x", "band":true))
                          //           ),
                         marks=[
                             mark(\type="symbol",
                                  properties = (
                                     "enter":(
                                      // "interpolate":("value":"monotone"),
                                      "fill":("scale":"color", "field":"data.c"),
                                      "x":("scale":"x","field":"data.x"),
                                      "y":("scale":"y","field":"data.y"),
                                      "y2":("scale":"y","value":0)
                                      ),
                                     "update":  ("fillOpacity":("value":1.0)),
                                     "hover":   ("fillOpacity":("value":0.5))   
                                  )
                               
                                )
                             ]
                                          
                          )
                   ]
                  
                ); 
             
        
 
 public VEGA groupedSymbol() {
    return _groupedSymbol;
    }
    
VEGA _groupedLine = vega(
            viewport = [1800, 1800]
             ,
            axes= [
                axe(scale="x", \type="x", grid = true 
               // , properties =("labels":("angle":("value":90),"dx":("value":1)
               // ,"baseline":("value":"middle"), "align":("value":"left"))
               // ,"title":("dy":("value":50)))
                )
                ,
                axe(scale = "y", \type ="y", grid = true
                //, properties =("labels":("fill":("value":"green")),
                // "title":("dy":("value":-30)))
                  )
                ]
            ,
            scales = 
                [scale(name="x", \type = "linear", 
                   domain = DOMAIN::ref(\data = "table", field = "data.x"),
                   range = RANGE::lit(key= "width")
                 ),
                 scale(name = "y", \type = "linear",
                   domain = DOMAIN::ref(\data="table", field = "data.y"),
                   range = RANGE::lit(key="height")
                   ,nice = true
                 ),
                 scale(name="color", \type = "ordinal",
                   range = RANGE::lit(key="category10")
                 )
                ]
               ,
              \data =[datum(name="table")]
             ,
             padding= padding(left=100, bottom = 30, top = 10, right = 10),
             marks = [mark(\type = "group",
                         marks=[
                             mark(\type="line",
                                  properties = (
                                     "enter":(
                                      "stroke":("scale":"color", "field":"data.c"),
                                      "interpolate":("value":"monotone"),
                                      "x":("scale":"x","field":"data.x"),
                                      "y":("scale":"y","field":"data.y"),
                                      "y2":("scale":"y","value":0)
                                      ),
                                     "update":  ("fillOpacity":("value":1.0)),
                                     "hover":   ("fillOpacity":("value":0.5))   
                                  )
                               
                                )
                             ]
                          ,
                        from = datum(\data="table"
                               ,transform = [
                                  transform(keys = ["data.c"],
                                    \type = "facet")
                               ]                     
                         )                       
                   )
                   ]                
                ); 
 
 public VEGA groupedLine() {
    return _groupedLine;
    }