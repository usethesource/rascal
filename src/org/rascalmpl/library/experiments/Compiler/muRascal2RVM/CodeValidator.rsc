module experiments::Compiler::muRascal2RVM::CodeValidator

import experiments::Compiler::RVM::AST;
import Type;
import List;
import Set;
import Relation;
import Map;
import IO;
import Node;
import analysis::graphs::Graph;
bool debug = false;

// Split a list of instructions in basic blocks
// Basic blocks are identified by serial number

map[int, list[Instruction]] makeBlocks(list[Instruction] instructions){
	map[int, list[Instruction]] blocks = ();
	int nblock = 0;
	current = [];
	for(ins <- instructions){
		if(isBlockStartInstruction(ins)){
			if(size(current) > 0){
				blocks[nblock] = current;
				nblock += 1;
			}	
			current = [ins];
		} else if(isBlockEndInstruction(ins)){
			current += ins;
			blocks[nblock] = current;
			nblock += 1;
			current = [];
		} else {
			current += ins;
		}	
	}
	if(size(current) > 0){
		blocks[nblock] = current;
	}
	if(debug){
		for(block_num <- sort(domain(blocks))){
			print("<block_num>: "); iprintln(blocks[block_num]);
		}
	}	
	return blocks;
}

// Turn basic blocks into a control flow graph

Graph[int] makeGraph(map[int, list[Instruction]]  blocks){

	labels = ();
	graph = {};
	for(int i <- domain(blocks)){
		current = blocks[i];
		if(LABEL(str name) := current[0]){
			labels[name] = i;
		}
	}
	
	for(int i <- domain(blocks)){
		current = blocks[i];
		if(JMP(str name) := current[-1]){
			graph += <i, labels[name]>;
		}  else if(   JMPTRUE(str name) := current[-1] 
		           || JMPFALSE(str name) := current[-1]){
			graph += {<i, labels[name]>, <i, i + 1>};
		} else if(RETURN0() !:= current[-1] && RETURN1(_)  !:= current[-1] && EXHAUST() !:= current[-1]){
			graph += {<i, i + 1>};
		}
	}
	return carrierR(graph, domain(blocks));
}

// Compute stack deltas per basic block

map[int,int] computeStackDeltas(map[int, list[Instruction]] blocks){
	deltas = ();
	for(int i <- sort(domain(blocks))){
		if(debug) println("Block <i>:");
		int sp = 0;
		for(ins <- blocks[i]){
			if(debug)println("\t<sp>: <ins>");
			sp = simulate(ins, sp);
		}
		if(debug)println("Block <i>, delta sp = <sp>");
		deltas[i] = sp;
	}
	return deltas;
}

// Identify the start and end instructions for a basic block

bool isBlockStartInstruction(Instruction ins) = LABEL(_) := ins;

bool isBlockEndInstruction(Instruction ins) = 
	getName(ins) in {"JMP", "JMPFALSE", "JMPTRUE", "RETURN0", "RETURN1"};

// Identify exit instructions

bool isExit(list[Instruction] instructions) =
	getName(instructions[-1]) in {"RETURN0", "RETURN1", "EXHAUST"};

// Compute all path through the CFG, from entry

set[list[int]] allPath(Graph[int] graph, map[int,int] deltas){
	nodes = carrier(graph);
	set[list[int]] paths = {[0]};
		
	solve(paths){
		//println("paths = <paths>");
		oldPaths = paths;
		paths = {};
		for(p <- oldPaths){
			lastNode = p[-1];
			extensions = graph[lastNode];
			for(extension <- extensions){
				//println("extending <p> with <extension>");
				if(!([lastNode, extension] <= p)){
					paths += (p + extension);
				} else {
					paths += p;
				}
			}
			if(extensions == {}){
				paths += p;
			}
		}
		paths = collapse(paths, deltas);		
	}
	return paths;
}

set[list[int]] collapse({first: [*int prefix, *int path1, int final], 
                         second: [prefix, *int path2, final], 
                         *list[int] other}, 
                         map[int,int] deltas){
    if(size(path1) > 0 && size(path2) > 0){
		delta1 = (0 | it + deltas[p] | p <- path1);
		delta2 = (0 | it + deltas[p] | p <- path2);
		if(delta1 == delta2){
			//println("collapse <first> and <second>");
			if(size(first) < size(second)){
				return collapse({first, *other}, deltas);
			} else {
				return collapse({second, *other}, deltas);
			}
		}
	}
	fail;

}

default set[list[int]] collapse(set[list[int]] paths, map[int,int] deltas) = paths;

// Validate a list of constructions

