@synopsis{Implements RFC1738 for URL syntax and maps them to `loc` values.}
module lang::paths::URL

lexical URL
    = http:    "http://" HostPort ( "/" Hpath ( "?" Search)?)?
    | ftp:     "ftp://" Login ("/" Fpath (";type=" FtpType)?)?    
    | news:    "news:" GroupPart
    | nntp:    "nntp://" HostPort "/" Group ("/" Digit+)?
    | telnet:  "telnet://" Login "/"?
    | gopher:  "gopher://" HostPort ( "/" ( Gtype ( Selector ("%09" Search ("%09" GopherPlusString)? )? )? )? )?
    | waisdatabase: "wais://" HostPort "/" Database 
    | waisindex:    "wais://" HostPort "/" Database "?" Search
    | waisdoc:      "wais://" HostPort "/" Database "/" Wtype "/" Wpath
    | mailto:   "mailto:" Encoded822addr
    | file:     "file://" (Host | "localhost")? "/" Fpath
    | prospero:  "prospero://" HostPort "/" Ppath *[ Fieldspec ]
    | other:     Scheme ":" SchemePart
    ;
 
lexical Ppath          = {Psegment "/"}+;

lexical Psegment       = (Uchar | [?:@&=])*;

lexical Fieldspec      = ";" FieldName "=" FieldValue;

lexical FieldName      = (Uchar | [?:@&])*;

lexical FieldValue     = (Uchar | [?:@&])*;

lexical Database       = Uchar*;
lexical Wtype          = Uchar*;
lexical Wpath          = Uchar*;

lexical GroupPart = "*" | Group | Article;

lexical Group  = Alpha (Alpha | Digit | [\-.+_])*;

lexical Article = (Uchar | [;/?:&=])* "@" Host;

lexical Encoded822addr = Xchar+; // further defined in RFC822

lexical Gtype = Xchar;
lexical Selector = Xchar*;
lexical GopherPlusString = Xchar*;

lexical Hpath = {Hsegment "/"}+;

lexical Hsegment = (Uchar | [;:@&=])*;

lexical Search = (Uchar | [;:@&=])*;

lexical Fpath = {Fsegment "/"}+;

lexical Fsegment= (Uchar | [?:@&=])*;

lexical FtpType = [AIDaid];

lexical Scheme = [a-z0-9+\-.]+;

lexical SchemePart = Xchar* | IpSchemePart;

lexical IpSchemePart = "//" Login ("/" UrlPath);

lexical Login = ( User ( ":" Password )? "@" )? HostPort;

lexical HostPort = Host ( ":" Port )?;

lexical Host = HostName | HostNumber;

lexical HostName = (DomainLabel ".")* TopLabel;

lexical DomainLabel = {AlphaDigit "-"}+;

lexical TopLabel = Alpha {AlphaDigit "-"}+;

lexical HostNumber = Digit+ "." Digit+ "." Digit+ "." Digit+;

lexical Port = Digit+;

lexical User = (Uchar | [;?&=])*;

lexical Password = (Uchar | [;?&=])*;

lexical UrlPath = Xchar* ; //depends on protocol see section 3.1 of RFC1738

lexical Alpha = LowAlpha | HighAlpha;

lexical AlphaDigit = Alpha | Digit;

lexical LowAlpha = [a-z];

lexical HighAlpha = [A-Z];

lexical Digit = [0-9];

lexical Safe   = [$ \- _ . +];

lexical Extra  = [!*\'(),];

lexical National = [{}|\\^~\[\]`];

lexical Punctuation = [\<\>#%\"];

lexical Reserved = [;/?:@&=];

lexical Hex = Digit | [A-Fa-f];

lexical Escape = "%" Hex Hex;

lexical Unreserved = Alpha | Digit | Safe | Extra;

lexical Uchar = Unreserved | Escape;

lexical Xchar = Unreserved | Reserved | Escape;

