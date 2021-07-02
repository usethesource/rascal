module lang::rascalcore::compile::Examples::Tst1


layout Whitespace = [\ \t\n]*;
syntax A = "a";

syntax P[&T] = "{" &T par "}";

syntax PA = P[A];

value main(){
    PA pa = [PA] "{a}";
    return pa;
}

//import lang::rascal::\syntax::Rascal;

//import lang::rascalcore::compile::Examples::Tst2;
//import List;

//test bool lexicalMatch1() = (Name) `a` := [Name] "a";
//test bool lexicalMatch2() = (Name) `ab` := [Name] "ab";

//test bool lexicalMatch3() = (QualifiedName) `a` := [QualifiedName] "a";
//test bool lexicalMatch4() = (QualifiedName) `ab` := [QualifiedName] "ab";
//
//test bool lexicalMatch5() = (Expression) `a` := [Expression] "a";
//test bool lexicalMatch6() = (Expression) `ab` := [Expression] "ab";
//
//int cntStats(Statement* stats) = size([s | s <- stats ]);
//
//test bool cntStats1() = cntStats(((Expression) `{x=1;}`).statements) == 1;
//test bool cntStats2() = cntStats(((Expression) `{x=1;x=2;}`).statements) == 2;



//syntax Assoc
//    = left: "left"
//    ;
//    
//data Associativity 
//    = \left()
//    ;
//
//value main() = 3; //\left();

//syntax As = "a"* as;
//
//value main() //test bool hasConcrete1() 
//    = ([As] "aaa") has as;


//value main() { //test bool stripLabels() {
//  return int _ <- [1,2,3];
//}



 
//syntax A = x: "a";
//syntax B = "b";
//
//
//value main() { //test bool concreteSwitch5(){
//    return x();
//}


// import ParseTree;

//syntax A = a: [a-z];
//
//value main() { //test bool testIs(){
//    //return ([A] "a").x;
//    pt = parse(#A, "a");
//   
//    return a() := pt && pt is a;
//}