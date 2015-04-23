module experiments::Compiler::muRascal2RVM::StackValidator

import experiments::Compiler::RVM::AST;
import Type;
import List;
import Set;
import String;
import Relation;
import Map;
import IO;
import Node;
import analysis::graphs::Graph;
import ParseTree;
import util::Math;
import experiments::Compiler::muRascal::AST;

bool debug = false;

/*
 * Compute stack offset and validate their usage:
 * - All jumps to a label should have the same stack size
 * - Compute stack offsets for exceptions
 */

// Split a list of instructions into basic blocks
// Basic blocks are identified by serial number
// By convention, the entry block has index 0.

private map[int, list[Instruction]] makeBlocks(list[Instruction] instructions){
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
	
	return blocks;
}

// Turn basic blocks into a control flow graph
// By convention, the root node has index 0.

private Graph[int] makeGraph(map[int, list[Instruction]]  blocks, set[str] exceptionTargets){
	labels = ();
	graph = {};
	blockNumbers = domain(blocks);
	for(int i <- blockNumbers){
		current = blocks[i];
		if(LABEL(str name) := current[0]){
			labels[name] = i;
		}
	}
	
	for(int i <- blockNumbers){
		current = blocks[i];
		if(JMP(str name) := current[-1]){
			graph += <i, labels[name]>;
		}  else if(   JMPTRUE(str name) := current[-1] 
		           || JMPFALSE(str name) := current[-1]){
			graph += {<i, labels[name]>, <i, i + 1>};
		} else if(SWITCH(map[int,str] caseLabels, str caseDefault, bool useConcreteFingerprint) := current[-1]){
			 graph += {<i, labels[caseLabels[cl]]> | cl <- caseLabels} + {<i, labels[caseDefault]>};
		} else if(TYPESWITCH(list[str] caseLabels) := current[-1]){
			 graph += {<i, labels[cl]> | cl <- caseLabels};
		} else if(getName(current[-1]) notin {"RETURN0", "RETURN1", "EXHAUST", "FAILRETURN", "THROW"}){
			  if(i + 1 in blockNumbers){
			  	if(LABEL(name) := blocks[i + 1][0]){
			  		if(name notin exceptionTargets){
			  			graph += {<i, i + 1>};
			  		}
			  	} else {
			  		graph += {<i, i + 1>};
			  	}
			}
		}
	}
	return carrierR(graph, domain(blocks));
}

// Compute stack effects per basic block

private tuple[map[int,int], map[int,int]] computeStackEffects(map[int, list[Instruction]] blocks){
	deltaSPBlock = ();
	maxSPBlock = ();
	for(int i <- sort(domain(blocks))){
		if(debug) println("Block <i>:");
		int sp = 0;
		int mx = 0;
		for(ins <- blocks[i]){
			if(debug)println("\t<sp>: <ins>");
			sp = simulate(ins, sp);
			if(sp > mx){
				mx = sp;
			}
		}
		if(debug)println("Block <i>, delta sp = <sp>, max = <mx>");
		deltaSPBlock[i] = sp;
		maxSPBlock[i] = mx;
	}
	return <deltaSPBlock, maxSPBlock>;
}

// Identify the start and end instructions for a basic block

private bool isBlockStartInstruction(Instruction ins) = LABEL(_) := ins;

private bool isBlockEndInstruction(Instruction ins) = 
	getName(ins) in {"JMP", "JMPFALSE", "JMPTRUE", "RETURN0", "RETURN1", "SWITCH", "TYPESWITCH", "THROW", "FAILRETURN", "HALT", "EXHAUST"};

// Validate a list of instructions.
// Given:
// - a list of instructions
// - a list of exceptions
// compute a tuple consisting of
// - the maxiaml stack size that can be reached by the instructions
// - an updated list of exceptions in which the fromSP field has received the stack entry value of the corresponding try statement

