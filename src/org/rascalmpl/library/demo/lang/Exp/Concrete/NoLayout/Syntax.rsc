module demo::lang::Exp::Concrete::NoLayout::Syntax
    
lexical IntegerLiteral = [0-9]+; // <1>

start syntax Exp        // <2>
  = IntegerLiteral      // <3>
  | bracket "(" Exp ")" // <4>
  > left Exp "*" Exp    // <5>
  > left Exp "+" Exp    // <6>
  ;
