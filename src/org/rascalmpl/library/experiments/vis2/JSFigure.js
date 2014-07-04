
/****************** The figure prototype with all defaults *********************/

var Figure = {
    figure: "figure",
    textValue: "none",
    hgap: 0,
    vgap: 0,
    fill: "white",
    "fill-opacity": 1.0,
    "stroke-width": 1,
    stroke: "black", 
    "stroke-dasharray": [],
    "stroke-opacity": 1.0,
    borderRadius: 0,
    halign: 0.5,
    valign: 0.5,
    "font-family": "sans-serif", // serif, sans-serif, monospace
    "font-name": "Helvetica",
    "font-size": 12,
    "font-style": "normal",        // normal, italic
    "font-weight": "normal",      // normal, bold
    "text-decoration": "none",     // none, underline, overline, line-through
    
    dataset: [], 
    figure_root:  {},
    
    hasDefinedWidth: function()  { return this.hasOwnProperty("definedWidth"); },
    
    hasDefinedHeight: function() { return this.hasOwnProperty("definedHeight"); },
                               
    getModelElement: function(accessor) { return eval(accessor); },
    
    setModelElement: function(accessor, v) { 
          var v1 = !isString(v) ? JSON.stringify(v) : v;
          eval(accessor + "=" + v1); return v; 
          },
    
    model: undefined,
    
    name: undefined
}

Object.defineProperty(Figure, "width", { get: function(){ 
                                                    if(this.hasOwnProperty("_width"))return this._width;
                                                    if(this.hasDefinedWidth()) return this.definedWidth;
                                                    return 0;
                                        },
             set: function(w){ this._width = w; }
        
    });
    
Object.defineProperty(Figure, "height", { get: function(){ if(this.hasOwnProperty("_height")) return this._height;
                                                    if(this.hasDefinedHeight) return this.definedHeight;
                                                    return 0;
                                               },
                                          set: function(h){ 
                                                    if(isString(h)) {
                                                        alert(h); 
                                                    }
                                                    this._height = h; 
                                               }
        
    });

/****************** Build a figure given its JSON representation ****************/

function isArray(obj) {
    return Object.prototype.toString.call(obj) === "[object Array]";
}

function isString(obj) {
    return Object.prototype.toString.call(obj) === "[object String]";
}

function buildFigure(description) {
    return buildFigure1(description, Figure);
}

function buildFigure1(description, parent) {
    var f = Object.create(parent);
    f.bbox = bboxFunction[description.figure]; // || throw "No bbox function defined for " + description.figure;
    f.draw = drawFunction[description.figure]; // || throw "No draw function defined for " + description.figure;
    
    for(p in description) {
        handle_prop = function(prop){       // Use extra closure to protect accessor as used in defineProperty
        if (prop === "inner") {
            var inner_description = description[prop];
            if (isArray(inner_description)) {
                var inner_array = new Array();
                for (var i = 0; i < inner_description.length; i++) {
                    inner_array[i] = buildFigure1(inner_description[i], f);
                }
                f[prop] = inner_array;
            } else {
                f[prop] = buildFigure1(inner_description, f);
            }
        } else {
            var prop_val = description[prop];
            if(prop_val.use){
                var accessor = prop_val.use;
                if(prop === "accessor"){
                    Object.defineProperty(f, prop, {get: function(){ return accessor;}  });
                } else {
                    Object.defineProperty(f, prop, {get: function(){ return eval(accessor);}  });
                }
            } else {
                var val = description[prop];
                if(val.hasOwnProperty("figure")){
                    val = buildFigure1(val, f);
                }
                Object.defineProperty(f, prop, {value: val});
            }
        }
        }; 
        handle_prop(p);
    }
    return f;
}

/****************** Draw a figure object ****************/

function drawFigure (description){
    drawFigure1(buildFigure(description));
}

function drawFigure1(f) {
    Figure.figure_root = f;
    f.bbox();
    var x = f.x || 0;
    var y = f.y || 0;
    var area = d3.select("#figurearea").append("svg").attr("width", f.width).attr("height", f.height);
    return f.draw(area, x, y);
}



bboxExtraFigure = function (fig){
    if(fig.hasOwnProperty("extra_figure")){
        fig.extra_figure.bbox();
    }
}