tuple[int, lrel[str from, str to, Symbol \type, str target, int fromSP]] validate(loc src, list[Instruction] instructions,  lrel[str from, str to, Symbol \type, str target, int fromSP] exceptions) {

	if(isEmpty(instructions)){
		return <1, exceptions>;	// Allow a single constant to be pushed in _init and _testsuite functions
	}

	blocks = makeBlocks(instructions);
	label2block = (lbl : blk | blk <- blocks, LABEL(lbl) := blocks[blk][0]);

	targets = toSet(exceptions.target);
	
	graph = makeGraph(blocks, targets);
	<deltaSPBlock, maxSPBlock> = computeStackEffects(blocks);

	stackAtEntry = (0: 0);
	stackAtExit  = (0: deltaSPBlock[0]);
	maxSPPath    = (0: maxSPBlock[0]);
	
	void update(int blk, int successor, int sp){
	    if(debug)println("update <blk>, <successor>, <sp>");
		if(stackAtEntry[successor]?){
			if(stackAtEntry[successor] != sp){
				throw("Inconsistent stackAtEntry for <src>, from block <blk> to <successor>: <stackAtEntry[successor]> versus <sp>");
			}
			maxSPPath[successor] = max(max(maxSPPath[blk], sp + maxSPBlock[successor]), maxSPPath[successor]);
		} else {
			stackAtEntry[successor] = sp;
			stackAtExit[successor] = sp + deltaSPBlock[successor];
			maxSPPath[successor] = max(maxSPPath[blk], sp + maxSPBlock[successor]);
		}	
	}
	
	solve(stackAtEntry, maxSPPath){
		for(blk <- domain(blocks)){
			if(stackAtEntry[blk]?){
				sp = stackAtExit[blk];
				for(successor <- graph[blk]){
					update(blk, successor, sp);
			   }
			}
		}
		for(exc <- exceptions){
			fromBlk = label2block[exc.from];
			targetBlk = label2block[exc.target];
			if(stackAtEntry[fromBlk]? && !stackAtEntry[targetBlk]?){
				update(fromBlk, targetBlk,  stackAtEntry[fromBlk]);
			}
		}
	}
	
	maxStack = max(range(maxSPPath));
	
	// Update the fromSP fields
	exceptions = for(exc <- exceptions){
					exc.fromSP = stackAtEntry[label2block[exc.from]] ? 0;
					append exc;
				 };
	
	if(debug){
		println("graph:          <graph>");
		println("stackAtEntry:   <stackAtEntry>");
		println("stackAtExit:    <stackAtExit>");
		println("maxSPBlock:     <maxSPBlock>");
		println("maxSPPath:      <maxSPPath>");
		println("maxStack:       <maxStack>");
		println("label2block:    <label2block>");
		println("exceptions:     <exceptions>");
	}
	
	return <maxStack + 1, exceptions>;  // + 1: to turn an index into a length;
}

