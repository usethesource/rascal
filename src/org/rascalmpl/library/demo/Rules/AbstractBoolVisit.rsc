@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module demo::Rules::AbstractBoolVisit

// A definition of Booleans and the operators band and bor using a visit function.
// See AbstractBool for a definition using rewrite rules.

data Bool = btrue();
data Bool = bfalse();
data Bool = band(Bool left, Bool right);
data Bool = bor(Bool left, Bool right);  

public Bool reduce(Bool B) {
    return innermost visit(B) {
      case band(btrue(),  B1)       => B1		  // Use variables
      case band(bfalse(), B1)       => bfalse()
      
      case bor(btrue(), btrue())    => btrue()    // Use a truth table
      case bor(btrue(), bfalse())   => btrue()
      case bor(bfalse(), btrue())   => btrue()
      case bor(bfalse(), bfalse())  => bfalse()
    };
}

// Tests
	
public test bool t1() = reduce(bor(band(btrue(),btrue()),band(btrue(), bfalse()))) == btrue();
