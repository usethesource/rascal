module lang::rascalcore::compile::Examples::Tst2

lexical Id  = [a-z][a-z0-9]* !>> [a-z0-9];
lexical Natural = [0-9]+ ;
lexical String = "\"" ![\"]*  "\"";

layout Layout = WhitespaceAndComment* !>> [\ \t\n\r%];

lexical WhitespaceAndComment 
   = [\ \t\n\r]
   | @category="Comment" ws2: "%" ![%]+ "%"
   | @category="Comment" ws3: "%%" ![\n]* $
   ;

start syntax Program 
   = program: "begin" Declarations decls {Statement  ";"}* body "end" ;

syntax Declarations 
   = "declare" {Declaration ","}* decls ";" ;  
 
start syntax Declaration = decl: Id id ":" Type tp;

syntax Type 
   = natural:"natural" 
   | string:"string" 
   ;

syntax Statement 
   = asgStat: Id var ":="  Expression val 
   | ifElseStat: "if" Expression cond "then" {Statement ";"}*  thenPart "else" {Statement ";"}* elsePart "fi"
   | whileStat: "while" Expression cond "do" {Statement ";"}* body "od"
  ;  
     
syntax Expression 
   = id: Id name
   | strCon: String string
   | natCon: Natural natcon
   | bracket "(" Expression e ")"
   > left conc: Expression lhs "||" Expression rhs
   > left ( add: Expression lhs "+" Expression rhs
          | sub: Expression lhs "-" Expression rhs
          )
  ;