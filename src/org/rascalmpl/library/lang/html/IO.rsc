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

data HTMLEscapeMode
  = baseMode()
  | extendedMode()
  | xhtmlMode()
  ;

data HTMLSyntax
  = htmlSyntax()
  | xmlSyntax()
  ;

@javaClass{org.rascalmpl.library.lang.html.IO}
@synopsis{Parse a HTML file and return an HTMLElement AST}
@description{
This function uses [JSoup's](http://www.jsoup.org) HTML parser which is robust
against errors in the HTML, and complete in the sense that it supports all of HTML.
}
java HTMLElement readHTMLFile(loc file, loc base=file, bool trackOrigins=false, bool includeEndTags=true);

@synopsis{Parse a HTML string and return an HTMLElement AST}
@description{
This function uses [JSoup's](http://www.jsoup.org) HTML parser which is robust
against errors in the HTML, and complete in the sense that it supports all of HTML.
}
@javaClass{org.rascalmpl.library.lang.html.IO}
java HTMLElement readHTMLString(str content, loc base=|http://localhost|, bool trackOrigins=false, bool includeEndTags=true, loc src=|unknown:///|);

@javaClass{org.rascalmpl.library.lang.html.IO}
@synopsis{Pretty-print the HTMLElement AST to a string}
@description{
This function uses [JSoup's](http://www.jsoup.org) DOM functionality to 
yield a syntactically correct (X)HTML string.
}
java str writeHTMLString(HTMLElement dom, str charset="UTF-8", HTMLEscapeMode escapeMode = baseMode(), bool outline=false, bool prettyPrint=true, int indentAmount=4, int maxPaddingWidth=30, HTMLSyntax \syntax=htmlSyntax());

@synopsis{Pretty-print the HTMLElement AST to a string}
@description{
This function uses [JSoup's](http://www.jsoup.org) DOM functionality to 
yield a syntactically correct (X)HTML file.
}
@javaClass{org.rascalmpl.library.lang.html.IO}
java void writeHTMLFile(loc file, HTMLElement dom, str charset="UTF-8", HTMLEscapeMode escapeMode = baseMode(), bool outline=false, bool prettyPrint=true, int indentAmount=4, int maxPaddingWidth=30, HTMLSyntax \syntax=htmlSyntax());

@synopsis{Convenience function to visualize an HTMLElement tree in the browser}
Content serve(HTMLElement elem) = html(writeHTMLString(elem));