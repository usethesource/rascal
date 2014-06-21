

var ex1 = {name: 			"box",
		   size:			[100, 100],
		   lineWidth:	1,
		   inner:			{name:		"box",
					 		size:		[50,50],
							 align:		[1,1],
					 		fillColor:	"red"}
		  };

var ex2 = {name: 			"box",
		   size:			[100, 100],
		   lineWidth:		1,
		   inner:			{	name:		"box",
					 			size:		[50,50],
					 			fillColor:	"red"
							}
		  };

var ex3 = {name: 			"hcat",
		   size:			[100, 100],
		   lineWidth:		1,
		   inner:			[ {	name:		"box",
					 			size:		[50,50],
					 			fillColor:	"red"},
							  {	name:		"box",
					 			size:		[200,200],
					 			fillColor:	"green"}
							 ]
		  };

var ex4 = {name: 			"hcat",
		   size:			[100, 100],
		   align:			[0.5, 0.8],
		   lineWidth:		1,
		   inner:			[ {	name:		"box",
					 			size:		[50,50],
					 			fillColor:	"red"},
							  {	name:		"box",
					 			size:		[200,200],
					 			fillColor:	"green"},
							  {	name:		"box",
					 			size:		[100,100],
					 			fillColor:	"blue"}
							 ]
		  };

var ex5 = {name: 			"vcat",
		   size:			[100, 100],
		   align:			[0.8, 0.5],
		   lineWidth:		1,
		   inner:			[ {	name:		"box",
					 			size:		[50,50],
					 			fillColor:	"red"},
							  {	name:		"box",
					 			size:		[200,200],
					 			fillColor:	"green"},
							  {	name:		"box",
					 			size:		[100,100],
					 			fillColor:	"blue"}
							 ]
		  };

var ex6 = {name: 			"text",
		   fontSize:		20,
		   textValue:		"Hello"
		  };

var ex7 = {name:			"box",
		   inner:			{name: 			"text",
		   					fontSize:		20,
		   					textValue:		"Hello"
		  					}
		  };

var DATA1 = [ 11, 12, 15, 20, 18, 17, 16, 18, 23, 25 ];

var DATA2 = [[5, 20], [480, 90], [250, 50], [100, 33], [330, 95],[410, 12], [475, 44], [25, 67], [85, 21], [220, 88], [600, 150]];

var ex8 = {name: "scatterplot",
		   size:	[400,300],
		   dataset:	DATA2
		  };

var ex9 = {name:		"hcat",
		   inner:		[
			   				{	name: "scatterplot",
		   						size:	[400,300],
		  						dataset:	DATA2
		  					},
			   				ex5
		   				]
		  };

var ex9 = {name: "box", fillColor: "blue", width: 200, height: 200, inner: {name:	"box", fillColor: "yellow", width: 50, height: 100 } };

var Figure = {
	name: 			"figure",
	textValue:		"none",
	size:			[0,0],
	gap:			[0,0],
	fillColor:		"white",
	fillOpacity:	1.0,
	lineWidth:		1,
	lineColor:		"black",
	lineStyle:		"solid",
	lineOpacity:	1.0,
	borderRadius:	0,
	align:			[0.5, 0.5],
	fontName:		"Arial",
	fontSize:		12,
	dataset:		[]
}

// Determine the size of the various figures types

var sizeofFunction = {};

// Make the various figure types

var makeFunction = {};

makeFunction.box = makeRectSvg;

makeFunction.hcat = makeHcatSvg;

makeFunction.vcat = makeVcatSvg;

makeFunction.text = makeTextSvg;

makeFunction.scatterplot = makeScatterplotSvg;


/**************** box *******************/

