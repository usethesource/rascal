
@synopsis{Content provides access to the content server of the Rascal terminal for viewing interactive HTML output.}
module Content

extend lang::json::IO;
extend lang::html::IO;
extend lang::xml::IO;
import IO;

@synopsis{Content wraps the HTTP Request/Response API to support interactive visualization types
on the terminal.}
@description{
Values wrapped in a `Content` wrapper will be displayed by interactive
Rascal applications such as the IDE, the REPL terminal and the documentation pages. 

```rascal-prepare
import Content;
```

For example, a piece of html can be displayed directly like such:
```rascal-shell,continue
html("\<a href=\"http://www.rascal-mpl.org\"\>Rascal homepage\</a\>")
```

In its most general form, `Content` is an HTTP(s) webserver callback, such that you might deliver
any kind of content, based on any kind of request. If you produce a `Content` value
which processes requests dynamically, subsequent interaction from the web browser will be 
processed as well. So using the `Content` wrapper you can start an interactive user experience
in the browser directly from the REPL. 

Content values stay plugged into the application server that is hidden in the REPL 
environment until they have not been used for at least 30 minutes. If you want the same
interaction back after 30 minutes of non-usage, you have to produce another Content value 
on the commandline. 

When you are happy with the interaction, or you want a permanent visualization which is not
garbage collected after 30 minutes, you can consider wrapping the same callback in
a webserver using the ((util::Webserver::serve)) function.
}
data Content 
  = content(str id, Response (Request) callback, str title = id, ViewColumn viewColumn = normalViewColumn(1))
  | content(Response response, str title="*static content*", ViewColumn viewColumn = normalViewColumn(1))
  ;

@synopsis{A static map with default MIME interpretations for particular file extensions.}  
public map[str extension, str mimeType] mimeTypes = (
        "json" :"application/json",
        "css" : "text/css",
        "htm" : "text/html",
        "html" : "text/html",
        "xml" : "text/xml",
        "java" : "text/x-java-source, text/java",
        "txt" : "text/plain",
        "asc" : "text/plain",
        "ico" : "image/x-icon",
        "gif" : "image/gif",
        "jpg" : "image/jpeg",
        "jpeg" : "image/jpeg",
        "png" : "image/png",
        "mp3" : "audio/mpeg",
        "m3u" : "audio/mpeg-url",
        "mp4" : "video/mp4",
        "ogv" : "video/ogg",
        "flv" : "video/x-flv",
        "mov" : "video/quicktime",
        "swf" : "application/x-shockwave-flash",
        "js" : "application/javascript",
        "pdf" : "application/pdf",
        "doc" : "application/msword",
        "ogg" : "application/x-ogg",
        "zip" : "application/octet-stream",
        "exe" : "application/octet-stream",
        "class" : "application/octet-stream"
   );  

@synopsis{Directly serve a static html page}
Content html(str html) = content(response(html));

@synopsis{Directly serve the contents of a file}
Content file(loc src) = content(response(src));

@synopsis{Directly serve the contents of a string as plain text}
Content plainText(str text) = content(plain(text));

@synopsis{Request values represent what a browser is asking for, most importantly the URL path.}
@description{
A request value also contains the full HTTP headers, the URL parameters as a `map[str,str]`
and possibly uploaded content, also coded as a map[str,str]. From the constructor type,
`put` or `get` you can see what kind of HTTP request it was.
}
@pitfalls{
* Note that `put` and `post` have not been implemented yet in the REPL server.
}
data Request (map[str, str] headers = (), map[str, str] parameters = ())
  = get (str path)
  | put (str path, Body content)
  | post(str path, Body content)
  | delete(str path)
  | head(str path)
  ;

@synopsis{A response encodes what is send back from the server to the browser client.}
@description{
The three kinds of responses, encode either content that is already a `str`,
some file which is streamed directly from its source location or a jsonResponse
which involves a handy, automatic, encoding of Rascal values into json values.
}                                            
data Response = response(Status status, str mimeType, map[str, str] header, Body body);

@synopsis{Bodies can be sent or received, depending on the context (client or)}
@description{
* put and post requests, when received by a server, receive bodies.
* put and post requests, when fetched by a client, sent bodies.
* a response y a server sends a body.
* a response that was fetched by a client receives a body.

The ((BodyKind)) encodes what we expect from the sender when it 
puts the value onto the socket, and what we expect from the receiver
when it reads the contents off the socket. This is where builtin
conversions (formatters, parsers and validators) are activated on
the bridge between Rascal and the HTTP protocol.
}
data Body
  = send(BodyKind kind, value source)
  | receive(&T (BodyKind kind, type[&T] expect) receiver)
  ;

