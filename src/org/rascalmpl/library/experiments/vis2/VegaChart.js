/********************************************************/
/*					Vega Chart functions						*/
/********************************************************/

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
var spec = {
  "width": 400,
  "height": 200,
  "padding": {"top": 10, "left": 30, "bottom": 30, "right": 10},
  "data": [{"name": "table"}],
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
      "from": {"data": "table"},
      "properties": {
        "enter": {
          "x": {"scale": "x", "field": "data.x"},
          "y": {"scale": "y", "field": "data.y"},
          "y2": {"scale": "y", "value": 0},
          "width": {"scale": "x", "band": true, "offset": -1}
        },
        "update": {
          "fill": {"value": "steelblue"}
        },
        "hover": {
          "fill": {"value": "green"}
        }
      }
    },
    {
      "type": "rect",
      "interactive": false,
      "from": {"data": "table"},
      "properties": {
        "enter": {
          "x": {"scale": "x", "field": "data.x", "offset": -3.5},
          "y": {"scale": "y", "field": "data.y", "offset": -3.5},
          "y2": {"scale": "y", "value": 0, "offset": 3.5},
          "width": {"scale": "x", "band": true, "offset": 6},
          "fill": {"value": "transparent"},
          "stroke": {"value": "red"},
          "strokeWidth": {"value": 2}
        },     
      "update": {
          "fill": {"value": "steelblue"}
        },
        "hover": {
          "fill": {"value": "green"}
        }
      }
    },
    {
      "type": "rect",
      "interactive": false,
      "from": {"data": "table"},
      "properties": {
        "enter": {
          "x": {"scale": "x", "field": "data.x", "offset": -3.5},
          "y": {"scale": "y", "field": "data.y", "offset": -3.5},
          "y2": {"scale": "y", "value": 0, "offset": 3.5},
          "width": {"scale": "x", "band": true, "offset": 6},
          "fill": {"value": "transparent"},
          "stroke": {"value": "red"},
          "strokeWidth": {"value": 2}
        },
        "update": {
          "strokeOpacity": {"value": 1}
        },
        "hover": {
          "strokeOpacity": {"value": 0}
        }
      }
    }
  ]
};

var data = {table: [
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
]};
	selection.append("foreignObject").attr("width", 400).attr("height", 400).append("xhtml:body").attr("id","chartName");
	
  vg.parse.spec(spec, function(chart) {
  // alert("chart");
  var view = chart({el: "#chartName", data:data, renderer:"svg"})
    .on("mouseover", function(event, item) {
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
        duration: 250,
        ease: "linear"
      });
    })
    .update();
    d3.select("canvas").remove();
    //alert(view);
}
 // , vg.headless.View.Factory
);
}