module rascal::conversion::grammar::Grammar2Rascal

// Convert the Rascal internal grammar representation format (Grammar) to 
// a syntax definition in Rascal source code

// TODO:
// - done!

// Commands to test conversion of Pico or Rascal grammar:
// println(grammar2rascal(readTextValueFile(#Grammar, |stdlib:///org/rascalmpl/library/rascal/conversion/grammar/Pico.grammar|)));
// - Manually paste output to /org/rascalmpl/library/rascal/conversion/grammar/Pico.rsc

// println(grammar2rascal(readTextValueFile(#Grammar, |stdlib:///org/rascalmpl/library/rascal/conversion/grammar/Rascal.grammar|)));
// - Manually paste output to /org/rascalmpl/library/rascal/conversion/grammar/Rascal.rsc

import ParseTree;
import rascal::parser::Grammar;
import IO;
import Set;
import List;
import String;
import ValueIO;

bool debug = false;

public str grammar2rascal(Grammar g, str name) {
  return "module <name> <grammar2rascal(g)>";
}

public str grammar2rascal(Grammar g) {
  <normals,literals> = separateLiterals(g.productions);
  return ( "" | it + "\n" + topProd2rascal(p) | gr <- groupByNonTerminal(normals), Production p <- gr)
       + "\n\n"
       + ( "" | it + "\n" + topProd2rascal(p) | Production p <- literals);
}

tuple[set[Production],set[Production]] separateLiterals(set[Production] prods) {
  literals = { p | p:prod(_,lit(_),_) <- prods } 
           + { p | p:restrict(lit(_),_,_) <- prods };
  return <prods - literals, literals>;
}

set[set[Production]] groupByNonTerminal(set[Production] productions) {
  solve (productions) {
    switch (productions) {
      case {set[Production] r, Production p:prod(_,Symbol rhs,_)} : 
        productions = {r, choice(rhs, {p})};
      case {set[Production] r, choice(Symbol s, set[Production] a1), choice(s, set[Production] a2)} :
        productions = {r, choice(s, a1 + a2)};
      case {set[Production] r, choice(Symbol s, set[Production] a1), restrict(s, others(s), set[list[Symbol]] restr)} :
        productions = {r, restrict(s,choice(s,a1), restr)};
    }
  }
   
  return group(productions, same);
}

bool same(Production p, Production q) {
  return sort(p) == sort(q);
}

public str topProd2rascal(Production p) {
  if (/prod(_,rhs,_) := p) {
    sym = symbol2rascal(rhs);
    return "<(start(_) := rhs) ? "start ":""><(sym == "LAYOUT") ? "layout" : "syntax"> <sym>\n\t= <prod2rascal(p)>;\n";
  }
  if (regular(_,_) := p) {
    return ""; // ignore generated stubs
  }
  if(restrict(rhs, language, restrictions) := p){
  	return "<for(r <- restrictions){>syntax <symbol2rascal(rhs)> =\n\t<prod2rascal(language)>\n\t# <for(e <- r){><symbol2rascal(e)> <}><}>;\n";
  }
  throw "could not find out defined symbol for <p>";
}

public str prod2rascal(Production p) {
  if(debug) println("prod2rascal: <p>");
  switch (p) {
    case choice(s, alts) : {
        	<fst, rest> = takeOneFrom(alts);
			return ( prod2rascal(fst) | "<it>\n\t| <prod2rascal(pr)>" | pr <- rest );
		}
    	
    case first(s, alts) :
      	return ( prod2rascal(head(alts)) | "<it>\n\t\> <prod2rascal(pr)>" | pr <- tail(alts) );
      
    case \assoc(s, a, alts) : {
    		<fst, rest> = takeOneFrom(alts);
    		return ( "<attr2mod(\assoc(a))> (  <prod2rascal(fst)> " | "<it>\n\t\t\> <prod2rascal(pr)>" | pr <- rest ) + "\n\t)";
 		}
    case diff(s,p,alts) : {
    		<fst, rest> = takeOneFrom(alts);
       		return ( "<prod2rascal(p)>\n\t- <prod2rascal(fst)>" | "<it>\n\t- <prod2rascal(pr)>" | pr <- rest );
       	}
 
    case restrict(rhs, language, restrictions):
    	return "<for(r <- restrictions){><symbol2rascal(rhs)>\n\t# <for(e <- r){> <symbol2rascal(e)> <}><}>";
 
    case others(sym):
        return "...";
 
    case prod(_,lit(_),_) : return "";
    
    case prod(list[Symbol] lhs,Symbol rhs,Attributes attrs) :
      	return "<attrs2mods(attrs)><for(s <- lhs){><symbol2rascal(s)> <}>";
 
    case regular(_,_) :
    	return "";
    
    default: throw "missed a case <p>";
  }
}

