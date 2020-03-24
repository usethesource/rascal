module lang::rascal::tutor::AsciiDoc

import ParseTree;
syntax Document  = Header Preamble Body ;

// NOTE: "You cannot have a revision line without an author line."
syntax Header    = DocumentTitle AuthorLine? RevisionLine? Attribute* NEW_LINE  ;

syntax DocumentTitle  = ^ "=" Phrase ;
syntax AuthorLine     =  CHARS+ "\<" Email "\>" NEW_LINE ;
 // based on example v1.0, 2014-01-01
lexical RevisionLine   = ^ "v" NUMBERS "." NUMBERS NEW_LINE ;
syntax Attribute      = ":" "!"? AttributeName "!"? ":" AttributeValue NEW_LINE ;
syntax AttributeName   = Word ;
syntax AttributeValue  = ANYCHAR ;

syntax Preamble        = Paragraph  ;
syntax Paragraph       = Admonition? Phrase+ NEW_LINE;

syntax LiteralParagraph = LiteralPhrase+ NEW_LINE;

syntax Body            = Section* ;
syntax Section         = ExplicitId? SectionHeader SectionBody ;
syntax SectionHeader  = ^ SectionStart Phrase ;

lexical SectionStart  = "==" "="* !>> "=";

syntax SectionBody    = Phrase* NEW_LINE ;

syntax InlineMacro    = Method ":" Target "[" Params "]" ;
syntax Params          = Param* ;
syntax Method          = "link" | "include" | IDENTIFIER ;
syntax Target          = IDENTIFIER ;
syntax Param           = Word ;

syntax Admonition     = "NOTE:" | "TIP:" | "IMPORTANT:" | "WARNING:" |"CAUTION:";
syntax ExplicitId     = ^ "[[" Word+ "]]" NEW_LINE;

// See http://www.regular-expressions.info/email.html for a better representation of an email
lexical Email          = CHARS "@" CHARS "." CHARS ; // 
syntax Phrase          = {Word WS}* !>> Word NEW_LINE ;

syntax LiteralPhrase  = Indent {Word WS}+ !>> Word NEW_LINE ;

lexical Indent        = [\ \t]+ !>> [\ \t];
syntax SpecialCharacters = "$" | "/" | "-"; // Temporal fix to avoid failing on URLs
syntax AttributeSubstitution =  "{" PresetAttributes "}" ;
// See http://asciidoctor.org/docs/user-manual/#built-in-data-attributes
syntax PresetAttributes = "author" | "email" | "backend" | "doctitle" ;

lexical NEW_LINE        = "\n" ;
lexical LINE_BREAK      = "+" ;
lexical NUMBERS          = [0-9]+ !>> [0-9];

lexical Word            = CHARS PUNCTUATION? ;
lexical CHARS           = [0-9a-zA-Z]+ !>> [0-9a-zA-Z];
lexical IDENTIFIER      = [a-zA-Z]+ !>> [a-zA-Z];
//BEGIN_PUNCTUATION = [¿¡] ; // Some languages have beginning punctuation (e.g. Spanish)
lexical PUNCTUATION     = [. : , ; !] ;
//word            = ~[\n\t ]+ ;
lexical ANYCHAR         = ![\n\t\ ]+ ; // PH
lexical WS              = [\ \t\r]* !>> [\ \t\r];

value main(){
    return 
    parse(#ExplicitId, "[[abc]]\n"
    );
}