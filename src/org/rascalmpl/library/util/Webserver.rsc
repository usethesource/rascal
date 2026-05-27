
module util::Webserver

extend Content;
    
@javaClass{org.rascalmpl.library.util.Webserver}
java void serve(loc server, Response (Request) callback, bool asDaemon = true);

@javaClass{org.rascalmpl.library.util.Webserver}
java void shutdown(loc server);

test bool testWebServer() {
   loc testLoc = |http://localhost:10001|;
   
  Response testServer(post("/post", Body b)) 
    = response("posted: <b.receiver(json(), #node)>");   

  Response testServer(put("/put", Body b)) 
    = response("put: <b.receiver(json(), #node)>");   

  Response testServer(g:get("/get")) 
    = response("get: <g.headers>");   
   
   try {
      serve(testLoc, testServer);
      return true;
   }
   catch value exception:
     throw exception;
   finally {
    ;
    //  shutdown(testLoc);
   }
}
