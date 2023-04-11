module lang::xml::IO

@javaClass{org.rascalmpl.library.lang.xml.IO}
java value readXML(loc file, bool fullyQualify=false, bool trackOrigins = false, bool includeEndTags=false, bool ignoreComments=true, bool ignoreWhitespace=true, str charset="UTF-8", bool inferCharset=!(charset?));

@javaClass{org.rascalmpl.library.lang.xml.IO}
java value readXML(str contents, loc src = |unknown:///|, bool fullyQualify=false, bool trackOrigins = false, bool includeEndTags=false, bool ignoreComments=true, bool ignoreWhitespace=true);

@javaClass{org.rascalmpl.library.lang.xml.IO}
@synopsis{Pretty-print any value as an XML string}
@description{
This function uses [JSoup's](http://www.jsoup.org) DOM functionality to 
yield a syntactically correct XML string.
}
java str writeXMLString(value val, str charset="UTF-8", bool outline=false, bool prettyPrint=true, int indentAmount=4, int maxPaddingWidth=30);

@synopsis{Pretty-print any value to an XML file}
@description{
This function uses [JSoup's](http://www.jsoup.org) DOM functionality to 
yield a syntactically correct (X)HTML file.
}
@javaClass{org.rascalmpl.library.lang.xml.IO}
java void writeXMLFile(loc file, value val, str charset="UTF-8", bool outline=false, bool prettyPrint=true, int indentAmount=4, int maxPaddingWidth=30);

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
