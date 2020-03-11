module lang::rascal::tests::concrete::Call

syntax XYZ = "x" | "y" | "z";

test bool  dispatchTest3() { 
    int f((XYZ) `x`) = 1;
    int f((XYZ) `y`) = 2;
    int f((XYZ) `z`) = 3;
        
    return [f((XYZ)`x`),f((XYZ)`y`),f((XYZ)`z`)] == [1,2,3];
}