value main(list[value] args) =
validate(
|project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst1.rsc|,
[
  LOADCON(false),
  LOADLOC(0),
  SWITCH(
    (101776608:"L52_101776608"),
    "L52",
    false),
  LABEL("L52_101776608"),
  POP(),
  LOAD_NESTED_FUN("experiments::Compiler::Examples::Tst2/generateNewItems(adt(\"Grammar\",[]);)#269/PHI_0/ALL_8(0)","experiments::Compiler::Examples::Tst2/generateNewItems(adt(\"Grammar\",[]);)#269/PHI_0"),
  CREATEDYN(0),
  STORELOC(14),
  NEXT0(),
  JMPFALSE("ELSE_LAB65"),
  LOADBOOL(true),
  STORELOCDEREF(1),
  POP(),
  CALLPRIM(
    "listwriter_open",
    0,
    |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst2.rsc|(12509,60,<278,6>,<279,21>)),
  STORELOC(10),
  POP(),
  LABEL("L54"),
  LOADLOC(8),
  LOADCON("conditional"),
  CALLPRIM(
    "is",
    2,
    |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst2.rsc|(12516,16,<278,13>,<278,29>)),
  JMPFALSE("ELSE_LAB67"),
  LOADCON(true),
  JMP("CONTINUE_LAB67"),
  LABEL("ELSE_LAB67"),
  LOADLOC(8),
  LOADCON("label"),
  CALLPRIM(
    "is",
    2,
    |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst2.rsc|(12536,10,<278,33>,<278,43>)),
  LABEL("CONTINUE_LAB67"),
  JMPFALSE("BREAK_TMP8"),
  LOADLOC(8),
  LOADCON("symbol"),
  CALLPRIM(
    "adt_field_access",
    2,
    |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst2.rsc|(12560,8,<279,12>,<279,20>)),
  STORELOC(8),
  POP(),
  JMP("L54"),
  LABEL("BREAK_TMP8"),
  LOADLOC(10),
  CALLPRIM(
    "listwriter_close",
    1,
    |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst2.rsc|(12509,60,<278,6>,<279,21>)),
  POP(),
  LOADCON(false),
  LOADLOC(8),
  STORELOC(11),
  SWITCH(
    (25942208:"L56_25942208"),
    "L56",
    false),
  LABEL("L56_25942208"),
  POP(),
  LOADLOC(11),
  LOADCON("iter"),
  LOADTYPE(adt(
      "Symbol",
      [])),
  LOADLOCREF(9),
  APPLY("Library/MATCH_TYPED_VAR",2),
  CALLMUPRIM("make_array",0),
  CALLMUPRIM("make_array",0),
  APPLY("Library/MATCH_KEYWORD_PARAMS",2),
  CALLMUPRIM("make_array",2),
  APPLY("Library/MATCH_SIMPLE_CALL_OR_TREE",2),
  APPLYDYN(1),
  CREATEDYN(0),
  STORELOC(15),
  NEXT0(),
  JMPFALSE("ELSE_LAB68"),
  LOADVAR("experiments::Compiler::Examples::Tst2/generateNewItems(adt(\"Grammar\",[]);)#269",4),
  LOADLOC(8),
  LABEL("TRY_FROM_L58"),
  LOADVAR("experiments::Compiler::Examples::Tst2/generateNewItems(adt(\"Grammar\",[]);)#269",4),
  LOADLOC(8),
  CALLPRIM(
    "map_subscript",
    2,
    |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst2.rsc|(12643,8,<283,10>,<283,18>)),
  LABEL("TRY_TO_L58"),
  LOADLOC(7),
  LOADCON(0),
  CALLMUPRIM("make_mmap",0),
  OCALL(
    "experiments::Compiler::Examples::Tst2/generateNewItems(adt(\"Grammar\",[]);)#269/blk#0/bool_scope#0/blk#0/blk#1/bool_scope#0/blk#0/bool_scope#0/use:item#283(adt(\"Production\",[]);int();)#12662_9",
    3,
    |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst2.rsc|(12662,9,<283,29>,<283,38>)),
  LOADVAR("experiments::Compiler::Examples::Tst2/generateNewItems(adt(\"Grammar\",[]);)#269",3),
  LOADLOC(9),
  LOADCON(0),
  CALLMUPRIM("make_mmap",0),
  OCALL(
    "experiments::Compiler::Examples::Tst2/generateNewItems(adt(\"Grammar\",[]);)#269/blk#0/bool_scope#0/blk#0/blk#1/bool_scope#0/blk#0/bool_scope#0/use:sym2newitem#283(adt(\"Grammar\",[]);adt(\"Symbol\",[]);int();)#12672_23",
    4,
    |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst2.rsc|(12672,23,<283,39>,<283,62>)),
  CALLPRIM(
    "map_create",
    2,
    |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst2.rsc|(12661,35,<283,28>,<283,63>)),
  CALLPRIM(
    "map_add_map",
    2,
    |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst2.rsc|(12643,14,<283,10>,<283,24>)),
  CALLPRIM(
    "map_update",
    3,
    |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst2.rsc|(12643,8,<283,10>,<283,18>)),
  STOREVAR("experiments::Compiler::Examples::Tst2/generateNewItems(adt(\"Grammar\",[]);)#269",4),
  STORELOC(11),
  POP(),
  LOADCON(true),
  JMP("CONTINUE_LAB68"),
  LABEL("ELSE_LAB68"),
  LOADCON(false),
  LABEL("CONTINUE_LAB68"),
  LABEL("L56"),
  JMPTRUE("CONTINUE_L56"),
  LOADCON(777),
  STORELOC(11),
  POP(),
  LABEL("CONTINUE_L56"),
  LOADLOC(11),
  POP(),
  LOADLOC(0),
  RETURN1(1),
  LABEL("ELSE_LAB65"),
  LOADLOC(0),
  RETURN1(1),
  LABEL("L52"),
  JMPTRUE("CONTINUE_L52"),
  LOADLOC(0),
  RETURN1(1),
  LABEL("CONTINUE_L52"),
  LOADLOC(0),
  LABEL("CATCH_FROM_L61"),
  LABEL("CATCH_FROM_L59"),
  STORELOC(13),
  POP(),
  LOADLOC(13),
  UNWRAPTHROWNLOC(12),
  JMP("L63"),
  LABEL("FAIL_LAB70"),
  JMP("ELSE_LAB70"),
  LABEL("L63"),
  LOADLOC(12),
  CALLMUPRIM("get_name",1),
  LOADCON({"IndexOutOfBounds","NoSuchField","NoSuchAnnotation","NoSuchKey","UninitializedVariable"}),
  CALLPRIM(
    "elm_in_set",
    2,
    |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst2.rsc|(12643,14,<283,10>,<283,24>)),
  JMPFALSE("ELSE_LAB70"),
  LOADVAR("experiments::Compiler::Examples::Tst2/generateNewItems(adt(\"Grammar\",[]);)#269",5),
  JMP("CONTINUE_LAB70"),
  LABEL("ELSE_LAB70"),
  LOADLOC(13),
  THROW(|project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst2.rsc|(12643,14,<283,10>,<283,24>)),
  LABEL("CONTINUE_LAB70"),
  LABEL("CATCH_TO_L59"),
  JMP("TRY_TO_L58")
],
[<"TRY_FROM_L58","TRY_TO_L58",\value(),"CATCH_FROM_L59",0>]);

