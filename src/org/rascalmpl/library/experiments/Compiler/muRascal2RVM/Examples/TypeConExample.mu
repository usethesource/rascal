module TypeConExample

function compareTypes[2,lhs,rhs] { return prim("type_equal_type", lhs, rhs); }

function subtype[2,lhs,rhs] { return prim("subtype", lhs, rhs); }

function compareTypesOfExpressions[2,lhs,rhs] { return compareTypes(muprim("typeOf",lhs),muprim("typeOf",rhs)); }

function compareIntAndValueTypes[0,] { return compareTypes(type "int()", type "value()"); }

function main[1,args,t1,t2,t3,e1,e2] {
	t1 = type "int()"; // type constant
	t2 = type "int()"; // type constant
	t3 = type "num()"; // type constant
	e1 = 1;
	e2 = 2;
	if(compareTypes(t1,t2)) { 
		if(subtype(t2,t3)) { 
			if(compareTypesOfExpressions(e1,e2)) {
				if(compareIntAndValueTypes()) {
					return 5;
				} else {
					return 4;
				};
			} else {
				return 3;
			};
		} else { 
			return 2; 
		}; 
	} else { 
		return 1; 
	};
	return;  
}