/****************** AddInteraction to a figure *************************/

function addInteraction(selection, x, y, fig) {
    if (fig.hasOwnProperty("event")) {
    	selection.style("cursor", "crosshair");
        
        if(fig.event != "click"){
            selection.on("mouseout", function(){ d3.event.preventDefault(); fig.draw_extra_figure = false;  redrawFigure();});
        }
        
        selection.on(fig.event, function(e) {
           d3.event.preventDefault();
           d3.event.stopPropagation();
           if(fig.hasOwnProperty("replacement")){
                Figure.setModelElement(fig.accessor, fig.replacement);
                refreshFromServer();
           } else if(fig.hasOwnProperty("extra_figure")){
               fig.draw_extra_figure = (fig.event === "click") ? ! fig.draw_extra_figure : true;
               redrawFigure();
           } else {
                redrawFigure();
           }
        });
    }
    return selection;
}

function drawExtraFigure(selection, x, y, fig){
    if(fig.hasOwnProperty("extra_figure") && fig.draw_extra_figure === true){
        fig.extra_figure.bbox();
        fig.extra_figure.draw(selection, x, y);
    }
}

function handleUserInput(fig, v) {
    d3.event.stopPropagation();
    
    if(fig.hasOwnProperty("replacement")){
        Figure.setModelElement(fig.accessor, fig.replacement);
        refreshFromServer();
    } else {
        Figure.setModelElement(fig.accessor, v);
        //redrawFigure();
        refreshFromServer();
    }
    return false;
}

/****************** Askserver and redraw a figure object ****************/

var ajax = {};
ajax.x = function() {
    if (typeof XMLHttpRequest !== 'undefined') {
        return new XMLHttpRequest();  
    }
    var versions = [
        "MSXML2.XmlHttp.5.0",   
        "MSXML2.XmlHttp.4.0",  
        "MSXML2.XmlHttp.3.0",   
        "MSXML2.XmlHttp.2.0",  
        "Microsoft.XmlHttp"
    ];

    var xhr;
    for(var i = 0; i < versions.length; i++) {  
        try {  
            xhr = new ActiveXObject(versions[i]);  
            break;  
        } catch (e) {
        }  
    }
    return xhr;
};

ajax.send = function(url, callback, method, data, sync) {
    var x = ajax.x();
    x.open(method, url, sync);
    x.onreadystatechange = function() {
        if (x.readyState == 4) {
            callback(x.responseText)
        }
    };
    if (method == 'POST') {
        x.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    }
    x.send(data)
};

ajax.get = function(url, data, callback, sync) {
    var query = [];
    for (var key in data) {
        query.push(encodeURIComponent(key) + '=' + encodeURIComponent(data[key]));
    }
    ajax.send(url + '?' + query.join('&'), callback, 'GET', null, sync)
};

ajax.post = function(url, data, callback, sync) {
    var query = [];
    for (var key in data) {
        query.push(encodeURIComponent(key) + '=' + encodeURIComponent(data[key]));
    }
    ajax.send(url, callback, 'POST', query.join('&'), sync)
};

function askServer(path, params) {
	ajax.post(path, params, function(responseText){
		try {
            if(d3.event){
              d3.event.stopPropagation();
            }
            var res = JSON.parse(responseText);
            var area = d3.select("#figurearea svg");
            if(!area.empty()){
              try { area.remove(); } catch(e) { console.log("askServer", e); };
		    }
            Figure.name = res.name;
            Figure.model_constructor = res.model_root;
            Figure.model = res.model_root;
            Figure.site = res.site;
            Figure.figure_root = res.figure_root;
            drawFigure(res.figure_root);
            return;
        } catch (e) {
            console.error(e.message + ", on figure " + responseText);
        }
	});
}

function refreshFromServer(){
    askServer(Figure.site + "/refresh/" + Figure.name, {"model" : JSON.stringify(Figure.model)});
}

function redrawFigure(){
    var area = d3.select("#figurearea svg");
    if(!area.empty()){
      try { area.remove(); } catch(e) { console.log(redrawFigure, e); };
    }
    drawFigure1(Figure.figure_root);
}

