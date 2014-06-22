

var ex1 = {figure: 			"box",
		   width:			100,
		   height:			100,
		   lineWidth:		1,
		   inner:			{figure:		"box",
							 width:		50,
							 height:	50,
							 halign:	1,
							 valign:	1,
					 		 fillColor:	"red"
							}
		  };

var ex2 = {figure: 			"box",
		   width:			100,
		   height:			100,
		   lineWidth:		1,
		   inner:			{	figure:		"box",
							 	width:		50,
							 	height:		50,
					 			fillColor:	"red"
							}
		  };

var ex3 = {figure: 			"hcat",
		   width:			100,
		   height:			100,
		   lineWidth:		1,
		   inner:			[ {	figure:		"box",
							   	width:		50,
							   	height:		50,
					 			fillColor:	"red"},
							  {	figure:		"box",
							   	width:		200,
							   	height:		200,
					 			fillColor:	"green"}
							 ]
		  };

var ex4 = {figure: 			"hcat",
		   size:			[100, 100],
		   width:			100,
		   height:			100,
		   halign:			0.5,
		   valign:			0.8,
		   lineWidth:		1,
		   inner:			[ {	figure:		"box",
							   	width:		50,
							   	height:		50,
					 			fillColor:	"red"},
							  {	figure:		"box",
							   	width:		200,
							   	height:		200,
					 			fillColor:	"green"},
							  {	figure:		"box",
							  	width:		100,
							   	height:		100,
					 			fillColor:	"blue"}
							 ]
		  };

var ex5 = {figure: 			"vcat",
		   width:			100,
		   height:			100,
		   halign:			0.8,
		   valign:			0.5,
		   lineWidth:		1,
		   inner:			[ {	figure:		"box",
					 			width:		50,
							   	height:		50,
					 			fillColor:	"red"},
							  {	figure:		"box",
					 			width:		200,
							   	height:		200,
					 			fillColor:	"green"},
							  {	figure:		"box",
					 			width:		100,
							   	height:		100,
					 			fillColor:	"blue"}
							 ]
		  };

var ex6 = {figure: 			"text",
		   fontSize:		20,
		   textValue:		"Hello"
		  };

var ex7 = {figure:			"box",
		   inner:			{figure: 		"text",
		   					fontSize:		20,
		   					textValue:		"Hello"
		  					}
		  };

var DATA1 = [ 11, 12, 15, 20, 18, 17, 16, 18, 23, 25 ];

var DATA2 = [[5, 20], [480, 90], [250, 50], [100, 33], [330, 95],[410, 12], [475, 44], [25, 67], [85, 21], [220, 88], [600, 150]];

var ex8 = {figure: 		"scatterplot",
		   width:		400,
		   height:		300,
		   dataset:		DATA2
		  };

var ex9 = {figure:		"hcat",
		   inner:		[
			   				{	figure: 	"scatterplot",
		   						width:		400,
		   						height:		300,
		  						dataset:	DATA2
		  					},
			   				ex5
		   				]
		  };

var ex9 = {figure: "box", fillColor: "blue", width: 200, height: 200, inner: {figure:	"box", fillColor: "yellow", width: 50, height: 100 } };

/****************** The figure prototype with all defaults *********************/

var Figure = {
	figure: 		"figure",
	textValue:		"none",
	width:			0,
	height:			0,
	hgap:			0,
	vgap:			0,
	fillColor:		"white",
	fillOpacity:	1.0,
	lineWidth:		1,
	lineColor:		"black",
	lineStyle:		"solid",
	lineOpacity:	1.0,
	borderRadius:	0,
	halign:			0.5,
	valign:			0.5,
	fontName:		"Arial",
	fontSize:		12,
	dataset:		[]
}


/****************** Build a figure given its JSON representation ****************/

function isArray(myArray) {
	return Object.prototype.toString.call(myArray) === "[object Array]";
}

function buildFigure(description){
	return buildFigure1(description, Figure);
}

function buildFigure1(description, parent){
	var f = Object.create(parent);
	f.bbox = bboxFunction[description.figure]; // || throw "No bbox function defined for " + description.figure;
	f.draw = drawFunction[description.figure]; // || throw "No draw function defined for " + description.figure;
	for(var prop in description){
		if(prop === "inner"){
			var inner_description = description[prop];
			if(isArray(inner_description)){
				var inner_array = new Array();
				for(var i = 0; i < inner_description.length; i++){
					inner_array[i] = buildFigure1(inner_description[i], f);
				}
				f[prop] = inner_array;
			} else {
				f[prop] = buildFigure1(inner_description, f);
			}
		} else {
			f[prop] = description[prop];
		}
	}
	return f;
}

/****************** Draw a figure object ****************/

function drawFigure(description){
	var f = buildFigure(description);
	f.bbox();
	var x = f.x || 0;
	var y = f.y || 0;
	var area = d3.select("#figurearea").append("svg").attr("width", f.width).attr("height", f.height);
	return f.draw(area, x, y);
}

