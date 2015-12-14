var ajax = {};
alertSize();
// window.alert( 'Height = ' + screenHeight );
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


function askServer(path, parameters, timer, timeout, callback) {
	ajax.post(path, parameters, function(responseText){
		try { 
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
   
 function alertSize() {
    if(typeof(window.innerWidth) == 'number') {
        // Non-IE
        screenWidth = window.innerWidth-50;
        screenHeight = window.innerHeight-50;
    } 
    else if(document.documentElement && (document.documentElement.clientWidth || document.documentElement.clientHeight)) {
        // IE 6+ in 'standards compliant mode'
        screenWidth = document.documentElement.clientWidth;
        screenHeight = document.documentElement.clientHeight;
    } 
    else if(document.body && (document.body.clientWidth || document.body.clientHeight)) {
        // IE 4 compatible
        screenWidth = document.body.clientWidth;
        screenHeight = document.body.clientHeight;
    }

   
}
   
 rxL= function(rx, ry) {return rx /* * Math.sqrt(rx*rx+ry*ry)/ry*/;};
 ryL= function(rx, ry) {return ry /* * Math.sqrt(rx*rx+ry*ry)/rx*/;};
 
 function corner(n, lineWidth) {
     if (n==0) return lineWidth;
     // var angle = Math.PI - 2 * Math.PI / n;
     angle = Math.PI/n;
     var lw = lineWidth<0?0:lineWidth;
     // alert(lw/Math.sin(angle))
     // return lw;
     return lw/Math.cos(angle)+1;
    }  
 
 function svgStyle(s, svg) {
     switch (s) {
          case "fillColor": return svg?"fill":"background";
          case "lineColor": return svg?"stroke":"border-color";
          case "lineWidth": return svg?"stroke-width":"border-width";
          case "fillOpacity": return svg?"fill-opacity":"opacity";
          case "lineOpacity": return svg?"stroke-opacity":"opacity";
          }
     return s;
     }
 
 function adjust0(f, id1, lw, hpad, vpad) { 
    
    var to = d3.select("#"+f.id);
    var from = d3.select("#"+id1); 
    var lw0 = parseInt(from.style("stroke-width"));
    var width = document.getElementById(id1).getBoundingClientRect().width;
    var height = document.getElementById(id1).getBoundingClientRect().height;
    // alert("adjust0:"+width);
    if  (from.attr("width")!=null) width = parseInt(from.attr("width"));
    if  (from.attr("height")!=null) height = parseInt(from.attr("height"));
    // alert("adjust0:"+width);
    if (width==0 || height == 0) return;
    width = width*f.hgrow + lw +hpad+f.x;
    height = height*f.vgrow + lw + vpad+f.y;
    if (from.node().nodeName=="text") {
            from.attr("x", width/2);
            from.attr("y", height/2);
            from.attr("dy", ".3em");
            }
    if (from.node().nodeName == "ellipse" || from.node().nodeName == "circle"
                                       || from.node().nodeName == "path" 
    // from.node().nodeName=="polygon"
     )
        {width += lw0; height += lw0;}
    switch (to.node().nodeName) {
        case "rect": 
                   to.attr("width",width).attr("height",height).
                   attr("x",0).attr("y", 0);
                   break;
        case "circle":  
                    var side =  Math.max(width, height);           
                    var r = (side-lw)/2;
                    to.attr("cx", r+lw/2).attr("cy", r+lw/2)
                     .attr("r", r); 
                    width = 2*r+lw;
                    height = 2*r+lw; 
                    to.attr("width",width).attr("height",height);                  
                    break;
        case "ellipse": 
                    var rx = (width-lw)/2;
                    var ry = (height-lw)/2;
                    // alert("rx="+width+" ry="+height+" "+rx+" " + ry);
                    to.attr("cx", rx+lw/2).attr("cy", ry+lw/2)
                     .attr("rx", rx).attr("ry", ry); 
                     width = 2*rx + lw;
                     height= 2*ry + lw;
                    to.attr("width",width).attr("height",height);               
                    break; 
        case "polygon":           
                    if (to.attr("r")==null) { 
                        var side =  Math.max(width, height); 
                        width = side+corner(f.n, f.lw);
                        height = side+corner(f.n, f.lw);   
                        var r = side/2;
                        to.attr("points",
                        translatePoints(f, r, width/2, height/2).map(function(a) {
                             return [a.x, a.y].join(",");
                             }
                        ).join(" ")
                        );
                        to.attr("width",width).attr("height",height);
                        var e = d3.select("#"+f.id+"_circle");
                        e.attr("cx", width/2).attr("cy", height/2)
                        .attr("r", r);
                        e.attr("width",width).attr("height",height);
                        }                                     
                    break;
        };
    d3.select("#"+f.id+"_fo_table").style("width",width).style("height",height);
    d3.select("#"+f.id+"_fo").attr("width",width).attr("height",height);
    d3.select("#"+f.id+"_svg").attr("width",width).attr("height",height);
   }
   
   function translatePoints(f, r, x, y) {
         var q = new Array();
         //  alert(f.angle);
         var p = f.angle/360.0*2*Math.PI;
         // alert(p);
         var angl = 2 * Math.PI / f.n;
         for (var i=0;i<f.n;i++) {
             q.push({"x":x+r*Math.cos(p+i*angl), "y":y+r*Math.sin(p+i*angl)});
             }
       return q;
       }
   
   function adjust1(id, f, width, height, hpad, vpad) { 
    if (f.id=="emptyFigure") return;
    var d = d3.select("#"+f.id);
    // alert("adjust1: "+id0+" "+d.node().nodeName+" "+width+" "+height);
    if (d.node().nodeName=="TABLE") {
          if (d.attr("width")==null) {
             d.style("width", parseInt(width));
             d.attr("width", parseInt(width));
             }
          if (d.attr("height")==null) { 
              d.style("height", parseInt(height));
              d.attr("height", parseInt(height));
              }
          return;
          } 
    if  (d.attr("width")!=null) width = parseInt(d.attr("width"));
    if  (d.attr("height")!=null) height = parseInt(d.attr("height"));
    var w = width * f.hshrink;
    var h = height* f.vshrink; 
    switch (d.node().nodeName) {
        case "rect": 
         
                   d.attr("width",w).attr("height",h).
                   attr("x",0).attr("y", 0); 
                   break;
        case "circle":               
                    if (d.attr("r")==null) { 
                        var side =  Math.min(width, height); 
                        var side1 =  Math.min(w, h);
                        width = side;
                        height = side; 
                        w = side1;
                        h = side1;        
                        var r1 = (side1-f.lw)/2-1;
                        d.attr("cx", side1/2).attr("cy", side1/2)
                        .attr("r", r1);
                        width = side;
                        height = side;
                        d.attr("width",w).attr("height",h);
                        } 
                                           
                    break;
        case "polygon":             
                    if (d.attr("r")==null) {   
                        var side =  Math.min(width, height); 
                        var side1 =  Math.min(w, h);
                        w = side1;
                        h = side1;    
                        var r1 = (side1/2)-corner(f.n, f.lw);
                        // alert(f.angle);
                        d.attr("points",
                        translatePoints(f, r1, side1/2, side1/2).map(function(a) {
                             return [a.x, a.y].join(",");
                             }
                        ).join(" ")
                        );
                        d.attr("width",w).attr("height",h);
                        var e = d3.select("#"+f.id+"_circle");
                        e.attr("cx", side1/2).attr("cy", side1/2)
                        .attr("r", r1);
                        width = side;
                        height = side;
                        e.attr("width",w).attr("height",h);
                        }                                     
                    break;
        case "ellipse":
                    if ((d.attr("rx")==null) && (d.attr("ry")==null)) {
                        //var rx = rxL(width/2, height/2);
                        //var ry = ryL(width/2, height/2);
                        //var rx1 = rxL(w/2, h/2);
                        //var ry1 = ryL(w/2, h/2);
                        var rx1 = (w-f.lw)/2;
                        var ry1 = (h-f.lw)/2;
                        d.attr("cx", w/2).attr("cy", h/2)
                        .attr("rx", rx1).attr("ry", ry1); 
                      d.attr("width",w).attr("height",h);
                     //width = 2*rx + lw;
                     //height= 2*ry + lw;               
                     }                            
                    break; 
        };
    // alert(width);
    // d3.select("#"+f.id").style("width",width).style("height",height);
    d3.select("#"+f.id+"_fo_table").style("width",""+w+"px").style("height",""+h+"px");
    d3.select("#"+f.id+"_fo").attr("width",""+w+"px").attr("height",""+h+"px");
    d3.select("#"+f.id+"_svg").attr("width",""+w+"px").attr("height",""+h+"px");
    d3.select("#"+id+"_"+f.id).style("width",""+width+"px").style("height",""+height+"px");
    d3.select("#"+id+"_"+f.id).attr("pointer-events", "none");
    d3.select("#"+f.id+"_fo_table").attr("pointer-events", "none");
   }
   
   function figShrink(id, hshrink, vshrink, lw, n, angle) {
        // alert("fig");
        return {id:id, hshrink:hshrink, vshrink: vshrink, lw: lw, n: n, angle: angle};
        }
        
  function figGrow(id, hgrow, vgrow, lw, n, angle, x, y) {
        return {id:id, hgrow:hgrow, vgrow: vgrow, lw: lw, n: n, angle: angle, x: x, y: y};
        }
   
   function getVal(f, key) {
      // alert(f.id);
      if (f.id=="emptyFigure") return 0;
      var d = d3.select("#"+f.id);
      
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
   
   function adjustTable(id1, clients) { 
       var aUndefWH = clients.filter(undefWH);
       if (aUndefWH.length==0) {
            var width = document.getElementById(id1).getBoundingClientRect().width;
            var height = document.getElementById(id1).getBoundingClientRect().height;
            d3.select("#"+id1).attr("width",""+width+"px").attr("height",""+height+"px");
            }
       }
       
   function adjustTableWH1(id1, clients) { 
         var aUndefWH = clients.filter(function(i) {return i.filter(undefWH).length!=0;}); 
         if (aUndefWH.length==0) {
            var width = document.getElementById(id1).getBoundingClientRect().width;
            var height = document.getElementById(id1).getBoundingClientRect().height;
            d3.select("#"+id1).attr("width",""+width+"px").attr("height",""+height+"px");
            }
       }
       
   function adjustOverlay(clients, id1, lw, hpad, vpad) { 
         // alert("adjust");
         var c = d3.select("#"+id1);
         var width = c.attr("width");
         var height = c.attr("height");
         // alert(id1);
         if (width==null||height==null) return;
         
         var aUndefWH = clients.filter(undefWH);
         var w = parseInt(width);
         var h = parseInt(height); 
         // alert(aUndefWH.length);
         for (var i=0;i<aUndefWH.length;i++) {
             id1, adjust1(id1, aUndefWH[i], w, h,  hpad, vpad);
             }
         width = 0; height = 0;
         var isEmpty = false;
         for (var i=0;i<clients.length;i++) {
            var d = d3.select("#"+clients[i].id);
            var e = d3.select("#"+clients[i].id+"_svg");
            if (!e.empty()) {
               w = parseInt(d.attr("width"))+ parseInt(e.attr("x"));
               h = parseInt(d.attr("height"))+ parseInt(e.attr("y"));
               if (w>width) width = w;
               if (h>height) height = h;
               } 
            else isEmpty = true;      
            }
         if (!isEmpty) {
             c.attr("width", width).attr("height", height);
             c = d3.select("#"+id1+"_svg");
             c.attr("width", width).attr("height", height);
             }     
         }
        
   function adjustTableW(clients, id1, lw, hpad, vpad) { 
         var c = d3.select("#"+id1);
         var width = c.attr("width");
         var height = c.attr("height");
         // alert(id1);
         if (width==null||height==null) return;
         
         var aUndefW = clients.filter(undefW);
         var aUndefWH = clients.filter(undefWH);
         var sDefW = sumWidth(clients.filter(defW));
         var nW = aUndefW.length; 
         var w = (parseInt(width)-sDefW)/nW;
         var h = parseInt(height); 
         // alert("adjustTableW:"+id1+" "+aUndefWH); 
         // lw  =10; 
         for (var i=0;i<aUndefWH.length;i++) {
             id1, adjust1(id1, aUndefWH[i], w, h,  hpad, vpad);
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
           if (original[i].length>ncols) ncols = original[i].length;
           } 
     var copy  = new Array(ncols);
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
         var c = d3.select("#"+id1);
         var width = c.attr("width");
         var height = c.attr("height");
         clients1 = transpose(clients);
         if (width==null||height==null) return;  
         var aUndefW = clients.map(function(i) {return i.filter(undefW);}); 
        
         var aUndefH = clients1.map(function(i) {return i.filter(undefH);});
         // alert("adjustWHJ");  
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
         // alert(w);
         // alert(h);
         for (var i =0;i<aUndefWH.length;i++) {
         for (var j=0;j<aUndefWH[i].length;j++) {
             adjust1(id1, aUndefWH[i][j], w, h,  hpad, vpad);
             }
           }
         }
         
         
 function adjustFrame(id0, width, height) {
       d3.select("#"+id0).attr("width",width).attr("height",height);
       d3.select("#"+id0+"_fo_table").style("width",width).style("height",height);
       d3.select("#"+id0+"_fo").attr("width",width).attr("height",height);
       d3.select("#"+id0+"_svg").attr("width",width).attr("height",height);
       }
         
  
  
   function adjustBox(id0, id1, hshrink, vshrink, lw, n, angle) { 
       // alert("adjustBox:"+id1+":"+d3.select("#"+id1).node().nodeName);
       // alert(""+id0+" "+d3.select("#"+id0).attr("width"));
       var to = d3.select("#"+id0);
       if (to.attr("width")!=null && to.attr("height")!=null) return;
       var from = d3.select("#"+id1);
       var width = document.getElementById(id1).getBoundingClientRect().width;
       if (from.attr("width")!=null) width = from.attr("width");
       var height = document.getElementById(id1).getBoundingClientRect().height;
       if (from.attr("height")!=null) height = from.attr("height");
       
       // alert(height);
       if (width==null||height==null) return;
       // alert(width);
       adjust1(id1, {id:id0, hshrink:hshrink, vshrink:vshrink, lw:lw, n:n, angle: angle}, width, height,  0, 0);
       }
 
function isObject (item) {
  return (typeof item === "object" && !Array.isArray(item) && item !== null);
  } 
  
function nl2br (str, is_xhtml) {
     var breakTag = (is_xhtml || typeof is_xhtml === 'undefined') ? '<br />' : '<br>';
     return (str + '').replace(/([^>\r\n]?)(\r\n|\n\r|\r|\n)/g, '$1' + breakTag + '$2');
  }    
  