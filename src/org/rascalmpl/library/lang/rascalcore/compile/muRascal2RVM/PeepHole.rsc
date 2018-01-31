module lang::rascalcore::compile::muRascal2RVM::PeepHole

import IO;
import String;
import Set;
import List;
import Map;

import lang::rascalcore::compile::RVM::AST;

alias INS = list[Instruction];

INS peephole(INS instructions) {
    //return instructions;
	return peephole1(instructions, false); // when bprintln("**** peephole length <size(instructions)>");
}

INS peephole1(INS instructions, bool isSplit){
	if(size(instructions) < 500){
		return peephole2(instructions, isSplit);
	}
	<l, r> = split(instructions);
	return peephole1(l, true) + peephole1(r, true);
}

private INS peephole2(INS instructions, bool isSplit){
  //println("**** peephole length <size(instructions)>");
  
  // Peephole-ing a fixed point problem multiple steps for debugging.
  // -- Maybe disable could be slow --
    INS result = instructions ;
    int loopcount = 0 ;
    solve (result) {
        loopcount = loopcount + 1 ;
        result = dead_code(result);
        if(!isSplit){
        	result = unused_labels(result);
        }
        //result = redundant_stores(result);
        result = jumps_to_jumps(result);
        result = jumps_to_returns(result);
    }
    //println("**** peephole removed <size(instructions) - size(result)> instructions (from <size(instructions)>) in <loopcount> iterations");
    //println("BEFORE:");
    //iprintln(instructions);
    //println("AFTER:");
    //iprintln(result);
    return result;
}

// Jumps_to_jumps

INS replace_jump_targets(INS ins, str from, str to) =
     visit(ins){
           case JMP(from) => JMP(to)
           case JMPTRUE(from) => JMPTRUE(to)
           case JMPFALSE(from) => JMPFALSE(to)
           };

INS jumps_to_jumps([ *Instruction ins1, LABEL(lab1), JMP(str lab2), *Instruction ins2] ) =
    [*replace_jump_targets(ins1, lab1, lab2), LABEL(lab1), JMP(lab2), *replace_jump_targets(ins2, lab1, lab2)];

default INS jumps_to_jumps(INS ins) = ins;   

INS replace_jumps_by_returns(INS ins, str to, Instruction ret) =
     visit(ins){
           case JMP(to) => ret
           };
           
INS jumps_to_returns([ *Instruction ins1, LABEL(lab1), RETURN0(), *Instruction ins2] ) =
    [*replace_jumps_by_returns(ins1, lab1, RETURN0()), LABEL(lab1), RETURN0(), *replace_jumps_by_returns(ins2, lab1, RETURN0())];
    
INS jumps_to_returns([ *Instruction ins1, LABEL(lab1), RETURN1(), *Instruction ins2] ) =
    [*replace_jumps_by_returns(ins1, lab1, RETURN1()), LABEL(lab1), RETURN1(), *replace_jumps_by_returns(ins2, lab1, RETURN1())];

default INS jumps_to_returns(INS ins) = ins;   


INS unused_labels([ *Instruction instructions ]){
    used = {};
    
    visit(instructions){
       case JMP(lab): used += lab;
       case JMPFALSE(lab): used += lab;
       case JMPTRUE(lab): used += lab;
       case TYPESWITCH(labs): used += toSet(labs);
       case SWITCH(labs, def, useConcreteFingerprint): used += range(labs) + def;
    };
    return 
      for(ins <- instructions){
          if(LABEL(lab) := ins){
             if(lab in used || startsWith(lab, "TRY") || startsWith(lab, "CATCH") || startsWith(lab, "FINALLY"))
                append ins;
          } else {
            append ins;
          }
    }
}

INS dead_code([ *Instruction ins ] ) {
    result = [];
    i = 0;
    while(i < size(ins)){
       result += ins[i];
       if(   JMP(lab) := ins[i] 
          || RETURN0() := ins[i] 
          || RETURN1() := ins[i] 
          || FAILRETURN() := ins[i] 
          || EXHAUST() := ins[i]){
          i += 1;
          while(i < size(ins) && LABEL(lab1) !:= ins[i]){
            //println("remove: <ins[i]>");
            i += 1;
          }
       } else {
         i += 1;
       }
    }
    return result;
}
