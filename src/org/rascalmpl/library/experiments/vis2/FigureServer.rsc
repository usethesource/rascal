module experiments::vis2::FigureServer

import experiments::vis2::Figure;
import experiments::vis2::Properties;
import experiments::vis2::Translate;
import util::Webserver;
import util::HtmlDisplay;
import IO;
import List;

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
	return response(generateUpdatedFigure(figure_state));
}

private Response page(post(), /^\/do_callback\/<fun:[a-zA-Z0-9_]+>/, map[str, str] state) {
	println("post: do_callback: <fun>, <state>");
	figure_state = do_callback(fun, state);
	return response(generateUpdatedFigure(figure_state));
}

private Response page(post(), /^\/do_callback_str\/<fun:[a-zA-Z0-9_]+>/, map[str, str] parameters) {
	println("post: do_callback_str: <fun>, <parameters>");
	do_callback_str(fun, parameters);
	return response(generateUpdatedFigure(parameters));
}

private default Response page(get(), str path, map[str, str] parameters) = response(base + path); 

private default Response page(post(), str path, map[str, str] parameters){
	throw "invalid request <path> with <parameters>";
}

private default Response page(!get(), str path, map[str, str] parameters) {
  throw "invalid request <path> with <parameters>";
}

// ********************** Generating figures **********************

private str figure_name = "";

private Figure figure_root = box(fillColor("red"), size(100,200));

private map[str,str] figure_state = ();

private loc site = startFigureServer();

private str state2Json(map[str,str] state) =
  "{" + intercalate(", ", ["\"<key>\" : \"<state[key]>\"" | key <- state]) + "}";

private str generateUpdatedFigure(map[str,str] state){ 
	println("generateUpdatedFigure: <site>, <state>, <figure_root>");
	figure_state = state;
	s = trJson(figure_root, figure_state, site);
	println(s);
	return "{\"figure_state\": <state2Json(figure_state)>, \"figure_root\" : <s> }";
}

public void generateInitialFigure(str title, map[str,str] state, Figure f){
	//stopRenderWebserver(site);
	//site = startRenderWebserver();
	figure_name = title;
	figure_root = f;
	figure_state = state;
	println("generateInitialFigure: <site>, state, <figure_root>");
	s = fig2html(title, state, f, site=site);
	print(s);
	htmlDisplay(|file:///tmp/<title>.html|, s);
}