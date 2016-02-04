module experiments::Compiler::Examples::Tst4

//import lang::rascal::\syntax::Rascal;
//import lang::rascal::grammar::definition::Literals;
//import ParseTree;

//value main() = unescape((StringConstant) `"a"`) == "a";

//test bool tstUnescapeSC2() = unescape((StringConstant) `"\\t"`) == "\t";
//test bool tstUnescapeSC3() = unescape((StringConstant) `"a\\tb"`) == "a\tb";
//test bool tstUnescapeSC4() = unescape((StringConstant) `"\\'"`) == "\'";
//test bool tstUnescapeSC5() = unescape((StringConstant) `"a\\tb\\'c"`) == "a\tb\'c";
//
//test bool tstUnescapeCI1() = unescape((CaseInsensitiveStringConstant) `'a'`) == "a";
//test bool tstUnescapeCI2() = unescape((CaseInsensitiveStringConstant) `'\\t'`) == "\t";
//test bool tstUnescapeCI3() = unescape((CaseInsensitiveStringConstant) `'a\\tb'`) == "a\tb";
//test bool tstUnescapeCI4() = unescape((CaseInsensitiveStringConstant) `'\\''`) == "\'";
//test bool tstUnescapeCI5() = unescape((CaseInsensitiveStringConstant) `'a\\tb\\'c'`) == "a\tb\'c";

//value main () = unescape("a") == "a";

public str unescape(str s) {
  return visit (s) {
    //case /\\b/ => "\b"
    //case /\\f/ => "\f"
    //case /\\n/ => "\n"
    case /\\t/ => "\t"
    //case /\\r/ => "\r"  
    //case /\\\"/ => "\""  
    //case /\\\'/ => "\'"
    //case /\\\\/ => "\\"
    //case /\\\</ => "\<"   
    //case /\\\>/ => "\>"    
  };      
}

//value main () = unescape("a") == "a";

value main () = unescape("\\t") == "\t";

//test bool tstUnescape3() = unescape("a\\tb") == "a\tb";
//test bool tstUnescape4() = unescape("\\\'") == "\'";
//test bool tstUnescape5() = unescape("a\\tb\\\'c") == "a\tb\'c";

