@license{
  Copyright (c) 2026 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available a
  http://www.eclipse.org/legal/epl-v10.html
}
module util::Webclient

extend Content;
import Exception;

@javaClass{org.rascalmpl.library.util.Webclient}
@synopsis{Synchronous fetch of a HTTP2 ((Request)), returning a ((Response))}
@benefits{
* Supports all of ((HTTP2, including any header.
* Manages mimetypes and charset encodings internally
* Streams JSON, HTML, XML and file content directly to and from the server; no intermediate copies.
* Mirrors exactly ((util::Webclient))
}
@pitfalls{
* Due to a global Rascal execution engine lock, the request can not be to our own ((util::Webserver)) instances. 
That would result in a deadlock.
}
java Response fetch(Request request);

@synopsis{Short-hand for construction of JSON post bodies}
Response postJSON(loc url, value content, JSONOptions options=jsonOptions()) 
    = fetch(post(url.path, send(json(options=options), content, mimeType="application/json", charset=DEFAULT_CHARSET), host=url[path=""]));

@synopsis{Short-hand for construction of JSON post bodies}
Response putJSON(loc url, value content, JSONOptions options=jsonOptions()) 
    = fetch(put(url.path, send(json(options=options), content,  mimeType="application/json", charset=DEFAULT_CHARSET), host=url[path=""]));

@synopsis{Short-hand for construction of JSON post bodies}
Response putFile(loc url, loc content) 
    = fetch(put(url.path, send(file(), content), host=url[path=""]));

@synopsis{Exemple use of the fetch API to download a HTML page and parse it immediately}
HTMLElement getWebPage(loc url)
    = fetch(get(url.path, host=url[path=""])).body.receiver(html(), #HTMLElement);

@synopsis{Exemple use of the fetch API to call a JSON parametrized webservice and read the response as JSON}
&T callWebService(loc url, value parameters, type[&T] expected)
    = postJSON(url, parameters).body.receiver(json(), expected);
