module experiments::Compiler::Examples::Tst1


import util::Webserver;
import IO;

loc base = |courses:///|;

loc startTutor() {
  loc site = |http://localhost:8081|;
  
  while (true) {
    try {
      serve(site, page);
      return site;
    }  
    catch IO("Address already in use"): {
      site.port += 1; 
    }
  }
}

void stopTutor(loc site) {
  shutdown(site);
}

Response page(Request r: get(/xxx/))   { println(r); return  response("xxx"); }
default Response page(value r)         { println(r); return  response(base + "favicon.ico"); }

value domain() { site = startTutor(); println(site); return true; }

