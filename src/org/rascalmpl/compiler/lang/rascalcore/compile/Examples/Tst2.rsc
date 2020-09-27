module lang::rascalcore::compile::Examples::Tst2

//public lrel[&T0,&T1] domainR (lrel[&T0,&T1] R, set[&T0] S)
//    = [ <V0, V1> | <&T0 V0, &T1 V1> <- R, V0 in S ];

//public lrel[&T0,&T1,&T2] domainR (lrel[&T0,&T1,&T2] R, set[&T0] S)
//    = [ <V0, V1, V2> | <&T0 V0, &T1 V1, &T2 V2> <- R, V0 in S ];
//
public lrel[&T0,&T1,&T2,&T3] domainR (lrel[&T0,&T1,&T2,&T3] R, set[&T0] S)
    = [ <V0, V1, V2, V3> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3> <- R, V0 in S ];

//public lrel[&T0,&T1,&T2,&T3,&T4] domainR (lrel[&T0,&T1,&T2,&T3,&T4] R, set[&T0] S)
//    = [ <V0, V1, V2, V3, V4> | <&T0 V0, &T1 V1, &T2 V2, &T3 V3, &T4 V4> <- R, V0 in S ];
    
    
///
public lrel[&T0,&T1] domainR (lrel[&T0,&T1] R, list[&T0] L)
    = [ <V0, V1> | &T0 V0 <- L, <V0, &T1 V1> <- R];

//public lrel[&T0,&T1,&T2] domainR (lrel[&T0,&T1,&T2] R, list[&T0] L)
//    = [ <V0, V1, V2> | &T0 V0 <- L, <V0, &T1 V1, &T2 V2> <- R];
//
public lrel[&T0,&T1,&T2,&T3] domainR (lrel[&T0,&T1,&T2,&T3] R, list[&T0] L)
    = [ <V0, V1, V2, V3> | &T0 V0 <- L, <V0, &T1 V1, &T2 V2, &T3 V3> <- R];

//public lrel[&T0,&T1,&T2,&T3,&T4] domainR (lrel[&T0,&T1,&T2,&T3,&T4] R, list[&T0] L)
//    = [ <V0, V1, V2, V3, V4> | &T0 V0 <- L, <V0, &T1 V1, &T2 V2, &T3 V3, &T4 V4> <- R];
