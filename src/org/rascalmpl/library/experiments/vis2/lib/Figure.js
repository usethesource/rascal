function post(path, params, method) {
    method = method || "post"; // Set method to post by default if not specified.
    
    // The rest of this code assumes you are not using a library.
    // It can be made less wordy if you use one.
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", path);

    for(var key in params) {
        if(params.hasOwnProperty(key)) {
            var hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", params[key]);

            form.appendChild(hiddenField);
         }
    }

    document.body.appendChild(form);
    form.submit();
}

function makeText(selection, x, y, width, height, options){
	var content = "text_value" in options ? options.text_value : "no text";
	return selection
			.append("text")
			.attr("x", x)
			.attr("y", y)
			.text(content)
			.style("font", "font" in options ? options.font : "Arial")
			.style("font-size", "font_size" in options ? options.font_size : 12)
			.style("text-anchor", "start")
	   		.style("fill", "fill" in options ? options.fill : "black")
	   		.style("fill-opacity", "fill_opacity" in options ? options.fill_opacity : 1)
			.style("stroke-width", "stroke_width" in options ? options.stroke_width : 1)
			.style("stroke-dasharray", "stroke_dasharray" in options ? options.stroke_dasharray : [])
			.style("stroke-opacity", "stroke_opacity" in options ? options.stroke_opacity : 1.0);
}


function makeCircle(selection, x, y, width, height, options){
	var cx = width / 2,
		cy = height / 2,
		r = "r" in options ? options.r : 25;
	return selection
			.append("circle")
			.attr("cx", cx)
			.attr("cy", cy)
			.attr("r", r)
			.style("stroke", "stroke" in options ? options.stroke : "black")
	   		.style("fill", "fill" in options ? options.fill : "black")
	   		.style("fill-opacity", "fill_opacity" in options ? options.fill_opacity : 1)
			.style("stroke-width", "stroke_width" in options ? options.stroke_width : 1)
			.style("stroke-dasharray", "stroke_dasharray" in options ? options.stroke_dasharray : [])
			.style("stroke-opacity", "stroke_opacity" in options ? options.stroke_opacity : 1.0);
}
	
function makeRect(selection, x, y, width, height, options){
	return selection
			.append("rect")
			.attr("x", x)
			.attr("y", y)
			.attr("width", width)
			.attr("height", height)
			.attr("rx", "rx" in options ? options.rx : 1)
			.attr("ry", "ry" in options ? options.ry : 1)	
			.style("stroke", "stroke" in options ? options.stroke : "black")
	   		.style("fill", "fill" in options ? options.fill : "black")
	   		.style("fill-opacity", "fill_opacity" in options ? options.fill_opacity : 1)
			.style("stroke-width", "stroke_width" in options ? options.stroke_width : 1)
			.style("stroke-dasharray", "stroke_dasharray" in options ? options.stroke_dasharray : [])
			.style("stroke-opacity", "stroke_opacity" in options ? options.stroke_opacity : 1.0);
}


			
		