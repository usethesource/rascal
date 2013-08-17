module TypeConExample

function compareTypes[1,2,lhs:0,rhs:1] { return prim("equals_type_type", lhs, rhs); }

function subtype[2,2,lhs:0,rhs:1] { return prim("subtype", lhs, rhs); }

function compareTypesOfExpressions[3,2,lhs:0,rhs:1] { return compareTypes(prim("typeOf",lhs),prim("typeOf",rhs)); }

function compareIntAndValueTypes[4,0,] { return compareTypes(type "int()", type "value()"); }

function main[5,1,args:0,t1:1,t2:2,t3:3,e1:4,e2:5] {
	t1 = type "int()"; // type constant
	t2 = type "int()"; // type constant
	t3 = type "num()"; // type constant
	e1 = 1;
	e2 = 2;
	if(compareTypes(t1,t2)) { 
		if(subtype(t2,t3)) { 
			if(compareTypesOfExpressions(e1,e2)) {
			} else {
				if(compareIntAndValueTypes()) {
					true
				} else {
					false
				}
			} 
		} else { 
			false 
		} 
	} else { 
		false 
	};
	return;  
}