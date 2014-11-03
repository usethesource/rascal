 /********************************************************/
/*					Vega Chart functions				*/
/********************************************************/

"use strict";

/****************** vegaBarChart *************************/

var printq = function(o){
    var str='';

    for(var p in o){
        // str+= p + '\n';
        if(typeof o[p] == 'string'){
            str+= p + ': ' + o[p]+';';
        }else{ str+= '';
            // str+= p + ': {' + printq(o[p]) + '}';
        }
        
    }

    return str;
}


/********************************************************/
/*				vega			 					*/
/********************************************************/


Figure.bboxFunction.vega = function(selection) {
    Figure.registerComponent("vega", this.variable);
    
    Figure.drawFunction[this.variable] = function(figure, x, y, w, h) {
   //  var data = {name:"table",:figure.data};
    // alert(data);
    var dfile =  Figure.site + "/vegaJSON/" + figure.module +"/"+figure.variable;
    // var dfile ="vega/StackedBar.json";
    d3.json(dfile, function(err, data){
         figure.padding = new Object();
         figure.padding.left = data.padding.left;
         figure.padding.right = data.padding.right;
         figure.padding.top = data.padding.top;
         figure.padding.bottom = data.padding.bottom;
    });
    
    function parse(err, d) { 
    figure.svg.append("foreignObject")
    .attr("width", figure.width)
    .attr("height", figure.height)
    .attr("x", x).attr("y", y)
    .append("xhtml:body")
    .attr("id", "chartName")
    .attr("style","stroke:none;");  
    vg.parse.spec(dfile, function(chart) {
         var view; 
         var data = {table: d};
         if (figure.data || figure.datasets) {
                view = chart({el: "#chartName", data: data, renderer: "svg"});
            } else {
                view = chart({el: "#chartName",renderer: "svg"});
            }
           view.width(figure.width-figure.padding.left-figure.padding.right)
           .height(figure.height-figure.padding.bottom-figure.padding.top).padding(figure.padding).update();
           d3.select("canvas").remove();  
           }
         );    
    } 
    
    function updateSpec(spec, width, height, top, left, bottom, right) {
        spec.width = width - left - right;
        spec.height = height - top - bottom;
        var padding = new Object();
        padding.top = top;
        padding.left = left;
        padding.bottom = bottom;
        padding.right = right;
        spec.padding = padding;
        return spec;
    }
    if (figure.data) {
       d3.json(figure.data ,  parse);
       }
    else {
         // alert(JSON.stringify(figure.datasets));
         parse("", figure.datasets);
         }
    }
  	this.svg = selection.append("svg");
  	if(!this.hasDefinedWidth()){
  		this.width = 400;
  	}
  	if(!this.hasDefinedHeight()){
  		this.height = 400;
  	}
  	return this.svg;
  	
}

Figure.drawFunction.vega = function (x, y, w, h) {
    // alert(this.variable);  
	return Figure.getDrawForComponent("vega", this.variable)(this, x, y, w, h);
    }
