module Let-abstract

import Integer;
import IO;



/*
 * Abstract syntax of the Let language. It consists of
 * - a let construct that binds a variable to a first expressions and evaluates the second expression using that binding
 * - a variable occurrence
 * - an integer constant
 * - an (arbitrary) binary operator op.
 */
 
type str Var;
data Exp let(Var name, Exp exp1, Exp exp2) | Var Name | intcon(int val) | op(Exp exp1, Exp exp2);

/*
 * TODO: the ideal definition would be:
 * data Var var(str Name);
 * data IntCon intcon(int val);
 * data Exp let(Var var, Exp exp1, Exp exp2) | Var var | IntCon ival | op(Exp exp1, Exp exp2);
*/

/*
 * Gensym, a unique name generator.
 */
 
int Cnt = 0;

public Var genSym()
{
	Cnt = Cnt + 1;
    return "x" + toString(Cnt);
}

// Rename all bound variables in an Exp
// Exp E: given expression to be renamed
// map[str,str]: renaming table

public Exp rename(Exp E, map[str,str] Rn) {
    switch (E) {
    case let(Var Name, Exp E1, Exp E2): {
    	 	Var Y = genSym();
         	return let(Y, rename(E1, Rn), rename(E2, Rn + (Name: Y)));
         }
         
    case op(Exp E1, Exp E2):
    	 return op(rename(E1, Rn), rename(E2, Rn));
    
    case Var Name:
    	 return Rn[Name] =? Name;

    default:
    	 return E;
    }
}

public Exp test1(){
 return rename(let("a", intcon(3), "a"), ());
}

public Exp test2(){
 return rename(let("a", intcon(3), "b"), ());
}

public Exp test3(){
 return rename(let("a", intcon(1),let("b", intcon(2), "b")), ());
}

public Exp test4(){
 return rename(let("a", intcon(1), op(let("b", intcon(2), "b"), "a")), ());
}

public Exp test5(){
 return rename(let("a", intcon(1), op(let("a", intcon(2), "a"), "a")), ());
}

public bool test(){
	return
		rename(let("a", intcon(3), "a"), ()) ==
		       let("x1",intcon(3), "x1") &&
		       
	 	rename(let("a", intcon(3), "b"), ()) == 
	 	       let("x2",intcon(3), "b") &&

	 	rename(let("a", intcon(1),let("b", intcon(2), "b")), ()) == 
	 	       let("x3",intcon(1),let("x4",intcon(2), "x4")) &&
	 	       
	 	rename(let("a", intcon(1), op(let("b", intcon(2), "b"),  "a")), ()) ==
	 	       let("x5",intcon(1), op(let("x6",intcon(2), "x6"), "x5")) &&
	 	       
	 	rename(let("a", intcon(1), op(let("a", intcon(2), "a"),  "a")), ()) ==
	 	       let("x7",intcon(1), op(let("x8",intcon(2), "x8"), "x7"));
}

	 	 
		