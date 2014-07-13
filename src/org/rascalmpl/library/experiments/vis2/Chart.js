/********************************************************/
/*					Chart functions						*/
/********************************************************/

"use strict";

/********************************************************/
/*				barChart			 					*/
/********************************************************/


Figure.bboxFunction.barChart = function(selection) {
  	this.svg = selection.append("svg");
  	if(!this.hasDefinedWidth()){
  		this.width = 400;
  	}
  	if(this.hasDefinedHeight()){
  		this.height = 400;
  	}
  	return this.svg;
}

Figure.drawFunction.barChart = function (x, y, w, h) {
	return Figure.getDrawForComponent("barChart", this.flavor)(this, x, y, w, h);
}

/********************************************************/
/*				barChart flavors			 			*/
/********************************************************/

//  NVD3 barchart

Figure.registerComponent("barChart", "nvBarChart");

Figure.drawFunction.nvBarChart = function (figure, x, y, w, h) {
 
  	nv.addGraph(function() {
		var chart = nv.models.discreteBarChart()
	  		.width(w)				 			// Set required width and height from figure
	    	.height(h)
      		.x(function(d) { return d.label })	// Specify the data accessors.
      		.y(function(d) { return d.value })
      		.staggerLabels(true)    			// Too many bars and not enough room? Try staggering labels.
      		.tooltips(false)        			// Don't show tooltips
      		.showValues(true)      				// ...instead, show the bar value right on top of each bar.
      		.transitionDuration(350)
      		;
	
	  	figure.svg 
	  		.attr("x", x)
	  		.attr("y", y)
	  		.attr("width", w)
	    	.attr("height", h) 
	    	.datum(figure.dataset)         	  	// Populate the <svg> element with chart data...
	    	.call(chart)                  		// Finally, render the chart
	     	;
	  	nv.utils.windowResize(function() { chart.update() });
	  	return chart;
   	});
  	return figure.svg;
}

/********************************************************/
/*				lineChart			 					*/
/********************************************************/

Figure.bboxFunction.lineChart = function(selection) {
	this.svg = selection.append("svg");
   	if(!this.hasDefinedWidth()){
  		this.width = 400;
  	}
  	if(this.hasDefinedHeight()){
  		this.height = 400;
  	}
  	return this.svg;
}

Figure.drawFunction.lineChart = function (x, y, w, h) {
	return Figure.getDrawForComponent("lineChart", this.flavor)(this, x, y, w, h);
}

/********************************************************/
/*				lineChart flavors	 					*/
/********************************************************/

//  NVD3 lineChart

Figure.registerComponent("lineChart", "nvLineChart");

Figure.drawFunction.nvLineChart = function (figure, x, y, w, h) {
	nv.addGraph(function() {
	  	var chart = nv.models.lineChart()
	  	 	.width(w)			 			// Set required width and height from figure
	     	.height(h)
	     	.margin({left: 100})  			// Adjust chart margins to give the x-axis some breathing room.
	     	.transitionDuration(350)  		// How fast do you want the lines to transition?
	  	 	.useInteractiveGuideline(true)	// We want nice looking tooltips and a guideline!
	     	.showLegend(true)       		// Show the legend, allowing users to turn on/off line series.
	     	.showYAxis(true)        		// Show the y-axis
	     	.showXAxis(true)        		// Show the x-axis
		 	;
	
	  	chart.xAxis                        	// Chart x-axis settings
	      	.axisLabel(figure.xAxis.label)
	      	.tickFormat(d3.format(figure.xAxis.tick));
	
	  	chart.yAxis                        	// Chart y-axis settings
	      	.axisLabel(figure.yAxis.label)
	      	.tickFormat(d3.format(figure.xAxis.tick));
	
	 	figure.svg
	  	  	.attr("x", x)
	  	  	.attr("y", y)
	  	  	.attr("width", w)
	      	.attr("height", h)
	      	.datum(figure.dataset)         	// Populate the <svg> element with chart data...
	      	.call(chart)                   	// Finally, render the chart
	      	;
	  	chart.update();
	      
	  	// Update the chart when window resizes.
	  	nv.utils.windowResize(function() { chart.update() });
	  	return chart;
   	});
   	return figure.svg;
}

// NVD3 lineWithFocusChart

Figure.registerComponent("lineChart", "nvLineWithFocusChart");

Figure.drawFunction.nvLineWithFocusChart = function (figure, x, y, w, h) {
	nv.addGraph(function() {
	  	var chart = nv.models.lineWithFocusChart()
			.width(w)			 			// Set required width and height from figure
	     	.height(h)
	     	.margin({left: 100})  			// Adjust chart margins to give the x-axis some breathing room.
	     	.transitionDuration(350)  		// How fast do you want the lines to transition?
	  		;
	
	  	chart.xAxis                        	// Chart x-axis settings
	      	.axisLabel(figure.xAxis.label)
	      	.tickFormat(d3.format(figure.xAxis.tick));
	
	  	chart.yAxis                        	// Chart y-axis settings
	      	.axisLabel(figure.yAxis.label)
	      	.tickFormat(d3.format(figure.xAxis.tick));
	
	 	figure.svg
	  	  	.attr("x", x)
	  	  	.attr("y", y)
	  	  	.attr("width", w)
	      	.attr("height", h)
	      	.datum(figure.dataset)         	// Populate the <svg> element with chart data...
	      	.call(chart)                   	// Finally, render the chart
	      	;
	  	chart.update();
	      
	  	// Update the chart when window resizes.
	  	nv.utils.windowResize(function() { chart.update() });
	  	return chart;
   	});
   	return figure.svg;
}

