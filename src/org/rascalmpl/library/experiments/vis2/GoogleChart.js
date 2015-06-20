 /********************************************************/
/*					Google Chart functions				*/
/********************************************************/

"use strict";

/****************** googleBarChart *************************/

var printq = function(o){
    var str='';

    for(var p in o){
        str+= p + '\n';
    /*    if(typeof o[p] == 'string'){
            str+= p + ': ' + o[p]+';';
        }else{ str+= p+";";
            // str+= p + ': {' + printq(o[p]) + '}';
        } */
        
    }

    return str;
}

/*
    Recursively merge properties and return new object
    obj1 <- obj2 [ <- ... ]  
 */
 
 function merge () {
        var dst = {}
            ,src
            ,p
            ,args = [].splice.call(arguments, 0)
        ;

        while (args.length > 0) {
            src = args.splice(0, 1)[0];
            if (toString.call(src) == '[object Object]') {
                for (var p in src) {
                    if (src.hasOwnProperty(p)) {
                        if (toString.call(src[p]) == '[object Object]') {
                            dst[p] = merge(dst[p] || {}, src[p]);
                        } else {
                            dst[p] = src[p];
                        }
                    }
                }
            }
        }
        return dst;
    }


// Load the Visualization API and the piechart package.
      google.load('visualization', '1.1', {'packages':['corechart']});
      google.load("visualization", '1.1', {'packages':['sankey']});
      // Set a callback to run when the Google Visualization API is loaded.
     // google.setOnLoadCallback(drawChart);

      // Callback that creates and populates a data table,
      // instantiates the pie chart, passes in the data and
      // draws it.
      function drawChart(figure, w, h) {
        // Create the data table.    
        var data = google.visualization.arrayToDataTable(figure.data, false);
        // Set chart options
        var options = {title:figure.name,            
                        width:w,
                        height:h,
                        forceIFrame:true,
                        legend: 'none',
                        lineWidth: 1,
                        pointSize: 3,
                        sizeAxis: {minSize:3, maxSize:3}
                       };

        // Instantiate and draw our chart, passing in some options.
         var chart = new google.visualization[figure.command](document.getElementById('chart_div'+idx));
         idx = idx + 1;    
        chart.draw(data, merge(options,figure.options));
      }



/********************************************************/
/*				google		 					*/
/********************************************************/

var idx = 0;

Figure.bboxFunction.google = function(selection) {
    Figure.registerComponent("google", this.name);
    Figure.drawFunction[this.name] = function(figure, x, y, w, h) {
      // alert(x);
      selection.append("foreignObject")
     .attr("width", figure.width)
     .attr("height", figure.height)
     .attr("x", x).attr("y", y)
     .append("xhtml:body")
     .attr("id", "chart_div"+idx)
     .attr("style","stroke:none;"); 
     // alert(figure.width); 
     drawChart(figure, w, h);
     } 
    this.svg = selection.append("svg");    
  	return this.svg;
}

Figure.drawFunction.google = function (x, y, w, h) {  
	return Figure.getDrawForComponent("google", this.name)(this, x, y, w, h);
    }