sizeofFunction.box = function(){
	var sz = this.size;
	var lw = this.lineWidth;
	sz[0] += lw/2;
	sz[1] += lw/2;
	console.log("box.sizeof:", sz);
	if(this.hasOwnProperty("inner")){
		console.log(this.inner);
		sz_inner = this.inner.sizeof();
		console.log("inner", sz_inner);
		this.size = [Math.max(sz[0], sz_inner[0] + 20), Math.max(sz[1], sz_inner[1] + 20)];
		console.log("outer size:", this.size);
		return this.size;
	}
// 	if(this.pos){
// 		p = this.pos;
// 		return [p[0] + sz[0], p[1] + sz[1]];
// 	}
	this.size = sz;
	return sz;
}
/*
function makeRectHtml(selection, x, y){
	var sel = selection;
	var align = this.align;
	var computed_size = this.size;
	var width = computed_size[0];
	var height = computed_size[1];
	var outersel = selection
			.append("div")
			.attr("class", "rascal-fig-box")
			.style("left", x +"px")
			.style("top", y + "px")
			.style("width", width + "px")
			.style("height", height + "px")
			.style("position", "absolute")
			.style("margin", "0 auto")
			.style("border-radius", this.borderRadius + "px")
			.style("border-color", this.lineColor)
	   		.style("background-color", this.fillColor)
			.style("border-width", this.lineWidth  + "px")
			.style("border-style", this.lineStyle)
	if(this.hasOwnProperty("inner")){
		var inner = this.inner;
		inner.make(outersel, x + inner.align[0]*(width - inner.size[0]), y + inner.align[1]*(height - inner.size[1]));
	}
	return outersel;
}
*/
function makeRectSvg(selection, x, y){
	var sel = selection;
	var align = this.align;
	var computed_size = this.size;
	var width = computed_size[0];
	var height = computed_size[1];
	var outersel = selection
			.append("rect")
			.attr("x", x)
			.attr("y", y)
			.attr("width", width)
			.attr("height", height)
			.style("stroke", this.lineColor)
	   		.style("fill", this.fillColor)
			.style("stroke-width", this.lineWidth  + "px")
			.style("stroke-dasharray", this.lineStyle)
	if(this.hasOwnProperty("inner")){
		var inner = this.inner;
		inner.make(sel, x + inner.align[0]*(width - inner.size[0]), y + inner.align[1]*(height - inner.size[1]));
	}
	return sel;
}

/**************** hcat *******************/

sizeofFunction.hcat = function(){
	var inner = this.inner;
	var width = 0;
	var height = 0;
	for(var i = 0; i < inner.length; i++){
		var sz = inner[i].sizeof();
		width += sz[0];
		height = Math.max(height, sz[1]);
	}
	this.size = [width, height];
	return this.size;
}

function makeHcatSvg(selection, x, y){
	var computed_size = this.size;
	var width = computed_size[0];
	var height = computed_size[1];
	var outersel = selection
			.append("rect")
			.attr("x", x)
			.attr("y", y)
			.attr("width", width)
			.attr("height", height)
			.style("fill", "none");
	
	var inner = this.inner;
	console.log("hcat:", inner);
	var valign = this.align[1];
	for(var i = 0; i < inner.length; i++){
		console.log("hcat: ", i, inner[i], inner[i].size, inner[i].size[1], y + valign * (height - inner[i].size[1]));
		inner[i].make(selection, x, y + valign * (height - inner[i].size[1]));
		x += inner[i].size[0];
	}
	return outersel;
}

/*
function makeHcatHtml(selection, x, y){
	var computed_size = this.size;
	var width = computed_size[0];
	var height = computed_size[1];
	var outersel = selection
			.append("div")
			.attr("class", "rascal-fig-box")
			.style("left", x +"px")
			.style("top", y + "px")
			.style("width", width + "px")
			.style("height", height + "px")
			.style("position", "absolute");
	
	var inner = this.inner;
	var valign = this.align[1];
	for(i = 0; i < inner.length; i++){
		inner[i].make(outersel, x, y + valign * (height - inner[i].size[1]));
		x += inner[i].size[0];
	}
	return outersel;
}
*/

/**************** vcat *******************/

sizeofFunction.vcat = function(){
	var inner = this.inner;
	var width = 0;
	var height = 0;
	for(var i = 0; i < inner.length; i++){
		var sz = inner[i].sizeof();
		width  = Math.max(width, sz[0]);
		height += sz[1];
	}
	this.size = [width, height];
	console.log("vcat.sizeof:", this.size);
	return this.size;
}

