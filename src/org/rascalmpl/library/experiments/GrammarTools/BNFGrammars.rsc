@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module experiments::GrammarTools::BNFGrammars
import experiments::GrammarTools::BNF;

public BNF G1BNF = 
       `grammar E 
        rules
           E ::= E '*' B;
           E ::= E '+' B;
           E ::= B;
           B ::= '0';
           B ::= '1';`;
            	
public BNF G2BNF =
       `grammar E
        rules
           E  ::= T E1;
           E1 ::= '+' T E1;
           E1 ::= ;
           T  ::= F T1;
           T1 ::= '*' F T1;
           T1 ::= ;
           F  ::= '(' E ')';
           F  ::= 'id';`;
            	
            	
