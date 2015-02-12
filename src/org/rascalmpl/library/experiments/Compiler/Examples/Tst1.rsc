module experiments::Compiler::Examples::Tst1



import ParseTree;
layout Whitespace = [\ ]*;
lexical IntegerLiteral = [0-9]+; 
lexical Identifier = [a-z]+;

syntax Exp 
  = IntegerLiteral  
  | Identifier        
  | bracket "(" Exp ")"     
  > left Exp "*" Exp        
  > left Exp "+" Exp  
  | Exp "==" Exp      
  ;

syntax Stat 
   = Identifier ":=" Exp
   | "if" Exp "then" {Stat ";"}* "else" {Stat ";"}* "fi"
   ;
   
int sw(value e) {
	n = 0;
	switch(e){

	case (Exp) `1`: 	n = 1;
	case (Exp) `2`: 	n = 2;
	case (Exp) `1* 2`:	n = 3;
	case (Exp) `1==2`: 	n = 4;
	case (Exp) `5==5`: 	n = 5;
	default:			n = 6;
	}
	return n;
}
//test bool concreteMatch220() = sw([Exp] "1") == 1;
//
//test bool concreteMatch221() = sw([Exp] "2") 			 == 2;
//test bool concreteMatch222() = sw([Exp] "1 * 2") 		 == 3;
//test bool concreteMatch223() = sw([Exp] "1 == 2") 		 == 4;
//test bool concreteMatch224() = sw([Exp] "5 == 5") 		 == 5;
//test bool concreteMatch225() = sw([IntegerLiteral] "2")  == 6;   

value main(list[value] args)  = sw([Exp] "1") == 1;

////test bool concreteMatch231() = [ s | /lit(str s) := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ] ==
//// 								["if","then",";","else",";","fi","if","then",";",":=",":=",";",":=",":=","else",";",":=",":=","fi"];
// 
//
//value main(list[value] args)  = [ s | /lit(str s) := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ] ;
//
//
////[ n | /int n := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ];   // ==
 					