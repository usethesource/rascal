module XMLDOM

data Document = document(Content.element root)
 		      | document(str version, Content.element root)
              | document(str version, str encoding, Content.element root)
              | document(str version, bool standalone, Content.element root)
              | document(str version, str encoding, bool standalone, Content.element root);
             
alias Attributes = map[str, str];

data Content = element(str name, Attributes attributes, list[Content] contents)
			 | charData(str text)
			 | cdata(str text)
			 | comment(str text)
			 | pi(str name, str text)
			 | entityRef(str name)
			 | charRef(int code);             

@javaClass{org.rascalmpl.library.XMLDOM}
public Document java readXMLDOM(loc file);

@javaClass{org.rascalmpl.library.XMLDOM}
public Document java readXMLDOMTrim(loc file);