/****************** Bounding box and draw function table *******/

// Determine the bounding box of the various figures types
var bboxFunction = {};

// Draw the various figure types
var drawFunction = {};

/**************** box *******************/


bboxFunction.box = function() {
    var width = 0, height = 0, definedW, definedH;
    
    if(this.hasDefinedWidth()){
        width = this.definedWidth;
        definedW = 1;
    }
    if(this.hasDefinedHeight()){
        height = this.definedHeight;
        definedH = 1;
    } 
    var lw = this["stroke-width"];
    width += (lw + 1) / 2;
    height += (lw + 1) / 2;
    console.log("box.bbox:", width, height);
    if (this.hasOwnProperty("inner")) {
        var inner = this.inner;
        console.log(inner);
        inner.bbox();
        console.log("inner", inner.width, inner.height);
        if(!definedW){
            width = Math.max(width, inner.width + 2 * this.hgap);
        }
        if(!definedH){
            height = Math.max(height, inner.height + 2 * this.vgap);
        }
        console.log("outer size:", width, height);
    }
    // 	if(this.pos){
    // 		p = this.pos;
    // 		return [p[0] + sz[0], p[1] + sz[1]];
    // 	}
    this.width = width;
    this.height = height;
}

drawFunction.box = function (selection, x, y) {
    var my_svg = selection
    .append("rect")
    .attr("x", x)
    .attr("y", y)
    .attr("width", this.width)
    .attr("height", this.height)
    .style("stroke", this.stroke)
    .style("fill", this.fill)
    .style("stroke-width", this["stroke-width"] + "px")
    .style("stroke-dasharray", this["stroke-dasharray"])
    if (this.hasOwnProperty("inner")) {
        var inner = this.inner;
        inner.draw(selection, x + this.hgap + inner.halign * (this.width - inner.width - 2 * this.hgap), 
        y + this.vgap + inner.valign * (this.height - inner.height - 2 * this.vgap));
    }
    drawExtraFigure(selection, x, y, this);
    addInteraction(my_svg, x, y, this);
    return my_svg;
}

/**************** hcat *******************/

bboxFunction.hcat = function() {
    var inner = this.inner;
    var width = 0;
    var height = 0;
    for (var i = 0; i < inner.length; i++) {
        var elm = inner[i];
        elm.bbox();
        width += elm.width;
        height = Math.max(height, elm.height);
    }
    this.width = width + (inner.length - 1) * this.hgap; //TODO length == 0
    this.height = height;
    return;
}

drawFunction.hcat = function (selection, x, y) {
    var my_svg = selection
        .append("rect")
        .attr("x", x)
        .attr("y", y)
        .attr("width", this.width)
        .attr("height", this.height)
        .style("fill", "none");

    var inner = this.inner;
    console.log("hcat:", inner);
    for (var i = 0; i < inner.length; i++) {
        var elm = inner[i];
        elm.draw(selection, x, y + this.valign * (this.height - elm.height));
        x += elm.width + this.hgap;
    }
    drawExtraFigure(selection, x, y, this);
    addInteraction(my_svg, x, y, this);
    return my_svg;
}

/**************** vcat *******************/

bboxFunction.vcat = function() {
    var inner = this.inner;
    var width = 0;
    var height = 0;
    for (var i = 0; i < inner.length; i++) {
        var elm = inner[i];
        elm.bbox();
        width = Math.max(width, elm.width);
        height += elm.height;
    }
    this.width = width;
    this.height = height + (inner.length - 1) * this.vgap;
}

drawFunction.vcat = function (selection, x, y) {
    var my_svg = selection
        .append("rect")
        .attr("x", x)
        .attr("y", y)
        .attr("width", this.width)
        .attr("height", this.height)
        .style("fill", "none");

    var inner = this.inner;
    var halign = this.halign;
    for (var i = 0; i < inner.length; i++) {
        var elm = inner[i];
        elm.draw(selection, x + halign * (this.width - elm.width), y);
        y += elm.height;
    }
    drawExtraFigure(selection, x, y, this);
    addInteraction(my_svg, x, y, this);
    return my_svg;
}

/**************** text *******************/

