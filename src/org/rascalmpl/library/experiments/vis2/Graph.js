/********************************************************/
/*					Graph functions						*/
/********************************************************/

"use strict";

/********************************************************/
/*				graph			 						*/
/********************************************************/


Figure.bboxFunction.graph = function(selection) {
  	this.svg = selection.append("svg");
	Figure.getBBoxForComponent("graph", this.flavor)(this, selection);
	return this.svg;
}

Figure.drawFunction.graph = function (x, y, w, h) {
	return Figure.getDrawForComponent("graph", this.flavor)(this, x, y, w, h);
}

/********************************************************/
/*				graph flavors			 				*/
/********************************************************/

/******************* layeredGraph ***********************/

Figure.registerComponent("graph", "layeredGraph");

Figure.bboxFunction.layeredGraph = function(figure, selection) {
   var  org_nodes = figure.nodes || [],
        org_edges = figure.edges || [],
		nodes     = [],
		edges 	  = [];

   figure.svg
   	.style("fill", "black")
   	.style("stroke", "none")
   	;
   var defs = figure.svg.append("defs");

   var g = new dagreD3.Digraph();
   
   for(var i = 0; i < org_nodes.length; i++){
	   var node = org_nodes[i];
	   var d = defs.append("g").attr("id", "def-" + node.name);
	   var f = buildFigure(node.inner);
	   f.bbox(d);
	   f.draw(0,0,f.width, f.height);
	   d.attr("width", f.width).attr("height", f.height).attr("transform", "translate(" + (-f.width/2) + "," + (-f.height/2) + ")");
	   
	   g.addNode(node.name, {label: node.name, useDef: "def-" + node.name});
   }
   
   for(var i = 0; i < org_edges.length; i++){
	   var edge = org_edges[i];
	   edge.label = edge.name;
	   console.log("edge:", edge);
	   g.addEdge(null, edge.source, edge.target, edge);
   }
  
   var renderer = new dagreD3.Renderer();
   var oldDrawNodes = renderer.drawNodes();
   renderer.drawNodes(function(graph, root) {
      var svgNodes = oldDrawNodes(graph, root);
      svgNodes.attr("id", function(u) { return "node-" + u; });
      return svgNodes;
   });
  
   var layout1 = dagreD3.layout()
  	.nodeSep(figure.nodeSep)
  	.edgeSep(figure.edgeSep)
  	.rankSep(figure.rankSep)
  	.rankDir(figure.rankDir)
  	;
  	
   var layout = renderer.layout(layout1).run(g, figure.svg);
  
   if(!figure.hasDefinedWidth()){
  	  figure.min_width = layout.graph().width + 40;
   } else {
  	 figure.min_width = Math.max(figure.width, layout.graph().width + 40);
   }
   if(!figure.hasDefinedHeight()){
  	  figure.min_height = layout.graph().height + 40;
    } else {
      figure.min_height = Math.max(figure.height, layout.graph().height + 40);
    }
//    drawExtraFigure(selection, x, y, this);
//    addInteraction(selection, x, y, this);
    return figure.svg;
}   

Figure.drawFunction.layeredGraph = function (figure, x, y, w, h) {
	figure.svg
		.attr("x", x)
		.attr("y", y)
		.attr("width", w)
		.attr("height", h)
		.attr("viewbox", x + " " + y + " " + w + " " + h)
		;
		
    return figure.svg;
}

/******************* springGraph ***********************/

Figure.registerComponent("graph", "springGraph");

Figure.bboxFunction.springGraph = function(figure, selection) {
	//figure.svg = selection.append("svg");
  	if(!figure.hasDefinedWidth()){
  		figure.width = 400;
  	}
  	if(!figure.hasDefinedHeight()){
  		figure.height = 400;
  	}
  	return figure.svg;
}	
	

Figure.drawFunction.springGraph = function (figure, x, y, w, h) {
    var width = w,
        height = h,
        org_nodes = figure.nodes || [],
        links = figure.edges || [];

	var xnodes = {};
    console.log("nodes:", org_nodes);
    var defs = figure.svg.append("defs");
    
    for (var i in org_nodes) {
        console.log("node", i, org_nodes[i]);
		var d = defs.append("g").attr("id", "node" + i);
        var f = buildFigure(org_nodes[i].inner);
        f.bbox(d);
		f.draw(0, 0, f.width, f.height);
       	d.attr("width", f.width).attr("height", f.height);
		
        xnodes[org_nodes[i].name] = {value: f};
    }
    console.log("links", links);

    var force = self.force = d3.layout.force()
    	.nodes(d3.values(xnodes))
    	.links(links)
    	.gravity(.02)
    	.linkDistance(200)
    	.charge(-200)
    	.size([width, height])
    	.start()
		;
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
    var link = figure.svg.selectAll(".link")
    	.data(links)
    	.enter().append("line")
    	.style("stroke", function(d) {return d.stroke; })
    	.style("fill", function(d) { return d.fill; })
    	.style("fill-opacity", function(d) { return d["fill-opacity"]; })
    	.style("stroke-width", function(d) { return d["stroke-width"];   })
    	.style("stroke-dasharray", function(d) { return d["stroke-dasharray"]; })
    	.style("stroke-opacity", function(d) { return d["stroke-opacity"]; })
   		.attr("class", "link")
    	//	   .attr("marker-end", "url(#end)")
    	;

    var node = figure.svg.selectAll("g.node")
    	.data(d3.values(xnodes))
    	.enter()
    	.append("use")
    	.attr("class", "node")
    	.attr("xlink:href", function(d, i) { return "#node" + i; })
    	.call(force.drag);

    force.on("tick", function() {

        node.attr("transform", function(d) { return "translate(" + (x + d.x) + "," + (y + d.y) + ")";  });

        link.attr("x1", function(d) { 
			return x + xnodes[d.source].x + xnodes[d.source].value.width / 2;
			})
        	.attr("y1", function(d) { return y + xnodes[d.source].y + xnodes[d.source].value.height / 2; })
        	.attr("x2", function(d) { return x + xnodes[d.target].x + xnodes[d.target].value.width / 2; })
        	.attr("y2", function(d) { return y + xnodes[d.target].y + xnodes[d.target].value.height / 2; })
			;
    });
    
    //drawExtraFigure(selection, x, y, this);
   // addInteraction(selection, x, y, this);
}
