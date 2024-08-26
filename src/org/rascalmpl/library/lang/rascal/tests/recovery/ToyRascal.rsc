module lang::rascal::tests::recovery::ToyRascal

start syntax FunctionDeclaration = Signature FunctionBody;

syntax Signature = Name Parameters;

syntax Name = "f" | "g";

syntax Parameters = "(" ")";

syntax FunctionBody = "{" Statement* statements "}" ;

syntax Statement 
    = "{" Statement+ statements "}"
    | "s" ";";

layout Layout = [ \n\r\t]*;
