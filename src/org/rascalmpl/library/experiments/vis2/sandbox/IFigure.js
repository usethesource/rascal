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
	return function(x, y) {
		// var offset = svgDocument.getBoundingClientRect();
		var matrix = element.getScreenCTM();
		return {
			x : (matrix.a * x) + (matrix.c * y) + matrix.e, // - offset.left,
			y : (matrix.b * x) + (matrix.d * y) + matrix.f // - offset.top
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

function isSvg(e) {
	switch (e.node().nodeName) {
	   case "FORM":
	   case "INPUT":
	   case "TABLE":	
	   case "BUTTON":
	        return false;
	}
	return true;
}

function ask2Server(site, ev, id, v, parameters) {
	// alert(site);
	askServer(site + "/getValue/" + ev + "/" + id + "/" + v, parameters, timer,
			timeout, function(t) {
				// alert(JSON.stringify(t));
				for ( var d in t) {
					// alert(JSON.stringify(d));
					var e = d3.select("#" + d);
					var style = t[d]["style"];
					if (style != null) {
						// var svg = style["svg"];
						for ( var i in style) {
							// if (i=="visibility") alert(""+d+"
							// "+style[i]);
							if (i == "visibility") {
								// alert(d);
								d3.select("#" + d + "_outer_fo").style(i,style[i])
								;
								d3.select("#" + d + "_fo").style(i,style[i])
								;
								d3.select("#" + d + "_svg").style(i, style[i])
								;
								d3.selectAll("." + d + "_div").style(i,style[i]);
								d3.selectAll("#" + d + " .google").style(i,style[i]);
								d3.select("#" + d + "_frame").style(i,style[i]);
								if (e.attr("clickable")=="yes")
								   e.attr("pointer-events", style[i]=="hidden"?"none":"all")
								;					
							}
							e = e.style(svgStyle(i, isSvg(e)), style[i]);
							
						}
					}
					// alert(d);
					if (t[d]["text"] != null) {
						var cnt = 0;
						for ( var i in t[d]["text"]) {
							var s = t[d]["text"][i];
							if (i == "plain" && s != "")
								e = e.text(s);
							if (i == "html" && s != "")
								e = e.html(s);
							if (s=="") cnt = cnt + 1;
						}
						if (cnt==2) {
							e = e.text("");
							e = e.html("");
						}
					}
					if (t[d]["attr"] != null)
						for ( var i in t[d]["attr"]) {
							if (i != "bigger" && i != "disabled")
								e = e.attr(i, t[d]["attr"][i]);
							if (i == "disabled")
								if (isSvg(e))
								e = e.attr("pointer-events",  t[d]["attr"][i] ? "none" : "all");
								else
								e = e.property(i, t[d]["attr"][i] ? true : false);
						}
					if (t[d]["property"] != null)
						for ( var i in t[d]["property"]) {
							e = e.property(i, t[d]["property"][i]);
						}
					if (t[d]["timer"] != null)
						for ( var i in t[d]["timer"]) {
							var q = doFunction("message", d);
							if (i == "command") {
								if (t[d]["timer"][i] == "start") {
									if (timer[d] != null)
										clearInterval(timer[d]);
									timer[d] = setInterval(q,
											t[d]["timer"]["delay"]);
								}
								if (t[d]["timer"][i] == "finish") {
									// e=e.attr("visibility", "hidden");
									if (timeout[d] != null)
										clearTimeout(timeout[d]);
									if (timer[d] != null)
										clearInterval(timer[d]);
								}
								if (t[d]["timer"][i] == "timeout") {
									if (timeout[d] != null)
										clearTimeout(timeout[d]);
									timeout[d] = setTimeout(q,
											t[d]["timer"]["delay"]);
								}
							}
						}
					var lab = t[d]["prompt"];
					if (lab != null && lab != "") {
						var v = prompt(lab, "");
						if (v == null)
							return;
						var q = eventFunction("prompt", d, v);
						setTimeout(q, 100);
					}
					var a = t[d]["alert"];
					if (a != null && a != "") {
						alert(a);
					}
					if (t[d]["property"] != null)
						for ( var i in t[d]["property"]) {
							var v = t[d]["property"][i];
							// e = e.property(i, v);
							if (i == "value") {
								if (isObject(v))
									for (name in v) {
										// alert(v[name]);
										
										d3.select("#" + d + "_" + name + "_i")
												.property("checked", v[name]);
									}
								else {
									if (v!="")
									d3.select("#" + d + "_" + v + "_i")
											.property("checked", true);
									else d3.selectAll("."+d).property("checked", false);
								     }
							}
						}
				}
				for ( var d in t) {
					for ( var i in t[d]["attr"]) {
						if (i == "bigger") {
							var a = d3.select("#" + d);
							// alert("#"+d);
							var w = parseInt(a.attr("width"));
							var h = parseInt(a.attr("height"));
							var cx1 = -(w / 2);
							var cy1 = -(h / 2);
							var cx2 = (w / 2);
							var cy2 = (h / 2);

							var e = d3.select("#" + d + "_g");

							var s = "scale(" + t[d]["attr"][i] + ")";
							var t1 = "translate(" + cx1 + "," + cy1 + ")";
							var t2 = "translate(" + cx2 + "," + cy2 + ")";
							e = e.attr("transform", t2 + s + t1);
						}
					}
				}
			});
}

function CR(evt, ev, id, v) {
	evt = evt || window.event;
	if (evt.keyCode == 13 && v) {
		ask(ev, id, v, {});
	}
}

function eventFire(el, etype){
	  if (el.fireEvent) {
	    el.fireEvent('on' + etype);
	  } else {
	    var evObj = document.createEvent('Events');
	    evObj.initEvent(etype, true, false);
	    el.dispatchEvent(evObj);
	  }
	}

var site;

function setSite(x) {
	site = x;
}

function radioAsk(ev, id,  v, parameters) {
	// d3.select("#"+id).attr("val", v);
	d3.select("#"+id).property("value", v);
	ask(ev,id, v, parameters);
}

function ask(ev, id, v, parameters) {
	if (v != null) {
		v = v.replace("+", "^plus");
		v = v.replace("/", "^div");
	}
	ask2Server(site, ev, id, v, parameters);
}

function doFunction(ev, id) {
	return function() {
		var v = this.value;
		ask(ev, id, v, {});
	};
}

function doAllFunction(ev, id) {
	return function() {
		var r = {};
		// alert("doall:"+id);
		if (ev=="ok")
		d3.selectAll("#" + id + " .form").call(function(d) {	
			d[0].forEach(function(z, g) {
				// alert(z.value);
				if (z.value==null) z.value="";
				r[z.id] = z.value;
			    }	     	
			);
		});
		var v = this.value;
		// alert(JSON.stringify(r));
		// d3.selectAll("."+id+"_div").style("visibility", "hidden");
		ask(ev, id, v, r);
	};
}

function eventFunction(ev, id, v) {
	return function() {
		ask(ev, id, v, {});
	};
}

function doTimerFunction(ev, id) {
	return function() {
		ask(ev, id, "", {});
		var e = d3.select("#" + id);
		return e.attr("visibility") == "hidden";
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
	if (v == null)
		return 0;
	return v.split(' ').length;
}

function fromInnerToOuterFigure(f, id1, toLw, hpad, vpad) {
	// alert("noot");
	var to = d3.select("#" + f.id);
	var from = d3.select("#" + id1);
	if (from.node().nodeName == "g") {
		from = d3.select("#" + id1 + "_svg");
	}
	var blow = 1.0;
	if (from.node().nodeName == "rect" || from.node().nodeName == "TABLE") {
		blow = Math.sqrt(2.0);
	}
	if (from.empty())
		return;
	var fromLw = parseInt(from.style("stroke-width"));
	var width = 0;
	if (!invalid(from.attr("width")))
		width = parseInt(from.attr("width"));
	else
		width = document.getElementById(id1).getBoundingClientRect().width;
	var height = 0;
	if (!invalid(from.attr("height")))
		height = parseInt(from.attr("height"));
	else
		height = document.getElementById(id1).getBoundingClientRect().height;
	// alert("height:"+height+":"+document.getElementById(id1).getBoundingClientRect().height+":"+id1);
	if (width == 0 || height == 0)
		return;
	toLw = corner(f.n, toLw);
	fromLw = corner(nPoints(from), fromLw);
	if (from.node().nodeName == "TABLE") {
		from.style("max-width", width).style("max-height", height);
	}
	width = width * f.hgrow + hpad + f.x + fromLw + toLw
	height = height * f.vgrow + vpad + f.y + fromLw + toLw;

	switch (to.node().nodeName) {
	case "rect":
		to.attr("width", width).attr("height", height).attr("x", toLw / 2)
				.attr("y", toLw / 2);
		break;
	case "circle":
		var side = (Math.max(width, height) - toLw) * blow + toLw;
		width = side;
		height = side;
		var r = side / 2;
		to.attr("cx", r + toLw / 2).attr("cy", r + toLw / 2).attr("r", r);
		to.attr("width", width).attr("height", height);
		break;
	case "ellipse":
		if ((to.attr("rx") == null) && (to.attr("ry") == null)) {
			width = (width - toLw) * blow + toLw;
			height = (height - toLw) * blow + toLw;
			var rx = width / 2;
			var ry = height / 2;
			to.attr("cx", (width + toLw) / 2).attr("cy", (height + toLw) / 2)
					.attr("rx", rx).attr("ry", ry).attr("width", width).attr(
							"height", height);
		}
		break;
	case "polygon":
		if (to.attr("r") == null) {
			var side = Math.max(width, height) * blow;
			width = side;
			height = side;
			var r = side / 2;
			to.attr("points", translatePoints(f.angle, f.n, r, r + toLw / 2,
					r + toLw / 2).map(function(a) {
				return [ a.x, a.y ].join(",");
			}).join(" "));
			to.attr("width", width).attr("height", height);
			var e = d3.select("#" + f.id + "_circle");
			e.attr("cx", r + toLw / 2).attr("cy", r + toLw / 2).attr("r",
					r + toLw / 2);
			e.attr("width", width).attr("height", height);
		}
		break;
	}
	d3.select("#" + f.id + "_fo_table").attr("w", width - f.x - toLw).attr("h",
			height - f.y - toLw);
	d3.select("#" + f.id + "_fo_table").style("width", width - f.x - toLw)
			.style("height", height - f.y - toLw);
	d3.select("#" + f.id + "_fo").attr("width", width - f.x - toLw).attr(
			"height", height - f.y - toLw);
	d3.select("#" + f.id + "_svg").attr("width", width + toLw).attr("height",
			height + toLw);
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

function adjust1(fromId, f, width, height) {
	if (f.id == "emptyFigure")
		return;
	return _adjust(f.id, fromId, f.hshrink, f.vshrink, f.lw, f.n, f.angle, 0,
			0, width, height);
}

function _adjust(toId, fromId, hshrink, vshrink, toLw, n, angle, x, y, width,
		height) {
	// toId, fromId, hshrink, vshrink, toLw, n, angle, x , y
	var to = d3.select("#" + toId);
	toLw = corner(n, toLw);
	var from = d3.select("#" + fromId);
	var fromLw = parseInt(from.style("stroke-width"));
	fromLw = corner(nPoints(from), fromLw);
	width = width - fromLw - toLw - x;
	height = height - fromLw - toLw - y;
	var w = width * hshrink;
	var h = height * vshrink;
	// alert("adjust1:"+ to.node().nodeName+" "+width+" "+height+" "+w+" "+h);
	var  invalidW = invalid(to.attr("width"))&&invalid(to.attr("w"));
	var  invalidH = invalid(to.attr("height"))&&invalid(to.attr("h"));
	switch (to.node().nodeName) {
	case "FORM":
	case "INPUT":
	case "TABLE":	
	case "BUTTON":
		invalidW = invalidW && invalid(to.style("width")); // nchars
		invalidH = invalidH && invalid(to.style("height")); // nchars
		if (invalidW) to.attr("w", w).style("width", w);	
		if (invalidH) to.attr("h", h).style("height", h);
		break;
	case "rect":
		if (invalidW) to.attr("width", w);
		if (invalidH) to.attr("height", h);
		to.attr("x", toLw / 2).attr("y", toLw / 2);
		break;
	case "circle":
		if (to.attr("r") == null) {
			var side = Math.min(w, h);
			var r = side / 2;
			w = side;
			h = side;
			to.attr("cx", r + toLw / 2).attr("cy", r + toLw / 2).attr("r", r);
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
			var r = (side / 2.0);
			to.attr("points", translatePoints(angle, n, r, (w + toLw) / 2.0,
					(h + toLw) / 2.0).map(function(a) {
				return [ a.x, a.y ].join(",");
			}).join(" "));
			to.attr("width", w).attr("height", h);
			// var e = d3.select("#" + f.id + "_circle");
			// e.attr("cx", (w+toLw)/2).attr("cy", (h+toLw)/2).attr("r", r);
			// e.attr("width", w).attr("height", h);
		}
		break;
	case "ellipse":
		if ((to.attr("rx") == null) && (to.attr("ry") == null)) {
			var rx1 = w / 2;
			var ry1 = h / 2;
			to.attr("cx", (w + toLw) / 2).attr("cy", (h + toLw) / 2).attr("rx",
					rx1).attr("ry", ry1);
			to.attr("width", w).attr("height", h);
		}
		break;
	}
	;
	//d3.select("#" + toId + "_mirror").attr("transform",
	//		"scale(1,-1)translate(0,"+(-h)+")");
	to = d3.select("#" + toId + "_fo_table");
	if (invalidW) {
	     to.attr("w", w - toLw).style("width", w - toLw);
	     d3.select("#" + toId + "_fo").attr("width", w - toLw);
	     d3.select("#" + toId + "_outer_fo").attr("width", w - toLw);
	     d3.select("#" + toId + "_svg").attr("width", w + toLw + x);
	}
	if (invalidH) {
	     to.attr("h", h - toLw).style("height", h - toLw);
	     d3.select("#" + toId + "_fo").attr("height", h - toLw);
		 d3.select("#" + toId + "_outer_fo").attr("height", h - toLw);
		 d3.select("#" + toId + "_svg").attr("height", h + toLw + y);
	}
	to.attr("pointer-events", "none");
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
	var d;
	if ([ "width", "height" ].indexOf(key) >= 0) {
		d = d3.select("#" + f.id + "_svg");
		if (!d.empty()) {
			var r = d.attr(key);
			if (r != null)
				return r;
		}
	}
	d = d3.select("#" + f.id);
	if ([ "TABLE", "BUTTON", "INPUT", "FORM" ].indexOf(d.node().nodeName) >= 0) {
		if (key == "width")
			return d.attr("w");
		if (key == "height")
			return d.attr("h");
		return d.style(key);
	}
	/*
	if (d.node().nodeName == "TABLE") {
		if (key == "width")
			return d.attr("w");
		if (key == "height")
			return d.attr("h");
	}
	*/
	if (d.attr(key) == null) {
		d = d3.select("#" + f.id + "_svg");
		return d.empty() ? null : d.attr(key);
	}
	if (parseInt(d.attr(key)) >= upperBound)
		return null;
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

function invalid(v) {
	return v == null || v == "auto" || parseInt(v) >= upperBound
			|| parseInt(v) <= lowerBound;
}

function adjustTd(to, from) {
	var width = d3.select("#" + from).attr("width");
	var height = d3.select("#" + from).attr("height");
	if (invalid(width) || invalid(height)) {
		width = d3.select("#" + from).style("width");
		height = d3.select("#" + from).style("height");
	}
	if (!invalid(width) && !invalid(height)) {
		d3.select("#" + to).style("width", width).style("height", height);
	}
}

function adjustTable(id1, clients) {
	// alert("adjustTable");
	var aUndefW = clients.filter(undefW);
	var width = d3.select("#" + id1).attr("w");
	// alert("adjustTable:"+aUndefWH.length+" "+width);
	if (invalid(width) && aUndefW.length == 0) {
		width = document.getElementById(id1).getBoundingClientRect().width;
		// alert("OK:"+width);
		d3.select("#" + id1).attr("w", "" + width + "px")
	}
	var aUndefH = clients.filter(undefH);
	var height = d3.select("#" + id1).attr("h");
	if (invalid(height) && aUndefH.length == 0) {
		height = document.getElementById(id1).getBoundingClientRect().height;
		d3.select("#" + id1).attr("h", "" + height + "px")
	}
	// alert("adjustTable:"+width);
	if (invalid(width) || invalid(height))
		return;
	width = parseInt(width);
	height = parseInt(height);
	var x = 0; 
	var y = 0;
	var e = d3.select("#" + id1 + "_outer_fo");
	if (!e.empty()) {
	    x =  parseInt(d3.select("#" + id1 + "_outer_fo").attr("x"));
	    y=   parseInt(d3.select("#" + id1 + "_outer_fo").attr("y"));
	    }
	d3.select("#" + id1 + "_outer_fo").attr("width", "" + width + "px").attr(
			"height", "" + height + "px")
	d3.select("#" + id1 + "_svg").attr("width", "" + (width+x) + "px").attr(
			"height", "" + (height+y) + "px");
}

function adjustTableWH1(id1, clients) {
	var aUndefW = clients.filter(function(i) {
		return i.filter(undefW).length != 0;
	});
	// alert("adjustTableWH1:"+width);
	var width = d3.select("#" + id1).attr("w");
	if (invalid(width)  && aUndefW.length == 0 ) {
		width = document.getElementById(id1).getBoundingClientRect().width;
		d3.select("#" + id1).attr("w", "" + width + "px").attr("width", width);
	}
	
	var aUndefH = clients.filter(function(i) {
		return i.filter(undefH).length != 0;
	});
	var height = d3.select("#" + id1).attr("h");
	if (invalid(height) && aUndefH.length == 0 ) {
		height = document.getElementById(id1).getBoundingClientRect().height;
		d3.select("#" + id1).attr("h","" + height + "px").attr("height", height);
	}
	if (!d3.select("#" + id1+"_ok").empty()) {
		  if (invalid(height)) height = document.getElementById(id1).getBoundingClientRect().height;
		  var bheight= parseInt(d3.select("#" + id1+"_ok").style("height"));
		  height=parseInt(height)+bheight+5;
	      d3.select("#" + id1).attr("h","" + height + "px").attr("height", height);
	      }
	d3.select("#" + id1 + "_outer_fo").attr("width", "" + width + "px").attr(
			"height", "" + height + "px")
	d3.select("#" + id1 + "_svg").attr("width", "" + width + "px").attr(
			"height", "" + height + "px");
	// alert("adjustTableWH1:"+width);
}

function adjustOverlay(clients, id1, lw, hpad, vpad) {
	// alert("adjust");
	var c = d3.select("#" + id1);
	var width = c.attr("width");
	var height = c.attr("height");
	if (invalid(width) || invalid(height)) {
		c = d3.select("#" + id1 + "_svg");
		if (!c.empty()) {
			width = c.attr("width");
			height = c.attr("height");
		} else
			return;
	}
	if (!invalid(width) && !invalid(height)) {
		var aUndefWH = clients.filter(undefWH);
		var w = parseInt(width);
		var h = parseInt(height);
		// alert(aUndefWH.length);
		for (var i = 0; i < aUndefWH.length; i++) {
			adjust1(id1, aUndefWH[i], w, h);
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
		if (width == 0 || height == 0)
			return;
		if (!isEmpty) {
			c = d3.select("#" + id1 + "_svg");
			c.attr("width", width).attr("height", height);
		}
	}
}

function adjustTableW(clients, from, lw, hpad, vpad, hgap, vgap) {
	var c = d3.select("#" + from);
	var width = c.attr("w");
	var height = c.attr("h");
	if (invalid(width) || invalid(height))
		return;
	width = parseInt(width) - hgap * clients.length;
	var aUndefW = clients.filter(undefW);
	var sDefW = sumWidth(clients.filter(defW));
	var nW = aUndefW.length;
	var w = (width - sDefW) / nW;
	var h = parseInt(height);
	for (var i = 0; i < aUndefW.length; i++) {
		adjust1(from, aUndefW[i], w, h);
	}
	var aUndefH = clients.filter(undefH);
	for (var i = 0; i < aUndefH.length; i++) {
		adjust1(from, aUndefH[i], w, h);
	}
}

function adjustTableH(clients, from, lw, hpad, vpad, hgap, vgap) {
	var c = d3.select("#" + from);
	var width = c.attr("w");
	var height = c.attr("h");
	if (invalid(width) || invalid(height))
		return;
	height = parseInt(height) - vgap * clients.length;
	var aUndefH = clients.filter(undefH);
	var aUndefWH = clients.filter(undefWH);
	var sDefH = sumHeight(clients.filter(defH));
	var nH = aUndefH.length;
	var h = (height - sDefH) / nH;
	var w = parseInt(width);
	for (var i = 0; i < aUndefWH.length; i++) {
		adjust1(from, aUndefWH[i], w, h);
	}
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

function adjustTableWH(clients, id1, lw, hpad, vpad, hgap, vgap) {
	var c = d3.select("#" + id1);
	var width = c.attr("width");
	var height = c.attr("height");
	if (invalid(width) || invalid(height))
		return;
	width = parseInt(width) - hgap * clients.length;
	;
	height = parseInt(height) - vgap * clients.length;
	clients1 = transpose(clients);
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
	var w = (width - sDefW) / nW;
	var h = (height - sDefH) / nH;
	for (var i = 0; i < aUndefWH.length; i++) {
		for (var j = 0; j < aUndefWH[i].length; j++) {
			adjust1(id1, aUndefWH[i][j], w, h);
		}
	}
}

function adjustFrame(id0, width, height) {
	d3.select("#" + id0).attr("width", width).attr("height", height);
	d3.select("#" + id0 + "_fo_table").attr("w", width).attr("h", height);
	d3.select("#" + id0 + "_fo").attr("width", width).attr("height", height);
	d3.select("#" + id0 + "_svg").attr("width", width).attr("height", height);
}

function fromOuterToInner(toId, fromId, hshrink, vshrink, toLw, n, angle, x, y) {
	var to = d3.select("#" + toId);
	if (!invalid(to.attr("width")) && !invalid(to.attr("height")))
		return;
	var from = d3.select("#" + fromId);
	var width = from.attr("width");
	var height = from.attr("height");
	if (invalid(from.attr("width")) || invalid(from.attr("height"))) {
		width = document.getElementById(fromId).getBoundingClientRect().width;
		height = document.getElementById(fromId).getBoundingClientRect().height;
	}
	_adjust(toId, fromId, hshrink, vshrink, toLw, n, angle, x, y, width, height);
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
	var s = d3.select("#" + q);
	var convert = makeAbsoluteContext(s.node());
	var x = s.attr("x");
	var y = s.attr("y");
	var w = getWidth("#" + q + "_tooltip_svg");
	var h = getHeight("#" + q + "_tooltip_svg");
	var u = d3.select("#" + q + "_tooltip_outer_fo");
	//if (u.empty())
	//	u = d3.select("#" + q + "_tooltip_fo");
	var z = convert(x, y);
	var x1 = 0;
	var y1 = 0;
	if (!u.empty()) {
		x1 = parseFloat(u.attr("x"));
		y1 = parseFloat(u.attr("y"));
	}
	s.on("mouseenter", function() {
		d3.select("#overlay").attr("width", z.x + w + x1);
		d3.select("#overlay").attr("height", z.y + h + y1);
		if (d3.select("#" + q + "_tooltip_outer_fo").empty()
			  // && d3.select("#" + q + "_tooltip_fo").empty()
			) {
			d3.select("#" + q + "_tooltip_svg").attr("x", z.x).attr("y", z.y);
		}
		d3.select("#" + q + "_tooltip").style("visibility", "visible");
		d3.select("#" + q + "_tooltip_fo").style("visibility", "visible").
		   selectAll(".google").style("visibility", "visible")
		   ;
	});
	s.on("mouseleave", function() {
		d3.select("#" + q + "_tooltip").style("visibility", "hidden");
		d3.select("#" + q + "_tooltip_fo").style("visibility", "hidden").
		   selectAll(".google").style("visibility", "hidden")
		;
	});
	var t = d3.select("#" + q + "_tooltip_svg");
	if (!u.empty()) {
		u.attr("x", z.x + x1).attr("y", z.y + y1);
		t.attr("width", w + z.x + x1).attr("height", h + z.y + y1);
	}
	d3.select("#" + q + "_tooltip").style("visibility", "hidden");
	d3.select("#" + q + "_tooltip_fo").style("visibility", "hidden").
	    selectAll(".google").style("visibility", "hidden")
	    ;
}

function adjust_panel(parent, q) {
	// alert("adjust_panel");
	var s = d3.select("#" + parent);
	// var r = d3.select("#close");
	var convert = makeAbsoluteContext(s.node());
	var x = s.attr("x");
	var y = s.attr("y");
	var w = getWidth("#" + q + "_svg");
	var h = getHeight("#" + q + "_svg");
	var u = d3.select("#" + q + "_outer_fo");
	var z = convert(x, y);
	var x1 = 0;
	var y1 = 0;
	if (!u.empty()) {
		x1 = parseFloat(u.attr("x"));
		y1 = parseFloat(u.attr("y"));
	}	
	var t = d3.select("#" + q + "_svg");
	if (!u.empty()) {
		u.attr("x", z.x + x1).attr("y", z.y + y1);
		t.attr("width", w + z.x + x1).attr("height", h + z.y + y1);
	}
	if (s.on("click")!=null) return;
	s.on("click", function() {
		d3.select("#overlay").attr("width", z.x + w + x1);
		d3.select("#overlay").attr("height", z.y + h + y1);
		if (d3.select("#" + q + "_outer_fo").empty()) {
			d3.select("#" + q + "_svg").attr("x", z.x).attr("y", z.y);
		}
		if (d3.select("#" + q + "").style("visibility") == "hidden") {
			d3.select("#" + q + "").style("visibility", "visible");
			d3.select("#" + q + "_fo").style("visibility", "visible");
			d3.select("#" + q + "_frame").style("visibility", "visible");
		} else {
			d3.select("#" + q + "").style("visibility", "hidden");
			d3.select("#" + q + "_fo").style("visibility", "hidden");
			d3.select("#" + q + "_frame").style("visibility", "hidden");
		}
	});
	d3.select("#" + q + "").style("visibility", "hidden");
	d3.select("#" + q + "_fo").style("visibility", "hidden").attr("pointer-events", "all");
}
