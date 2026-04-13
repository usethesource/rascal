
module util::Webserver

extend Content;
    
@javaClass{org.rascalmpl.library.util.Webserver}
java void serve(loc server, Response (Request) callback, bool asDaemon = true);

@javaClass{org.rascalmpl.library.util.Webserver}
java void shutdown(loc server);

test bool testWebServer() {
   loc testLoc = |http://localhost:10001|;
   
   // simple get
   // Response testServer(get("/hello")) = response("hello world!");
   Response testServer(p:post("/upload8", value (type[value] _) stuff)) = response("uploaded: <p.parameters["firstname"]> <stuff(#value)>");   
   
   try {
      serve(testLoc, testServer);
      return true;
   }
   catch value exception:
     throw exception;
   finally {
     shutdown(testLoc);
   }
}
