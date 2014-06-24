module experiments::vis2::Examples2

import experiments::vis2::Figure;
import experiments::vis2::Properties;
import experiments::vis2::Translate;
import util::Math;
import util::HtmlDisplay;

import util::Webserver;
import IO;

loc base = |file:///|;

loc startRenderWebserver() {
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
 
void stopRenderWebserver(loc site) {
  shutdown(site);
}

Response page1(Method method, str path, map[str, str] parameters){
	//println("page1: <site>, <method>, <path>, <parameters>");
	return page(method, path, parameters);
}

Response page(get(), /^\/$/                          , map[str,str] _)   = response(generateUpdatedFigure());

Response page(post(), /^\/initial_figure/, map[str, str] parameters) {
	//println("post: initial_figure: <parameters>");
	return response(generateUpdatedFigure());
}

Response page(post(), /^\/do_callback\/<fun:[a-zA-Z0-9_]+>/, map[str, str] parameters) {
	//println("post: do_callback: <fun>, <parameters>");
	do_callback(fun, parameters);
	return response(generateUpdatedFigure());
}


Response page(post(), /^\/do_callback_str\/<fun:[a-zA-Z0-9_]+>/, map[str, str] parameters) {
	//println("post: do_callback_str: <fun>, <parameters>");
	do_callback_str(fun, parameters);
	return response(generateUpdatedFigure());
}

default Response page(get(), str path, map[str, str] parameters) = response(base + path); 

default Response page(post(), str path, map[str, str] parameters){
	throw "invalid request <path> with <parameters>";
}

default Response page(!get(), str path, map[str, str] parameters) {
  throw "invalid request <path> with <parameters>";
}

// ********************** Examples **********************

str current_title = "";

Figure current_fig = box(fillColor("red"), size(100,200));

loc site = startRenderWebserver();

str generateUpdatedFigure(){ 
	//println("generateUpdatedFigure: <site>, <current_fig>");
	s = trJson(current_fig, site);
	//println(s);
	return s;
}

void generateInitialFigure(str title, Figure f){
	//stopRenderWebserver(site);
	//site = startRenderWebserver();
	current_title = title;
	current_fig = f;
	//println("generateInitialFigure: <site>, <current_fig>");
	s = fig2html(title, f, site=site);
	htmlDisplay(|file:///tmp/<title>.html|, s);
}

// ********************** Examples **********************

void ex(str title, Figure f){ generateInitialFigure(title, f); }

void ex1(){
	int counter = 0;
	str getCounter() = "... <counter>...";
	ex("ex1", vcat([ box(text("Click me"), onClick((){ counter += 1; }), fontSize(20), gap(2,2), fillColor("whitesmoke")),
					 text(getCounter, size(150, 50), fontSize(30))
				   ], align(left(),vcenter())));
}

void ex2(){
	str color = "red";
	str getFillColor() { return color; }
	ex("ex2", hcat([ text("Enter:", size(150, 50), fontSize(18) ), textfield((str s){ color = s; }, size(100,25)), box(fillColor(getFillColor), size(100,100))
				   ], gap(10,10)));
}

