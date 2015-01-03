@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jeroen van den Bos - Jeroen.van.den.Bos@cwi.nl (CWI)}
@contributor{Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl (CWI)}
module lang::jvm::transform::SerializeClass

import Exception;

import lang::jvm::ast::Level0;

@javaClass{org.rascalmpl.library.lang.jvm.transform.SerializeClass}
@reflect{Uses URI Resolver}
public java void serialize(Class class, loc path)
throws PathNotFound(loc), IO(str msg);

@javaClass{org.rascalmpl.library.lang.jvm.transform.Rascalify}
public java void deserializeToDisk(loc source, loc destination, str moduleName)
throws PathNotFound(loc), IO(str msg);
