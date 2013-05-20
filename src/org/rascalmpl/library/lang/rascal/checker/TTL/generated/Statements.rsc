module lang::rascal::checker::TTL::generated::Statements
import lang::rascal::checker::TTL::Library;
import Type;
import IO;
import List;
import Set;
import Message;
import util::Eval;
import lang::rascal::types::AbstractName;
import lang::rascal::types::TestChecker;
import lang::rascal::checker::TTL::PatternGenerator;
public bool verbose = true;

/* test 1 variables { y = _V0; } expect {_T0 y} */ 
test bool Statements48(&T0 arg0){
  vtypes = [type(typeOf(arg0), ())]; 
  venv = ( );
  venv["0"] = < vtypes[0], arg0 >; 
  vsenv = ("T<id>" : vtypes[id].symbol | id <- index(vtypes) );
  stats = buildStatements("y = _V0;", venv);
  if(verbose) println("[Statements48] stats: " + stats);
  checkResult = checkStatementsString(stats, importedModules=[], initialDecls = []);
  if(!subtype(getTypeForName(checkResult.conf, "y"), typeOf(arg0))) return false;
  validatedMessages = {};
  unvalidatedMessages = getAllMessages(checkResult) - validatedMessages;
  if(size(unvalidatedMessages) > 0) { println("[Statements48] *** Unexpected messages: <unvalidatedMessages>"); return false;}
  return true;
}

/* test 1 variables { _T0 y = _V0; } expect {_T0 y} */ 
test bool Statements49(&T0 arg0){
  vtypes = [type(typeOf(arg0), ())]; 
  venv = ( );
  venv["0"] = < vtypes[0], arg0 >; 
  vsenv = ("T<id>" : vtypes[id].symbol | id <- index(vtypes) );
  stats = buildStatements("_T0 y = _V0;", venv);
  if(verbose) println("[Statements49] stats: " + stats);
  checkResult = checkStatementsString(stats, importedModules=[], initialDecls = []);
  if(!subtype(getTypeForName(checkResult.conf, "y"), typeOf(arg0))) return false;
  validatedMessages = {};
  unvalidatedMessages = getAllMessages(checkResult) - validatedMessages;
  if(size(unvalidatedMessages) > 0) { println("[Statements49] *** Unexpected messages: <unvalidatedMessages>"); return false;}
  return true;
}

/* test 1 variables { _T0 _X0 = _V0; y = _V0; } expect {_T0 y} */ 
test bool Statements50(&T0 arg0){
  vtypes = [type(typeOf(arg0), ())]; 
  venv = ( );
  venv["0"] = < vtypes[0], arg0 >; 
  vsenv = ("T<id>" : vtypes[id].symbol | id <- index(vtypes) );
  stats = buildStatements("_T0 _X0 = _V0; y = _V0;", venv);
  if(verbose) println("[Statements50] stats: " + stats);
  checkResult = checkStatementsString(stats, importedModules=[], initialDecls = []);
  if(!subtype(getTypeForName(checkResult.conf, "y"), typeOf(arg0))) return false;
  validatedMessages = {};
  unvalidatedMessages = getAllMessages(checkResult) - validatedMessages;
  if(size(unvalidatedMessages) > 0) { println("[Statements50] *** Unexpected messages: <unvalidatedMessages>"); return false;}
  return true;
}

/* test 1 variables { _T0 _X0 = _V0; y = _X0; } expect {_T0 y} */ 
test bool Statements51(&T0 arg0){
  vtypes = [type(typeOf(arg0), ())]; 
  venv = ( );
  venv["0"] = < vtypes[0], arg0 >; 
  vsenv = ("T<id>" : vtypes[id].symbol | id <- index(vtypes) );
  stats = buildStatements("_T0 _X0 = _V0; y = _X0;", venv);
  if(verbose) println("[Statements51] stats: " + stats);
  checkResult = checkStatementsString(stats, importedModules=[], initialDecls = []);
  if(!subtype(getTypeForName(checkResult.conf, "y"), typeOf(arg0))) return false;
  validatedMessages = {};
  unvalidatedMessages = getAllMessages(checkResult) - validatedMessages;
  if(size(unvalidatedMessages) > 0) { println("[Statements51] *** Unexpected messages: <unvalidatedMessages>"); return false;}
  return true;
}

