@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::json::\syntax::JSON

/* This JSON parser is based on the JSON grammar found in RFC4627.
   http://www.ietf.org/rfc/rfc4627
 */

/* The definition of a JSON Text is taken from section 2 */ 
start syntax JSONText
	= Object
	| Array
	;
	
/* The definition of value is taken from section 2.1 */
syntax Value
	= Object
	| Array
	| NumericLiteral
	| StringLiteral
	| False: "false"
	| Null: "null"
	| True: "true"
	;

/* The definition of objects is taken from section 2.2 */
syntax Object
	= Object: "{" { Member "," }* members "}"
	;

syntax Member
	= Member: StringLiteral memberName ":" Value memberValue
	;
	
/* The definition of arrays is taken from section 2.3 */
syntax Array
	= Array: "[" { Value ","}* values "]"
	;
	
/* The definition of numbers is taken from section 2.4 */
lexical NumericLiteral
	= RealLiteral
	| IntegerLiteral
	;
	
lexical RealLiteral
	= [\-]? [0] [E e] [+ \-]? [0-9]+
	| [\-]? [1-9] [0-9]* [E e] [+ \-]? [0-9]+
	| [\-]? [0] "." !>> "." [0-9]*   
	| [\-]? [1-9] [0-9]* "." !>> "." [0-9]*   
	| [\-]? [0] "." [0-9]* [E e] [+ \-]? [0-9]+  
	| [\-]? [1-9] [0-9]* "." [0-9]* [E e] [+ \-]? [0-9]+  
	;

lexical IntegerLiteral
	= [\-]? "0" !>> [0-9] 
	| [\-]? [1-9] [0-9]* !>> [0-9] ;
 
/* The definition of strings is taken from section 2.5 */
lexical StringLiteral
	= "\"" StringChar* chars "\"";
	
lexical StringChar
	= ![\" \\]
	| "\\" [\" \\ / b f n r t] 
	| UnicodeEscape 
	;

lexical UnicodeEscape
	= utf16: "\\" [u] [0-9 A-F a-f] [0-9 A-F a-f] [0-9 A-F a-f] [0-9 A-F a-f] 
    | utf32: "\\" [U] [0-9 A-F a-f] [0-9 A-F a-f] [0-9 A-F a-f] [0-9 A-F a-f] [0-9 A-F a-f] [0-9 A-F a-f] 
	;

/* The definition of keywords is taken from section 2.1 */	
keyword JSONKeywords
	= "false"
	| "null"
	| "true"
	;

/* The definition of layout is taken from section 2 */
layout LAYOUT
	= [\t-\n \r \ ]* !>> [\t-\n \r \ ]
	;
