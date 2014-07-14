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

//////// Functions taken from dagre-d3 to override drawNode

function isComposite(g, u) {
  return 'children' in g && g.children(u).length;
}

function addLabel(node, root, marginX, marginY) {
  // Add the rect first so that it appears behind the label
  var label = node.label;
  var rect = root.append('rect');
  var labelSvg = root.append('g');

  if (label[0] === '<') {
    addForeignObjectLabel(label, labelSvg);
    // No margin for HTML elements
    marginX = marginY = 0;
  } else {
    addTextLabel(label,
                 labelSvg,
                 Math.floor(node.labelCols),
                 node.labelCut);
  }

  var bbox = root.node().getBBox();

  labelSvg.attr('transform',
             'translate(' + (-bbox.width / 2) + ',' + (-bbox.height / 2) + ')');

  rect
    .attr('rx', node.rx || 5)
    .attr('ry', node.ry || 5)
    .attr('x', -(bbox.width / 2 + marginX))
    .attr('y', -(bbox.height / 2 + marginY))
    .attr('width', bbox.width + 2 * marginX)
    .attr('height', bbox.height + 2 * marginY)
    .style('fill', node.fill || "#000000")
    .style("stroke", node.stroke)
    .style("stroke-width", node["stroke-Width"] + "px")
    .style("stroke-dasharray", node["stroke-dasharray"]);

  if (node.fill) {
    rect.attr('fill', "#afafaf"); //node.fill);
  }

  if (node.href) {
    root
      .attr('class', root.attr('class') + ' clickable')
      .on('click', function() {
        window.open(node.href);
      });
  }
}

function addTextLabel(label, root, labelCols, labelCut) {
  if (labelCut === undefined) labelCut = 'false';
  labelCut = (labelCut.toString().toLowerCase() === 'true');

  var node = root
    .append('text')
    .attr('text-anchor', 'left');

  label = label.replace(/\\n/g, '\n');

  var arr = labelCols ? wordwrap(label, labelCols, labelCut) : label;
  arr = arr.split('\n');
  for (var i = 0; i < arr.length; i++) {
    node
      .append('tspan')
        .attr('dy', '1em')
        .attr('x', '1')
        .text(arr[i]);
  }
}

var GraphRenderer = new dagreD3.Renderer();

function defaultDrawNodes(g, root) {
  var nodes = g.nodes().filter(function(u) { return !isComposite(g, u); });
  console.log("defaultDrawNodes");
  var svgNodes = root
    .selectAll('g.node')
    .classed('enter', false)
    .data(nodes, function(u) { return u; });

  svgNodes.selectAll('*').remove();

  svgNodes
    .enter()
      .append('g')
        .style('opacity', 0)
        .attr('class', 'node enter');

  svgNodes.each(function(u) { addLabel(g.node(u), d3.select(this), 10, 10); });

  this._transition(svgNodes.exit())
      .style('opacity', 0)
      .remove();

  return svgNodes;
}

Figure.bboxFunction.layeredGraph = function(figure, selection) {
   var  org_nodes = figure.nodes || [],
        org_edges = figure.edges || [],
		nodes     = [],
		edges 	  = [];
		
   var g = new dagreD3.Digraph();
   
   for(var i = 0; i < org_nodes.length; i++){
	   var node = org_nodes[i];
	   node.inner.label = node.name;
	   g.addNode(node.name, node.inner);
   }
   
   for(var i = 0; i < org_edges.length; i++){
	   var edge = org_edges[i];
	   edge.label = edge.name;
	   
	   g.addEdge(null, edge.source, edge.target, edge);
   }

  //var renderer = GraphRenderer = new dagreD3.Renderer();
  GraphRenderer.drawNodes(defaultDrawNodes);
  var oldDrawNodes = GraphRenderer.drawNodes();
  GraphRenderer.drawNodes(function(graph, root) {
    var svgNodes = oldDrawNodes(graph, root);
    svgNodes.attr("id", function(u) { return "node-" + u; });
    return svgNodes;
  });
  var layout = GraphRenderer.run(g, figure.svg);
  
  if(!figure.hasDefinedWidth()){
  	figure.min_width = layout.graph().width + 40;
  }
  if(!figure.hasDefinedHeight()){
  	figure.min_height = layout.graph().height + 40;
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
		;
		
    return figure.svg;
}

/******************* springGraph ***********************/

Figure.registerComponent("graph", "springGraph");

Figure.bboxFunction.springGraph = function(figure, selection) {
	figure.svg = selection.append("svg");
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
    console.log("nodes:", nodes);
    var defs = figure.svg.append("defs");
    
    for (var i in org_nodes) {
        console.log("node", i, nodes[i]);
		var d = defs.append("g").attr("id", "node" + i);
        var f = buildFigure(nodes[i].inner);
        f.bbox(d);
		f.draw(0, 0, f.width, f.height);
       	d.attr("width", f.width).attr("height", f.height);
		
        xnodes[nodes[i].name] = {value: f};
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

        node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")";  });

        link.attr("x1", function(d) { 
			return xnodes[d.source].x + xnodes[d.source].value.width / 2;
			})
        	.attr("y1", function(d) { return xnodes[d.source].y + xnodes[d.source].value.height / 2; })
        	.attr("x2", function(d) { return xnodes[d.target].x + xnodes[d.target].value.width / 2; })
        	.attr("y2", function(d) { return xnodes[d.target].y + xnodes[d.target].value.height / 2; })
			;
    });
    
    //drawExtraFigure(selection, x, y, this);
   // addInteraction(selection, x, y, this);
}
