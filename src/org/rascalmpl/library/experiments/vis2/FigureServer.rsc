module experiments::vis2::FigureServer

import experiments::vis2::Figure;
import experiments::vis2::Properties;
import experiments::vis2::Translate;
import util::Webserver;
import util::HtmlDisplay;
import IO;

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

private Response page1(Method method, str path, map[str, str] parameters){
	println("page1: <site>, <method>, <path>, <parameters>");
	return page(method, path, parameters);
}

private Response page(get(), /^\/$/                          , map[str,str] _)   = response(generateUpdatedFigure());

private Response page(post(), /^\/initial_figure/, map[str, str] parameters) {
	println("post: initial_figure: <parameters>");
	return response(generateUpdatedFigure());
}

private Response page(post(), /^\/do_callback\/<fun:[a-zA-Z0-9_]+>/, map[str, str] parameters) {
	println("post: do_callback: <fun>, <parameters>");
	do_callback(fun, parameters);
	return response(generateUpdatedFigure());
}


private Response page(post(), /^\/do_callback_str\/<fun:[a-zA-Z0-9_]+>/, map[str, str] parameters) {
	println("post: do_callback_str: <fun>, <parameters>");
	do_callback_str(fun, parameters);
	return response(generateUpdatedFigure());
}

private default Response page(get(), str path, map[str, str] parameters) = response(base + path); 

private default Response page(post(), str path, map[str, str] parameters){
	throw "invalid request <path> with <parameters>";
}

private default Response page(!get(), str path, map[str, str] parameters) {
  throw "invalid request <path> with <parameters>";
}

// ********************** Generating figures **********************

private str current_title = "";

private Figure current_fig = box(fillColor("red"), size(100,200));

private loc site = startFigureServer();

private str generateUpdatedFigure(){ 
	println("generateUpdatedFigure: <site>, <current_fig>");
	s = trJson(current_fig, site);
	println(s);
	return s;
}

public void generateInitialFigure(str title, Figure f){
	//stopRenderWebserver(site);
	//site = startRenderWebserver();
	current_title = title;
	current_fig = f;
	println("generateInitialFigure: <site>, <current_fig>");
	s = fig2html(title, f, site=site);
	htmlDisplay(|file:///tmp/<title>.html|, s);
}