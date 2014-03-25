module PartialApplicationPlusCoroutines

coroutine GENNUM[4,start,end,step,rRes] {
     guard end > start;
     while(start < (end + step)) {
         yield(start);
         start = start + step;
     };
}

function MAIN[2,args,kwargs,f,res1,res2,co1,co2,continue,f1,f2] {
    f = GENNUM(0,100);
    co1 = create(f, 10, ref res1);
    co2 = create(f, 20, ref res2);
    continue = true;
    while(continue) {
        if(next(co1)) {
            if(next(co2)) {
                println(res1,"; ",res2);
            } else {
                continue = false;
            };
        } else {
            continue = false;
        };
    };
    
    println("The same but using \"bind\":");
    
    f1 = bind(f, 10, ref res1);
    f2 = bind(f, 20, ref res2);
    co1 = create(f1);
    co2 = create(f2);
    continue = true;
    while(continue) {
        if(next(co1)) {
            if(next(co2)) {
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