set[list[int]] validate(list[Instruction] instructions) {
	blocks = makeBlocks(instructions);
	graph = makeGraph(blocks);
	if(debug)println(graph);
	deltas = computeStackDeltas(blocks);
	paths =  allPath(graph, deltas);
	nonzero = for(p <- paths, isExit(blocks[p[-1]])){
				d = (0 | it + deltas[nd]?0 | nd <- p);
				if(debug)println("path <p>, delta <d>");
				if(!(d == 0 || (d == 1 && EXHAUST() := blocks[p[-1]][-1])))
					append p;
		}
	return toSet(nonzero);
}

value main(list[value] args)
{debug=true; return validate(X); }

int simulate(LABEL(str label), int sp) = sp;
int simulate(JMP(str label), int sp) = sp;
int simulate(JMPTRUE(str label), int sp) = sp - 1;
int simulate(JMPFALSE(str label), int sp) = sp - 1;
int simulate(TYPESWITCH(list[str] labels), int sp) = sp - 1;
int simulate(SWITCH(map[int,str] caseLabels, str caseDefault, bool useConcreteFingerprint), int sp) =
	sp - 1;
int simulate(JMPINDEXED(list[str] labels), int sp) = sp - 1;

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

int simulate(STOREMODULEVAR(str fuid), int sp) = sp + 0;
int simulate(LOADLOCREF(int pos), int sp) = sp + 1;
int simulate(LOADLOCDEREF(int pos), int sp) = sp + 1;
int simulate(STORELOCDEREF(int pos), int sp) = sp + 0;
int simulate(LOADVARREF(str fuid, int pos), int sp) = sp + 1;
int simulate(LOADVARDEREF(str fuid, int pos), int sp) = sp + 1;
int simulate(STOREVARDEREF(str fuid, int pos), int sp) = sp + 0;
int simulate(CALL(str fuid, int arity), int sp) = sp - arity + 1;

int simulate(CALLDYN(int arity), int sp) = sp - 1 - arity + 1;
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
		           int reflect), int sp) {
	if(\tuple(list[value] params) := parameterTypes	 && \tuple(list[value] keywordParams) := keywordTypes){
		sp =  sp - size(params) + 1;      
		if(size(keywordParams) > 0){
			sp -= 2;
		}  
		return sp;
	}
	throw "CALLJAVA: cannot match <parameterTypes>";
}	
int simulate( RETURN0()	, int sp) = sp + 0;
int simulate(RETURN1(int arity), int sp) = sp - arity;

int simulate(FAILRETURN(), int sp) = sp + 0;
int simulate(FILTERRETURN(), int sp) = sp + 0;
int simulate(THROW(loc src), int sp) = sp + 0;

int simulate(CREATE(str fuid, int arity) , int sp) = sp - arity + 1;
int simulate(CREATEDYN(int arity), int sp) = sp - 1 - arity + 1;
int simulate(NEXT0(), int sp) = sp + 0;
int simulate(NEXT1(), int sp) = sp - 1;
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
//| RE\set()
//| SHIFT()
	
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

test bool validate1() =
	validate([
    CHECKARGTYPEANDCOPY(
      0,
      \int(),
      3),
    JMPFALSE("ELSE_LAB0"),
    LOADLOC(3),
    LOADCON(0),
    CALLPRIM(
      "int_equal_int",
      2,
      |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Fib.rsc|(63,6,<3,18>,<3,24>)),
    JMPFALSE("ELSE_LAB1"),
    LOADCON(0),
    RETURN1(1),
    LABEL("ELSE_LAB1"),
    LOADLOC(3),
    LOADCON(1),
    CALLPRIM(
      "int_equal_int",
      2,
      |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Fib.rsc|(78,6,<3,33>,<3,39>)),
    JMPFALSE("ELSE_LAB2"),
    LOADCON(1),
    RETURN1(1),
    LABEL("ELSE_LAB2"),
    LOADLOC(3),
    LOADCON(1),
    CALLPRIM(
      "int_subtract_int",
      2,
      |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Fib.rsc|(97,3,<3,52>,<3,55>)),
    CALLMUPRIM("make_mmap",0),
    OCALL(
      "experiments::Compiler::Examples::Fib/fib(int();)#3/bool_scope#0/use:fib#3(int();)#93_8",
      2,
      |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Fib.rsc|(93,8,<3,48>,<3,56>)),
    LOADLOC(3),
    LOADCON(2),
    CALLPRIM(
      "int_subtract_int",
      2,
      |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Fib.rsc|(108,3,<3,63>,<3,66>)),
    CALLMUPRIM("make_mmap",0),
    OCALL(
      "experiments::Compiler::Examples::Fib/fib(int();)#3/bool_scope#0/use:fib#3(int();)#104_8",
      2,
      |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Fib.rsc|(104,8,<3,59>,<3,67>)),
    CALLPRIM(
      "int_add_int",
      2,
      |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Fib.rsc|(93,19,<3,48>,<3,67>)),
    RETURN1(1),
    LABEL("ELSE_LAB0"),
    FAILRETURN()
  ]) == ();
  
