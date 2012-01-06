@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}

module util::SystemAPI

import Exception;
import String;

@doc{Api to the System for example System Variables or System Commands.}

@doc{For using the char backquote in Rascal Programs without interpretation.}

public str backquote = "`";

@javaClass{org.rascalmpl.library.util.SystemAPI}
public java str getSystemProperty(str property);

/*
@doc{Returns content of location <loc> which is assumed a rascal program. The backquotes
will be replaced by stringquotes. And the stringquotes inside them will be escaped.}
@javaClass{org.rascalmpl.library.util.SystemAPI}
public java str getRascalFileContent(loc g) throws 
              UnsupportedScheme(loc file), PathNotFound(loc file), IOError(str msg);
*/
