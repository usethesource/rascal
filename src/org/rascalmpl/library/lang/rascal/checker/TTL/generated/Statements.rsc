module lang::rascal::checker::TTL::generated::Statements
import lang::rascal::checker::TTL::Library;
import Type;
import IO;
import util::Eval;
import lang::rascal::types::TestChecker;

/* test { int x = 1; } expect {int x} */ 
test bool tst42(){
  checkResult = checkStatementsString("int x = 1;", importedModules=[], initialDecls = []);
  if(getTypeForName(checkResult.conf, "x") != (#int).symbol) return false;
  return true;
}

/* test { x = 1; } expect {int x} */ 
test bool tst43(){
  checkResult = checkStatementsString("x = 1;", importedModules=[], initialDecls = []);
  if(getTypeForName(checkResult.conf, "x") != (#int).symbol) return false;
  return true;
}

/* test { x = 1; y = "a";} expect {int x, str y} */ 
test bool tst44(){
  checkResult = checkStatementsString("x = 1; y = \"a\";", importedModules=[], initialDecls = []);
  if(getTypeForName(checkResult.conf, "x") != (#int).symbol) return false;if(getTypeForName(checkResult.conf, "y") != (#str).symbol) return false;
  return true;
}

/* test { x = 1; x = false; } expect {value x} */ 
test bool tst45(){
  checkResult = checkStatementsString("x = 1; x = false;", importedModules=[], initialDecls = []);
  if(getTypeForName(checkResult.conf, "x") != (#value).symbol) return false;
  return true;
}

/* test { int x = 1; x = false; } expect {/Unable to bind subject type/} */ 
test bool tst46(){
  checkResult = checkStatementsString("int x = 1; x = false;", importedModules=[], initialDecls = []);
  
  for(msg <-  getFailureMessages(checkResult)){
        if(/Unable to bind subject type/ := msg)
  	      return true;
  }
  return false;
  return true;
}

/* test { use Y :: y; } expect {int y} */ 
test bool tst47(){
  checkResult = checkStatementsString("y;", importedModules=[], initialDecls = ["int y = 1;"]);
  if(getTypeForName(checkResult.conf, "y") != (#int).symbol) return false;
  return true;
}

/* test { int y = 1; x = y;} expect {int x, int y} */ 
test bool tst48(){
  checkResult = checkStatementsString("int y = 1; x = y;", importedModules=[], initialDecls = []);
  if(getTypeForName(checkResult.conf, "x") != (#int).symbol) return false;if(getTypeForName(checkResult.conf, "y") != (#int).symbol) return false;
  return true;
}

/* test { use triple :: triple([1,2,3]); } expect {/cannot be called with argument/} */ 
test bool tst49(){
  checkResult = checkStatementsString("triple([1,2,3]);", importedModules=[], initialDecls = ["int triple(int x) = 3 * x;"]);
  
  for(msg <-  getFailureMessages(checkResult)){
        if(/cannot be called with argument/ := msg)
  	      return true;
  }
  return false;
  return true;
}

/* test { x = 3; x = 3.5; } expect{num x} */ 
test bool tst50(){
  checkResult = checkStatementsString("x = 3; x = 3.5;", importedModules=[], initialDecls = []);
  if(getTypeForName(checkResult.conf, "x") != (#num).symbol) return false;
  return true;
}

/* test {int x = 3; y = x + "a";} expect{/Addition not defined/} */ 
test bool tst51(){
  checkResult = checkStatementsString("int x = 3; y = x + \"a\";", importedModules=[], initialDecls = []);
  
  for(msg <-  getFailureMessages(checkResult)){
        if(/Addition not defined/ := msg)
  	      return true;
  }
  return false;
  return true;
}

/* test {int x = 3; y = x - "a";} expect{/Subtraction not defined/} */ 
test bool tst52(){
  checkResult = checkStatementsString("int x = 3; y = x - \"a\";", importedModules=[], initialDecls = []);
  
  for(msg <-  getFailureMessages(checkResult)){
        if(/Subtraction not defined/ := msg)
  	      return true;
  }
  return false;
  return true;
}

/* test { use M1 :: x; } expect {int x} */ 
test bool tst53(){
  checkResult = checkStatementsString("x;", importedModules=["lang::rascal::checker::TTL::generated::M1"], initialDecls = []);
  if(getTypeForName(checkResult.conf, "x") != (#int).symbol) return false;
  return true;
}

