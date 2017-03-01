module util::Webserver

import Exception;
import IO;

alias Body = value (type[value] expected);

data Request (map[str, str] headers = (), map[str, str] parameters = (), map[str,str] uploads = ())
  = get (str path)
  | put (str path, Body content)
  | post(str path, Body content)
  | delete(str path)
  | head(str path)
  ;
                                            
data Response 
  = response(Status status, str mimeType, map[str,str] header, str content)
  | fileResponse(loc file, str mimeType, map[str,str] header)
  | jsonResponse(Status status, map[str,str] header, value val, bool implicitConstructors = true,  bool implicitNodes = true, str dateTimeFormat = "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'")
  ;
  
Response response(str content)                    = response(ok(), "text/html", (), content);
Response response(Status status, str explanation) = response(status, "text/plain", (), explanation);
Response response(loc f)                          = fileResponse(f, mimeTypes[f.extension]?"text/plain", ());
default  Response response(value val)             = jsonResponse(ok(), (), val);
  
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
    
@javaClass{org.rascalmpl.library.util.Webserver}
@reflect{To get access to the data types}
java void serve(loc server, Response (Request) callback);

@javaClass{org.rascalmpl.library.util.Webserver}
java void shutdown(loc server);

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