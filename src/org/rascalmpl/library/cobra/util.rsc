@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Wietse Venema - wietsevenema@gmail.com - CWI}
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


