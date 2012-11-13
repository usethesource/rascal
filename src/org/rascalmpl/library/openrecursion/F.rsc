module openrecursion::F

import openrecursion::TraversalStrategies;

import IO;
import List;

data Expr = integer(int i) 
			| string(str s)
			| add(Expr e1, Expr e2) 
			| mult(Expr e1, Expr e2)
			;

public test bool ftd() {
	list[Expr] exprs = [];
	list[Expr] addExprs = [];
	list[int] nums = [];
	Expr (Expr) es = Expr (Expr e) { exprs += e; return e; };
	int (int) ns = int (int n) { nums += n; return n; };
	Expr ae(e:add(_,_)) { addExprs += e; return e; }
	
	Expr input = add(integer(1), mult(integer(2), add(integer(3), integer(0))));
	
	(td(es) o fvisit)(input);
	(td(ns) o fvisit)(input);
	(td(ae) o fvisit)(input);
	
	list[Expr] vexprs = [];
	list[Expr] vaddExprs = [];
	list[int] vnums = [];
	
	top-down visit(input) {
		case Expr e: vexprs += e;
	}
	top-down visit(input) {
		case int i: vnums += i;
	}
	top-down visit(input) {
		case e:add(_,_): vaddExprs += e;
	}
	
	list[value] alles = [];
	Expr (Expr) es1 = Expr (Expr e) { alles += e; return e; };
	int (int) ns1 = int (int n) { alles += n; return n; };
	
	(td(ns1) o td(es1) o fvisit)(input);
	
	list[value] valles = [];
	
	top-down visit(input) {
		case Expr e: valles += e;
		case int i: valles += i;
	}
	
	return exprs == vexprs && addExprs == vaddExprs && nums == vnums && alles == valles;
}

public test bool fbu() {
	list[Expr] exprs = [];
	list[Expr] addExprs = [];
	list[int] nums = [];
	Expr (Expr) es = Expr (Expr e) { exprs += e; return e; };
	int (int) ns = int (int n) { nums += n; return n; };
	Expr ae(e:add(_,_)) { addExprs += e; return e; }
	
	Expr input = add(integer(1), mult(integer(2), add(integer(3), integer(0))));
	
	(bu(es) o fvisit)(input);
	(bu(ns) o fvisit)(input);
	(bu(ae) o fvisit)(input);
	
	list[Expr] vexprs = [];
	list[Expr] vaddExprs = [];
	list[int] vnums = [];
	
	visit(input) {
		case Expr e: vexprs += e;
	}
	visit(input) {
		case int i: vnums += i;
	}
	visit(input) {
		case e:add(_,_): vaddExprs += e;
	}
	
	list[value] alles = [];
	Expr (Expr) es1 = Expr (Expr e) { alles += e; return e; };
	int (int) ns1 = int (int n) { alles += n; return n; };
	
	(bu(ns1) o bu(es1) o fvisit)(input);
	
	list[value] valles = [];
	
	visit(input) {
		case Expr e: valles += e;
		case int i: valles += i;
	}
	
	return exprs == vexprs && addExprs == vaddExprs && nums == vnums && alles == valles;
}

public test bool tdBu() {
	list[Expr] state1 = [];
	Expr s1(Expr e) { state1 += e; return e; };
	list[Expr] state2 = [];
	Expr s2(Expr e) { state2 += e; return e; };
	int addOne(int i) = i + 1;
	
	Expr input = add(integer(1), mult(integer(2), add(integer(3), integer(0))));
	input = (td(s1) o td(addOne) o bu(s2) o fvisit)(input);
	return
	state1 == [add(integer(1),mult(integer(2),add(integer(3),integer(0)))),
			   integer(1),
			   mult(integer(2),add(integer(3),integer(0))),
			   integer(2),
			   add(integer(3),integer(0)),
			   integer(3),
			   integer(0)]
	&&
	state2 == [integer(2),
			   integer(3),
			   integer(4),
			   integer(1),
			   add(integer(4),integer(1)),
			   mult(integer(3),add(integer(4),integer(1))),
			   add(integer(2),mult(integer(3),add(integer(4),integer(1))))];
}

//public Expr bottomUpBreakVisit(Expr e)
//	= bottom-up-break visit(e) {
//		case integer(0): insert integer(100);
//		case integer(int i): insert integer(i+1);
//		case add(op1, op2) : insert add(add(op1, integer(555)), op2);
//	};
//
//public test bool test2() {
//	Expr addOne(e:integer(0)) = integer(100);
//	Expr addOne(e:integer(int i)) = integer(i+1);
//	Expr addOne(add(op1, op2)) = add(add(op1, integer(555)), op2);
//	
//	Expr input = add(integer(1), mult(integer(2), add(integer(3), integer(0))));
//	
//	bool isCool = bottomUpBreakVisit(input) == (bub(addOne) o fvisit)(input);
//	
//	println((children(addOne) o fvisit)(input));
//	
//	println("normal: <bottomUpBreakVisit(input)>");
//	
//	str state = "";
//	Expr (Expr) strategy = Expr (Expr e) { state = state + "<e> ; "; return e; };
//	
//	println("composed: <(bub(addOne) o fvisit)(input)>");
//	println("state: <state>");
//	
//	input = add(mult(add(integer(0), integer(3)), integer(2)), integer(1));
//
//	bool isEvenCooler = bottomUpBreakVisit(input) == (bub(addOne) o fvisit)(input);
//	
//	return isCool && isEvenCooler;
//}
//
//public void main3() {
//	list[Expr] parents = [];
//	list[Expr] children = [];
//	list[tuple[Expr, list[Expr]]] parentChildRel = [];
//	Expr (Expr) addParent = Expr (Expr e) { 
//			parents = parents + [e]; 
//			return e; 
//		};
//	Expr (Expr) printOutChildren = Expr (Expr e) { 
//		parentChildRel = parentChildRel + [<last(parents), children - last(parents)>];
//		children = []; 
//		return e; 
//	};
//	Expr (Expr) addChild = Expr (Expr e) { 
//			children = children + [e]; 
//			return e; 
//		};
//	Expr (Expr) addChildren = bottomUp(addChild) o traverse;
//	
//	Expr input = add(integer(1), mult(integer(2), integer(3)));
//	( td( printOutChildren o. addChildren o. addParent ) o traverse )
//		(input);
//		
//	iprintln(parentChildRel);
//}
