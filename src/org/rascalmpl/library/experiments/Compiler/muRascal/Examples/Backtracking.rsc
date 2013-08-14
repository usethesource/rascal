module experiments::Compiler::muRascal::Examples::Backtracking

import Prelude;
import  experiments::Compiler::muRascal::AST;
import experiments::Compiler::RVM::AST;
import experiments::Compiler::RVM::Run;
import experiments::Compiler::muRascal::mu2rvm;

list[MuFunction] functions = [

muFunction("TRUE", 1, 0, 0, 
	[
		muReturn(muCon(true))
	]
),
       
muFunction("FALSE", 1, 0, 0, 
	[
		muReturn(muCon(false))
	]
),
        
muFunction("AND", 1, 2, 2, 
	[					
		muAssignLoc("lhs", 0, muInit(muCreate(muLoc("lhs", 0)))),
		muWhile(muHasNext(muLoc("lhs", 0)),
			[ muIfelse(muNext(muLoc("lhs", 0)),
			     [ muAssignLoc("rhs", 1, muInit(muCreate(muLoc("rhs", 1)))),
			       muWhile(muHasNext(muLoc("rhs", 1)),
						   [ muYield(muNext(muLoc("rhs", 1))) ])
				],
				[])
			]),
		muReturn(muCon(false))
	]
),
 /*         
muFunction("ONE", 1, 1, 1, 
	[
		muAssign("lhs", 1, 0, muInit(muCreate(muVar("arg", 1, 0)))),
		muReturn(muNext(muVar("arg", 1, 0)))
	]
),
				
muFunction("ALL", 0, 1, 1, 
	[					
		muAssign("arg", 0, 0, muInit(muCreate(muVar("arg", 0, 0)))),
		muWhile(muHasNext(muVar("lhs", 0, 0)),
			[ muYield(muNext(muVar("arg", 0, 0)))
			]),
	    muReturn(muCon(false))		
	]
),
*/

// Main 1:
//muFunction("main", 1, 1, 2, 
//	[		
//		muAssign("c", 1, 1, muInit(muCreate("AND"),[muFun("TRUE"), muFun("TRUE")] )),
//		muReturn(muNext(muLoc("c", 1)))
//	]
//)
// Main 2:
muFunction("main", 1, 1, 3, 
	[		
		muAssignLoc("c", 1, muInit(muCreate("AND"),[muFun("TRUE"), muFun("TRUE")] )),
		muAssignLoc("count", 2, muCon(0)),
		muWhile(muHasNext(muLoc("c", 1)),
			[ muNext(muLoc("c", 1)),
			  muAssignLoc("count", 2, muCallPrim("addition_num_num", 
													muLoc("count", 2), 
													muCon(1)))]
					),
		muReturn(muLoc("count", 2))
	]
)

];

bool runBacktracking(){
  muP = muModule("Backtracking", [], functions, [], []);
  rvmP = mu2rvm(muP);
  iprintln(rvmP);
  <v, t> = executeProgram(rvmP, true, 1);
  println("Result = <v>, [<t> msec]");
  return bool b := v ? b : false;
}

