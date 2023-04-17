module lang::rascal::tests::library::lang::html::IOTests

import lang::html::IO;
import IO;

private loc exampleHTML = |std:///lang/html/examples/example.html|;

test bool simpleParseTest() 
    = html(_) := readHTMLFile(exampleHTML);

test bool partialDocumentsAreCompletedOnRead() 
    = html([head(_),body(_)]) := readHTMLString("\<p\>paragraaf\</p\>");

test bool partialDocumentsAreCompletedOnWrite() 
    = /html/ := writeHTMLString(p([text("paragraaf")]));

test bool originTrackingElements() {
    return originTracking(readHTMLFile(exampleHTML, trackOrigins=true), readFile(exampleHTML));
}

test bool originTrackingElementsWithEndTags() {
    return originTracking(readHTMLFile(exampleHTML, trackOrigins=true, includeEndTags=true), readFile(exampleHTML));
}

private bool originTracking(node example, str content) {
   poss = [x.location | /HTMLElement x := example, !(x is text), !(x is \data)]; // every node has a .src field, otherwise this fails with an explicitTemplateSpecialization

   for (loc p <- poss, p.offset?) { // some (top) nodes do not have offsets
      assert content[p.offset] == "\<";                // all nodes start with a opening tag <
      assert content[p.offset + p.length - 1] == "\>"; // all nodes end with a closing tag >
   }

   return true;
}