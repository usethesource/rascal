module lang::rascal::tests::concrete::Patterns2

import ParseTree;
layout Whitespace = [\ ]* !>> [\ ];
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

test bool concreteMatch201() = (Exp) `1` := [Exp] "1";
test bool concreteMatch202() = (Exp) `1*2` := [Exp] "1*2";
test bool concreteMatch203() = (Exp) `1 *2` := [Exp] "1* 2";
test bool concreteMatch204() = (Exp) `1==2` := [Exp] "1==2";

test bool concreteMatch205() =   (Exp) `<Exp _>` := [Exp] "1";

test bool concreteMatch206() = (Stat) `a:=1` := [Stat] "a:=1";
test bool concreteMatch207() = (Stat) `<Identifier x> := <Exp e>` := [Stat] "a:=      1" && "<x>" == "a" && "<e>" == "1";
test bool concreteMatch208() = (Stat) `<Identifier x> := <Exp e1> + <Exp e2>` := [Stat] "a:= 10 + 20" && "<x>" == "a" && "<e1>" == "10" && "<e2>" == "20";
test bool concreteMatch209() = (Stat) `if x then a := 1 else b:=2 fi` := [Stat] "if x then a := 1 else b:=2 fi";
test bool concreteMatch210() = (Stat) `if <Exp c> then <{Stat ";"}* th> else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi" && "<c>" == "x" && "<th>" == "a := 1;b:=2" && "<el>" == "c:=3";


test bool concreteMatch211() = (Stat) `if <Exp c> then <{Stat ";"}* th> else <Identifier lhs>:=<Exp rhs> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi" && "<c>" == "x" && "<th>" == "a := 1;b:=2" && "<lhs>" == "c" && "<rhs>" == "3";
test bool concreteMatch212() = (Stat) `if <Exp c> then a:=1;<{Stat ";"}* th> else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi" && "<c>" == "x" && "<th>" == "b:=2" && "<el>" == "c:=3";
test bool concreteMatch213() = (Stat) `if <Exp c> then a:=1;<{Stat ";"}* th>;b:=2 else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi" && "<c>" == "x" && "<th>" == "" && "<el>" == "c:=3";
test bool concreteMatch214() = (Stat) `if <Exp c> then a:=1;b:=2;<{Stat ";"}* th> else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi" && "<c>" == "x" && "<th>" == "" && "<el>" == "c:=3";
test bool concreteMatch215() = (Stat) `if <Exp _> then <{Stat ";"}* th1>;a := 1;<{Stat ";"}* th2> else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi" && "<th1>" == "" && "<th2>" == "b:=2" && "<el>" == "c:=3";
test bool concreteMatch216() = (Stat) `if <Exp _> then <{Stat ";"}* th1>;a := 1;<{Stat ";"}* th2>; d := 5 else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2;d:=5 else c:=3 fi" && "<th1>" == "" && "<th2>" == "b:=2" && "<el>" == "c:=3";

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
test bool concreteMatch220() = sw([Exp] "1") == 1;

test bool concreteMatch221() = sw([Exp] "2") 			 == 2;
test bool concreteMatch222() = sw([Exp] "1 * 2") 		 == 3;
test bool concreteMatch223() = sw([Exp] "1 == 2") 		 == 4;
test bool concreteMatch224() = sw([Exp] "5 == 5") 		 == 5;
test bool concreteMatch225() = sw([IntegerLiteral] "2")  == 6;


test bool semiConcrete1() = list[Identifier] _ := [[Identifier] "x"];
test bool semiConcrete2() = tuple[Identifier,Identifier] _ := <[Identifier] "x", [Identifier] "y">;

test bool concreteMatch230() = [ "<ident>" | /Identifier ident := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ] == 
 							   ["x", "a", "b", "c"];
 							   
test bool concreteMatch231() {
	res = [];
	visit([Stat] "if x then a := 1;b:=2 else c:=3 fi"){
		case Identifier ident: res += "<ident>";
	}
	return res == ["x", "a", "b", "c"];
}						   
 							   
test bool concreteMatch232() = [ "<stat>" | /Stat stat := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ] == 
							   ["a := 1", "b:=2", "c:=3", "if x then a := 1;b:=2 else c:=3 fi"];
							   
test bool concreteMatch233() {
	res = [];
	visit([Stat] "if x then a := 1;b:=2 else c:=3 fi"){
		case Stat stat: res += "<stat>";
	}
	return res ==  ["a := 1", "b:=2", "c:=3", "if x then a := 1;b:=2 else c:=3 fi"];
}	
								   
test bool concreteMatch234() = [ "<stats>" | /{Stat ";"}* stats := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ] ==
 							   ["a := 1;b:=2", "c:=3"];
 							   
test bool concreteMatch235() {
	res = [];
	visit( [Stat] "if x then a := 1;b:=2 else c:=3 fi"){
		case {Stat ";"}* stats: res += "<stats>";
	}
	return res == ["a := 1;b:=2", "c:=3"];
}	
				   
test bool concreteMatch236() = [ "<stats>" | /{Stat ";"}+ stats := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ] ==
                               ["a := 1;b:=2", "c:=3"];
                             
test bool concreteMatch237(){
	res = [];
	visit([Stat] "if x then a := 1;b:=2 else c:=3 fi"){
		case {Stat ";"}+ stats: res += "<stats>";
	}
	return res == ["a := 1;b:=2", "c:=3"];
}
    
test bool concreteMatch238() = [ "<stats>" | /{Stat ";"}+ stats := [Stat] "if x then else c:=3 fi" ] ==
    						   ["c:=3"];

test bool concreteMatch239(){
	res = [];
	visit( [Stat] "if x then else c:=3 fi" ){
		case {Stat ";"}+ stats: res += "<stats>";
	}
	return res == ["c:=3"];
}   						   
 							   
test bool concreteMatch240() = [ s | /lit(str s) := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ] ==
 								["if","then",";","else",";","fi","if","then",";",":=",":=",";",":=",":=","else",";",":=",":=","fi"];
 								
test bool concreteMatch241(){
	res = [];
	visit( [Stat] "if x then a := 1;b:=2 else c:=3 fi" ){
		case lit(str s): res += s;
	}
	return res == ["if","then",";","else",";","fi","if","then",";",":=",":=",";",":=",":=","else",";",":=",":=","fi"];
}								
 								
test bool concreteMatch242() =  [ n | /int n := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ] ==
 [105,105,102,102,105,102,32,32,32,32,32,32,32,97,122,97,122,120,32,32,32,32,32,32,32,116,116
 ,104,104,101,101,110,110,116,104,101,110,32,32,32,32,32,32,32,97,122,97,122,97,32,32,32,32,32
 ,32,32,58,58,61,61,58,61,32,32,32,32,32,32,32,48,57,48,57,49,32,32,32,32,32,32,59,59,59,32,32
 ,32,32,32,32,97,122,97,122,98,32,32,32,32,32,32,58,58,61,61,58,61,32,32,32,32,32,32,48,57,48
 ,57,50,32,32,32,32,32,32,32,101,101,108,108,115,115,101,101,101,108,115,101,32,32,32,32,32,32
 ,32,97,122,97,122,99,32,32,32,32,32,32,58,58,61,61,58,61,32,32,32,32,32,32,48,57,48,57,51,32
 ,32,32,32,32,32,32,102,102,105,105,102,105];
 
test bool concreteMatch243(){
	res = [];
	visit( [Stat] "if x then a := 1;b:=2 else c:=3 fi" ){
		case int n: res += n;
	}
	return res ==
	 [105,105,102,102,105,102,32,32,32,32,32,32,32,97,122,97,122,120,32,32,32,32,32,32,32,116,116
 	 ,104,104,101,101,110,110,116,104,101,110,32,32,32,32,32,32,32,97,122,97,122,97,32,32,32,32,32
 	 ,32,32,58,58,61,61,58,61,32,32,32,32,32,32,32,48,57,48,57,49,32,32,32,32,32,32,59,59,59,32,32
	 ,32,32,32,32,97,122,97,122,98,32,32,32,32,32,32,58,58,61,61,58,61,32,32,32,32,32,32,48,57,48
 	 ,57,50,32,32,32,32,32,32,32,101,101,108,108,115,115,101,101,101,108,115,101,32,32,32,32,32,32
 	 ,32,97,122,97,122,99,32,32,32,32,32,32,58,58,61,61,58,61,32,32,32,32,32,32,48,57,48,57,51,32
	 ,32,32,32,32,32,32,102,102,105,105,102,105];
} 


test bool concreteMatch244() =  size([ x | /x:appl(_,_) := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ]) == 60;
 
test bool concreteMatch245(){
	n = 0;
	visit( [Stat] "if x then a := 1;b:=2 else c:=3 fi" ){
		case appl(_,_): n += 1;
	}
	return n == 60;
}

test bool concreteMatch246a() =  size([ x | /x:\char-class(_) := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ]) == 75;
 
test bool concreteMatch247a(){
	n = 0;
	visit( [Stat] "if x then a := 1;b:=2 else c:=3 fi" ){
		case \char-class(_): n += 1;
	}
	return n == 75;
}

test bool concreteMatch246b() =  size([ x | /x:\sort(_) := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ]) == 16;
 
test bool concreteMatch247b(){
	n = 0;
	visit( [Stat] "if x then a := 1;b:=2 else c:=3 fi" ){
		case \sort(_): n += 1;
	}
	return n == 16;
}

test bool concreteMatch248() =  size([ x | /x:\layouts(_) := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ]) == 34;
 
test bool concreteMatch249(){
	n = 0;
	visit( [Stat] "if x then a := 1;b:=2 else c:=3 fi" ){
		case \layouts(_): n += 1;
	}
	return n == 34;
}	

test bool concreteMatch250() = [ n | /char(int n) := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ] 
                               == [105,102,32,120,32,116,104,101,110,32,97,32,58,61,32,49,59,98,58,61,50,32,101,108,115,101,32,99,58,61,51,32,102,105];
 		
test bool concreteMatch251() {
    res = [];;
    visit( [Stat] "if x then a := 1;b:=2 else c:=3 fi" ){
        case char(int n):res += n;
    }
    return res == [105,102,32,120,32,116,104,101,110,32,97,32,58,61,32,49,59,98,58,61,50,32,101,108,115,101,32,99,58,61,51,32,102,105];
}

test bool concreteMatch252() = size([ p | /p:prod(_,_,_) := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ]) 
             == 37;
 
test bool concreteMatch253(){
    n = 0;
    visit( [Stat] "if x then a := 1;b:=2 else c:=3 fi" ){
        case prod(_,_,_): n += 1;
    }
    return n == 37;
}  

test bool concreteMatch254() = size([ r | /r:range(_,_) := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ]) == 75;  
 
test bool concreteMatch255(){
    n = 0;
    visit( [Stat] "if x then a := 1;b:=2 else c:=3 fi" ){
        case range(_,_): n += 1;
    }
    return n == 75;
} 

test bool concreteMatch256() = size([ iss | /iss:\iter-star-seps(_,_) := [Stat] "if x then a := 1;b:=2 else c:=3 fi" ]) == 4;

test bool concreteMatch257(){
    n = 0;
    visit( [Stat] "if x then a := 1;b:=2 else c:=3 fi" ){
        case \iter-star-seps(_,_): n += 1;
    }
    return n == 4;
}   
 
 								
