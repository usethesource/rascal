module lang::rascal::tests::library::Node

import Node;
import ValueIO;
import IO;
import util::UUID;

// arity

data XNODE = xf() | xf(int) | xf(int,int) | xf(int,int,int);

test bool arity1() = arity(xf()) == 0;

test bool arity2() = arity(xf(1)) == 1;

test bool arity3() = arity(xf(1,2)) == 2;

// delAnnotation

data ANODE = leaf(int n) | a(ANODE left, ANODE right);
anno int ANODE@pos;
anno str ANODE@label;

public ANODE A1 = leaf(3);
public ANODE A2 = leaf(3)[@pos = 1][@label="a"];
public ANODE A3 = a(leaf(10)[@pos = 1][@label="a"], leaf(20)[@pos=2][@label="b"])[@pos=3][@label="c"];

test bool delAnnotation1() = !delAnnotation(A1, "pos")@pos?;
test bool delAnnotation2() = !delAnnotation(A2, "pos")@pos?;
test bool delAnnotation3() = delAnnotation(A2, "pos")@label == "a";
test bool delAnnotation4() = !delAnnotation(A3, "pos")@pos?;
test bool delAnnotation5() = delAnnotation(A3, "pos")@label == "c";


// delAnnotations
test bool delAnnotations1() = !delAnnotations(A1)@pos?;
test bool delAnnotations2() = !delAnnotations(A1)@label?;

test bool delAnnotations3() = !delAnnotations(A2)@pos?;
test bool delAnnotations4() = !delAnnotations(A2)@label?;

test bool delAnnotations5() = !delAnnotations(A3)@pos?;
test bool delAnnotations6() = !delAnnotations(A3)@label?;

test bool delAnnotations7() = ANODE n := delAnnotations(A3)[0] && n@pos == 1;
test bool delAnnotations8() = ANODE n := delAnnotations(A3)[0] && n@label == "a";

test bool delAnnotations9() = ANODE n := delAnnotations(A3)[1] && n@pos == 2;
test bool delAnnotations10() = ANODE n := delAnnotations(A3)[1] && n@label == "b";


// delAnnotationsRec
test bool delAnnotationsRec1() = !delAnnotationsRec(A1)@pos?;
test bool delAnnotationsRec2() = !delAnnotationsRec(A1)@label?;

test bool delAnnotationsRec3() = !delAnnotationsRec(A2)@pos?;
test bool delAnnotationsRec4() = !delAnnotationsRec(A2)@label?;

test bool delAnnotationsRec5() = !delAnnotationsRec(A3)@pos?;
test bool delAnnotationsRec6() = !delAnnotationsRec(A3)@label?;

test bool delAnnotationsRec7() = ANODE n := delAnnotationsRec(A3)[0] && !n@pos?;
test bool delAnnotationsRec8() = ANODE n := delAnnotationsRec(A3)[0] && !n@label?;

test bool delAnnotationsRec9() = ANODE n := delAnnotationsRec(A3)[1] && !n@pos?;
test bool delAnnotationsRec10() = ANODE n := delAnnotationsRec(A3)[1] && !n@label?;

// getAnnotations
test bool getAnnotations1() = getAnnotations(A1) == ();

test bool getAnnotations2() = getAnnotations(A2) == ("pos": 1, "label":"a");

test bool getAnnotations3() = getAnnotations(A3) ==("pos": 3, "label": "c");

// getChildren

data YNODE = yf() | yf(int) | yf(int,int) | yf(int,int,int);

test bool getChildren1() = getChildren(yf()) == [];

test bool getChildren2() = getChildren(yf(1)) == [1];

test bool getChildren3() = getChildren(yf(1,2)) == [1,2];

// getName

data ZNODE = zf() | zf(int) | zf(int,int) | zf(int,int,int);

test bool getName1() = getName(zf()) == "zf";
test bool getName2() = getName(zf(1,2,3)) == "zf";

// makeNode

test bool makeNode1() {node n = makeNode("f"); return getName(n) == "f" && arity(n) == 0 && getChildren(n) == []; }

test bool makeNode2() {node n = makeNode("f", 1); return getName(n) == "f" && arity(n) == 1 && getChildren(n) == [1];}

test bool makeNode3() {node n = makeNode("f", 1, 2); return getName(n) == "f" && arity(n) == 2 && getChildren(n) == [1,2];}

test bool makeNode4() {node n = makeNode("f", 1, 2, 3); return getName(n) == "f" && arity(n) == 3 && getChildren(n) == [1,2,3];}


// setAnnotations

test bool setAnnotations1() = setAnnotations(leaf(3), ()) == leaf(3);
test bool setAnnotations2() = setAnnotations(leaf(3), ("pos": 1, "label":"a"))@pos == 1;
test bool setAnnotations3() = setAnnotations(leaf(3), ("pos": 1, "label":"a"))@label == "a";

// unset

public node FG = "f"(1, "g"(2, kwg1="a", kwg2=10, com=15), kwf1=true, kwf2="z", com=15);

test bool unsetKW1() = unset(FG) == "f"(1, "g"(2, kwg1="a", kwg2=10, com=15));

