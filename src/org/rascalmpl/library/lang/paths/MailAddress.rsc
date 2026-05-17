@synopsis{This encodes the mail address part of RFC822}
@pitfalls{
This was taken over literally from RFC822 but not tested.
}
module lang::paths::MailAddress

lexical Address 
    =  adressee: Mailbox
    | namedList: Group 
    ;

lexical Group =  Phrase ":" {Mailbox ","}+ ";";

lexical Mailbox  
    = simpleAddress: AddrSpec         
    | nameAndAddrSpec: Phrase RouteAddr
    ;

lexical RouteAddr  =  "\<" Route? AddrSpec "\>";

lexical Route =  {Domain "@"}+ ":"; // hard to interpret original: 1#("@" domain) ":"  

lexical AddrSpec =  globalAddress: LocalPart "@" Domain; 

lexical LocalPart  =  uninterpretedCasePreserved: {Word "."}+ ;

lexical Domain =  {SubDomain "."}+;

lexical SubDomain  =  DomainRef | DomainLiteral;

lexical DomainLiteral = "[" (Dtext | QuotedPair)* "]";

lexical Dtext = (![\\\]\[\n\r\ ] + [\ ])+;

lexical DomainRef =  Atom;

lexical Phrase = Word+;

lexical Atom = ![] - [()\<\>@,;:|\".\[\]] - [\a00-a20] - [\ ];

lexical Specials = [()\<\>@,;:|\".\[\]];    

lexical Controls = [\a00-\a20];

lexical Word = Atom | QuotedString;

lexical QuotedString = "\"" (Qtext | QuotedPair)* "\"";

lexical Qtext = (![\\\"\n\r] + [\ ])+;

lexical QuotedPair = "\\" CHAR;

lexical CHAR = [\a00-\a7F]; // All of ASCII
