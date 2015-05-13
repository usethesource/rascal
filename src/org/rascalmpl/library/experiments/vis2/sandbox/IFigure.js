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
   

function adjust1(id0, id1, lw, hpad, vpad) { 
    var d = d3.select("#"+id0);
    if (d.attr("width")) return;   
    var left1 = document.getElementById(id1).getBoundingClientRect().left;
    var right1 = document.getElementById(id1).getBoundingClientRect().right;
    var bottom1 = document.getElementById(id1).getBoundingClientRect().bottom;
    var top1 = document.getElementById(id1).getBoundingClientRect().top;
    var width1 = document.getElementById(id1).getBoundingClientRect().width;
    var height1 = document.getElementById(id1).getBoundingClientRect().height;
    d3.select("#"+id0).attr("width",width1+lw+hpad).attr("height",height1+lw+vpad);
    d3.select("#"+id0).attr("x",0).attr("y", 0);
   }
   
 function adjustEllipse(id0, id1, lw) { 
    if (document.getElementById(id0).style.width!="") return;
    var left1 = document.getElementById(id1).getBoundingClientRect().left;
    var right1 = document.getElementById(id1).getBoundingClientRect().right;
    var bottom1 = document.getElementById(id1).getBoundingClientRect().bottom;
    var top1 = document.getElementById(id1).getBoundingClientRect().top;
    d3.select("#"+id0).attr("cx", (right1-left1)/2).attr("cy", (bottom1-top1)/2).
    attr("rx", (right1 - left1)/2).attr("ry",(bottom1-top1)/2);
    d3.select("#"+id0+"_").style("width",right1-left1).style("height",bottom1-top1);
   }
   
 function adjustCircle(id0, id1, lw) { 
    var d = d3.select("#"+id0);
    if (d.attr("r")) return;
    var left1 = document.getElementById(id1).getBoundingClientRect().left;
    var right1 = document.getElementById(id1).getBoundingClientRect().right;
    var bottom1 = document.getElementById(id1).getBoundingClientRect().bottom;
    var top1 = document.getElementById(id1).getBoundingClientRect().top;
    d.attr("cx", (left1 + right1)/2+lw).attr("cy", (bottom1+top1)/2+lw).
    attr("r", (right1 - left1)/2);
   }
   
 function adjustSvgStyle(id0, id1, lw, hpad, vpad) { 
     var d = d3.select("#"+id0);
     // if (d.attr("width")) return;   
     var left1 = document.getElementById(id1).getBoundingClientRect().left;
     var right1 = document.getElementById(id1).getBoundingClientRect().right;
     var bottom1 = document.getElementById(id1).getBoundingClientRect().bottom;
     var top1 = document.getElementById(id1).getBoundingClientRect().top;
     var width1 = document.getElementById(id1).getBoundingClientRect().width;
     var height1 = document.getElementById(id1).getBoundingClientRect().height;
    // if (d3.select("#"+id0).style("width")) return;
     var d = d3.select("#"+id1); 
     d3.select("#"+id0).style("width",width1+lw+hpad).style("height",height1+lw+vpad);
     // alert(width1);
   }
   
   function adjustSvgAttr(id0, id1, lw, hpad, vpad) { 
     var d = d3.select("#"+id0);
     // if (d.attr("width")) return;   
     var left1 = document.getElementById(id1).getBoundingClientRect().left;
     var right1 = document.getElementById(id1).getBoundingClientRect().right;
     var bottom1 = document.getElementById(id1).getBoundingClientRect().bottom;
     var top1 = document.getElementById(id1).getBoundingClientRect().top;
     var width1 = document.getElementById(id1).getBoundingClientRect().width;
     var height1 = document.getElementById(id1).getBoundingClientRect().height;
    // if (d3.select("#"+id0).style("width")) return;
     var d = d3.select("#"+id1); 
     d3.select("#"+id0).attr("width",width1+lw+hpad).style("height",height1+lw+vpad);
     // alert(width1);
   }
   
 
