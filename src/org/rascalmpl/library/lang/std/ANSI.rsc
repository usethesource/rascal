@synopsis{Documents a part of the ANSI standard for terminal control sequences}
module lang::std::ANSI

lexical ControlCode 
    = bell          : [\a07]
    | backspace     : [\a07]
    | tab           : [\a09]
    | lineFeed      : [\a0A]
    | formFeed      : [\a0C]
    | carriageReturn: [\a0D]
    | escape        : [\a1B]
    ;

lexical ControlSequenceIntroducer = [\a1B] [\[];

lexical GenericControlSequence = ControlSequenceIntroducer csi [0-9:;\<=\>?]+ parameters [!\"#$%&\'()*+,\-./]* intermediate [@A-Z\[\\\]^_`a-z{|}~] final;

lexical SpecialControlSequence = controlSequence: ControlSequenceIntroducer csi CSISequence code;

lexical CSISequence 
    = cursorUp                   :  [0-9]+ n [A]
	| cursorDown                 :  [0-9]+ n [B]
	| cursorForward              :  [0-9]+ n [C]
	| cursorBack                 :  [0-9]+ n [D]
	| cursorNextLine             :  [0-9]+ n [E]
	| cursorPreviousLine         :  [0-9]+ n [F]
	| cursorHorizontalAbsolute   :  [0-9]+ n [G]
	| cursorPosition             :  [0-9]+ n [;] [0-9]+ m [H]
	| cursorDown                 :  [0-9]+ n [B]
	| clearToEndOfScreen         :  [0-9]+ n [0]? [J]
	| clearToBeginningOfScreen   :  [0-9]+ n [1] [J]
	| clearEntireScreen          :  [0-9]+ n [2] [J]
	| clearEntireScreenAndBuffer :  [0-9]+ n [3] [J]
	| clearToEndOfLine           :  "0" [K]
	| clearToBeginningOfLine     :  "1" [K]
	| clearEntireLine            :  "2" [K]
	| scrollUp                   :  [0-9]+ n [S] 
	| scrollDown                 :  [0-9]+ n [T] 
	| horizontalVerticalPosition :  [0-9]+ n [;] [0-9]+ m [f]
	| selectGraphicsRendition    :  Rendition r [m]
    ;

lexical Rendition
    = reset                         : "0"
	| bold                          : "1"
	| faint                         : "2"	
	| italic                        : "3"
	| underline                     : "4"
	| slowBlink                     : "5"
	| rapidBlink                    : "6"
	| reverse                       : "7"
	| conceal                       : "8"
	| crossedOut                    : "9"
	| primaryFont                   : "10"
	| alternativeFont               : "1" [0-9]
	| fraktur                       : "20"
	| doubleUnderline               : "21"
	| normalIntensity               : "22"
	| neitherItalicNorBlackletter   : "23"
	| notUnderlined                 : "24"
	| notBlinking                   : "25"	
	| proportionalSpacing           : "26"
	| notReversed                   : "27"
	| notConcealed                  : "28"
	| notCrossedOut                 : "29"
	| setForegroundColor            : "3" [0-7]
	| setForegroundColorRGB         : "38" "5" [;] [0-9]+ color 
    | setForegroundColorRGB         : "38" "2" [;] [0-9]+ red [;] [0-9]+ green [;] [0-9]+ blue
	| setDefaultForegroundColor     : "39"
    | setBackgroundColor            : "4" [0-7]
    | setBackgroundColorRGB         : "48" "5" [;] [0-9]+ color 
    | setBackgroundColorRGB         : "48" "2" [;] [0-9]+ red [;] [0-9]+ green [;] [0-9]+ blue
	| setDefaultBackgroundColor     : "49"
	| disableProportionalSpacing    : "50"
	| framed                        : "51"
	| encircled                     : "52"
	| overlined                     : "53"
	| neitherFramedNorEncircled     : "54"
	| notOverlined                  : "55"
	| setUnderlineColor             : "58" "5" [;] [0-9]+ color
    | setUnderlineColor             : "58" "2" [;] [0-9]+ red [;] [0-9]+ green [;] [0-9]+ blue
	| defaultUnderlineColor         : "59"
	| rightsideLine                 : "60"	
	| rightsideDoubleLine           : "61"	
	| leftsideLine                  : "62"
	| leftsideDoubleLine            : "63"
	| stressMarking                 : "64"
	| resetAllLines                 : "65"
	| superscript                   : "73"	
	| subscript                     : "74"	
	| neitherSubscriptNorSuperScript: "75"
	| setBrightForegroundColor      : "9" [0-7]
	| setBrightBackgroundColor      : "10" [0-7]
    ;