test prod2rascal(prod([sort("PICO-ID"),lit(":"),sort("TYPE")],sort("ID-TYPE"),\no-attrs()))
     == "PICO_ID \":\" TYPE ";

test prod2rascal(
     prod([sort("PICO-ID"), lit(":"), sort("TYPE")],
               sort("ID-TYPE"),
              attrs([term(cons("decl")),\assoc(left())]))) ==
               "left decl: PICO_ID \":\" TYPE ";
               
test prod2rascal(
	 prod([\char-class([range(9,9), range(10,10),range(13,13),range(32,32)])],sort("LAYOUT"),attrs([term(cons("whitespace"))]))) ==
	 "whitespace: [\\t\\n\\r\\ ] ";

test prod2rascal(
	first(sort("EXP"),[prod([sort("EXP"),lit("||"),sort("EXP")],sort("EXP"),\no-attrs()),
	                   prod([sort("EXP"),lit("-"),sort("EXP")],sort("EXP"),\no-attrs()),
	                   prod([sort("EXP"),lit("+"),sort("EXP")],sort("EXP"),\no-attrs())])) ==
	"EXP \"||\" EXP \n\t\> EXP \"-\" EXP \n\t\> EXP \"+\" EXP ";	

public str attrs2mods(Attributes as) {
  switch (as) {
    case \no-attrs(): 
      return "";
      
    case \attrs([list[Attr] a,term(node zz:"cons"(str c)),list[Attr] b]) : {
      return attrs2mods(\attrs([a,b])) + "<c>: ";
      }
      
    case \attrs([a,b*]): {
        if(size(b) == 0)
           return "<attr2mod(a)> ";
        return "<attr2mod(a)> <attrs2mods(\attrs(b))>"; 
      }
      
    case \attrs([]):
    	return "";  
    	 
    default:   throw "attrs2rascal: missing case <attrs>";
  }
}

test attrs2mods(\attrs([\assoc(\left())])) == "left ";
test attrs2mods(\attrs([\assoc(\left()), \assoc(\right())])) == "left right ";
test attrs2mods(\attrs([\assoc(\left()), term(cons("C")), \assoc(\right())])) == "left right C: ";
test attrs2mods(\attrs([term(cons("C"))])) == "C: ";
test attrs2mods(\attrs([term(cons("C")), term("lexical")])) == "lex C: ";

public str attr2mod(Attr a) {
  switch(a) {
    case \assoc(\left()): return "left";
    case \assoc(\right()): return "right";
    case \assoc(\non-assoc()): return "non-assoc";
    case \assoc(\assoc()): return "assoc";
    case term("lexical"): return "lex";
    case \bracket(): return "bracket";
    default : return "/*<a>*/";
  }
}

test attr2mod(\assoc(\left())) == "left";

