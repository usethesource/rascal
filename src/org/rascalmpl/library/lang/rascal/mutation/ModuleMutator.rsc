@bootstrapParser
@doc{
Synopsis: Mutant Generator for Rascal modules

Description:  This module contains tools to automatically generate broken mutations from a working 
  Rascal module. The use case is to test how good the tests are for such a module. The tests
  should be able to find the bugs we introduce using the mutators.
  
Examples

<listing>
import lang::rascal::mutation::ModuleMutator;
mutate(|project://rascal/src/org/rascalmpl/library/lang/rascal/\format/Grammar.rsc|, mutations=5)
</listing>  
}
module lang::rascal::mutation::ModuleMutator

import lang::rascal::\syntax::Rascal;
import List;
import util::Math;
import ParseTree;
import IO;
import String;

list[str] mutate(loc input, int mutations = 5, real chance = 0.1, str folder="mutants", str prefix="") {
  m = parse(#start[Module], input).top;
  list[str] modList = [];
  str muModName;
  for (new <- mutate(m, mutations=mutations, chance=chance, prefix=prefix, parentMod=folder)) {
    muModName = "<getModuleName(new)>.rsc";
    writeFile(input.parent + folder + muModName, "<new>");
    modList += muModName;
  }
  return modList;
}

list[Module] mutate(Module input, int mutations = 5, real chance = 0.1, str prefix="", str parentMod="") {
  list[Module] ret = [];
  
  for(opId <- muOpers) {
    ret = ret + make(input, opId, chance=chance, prefix=prefix,parentMod=parentMod);
    if (size(ret) >= mutations) break;
  }
  return ret;
}

private list[int] muOpers = [0..6];

tuple[bool,PatternWithAction] muOperPatt(0, (PatternWithAction) `<Pattern p> =\> <Replacement _>`) = <true,
(PatternWithAction) `<Pattern p> : throw "mutant! OP0: Remove pattern rewrite.";`>
when !(p is concrete);

tuple[bool,PatternWithAction] muOperPatt(1, (PatternWithAction) `<Pattern p> : <Statement _>`) = <true,
(PatternWithAction) `<Pattern p> : throw "mutant! OP1: Remove pattern with action.";`>
when !(p is concrete);

default tuple[bool,PatternWithAction] muOperPatt(int opId, PatternWithAction pa) = <false,pa>;

tuple[bool,Statement] muOperStm(2, (Statement) `if (<{Expression ","}+ cond>) <Statement s>`) = <true,
(Statement) `if (<{Expression ","}+ cond>) 
            '  throw "mutant! OP2: Remove if conditionals. Case 1.";`>
when !(cond is concrete);

tuple[bool,Statement] muOperStm(3, (Statement) `if (<{Expression ","}+ cond>) <Statement s> else <Statement t>`) = <true,
(Statement) `if (<{Expression ","}+ cond>) throw "mutant! OP3: Remove if conditionals. Case 2."; else <Statement t>` >
when !(cond is concrete);

tuple[bool,Statement] muOperStm(4, (Statement) `if (<{Expression ","}+ cond>) <Statement s> else <Statement t>`) = <true,
(Statement) `if (<{Expression ","}+ cond>) <Statement s> else throw "mutant! OP4: Remove if conditionals. Case 3.";` >
when !(cond is concrete);

tuple[bool,Statement] muOperStm(5, (Statement) `while (<{Expression ","}+ cond>) <Statement s>`) = <true,
(Statement) `while (<{Expression ","}+ cond>) throw "mutant! OP5: Remove while conditionals.";` >
when !(cond is concrete);

tuple[bool,Statement] muOperStm(6, (Statement) `for (<{Expression ","}+ cond>) <Statement s>`) = <true,
(Statement) `for (<{Expression ","}+ cond>) throw "mutant! OP6: Remove for conditionals.";` >
when !(cond is concrete);

default tuple[bool,Statement] muOperStm(int opId, Statement stm) = <false,stm>;
        
list[Module] mutationOp(int opId, Module input) {
  if (opId > muOpers[-1]) throw "mutationOp: Invalid mutation operator!";
  
  list[Module] lMMod = [];
  
  Module m;
  bool runOp = false;
  int curMuOperPoint;
  int curMuOper = 1;
  
  while (!runOp) {
    runOp = true;
    curMuOperPoint = 0;
    
    m = top-down visit(input) {
      // do not mutate inside test definitions:
       case FunctionDeclaration d => d 
         when runOp, \test() <- d.signature.modifiers.modifiers
         
       case PatternWithAction pa: {
         if (runOp) {
           tuple[bool r,PatternWithAction s] ret = muOperPatt(opId, pa);
           if (ret.r) {
             curMuOperPoint += 1;
             if (curMuOper == curMuOperPoint) {
               runOp = false;
               insert ret.s;
             }
           }
         }
       }
       
       case Statement stm: {
         if (!(stm is \visit) && runOp) {
           tuple[bool r, Statement s] ret = muOperStm(opId, stm);
           if (ret.r) {
             curMuOperPoint += 1;
             
             if (curMuOper == curMuOperPoint) {
               runOp = false;
               insert ret.s;
             }
           }
         }
       }
    };
    
    if (!runOp) lMMod += m;
    
    curMuOper += 1;
  }
  return lMMod;
}
  
list[Module] make(Module input, int opId, real chance = 0.1, str prefix="", str parentMod="") {
  list[Module] lMMod = [];
  
  if (opId > muOpers[-1]) throw "make: Invalid mutation operator!";
  
  lMMod = mutationOp(opId,input);
  
  if (size(lMMod) == 0) return [];
  
  int bIndex = opId * 1000;
  lMMod = [rename(bIndex+mIdx, prefix, parentMod, lMMod[mIdx]) | mIdx <- [0..size(lMMod)]];
  return lMMod;
}
 
Module rename(int c, str prefix, "", (Module) `<Tags t> module <{Name "::"}+ p> :: <Name last> <Import* i> <Body b>`) =
(Module) `<Tags t> 
            'module <{Name "::"}+ p>::<Name newName>
            '
            '<Import* i>
            '
            '<Body b>`
   when Name newName := [Name] "<prefix><last><c>";
   
Module rename(int c, str prefix, str parentMod, (Module) `<Tags t> module <{Name "::"}+ p> :: <Name last> <Import* i> <Body b>`) =
   (Module) `<Tags t> 
            'module <{Name "::"}+ p>::<Name f>::<Name newName> 
            '
            '<Import* i>
            '
            '<Body b>`
   when Name newName := [Name] "<prefix><last><c>" && Name f := [Name] "<parentMod>";

Module rename(int c, str prefix, "", (Module) `<Tags t> module <Name last> <Import* i> <Body b>`) = 
   (Module) `<Tags t> 
            'module <Name newName>
            '
            '<Import* i>
            '
            '<Body b>`
   when Name newName := [Name] "<prefix><last><c>";

Module rename(int c, str prefix, str parentMod, (Module) `<Tags t> module <Name last> <Import* i> <Body b>`) = 
   (Module) `<Tags t>
            'module <Name f>::<Name newName>
            '
            '<Import* i>
            '
            '<Body b>`
   when Name newName := [Name] "<prefix><last><c>" && Name f := [Name] "<parentMod>";
   
default str rename(Module x) { throw "can not rename name of <x.header>"; } 
  
str getModuleName((Module) `<Tags _> module <{Name "::"}+ _> :: <Name last> <Import* _> <Body _>`) = "<last>";
str getModuleName((Module) `<Tags _> module <Name last> <Import* _> <Body _>`) = "<last>"; 
default str getModuleName(Module x) { throw "can not find name of <x.header>"; }

test bool renameTest1() = rename(20, "", "", (Module) `module a::b::X`) == (Module) `module a::b::X20`;
test bool renameTest2() = rename(20, "", "", (Module) `module X`) == (Module) `module X20`;

test bool renameTest3() = rename(20, "", "mutants", (Module) `module a::b::X`) == (Module) `module a::b::mutants::X20`;
test bool renameTest4() = rename(20, "", "mutants", (Module) `module X`) == (Module) `module mutants::X20`;