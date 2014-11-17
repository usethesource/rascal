module experiments::vis2::vega::VegaChart

import experiments::vis2::vega::Json;

import Prelude;

@doc{Create a fixed color palette}
public list[str] color12 = [ "yellow", "aqua", "navy", "violet", 
                          "red", "darkviolet", "maroon", "green",
                          "teal", "blue", "olive", "lime"];
                          

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
  
public VEGA setScale(VEGA vega, str name, SCALE s) {
    return visit(vega) {
          case scale(name=name) => s
          } 
    }
    
public void Main() {
     }  
     
data TICKLABELS = tickLabels(int angle = 0,   int dx = 99999, int dy = 99999, 
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
                
VEGA  _stackedBar = vega(
            viewport = [1800, 1800]
             ,
            axes= [
                axe(scale="x", \type="x"
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
             padding= padding(left=100, bottom = 30, top = 10, right = 100),
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
                   ],
                legends = []          
                ); 
                
 VEGA update(VEGA r, bool grid = false, 
    map[str, str] title = (), map[str, str] legends = (),
    TICKLABELS xTickLabels = tickLabels(),
    TICKLABELS yTickLabels = tickLabels(), list[str] palette = []
    )
    {
        AXE ax = getAxe(r, "x"); 
        if (title["x"]?) ax.title = title["x"];
        ax.grid = grid;
        ax.properties += _tickLabels("x", xTickLabels);           
        AXE ay = getAxe(r, "y");
        if (title["y"]?) ay.title = title["y"];
        ay.grid = grid;  
        ay.properties += _tickLabels("y", yTickLabels);     
        r = setAxe(r, "x", ax);
        r = setAxe(r, "y", ay);
        r.legends = [createLegend(k, legends[k], (title[k]?)?title[k]:"")| k <-legends];
        if (!isEmpty(palette)) {
           SCALE color = getScale(r, "color");
           color.range = array(values = hexColors(palette)); 
           r = setScale(r, "color", color);
           }
        return r;
    }
 
 public VEGA() stackedBar(bool grid = false, 
    map[str, str] title = (), map[str, str] legends = (), list[str] palette =[],
    TICKLABELS xTickLabels =  tickLabels()
    ,TICKLABELS yTickLabels = tickLabels()
    ) {
    return VEGA() {
        return update(_stackedBar, grid = grid, title = title, legends = legends,
        xTickLabels = xTickLabels, yTickLabels = yTickLabels, palette = palette);
        };
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
             padding= padding(left=100, bottom = 100, top = 10, right = 10),
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
             
public VEGA() groupedBar(bool grid = false, 
    map[str, str] title = (), map[str, str] legends = ()
    ,TICKLABELS xTickLabels = tickLabels()
    ,TICKLABELS yTickLabels = tickLabels()
    , list[str] palette =[]
    ) {
    return VEGA() {
        return update(_groupedBar, grid = grid, title = title, legends = legends
        ,xTickLabels = xTickLabels, yTickLabels = yTickLabels, palette = palette);
        };
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