function makeButton(selection, x, y, width, height, options){

	var fill = "fill" in options ? options.fill : "green";
	var callback = "callback" in options ? options.callback : "0";
	var site = "site" in options ? options.site : "http://localhost:8081";
	//var form1 = "<form action=\"" + site + "/do_callback?callback=" + callback + "\"> <input type=\"submit\" value=\"Click me\"></form>";
	//var form2 = "<a href=\"" + site + "/do_callback/" + callback + "\"> Click me </a>";
	
	var form3 = "<form action=\"\"> <input type=\"button\" value=\"Click me\" onclick=\"post('" + site + "/do_callback/" + callback + "', {})\"\></form>";
	//alert(form3);
	return selection.append("foreignObject")
			.attr("x", x)
			.attr("y",y)
    		.attr("width", width)
    		.attr("height", height)
     		.append("xhtml:body")
    		.style("font", "font" in options ? options.font : "Arial")
			.style("font-size", "font_size" in options ? options.font_size : 12)
			.style("text-anchor", "start")
   			.html(form3);
}