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
 
 function svgStyle(s, svg) {
     switch (s) {
          case "fillColor": return svg?"fill":"background";
          case "lineColor": return svg?"stroke":"border-color";
          case "lineWidth": return svg?"stroke-width":"border-width";
          }
     return s;
     }
 
 function adjust0(id0, id1, lw, hpad, vpad) { 
    var d = d3.select("#"+id0);
    var lw0 = parseInt(d3.select("#"+id1).style("stroke-width"));
    var width = document.getElementById(id1).getBoundingClientRect().width+lw+hpad;
    var height = document.getElementById(id1).getBoundingClientRect().height+lw+vpad;
    if  (d.attr("width")!=null) width = d.attr("width");
    if  (d.attr("height")!=null) height = d.attr("height");
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
   
   function adjust1(id0, width, height, lw, hpad, vpad) { 
    var d = d3.select("#"+id0);
    // alert("adjust1: "+id0+" "+d.node().nodeName+" "+width+" "+height);
    if (d.node().nodeName=="TABLE") {
          if (d.attr("width")==null) {
             d.style("width", width);
             d.attr("width", width);
             }
          if (d.attr("height")==null) { 
              d.style("height", height);
              d.attr("height", height);
              }
          return;
          }
    if  (d.attr("width")!=null) width = d.attr("width");
    if  (d.attr("height")!=null) height = d.attr("height");
    switch (d.node().nodeName) {
        case "rect": 
                   d.attr("width",width).attr("height",height).
                   attr("x",0).attr("y", 0); 
                   break;
        case "circle":               
                    if (d.attr("r")==null) { 
                        var side =  Math.max(width, height);          
                        var r = side/2;
                        d.attr("cx", r+lw/2).attr("cy", r+lw/2)
                        .attr("r", r);
                        width = 2*r+lw;
                        height = 2*r+lw;
                        } 
                                           
                    break;
        case "ellipse":
                    if ((d.attr("rx")==null) && (d.attr("ry")==null)) {
                        var rx = rxL(width/2, height/2);
                        var ry = ryL(width/2, height/2);
                        d.attr("cx", rx+lw/2).attr("cy", ry+lw/2)
                        .attr("rx", rx).attr("ry", ry); 
                     width = 2*rx + lw;
                     height= 2*ry + lw;               
                     }                            
                    break; 
        };
    // d3.select("#"+id0").style("width",width).style("height",height);
    // d3.select("#"+id0+"_fo_table").style("width",""+width+"px").style("height",""+height+"px");
    d3.select("#"+id0+"_fo").attr("width",""+width+"px").attr("height",""+height+"px");
    d3.select("#"+id0+"_svg").attr("width",""+width+"px").attr("height",""+height+"px");
   }
   
   function getVal(id0, key) {
      var d = d3.select("#"+id0);
      
      if (d.node().nodeName=="TABLE") {
          // alert(""+id0+" "+d.node().nodeName+" "+key+" :"+d.attr(key));  
          return d.attr(key);
          }
        return d.attr(key);
      }
        
   
   function undefWH(v) { 
      return (   
        (getVal(v, "width")==null)
     ||
        (getVal(v, "height")==null)
     )
     ; 
     }
     
   function sumWidth(vs) {
       var r = 0;
       for (var i=0;i<vs.length;i++) {
          r = r + parseInt(getVal(vs[i], "width"));
          }
       return r;
       }
       
   function sumHeight(vs) {
       var r = 0;
       for (var i=0;i<vs.length;i++)
          r = r + parseInt(getVal(vs[i], "height"));
       return r;
       }
       
   function undefW(v) { 
       return (getVal(v, "width")==null);
       }
   
   function undefH(v) { 
        return (getVal(v, "height")==null);
        }
   
   function defW(v) {return (getVal(v, "width")!=null);}
   
   function defH(v) {return (getVal(v, "height")!=null);}
   
   function adjustTableW(clients, id1, lw, hpad, vpad) {
         
         var c = d3.select("#"+id1);
         var width = c.attr("width");
         var height = c.attr("height");
         if (width==null||height==null) return;
         var aUndefW = clients.filter(undefW);
         var aUndefWH = clients.filter(undefWH);
         var sDefW = sumWidth(clients.filter(defW));
         var nW = aUndefW.length; 
         var w = (parseInt(width)-sDefW)/nW;
         var h = parseInt(height); 
         // alert("adjustTableW:"+id1+" "+aUndefWH);  
         for (var i=0;i<aUndefWH.length;i++) {
             adjust1(aUndefWH[i], w, h, lw, hpad, vpad);
             }
         }
   
   function adjustTableH(clients, id1, lw, hpad, vpad) {
         var c = d3.select("#"+id1);
         var width = c.attr("width");
         var height = c.attr("height");
         if (width==null||height==null) return;
         var aUndefH = clients.filter(undefH);
         var aUndefWH = clients.filter(undefWH);
         var sDefH = sumHeight(clients.filter(defH)); 
         var nH = aUndefH.length;
         var h = (parseInt(height)-sDefH)/nH;
         var w = parseInt(width); 
         for (var i=0;i<aUndefWH.length;i++)
             adjust1(aUndefWH[i], w, h, lw, hpad, vpad);
         }
         
        function getMaxOfArray(numArray) {
             return Math.max.apply(null, numArray);
         }
         
    function getMinOfArray(numArray) {
             return Math.min.apply(null, numArray);
         }
              
   function transpose(a) {
      return a[0].map(function (_, c) { return a.map(function (r) { return r[c]; }); });
   }
         
   function adjustTableWH(clients, id1, lw, hpad, vpad) {       
         var c = d3.select("#"+id1);
         var width = c.attr("width");
         var height = c.attr("height");
         clients1 = transpose(clients);
         if (width==null||height==null) return;   
         var aUndefW = clients.map(function(i) {return i.filter(undefW);});
         var aUndefH = clients1.map(function(i) {return i.filter(undefH);});
         var aUndefWH = clients.map(function(i) {return i.filter(undefWH);});
         var sDefW = 
             aUndefW.length<clients.length?Math.max(parseInt(width)/clients1.length
             ,getMaxOfArray(clients.map(function(i) {return sumWidth(i.filter(defW));}))
             ):0;
         // alert(sDefW);
         var sDefH = aUndefW.length<clients.length?
              Math.max(parseInt(height)/clients.length
              ,getMaxOfArray(clients1.map(function(i) {return sumHeight(i.filter(defH));}))
              ):0;
         // alert(sDefH);
         var nW = getMinOfArray(aUndefW.map(function (i){return i.length;}));
         var nH = getMinOfArray(aUndefH.map(function (i){return i.length;}));  
         var w = (parseInt(width)-sDefW)/nW;
         var h = (parseInt(height)-sDefH)/nH;
         // alert("adjustTableW:"+id1+" "+aUndefWH);
         for (var i =0;i<aUndefWH.length;i++) {
         for (var j=0;j<aUndefWH[i].length;j++) {
             adjust1(aUndefWH[i][j], w, h, lw, hpad, vpad);
             }
           }
         }
   
   function adjustFrame(id0, width, height) {
       d3.select("#"+id0).attr("width",width).attr("height",height);
       d3.select("#"+id0+"_fo_table").style("width",width).style("height",height);
       d3.select("#"+id0+"_fo").attr("width",width).attr("height",height);
       d3.select("#"+id0+"_svg").attr("width",width).attr("height",height);
       }
       
  