/* test 1 variables {_T0 _X0 = _V0; _T0 y = _X0; } expect {_T0 y} */ 
test bool Statements52(&T0 arg0){
  vtypes = [type(typeOf(arg0), ())]; 
  venv = ( );
  venv["0"] = < vtypes[0], arg0 >; 
  vsenv = ("T<id>" : vtypes[id].symbol | id <- index(vtypes) );
  stats = buildStatements("_T0 _X0 = _V0; _T0 y = _X0;", venv);
  if(verbose) println("[Statements52] stats: " + stats);
  checkResult = checkStatementsString(stats, importedModules=[], initialDecls = []);
  if(!subtype(getTypeForName(checkResult.conf, "y"), typeOf(arg0))) return false;
  validatedMessages = {};
  unvalidatedMessages = getAllMessages(checkResult) - validatedMessages;
  if(size(unvalidatedMessages) > 0) { println("[Statements52] *** Unexpected messages: <unvalidatedMessages>"); return false;}
  return true;
}

/* test 2 variables {_T0 _X0 = _V0; _T1 _X1 = _V1;} expect {_T0 _X0, _T1 _X1} */ 
test bool Statements53(&T0 arg0, &T1 arg1){
  vtypes = [type(typeOf(arg0), ()), type(typeOf(arg1), ())]; 
  venv = ( );
  venv["0"] = < vtypes[0], arg0 >; venv["1"] = < vtypes[1], arg1 >; 
  vsenv = ("T<id>" : vtypes[id].symbol | id <- index(vtypes) );
  stats = buildStatements("_T0 _X0 = _V0; _T1 _X1 = _V1;", venv);
  if(verbose) println("[Statements53] stats: " + stats);
  checkResult = checkStatementsString(stats, importedModules=[], initialDecls = []);
  if(!subtype(getTypeForName(checkResult.conf, "X0"), typeOf(arg0))) return false;if(!subtype(getTypeForName(checkResult.conf, "X1"), typeOf(arg1))) return false;
  validatedMessages = {};
  unvalidatedMessages = getAllMessages(checkResult) - validatedMessages;
  if(size(unvalidatedMessages) > 0) { println("[Statements53] *** Unexpected messages: <unvalidatedMessages>"); return false;}
  return true;
}