/*
The above example crashes, see this trace at AND[32].

	0: [Lorg.eclipse.imp.pdb.facts.IValue;@4da50c2a
#module_init[0] LOADLOC 0
	0: [Lorg.eclipse.imp.pdb.facts.IValue;@4da50c2a
	1: [Lorg.eclipse.imp.pdb.facts.IValue;@4da50c2a
#module_init[2] CALL 1 [main]
	0: [Lorg.eclipse.imp.pdb.facts.IValue;@4da50c2a
	1: null
main[0] LOADFUN 0 [TRUE]
	0: [Lorg.eclipse.imp.pdb.facts.IValue;@4da50c2a
	1: null
	2: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@52748cc0
main[2] LOADFUN 0 [TRUE]
	0: [Lorg.eclipse.imp.pdb.facts.IValue;@4da50c2a
	1: null
	2: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@52748cc0
	3: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
main[4] CREATE 2 [AND]
	0: [Lorg.eclipse.imp.pdb.facts.IValue;@4da50c2a
	1: null
	2: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@52748cc0
	3: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
	4: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@46745141
main[6] INIT
	0: [Lorg.eclipse.imp.pdb.facts.IValue;@4da50c2a
	1: null
	2: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@46745141
main[7] STORELOC 1
	0: [Lorg.eclipse.imp.pdb.facts.IValue;@4da50c2a
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@46745141
	2: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@46745141
main[9] POP
	0: [Lorg.eclipse.imp.pdb.facts.IValue;@4da50c2a
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@46745141
main[10] LOADLOC 1
	0: [Lorg.eclipse.imp.pdb.facts.IValue;@4da50c2a
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@46745141
	2: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@46745141
main[12] NEXT0
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@52748cc0
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
	2: null
AND[0] LOADLOC 0
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@52748cc0
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
	2: null
	3: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@52748cc0
AND[2] CREATEDYN
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@52748cc0
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
	2: null
	3: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
AND[3] INIT
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@52748cc0
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
	2: null
	3: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
AND[4] STORELOC 0
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
	2: null
	3: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
AND[6] POP
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
	2: null
AND[7] LOADLOC 0
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
	2: null
	3: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
AND[9] HASNEXT
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
	2: null
	3: true
AND[10] JMPFALSE 43
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
	2: null
AND[12] LOADLOC 0
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
	2: null
	3: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
AND[14] NEXT0
	0: null
TRUE[0] LOADCON 0 [true]
	0: null
	1: true
TRUE[2] RETURN1
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
	2: null
	3: true
AND[15] JMPFALSE 38
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
	2: null
AND[17] LOADLOC 1
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
	2: null
	3: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
AND[19] CREATEDYN
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
	2: null
	3: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
AND[20] INIT
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Closure@61c4bb8a
	2: null
	3: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
AND[21] STORELOC 1
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: null
	3: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
AND[23] POP
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: null
AND[24] LOADLOC 1
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: null
	3: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
AND[26] HASNEXT
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: null
	3: true
AND[27] JMPFALSE 36
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: null
AND[29] LOADLOC 1
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: null
	3: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
AND[31] NEXT0
	0: null
TRUE[0] LOADCON 0 [true]
	0: null
	1: true
TRUE[2] RETURN1
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: null
	3: true
AND[32] YIELD1
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: null
	3: true
	
===> What is going on here? I would expect a return to main! <===
	
AND[33] POP
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: null
AND[34] JMP 24
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: null
AND[24] LOADLOC 1
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: null
	3: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
AND[26] HASNEXT
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: null
	3: true
AND[27] JMPFALSE 36
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: null
AND[29] LOADLOC 1
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: null
	3: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
AND[31] NEXT0
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: null
AND[32] YIELD1
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: null
AND[33] POP
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
AND[34] JMP 24
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
AND[24] LOADLOC 1
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
AND[26] HASNEXT
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: true
AND[27] JMPFALSE 36
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
AND[29] LOADLOC 1
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
	2: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
AND[31] NEXT0
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
AND[32] YIELD1
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
AND[33] POP
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
AND[34] JMP 24
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
AND[24] LOADLOC 1
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@3383ad42
AND[26] HASNEXT
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: true
AND[27] JMPFALSE 36
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
AND[29] LOADLOC 1
	0: org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine@7e82a689
	1: true
AND[31] NEXT0
PANIC: exception caused by invoking a primitive or illegal instruction sequence
java.lang.ClassCastException: org.eclipse.imp.pdb.facts.impl.primitive.BoolValue$1 cannot be cast to org.rascalmpl.library.experiments.CoreRascal.RVM.Coroutine
	at org.rascalmpl.library.experiments.CoreRascal.RVM.RVM.executeProgram(RVM.java:368)
	at org.rascalmpl.library.experiments.CoreRascal.RVM.Execute.executeProgram(Execute.java:182)
	
*/