// Simulate the effect of each RVM instruction on the stack pointer

int simulate(LABEL(str label), int sp) 					= sp;
int simulate(JMP(str label), int sp) 					= sp;
int simulate(JMPTRUE(str label), int sp) 				= sp - 1;
int simulate(JMPFALSE(str label), int sp) 				= sp - 1;
int simulate(TYPESWITCH(list[str] labels), int sp) 		= sp - 1;
int simulate(SWITCH(map[int,str] caseLabels, 
				   str caseDefault, 
				   bool useConcreteFingerprint), 
		     int sp) 									= sp - 1;
int simulate(JMPINDEXED(list[str] labels), int sp) 		= sp - 1;

int simulate(LOADBOOL(bool bval), int sp) 				= sp + 1;
int simulate(LOADINT(int nval), int sp) 				= sp + 1;
int simulate(LOADCON(value val), int sp) 				= sp + 1;
int simulate(LOADTYPE(Symbol \type), int sp) 			= sp + 1;
int simulate(LOADFUN(str fuid), int sp) 				= sp + 1;
int simulate(LOAD_NESTED_FUN(str fuid, str scopeIn), 
			 int sp) 									= sp + 1;
int simulate(LOADCONSTR(str fuid), int sp) 				= sp + 1;
int simulate(LOADOFUN(str fuid), int sp) 				= sp + 1;
int simulate(LOADLOC(int pos), int sp) 					= sp + 1;
int simulate(STORELOC(int pos), int sp) 				= sp;
int simulate(RESETLOCS(list[int] positions), int sp) 	= sp + 1;

int simulate(LOADLOCKWP(str name), int sp) 				= sp + 1;
int simulate(STORELOCKWP(str name), int sp) 			= sp;
int simulate(UNWRAPTHROWNLOC(int pos), int sp) 			= sp;
int simulate(UNWRAPTHROWNVAR(str fuid, int pos),
			 int sp) 									= sp;
int simulate(LOADVAR(str fuid, int pos) , int sp) 		= sp + 1;
int simulate(STOREVAR(str fuid, int pos), int sp) 		= sp;
int simulate(LOADVARKWP(str fuid, str name), int sp) 	= sp + 1;
int simulate(STOREVARKWP(str fuid, str name), int sp) 	= sp;
int simulate(LOADMODULEVAR(str fuid), int sp) 			= sp + 1;