bboxFunction.text = function() {
    var svgtmp = d3.select("body").append("svg").attr("id", "svgtmp").attr("width", 1000).attr("height", 1000);
    //console.log("svgtmp", svgtmp);
    var txt = svgtmp.append("text")
        .attr("x", 0)
        .attr("y", 0)
        .style("text-anchor", "start")
        .text(this.textValue)
        .style("font-family", this["font-family"])
        .style("font-style", this["font-style"])
        .style("font-weight", this["font-weight"])
        .style("font-size", this["font-size"])
        .style("stroke", this.stroke)
        .style("fill",   this.stroke);
   
    var bb = txt.node().getBBox();
    svgtmp.node().remove();
    this.width = Math.max(this.width,1.05*bb.width);
    this.height = Math.max(this.height, 1.05*bb.height);
    this.ascent = bb.y; // save the y of the bounding box as ascent
    console.log("text:", this.width, this.height, this.ascent);
}

drawFunction.text = function (selection, x, y) {
    var my_svg =  selection
        .append("text")
        .attr("x", x)
        .attr("y", y - this.ascent) // take ascent into account
        .style("text-anchor", "start")
        .text(this.textValue)
        .style("font-family", this["font-family"])
        .style("font-style", this["font-style"])
        .style("font-weight", this["font-weight"])
        .style("font-size", this["font-size"])
        .style("stroke", this.stroke)
        .style("fill",   this.stroke);
    
    drawExtraFigure(selection, x, y, this);
    addInteraction(my_svg, x, y, this);
    return my_svg;
}

/**************** scatterplot *******************/

bboxFunction.scatterplot = function() {
    if (this.width == 0) {
        this.width = 200;
    }
    if (this.height == 0) {
        this.height = 200;
    }
}

drawFunction.scatterplot = function (selection, x, y) {
    //Width and height
    var w = this.width;
    var h = this.height;
    var padding = 30;

    var dataset = this.dataset;
    console.log("dataset: ", dataset);
    console.log("datase[0] + 1: ", dataset[0] + 1);

    //Create scale functions
    var xScale = d3.scale.linear()
    .domain([0, d3.max(dataset, function(d) {
        return d[0];
    })])
    .range([padding, w - padding * 2]);

    var yScale = d3.scale.linear()
    .domain([0, d3.max(dataset, function(d) {
        return d[1];
    })])
    .range([h - padding, padding]);

    var rScale = d3.scale.linear()
    .domain([0, d3.max(dataset, function(d) {
        return d[1];
    })])
    .range([2, 5]);

    //Define X axis
    var xAxis = d3.svg.axis()
    .scale(xScale)
    .orient("bottom")
    .ticks(5);

    //Define Y axis
    var yAxis = d3.svg.axis()
    .scale(yScale)
    .orient("left")
    .ticks(5);

    //Create SVG element
    var svg = selection
    .append("svg")
    .attr("x", x)
    .attr("y", y)
    .attr("width", w)
    .attr("height", h);

    //Create circles
    svg.selectAll("circle")
    .data(dataset)
    .enter()
    .append("circle")
    .attr("cx", function(d) {
        return xScale(d[0]);
    })
    .attr("cy", function(d) {
        return yScale(d[1]);
    })
    .attr("r", function(d) {
        return rScale(d[1]);
    });

    //Create labels
    svg.selectAll("text")
    .data(dataset)
    .enter()
    .append("text")
    // .text(function(d) {
    // 		return d[0] + "," + d[1];
    // })
    .attr("x", function(d) {
        return xScale(d[0]);
    })
    .attr("y", function(d) {
        return yScale(d[1]);
    })
    .attr("font-family", "sans-serif")
    .attr("font-size", "11px")
    .attr("fill", "red");

    //Create X axis
    svg.append("g")
    .attr("class", "axis")
    .attr("transform", "translate(0," + (h - padding) + ")")
    .call(xAxis);

    //Create Y axis
    svg.append("g")
    .attr("class", "axis")
    .attr("transform", "translate(" + padding + ",0)")
    .call(yAxis);
    
    drawExtraFigure(selection, x, y, this);
    addInteraction(svg, x, y, this);
    return svg;
}

/**************** barchart ****************/

