/*****************************/
/* DEPRECATED                */
/* Use lang::html::IO      */
/* DO NOT EDIT               */
/*****************************/


@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}

@deprecated{Use "import lang::html::IO;" instead.}
module HTMLIO


@javaClass{org.rascalmpl.library.lang.html.IO}
@reflect
public java node readHTMLFile(loc file);
