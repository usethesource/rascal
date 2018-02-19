module lang::rascalcore::check::Test1

//lexical Id  = [a-z][a-z0-9]*;// !>> [a-z0-9];
//lexical Natural = [0-9]+ ;
//lexical String = "\"" ![\"]*  "\"";

//layout Layout = WhitespaceAndComment* !>> [\ \t\n\r%];

//lexical WhitespaceAndComment 
//   = [\ \t\n\r]
//   | @category="Comment" ws2: "%" ![%]+ "%"
//   | @category="Comment" ws3: "%%" ![\n]* $
//   ;
     
syntax Expression 
   =  "123" 
   | "def"
   // id: Id name
//   | strCon: String string
    //natConQQQ: Natural natcon
   //| bracket "(" Expression e ")"
   //> left conc: Expression lhs "||" Expression rhs
   //> left ( add: Expression lhs "+" Expression rhs
   //       | sub: Expression lhs "-" Expression rhs
   //       )
  ;