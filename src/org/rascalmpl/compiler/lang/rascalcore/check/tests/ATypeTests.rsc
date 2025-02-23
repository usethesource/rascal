@license{
Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::check::tests::ATypeTests

import lang::rascalcore::check::AType;
import List;
import Set;

// Remove artifacts that are only relevant for typepal internally
AType clean(AType t){
    return visit(t){
        case tvar(loc _) => aint()
        case aparameter(_, AType t) => t
        case lazyLub(list[AType] lst) => isEmpty(lst) ? avoid() : avalue()
        case \start(AType t) => t
        case  overloadedAType(rel[loc, IdRole, AType] overloads):
            if(isEmpty(overloads)){
                insert avoid();
            } else {
                fail;
            }
    }
}

bool asubtypeClean(AType x, AType y) = asubtype(clean(x), clean(y));
AType alubClean(AType x, AType y) = alub(clean(x), clean(y));
AType aglbClean(AType x, AType y) = aglb(clean(x), clean(y));

// asubtype

test bool asubtypeMax(AType x) = asubtypeClean(x, \avalue());
test bool asubtypeMin(AType x) = asubtypeClean(\avoid(), x);
test bool asubtypeReflexive(AType x) = asubtypeClean(x, x);

//test bool asubtypeAntisymmetric(AType x, AType y) { 
//    x1 = clean(x); y1 = clean(y);
//    if(asubtype(x1, y1)){
//        return x1 !:= y1 ? !asubtype(y1, x1) : true;
//    } else if(asubtype(y1, x1)){
//        return x1 !:= y1 ? !asubtype(x1, y1) : true;
//    } else
//        return true;
//}

@ignore{Issues to be studied}
test bool asubtypeTransitive(AType x, AType y, AType z){
    return  (asubtypeClean(x, y) && asubtypeClean(y, z)) ==> asubtypeClean(x, z);
}

// alub

test bool alubWithMin(AType x) = alubClean(\avoid(), x) == clean(x);
test bool alubWithMax(AType x) = alubClean(\avalue(), x) == \avalue();
// test bool alubCommutative(AType x, AType y) = alubClean(x, y) == alubClean(y, x);
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
test bool aglbWithMin(AType x) = aglbClean(\avoid(), x) == avoid();
test bool aglbWithMax(AType x) = aglbClean(\avalue(), x) == clean(x);
//test bool aglbCommutative(AType x, AType y) = aglbClean(x, y) == aglbClean(y, x);
//test bool aglConsistent(AType x, AType y){
//    x1 = clean(x);
//    y1 = clean(y);
//    z = aglb(x1, y1);
//    return asubtype(z, x1) && asubtype(z, y1);   
//}
//test bool aglbConsistentWithSubtype(AType x, AType y) = asubtypeClean(aglb(x, y), x) == asubtypeClean(aglb(x, y), y);