@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@bootstrapParser
module lang::rascal::format::Escape

import String;

@doc{
  A good old ASCII table in order to convert numbers < 128 to readable (properly escaped) string
  characters. For instance, ascii((10)) maps to the string "\\n".
}
public list[str] ascii =
[
//Decimal   Value   Description
//-------  -------  --------------------------------
/* 000 */  "\\a00", // NUL   (Null char.)
/* 001 */  "\\a01", // SOH   (Start of Header)
/* 002 */  "\\a02", // STX   (Start of Text)
/* 003 */  "\\a03", // ETX   (End of Text)
/* 004 */  "\\a04", // EOT   (End of Transmission)
/* 005 */  "\\a05", // ENQ   (Enquiry)
/* 006 */  "\\a06", // ACK   (Acknowledgment)
/* 007 */  "\\a07", // BEL   (Bell)
/* 008 */    "\\b", // BS    (Backspace)
/* 009 */    "\\t", // HT    (Horizontal Tab)
/* 010 */    "\\n", // LF    (Line Feed)
/* 011 */  "\\a0B", // VT    (Vertical Tab)
/* 012 */  "\\a0C", // FF    (Form Feed)
/* 013 */  "\\a0D", // CR    (Carriage Return)
/* 014 */  "\\a0E", // SO    (Shift Out)
/* 015 */  "\\a0F", // SI    (Shift In)
/* 016 */  "\\a10", // DLE   (Data Link Escape)
/* 017 */  "\\a11", // DC1   (Device Control 1)
/* 018 */  "\\a12", // DC2   (Device Control 2)
/* 019 */  "\\a13", // DC3   (Device Control 3)
/* 020 */  "\\a14", // DC4   (Device Control 4)
/* 021 */  "\\a16", // NAK   (Negative Acknowledgemnt)
/* 022 */  "\\a16", // SYN   (Synchronous Idle)
/* 023 */  "\\a17", // ETB   (End of Trans. Block)
/* 024 */  "\\a18", // CAN   (Cancel)
/* 025 */  "\\a19", // EM    (End of Medium)
/* 026 */  "\\a1A", // SUB   (Substitute)
/* 027 */  "\\a1B", // ESC   (Escape)
/* 028 */  "\\a1C", // FS    (File Separator)
/* 029 */  "\\a1D", // GS    (Group Separator)
/* 030 */  "\\a1E", // RS    (Reqst to Send)(Rec. Sep.)
/* 031 */  "\\a1F", // US    (Unit Separator)
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
/* 127 */  "\\a7F"  // DEL   (delete)
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

private list[str] hex = ["<i>" | i <- [0..10]] + ["A","B","C","D","E","F"];

@doc{
  Creates a Rascal escaped string character from a given decimal index into the UTF8 table.
} 
public str makeStringChar(int ch) {
  if(ch < 128)
     return ascii[ch];
  elseif (ch >= 128 && ch <= 0xFFF) {
    d1 = ch % 8; r1 = ch / 8;
    d2 = r1 % 8; r2 = r1 / 8;
    d3 = r2 % 8; r3 = r2 / 8;
    d4 = r3;
    return "\\u<d4><d3><d2><d1>";
  }
       
  d1 = ch % 16; r1 = ch / 16;
  d2 = r1 % 16; r2 = r1 / 16;
  d3 = r2 % 16; r3 = r2 / 16;
  d4 = r3 % 16; r4 = r3 / 16;
  d5 = r4 % 16; r5 = r4 / 16;
  d6 = r5;
  return "\\U<hex[d6]><hex[d5]><hex[d4]><hex[d3]><hex[d2]><hex[d1]>";
}

test bool testA() = makeStringChar(97) 	== "a";
test bool testNl() = makeStringChar(10) 	== "\\n";
test bool testQuote() = makeStringChar(34) 	== "\\\"";
test bool testEOF() = makeStringChar(255) == "\\u0377";
test bool testHex() = makeStringChar(0xABCDEF) == "\\UABCDEF";

@doc{
  Escapes the characters of the given string using the Rascal escaping conventions.
}
public str escape(str s){
  if (s == "") return s;
  return (""| it + makeStringChar(charAt(s, i)) | i <- [0..size(s)]);
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