/* test 2 variables {_T0 x = _V0; x = _V1; } expect{LUB(_T0,_T1) x} */ 
test bool Statements54(&T0 arg0, &T1 arg1){
  vtypes = [type(typeOf(arg0), ()), type(typeOf(arg1), ())]; 
  venv = ( );
  venv["0"] = < vtypes[0], arg0 >; venv["1"] = < vtypes[1], arg1 >; 
  vsenv = ("T<id>" : vtypes[id].symbol | id <- index(vtypes) );
  stats = buildStatements("_T0 x = _V0; x = _V1;", venv);
  if(verbose) println("[Statements54] stats: " + stats);
  checkResult = checkStatementsString(stats, importedModules=[], initialDecls = []);
  if(!subtype(getTypeForName(checkResult.conf, "x"), normalize(lub(#&T0.symbol,#&T1.symbol), vsenv))) return false;
  validatedMessages = {};
  unvalidatedMessages = getAllMessages(checkResult) - validatedMessages;
  if(size(unvalidatedMessages) > 0) { println("[Statements54] *** Unexpected messages: <unvalidatedMessages>"); return false;}
  return true;
}

/* test 2 variables {x = _V0; x = _V1; } expect{LUB(_T0,_T1) x} */ 
test bool Statements55(&T0 arg0, &T1 arg1){
  vtypes = [type(typeOf(arg0), ()), type(typeOf(arg1), ())]; 
  venv = ( );
  venv["0"] = < vtypes[0], arg0 >; venv["1"] = < vtypes[1], arg1 >; 
  vsenv = ("T<id>" : vtypes[id].symbol | id <- index(vtypes) );
  stats = buildStatements("x = _V0; x = _V1;", venv);
  if(verbose) println("[Statements55] stats: " + stats);
  checkResult = checkStatementsString(stats, importedModules=[], initialDecls = []);
  if(!subtype(getTypeForName(checkResult.conf, "x"), normalize(lub(#&T0.symbol,#&T1.symbol), vsenv))) return false;
  validatedMessages = {};
  unvalidatedMessages = getAllMessages(checkResult) - validatedMessages;
  if(size(unvalidatedMessages) > 0) { println("[Statements55] *** Unexpected messages: <unvalidatedMessages>"); return false;}
  return true;
}

/* test 1 variables {_T0 _X0 = _V0; if(true) y = _X0; else y = _V0; } expect {_T0 y } */ 
test bool Statements56(&T0 arg0){
  vtypes = [type(typeOf(arg0), ())]; 
  venv = ( );
  venv["0"] = < vtypes[0], arg0 >; 
  vsenv = ("T<id>" : vtypes[id].symbol | id <- index(vtypes) );
  stats = buildStatements("_T0 _X0 = _V0; if(true) y = _X0; else y = _V0;", venv);
  if(verbose) println("[Statements56] stats: " + stats);
  checkResult = checkStatementsString(stats, importedModules=[], initialDecls = []);
  if(!subtype(getTypeForName(checkResult.conf, "y"), typeOf(arg0))) return false;
  validatedMessages = {};
  unvalidatedMessages = getAllMessages(checkResult) - validatedMessages;
  if(size(unvalidatedMessages) > 0) { println("[Statements56] *** Unexpected messages: <unvalidatedMessages>"); return false;}
  return true;
}

/* test 0 variables { int x = 1; x = false; } expect {/Unable to bind subject type/} */ 
test bool Statements57(){
  vtypes = []; 
  venv = ( );
  
  vsenv = ("T<id>" : vtypes[id].symbol | id <- index(vtypes) );
  stats = buildStatements("int x = 1; x = false;", venv);
  if(verbose) println("[Statements57] stats: " + stats);
  checkResult = checkStatementsString(stats, importedModules=[], initialDecls = []);
  
  validatedMessages = {};
  for(m <-  getFailureMessages(checkResult)){
        if(/Unable to bind subject type/ := m.msg)
  	        validatedMessages += m;
  }
  for(m <-  getWarningMessages(checkResult)){
        if(/Unable to bind subject type/ := m.msg)
  	        validatedMessages += m;
  }
  unvalidatedMessages = getAllMessages(checkResult) - validatedMessages;
  if(size(unvalidatedMessages) > 0) { println("[Statements57] *** Unexpected messages: <unvalidatedMessages>"); return false;}
  return true;
}

/* test 0 variables { use Y :: y; } expect {int y} */ 
test bool Statements58(){
  vtypes = []; 
  venv = ( );
  
  vsenv = ("T<id>" : vtypes[id].symbol | id <- index(vtypes) );
  stats = buildStatements("y;", venv);
  if(verbose) println("[Statements58] stats: " + stats);
  checkResult = checkStatementsString(stats, importedModules=[], initialDecls = ["int y = 1;"]);
  if(!subtype(getTypeForName(checkResult.conf, "y"), normalize(#int.symbol, vsenv))) return false;
  validatedMessages = {};
  unvalidatedMessages = getAllMessages(checkResult) - validatedMessages;
  if(size(unvalidatedMessages) > 0) { println("[Statements58] *** Unexpected messages: <unvalidatedMessages>"); return false;}
  return true;
}

/* test 0 variables { use triple :: triple([1,2,3]); } expect {/cannot be called with argument/} */ 
test bool Statements59(){
  vtypes = []; 
  venv = ( );
  
  vsenv = ("T<id>" : vtypes[id].symbol | id <- index(vtypes) );
  stats = buildStatements("triple([1,2,3]);", venv);
  if(verbose) println("[Statements59] stats: " + stats);
  checkResult = checkStatementsString(stats, importedModules=[], initialDecls = ["int triple(int x) = 3 * x;"]);
  
  validatedMessages = {};
  for(m <-  getFailureMessages(checkResult)){
        if(/cannot be called with argument/ := m.msg)
  	        validatedMessages += m;
  }
  for(m <-  getWarningMessages(checkResult)){
        if(/cannot be called with argument/ := m.msg)
  	        validatedMessages += m;
  }
  unvalidatedMessages = getAllMessages(checkResult) - validatedMessages;
  if(size(unvalidatedMessages) > 0) { println("[Statements59] *** Unexpected messages: <unvalidatedMessages>"); return false;}
  return true;
}

/* test 0 variables {int x = 3; y = x + "a";} expect{/Addition not defined/} */ 
test bool Statements60(){
  vtypes = []; 
  venv = ( );
  
  vsenv = ("T<id>" : vtypes[id].symbol | id <- index(vtypes) );
  stats = buildStatements("int x = 3; y = x + \"a\";", venv);
  if(verbose) println("[Statements60] stats: " + stats);
  checkResult = checkStatementsString(stats, importedModules=[], initialDecls = []);
  
  validatedMessages = {};
  for(m <-  getFailureMessages(checkResult)){
        if(/Addition not defined/ := m.msg)
  	        validatedMessages += m;
  }
  for(m <-  getWarningMessages(checkResult)){
        if(/Addition not defined/ := m.msg)
  	        validatedMessages += m;
  }
  unvalidatedMessages = getAllMessages(checkResult) - validatedMessages;
  if(size(unvalidatedMessages) > 0) { println("[Statements60] *** Unexpected messages: <unvalidatedMessages>"); return false;}
  return true;
}

/* test 0 variables {int x = 3; y = x - "a";} expect{/Subtraction not defined/} */ 
test bool Statements61(){
  vtypes = []; 
  venv = ( );
  
  vsenv = ("T<id>" : vtypes[id].symbol | id <- index(vtypes) );
  stats = buildStatements("int x = 3; y = x - \"a\";", venv);
  if(verbose) println("[Statements61] stats: " + stats);
  checkResult = checkStatementsString(stats, importedModules=[], initialDecls = []);
  
  validatedMessages = {};
  for(m <-  getFailureMessages(checkResult)){
        if(/Subtraction not defined/ := m.msg)
  	        validatedMessages += m;
  }
  for(m <-  getWarningMessages(checkResult)){
        if(/Subtraction not defined/ := m.msg)
  	        validatedMessages += m;
  }
  unvalidatedMessages = getAllMessages(checkResult) - validatedMessages;
  if(size(unvalidatedMessages) > 0) { println("[Statements61] *** Unexpected messages: <unvalidatedMessages>"); return false;}
  return true;
}

/* test 0 variables { use M1 :: x; } expect {int x} */ 
test bool Statements62(){
  vtypes = []; 
  venv = ( );
  
  vsenv = ("T<id>" : vtypes[id].symbol | id <- index(vtypes) );
  stats = buildStatements("x;", venv);
  if(verbose) println("[Statements62] stats: " + stats);
  checkResult = checkStatementsString(stats, importedModules=["lang::rascal::checker::TTL::generated::M1"], initialDecls = []);
  if(!subtype(getTypeForName(checkResult.conf, "x"), normalize(#int.symbol, vsenv))) return false;
  validatedMessages = {};
  unvalidatedMessages = getAllMessages(checkResult) - validatedMessages;
  if(size(unvalidatedMessages) > 0) { println("[Statements62] *** Unexpected messages: <unvalidatedMessages>"); return false;}
  return true;
}

