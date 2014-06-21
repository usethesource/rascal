function makeTextfield(selection, x, y, width, height, options){

	var fill = "fill" in options ? options.fill : "green";
	var callback = "callback" in options ? options.callback : "0";
	var site = "site" in options ? options.site : "http://localhost:8081";
	//var form1 = "<form action=\"" + site + "/do_callback?callback=" + callback + "\"> <input type=\"submit\" value=\"Click me\"></form>";
	//var form2 = "<a href=\"" + site + "/do_callback/" + callback + "\"> Click me </a>";
	
	//var form3 = "<form action=\"\"> <input type=\"text\" id=\"callback_str_arg\" onchange=\"post('" + site + "/do_callback_str/" + callback + "', {callback_str_arg :  document.getElementById('callback_str_arg').value })\"\></form>";

	var form3 = "<form action='" + site + "/do_callback_str/" + callback + "' method='post'> <input type=\"text\" name=\"callback_str_arg\" /></form>";

	//alert(form3);
	return selection.append("foreignObject")
			.attr("x", x)
			.attr("y", y)
    		.attr("width", width)
    		.attr("height", height)
     		.append("xhtml:body")
    		.style("font", "12px 'Helvetica Neue'")
   			.html(form3);
}