public str symbol2rascal(Symbol sym) {
  switch (sym) {
    case label(str l, x) :
    	return "<symbol2rascal(x)> <l>";  
    case sort(x) :
    	return replaceAll(x, "-", "_");
    case lit(x) :
    	return "\"<escape(x)>\"";
    case cilit(x) :
    	return "\"<escape(x)>\"";
    case \lex(x):
    	return symbol2rascal(x);
    case \cf(x):
    	return symbol2rascal(x);
    case \parameterized-sort(str name, list[Symbol] parameters):
        return "<name>[<params2rascal(parameters)>]";
    case \char-class(x) : 
    	return cc2rascal(x);
    case \seq(syms):
        return "( <for(s <- syms){> <symbol2rascal(s)> <}> )";
    case opt(x) : 
    	return "<symbol2rascal(x)>?";
    case iter(x) : 
    	return "<symbol2rascal(x)>+";
    case \iter-star(x) : 
    	return "<symbol2rascal(x)>*";
    case \iter-seps(x,seps) :
        return iterseps2rascal(x, seps, "+");
    case \iter-star-seps(x,seps) : 
    	return iterseps2rascal(x, seps, "*");
    case \layout(): 
    	return "";
    case \start(x):
    	return symbol2rascal(x);
    case intersection(lhs, rhs):
        return "<symbol2rascal(lhs)> && <symbol2rascal(rhs)>";
    case union(lhs,rhs):
     	return "<symbol2rascal(lhs)> || <symbol2rascal(rhs)>";
    case difference(lhs,rhs):
     	return "<symbol2rascal(lhs)> -  <symbol2rascal(rhs)>";
    case complement(lhs):
     	return "!<symbol2rascal(lhs)>";

  }
  throw "symbol2rascal: missing case <sym>";
}

test symbol2rascal(lit("abc")) == "\"abc\"";
test symbol2rascal(lit("\\\n")) == "\"\\\\\\n\"";
test symbol2rascal(sort("ABC")) == "ABC";
test symbol2rascal(cilit("abc")) == "\"abc\"";
test symbol2rascal(label("abc",sort("ABC"))) == "ABC abc";
test symbol2rascal(\parameterized-sort("A", [sort("B")])) == "A[[B]]";
test symbol2rascal(\parameterized-sort("A", [sort("B"), sort("C")])) == "A[[B, C]]";
test symbol2rascal(opt(sort("A"))) == "A?";
test symbol2rascal(\char-class([range(97,97)])) == "[a]";
test symbol2rascal(\iter-star-seps(sort("A"),[\layout()])) == "A*";
test symbol2rascal(\iter-seps(sort("A"),[\layout()])) == "A+";
test symbol2rascal(opt(\iter-star-seps(sort("A"),[\layout()]))) == "A*?";
test symbol2rascal(opt(\iter-seps(sort("A"),[\layout()]))) == "A+?";
test symbol2rascal(\iter-star-seps(sort("A"),[\layout(),lit("x"),\layout()])) == "{A \"x\"}*";
test symbol2rascal(\iter-seps(sort("A"),[\layout(),lit("x"),\layout()])) == "{A \"x\"}+";
test symbol2rascal(opt(\iter-star-seps(sort("A"),[\layout(),lit("x"),\layout()]))) == "{A \"x\"}*?";
test symbol2rascal(opt(\iter-seps(sort("A"),[\layout(),lit("x"),\layout()]))) == "{A \"x\"}+?";
test symbol2rascal(\iter-star(sort("A"))) == "A*";
test symbol2rascal(\iter(sort("A"))) == "A+";
test symbol2rascal(opt(\iter-star(sort("A")))) == "A*?";
test symbol2rascal(opt(\iter(sort("A")))) == "A+?";
test symbol2rascal(\iter-star-seps(sort("A"),[lit("x")])) == "{A \"x\"}*";
test symbol2rascal(\iter-seps(sort("A"),[lit("x")])) == "{A \"x\"}+";
test symbol2rascal(opt(\iter-star-seps(sort("A"),[lit("x")]))) == "{A \"x\"}*?";
test symbol2rascal(opt(\iter-seps(sort("A"),[lit("x")]))) == "{A \"x\"}+?";

public str iterseps2rascal(Symbol sym, list[Symbol] seps, str iter){
  separators = "<for(sp <- seps){><symbol2rascal(sp)><}>";
  if (separators != "")
     return "{<symbol2rascal(sym)> <separators>}<iter>";
  else
    return "<symbol2rascal(sym)><separators><iter>";
}

public str params2rascal(list[Symbol] params){
  len = size(params);
  if(len == 0)
  	return "";
  if(len == 1)
  	return symbol2rascal(params[0]);
  sep = "";
  res = "";
  for(Symbol p <- params){
      res += sep + symbol2rascal(p);
      sep = ", ";
  }
  return res;	
}

public str escape(str s){
  res = "";
  n = size(s);
  if(n > 0)
 	 for(int i <- [0 .. n-1])
     	res += stringchar2rascal(charAt(s, i));
  return res;
}

