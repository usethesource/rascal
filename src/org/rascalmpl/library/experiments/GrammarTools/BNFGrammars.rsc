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
            	
            	
