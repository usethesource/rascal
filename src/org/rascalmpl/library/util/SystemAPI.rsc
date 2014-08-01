@license{
   Copyright (c) 2009-2014 CWI
   All rights reserved. This program and the accompanying materials
   are made available under the terms of the Eclipse Public License v1.0
   which accompanies this distribution, and is available at
   http://www.eclipse.org/legal/epl-v10.html
 }
 @contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
 @contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}

@doc{API to the System for example System Variables or System Commands.} 
module util::SystemAPI

import Exception;
import String;

@javaClass{org.rascalmpl.library.util.SystemAPI}
public java str getSystemProperty(str property);

@doc{Returns the absolute location of the (relative-) location}
@javaClass{org.rascalmpl.library.util.SystemAPI}
@reflect{Uses URI Resolver Registry}
public java loc resolveLoc(loc location);

/*
@doc{For using the char backquote in Rascal Programs without interpretation.}
public str backquote = "`";

@doc{Returns content of location <loc> which is assumed a rascal program. The backquotes
will be replaced by string quotes. And the string quotes inside them will be escaped.}
@javaClass{org.rascalmpl.library.util.SystemAPI}
public java str getRascalFileContent(loc g) throws 
              UnsupportedScheme(loc file), PathNotFound(loc file), IO(str msg);
*/