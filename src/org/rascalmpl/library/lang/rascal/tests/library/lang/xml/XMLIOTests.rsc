module lang::rascal::tests::library::lang::xml::XMLIOTests

import IO;
import lang::xml::IO;

bool checkXMLResult(str input, node expected, bool fullyQualify = false) {
    result = readXML(input, fullyQualify = fullyQualify);
    if (result == expected) {
        return true;
    }
    println("readXML failed");
    println("Expected: ");
    iprintln(expected);
    println("Got: ");
    iprintln(result);
    return false;
}

str buildXML(str name, map[str, str] attrs = (), list[str] children = []) {
    result = "\<<name>";
    for (a <- attrs) {
        result += " <fixColon(a)> = \"<attrs[a]>\"";
    }
    result += "\>";
    for (c <- children) {
        result += c;
    }
    return result + "\</<name>\>";
}

 str fixColon(str attr) = visit(attr) { case /-/ => ":" };
    
test bool simpleXMLTest1() 
    = checkXMLResult(buildXML("xml"), "xml"());

test bool simpleXMLTest2() 
    = checkXMLResult(buildXML("xml", children = [ buildXML("a") ]), "xml"("a"()));

test bool simpleXMLTest3() 
    = checkXMLResult(buildXML("xml", attrs = ("href" : "#42")), "xml"(href="#42"));
    
test bool namespaceIgnored1() 
    = checkXMLResult(buildXML("table", attrs = ("xmlns" : "http://www.w3.org/TR/html4/")), "table"());

test bool namespaceIgnored2() 
    = checkXMLResult(
        buildXML("table", attrs = ("xmlns-ht" : "http://www.w3.org/TR/html4/"), children=[
            buildXML("ht:tr")
        ]), 
        "table"("tr"()));

test bool namespaceIncluded() 
    = checkXMLResult(
        buildXML("table", attrs = ("xmlns-ht" : "http://www.w3.org/TR/html4/"), children=[
            buildXML("ht-tr")
        ]), 
        "table"("ht-tr"(), xmlns = ("ht": "http://www.w3.org/TR/html4/")), fullyQualify = true);

test bool namespacesMultiple() 
    = checkXMLResult(
        buildXML("table", attrs = ("xmlns-ht" : "http://www.w3.org/TR/html4/"), children=[
                buildXML("ht-tr"),
                buildXML("tr", attrs = ("xmlns-ht2": "http://www.w3.org/TR/html5/"))
            ]), 
            "table"(
                    "ht-tr"(),
                    "tr"(xmlns = ("ht2": "http://www.w3.org/TR/html5/"))
                , xmlns = ("ht": "http://www.w3.org/TR/html4/")
            )
        , fullyQualify = true);
