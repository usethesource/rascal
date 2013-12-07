module GR8

declares "cons(adt(\"E\",[]),\"E_\",[label(\"child1\",adt(\"E\",[])),label(\"child2\",adt(\"LIT\",[])),label(\"child3\",adt(\"E\",[]))])"
declares "cons(adt(\"E\",[]),\"E_\",[label(\"child\",adt(\"LIT\",[]))])"

declares "cons(adt(\"LIT\",[]),\"LIT_\",[label(\"child\",str())])"

coroutine A[5,cs,bcs,conts,iSubject,rTree,rI,
              id] {
    
    id = "A_" + prim("value_to_string",deref rI);
    if(prim("elm_in_list",bcs,id)) {
        bcs_size = size(bcs);
        j = 0;
        while(j < bcs_size) {
            
        };
        exhaust;
    };
    
    // Register the coroutine instance
    if(is_defined(cs)) {
        muprim("mset_destructive_add_elm", cs, id);    
    } else {
        cs = make_mset();
        muprim("mset_destructive_add_elm", cs, id);
    };
    
    if(is_defined(bs)) {
        muprim("mset_destructive_add_elm", bcs, create(LIT,"e"));
    } else {
        bcs = make_mset();
        muprim("mset_destructive_add_elm", bcs, create(LIT,"e"));
    };
    
    c_lit = create(LIT,"c");
    if(is_defined(conts)) {
        conts = prim("list_add_elem",conts,c_lit);
    } else {
        conts = prim("list_create");
        conts_plus = prim("list_add_elem",conts,c_lit);
    };
    
    b = init(create(B,cs,bcs,conts,iSubject,rI,ref tree1));
    c_lit = init(c_lit,iSubject,rI,ref tree2);
    
    
    
    return;
}

coroutine ALL[1, parsers, 
                 len,j,co] {
    len = size_array(parsers);
    j = 0;
    while(j >= 0) {
        co = get_array(parsers,j);
        if(next(co)) {
            if(j == len - 1) {
                yield;
            } else {
                j = j + 1;
            };
        } else {
            j = j - 1; 
        };
    };
}

coroutine OR[1, parsers, 
                len,j,co] {
    len = size_array(parsers);
    j = 0;
    while(j < len) {
        co = get_array(parsers,j);
        while(next(co)) {
            yield;
        };
        j = j + 1; 
    };
}


coroutine LIT[4,iLit,rI,rTree,iSubject,
                index,lit] {
    guard deref rI < size(iSubject);
    index = deref rI;
    lit = muprim("subscript_str_mint",iSubject, deref rI);
    if(equal(lit,iLit)) {
        yield(1 + deref rI,cons LIT_(iLit));
    };
    // Un-do upon a failure
    deref rI = index;
}