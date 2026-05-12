module util::Webclient

extend Content;

data BodyExpectation
    = textBody()
    | jsonBody(type[value] expected)
    | fileBody(loc storage)
    ;

@synopsis{Extends ((Content-Status)) with everything HTTP has out there in the wild.}
data Status
    = ok()
    | notFound()
    | accepted()
    | badRequest() 
    | conflict()                    
    | create()
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
    | rangeNotSatisfieable()                   
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

data Request(
    loc host = |http://www.example.com|, 
    str \content-type = "text/plain",
    BodyExpectation body = text()
    );

@synopsis{Short-hand for construction of JSON post bodies}
Request jsonPost(str path, &T content, loc host=|http://www.example.com|) = post(path, &T (type[&T] _expected) { return content; }, host=host);

@javaClass{org.rascalmpl.library.util.Webclient}
java Response fetch(Request request);

// @synopsis{Short-hand for a string post}
// Request post(str path, str body, loc uri = |http://www.example.com|) 
//     = post(uri = uri, path, str (type[str] _) { return body; });
