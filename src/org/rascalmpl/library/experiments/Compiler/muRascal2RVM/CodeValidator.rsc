module experiments::Compiler::muRascal2RVM::CodeValidator

import experiments::Compiler::RVM::AST;
import Type;
import Set;
import Relation;
import Map;
import IO;

rel[str, int] label_gets_sp = {};

map[str,set[int]] validate(list[Instruction] instructions){
	label_gets_sp = {};
	int sp = 0;
	incorrect = ();
	for(ins <- instructions){
		//println("<sp>: <ins>");
		sp = simulate(ins, sp);
	}
	labels = domain(label_gets_sp);
	for(lab <- labels){
		sps = label_gets_sp[lab];
		if(size(sps) > 1){
			//println("Multiple stack values <sps> for label <lab>");
			incorrect[lab] = sps;
		}
	}
	return incorrect;
}

int simulate(LABEL(str label), int sp){
	label_gets_sp += <label, sp>;
 	return sp;
 }
 
int simulate(JMP(str label), int sp) {
	label_gets_sp += <label, sp>;
	return sp;
}	
int simulate(JMPTRUE(str label), int sp) {
	sp -= 1;
	label_gets_sp += <label, sp>;
	return sp;
}	
int simulate(JMPFALSE(str label), int sp){
	sp -= 1;
	label_gets_sp += <label, sp>;
	return sp;
}

int simulate(TYPESWITCH(list[str] labels), int sp) {
	label_gets_sp += {<lab, sp> | lab <- labels};
	return sp;
}		
int simulate(SWITCH(map[int,str] caseLabels, str caseDefault, bool useConcreteFingerprint), int sp){
	label_gets_sp += ({<lab, sp> | lab <- range(caseLabels)} + <caseDefault, sp>);
	return sp;
}
int simulate(JMPINDEXED(list[str] labels), int sp){
	sp -= 1;
	label_gets_sp += {<lab, sp> | lab <- labels};
	return sp;
}


int simulate(LOADBOOL(bool bval), int sp) = sp + 1;
int simulate(LOADINT(int nval), int sp) = sp + 1;
int simulate(LOADCON(value val), int sp) = sp + 1;
int simulate(LOADTYPE(Symbol \type), int sp) = sp + 1;
int simulate(LOADFUN(str fuid), int sp) = sp + 1;
int simulate(LOAD_NESTED_FUN(str fuid, str scopeIn), int sp) = sp + 1;
int simulate(LOADCONSTR(str fuid), int sp) = sp + 1;
int simulate(LOADOFUN(str fuid), int sp) = sp + 1;
int simulate(LOADLOC(int pos), int sp) = sp + 1;
int simulate(STORELOC(int pos), int sp) = sp + 0;
int simulate(RESETLOCS(list[int] positions), int sp) = sp + 1;

int simulate(LOADLOCKWP(str name), int sp) = sp + 1;
int simulate(STORELOCKWP(str name), int sp) = sp + 0;
int simulate(UNWRAPTHROWNLOC(int pos), int sp) = sp + 0;
int simulate(UNWRAPTHROWNVAR(str fuid, int pos), int sp) = sp + 0;
int simulate(LOADVAR(str fuid, int pos) , int sp) = sp + 1;
int simulate(STOREVAR(str fuid, int pos), int sp) = sp + 0;
int simulate(LOADVARKWP(str fuid, str name), int sp) = sp + 1;
int simulate(STOREVARKWP(str fuid, str name), int sp) = sp + 0;
int simulate(LOADMODULEVAR(str fuid), int sp) = sp + 1;

int simulate(STOREMODULEVAR(str fuid), int sp) = sp + 1;
int simulate(LOADLOCREF(int pos), int sp) = sp + 1;
int simulate(LOADLOCDEREF(int pos), int sp) = sp + 1;
int simulate(STORELOCDEREF(int pos), int sp) = sp + 0;
int simulate(LOADVARREF(str fuid, int pos), int sp) = sp + 1;
int simulate(LOADVARDEREF(str fuid, int pos), int sp) = sp + 1;
int simulate(STOREVARDEREF(str fuid, int pos), int sp) = sp + 0;
int simulate(CALL(str fuid, int arity), int sp) = sp - arity + 1;

