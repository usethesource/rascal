module experiments::vis2::FigureServer

import experiments::vis2::Figure;
import experiments::vis2::Translate;
import util::Webserver;
import util::HtmlDisplay;
import util::Eval;
import IO;
import List;
import Map;
import Set;
import util::Cursor;
import lang::json::IO;
import Type;
import Exception;

/************************* Figure server *********************************
 This server responds to two requests:
 - initial_figure/<name>: returns the initial version of figure <name>
   The parameters of the request are ignored.
 - refresh/<name>: returns a recomputed version of figure <name> and its model
   The parameters of the request should contain an element named "model" that
   represents the model as updated by the client.
 *************************************************************************/

data Descriptor = descriptor(
	str name, 						// Unique visualization name
	type[&T] model_type, 			// Type of the model
	&T model, 						// The model itself: contains application specific data
	Figure (str event, str utag, &T model) visualize,  // Model visualizer: turns model into a Figure
	&T (&T model) transform,		// Model transformer: computes application specific model transformations
	Figure figure					// Current figure
	);

public map[str, Descriptor] visualizations = ();

/********************** handle page requests ********************/

Response page1(Method method, str path, map[str, str] parameters){ // Debugging only
	// println("page1: <site>, <method>, <path>, <parameters>");
	return page(method, path, parameters);
}

Response page(get(), /^\/$/                          , map[str,str] _) {
res = "\<html\>
		'\<head\>
		'	\<meta charset=UTF-8\>
        '	\<title\>Rascal Visualization Server\</title\>
        
        '	\<link rel=\"stylesheet\" href=\"lib/reset.css\" /\>
        '	\<link rel=\"stylesheet\" href=\"lib/Figure.css\" /\>
        
        '	\<script src=\"http://d3js.org/d3.v3.min.js\" charset=\"utf-8\"\>\</script\>
       
        '   \<!-- GoogleChart --\>
        '   \<script src=\"https://www.google.com/jsapi\"\>\</script\>
        
        '	\<!-- DAGRE-D3 --\>
        '	\<script src=\"lib/dagre-d3.js\"\>\</script\>
        
        ' 	\<!-- MathJax --\>
        ' 	\<script src=\"http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_SVG\"\>\</script\>

        ' 	\<!-- Markdown.Converter --\>
        '	\<script src=\"lib/MarkdownConverter.js\"\>\</script\>
        
        '	\<script src=\"JSFigure.js\"\>\</script\>
 		'	\<script src=\"Graph.js\"\>\</script\>
 		'	\<script src=\"GoogleChart.js\"\>\</script\>
        
        '   \<style\>
        '	a { border: 1px solid; pad: 10px; color: #000000; text-decoration: none; border-radius: 4px;}
		'   a:link { background-color: #E6E6E6; fill: #000000;}
		'   a:hover { background-color: #FFFFFF; }
        '   \</style\>
		'\</head\>
		'\<body\>
		'	\<div id=\"active\"\>
		'   \<br\>
		'	    <for(name <- sort(toList(domain(visualizations)))){> \<a href=\"#\" onclick=\"askServer(\'<getSite()>/initial_figure/<name>\')\"\><name>\</a\>\n   <}>
		'   \<hr\>
		'   \</div\>
		'   \<div id=\"figurearea\"\>
		'   \</div\>
		'\</body\>
		'\</html\>
		";
	// println(res);
	return response(res);
}

Response page(post(), /^\/initial_figure\/<name:[a-zA-Z0-9_]+>/, map[str, str] parameters) {
	// println("post: initial_figure: <name>, <parameters>");
	// println(get_initial_figure(name));
	return response(get_initial_figure(name));
}

Response page(get(), /^\/initial_figure\/<name:[a-zA-Z0-9_]+>/, map[str, str] parameters) {
	// println("get: initial_figure: <name>, <parameters>");
	return response(get_initial_figure(name));
}

Response page(post(), /^\/refresh\/<name:[a-zA-Z0-9_]+>/, map[str, str] parameters) {
	// println("post: refresh: <name>, <parameters>");
	return response(refresh(name, parameters["model"], parameters["event"],parameters["utag"]));
}

default Response page(get(), str path, map[str, str] parameters) = response(base + path); 

default Response page(post(), str path, map[str, str] parameters){
	throw "invalid request <path> with <parameters>";
}

default Response page(!get(), str path, map[str, str] parameters) {
  throw "invalid request <path> with <parameters>";
}


/********************** web server creation ********************/


private loc base = |std:///experiments/vis2|;

private loc startFigureServer() {
  	loc site = |http://localhost:8081|;
  
  while (true) {
    try {
      //println("Trying ... <site>");
      serve(site, dispatchserver(page1));
      return site;
    }  
    catch IO(_): {
      site.port += 1; 
    }
  }
}

private loc site = startFigureServer();

private str getSite() = "<site>"[1 .. -1];

/********************** Render *********************************/

public void render(str name, Figure fig) {
	render(name, #list[void], [], Figure(str event, str utag, value m) { return fig; }, value(value v){return v;});
}

public void render(str name, type[&T] model_type, &T model, Figure (str event, str utag, &T model) visualize){
	render(name, model_type, model, visualize, value(value v){return v;});
}

public void render(str name, type[&T] model_type, &T model, Figure (str event, str utag, &T model) visualize, &T (&T model) transform){
    // println("render: <model_type> <trCursor(makeCursor(model))>");
	f = visualize("init", "all", makeCursor(model));
	visualizations[name] = descriptor(name, model_type, model, visualize, transform, f);
	println(getSite());
	htmlDisplay(site /*+ "?name=<name>"*/);
}

/********************** get_initial_figure **********************/

private str get_initial_figure(str name){
	if(visualizations[name]?){  
		descr = visualizations[name];
		f = descr.visualize("init", "all", makeCursor(descr.model));
		println("get_initial_figure: <descr.model>");
    	res = "{\"model_root\": <toJSON(descr.model)>, \"figure_root\" : <figToJSON(f, getSite())>, \"site\": \"<getSite()>\", \"name\": \"<name>\" }";
    	println("get_initial_server: res = <res>");
    	return res;
    } else {
    	throw "get_initial_figure: visualization <name> unknown";
    }
}

/********************** refresh ********************************/

private str refresh(str name, str modelAsJSON, str event, str utag){ 
	try {
	    // println("START <name>");
		if(visualizations[name]?){
		    // println("refresh:<event>  <utag>");
			descr = visualizations[name];
			model = fromJSON(descr.model_type, modelAsJSON);  // Loopt altijd achter
			model = descr.transform(model);
			// model = descr.transform(makeCursor(model));
			Figure figure = descr.visualize(event, utag, makeCursor(model));
			// println("refresh: figure after figToJSON: <figure>");	
			s = figToJSON(figure, getSite());
			// println(s);	
			descr.model = model;
			visualizations[name] = descr;
			return "{\"model_root\": <toJSON(model)>, \"figure_root\" : <s>, \"site\":  \"<getSite()>\", \"name\": \"<name>\" }";
		} else {
			return "refresh: unknown visualization: <name>";
		}
	} catch e: {
		throw "refresh: unexpected: <e>";
	}
}
