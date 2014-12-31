@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Wietse Venema - wietsevenema@gmail.com - CWI}
module cobra::monadicgenerators

import Prelude;
import util::Math;

alias Gen[&T] = &T (int);

public Gen[bool] iszero = bool (int d) {
	if(d<0){
		throw IllegalArgument(d, "Depth below zero");
	} else {
		return d==0;
	}
};

public Gen[&A] unit( &A val ){
	return lift( &A () { return val;})();
}


public Gen[&R] () lift( &R () f){
	return Gen[&R] (){
		return &R (int d){
			return f();
		};
	};
}


public Gen[&B] (Gen[&A] a) lift( &B (&A) f){
	return Gen[&B] (Gen[&A] a){
		return &B (int d){
			return f(a(d));
		};
	};
}

public Gen[&R] (Gen[&A] a, Gen[&B] b) lift( &R (&A, &B) f){
	return Gen[&R] (Gen[&A] a, Gen[&B] b){
		return &R (int d){
			return f(a(d), b(d));
		};
	};
}

public Gen[&R] (Gen[&A] a, Gen[&B] b, Gen[&C] c) lift( &R (&A, &B, &C) f){
	return Gen[&R] (Gen[&A] a, Gen[&B] b, Gen[&C] c){
		return &R (int d){
			return f(a(d), b(d), c(d));
		};
	};
}

public Gen[&R] (Gen[&A] a, Gen[&B] b, Gen[&C] c, Gen[&D] d) lift( &R (&A, &B, &C, &D) f){
	return Gen[&R] (Gen[&A] a, Gen[&B] b, Gen[&C] c, Gen[&D] d){
		return &R (int depth){
			return f(a(depth), b(depth), c(depth), d(depth));
		};
	};
}

public Gen[&R] (Gen[&A] a, Gen[&B] b, Gen[&C] c, Gen[&D] d, Gen[&E] e) lift( &R (&A, &B, &C, &D, &E) f){
	return Gen[&R] (Gen[&A] a, Gen[&B] b, Gen[&C] c, Gen[&D] d, Gen[&E] e){
		return &R (int depth){
			return f(a(depth), b(depth), c(depth), d(depth), e(depth));
		};
	};
}




public Gen[int] number( int limit){
	return int (int d){
		return arbInt(limit);
	};
}

public Gen[int] choose( int from, int to){
	return int (int d){
		return arbInt(to-from)+from;
	};
}

public Gen[&A] elements( list[&A] as){
	return unit(getOneFrom(as));
}

public Gen[&A] oneof( list[Gen[&A]] gens ){
	return getOneFrom(gens); 
}

public Gen[list[&A]] genList( Gen[int] sizeGen, Gen[&A] valGen){
	return list[&A] (int d){
		int size = sizeGen(d);
		size = max(0, size);
		int n = 0;
		return while(n < size){
			append valGen(d);
			n += 1;
		}
	};
}

public Gen[&A] smaller( list[Gen[&A]] gens ){
	return &A (int d){
		d = d-1;	
		Gen[&A] pick = getOneFrom(gens);
		return pick(d);
	};
}

public Gen[&B] bind( Gen[&A] f, Gen[&B] (&A) g){
	return &B ( int d ){
		&A genVal = f(d);
		return g(genVal)(d);
	};
}   



