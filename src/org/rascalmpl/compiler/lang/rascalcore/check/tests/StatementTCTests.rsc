@bootstrapParser
module lang::rascalcore::check::tests::StatementTCTests
/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
*******************************************************************************/

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool assertOK1() = checkOK("void main(){ assert true; }");

test bool assertError2() = unexpectedType("void main(){ assert 1; } ");

test bool assertOK2() = checkOK("void main(){ assert true: \"msg\" ; }");

test bool assertError3() = unexpectedType("void main(){ assert 1: \"msg\" ; }");

test bool assertError4() = unexpectedType("void main(){ assert true: 5 ; }");

test bool assertError5() = unexpectedType("assert 3.5;");

test bool assertError6() = unexpectedType("assert 3.5: \"Wrong expression type\";");

test bool assertError7() = undeclaredVariable("assert X;");
 
test bool assertError8() = undeclaredVariable("assert X: \"Wrong expression type\";");


  	
test bool ifThenError1() = unexpectedType("if(3){n = 4;};");

test bool ifThenElseError1() = unexpectedType("if(\"abc\") {n = 4;} else {n=5;}");
  
test bool solveError1() = unexpectedType("rel[int,int] R1 = {\<1,2\>, \<2,3\>, \<3,4\>}; rel[int,int] T = R1; solve (T; true)  T = T + (T o R1);");

test bool doWhileError1() = unexpectedType("do {n = 4;} while(3);");

test bool whileError1() = unexpectedType("while(3){n = 4;}");	


// test bool Visit1 () = checkOK("void main() { visit(1) { case 1 => 2 } } ");

// test bool Visit2 () = checkOK("void main() { visit(1) { case 1 => "a" } } ");
// expect { "A pattern of type `int` cannot be replaced by `str`" }

// test bool Visit3 () = checkOK("void main() { visit(1) { case 1: insert 2; } } ");

// test bool Visit4 () = checkOK("void main() { visit(1) { case 1: insert "a"; } } ");
// expect { "Insert type should be subtype of `int`, found `str`" }

// test bool Visit5 () = checkOK("void main() { visit(1) { case int x: insert "a"; } } ");
// expect { "Insert type should be subtype of `int`, found `str`" }

// test bool Visit6 () = checkOK("void main() { visit(1) { case int x: insert 1; }; x; } ");
// expect { "Undefined variable, function, field or constructor `x`" }

// test bool Visit7 () = checkOK("void main() { insert 2; } ");
// expect { "Insert found outside replacement context" }

// test bool IfT1() = checkOK("void main(){ if(true) 1; }");
// test bool IfT2() = checkOK("void main(){ if(true,true) 1; }");
// test bool IfT3() = checkOK("void main(){ if(true,"a") 1; }");
// expect { "Condition should be `bool`, found `str`" }

// test bool IfTE1() = checkOK("void main(){ if(true) 1; else 2;}");
// test bool IfTE2() = checkOK("void main(){ if(true,true) 1; else 2; }");
// test bool IfTE3() = checkOK("void main(){ if(true,"a") 1; else 2;}");
// expect { "Condition should be `bool`, found `str`" }

// test bool While1() = checkOK("void main(){ while(true) 1; }");
// test bool While2() = checkOK("void main(){ while(13) 1; }");
// expect { "Condition should be `bool`, found `int`" }

// test bool Do1() = checkOK("void main(){ do 1; while(true); }");
// test bool Do2() = checkOK("void main(){ do 1; while(13); }");
// expect { "Condition should be `bool`, found `int`" }

// test bool For1() = checkOK("void main(){ for(true) 1; }");
// test bool For1() = checkOK("void main(){ for(13) 1; }");
// expect { "Condition should be `bool`, found `int`" }
  	
