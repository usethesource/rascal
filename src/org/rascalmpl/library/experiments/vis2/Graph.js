/********************************************************/
/*					Graph functions						*/
/********************************************************/

/******************* layeredGraph ***********************/

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

Figure.bboxFunction.graph = function() {
    if (this.width == 0) {
        this.width = 200;
    }
    if (this.height == 0) {
        this.height = 200;
    }
}

Figure.drawFunction.graph = function (selection, x, y) {
    var width = this.width,
        height = this.height,
        nodes = this.nodes || [],
        edges = this.edges || [];

  //var renderer = GraphRenderer = new dagreD3.Renderer();
  GraphRenderer.drawNodes(defaultDrawNodes);
  var oldDrawNodes = GraphRenderer.drawNodes();
  GraphRenderer.drawNodes(function(graph, root) {
    var svgNodes = oldDrawNodes(graph, root);
    svgNodes.attr("id", function(u) { return "node-" + u; });
    return svgNodes;
  });
  selection = selection.append("svg").attr("width", 650).attr("height", 680).attr("x", x).attr("y", y).append("g").attr("transform", "translate(20,20)");
  var layout = GraphRenderer.run(dagreD3.json.decode(nodes, edges), selection);
  selection
    .attr("width", layout.graph().width + 40)
    .attr("height", layout.graph().height + 40);

    drawExtraFigure(selection, x, y, this);
    addInteraction(selection, x, y, this);
}

/******************* springGraph ***********************/

// Function.drawFunction.graph = function (selection, x, y) {
//     var width = this.width,
//         height = this.height,
//         nodes = this.nodes || [],
//         links = this.edges || [];

//     console.log("nodes:", nodes);
//     var defs = selection.append("defs");
    
//     for (var i in nodes) {
//         console.log("node", i, nodes[i]);
//         var f = buildFigure(nodes[i]);
//         f.bbox();
//         var d = defs.append("g").attr("id", "node" + i).attr("width", f.width).attr("height", f.height);
//         nodes[i] = f.draw(d, 0, 0);
//     }
//     console.log("links", links);

//     var force = self.force = d3.layout.force()
//     .nodes(nodes)
//     .links(links)
//     .gravity(.02)
//     .linkDistance(200)
//     .charge(-200)
//     .size([width, height])
//     .start()
//     /*	
//             	// build the arrow.
//         	selection.append("svg:defs").selectAll("marker")
//             	.data(["end"])
//           		.enter().append("svg:marker")
//             	.attr("id", String)
//             	.attr("viewBox", "0 -5 10 10")
//             	.attr("refX", 100)
//            		.attr("refY", -1)
//             	.attr("markerWidth", 6)
//             	.attr("markerHeight", 6)
//             	.attr("orient", "auto")
//           		.append("svg:path")
//             	.attr("d", "M0,-5L10,0L0,5");
//          */
//     var link = selection.selectAll(".link")
//     .data(links)
//     .enter().append("line")
//     .style("stroke", function(d) {
//         return d.stroke || "black";
//     })
//     .style("fill", function(d) {
//         return d.fill || "black";
//     })
//     .style("fill-opacity", function(d) {
//         return d.fill_opacity || 1.0;
//     })
//     .style("stroke-width", function(d) {
//         return d.stroke_width || 1;
//     })
//     .style("stroke-dasharray", function(d) {
//         return d.stroke_dasharray || [];
//     })
//     .style("stroke-opacity", function(d) {
//         return d.stroke_opacity || 1.0;
//     })
//     .attr("class", "link")
//     //	   .attr("marker-end", "url(#end)")
//     ;

//     var node = selection.selectAll("g.node")
//     .data(nodes)
//     .enter()
//     .append("use")
//     .attr("class", "node")
//     .attr("xlink:href", function(d, i) {
//         return "#node" + i;
//     })
//     .call(force.drag);

//     force.on("tick", function() {

//         node.attr("transform", function(d) {
//             return "translate(" + d.x + "," + d.y + ")";
//         });

//         link.attr("x1", function(d) {
//             console.log(d);
//             return d.source.x + nodes[d.source].attr("width") / 2;
//         })
//         .attr("y1", function(d) {
//             return d.source.y + d.source.attr("height") / 2;
//         })
//         .attr("x2", function(d) {
//             return d.target.x + d.target.attr("width") / 2;
//         })
//         .attr("y2", function(d) {
//             return d.target.y + d.target.attr("height") / 2;
//         });


//     });
    
//     drawExtraFigure(selection, x, y, this);
//     addInteraction(selection, x, y, this);
// }
