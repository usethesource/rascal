module openrecursion::F

import IO;
import List;

data Expr = integer(int i) 
			| add(Expr e1, Expr e2) 
			| mult(Expr e1, Expr e2)
			;
// given the 'visit' semantics			
public Expr traverse(e:mult(Expr e1, Expr e2)) = mult(traverse(e1), traverse(e2));
public Expr traverse(e:add(Expr e1, Expr e2)) = add(traverse(e1), traverse(e2));
public Expr traverse(e:integer(int i)) = e;

public Expr (Expr) (Expr (Expr)) topDown(Expr (Expr) f) 
	= Expr (Expr) (Expr (Expr) super) { 
		return Expr (Expr e) { return super(f(e)); }; };

public Expr (Expr) (Expr (Expr)) bottomUp(Expr (Expr) f) 
	= Expr (Expr) (Expr (Expr) super) { 
		return Expr (Expr e) { return f(super(e)); }; };

public Expr (Expr) (Expr (Expr)) downUp(Expr (Expr) top, Expr (Expr) bottom) 
	= Expr (Expr) (Expr (Expr) super) { 
		return Expr (Expr e) { return bottom(super(top(e))); }; };

public void main1() {
	str state = "";
	Expr (Expr) strategy = Expr (Expr e) { state = state + "<e> ; "; return e; };
	(topDown(strategy) o traverse)(add(integer(1), mult(integer(2), integer(3))));
	println(state);
}

public void main2() {
	str state = "";
	Expr (Expr) strategy = Expr (Expr e) { state = state + "<e> ; "; return e; };
	(bottomUp(strategy) o traverse)(add(integer(1), mult(integer(2), integer(3))));
	println(state);
}

public void main3() {
	list[Expr] parents = [];
	list[Expr] children = [];
	list[tuple[Expr, list[Expr]]] parentChildRel = [];
	Expr (Expr) addParent = Expr (Expr e) { 
			parents = parents + [e]; 
			return e; 
		};
	Expr (Expr) printOutChildren = Expr (Expr e) { 
		parentChildRel = parentChildRel + [<last(parents), children - last(parents)>];
		children = []; 
		return e; 
	};
	Expr (Expr) addChild = Expr (Expr e) { 
			children = children + [e]; 
			return e; 
		};
	Expr (Expr) addChildren = bottomUp(addChild) o traverse;
	( topDown( printOutChildren o. addChildren o. addParent ) o traverse )
		(add(integer(1), mult(integer(2), integer(3))));
		
	iprintln(parentChildRel);
}