bboxFunction.barchart = function() {
    if (this.width == 0) {
        this.width = 200;
    }
    if (this.height == 0) {
        this.height = 200;
    }
}

drawFunction.barchart = function (selection, x, y) {

    //Width and height
    var w = this.width;
    var h = this.height;

    var dataset = this.dataset || [];

    var xScale = d3.scale.ordinal()
    .domain(d3.range(dataset.length))
    .rangeRoundBands([0, w], 0.05);

    var yScale = d3.scale.linear()
    .domain([0, d3.max(dataset)])
    .range([0, h]);

    //Create SVG element
    var svg = selection
    .append("svg")
    .attr("x", x)
    .attr("y", y)
    .attr("width", w)
    .attr("height", h);

    //Create bars
    svg.selectAll("rect")
    .data(dataset)
    .enter()
    .append("rect")
    .attr("x", function(d, i) {
        return xScale(i);
    })
    .attr("y", function(d) {
        return h - yScale(d);
    })
    .attr("width", xScale.rangeBand())
    .attr("height", function(d) {
        return yScale(d);
    })
    .attr("fill", function(d) {
        return "rgb(0, 0, " + (d * 10) + ")";
    });

    //Create labels
    svg.selectAll("text")
    .data(dataset)
    .enter()
    .append("text")
    .text(function(d) {
        return d;
    })
    .attr("text-anchor", "middle")
    .attr("x", function(d, i) {
        return xScale(i) + xScale.rangeBand() / 2;
    })
    .attr("y", function(d) {
        return h - yScale(d) + 14;
    })
    .attr("font-family", "sans-serif")
    .attr("font-size", "11px")
    .attr("fill", "white");
    
    drawExtraFigure(selection, x, y, this);
    addInteraction(svg, x, y, this);
}

/**************** graph *******************/

bboxFunction.graph = function() {
    if (this.width == 0) {
        this.width = 200;
    }
    if (this.height == 0) {
        this.height = 200;
    }
}

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

drawFunction.graph = function (selection, x, y) {
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
  selection = selection.append("svg").attr("width", 650).attr("height", 680).append("g").attr("transform", "translate(20,20)");
  var layout = GraphRenderer.run(dagreD3.json.decode(nodes, edges), selection);
  selection
    .attr("width", layout.graph().width + 40)
    .attr("height", layout.graph().height + 40);

    drawExtraFigure(selection, x, y, this);
    addInteraction(selection, x, y, this);
}

// drawFunction.graph = function (selection, x, y) {
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

// visibility control

/********************* choice ***************************/

bboxFunction.choice = function() {
    var inner = this.inner;
    var selector = Math.min(Math.max(Figure.getModelElement(this.selector),0), inner.length - 1);
    var selected = inner[selector];
    selected.bbox();
    this.width = selected.width;
    this.height = selected.height;
}

drawFunction.choice = function (selection, x, y) {
    var inner = this.inner;
    var selector = Math.min(Math.max(Figure.getModelElement(this.selector),0), inner.length -1);
    var selected = this.inner[selector];
    return selected.draw(selection, x, y);
}

/********************* visible ***************************/

bboxFunction.visible = function() {
    var inner = this.inner;
    var visible = Figure.getModelElement(this.selector);
    if(visible){
        inner.bbox();
        this.width = inner.width;
        this.height = inner.height;
    } else {
        this.width = this.height = 0;
    }
}

drawFunction.visible = function (selection, x, y) {
    var inner = this.inner;
    var visible = Figure.getModelElement(this.selector);
    return visible ? inner.draw(selection, x, y) : selection;
} 

// Input elements

/********************* buttonInput ***************************/

bboxFunction.buttonInput = function() {
    if (!this.width || this.width == 0) {
        this.width = 200;
    }
    if (!this.height || this.height == 0) {
        this.height = 200;
    }
}

drawFunction.buttonInput = function (selection, x, y) {
    var fig = this;
    var accessor = this.accessor; 
    var b = Figure.getModelElement(accessor);
    
    var foreign = selection.append("foreignObject")
        .attr("x", x).attr("y", y).attr("width", this.width).attr("height", this.height);
   
    foreign.append("xhtml:body")
        .append("form").attr("action", "")
        .append("input")
            .style("width", this.width + "px").style("height", this.height + "px")
            .attr("type", "button").attr("value", b ? this.trueText : this.falseText);
        
     foreign.on("mousedown", function() {
          var b = !Figure.getModelElement(accessor); 
          return handleUserInput(fig, b);
     });
}

