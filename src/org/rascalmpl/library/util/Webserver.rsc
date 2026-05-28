
@license{
  Copyright (c) 2014-2026 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available a
  http://www.eclipse.org/legal/epl-v10.html
}
module util::Webserver

extend Content;
    
@javaClass{org.rascalmpl.library.util.Webserver}
@synopsis{Starts a webserver}
@description{
* `server` is always localhost but a _unique_ port number is used for the current server.
* `callback` is an implementation of the HTTP2 protocol following the definitions in ((Content)) of ((Request)) and ((Response).)
* `asDeamon` directs what happens when  the main JVM thread terminates. If set to `true` the server will also stop.

The server is run asynchronously and the call to serve returns as soon as the server has commenced listening.
}
@benefits{
* Supports all of HTTP2 including any kind of header
* Manages mimetypes and charset encodings
* Streams JSON, HTML, XML and file content directly to and from the server; no intermediate copies.
}
@pitfalls{
* The `callback` locks and blocks the main Rascal execution thread, so we can not use ((util::Webclient::fetch)) on our own ((serve))rs. That would create a deadlock.
}
java void serve(loc server, Response (Request) callback, bool asDaemon = true);

@javaClass{org.rascalmpl.library.util.Webserver}
@synopsis{Stops a server}
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
