module experiments::vis2::Examples2

import experiments::vis2::Figure;
import experiments::vis2::Properties;
import experiments::vis2::Translate;
import experiments::vis2::Color;
import util::Math;
import IO;
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

str render(str title, Figure f){
	println("render: <title>");
	current_title = title;
	current_fig = f;
	<w, h> = sizeOf(f);
	s = trPrims(title, w, h, trFig(f, site=site));
	println(s);
	return s;
}

void ex(str title, Figure f){
	println("site = <site>");
	s = render(title, f);
	htmlDisplay(|file:///tmp/<title>.html|, s);
	//stopRenderWebserver(site);
}

void ex1(){
	int counter = 0;
	str getCounter() = "... <counter>...";
	ex("ex1", vcat([ button("Click me", (){ counter += 1; }, size(80,50), fontSize(20), fillColor("yellow")),
					 text(getCounter, size(150, 50), fontSize(30))
				   ], align(left(),vcenter())));
}

void ex2(){
	str color = "red";
	str getFillColor() { return color; }
	ex("ex2", hcat([ text("Enter:", size(150, 50), fontSize(18) ), textfield((str s){ color = s; }, size(100,50)), box(fillColor(getFillColor), size(100,100))
				   ], gap(50,50)));
}

