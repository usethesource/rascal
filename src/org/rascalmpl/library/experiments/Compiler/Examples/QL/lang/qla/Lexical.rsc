module experiments::Compiler::Examples::QL::lang::qla::Lexical

lexical Label = @category="Constant" String; 
  
syntax QType
  = boolean: "boolean" 
  | string: "string"
  | integer: "integer"
  | money: "money"
  ;

lexical String = [\"] StrChar* [\"];

lexical StrChar
  = ![\"\\]
  | [\\][\\\"nfbtr]
  ;

lexical Integer =  [0-9]+ !>> [0-9];

lexical Money =  [0-9]+ "." [0-9]+ !>> [0-9] ;

layout Standard = WhitespaceOrComment* !>> [\ \t\n\f\r] !>> "//";
  
lexical Comment = @category="Comment" "//" ![\n\r]* $;

syntax WhitespaceOrComment 
  = whitespace: Whitespace
  | comment: Comment
  ;   

lexical Whitespace 
  = [\u0009-\u000D \u0020 \u0085 \u00A0 \u1680 \u180E \u2000-\u200A \u2028 \u2029 \u202F \u205F \u3000]
  ; 
  
lexical Id 
  = id: ([a-z A-Z 0-9 _] !<< [a-z A-Z][a-z A-Z 0-9 _]* !>> [a-z A-Z 0-9 _]) \ Keywords
  ;
  
keyword Keywords = ;