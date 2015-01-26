@license{
  Copyright (c) 2009-2015 CWI
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
import util::Monitor;

public bool hasAmb(Tree x) = /a:amb(_) := x;

public bool testModules(list[loc] files, list[loc] path) {
  errors = [];
  for (f <- files) {
    event("parsing <f>");
    
    try {
      t = parseModule(f, path);
      if (hasAmb(t)) {
        println("Ambiguity found while parsing: <f>");
        iprintln(diagnose(t));
        errors += [<f,"ambiguous">];
      }
    }
    catch value x: {
      errors += [<f,x>];
    }
  }
  
  if (errors != []) {
    for (<f,e> <- errors) println("failed <f>: <e>");
    return false;
  }
  
  return true;
}


public test bool StandardLibrary() = testModules([f |  /file(f) <- crawl(|std:///|), f.extension == "rsc", /experiments/ !:= f.path], []);

public test bool testTutor() = testModules([f |  /file(f) <- crawl(|tutor:///|), f.extension == "rsc"], [|tutor:///|]);