@synopsis{The type's of ((Body)) that we are sending or expecting to receive}
@description{
This interface bridges Rascal data to the HTTP protocol. Typically large input 
such as (composite) strings and JSON code is _streamed_ onto the HTTP socket.
}
data BodyKind
  = text()
  | json(JSONOptions options=jsonOptions())
  | html()
  | xml()
  | file(loc storage=|unknown:///|)
  ;

data JSONOptions
  = jsonOptions(
      str dateTimeFormat = "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'", 
      JSONFormatter[value] formatter = str (value _) { fail; },
      bool explicitConstructorNames=false, 
      bool explicitDataTypes=false, 
      bool dateTimeAsInt=true, 
      bool rationalsAsString=false
  );

@synopsis{Convenience function for construction a JSON response value}
Response jsonResponse(Status status, map[str,str] header, value val, JSONOptions options = jsonOptions())
  = response(status, "application/json", header, send(json(options=options), val));

@synopsis{Convenience function for construction a text response value}
Response response(Status status, str mimeType, map[str,str] header, str content)
  = response(status, mimeType, header, send(text(), content));

@synopsis{Convenience function for file response value}
Response fileResponse(loc source, str mimeType, map[str,str] header)
  = exists(source) 
    ? response(ok(), mimeType, header, send(file(), source))
    : response(notFound(), "text/plain", (), send(text(), "<file> not found."))
  ;

@synopsis{Convenience function for construction a file response value with automatic mimetype}
Response fileResponse(loc source, map[str,str] header)
  = exists(source) 
    ? response(ok(), mimeTypes[source.extension]?"text/plain", header, send(file(), source))
    : response(notFound(), "text/plain", (), send(text(), "<source> not found."))
  ;

@synopsis{Utility to quickly render a string as HTML content}  
Response response(str content, map[str,str] header = ()) = response(ok(), "text/html", header, send(text(), content));

@synopsis{Utility to quickly report an HTTP error with a user-defined message}
Response response(Status status, str explanation, map[str,str] header = ()) = response(status, "text/plain", header, explanation);

@synopsis{Utility to quickly make a plaintext response.}
Response plain(str text) = response(ok(), "text/plain", (), text);

@synopsis{Utility to serve a file from any source location.}
Response response(loc f, map[str,str] header = ()) = fileResponse(f, mimeTypes[f.extension]?"text/plain", header);

@synopsis{Utility to quickly serve any rascal value as a json text.}
@benefits{
This comes in handy for asynchronous HTTP requests from Javascript clients. Rascal Values are
fully transformed to their respective JSON serialized form before being communicated over HTTP.
}
default  Response response(value val, map[str,str] header = ()) = jsonResponse(ok(), header, val);

@synopsis{Utility to quickly serve any rascal value as a json text, formatting data-types on-the-fly using a `formatter` function}
@benefits{
Fast way of producing JSON strings for embedded DSLs on the Rascal side.
}
Response response(value val, JSONFormatter[value] formatter, map[str,str] header = ()) = jsonResponse(ok(), header, val, options=jsonOptions(formatter=formatter));

@synopsis{Encoding of HTTP status}  
data Status
    = ok()
    | notFound()
    | accepted()
    | badRequest() 
    | conflict()                    
    | created()
    | expectationFailed()                    
    | forbidden() 
    | found()                    
    | gone()                    
    | internalError()                    
    | lengthRequired()                   
    | methodNotAllowed()                   
    | multiStatus()                   
    | notAcceptible()                   
    | notImplemented()                   
    | notModified() 
    | noContent()                   
    | partialContent()                   
    | payloadTooLarge()                   
    | preconditionFailed()                   
    | rangeNotSatisfiable()                   
    | redirect()                   
    | redirectSeeOther()                   
    | requestTimeout()                   
    | serviceUnavailable()                   
    | switchProtocol()
    | temporaryRedirect()
    | tooManyRequests()
    | unauthorized()
    | unsupportedHTTPVersion()
    | unsupportedMediaType()
    ;


@synopsis{Hint the IDE where to open the next web view or editor}
@description{
The `viewColumn`  decides where in the IDE a web client or editor is opened,
_if_ the current IDE honors this parameter. 

There are _9_ possible
view columns: 1, 2, 3, 4, 5, 6, 7, 8, 9. 

Next to this:
* view column `-1` is converted to the _currently active_ view column before the editor is opened.
* view column `-2` is chosen to be a view column _beside_ (to the right) of the currently active view column.

All other view column integers are ignored and interpreted as `-1`.
}
alias ViewColumn=int;

ViewColumn activeViewColumn() = -1;
ViewColumn besideViewColumn() = -2;
ViewColumn normalViewColumn(int v) = v when v >= 1 && v <= 9;
