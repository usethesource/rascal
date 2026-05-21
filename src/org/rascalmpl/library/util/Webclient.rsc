module util::Webclient

extend Content;
import Exception;

private str DEFAULT_CHARSET = "UTF-8";

@synopsis{From the client side, a host name is required and also an expected content-type}
@description{
* `host` is the essential information, the URL to fetch input from. The path will be retrieved from the `path` parameter of `get`, `put`, `post` and `head`
* `content-type` is relevant for POST and PUT options where content is uploaded to the server. The mimetype will be set to this value.
* `charset` is also relevant for POST and PUT options, the outgoing stream will be encoded accordingly and the headers will contained the right meta-data.
}
data Request(loc host = hostIsRequired(), str \content-type = "text/plain", str charset=DEFAULT_CHARSET);

@javaClass{org.rascalmpl.library.util.Webclient}
java Response fetch(Request request);

@synopsis{Short-hand for construction of JSON post bodies}
Request jsonPost(str path, value content, loc host=hostIsRequired(), JSONOptions options=jsonOptions()) 
    = post(path, send(json(options=options), content), host=host, \content-type="application/json");

@synopsis{Short-hand for construction of JSON post bodies}
Request jsonPut(str path, value content, loc host=hostIsRequired(), JSONOptions options=jsonOptions()) 
    = put(path, send(json(options=options), content), host=host, \content-type="application/json");

@synopsis{Short-hand for construction of JSON post bodies}
Request filePut(str path, loc content, loc host=hostIsRequired()) 
    = put(path, send(file(), content), host=host, \content-type="application/json");


private loc hostIsRequired() throws IllegalArgument { 
    throw IllegalArgument("missing host parameter");
}
