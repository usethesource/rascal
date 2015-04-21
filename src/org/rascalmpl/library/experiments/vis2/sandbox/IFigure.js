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
   

function adjust1(id0, id1) { 
    if (document.getElementById(id0).style.width!="") return;
    var left1 = document.getElementById(id1).getBoundingClientRect().left;
    var right1 = document.getElementById(id1).getBoundingClientRect().right;
    var bottom1 = document.getElementById(id1).getBoundingClientRect().bottom;
    var top1 = document.getElementById(id1).getBoundingClientRect().top;
    d3.select("#"+id0).style("width",right1-left1).style("height",bottom1-top1);
   }
   
 function adjustSvg(id0, id1, lw) { 
    if (document.getElementById(id0).style.width!="") return;
    var d = d3.select("#"+id1);
    var b = d.node().getBBox();
    d3.select("#"+id0).attr("width",b.x+b.width+lw).attr("height",b.y+b.height+lw);
   }
   
 
