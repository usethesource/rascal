module lang::rascalcore::compile::Examples::Tst1

syntax XYZ = "x" | "y" | "z";


int f((XYZ) `x`) = 1;
int f((XYZ) `y`) = 2;
//int f((XYZ) `z`) = 3;
  
value main(){      
    //return f((XYZ)`x`);
    return f((XYZ)`y`);
    //return f((XYZ)`z`)];
}