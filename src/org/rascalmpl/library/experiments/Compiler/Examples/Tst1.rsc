module experiments::Compiler::Examples::Tst1


import util::Webserver;

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

Response page(get(/.*/, Body _))   = response(base + "index.html");

value main() { startTutor(); return true; }