test bool unsetSelKW1() = unset(FG, "kwf1") == "f"(1, "g"(2, kwg1="a", kwg2=10, com=15), kwf2="z", com=15);

test bool unsetSelKW2() = unset(FG, {}) == FG;
test bool unsetSelKW3() = unset(FG, {"kwf1", "kwf2"}) == "f"(1, "g"(2, kwg1="a", kwg2=10, com=15), com=15);
test bool unsetSelKW4() = unset(FG, {"kwf1"}) == "f"(1, "g"(2, kwg1="a", kwg2=10, com=15), kwf2="z", com=15);
test bool unsetSelKW5() = unset(FG, {"kwf2"}) == "f"(1, "g"(2, kwg1="a", kwg2=10, com=15),  kwf1=true, com=15);
test bool unsetSelKW6() = unset(FG, {"com"}) == "f"(1, "g"(2, kwg1="a", kwg2=10, com=15),  kwf1=true, kwf2="z");


// unsetRec

test bool unsetRecKW1() = unsetRec(FG) == "f"(1, "g"(2));

test bool unsetRecSelKW1() = unsetRec(FG, "kwf1") == "f"(1, "g"(2, kwg1="a", kwg2=10, com=15), kwf2="z", com=15);
test bool unsetRecSelKW2() = unsetRec(FG, {}) == FG;
test bool unseRecSelKW3() = unsetRec(FG, {"kwf1", "kwf2"}) == "f"(1, "g"(2, kwg1="a", kwg2=10, com=15), com=15);
test bool unseRecSelKW4() = unsetRec(FG, {"kwf1"}) == "f"(1, "g"(2, kwg1="a", kwg2=10, com=15), kwf2="z", com=15);
test bool unsetRecSelKW5() = unsetRec(FG, {"kwf2"}) == "f"(1, "g"(2, kwg1="a", kwg2=10, com=15),  kwf1=true, com=15);
test bool unsetRecSelKW6() = unsetRec(FG, {"com"}) == "f"(1, "g"(2, kwg1="a", kwg2=10),  kwf1=true, kwf2="z");
test bool unsetRecSelKW7() = unsetRec(FG, {"kwg1", "com"}) == "f"(1, "g"(2, kwg2=10),  kwf1=true, kwf2="z");


// kw args

data KNODE(int x = 2) = z() | y();

test bool setKW1() = setKeywordParameters(z(), ("x" : 4)) == z(x=4);
test bool setKW2() = setKeywordParameters(z(x=3), ("x" : 4)) == z(x=4);
test bool setKW3() = setKeywordParameters(z(x=3), ("x" : 4)).x == 4;



/*TODO: clean up tmp */

private bool textWriteRead(type[&T] typ, str termString, value termValue){
   tmp = |memory://test-tmp/xxx-node-<"<uuidi()>">.test|; 
   writeFile(tmp, termString);
   try {
        return readTextValueFile(typ, tmp) == termValue;
   } catch:
        return false;
}

test bool readTermFromFileInt1() = textWriteRead(#node, "f(1)", makeNode("f", 1));

test bool readTermFromFileInt2() = textWriteRead(#node, "f(1)", "f"(1));

test bool readTermFromFileStr1() = textWriteRead(#node, "f(\"abc\")",  makeNode("f", "abc"));

test bool readTermFromFileStr2() = textWriteRead(#node, "f(\"abc\")",  "f"("abc"));

test bool readTermFromFileList1() = textWriteRead(#node, "f([1,2,3])",  makeNode("f", [[1,2,3]]));

test bool readTermFromFileList2() = textWriteRead(#node, "f([1,2,3])", "f"([1,2,3]));

test bool readTermFromFileFun1() = textWriteRead(#node, "f()",  makeNode("f"));

test bool readTermFromFileFun2() = textWriteRead(#node, "f()",  "f"());

test bool readTermFromFileFunWithArgs1() = textWriteRead(#node, "f(1,2,3)",  makeNode("f",1,2,3));

test bool readTermFromFileFunWithArgs2() = textWriteRead(#node, "f(1,2,3)",  "f"(1,2,3));

data FUN = f(int A, int B, int C);

test bool readTermFromFileADT1() = textWriteRead(#FUN, "f(1,2,3)",  FUN::f(1,2,3));

test bool readTermFromFileADT2() = textWriteRead(#FUN, "f(1,2,3)",  f(1,2,3));


test bool toStringTest() { node n = "f"(1, 2, 3); return toString(n) == "\"f\"(1,2,3)";}
    
test bool kwParamsInfluenceUnEqual() = ""() != ""(n=1);

test bool kwParamsInfluenceEqual() = !(""() == ""(n=1)); 

test bool kwParamsInfluenceSetSemantics() = (0 | it + 1 | _ <- {""(), ""(n=1)}) == 2;  

test bool kwParamsDoNotInfluenceMatch() {
   node a = ""();
   node b = ""(n=1);
   return a := b && b := a;
}

test bool kwParamsDoNotInfluenceNoMatch() {
   node a = ""();
   node b = ""(n=1);
   return !(a !:= b) && !(b !:= a);
}

test bool keywordParametersAreFields() {
   node a = "x"(age=1);
   return a.age == 1 && a[age=2].age == 2;
}