/********************* checkboxInput ***************************/

bboxFunction.checkboxInput = bboxFunction.buttonInput;

drawFunction.checkboxInput = function (selection, x, y) {
    var fig = this;
    var accessor = this.accessor; 
    var b = Figure.getModelElement(accessor);
    
    var foreign = selection.append("foreignObject")
        .attr("x", x).attr("y", y).attr("width", this.width).attr("height", this.height);
    
    foreign.append("xhtml:body")
        .append("form").attr("action", "")
        .append("input")
            .style("width", this.width + "px").style("height", this.height + "px")
            .attr("type", "checkbox");
     if(b){
         foreign.select("input").attr("checked", "checked");
     }
        
     foreign.on("mousedown", function() {
         return handleUserInput(fig, !Figure.getModelElement(accessor));
     });
}

/********************* strInput ***************************/

bboxFunction.strInput = bboxFunction.buttonInput

drawFunction.strInput = function (selection, x, y) {
    var fig = this;
    var accessor = this.accessor; 
     
    var foreign = selection.append("foreignObject")
        .attr("x", x).attr("y", y).attr("width", this.width).attr("height", this.height);
    
    foreign.append("xhtml:body")
        .append("form").attr("action", "").attr("onsubmit", "return false;")
        .append("input")
            .style("width", this.width + "px").style("height", this.height + "px")
            .attr("type", "text").attr("value", Figure.getModelElement(accessor));
  
     foreign.on(fig.event, function() {
        return handleUserInput(fig, "'" + foreign.select("input")[0][0].value + "'");
     });
}

/********************* colorInput ***************************/

