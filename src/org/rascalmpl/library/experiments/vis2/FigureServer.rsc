module experiments::vis2::FigureServer

import experiments::vis2::Figure;
import experiments::vis2::Translate;
import util::Webserver;
import util::HtmlDisplay;
import IO;
import List;
import util::Cursors;
import lang::json::IO;
import Type;


/************************* Figure server *********************************
 This server responds to two requests:
 - initial_figure: returns the initial version of the figure
   The parameters of the request are ignored.
 - refresh: returns a recomputed version of the figure
   The parameters of the request should contain an element named "model" that
   represents the model as updated by the client.
 *************************************************************************/

private loc base = |file:///|;

private loc startFigureServer() {
  loc site = |http://localhost:8081|;
  
  while (true) {
    try {
      serve(site, dispatchserver(page1));
      return site;
    }  
    catch IO("Address already in use"): {
      site.port += 1; 
    }
  }
}
 
private void stopFigureServer(loc site) {
  shutdown(site);
}

private Response page1(Method method, str path, map[str, str] parameters){ // Debugging only
	println("page1: <site>, <method>, <path>, <parameters>");
	return page(method, path, parameters);
}

private Response page(post(), /^\/initial_figure/, map[str, str] parameters) {
	println("post: initial_figure: <parameters>");
	return response(refresh(model_root));
}

private Response page(post(), /^\/refresh/, map[str, str] parameters) {
	println("post: refresh: <parameters>");
	return response(refresh(fromJSON(type(model_type,()), parameters["model"])));
}

private default Response page(get(), str path, map[str, str] parameters) = response(base + path); 

private default Response page(post(), str path, map[str, str] parameters){
	throw "invalid request <path> with <parameters>";
}

private default Response page(!get(), str path, map[str, str] parameters) {
  throw "invalid request <path> with <parameters>";
}

/*********************** Global state ***************************/

private str figure_name = "";

private Symbol model_type;

private value model_root = ();

Figure (value model) figure_builder = Figure(value v) { return box(); };

private loc site = startFigureServer();

public str getSite() = "<site>"[1 .. -1];

/********************** Render *********************************/

public void render(str title, Figure fig) {
	render(title, [], Figure(value m) { return fig; });
}

public void render(str title, &T model, Figure (&T model) makeFig){
	figure_name = title;
	model_root = model;
	model_type = typeOf(model);
	figure_builder = makeFig;
	
	print(getSite());
	htmlDisplay(|file:///tmp/<title>.html|, fig2html(title, getSite()));
}

/********************** refresh ********************************/

private str refresh(value model){ 
	try {
		println("refresh: <site>, <model>");
		model_root = model;
		figure_root = figure_builder(makeCursor(model_root));
		s = trJson(figure_root);
		println(s);
		return "{\"model_root\": <toJSON(model_root)>, \"figure_root\" : <s>, \"site\":  \"<getSite()>\"}";
	} catch e: {
		throw "refresh: unexpected: <e>";
	}
}