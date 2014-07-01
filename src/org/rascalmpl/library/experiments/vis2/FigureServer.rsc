module experiments::vis2::FigureServer

import experiments::vis2::Figure;
import experiments::vis2::Translate;
import util::Webserver;
import util::HtmlDisplay;
import IO;
import List;
import util::Cursor;
import lang::json::IO;
import Type;

import experiments::vis2::Exp;

/************************* Figure server *********************************
 This server responds to two requests:
 - initial_figure: returns the initial version of the figure
   The parameters of the request are ignored.
 - refresh: returns a recomputed version of the figure
   The parameters of the request should contain an element named "model" that
   represents the model as updated by the client.
 *************************************************************************/

private loc site = |http://localhost:8081|; // Most recent web server

private &T identity(&T model) = model;		// Model identity function

/********************** Render *********************************/

public void render(str title, Figure fig) {
	render(title, #list[void], [], Figure(value m) { return fig; }, identity);
}

public void render(str title, type[&T] mt, &T model, Figure (&T model) makeFig){
	render(title, mt, model, makeFig, identity);
}

public void render(str title, type[&T] model_type, &T model, Figure (&T model) makeFig, &T (&T model) transformer){

	Figure figure = box();
	
	/********************** get_initial_figure **********************/
	
	str get_initial_figure(){
	    res = "{\"model_root\": <toJSON(model)>, \"figure_root\" : <trJson(figure)>, \"site\":  \"<getSite()>\"}";
	    println("get_initial_server: <res>");
	    return res;
	}
	
	/********************** refresh ********************************/
	
	str refresh(str modelAsJSON){ 
		try {
			model = fromJSON(model_type, modelAsJSON);
			println("refresh: <site>, <model>");
			model = transformer(model);
			figure = makeFig(makeCursor(model));
			s = trJson(figure);
			println(s);
			return "{\"model_root\": <toJSON(model)>, \"figure_root\" : <s>, \"site\":  \"<getSite()>\"}";
		} catch e: {
			throw "refresh: unexpected: <e>";
		}
	}
	
	/********************** handle page requests ********************/
	
	Response page1(Method method, str path, map[str, str] parameters){ // Debugging only
		println("page1: <site>, <method>, <path>, <parameters>");
		return page(method, path, parameters);
	}
	
	Response page(post(), /^\/initial_figure/, map[str, str] parameters) {
		println("post: initial_figure: <parameters>");
		return response(get_initial_figure());
	}
	
	Response page(post(), /^\/refresh/, map[str, str] parameters) {
		println("post: refresh: <parameters>");
		return response(refresh(parameters["model"]));
	}
	
	default Response page(get(), str path, map[str, str] parameters) = response(base + path); 
	
	default Response page(post(), str path, map[str, str] parameters){
		throw "invalid request <path> with <parameters>";
	}
	
	default Response page(!get(), str path, map[str, str] parameters) {
	  throw "invalid request <path> with <parameters>";
	}
	
	/********************** web server creation ********************/
	
	loc base = |file:///|;
	
	loc startFigureServer() {
	  loc site = |http://localhost:8081|;
	  
	  while (true) {
	    try {
	      println("Trying ... <site>");
	      serve(site, dispatchserver(page1));
	      return site;
	    }  
	    catch IO(_): {
	      site.port += 1; 
	    }
	  }
	}
	
	public str getSite() = "<experiments::vis2::FigureServer::site>"[1 .. -1];
	
	//loc site = |http://localhost:8081|; // Most recent web server
	
	try {
		println("shutdown ... <experiments::vis2::FigureServer::site>");
		shutdown(experiments::vis2::FigureServer::site);
		shutdown(experiments::vis2::FigureServer::site);
	} catch e: { };
	
	experiments::vis2::FigureServer::site = startFigureServer();
	
	figure = makeFig(makeCursor(model));

	print(getSite());
	htmlDisplay(|file:///tmp/<title>.html|, fig2html(title, getSite()));
}

