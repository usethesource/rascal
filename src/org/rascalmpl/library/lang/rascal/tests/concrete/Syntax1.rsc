module lang::rascal::tests::concrete::Syntax1

import Exception;
import ParseTree;

layout Whitespace = [\ \t\n]*;

start syntax A = "a";
start syntax A = "A";
start syntax As = A+;
start syntax B = "b";
start syntax B = "B";
start syntax Bs = B+;
start syntax C = A B;
start syntax D = "d";
start syntax DS = D+ ds;
start syntax E = "e";
start syntax ES = {E ","}+ args;

start syntax X1 = "x" x1;
syntax X2 = () x2;
syntax X3 = ("x"  "y") x3;
syntax X4 = [a-z] x4;
syntax X5 = ("x" | "y") x5;
syntax X6 = 'ax' x6;
syntax X7 = "x"? x7;

test bool tX1() = "<([X1] "x").x1>" == "x";
test bool tX2() = "<([X2] "").x2>" == "";
test bool tX3() = "<([X3] "xy").x3>" == "xy";
test bool tX4() = "<([X4] "x").x4>" == "x";
test bool tX5() = "<([X5] "x").x5>" == "x";
test bool tX7b() ="<([X7] "").x7>" == "";

test bool parseD1() = (D)`d` := parse(#D, "d");

@expected{ParseError}
test bool parseD2() = (D)`d` := parse(#D, " d");
@expected{ParseError}
test bool parseD3() = (D)`d` := parse(#D, "d ");

test bool parseD4() = (start[D])`d` := parse(#start[D], " d ");
test bool parseD5() = (D)`d` := parse(#start[D], " d ").top;

test bool parseDS() = (DS)`d d d` := parse(#DS, "d d d") ;

test bool parseDSfromFile() =  (DS)`d d d` := parse(#DS, |testdata:///DS.trm|);

test bool singleA() = (A) `a` := (A) `a`;

test bool DSmatch() = (DS)`d d` := (DS)`d d`;

test bool DvarsTypedInsert2() = (DS)`<D+ _>` := (DS)`d`;

test bool DvarsTypedInsert3() = (DS)`<D+ _>` := (DS)`d d`;

test bool DvarsTypedInsert4() = (DS)`d <D+ Xs>` := (DS)`d d` && (DS)`d <D+ Xs2>` := (DS)`d d` && Xs := Xs2;

test bool DvarsTypedInsert5() = (DS)`d <D+ Xs>` := (DS)`d d d` && (DS)`d <D+ Xs2>` := (DS)`d d d` && Xs := Xs2;

test bool sortA()   = A _ := [A] "a";
test bool sortB()   = B _ := [B] "b";
test bool sortC()   = C _ := [C] "ab";
test bool sortD()   = D _ := [D] "d";
test bool sortDS()  = DS _ := [DS] "ddd";
test bool sortE()   = E _ := [E] "e";
test bool sortES1() = ES _ := [ES] "e,e,e";
test bool sortES2() = {E ","}+ _ := ([ES] "e,e,e").args;

test bool asType1() = < (As) `aaaa`, (Bs) `bbb` > := < [As] "aaaa", [Bs] "bbb" >;

test bool asType2() = < (As) `aaAA`, (Bs) `bbBB` > := < [As] "aaAA", [Bs] "bbBB" >;

int cntDS(D+ ds) = size([d | d <- ds ]);

test bool cntDS1() = cntDS(((DS) `d`).ds) == 1;
test bool cntDS2() = cntDS(((DS) `dd`).ds) == 2;
test bool cntDS3() = cntDS(((DS) `d d`).ds) == 2;

int cntES({E ","}+ es) = size([e | e <- es ]);

test bool cntES1() = cntES(((ES) `e`).args) == 1;
test bool cntES2() = cntES(((ES) `e,e`).args) == 2;
test bool cntES3() = cntES(((ES) `e ,e`).args) == 2;
test bool cntES4() = cntES(((ES) `e, e`).args) == 2;
test bool cntES5() = cntES(((ES) `e , e`).args) == 2;

/*

    @Test
    public void parseDSInModule(){
        prepareModule("M", "module M " +
                "import GrammarABCDE;" +
                "import ParseTree;" +
                "public DS ds = (DS)`d d d`;" +
                "public DS parseDS(str input) { return parse(#DS, input); }");
        prepareMore("import ParseTree;");
        prepareMore("import M;");
        assertTrue(runTestInSameEvaluator("parseDS(\"d d d\") == ds;"));
    }
    
    @Test @Ignore("not supported")
    public void singleA(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`a` := `a`;"));
    }
    
    @Test @Ignore("not supported")
    public void singleATyped(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("(A)`a` := `a`;"));
    }
    
    @Test @Ignore("not supported")
    public void singleAUnquoted1(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("a := `a`;"));
    }
    
    @Test(expected=UndeclaredVariable.class) @Ignore("not supported")
    public void singleAUnquoted2(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("a := a;"));
    }
    
    @Test @Ignore("not supported")
    public void AB(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`a b` := `a b`;"));
    }
    
    @Test @Ignore("not supported")
    public void ABspaces1(){ 
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`a b` := `a   b`;"));
    }
    
    @Test(expected=UnsupportedPattern.class) @Ignore("not supported")
    public void varAQuoted(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        runTestInSameEvaluator("`<someA>` := `a`;");
    }
    
    @Test @Ignore("not supported")
    public void varAassign(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        runTestInSameEvaluator("{someA := `a` && someA == `a`;}");
    }
    
    @Test @Ignore("not supported")
    public void varAQuotedTyped(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`<A someA>` := `a`;"));
    }
    
    @Test @Ignore("not supported")
    public void varAQuotedDeclaredBefore(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{A someA; (A)`<someA>` := `a`;}"));
    }
    
    @Test @Ignore("not supported") 
    public void VarATypedInsertAmbiguous(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{ `<A someA>` := `a` && someA == `a`; }"));
    }

    @Test @Ignore("not supported")
    public void VarATypedInsert(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{ `<A someA>` := `a` && someA == `a`; }"));
    }
    
    @Test @Ignore("not supported")
    public void ABvars1(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`a <someB>` := `a b`;"));
    }
    
    @Test @Ignore("not supported")
    public void ABvars1Typed(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`a <B someB>` := `a b`;"));
    }
    
    @Test @Ignore("not supported")
    public void ABvars2(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`<someA> <someB>` := `a b`;"));
    }

    @Test @Ignore("not supported")
    public void ABvars2Typed(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`<A someA> <B someB>` := `a b`;"));
    }
    
    @Test @Ignore("not supported")
    public void ABvars2TypedEq(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{`<A someA> <B someB>` := `a b` && someA ==`a` && someB == `b`;}"));
    }
    
    @Test @Ignore("not supported")
    public void ABvars2TypedInsertWithoutTypes(){ 
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{ `<A someA><B someB>` := `a b` &&  `<someA><someB>` == `a b`;}"));
    }
    
    @Test @Ignore("not supported")
    public void ABvars2TypedInsertWithTypes(){ 
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{ `<A someA><B someB>` := `a b` && (C)`<someA><someB>` == `a b`;}"));
    }
    
    @Test @Ignore("not supported")
    public void ABequal1(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`a b` == `a b`;"));
    }
    
    @Test @Ignore("not supported")
    public void ABequal5(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`a b` == `a  b`;"));
    }
    
    @Test @Ignore("not supported")
    public void D1(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`d` := `d`;"));
    }
    
    @Test @Ignore("not supported")
    public void D2(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`d d` := `d d`;"));
    }

    @Test @Ignore("not supported")
    public void D3(){
        prepare("import GrammarABCDE;");
        assertTrue(runTestInSameEvaluator("(DS)`d d` := `d d`;"));
    }

    @Test @Ignore("not supported")
    public void D4(){
        prepare("import GrammarABCDE;");
        assertTrue(runTestInSameEvaluator("`d d` := (DS)`d d`;"));
    }
    
    @Test(expected=UnsupportedPattern.class) @Ignore("not supported")
    public void Dvars(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`<Xs>` := `d d`;"));
    }
    
    @Test @Ignore("not supported")
    public void DvarsTyped(){
        prepare("import GrammarABCDE;");
        assertTrue(runTestInSameEvaluator("{ D+ Xs := (D+) `d d` && Xs == (D+) `d d`; }"));
    }
    
    @Test(expected=StaticError.class) @Ignore("not supported")
    public void DvarsTypedInsert2Untyped(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{ `<D+ Xs>` := `d`; }"));
    }
    
    @Test @Ignore("not supported")
    public void DvarsTypedInsert32(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{ `<D+ Xs>` := (D+) `d d`; }"));
    }
    
    @Test @Ignore("not supported")
    public void DvarsTypedInsert4UnTyped(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("(`d <D+ Xs>` := `d d`)  && `d <D+ Xs>` == `d d`;"));
    }
    
     @Test @Ignore("not supported")
    public void DvarsTypedInsert5Untyped(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{ `d <D+ Xs>` := `d d d` && `d <D+ Xs>` == `d d d`; }"));
    }
    
    @Test @Ignore("not supported")
    public void E1(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`e` := `e`;"));
    }
    
    @Test @Ignore("not supported")
    public void E2(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`e, e` := `e, e`;"));
    }
    
    @Test @Ignore("not supported")
    public void E2spaces1(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`e, e` := `e , e`;"));
    }
    
    @Test @Ignore("not supported")
    public void E2spaces2(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`e, e` := `e ,  e`;"));
    }
    
    @Test @Ignore("not supported")
    public void Evars1(){
        prepare("import GrammarABCDE;");
        assertTrue(runTestInSameEvaluator("{ Xs := `e, e` && Xs == `e, e`;}"));
    }
    
    @Test 
    @Ignore("Functionality subject to future/current change")
    public void Evar1Typed(){
        prepare("import GrammarABCDE;");
        assertTrue(runTestInSameEvaluator("{ {E \",\"}+ Xs := ({E \",\"}+) `e, e` && Xs == ({E \",\"}+)  `e, e`;}"));
    }
    
    @Test @Ignore("not supported") // (expected=AmbiguousConcretePattern.class)
    public void Evars2(){
        prepare("import GrammarABCDE;");
        assertTrue(runTestInSameEvaluator("{ `e, <Xs>` := `e, e` && Xs == `e`;}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void NoStarSubjectToPlusVar(){
        prepare("import GrammarABCDE;");
        assertFalse(runTestInSameEvaluator("{E \",\"}+ Xs := ({E \",\"}*) ``;"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void plusListShouldNotMatchEmptyList() {
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertFalse(runTestInSameEvaluator("({E \",\"}+) `e, <{E \",\"}+ Es>` := ({E \",\"}+) `e`;"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void starListPatternShouldMatchPlusListSubject() {
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{E \",\"}* Zs := ({E \",\"}+) `e, e`;"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void plusListPatternShouldMatchPStarListSubjectIfNotEmpty() {
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{E \",\"}+ Zs := ({E \",\"}*) `e, e`;"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void plusListPatternShouldNotMatchPStarListSubjectIfEmpty() {
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertFalse(runTestInSameEvaluator("{E \",\"}+ Zs := ({E \",\"}*) ``;"));
    }
    
    @Test @Ignore("not supported")
    public void emptyListVariablePatternShouldBeSplicedInbetweenSeparators() {
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`e, <{E \",\"}* Xs>, e` := `e, e`;"));
    }

    @Test
    @Ignore("Functionality subject to future/current change")
    public void emptyListVariablePatternShouldBeSplicedInbetweenSeparatorsAndBindToEmptyList() {
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("`e, <{E \",\"}* Xs>, e` := `e, e` && Xs == ({E \",\"}*) ``;"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void emptySepListShouldSpliceCorrectly(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{E \",\"}* Xs := ({E \",\"}*) `` && `e, <{E \",\"}* Xs>, e` == `e, e`;"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void Evars2Typed(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{ `e, <{E \",\"}+ Xs>` := `e, e` && Xs == ({E \",\"}+) `e`;}"));
    }
    
    @Test // (expected=AmbiguousConcretePattern.class)
    @Ignore("not supported")
    // @Ignore("needs to be reinstated when we have a type checker")
    public void Evars3(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{ `e, <Xs>` := `e, e` &&  Xs == `e` && `e, <Xs>` == `e, e`; }"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void Evars3Typed(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{ `e, <{E \",\"}+ Xs>` := `e, e` && Xs == ({E \",\"}+) `e` && ({E \",\"}+) `e, <{E \",\"}+ Xs>` == `e, e`; }"));
    }
    
    @Test // (expected=AmbiguousConcretePattern.class)
    @Ignore("needs to be reinstated when we have a type checker")
    public void Evars4(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{ `e, <Xs>` := `e` && Xs == ` ` && ` e, <Xs> ` == `e`; }"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void EvarsTyped(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{E \",\"}+ Xs := `e, e`;"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void EvarsTypedInsert1(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{ `<{E \",\"}+ Xs>` := `e, e` && `e, <{E \",\"}+ Xs>` == `e, e, e`; }"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void EvarsTypedInsert1Typed(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{ `<{E \",\"}+ Xs>` := `e, e` && `e, <{E \",\"}+ Xs>` == `e, e, e`; }"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void EvarsTypedInsert2(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{ `<{E \",\"}+ Xs>` := `e, e` && `e, <{E \",\"}+ Xs>` == `e, e, e`; }"));
    }
    
    @Test @Ignore("not supported")
    public void EvarsTypedInsert3(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{ `e, <{E \",\"}+ Xs>` := `e, e, e` && `e, <{E \",\"}+ Xs>` == `e, e, e`; }"));
    }
    
     @Test @Ignore("can not know that `d` should be a D+ list without a type checker")
    public void enumeratorDs1Untyped(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = [X | X <- `d` ]; L == [`d`];}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void enumeratorDs1Typed(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = [X | D X <- `d` ]; L == [ `d` ];}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void enumeratorDsUnyped(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = [X | X <- `d d d` ]; L == [`d`, `d`, `d`];}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void enumeratorDsTyped(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = [X | D X <- `d d d` ]; L == [`d`, `d`, `d`];}"));
    }
    
    @Test @Ignore("Can not know that `e` should be an E* or E+ list without a type-checker")
    public void enumeratorEs1Untyped(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = [X | X <- `e` ]; L == [ `e` ];}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void enumeratorEs1Typed(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = [X | E X <- `e` ]; L == [ `e` ];}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void enumeratorEsUntyped(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = [X | X <- `e, e, e` ]; L == [`e`, `e`, `e`];}"));
    }
    
    @Test
    @Ignore("Functionality subject to future/current change")
    public void enumeratorEsTyped(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("{L = [X | E X <- `e, e, e` ]; L == [`e`, `e`, `e`];}"));
    }

    @Test
    @Ignore("Functionality subject to future/current change")
    public void EvarsTypedInsert3Empty(){
        prepare("import GrammarABCDE;");
        prepareMore("import ParseTree;");
        assertTrue(runTestInSameEvaluator("` e, <{E \",\"}* Xs> ` := ({E \",\"}+) `e`;"));
    }
    
    */
