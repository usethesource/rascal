module demo::lang::Pico::Syntax

import ParseTree;

lexical Id = [a-z][a-z0-9]* !>> [a-z0-9]; // <1>

lexical Natural = [0-9]+ ; // <1>

lexical String = "\"" ![\"]*  "\""; // <1>

layout Layout = WhitespaceAndComment* !>> [\ \t\n\r%]; // <2>

lexical WhitespaceAndComment // <2>
   = [\ \t\n\r]
   | @category="Comment" ws2: "%" ![%]+ "%" // <3>
   | @category="Comment" ws3: "%%" ![\n]* $ // <3>
   ;

start syntax Program // <4>
   = program: "begin" Declarations decls {Statement  ";"}* body "end" ;

syntax Declarations 
   = "declare" {Declaration ","}* decls ";" ;  
 
syntax Declaration = decl: Id id ":" Type tp;

syntax Type 
   = natural:"natural" 
   | string :"string" 
   ;

syntax Statement 
   = asgStat: Id var ":="  Expression val 
   | ifElseStat: "if" Expression cond "then" {Statement ";"}*  thenPart "else" {Statement ";"}* elsePart "fi"
   | whileStat: "while" Expression cond "do" {Statement ";"}* body "od"
  ;  
     
syntax Expression // <5>
   = id: Id name
   | strCon: String string
   | natCon: Natural natcon
   | bracket "(" Expression e ")"
   > left conc: Expression lhs "||" Expression rhs
   > left ( add: Expression lhs "+" Expression rhs
          | sub: Expression lhs "-" Expression rhs
          )
  ;

/*<6>*/ start[Program] program(str s) {
  return parse(#start[Program], s);
}

/*<6>*/ start[Program] program(str s, loc l) {
  return parse(#start[Program], s, l);
} 
