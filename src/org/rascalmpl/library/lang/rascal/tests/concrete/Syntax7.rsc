module lang::rascal::tests::concrete::Syntax7

import ParseTree;
import Exception;

syntax E = [a-zA-Z]+ \ 'if';

bool expectParseError(type[Tree] grammar, str input) {
   try {
     parse(grammar, input);
     return false;
   }
   catch ParseError(_):
     return true;
}

test bool caseInsensitiveKeyword1()
  = expectParseError(#E, "if");

test bool caseInsensitiveKeyword2()
  = expectParseError(#E, "iF");

test bool caseInsensitiveKeyword3()
  = expectParseError(#E, "If");

test bool caseInsensitiveKeyword4()
  = expectParseError(#E, "IF");

test bool caseInsensitiveKeyword5()
  = !expectParseError(#E, "Ig");

test bool caseInsensitiveKeyword6()
  = !expectParseError(#E, "aF");