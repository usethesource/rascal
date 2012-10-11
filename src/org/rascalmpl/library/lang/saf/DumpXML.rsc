module lang::saf::DumpXML

import lang::xml::DOM;
import lang::saf::AST;

public Node saf2xml(fighter(n, ss)) = 
  document(element("fighter", [attribute("name", n)] + [saf2xml(s) | s <- ss ]));

public Node saf2xml(lang::saf::AST::attribute(n, s)) = 
  element("attribute", [attribute("name", n), attribute("strength", "<s>")]); 

public Node saf2xml(behavior(c, m, f)) = 
  element("behavior", [saf2xml(c), saf2xml(m), saf2xml(f)]); 

public Node saf2xml(const(n)) = 
  element("const", [attribute("name", n)]);
  
public Node saf2xml(and(lhs, rhs)) =
  element("and", [saf2xml(lhs), saf2xml(rhs)]);

public Node saf2xml(or(lhs, rhs)) =
  element("or", [saf2xml(lhs), saf2xml(rhs)]);
  
public Node saf2xml(action(n)) = // remove this = and all results are filtered..
  element("action", [attribute("name", n)]);
  
public Node saf2xml(choose(as)) =
  element("choose", [ element("action", [attribute("name", a)]) |  a <- as ]);

