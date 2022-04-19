module lang::rascal::tests::basic::FunctionAssignment

import String;

public int i = 0;

test bool triggerAssignmentBug() {
    str(str) stealTheFunction = toUpperCase;
    
    // by now the interpreter would fail with an exception: 
    //   Undeclared variable: toUpperCase
    return toUpperCase("abc") == "ABC";
}