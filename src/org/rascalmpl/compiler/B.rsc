module B


import ParseTree;

//syntax B = 'b'; 
//
//void X(B t) { 
//    if (t.src != |unkown:///|) { // gaat goed
//        throw "";
//    }
//}

void X(Tree t2) {
    if (t2.src != |unkown:///|) { // error
        throw "";
    }
}