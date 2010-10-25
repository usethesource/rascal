module rascal::syntax::Escape

import String;

@doc{
  A good old ASCII table in order to convert numbers < 128 to readable (properly escaped) string
  characters. For instance, ascii[10] maps to the string "\\n".
}
public list[str] ascii =
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

@doc{
  Creates a Rascal-character-classes escaped string character from a given
  decimal index into the UTF8 table. 
}
public str makeCharClassChar(int ch){
  switch(ch) {
    case 32:	return "\\ ";	// space ( )
    case 45:	return "\\-";	// minus (-)
    case 91:	return "\\[";	// left bracket ([)
    case 93:	return "\\]";	// right bracket (])
    case 95:	return "_";	// underscore (_)
    default:	return makeStringChar(ch);
  }
}

private list[str] hex = ["<i>" | i <- [0..9]] + ["A","B","C","D","E","F"];

@doc{
  Creates a Rascal escaped string character from a given decimal index into the UTF8 table.
} 
public str makeStringChar(int ch) {
  if(ch < 128) {
     return ascii[ch];
  }
  
  if (ch < 256) {
    d1 = ch % 8; r1 = ch / 8;
    d2 = r1 % 8; r2 = r1 / 8;
    d3 = r2;
    return "\\<d3><d2><d1>";
  }
       
  d1 = ch % 16; r1 = ch / 16;
  d2 = r1 % 16; r2 = r1 / 16;
  d3 = r2 % 16; r3 = r2 / 16;
  d4 = r3;
  return "\\u<hex[d4]><hex[d3]><hex[d2]><hex[d1]>";
}

test makeStringChar(97) 	== "a";
test makeStringChar(10) 	== "\\n";
test makeStringChar(34) 	== "\\\"";
test makeStringChar(255) == "\\377";

@doc{
  Escapes the characters of the given string using the Rascal escaping conventions.
}
public str escape(str s){
  if (s == "") return s;
  return (""| it + makeStringChar(charAt(s, i)) | i <- [0..size(s)-1]);
}

@doc{
  Escapes the characters of the given string using the Rascal escaping conventions.
  and surround by " quotes.
}
public str quote(str s) {
  return "\"<escape(s)>\"";
}

@doc{
  Escapes the characters of the given string using the Rascal escaping conventions.
  and surround by ' quotes.
}
public str ciquote(str s) {
  return "\'<escape(s)>\'";
}
