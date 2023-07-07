
@synopsis{

Character classes commonly known in the ASCII set

}
@description{
} 
module lang::std::ASCII

lexical Ascii = [\a00-\a7f];

lexical Alnum = [A-Z a-z 0-9];

lexical Alpha = [A-Z a-z];

lexical Upper = [A-Z];

lexical Lower = [a-z];

lexical Blank = [\ \t];

lexical Ctrl = [\a00-\a1f \a7f];

lexical Digit = [0-9];

lexical Graph = [~-!];

lexical HexNumber = [A-F a-f 0-9];

lexical Print = [\ ~-!];

lexical Punt = [!-/ :-@ \[-~];

lexical Space = [\t \n \a0B \a0C \r \ ];
