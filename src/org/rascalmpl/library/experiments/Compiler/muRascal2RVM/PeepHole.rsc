module experiments::Compiler::muRascal2RVM::PeepHole

import Prelude;

import experiments::Compiler::RVM::AST;

alias INS = list[Instruction];

int n_redundant_stores = 0;
int n_jumps_to_jumps = 0;

INS peephole(INS instructions){
  return instructions;							// Not (yet) used, due to lack of impact.
  result1 = redundant_stores(instructions);
  result2 = jumps_to_jumps(result1);
  result3 = unused_labels(result2);
  result4 = dead_code(result3);
  println("**** peephole removed <size(instructions) - size(result4)> instructions");
  iprintln(instructions);
  iprintln(result4);
  return result4;
}

// Redundant_stores

INS redundant_stores([ *Instruction ins1, STOREVAR(v), POP(), LOADVAR(v),  *Instruction ins2] ) {
    n_redundant_stores += 1;
    return redundant_stores([*ins1, STOREVAR(v), *ins2]);
    
}

INS redundant_stores([ *Instruction ins1, STORELOC(v), POP(), LOADLOC(v),  *Instruction ins2] ) {
    n_redundant_stores += 1;
    return redundant_stores([*ins1, STORELOC(v), *ins2]);
}

default INS redundant_stores(INS ins) = ins;

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

INS unused_labels([ *Instruction instructions ]){
    used = {};
    
    visit(instructions){
       case JMP(lab): used += lab;
       case JMPFALSE(lab): used += lab;
       case JMPTRUE(lab): used += lab;
       case TYPESWITCH(labs): used += toSet(labs);
       case JMPINDEXED(labs): used += toSet(labs);
    };
    return 
      for(ins <- instructions){
          if(LABEL(lab) := ins){
             if(lab in used || startsWith(lab, "TRY") || startsWith(lab, "FINALLY"))
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
       if(JMP(lab) := ins[i] || RETURN0() := ins[i] || RETURN1() := ins[i], FAILRETURN() := ins[i]){
          i += 1;
          while(i < size(ins) &&  LABEL(lab1) !:= ins[i]){
            println("remove: <ins[i]>");
            i += 1;
          }
       } else {
         i += 1;
       }
    }
    return result;
}
   


