module lang::rascalcore::compile::Examples::Tst1

import Node;

// delAnnotation

data ANODE = leaf(int n) | a(ANODE left, ANODE right);
anno int ANODE@pos;
anno str ANODE@label;

public ANODE A1 = leaf(3);
public ANODE A2 = leaf(3)[@pos = 1][@label="a"];

value main() //test bool delAnnotationsRec3() 
    = !delAnnotationsRec(A2)@pos?;
    
    