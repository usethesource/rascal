module lang::std::Comment
 
syntax Comment lex = lex "//" ![\n]* [\n];