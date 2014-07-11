/********************************************************/
/*					Vega Chart functions						*/
/********************************************************/

function rsc2vega(dataset) {
     if (dataset.length!=1) {
         alert("Illegal dataset");
         return null;
         }
     var vs = dataset[0].values;    
     var r = new Array(); 
     for (i=0;i<vs.length;i++) {
         var q = new Object();
         q.x = vs[i].label;
         q.y = vs[i].value;
         r[i] = q;
         }
     return r;
     }
     
/****************** vegaBarChart ****************************/

Figure.bboxFunction.vegaBarChart = function() {
    if (this.width == 0) {
        this.width = 400;
    }
    if (this.height == 0) {
        this.height = 400;
    } 
}



Figure.drawFunction.vegaBarChart = function (selection, x, y) {
//alert(selection);
var figure = this;
var data = {table: rsc2vega(figure.dataset)};

function updateSpec(spec, width, height, top, left, bottom, right) {
    spec.width = width - left - right;
    spec.height = height - top - bottom;
    padding = new Object();
    padding.top = top;
    padding.left = left;
    padding.bottom = bottom;
    padding.right = right;
    spec.padding = padding;
    return spec;
    }


var spec = {
  //"width": figure.width,
  // "height": figure.height,
  // "padding": {"top": 10, "left": "30", "bottom": 30, "right": 10},
  "data": [{"name": "table"}
  ],
  "scales": [
    {
      "name": "x", "type": "ordinal", "range": "width",
      "domain": {"data": "table", "field": "data.x"}
    },
    {
      "name": "y", "range": "height", "nice": true,
      "domain": {"data": "table", "field": "data.y"}
    }
  ],
  "axes": [
    {"type": "x", "scale": "x"},
    {"type": "y", "scale": "y"}
  ],
  "marks": [
    {
      "type": "rect",
      "from": {"data": "table"    
      }
      , "properties": {
        "enter": {
          "x": {"scale": "x", "field": "data.x"},
          "y": {"scale": "y", "field": "data.y"},
          "y2": {"scale": "y", "value": 0},
          "width": {"scale": "x", "band": true, "offset": -1}
          , "stroke": {"value": "lightgrey"},
          "strokeWidth": {"value": 1}
        },
        "update": {
          "fill": {"value": "steelblue"}
        },
        "hover": {
          "fill": {"value": "red"}
        } 
       
      }
    }      
   ,{
          "type": "text"
         , "from": {"data": "table"
         , "transform": [{"type":"formula", "field": "lab", "expr":"Math.round(d.data.y)"}]
          }
         ,"properties": {
           "enter": {
               "x": {"scale": "x", "field": "data.x"}
              ,"y": {"scale": "y", "field": "data.y", "offset": -4}
              , "fill": {"value": "black"}
              // , "valign": {"value": "top"}
              // , "dx": {"value": "center"}  
              // , "baseline": {"value": "middle"}
              , "text": {"field": "lab"}
              
              
            } 
          } 
        }   
  ]
};


/*
[
  {"x": 1,  "y": 28}, {"x": 2,  "y": 55},
  {"x": 3,  "y": 43}, {"x": 4,  "y": 91},
  {"x": 5,  "y": 81}, {"x": 6,  "y": 53},
  {"x": 7,  "y": 19}, {"x": 8,  "y": 87},
  {"x": 9,  "y": 52}, {"x": 10, "y": 48},
  {"x": 11, "y": 24}, {"x": 12, "y": 49},
  {"x": 13, "y": 87}, {"x": 14, "y": 66},
  {"x": 15, "y": 17}, {"x": 16, "y": 27}, 
  {"x": 17, "y": 68}, {"x": 18, "y": 16},
  {"x": 19, "y": 49}, {"x": 20, "y": 75}
]
*/

selection.append("foreignObject").attr("width", figure.width).attr("height", figure.height).attr("x", x).attr("y", y).append("xhtml:body").attr("id","chartName");
	
  vg.parse.spec(updateSpec(spec, figure.width, figure.height, 10, 30, 30, 10), function(chart) {
  // alert("chart");
  var view = chart({el: "#chartName", data:data, renderer:"svg"})
   /* .on("mouseover", function(event, item) {
      // invoke hover properties on cousin one hop forward in scenegraph
      view.update({
        props: "hover",
        items: item.cousin(1)
      });
    })
    .on("mouseout", function(event, item) {
      // reset cousin item, using animated transition
      view.update({
        props: "update",
        items: item.cousin(1),
        duration: 500,
        ease: "linear"
      });
    })
    */ .update(); 
    d3.select("canvas").remove();
    //alert(view);
}
 // , vg.headless.View.Factory
);
}