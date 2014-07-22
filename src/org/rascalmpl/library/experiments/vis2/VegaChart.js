 /********************************************************/
/*					Vega Chart functions				*/
/********************************************************/

"use strict";

/****************** vegaBarChart *************************/

Figure.registerComponent("barChart", "vegaBarChart");

Figure.drawFunction.vegaBarChart = function(figure, x, y, w, h) {
    var data = {table: figure.datasets};
    
    function updateSpec(spec, width, height, top, left, bottom, right) {
        spec.width = width - left - right;
        spec.height = height - top - bottom;
        var padding = new Object();
        padding.top = top;
        padding.left = left;
        padding.bottom = bottom;
        padding.right = right;
        spec.padding = padding;
        return spec;
    }
    var dfile="illegal orientation";
    if (figure.orientation=="vertical") dfile = "vega/Vertical.json";
    if (figure.orientation=="horizontal") dfile = "vega/Horizontal.json";
   
	d3.json(dfile, function(err, spec) {
	// alert(spec);
	figure.svg
    .append("foreignObject")
    .attr("width", figure.width)
     .attr("height", figure.height)
     .attr("x", x).attr("y", y)
   .append("xhtml:body")
    .attr("id", "chartName")
    ;
    
    vg.parse.spec(updateSpec(spec, figure.width, figure.height, 100, 100, 100, 100), function(chart) {
        // alert("chart");
          var view = chart({el: "#chartName",data: data,renderer: "svg"})
           .update();
            d3.select("canvas").remove();
            }
           );
      }
      );
    }

