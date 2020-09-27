module lang::rascalcore::compile::Examples::Tst2

//public lrel[&T0,&T1,&T2,&T3] domainR (lrel[&T0,&T1,&T2,&T3] R, set[&T0] S)
//    = [ <V0, V1, V2, V3> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3> <- R, V0 in S ];    
//    
/////
//public lrel[&T0,&T1] domainR (lrel[&T0,&T1] R, list[&T0] L)
//    = [ <V0, V1> | &T0 V0 <- L, <V0, &T1 V1> <- R];
//
//public lrel[&T0,&T1,&T2,&T3] domainR (lrel[&T0,&T1,&T2,&T3] R, list[&T0] L)
//    = [ <V0, V1, V2, V3> | &T0 V0 <- L, <V0, &T1 V1, &T2 V2, &T3 V3> <- R];


// Make a triple of lists from a list of triples.
public tuple[list[&T],list[&U],list[&V]] unzip(list[tuple[&T,&U,&V]] lst) =
    <[t | <t,_,_> <- lst], [u | <_,u,_> <- lst], [w | <_,_,w> <- lst]>;
    
public tuple[list[&T],list[&U]] unzip(list[tuple[&T,&U]] lst) =
    <[t | <t,_> <- lst], [u | <_,u> <- lst]>;

