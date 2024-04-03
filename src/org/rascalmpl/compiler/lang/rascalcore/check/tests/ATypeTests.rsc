module lang::rascalcore::check::tests::ATypeTests

import lang::rascalcore::check::AType;

// Remove artifacts that are only relevant for typepal internally
AType clean(AType t){
    return visit(t){
        case tvar(loc _) => aint()
        case lazyLub(list[AType] _) => avalue()
        case overloadedAType({}) => avalue()
    }
}

bool asubtypeClean(AType x, AType y) = asubtype(clean(x), clean(y));
AType alubClean(AType x, AType y) = alub(clean(x), clean(y));
AType aglbClean(AType x, AType y) = aglb(clean(x), clean(y));

// asubtype

//test bool asubtypeMax(AType x) = asubtypeClean(x, \avalue());
//test bool asubtypeMin(AType x) = asubtypeClean(\avoid(), x);
//test bool asubtypeReflexive(AType x) = asubtypeClean(x, x);
value main() = asubtype(avalue(), iter(avalue()));

//test bool asubtypeAntisymmetric(AType x, AType y) { 
//    x1 = clean(x); y1 = clean(y);
//    if(asubtype(x1, y1)){
//        return x1 !:= y1 ? !asubtype(y1, x1) : true;
//    } else if(asubtype(y1, x1)){
//        return x1 !:= y1 ? !asubtype(x1, y1) : true;
//    } else
//        return true;
//}
    
test bool asubtypeTransitive(AType x, AType y, AType z) = (asubtypeClean(x, y) && asubtypeClean(y, z)) ==> asubtypeClean(x, z);

//// alub
//
//test bool alubWithMin(AType x) = alubClean(\avoid(), x) == clean(x);
//test bool alubWithMax(AType x) = alubClean(\avalue(), x) == \avalue();
//test bool alubCommutative(AType x, AType y) = alubClean(x, y) := alubClean(y, x);
//
//test bool lubConsistent(AType x, AType y){
//    x1 = clean(x);
//    y1 = clean(y);
//    z = alub(x1, y1);
//    return asubtype(x1, z) && asubtype(y1, z);   
//}
//
//// aglb
//
//test bool aglbWithMin(AType x) = aglbClean(\avoid(), x) == avoid();
//test bool aglbWithMax(AType x) = aglbClean(\avalue(), x) == clean(x);
//test bool aglbCommutative(AType x, AType y) = aglbClean(x, y) == aglbClean(y, x);
//test bool aglConsistent(AType x, AType y){
//    x1 = clean(x);
//    y1 = clean(y);
//    z = aglb(x1, y1);
//    return asubtype(z, x1) && asubtype(z, y1);   
//}
//test bool aglbConsistentWithSubtype(AType x, AType y) = asubtypeClean(aglb(x, y), x) == asubtypeClean(aglb(x, y), y);