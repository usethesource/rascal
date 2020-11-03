module lang::rascalcore::compile::Examples::Tst1

//import analysis::graphs::Graph;
//import List;
//import Set;
//import Relation;
//import IO;
//
//test bool sccDisjointComponents(Graph[int] G) {
//    components = stronglyConnectedComponents(G);
//    iprintln(components);
//    return isEmpty(G) || size(components) <= 1 || all(set[int] comp1 <- components, set[int] comp2 <- components, ((comp1 == comp2) ? true : isEmpty(comp1 & comp2)));
//}

//@javaClass{org.rascalmpl.library.Prelude}
//public java bool isEmpty(set[&T] st);

value main(){
    //components = {{3}, {8}};
    //b = all(set[int] comp1 <- components, set[int] comp2 <- components, ((comp1 == comp2) || isEmpty(comp1 & comp2)));
    //return b;
    return all(c <- [1,2], c == 1 ? true : c == 2);
    //c = 2;
    //return c == 1 ? true : c == 2;
}