function makeVcatSvg(selection, x, y){
	var computed_size = this.size;
	var width = computed_size[0];
	var height = computed_size[1];
	var outersel = selection
			.append("rect")
			.attr("x", x)
			.attr("y", y)
			.attr("width", width)
			.attr("height", height)
			.style("fill", "none");
	
	var inner = this.inner;
	var halign = this.align[0];
	for(var i = 0; i < inner.length; i++){
		inner[i].make(selection, x + halign * (width - inner[i].size[0]), y);
		y += inner[i].size[1];
	}
	return outersel;
}

/*
function makeHcatHtml(selection, x, y){
	var computed_size = this.size;
	var width = computed_size[0];
	var height = computed_size[1];
	var outersel = selection
			.append("div")
			.attr("class", "rascal-fig-box")
			.style("left", x +"px")
			.style("top", y + "px")
			.style("width", width + "px")
			.style("height", height + "px")
			.style("position", "absolute");
	
	var inner = this.inner;
	var valign = this.align[1];
	for(i = 0; i < inner.length; i++){
		inner[i].make(outersel, x, y + valign * (height - inner[i].size[1]));
		x += inner[i].size[0];
	}
	return outersel;
}
*/

/**************** text *******************/

sizeofFunction.text = function(){
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
	this.size = [bb.width, bb.height];
	this.y = bb.y;	// save the y of the bounding box as ascent
	return this.size;
}

function makeTextSvg(selection, x, y){
	return selection
			.append("text")
			.attr("x", x)
			.attr("y", y - this.y)	// take ascent into account
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

function makeTextHtml(selection, x, y){
	return selection
			.append("div")
			.attr("class", "rascal-fig-text")
// 			.attr("x", x)
// 			.attr("y", y)
 			.text(options.textValue)
// 			.style("font", "font" in options ? options.font : "Arial")
// 			.style("font-size", "font_size" in options ? options.font_size : 12)
// 			.style("text-anchor", "start")
// 	   		.style("fill", "fill" in options ? options.fill : "black")
// 	   		.style("fill-opacity", "fill_opacity" in options ? options.fill_opacity : 1)
// 			.style("stroke-width", "stroke_width" in options ? options.stroke_width : 1)
// 			.style("stroke-dasharray", "stroke_dasharray" in options ? options.stroke_dasharray : [])
// 			.style("stroke-opacity", "stroke_opacity" in options ? options.stroke_opacity : 1.0);
}

/**************** scatterplot *******************/

sizeofFunction.scatterplot = function(){
	var sz = this.size || [200,200];
	this.size = sz;
	return sz;
}

function makeScatterplotSvg(selection, x, y){
			//Width and height
			var sz = this.size;
			var w = sz[0];
			var h = sz[1];
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


// Create a figure

function makeFigure(description){
	var f = figure(description);
	var sz = f.sizeof();
	sz[0] += 0;
	sz[1] += 0;
	var ps = f.pos || [0,0];
	//svg = d3.select("#svgarea").attr("width", sz[0]).attr("height", sz[1]);
	var area = d3.select("#figurearea").append("svg").attr("width", sz[0]).attr("height", sz[1]);
	//svgtmp = d3.select("body").append("svg").attr("id","svgtmp").attr("width", 100).attr("height", 100);
	return f.make(area, ps[0], ps[1]);
}

// Create a figure from its JSON representation

function isArray(myArray) {
	return Object.prototype.toString.call(myArray) === "[object Array]";
}

function figure(description){
	return figure1(description, Figure);
}

function figure1(description, parent){
	var f = Object.create(parent);
	f.sizeof = sizeofFunction[description.name]; // || throw "No sizeof function defined for " + description.name;
	f.make = makeFunction[description.name]; // || throw "No make function defined for " + description.name;
	for(var prop in description){
		if(prop === "inner"){
			var inner_description = description[prop];
			if(isArray(inner_description)){
				console.log("ARRAY CASE");
				var inner_array = new Array();
				for(var i = 0; i < inner_description.length; i++){
					inner_array[i] = figure1(inner_description[i], f);
				}
				f[prop] = inner_array;
			} else {
				console.log("NO ARRAY CASE");
				f[prop] = figure1(inner_description, f);
			}
		} else {
			f[prop] = description[prop];
		}
	}
	return f;
}




