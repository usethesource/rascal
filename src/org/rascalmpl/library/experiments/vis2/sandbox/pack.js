/**
 * 
 */

function  packDraw(id, root, fill_node, fill_leaf, fillopacity_node, fillopacity_leaf, stroke, stroke_width) {
   var diameter = 960, format = d3.format(",d"); 
   var pack = d3.layout.pack()
    .size([diameter - 4, diameter - 4])
    .value(function(d) { return d.size; });

var svg = d3.select("#"+id).append("svg")
    .attr("width", diameter)
    .attr("height", diameter)
  .append("g")
    .attr("transform", "translate(2,2)");


  var node = svg.datum(root).selectAll(".node")
      .data(pack.nodes)
    .enter().append("g")
      .attr("class", function(d) { return d.children ? "node" : "leaf node"; })
      .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });

  node.append("title")
      .text(function(d) { return d.name + (d.children ? "" : ": " + format(d.size)); });

  node.append("circle")
      .attr("r", function(d) { return d.r; })
      .style("fill", function(d){return d.children ? fill_node:fill_leaf})
      .style("fill-opacity", function(d){return d.children ? fillopacity_node:fillopacity_leaf})
      .style("stroke", stroke)
      .style("stroke-width", stroke_width)
      ;

  node.filter(function(d) { return !d.children; }).append("text")
      .attr("dy", ".3em")
      .style("text-anchor", "middle")
      .text(function(d) { return d.name.substring(0, d.r / 3); });
   d3.select(self.frameElement).style("height", diameter + "px");

}

// packDraw("body", "d.json", "", "");
/**
* 
*/
function  treemapDraw(id, root) {
	var margin = {top: 40, right: 10, bottom: 10, left: 10},
	width = 960 - margin.left - margin.right,
	height = 500 - margin.top - margin.bottom;
	var color = d3.scale.category20c();
	var treemap = d3.layout.treemap()
	.size([width, height])
	.sticky(true)
	.value(function(d) { return d.size; });
	var svg = d3.select("#"+id).append("svg")
	.attr("width", width)
	.attr("height", height)
	var node = svg.datum(root).selectAll(".node")
	  .data(treemap.nodes)
	.enter().append("g")
	   .attr("class", function(d) { return d.children ? "node" : "leaf node"; })
	   .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
	node.filter(function(d) { return !d.children; }).append("title")
	   .text(function(d) { return d.name+":"+d.size;})
	 ;
	 node.append("rect").style("fill","none")
	   .attr("width", function(d) { return Math.max(0, d.dx - 1) + "px"; })
	   .attr("height", function(d) { return Math.max(0, d.dy - 1) + "px"; })
	   .style("fill", function(d) { return d.children  ? color(d.name) : "none"; })
	   .style("stroke","black")
	   .style("stroke-width", "1")
	   ;     
	 node.filter(function(d) { return !d.children; }).append("text")
	   .attr("dy", function(d) { return Math.max(0, d.dy - 1)*0.75 + "px"; })
	   .attr("dx", function(d) { return Math.max(0, d.dx - 1)/2 + "px"; } )
	   .style("text-anchor", "middle")
	   .text(function(d) {return d.name.substring(0,  Math.max(0, d.dx - 1)/9); });
}
