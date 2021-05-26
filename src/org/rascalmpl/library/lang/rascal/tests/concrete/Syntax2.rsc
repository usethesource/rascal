module lang::rascal::tests::concrete::Syntax2

import ParseTree;
import demo::lang::Pico::Syntax;

test bool Pico1()  {t1 = (Program) `begin declare x: natural; x := 10 end`; return true;}

test bool Pico2() { return Program _ := (Program) `begin declare x: natural; x := 10 end`;}

test bool Pico3() { return (Program) `<Program _>` := (Program) `begin declare x: natural; x := 10 end`;}
  
Tree t1 = (Program) `begin declare x: natural; x := 10 end`;
 
//Tree t2 = (Declarations) `declare x : natural;`;
 
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

 /*
    @Test
    @Ignore("Functionality subject to future/current change")
    public void Pico4(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{`begin <decls> <stats> end` := `begin declare x: natural; x := 10 end`;}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void Pico5(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{`begin <Declarations decls> <{Statement \";\"}* stats> end` := `begin declare x: natural; x := 10 end`;}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void Pico6(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{Declarations decls; {Statement \";\"}* stats; `begin <decls> <stats> end` := `begin declare x: natural; x := 10 end`;}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void Pico7a(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{`begin <Declarations decls> <{Statement \";\"}+ stats> end` := `begin declare x: natural; x := 1; x := 2 end` &&" +
                                          "(decls == `declare x: natural;`);}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void Pico7b(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{`begin <Declarations decls> <{Statement \";\"}+ stats> end` := `begin declare x: natural; x := 1; x := 2 end` &&" +
                                          "(decls == `declare x: natural;`) && (stats == ({Statement \";\"}+)`x := 1; x := 2`);}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void Pico7c(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{`begin <Declarations decls> <{Statement \";\"}* stats> end` := `begin declare x: natural; x := 1; x := 2 end` &&" +
                                          "(decls == `declare x: natural;`) && (stats == ({Statement \";\"}*)`x := 1; x := 2`);}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void Pico8(){
        prepare("import lang::pico::\\syntax::Main;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{ bool B;" +
                                          "  if(`begin <Declarations decls> <{Statement \";\"}* stats> end` := `begin declare x: natural; x := 1; x := 2 end`){" +
                                          "            B = (decls == `declare x: natural;`);" +
                                          "  } else" +
                                          "     B = false; " +
                                          "  B;" +
                                          "}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void PicoQuoted3(){
        prepareModule("M", QmoduleM + "public bool match3() { return (Program) `begin <Declarations decls> <{Statement \";\"}* stats> end` := t1; }\n");
        prepareMore("import ParseTree;");
        prepareMore("import M;");
        assertTrue(runTestInSameEvaluator("match3();"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void PicoQuoted4(){
        prepareModule("M", QmoduleM + "public bool match4() { return `begin <Declarations decls> <stats> end` := t1; }");
        prepareMore("import ParseTree;");
        prepareMore("import M;");
        assertTrue(runTestInSameEvaluator("match4();"));
    }
 */
  /*   
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void PicoQuoted6(){
        prepareModule("M", QmoduleM + "public bool match6() { return (Program) `begin <Declarations decls> <{Statement \";\"}* stats> end` := t1; }");
        prepareMore("import ParseTree;");
        prepareMore("import M;");
        assertTrue(runTestInSameEvaluator("match6();"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void PicoQuoted7(){
        prepareModule("M", QmoduleM + "public bool match7() { return ` begin declare <{IdType \",\" }* decls>; <{Statement \";\"}* Stats> end ` := t1; }");
        prepareMore("import ParseTree;");
        prepareMore("import M;");
        assertTrue(runTestInSameEvaluator("match7();"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void PicoQuoted8(){
        prepareModule("M", QmoduleM + "public bool match8() { return ` declare <{IdType \",\" }* decls>; ` := t2; }");
        prepareMore("import ParseTree;");
        prepareMore("import M;");
        assertTrue(runTestInSameEvaluator("match8();"));
    }
    
    private String UQmoduleM = "module M\n" +
    "import lang::pico::\\syntax::Main;\n" +
    "import ParseTree;" +
    "public Tree t1 = begin declare x: natural; x := 10 end;\n";

    @Test(expected=StaticError.class) // Directly antiquoting without quotes not allowed.
    @Ignore("Functionality subject to future/current change")
    public void PicoUnQuoted1(){
        prepareModule("M", UQmoduleM + "public bool match1() { return <Program program> := t1; }\n");
        prepareMore("import ParseTree;");
        prepareMore("import M;");
        assertTrue(runTestInSameEvaluator("match1();"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void PicoUnQuoted2(){
        prepareModule("M", UQmoduleM + "public bool match2() { return Program program := t1; }\n");
        prepareMore("import ParseTree;");
        prepareMore("import M;");
        assertTrue(runTestInSameEvaluator("match2();"));
    }
    
    @Test @Ignore
    public void PicoUnQuoted3(){
        prepareModule("M", UQmoduleM + "public bool match3() { return begin <decls> <stats> end := t1; }\n");
        prepareMore("import ParseTree;");
        prepareMore("import M;");
        assertTrue(runTestInSameEvaluator("match3();"));
    }
    
    @Test @Ignore
    public void PicoUnQuoted4(){
        prepareModule("M", UQmoduleM + "public bool match4() { return begin <Declarations decls> <stats> end := t1; }");
        prepareMore("import ParseTree;");
        prepareMore("import M;");
        assertTrue(runTestInSameEvaluator("match4();"));
    }
    
    @Test @Ignore
    public void PicoUnQuoted5(){
        prepareModule("M", UQmoduleM + "public bool match5() { return begin <decls> <{Statement \";\"}* stats> end := t1; }");
        prepareMore("import ParseTree;");
        prepareMore("import M;");
        assertTrue(runTestInSameEvaluator("match5();"));
    }
    
    @Test @Ignore
    public void PicoUnQuoted6(){
        prepareModule("M", UQmoduleM + "public bool match6() { return begin <Declarations decls> <{Statement \";\"}* stats> end := t1; }");
        prepareMore("import ParseTree;");
        prepareMore("import M;");
        assertTrue(runTestInSameEvaluator("match6();"));
    }
    
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
  