module lang::rascalcore::check::Test3
//import ParseTree;
 
//lexical TTL_id = ([A-Z][a-zA-Z0-9]* !>> [a-zA-Z0-9]) ;//\ TTL_Reserved;
lexical TTL_StringCharacter = "a";
    //= "\\" [\" \' \< \> \\ b f n r t]  
    //| ![\" \' \< \> \\]
    //| [\n][\ \t \u00A0 \u1680 \u2000-\u200A \u202F \u205F \u3000]* [\'] // margin 
    //;

lexical TTL_String = "\"" TTL_StringCharacter*  "\"";

//keyword TTL_Reserved = "test" | "expect" ;
//
//layout TTL_Layout = TTL_WhitespaceAndComment* !>> [\ \t\n\r];
//
//lexical TTL_WhitespaceAndComment 
//   = [\ \t\n\r]
//   | @category="Comment" ws2: "@@" ![\n]+ $
//   | @category="Comment" ws3: "\<@@" ![]*  "@@\>"
//   ;
   
//start syntax TTL = ttl: TTL_TestItem* items ;
//
//lexical TTL_Token = ![\[\]] | "[" ![\[]* "]";
//
//syntax TTL_TestItem
//    = "test" TTL_id name "[[" TTL_Token* tokens "]]" TTL_Expect expect
//    ;
//
//syntax TTL_Expect
//    = none: ()
//    | "expect" "{" {TTL_String ","}* messages "}"
//    ;