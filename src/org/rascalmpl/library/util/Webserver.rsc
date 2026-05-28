
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

@synopsis{Starts a test server that does not need the Rascal execution lock when handling a request.}
@description{
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
java void startTestEchoServer(int port=10001);
