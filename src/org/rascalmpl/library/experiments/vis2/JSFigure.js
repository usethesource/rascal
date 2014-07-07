
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
    
Object.defineProperty(Figure, "height", { get: function(){ 
													if(this.hasOwnProperty("_height")) return this._height;
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
    f.bbox = Figure.bboxFunction[description.figure]; // || throw "No bbox function defined for " + description.figure;
    f.draw = Figure.drawFunction[description.figure]; // || throw "No draw function defined for " + description.figure;
    
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
Figure.bboxFunction = {};

// Draw the various figure types
Figure.drawFunction = {};

/**************** box *******************/

Figure.bboxFunction.box = function() {
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
    height += (lw + 1) / 2
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
    this.width = width;
    this.height = height;
}

Figure.drawFunction.box = function (selection, x, y) {
 	var lw = (this["stroke-width"])/2;		// TODO: check this
    var my_svg = selection
    .append("rect")
    .attr("x", x+lw)
    .attr("y", y+lw)
    .attr("width", this.width)
    .attr("height", this.height)
    .style("stroke", this.stroke)
    .style("fill", this.fill)
    .style("stroke-width", this["stroke-width"] + "px")
    .style("stroke-dasharray", this["stroke-dasharray"])
    if (this.hasOwnProperty("inner")) {
        var inner = this.inner;
        inner.draw(selection, x + lw + this.hgap + this.halign * (this.width - inner.width - 2 * this.hgap), 
                              y + lw + this.vgap + this.valign * (this.height - inner.height - 2 * this.vgap));
    }
    drawExtraFigure(selection, x, y, this);
    addInteraction(my_svg, x, y, this);
    return my_svg;
}

/**************** shape *****************/

Figure.bboxFunction.shape = function() {

}

Figure.drawFunction.shape = function (selection, x, y) {
	selection
		.append("path")
		.attr("d", this.path)
		.style("stroke", this.stroke)
    	.style("fill", this.fill)
    	.style("stroke-width", this["stroke-width"] + "px")
    	.style("stroke-dasharray", this["stroke-dasharray"])
    	;
}

/**************** hcat *******************/

Figure.bboxFunction.hcat = function() {
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

Figure.drawFunction.hcat = function (selection, x, y) {
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

Figure.bboxFunction.vcat = function() {
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

Figure.drawFunction.vcat = function (selection, x, y) {
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
        y += elm.height + this.vgap;
    }
    drawExtraFigure(selection, x, y, this);
    addInteraction(my_svg, x, y, this);
    return my_svg;
}

/**************** text *******************/

Figure.bboxFunction.text = function() {
    var svgtmp = d3.select("body").append("svg").attr("id", "svgtmp").attr("width", 1000).attr("height", 1000);
    //console.log("svgtmp", svgtmp);
    if(!this.width)
    	this.width = 0;
    if(!this.height)			// TODO: height is not set automatically, why?
    	this.height = 0;

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

Figure.drawFunction.text = function (selection, x, y) {
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

// visibility control

/********************* choice ***************************/

Figure.bboxFunction.choice = function() {
    var inner = this.inner;
    var selector = Math.min(Math.max(Figure.getModelElement(this.selector),0), inner.length - 1);
    var selected = inner[selector];
    selected.bbox();
    this.width = selected.width;
    this.height = selected.height;
}

Figure.drawFunction.choice = function (selection, x, y) {
    var inner = this.inner;
    var selector = Math.min(Math.max(Figure.getModelElement(this.selector),0), inner.length -1);
    var selected = this.inner[selector];
    return selected.draw(selection, x, y);
}

/********************* visible ***************************/

Figure.bboxFunction.visible = function() {
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

Figure.drawFunction.visible = function (selection, x, y) {
    var inner = this.inner;
    var visible = Figure.getModelElement(this.selector);
    return visible ? inner.draw(selection, x, y) : selection;
} 

/********************* Input elements ************************/

/********************* buttonInput ***************************/

Figure.bboxFunction.buttonInput = function() {
    if (!this.width || this.width == 0) {
        this.width = 200;
    }
    if (!this.height || this.height == 0) {
        this.height = 200;
    }
}

Figure.drawFunction.buttonInput = function (selection, x, y) {
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

Figure.bboxFunction.checkboxInput = Figure.bboxFunction.buttonInput;

Figure.drawFunction.checkboxInput = function (selection, x, y) {
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

Figure.bboxFunction.strInput = Figure.bboxFunction.buttonInput

Figure.drawFunction.strInput = function (selection, x, y) {
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

Figure.bboxFunction.colorInput = Figure.bboxFunction.buttonInput;

Figure.drawFunction.colorInput = function (selection, x, y) {
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

Figure.bboxFunction.numInput  = Figure.bboxFunction.buttonInput;

Figure.drawFunction.numInput = function (selection, x, y) {
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

Figure.bboxFunction.rangeInput = Figure.bboxFunction.buttonInput

Figure.drawFunction.rangeInput = function (selection, x, y) { 
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

Figure.bboxFunction.choiceInput = Figure.bboxFunction.buttonInput

Figure.drawFunction.choiceInput = function (selection, x, y) { 
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
