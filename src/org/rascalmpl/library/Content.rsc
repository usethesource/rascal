@doc{
.Synopsis
Content provides access to the content server of the Rascal terminal for viewing interactive HTML output.
}
module Content

@doc{
.Synopsis
Content wraps the HTTP Request/Response API to support interactive visualization types
on the terminal <<Rascal.REPL>>.  

.Description

Values wrapped in a `Content` wrapper will be displayed by interactive
Rascal applications such as the IDE, the REPL terminal and the documentation pages. 

For example, a piece of html can be displayed directly like such:
[source,rascal-shell]
html("\<a href=\"http://www.rascal-mpl.org\"\>Rascal homepage\</a\>")
----

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
a webserver using the <<util::Webserver::serve>> function. 
}
data Content 
  = content(str id, Response (Request) callback)
  | content(Response response)
  ;

@doc{
.Synopsis
Directly serve a static html page
}
Content html(str html) = content(response(html));

@doc{
.Synopsis
Directly serve the contents of a file
}
Content file(loc src) = content(response(src));

@doc{
.Synopsis
Directly serve the contents of a string as plain text
}
Content text(str text) = content(plain(text));

alias Body = value (type[value] expected);

@doc{
.Synopsis
Request values represent what a browser is asking for, most importantly the URL path.

.Description

A request value also contains the full HTTP headers, the URL parameters as a `map[str,str]`
and possibly uploaded content, also coded as a map[str,str]. From the constructor type,
`put` or `get` you can see what kind of HTTP request it was. 

.Pitfalls 

* Note that `put` and `post` have not been implemented yet in the REPL server. 
}
data Request (map[str, str] headers = (), map[str, str] parameters = (), map[str,str] uploads = ())
  = get (str path)
  | put (str path, Body content)
  | post(str path, Body content)
  | delete(str path)
  | head(str path)
  ;

@doc{
.Synopsis
A response encodes what is send back from the server to the browser client.

.Description

The three kinds of responses, encode either content that is already a `str`,
some file which is streamed directly from its source location or a jsonResponse
which involves a handy, automatic, encoding of Rascal values into json values.
}                                            
data Response 
  = response(Status status, str mimeType, map[str,str] header, str content)
  | fileResponse(loc file, str mimeType, map[str,str] header)
  | jsonResponse(Status status, map[str,str] header, value val, bool implicitConstructors = true,  bool implicitNodes = true, str dateTimeFormat = "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'")
  ;
  
@doc{
.Synopsis
Utility to quickly render a string as HTML content
}  
Response response(str content)                    = response(ok(), "text/html", (), content);

@doc{
.Synopsis
Utility to quickly report an HTTP error with a user-defined message
}
Response response(Status status, str explanation) = response(status, "text/plain", (), explanation);

@doc{
.Synopsis
Utility to quickly make a plaintext response.
}
Response plain(str text) = response(ok(), "text/plain", (), text);

@doc{
.Synopsis
Utility to serve a file from any source location.
}
Response response(loc f)                          = fileResponse(f, mimeTypes[f.extension]?"text/plain", ());

@doc{
.Synopsis
Utility to quickly serve any rascal value as a json text. This comes in handy for
asynchronous HTTP requests from Javascript.
}
default  Response response(value val, map[str,str] header = ())             = jsonResponse(ok(), header, val);
  
@doc{
.Synopsis
Encoding of HTTP status
}  
data Status 
  = ok() 
  | created() 
  | accepted() 
  | noContent() 
  | partialContent() 
  | redirect() 
  | notModified() 
  | badRequest() 
  | unauthorized() 
  | forbidden() 
  | notFound() 
  | rangeNotSatisfiable() 
  | internalError()
  ; 

@doc{
.Synopsis
A static map with default MIME interpretations for particular file extensions.
}  
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