/****************** Bounding box and draw function table *******/

// Determine the bounding box of the various figures types
var bboxFunction = {};

// Draw the various figure types
var drawFunction = {};

/**************** box *******************/

bboxFunction.box = function(){
	var width = this.width;
	var height = this.height;
	var lw = this.lineWidth;
	width += (lw + 1)/2;
	height += (lw + 1)/2;
	console.log("box.bbox:", width, height);
	if(this.hasOwnProperty("inner")){
		var inner = this.inner;
		console.log(inner);
		inner.bbox();
		console.log("inner", inner.width, inner.height);
		width = Math.max(width, inner.width + 2 * this.hgap);
		height = Math.max(height, inner.height + 2 * this.vgap);
		console.log("outer size:", width, height);
	}
// 	if(this.pos){
// 		p = this.pos;
// 		return [p[0] + sz[0], p[1] + sz[1]];
// 	}
	this.width = width;
	this.height = height;
	return;
}

drawFunction.box = function (selection, x, y){
	var sel = selection;
	var outersel = selection
			.append("rect")
			.attr("x", x)
			.attr("y", y)
			.attr("width", this.width)
			.attr("height", this.height)
			.style("stroke", this.lineColor)
	   		.style("fill", this.fillColor)
			.style("stroke-width", this.lineWidth  + "px")
			.style("stroke-dasharray", this.lineStyle)
	if(this.hasOwnProperty("inner")){
		var inner = this.inner;
		inner.draw(sel, x + this.hgap + inner.halign * (this.width  - inner.width  - 2 * this.hgap), 
						y + this.vgap + inner.valign * (this.height - inner.height - 2 * this.vgap));
	}
	return addInteraction(sel, this);
}

function addInteraction(selection, fig){
	if(fig.hasOwnProperty("onClick")){
		selection.on("click", function() { post(fig.site + "/do_callback/" + fig.onClick); });
	}
	return selection;
}

function post(path, params, method) {
    method = method || "post"; // Set method to post by default if not specified.
    
    // The rest of this code assumes you are not using a library.
    // It can be made less wordy if you use one.
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", path);

    for(var key in params) {
        if(params.hasOwnProperty(key)) {
            var hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", params[key]);

            form.appendChild(hiddenField);
         }
    }

    document.body.appendChild(form);
    form.submit();
}



/**************** hcat *******************/

bboxFunction.hcat = function(){
	var inner = this.inner;
	var width = 0;
	var height = 0;
	for(var i = 0; i < inner.length; i++){
		var elm = inner[i];
		elm.bbox();
		width += elm.width;
		height = Math.max(height, elm.height);
	}
	this.width = width + (inner.length - 1) * this.hgap;	//TODO length == 0
	this.height = height;
	return;
}

drawFunction.hcat = function (selection, x, y){
	var outersel = selection
			.append("rect")
			.attr("x", x)
			.attr("y", y)
			.attr("width", this.width)
			.attr("height", this.height)
			.style("fill", "none");
	
	var inner = this.inner;
	console.log("hcat:", inner);
	for(var i = 0; i < inner.length; i++){
		var elm = inner[i];
		elm.draw(selection, x, y + this.valign * (this.height - elm.height));
		x += elm.width + this.hgap;
	}
	return outersel;
}

/**************** vcat *******************/

bboxFunction.vcat = function(){
	var inner = this.inner;
	var width = 0;
	var height = 0;
	for(var i = 0; i < inner.length; i++){
		var elm = inner[i];
		elm.bbox();
		width  = Math.max(width, elm.width);
		height += elm.height;
	}
	this.width = width;
	this.height = height + (inner.length - 1) * this.vgap;
	return;
}

drawFunction.vcat = function (selection, x, y){
	var outersel = selection
			.append("rect")
			.attr("x", x)
			.attr("y", y)
			.attr("width", this.width)
			.attr("height", this.height)
			.style("fill", "none");
	
	var inner = this.inner;
	var halign = this.halign;
	for(var i = 0; i < inner.length; i++){
		var elm = inner[i];
		elm.draw(selection, x + halign * (this.width - elm.width), y);
		y += elm.height;
	}
	return outersel;
}

/**************** text *******************/

bboxFunction.text = function(){
	var svgtmp = d3.select("body").append("svg").attr("id","svgtmp").attr("width", 100).attr("height", 100);
	//console.log("svgtmp", svgtmp);
 	var txt = svgtmp.append("text")
			.attr("x", 0)
			.attr("y", 0)
			.style("text-anchor", "start")
  			.text(this.textValue)
 			.style("font", this.fontName)
  			.style("font-size", this.fontSize)
 			.style("stroke", this.lineColor)
 	   		.style("fill", this.fillColor);
	//console.log("svgtmp", svgtmp);
	//console.log("txt", txt);
	var bb = txt.node().getBBox();
	svgtmp.node().remove();
	this.width = bb.width;
	this.height = bb.height;
	this.ascent = bb.y;	// save the y of the bounding box as ascent
	console.log("text:", this.width, this.height, this.ascent);
	return;
}

