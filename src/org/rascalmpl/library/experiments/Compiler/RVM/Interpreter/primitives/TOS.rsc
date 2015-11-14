module experiments::Compiler::RVM::Interpreter::primitives::TOS

import IO;
import List;
import experiments::Compiler::muRascal::AST;

import experiments::Compiler::RVM::AST;

data Dest = accu() | stack() | local(int pos) | con(value v) | nowhere();

data Instruction = PUSHACCU();

alias INS = list[Instruction];

MuExp E1 = muInt(10);
MuExp E2 = muCallPrim3("int_add_int", [muInt(10), muInt(20)], |e2:///|);

MuExp E3 = muAssignLoc("i", 0, muCallPrim3("int_add_int", [muLoc("j", 1), muInt(10)], |e2:///|));

INS trExps(list[MuExp] exps) = [ *trExp(exp) | exp <- exps ];

INS emit(muInt(n), Dest d) = plug(d, con(n));

INS emit(muLoc(name, pos), Dest d) = plug(d, local(pos));

INS emit(muCallPrim3(name, [left], src), Dest d) = 
    [*emit(left, accu()), 
     CALLPRIM1(name, src),
     *plug(d, accu())
     ];
     
INS emit(muCallPrim3(name, [left, right], src), Dest d) = 
    [*emit(left, stack()), 
     *emit(right,accu()),
     CALLPRIM2(name, src),
     *plug(d, accu())
     ];

INS plug(Dest d1,Dest d2) = [ ] when d1 == d2;

INS plug(accu(), accu()) = [ ];
INS plug(stack(), accu()) = [ PUSHACCU() ];
INS plug(local(pos), accu()) = [ STORELOC(pos)];
INS plug(nowhere(), accu()) = [ ];

INS plug(accu(), con(v)) = [LOADCON(v)];
INS plug(stack(), con(v)) = [LOADCON(v), PUSHACCU()];
INS plug(local(pos), con(v)) = [LOADCON(v), STORELOC(pos)];
INS plug(nowhere(), con(v)) = [ ];

INS plug(accu(), local(pos)) = [ LOADLOC(pos) ];
INS plug(stack(), local(pos)) = [ LOADLOC(pos), PUSHACCU() ];
INS plug(local(pos1), local(pos2)) = [ LOADLOC(pos2), STORELOC(pos2) ];
INS plug(nowhere(), local(pos)) = [ ];

INS emit(muAssignLoc(str id, int pos, MuExp exp), Dest d) =  
    [*emit(exp, accu()), 
      STORELOC(pos),
     *plug(d, accu())
    ];

value main(){
    println("E1: <emit(E1, local(3))>");
    
    println("E2: <emit(E2, stack())>");
    
    println("E3: <emit(E3, nowhere())>");

    return true;
}



