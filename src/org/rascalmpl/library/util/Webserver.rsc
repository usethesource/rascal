
@synopsis{Embed a simple HTTP server inside a Rascal program.}
@description{
`util::Webserver` lets a Rascal program listen for HTTP requests and respond to them.
The server dispatches every incoming request to a user-supplied callback function
that maps a `Request` to a `Response` (both defined in ((Content))).

Use `serve` to start listening and `shutdown` to stop the server.
}
module util::Webserver

extend Content;

@synopsis{Start an HTTP server at the given address.}
@description{
Registers `callback` as the request handler for the server at `server` and begins accepting
connections. By default the server thread is a daemon thread, meaning it will not prevent the
JVM from exiting when the main thread finishes. Pass `asDaemon = false` to keep the process
alive until `shutdown` is called.
}
@javaClass{org.rascalmpl.library.util.Webserver}
java void serve(loc server, Response (Request) callback, bool asDaemon = true);

@synopsis{Stop the HTTP server running at the given address.}
@description{
Stops the server previously started with `serve` at `server` and releases the port.
}
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