list[Instruction] X =
[
  CHECKARGTYPEANDCOPY(
    0,
    \loc(),
    4),
  JMPFALSE("ELSE_LAB0"),
  CHECKARGTYPEANDCOPY(
    1,
    adt(
      "GrammarDefinition",
      []),
    5),
  JMPFALSE("ELSE_LAB1"),
  CALLPRIM(
    "listwriter_open",
    0,
    |std:///lang/rascal/format/Grammar.rsc|(1037,142,<32,2>,<34,3>)),
  STORELOC(7),
  POP(),
  LOADLOCREF(6),
  LOADLOC(5),
  LOADCON("modules"),
  CALLPRIM(
    "adt_field_access",
    2,
    |std:///lang/rascal/format/Grammar.rsc|(1047,11,<32,12>,<32,23>)),
  APPLY("Library/ENUMERATE_AND_ASSIGN",2),
  CREATEDYN(0),
  STORELOC(14),
  POP(),
  LABEL("CONTINUE_TMP0"),
  LOADLOC(14),
  NEXT0(),
  JMPFALSE("BREAK_TMP0"),
  LOADLOC(4),
  LOADCON("/"),
  CALLPRIM(
    "loc_add_str",
    2,
    |std:///lang/rascal/format/Grammar.rsc|(1077,12,<33,15>,<33,27>)),
  LOADBOOL(false),
  STORELOC(8),
  POP(),
  LOADBOOL(false),
  STORELOC(9),
  POP(),
  LOADBOOL(false),
  STORELOC(10),
  POP(),
  LOAD_NESTED_FUN("lang::rascal::format::Grammar/definition2disk(\\loc();adt(\"GrammarDefinition\",[]);)#31/PHI_0","lang::rascal::format::Grammar/definition2disk(\\loc();adt(\"GrammarDefinition\",[]);)#31"),
  LOADLOC(6),
  LOADLOCREF(8),
  LOADLOCREF(9),
  LOADLOCREF(10),
  LOADLOCREF(11),
  LOADLOCREF(12),
  LOADCON("lang::rascal::format::Grammar/definition2disk(\\loc();adt(\"GrammarDefinition\",[]);)#31/PHI_0"),
  LOADCON({}),
  LOADCON(false),
  LOADCON((
      
    )),
  CALLMUPRIM("make_descendant_descriptor",4),
  LOADBOOL(true),
  CALL("Library/TRAVERSE_BOTTOM_UP",9),
  STORELOC(13),
  POP(),
  LOADLOC(10),
  JMPFALSE("ELSE_LAB11"),
  LOADLOC(13),
  RETURN1(1),
  LABEL("ELSE_LAB11"),
  LOADLOC(13),
  CALLPRIM(
    "loc_add_str",
    2,
    |std:///lang/rascal/format/Grammar.rsc|(1077,44,<33,15>,<33,59>)),
  LOADCON("extension"),
  LOADCON(".rsc"),
  CALLPRIM(
    "loc_field_update",
    3,
    |std:///lang/rascal/format/Grammar.rsc|(1076,66,<33,14>,<33,80>)),
  LOADLOC(5),
  LOADCON("modules"),
  CALLPRIM(
    "adt_field_access",
    2,
    |std:///lang/rascal/format/Grammar.rsc|(1158,11,<33,96>,<33,107>)),
  LOADLOC(6),
  CALLPRIM(
    "map_subscript",
    2,
    |std:///lang/rascal/format/Grammar.rsc|(1158,14,<33,96>,<33,110>)),
  CALLMUPRIM("make_mmap",0),
  OCALL(
    "lang::rascal::format::Grammar/definition2disk(\\loc();adt(\"GrammarDefinition\",[]);)#31/blk#0/bool_scope#0/blk#0/blk#0/use:module2rascal#33(adt(\"GrammarModule\",[]);)#1144_29",
    2,
    |std:///lang/rascal/format/Grammar.rsc|(1144,29,<33,82>,<33,111>)),
  CALLMUPRIM("make_mmap",0),
  OCALL(
    "lang::rascal::format::Grammar/definition2disk(loc();adt(\"GrammarDefinition\",[]);)#31/blk#0/bool_scope#0/blk#0/blk#0/use:writeFile#33(\\loc();str();)#1066_108",
    3,
    |std:///lang/rascal/format/Grammar.rsc|(1066,108,<33,4>,<33,112>)),
  POP(),
  JMP("CONTINUE_TMP0"),
  LABEL("BREAK_TMP0"),
  LOADLOC(7),
  CALLPRIM(
    "listwriter_close",
    1,
    |std:///lang/rascal/format/Grammar.rsc|(1037,142,<32,2>,<34,3>)),
  RETURN1(1),
  LABEL("ELSE_LAB1"),
  FAILRETURN(),
  LABEL("ELSE_LAB0"),
  FAILRETURN()
]
;
	

	
