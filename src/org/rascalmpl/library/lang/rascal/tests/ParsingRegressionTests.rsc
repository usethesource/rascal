module lang::rascal::tests::ParsingRegressionTests

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
    if (hasAmb(removeConcreteSyntax(parseFullModule(readFile(f), f)))) {
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

public bool testStandardLibrary() = (true | testModule(f) && it | /file(f) <- crawl(|std:///|), endsWith(f.path, ".rsc"));

public bool testTutor() = (true | testModule(f) && it | /file(f) <- crawl(|tutor:///|), endsWith(f.path, ".rsc"));

public bool testEclipseLibrary() = (true | testModule(f) && it | /file(f) <- crawl(|eclipse-std:///|), endsWith(f.path, ".rsc"));