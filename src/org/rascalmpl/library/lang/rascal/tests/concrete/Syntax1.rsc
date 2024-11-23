module lang::rascal::tests::concrete::Syntax1

import Exception;
import ParseTree;
import IO;

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

test bool parseDSfromFile() {
    writeFile(|tmp:///DS.trm|, "d d d");
    return (DS)`d d d` := parse(#DS, |tmp:///DS.trm|);
}  

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