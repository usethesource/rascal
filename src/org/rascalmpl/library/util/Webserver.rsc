module util::Webserver

import Exception;
import IO;

data Request(map[str, str] headers = (), map[str, str] parameters = (), map[str,str] uploads = ())
  = get (loc uri)
  | put (loc uri, value (type[value] expected) content)
  | post(loc uri, value (type[value] expected) content)
  | delete(loc uri)
  | head(loc uri)
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
    
data MyServerRequestData 
    = optionA(A a)
    | optionB(B b)
    ;    
    
data A = a();
data B = b();    
    
@javaClass{org.rascalmpl.library.util.Webserver}
@reflect{to get access to the data types}
java void serve(loc server, type[&T] requestType, Response (Request[&T]) callback);

void serve(server, Response (Request[str]) callback) = serve(server, #str, callback);

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
   

data Method 
    = get()
    | post()
    | put()
    ;

data Person = person(str name, str address, int age);
data Company = company(str name, int employees);
data Result = result(int money, str message);

Method getMethod(post(loc _, value (type[value] _) _)) = post();
Method getMethod(get(loc _)) = get();

alias Content = value (type[value]);

Response myServer(post(loc uri, Content content)) = response("response" (warning="Watch out <x.name>!", ageInsult="<x.age> is really old..."))
  when /person/ := uri.path,
       Person x := content(#Person);

Response myServer(post(loc uri, Content content)) = response(result(1000 * x.employees, "veel he?"))
  when /company/ := uri.path,
       Company x := content(#Company);

Response myServer(get(loc uri)) = response("\<html\>\<body\>hoi\</body\>\</html\>") when |request:///index.html| == uri;
  
default Response myServer(Request r) =  response(notFound(), "dunno <r.uri>");
  
void main() {
  serve(|http://localhost:9001|, myServer);
}