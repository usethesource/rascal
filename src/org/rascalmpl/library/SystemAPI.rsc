module SystemAPI

import String;

@doc{Api to the System for example System Variables or System Commands.}

@javaClass{org.rascalmpl.library.SystemAPI}
public str java getSystemProperty(str property);

@doc{Returns canonical path to rascal library appended with "/<fileName>".}
@javaClass{org.rascalmpl.library.SystemAPI}
public loc java getLibraryPath(str fileName);