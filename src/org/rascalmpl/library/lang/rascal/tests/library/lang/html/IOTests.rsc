module lang::rascal::tests::library::lang::html::IOTests

import lang::html::IO;

private loc exampleHTML = |std:///lang/html/examples/example.html|;

test bool simpleParseTest() 
    = html(_) := readHTMLFile(exampleHTML);

test bool partialDocumentsAreCompletedOnRead() 
    = html([head(_),body(_)]) := readHTMLString("\<p\>paragraaf\</p\>");

test bool partialDocumentsAreCompletedOnWrite() 
    = /html/ := writeHTMLString(p([text("paragraaf")]));
