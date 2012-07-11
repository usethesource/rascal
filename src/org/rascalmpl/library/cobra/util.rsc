module cobra::util
import Prelude;
import util::Math;

public set[&T] getSomeFrom( set[&T] elms ){
	if(size(elms) == 0){
		return {};
	}
	int nr = arbInt(size(elms)); 
	set[&T] result = {}; 
	while(size(result) < nr){
		&T t;
		<t, elms> = takeOneFrom(elms); 
		result += t;
	}
	return result;
}

public list[&T] getSomeFrom( list[&T] lst ){
	if(size(lst) == 0){
		return [];
	}
	int nr = arbInt(size(lst)); 
	list[&T] result = []; 
	while(size(result) > nr){
		result += takeOneFrom(lst);
	}
	return result;
}


