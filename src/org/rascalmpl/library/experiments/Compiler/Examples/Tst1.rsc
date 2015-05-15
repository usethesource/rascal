module experiments::Compiler::Examples::Tst1

syntax XorY = x : "x" | y : "y";

test bool concreteSwitch5(){
  switch([XorY] "y"){
    case (XorY) `x`: throw "fail to due extra match"; 
    case y():  return true;
  }
  throw "fail due to missing match";
}
