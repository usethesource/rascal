@license{
   Copyright (c) 2009-2015 CWI
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

