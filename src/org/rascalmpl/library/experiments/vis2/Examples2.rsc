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
      serve(site, dispatchserver(page));
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

Response page(post(), /^\/$/                         , map[str,str] _)   = response(render(current_title, current_fig));
Response page(get(), /^\/$/                          , map[str,str] _)   = response(render(current_title, current_fig));

Response page(post(), /^\/do_callback\/<fun:[a-zA-Z0-9_]+>/, map[str, str]ps) {
	println("do_callback: <fun>, <ps>");
	do_callback(fun, ps);
	return response(render(current_title, current_fig));
}

Response page(post(), /^\/do_callback_str\/<fun:[a-zA-Z0-9_]+>/, map[str, str]ps) {
	println("do_callback_str: <fun>, <ps>");
	do_callback_str(fun, ps);
	return response(render(current_title, current_fig));
}

Response page(get(), /^\/do_callback_str\/<fun:[a-zA-Z0-9_]+>/, map[str, str]ps) {
	println("do_callback_str: <fun>, <ps>");
	do_callback_str(fun, ps);
	return response(render(current_title, current_fig));
}

default Response page(get(), str path, map[str, str] ps) = response(base + path); 

default Response page(put(), str path, map[str, str] ps){
	throw "invalid <path> with <ps>";
}

default Response page(!get(), str path, map[str, str] ps) {
  throw "invalid <path> with <ps>";
}

// ********************** Examples **********************

str current_title;
Figure current_fig = box(fillColor("red"), size(100,200));
loc site = startRenderWebserver();

void render(str title, Figure f){
	current_title = title;
	current_fig = f;
	println("site = <site>");
	println("f = <f>");
	s = fig2html(title, f);
	//println(s);
	htmlDisplay(|file:///tmp/<title>.html|, s);
}

void ex(str title, Figure f){ render(title, f); }

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

