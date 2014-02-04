module experiments::Compiler::Examples::ConcreteMatching2


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
   
value main(list[value] args){
   res = "";
   if((Exp) `1` := [Exp] "1") 			
   		res += "[10] 1 "; else res += "[10] FAIL ";
   
   if((Exp) `1*2` := [Exp] "1*2")
   		res += "[11] 1*2 ";else res += "[11] FAIL ";
   		 
   if((Exp) `1 *2` := [Exp] "1* 2")
    	res += "[12] 1*2 "; else res += "[12] FAIL ";
   
   if((Exp) `1==2` := [Exp] "1==2")
    	res += "[13] 1==2 "; else res += "[13] FAIL ";

   if((Exp) `<Exp e>` := [Exp] "1")
    	res += "[14] e = <e> "; else res += "[14] FAIL ";
   
   if((Stat) `a:=1` := [Stat] "a:=1")
   	 	res += "[15] a:=1 "; else res += "[15] FAIL ";

   if((Stat) `<Identifier x> := <Exp e>` := [Stat] "a:=      1") 
   		res += "[16] <x>:=<e> "; else res += "[16] FAIL ";

   if((Stat) `<Identifier x> := <Exp e1> + <Exp e2>` := [Stat] "a:= 10 + 20") 
   		res += "[17] <x>:=<e1>+<e2> "; else res += "[17] FAIL ";

   if((Stat) `if x then a := 1 else b:=2 fi` := [Stat] "if x then a := 1 else b:=2 fi") 
   		res += "[20] if x then a := 1 else b:=2 fi "; else res += "[20] FAIL ";
   										
   if((Stat) `if <Exp c> then <{Stat ";"}* th> else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi") 
      	res += "[21] <c>/<th>/<el> "; else res += "[21] FAIL ";

   if((Stat) `if <Exp c> then <{Stat ";"}* th> else <Identifier lhs>:=<Exp rhs> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi") 
      	res += "[22] <c>/<th>/<lhs>/<rhs> "; else res += "[22] FAIL ";
      									
    if((Stat) `if <Exp c> then a:=1;<{Stat ";"}* th> else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi") 
      	res += "[23] <th>/<el> "; else res += "[23] FAIL ";
    
    if((Stat) `if <Exp c> then a:=1;<{Stat ";"}* th>;b:=2 else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi") 
    	res += "[24] <th>/<el> "; else res += "[24] FAIL ";
    
    if((Stat) `if <Exp c> then a:=1;b:=2;<{Stat ";"}* th> else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi") 
      	res += "[25] <th>/<el> "; else res += "[25] FAIL ";
      									
   if((Stat) `if <Exp c> then <{Stat ";"}* th1>;a := 1;<{Stat ";"}* th2> else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2 else c:=3 fi") 
      	res += "[26] <c>/<th1>/<th2>/<el> "; else res += "[26] FAIL ";
      	
   if((Stat) `if <Exp c> then <{Stat ";"}* th1>;a := 1;<{Stat ";"}* th2>; d := 5 else <{Stat ";"}* el> fi` := [Stat] "if x then a := 1;b:=2;d:=5 else c:=3 fi") 
      	res += "[26] <c>/<th1>/<th2>/<el> "; else res += "[26] FAIL ";
      
   return res;
}
