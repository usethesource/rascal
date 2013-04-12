module lang::rascal::checker::TTL::generated::Statements
import lang::rascal::checker::TTL::Library;
import Type;
import IO;
import util::Eval;
import lang::rascal::types::TestChecker;

/* test  { x = 1; } expect {int x} */ 
test bool tst(){
  checkResult = checkStatementsString("x = 1;", importedModules=[], initialDecls = []);
  if(!(getTypeForName(checkResult.conf, "x") == (#int).symbol)) return false;
  return true;
}

/* test { x = 1; x = false; } expect {value x} */ 
test bool tst(){
  checkResult = checkStatementsString("x = 1; x = false;", importedModules=[], initialDecls = []);
  if(!(getTypeForName(checkResult.conf, "x") == (#value).symbol)) return false;
  return true;
}

/* test { use M1 :: x; } expect {int x} */ 
test bool tst(){
  checkResult = checkStatementsString("x;", importedModules=["lang::rascal::checker::TTL::generated::M1"], initialDecls = []);
  if(!(getTypeForName(checkResult.conf, "x") == (#int).symbol)) return false;
  return true;
}

/* test { use Y :: y; } expect {int y} */ 
test bool tst(){
  checkResult = checkStatementsString("y;", importedModules=[], initialDecls = ["int y = 1;"]);
  if(!(getTypeForName(checkResult.conf, "y") == (#int).symbol)) return false;
  return true;
}

/* test { int y = 1; x = y;} expect {int x} */ 
test bool tst(){
  checkResult = checkStatementsString("int y = 1; x = y;", importedModules=[], initialDecls = []);
  if(!(getTypeForName(checkResult.conf, "x") == (#int).symbol)) return false;
  return true;
}

/* test { use triple :: triple([1,2,3]); } expect {/cannot be called with argument/} */ 
test bool tstx(){
  checkResult = checkStatementsString("triple([1,2,3]);", importedModules=[], initialDecls = ["int triple(int x) = 3 * x;"]);
  
  for(msg <-  getFailureMessages(checkResult)){
        println("msg = <msg>");
        if(/cannot be called with argument/ := msg)
  	      return true;
  }
  return false;
  return true;
}

/* test { x = 3; x = 3.5; } expect{num x} */ 
test bool tst(){
  checkResult = checkStatementsString("x = 3; x = 3.5;", importedModules=[], initialDecls = []);
  if(!(getTypeForName(checkResult.conf, "x") == (#num).symbol)) return false;
  return true;
}

