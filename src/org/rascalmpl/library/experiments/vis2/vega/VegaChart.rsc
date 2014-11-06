module experiments::vis2::vega::VegaChart

import experiments::vis2::vega::Json;

import Prelude;

 public void Main() {
     iprintln(stackedBar);
     }

VEGA  stackedBar = vega(
            viewport = [1800, 1800]
             ,
            axes= [
                axe(scale="x", \type="x", 
                properties =("labels":("angle":("value":90),"dx":("value":1)
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
                              t(keys=["data.x"],                       
                                \type = "facet"),      
                              t(\value="data.y",
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
                                  t(keys = ["data.c"],
                                    \type = "facet"),
                                  t(point = "data.x", 
                                    height = "data.y",
                                    \type = "stack")
                               ] 
                         
                         )                       
                   )
                   ]
                  
                ); 
 
 public JSON stackedBar() {
    return toJson(stackedBar);
    }
               

 VEGA  stackedArea = vega(
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
                              t(keys=["data.x"],                       
                                \type = "facet"),      
                              t(\value="data.y",
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
                                  t(keys = ["data.c"],
                                    \type = "facet"),
                                  t(point = "data.x", 
                                    height = "data.y",
                                    \type = "stack")
                               ] 
                         
                         )                       
                   )
                   ]
                  
                ); 
 
 public JSON stackedArea() {
    return toJson(stackedArea);
    }
    
VEGA  groupedBar = vega(
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
                                          t(keys = ["data.x"],
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
             
public JSON groupedBar() {
    return toJson(groupedBar);
    }
    
VEGA  groupedSymbol= 
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
                                          t(keys = ["data.x"],
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
             
        
 
 public JSON groupedSymbol() {
    return toJson(groupedSymbol);
    }
    
VEGA  _groupedLine() = vega(
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
                                  t(keys = ["data.c"],
                                    \type = "facet")
                               ]                     
                         )                       
                   )
                   ]                
                ); 
 
 public JSON groupedLine() {
    return toJson(_groupedLine());
    }