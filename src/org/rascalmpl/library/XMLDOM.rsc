module XMLDOM

data Document = document(XMLDecl xml, Content.element root)
			  | document(Content.element root);


data XMLDecl = xml(str version)
             | xml(str version, str encoding)
             | xml(str version, bool standalone)
             | xml(str version, str encoding, bool standalone);
             

data Content = element(str name, map[str, value] attrs, list[Content] contents)
			 | charData(str text)
			 | cdata(str text)
			 | comment(str text)
			 | pi(str name, str text)
			 | entityRef(str name)
			 | charRef(int code);             

@javaClass{org.rascalmpl.library.XMLDOM}
public Document java readXMLDOM(loc file);