function colorNameToHex(color)
{
    var colors = {"aliceblue":"#f0f8ff","antiquewhite":"#faebd7","aqua":"#00ffff","aquamarine":"#7fffd4","azure":"#f0ffff",
    "beige":"#f5f5dc","bisque":"#ffe4c4","black":"#000000","blanchedalmond":"#ffebcd","blue":"#0000ff","blueviolet":"#8a2be2","brown":"#a52a2a","burlywood":"#deb887",
    "cadetblue":"#5f9ea0","chartreuse":"#7fff00","chocolate":"#d2691e","coral":"#ff7f50","cornflowerblue":"#6495ed","cornsilk":"#fff8dc","crimson":"#dc143c","cyan":"#00ffff",
    "darkblue":"#00008b","darkcyan":"#008b8b","darkgoldenrod":"#b8860b","darkgray":"#a9a9a9","darkgreen":"#006400","darkkhaki":"#bdb76b","darkmagenta":"#8b008b","darkolivegreen":"#556b2f",
    "darkorange":"#ff8c00","darkorchid":"#9932cc","darkred":"#8b0000","darksalmon":"#e9967a","darkseagreen":"#8fbc8f","darkslateblue":"#483d8b","darkslategray":"#2f4f4f","darkturquoise":"#00ced1",
    "darkviolet":"#9400d3","deeppink":"#ff1493","deepskyblue":"#00bfff","dimgray":"#696969","dodgerblue":"#1e90ff",
    "firebrick":"#b22222","floralwhite":"#fffaf0","forestgreen":"#228b22","fuchsia":"#ff00ff",
    "gainsboro":"#dcdcdc","ghostwhite":"#f8f8ff","gold":"#ffd700","goldenrod":"#daa520","gray":"#808080","green":"#008000","greenyellow":"#adff2f",
    "honeydew":"#f0fff0","hotpink":"#ff69b4",
    "indianred ":"#cd5c5c","indigo":"#4b0082","ivory":"#fffff0","khaki":"#f0e68c",
    "lavender":"#e6e6fa","lavenderblush":"#fff0f5","lawngreen":"#7cfc00","lemonchiffon":"#fffacd","lightblue":"#add8e6","lightcoral":"#f08080","lightcyan":"#e0ffff","lightgoldenrodyellow":"#fafad2",
    "lightgrey":"#d3d3d3","lightgreen":"#90ee90","lightpink":"#ffb6c1","lightsalmon":"#ffa07a","lightseagreen":"#20b2aa","lightskyblue":"#87cefa","lightslategray":"#778899","lightsteelblue":"#b0c4de",
    "lightyellow":"#ffffe0","lime":"#00ff00","limegreen":"#32cd32","linen":"#faf0e6",
    "magenta":"#ff00ff","maroon":"#800000","mediumaquamarine":"#66cdaa","mediumblue":"#0000cd","mediumorchid":"#ba55d3","mediumpurple":"#9370d8","mediumseagreen":"#3cb371","mediumslateblue":"#7b68ee",
    "mediumspringgreen":"#00fa9a","mediumturquoise":"#48d1cc","mediumvioletred":"#c71585","midnightblue":"#191970","mintcream":"#f5fffa","mistyrose":"#ffe4e1","moccasin":"#ffe4b5",
    "navajowhite":"#ffdead","navy":"#000080",
    "oldlace":"#fdf5e6","olive":"#808000","olivedrab":"#6b8e23","orange":"#ffa500","orangered":"#ff4500","orchid":"#da70d6",
    "palegoldenrod":"#eee8aa","palegreen":"#98fb98","paleturquoise":"#afeeee","palevioletred":"#d87093","papayawhip":"#ffefd5","peachpuff":"#ffdab9","peru":"#cd853f","pink":"#ffc0cb","plum":"#dda0dd","powderblue":"#b0e0e6","purple":"#800080",
    "red":"#ff0000","rosybrown":"#bc8f8f","royalblue":"#4169e1",
    "saddlebrown":"#8b4513","salmon":"#fa8072","sandybrown":"#f4a460","seagreen":"#2e8b57","seashell":"#fff5ee","sienna":"#a0522d","silver":"#c0c0c0","skyblue":"#87ceeb","slateblue":"#6a5acd","slategray":"#708090","snow":"#fffafa","springgreen":"#00ff7f","steelblue":"#4682b4",
    "tan":"#d2b48c","teal":"#008080","thistle":"#d8bfd8","tomato":"#ff6347","turquoise":"#40e0d0",
    "violet":"#ee82ee",
    "wheat":"#f5deb3","white":"#ffffff","whitesmoke":"#f5f5f5",
    "yellow":"#ffff00","yellowgreen":"#9acd32"};

    if (typeof colors[color.toLowerCase()] != 'undefined')
        return colors[color.toLowerCase()];

    return color;
}

bboxFunction.colorInput = bboxFunction.buttonInput;

drawFunction.colorInput = function (selection, x, y) {
    var fig = this;
    var accessor = this.accessor;
    
    var foreign = selection.append("foreignObject")
        .attr("x", x).attr("y", y).attr("width", this.width).attr("height", this.height);
    
    console.log(Figure.getModelElement(accessor));
    foreign.append("xhtml:body")
        .append("form").attr("action", "").attr("onsubmit", "return false")
        .append("input")
            .style("width", this.width + "px").style("height", this.height + "px")
            .attr("type", "color").attr("value", Figure.getModelElement(accessor));
            
    foreign.on("change", function() {
            return handleUserInput(fig, "\"" + foreign.select("input")[0][0].value + "\"");
     });
}

/********************* numInput ***************************/

bboxFunction.numInput  = bboxFunction.buttonInput;

drawFunction.numInput = function (selection, x, y) {
    var fig = this;
    var accessor = this.accessor;
    
    var foreign = selection.append("foreignObject")
        .attr("x", x).attr("y", y).attr("width", this.width).attr("height", this.height);
    
    foreign.append("xhtml:body")
        .append("form").attr("action", "").attr("onsubmit", "return false")
        .append("input")
            .style("width", this.width + "px").style("height", this.height + "px")
            .attr("type", "number").attr("value", Figure.getModelElement(accessor));
     
     foreign.on(this.event, function() {
            return handleUserInput(fig, foreign.select("input")[0][0].value);
     });
}

/********************* rangeInput ***************************/

bboxFunction.rangeInput = bboxFunction.buttonInput

