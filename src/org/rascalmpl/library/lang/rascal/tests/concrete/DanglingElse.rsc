module lang::rascal::tests::concrete::DanglingElse

import ParseTree;

layout Layout = [\ \t\n]* !>> [\ \t\n];

start syntax Stmt
    = "if" "(" "x" ")" Stmt "else" Stmt
    | "if" "(" "x" ")" Stmt () !>> "else"
    | "{" "}"
    ;

bool expectSuccess(str src) {
  try {
  	parse(#Stmt, src);
  	return true;
  }
  catch value _: {
    return false;
  }
}

test bool noElse1() = expectSuccess("if (x) {}");

test bool noElse2() = expectSuccess("if (x) if (x) {}");

test bool noElse3() = expectSuccess("if (x) if (x) if (x) {}");

test bool noElse1Trailing() = expectSuccess("if (x) {}" );

test bool noElse2Trailing() = expectSuccess("if (x) if (x) {} ");

test bool noElse3Trailing() = expectSuccess("if (x) if (x) if (x) {} ");

test bool ifElse1() = expectSuccess("if (x) {} else {}" );

test bool ifElse2() = expectSuccess("if (x) if (x) {} else {}");

test bool ifElse3() = expectSuccess("if (x) if (x) if (x) {} else {}");

test bool ifElseElse1() = expectSuccess("if (x) {} else {}" );

test bool ifElseElse2() = expectSuccess("if (x) if (x) {} else {} else {}");

test bool ifElseElse3() = expectSuccess("if (x) {} else if (x) {} else if (x) {} else {}");
