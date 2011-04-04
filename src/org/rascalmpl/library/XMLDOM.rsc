@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module XMLDOM


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

@javaClass{org.rascalmpl.library.XMLDOM}
@reflect{Uses URI Resolver Registry}
public Node java readXMLDOM(loc file);

@javaClass{org.rascalmpl.library.XMLDOM}
@reflect{Uses URI Resolver Registry}
public Node java readXMLDOMTrim(loc file);

@javaClass{org.rascalmpl.library.XMLDOM}
@reflect{Uses URI Resolver Registry}
public void java writeXMLRaw(loc file, Node x);

@javaClass{org.rascalmpl.library.XMLDOM}
@reflect{Uses URI Resolver Registry}
public void java writeXMLCompact(loc file, Node x);

@javaClass{org.rascalmpl.library.XMLDOM}
@reflect{Uses URI Resolver Registry}
public void java writeXMLPretty(loc file, Node x);
