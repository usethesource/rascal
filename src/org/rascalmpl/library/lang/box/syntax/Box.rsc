
module lang::box::syntax::Box

start syntax Box
        = StrCon
        | BoxOperator operator "[" Box* list "]"
        | "LBL" "[" StrCon "," Box "]"
        | "REF" "[" StrCon "," Box "]"
        | "CNT" "[" StrCon "," StrCon "]"
        | "O" SOptions "[" Box BoxString Box "]"
 ;

syntax BoxOperator
        = "A" AlignmentOptions alignments SpaceOption* options
        | "R"
        | "H" SpaceOption* options
        | "V" SpaceOption* options
        | "HV" SpaceOption* options
        | "HOV" SpaceOption* options
        | "I" SpaceOption* options
        | "WD"
        | "COMM"
        | "F" FontOption* options
        | "G" GroupOption* options
        | "SL" GroupOption* options
 ;
syntax AlignmentOption
        = "l" SpaceOption* options
        | "c" SpaceOption* options
        | "r" SpaceOption* options
 ;
syntax AlignmentOptions
        =
        "(" {AlignmentOption ","}* ")"
 ;
syntax SpaceSymbol
        = "hs"
        | "vs"
        | "is"
        | "ts"
 ;
syntax SpaceOption
        =
        SpaceSymbol "=" NatCon
 ;

syntax Context
        = "H"
        | "V"
 ;
syntax FontValue
        = NatCon
        | FontId
 ;
syntax FontOption
        =
        FontParam "=" FontValue
 ;
syntax FontParam
        = "fn"
        | "fm"
        | "se"
        | "sh"
        | "sz"
        | "cl"
 ;
syntax FontOperator
        = "KW"
        | "VAR"
        | "NUM"
        | "MATH"
        | "ESC"
        | "COMM"
        | "STRING"
 ;
syntax GroupOption
        = "gs" "=" NatCon
        | "op" "=" BoxOperator
 ;
