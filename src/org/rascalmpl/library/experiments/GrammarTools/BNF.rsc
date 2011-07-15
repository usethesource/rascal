module experiments::GrammarTools::BNF

lexical NonTerminal = [A-Z][A-Za-z0-9\-_]* !>> [A-Za-z0-9\-_]
                      ;

lexical Terminal = "\'" ![\']* "\'"
                   ;

start syntax BNF = "grammar" NonTerminal startSym "rules" BNFRule+ rules
             ;

syntax BNFRule = NonTerminal lhs "::=" BNFElement* elements ";"
                 ;

syntax BNFElement = NonTerminal
                    | Terminal
                    ;

layout LAYOUTLIST = [\t-\n \r \ ]* !>> [\t-\n \r \ ]
                    ;
