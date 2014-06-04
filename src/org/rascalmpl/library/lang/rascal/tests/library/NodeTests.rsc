module lang::rascal::tests::library::NodeTests

import Node;
import ValueIO;
import IO;

// arity

data XNODE = xf() | xf(int) | xf(int,int) | xf(int,int,int);

test bool arity1() = arity(xf()) == 0;

test bool arity2() = arity(xf(1)) == 1;

test bool arity3() = arity(xf(1,2)) == 2;

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

/*TODO: clean up tmp */

private bool textWriteRead(type[&T] typ, str termString, value termValue){
   tmp = |file:///tmp/xxx|;
   writeFile(tmp, termString);
   try {
        if(readTextValueFile(typ, tmp) == termValue) return true;
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

test bool readTermFromFileFun1() = textWriteRead(#node, "f()",  "f"());

test bool readTermFromFileFunWithArgs1() = textWriteRead(#node, "f(1,2,3)",  makeNode("f",1,2,3));

test bool readTermFromFileFunWithArgs2() = textWriteRead(#node, "f(1,2,3)",  "f"(1,2,3));

data FUN = f(int A, int B, int C);

test bool readTermFromFileADT1() = textWriteRead(#FUN, "f(1,2,3)",  FUN::f(1,2,3));

test bool readTermFromFileADT2() = textWriteRead(#FUN, "f(1,2,3)",  f(1,2,3));


test bool toStringTest() { node n = "f"(1, 2, 3); return toString(n) == "\"f\"(1,2,3)";}
    
