module util::Webserver

import Exception;
import IO;

public map[str extension, str mimeType] mimeTypes = (
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
                                          
data Response = response(Status status, str mimeType, map[str,str] header, str content)
              | fileResponse(loc file, str mimeType, map[str,str] header);

data Method = get() | put() | post() | head() | delete();

alias Handler = Response (loc uri, Method method, map[str, str] headers, map[str, str] parameters,  map[str, str] uploads);

Response response(str content) = response(ok(), "text/html", (), content);
Response response(Status status, str explanation) = response(status, "text/plain", (), explanation);
Response response(loc f) = fileResponse(f, mimeTypes[f.extension]?"text/plain", ());

@javaClass{org.rascalmpl.library.util.Webserver}
@reflect{to get access to the data types}
java void serve(loc server, Handler callback); 

Handler fileserver(loc root) = 
  Response (loc uri, Method method, map[str, str] headers, map[str, str] parameters,  map[str, str] uploads) {
    if (method != get()) 
      return response(badRequest(), "GET is the only supported method");
      
    if (/\.\./ := "<uri>")
      return response(forbidden(), ".. is not allowed");
      
    return response(root + uri.path);
  };

Handler dispatchserver(Response (Method, str, map[str,str]) router) =
  Response (loc uri, Method method, map[str, str] headers, map[str, str] ps,  map[str, str] uploads) {
    try {
      return router(method, uri.path, ps);
    } catch value e : {
      return response(notFound(), "<uri.path> failed, due to <e>");
    }
  };
   
Handler functionserver(map[str, str (map[str,str] parameters)] functions) =
  Response (loc uri, Method method, map[str, str] headers, map[str, str] parameters,  map[str, str] uploads) {
    try {
      return response(ok(),  mimeTypes[uri.extension]?"text/html", (), functions[uri.path](parameters));
    }
    catch value x: {
      return response(notFound(), "<uri.path> not found");
    }
  };

@javaClass{org.rascalmpl.library.util.Webserver}
java void shutdown(loc server);