int simulate(CALLDYN(int arity), int sp) = sp -arity + 1;
int simulate(APPLY(str fuid, int arity)  , int sp) = sp - arity + 1;
int simulate(APPLYDYN(int arity), int sp) = sp - arity - 1 + 1;
int simulate(CALLCONSTR(str fuid, int arity)	, int sp) = sp - arity + 1;
int simulate(OCALL(str fuid, int arity, loc src), int sp) = sp -arity + 1;
int simulate(OCALLDYN(Symbol types, int arity, loc src), int sp) = sp - 1 - arity + 1;
int simulate(CALLMUPRIM(str name, int arity), int sp) = sp - arity + 1;
int simulate(CALLPRIM(str name, int arity, loc src), int sp) = sp - arity + 1;
int simulate(CALLJAVA(str name, str class, 
		           Symbol parameterTypes,
		           Symbol keywordTypes,
		           int reflect), int sp) = sp + 1;  // type size
int simulate( RETURN0()	, int sp) = sp + 0;
int simulate(RETURN1(int arity), int sp) = sp - arity;

int simulate(FAILRETURN(), int sp) = sp + 0;
int simulate(FILTERRETURN(), int sp) = sp + 0;
int simulate(THROW(loc src), int sp) = sp + 0;

int simulate(CREATE(str fuid, int arity) , int sp) = sp -arity + 1;
int simulate(CREATEDYN(int arity), int sp) = sp - 1 - arity + 1;
int simulate(NEXT0(), int sp) = sp + 0;
int simulate(NEXT1(), int sp) = sp + 0;
int simulate(YIELD0(), int sp) = sp + 1;
int simulate(YIELD1(int arity), int sp) = sp - arity + 1;
int simulate(EXHAUST(), int sp) = sp + 0;
int simulate(GUARD(), int sp) = sp - 1;
int simulate(PRINTLN(int arity), int sp) = sp - arity;
int simulate(POP(), int sp) = sp - 1;
int simulate(HALT(), int sp) = sp + 0;
int simulate(SUBSCRIPTARRAY(), int sp) = sp - 1;
int simulate(SUBSCRIPTLIST(), int sp) = sp - 1;
int simulate(LESSINT()	, int sp) = sp - 1;
int simulate(GREATEREQUALINT(), int sp) = sp - 1;
int simulate(ADDINT(), int sp) = sp - 1;
int simulate(SUBTRACTINT(), int sp) = sp - 1;
int simulate(ANDBOOL(), int sp) = sp - 1;
int simulate(TYPEOF(), int sp) = sp + 0;
int simulate(SUBTYPE(), int sp) = sp - 1;
int simulate(CHECKARGTYPEANDCOPY(
			int pos1, Symbol \type, int pos2), int sp) = sp + 1;
int simulate(LOADBOOL(bool bval), int sp) = sp + 1;
int simulate(LOADBOOL(bool bval), int sp) = sp + 1;

		
// Delimited continuations (experimental)
//| LOADCONT(str fuid)
//| RESET()
//| SHIFT()
	

value main(list[value] args){

	return validate([
    CHECKARGTYPEANDCOPY(0,\list(\value()),3),
    JMPFALSE("ELSE_LAB0"),
    LOADCON(10),
    RETURN1(1),
    LABEL("ELSE_LAB0"),
    FAILRETURN()
  ]);
}
	
test bool validate1() = 
	validate(
	[
    LOADCON(true),
    RETURN1(1),
    HALT()
  	]) == ();
  		

test bool validate1() =
	validate([LABEL("A"), JMP("B"), LOADCON(1), LABEL("B")]) == ("B":{1,0});
	
test bool validate1() =
    validate([
    CHECKARGTYPEANDCOPY(0,\list(\value()),3),
    JMPFALSE("ELSE_LAB0"),
    LOADCON(10),
    RETURN1(1),
    LABEL("ELSE_LAB0"),
    FAILRETURN()
  ]) == ();
