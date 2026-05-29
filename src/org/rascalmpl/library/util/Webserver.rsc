
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
@synopsis{Starts a HTTP1.1 webserver from a handler function}
@description{
* `port` is _unique_ port number is used for the current server.
* `callback` is an implementation of the HTTP2 protocol following the definitions in ((Content)) of ((Request)) and ((Response).)
* `asDeamon` directs what happens when  the main JVM thread terminates. If set to `true` the server will also stop.

The server is run asynchronously and the call to serve returns as soon as the server has commenced listening.
Yhe exactly used port number is returned,
}
@benefits{
* Supports all of HTTP1.1 including any kind of header
* Manages mimetypes and charset encodings
* Streams JSON, HTML, XML and file content directly to and from the server; no intermediate copies.
}
@pitfalls{
* The `callback` locks and blocks the main Rascal execution thread, so we can not use ((util::Webclient::fetch)) on our own ((serve))rs. That would create a deadlock.
}
java int serve(int port, Response (Request) callback, bool asDaemon = true);

@synopsis{Start a server with the port number encoded in the server `loc`}
@deprecated{Use ((serve)) with  the `port` argument, because the hostname was never configurable in the first place.}
void serve(loc server, Response (Request) callback, bool asDaemon = true) {
    serve(server.port, callback, asDaemon=asDaemon);
}

@javaClass{org.rascalmpl.library.util.Webserver}
@synopsis{Stops a server}
java void shutdown(int port);

@synopsis{Stops a server via a localhost URL with a port number}
@deprecated{Use the ((shutdown)) function with the port number directly.}
void shutdown(loc server) = shutdown(server.port);

@synopsis{Starts a test server that does not need the Rascal execution lock when handling a request.}
@description{
This works exactly as startTestWebServerRascal:
* `/get` returns `ok`
* `/post/text` and `/put/text` accept proper textual uploads, parses it to `str`, checks the type and returns is as proper string again.
* `/post/json` and `/put/json` accept any JSON upload, parse it to a `node`, check the type and return it as json again.
* `/post/html` and `/put/html` accept proper HTML uploads, parses it to HTMLElements, checks the type and returns is as proper HTML again.
* `/post/xml` and `/put/xml` accept proper XML uploads, parses it to `node`, checks the type and returns is as proper XML again.
* `/head` returns the headers of the request as text.
}
@benefits{
* can be used to test the ((util::Webclient)) without deadlock
* offers all HTTP2 methods as an echo server to test the internals of the ((serve)) function.
}
@pitfalls{
* only default mimetypes and charsets are used.
}
@javaClass{org.rascalmpl.library.util.Webserver}
java void startEchoServerJava(int port=10001);

@synopsis{Test server for Webserver and Webclient}
void startEchoServerRascal(int port=10002) {
  
  Response testServer(get("/get")) 
    = response("ok");   

  Response testServer(r:head("/head")) 
    = response("<r.headers>");   

  Response testServer(post("/post/text", Body b)) 
    = response(ok(), send(text(), b.receiver(text(), #str)));   

  Response testServer(post("/put/text", Body b)) 
    = response(ok(), send(text(), b.receiver(text(), #str)));   

  Response testServer(post("/post/json", Body b)) 
    = response(ok(), send(json(), b.receiver(json(), #node)));   

  Response testServer(post("/put/json", Body b)) 
    = response(ok(), send(json(), b.receiver(json(), #node)));   

  Response testServer(post("/post/html", Body b)) 
    = response(ok(), send(html(), b.receiver(html(), #HTMLElement)));   

  Response testServer(post("/put/html", Body b)) 
    = response(ok(), send(html(), b.receiver(html(), #HTMLElement)));   

  Response testServer(post("/post/xml", Body b)) 
    = response(ok(), send(xml(), b.receiver(xml(), #node)));   

  Response testServer(post("/put/xml", Body b)) 
    = response(ok(), send(xml(), b.receiver(xml(), #node)));   

  default Response testServer(Request q) 
    = response(notFound(), send(text(), "<q.path> not found"));

  serve(port, testServer);
}