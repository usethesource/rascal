module lang::rascal::tests::concrete::Syntax2

import IO;
import ParseTree;
import lang::pico::\syntax::Main;

test bool Pico1()  {t1 = (Program) `begin declare x: natural; x := 10 end`; return true;}

test bool Pico2() { return Program _ := (Program) `begin declare x: natural; x := 10 end`;}

test bool Pico3() { return (Program) `<Program _>` := (Program) `begin declare x: natural; x := 10 end`;}
  
Tree t1 = (Program) `begin declare x: natural; x := 10 end`;
 
//Tree t2 = (Declarations) `declare x : natural;`;


test bool PicoQuotedId() = (Id) `a`:= (Id) `a`;

test bool PicoQuotedType() = (Type) `natural` := (Type) `natural`;
test bool PicoQuotedExp1() = (Expression) `x` := (Expression) `x`;
test bool PicoQuotedExp2() = (Expression) `"abc"` := (Expression) `"abc"`;
test bool PicoQuotedExp3() = (Expression) `42` := (Expression) `42`;
test bool PicoQuotedExp4() = (Expression) `(42)` := (Expression) `(42)`;
test bool PicoQuotedExp5() = (Expression) `4 + 2` := (Expression) `4 + 2`;
test bool PicoQuotedExp6() = (Expression) `4 - 2` := (Expression) `4 - 2`;

test bool PicoQuotedStat1() = (Statement) `x:=1` := (Statement) `x:=1`;

test bool PicoQuotedIfElse1() = (Statement) `if x then a:=1;b:=2 else fi` := (Statement) `if x then a:=1;b:=2 else fi`;

test bool PicoQuotedWhile1() = (Statement) `while x do a:=1;b:=2 od` := (Statement) `while x do a:=1;b:=2 od`;
 

test bool PicoQuotedProgram1() {
    return (Program) `<Program _>` := t1;
}

test bool PicoQuotedProgram2(){
    return Program _ := t1;
}

test bool PicoQuotedProgram3(){
    return (Program) `begin <Declarations _> <{Statement ";"}* _> end` := t1;
}

test bool PicoQuotedProgram4(){
    return (Program)`begin <Declarations decls> <{Statement ";"}* stats> end` := t1;
}

test bool PicoQuotedProgram5(){
    if((Program)`begin <Declarations decls> <{Statement ";"}* stats> end` := t1){
        return decl := (Declarations) `declare x: natural;`;
    }
    return false;
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

test bool PicoAssign3(){
   Statement s = (Statement)`x := 0`;
   Expression exp1 = (Expression) `1+2`;
   s.val = exp1;
   return s := (Statement)`x := 1+2`;
}

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

test bool PicoAssign6(){
   Statement ifStat1 = (Statement)`if x then a:=1;b:=2 else fi`;
   Statement ifStat2 = (Statement)`if y then a:=10;b:=20 else fi`;
   ifStat1.elsePart = ifStat2.thenPart;
   return ifStat1 := (Statement)`if x then a:=1;b:=2 else a:=10;b:=20 fi`;
}

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

test bool descendPicoStatement1(){
    S = (Statement) `a:=1`;
    L = [ X | /Statement X :=  S]; 
    return L == [ S ];
}

test bool descendPicoStatement2(){
    S = (Statement)`if x then a:=10;b:=20 else a:=1;b:=2 fi`;
    L = [X | /Statement X := S]; 
    return size(L) == 5;
}

  /*   
  
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void enumeratorPicoStatement1Untyped(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = [X | X <- `a:=1` ]; L == [ `a:=1` ];}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void enumeratorPicoStatement1Typed(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = [X | Statement X <- `a:=1` ]; L == [ `a:=1` ];}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void enumeratorPicoStatementsUntyped(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = [X | X <- `a:=1;a:=2;a:=3` ]; L == [`a:=1`, `a:=2`, `a:=3`];}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void enumeratorPicoStatementsTyped(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = [X | Statement X <- `a:=1;a:=2;a:=3` ]; L == [`a:=1`, `a:=2`, `a:=3`];}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void enumeratorPicoStatementsConcretePattern1(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = [X | `<\\PICO-ID X>:=1` <- `a:=1;b:=2;c:=1` ]; L == [ `a`, `c` ];}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void enumeratorPicoStatementsConcretePattern2(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = [X | /`b:=<EXP X>` <- `a:=1;b:=2;c:=3` ]; L == [ (EXP)`2` ];}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void enumeratorPicoStatementsConcretePattern3(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = [Id | /`<\\PICO-ID Id> : <TYPE Tp>` <- `x : natural, y : string` ]; L == [ `x`, `y` ];}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void enumeratorPicoStatementsConcretePattern4(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = []; for(/`<\\PICO-ID Id> : <TYPE Tp>` <- `x : natural, y : string`){L += Id;} L == [ `x`, `y` ];}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void forPicoStatementsTyped1(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = [X | /Statement X <- `a:=1;a:=2;a:=3` ]; L == [`a:=1`, `a:=2`, `a:=3`];}"));
    }
    */
      /*
    
   test bool forPicoStatementsTyped2() {L = [X | /Statement X <- (Program) `begin declare a : natural; a:=1;a:=2;a:=3 end` ]; L == [(Statement) `a:=1`, (Statement)`a:=2`, (Statement) `a:=3`];}
   
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void forPicoStatementsTyped3(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = [X | /EXP X <- `begin declare a : natural; a:=1;b:=2;c:=3 end` ]; L == [(EXP)`1`, (EXP)`2`, (EXP)`3` ];}"));
    }
    
    @Test
    public void PicoStringDoesNotOverrideRascalString1(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{str s = \"abc\"; s == \"abc\";}"));
    }
    
    @Test
    public void PicoStringDoesNotOverrideRascalString2(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{int n = 3; s = \"abc<n>\"; s == \"abc3\";}"));
    }
    
}
*/
  
