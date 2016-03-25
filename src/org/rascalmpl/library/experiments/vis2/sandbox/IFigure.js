var ajax = {};
var timer = {};
var timeout = {};
alertSize();
// window.alert( 'Height = ' + screenHeight );
ajax.x = function() {
	if (typeof XMLHttpRequest !== 'undefined') {
		return new XMLHttpRequest();
	}
	var versions = [ "MSXML2.XmlHttp.5.0", "MSXML2.XmlHttp.4.0",
			"MSXML2.XmlHttp.3.0", "MSXML2.XmlHttp.2.0", "Microsoft.XmlHttp" ];

	var xhr;
	for (var i = 0; i < versions.length; i++) {
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
	for ( var key in data) {
		query.push(encodeURIComponent(key) + '='
				+ encodeURIComponent(data[key]));
	}
	ajax.send(url + '?' + query.join('&'), callback, 'GET', null, sync)
};

ajax.post = function(url, data, callback, sync) {
	var query = [];
	for ( var key in data) {
		query.push(encodeURIComponent(key) + '='
				+ encodeURIComponent(data[key]));
	}
	ajax.send(url, callback, 'POST', query.join('&'), sync)
};

function makeAbsoluteContext(element) {
	  return function(x,y) {
	    // var offset = svgDocument.getBoundingClientRect();
	    var matrix = element.getScreenCTM();
	    return {
	      x: (matrix.a * x) + (matrix.c * y) + matrix.e, //- offset.left,
	      y: (matrix.b * x) + (matrix.d * y) + matrix.f // - offset.top
	    };
	  };
	}

function askServer(path, parameters, timer, timeout, callback) {
	ajax.post(path, parameters, function(responseText) {
		try {
			// alert(responseText);
			var res = JSON.parse(responseText);
			callback(res);
		} catch (e) {
			for (d in timer) {
				clearInterval(timer[d]);
			}
			if (d in timeout) {
				clearTimeout(timeout[d]);
			}
			alert(e.message + ", on figure " + responseText);
		}
	});
}

function ask2Server(site, ev, id, v) {
	    // alert(site);
	    askServer(site+"/getValue/"+ev+"/"+id+"/"+v,
		{}, timer, timeout,
		          function(t) {   
                      // alert(JSON.stringify(t));         
                      for (var d in t) {
                        // alert(JSON.stringify(d));
                        var e = d3.select("#"+d); 
                        var style = t[d]["style"];
                        if (style!=null) {
                        var svg = style["svg"];
                         for (var i in style) {  
                              // if (i=="visibility") alert(""+d+" "+style[i]); 
                              if (i=="visibility") {
                                 d3.select("#"+d+"_outer_fo").style(i, style[i]);
                                 d3.select("#"+d+"_svg").style(i, style[i]);                           
                              }              
                              e=e.style(svgStyle(i, svg), style[i]);
                              }
                         }
                         // alert(d);
                         if (t[d]["text"]!=null) 
                         for (var i in t[d]["text"]) {
                              if (i=="text") e=e.text(t[d]["text"][i]);
                              if (i=="html") e=e.html(t[d]["text"][i]);
                              }
                         if (t[d]["attr"]!=null)
                         for (var i in t[d]["attr"]) {
                              if (i!="bigger" && i!="disabled")
                              e=e.attr(i, t[d]["attr"][i]);
                              if (i=="disabled") e=e.attr(i, t[d]["attr"][i]?true:null);
                              }
                         if (t[d]["property"]!=null)
                         for (var i in t[d]["property"]) {
                              e=e.property(i, t[d]["property"][i]);
                              }
                         if (t[d]["timer"]!=null)
                         for (var i in t[d]["timer"]) {
                              var q =  doFunction("message", d);
                              if (i=="command") { 
                                    if (t[d]["timer"][i]=="start") {
                                          if (timer[d]!=null) clearInterval(timer[d]);                                
                                          timer[d] = setInterval(q, t[d]["timer"]["delay"]); 
                                          }
                                    if (t[d]["timer"][i]=="finish") {
                                        // e=e.attr("visibility", "hidden"); 
                                        if (timeout[d]!=null) clearTimeout(timeout[d]);
                                        if (timer[d]!=null) clearInterval(timer[d]); 
                                    }
                                    if (t[d]["timer"][i]=="timeout") {
                                          if (timeout[d]!=null) clearTimeout(timeout[d]);                             
                                          timeout[d]= setTimeout(q, t[d]["timer"]["delay"]); 
                                          }
                              }
                         }
                         var lab = t[d]["prompt"];
                         if (lab!=null && lab!="") {
                                   var v = prompt(lab, "");
                                   if (v==null) return;
                                   var q =   eventFunction("prompt", d, v);
                                   setTimeout(q, 100);
                            }
                         var a = t[d]["alert"];
                         if (a!=null && a!="") {
                                   alert(a);                   
                            }
                         if (t[d]["property"]!=null)
                         for (var i in t[d]["property"]) {
                              var v = t[d]["property"][i];
                              e=e.property(i, v);           
                              if (i=="value") {
                                 if (isObject(v)) 
                                   for (name in v) {
                                      // alert(v[name]);
                                      d3.select("#"+d+"_"+name+"_i").property("checked", v[name]); 
                                   }
                              else
                                 d3.select("#"+d+"_"+v+"_i").property("checked", true);
                               }
                              }   
                         }
                         for (var d in t) {      
                               for (var i in t[d]["attr"]) {
                                    if (i=="bigger") {
                                       var a = d3.select("#"+d);
                                       // alert("#"+d);
                                       var w = parseInt(a.attr("width"));
                                       var h = parseInt(a.attr("height"));
                                       var cx1 =  -(w/2);
                                       var cy1 =  -(h/2);
                                       var cx2 =  (w/2);
                                       var cy2 =  (h/2);
       
                                       var e = d3.select("#"+d+"_g");
                                       
                                       var s = "scale("+t[d]["attr"][i]+")";
                                       var t1= "translate("+cx1+","+cy1+")";
                                       var t2= "translate("+cx2+","+cy2+")";
                                       e=e.attr("transform", t2+s+t1);
                                    }
                              }
                           }
                      }
		);
      }

    function CR(evt, ev, id, v ) {
       evt = evt || window.event;
       if (evt.keyCode == 13 && v) {
            ask(ev, id , v);
       }
    }
    
var site;

function setSite(x) {site = x;}
    
function ask(ev, id, v) {
        if (v!=null) {
           v=v.replace("+","^plus");
           v=v.replace("/","^div");
           } 
           ask2Server(site, ev, id, v);
      }
 
function doFunction(ev, id) { 
    return function() {  
    var v = this.value;
     ask(ev, id, v);
   };
 }

function eventFunction(ev, id, v) { 
    return function() {  
     ask(ev, id, v);
   };
 }

function doTimerFunction(ev, id) { 
    return function() {  
     ask(ev, id, "");
     var e = d3.select("#"+id); 
     return  e.attr("visibility")=="hidden";
   };
 }

function alertSize() {
	if (typeof (window.innerWidth) == 'number') {
		// Non-IE
		screenWidth = window.innerWidth - 50;
		screenHeight = window.innerHeight - 50;
	} else if (document.documentElement
			&& (document.documentElement.clientWidth || document.documentElement.clientHeight)) {
		// IE 6+ in 'standards compliant mode'
		screenWidth = document.documentElement.clientWidth;
		screenHeight = document.documentElement.clientHeight;
	} else if (document.body
			&& (document.body.clientWidth || document.body.clientHeight)) {
		// IE 4 compatible
		screenWidth = document.body.clientWidth;
		screenHeight = document.body.clientHeight;
	}

}

rxL = function(rx, ry) {
	return rx /* * Math.sqrt(rx*rx+ry*ry)/ry */;
};
ryL = function(rx, ry) {
	return ry /* * Math.sqrt(rx*rx+ry*ry)/rx */;
};

function corner(n, lineWidth) {
	if (n == 0)
		return lineWidth;
	// var angle = Math.PI - 2 * Math.PI / n;
	angle = Math.PI / n;
	var lw = lineWidth < 0 ? 0 : lineWidth;
	// alert(lw/Math.sin(angle))
	// return lw;
	return (lw / Math.cos(angle));
}

function svgStyle(s, svg) {
	switch (s) {
	case "fillColor":
		return svg ? "fill" : "background";
	case "lineColor":
		return svg ? "stroke" : "border-color";
	case "lineWidth":
		return svg ? "stroke-width" : "border-width";
	case "fillOpacity":
		return svg ? "fill-opacity" : "opacity";
	case "lineOpacity":
		return svg ? "stroke-opacity" : "opacity";
	}
	return s;
}

function nPoints(el) {
	 var v = el.attr("points");
	 if (v ==null) return 0;
	 return v.split(' ').length;
}

function fromInnerToOuterFigure(f, id1, toLw, hpad, vpad) {
	var to = d3.select("#" + f.id);
    var from = d3.select("#"+id1);
    if (from.node().nodeName=="g") {
    	from = d3.select("#"+id1+"_svg");
    }
    if (from.empty()) return;
	var fromLw = parseInt(from.style("stroke-width"));
	var width = document.getElementById(id1).getBoundingClientRect().width;
	var height = document.getElementById(id1).getBoundingClientRect().height;
	if (!invalid(from.attr("width")))
		width = parseInt(from.attr("width"));
	if (!invalid(from.attr("height")))
		height = parseInt(from.attr("height"));
	if (width == 0 || height == 0)
		return;
	toLw = corner(f.n, toLw);
	var border =  corner(nPoints(from), fromLw);
	width = width *   f.hgrow +  hpad + f.x+ border+toLw
	height = height * f.vgrow +  vpad + f.y+ border+toLw;
	switch (to.node().nodeName) {
	case "rect":
		to.attr("width", width).attr("height", height).attr("x", toLw/2).attr("y", toLw/2);
		break;
	case "circle":
		var side = Math.max(width, height);
		var r = side / 2;
		to.attr("cx", r + toLw / 2).attr("cy", r + toLw / 2).attr("r", r);
		width = side;
		height = side;
		to.attr("width", width).attr("height", height);
		break;
	case "ellipse":
		if ((to.attr("rx") == null) && (to.attr("ry") == null)) {
			var rx = width  / 2;
			var ry = height  / 2;
			to.attr("cx", (width+toLw) / 2).attr("cy", (height+toLw) / 2).attr("rx", rx).attr("ry",
					ry).attr("width", width).attr("height", height);
		}
		break;
	case "polygon":
        if (to.attr("r") == null) {
                var side = Math.max(width, height);
                width = side;
                height = side;
                var r = side / 2.0;
                to.attr("points", translatePoints(f.angle, f.n, r, r + toLw/2.0, r + toLw/2.0).map(
                                function(a) {
                                        return [ a.x, a.y ].join(",");
                                }).join(" "));
                to.attr("width", width).attr("height", height)
                ;
                var e = d3.select("#" + f.id + "_circle");
                e.attr("cx", r + toLw/2).attr("cy", r + toLw/2.0).attr("r", r+toLw/2.0);
                e.attr("width", width).attr("height", height);
        }
        break;
	}
	d3.select("#" + f.id + "_fo_table").style("width", width-f.x-toLw).style("height",
			height-f.y-toLw);
	d3.select("#" + f.id + "_fo").attr("width", width-f.x-toLw).attr("height", height-f.y-toLw);
	d3.select("#" + f.id + "_svg").attr("width", width+toLw).attr("height", height+toLw);
}

function translatePoints(angle, n, r, x, y) {
	var q = new Array();
	// alert(f.angle);
	var p = angle / 360.0 * 2 * Math.PI;
	// alert(p);
	var angl = 2 * Math.PI / n;
	for (var i = 0; i < n; i++) {
		q.push({
			"x" : x + r * Math.cos(p + i * angl),
			"y" : y + r * Math.sin(p + i * angl)
		});
	}
	return q;
}

function adjust1(fromId, f, width, height, hpad, vpad) {
	if (f.id == "emptyFigure")
		return;
	var to = d3.select("#" + f.id);
	var toLw = f.lw;
	// alert("adjust1:"+ to.node().nodeName+" "+width+" "+height);
	if (to.node().nodeName == "TABLE") {
		if (invalid(to.attr("width"))) {
			to.style("width", parseInt(width));
			to.attr("width", parseInt(width));
		}
		if (invalid(to.attr("height"))) {
			to.style("height", parseInt(height));
			to.attr("height", parseInt(height));
		}
		return;
	}
	if (!invalid(to.attr("width")))
		width = parseInt(to.attr("width"));
	if (!invalid(to.attr("height")))
		height = parseInt(to.attr("height"));
	var from = d3.select("#" + fromId);
	var fromLw = parseInt(from.style("stroke-width"));
	width = width - fromLw - toLw;
	height = height - fromLw - toLw;
	var w = width * f.hshrink;
	var h = height * f.vshrink; 
	switch (to.node().nodeName) {
	case "rect":
		to.attr("width", w).attr("height", h).attr("x", 0).attr("y", 0);
		break;
	case "circle":
		if (to.attr("r") == null) {
			var side = Math.min(w, h);
			var r = side/2;
			w = side;
			h = side;
			to.attr("cx", r+toLw/2).attr("cy", r+toLw/2).attr("r", r);
			width = side;
			height = side;
			to.attr("width", w).attr("height", h);		
		}

		break;
	case "polygon":
		if (to.attr("r") == null && to.attr("points") == null) {
			var side = Math.min(w, h);
			w = side;
			h = side;
			var r = (side/2.0);
			to.attr("points", translatePoints(f.angle, f.n, r, (w+toLw)/2.0, (h+toLw)/2.0).map(
					function(a) {
						return [ a.x, a.y ].join(",");
					}).join(" "));
			to.attr("width", w).attr("height", h);
			//var e = d3.select("#" + f.id + "_circle");
			//e.attr("cx", (w+toLw)/2).attr("cy", (h+toLw)/2).attr("r", r);
			//e.attr("width", w).attr("height", h);
		}
		break;
	case "ellipse":
		if ((to.attr("rx") == null) && (to.attr("ry") == null)) {
			var rx1 = (w - toLw) / 2;
			var ry1 = (h - toLw) / 2;
			to.attr("cx", w / 2).attr("cy", h / 2).attr("rx", rx1).attr("ry",
					ry1);
			to.attr("width", w).attr("height", h);
		}
		break;
	}
	;
	// alert(width);
	// d3.select("#"+f.id").style("width",width).style("height",height);
	d3.select("#" + f.id + "_fo_table").style("width", w -toLw).style(
			"height",  h - toLw );
	d3.select("#" + f.id + "_fo").attr("width", w -toLw).attr("height",
			h -toLw);
	d3.select("#" + f.id + "_svg").attr("width", w+toLw).attr("height",
			h +toLw);
	d3.select("#" + fromId + "_" + f.id).style("width", width+toLw).style(
			"height", height+toLw);
	d3.select("#" + fromId + "_" + f.id).attr("pointer-events", "none");
	d3.select("#" + f.id + "_fo_table").attr("pointer-events", "none");
}

function figShrink(id, hshrink, vshrink, lw, n, angle) {
	// alert("fig");
	return {
		id : id,
		hshrink : hshrink,
		vshrink : vshrink,
		lw : lw,
		n : n,
		angle : angle
	};
}

function figGrow(id, hgrow, vgrow, lw, n, angle, x, y) {
	return {
		id : id,
		hgrow : hgrow,
		vgrow : vgrow,
		lw : lw,
		n : n,
		angle : angle,
		x : x,
		y : y
	};
}

function getVal(f, key) {
	// alert(f.id);
	if (f.id == "emptyFigure")
		return 0;
	var d = d3.select("#" + f.id);
	if (["BUTTON","INPUT", "FORM"].indexOf(d.node().nodeName)>=0) {	   
	   return d.style(key);
	 }
	if (d.attr(key)==null) return null;
	if (parseInt(d.attr(key))>=upperBound) return null;
	return d.attr(key);
}

function undefWH(v) {
	return ((getVal(v, "width") == null) || (getVal(v, "height") == null));
}

function sumWidth(vs) {
	var r = 0;
	for (var i = 0; i < vs.length; i++) {
		r = r + parseInt(getVal(vs[i], "width"));
	}
	return r;
}

function sumHeight(vs) {
	var r = 0;
	for (var i = 0; i < vs.length; i++)
		r = r + parseInt(getVal(vs[i], "height"));
	return r;
}

function undefW(v) {
	return (getVal(v, "width") == null);
}

function undefH(v) {
	return (getVal(v, "height") == null);
}

function defW(v) {
	return (getVal(v, "width") != null);
}

function defH(v) {
	return (getVal(v, "height") != null);
}

function adjustText(id1) {
	var width = document.getElementById(id1).getBoundingClientRect().width;
	var height = document.getElementById(id1).getBoundingClientRect().height;
	d3.select("#" + id1).attr("width", "" + width + "px").attr("height",
			"" + height + "px").attr("x", width / 2).attr("y", height / 2)
			.attr("dy", ".3em");
}

function invalid(v) {return v==null || parseInt(v)>=upperBound;}

function adjustTable(id1, clients) {
	// alert("adjustTable");
	var aUndefWH = clients.filter(undefWH);
	var width = d3.select("#" + id1).attr("width");
	var height = d3.select("#" + id1).attr("height");
	if (invalid(width) || invalid(height)) {
        width = d3.select("#" + id1).style("width");
        height = d3.select("#" + id1).style("height");
        }
	if ((invalid(height) || invalid(width)) && aUndefWH.length == 0) {
		width = document.getElementById(id1).getBoundingClientRect().width;
		height = document.getElementById(id1).getBoundingClientRect().height;
		d3.select("#" + id1).attr("width", "" + width + "px").attr("height",
				"" + height + "px");
	}
    width = parseInt(width);
    height = parseInt(height);
	d3.select("#" + id1 + "_outer_fo").attr("width", "" + width + "px").attr(
			"height", "" + height + "px")
	d3.select("#" + id1 + "_svg").attr("width", "" + width + "px").attr(
			"height", "" + height + "px");
}

function adjustTableWH1(id1, clients) {
	var width = d3.select("#" + id1).attr("width");
	var height = d3.select("#" + id1).attr("height");
	if (invalid(width) || invalid(height)) {
        width = d3.select("#" + id1).style("width");
        height = d3.select("#" + id1).style("height");
        }
	var aUndefWH = clients.filter(function(i) {
		return i.filter(undefWH).length != 0;
	});
	if ((invalid(height) || invalid(width)) && aUndefWH.length == 0) {
		width = document.getElementById(id1).getBoundingClientRect().width;
		height = document.getElementById(id1).getBoundingClientRect().height;
		d3.select("#" + id1).attr("width", "" + width + "px").attr("height",
				"" + height + "px");
	    } 
     d3.select("#" + id1 + "_outer_fo").attr("width", "" + width + "px").attr(
				"height", "" + height + "px")
     d3.select("#" + id1 + "_svg").attr("width", "" + width + "px").attr(
				"height", "" + height + "px");
}

function adjustOverlay(clients, id1, lw, hpad, vpad) {
	// alert("adjust");
	var c = d3.select("#" + id1);
	var width = c.attr("width");
	var height = c.attr("height");
	if (invalid(width) || invalid(height)) {
		c = d3.select("#" + id1+"_svg");
		if (!c.empty()) {
			width = c.attr("width");
			height = c.attr("height");
		    } else return;
	    }
	if (!invalid(width) && !invalid(height)) {
	  var aUndefWH = clients.filter(undefWH);
	  var w = parseInt(width);
	  var h = parseInt(height);
	// alert(aUndefWH.length);

	  for (var i = 0; i < aUndefWH.length; i++) {
		adjust1(id1, aUndefWH[i], w, h, hpad, vpad);
	  }
	} else {
	  width = 0;
	  height = 0;
	  var isEmpty = false;
	  for (var i = 0; i < clients.length; i++) {
		var d = d3.select("#" + clients[i].id);
		var e = d3.select("#" + clients[i].id + "_svg");
		if (!e.empty()) {
			w = parseInt(d.attr("width")) + parseInt(e.attr("x"));
			h = parseInt(d.attr("height")) + parseInt(e.attr("y"));
			if (w > width)
				width = w;
			if (h > height)
				height = h;
		} else
			isEmpty = true;
	    }
	   if (width == 0 || height == 0) return;
	   if (!isEmpty) {
		// c.attr("width", width).attr("height", height);
		c = d3.select("#" + id1 + "_svg");
		c.attr("width", width).attr("height", height);
	   }
	}
}

function adjustTableW(clients, id1, lw, hpad, vpad) {
	var c = d3.select("#" + id1);
	var width = c.attr("width");
	var height = c.attr("height");
	// alert(id1);
	if (invalid(width) || invalid(height))
		return;

	var aUndefW = clients.filter(undefW);
	var aUndefWH = clients.filter(undefWH);
	var sDefW = sumWidth(clients.filter(defW));
	var nW = aUndefW.length;
	var w = (parseInt(width) - sDefW) / nW;
	var h = parseInt(height);
	// alert("adjustTableW:"+id1+" "+aUndefWH);
	// lw =10;
	for (var i = 0; i < aUndefWH.length; i++) {
		adjust1(id1, aUndefWH[i], w, h, hpad, vpad);
	}
}

function adjustTableH(clients, id1, lw, hpad, vpad) {
	var c = d3.select("#" + id1);
	var width = c.attr("width");
	var height = c.attr("height");
	if (invalid(width) || invalid(height))
		return;
	var aUndefH = clients.filter(undefH);
	var aUndefWH = clients.filter(undefWH);
	var sDefH = sumHeight(clients.filter(defH));
	var nH = aUndefH.length;
	var h = (parseInt(height) - sDefH) / nH;
	var w = parseInt(width);
	for (var i = 0; i < aUndefWH.length; i++)
		adjust1(id1, aUndefWH[i], w, h, hpad, vpad);
}

function getMaxOfArray(numArray) {
	return Math.max.apply(null, numArray);
}

function getMinOfArray(numArray) {
	return Math.min.apply(null, numArray);
}

function transpose(original) {
	var ncols = 0;
	for (var i = 0; i < original.length; ++i) {
		if (original[i].length > ncols)
			ncols = original[i].length;
	}
	var copy = new Array(ncols);
	for (var i = 0; i < ncols; ++i) {
		copy[i] = new Array();
	}
	for (var i = 0; i < original.length; ++i) {
		for (var j = 0; j < original[i].length; ++j) {
			copy[j].push(original[i][j]);
		}
	}
	return copy;
}

function adjustTableWH(clients, id1, lw, hpad, vpad) {
	var c = d3.select("#" + id1);
	var width = c.attr("width");
	var height = c.attr("height");
	clients1 = transpose(clients);
	if (invalid(width) || invalid(height))
		return;
	var aUndefW = clients.map(function(i) {
		return i.filter(undefW);
	});

	var aUndefH = clients1.map(function(i) {
		return i.filter(undefH);
	});
	// alert("adjustWHJ");
	var aUndefWH = clients.map(function(i) {
		return i.filter(undefWH);
	});

	var sDefW = aUndefW.length < clients.length ? Math.max(parseInt(width)
			/ clients1.length, getMaxOfArray(clients.map(function(i) {
		return sumWidth(i.filter(defW));
	}))) : 0;
	// alert(sDefW);
	var sDefH = aUndefW.length < clients.length ? Math.max(parseInt(height)
			/ clients.length, getMaxOfArray(clients1.map(function(i) {
		return sumHeight(i.filter(defH));
	}))) : 0;
	// alert(sDefH);
	var nW = getMinOfArray(aUndefW.map(function(i) {
		return i.length;
	}));
	var nH = getMinOfArray(aUndefH.map(function(i) {
		return i.length;
	}));
	var w = (parseInt(width) - sDefW) / nW;
	var h = (parseInt(height) - sDefH) / nH;
	// alert("adjustTableW:"+id1+" "+aUndefWH);
	// alert(w);
	// alert(h);
	for (var i = 0; i < aUndefWH.length; i++) {
		for (var j = 0; j < aUndefWH[i].length; j++) {
			adjust1(id1, aUndefWH[i][j], w, h, hpad, vpad);
		}
	}
}

function adjustFrame(id0, width, height) {
	d3.select("#" + id0).attr("width", width).attr("height", height);
	d3.select("#" + id0 + "_fo_table").style("width", width).style("height",
			height);
	d3.select("#" + id0 + "_fo").attr("width", width).attr("height", height);
	d3.select("#" + id0 + "_svg").attr("width", width).attr("height", height);
}

function fromOuterToInner(toId, fromId, hshrink, vshrink, toLw, n, angle, x , y) {
	var to = d3.select("#" + toId);	
	if (!invalid(to.attr("width")) && !invalid(to.attr("height")))
		return;
	var from = d3.select("#" + fromId);
	var width = document.getElementById(fromId).getBoundingClientRect().width;
	if (!invalid(from.attr("width")))
		width = from.attr("width");
	var height = document.getElementById(fromId).getBoundingClientRect().height;
	if (!invalid(from.attr("height")))
		height = from.attr("height");
	var fromLw = parseInt(from.style("stroke-width"));
	fromLw =  corner(nPoints(from), fromLw);
	if (invalid(width) || invalid(height))
		return;
	toLw = corner(n, toLw);
	width = width - fromLw - toLw -x;
	height = height - fromLw - toLw -x;
	var w = width * hshrink;
	var h = height * vshrink; 
	switch (to.node().nodeName) {
	case "TABLE":
		 var tw = document.getElementById(toId).getBoundingClientRect().width;
		 if (tw==null || tw< 20) {
		     to.style("width", w);
		     to.attr("width", w);    
	         }
		 var th = document.getElementById(toId).getBoundingClientRect().height;
		 if (th==null || th< 20) {
		    to.style("height", h);
		    to.attr("height", h);
	        }
		 break;
	case "rect":
		to.attr("width", w).attr("height", h).attr("x", toLw/2).attr("y", toLw/2);
		break;
	case "circle":
		if (to.attr("r") == null) {
			var side = Math.min(w, h);
			w = side;
			h = side;
			var r = side / 2;
			to.attr("cx", (w+toLw) / 2).attr("cy", (h+toLw) / 2).attr("r", r).
			attr("width", w).attr("height", h);
		}
		break;
	case "polygon":
		if (to.attr("r") == null && to.attr("points") == null) {
			var side = Math.min(w, h);
			w = side;
			h = side;
			var r = (side/2.0);
			to.attr("points", translatePoints(angle, n, r, (w+toLw)/2.0, (h+toLw)/2.0).map(
					function(a) {
						return [ a.x, a.y ].join(",");
					}).join(" "));
			to.attr("width", w).attr("height", h);
			var e = d3.select("#" + toId + "_circle");
			e.attr("cx", (w+toLw)/2).attr("cy", (h+toLw)/2).attr("r", r);
			e.attr("width", w).attr("height", h);
		}
		break;
	case "ellipse":
		if ((to.attr("rx") == null) && (to.attr("ry") == null)) {
			var rx = w  / 2;
			var ry = h  / 2;
			to.attr("cx", (w+toLw) / 2).attr("cy", (h+toLw) / 2).attr("rx", rx).attr("ry",
					ry).attr("width", w).attr("height", h);
		}
		break;
	}
	;
	d3.select("#" + toId + "_fo_table").style("width", w-toLw).style(
			"height", h-toLw);
	d3.select("#" + toId + "_fo").attr("width", w-toLw).attr("height",
			h-toLw);
	d3.select("#" + toId + "_svg").attr("width", w+toLw+x).attr("height",
			h+toLw+y);
    }
	    

function isObject(item) {
	return (typeof item === "object" && !Array.isArray(item) && item !== null);
}

function nl2br(str, is_xhtml) {
	var breakTag = (is_xhtml || typeof is_xhtml === 'undefined') ? '<br />'
			: '<br>';
	return (str + '').replace(/([^>\r\n]?)(\r\n|\n\r|\r|\n)/g, '$1' + breakTag
			+ '$2');
}

function diagClose(e, id) {
	e.preventDefault();
	document.querySelector('dialog').close();
};

function getWidth(q) {
	var r = d3.select(q).attr("width");
	// if (r==null|| r=="auto") r = d3.select(q).style("width");
	return parseInt(r);
}

function getHeight(q) {
	var r = d3.select(q).attr("height");
	// if (r==null || r =="auto") r = d3.select(q).style("height");
	return parseInt(r);
}

function adjust_tooltip(q) {
	var s = d3.select("#"+q);
	var convert = makeAbsoluteContext(s.node());   
	var x = s.attr("x");
    var y = s.attr("y");
    var w = getWidth("#"+q+"_tooltip_svg");
    var h = getHeight("#"+q+"_tooltip_svg");
    var u = d3.select("#"+q+"_tooltip_outer_fo");
    var z = convert(x, y);
    var x1 = 0;
    var y1 = 0;
    if (!u.empty()) {
    	x1 = parseFloat(u.attr("x"));
    	y1 = parseFloat(u.attr("y")); 
    }
	s.on("mouseenter", function(){
		    d3.select("#overlay").attr("width", z.x+w+x1);
	        d3.select("#overlay").attr("height", z.y+h+y1);
		    if (d3.select("#"+q+"_tooltip_outer_fo").empty()) {
		        d3.select("#"+q+"_tooltip_svg").attr("x", z.x).attr("y", z.y);
	            }
		    d3.select("#"+q+"_tooltip").style("visibility", "visible");
		    d3.select("#"+q+"_tooltip_fo").style("visibility", "visible");
		    });
	s.on("mouseleave", function(){
		    d3.select("#"+q+"_tooltip").style("visibility", "hidden");
		    d3.select("#"+q+"_tooltip_fo").style("visibility", "hidden");
		    });
    var t = d3.select("#"+q+"_tooltip_svg");
    //t.attr("x", 0).attr("y", 0);
    // t.attr("width", 1600).attr("height", 1600);
    if (!u.empty()) {
    	u.attr("x", z.x+x1).attr("y", z.y+y1);
        t.attr("width", w + z.x+x1).attr("height", h + z.y+y1); 
        }
    d3.select("#"+q+"_tooltip").style("visibility", "hidden");
	d3.select("#"+q+"_tooltip_fo").style("visibility", "hidden");
}