drawFunction.rangeInput = function (selection, x, y) { 
    var fig = this;
    var accessor = this.accessor;
    
    var foreign = selection.append("foreignObject")
        .attr("x", x).attr("y", y).attr("width", this.width).attr("height", this.height);
    
    foreign.append("xhtml:body")
        .append("form").attr("action", "").attr("onsubmit", "return false")
        .append("input")
            .style("width", this.width + "px").style("height", this.height + "px")
            .attr("type", "range")
            .attr("min", this.min).attr("max", this.max).attr("step", this.step)
            .attr("value", Figure.getModelElement(this.accessor));
        
     foreign.on(fig.event, function(){
            return handleUserInput(fig, foreign.select("input")[0][0].value);
     });
}

/********************* choiceInput ***************************/

bboxFunction.choiceInput = bboxFunction.buttonInput

drawFunction.choiceInput = function (selection, x, y) { 
    var fig = this;
    var accessor = this.accessor;
    var selectedIndex = Figure.getModelElement(this.accessor);
    
    var foreign = selection.append("foreignObject")
        .attr("x", x).attr("y", y).attr("width", this.width).attr("height", this.height);
    
    var select =foreign.append("xhtml:body")
        .append("form").attr("action", "").attr("onsubmit", "return false")
        .append("select").attr("value", selectedIndex).attr("selectedIndex", selectedIndex)
            .style("width", this.width + "px").style("height", this.height + "px");
    
    for(var i = 0; i < this.choices.length; i++){
        var opt = select.append("option").attr("value", i);
        if(i == selectedIndex){
            opt.attr("selected", "selected");
        }
        opt.text(this.choices[i]);
    }
        
     foreign.on(fig.event, function(){
            return handleUserInput(fig, foreign.select("select")[0][0].selectedIndex);
     });
}


/****************** linechart ************************/

bboxFunction.linechart = function() {
    if (this.width == 0) {
        this.width = 200;
    }
    if (this.height == 0) {
        this.height = 200;
    }
}

drawFunction.linechart = function (selection, x, y) {

nv.addGraph(function() {
  var chart = nv.models.lineChart()
                .margin({left: 100})  //Adjust chart margins to give the x-axis some breathing room.
                .useInteractiveGuideline(true)  //We want nice looking tooltips and a guideline!
                .transitionDuration(350)  //how fast do you want the lines to transition?
                .showLegend(true)       //Show the legend, allowing users to turn on/off line series.
                .showYAxis(true)        //Show the y-axis
                .showXAxis(true)        //Show the x-axis
  ;

  chart.xAxis     //Chart x-axis settings
      .axisLabel('Time (ms)')
      .tickFormat(d3.format(',r'));

  chart.yAxis     //Chart y-axis settings
      .axisLabel('Voltage (v)')
      .tickFormat(d3.format('.02f'));

  /* Done setting the chart up? Time to render it!*/
  var myData = sinAndCos();   //You need data...

  selection    //Select the <svg> element you want to render the chart in.   
      .datum(myData)         //Populate the <svg> element with chart data...
      .call(chart);          //Finally, render the chart!

  //Update the chart when window resizes.
  nv.utils.windowResize(function() { chart.update() });
  return chart;
});
/**************************************
 * Simple test data generator
 */
function sinAndCos() {
  var sin = [],sin2 = [],
      cos = [];

  //Data is represented as an array of {x,y} pairs.
  for (var i = 0; i < 100; i++) {
    sin.push({x: i, y: Math.sin(i/10)});
    sin2.push({x: i, y: Math.sin(i/10) *0.25 + 0.5});
    cos.push({x: i, y: .5 * Math.cos(i/10)});
  }

  //Line chart data should be sent as an array of series objects.
  return [
    {
      values: sin,      //values - represents the array of {x,y} data points
      key: 'Sine Wave', //key  - the name of the series.
      color: '#ff7f0e'  //color - optional: choose your own line color.
    },
    {
      values: cos,
      key: 'Cosine Wave',
      color: '#2ca02c'
    },
    {
      values: sin2,
      key: 'Another sine wave',
      color: '#7777ff',
      area: true      //area - set to true if you want this line to turn into a filled area chart.
    }
  ];
}
}

