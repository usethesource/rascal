module lang::rascalcore::compile::muRascal2RVM::StackValidator

import lang::rascalcore::compile::RVM::AST;
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
import lang::rascalcore::compile::muRascal::AST;

import lang::rascalcore::check::AType;

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
		} else if(getName(current[-1]) notin {"RETURN0", "RETURN1", "CORETURN0", "CORETURN1","EXHAUST", "FAILRETURN", "THROW"}){
			  if(i + 1 in blockNumbers){
			  	if(LABEL(str name) := blocks[i + 1][0]){	// TODO: type was added for new (experimental) type checker; otherwise clashes with previous declaration
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
		bool accu = false;
		for(ins <- blocks[i]){
			if(debug)println("\t<sp>, <accu ? "Y" : "N">: <ins>");
			<sp, accu> = simulate(ins, sp);
			
			if(sp > mx){
				mx = sp;
			}
		}
		if(debug)println("Block <i>, delta sp = <sp>, max = <mx>");
		deltaSPBlock[i] = sp;// + (accu ? 1 : 0);
		maxSPBlock[i] = mx;
	}
	return <deltaSPBlock, maxSPBlock>;
}

// Identify the start and end instructions for a basic block

private bool isBlockStartInstruction(Instruction ins) = LABEL(_) := ins;

private bool isBlockEndInstruction(Instruction ins) = 
	getName(ins) in {"JMP", "JMPFALSE", "JMPTRUE", "RETURN0", "RETURN1", "CORETURN0", "CORETURN1", "SWITCH", "TYPESWITCH", "THROW", "FAILRETURN", "HALT", "EXHAUST"};

// Validate a list of instructions.
// Given:
// - a list of instructions
// - a list of exceptions
// compute a tuple consisting of
// - the maxiaml stack size that can be reached by the instructions
// - an updated list of exceptions in which the fromSP field has received the stack entry value of the corresponding try statement

tuple[int, lrel[str from, str to, AType \type, str target, int fromSP]] validate(loc src, list[Instruction] instructions,  lrel[str from, str to, AType \type, str target, int fromSP] exceptions) {
    
	if(isEmpty(instructions)){
		return <1, exceptions>;	// Allow a single constant to be pushed in _init and _testsuite functions
	}

	blocks = makeBlocks(instructions);
	label2block = (lbl : blk | blk <- blocks, LABEL(lbl) := blocks[blk][0]);

	targets = toSet(exceptions.target);
	
	graph = makeGraph(blocks, targets);
	<deltaSPBlock, maxSPBlock> = computeStackEffects(blocks);
	
//	useDefBlock = computeUseDefEffects(blocks);
	
//	findUnassigned(graph, blocks);

	stackAtEntry = (0: 0);
	stackAtExit  = (0: deltaSPBlock[0]);
	maxSPPath    = (0: maxSPBlock[0]);
	
	void update(int blk, int successor, int sp){
	    //if(debug)println("update <blk>, <successor>, <sp>");
		if(stackAtEntry[successor]?){
			if(stackAtEntry[successor] != sp){
			    println("graph:          <graph>");
                println("stackAtEntry:   <stackAtEntry>");
                println("stackAtExit:    <stackAtExit>");
                println("maxSPBlock:     <maxSPBlock>");
                println("maxSPPath:      <maxSPPath>");
                //println("maxStack:       <maxStack>");
                println("label2block:    <label2block>");
                println("exceptions:     <exceptions>");
                b2l = invertUnique(label2block);
				throw("Inconsistent stackAtEntry at <src>, from block <blk> (<b2l[blk]?blk>) to <successor> (<b2l[successor]?successor>): <stackAtEntry[successor]> versus <sp> 
				      '(all links to <b2l[successor]>: <for(b <- invert(graph)[successor]){><b2l[b]?b> <}>)");
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
	
	return <maxStack + 1 + 1, exceptions>;  // + 1: to turn an index into a length; 
										    // + 1 to cater for some imprecision
}



// Simulate the effect of each RVM instruction on the stack pointer

alias Effect = tuple[int sp, bool accu];

Effect simulate(LABEL(str label), int sp) 					= <sp,     false>; 
Effect simulate(JMP(str label), int sp) 					= <sp,     false>; 
Effect simulate(JMPTRUE(str label), int sp) 				= <sp,     false>; 
Effect simulate(JMPFALSE(str label), int sp) 				= <sp,     false>; 
Effect simulate(TYPESWITCH(list[str] labels), int sp) 		= <sp,     false>; 
Effect simulate(SWITCH(map[int,str] caseLabels, 
				   str caseDefault, 
				   bool useConcreteFingerprint), 
		     int sp) 									    = <sp,     false>; 

Effect simulate(LOADBOOL(bool bval), int sp) 				= <sp,     true>; 
Effect simulate(LOADINT(int nval), int sp) 				    = <sp,     true>; 
Effect simulate(LOADCON(value val), int sp) 				= <sp,     true>; 
Effect simulate(PUSHCON(value val), int sp)                 = <sp + 1, false>;
Effect simulate(LOADTYPE(AType \type), int sp) 			= <sp,     true>; 
Effect simulate(PUSHTYPE(AType \type), int sp)             = <sp + 1, false>; 

Effect simulate(PUSHACCU(), int sp)                         = <sp + 1, false>;
Effect simulate(POPACCU(), int sp)                          = <sp - 1, true>; 

Effect simulate(PUSH_ROOT_FUN(str fuid), int sp) 		    = <sp + 1, false>;
Effect simulate(PUSH_NESTED_FUN(str fuid, str scopeIn), 
			 int sp) 									    = <sp + 1, false>;
Effect simulate(PUSHCONSTR(str fuid), int sp) 				= <sp + 1, false>;
Effect simulate(PUSHOFUN(str fuid), int sp) 				= <sp + 1, false>;
Effect simulate(LOADLOC(int pos), int sp) 					= <sp,     true>; 
Effect simulate(PUSHLOC(int pos), int sp)                   = <sp + 1, false>;
Effect simulate(STORELOC(int pos), int sp) 				    = <sp,     false>; 
Effect simulate(RESETLOCS(list[int] positions), int sp) 	= <sp,     false>;
Effect simulate(RESETLOC(int pos), int sp)                  = <sp,     false>; 

Effect simulate(LOADLOCKWP(str name), int sp) 				= <sp,     true>;
Effect simulate(PUSHLOCKWP(str name), int sp)               = <sp + 1, false>;
Effect simulate(STORELOCKWP(str name), int sp) 			    = <sp,     false>; 
Effect simulate(UNWRAPTHROWNLOC(int pos), int sp) 			= <sp,     false>; 
Effect simulate(UNWRAPTHROWNVAR(str fuid, int pos),
			 int sp) 									    = <sp,     false>; 
Effect simulate(LOADVAR(str fuid, int pos) , int sp) 		= <sp,     true>; 
Effect simulate(PUSHVAR(str fuid, int pos) , int sp)        = <sp + 1, false>; 
Effect simulate(STOREVAR(str fuid, int pos), int sp) 		= <sp,     false>; 
Effect simulate(RESETVAR(str fuid, int pos), int sp)        = <sp,     false>; 
Effect simulate(LOADVARKWP(str fuid, str name), int sp) 	= <sp,     true>;
Effect simulate(PUSHVARKWP(str fuid, str name), int sp)     = <sp + 1, false>;
Effect simulate(STOREVARKWP(str fuid, str name), int sp) 	= <sp,     false>; 

Effect simulate(LOADLOCREF(int pos), int sp) 				= <sp,     true>;
Effect simulate(PUSHLOCREF(int pos), int sp)                = <sp + 1, false>;
Effect simulate(LOADLOCDEREF(int pos), int sp) 			    = <sp,     true>;
Effect simulate(PUSHLOCDEREF(int pos), int sp)              = <sp + 1, false>;
Effect simulate(STORELOCDEREF(int pos), int sp) 			= <sp,     false>; 
Effect simulate(LOADVARREF(str fuid, int pos), int sp) 	    = <sp,     true>;
Effect simulate(PUSHVARREF(str fuid, int pos), int sp)      = <sp + 1, false>;
Effect simulate(LOADVARDEREF(str fuid, int pos), int sp) 	= <sp,     true>;
Effect simulate(PUSHVARDEREF(str fuid, int pos), int sp)    = <sp + 1, false>;
Effect simulate(STOREVARDEREF(str fuid, int pos), int sp) 	= <sp,     false>; 
Effect simulate(CALL(str fuid, int arity), int sp) 		    = <sp - arity, true>;

Effect simulate(CALLDYN(int arity), int sp) 				= <sp - 1 - arity, true>;
Effect simulate(APPLY(str fuid, int arity), int sp) 		= <sp - arity + 1, false>;
Effect simulate(APPLYDYN(int arity), int sp) 				= <sp - arity - 1 + 1, false>;
Effect simulate(CALLCONSTR(str fuid, int arity), int sp) 	= <sp - arity, true>;
Effect simulate(OCALL(str fuid, int arity, loc src), int sp)= <sp - arity, true>;
Effect simulate(OCALLDYN(AType types, int arity, loc src), 
			 int sp) 									    = <sp - 1 - arity, true>;
Effect simulate(CALLMUPRIM0(str name), int sp)              = <sp,     true>;
Effect simulate(CALLMUPRIM1(str name), int sp)              = <sp,     true>; 
Effect simulate(CALLMUPRIM2(str name), int sp)              = <sp - 1, true>; 
Effect simulate(CALLMUPRIMN(str name, int arity), int sp)   = <sp - arity, true>;

Effect simulate(PUSHCALLMUPRIM0(str name), int sp)          = <sp + 1, false>;
Effect simulate(PUSHCALLMUPRIM1(str name), int sp)          = <sp + 1, false>; 
Effect simulate(PUSHCALLMUPRIM2(str name), int sp)          = <sp,     false>; 
Effect simulate(PUSHCALLMUPRIMN(str name, int arity), int sp)   = <sp - arity + 1, false>;

Effect simulate(CALLPRIM0(str name, loc src), int sp)       = <sp,     true>;
Effect simulate(CALLPRIM1(str name, loc src), int sp)       = <sp,     true>; 
Effect simulate(CALLPRIM2(str name, loc src), int sp)       = <sp - 1, true>; 
             			 
Effect simulate(CALLPRIMN(str name, int arity, loc src), 
             int sp)                                        = <sp - arity, true>;

Effect simulate(PUSHCALLPRIM0(str name, loc src), int sp)   = <sp + 1, false>;
Effect simulate(PUSHCALLPRIM1(str name, loc src), int sp)   = <sp + 1, false>; 
Effect simulate(PUSHCALLPRIM2(str name, loc src), int sp)   = <sp,     false>; 	
Effect simulate(PUSHCALLPRIMN(str name, int arity, loc src), 
             int sp)                                        = <sp - arity + 1, false>;		 
			 
Effect simulate(CALLJAVA(str name, str class, 
		           AType parameterTypes,
		           AType keywordTypes,
		           int reflect), int sp) {
	if(\tuple(list[AType] params) := parameterTypes	&&
	   \tuple(list[AType] keywordParams) := keywordTypes){
		sp =  sp - size(params) + 1;      
		if(size(keywordParams) > 0){
			sp -= 2;
		}  
		return <sp, false>;
	}
	throw "CALLJAVA: cannot match <parameterTypes>";
}
	
Effect simulate(RETURN0(), int sp) 						    = <sp,     false>; 
Effect simulate(RETURN1(), int sp) 				            = <sp,     false>;

Effect simulate(CORETURN0(), int sp)                        = <sp,     false>; 
Effect simulate(CORETURN1(int arity), int sp)               = <sp - arity, false>;

Effect simulate(FAILRETURN(), int sp) 						= <sp,     false>; 
Effect simulate(FILTERRETURN(), int sp) 					= <sp,     false>; 
Effect simulate(THROW(loc src), int sp) 					= <sp + 2, false>;		// TODO Check This.

Effect simulate(CREATE(str fuid, int arity) , int sp)		= <sp - arity, true>;
Effect simulate(CREATEDYN(int arity), int sp) 				= <sp - 1 - arity, true>;
Effect simulate(NEXT0(), int sp) 							= <sp,     true>; 
Effect simulate(NEXT1(), int sp) 							= <sp - 1, true>; 
Effect simulate(YIELD0(), int sp) 							= <sp + 1, false>;
Effect simulate(YIELD1(int arity), int sp) 				    = <sp - arity + 1, false>;
Effect simulate(EXHAUST(), int sp) 						    = <sp,     false>; 
Effect simulate(GUARD(), int sp)							= <sp,     false>; 
Effect simulate(PRINTLN(int arity), int sp) 				= <sp - arity + 1, false>;
Effect simulate(POP(), int sp) 							    = <sp - 1, false>; 
Effect simulate(HALT(), int sp) 							= <sp,     false>; 
Effect simulate(SUBSCRIPTARRAY(), int sp) 					= <sp - 1, true>; 
Effect simulate(SUBSCRIPTLIST(), int sp) 					= <sp - 1, true>; 
Effect simulate(LESSINT()	, int sp)						= <sp - 1, true>; 
Effect simulate(GREATEREQUALINT(), int sp) 				    = <sp - 1, true>; 
Effect simulate(ADDINT(), int sp) 							= <sp - 1, true>; 
Effect simulate(SUBTRACTINT(), int sp) 					    = <sp - 1, true>; 
Effect simulate(ANDBOOL(), int sp) 						    = <sp - 1, true>; 
Effect simulate(TYPEOF(), int sp) 							= <sp,     true>; 
Effect simulate(SUBTYPE(), int sp) 						    = <sp - 1, true>; 
Effect simulate(VALUESUBTYPE(AType \type), int sp)         = <sp,     true>; 
Effect simulate(CHECKARGTYPEANDCOPY(
			int pos1, AType \type, int pos2), int sp)	    = <sp, false>; 

Effect simulate(VISIT(bool direction, bool fixedpoint, 
                   bool progress, bool rebuild),
                   int sp)          					    = <sp - 8 + 1, false>;
Effect simulate(CHECKMEMO(), int sp)    					= <sp,     true>;
Effect simulate(PUSHEMPTYKWMAP(), int sp)                   = <sp + 1, false>;

/*
// Simulate the effect of each RVM instruction on availability of variables

// Compute use/def per basic block

private map[int,UseDef] computeUseDefEffects(map[int, list[Instruction]] blocks){
    UseDefBlock = ();
    for(int i <- sort(domain(blocks))){
        if(debug) println("Block <i>:");
        UseDef ud = <{}, {}, {}, {}>;
        for(ins <- blocks[i]){
            if(debug)println("\t<ins>");
            ud = simulateUseDef(ins, ud);
        
        }
        if(debug)println("Block <i>, <ud>");
        UseDefBlock[i] = ud;
    }
    return UseDefBlock;
}

set[int] findUnitialized(map[int, list[Instruction]] blocks, Graph[int] graph, map[int, UseDef] useDef){
    useDefAtEntry = (0: <{},{},{},{}>);
    useDefAtExit  = (0: useDef[0]);
    
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
    
    solve(useDefAtEntry){
        for(blk <- domain(blocks)){
            if(useDefAtEntry[blk]?){
                ud = usedDefAtExit[blk];
                for(successor <- graph[blk]){
                    update(blk, successor, sp);
               }
            }
        }
        //for(exc <- exceptions){
        //    fromBlk = label2block[exc.from];
        //    targetBlk = label2block[exc.target];
        //    if(stackAtEntry[fromBlk]? && !stackAtEntry[targetBlk]?){
        //        update(fromBlk, targetBlk,  stackAtEntry[fromBlk]);
        //    }
        //}
    } 
}

alias Var = tuple[str fuid, int pos];

alias UseDef = tuple[set[int] localUses, set[int] localDefines, set[Var] globalUses, set[Var] globalDefines];

UseDef addLocalUse(UseDef ud, int pos) = 
  pos in ud.localDefines ? ud : <ud.localUses + pos, ud.localDefines, ud.globalUses, ud.globalDefines>;
  
UseDef addLocalDef(UseDef ud, int pos) = 
  <ud.localUses, ud.localDefines + pos, ud.globalUses, ud.globalDefines>;
  
UseDef addGlobalUse(UseDef ud, str fuid, int pos) = 
  <ud.localUses, ud.localDefines, ud.globalUses + <fuid, pos>, ud.globalDefines>;

UseDef addGlobalDef(UseDef ud, str fuid, int pos) = 
  <ud.localUses + pos, ud.localDefines, ud.globalUses + <fuid, pos>, ud.globalDefines>;


UseDef simulateUseDef(LOADLOC(int pos), UseDef ud)                  = addLocalUse(ud, pos);
UseDef simulateUseDef(STORELOC(int pos), UseDef ud)                 = addLocalDef(ud, pos);
UseDef simulateUseDef(RESETLOCS(list[int] positions), UseDef ud)    = ud;

UseDef simulateUseDef(LOADLOCKWP(str name), UseDef ud)              = ud;
UseDef simulateUseDef(STORELOCKWP(str name), UseDef ud)             = ud;
UseDef simulateUseDef(UNWRAPTHROWNLOC(int pos), UseDef ud)          = ud;
UseDef simulateUseDef(UNWRAPTHROWNVAR(str fuid, int pos),
             UseDef ud)                                    = ud;
UseDef simulateUseDef(LOADVAR(str fuid, int pos) , UseDef ud)       = addGlobalUse(ud, fuid, pos);
UseDef simulateUseDef(STOREVAR(str fuid, int pos), UseDef ud)       = addGlobalDef(ud, fuid, pos);
UseDef simulateUseDef(LOADVARKWP(str fuid, str name), UseDef ud)    = ud;
UseDef simulateUseDef(STOREVARKWP(str fuid, str name), UseDef ud)   = ud;
UseDef simulateUseDef(LOADMODULEVAR(str fuid), UseDef ud)           = addGlobalUse(ud, fuid, -1);

UseDef simulateUseDef(STOREMODULEVAR(str fuid), UseDef ud)          = addGlobalDef(ud, fuid, -1);
UseDef simulateUseDef(LOADLOCREF(int pos), UseDef ud)               = addLocalDef(ud, pos);
UseDef simulateUseDef(LOADLOCDEREF(int pos), UseDef ud)             = addLocalDef(ud, pos);
UseDef simulateUseDef(STORELOCDEREF(int pos), UseDef ud)            = addGlobalDef(ud, fuid, pos);
UseDef simulateUseDef(LOADVARREF(str fuid, int pos), UseDef ud)     = addGlobalUse(ud, fuid, pos);
UseDef simulateUseDef(LOADVARDEREF(str fuid, int pos), UseDef ud)   = addGlobalUse(ud, fuid, pos);
UseDef simulateUseDef(STOREVARDEREF(str fuid, int pos), UseDef ud)  = addGlobalDef(ud, fuid, pos);

UseDef simulateUseDef(CHECKARGTYPEANDCOPY(
            int pos1, Symbol \type, int pos2), UseDef ud)           = addLocalDef(addLocalDef(ud, pos1), pos2);
            
default UseDef simulateUseDef(Instruction ins, UseDef ud)           = ud;

*/