module Library


/*


// ***** Descendent pattern ***

function MATCH_DESCENDANT[2, pat, ^subject, gen, cpat]{
   //println("MATCH_DESCENDANT", pat, ^subject);
   DO_ALL(create(MATCH_AND_DESCENT, pat),  ^subject);
   return false;
}

// ***** Match and descent for all types *****

function MATCH_AND_DESCENT[2, pat, ^val]{
  //println("MATCH_AND_DESCENT", pat, ^val);
  DO_ALL(pat, ^val);
  
  //println("MATCH_AND_DESCENT", "outer match completed"); 
  typeswitch(^val){
    case list:        DO_ALL(create(MATCH_AND_DESCENT_LIST, pat), ^val);
    case lrel:        DO_ALL(create(MATCH_AND_DESCENT_LIST, pat), ^val);
    case node:        DO_ALL(create(MATCH_AND_DESCENT_NODE, pat), ^val);
    case constructor: DO_ALL(create(MATCH_AND_DESCENT_NODE, pat), ^val);
    case map:         DO_ALL(create(MATCH_AND_DESCENT_MAP, pat), ^val);
    case set:         DO_ALL(create(MATCH_AND_DESCENT_SET, pat), ^val);
    case rel:         DO_ALL(create(MATCH_AND_DESCENT_SET, pat), ^val);
    case tuple:       DO_ALL(create(MATCH_AND_DESCENT_TUPLE, pat), ^val);
    default:          return false;
  };  
  return false;
}
*/
/*
function VISIT[1, visitor]{
   //println("VISIT", visitor);
   while(hasNext(visitor)){
        if(next(visitor)){
           if(hasNext(visitor)){
              yield true;
           } else {
             return true;
           };
        };
   }; 
   return false;     
}   
*//*
function MATCH_AND_DESCENT_LITERAL[2, pat, ^subject, res]{
  //println("MATCH_AND_DESCENT_LITERAL", pat, ^subject);
  if(equal(typeOf(pat), typeOf(^subject))){
     res = equal(pat, ^subject);
     return res;
  };
  
  return MATCH_AND_DESCENT(create(MATCH_LITERAL, pat), ^subject);
}

function MATCH_AND_DESCENT_LIST[2, pat, ^lst, last, i]{
   //println("MATCH_AND_DESCENT_LIST", pat, ^lst);
   last = size_list(^lst);
   i = 0;
   while(i < last){
      DO_ALL(pat, get_list ^lst[i]);
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_list ^lst[i]);
      i = i + 1;
   };
   return false;
}

function MATCH_AND_DESCENT_SET[2, pat, ^set, ^lst, last, i]{
   //println("MATCH_AND_DESCENT_SET", pat, ^set);
   ^lst = set2list(^set);
   last = size_list(^lst);
   i = 0;
   while(i < last){
      DO_ALL(pat, get_list ^lst[i]);
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_list ^lst[i]);
      i = i + 1;
   };
   return false;
}

function MATCH_AND_DESCENT_MAP[2, pat, ^map, ^klst, ^vlst, last, i]{
   ^klst = keys(^map);
   ^vlst = values(^map);
   last = size_list(^klst);
   i = 0;
   while(i < last){
      DO_ALL(pat, get_list ^klst[i]);
      DO_ALL(pat, get_list ^vlst[i]);
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_list ^klst[i]);
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_list ^vlst[i]);
      i = i + 1;
   };
   return false;
}

function MATCH_AND_DESCENT_NODE[2, pat, ^nd, last, i, ar]{
   ar = get_name_and_children(^nd);
   last = size_array(ar);
   i = 0; 
   while(i < last){
      DO_ALL(pat, get_array ar[i]);
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_array ar[i]);
      i = i + 1;
   };
   return false;
}

function MATCH_AND_DESCENT_TUPLE[2, pat, ^tup, last, i]{
   last = size_tuple(^tup);
   i = 0;
   while(i < last){
      DO_ALL(pat, get_tuple ^tup[i]);
      DO_ALL(create(MATCH_AND_DESCENT, pat),  get_tuple ^tup[i]);
      i = i + 1;
   };
   return false;
}

// ***** Regular expressions *****

function MATCH_REGEXP[3, ^regexp, varrefs, ^subject, matcher, i, varref]{
   matcher = muprim("regexp_compile", ^regexp, ^subject);
   while(muprim("regexp_find", matcher)){
     i = 0; 
     while(i < size_array(varrefs)){
        varref = get_array varrefs[i];
        deref varref = muprim("regexp_group", matcher, i + 1);
        i = i + 1;
     };
     yield true;
   };
   return false;
}

// ***** Traverse functions *****

function TRAVERSE_TOP_DOWN[5, phi, ^subject, hasMatch, beenChanged, rebuild, 
							  matched, changed] {
	matched = false; // ignored	
	changed = false;
	^subject = phi(^subject, ref matched, ref changed);
	if(rebuild) {
		deref beenChanged = changed || deref beenChanged;
		changed = false;
		^subject = VISIT_CHILDREN(^subject, Library::TRAVERSE_TOP_DOWN::5, phi, hasMatch, ref changed, rebuild);
		deref beenChanged = changed || deref beenChanged;	
		return ^subject;
	};
	return VISIT_CHILDREN_VOID(^subject, Library::TRAVERSE_TOP_DOWN::5, phi, hasMatch, ref changed, rebuild);
}

function TRAVERSE_TOP_DOWN_BREAK[5, phi, ^subject, hasMatch, beenChanged, rebuild, 
									matched, changed] {
	matched = false;
	changed = false;
	^subject = phi(^subject, ref matched, ref changed);
	deref beenChanged = changed || deref beenChanged;	
	if(deref hasMatch = matched || deref hasMatch) {	
		return ^subject;
	};
	if(rebuild) {
		changed = false;
		^subject = VISIT_CHILDREN(^subject, Library::TRAVERSE_TOP_DOWN_BREAK::5, phi, hasMatch, ref changed, rebuild);
		deref beenChanged = changed || deref beenChanged;
		return ^subject;
	};	
	return VISIT_CHILDREN_VOID(^subject, Library::TRAVERSE_TOP_DOWN_BREAK::5, phi, hasMatch, ref changed, rebuild);
}

function TRAVERSE_BOTTOM_UP[5, phi, ^subject, hasMatch, beenChanged, rebuild, 
							   matched, changed] {
	matched = false; // ignored
	changed = false;
	if(rebuild) {
		^subject = VISIT_CHILDREN(^subject, Library::TRAVERSE_BOTTOM_UP::5, phi, hasMatch, ref changed, rebuild);
		deref beenChanged = changed || deref beenChanged;
		changed = false;
	} else {
		VISIT_CHILDREN_VOID(^subject, Library::TRAVERSE_BOTTOM_UP::5, phi, hasMatch, ref changed, rebuild);
	};
	^subject = phi(^subject, ref matched, ref changed);
	deref beenChanged = changed || deref beenChanged;
	return ^subject;
}

function TRAVERSE_BOTTOM_UP_BREAK[5, phi, ^subject, hasMatch, beenChanged, rebuild, 
									 matched, changed] {
	matched = false;
	changed = false;
	if(rebuild) {
		^subject = VISIT_CHILDREN(^subject, Library::TRAVERSE_BOTTOM_UP_BREAK::5, phi, hasMatch, ref changed, rebuild);
		deref beenChanged = changed || deref beenChanged;
		changed = false;
	} else {
		VISIT_CHILDREN_VOID(^subject, Library::TRAVERSE_BOTTOM_UP_BREAK::5, phi, hasMatch, ref changed, rebuild);
	};		
	if(deref hasMatch) {	
		return ^subject;
	};
	^subject = phi(^subject, ref matched, ref changed);
	deref hasMatch = matched || deref hasMatch;
	deref beenChanged = changed || deref beenChanged;	
	return ^subject;
}

function VISIT_CHILDREN[6, ^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild, 
						   children] {
	if((^subject is list) || (^subject is set) || (^subject is tuple) || (^subject is node)) {
		children = VISIT_NOT_MAP(^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild);
	} else {
		if(^subject is map) {
			children = VISIT_MAP(^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild); // special case of map
		};
	};
	if(deref beenChanged) {
		return typeswitch(^subject) {
	    			case list:  prim("list", children);
	    			case lrel:  prim("list", children);
	    			case set:   prim("set",  children);
	    			case rel:   prim("set",  children);
	    			case tuple: prim("tuple",children);
	    			case node:  prim("node", muprim("get_name", ^subject), children);
	    			case constructor: 
	                			prim("constructor", muprim("typeOf_constructor", ^subject), children);	    
	    			case map:   children; // special case of map	    
	    			default:    ^subject;
				};
	};
	return ^subject;
}

function VISIT_NOT_MAP[6, ^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild,
						  iarray, enumerator, ^child, i, childHasMatch, childBeenChanged] {
	iarray = make_iarray(size(^subject));
	enumerator = create(ENUMERATE_AND_ASSIGN, ref ^child, ^subject);
	i = 0;
	while(all(multi(enumerator))) {
		childHasMatch = false;
		childBeenChanged = false;
		^child = traverse_fun(phi, ^child, ref childHasMatch, ref childBeenChanged, rebuild);
		set_array iarray[i] = ^child;
		i = i + 1;
		deref hasMatch = childHasMatch || deref hasMatch;
		deref beenChanged = childBeenChanged || deref beenChanged;
	};
	return iarray;
}

function VISIT_MAP[6, ^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild,
					  writer, enumerator, ^key, ^val, childHasMatch, childBeenChanged] {
	writer = prim("mapwriter_open");
	enumerator = create(ENUMERATE_AND_ASSIGN, ref ^key, ^subject);
	while(all(multi(enumerator))) {
		^val = prim("map_subscript", ^subject, ^key);
		
		childHasMatch = false;
		childBeenChanged = false;
		^key = traverse_fun(phi, ^key, ref childHasMatch, ref childBeenChanged, rebuild);
		deref hasMatch = childHasMatch || deref hasMatch;
		deref beenChanged = childBeenChanged || deref beenChanged;
		
		childHasMatch = false;
		childBeenChanged = false;
		^val = traverse_fun(phi, ^val, ref childHasMatch, ref childBeenChanged, rebuild);
		deref hasMatch = childHasMatch || deref hasMatch;
		deref beenChanged = childBeenChanged || deref beenChanged;
		
		prim("mapwriter_add", writer, ^key, ^val);
	};
	return prim("mapwriter_close", writer);
}

function VISIT_CHILDREN_VOID[6, ^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild] {	
	if((^subject is list) || (^subject is set) || (^subject is tuple) || (^subject is node)) {
		VISIT_NOT_MAP_VOID(^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild);
		return ^subject;
	};
	if(^subject is map) {
		VISIT_MAP_VOID(^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild); // special case of map
	};
	return ^subject;
}

function VISIT_NOT_MAP_VOID[6, ^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild,
						       enumerator, ^child, childHasMatch, childBeenChanged] {
	enumerator = create(ENUMERATE_AND_ASSIGN, ref ^child, ^subject);
	childBeenChanged = false; // ignored
	while(all(multi(enumerator))) {
		childHasMatch = false;
		traverse_fun(phi, ^child, ref childHasMatch, ref childBeenChanged, rebuild);
		deref hasMatch = childHasMatch || deref hasMatch;
	};
	return;
}

function VISIT_MAP_VOID[6, ^subject, traverse_fun, phi, hasMatch, beenChanged, rebuild,
					       enumerator, ^key, ^val, childHasMatch, childBeenChanged] {
	enumerator = create(ENUMERATE_AND_ASSIGN, ref ^key, ^subject);
	childBeenChanged = false; // ignored  
	while(all(multi(enumerator))) {
		childHasMatch = false;
		traverse_fun(phi, ^key, ref childHasMatch, ref childBeenChanged, rebuild);
		deref hasMatch = childHasMatch || deref hasMatch;
		
		childHasMatch = false;
		traverse_fun(phi, prim("map_subscript", ^subject, ^key), ref childHasMatch, ref childBeenChanged, rebuild);
		deref hasMatch = childHasMatch || deref hasMatch;	
	};
	return;
}*/