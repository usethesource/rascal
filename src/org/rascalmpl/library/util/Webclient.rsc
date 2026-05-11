module util::Webclient

extend Content;

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

data Request(loc uri = |http://www.example.com|, str \content-type = "text/plain");

@javaClass{org.rascalmpl.library.util.Webclient}
java Response fetch(Request request);
