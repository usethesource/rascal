module EnumTree

// data BinTree = leaf(str n) | pair(BinTree left, str n, BinTree right);
declares "cons(adt(\"BinTree\",[]),\"leaf\",[label(\"n\",str())])"
declares "cons(adt(\"BinTree\",[]),\"pair\",[label(\"lhs\",adt(\"BinTree\",[])),label(\"n\",str()),label(\"rhs\",adt(\"BinTree\",[]))])"

function enumTree[2,^tree,
					nd, // a node value to be yielded to the caller (reference)
					pat1,pat2,
					case1,case2,
					^n,^lhs,^rhs,
					matched] {
	pat1  = create(Library::MATCH_CALL_OR_TREE::2,[ create(Library::MATCH_LITERAL::2,"leaf"), 
													create(Library::MATCH_VAR::2, ref ^n) ]);
	pat2  = create(Library::MATCH_CALL_OR_TREE::2,[ create(Library::MATCH_LITERAL::2,"pair"), 
													create(Library::MATCH_VAR::2, ref ^lhs), 
													create(Library::MATCH_VAR::2, ref ^n),
													create(Library::MATCH_VAR::2, ref ^rhs) ]);
	matched = false;
	case1 = init(create(Library::MATCH::2,pat1,^tree));
	if(hasNext(case1)) {
	    if(next(case1)) {
	    	// The body of the first case
	        matched = true;
		    deref nd = ^n;
	        yield true;
	    };
	};
	if(matched) {
	    // continue
	} else { 
	    case2 = init(create(Library::MATCH::2,pat2,^tree));
	    if(hasNext(case2)) {
	    	if(next(case2)) {
	    	    // The body of the second case
	    	    enumTree(^lhs,nd);
	            deref nd = ^n;
	    	    yield true;
	    	    enumTree(^rhs,nd);
	    	};
	    };
	};
	
    return false;
}

function main[1,args,^subject,co,^child] {
	^subject = cons pair(cons pair(cons leaf("1"), "2", cons leaf("3")), "4", cons pair(cons leaf("5"), "6", cons leaf("7")));
	co = init(create(enumTree, ^subject, ref ^child));
	while(hasNext(co)) {
	    if(next(co)) {
		    println("Child: ", ^child);
		};
	};
	return "999";
}