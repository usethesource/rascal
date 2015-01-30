@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl (CWI)}
module lang::jvm::run::RunClassFile

import Exception;

@doc{Register the class file, and its dependencies and run it's main method}
@javaClass{org.rascalmpl.library.lang.jvm.run.RunClassFile}
@reflect{Uses URI Resolver Registry}
public java void runClassFile(loc path,loc dependencies...)
throws PathNotFound(loc), IO(str msg);
