//@bootstrapParser
module lang::rascalcore::compile::Examples::Tst2

//data AType
//    = anode(list[AType] fields)
//    | aparameter(str pname, AType bound) 
//    ;
//
////map[str, int] propagateParams(map[str,AType] typeVarBounds){
////    return (tvname : (aparameter(tvname2, _) := typeVarBounds[tvname])
////            ? 10 //typeVarBounds[tvname2] 
////            : 20 //typeVarBounds[tvname]
////            | tvname <- typeVarBounds);
////}
//
//bool propagateParams(map[str,AType] typeVarBounds){
//    return aparameter(tvname2, _) := typeVarBounds["T"];
//}
//
//value main() = propagateParams(("T" : anode([])));

data Symbol = sym() | label(str name, Symbol s);

private list[Symbol] stripLabels(list[Symbol] l) = [ (label(_,v) := li) ? v : li | li <- l ]; 

value main() = stripLabels([ sym(), label("L", sym()) ]);