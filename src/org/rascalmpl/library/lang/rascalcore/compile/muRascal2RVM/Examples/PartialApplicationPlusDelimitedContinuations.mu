module PartialApplicationPlusDelimitedContinuations

declares "cons(adt(\"Gen\",[]),\"NEXT\",[ label(\"cont\",func(\\value(),[])) ])"
declares "cons(adt(\"Gen\",[]),\"EXHAUSTED\",[])"

function GENNUM[4,start,end,step,rRes] {
     if(end < start) {
         return cons EXHAUSTED();
     };
     while(start < (end + step)) {
         shift(cons NEXT(bind(GENNUM::4::SHIFT_CLOSURE::3,cont,rRes,start)));
         start = start + step;
     };
     return cons EXHAUSTED();
}

function GENNUM::4::SHIFT_CLOSURE[3,k,rRes,start] {
    deref rRes = start;
    return k();
}

function MAIN[2,args,kwargs,res1,res2,gen1,gen2,continue,f1,f2] {
    gen1 = reset(bind(GENNUM, 0, 100, 10, ref res1));
    gen2 = reset(bind(GENNUM, 0, 100, 20, ref res2));
    continue = true;
    while(continue) {
        if(Library::NEXT::1(gen1)) {
            gen1 = prim("adt_field_access",gen1,"cont")();
            if(Library::NEXT::1(gen2)) {
                gen2 = prim("adt_field_access",gen2,"cont")();
                println(res1,"; ",res2);
            } else {
                continue = false;
            };
        } else {
            continue = false;
        };
    };
    return "DONE!";
}