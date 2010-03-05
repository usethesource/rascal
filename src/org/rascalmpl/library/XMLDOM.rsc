module XMLDOM

data Document = document(Content root)
 		      | document(str version, Content root)
              | document(str version, str encoding, Content root)
              | document(str version, bool standalone, Content root)
              | document(str version, str encoding, bool standalone, Content root);
             
alias Attributes = set[Attribute];

data Attribute = attr(str name, str text)
                | attr(Namespace namespace, str name, str text);

data Namespace = namespace(str prefix, str uri);

data Content = element(str name, Attributes attributes, list[Content] contents)
             | element(Namespace namespace, str name, Attributes attributes, list[Content] contents)
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
