@synopsis{Basic IO for XML to Rascal and  back}
@description{
The XML binding implemented by this module is _untyped_. The readers produce
values of type `node` for every (nested) tag. 

To bind the resulting values to more strictly typed ADTs, use ((util::Validate)).
}
module lang::xml::IO

import util::Maybe;

@javaClass{org.rascalmpl.library.lang.xml.IO}
java value readXML(loc file, bool fullyQualify=false, bool trackOrigins = false, bool includeEndTags=false, bool ignoreComments=true, bool ignoreWhitespace=true, str charset="UTF-8", bool inferCharset=!(charset?));

@javaClass{org.rascalmpl.library.lang.xml.IO}
@synopsis{Stream all the tags in a file, one-by-one, without ever having the entire XML file in memory.}
@description{
((streamXML)) returns a closure function. When you call it repeatedly, it will produce a single value `just(...)` for each
occurrence of `elementName` tags in the input. The final call will produce `nothing()`, so you know when to stop.

`IO` exceptions can still be thrown even when you are already streaming. This means an entire file has dissappeared,
or permissions were revoked during the execution of the stream. Only when you receive `nothing()` it is indicated
that the `elementName` tag is not further present in the file.
}
java Maybe[value]() streamXML(loc file, str elementName, bool fullyQualify=false, bool trackOrigins = false, bool includeEndTags=false, bool ignoreComments=true, bool ignoreWhitespace=true, str charset="UTF-8", bool inferCharset=!(charset?));

@javaClass{org.rascalmpl.library.lang.xml.IO}
java value readXML(str contents, loc src = |unknown:///|, bool fullyQualify=false, bool trackOrigins = false, bool includeEndTags=false, bool ignoreComments=true, bool ignoreWhitespace=true);

@javaClass{org.rascalmpl.library.lang.xml.IO}
@synopsis{Pretty-print any value as an XML string}
@description{
This function uses [JSoup's](http://www.jsoup.org) DOM functionality to 
yield a syntactically correct XML string.
}
java str writeXMLString(value val, str charset="UTF-8", bool outline=false, bool prettyPrint=true, int indentAmount=4, int maxPaddingWidth=30, bool dropOrigins=true);

@synopsis{Pretty-print any value to an XML file}
@description{
This function uses [JSoup's](http://www.jsoup.org) DOM functionality to 
yield a syntactically correct (X)HTML file.
}
@javaClass{org.rascalmpl.library.lang.xml.IO}
java void writeXMLFile(loc file, value val, str charset="UTF-8", bool outline=false, bool prettyPrint=true, int indentAmount=4, int maxPaddingWidth=30, bool dropOrigins=true);

test bool nestedElementTest() {
  example = "\<aap\>\<noot\>mies\</noot\>\</aap\>";
  
  val = readXML(example);
  
  return val == "aap"("noot"("mies"));
}
  
test bool attributeTest() {
  example = "\<aap age=\"1\"\>\</aap\>";
  
  val = readXML(example);
  
  return val == "aap"(age="1");
}

test bool namespaceTest() {
  example = "\<aap xmlns:ns=\"http://trivial\" ns:age=\"1\" age=\"2\"\>\</aap\>";
  
  val = readXML(example);
  
  return "aap"(\ns-age="1", age="2") := val;
}