int simulate(STOREMODULEVAR(str fuid), int sp) 			= sp;
int simulate(LOADLOCREF(int pos), int sp) 				= sp + 1;
int simulate(LOADLOCDEREF(int pos), int sp) 			= sp + 1;
int simulate(STORELOCDEREF(int pos), int sp) 			= sp;
int simulate(LOADVARREF(str fuid, int pos), int sp) 	= sp + 1;
int simulate(LOADVARDEREF(str fuid, int pos), int sp) 	= sp + 1;
int simulate(STOREVARDEREF(str fuid, int pos), int sp) 	= sp;
int simulate(CALL(str fuid, int arity), int sp) 		= sp - arity + 1;

int simulate(CALLDYN(int arity), int sp) 				= sp - 1 - arity + 1;
int simulate(APPLY(str fuid, int arity), int sp) 		= sp - arity + 1;
int simulate(APPLYDYN(int arity), int sp) 				= sp - arity - 1 + 1;
int simulate(CALLCONSTR(str fuid, int arity), int sp) 	= sp - arity + 1;
int simulate(OCALL(str fuid, int arity, loc src), 
			 int sp) 									= sp -arity + 1;
int simulate(OCALLDYN(Symbol types, int arity, loc src), 
			 int sp) 									= sp - 1 - arity + 1;
int simulate(CALLMUPRIM(str name, int arity), int sp) 	= sp - arity + 1;
int simulate(CALLPRIM(str name, int arity, loc src), 
			 int sp) 									= sp - arity + 1;
int simulate(CALLJAVA(str name, str class, 
		           Symbol parameterTypes,
		           Symbol keywordTypes,
		           int reflect), int sp) {
	if(\tuple(list[Symbol] params) := parameterTypes	&&
	   \tuple(list[Symbol] keywordParams) := keywordTypes){
		sp =  sp - size(params) + 1;      
		if(size(keywordParams) > 0){
			sp -= 2;
		}  
		return sp;
	}
	throw "CALLJAVA: cannot match <parameterTypes>";
}
	
int simulate(RETURN0(), int sp) 						= sp;
int simulate(RETURN1(int arity), int sp) 				= sp - arity;

int simulate(FAILRETURN(), int sp) 						= sp;
int simulate(FILTERRETURN(), int sp) 					= sp;
int simulate(THROW(loc src), int sp) 					= sp + 2;		// TODO Check This.

int simulate(CREATE(str fuid, int arity) , int sp)		= sp - arity + 1;
int simulate(CREATEDYN(int arity), int sp) 				= sp - 1 - arity + 1;
int simulate(NEXT0(), int sp) 							= sp;
int simulate(NEXT1(), int sp) 							= sp - 1;
int simulate(YIELD0(), int sp) 							= sp + 1;
int simulate(YIELD1(int arity), int sp) 				= sp - arity + 1;
int simulate(EXHAUST(), int sp) 						= sp;
int simulate(GUARD(), int sp)							= sp - 1;
int simulate(PRINTLN(int arity), int sp) 				= sp - arity;
int simulate(POP(), int sp) 							= sp - 1;
int simulate(HALT(), int sp) 							= sp;
int simulate(SUBSCRIPTARRAY(), int sp) 					= sp - 1;
int simulate(SUBSCRIPTLIST(), int sp) 					= sp - 1;
int simulate(LESSINT()	, int sp)						= sp - 1;
int simulate(GREATEREQUALINT(), int sp) 				= sp - 1;
int simulate(ADDINT(), int sp) 							= sp - 1;
int simulate(SUBTRACTINT(), int sp) 					= sp - 1;
int simulate(ANDBOOL(), int sp) 						= sp - 1;
int simulate(TYPEOF(), int sp) 							= sp;
int simulate(SUBTYPE(), int sp) 						= sp - 1;
int simulate(CHECKARGTYPEANDCOPY(
			int pos1, Symbol \type, int pos2), int sp)	= sp + 1;
int simulate(LOADBOOL(bool bval), int sp) 				= sp + 1;
int simulate(LOADBOOL(bool bval), int sp) 				= sp + 1;