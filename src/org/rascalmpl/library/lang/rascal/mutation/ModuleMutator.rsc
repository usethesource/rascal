@bootstrapParser
@doc{
Synopsis: Mutant Generator for Rascal modules

Description:  This module contains tools to automatically generate broken mutations from a working 
  Rascal module. The use case is to test how good the tests are for such a module. The tests
  should be able to find the bugs we introduce using the mutators.
  
Examples

<listing>
import lang::rascal::mutation::ModuleMutator;
mutate(|project://rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|, mutations=5)
</listing>  
}
module lang::rascal::mutation::ModuleMutator

import lang::rascal::\syntax::Rascal;
import List;
import util::Math;
import ParseTree;
import IO;

list[str] mutate(loc input, int mutations = 5, real chance = 0.1, str folder="mutants", str prefix="") {
  m = parse(#start[Module], input).top;
  list[str] modList = [];
  str muModName;
  for (new <- mutate(m, mutations=mutations, chance=chance)) {
    muModName = "<prefix><getModuleName(new)>.rsc";
    writeFile(input.parent + folder + muModName, "<new>");
    modList += muModName;
  }
  return modList;
}

set[Module] mutate(Module input, int mutations = 5, real chance = 0.1) 
 = {new | int c <- [0..mutations], new := make(input, c, chance=chance), new != input};
 
Module make(Module input, int c, real chance = 0.1)
 = rename(c, top-down-break visit(input) {
     // do not mutate inside test definitions:
     case FunctionDeclaration d => d 
       when \test() <- d.signature.modifiers.modifiers 
     
     // removals in switch and visits   
     case (PatternWithAction) `<Pattern p> =\> <Replacement _>` => 
          (PatternWithAction) `<Pattern p> : throw "mutant! Remove =\>";`
       when arbInt(10) == 0, !(p is concrete)   
          
     case (PatternWithAction) `<Pattern p> : <Statement _>` => 
          (PatternWithAction) `<Pattern p> : throw "mutant! Remove :";`
       when arbInt(10) == 0, !(p is concrete)
       
     // removals when conditionals use pattern matching:  
     case (Statement) `if (<{Expression ","}+ cond>) <Statement s>` =>
          (Statement) `if (<{Expression ","}+ cond>) 
                      '  throw "mutant! Remove if conditionals case 1";`
       when  arbInt(10) == 0, /Pattern p := cond, !(p is concrete)
       
     case (Statement) `if (<{Expression ","}+ cond1>) <Statement s> else <Statement t>` =>
          (Statement) `if (<{Expression ","}+ cond1>) throw "mutant! Remove if conditionals case 2"; else <Statement t>`
       when  arbInt(10) == 0, /Pattern p := cond1, !(p is concrete)
       
     case (Statement) `if (<{Expression ","}+ cond2>) <Statement s> else <Statement t>` =>
          (Statement) `if (<{Expression ","}+ cond2>) <Statement s> else throw "mutant! Remove if conditionals case 3";`
       when  arbInt(10) == 0, /Pattern p := cond2, !(p is concrete) 
       
     case (Statement) `while (<{Expression ","}+ cond3>) <Statement s>` =>
          (Statement) `while (<{Expression ","}+ cond3>) throw "mutant! Remove while conditionals case 1";`
       when  arbInt(10) == 0, /Pattern p := cond3, !(p is concrete)
       
     case (Statement) `for (<{Expression ","}+ cond4>) <Statement s>` =>
          (Statement) `for (<{Expression ","}+ cond4>) throw "mutant! Remove for conditionals case 1";`
       when  arbInt(10) == 0, /Pattern p := cond4, !(p is concrete)             
 });
 
test bool renameTest1() = rename(20, (Module) `module a::b::X`) == (Module) `module a::b::X20`;
test bool renameTest2() = rename(20, (Module) `module X`) == (Module) `module X20`;

test bool renameTest3() = rename(20, "mutants", (Module) `module a::b::X`) == (Module) `module a::b::mutants::X20`;
test bool renameTest4() = rename(20, "mutants", (Module) `module X`) == (Module) `module mutants::X20`;
 
Module rename(int c, (Module) `<Tags t> module <{Name "::"}+ p> :: <Name last> <Import* i> <Body b>`) =
(Module) `<Tags t> 
            'module <{Name "::"}+ p>::<Name newName>
            '<Import* i>
            '<Body b>`
   when Name newName := [Name] "<last><c>";

Module rename(int c, (Module) `<Tags t> module <Name last> <Import* i> <Body b>`) = 
   (Module) `<Tags t> 
            'module <Name newName>
            '<Import* i>
            '<Body b>`
   when Name newName := [Name] "<last><c>";
   
Module rename(int c, str folder, (Module) `<Tags t> module <{Name "::"}+ p> :: <Name last> <Import* i> <Body b>`) = 
   (Module) `<Tags t> 
            'module <{Name "::"}+ p>::<Name f>::<Name newName>
            '<Import* i>
            '<Body b>`
   when Name newName := [Name] "<last><c>" && Name f := [Name] "<folder>";
   
Module rename(int c, str folder, (Module) `<Tags t> module <Name last> <Import* i> <Body b>`) = 
   (Module) `<Tags t> 
            'module <Name f>::<Name newName>
            '<Import* i>
            '<Body b>`
   when Name newName := [Name] "<last><c>" && Name f := [Name] "<folder>";
   
default str rename(Module x) { throw "can not rename name of <x.header>"; } 
  
str getModuleName((Module) `<Tags _> module <{Name "::"}+ _> :: <Name last> <Import* _> <Body _>`) = "<last>";
str getModuleName((Module) `<Tags _> module <Name last> <Import* _> <Body _>`) = "<last>"; 
default str getModuleName(Module x) { throw "can not find name of <x.header>"; }