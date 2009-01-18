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
data Exp let(str name, Exp exp1, Exp exp2) | var(str Name) | intcon(int val) | op(Exp exp1, Exp exp2);

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

public str genSym()
{
	Cnt = Cnt + 1;
    return "x" + toString(Cnt);
}

// Rename all bound variables in an Exp
// Exp E: given expression to be renamed
// map[str,str]: renaming table

public Exp rename(Exp E, map[str,str] Rn) {
    switch (E) {
    case let(str Name, Exp E1, Exp E2): {
    	 	str Y = genSym();
         	return let(Y, rename(E1, Rn), rename(E2, Rn + (Name: Y)));
         }
         
    case op(Exp E1, Exp E2):
    	 return op(rename(E1, Rn), rename(E2, Rn));
    
    case var(str Name):
    	 return var(Rn[Name] =? Name);

    default:
    	 return E;
    }
}

public Exp test1(){
 return rename(let("a", intcon(3), var("a")), ());
}

public Exp test2(){
 return rename(let("a", intcon(3), var("b")), ());
}

public Exp test3(){
 return rename(let("a", intcon(1),let("b", intcon(2), var("b"))), ());
}

public Exp test4(){
 return rename(let("a", intcon(1), op(let("b", intcon(2), var("b")), var("a"))), ());
}

public Exp test5(){
 return rename(let("a", intcon(1), op(let("a", intcon(2), var("a")), var("a"))), ());
}

public bool test(){
	return
		rename(let("a", intcon(3), var("a")), ()) ==
		       let("x1",intcon(3),var("x1")) &&
		       
	 	rename(let("a", intcon(3), var("b")), ()) == 
	 	       let("x2",intcon(3),var("b")) &&

	 	rename(let("a", intcon(1),let("b", intcon(2), var("b"))), ()) == 
	 	       let("x3",intcon(1),let("x4",intcon(2),var("x4"))) &&
	 	       
	 	rename(let("a", intcon(1), op(let("b", intcon(2), var("b")), var("a"))), ()) ==
	 	       let("x5",intcon(1),op(let("x6",intcon(2),var("x6")),var("x5"))) &&
	 	       
	 	rename(let("a", intcon(1), op(let("a", intcon(2), var("a")), var("a"))), ()) ==
	 	       let("x7",intcon(1),op(let("x8",intcon(2),var("x8")),var("x7")));
}

	 	 
		