drawFunction.text = function (selection, x, y){
	return selection
			.append("text")
			.attr("x", x)
			.attr("y", y - this.ascent)	// take ascent into account
			.style("text-anchor", "start")
 			.text(this.textValue)
 			.style("font", this.fontName)
 			.style("font-size", this.fontSize)
			.style("stroke", this.lineColor)
	   		.style("fill", this.fillColor)
// 	   		.style("fill-opacity", "fill_opacity" in options ? options.fill_opacity : 1)
// 			.style("stroke-width", "stroke_width" in options ? options.stroke_width : 1)
// 			.style("stroke-dasharray", "stroke_dasharray" in options ? options.stroke_dasharray : [])
// 			.style("stroke-opacity", "stroke_opacity" in options ? options.stroke_opacity : 1.0);
}

/**************** scatterplot *******************/

bboxFunction.scatterplot = function(){
	if(this.width == 0){
		this.width = 200;
	}
	if(this.height == 0){
		this.height = 200;
	}
}

drawFunction.scatterplot = function (selection, x, y){
			//Width and height
			var w = this.width;
			var h = this.height;
			var padding = 30;
			
			var dataset = this.dataset;
			console.log("dataset: ", dataset);
			console.log("datase[0] + 1: ", dataset[0] + 1);

			//Create scale functions
			var xScale = d3.scale.linear()
								 .domain([0, d3.max(dataset, function(d) { return d[0]; })])
								 .range([padding, w - padding * 2]);

			var yScale = d3.scale.linear()
								 .domain([0, d3.max(dataset, function(d) { return d[1]; })])
								 .range([h - padding, padding]);

			var rScale = d3.scale.linear()
								 .domain([0, d3.max(dataset, function(d) { return d[1]; })])
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
}	

/**************** barchart ****************/

bboxFunction.barchart = function(){
	if(this.width == 0){
		this.width = 200;
	}
	if(this.height == 0){
		this.height = 200;
	}
}

drawFunction.barchart = function (selection, x, y){

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
}  

/**************** graph *******************/

bboxFunction.graph = function(){
	if(this.width == 0){
		this.width = 200;
	}
	if(this.height == 0){
		this.height = 200;
	}
}

drawFunction.graph = function (selection, x, y){
	var width = this.width,
		height = this.height,
		nodes = this.nodes || [],
		links = this.edges || [];
		
	console.log("nodes:", nodes);
	var defs = selection.append("defs");
	for(var i = 0; i < nodes.length; i++){
		console.log("node", i, nodes[i]);
		var f = buildFigure(nodes[i]);
		f.bbox();
		var d = defs.append("g").attr("id", "node" + i).attr("width", f.width).attr("height", f.height);
		nodes[i] = f.draw(d, 0, 0);
	}
	console.log("links", links);

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
   		.enter()
   		.append("use")
   		.attr("class", "node")
   		.attr("xlink:href", function(d, i) { return "#node" + i; })
   		.call(force.drag);
	
	force.on("tick", function() {
	
	  node.attr("transform", function(d) {return "translate(" + d.x + "," + d.y + ")";});
	 
      link.attr("x1", function(d) { return d.source.x + d.source.attr("width")/2; })
	      .attr("y1", function(d) { return d.source.y + d.source.attr("height")/2; })
	      .attr("x2", function(d) { return d.target.x + d.target.attr("width")/2; })
	      .attr("y2", function(d) { return d.target.y + d.target.attr("height")/2; });
	      
	  
    });
}

/********************* textfield ***************************/
bboxFunction.textfield = function(){
	if(this.width == 0){
		this.width = 200;
	}
	if(this.height == 0){
		this.height = 200;
	}
}

drawFunction.textfield = function (selection, x, y){

	//var form1 = "<form action=\"" + site + "/do_callback?callback=" + callback + "\"> <input type=\"submit\" value=\"Click me\"></form>";
	//var form2 = "<a href=\"" + site + "/do_callback/" + callback + "\"> Click me </a>";
	
	//var form3 = "<form action=\"\"> <input type=\"text\" id=\"callback_str_arg\" onchange=\"post('" + site + "/do_callback_str/" + callback + "', {callback_str_arg :  document.getElementById('callback_str_arg').value })\"\></form>";

	var form3 = "<form action='" + this.site + "/do_callback_str/" + this.callback + "' method='post'> <input type=\"text\" name=\"callback_str_arg\" /></form>";

	//alert(form3);
	return selection.append("foreignObject")
			.attr("x", x)
			.attr("y", y)
    		.attr("width", this.width)
    		.attr("height", this.height)
     		.append("xhtml:body")
    		.style("font", this.fontName)
 			.style("font-size", this.fontSize)
   			.html(form3);
}


