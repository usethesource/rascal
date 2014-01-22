module experiments::Compiler::Examples::ConcreteMatching


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
//   if((Exp) `1` := [Exp] "1") res += "[A] 1;";

//   if((Exp) `<Exp e>` := [Exp] "1") res += "[B] e = <e>;";
//   
//   if((Stat) `a:=1` := [Stat] "a:=1") res += "[C] a:=1;";

//   if((Stat) `<Identifier x> := <Exp e>` := [Stat] "a:=      1") res += "[D] <x>:=<e>;";

//   if((Stat) `<Identifier x> := <Exp e1> + <Exp e2>` := [Stat] "a:= 10 + 20") res += "[E] <x>:=<e1>+<e2>;";
//
//   if((Stat) `if x then a := 1 else b:=2 fi` := [Stat] "if x then a := 1 else b:=2 fi") res += "[F] if x then a := 1 else b:=2 fi;";

//   if((Stat) `if <Exp c> then <{Stat ";"}* th> else <Identifier lhs>:=<Exp rhs> fi` := 
//      [Stat] "if x then a := 1;b:=2 else c:=3 fi") res += "[G] <c>, <th>, <lhs>, <rhs>;";
      
   if((Stat) `if <Exp c> then <{Stat ";"}* th1>; a := 1; <{Stat ";"}* th2> else <Identifier lhs>:=<Exp rhs> fi` := 
      [Stat] "if x then a := 1;b:=2 else c:=3 fi") res += "[H] <c>, <th1>, <th2>, <lhs>, <rhs>;";
      
   return res;
}
