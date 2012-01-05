@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}


module lang::xml::DOM


data Namespace = namespace(str prefix, str uri)
               | none();

data Node = document(Node root)
          | attribute(Namespace namespace, str name, str text)
		  | element(Namespace namespace, str name, list[Node] children)
		  | charData(str text)
		  | cdata(str text)
		  | comment(str text)
		  | pi(str target, str text)
		  | entityRef(str name)
		  | charRef(int code);             


public Node attribute(str name, str text) = attribute(none(), name, text);
public Node element(str name, list[Node] kids) = element(none(), name, kids);



@javaClass{org.rascalmpl.library.lang.xml.DOM}
public java Node parseXMLDOM(str src);

@javaClass{org.rascalmpl.library.lang.xml.DOM}
public java Node parseXMLDOMTrim(str src);

@javaClass{org.rascalmpl.library.lang.xml.DOM}
public java str xmlRaw(Node x);

@javaClass{org.rascalmpl.library.lang.xml.DOM}
public java str xmlCompact(Node x);

@javaClass{org.rascalmpl.library.lang.xml.DOM}
public java str xmlPretty(Node x);


// TODO: remove these; use Rascal stdlib for IO
@javaClass{org.rascalmpl.library.lang.xml.DOM}
@reflect{Uses URI Resolver Registry}
public java void writeXMLRaw(loc file, Node x);

@javaClass{org.rascalmpl.library.lang.xml.DOM}
@reflect{Uses URI Resolver Registry}
public java Node readXMLDOM(loc file);

@javaClass{org.rascalmpl.library.lang.xml.DOM}
@reflect{Uses URI Resolver Registry}
public java Node readXMLDOMTrim(loc file);

@javaClass{org.rascalmpl.library.lang.xml.DOM}
@reflect{Uses URI Resolver Registry}
public java void writeXMLCompact(loc file, Node x);

@javaClass{org.rascalmpl.library.lang.xml.DOM}
@reflect{Uses URI Resolver Registry}
public java void writeXMLPretty(loc file, Node x);

