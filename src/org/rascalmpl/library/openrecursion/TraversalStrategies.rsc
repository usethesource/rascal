module openrecursion::TraversalStrategies

import IO;

public &T2 (&T2) (&T2 (&T2)) td(&T1 (&T1) f) 
	= &T (&T2) (&T2 (&T2) super) { 
		return &T2 (&T2 e) { 
			bool matched = true;
			&T1 (&T1) id = &T1 (&T1 n) { matched = false; return n; };
			switch(e) { 
				case &T1 t: { e = (id + f)(t); } 
				default: { matched = false; } 
			} 
			return super(e);
		}; };
		
public &T2 (&T2) (&T2 (&T2)) bu(&T1 (&T1) f) 
	= &T2 (&T2) (&T2 (&T2) super) {
		return &T2 (&T2 e) {
			e = super(e);
			bool matched = true; 
			&T1 (&T1) id = &T1 (&T1 n) { matched = false; return n; }; 
			switch(e) {
				case &T1 t: e = (id + f)(t);
				default: matched = false;
			} 
			return e;
		}; };
		
public &T2 (&T2) (&T2 (&T2)) tdb(&T1 (&T1) f) 
	= &T (&T2) (&T2 (&T2) super) { 
		return &T2 (&T2 e) { 
			bool matched = true;
			&T1 (&T1) id = &T1 (&T1 n) { matched = false; return n; };
			switch(e) { 
				case &T1 t: e = (id + f)(t); 
				default: matched = false; 
			} 
			if(!matched) e = super(e);
			return e;
		}; };
		
public &T2 (&T2) (&T2 (&T2)) children(&T1 (&T1) f) {	
	bool isParent = true;
	return &T2 (&T2) (&T2 (&T2) super) { 
		return &T2 (&T2 e) { 
			if(isParent) { isParent = false; return super(e); }  
			&T2 (&T2) id = &T2 (&T2 n) { return n; };
			switch(e) { 
				case &T1 t: e = (id + f)(t); 
				default: ; 
			}
			return e;
	}; };
}
					
public &T3 (&T3) (&T3 (&T3)) du(&T1 (&T1) top, &T2 (&T2) bottom) 
	= &T3 (&T3) (&T3 (&T3) super) { 
		return &T3 (&T3 e) { 
			&T1 (&T1) idTop = &T1 (&T1 n) { return n; };
			&T2 (&T2) idBottom = &T2 (&T2 n) { return n; };
			switch(e) {
				case &T1 t: e = (idTop + top)(t);
				default: ;
			}
			switch(e) {
				case &T2 t: return (idBottom + bottom)(super(e));
				default: return super(e);
			} 
		}; };