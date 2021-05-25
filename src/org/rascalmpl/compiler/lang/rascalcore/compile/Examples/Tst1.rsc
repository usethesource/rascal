module lang::rascalcore::compile::Examples::Tst1


//import ParseTree;
//import demo::lang::Pico::Syntax;

import lang::rascalcore::compile::Examples::Tst2;



test bool Pico1()  {t1 = (Program) `begin declare x: natural; x := 10 end`; return true;}

test bool Pico2() { return Program _ := (Program) `begin declare x: natural; x := 10 end`;}

test bool Pico3() { return (Program) `<Program _>` := (Program) `begin declare x: natural; x := 10 end`;}
  
Tree t1 = (Program) `begin declare x: natural; x := 10 end`;
 
Tree t2 = (Declarations) `declare x : natural;`;
 
test bool PicoQuoted1() {
    bool match() { return (Program) `<Program _>` := t1; }

    return match();
}

test bool PicoQuoted2(){
    bool match() { return Program _ := t1; }

    return match();
}

test bool PicoQuoted5(){
    bool match() { return (Program) `begin <Declarations _> <{Statement ";"}* _> end` := t1; }
    
    return match();
}

test bool PicoAssign1(){
   Statement s = (Statement)`x := 0`;
   Id idY = (Id) `y`;
   s.var = idY;
   return s := (Statement)`y := 0`;
}

test bool PicoAssign2(){
   Statement s = (Statement)`x := 0`;
   Expression exp1 = (Expression) `1`;
   s.val = exp1;
   return s := (Statement)`x := 1`;
}

//test bool PicoAssign3(){
//   Statement s = (Statement)`x := 0`;
//   Expression exp1 = (Expression) `1+2`;
//   s.val = exp1;
//   return s := (Statement)`x := 1+2`;
//}

test bool PicoAssign4(){
   Statement ifStat = (Statement)`if x then a:=1;b:=2 else fi`;
   Expression idY = (Expression) `y`;
   ifStat.cond = idY;
   return ifStat := (Statement)`if y then a:=1;b:=2 else fi`;
}

test bool PicoAssign5(){
   Statement ifStat1 = (Statement)`if x then a:=1;b:=2 else fi`;
   Statement ifStat2 = (Statement)`if y then a:=10;b:=20 else fi`;
   ifStat1.thenPart = ifStat2.thenPart;
   return ifStat1 := (Statement)`if x then a:=10;b:=20 else fi`;
}
//
//@ignoreCompiler{FIX: A space is missing after substituted thenPart}
//test bool PicoAssign6(){
//   Statement ifStat1 = (Statement)`if x then a:=1;b:=2 else fi`;
//   Statement ifStat2 = (Statement)`if y then a:=10;b:=20 else fi`;
//   ifStat1.elsePart = ifStat2.thenPart;
//   return ifStat1 := (Statement)`if x then a:=1;b:=2 else a:=10;b:=20 fi`;
//}

test bool PicoAssign7(){
   Statement ifStat1 = (Statement)`if x then a:=1;b:=2 else fi`;
   Statement ifStat2 = (Statement)`if y then a:=10;b:=20 else fi`;
   ifStat1.elsePart = ifStat2.thenPart;
   return (Statement)`if x then a:=1;b:=2 else a:=10;b:=20 fi` := ifStat1;
}

test bool PicoAssign8(){
   Statement ifStat = (Statement)`if x then a:=1;b:=2 else a:=10;b:=20 fi`;
   thenPart = ifStat.thenPart;
   elsePart = ifStat.elsePart;
   
   ifStat.thenPart = elsePart;
   ifStat.elsePart = thenPart;
   return ifStat := (Statement)`if x then a:=10;b:=20 else a:=1;b:=2 fi`;
}
