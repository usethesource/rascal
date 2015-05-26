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


function askServer(path, parameters, callback) {
	ajax.post(path, parameters, function(responseText){
		try { 
            var res = JSON.parse(responseText);      
            callback(res);
        } catch (e) {
            alert(e.message + ", on figure " + responseText);
        }
	});
   }
   
 rxL= function(rx, ry) {return rx * Math.sqrt(rx*rx+ry*ry)/ry;};
 ryL= function(rx, ry) {return ry * Math.sqrt(rx*rx+ry*ry)/rx;};
 
 function adjust0(id0, id1, lw, hpad, vpad) { 
    var d = d3.select("#"+id0);
    var lw0 = parseInt(d3.select("#"+id1).style("stroke-width"));
    var width = document.getElementById(id1).getBoundingClientRect().width+lw+hpad;
    var height = document.getElementById(id1).getBoundingClientRect().height+lw+vpad;  
    // alert(d.node().nodeName);
    var c = d3.select("#"+id1);
    if (c.node().nodeName == "ellipse" || c.node().nodeName == "circle"
                                       || c.node().nodeName == "path")
        {width += lw0; height += lw0;}
    switch (d.node().nodeName) {
        case "rect": 
                   d.attr("width",width).attr("height",height).
                   attr("x",0).attr("y", 0); 
                   break;
        case "circle":  
                    var side =  Math.max(width, height);           
                    var r = side/2;
                    d.attr("cx", r+lw/2).attr("cy", r+lw/2)
                     .attr("r", r); 
                    width = 2*r+lw;
                    height = 2*r+lw;               
                    break;
        case "ellipse":  
                    var rx = rxL(width/2, height/2);
                    var ry = ryL(width/2, height/2);
                    d.attr("cx", rx+lw/2).attr("cy", ry+lw/2)
                     .attr("rx", rx).attr("ry", ry); 
                     width = 2*rx + lw;
                     height= 2*ry + lw;               
                    break; 
        };
    d3.select("#"+id0+"_fo_table").style("width",width).style("height",height);
    d3.select("#"+id0+"_fo").attr("width",width).attr("height",height);
    d3.select("#"+id0+"_svg").attr("width",width).attr("height",height);
   }
