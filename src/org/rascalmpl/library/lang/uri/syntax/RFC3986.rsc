module lang::uri::\syntax::RFC3986

start syntax Top = URI;

lexical URI = Scheme scheme ":" HierPart ("?" Query)? ("#" Fragment)?;

lexical PctEncoded = "%" [A-Fa-f0-9] [A-Fa-f0-9];

lexical Reserved = GenDelims | SubDelims;

lexical GenDelims = [:/?#\[\]@];

lexical SubDelims = [!$&\'()*+,;=];

lexical Unreserved = [A-Za-z0-9\-._~];

lexical HierPart 
  = "//" Authority PathAbEmpty
  | PathAbsolute
  | PathRootless
  | PathEmpty
  ;

lexical Scheme = [A-Za-z] [A-Za-z0-9+\-.]*;

lexical Authority = (Userinfo "@")? Host (":" Port)?;

lexical Userinfo = (Unreserved | PctEncoded | SubDelims | ":")*;

lexical Host = IPLiteral | IPv4Address | RegName;

lexical IPLiteral = "[" (IPv6addres | IPvFuture) "]";

lexical IPvFuture = "v" [A-Fa-f0-9]+ "." (Unreserved | SubDelims | ":")+;

lexical IPv6addres 
  = H16 ":" H16 ":" H16 ":" H16 ":" H16 ":" H16 ":" LS32
  | "::" H16 ":" H16 ":" H16 ":" H16 ":" H16 ":" LS32
  | H16? "::" H16 ":" H16 ":" H16 ":" H16 ":" LS32
  | (H16 ":" H16)? "::" H16 ":" H16 ":" H16 ":" LS32
  | (H16 ":" H16 ":" H16)? "::" H16 ":" H16 ":" LS32
  | (H16 ":" H16 ":" H16 ":" H16)? "::" H16 ":" LS32
  | (H16 ":" H16 ":" H16 ":" H16 ":" H16)? "::" LS32
  | (H16 ":" H16 ":" H16 ":" H16 ":" H16 ":" H16)? "::" H16 
  | (H16 ":" H16 ":" H16 ":" H16 ":" H16 ":" H16 ":" H16)? "::"  
  ;

lexical LS32 = H16 ":" H16 | IPv4Address;

lexical H16 = [A-Fa-f0-9] [A-Fa-f0-9] [A-Fa-f0-9] [A-Fa-f0-9] ;

lexical IPv4Address = DecOctet "." DecOctet "." DecOctet "." DecOctet;

lexical DecOctet 
  = [0-9]
  | [1-9] [0-9]
  | "1" [0-9] [0-9]
  | "2" [0-4] [0-9]
  | "25" [0-5]
  ;

lexical RegName = (Unreserved | PctEncoded | SubDelims)*;

lexical Port = [0-9]*;

lexical Path 
  = PathAbEmpty
  | PathAbsolute
  | PathNoScheme
  | PathRootless
  | PathEmpty
  ;

lexical PathAbEmpty = ("/" Segment)*;

lexical PathAbsolute = "/" (SegmentNz ("/" Segment)*)? ;

lexical PathNoScheme = SegmentNzNc ("/" Segment)*;

lexical PathRootless = SegmentNz ("/" Segment)*;

lexical PathEmpty = ; // 0<pchar> what does that mean ??

lexical Segment = Pchar*;

lexical SegmentNz = Pchar+;

lexical SegmentNzNc = (Unreserved | PctEncoded | SubDelims | "@")+;

lexical Pchar = Unreserved | PctEncoded | SubDelims | ":" | "@";

lexical Query = (Pchar | "/" | "?")*;

lexical Fragment = (Pchar | "/" | "?")*;


