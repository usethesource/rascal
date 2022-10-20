@license{
  Copyright (c) 2009-2022 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@synopsis{Provides read and write functionality for HTMLElements defined in ((lang::html::AST))}
module lang::html::IO

extend lang::html::AST;
import Content;

@javaClass{org.rascalmpl.library.lang.html.IO}
@synopsis{Parse a HTML file and return an HTMLElement AST}
@description{
This function uses Java's EditorKit HTML parser which is robust
against errors in the HTML, and complete in the sense that it supports all of HTML.
}
java HTMLElement readHTMLFile(loc file);

@synopsis{Parse a HTML string and return an HTMLElement AST}
@description{
This function uses Java's EditorKit HTML parser which is robust
against errors in the HTML, and complete in the sense that it supports all of HTML.
}
@javaClass{org.rascalmpl.library.lang.html.IO}
java HTMLElement readHTMLString(str content);

@javaClass{org.rascalmpl.library.lang.html.IO}
@synopsis{Pretty-print the HTMLElement AST to a string}
@description{
This function uses jdom's XML DOM functionality to 
yield a syntactically correct XHTML string.
}
java str writeHTMLString(HTMLElement dom);

@synopsis{Pretty-print the HTMLElement AST to a string}
@description{
This function uses jdom's XML DOM functionality to 
yield a syntactically correct XHTML file.
}
@javaClass{org.rascalmpl.library.lang.html.IO}
java void writeHTMLFile(loc file, HTMLElement dom);

@synopsis{Convenience function to visualize an HTMLElement tree in the browser}
Content serve(HTMLElement elem) = html(writeHTMLString(elem));