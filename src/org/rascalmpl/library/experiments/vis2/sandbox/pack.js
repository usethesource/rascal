/**
 * 
 */

function  packDraw(id, root, fill_node, fill_leaf, fillopacity_node, fillopacity_leaf, stroke, stroke_width, diameter) {
   var format = d3.format(",d"); 
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

  

  node.append("circle")
      .attr("r", function(d) { return d.r; })
      .style("fill", function(d){return d.children ? fill_node:fill_leaf})
      .style("fill-opacity", function(d){return d.children ? fillopacity_node:fillopacity_leaf})
      .style("stroke", stroke)
      .style("stroke-width", stroke_width)
      ;
  
  node.append("title")
  .text(function(d) { return d.name + (d.children ? "" : ": " + format(d.size)); });

  // node.filter(function(d) { return !d.children; }).append("text")
  //    .attr("dy", ".3em")
  //    .style("text-anchor", "middle")
  //    .text(function(d) { return d.name.substring(0, d.r / 3); });
   d3.select(self.frameElement).style("height", diameter + "px");

}

// packDraw("body", "d.json", "", "");
/**
* 
*/
function  treemapDraw(id, root, width, height) {
	var margin = {top: 40, right: 10, bottom: 10, left: 10},
	width = width - margin.left - margin.right,
	height = height - margin.top - margin.bottom;
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
	   .style("pointer-events","visible")
	   .style("stroke","black")
	   .style("stroke-width", "1")
	   ;     
	//node.filter(function(d) { return !d.children; }).append("text")
	//   .attr("dy", function(d) { return Math.max(0, d.dy - 1)*0.75 + "px"; })
	//   .attr("dx", function(d) { return Math.max(0, d.dx - 1)/2 + "px"; } )
	//   .style("text-anchor", "middle")
	//   .text(function(d) {return d.name.substring(0,  Math.max(0, d.dx - 1)/9); });
	 
}

function  treeDraw(id, root, width, height) {
	 var tree = d3.layout.tree()
	    .size([300,150])
	    .children(function(d) {return d.children;})
	    .separation(function(a,b){
	      return (a.width+b.width)/2+2;
	      })
	    ;
	 var vis = d3.select("#"+id)
     .attr("width", 400)
     .attr("height", 300)
     .append("svg:g")
     .attr("transform", "translate(40, 0)"); // shift everything to the right

	 
	      var diagonal = d3.svg.diagonal()
	      // change x and y (for the left to right tree)
	      .projection(function(d) { return [d.x, d.y]; });
	 
	      // Preparing the data for the tree layout, convert data into an array of nodes
	      var nodes = tree.nodes(root);
	      // Create an array with all the links
	      var links = tree.links(nodes);
	 
	      var link = vis.selectAll("pathlink")
	      .data(links)
	      .enter().append("svg:path")
	      .attr("class", "link")
	      .attr("d", diagonal)
	 
	      var node = vis.selectAll("g.node")
	      .data(nodes)
	      .enter().append("svg:g")
	      .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; })
	 
	      // Add the dot at every node
	      node.append("svg:rect")
	      .attr("width", function(d) {return d.width;})
	      .attr("height", function(d) {return d.height;}); 
    }
