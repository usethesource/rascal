function makeCircle(selection, x, y, width, height, options){
	var cx = width / 2,
		cy = height / 2,
		r = "r" in options ? options.r : 25,
		fill = "fill" in options ? options.fill : "black",
		stroke = "stroke" in options ? options.stroke : "black";
	return selection
			.append("circle")
			.attr("cx", cx)
			.attr("cy", cy)
			.attr("r", r)
			.style("fill", fill)
			.style("stroke", stroke);
}
	
function makeRect(selection, x, y, width, height, options){
	var	fill = "fill" in options ? options.fill : "black",
		stroke = "stroke" in options ? options.stroke : "black",
		stroke_width = "stroke_width" in options ? options.stroke_width : 1,
		stroke_dasharray = "stroke_dasharray" in options ? options.stroke_dasharray : [];
		
	return selection
			.append("rect")
			.attr("x", x)
			.attr("y", y)
			.attr("width", width)
			.attr("height", height)
			.style("fill", fill)
			.style("stroke", stroke)
			.style("stroke-width", stroke_width)
			.style("stroke-dasharray", stroke_dasharray);
}


			
		