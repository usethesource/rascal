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
 
	d3.json("vega/Vertical.json", function(err, spec) {
	alert(spec);
	figure.svg
                      //.attr("x", x)
                      // .attr("y", y)
                      // .attr("width", w)
                 //  .attr("height", h)
    .append("foreignObject")
    .attr("width", figure.width)
     .attr("height", figure.height)
     .attr("x", x).attr("y", y)
   .append("xhtml:body")
    .attr("id", "chartName")
    ;
    
    vg.parse.spec(updateSpec(spec, figure.width, figure.height, 10, 70, 100, 10), function(chart) {
        // alert("chart");
          var view = chart({el: "#chartName",data: data,renderer: "svg"})
           .update();
            d3.select("canvas").remove();
            }
           );
      }
      );
    }

