@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascal::syntax::tests::ParsingRegressionTests

import util::Reflective;
import IO;
import util::FileSystem;
import Exception;
import String;
import ParseTree;

public Tree removeConcreteSyntax(Tree m) = innermost visit (m) {
  case appl(prod(     label("ConcreteQuoted",_),_,_),_) => appl(skipped(),[])
  case appl(prod(label("ConcreteTypedQuoted",_),_,_),_) => appl(skipped(),[])
  case amb({t}) => t
};

public bool hasAmb(Tree x) = /a:amb(_) := x;

public bool testModule(loc f) {
  println(f);
  try {
    if (hasAmb(removeConcreteSyntax(parseModule(f)))) {
      println("Ambiguity found while parsing: <f>");
    }
    else {
      return true;
    }
  }
  catch ParseError(_) : println("Parsing failed for: <f>");   
  catch Java("Parse error"): println("Parsing failed for: <f>");  
  catch RuntimeException e : println("Parsing failed for: <f> error:(<e>)"); 
  
  return false;
}

public test bool StandardLibrary() = (true | testModule(f) && it | /file(f) <- crawl(|std:///|), endsWith(f.path, ".rsc"));

public test bool testTutor() = (true | testModule(f) && it | /file(f) <- crawl(|tutor:///|), endsWith(f.path, ".rsc"));

