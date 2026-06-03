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

@synopsis{When writing HTML this decides the escaping convention.}
data HTMLEscapeMode
  = baseMode()
  | extendedMode()
  | xhtmlMode()
  ;

@synopsis{When writing HTML this decides between syntactic differences between generic XML and HTML.}
data HTMLSyntax
  = htmlSyntax()
  | xmlSyntax()
  ;

@synopsis{All HTML read/write options grouped.}
data HTMLOptions
  = htmlOptions(
      HTMLEscapeMode escapeMode = baseMode(), 
      bool outline=false, 
      bool prettyPrint=true, 
      int indentAmount=4, 
      int maxPaddingWidth=30, 
      HTMLSyntax \syntax=htmlSyntax(), 
      bool dropOrigins=true, 
      bool normalise=true,
      bool trackOrigins=false, 
      bool includeEndTags=true
  );

@javaClass{org.rascalmpl.library.lang.html.IO}
@synopsis{Parse a HTML file and return an HTMLElement AST}
@description{
This function uses [JSoup's](http://www.jsoup.org) HTML parser which is robust
against errors in the HTML, and complete in the sense that it supports all of HTML.
}
java HTMLElement readHTMLFile(loc file, loc base=file, bool trackOrigins=false, bool includeEndTags=true);

@synopsis{Just like ((readHTMLFile)) but with grouped `options`.}
HTMLElement readHTMLFile(loc file, loc base=file, HTMLOptions options = htmlOptions())
  = readHTMLFile(file, base=base, trackOrigins=options.trackOrigins, includeEndTags=options.includeEndTags);

@synopsis{Parse a HTML string and return an HTMLElement AST}
@description{
This function uses [JSoup's](http://www.jsoup.org) HTML parser which is robust
against errors in the HTML, and complete in the sense that it supports all of HTML.
}
@javaClass{org.rascalmpl.library.lang.html.IO}
java HTMLElement readHTMLString(str content, loc base=|http://localhost|, bool trackOrigins=false, bool includeEndTags=true, loc src=|unknown:///|);

@synopsis{Just like ((readHTMLString)) but with grouped `options`.}
HTMLElement readHTMLString(str content, loc base=|http://localhost|, loc src = |unknown:///|, HTMLOptions options = htmlOptions())
  = readHTMLString(content, base=base, src=src, trackOrigins=options.trackOrigins, includeEndTags=options.includeEndTags);


@javaClass{org.rascalmpl.library.lang.html.IO}
@synopsis{Pretty-print the HTMLElement AST to a string}
@description{
This function uses [JSoup's](http://www.jsoup.org) DOM functionality to 
yield a syntactically correct (X)HTML string.

* `normalise`: when true arbitrary HTML elements will be nested in a <body> and a <html> wrapper
* `dropOrigins`: any additional `src` origin attributes will not be serialized into the HTML document
* the other options are JSoup options.
}
java str writeHTMLString(HTMLElement dom, str charset="UTF-8", HTMLEscapeMode escapeMode = baseMode(), bool outline=false, bool prettyPrint=true, int indentAmount=4, int maxPaddingWidth=30, HTMLSyntax \syntax=htmlSyntax(), bool dropOrigins=true, bool normalise=true);

str writeHTMLString(HTMLElement dom, str charset="UTF-8", HTMLOptions options= htmlOptions()) 
  = writeHTMLString(dom, charset=charset, escapeMode=options.escapeMode, outline=options.outline, prettyPrint=options.prettyPrint, indentAmount=options.indentAmount, maxPaddingWidth=options.maxPaddingWidth, \syntax=options.\syntax, dropOrigins=options.dropOrigins, normalise=options.normalise);

@synopsis{Pretty-print the HTMLElement AST to a string}
@description{
This function uses [JSoup's](http://www.jsoup.org) DOM functionality to 
yield a syntactically correct (X)HTML file.
}
@javaClass{org.rascalmpl.library.lang.html.IO}
java void writeHTMLFile(loc file, HTMLElement dom, str charset="UTF-8", HTMLEscapeMode escapeMode = baseMode(), bool outline=false, bool prettyPrint=true, int indentAmount=4, int maxPaddingWidth=30, HTMLSyntax \syntax=htmlSyntax(), bool dropOrigins=true, bool normalise=true);

@synopsis{Like ((writeHTMLFile)) but with grouped options}
void writeHTMLFile(loc file, HTMLElement dom, str charset="UTF-8", HTMLOptions options= htmlOptions()) 
  = writeHTMLFile(file, dom, charset=charset, escapeMode=options.escapeMode, outline=options.outline, prettyPrint=options.prettyPrint, indentAmount=options.indentAmount, maxPaddingWidth=options.maxPaddingWidth, \syntax=options.\syntax, dropOrigins=options.dropOrigins, normalise=options.normalise);

