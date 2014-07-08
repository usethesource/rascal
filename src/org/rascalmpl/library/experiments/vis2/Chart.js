/********************************************************/
/*					Chart functions						*/
/********************************************************/

/****************** barChart ****************************/

Figure.bboxFunction.barChart = function() {
    if (this.width == 0) {
        this.width = 400;
    }
    if (this.height == 0) {
        this.height = 200;
    } 
}

Figure.drawFunction.barChart = function (selection, x, y) {
alert(selection);
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
	selection.append("g").attr("id","chartName");
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
    alert(view);
}
 // , vg.headless.View.Factory
);
}

  
  
         
        


/*
Figure.drawFunction.barChart = function (selection, x, y) {
  var figure = this;
  nv.addGraph(function() {
	  var chart = nv.models.discreteBarChart()
	  	.width(figure.width)				 // Set required width and height from figure
	    .height(figure.height)
      	.x(function(d) { return d.label })   // Specify the data accessors.
      	.y(function(d) { return d.value })
      	.staggerLabels(true)    			 // Too many bars and not enough room? Try staggering labels.
      	.tooltips(false)        			 // Don't show tooltips
      	.showValues(true)      				 // ...instead, show the bar value right on top of each bar.
      	.transitionDuration(350)
      	;
	
	  selection.append("svg")               // Create <svg> element to render the chart in.
	  	  .attr("x", x)
	  	  .attr("y", y)
	  	  .attr("width", figure.width)
	      .attr("height", figure.height) 
	      .datum(figure.dataset)         	// Populate the <svg> element with chart data...
	      .call(chart)                   	// Finally, render the chart
	      ;
	  chart.update();
	      
	  // Update the chart when window resizes.
	  nv.utils.windowResize(function() { chart.update() });
	  return chart;
   });
}
*/
/****************** lineChart ***************************/

Figure.lineChartFlavors = {
	lineChart:  nv.models.lineChart,
	lineWithFocusChart:
				nv.models.lineWithFocusChart
};

Figure.bboxFunction.lineChart = function() {
    if (this.width == 0) {
        this.width = 200;
    }
    if (this.height == 0) {
        this.height = 200;
    }
}

Figure.drawFunction.lineChart = function (selection, x, y) {
  var figure = this;
  var flavor = figure.flavor;
  nv.addGraph(function() {
	  var chart = Figure.lineChartFlavors[flavor]()
	  	 .width(figure.width)			 // Set required width and height from figure
	     .height(figure.height)
	     .margin({left: 100})  			 // Adjust chart margins to give the x-axis some breathing room.
	     .transitionDuration(350)  		 // How fast do you want the lines to transition?
	  ;
	  
	  if(flavor !== "lineWithFocusChart"){
	  	chart
	  		.useInteractiveGuideline(true)  // We want nice looking tooltips and a guideline!
	     	.showLegend(true)       		 // Show the legend, allowing users to turn on/off line series.
	     	.showYAxis(true)        		 // Show the y-axis
	     	.showXAxis(true)        		 // Show the x-axis
	  }
	
	  chart.xAxis                        //Chart x-axis settings
	      .axisLabel(figure.xAxis.label)
	      .tickFormat(d3.format(figure.xAxis.tick));
	
	  chart.yAxis                        //Chart y-axis settings
	      .axisLabel(figure.yAxis.label)
	      .tickFormat(d3.format(figure.xAxis.tick));
	
	  selection.append("svg")             // Create <svg> element to render the chart in.
	  	  .attr("x", x)
	  	  .attr("y", y)
	  	  .attr("width", figure.width)
	      .attr("height", figure.height)
	      .datum(figure.dataset)         // Populate the <svg> element with chart data...
	      .call(chart)                   // Finally, render the chart
	      ;
	  chart.update();
	      
	  // Update the chart when window resizes.
	  nv.utils.windowResize(function() { chart.update() });
	  return chart;
   });
}
