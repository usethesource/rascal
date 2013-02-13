@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascal::\syntax::tests::ParsingRegressionTests

import util::Reflective;
import IO;
import util::FileSystem;
import Exception;
import String;
import ParseTree;
import Ambiguity;

public bool hasAmb(Tree x) = /a:amb(_) := x;

public bool testModule(loc f, list[loc] path) {
  println(f);
  try {
    t = parseModule(f, path);
    if (hasAmb(t)) {
      println("Ambiguity found while parsing: <f>");
      println(diagnose(t));
    }
    else {
      return true;
    }
  }
  catch value e : println("Parsing failed for <f>: <e>");   
  
  return false;
}

public test bool StandardLibrary() = (true | testModule(f, []) && it | /file(f) <- crawl(|std:///|), endsWith(f.path, ".rsc"));

public test bool testTutor() = (true | testModule(f, [|tutor:///|]) && it | /file(f) <- crawl(|tutor:///|), endsWith(f.path, ".rsc"));


