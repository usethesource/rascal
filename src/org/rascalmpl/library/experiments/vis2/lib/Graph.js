function makeGraph(selection, x, y, width, height, options){

	var nodes = "nodes" in options ? options.nodes : [],
		links = "links" in options ? options.links : [];
	
	var w = width,
	    h = height;
	
	var svg = selection.append("svg")
	    .attr("width", w)
	    .attr("weight", h);
	
	var force = d3.layout.force()
	    .nodes(nodes)
	    .links(links)
	    .size([w, h])
	    .charge(-200)
	    .on("tick", tick)
	    .start();
	
	var link = svg.selectAll(".link")
	   .data(links)
	   .enter().append("line")
	   .attr("class", "link");
	
	var node = svg.selectAll(".node")
	   .data(nodes)
	   .enter().append("circle")
	   .attr("class", "node")
	   .attr("r", 4.5);
	
	function tick() {
	  link.attr("x1", function(d) { return d.source.x; })
	      .attr("y1", function(d) { return d.source.y; })
	      .attr("x2", function(d) { return d.target.x; })
	      .attr("y2", function(d) { return d.target.y; });
	
	  node.attr("cx", function(d) { return d.x; })
	      .attr("cy", function(d) { return d.y; });
	}
}