public str cc2rascal(list[CharRange] ranges) {
  return "[<for (r <- ranges){><range2rascal(r)><}>]";
}

public str range2rascal(CharRange r) {
  switch (r) {
    case range(c,c) : return charclasschar2rascal(c);
    case range(c,d) : return "<charclasschar2rascal(c)>-<charclasschar2rascal(d)>";
    default: throw "range2rascal: missing case <range>";
  }
}

test range2rascal(range(97,97))  == "a";
test range2rascal(range(97,122)) == "a-z";
test range2rascal(range(10,10))  == "\\n";
test range2rascal(range(34,34))  == "\\\"";

// A good old ASCII table in order to convert numbers < 128 to readable (properly escaped) string
// characters. For instance, ascii[10] maps to the string "\\n".
// Note that in charclasses also the characters minus (-) and underscore (_) have to be escaped.
// This is handled by charclasschar2rascal.

private list[str] ascii =
[
 
//Decimal   Value   Description
//-------  -------  --------------------------------
/* 000 */  "\\000", // NUL   (Null char.)
/* 001 */  "\\001", // SOH   (Start of Header)
/* 002 */  "\\002", // STX   (Start of Text)
/* 003 */  "\\003", // ETX   (End of Text)
/* 004 */  "\\004", // EOT   (End of Transmission)
/* 005 */  "\\005", // ENQ   (Enquiry)
/* 006 */  "\\006", // ACK   (Acknowledgment)
/* 007 */  "\\007", // BEL   (Bell)
/* 008 */    "\\b", // BS    (Backspace)
/* 009 */    "\\t", // HT    (Horizontal Tab)
/* 010 */    "\\n", // LF    (Line Feed)
/* 011 */  "\\013", // VT    (Vertical Tab)
/* 012 */    "\\f", // FF    (Form Feed)
/* 013 */    "\\r", // CR    (Carriage Return)
/* 014 */  "\\016", // SO    (Shift Out)
/* 015 */  "\\017", // SI    (Shift In)
/* 016 */  "\\020", // DLE   (Data Link Escape)
/* 017 */  "\\021", // DC1   (Device Control 1)
/* 018 */  "\\022", // DC2   (Device Control 2)
/* 019 */  "\\023", // DC3   (Device Control 3)
/* 020 */  "\\024", // DC4   (Device Control 4)
/* 021 */  "\\025", // NAK   (Negative Acknowledgemnt)
/* 022 */  "\\026", // SYN   (Synchronous Idle)
/* 023 */  "\\027", // ETB   (End of Trans. Block)
/* 024 */  "\\030", // CAN   (Cancel)
/* 025 */  "\\031", // EM    (End of Medium)
/* 026 */  "\\032", // SUB   (Substitute)
/* 027 */  "\\033", // ESC   (Escape)
/* 028 */  "\\034", // FS    (File Separator)
/* 029 */  "\\035", // GS    (Group Separator)
/* 030 */  "\\036", // RS    (Reqst to Send)(Rec. Sep.)
/* 031 */  "\\037", // US    (Unit Separator)
/* 032 */      " ", // SP    (Space)
/* 033 */      "!", //  !    (exclamation mark)
/* 034 */   "\\\"", //  "    (double quote)
/* 035 */      "#", //  #    (number sign)
/* 036 */      "$", //  $    (dollar sign)
/* 037 */      "%", //  %    (percent)
/* 038 */      "&", //  &    (ampersand)
/* 039 */   "\\\'", //  '    (single quote)
/* 040 */      "(", //  (    (left/open parenthesis)
/* 041 */      ")", //  )    (right/closing parenth.)
/* 042 */      "*", //  *    (asterisk)
/* 043 */      "+", //  +    (plus)
/* 044 */      ",", //  ,    (comma)
/* 045 */      "-", //  -    (minus or dash)
/* 046 */      ".", //  .    (dot)
/* 047 */      "/", //  /    (forward slash)
/* 048 */      "0", //  0
/* 049 */      "1", //  1
/* 050 */      "2", //  2
/* 051 */      "3", //  3
/* 052 */      "4", //  4
/* 053 */      "5", //  5
/* 054 */      "6", //  6
/* 055 */      "7", //  7
/* 056 */      "8", //  8
/* 057 */      "9", //  9
/* 058 */      ":", //  :    (colon)
/* 059 */      ";", //  ;    (semi-colon)
/* 060 */   "\\\<", //  <    (less than)
/* 061 */      "=", //  =    (equal sign)
/* 062 */   "\\\>", //  >    (greater than)
/* 063 */      "?", //  ?    (question mark)
/* 064 */      "@", //  @    (AT symbol)
/* 065 */      "A", //  A
/* 066 */      "B", //  B
/* 067 */      "C", //  C
/* 068 */      "D", //  D
/* 069 */      "E", //  E
/* 070 */      "F", //  F
/* 071 */      "G", //  G
/* 072 */      "H", //  H
/* 073 */      "I", //  I
/* 074 */      "J", //  J
/* 075 */      "K", //  K
/* 076 */      "L", //  L
/* 077 */      "M", //  M
/* 078 */      "N", //  N
/* 079 */      "O", //  O
/* 080 */      "P", //  P
/* 081 */      "Q", //  Q
/* 082 */      "R", //  R
/* 083 */      "S", //  S
/* 084 */      "T", //  T
/* 085 */      "U", //  U
/* 086 */      "V", //  V
/* 087 */      "W", //  W
/* 088 */      "X", //  X
/* 089 */      "Y", //  Y
/* 090 */      "Z", //  Z
/* 091 */      "[", //  [    (left/opening bracket)
/* 092 */   "\\\\", //  \    (back slash)
/* 093 */      "]", //  ]    (right/closing bracket)
/* 094 */      "^", //  ^    (caret/circumflex)
/* 095 */      "_", //  _    (underscore)
/* 096 */      "`", //  `    (backquote)
/* 097 */      "a", //  a
/* 098 */      "b", //  b
/* 099 */      "c", //  c
/* 100 */      "d", //  d
/* 101 */      "e", //  e
/* 102 */      "f", //  f
/* 103 */      "g", //  g
/* 104 */      "h", //  h
/* 105 */      "i", //  i
/* 106 */      "j", //  j
/* 107 */      "k", //  k
/* 108 */      "l", //  l
/* 109 */      "m", //  m
/* 110 */      "n", //  n
/* 111 */      "o", //  o
/* 112 */      "p", //  p
/* 113 */      "q", //  q
/* 114 */      "r", //  r
/* 115 */      "s", //  s
/* 116 */      "t", //  t
/* 117 */      "u", //  u
/* 118 */      "v", //  v
/* 119 */      "w", //  w
/* 120 */      "x", //  x
/* 121 */      "y", //  y
/* 122 */      "z", //  z
/* 123 */      "{", //  {    (left/opening brace)
/* 124 */      "|", //  |    (vertical bar)
/* 125 */      "}", //  }    (right/closing brace)
/* 126 */      "~", //  ~    (tilde)
/* 127 */  "\\177"  //DEL    (delete)
];

