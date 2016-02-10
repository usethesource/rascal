@bootstrapParser
module experiments::Compiler::Examples::Tst1

//import Relation;
//import lang::rascal::types::AbstractName;
//import lang::rascal::\syntax::Rascal;
//import lang::rascal::meta::ModuleInfo;
//import lang::rascal::types::TypeSignature;
//
//public rel[RName mname, bool isext] getDefaultImports() {
//    return { < RSimpleName("Exception"), false > };
//}

value main(){
    //map[RName, list[Import]] importLists = ( );
    //RName mn;
    
    //{
    //(Import)`extend <ImportedModule _>;` := importItem |
    //  importItem <- importLists[mn]
    //};
    
    //L = [1,2,3];
    
    { x := 1 | x <- [1,2,3]};
}

//import lang::rascal::tests::functionality::SetMatchTests1;
//
//// Anastassija's type constraint examples
//
//// Version 4; with overloaded constructor INTERSECT , and non-linear constraints (tset)
//        
//public TYPESET INTERSECT({ SUBTYPES(INTERSECT({ TYPESET tset, *TYPESET rest})), tset, *TYPESET rest1 }) {
//    return INTERSECT({ SUBTYPES(INTERSECT(rest)), tset, *rest1 });
//}
//     
//value main() = 
//   INTERSECT({ SUBTYPES(INTERSECT({  })), SET("s1") })
//    ==
//   INTERSECT({ SUBTYPES(INTERSECT({  })), SET("s1") })
//   ;
   
//data D = d1(int n, str s) | d2(str s, bool b) | d3(list[int] l, list[int] r);
//value main() =  d1(3, "a"); // >= d1(2, "a");



//[*int x] := [1,2,3,4,5];