module lang::saf::SAF

start syntax Fighter 
  = fighter: Id name "{" Spec* specs "}";

syntax Spec
  = attribute: Id name "=" Number strength  
  | behavior: Cond cond "[" Action move Action fight "]";

syntax Cond 
  = const: Id 
  | left and: Cond lhs "and" Cond rhs
  > left or: Cond lhs "or" Cond rhs;
  
syntax Action
  = /*@category="Constant"*/ action: Id 
  | choose: "choose" "(" Id* actions ")";
  
lexical Number
  = [0-9]+ !>> [0-9];

lexical Id 
  = ([a-z A-Z 0-9 _] !<< [a-z A-Z][a-z A-Z 0-9 _]* !>> [a-z A-Z 0-9 _])  
  ;

layout LAYOUTLIST
  = LAYOUT* !>> [\t-\n \r \ ] !>> "//" !>> "/*";

lexical LAYOUT
  = Comment 
  | [\t-\n \r \ ];
    
lexical Comment
  = /*@category="Comment"*/  "/*" (![*] | [*] !>> [/])* "*/" 
  | /*@category="Comment"*/  "//" ![\n]* [\n];
  