// In character classes, additional characters have to be escaped.
// These are filtered here before falling back on the standard string escapes.

public str charclasschar2rascal(int ch){
  switch(ch){
  case 32:	return "\\ ";	// space ( )
  	
  case 45:	return "\\-";	// minus (-)
  	
  case 91:	return "\\[";	// left bracket ([)
  	
  case 93:	return "\\]";	// right bracket (])
  	
  case 95:	return "_";	// underscore (_)
 
  default:	return stringchar2rascal(ch);
  }
}

public str stringchar2rascal(int ch) {
  if(ch < 128)
     return ascii[ch];
  if (ch < 256) {
    d1 = ch % 8; r1 = ch / 8;
    d2 = r1 % 8; r2 = r1 / 8;
    d3 = r2;
    return "\\<d3><d2><d1>";
  }
  else {
    d1 = ch % 16; r1 = ch / 16;
    d2 = r1 % 16; r2 = r1 / 16;
    d3 = r2 % 16; r3 = r2 / 16;
    d4 = r3;
    return "\\u<d4><d3><d2><d1>";
  }
}

test stringchar2rascal(97) 	== "a";
test stringchar2rascal(10) 	== "\\n";
test stringchar2rascal(34) 	== "\\\"";
test stringchar2rascal(255) == "\\377";
