module lang::rascalcore::compile::Examples::Tst4
import Type;
value main() = subtype(\int(), \num());

//test bool everyTypeCanBeReifiedWithoutExceptions(&T u) = _ := typeOf(u);
//
//test bool allConstructorsAreDefined() 
//  = (0 | it + 1 | /cons(_,_,_,_) := #P.definitions) == 7;
//
//test bool allConstructorsForAnAlternativeDefineTheSameSort() 
//  = !(/choice(def, /cons(label(_,def),_,_,_)) !:= #P.definitions);
//  
//test bool typeParameterReificationIsStatic1(&F _) = #&F.symbol == \parameter("F",\value());
//test bool typeParameterReificationIsStatic2(list[&F] _) = #list[&F].symbol == \list(\parameter("F",\value()));
//
//@ignore{issue #1007}
//test bool typeParameterReificationIsStatic3(&T <: list[&F] f) = #&T.symbol == \parameter("T", \list(\parameter("F",\value())));
//
//test bool dynamicTypesAreAlwaysGeneric(value v) = !(type[value] _ !:= type(typeOf(v),()));
//
//// New tests which can be enabled after succesful bootstrap
//data P(int size = 0);
//
//@ignore{Does not work after changed TypeReifier in compiler}
//test bool allConstructorsHaveTheCommonKwParam()
//  =  all(/choice(def, /cons(_,_,kws,_)) := #P.definitions, label("size", \int()) in kws);
//   
//@ignoreCompiler{Does not work after changed TypeReifier in compiler}  
//test bool axiomHasItsKwParam()
//  =  /cons(label("axiom",_),_,kws,_) := #P.definitions && label("mine", \adt("P",[])) in kws;  
//
//@ignore{Does not work after changed TypeReifier in compiler}  
//test bool axiomsKwParamIsExclusive()
//  =  all(/cons(label(!"axiom",_),_,kws,_) := #P.definitions, label("mine", \adt("P",[])) notin kws);
//  
  
  


//import List;
//test bool listCount1(list[int] L){
//   int cnt(list[int] L){
//    int count = 0;
//    while ([int _, *int _] := L) { 
//           count = count + 1;
//           L = tail(L);
//    }
//    return count;
//  }
//  return cnt(L) == size(L);
//}
//
//value main()= listCount1([-8,1121836232,-5,0,1692910390]);


//test bool testSimple1() 
//    = int i <- [1,4] && int j <- [2,1] && int k := i + j && k >= 5;
//
//value main() = testSimple1();

//int f(list[int] ds){
//    if([int xxx]:= ds, xxx > 0){
//        return 1;
//    } else {
//        return 2;
//    }
//}
//value main() = /*testSimple1() && */f([1]) == 1;
