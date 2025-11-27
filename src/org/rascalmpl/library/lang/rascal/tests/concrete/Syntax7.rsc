module lang::rascal::tests::concrete::Syntax7

import ParseTree;
import Exception;


syntax A = ":" >> 'if' [a-zA-z]+ ;
syntax B = ":" !>> 'if' [a-zA-z]+ ;
syntax C = [a-zA-Z]+ 'if' << ":";
syntax D = [a-zA-Z]+ 'if' !<< ":";
syntax E = [a-zA-Z]+ \ 'if';

lexical LOOPKW = WHILE || DO;
lexical WHILE = 'while';
lexical DO = "do";
lexical BEGINEND = BEGIN || END;
lexical BEGIN = 'begin';
lexical END = "END";

keyword KW = LOOPKW || BEGINEND;

bool expectParseError(type[Tree] grammar, str input) {
   try {
     parse(grammar, input);
     return false;
   }
   catch ParseError(_):
     return true;
}

test bool caseInsensitiveFollowRequirement1()
  = expectParseError(#A, ":jg");

test bool caseInsensitiveFollowRequirement2()
  = !expectParseError(#A, ":if");

test bool caseInsensitiveFollowRequirement3()
  = !expectParseError(#A, ":iF");  

test bool caseInsensitiveFollowRequirement4()
  = !expectParseError(#A, ":iF");

test bool caseInsensitiveFollowRequirement5()
  = !expectParseError(#A, ":IF");

test bool caseInsensitiveFollowRestriction()
  = !expectParseError(#B, ":jg");

test bool caseInsensitiveFollowRestriction2()
  = expectParseError(#B, ":if");

test bool caseInsensitiveFollowRestriction3()
  = expectParseError(#B, ":iF");  

test bool caseInsensitiveFollowRestriction4()
  = expectParseError(#B, ":iF");

test bool caseInsensitiveFollowRestriction5()
  = expectParseError(#B, ":IF");

test bool caseInsensitivePrecedeRequirement1()
  = expectParseError(#C, "jg:");

test bool caseInsensitivePrecedeRequirement2()
  = !expectParseError(#C, "if:");

test bool caseInsensitivePrecedeRequirement3()
  = !expectParseError(#C, "iF:");  

test bool caseInsensitivePrecedeRequirement4()
  = !expectParseError(#C, "iF:");

test bool caseInsensitivePrecedeRequirement5()
  = !expectParseError(#C, "IF:");

test bool caseInsensitivePrecedeRestriction()
  = !expectParseError(#D, "jg:");

test bool caseInsensitivePrecedeRestriction2()
  = expectParseError(#D, "if:");

test bool caseInsensitivePrecedeRestriction3()
  = expectParseError(#D, "iF:");  

test bool caseInsensitivePrecedeRestriction4()
  = expectParseError(#D, "iF:");

test bool caseInsensitivePrecedeRestriction5()
  = expectParseError(#D, "IF:");     

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
