module util::Webserver

extend Content;
    
@javaClass{org.rascalmpl.library.util.Webserver}
@reflect{To get access to the data types}
java void serve(loc server, Response (Request) callback, bool asDaemon = true);

@javaClass{org.rascalmpl.library.util.Webserver}
java void shutdown(loc server);

