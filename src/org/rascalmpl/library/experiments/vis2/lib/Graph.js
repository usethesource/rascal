
function combine(obj1, obj2){
	for (var attrname in obj2) { obj1[attrname] = obj2[attrname]; }
	return obj1;
}

function makeGraph(selection, x, y, width, height, options){

	var nodes = "nodes" in options ? options.nodes : [],
		links = "edges" in options ? options.edges : [];

	var force = self.force = d3.layout.force()
    	.nodes(nodes)
    	.links(links)
    	.gravity(.02)
    	.linkDistance(200)
    	.charge(-200)
    	.size([width, height])
    	.start()
 /*   	
    	// build the arrow.
	selection.append("svg:defs").selectAll("marker")
    	.data(["end"])
  		.enter().append("svg:marker")
    	.attr("id", String)
    	.attr("viewBox", "0 -5 10 10")
    	.attr("refX", 100)
   		.attr("refY", -1)
    	.attr("markerWidth", 6)
    	.attr("markerHeight", 6)
    	.attr("orient", "auto")
  		.append("svg:path")
    	.attr("d", "M0,-5L10,0L0,5");
 */   	
	var link = selection.selectAll(".link")
	   	.data(links)
	   	.enter().append("line")
	   	.style("stroke", function(d) { return d.stroke || "black"; })
	   	.style("fill", function(d) { return d.fill || "black"; })
	   	.style("fill-opacity", function(d) { return d.fill_opacity || 1.0; })
		.style("stroke-width", function(d) { return d.stroke_width || 1; })
		.style("stroke-dasharray", function(d) { return d.stroke_dasharray || []; })
		.style("stroke-opacity", function(d) { return d.stroke_opacity || 1.0; })
	   	.attr("class", "link")
//	   .attr("marker-end", "url(#end)")
	   ;
	   
	var node = selection.selectAll("g.node")
   		.data(nodes)
   		.call(force.drag);

	force.on("tick", function() {
      link.attr("x1", function(d) { return d.source.x + d.source.attr("width")/2; })
	      .attr("y1", function(d) { return d.source.y + d.source.attr("height")/2; })
	      .attr("x2", function(d) { return d.target.x + d.target.attr("width")/2; })
	      .attr("y2", function(d) { return d.target.y + d.target.attr("height")/2; });
	      
	   node.attr("transform", function(d) {return "translate(" + d.x + "," + d.y + ")";});
    });
}

/*
function makeGraph(selection, x, y, width, height, options){

	var nodes = "nodes" in options ? options.nodes : [],
		links = "edges" in options ? options.edges : [];

	var force = self.force = d3.layout.force()
    	.nodes(nodes)
    	.links(links)
    	.gravity(.02)
    	.linkDistance(200)
    	.charge(-200)
    	.size([width, height])
    	.start()
    	
	var link = selection.selectAll(".link")
	   .data(links)
	   .enter().append("line")
	   .attr("class", "link");
	   
	var node = selection.selectAll("g.node")
   		.data(nodes)
   		.call(force.drag);

	force.on("tick", function() {
      link.attr("x1", function(d) { return d.source.x + d.source.attr("width")/2; })
	      .attr("y1", function(d) { return d.source.y + d.source.attr("height")/2; })
	      .attr("x2", function(d) { return d.target.x + d.target.attr("width")/2; })
	      .attr("y2", function(d) { return d.target.y + d.target.attr("height")/2; });
	      
	   node.attr("transform", function(d) {return "translate(" + d.x + "," + d.y + ")";});
    });
}
*/