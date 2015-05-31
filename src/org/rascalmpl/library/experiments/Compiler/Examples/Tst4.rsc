module experiments::Compiler::Examples::Tst4

import Node;

public &T delAnnotationsRec1(&T v) = visit(v) { 
     case node n => delAnnotations(n) 
  };

data ANODE = leaf(int n) | a(ANODE left, ANODE right);
anno int ANODE @ pos;
anno str ANODE @ label;

public ANODE A1 = leaf(3);
public ANODE A2 = leaf(3)[@pos = 1][@label="a"];

//test bool delAnnotationsRec3() = !delAnnotationsRec(A2)@pos?;
//test bool delAnnotationsRec4() = !delAnnotationsRec(A2)@label?;

value main(list[value] args) = !delAnnotationsRec1(A2)@pos?;