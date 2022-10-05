module lang::rascal::tests::concrete::Call

lexical ABC = "a" | "b" | "c";

syntax XYZ = "x" | "y" | "z";

//keyword KEY = "k" | "e"| | "y";

test bool  dispatchTestLexical() { 
    int f((ABC) `a`) = 1;
    int f((ABC) `b`) = 2;
    int f((ABC) `c`) = 3;
        
    return [f((ABC)`a`),f((ABC)`b`),f((ABC)`c`)] == [1,2,3];
}

test bool  dispatchTestSyntax() { 
    int f((XYZ) `x`) = 1;
    int f((XYZ) `y`) = 2;
    int f((XYZ) `z`) = 3;
        
    return [f((XYZ)`x`),f((XYZ)`y`),f((XYZ)`z`)] == [1,2,3];
}

//@ignoreCompiler{To be analyzed}
//test bool  dispatchTestKeyword() { 
//    int f((KEY) `k`) = 1;
//    int f((KEY) `e`) = 2;
//    int f((KEY) `y`) = 3;
//        
//    return [f((KEY)`k`),f((KEY)`e`),f((KEY)`y`)] == [1,2,3];
//}
