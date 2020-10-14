module lang::rascalcore::compile::muRascal2Java::SameJavaType

extend lang::rascalcore::check::CheckerCommon;

// Determine whether two atypes lead to the same Java type in the generated code

bool leadToSameJavaType(AType t1, AType t2) {
    r = leadToSameJavaType1(t1, t2);
    println("leadToSameJavaType(<t1>, <t2>) ==\> <r>");
    return r;
}

bool leadToSameJavaType1(alist(AType t1), alist(AType t2)) = !equivalent(t1, t2);
bool leadToSameJavaType1(aset(AType t1), aset(AType t2)) = !equivalent(t1, t2);
bool leadToSameJavaType1(abag(AType t1), abag(AType t2)) = !equivalent(t1, t2);
bool leadToSameJavaType1(arel(atypeList(list[AType] ts1)), arel(atypeList(list[AType] ts2))) = size(ts1) != size(ts2);
bool leadToSameJavaType1(arel(atypeList(list[AType] ts1)), aset(AType t2)) = true;
bool leadToSameJavaType1(aset(AType t1), arel(atypeList(list[AType] ts2))) = true;

bool leadToSameJavaType1(alrel(atypeList(list[AType] ts1)), alrel(atypeList(list[AType] ts2))) = size(ts1) != size(ts2);
bool leadToSameJavaType1(alrel(atypeList(list[AType] ts1)), alist(AType t1)) = true;
bool leadToSameJavaType1(alist(AType t1), alrel(atypeList(list[AType] ts1))) = true;

bool leadToSameJavaType1(atuple(atypeList(ts1)), atuple(atypeList(ts2))) = size(ts1) != size(ts2);

bool leadToSameJavaType1(amap(AType k1, AType v1), amap(AType k2, AType v2)) = !equivalent(k1,k2) || !equivalent(v1, v2);
bool leadToSameJavaType1(afunc(AType ret1, list[AType] formals1, list[Keyword] kwFormals1), afunc(AType ret2, list[AType] formals2, list[Keyword] kwFormals2))
    = size(formals1) == size(formals2) && (any(int i <- index(formals1), leadToSameJavaType(formals1[i], formals2[i]))/* || leadToSameJavaType(ret1, ret2) */);
    
bool leadToSameJavaType1(aparameter(str pname1, AType bound1), aparameter(str pname2, AType bound2)) = leadToSameJavaType(bound1, bound2);

bool leadToSameJavaType1(areified(AType atype1), areified(AType atype2)) = !equivalent(atype1, atype2);
bool leadToSameJavaType1(areified(AType atype1), aadt(str adtName1, list[AType] parameters1, SyntaxRole syntaxRole1)) = true;
bool leadToSameJavaType1(aadt(str adtName1, list[AType] parameters1, SyntaxRole syntaxRole1), areified(AType atype1)) = true;
bool leadToSameJavaType1(aadt(str adtName1, list[AType] parameters1, SyntaxRole syntaxRole1), aadt(str adtName2, list[AType] parameters2, SyntaxRole syntaxRole2)) = true;
    
default bool leadToSameJavaType1(AType t1, AType t2) = !equivalent(t1, t2);

bool leadToSameJavaType(atypeList(list[AType] elms1), atypeList(list[AType] elms2)) = size(elms1) != size(elms2) || any(i <- index(elms1), leadToSameJavaType(elms1[i], elms2[i]));
bool leadToSameJavaType(list[AType] elms1, list[AType] elms2) = size(elms1) != size(elms2) || any(i <- index(elms1), leadToSameJavaType(elms1[i], elms2[i]));
