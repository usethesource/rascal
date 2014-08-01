function makeTexteditor(selection, x, y, width, height, options){
	return selection.append("foreignObject")
			.attr("x", x)
			.attr("y",y)
    		.attr("width", width)
    		.attr("height", height)
     		.append("xhtml:body")
    		.style("font", "14px 'Helvetica Neue'")
   			.html("<textarea autofocus=\"autofocus\" id=\"texteditor\" cols=" + (width/5) + " rows=" + (height/5) +"\>aa bb cc</textarea>");   
}